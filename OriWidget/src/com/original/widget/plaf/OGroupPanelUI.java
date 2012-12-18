/*
 *  com.original.widget.plaf.OImagePanelUI.java
 *  
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget.plaf;

import com.original.widget.OGroupPanel;
import com.original.widget.model.GroupPanelModel;
import java.awt.*;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;

/**
 * (Class Annotation.)
 *
 * @author   yangkj
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-5-14 13:49:43
 */
public class OGroupPanelUI extends BasicPanelUI {

    private OGroupPanel comp;
    private GroupPanelModel model;
    final Polygon polygon = new Polygon();
    private Color lineColor = Color.red, panelColor = Color.blue;

    public static ComponentUI createUI(JComponent c) {
        return new OGroupPanelUI(c);
    }

    public OGroupPanelUI(JComponent com) {
        comp = (OGroupPanel) com;
        model = comp.getModel();
    }

    public void redraw() {
        if (model == null) {
            model = comp.getModel();
        }
        model.setSize(comp.getHeight(), comp.getWidth());
    }

    @Override
    public void paint(Graphics g, JComponent c) {
//        if (model == null) {
//            model = comp.getModel();
//        }
//        Graphics2D g2d = (Graphics2D) g;
//
//        RoundRectangle2D r2d = new RoundRectangle2D.Double(
//                0,
//                0,
//                model.getWidth(),
//                model.getHeight(),
//                0,
//                0);
//
//        if (model.hasSelected()) {
//            //Draw the background frame.
//            OriPainter.fillAreaWithSingleColor(g, new Area(r2d),
//                    (model.isSelected() ? model.getNotSelectedColor() : model.getBackgroundcolor()));
//            if (model.isSelected()) {
//                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                        RenderingHints.VALUE_ANTIALIAS_ON);
//                calcArrowRect();
//
//                Color oldColor = g2d.getColor();
//                g2d.setColor(panelColor);
//                g2d.fill(polygon);
//                g2d.setColor(lineColor);
//                g2d.draw(polygon);
//                g2d.setColor(oldColor);
//            } else {
//                r2d = new RoundRectangle2D.Double(
//                        0,
//                        0,
//                        10,
//                        model.getHeight(),
//                        0,
//                        0);
//                OriPainter.fillAreaWithSingleColor(g, new Area(r2d), model.getNotSelectedColor());
//            }
//        } else{
//            //Draw the background frame.
//            OriPainter.fillAreaWithSingleColor(g, new Area(r2d),   model.getBackgroundcolor());
//        }
        super.paint(g, c);
    }

    /**
     * 布局计算箭头的大小位置。
     */
    public void calcArrowRect() {

        if (model == null) {
            model = comp.getModel();
        }
        lineColor = model.getSelectedColor();
        panelColor = model.getSelectedColor();
        Rectangle rect = (Rectangle) comp.getBounds().clone();
        rect.x = 0;
        rect.y = 0;
        rect.width = 16;
        rect.height = comp.getHeight() - 1;
        int thickness = 6;
        int arrowPos = comp.getHeight() / 4;
        int arrowWidth = 10;
        polygon.reset();

        rect.width -= thickness;
        if (arrowPos > rect.height) {
            arrowPos = rect.height;
        }

        polygon.addPoint(rect.x + rect.width, rect.y);

        if (arrowPos > (arrowWidth / 2)) {
            polygon.addPoint(rect.x + rect.width, rect.y + arrowPos
                    - arrowWidth / 2);
        }
        polygon.addPoint(rect.x + rect.width + thickness, rect.y + arrowPos);

        if ((arrowPos + (arrowWidth) / 2) < rect.height) {
            polygon.addPoint(rect.x + rect.width, rect.y + arrowPos
                    + arrowWidth / 2);
        }

        polygon.addPoint(rect.x + rect.width, rect.y + rect.height);
        polygon.addPoint(rect.x, rect.y + rect.height);
        polygon.addPoint(rect.x, rect.y);
    }
}
