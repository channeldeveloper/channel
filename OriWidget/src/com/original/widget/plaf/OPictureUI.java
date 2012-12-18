/*
 *  com.original.widget.comp.view.OTextFieldUI.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget.plaf;

import com.original.widget.OImageLoad;
import com.original.widget.OPicture;
import com.original.widget.datadef.OriCircle;
import com.original.widget.draw.GeomOperator;
import com.original.widget.draw.OriPainter;
import com.original.widget.model.ByteBlockModel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

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
 * 基本文本编辑组件
 *  修改记录
 *   1. 修正了老杨提出的静态对象应用的Bug，就是多个组件互相干涉现象 2012-05-27
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   May 4, 2012 2:33:56 PM
 */
public class OPictureUI extends BasicPanelUI   {
    // Shared UI object
    //private static OPictureUI cui;
    private OPicture comp;
    private ByteBlockModel model;

    //双缓存
    private BufferedImage dblCache = null;
    //Browse按钮区域
    private Rectangle btnRect = null;
    //头像绘制区域
    private Rectangle imgRect = null;

    //这些都可以放到模型中，但是这次简单处理了。
    private Color clrBorder = new Color(246, 246, 246, 76);
    private Color clrFrame = Color.white;
    private Color clrBkGraColorS = new Color(248, 246, 247);
    private Color clrBkGraColorE = new Color(219, 217, 218);
    private Color clrBrwBtnHolderColor = new Color(228,228,228);
    private Color clrBrwBtnHolderBorder = new Color(192,192,192);

    private Color clrBrwBtnBkColorS = new Color(230,230,230,255);
    private Color clrBrwBtnBkColorE = new Color(210,210,210,255);
    
    private Color clrShadowS = new Color(220,220,220, 76);
    private Color clrShadowE = new Color(161,161,161, 75);

    private int shadowsize = 10;
    public static ComponentUI createUI(JComponent c) {
        return new OPictureUI(c);
    }
    
    public OPictureUI(JComponent com) {
        comp = (OPicture)com;
    	//model = comp.getModel();
    }

    public Rectangle getBtnRect() {
        if(btnRect==null){
            btnRect =  new Rectangle();
            
        }
        return btnRect;
    }

    public void setBtnRect(Rectangle btnRect) {
        this.btnRect = btnRect;
    }

    public BufferedImage getDblCache() {
        return dblCache;
    }

    public void setDblCache(BufferedImage dblCache) {
        this.dblCache = dblCache;
    }

    public Rectangle getImgRect() {
        return imgRect;
    }

    public void setImgRect(Rectangle imgRect) {
        this.imgRect = imgRect;
    }

    /*public void redraw() {
        if (model == null){
			model = comp.getModel();
		}
		model.setSize(comp.getHeight(), comp.getWidth());        
    }*/

    @Override
    public void paint(Graphics g, JComponent c){
        model = comp.getModel();
        
        int width = model.getWidth();
        int height = model.getHeight();
        //draw the frame.
        drawBackground(g, width, height);
        try {
            //draw the triangle.
            drawPictureArea(g, width, height, comp, model);
            //draw the button if necessary.
            //drawButton(g);
        } catch (IOException ex) {
        }
        //draw the button if necessary.
        if(model.isMouseOver() || !model.isHasdata())
            drawButton(g, width, height, model);
       
        super.paint(g,c);
     }
     /**
      * 根据需要绘制按钮（该按钮是模拟按钮）
      * @param g
      * @param widthx
      * @param heightx
      */
     protected void drawButton(Graphics g, int width, int height,
            ByteBlockModel model ){
        //按钮大小
        int btnWidth = 110;
        int btnHeight = 34;
         //draw a button.
        int x = (width-btnWidth)/2;
        int y = (height-34-btnHeight);
        if(btnRect==null || btnRect.isEmpty())
            btnRect = new Rectangle(x, y, btnWidth, btnHeight);

        RoundRectangle2D r2d = new RoundRectangle2D.Double(
                btnRect.x,
                btnRect.y,
                btnRect.width, btnRect.height,
                4,
                4);
        Area areaButton = new Area(r2d);
        boolean mouseOverBtn = model.isMouseOverBtn();

        if(!mouseOverBtn){
            OriPainter.drawButtonFrame(g, areaButton, this.clrBrwBtnHolderColor,
                    this.clrBrwBtnHolderBorder,
                    this.clrBkGraColorS, this.clrBrwBtnBkColorE, 2);
       }
         else{
               OriPainter.drawButtonFrame(g, areaButton, this.clrBrwBtnHolderColor,
                       this.clrBrwBtnHolderBorder,
                    this.clrBkGraColorE, this.clrBrwBtnBkColorS, 2);
         }
         OriPainter.drawStringInAreaCenter(g, areaButton,
                 model.getBrwbtntext(), model.getBtnBrwFont(), Color.BLACK);
        
     }
     /**
      * 绘制头像区域
      * @param g
      * @param width
      * @param height
      * @throws IOException
      */

     protected void drawPictureArea(Graphics g, int width, int height,
             OPicture comp, ByteBlockModel model)
             throws IOException{
         int offset = 11;
         if(imgRect==null){
             imgRect = new Rectangle();
             imgRect.setBounds(offset, offset,
                        width - offset * 2 , height - offset * 2);
         }
         boolean hasImg = model.isHasdata();
         //Fill the background.
         Rectangle2D bkRect = new Rectangle2D.Double(offset+2, offset,
                 width-offset*2-4, height-offset*2-13);
         Area bkArea = new Area(bkRect);
         OriPainter.gradientFillArea(g, bkArea,
                     clrBkGraColorS, clrBkGraColorE, true);
         OriPainter.drawAreaBorderWithSingleColor(g, bkArea, clrBorder, 1);
         BufferedImage headImg = null;
         if(!hasImg)
            headImg = (BufferedImage)OImageLoad.getImageRel("images/no_sel.png");
         else{ //draw real Pictures.
            InputStream in = new ByteArrayInputStream(model.getDatablock());
            headImg = ImageIO.read(in);
         }
         OriPainter.drawFitImage(g, headImg, bkArea);
     }
     /**
      * 绘制头像输入的边框等信息
      * @param g
      * @param width
      * @param height
      */
    protected void drawBackground(Graphics g, int width, int height){
        //draw basic shadow.
        Rectangle2D shadowRect = new Rectangle2D.Double(1, 0, width-2, height-12);
        Area shadowArea = new Area(shadowRect);
        GeomOperator.offset(shadowArea, 0, 2);
       
        Area ashadowArea = genAdditionalShadowArea(width, height);
        shadowArea.add(ashadowArea);
        
        

        BufferedImage shadow = OriPainter.drawGradientBoxShadow(g,
                shadowArea, clrShadowS, clrShadowE);
        OriPainter.drawImage(g, shadow,1, 0);
       
        //draw the outer frame.
        RoundRectangle2D r2d = new RoundRectangle2D.Double(2,0, width-4, height-14, 4,4);
        Area outer = new Area(r2d);
        OriPainter.drawAreaBorderWithSingleColor(g, outer, clrBorder,1);
        //draw the inner frame.
        Area inner = GeomOperator.shrinkCopy(new Area(outer), 10);
        GeomOperator.offset(inner, 10, 10);
        outer.subtract(inner);
        OriPainter.fillAreaWithSingleColor(g, outer, clrFrame);
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
        shape.moveTo(1, height-14);
        shape.curveTo(1, height-10, x_control,
                height, x_control2, height);
        shape.lineTo(width-x_control2, height);
        shape.curveTo(width-x_control2, height,
                width-x_control, height,
                width, height-12);
        
        shape.lineTo(1, height-14);
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
        
        //double radius = 1.0*height*18.1;
        //Ellipse2D  circle = new Ellipse2D.Double(-radius/2+width/2, height-10, radius,
        //        radius*0.9);
        //base.subtract(new Area(circle));
        return base;
    }
}
