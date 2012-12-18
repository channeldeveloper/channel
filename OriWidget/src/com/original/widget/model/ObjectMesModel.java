/*
 *  com.original.widget.model.ObjectMesModel.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget.model;

import com.original.widget.datadef.OriObject;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * (Class Annotation.)
 *  对象输入组件的数据模型
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   May 7, 2012 8:47:57 PM
 */
public class ObjectMesModel {
    private int width;
    private int height;
    
    //MeshUp 组件对象的组合
    private List<OInnerObject> subobjects = new ArrayList<OInnerObject>();

    private Font font;

    private Color backgroundcolor;
    
    public static int CORNERRADIUS = 10;
    public static Color BORDERCOLOR = new Color(152,152,152);
    public static Color SHADOWCOLOR = new Color(0.0f, 0.0f, 0.0f, 0.4f);
    //对象类型，目前分为两类
    public enum INNEROBJTYPE{
        PLAIN, BLOCK
    }

    public ObjectMesModel(){
        this.font = new Font("微软雅黑", Font.PLAIN,  16);
        this.backgroundcolor = new Color(249,249,249);
    }
    protected transient ChangeEvent changeEvent = null;

    /** Stores the listeners on this model. */
    protected EventListenerList listenerList = new EventListenerList();

    public Color getBackgroundcolor() {
        return backgroundcolor;
    }

    public void setBackgroundcolor(Color backgroundcolor) {
        this.backgroundcolor = backgroundcolor;
    }

    
    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
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

    //增加一些对外使用的函数
    //获取索引编号
    public int indexOf(OInnerObject obj){
        return this.subobjects.indexOf(obj);
    }
    //遍历内部组件块
    public Iterator<OInnerObject> iter(){
        return this.subobjects.iterator();
    }
    //根据Index获取
    public OInnerObject get(int ind){
        return this.subobjects.get(ind);
    }
    //删除特定块对象
    public void remove(OInnerObject obj){
        this.subobjects.remove(obj);
    }
    public void remove(int index){
        this.subobjects.remove(index);
    }
    public int size(){
        return this.subobjects.size();
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

    /**
     * Returns an array of all the change listeners
     * registered on this <code>DefaultButtonModel</code>.
     *
     * @return all of this model's <code>ChangeListener</code>s
     *         or an empty
     *         array if no change listeners are currently registered
     *
     * @see #addChangeListener
     * @see #removeChangeListener
     *
     * @since 1.4
     */
    public ChangeListener[] getChangeListeners() {
        return (ChangeListener[])listenerList.getListeners(
                ChangeListener.class);
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is created lazily.
     *
     * @see EventListenerList
     */
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
    public OInnerObject createObject(INNEROBJTYPE type, Object obj){
        return new OInnerObject(type,obj);
    }
    public void add(int index, OInnerObject obj){
        subobjects.add(index, obj);
    }
    //inner Classes
    public class OInnerObject{
        private Object obj;
        private INNEROBJTYPE type;
        private boolean changed;

        //
        public OInnerObject(INNEROBJTYPE type, Object obj){
            this.obj = obj;
            this.type =type;
        }
        public OInnerObject(){}

        public Object getObj() {
            return obj;
        }
        public void setObj(Object obj) {
            this.obj = obj;
        }

        public INNEROBJTYPE getType() {
            return type;
        }

        public void setType(INNEROBJTYPE type) {
            this.type = type;
        }

        public boolean isChanged() {
            return changed;
        }

        public void setChanged(boolean changed) {
            this.changed = changed;
        }

        public String getDisplay(){
            if(type==INNEROBJTYPE.PLAIN)
                return obj.toString();
            else{
                if(obj instanceof OriObject){
                    return ((OriObject)obj).getDisplay();
                }
                if(obj instanceof Map){
                    return ((Map<String, Object>)obj).get("title").toString();
                }
                else
                    return obj.toString();
            }
        }

        @Override
        public String toString(){
            return obj.toString();
        }
    }

    //设置数据和读取数据
    //读取数据
    public String getValue(){
        StringBuilder builder = new StringBuilder();
        for(OInnerObject obj: this.subobjects){
            String tmp = obj.toString().trim();
            if(tmp.isEmpty()) continue;
            builder.append(obj.toString()).append(",");
        }
        String ret = builder.toString().trim();
        if(ret.isEmpty()) return "";
        return ret.substring(0, ret.length()-1);
    }

    //设置数据
    public void setValue(List<OriObject> data){
        this.subobjects.clear();
        for(OriObject obj: data){
            if(obj.isComplex()){
                subobjects.add(createObject(INNEROBJTYPE.BLOCK, obj));
            }else{
                subobjects.add(createObject(INNEROBJTYPE.PLAIN, new StringBuffer(obj.getDisplay())
                        ));
            }
            
        }
    }
}
