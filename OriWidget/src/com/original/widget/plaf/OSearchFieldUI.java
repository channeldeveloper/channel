/*
 *  com.original.widget.plaf.OSearchFieldUI.java
 *
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget.plaf;

import com.original.widget.OSearchField;
import com.original.widget.draw.GeomOperator;
import com.original.widget.draw.OriPainter;
import com.original.widget.model.TextBlockModel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextFieldUI;

/**
 * (Class Annotation.)
 *修改记录
 * 1. 修正UI干涉现象
 * 
 * @author   Administrator
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-5-21 21:58:34
 */
public class OSearchFieldUI extends BasicTextFieldUI{
    // Shared UI object
    public static int CORNERRADIUS = 15;

    private  OSearchField comp;
    private  TextBlockModel model;

    private Area m_searchIcon = null;
    private boolean m_overSearchIcon = false;
    private Rectangle _rectClearButton = null;
    private boolean _mouseRolloverClearButton = false;
    public static ComponentUI createUI(JComponent c) {
        return new OSearchFieldUI(c);
    }

    public OSearchFieldUI(JComponent com) {
        comp = (OSearchField)com;
    	model = comp.getModel();
    }

    public void redraw() {
        if (model == null){
			model = comp.getModel();
		}
		model.setSize(comp.getHeight(), comp.getWidth());
    }

    @Override
    public void update(Graphics g, JComponent c){
        if (model == null){
			model = comp.getModel();
		}
        int width = comp.getWidth();
        int height = comp.getHeight();

        //圆角长方形
        RoundRectangle2D r2d = new RoundRectangle2D.Double(
                TextBlockModel.CORNERRADIUS/2,
                TextBlockModel.CORNERRADIUS/2,
                width-TextBlockModel.CORNERRADIUS,
                height-TextBlockModel.CORNERRADIUS,
                CORNERRADIUS*2,
                CORNERRADIUS*2);
        //Draw the background frame.
        OriPainter.fillAreaWithSingleColor(g, new Area(r2d),
                model.getBackgroundcolor());
        OriPainter.drawAreaBorderWithSingleColor(g, new Area(r2d),
                TextBlockModel.BORDERCOLOR, 1);

        //draw the inner Shadow.
        //calculate the shadow area.
        Area areaOne = new Area(r2d);
        Area areaTwo = GeomOperator.offsetCopy(areaOne, 0, 2.4);
        areaOne.subtract(areaTwo); //areaOne will be the shadow area.
        //generate the shadow image.
        BufferedImage shadow = OriPainter.drawGradientInnerShadow(g,
                areaOne, TextBlockModel.SHADOWCOLOR, model.getBackgroundcolor(),
                40);
        //paint the shadow.
        OriPainter.drawImage(g, shadow, 0, 0);
        shadow = null;

        drawMagicIcon(g, c);


        // 画提示背景
        String val = comp.getText();
        String caption = model.getCaption();
        if ((val== null || val.length() == 0 ) && caption!= null && caption.length()>0){
            Insets bound = comp.getInsets();
            int x = bound.left+2;
            int y = bound.top+4;
            int w = comp.getWidth() - bound.left - bound.right - 4;
            int h = comp.getHeight() - bound.top - bound.bottom - 4;
            OriPainter.drawString(g,model.getFont(), new Area(new Rectangle(x,y,w,h)), caption, TextBlockModel.SHADOWCOLOR);
        }
        if(val.length()>0)
            drawClearButton(g, c);
        super.update(g,c);

     }
    //绘制清理Button
     private void drawClearButton(Graphics g, JComponent c){
         Rectangle rect = comp.getBounds();
         //计算绘制叉的区域的左定点
         int radius = 12;
         int x = rect.width-TextBlockModel.CORNERRADIUS-radius-6;
         int offset = 2;
         int y = (rect.height-radius)/2;
         //int width = 16;
         _rectClearButton = new Rectangle(x, 0, radius, rect.height );
         GeneralPath path = new GeneralPath();
         path.moveTo(x+offset, y+offset);
         path.lineTo(x+radius-offset, y+radius-offset);
         path.moveTo(x+radius-offset, y+offset);
         path.lineTo(x+offset, y+radius-offset);

         Graphics2D g2d = (Graphics2D)  g;
         if(_mouseRolloverClearButton)
            g2d.setPaint(new Color(69,160,249));
         else
            g2d.setPaint(new Color(144,144,144));
         g2d.setStroke(new BasicStroke(3.2f,                     // Line width
                            BasicStroke.CAP_ROUND,    // End-cap style
                            BasicStroke.JOIN_ROUND));
         g2d.draw(path);
     }
     
    //绘制查询图标
     private void drawMagicIcon(Graphics g, JComponent c){
         //draw the magic
        Ellipse2D circle = new Ellipse2D.Double(CORNERRADIUS+2, CORNERRADIUS/2+6,
                12,12);
        Point pt1 = new Point(CORNERRADIUS + (int)( 12*Math.cos(Math.PI/4))
                , CORNERRADIUS/2+6 + (int)( 12*Math.sin(Math.PI/4)));
        Point pt2 = new Point(pt1.x - (int)( 12*Math.cos(Math.PI/4)),
                pt1.y +  (int)( 12*Math.sin(Math.PI/4)));

        Color clr = null;
        clr = new Color(152,152,152);

        OriPainter.drawLine(g, pt1, pt2, clr, 2.8f);
        OriPainter.radialGradidentFillArea(g, new Area(circle),
            new Point2D.Double(CORNERRADIUS+2,
            CORNERRADIUS/2+10), 12,
            new float[]{0.0f, 0.4f,1.0f},
            new Color[]{Color.WHITE, Color.WHITE, clr});
        OriPainter.drawAreaBorderWithSingleColor(g, new Area(circle), clr, 1.8f);        
     }

    /**
     * 事件处理
     */
     //事件处理
     public void procMouseExit(){
        if(_rectClearButton==null) return;
        _mouseRolloverClearButton = false;
        comp.repaint(_rectClearButton.getBounds());
    }
    public void procMouseClick(MouseEvent e){
        if(_rectClearButton==null) return;
        Point pt = e.getPoint();
        if(_rectClearButton.contains(pt)){
            comp.setText("");
            comp.repaint();
        }
    }
    public void procMouseMoved(MouseEvent e){
        if(_rectClearButton==null) return;
        Point pt = e.getPoint();
        if(_rectClearButton.contains(pt)){
            comp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            _mouseRolloverClearButton = true;
        }else{
            comp.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
            _mouseRolloverClearButton = false;
        }
        comp.repaint(_rectClearButton.getBounds());
    }

}
