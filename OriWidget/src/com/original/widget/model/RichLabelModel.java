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
public class RichLabelModel extends OBaseModel {
    // 是否自动计算宽度

    private boolean autowidthsize = false;
    private boolean autoheightsize = false;
    //HTML文本
    private String value = "";

    public RichLabelModel(int width, int height) {
        super(width, height);
    }
    
    public boolean isAutoWidthSize() {
        return autowidthsize;
    }

    public boolean isAutoHeightSize() {
        return autoheightsize;
    }

    public void setAutoWidthSize(boolean _autosize) {
        this.autowidthsize = _autosize;
        this.autoheightsize = !_autosize;
        fireStateChanged();
    }

    public void setAutoHeightSize(boolean _autosize) {
        this.autowidthsize = !_autosize;
        this.autoheightsize = _autosize;
        fireStateChanged();
    }
    public String getValue() {
        return this.value;
    }

    public void setValue(String _value) {
        this.value = _value;
        fireStateChanged();
    }
}
