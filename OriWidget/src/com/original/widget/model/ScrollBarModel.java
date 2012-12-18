/*
 *  com.original.widget.model.ScrollBarModel.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget.model;

import java.awt.Color;
import javax.swing.DefaultBoundedRangeModel;

/**
 * (Class Annotation.)
 * 滚动条的数据模型
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   May 5, 2012 12:10:38 AM
 */
public class ScrollBarModel extends DefaultBoundedRangeModel{
    //private int type;  //0-horizonl 1-vertizon
    private int barcronerradius;
    private int barwidth;
    private Color barcolor;
    private Color trackColor;
    private int height;
    private int width;
    
    public ScrollBarModel(){
        super();
        this.barcronerradius = 2;
        this.barwidth = 6;
        this.barcolor = new Color(126, 126, 126);
        this.trackColor = new Color(249,249,249);
    }

    public int getBarcronerradius() {
        return barcronerradius;
    }

    public void setBarcronerradius(int barcronerradius) {
        this.barcronerradius = barcronerradius;
    }

    public int getBarwidth() {
        return barwidth;
    }

    public void setBarwidth(int barwidth) {
        this.barwidth = barwidth;
    }

    public Color getBarcolor() {
        return barcolor;
    }

    public void setBarcolor(Color barcolor) {
        this.barcolor = barcolor;
        fireStateChanged();
    }

    public Color getTrackColor() {
        return trackColor;
    }

    public void setTrackColor(Color trackColor) {
        this.trackColor = trackColor;
        fireStateChanged();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        fireStateChanged();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        fireStateChanged();
    }


    public void setSize(int width, int height){
        this.width = width;
        this.height = height;
    }

    
}
