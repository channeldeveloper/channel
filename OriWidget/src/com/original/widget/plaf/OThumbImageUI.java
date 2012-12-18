/*
 *  com.original.widget.plaf.OThumbImageUI.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget.plaf;

import com.original.widget.OThumbImage;
import com.original.widget.datadef.OriCircle;
import com.original.widget.draw.GeomOperator;
import com.original.widget.draw.OriPainter;
import com.original.widget.model.ByteBlockModel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;

/**
 * (Class Annotation.)
 * 修改记录
 *   1. 修正了老杨提出的静态对象应用的Bug，就是多个组件互相干涉现象 2012-05-27
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   May 6, 2012 4:41:56 PM
 */
public class OThumbImageUI extends BasicPanelUI {
    // Shared UI object
    //private static OThumbImageUI cui;
    private OThumbImage comp;
    private ByteBlockModel model;

    //这些都可以放到模型中，但是这次简单处理了。
    private Color clrBorder = new Color(204, 204, 204,76);
    private Color clrShadowS = new Color(220,220,220, 76);
    private Color clrShadowE = new Color(161,161,161, 76);

    //缩微图绘制区域
    private Rectangle imgRect = null;
    private int shadowsize = 10;

    public static ComponentUI createUI(JComponent c) {
        return new OThumbImageUI(c);
    }

    public OThumbImageUI(JComponent com) {
        comp = (OThumbImage)com;
    	//model = comp.getModel();
    }

    

     @Override
    public void paint(Graphics g, JComponent c){
         model = comp.getModel();
       
        int width = comp.getWidth();
        int height = comp.getHeight();
        //draw the frame.
        drawBackground(g, width, height);
        try {
            //draw the triangle.
            drawPictureArea(g, width, height, model);
            //draw the button if necessary.
            //drawButton(g);
        } catch (IOException ex) {
        }
        

        super.paint(g,c);
     }
     
     /**
      * 绘制头像区域
      * @param g
      * @param width
      * @param height
      * @throws IOException
      */

     protected void drawPictureArea(Graphics g, int width, int height,
             ByteBlockModel model)
             throws IOException{
         int offset = 1;
         if(imgRect==null){
             imgRect = new Rectangle();
             imgRect.setBounds(offset, offset,
                        width - offset * 2 , height - offset * 2-shadowsize);
         }
         boolean hasImg = model.isHasdata();
         //Fill the background.
         Rectangle2D bkRect = new Rectangle2D.Double(offset, offset,
                 width-offset*2, height-offset*2-shadowsize);
         Area bkArea = new Area(bkRect);
         //OriPainter.fillAreaWithSingleColor(g, bkArea, Color.WHITE);
         //OriPainter.drawAreaBorderWithSingleColor(g, bkArea, clrBorder, 1);
         BufferedImage headImg = null;
         if(hasImg){
            //headImg = ImageIO.read(comp.getClass().getResource("images/no_sel.png"));
         //else{ //draw real Pictures.
            InputStream in = new ByteArrayInputStream(model.getDatablock());
            headImg = ImageIO.read(in);
            OriPainter.drawFitImage(g, headImg, bkArea);
         }

         
     }
     /**
      * 绘制头像输入的边框等信息
      * @param g
      * @param width
      * @param height
      */
    protected void drawBackground(Graphics g, int width, int height){
        int corner = 4;
        //draw basic shadow.
        RoundRectangle2D shadowRect = new RoundRectangle2D.Double(1, 1, 
                width-1, height-shadowsize-1,corner,corner);
        Area shadowArea = new Area(shadowRect);
        GeomOperator.offset(shadowArea, -1, -1);

        Area ashadowArea = genAdditionalShadowArea(width, height);
        shadowArea.add(ashadowArea);



        BufferedImage shadow = OriPainter.drawGradientBoxShadow(g,
                shadowArea, clrShadowS, clrShadowE);
        OriPainter.drawImage(g, shadow, 0, 0);
        

        //draw the outer frame.
        RoundRectangle2D r2d = new RoundRectangle2D.Double(0,0, width-2, height-14,
                corner,corner);
        Area outer = new Area(r2d);
        OriPainter.fillAreaWithSingleColor(g, outer, Color.WHITE);
        OriPainter.drawAreaBorderWithSingleColor(g, outer, clrBorder, 1);
    }
    /**
     * 计算座子
     * @param width
     * @param height
     * @return
     */
    private Area genAdditionalShadowArea(int width, int height){
        GeneralPath shape = new GeneralPath();
        double x_control = 15.4;
        double x_control2 = width-15.4;
        
        shape.moveTo(1, height-shadowsize-1);
        shape.curveTo(1, height-shadowsize, x_control,
                height, x_control2, height);
        shape.lineTo(width-x_control2, height);
        shape.curveTo(width-x_control2, height,
                width-x_control, height,
                width, height-shadowsize-1);

        shape.lineTo(1, height-shadowsize-1);
        shape.closePath();
        Area base =  new Area(shape);

        double shadowheight = 3;
        Point2D pt1 = new Point2D.Double(x_control, height-shadowsize+shadowheight);
        Point2D pt2 = new Point2D.Double(width/2, height-shadowsize);
        Point2D pt3 = new Point2D.Double(x_control2, height-shadowsize+shadowheight);
        OriCircle ocircle = GeomOperator.findCenterRadius(pt1, pt2, pt3);

        
        double radius = ocircle.getRadius();
        double x = ocircle.getCenter().getX()-radius;
        double y = ocircle.getCenter().getY() - radius;
        Ellipse2D  circle = new Ellipse2D.Double(x, y, radius*2,
                radius*2);
        base.subtract(new Area(circle));
        return base;
    }
}
