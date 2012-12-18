/*
 *  com.original.widget.comp.view.OTextFieldUI.java
 *
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget.plaf;

import com.original.widget.OPanel;
import com.original.widget.draw.OriPainter;
import com.original.widget.model.PanelModel;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;

/**
 * (Class Annotation.)
 * 基本文本编辑组件
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   May 4, 2012 2:33:56 PM
 */
public class OPanelUI extends BasicPanelUI {

    private OPanel comp;
    private PanelModel model;

    public static ComponentUI createUI(JComponent c) {
        return new OPanelUI(c);
    }

    public OPanelUI(JComponent com) {
        comp = (OPanel) com;
        model = comp.getModel();
    }

    public void redraw() {
        if (model == null) {
            model = comp.getModel();
        }
        model.setSize(comp.getHeight(), comp.getWidth());
    }

    @Override
    public void update(Graphics g, JComponent c) {
        if (model == null) {
            model = comp.getModel();
        }

        //draw the background.
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        //draw the background color
        OPoint pt1 = new OPoint(0, 3);
        OPoint pt2 = new OPoint(comp.getWidth()/2, 1);
        OPoint pt3 = new OPoint(comp.getWidth(), 3);
        OCircle circle = findCenterRadius(pt1,pt2, pt3);
        float fscale = (float)((circle.radius-1)/circle.radius);
        float[] dist1 = {0.0f, fscale, 1.0f};

        Color[] colors1 = {new Color(226,226,226), new Color(98,98,98),new Color(98,98,98)};
        RadialGradientPaint p1 =
             new RadialGradientPaint(new Point((int)circle.center.x,
             (int)circle.center.y), (float)circle.radius, dist1, colors1);

        Area area = new Area(new Rectangle(0, 0, comp.getWidth(), 3));
        area.subtract(new Area(new Ellipse2D.Double(circle.center.x-circle.radius,
                circle.center.y-circle.radius, circle.radius*2, circle.radius*2)));

        int x = comp.getWidth()/2;
        int y = comp.getHeight();
        float radius = (float)(Math.sqrt(x*x + y*y));
        //y = (int)radius;
        Point2D center = new Point2D.Float(x, y+80);

         //Point2D focus = new Point2D.Float(40, 40);
        float fScale = y/radius;
        float[] dist = {0.0f, fScale, 1.0f};
        Color[] colors = {Color.WHITE, Color.WHITE,new Color(226,226,226)};
        RadialGradientPaint p =
             new RadialGradientPaint(center, radius, dist, colors);

        g2d.setPaint(p);
        //g2d.translate(0, 8);
        Area newarea = new Area(new Rectangle(0, 0, comp.getWidth(), comp.getHeight()));
        newarea.subtract(area);
        g2d.fill(newarea);


        OriPainter.drawAreaDropShadowEx(g, area, new Color(98,98,98), 3, 3, Math.PI*3/2, 0.5f);
        g2d.setPaint(new Color(98,98,98,120));
        g2d.fill(area);
        super.update(g, c);
    }

    class OCircle{
        public OPoint center;
        public double radius;
        public OCircle(OPoint center, double radius){
            this.center = center;
            this.radius = radius;
        }
    }
    class OPoint{
        public  double x;
        public double y;
        public OPoint(double x, double y){
            this.x = x;
            this.y = y;
        }
    }
    double distance( OPoint p1, OPoint p2 ){
        return Math.sqrt( (p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y) );
    }
    OCircle findCenterRadius( OPoint p1, OPoint p2, OPoint p3 )
    {
        double x = (p3.x*p3.x * (p1.y - p2.y)
                + (p1.x*p1.x + (p1.y - p2.y)*(p1.y - p3.y))
                * (p2.y - p3.y) + p2.x*p2.x * (-p1.y + p3.y))
            / (2 * (p3.x * (p1.y - p2.y) + p1.x * (p2.y - p3.y) + p2.x
                * (-p1.y + p3.y)));

        double y = (p2.y + p3.y)/2 - (p3.x - p2.x)/(p3.y - p2.y)
            * (x - (p2.x + p3.x)/2);

        OPoint c = new OPoint( x, y );
        double r = distance(c, p1);

        return new OCircle( c, r );
     }


}
