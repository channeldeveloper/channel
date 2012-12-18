/*
 *  com.original.widget.model.PopupMenuModel.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget.model;

import java.awt.Color;

/**
 * (Class Annotation.)
 * 用于描述PopupMenu的外观模型
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-5-28 22:03:40
 */
public class PopupMenuModel {
    public static int CORNERRADIUS = 10;

    private Color backgroundColor ;
    private Color shadowColor;
    private int shadowSize;
    private int shadowDistance;
    private double shadowDirection;
    private float shadowOpacity;

    public PopupMenuModel(){
        backgroundColor = new Color(249,249,249);
        shadowColor = Color.black;
        shadowSize = 4;
        shadowDistance = 1;
        shadowDirection = Math.PI/2;
        shadowOpacity = 0.6f;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color getShadowColor() {
        return shadowColor;
    }

    public void setShadowColor(Color shadowColor) {
        this.shadowColor = shadowColor;
    }

    public double getShadowDirection() {
        return shadowDirection;
    }

    public void setShadowDirection(double shadowDirection) {
        this.shadowDirection = shadowDirection;
    }

    public int getShadowDistance() {
        return shadowDistance;
    }

    public void setShadowDistance(int shadowDistance) {
        this.shadowDistance = shadowDistance;
    }

    public float getShadowOpacity() {
        return shadowOpacity;
    }

    public void setShadowOpacity(float shadowOpacity) {
        this.shadowOpacity = shadowOpacity;
    }

    public int getShadowSize() {
        return shadowSize;
    }

    public void setShadowSize(int shadowSize) {
        this.shadowSize = shadowSize;
    }

    
}
