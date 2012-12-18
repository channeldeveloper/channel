/*
 *  com.original.widget.plaf.OImagePanelUI.java
 *  
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget.plaf;

import com.original.widget.OImagePanel;
import com.original.widget.datadef.OriCircle;
import com.original.widget.draw.GeomOperator;
import com.original.widget.draw.OriPainter;
import com.original.widget.model.ImagePanelModel;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
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
public class OImagePanelUI extends BasicPanelUI {

    private OImagePanel comp;
    private ImagePanelModel model;
    private Color clrShadowS = new Color(220, 220, 220);
    private Color clrShadowE = new Color(161, 161, 161);
    private Color clrBorder = new Color(204, 204, 204, 76);
    private int iHeight = 0;
    private int labWidth = 0;
    private int imgWidth = 0;
    private Rectangle imgRect = null;
    private int shadowsize = 10;

    public static ComponentUI createUI(JComponent c) {
        return new OImagePanelUI(c);
    }

    public OImagePanelUI(JComponent com) {
        comp = (OImagePanel) com;
        model = comp.getModel();
    }

    public void redraw() {
        if (model == null) {
            model = comp.getModel();
        }
        model.setSize(comp.getHeight(), comp.getWidth());
    }

    private String getTitle(Graphics g) {
        int b = 0;
        String str = model.getTitle();
        StringBuffer ret = new StringBuffer();
        FontMetrics metrics = g.getFontMetrics(model.getFont());
        if (model.getOrientation().equalsIgnoreCase(BorderLayout.EAST) || model.getOrientation().equalsIgnoreCase(BorderLayout.WEST)) {
            imgWidth = model.getHeight();
            labWidth = model.getWidth() - imgWidth;
        } else {
            labWidth = model.getWidth();
            imgWidth = model.getWidth();
        }
        int adv = labWidth - metrics.charWidth('W');
        iHeight = metrics.getHeight();
        for (int i = 0; i < str.length(); i++) {
            int adv1 = metrics.stringWidth(str.substring(b, i));
            if (adv1 > adv) {
                ret.append("\n");
                b = i;
            }
            ret.append(str.charAt(i));
        }
        return ret.toString();

    }

    @Override
    public void paint(Graphics g, JComponent c) {
        if (model == null) {
            model = comp.getModel();
        }

        String[] titles = getTitle(g).split("\n");
        int width1 = imgWidth - model.getRightOffset() - model.getLeftOffset();
        int height1 = model.getHeight() - iHeight * titles.length - 2;
        if (model.getOrientation().equalsIgnoreCase(BorderLayout.EAST) ||
                model.getOrientation().equalsIgnoreCase(BorderLayout.WEST)) {
            height1 = model.getHeight() - 2;
        }

        int offset = 1;
        if (imgRect == null) {
            imgRect = new Rectangle();
            imgRect.setBounds(offset, offset,
                    width1 - offset * 2, height1 - offset * 2 - shadowsize);
        }

        if (model.getOrientation().equalsIgnoreCase(BorderLayout.EAST)) {
            this.drawBackground(g, imgRect.x, imgRect.y,width1, height1);
            g.drawImage(model.getImage(), imgRect.x, imgRect.y, imgRect.width, imgRect.height, null);

            int iheight = model.getHeight() - (iHeight + 2) * titles.length - shadowsize ;
            for (int i = 0; i < titles.length; i++) {
                Area area = new Area(new Rectangle(width1 + model.getLeftOffset() + model.getRightOffset() + 1,
                        iheight + 2 + iHeight * i, labWidth, iHeight));
                if (i == 0 && titles.length == 1) {
                    OriPainter.drawString(g, model.getFont(), area, titles[i], model.getForecolor());
                } else {
                    OriPainter.drawStringInAreaCenter(g, area, titles[i], model.getFont(), model.getForecolor());
                }
            }
        } else if (model.getOrientation().equalsIgnoreCase(BorderLayout.WEST)) {
            this.drawBackground(g, imgRect.x + labWidth, imgRect.y,width1, height1);
            g.drawImage(model.getImage(), imgRect.x + labWidth, imgRect.y, imgRect.width, imgRect.height, null);
            int iheight = model.getHeight() - (iHeight + 2) * titles.length;
            for (int i = 0; i < titles.length; i++) {
                Area area = new Area(new Rectangle(1, iheight + 2 + iHeight * i, labWidth, iHeight));
                OriPainter.drawStringInAreaCenter(g, area, titles[i], model.getFont(), model.getForecolor());
            }
        } else if (model.getOrientation().equalsIgnoreCase(BorderLayout.SOUTH)) {
            this.drawBackground(g, imgRect.x, imgRect.y,width1, height1);
           g.drawImage(model.getImage(), imgRect.x, imgRect.y, imgRect.width, imgRect.height, null);

            for (int i = 0; i < titles.length; i++) {
                Area area = new Area(new Rectangle(1, height1 + 2 + iHeight * i, labWidth, iHeight));
                OriPainter.drawStringInAreaCenter(g, area, titles[i], model.getFont(), model.getForecolor());
            }
        } else if (model.getOrientation().equalsIgnoreCase(BorderLayout.NORTH)) {
            this.drawBackground(g,  imgRect.x,imgRect.y ,width1, model.getHeight());
            g.drawImage(model.getImage(), imgRect.x, imgRect.y + (iHeight + 2) * titles.length, imgRect.width, imgRect.height-2, null);
            for (int i = 0; i < titles.length; i++) {
                Area area = new Area(new Rectangle(1, 2 + iHeight * i, labWidth, iHeight));
                OriPainter.drawStringInAreaCenter(g, area, titles[i], model.getFont(), model.getForecolor());
            }
        }
        if (model.isSelected()) {
            Polygon filledPolygon = new Polygon();
            if (model.getOrientation().equalsIgnoreCase(BorderLayout.WEST)) {
                filledPolygon.addPoint(labWidth + width1 + model.getLeftOffset() + 1, height1 / 2);
                filledPolygon.addPoint(model.getWidth() + 1, height1 / 2 - model.getRightOffset());
                filledPolygon.addPoint(model.getWidth() + 1, height1 / 2 + model.getRightOffset());
            } else if (model.getOrientation().equalsIgnoreCase(BorderLayout.NORTH)) {
                filledPolygon.addPoint(width1 + model.getLeftOffset() + 1, height1 / 2 + (iHeight + 2) * titles.length);
                filledPolygon.addPoint(width1 + model.getLeftOffset() + model.getRightOffset(), height1 / 2 - model.getRightOffset() + (iHeight + 2) * titles.length);
                filledPolygon.addPoint(width1 + model.getLeftOffset() + model.getRightOffset(), height1 / 2 + model.getRightOffset() + (iHeight + 2) * titles.length);
            } else {
                filledPolygon.addPoint(width1 + model.getLeftOffset() + 1, height1 / 2);
                filledPolygon.addPoint(width1 + model.getLeftOffset() + model.getRightOffset(), height1 / 2 - model.getRightOffset());
                filledPolygon.addPoint(width1 + model.getLeftOffset() + model.getRightOffset(), height1 / 2 + model.getRightOffset());
            }
            g.setColor(model.getSelectedColor());
            g.fillPolygon(filledPolygon);
        }
        super.paint(g, c);
    }

    /**
     * 绘制头像输入的边框等信息
     * @param g
     * @param x
     * @param y
     * @param width
     * @param height
     */
    protected void drawBackground(Graphics g, int x,int y,int width, int height) {
        int corner = 4;
        //draw basic shadow.
        RoundRectangle2D shadowRect = new RoundRectangle2D.Double(x, y,
                width - 1, height - shadowsize - 1, corner, corner);

        Area shadowArea = new Area(shadowRect);
        GeomOperator.offset(shadowArea, -1, -1);

        Area ashadowArea = genAdditionalShadowArea(width, height);
        shadowArea.add(ashadowArea);

        BufferedImage shadow = OriPainter.drawGradientBoxShadow(g,
                shadowArea, clrShadowS, clrShadowE);
        OriPainter.drawImage(g, shadow, x, y);


        //draw the outer frame.
        RoundRectangle2D r2d = new RoundRectangle2D.Double(x, y, width - 2, height - 14,
                corner, corner);
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
    private Area genAdditionalShadowArea(int width, int height) {
        GeneralPath shape = new GeneralPath();
        double x_control = 15.4;
        double x_control2 = width - 15.4;

        shape.moveTo(1, height - shadowsize - 1);
        shape.curveTo(1, height - shadowsize, x_control,
                height, x_control2, height);
        shape.lineTo(width - x_control2, height);
        shape.curveTo(width - x_control2, height,
                width - x_control, height,
                width, height - shadowsize - 1);

        shape.lineTo(1, height - shadowsize - 1);
        shape.closePath();
        Area base = new Area(shape);

        double shadowheight = 3;
        Point2D pt1 = new Point2D.Double(x_control, height - shadowsize + shadowheight);
        Point2D pt2 = new Point2D.Double(width / 2, height - shadowsize);
        Point2D pt3 = new Point2D.Double(x_control2, height - shadowsize + shadowheight);
        OriCircle ocircle = GeomOperator.findCenterRadius(pt1, pt2, pt3);

        double radius = ocircle.getRadius();
        double x = ocircle.getCenter().getX() - radius;
        double y = ocircle.getCenter().getY() - radius;
        Ellipse2D circle = new Ellipse2D.Double(x, y, radius * 2,
                radius * 2);
        base.subtract(new Area(circle));
        return base;
    }
}
