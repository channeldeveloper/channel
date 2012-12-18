/**
 *  com.original.widget.comp.arrowedpanel.view;
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget.plaf;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;

import com.original.widget.OArrowedPanel;
import com.original.widget.model.ArrowModel;

/**
 * 频道组件GUI。
 *
 *
 * @author Ni Min,Song Xueyong
 * @version 1.00 2012/4/23
 */
public class OArrowedPanelUI extends BasicPanelUI {

//    // Shared UI object
//    private static OArrowedPanelUI arrowedPanelUI;
    private OArrowedPanel arrowedPanel;
    private ArrowModel model;

    public static ComponentUI createUI(JComponent c) {
//        if (arrowedPanelUI == null) {
//            arrowedPanelUI = new OArrowedPanelUI(c);
//        }
//        return arrowedPanelUI;
        return new OArrowedPanelUI(c);
    }

    public OArrowedPanelUI(JComponent com) {
        arrowedPanel = (OArrowedPanel) com;
        model = arrowedPanel.getModel();
    }
    private Color lineColor = Color.red, panelColor = Color.blue;//lightGray;

    /**
     * onPaint() 面板重绘事件
     *
     */
    private void onPaint() {
    }
    final Polygon polygon = new Polygon();

    /**
     * 布局计算箭头的大小位置。
     */
    public void calcArrowRect() {

        if (model == null) {
            model = arrowedPanel.getModel();
        }
        lineColor = model.getLineColor();
        panelColor = model.getPanelColor();
        //get from model

        Rectangle rect = (Rectangle) arrowedPanel.getBounds().clone();
        rect.x = 0;
        rect.y = 0;
        rect.width = arrowedPanel.getWidth() - 1;
        rect.height = arrowedPanel.getHeight() - 1;
        int thickness = model.getThickness();
        String arrowDirection = model.getArrowDirection();
        int arrowPos = model.getArrowPos();
        int arrowWidth = model.getArrowWidth();
//		Color lineColor=Color.red,panelColor=Color.blue;



        polygon.reset();

        if (arrowDirection.equalsIgnoreCase(BorderLayout.WEST)) {
            rect.x += thickness;
            rect.width -= thickness;

            if (arrowPos > rect.height) {
                arrowPos = rect.height;
            }

            polygon.addPoint(rect.x, rect.y);

            if (arrowPos > (arrowWidth / 2)) {
                polygon.addPoint(rect.x, rect.y + arrowPos - arrowWidth / 2);
            }

            polygon.addPoint(rect.x - thickness, rect.y + arrowPos);

            if ((arrowPos + (arrowWidth) / 2) < rect.height) {
                polygon.addPoint(rect.x, rect.y + arrowPos + arrowWidth / 2);
            }

            polygon.addPoint(rect.x, rect.y + rect.height);
            polygon.addPoint(rect.x + rect.width, rect.y + rect.height);
            polygon.addPoint(rect.x + rect.width, rect.y);

        } else if (arrowDirection.equalsIgnoreCase(BorderLayout.EAST)) {
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

        } else if (arrowDirection.equalsIgnoreCase(BorderLayout.NORTH)) {
            rect.y += thickness;
            rect.height -= thickness;

            if (arrowPos > rect.width) {
                arrowPos = rect.width;
            }

            polygon.addPoint(rect.x, rect.y);

            if (arrowPos > (arrowWidth / 2)) {
                polygon.addPoint(rect.x + arrowPos - arrowWidth / 2, rect.y);
                polygon.addPoint(rect.x + arrowPos - arrowWidth / 2, rect.y
                        - thickness);
            } else {
                polygon.addPoint(rect.x + arrowPos, rect.y - thickness);
            }

            if ((arrowPos + (arrowWidth) / 2) < rect.height) {
                polygon.addPoint(rect.x + arrowPos + arrowWidth / 2, rect.y);
            }

            polygon.addPoint(rect.x + rect.width, rect.y);
            polygon.addPoint(rect.x + rect.width, rect.y + rect.height);
            polygon.addPoint(rect.x, rect.y + rect.height);

        } else if (arrowDirection.equalsIgnoreCase(BorderLayout.SOUTH)) {
            polygon.addPoint(rect.x, rect.y + rect.height);

            if (arrowPos > rect.width) {
                arrowPos = rect.width;
            }

            if (arrowPos > (arrowWidth / 2)) {
                polygon.addPoint(rect.x + arrowPos - arrowWidth / 2, rect.y
                        + rect.height);
                polygon.addPoint(rect.x + arrowPos - arrowWidth / 2, rect.y
                        + rect.height + thickness);
            } else {
                polygon.addPoint(rect.x + arrowPos, rect.y + rect.height
                        + thickness);
            }

            if ((arrowPos + (arrowWidth) / 2) < rect.height) {
                polygon.addPoint(rect.x + arrowPos + arrowWidth / 2, rect.y
                        + rect.height);
            }

            polygon.addPoint(rect.x + rect.width, rect.y + rect.height);
            polygon.addPoint(rect.x + rect.width, rect.y);
            polygon.addPoint(rect.x, rect.y);

        } else if (arrowDirection.equalsIgnoreCase(BorderLayout.CENTER)) {

            polygon.addPoint(rect.x, rect.y + rect.height);
            polygon.addPoint(rect.x + rect.width, rect.y + rect.height);
            polygon.addPoint(rect.x + rect.width, rect.y);
            polygon.addPoint(rect.x, rect.y);

        }
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        onPaint();

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        calcArrowRect();

        Color oldColor = g2d.getColor();
        g2d.setColor(panelColor);
        g2d.fill(polygon);
        g2d.setColor(lineColor);
        g2d.draw(polygon);
        g2d.setColor(oldColor);
    }
    // View end
}
