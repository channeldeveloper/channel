/*
 *  com.original.widget.model.DialogModel.java
 *
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget.model;

import java.awt.Color;
import java.awt.Font;

/**
 * (Class Annotation.)
 *
 * @author   Administrator
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-5-21 22:25:52
 */
public class DialogModel extends OBaseModel {
    private int cornerradius = 18;
    private int shadowsize = 10;
    private int shadowdistance = 3;
    private double shadowangle = Math.PI*3/2;
    private Color shadowColor = Color.BLACK;
    private float opacity = 0.6f;
    private Color backgroundColor = new Color(249,249,249);
    private Font dialogTitleFont = new Font("微软雅黑", Font.PLAIN, 16);
    private Color titleBackColor = new Color(234,234,234);
    private Color separtorColor = new Color(212,212,212);
    public DialogModel(){
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;

    }

    public int getCornerradius() {
        return cornerradius;
    }

    public void setCornerradius(int cornerradius) {
        this.cornerradius = cornerradius;

    }

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;

    }

    public Color getShadowColor() {
        return shadowColor;
    }

    public void setShadowColor(Color shadowColor) {
        this.shadowColor = shadowColor;

    }

    public double getShadowangle() {
        return shadowangle;
    }

    public void setShadowangle(double shadowangle) {
        this.shadowangle = shadowangle;

    }

    public int getShadowdistance() {
        return shadowdistance;
    }

    public void setShadowdistance(int shadowdistance) {
        this.shadowdistance = shadowdistance;

    }

    public int getShadowsize() {
        return shadowsize;
    }

    public void setShadowsize(int shadowsize) {
        this.shadowsize = shadowsize;

    }

    public Font getDialogTitleFont() {
        return dialogTitleFont;
    }

    public void setDialogTitleFont(Font dialogTitleFont) {
        this.dialogTitleFont = dialogTitleFont;
    }

    public Color getTitleBackColor() {
        return titleBackColor;
    }

    public void setTitleBackColor(Color titleBackColor) {
        this.titleBackColor = titleBackColor;
    }

    public Color getSepartorColor() {
        return separtorColor;
    }

    public void setSepartorColor(Color separtorColor) {
        this.separtorColor = separtorColor;
    }


}
