/*
 *  com.original.widget.model.ImagePanelModel.java
 *  
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget.model;

/**
 * (Class Annotation.)
 *
 * @author   yangkj
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-5-14 13:52:21
 */
public class PanelModel extends OBaseModel {
    // 图形对象

    private String imagename = null;
    private double arcw = CORNERRADIUS;
    private double arch = CORNERRADIUS;

    public PanelModel(int width, int height) {
        this(width, height, null);
    }

    public PanelModel(int width, int height, String _imagename) {
        super(width, height);
        this.imagename = _imagename;
    }

    public double getArch() {
        return this.arch;
    }

    public void setArch(double _arch) {
        this.arch = _arch;
        fireStateChanged();
    }

    public double getArcw() {
        return this.arcw;
    }

    public void setArcw(double _arcw) {
        this.arcw = _arcw;
        fireStateChanged();
    }

    public String getImageName() {
        return imagename;
    }

    public void setImageName(String _imagename) {
        this.imagename = _imagename;
        fireStateChanged();
    }
}
