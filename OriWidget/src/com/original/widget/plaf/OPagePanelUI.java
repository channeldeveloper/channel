/*
 *  com.original.widget.plaf.OPagePanelUI.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget.plaf;

import com.original.widget.OPagePanel;
import com.original.widget.draw.OriPainter;
import com.original.widget.model.PagePanelModel;
import com.original.widget.model.PagePanelModel.PagePanelBorder;
import com.original.widget.model.PagePanelModel.PagePanelShadow;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;

/**
 * (Class Annotation.)
 *
 * @author   Administrator
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-6-26 22:19:44
 */
public class OPagePanelUI extends BasicPanelUI {
    private OPagePanel comp;
    
    public static ComponentUI createUI(JComponent c) {
        return new OPagePanelUI(c);
    }

    public OPagePanelUI(JComponent com) {
        comp = (OPagePanel) com;
    }

    @Override
    public void update(Graphics g, JComponent c){
        PagePanelModel model = comp.getModel();
        //绘制背景
        drawBackground(g, model);
        //绘制阴影
        drawShadow(g, model);
        //绘制边框
        drawBorder(g, model);
    }

    //绘制边框
    protected void drawBorder(Graphics g, PagePanelModel model){
        List<PagePanelBorder> borders = model.getBorders();
        
        for(PagePanelBorder border: borders){
            Point2D pt1 = null, pt2 = null;
            switch(border.direction){
                case 0: //left;
                    pt1 = border.inner==true?new Point2D.Double(2,0):new Point2D.Double(1,0);
                    pt2 = border.inner==true?new Point2D.Double(2,comp.getHeight())
                            :new Point2D.Double(1,comp.getHeight());
                    break;

                case 1: //right
                    pt1 = border.inner==true?new Point2D.Double(comp.getWidth()-2,0):new Point2D.Double(comp.getWidth()-1,0);
                    pt2 = border.inner==true?new Point2D.Double(comp.getWidth()-2,comp.getHeight())
                            :new Point2D.Double(comp.getWidth()-1,comp.getHeight());
                    break;
                case 2: //top
                    pt1 = border.inner==true?new Point2D.Double(0,2):new Point2D.Double(0,1);
                    pt2 = border.inner==true?new Point2D.Double(comp.getWidth(),2)
                            :new Point2D.Double(comp.getWidth(),1);
                    break;
                case 3:
                    pt1 = border.inner==true?new Point2D.Double(0,comp.getHeight()-2):new Point2D.Double(0,comp.getHeight()-1);
                    pt2 = border.inner==true?new Point2D.Double(comp.getWidth(),comp.getHeight()-2)
                            :new Point2D.Double(comp.getWidth(),comp.getHeight()-1);
                    break;
            }
            OriPainter.drawLine(g, pt1, pt2, border.linecolor, (float)border.linewidth);
        }
    }
    //绘制阴影
    protected void drawShadow(Graphics g, PagePanelModel model){
        List<PagePanelShadow> shadows =model.getShadows();
        Rectangle2D r = null;
        for(PagePanelShadow shadow: shadows){
            switch(shadow.direction){ //left
                case 0:
                    r = new Rectangle2D.Double(0, 0, shadow.size/2, comp.getHeight());
                    break;
                case 1:
                    r = new Rectangle2D.Double(comp.getWidth()-1, 0, 1, comp.getHeight());
                    break;
                case 2:
                    r = new Rectangle2D.Double(0, 0, comp.getWidth(), 1);
                    break;
                case 3:
                    r = new Rectangle2D.Double(0, comp.getHeight()-1, comp.getWidth(), 1);
                    break;
            }
            Area area = new Area(r);
            OriPainter.basicDrawDropShadow(g, area,shadow.shadowcolor, shadow.size,
                    0, shadow.angle, shadow.opacity);
        }
    }
    //绘制背景
    protected void drawBackground(Graphics g, PagePanelModel model){
        
        Color[] colors = model.getBackFillColors();
        Float[] divids = model.getBackFillDivid();

        if(colors.length==1){
            Rectangle2D r = new Rectangle2D.Double(0, 0, comp.getWidth(), comp.getHeight());
            OriPainter.fillAreaWithSingleColor(g, new Area(r), colors[0]);
            return;
        }
        int ind = 0;
        int offsety = 0;
        for(int i=0;i<colors.length-1;i++){
            Rectangle2D r = new Rectangle2D.Double(0, offsety, comp.getWidth(), comp.getHeight()*divids[ind]);
            Area area = new Area(r);
            OriPainter.gradientFillArea(g, area, colors[i], colors[i+1], true);
            offsety+=comp.getHeight()*divids[ind];
            ind++;
        }
    }
}
