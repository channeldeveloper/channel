/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.widget.plaf;

import com.original.widget.OIrregularButton;
import com.original.widget.draw.GeomOperator;
import com.original.widget.draw.OriPainter;
import com.original.widget.draw.OriShadow;
import com.original.widget.model.IrregularButtonModel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;

/**
 * 特殊形状按钮UI
 * @author Changjian Hu
 */
public class OIrregularButtonUI extends BasicPanelUI   {
    private OIrregularButton comp = null;
    private Area funcArea = null;
    public static ComponentUI createUI(JComponent c) {
        return new OIrregularButtonUI(c);
    }

    public OIrregularButtonUI(JComponent com) {
        comp = (OIrregularButton)com;
    }

    @Override
    public void paint(Graphics g, JComponent c){
        drawBackground(g,c);
        super.paint(g,c);
     }

     private void drawBackground(Graphics g, JComponent c){
        //draw the half circle
        int shadowsize = 4;
        int radius = comp.getWidth()-shadowsize;
        Ellipse2D shape = new Ellipse2D.Double(shadowsize, 0,
                radius*2, radius*2);
        Area area = new Area(shape);
        funcArea = area;
        OriPainter.basicDrawDropShadow(g, area, Color.BLACK, shadowsize, shadowsize,
                Math.PI*3/2, 0.3f);
        Area top = (Area)area.clone();
        top.subtract(new Area(new Rectangle(shadowsize, radius, radius*2, radius)));
        OriPainter.gradientFillArea(g, top, new Color(255, 255,255), new Color(217,222,221), true);

        Area bottom = (Area)area.clone();
        bottom.subtract(new Area(new Rectangle(shadowsize, 0, radius*2, radius)));
        OriPainter.gradientFillArea(g, bottom, new Color(210,210,210), new Color(212,210,211), true);
        OriPainter.drawAreaBorderWithSingleColor(g, area, Color.white, 0.1f);

        //draw the inner circle.
        Area circle= drawInnerCircle(g, shadowsize, radius);
        
        //draw the Symbol.
        drawCross(g, circle);
     }
     //检测是否咋按钮的功能区域
     public boolean isInFunctionArea(Point pt){
         if(funcArea!=null && funcArea.contains(pt))
             return true;
         else
             return false;
     }
     //绘制按钮的中心小圆
     private Area drawInnerCircle(Graphics g, int shadowsize, int radius){
         IrregularButtonModel model = comp.getModel();
         int circlecenteroffset = 20;
        int circleradius = 14;
        Point2D center = new Point2D.Double(shadowsize+circlecenteroffset, radius);
        Ellipse2D innerCircle = new Ellipse2D.Double(center.getX()-circleradius,
                center.getY()-circleradius, circleradius*2, circleradius*2);
        Area area = new Area(innerCircle);
        Area top = (Area)area.clone();
        top.subtract(new Area(new Rectangle2D.Double(center.getX()-circleradius,
                center.getY(), circleradius*2, circleradius)));

        
        OriPainter.gradientFillArea(g, top, new Color(144,158,167),
                new Color(201,203,202), true );
        
        Area bottom = (Area)area.clone();
        bottom.subtract(new Area(new Rectangle2D.Double(center.getX()-circleradius,
                center.getY()-circleradius, circleradius*2, circleradius)));
        OriPainter.gradientFillArea(g, bottom, new Color(207,207,207),
                new Color(246,246,246), true);
        area = GeomOperator.centerFixShrinkCopy(area, 1.2f);
        area = GeomOperator.offsetCopy(area, 0.3, 0.3);
        if(model.isRollover())
            OriPainter.gradientFillArea(g, area,
                new Color(121,181,0), new Color(35,135,0), true);
        else
            OriPainter.gradientFillArea(g, area,
                new Color(121,181,218), new Color(35,135,197), true);
        
        //draw other effect.
        Ellipse2D bubble = new Ellipse2D.Double(center.getX()-circleradius+4,
                center.getY()-circleradius+3, circleradius*2-8, circleradius*2-14);
        OriPainter.gradientFillArea(g, new Area(bubble),
                new Color(195,224,238), new Color(195,224,238,0), true);

        Rectangle2D r = area.getBounds();
        Point2D focus = new Point2D.Double(r.getCenterX(), r.getMaxX()+radius/2);
        Ellipse2D light = new Ellipse2D.Double(focus.getX()-radius,
                focus.getY()-radius, radius*2, radius*2);
        float rradius = (float)(r.getWidth()/2);
        float[] dist = {0.0f, 0.75f, 1.0f};
        Color[] colors = {new Color(246,246,246), new Color(246,246,246,50), new Color(255, 255, 255, 0 )};
        RadialGradientPaint p =
           new RadialGradientPaint(focus, rradius, dist, colors);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setPaint(p);
        g2d.setClip(innerCircle);
        g2d.fill(light);
        g2d.setClip(null);
        return new Area(innerCircle);
     }
     //绘制一个Cross
     private void drawCross(Graphics g, Area area){
         int crosswidth = 10;
         IrregularButtonModel model = comp.getModel();
         Rectangle2D r = area.getBounds2D();
         double offsetx = r.getX()+ (r.getWidth()-crosswidth)/2;
         double offsety = r.getY()+ (r.getHeight()-crosswidth)/2;
         Point2D pt1 = new Point2D.Double(offsetx, r.getY()+r.getHeight()/2);
         Point2D pt2 = new Point2D.Double(offsetx+crosswidth, r.getY()+r.getHeight()/2);
         Color clr = Color.WHITE;
         if(model.isPressed())
             clr = Color.RED;
         OriPainter.drawLine(g, pt1, pt2, clr, 2.4f);
         pt1 = new Point2D.Double(r.getX()+r.getWidth()/2, offsety);
         pt2 = new Point2D.Double(r.getX()+r.getWidth()/2, offsety+crosswidth);
         OriPainter.drawLine(g, pt1, pt2, clr, 2.4f);

     }
}
