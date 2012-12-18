/*
 *  com.original.ui.draw.GeomOperator.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget.draw;

import com.original.widget.datadef.OriCircle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;

/**
 * (Class Annotation.)
 * Java Graphics 2D 一些对象操作
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   May 4, 2012 9:45:53 PM
 */
public class GeomOperator {
    /**
     * 平移一个图形区域创建一个新的区域
     * @param r
     * @param x
     * @param y
     */
    public static Area offsetCopy(Area r, double x, double y){
        AffineTransform trans = AffineTransform.getTranslateInstance(x, y);
        return r.createTransformedArea(trans);
    }
    /**
     * 平移一个图形区域
     * @param r
     * @param x
     * @param y
     */
    public static void offset(Area r, double x, double y){
        AffineTransform trans = AffineTransform.getTranslateInstance(x, y);
        r.transform(trans);
    }
    /**
     * 压缩一个图形区域
     * @param r
     * @param x
     * @return
     */
    public static Area shrinkCopy(Area r, double x){
        double width =  r.getBounds2D().getWidth();
        double height = r.getBounds2D().getHeight();
        double scalex = (width-x*2)/width;
        double scaley = (height-x*2)/height;
        AffineTransform trans = AffineTransform.getScaleInstance(scalex, scaley);
        return r.createTransformedArea(trans);
    }

    public static Area centerFixShrinkCopy(Area r, double x){
        double width =  r.getBounds2D().getWidth();
        double height = r.getBounds2D().getHeight();
        double scalex = (width-x*2)/width;
        double scaley = (height-x*2)/height;
        AffineTransform trans = AffineTransform.getScaleInstance(scalex, scaley);
        Area ret =  r.createTransformedArea(trans);
        double mx = r.getBounds2D().getCenterX()-ret.getBounds2D().getCenterX();
        double my = r.getBounds2D().getCenterY()-ret.getBounds2D().getCenterY();
        offset(ret, mx, my);
        return ret;
    }

    //给定三点计算其圆心和半径
    private static double distance( Point2D p1, Point2D p2 ){
        return Math.sqrt( (p1.getX()-p2.getX())*(p1.getX()-p2.getX())
                + (p1.getY()-p2.getY())*(p1.getY()-p2.getY()) );
    }
    //计算函数
    public static OriCircle findCenterRadius( Point2D p1, Point2D p2, Point2D p3 )
    {
        double x = (p3.getX()*p3.getX() * (p1.getY() - p2.getY())
                + (p1.getX()*p1.getX() + (p1.getY() - p2.getY())*(p1.getY() - p3.getY()))
                * (p2.getY() - p3.getY()) + p2.getX()*p2.getX() * (-p1.getY() + p3.getY()))
            / (2 * (p3.getX() * (p1.getY() - p2.getY()) + p1.getX() * (p2.getY() - p3.getY()) + p2.getX()
                * (-p1.getY() + p3.getY())));

        double y = (p2.getY() + p3.getY())/2 - (p3.getX() - p2.getX())/(p3.getY() - p2.getY())
            * (x - (p2.getX() + p3.getX())/2);

        Point2D c = new Point2D.Double( x, y );
        double r = distance(c, p1);

        return new OriCircle( c, r );
     }

    
            
}
