/*
 *  com.original.widget.comp.view.OTextFieldUI.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget.plaf;

import com.original.widget.ORichLabel;
import com.original.widget.draw.GeomOperator;
import com.original.widget.draw.OriPainter;
import com.original.widget.model.RichLabelModel;
import com.original.widget.model.OBaseModel;
import java.awt.Graphics;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLabelUI;

/**
 * (Class Annotation.)
 *
 * @author   yangkj
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-5-21 14:01:07
 */
public class ORichLabelUI extends BasicLabelUI {

    private ORichLabel comp;
    private RichLabelModel model;

    public static ComponentUI createUI(JComponent c) {
        return new ORichLabelUI(c);
    }

    public ORichLabelUI(JComponent com) {
        comp = (ORichLabel) com;
        model = comp.getModel();
    }

    public void redraw() {
        if (model == null) {
            model = comp.getModel();
        }
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        if (model == null) {
            model = comp.getModel();
        }
        int width = c.getWidth();
        int height = c.getHeight();
        //圆角长方形
        RoundRectangle2D r2d = new RoundRectangle2D.Double(
                OBaseModel.CORNERRADIUS/2,
                OBaseModel.CORNERRADIUS/2,
                width - OBaseModel.CORNERRADIUS,
                height - OBaseModel.CORNERRADIUS,
                OBaseModel.CORNERRADIUS,
                OBaseModel.CORNERRADIUS);

        //Draw the background frame.
        OriPainter.fillAreaWithSingleColor(g, new Area(r2d),
                model.getBackgroundcolor());
        OriPainter.drawAreaBorderWithSingleColor(g, new Area(r2d),
                OBaseModel.BORDERCOLOR, 1);

        //draw the inner Shadow.
        //calculate the shadow area.
        Area areaOne = new Area(r2d);
        Area areaTwo = GeomOperator.offsetCopy(areaOne, 0, 2.4);
        areaOne.subtract(areaTwo); //areaOne will be the shadow area.
        //generate the shadow image.
        BufferedImage shadow = OriPainter.drawGradientInnerShadow(g,
                areaOne, OBaseModel.SHADOWCOLOR, model.getBackgroundcolor(),
                40);
        //paint the shadow.
        OriPainter.drawImage(g, shadow, 0, 0);
        super.paint(g, c);
    }
}
