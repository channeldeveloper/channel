/*
 *  com.original.widget.plaf.OMultiSelectorUI.java
 *
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget.plaf;

import com.original.widget.OMultiSelector;
import com.original.widget.draw.OriPainter;
import com.original.widget.model.MultiSelectorModel;
import com.original.widget.model.MultiSelectorModel.ComplexChoiceDataItem;
import com.original.widget.model.MultiSelectorModel.SimpleChoiceDataItem;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;
/**
 * (Class Annotation.)
 * 多项选择器的UI
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-6-2 23:50:04
 *
 * 修改记录
 * 1. 根据最新设计修改渲染界面  2012/11/10 胡长建 @倪旻的新组件更改要求
 */
public class OMultiSelectorUI extends BasicPanelUI {
    private OMultiSelector comp;
    private MultiSelectorModel model;

    private Color clrBackGradientStart = new Color(255, 255, 255, 38);
    private Color clrBackGradientEnd = new Color(255, 255, 255, 0);

    private Color clrSelectorStart = new Color(255,255,255);
    private Color clrSelectorEnd = new Color(176,176,176);

    private Color clrComplexFillEnd = new Color(207,207,207);
    private Font fontSimpleChoice = new Font("微软雅黑", Font.PLAIN, 12);
    private Font fontComplexChoice = new Font("微软雅黑", Font.PLAIN, 16);

    private int CORNERSIZE = 4;

    private int COMPLEX_LEFT_PART_WIDTH = 62;
    private int COMPLEX_RIGHT_PART_WIDTH = 32;

    public static ComponentUI createUI(JComponent c) {
        return new OMultiSelectorUI(c);
    }

    public OMultiSelectorUI(JComponent com) {
        comp = (OMultiSelector)com;

    }
    //events
    public void procMouseClicked(MouseEvent e){
        Point pt = e.getPoint();
        if(model.getType()==MultiSelectorModel.SELECTORTYPE.SIMPLE){
            procSimpleMouseClicked(pt);
        }else
            procComplexMouseClicked(pt);
    }

     @Override
    public void paint(Graphics g, JComponent c){
        model = comp.getModel();
        if(model.getType()==MultiSelectorModel.SELECTORTYPE.SIMPLE){
            drawSimple(g, c);
        }else{
            drawComplex(g, c);
        }
        super.paint(g,c);
     }

    private Color getMixedColor(Color c1, float pct1, Color c2, float pct2) {
        float[] clr1 = c1.getComponents(null);
        float[] clr2 = c2.getComponents(null);
        for (int i = 0; i < clr1.length; i++) {
            clr1[i] = (clr1[i] * pct1) + (clr2[i] * pct2);
        }
        return new Color(clr1[0], clr1[1], clr1[2], clr1[3]);
    }
    private static final Color clrGlowInnerHi = new Color(255, 255, 255, 255);
    private static final Color clrGlowInnerLo = new Color(255, 255, 255, 255);
    private static final Color clrGlowOuterHi = new Color(255, 255, 255, 100);
    private static final Color clrGlowOuterLo = new Color(255, 255, 255, 100);

    private void paintBorderGlow(Graphics g, int glowWidth, Area area) {
        int gw = glowWidth*2;
        int height = area.getBounds().height;
        Graphics2D g2 = (Graphics2D)g;
        for (int i=gw; i >= 2; i-=2) {
            float pct = (float)(gw - i) / (gw - 1);

            Color mixHi = getMixedColor(clrGlowInnerHi, pct,
                                        clrGlowOuterHi, 1.0f - pct);
            Color mixLo = getMixedColor(clrGlowInnerLo, pct,
                                        clrGlowOuterLo, 1.0f - pct);
            g2.setPaint(new GradientPaint(0.0f, height*0.25f,  mixHi,
                                          0.0f, height, mixLo));
            //g2.setColor(Color.WHITE);

            // See my "Java 2D Trickery: Soft Clipping" entry for more
            // on why we use SRC_ATOP here
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, pct));
            g2.setStroke(new BasicStroke(i));
            g2.draw(area);
        }
    }

     ///------------------------draw complex BEGIN---------------------------
     protected void drawComplex(Graphics g, JComponent c){
         Area area = drawComplexBackground(g, c);
         drawComplexItems(g, c, area);
         paintBorderGlow(g, 3, area);
     }
     private void drawComplexItems(Graphics g, JComponent c, Area area){
         model = comp.getModel();
         Rectangle rect = area.getBounds();
         //draw left part.
        Point center = new Point(32,rect.y+rect.height/2);
        int radius = 15;
        double x = center.x - radius;
        double y = center.y - radius;

        Area outer = new Area(new Ellipse2D.Double(x, y, radius*2, radius*2));
        OriPainter.gradientFillArea(g, outer, new Color(135,135,135), new Color(66,66,66), true);
        radius = 6;
        x = center.x - radius;
        y = center.y - radius;
        outer = new Area(new Ellipse2D.Double(x, y, radius*2, radius*2));
        OriPainter.gradientFillArea(g, outer, new Color(204,212,39), new Color(168,118,33), true);
        //draw the first seperator.
        drawSeparator(g, c, area, rect.x+COMPLEX_LEFT_PART_WIDTH);
        int offsetx = rect.x+COMPLEX_LEFT_PART_WIDTH+2;

        int size = model.getChoiceSize();
        if(size<=0) return;
        int width = rect.width-COMPLEX_LEFT_PART_WIDTH-COMPLEX_RIGHT_PART_WIDTH-4;
        int height = rect.height;
        int itemSize = (width+2)/size-2;
        List<Object> choices = model.getChoices();
        for(int i=0;i<size;i++){
             ComplexChoiceDataItem item = (ComplexChoiceDataItem)choices.get(i);
             RoundRectangle2D r2d = new RoundRectangle2D.Double(offsetx,rect.y,itemSize,height,
                CORNERSIZE,
                CORNERSIZE);
             Area itmarea = new Area(r2d);
             boolean isLast = true;
             if(i<size-1)
                 isLast = false;
             if(item.isSelected()){
                 Area maskArea = getBlockArea(area, offsetx, itemSize, isLast);
                 OriPainter.gradientMiddleFillAreaEx(g, maskArea, new Color(249,232,223),
                 new Color(217,105,41), true);
                 //OriPainter.gradientFillArea(g, itmarea, clrSelectorStart, clrSelectorEnd, true);

                 OriPainter.drawStringInAreaAlign(g, itmarea, item.getDisplay(), fontComplexChoice,
                     Color.WHITE, JTextField.CENTER_ALIGNMENT, 0);
             }else
                OriPainter.drawStringInAreaAlign(g, itmarea, item.getDisplay(), fontComplexChoice,
                     Color.BLACK, JTextField.CENTER_ALIGNMENT, 0);
             offsetx+=itemSize;
             if(!isLast)
                drawSeparator(g, c, area, offsetx);
             offsetx+=2;
         }
     }
     //获取对应得到的区域
     private Area getBlockArea(Area area, int offsetx, int itemSize, boolean isLast){
         Area ret = (Area)area.clone();
         Rectangle rect = area.getBounds();
         Rectangle left = new Rectangle(rect.x, rect.y, offsetx-rect.x,
                 rect.height);
         ret.subtract(new Area(left));
         if(!isLast){
              Rectangle right = new Rectangle(offsetx+itemSize, rect.y, rect.width,
                 rect.height);
             ret.subtract(new Area(right));
         }
         return ret;
     }
     //绘制分割符号
     private void drawSeparator(Graphics g, JComponent c, Area area, int offsetx){
         Rectangle rect = area.getBounds();
         int offsety = 5;
         int height = rect.height-offsety*2;
         Rectangle separator = new Rectangle(offsetx, rect.y+offsety, 2, height);
         OriPainter.gradientMiddleFillAreaEx(g, new Area(separator), new Color(252,252,252),
                 new Color(176,176,176), true);
         OriPainter.drawLine(g, new Point(offsetx+1, rect.y+offsety),
                 new Point(offsetx+1, rect.y+offsety+height), Color.WHITE, 1.0f);
     }
     private Area drawComplexBackground(Graphics g, JComponent c){
         int shadowsize = 5;
         int width = comp.getWidth()-shadowsize*2;
         int height = comp.getHeight()-shadowsize*2;
         RoundRectangle2D r2d = new RoundRectangle2D.Double(shadowsize,1.2,width, height,
                height,
                height);
         Area area = new Area(r2d);

         OriPainter.drawDropShadow(g, area, Color.BLACK, shadowsize, shadowsize,
                 Math.PI*3/2, 1.0f);
         OriPainter.gradientMiddleFillAreaEx(g, area, Color.WHITE, clrComplexFillEnd, true);
         return area;
     }

     protected void procComplexMouseClicked(Point pt){
         //guess the potential clicked item.
         int shadowsize = 5;
         int width = comp.getWidth()-shadowsize*2;
         int height = comp.getHeight()-shadowsize*2;
         RoundRectangle2D r2d = new RoundRectangle2D.Double(shadowsize,1.2,width, height,
                height,
                height);
         Area area = new Area(r2d);
         Rectangle rect = area.getBounds();
         model = comp.getModel();
         int size = model.getChoiceSize();
         if(size<=0) return;

        int offsetx = rect.x+COMPLEX_LEFT_PART_WIDTH+2;

        width = rect.width-COMPLEX_LEFT_PART_WIDTH-COMPLEX_RIGHT_PART_WIDTH-4;
        height = rect.height;
        int itemSize = (width+2)/size-2;

         for(int i=0;i<size;i++){
             r2d = new RoundRectangle2D.Double(offsetx,0,itemSize,height,
                CORNERSIZE,
                CORNERSIZE);
             if(r2d.contains(pt)){
                model.setSelectedIndex(i);
                comp.fireSelectionChanged(i, model.getChoices().get(i));
                break;
             }
             offsetx+=itemSize;
         }
     }
     //------------------------Draw Complex END------------------------------------------------
     //------------------------Draw Simple begin------------------------------------------------
     protected void drawSimple(Graphics g, JComponent c){
         drawSimpleBackground(g, c);
         drawSimpleItems(g, c);
     }


     private void drawSimpleItems(Graphics g, JComponent c){
         model = comp.getModel();
         int size = model.getChoiceSize();
         if(size<=0) return;

         int offset = 2;
         int width = comp.getWidth()-offset*2;
         int height = comp.getHeight()-offset*2-3;
         int itemSize = width/size;
         List<Object> choices = model.getChoices();
         int offsetx = offset;
         for(int i=0;i<size;i++){
             SimpleChoiceDataItem item = (SimpleChoiceDataItem)choices.get(i);
             RoundRectangle2D r2d = new RoundRectangle2D.Double(offsetx,offset,itemSize,height,
                CORNERSIZE,
                CORNERSIZE);
             Area area = new Area(r2d);
             if(item.isSelected()){
                 OriPainter.basicDrawDropShadow(g, area, Color.BLACK,
                    2, 1, Math.PI*3/2, 0.15f);
                 OriPainter.gradientFillArea(g, area, clrSelectorStart, clrSelectorEnd, true);
                 //OriPainter.drawAreaBorderWithSingleColor(g, area,  new Color(255,255,255,120), 1.0f);

                 OriPainter.drawStringInAreaAlign(g, new Area(r2d), item.getDisplay(), fontSimpleChoice,
                     Color.BLACK, JTextField.CENTER_ALIGNMENT, 0);
             }else
                OriPainter.drawStringInAreaAlign(g, new Area(r2d), item.getDisplay(), fontSimpleChoice,
                     new  Color(181,181,181), JTextField.CENTER_ALIGNMENT, 0);
             offsetx+=itemSize;
             //OriPainter.drawAreaBorderWithSingleColor(g, area, clrSelectorEnd, 1);
         }
     }
     /**
      * 绘制其背景
      * @param g
      * @param c
      */
     private void drawSimpleBackground(Graphics g, JComponent c){
         int width = comp.getWidth();
         int height = comp.getHeight();

         RoundRectangle2D r2d = new RoundRectangle2D.Double(CORNERSIZE/2,1,
                width-CORNERSIZE,height-CORNERSIZE-1,
                CORNERSIZE,
                CORNERSIZE);
        Area area = new Area(r2d);
        OriPainter.basicDrawDropShadow(g, area, Color.BLACK,
                2, 1, Math.PI*3/2, 0.4f);
        OriPainter.fillAreaWithSingleColor(g, area, new Color(0,0,0,(int)(0.7*255)));//
        OriPainter.gradientFillArea(g, area, clrBackGradientStart,
                    clrBackGradientEnd, true);
        OriPainter.drawAreaBorderWithSingleColor(g, area,
                new Color(255,255,255,102), 1.0f);
     }
     /**
      * 处理鼠标点击事件（简单选择器）
      * @param pt
      */
     protected void procSimpleMouseClicked(Point pt){
         //guess the potential clicked item.
         model = comp.getModel();
         int size = model.getChoiceSize();
         if(size<=0) return;

         int offset = 2;
         int width = comp.getWidth()-offset*2;
         int height = comp.getHeight()-offset*2;
         int itemSize = width/size;
         int offsetx = offset;
         for(int i=0;i<size;i++){
             RoundRectangle2D r2d = new RoundRectangle2D.Double(offsetx,offset,itemSize,height,
                CORNERSIZE,
                CORNERSIZE);
             if(r2d.contains(pt)){
                model.setSelectedIndex(i);
                comp.fireSelectionChanged(i, model.getChoices().get(i));
                break;
             }
             offsetx+=itemSize;
         }
     }
     //------------------------Draw Simple END ------------------------------------------------

}
