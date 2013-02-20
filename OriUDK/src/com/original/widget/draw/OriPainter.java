/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.widget.draw;
//This class will be used to draw special effects, and
//generate special components.

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.Kernel;

import javax.swing.JTextField;

import com.jhlabs.image.ConvolveFilter;
import com.jhlabs.image.GaussianFilter;
import com.jhlabs.image.OpacityFilter;
import com.jhlabs.image.SmartBlurFilter;
import com.thebuzzmedia.imgscalr.Scalr;
import com.thebuzzmedia.imgscalr.Scalr.Method;

/**
 *
 * @author Changjian Hu
 * 修改记录：
 * 
 */
public class OriPainter  {
    /**
     * 使用单色填充特定区域
     * @param area
     * @param color
     */
    public static void fillAreaWithSingleColor(Graphics g, Area area, Color clr){
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setPaint(clr);
        g2d.fill(area);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
    }

    /**
     * 渐进色填充一个区域
     * @param g
     * @param area
     * @param clrStart
     * @param clrEnd
     * @param vertical
     */
    public static void gradientFillArea(Graphics g, Area area, Color clrStart,
            Color clrEnd, boolean vertical){
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        Rectangle r = area.getBounds();
        GradientPaint gp = null;
        if(vertical)
            gp = new GradientPaint(r.x, r.y,
                clrStart, r.x, r.y+r.height, clrEnd);
        else
            gp = new GradientPaint(r.x, r.y,
                clrStart, r.x+r.width, r.y, clrEnd);

        g2d.setPaint(gp);
        g2d.fill(area);
    }
    //为绘制沙漏做伏笔，提供对应API
    public static void gradientPartlyFillArea(Graphics g, Area area, Color clrStart,
            Color clrEnd, Color clrBack, boolean vertical, float divid, boolean invert){
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        Rectangle r = area.getBounds();
        GradientPaint gp = null;
        if(vertical)
            gp = new GradientPaint(r.x, r.y,
                clrStart, r.x, r.y+r.height, clrEnd);
        else{
            if(!invert){ //right
                Area paint = (Area)area.clone();
                paint.subtract(new Area(new Rectangle2D.Double(r.x, r.y, r.width*(1-divid), r.height)));
                g2d.setPaint(clrBack);
                g2d.fill(paint);

                Area nopaint = (Area)area.clone();
                nopaint.subtract(new Area(new Rectangle2D.Double(r.x+r.width*(1-divid), r.y, r.width, r.height)));

                r = area.getBounds();
                gp = new GradientPaint(r.x, r.y, clrEnd, r.x+r.width, r.y, clrStart);
                g2d.setPaint(gp);
                g2d.fill(nopaint);
            }else{
                Area paint = (Area)area.clone();
                paint.subtract(new Area(new Rectangle2D.Double(r.x+r.width*divid, r.y, r.width, r.height)));
                g2d.setPaint(clrBack);
                g2d.fill(paint);

                Area nopaint = (Area)area.clone();
                nopaint.subtract(new Area(new Rectangle2D.Double(r.x, r.y, r.width*divid, r.height)));

                r = area.getBounds();
                gp = new GradientPaint(r.x, r.y, clrEnd, r.x+r.width, r.y, clrStart);
                g2d.setPaint(gp);
                g2d.fill(nopaint);
            }

        }
        g2d.setPaint(gp);
        g2d.fill(area);
    }
    /**
     * 为一个区域绘制边框
     * @param g
     * @param area
     * @param clr
     * @param border
     */
    public static void drawAreaBorderWithSingleColor(Graphics g, Area area, 
            Color clrLine,
            float lWidth){
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(lWidth));
        g2d.setPaint(clrLine);
        g2d.draw(area);
    }

    /**
     * 给定Area绘制其内阴影
     * @param g
     * @param area
     * @param clrStart
     * @param clrEnd
     * @param opacity
     * @return
     */
    public static BufferedImage drawGradientInnerShadow(Graphics g, Area area,
            Color clrStart, Color clrEnd, int opacity){

        Rectangle rect = area.getBounds();
        BufferedImage tmpImg = new BufferedImage(rect.x + rect.width,
                rect.y + rect.height,
                BufferedImage.TYPE_INT_ARGB );
		Graphics2D rg = tmpImg.createGraphics();

        rg.setBackground(new Color(255, 255, 255, 0));
        rg.clearRect(0, 0, rect.x + rect.width, rect.y + rect.height);
        GradientPaint gp = new GradientPaint(rect.x,
                0,
                clrStart, rect.x,
                rect.y+ rect.height, clrEnd);

        rg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        rg.setPaint(gp);
        rg.fill(area);

        OpacityFilter filter = new OpacityFilter(40);
        SmartBlurFilter filter2 = new SmartBlurFilter();
        BufferedImage dist = filter2.filter(tmpImg, null);
        dist = filter.filter(dist, null);
        rg.dispose();
        tmpImg = null;
        return dist;
    }

    /**
     * 给定Area获取特殊阴影
     * @param g
     * @param area
     * @param clrStart
     * @param clrEnd
     * @param opacity
     * @return
     */
    public static BufferedImage drawGradientBoxShadow(Graphics g, Area area,
            Color clrStart, Color clrEnd){

        Rectangle rect = area.getBounds();
        BufferedImage tmpImg = new BufferedImage(rect.x + rect.width,
                rect.y + rect.height,
                BufferedImage.TYPE_INT_ARGB );
		Graphics2D rg = tmpImg.createGraphics();

        rg.setBackground(new Color(255, 255, 255, 0));
        rg.clearRect(0, 0, rect.x + rect.width, rect.y + rect.height);
        /*GradientPaint gp = new GradientPaint(rect.x,
                0,
                clrStart, rect.x,
                rect.y+ rect.height, clrEnd);*/
         int x = rect.x + rect.width/2;
         int y = rect.y + rect.height/2;
         Point2D center = new Point2D.Float(x, y);
         float radius = (float)(rect.height/2*Math.sqrt(2));
         Point2D focus = new Point2D.Float(x, y);
         float[] dist = {0.0f, 0.30f, 1.0f};
         Color[] colors = {clrEnd, clrEnd, clrStart};
         RadialGradientPaint p =
         new RadialGradientPaint(center, radius, focus,
                                 dist, colors,
                                 CycleMethod.NO_CYCLE);

        rg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //rg.setPaint(p);
        rg.setPaint(clrStart);
        rg.fill(area);
        rg.setPaint(clrEnd);
        Area rlArea = GeomOperator.offsetCopy(area, 0, -2);
        rg.fill(rlArea);
       
        float[] matrix = {
            0.111f, 0.111f, 0.111f,
            0.111f, 0.111f, 0.111f,
            0.111f, 0.111f, 0.111f,
        };
        ConvolveFilter filter2 = new ConvolveFilter( new Kernel(3, 3, matrix));
        BufferedImage ret = filter2.filter(tmpImg, null);
        rg.dispose();
        tmpImg = null;
        return ret;
    }

    /**
     * 在具体位置上绘制图像内容
     * @param g
     * @param img
     * @param x
     * @param y
     */
    public static void drawImage(Graphics g, BufferedImage img, int x, int y){
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(img, x, y, null);
    }

    /**
     * 自动缩放图像并将图像绘制到给定区域里,并将图像居中
     * @param g
     * @param img
     * @param area
     */
    public static void drawFitImage(Graphics g, BufferedImage img, Area area){
        Rectangle rect = area.getBounds();
        int width = rect.width-2;
        int height = rect.height -2;
        BufferedImage img2Draw = Scalr.resize(img, Method.QUALITY,
                        width, height, Scalr.OP_ANTIALIAS);
        int x = rect.x + 1;
        int y = rect.y +1;

        x += (width - img2Draw.getWidth())/2;
        y += (height - img2Draw.getHeight())/2;

        drawImage(g, img2Draw, x, y);
    }

    /**
     * 绘制一个内嵌Button
     * @param g
     * @param area
     * @param clrHolder
     * @param clrBkStart
     * @param clrBkEnd
     * @param margin
     */
    public static void drawButtonFrame(Graphics g, Area area, Color clrHolder,
            Color clrHolderBorder,
            Color clrBkStart,
            Color clrBkEnd, int margin){
        
        fillAreaWithSingleColor(g, area, clrHolder);
        drawAreaBorderWithSingleColor(g, area, clrHolderBorder, 1);
        Area btnArea = GeomOperator.shrinkCopy(area, margin);
        double offset_x = area.getBounds2D().getX()+margin;
        double offset_y = area.getBounds2D().getY()+margin;
        offset_x -= btnArea.getBounds2D().getX();
        offset_y -= btnArea.getBounds2D().getY();
        GeomOperator.offset(btnArea, offset_x, offset_y);
        gradientFillArea(g, btnArea, clrBkStart, clrBkEnd, true );
    }

    /**
     * 在特定区域绘制字符串并居中放置
     * @param g
     * @param area
     * @param title
     * @param font
     * @param clrText
     */
    public static void drawStringInAreaCenter(Graphics g, Area txtArea, String txt,
            Font txtFont, Color txtColor){
        Graphics2D g2d = (Graphics2D)g;
        Rectangle rect = txtArea.getBounds();
        int width = rect.width;
        int height = rect.height;
        g2d.setColor(txtColor);
        g2d.setFont(txtFont);
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        int adv = metrics.stringWidth(txt);
        int hgt = metrics.getHeight();
        int offsetx = rect.x + (width-adv)/2;
        int offsety = rect.y + height/2 + (int)(hgt*0.3);
        g2d.drawString(txt, offsetx, offsety);
    }


    /**
     * 绘制一个简单水滴，这个水滴的下半部分是一个正切三角形
     * @param g
     * @param pt 水滴最下面的点位置
     * @param radius 水滴头的最外圈半径
     * @param shrinksize 水滴内部内圈相比外圈缩小大小
     * @param angle 水滴底部头对应的角度
     * @param clr 水滴填充颜色
     */
    public static void drawSharpWaterDrop(Graphics g, Point2D pt, double radius,
            double shrinksize, double angle,
            Color clr){
        double centery = pt.getY() - radius/Math.sin(angle/2);
        double centerx = pt.getX();

        //水滴头
        Shape outer = new Ellipse2D.Double(centerx-radius, centery-radius,
                radius*2, radius*2);
        Area round = new Area(outer);

        //水滴尾
        double dH = radius * Math.sin(angle/2);
        double dW = radius * Math.cos(angle/2);
        GeneralPath shape = new GeneralPath ();
        shape.moveTo(centerx-dW , centery+dH);
        shape.lineTo(pt.getX(), pt.getY());
        shape.lineTo(centerx + dW, centery + dH);
        shape.closePath();
        round.add(new Area(shape));
        basicDrawDropShadow(g, round, Color.BLACK, 5, 6, Math.PI*3/2, 0.4f);
        fillAreaWithSingleColor(g, round, clr);

        Area inner = GeomOperator.centerFixShrinkCopy(new Area(outer), shrinksize);
        GeomOperator.offset(inner, 0, -shrinksize/2);
        gradientFillArea(g, inner, new Color(255,255,255,(int)(255*0.6)),
                new Color(255,255,255,(int)(255*0.0)), true);
    }


    /**
     * 绘制一个流畅型雨滴
     * @param g
     * @param pt 水滴头所在位置
     * @param radius 水滴头的外圆半径
     * @param scale 外交叉园控制因素
     * @param shrinksize 水滴头内园半径和外圆半径的差     
     * @param angle 角度
     * @param clr 填充颜色
     */
    public static void drawSmoothWaterDrop(Graphics g, Point2D pt, double radius, double scale,
            double shrinksize, double angle, Color clr){
        double oradius = radius*scale;
        double cos = Math.cos(angle/2);
        double sin = Math.sin(angle/2);
        double ocentery = pt.getY() - oradius*cos;
        double ocenterx = pt.getX() - oradius*sin;
        
        double sangle = Math.acos(oradius*sin/(oradius-radius));
        double centery = ocentery + (oradius-radius)*Math.sin(sangle);
        double centerx = ocenterx + oradius * sin;
        //水滴头
        Shape outer = new Ellipse2D.Double(centerx-radius, centery-radius,
                radius*2, radius*2);
        Area round = new Area(outer);

        //水滴尾
        //计算两个大圆（交叉）
        Shape lo = new Ellipse2D.Double(ocenterx-oradius, ocentery-oradius,
                oradius*2, oradius*2);
        Area loa = new Area(lo);
        Area roa = GeomOperator.offsetCopy(loa, oradius*sin*2, 0);

        loa.intersect(roa);
        //fillAreaWithSingleColor(g, loa, clr);
        double dH = radius * Math.sin(sangle);
        double dW = radius * Math.cos(sangle);
        GeneralPath shape = new GeneralPath ();
        shape.moveTo(centerx-dW , centery+dH);
        shape.lineTo(centerx-dW, pt.getY());
        shape.lineTo(centerx+dW, pt.getY());
        shape.lineTo(centerx+dW, centery+dH);
        shape.closePath();
        //fillAreaWithSingleColor(g, new Area(shape), clr);
        loa.intersect(new Area(shape));
        round.add(loa);
        fillAreaWithSingleColor(g, round, clr);

        Area inner = GeomOperator.centerFixShrinkCopy(new Area(outer), shrinksize);
        GeomOperator.offset(inner, 0, -shrinksize/2);
        gradientFillArea(g, inner, new Color(255,255,255,(int)(255*0.6)),
                new Color(255,255,255,(int)(255*0.0)), true);
        
    }

    /**
     * 给定位置绘制一个园，绘制对应的边。
     * @param g
     * @param center
     * @param radius
     * @param clr
     * @param lwidth
     */
    public static void drawGradidentCircle(Graphics g, Point center, int radius,
            Color clr, Color clrEnd, float lWidth){
        double x = center.x - radius;
        double y = center.y - radius;

        Area outer = new Area(new Ellipse2D.Double(x, y, radius*2, radius*2));
        Area inner = new Area(new Ellipse2D.Double(x+lWidth, y+lWidth,
                (radius-lWidth)*2, (radius-lWidth)*2));
        outer.subtract(inner);
        gradientFillArea(g, outer, clr, clrEnd, true);
    }

     /**
     * 给定位置绘制一个园，绘制对应的边。
     * @param g
     * @param center
     * @param radius
     * @param clr
     * @param lwidth
     */
    public static void drawGradidentRoundRect(Graphics g, Point lrcorner, int width,
            int cornersize,
            Color clr, Color clrEnd, float lWidth){

        int x = lrcorner.x;
        int y = lrcorner.y;
        Area outer = new Area(new RoundRectangle2D.Double(x,
                y, width, width, cornersize, cornersize));
        Area inner = new Area(new RoundRectangle2D.Double(x+lWidth, y+lWidth,
                width-lWidth*2, width-lWidth*2, cornersize, cornersize));
        outer.subtract(inner);
        gradientFillArea(g, outer, clr, clrEnd, true);
    }

    /**
     * 使用单色绘制一个园
     * @param g
     * @param center
     * @param radius
     * @param clr
     */
    public static void fillCircleWithSingleColor(Graphics g, Point center, int radius,
            Color clr){
        double x = center.x - radius;
        double y = center.y - radius;

        Area outer = new Area(new Ellipse2D.Double(x, y, radius*2, radius*2));
        fillAreaWithSingleColor(g, outer, clr);
    }

    /**
     * 在一个正方形框里绘制一个对号
     * @param g
     * @param rect
     * @param clr
     * @param lWidth
     */
    public static void drawCheckMark(Graphics g, Rectangle rect, Color clr, float lWidth){
        GeneralPath path = new GeneralPath();
        int x = rect.x;
        int y = rect.y;
        int width = rect.width;
        path.moveTo(x + width/3, y + width/3+3);
        path.lineTo(x + width/2, y + width*2/3);
        path.lineTo(x + width-4, y+4);

        Graphics2D g2d = (Graphics2D)g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(lWidth, BasicStroke.CAP_ROUND , BasicStroke.JOIN_BEVEL ));
        g2d.setPaint(clr);
        g2d.draw(path);

//        Color oldclr = g.getColor();
//        float xWidth = 2.1f;
//        g2d.setStroke(new BasicStroke(xWidth));
//        g2d.setPaint(oldclr);
//        double addition = Math.cos(Math.PI/4)*lWidth + xWidth;
//        g2d.drawLine(x + width/2,(int)(y + width*2/3-addition),
//                x + width, (int)(y+2-addition));
//
//        g2d.drawLine(x + width/2,(int)( y + width*2/3+addition),
//                x + width, (int)(y+2+addition));

    }

    /**
     * 在两点之间绘制一条线
     * @param g
     * @param pt1
     * @param pt2
     * @param clrLine
     * @param lWidth
     */

    public static void drawLine(Graphics g, Point pt1, Point pt2, Color clrLine,
            float lWidth){
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(lWidth));
        g2d.setPaint(clrLine);
        g2d.drawLine(pt1.x, pt1.y, pt2.x, pt2.y);
    }
    public static void drawLine(Graphics g, Point2D pt1, Point2D pt2, Color clrLine,
            float lWidth){
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(lWidth));
        g2d.setPaint(clrLine);
        g2d.drawLine((int)pt1.getX(), (int)pt1.getY(), (int)pt2.getX(), (int)pt2.getY());
    }
    /**
     * 辐射绘制区域
     * @param g
     * @param area
     * @param fractions
     * @param clrs
     */
    public static void radialGradidentFillArea(Graphics g, Area area, Point2D center,
            int radius,
            float[] fractions, Color[] clrs){
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //Point2D center = new Point2D.Double(area.getBounds2D().getCenterX(),
        //        area.getBounds2D().getCenterY());
        //int radius = (int)Math.sqrt( Math.pow(area.getBounds2D().getHeight()/2,2) +
        //        Math.pow(area.getBounds2D().getWidth()/2,2));
        //Rectangle r = area.getBounds();
        RadialGradientPaint  gp = new RadialGradientPaint(center,
                radius, fractions, clrs);
        g2d.setPaint(gp);
        g2d.fill(area);
    }

    /**
     * 在特定区域绘制字符串并居中/放置
     * @param g
     * @param area
     * @param title
     * @param font
     * @param clrText
     */
    public static void drawStringInAreaAlign(Graphics g, Area txtArea, String txt,
            Font txtFont, Color txtColor,float align, int offset){
        Graphics2D g2d = (Graphics2D)g;
        Rectangle rect = txtArea.getBounds();

        g2d.setColor(txtColor);
        g2d.setFont(txtFont);
        FontMetrics fm = g2d.getFontMetrics();
        int adv = fm.stringWidth(txt);
        int hgt = fm.getHeight();
        int offsetx = offset;
        int width = rect.width; //fm.stringWidth(txt);

        if(align == JTextField.LEFT_ALIGNMENT)
            offsetx += rect.x;
        else if(align==JTextField.CENTER_ALIGNMENT)
            offsetx += rect.x + (width-adv)/2;
        else
            offsetx += rect.x + (width-adv);
        int offsety = rect.y + rect.height/2 + (int)(hgt*0.25);
        g2d.drawString(txt, offsetx, offsety);
    }

    /**
     * 从外向内过度
     * @param g
     * @param are
     * @param clrOut
     * @param clrMiddle
     * @param vertical
     */
    public static void gradientMiddleFillArea(Graphics g, Area area, Color clrOut,
            Color clrMiddle, boolean vertical){

        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        Rectangle r = area.getBounds();
        GradientPaint gp = null;

        if(vertical){
            Area top = new Area(new Rectangle2D.Double(r.x, r.y, r.x + r.width,
                    r.y + r.height/2-2));

            Area bottom = new Area(new Rectangle2D.Double(r.x, r.y+r.height/2+2, r.x + r.width,
                    r.y + r.height));
            Area tmp = (Area)area.clone();
            tmp.subtract(bottom);

            gp = new GradientPaint(r.x, r.y,
                clrOut, r.x, r.y+r.height/2, clrMiddle);
            g2d.setPaint(gp);

            g2d.fill(tmp);
            gp = new GradientPaint(r.x, r.y+r.height/2,
                clrMiddle, r.x, r.y+r.height, clrOut);
            tmp = (Area)area.clone();
            tmp.subtract(top);
            g2d.setPaint(gp);
            g2d.fill(tmp);
        }
        else{
            Area left = new Area(new Rectangle(r.x, r.y, r.x + r.width/2-2,
                    r.y + r.height));

            Area right = new Area(new Rectangle(r.x+r.width/2+2, r.y,
                    r.x + r.width,
                    r.y + r.height));
            Area tmp = (Area)area.clone();
            tmp.subtract(right);

            gp = new GradientPaint(r.x, r.y,
                clrOut, r.x+r.width/2, r.y, clrMiddle);
            g2d.setPaint(gp);

            g2d.fill(tmp);
            gp = new GradientPaint(r.x+r.width/2, r.y,
                clrMiddle, r.x+r.width, r.y, clrOut);
            tmp = (Area)area.clone();
            tmp.subtract(left);
            g2d.setPaint(gp);
            g2d.fill(tmp);
        }

    }

    public static void gradientMiddleFillAreaEx(Graphics g, Area area, Color clrOut,
            Color clrMiddle, boolean vertical){

        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        Rectangle r = area.getBounds();
        GradientPaint gp = null;

        if(vertical){
            Area top = new Area(new Rectangle2D.Double(r.x, r.y, r.x + r.width,
                    r.y + r.height/2-4));

            Area bottom = new Area(new Rectangle2D.Double(r.x, r.y+r.height/2+4, r.x + r.width,
                    r.y + r.height));
            Area tmp = (Area)area.clone();
            tmp.subtract(bottom);

            gp = new GradientPaint(r.x, r.y,
                clrOut, r.x, r.y+r.height/2, clrMiddle);
            g2d.setPaint(gp);

            g2d.fill(tmp);
            gp = new GradientPaint(r.x, r.y+r.height/2,
                clrMiddle, r.x, r.y+r.height, clrOut);
            tmp = (Area)area.clone();
            tmp.subtract(top);
            g2d.setPaint(gp);
            g2d.fill(tmp);
        }
        else{
            Area left = new Area(new Rectangle(r.x, r.y, r.x + r.width/2-1,
                    r.y + r.height));

            Area right = new Area(new Rectangle(r.x+r.width/2+1, r.y,
                    r.x + r.width,
                    r.y + r.height));
            Area tmp = (Area)area.clone();
            tmp.subtract(right);

            gp = new GradientPaint(r.x, r.y,
                clrOut, r.x+r.width/2, r.y, clrMiddle);
            g2d.setPaint(gp);

            g2d.fill(tmp);
            gp = new GradientPaint(r.x+r.width/2, r.y,
                clrMiddle, r.x+r.width, r.y, clrOut);
            tmp = (Area)area.clone();
            tmp.subtract(left);
            g2d.setPaint(gp);
            g2d.fill(tmp);
        }

    }
    /**
     * 在特定区域居中绘制字符
     * @param g
     * @param area
     * @param str
     * @param clr
     */
    public static void drawStringInArea(Graphics g, Font font, Area area, String str,
            Color clr){
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(str);
        Rectangle rect = area.getBounds();
        int offsetx = rect.x + (rect.width-width)/2;
        int offsety = rect.y + rect.height/2 + (int) (fm.getHeight()*0.25);
        g2d.setPaint(clr);
        g2d.drawString(str, offsetx, offsety);

    }

    /**
     * 在特定区域靠左绘制字符
     * @param g
     * @param font 
     * @param area
     * @param str
     * @param clr
     */
    public static void drawString(Graphics g, Font font, Area area, String str,
            Color clr){
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        Rectangle rect = area.getBounds();
        int offsetx = rect.x ;
        int offsety = rect.y + rect.height/2 + (int) (fm.getHeight()*0.25);
        g2d.setPaint(clr);
        g2d.drawString(str, offsetx, offsety);
    }
    //在特定Point绘制文字（用于时间轴组件开发）
    public static void drawStringAtPoint(Graphics2D g2d, Font font, Point2D pt,
            String str
            ){
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int offsetx = (int)(pt.getX() - fm.stringWidth(str)/2)-3;
        int offsety = (int)pt.getY()-2;
        g2d.drawString(str, offsetx, offsety);
    }
    /**
     * 对给定区域绘制阴影
     * @param g
     * @param area
     * @param clr
     * @param radius
     *
     * @param dist
     * @param angle
     * @param opacity
     */
    public static void drawDropShadow(Graphics g, Area area, Color clr,
            int radius, int dist, double angle, float opacity){
        Rectangle rect = area.getBounds();
        
        BufferedImage tmpImg = new BufferedImage(rect.x+rect.width+radius*2,
                rect.y+rect.height+radius*2,
                BufferedImage.TYPE_INT_ARGB );
	Graphics2D rg = tmpImg.createGraphics();

        rg.setBackground(new Color(255, 255, 255, 0));
        rg.clearRect(0, 0, rect.width, rect.height);
        
        rg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        rg.setPaint(clr);

        rg.fill(area);
        int xoffset = dist+(int)(radius*Math.cos(angle));
        int yoffset = dist+(int)(radius*Math.sin(angle));

//        GaussianFilter filter2 = new GaussianFilter(radius*2);
//        BufferedImage shadow = filter2.filter(tmpImg, null);
        rg.dispose();
//        tmpImg = null;

        Graphics2D g2d = (Graphics2D)g.create();
        g2d.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, opacity ) );
        g2d.drawImage(tmpImg, dist-xoffset-radius/4, dist-yoffset-radius, null);
        tmpImg = null;
        g2d.dispose();
    }

    public static void drawAreaDropShadow(Graphics g, Area area, Color clr,
            int radius, int dist, double angle, float opacity){
        Rectangle rect = area.getBounds();
        Area filArea = (Area)area.clone();
        //GeomOperator.offset(filArea, -rect.x, -rect.y);
        BufferedImage tmpImg = new BufferedImage(rect.width+radius*2,
                rect.height+radius*2,
                BufferedImage.TYPE_INT_ARGB );
	    Graphics2D rg = tmpImg.createGraphics();
        rg.translate(-rect.x+radius, -rect.y+radius);
        rg.setBackground(new Color(255, 255, 255, 0));
        //rg.clearRect(0, 0, rect.width, rect.height);

        rg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        rg.setPaint(clr);

        rg.fill(filArea);
        int xoffset = rect.x + (int)(dist*Math.sin(angle));
        int yoffset = rect.y + (int)(dist*Math.cos(angle));

        GaussianFilter filter2 = new GaussianFilter(radius*2);
        BufferedImage shadow = filter2.filter(tmpImg, null);
        rg.dispose();
        tmpImg = null;

        Graphics2D g2d = (Graphics2D)g.create();
        g2d.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, opacity ) );
        g2d.drawImage(shadow, xoffset, yoffset, null);
        g2d.dispose();
    }

     public static void drawAreaDropShadowEx(Graphics g, Area area, Color clr,
            int radius, int dist, double angle, float opacity){
        Rectangle rect = area.getBounds();
        Area filArea = (Area)area.clone();
        //GeomOperator.offset(filArea, -rect.x, -rect.y);
        BufferedImage tmpImg = new BufferedImage(rect.width+radius*2,
                rect.height+radius*2,
                BufferedImage.TYPE_INT_ARGB );
	    Graphics2D rg = tmpImg.createGraphics();
        rg.translate(-rect.x+radius, -rect.y+radius);
        rg.setBackground(new Color(255, 255, 255, 0));
        //rg.clearRect(0, 0, rect.width, rect.height);

        rg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        rg.setPaint(clr);

        rg.fill(filArea);
        int xoffset = rect.x + (int)(dist*Math.sin(angle));
        int yoffset = rect.y + (int)(dist*Math.cos(angle));

        GaussianFilter filter2 = new GaussianFilter(radius*2);
        BufferedImage shadow = filter2.filter(tmpImg, null);
        rg.dispose();
        tmpImg = null;

        Graphics2D g2d = (Graphics2D)g.create();
        g2d.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, opacity ) );

        rect.height+=radius;
        g2d.setClip(rect);
        g2d.drawImage(shadow, xoffset-radius, yoffset-radius-2, null);
        g2d.dispose();
    }
    /**
     * 绘制一个区域做阴影处理
     * @param g
     * @param w
     * @param h
     * @param cornersize
     * @param filColor
     * @param shadowColor
     * @param radius
     * @param dist
     * @param angle
     * @param opacity
     */
    public static void drawDropShadow(Graphics g,
            int w, int h, int cornersize, Color filColor,
            Color shadowColor,
            int radius, int dist, double angle, float opacity){
        //draw the border.
        int ow = w-radius*2;
        int oh = h-radius*2;
        BufferedImage original = new BufferedImage(ow,
                oh,
                BufferedImage.TYPE_INT_ARGB );
        Graphics2D og = original.createGraphics();

        RoundRectangle2D r2d = new RoundRectangle2D.Double(
                0,
                0,
                ow,
                oh,
                cornersize,
                cornersize);
        OriPainter.fillAreaWithSingleColor(og, new Area(r2d),
                filColor);
        
        int width = original.getWidth()+radius*2;
        int height = original.getHeight()+radius*2;

        BufferedImage tmpImg = new BufferedImage(width,
                height,
                BufferedImage.TYPE_INT_ARGB );
		Graphics2D rg = tmpImg.createGraphics();
        r2d = new RoundRectangle2D.Double(
                radius,
                radius,
                ow,
                oh,
                cornersize,
                cornersize);
       OriPainter.fillAreaWithSingleColor(rg, new Area(r2d),
                shadowColor);
        GaussianFilter filter = new GaussianFilter(radius);
        BufferedImage shadow = filter.filter(tmpImg, null);
        rg.dispose();

        Graphics2D g2d = (Graphics2D)g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        int offsetx =  (int)(dist * Math.cos(angle));
        int offsety =  (int)(dist * Math.sin(angle));
        g2d.drawImage(shadow, offsetx, offsety, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g2d.drawImage(original, radius, radius, null);
    }

    /**
     * 在特定的Rectangle中绘制一个向下箭头
     * @param g
     * @param rect
     * @param clr
     * @param lwidth
     */
    public static void drawDownArrow(Graphics g, Rectangle rect, Color clr,
             float lwidth){
         GeneralPath path = new GeneralPath();

         int offset = 3;
         double len = 4 * Math.sqrt(2);
         int h = rect.height;
         int w = rect.width;
         int x = w/2;
         int y = h/2+offset;
         path.moveTo(x-(int)(len*Math.cos(Math.PI/4)), y-(int)(len*Math.sin(Math.PI/4)));
         path.lineTo(x, y);
         path.lineTo(x+(int)(len*Math.cos(Math.PI/4)), y-(int)(len*Math.sin(Math.PI/4)));

         Graphics2D g2d = (Graphics2D)g;
         g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
         g2d.setPaint(clr);
         g2d.setStroke(new BasicStroke(lwidth, BasicStroke.CAP_ROUND,
                 BasicStroke.JOIN_BEVEL));
         g2d.draw(path);

     }

    public static void drawInnerShadow(Graphics2D g, Area a, Color shadowColor, int size,
            int distance, float opacity){
         int width = a.getBounds().width + 6;
        int height = a.getBounds().height + 6;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        BufferedImage glow = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = glow.createGraphics();

        Area area1 = new Area(new Rectangle(0, 0, width, height));
        a.transform(AffineTransform.getTranslateInstance(2, 2));
        area1.subtract(a);

        BufferedImage tmp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g3 = tmp.createGraphics();
        g3.setPaint(new GradientPaint(0, 0, shadowColor, width, height, shadowColor));
        g3.fill(area1);

        GaussianFilter gf = new GaussianFilter(size+2);
        glow = gf.filter(tmp, glow);

        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_IN, opacity);
        g2.setColor(Color.blue);
        g2.setComposite(ac);
        g2.fill(area1);
        Area clip = new Area(new Rectangle(3, 4, width-6, height));
        Area cliparea = (Area)a.clone();
        cliparea.subtract(clip);
        g.setClip(cliparea);
        g.drawImage(glow, null, -2, distance-2);
        //g.drawImage(glow, null, 0, distance);
    }

    //绘制基本阴影，将来这个类的内容将会重新整理 - 胡长建 06-12/2012
    public static void basicDrawDropShadow(Graphics g, Area area, Color clr,
            int radius, int dist, double angle, float opacity){
        Rectangle rect = area.getBounds();

        BufferedImage tmpImg = new BufferedImage(rect.width+radius*2,
                rect.height+radius*2,
                BufferedImage.TYPE_INT_ARGB );
        Graphics2D rg = tmpImg.createGraphics();
        rg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        rg.setPaint(clr);
        rg.translate(-rect.x+radius, -rect.y+radius);
        rg.fill(area);

        int xoffset = (int)(dist*Math.cos(angle));
        int yoffset = (int)(dist*Math.sin(angle));

        GaussianFilter filter2 = new GaussianFilter(radius*2);
        BufferedImage shadow = filter2.filter(tmpImg, null);
        rg.dispose();
        tmpImg = null;

        Graphics2D g2d = (Graphics2D)g.create();
        g2d.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, opacity ) );
        g2d.translate(rect.x, rect.y);
        g2d.drawImage(shadow, -radius+xoffset, -radius-yoffset, null);
        g2d.dispose();
    }
    public static void basicDrawDropShadowEx(Graphics g, Area area, Color clr,
            int radius, int dist, double angle, float opacity, int offsety){
        Rectangle rect = area.getBounds();

        BufferedImage tmpImg = new BufferedImage(rect.width+radius*2,
                rect.height+radius*2,
                BufferedImage.TYPE_INT_ARGB );
        Graphics2D rg = tmpImg.createGraphics();
        rg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        rg.setPaint(clr);
        rg.translate(-rect.x+radius, -rect.y+radius);
        rg.fill(area);

        int xoffset = (int)(dist*Math.cos(angle));
        int yoffset = (int)(dist*Math.sin(angle));

        GaussianFilter filter2 = new GaussianFilter(radius*2);
        BufferedImage shadow = filter2.filter(tmpImg, null);
        rg.dispose();
        tmpImg = null;

        Graphics2D g2d = (Graphics2D)g.create();
        g2d.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, opacity ) );
        g2d.translate(rect.x, rect.y);
        //g2d.setClip(new Rectangle(-radius, rect.height/2+offsety, rect.width+radius, rect.height));
        g2d.drawImage(shadow, -radius+xoffset, -radius-yoffset, null);
        g2d.dispose();
    }
    //垂直分割并进行过度绘制
    public static void blkGradientMiddleFillArea(Graphics g, Area area, Color clrOut,
            Color clrMiddle, float divid, boolean vertical){

        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        Rectangle r = area.getBounds();
        GradientPaint gp = null;

        if(vertical){
            Area top = new Area(new Rectangle2D.Double(r.x, r.y, r.x + r.width,
                    r.y + r.height*divid-4));

            Area bottom = new Area(new Rectangle2D.Double(r.x, r.y+r.height*divid+4, r.x + r.width,
                    r.y + r.height));
            Area tmp = (Area)area.clone();
            tmp.subtract(bottom);

            gp = new GradientPaint(r.x, r.y,
                clrOut, r.x, r.y+r.height*divid, clrMiddle);
            g2d.setPaint(gp);

            g2d.fill(tmp);
            gp = new GradientPaint(r.x, r.y+r.height*divid,
                clrMiddle, r.x, r.y+r.height, clrOut);
            tmp = (Area)area.clone();
            tmp.subtract(top);
            g2d.setPaint(gp);
            g2d.fill(tmp);
        }

    }
}


