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
import java.util.Map;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * (Class Annotation.)
 *  描述一个复杂多粒度选择的列表模型
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   May 7, 2012 10:48:58 PM
 */
public class GranularListModel {
    //basic ui.
    private int width;
    private int height;
    private Color forecolor;
    private Color extraColor;
    private Color backgroundcolor;
    private Font titlefont;
    private Font itmfont;
    //basic data.
    List<OGranularItem> items = new ArrayList<OGranularItem>();
    private int selectedindex;

    protected transient ChangeEvent changeEvent = null;
    protected EventListenerList listenerList = new EventListenerList();

    public GranularListModel(){
		this.forecolor = Color.BLACK;
        this.extraColor = new Color(106,145,174);
        this.backgroundcolor = new Color(249,249,249);
        this.titlefont = new Font("微软雅黑", Font.PLAIN,  14);
        this.itmfont = new Font("微软雅黑", Font.PLAIN,  12);
        this.selectedindex = -1;       
    }

    public void clear(){
        items.clear();
        selectedindex = -1;
    }

    //返回对应选择的内容
    public String getSel(){
        String name = items.get(selectedindex).title;
        String address = items.get(selectedindex).getSel();
        return String.format("<%s>%s", name, address);
    }
    public void getObjSel(Map<String, Object> map){
        String name = items.get(selectedindex).title;
        String address = items.get(selectedindex).getSel();

        map.put("title", name);
        map.put("object", address);
    }
    //获取当前选中的子Item
    public int getSelectedSubIndex(){
        if(this.selectedindex==-1) return -1;
        return this.items.get(this.selectedindex).getSelectedindex();
    }
    //获取当前选中的Item
    public int getSelectedIndex() {
        return selectedindex;
    }

    public void setFirstSelected(){
        if(items.size()<=0) return;
        items.get(0).setSelected(true);
        items.get(0).setSelectedindex(0);
    }

    public void setSelectedIndex(int selectedindex) {
        this.selectedindex = selectedindex;
    }
    
    public void add(OGranularItem item){
        this.items.add(item);
    }
    public OGranularItem createBlankItem(){
        return new OGranularItem();
    }
    public int size(){
        return this.items.size();
    }
    public OGranularItem get(int index){
        return this.items.get(index);
    }
    public int indexOf(OGranularItem item){
        return this.items.indexOf(item);
    }
    public Iterator<OGranularItem> iter(){
        return this.items.iterator();
    }
    public Color getBackgroundcolor() {
        return backgroundcolor;
    }

    public void setBackgroundcolor(Color backgroundcolor) {
        this.backgroundcolor = backgroundcolor;
    }

    public Color getExtraColor() {
        return extraColor;
    }

    public void setExtraColor(Color extraColor) {
        this.extraColor = extraColor;
    }

    public Color getForecolor() {
        return forecolor;
    }

    public void setForecolor(Color forecolor) {
        this.forecolor = forecolor;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        this.fireStateChanged();
    }

    public Font getItmfont() {
        return itmfont;
    }

    public void setItmfont(Font itmfont) {
        this.itmfont = itmfont;
    }

    public Font getTitlefont() {
        return titlefont;
    }

    public void setTitlefont(Font titlefont) {
        this.titlefont = titlefont;
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


    public class OGranularItem{
        BufferedImage img;
        String title;
        String addition;

        boolean selected  = false;
        int selectedindex = -1;
        List<Object> items = new ArrayList<Object>();

        public String getSel(){
            return items.get(selectedindex-1).toString();
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

        public List<Object> getItems() {
            return items;
        }

        public void setItems(List<Object> items) {
            this.items = items;
        }

        public void addItem(Object item){
            this.items.add(item);
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public int getSelectedindex() {
            return selectedindex;
        }

        public void setSelectedindex(int selectedindex) {
            this.selectedindex = selectedindex;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
        //public API area.
        //列表子行数
        public int getLines(){
            return items.size()+1;
        }
        //是否有下一个子列表
        public boolean hasNext(){
            if(this.selectedindex< items.size())
                return true;
            else
                return false;
        }
        //是否有上一个子列表
        public boolean hasPrevious(){
            if(this.selectedindex>1)
                return true;
            else
                return false;
        }
        //向下一个
        public boolean next(){
            if(hasNext()){
                selectedindex++;
                return true;
            }else
                return false;
        }
        //向上一个
        public boolean previous(){
            if(hasPrevious()){
                selectedindex--;
                return true;
            }else
                return false;
        }


        
    }
}
