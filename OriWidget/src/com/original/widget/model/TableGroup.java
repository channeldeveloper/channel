/*
 *  com.original.widget.model.TableGroup.java
 *  
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget.model;

import javax.swing.ImageIcon;

/**
 * (Class Annotation.)
 *
 * @author   yangkj
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-6-5 10:09:19
 */
public class TableGroup {

    private String name;
    private ImageIcon icon;
    private int groupindex=-1;

    public TableGroup(String _name, ImageIcon _icon,int _groupindex) {
        name = _name;
        icon = _icon;
        groupindex = _groupindex;
    }

    public ImageIcon getIcon()
    {
        return icon;
    }
    public void setName(String _name){
        name = _name;
    }
    public void setIcon(ImageIcon _icon){
        icon = _icon;
    }
    public int getIndex(){
        return this.groupindex;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
