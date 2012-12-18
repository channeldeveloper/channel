/*
 *  com.original.widget.model.ImagePanelModel.java
 *  
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget.model;

import com.original.widget.OGroupPanel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * (Class Annotation.)
 *
 * @author   yangkj
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-5-14 13:52:21
 */
public class GroupListModel extends OBaseModel {

    private int rowHeight = 100;
    private int selectedindex;
    List<OGroupPanel> items = new ArrayList<OGroupPanel>();
    private boolean isMultiple = false;

    public GroupListModel(int width, int height, int _rowHeight) {
        super(width, height);
        this.rowHeight = _rowHeight;
    }

    public boolean isMultiple() {
        return this.isMultiple;
    }

    public void setMutilple(boolean _isMutiple) {
        this.isMultiple = _isMutiple;
    }

    public void setRowHeight(int _rowHeight) {
        this.rowHeight = _rowHeight;
        fireStateChanged();
    }

    public int getRowHeight() {
        return this.rowHeight;
    }

    //获取当前选中的Item
    public int getSelectedIndex() {
        return selectedindex;
    }

    public void setSelectedIndex(int selectedindex) {
        this.selectedindex = selectedindex;
    }

    public void add(OGroupPanel item) {
        this.items.add(item);
    }

    public void remove(OGroupPanel item) {
        if (selectedindex >= items.size()) {
            selectedindex = -1;
            
        }
        this.items.remove(item);
    }

    public void remove(int index) {
        if (selectedindex >= items.size()) {
            selectedindex = -1;
            
        }
        this.items.remove(index);
    }

    public int size() {
        return this.items.size();
    }

    public OGroupPanel get(int index) {
        if (selectedindex >= items.size()) {
            selectedindex = -1;

        }
        if (selectedindex == -1) {
            return null;
            
        }
        return this.items.get(index);
    }

    public int indexOf(OGroupPanel item) {
        return this.items.indexOf(item);
    }

    public Iterator<OGroupPanel> iter() {
        return this.items.iterator();
    }

    public List<OGroupPanel> getSelectedList() {
        List<OGroupPanel> lists = new ArrayList<OGroupPanel>();
        for (int i = 0; i < items.size(); i++) {
            OGroupPanel o = items.get(i);
            if (o.getModel().isSelected()) {
                lists.add(o);
            }
        }
        return lists;
    }

    public void setSelected(OGroupPanel obj, boolean isSelected) {
        for (int i = 0; i < items.size(); i++) {
            OGroupPanel o = items.get(i);
            if (o.equals(obj)) {
                if (isSelected) {
                    this.selectedindex = i;
                } else {
                    this.selectedindex = -1;
                }
                o.setSelected(isSelected);
            } else if (!isMultiple) {
                o.setSelected(false);
            }
        }
        if (!isSelected) {
            for (int i = 0; i < items.size(); i++) {
                OGroupPanel o = items.get(i);
                if (o.getModel().isSelected()) {
                    this.selectedindex = i;
                    break;
                }
            }
        }
    }
}
