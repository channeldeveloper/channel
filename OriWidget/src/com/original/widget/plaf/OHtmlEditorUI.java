/*
 *  com.original.widget.comp.view.OTextFieldUI.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget.plaf;

import com.original.widget.OHtmlEditor;
import com.original.widget.draw.GeomOperator;
import com.original.widget.draw.OriPainter;
import com.original.widget.model.TextBlockModel;
import java.awt.Graphics;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;

/**
 * (Class Annotation.)
 * 基本文本编辑组件
 * 修改记录
 *   1. 修正了老杨提出的静态对象应用的Bug，就是多个组件互相干涉现象 2012-05-28
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   May 4, 2012 2:33:56 PM
 */
public class OHtmlEditorUI extends BasicPanelUI   {
    // Shared UI object
    //private static OHtmlEditorUI cui;
    private OHtmlEditor comp;
    private TextBlockModel model;

    public static ComponentUI createUI(JComponent c) {
        return new OHtmlEditorUI(c);
    }
    
    public OHtmlEditorUI(JComponent com) {
        comp = (OHtmlEditor)com;
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
                TextBlockModel.CORNERRADIUS,
                TextBlockModel.CORNERRADIUS);
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
        super.update(g,c);
     }
     
}
