/*
 *  com.original.widget.model.GranularListModel.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * (Class Annotation.)
 *  描述一个复杂单粒度选择的列表模型
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   Nov 24, 2012 21:01:58 PM
 */
public class CustomdrawnListModel {
    //basic ui.
    private int width;
    private int height;
    
    //basic data.
    List<OCustomDrawListItem> items = new ArrayList<OCustomDrawListItem>();
    
    protected transient ChangeEvent changeEvent = null;
    protected EventListenerList listenerList = new EventListenerList();

    public CustomdrawnListModel(){
	}

    public void clear(){
        items.clear();
    }

    
    
    public void add(OCustomDrawListItem item){
        this.items.add(item);
    }
    public OCustomDrawListItem createBlankItem(){
        return new OCustomDrawListItem();
    }
    public int size(){
        return this.items.size();
    }
    public OCustomDrawListItem get(int index){
        return this.items.get(index);
    }
    public int indexOf(OCustomDrawListItem item){
        return this.items.indexOf(item);
    }
    public Iterator<OCustomDrawListItem> iter(){
        return this.items.iterator();
    }
    
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        this.fireStateChanged();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        this.fireStateChanged();
    }
    public void setSize(int height, int width){
        this.height = height;
        this.width = width;
    }

     /**
     * {@inheritDoc}
     */
    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    /**
     * {@inheritDoc}
     */
    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    
    public ChangeListener[] getChangeListeners() {
        return (ChangeListener[])listenerList.getListeners(
                ChangeListener.class);
    }

    
    protected void fireStateChanged() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ChangeListener.class) {
                // Lazily create the event:
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((ChangeListener)listeners[i+1]).stateChanged(changeEvent);
            }
        }
    } 


    public class OCustomDrawListItem{
        BufferedImage img;
        String title;
        String addition;
        Object anchor;

        Font titleFont = null;
        Font additionFont = null;
        Color backgroundColor = null;
        Color drawColor = null;

        public Color getDrawColor() {
            if(drawColor==null)
                return Color.WHITE;
            return drawColor;
        }

        public void setDrawColor(Color drawColor) {
            this.drawColor = drawColor;
        }


        public Font getAdditionFont() {
            if(additionFont==null)
                return new Font("verdana", Font.PLAIN,  12);
            return additionFont;
        }

        public void setAdditionFont(Font additionFont) {
            this.additionFont = additionFont;
        }

        public Color getBackgroundColor() {
            return backgroundColor;
        }

        public void setBackgroundColor(Color backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        public Font getTitleFont() {
            if(titleFont==null)
                return new Font("微软雅黑", Font.PLAIN,  16);
            return titleFont;
        }

        public void setTitleFont(Font titleFont) {
            this.titleFont = titleFont;
        }

        public String getAddition() {
            return addition;
        }

        public void setAddition(String addition) {
            this.addition = addition;
        }

        public BufferedImage getImg() {
            return img;
        }

        public void setImg(BufferedImage bytimg) {
            this.img = bytimg;
        }
       
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Object getAnchor() {
            return anchor;
        }

        public void setAnchor(Object anchor) {
            this.anchor = anchor;
        }

    }
}
