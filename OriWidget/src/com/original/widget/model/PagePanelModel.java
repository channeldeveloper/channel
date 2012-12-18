/*
 *  com.original.widget.model.PagePanelModel.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * (Class Annotation.)
 * 模型
 * @author   Administrator
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-6-26 22:21:19
 */
public class PagePanelModel {
    //过度色
    private Color[] colors;
    private Float[] divids;
    
    //shadow
    List<PagePanelShadow> shadows;
    List<PagePanelBorder> borders;

    public PagePanelModel(){
        shadows = new ArrayList<PagePanelShadow>();
        borders = new ArrayList<PagePanelBorder>();
    }
    public Color[] getBackFillColors(){return colors;}
    public Float[] getBackFillDivid(){return divids;}
    public void setBackFillPattern(Color[] colors, Float[] divids){
        this.colors = colors;
        this.divids = divids;
    }
    public PagePanelModel(Color[] colors, Float[] divids){
        this.colors = colors;
        this.divids = divids;
        shadows = new ArrayList<PagePanelShadow>();
        borders = new ArrayList<PagePanelBorder>();
    }
    public List<PagePanelShadow> getShadows(){
        return shadows;
    }
    public List<PagePanelBorder> getBorders(){
        return borders;
    }
    //增加阴影
    public void addShadow(int direction, int size,
                Color shadowcolor, float opacity, double angle){
        shadows.add(new PagePanelShadow(direction, size, shadowcolor,
                opacity, angle));
    }
    //增加Border
    public void addBorder(int direction,boolean inner, boolean dashed, double
            linewidth, Color linecolor){
        borders.add(new PagePanelBorder(direction, inner, dashed, linewidth, linecolor));
    }

    public class PagePanelBorder{
        public int direction; //0-left, 1-right, 2-top, 3-bottom.
        public boolean inner;
        public boolean dashed;
        public double linewidth;
        public Color linecolor;
        
        public PagePanelBorder(int direction, boolean inner, boolean dashed,
                double linewidth, Color linecolor){
            this.direction = direction;
            this.inner = inner;
            this.dashed = dashed;
            this.linewidth = linewidth;
            this.linecolor = linecolor;
        }
    }
    public class PagePanelShadow{
        public int direction; //LEFT - 0, RIGHT -1, TOP = 2, BOTTOM=3;
        public int size; //阴影大小
        public Color shadowcolor;
        public float opacity;
        public double angle;

        public PagePanelShadow(int direction, int size,
                Color shadowcolor, float opacity, double angle){
            this.direction = direction;
            this.size = size;
            this.shadowcolor = shadowcolor;
            this.opacity = opacity;
            this.angle = angle;
        }

        public PagePanelShadow(int direction, int size, 
                Color shadowcolor, float opacity){
            this.direction = direction;
            this.size = size;
            this.shadowcolor = shadowcolor;
            this.opacity = opacity;
            switch(direction){
                case 0:
                    this.angle = Math.PI;
                    break;
                case 1:
                    this.angle = 0;
                    break;
                case 2:
                    this.angle = Math.PI *3/2;
                    break;
                case 3:
                    this.angle = Math.PI/2;
                    break;
          }
        }
    }
}
