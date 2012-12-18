/*
 *  com.original.widget.model.ImagePanelModel.java
 *  
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget.model;

import com.original.widget.OImageLoad;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;

/**
 * (Class Annotation.)
 *
 * @author   yangkj
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-5-14 13:52:21
 */
public class ImagePanelModel extends OBaseModel {
    // 显示的标签

    private String title = null;
    // 标签显示
    private String orientation = BorderLayout.SOUTH;
    // 用户数据
    private Object data = null;
    // 图形对象
    private Image image = null;
    private int left_offset = 0;
    private int right_offset = 0;
    private Color selectgroundcolor = new Color(238, 238, 238);
    private Color notselectgroundcolor = Color.WHITE;
    private boolean isSelected = false;
    private final Image excel = OImageLoad.getImage("com/original/widget/images/excel.png");
    private final Image word = OImageLoad.getImage("com/original/widget/images/word.png");
    private final Image ppt = OImageLoad.getImage("com/original/widget/images/ppt.png");
    private final Image video = OImageLoad.getImage("com/original/widget/images/video.png");
    private final Image photo = OImageLoad.getImage("com/original/widget/images/photo.png");
    private final Image music = OImageLoad.getImage("com/original/widget/images/music.png");
    private final Image yozo = OImageLoad.getImage("com/original/widget/images/yozo.png");

    public ImagePanelModel(String _title, Object _data,  Image _image) {
        this(_title,_data,120,100,_image);
    }
    
    public ImagePanelModel(String _title, Object _data, int width, int height, Image _image) {
        super(width, height);
        this.title = _title;
        this.data = _data;
        this.image = _image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String _title) {
        this.title = _title;
        fireStateChanged();
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String _orientation) {
        this.orientation = _orientation;
        fireStateChanged();
    }

    public int getLeftOffset() {
        return this.left_offset;
    }

    public void setLeftOffset(int _left_offset) {
        this.left_offset = _left_offset;
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

    public Color getNotSelectedColor() {
        return this.notselectgroundcolor;
    }

    public void setNotSelectColor(Color color) {
        this.notselectgroundcolor = color;
        fireStateChanged();
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean _isSelected) {
        this.isSelected = _isSelected;
        fireStateChanged();
    }

    public Object getData() {
        return data;
    }

    public void setData(Object _data) {
        this.data = _data;
        fireStateChanged();
    }

    private Image getImageByTitle(){
        int lastidx = this.getTitle().lastIndexOf(".");
        if ( lastidx == -1 ) return null;
        String type = this.getTitle().substring(lastidx+1).toLowerCase();
        if ( type.equals("doc") || type.equals("docx"))
            return this.word;
        if ( type.equals("eio") )
            return this.yozo;
        if ( type.equals("xls") || type.equals("xlsx"))
            return this.excel;
        if ( type.equals("ppt") || type.equals("pptx"))
            return this.ppt;
        if ( type.equals("avi") || type.equals("mpeg") || type.equals("mpg") || type.equals("dat")||
             type.equals("rm") || type.equals("rmvb") || type.equals("mov") || type.equals("qt")
              || type.equals("wmv") || type.equals("flv") || type.equals("divx") || type.equals("mp4"))
            return this.video;
        if ( type.equals("bmp") || type.equals("gif") || type.equals("png") || type.equals("tif") || type.equals("tiff")
             || type.equals("cdr") || type.equals("wmf") || type.equals("jpg") || type.equals("jpeg") || type.equals("pcd")
              || type.equals("psd") || type.equals("tga"))
            return this.photo;
        if ( type.equals("wma") || type.equals("wav") || type.equals("asf") || type.equals("mp3") || type.equals("au")
                 || type.equals("ra") || type.equals("ram") || type.equals("mid") || type.equals("tti"))
            return this.music;
        return null;
    }
    public Image getImage() {
        Image m = this.getImageByTitle();
        if ( m != null ) return m;
        return image;
    }

    public void setImage(Image _image) {
        this.image = _image;
        fireStateChanged();
    }
}
