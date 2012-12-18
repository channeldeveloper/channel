/*
 *  com.original.widget.model.ImagePanelModel.java
 *  
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget.model;

import java.awt.Color;

/**
 * (Class Annotation.)
 *
 * @author   yangkj
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-5-14 13:52:21
 */
public class GroupPanelModel extends OBaseModel {

    private boolean isHorizontal = false;
    private int right_offset = 0;
    private Color selectgroundcolor = new Color(219,229,232);
    private Color notselectgroundcolor = new Color(238, 238, 238);
    private boolean isSelected = false;
    private boolean isDrop = false;
    private boolean isImageSelected = false;
    private boolean hasSelected = true;
    private int vcount = 6;

    public GroupPanelModel(int width, int height, int _rightoffset, boolean _isHorizontal, boolean _isDrop) {
        super(width, height);
        this.right_offset = _rightoffset;
        this.isHorizontal = _isHorizontal;
        isDrop = _isDrop;
    }

    public int getVisibleCount(){
        return this.vcount;
    }
    public void setVisbleCount(int _vcount){
        this.vcount = _vcount;
    }
    
    public Color getNotSelectedColor() {
        return this.notselectgroundcolor;
    }

    public void setNotSelectColor(Color color) {
        this.notselectgroundcolor = color;
        fireStateChanged();
    }

    public boolean hasSelected() {
        return this.hasSelected;
    }

    public void setHasSelected(boolean _hasselected) {
        this.hasSelected = _hasselected;
        fireStateChanged();
    }

    public int getRightOffset() {
        return this.right_offset;
    }

    public void setRightOffset(int _right_offset) {
        this.right_offset = _right_offset;
        fireStateChanged();
    }

    public Color getSelectedColor() {
        return this.selectgroundcolor;
    }

    public void setSelectColor(Color color) {
        this.selectgroundcolor = color;
        fireStateChanged();
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean _isSelected) {
        this.isSelected = _isSelected;
        fireStateChanged();
    }

    public boolean isDrop() {
        return this.isDrop;
    }

    public void setDrop(boolean _isDrop) {
        this.isDrop = _isDrop;
        fireStateChanged();
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    public void setHorizontal(boolean _isHorizontal) {
        this.isHorizontal = _isHorizontal;
        fireStateChanged();
    }

    public boolean isImageSelected() {
        return this.isImageSelected;
    }

    public void setImageSelected(boolean _isImageSelected) {
        this.isImageSelected = _isImageSelected;
        fireStateChanged();
    }

    public void copy(GroupPanelModel c) {
        this.setFont(c.getFont());
        this.setBackgroundcolor(c.getBackgroundcolor());
        this.setForecolor(c.getForecolor());
        this.notselectgroundcolor = c.getNotSelectedColor();
        this.selectgroundcolor = c.getSelectedColor();
        this.isImageSelected = c.isImageSelected();
    }
}
