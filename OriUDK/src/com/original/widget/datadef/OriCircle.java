/*
 *  com.original.widget.datadef.OriCircle.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget.datadef;

import java.awt.geom.Point2D;

/**
 * (Class Annotation.)
 * 一个圆的定义，圆心和半径
 * @author   Administrator
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-6-20 23:45:09
 */
public class OriCircle {
    private double radius;
    private Point2D center;

    public OriCircle(Point2D center, double radius){
        this.center = center;
        this.radius = radius;
    }

    public Point2D getCenter() {
        return center;
    }

    public void setCenter(Point2D center) {
        this.center = center;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    
}
