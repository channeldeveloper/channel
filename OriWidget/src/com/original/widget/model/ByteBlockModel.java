/*
 *  com.original.widget.model.ByteBlockModel.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget.model;

import java.awt.Color;
import java.awt.Font;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * (Class Annotation.)
 * 
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   May 5, 2012 10:52:13 PM
 */
public class ByteBlockModel {
    private int width;
    private int height;
    //是否有有数据
    private  boolean hasdata;
    //Browse按钮对应的文本内容
    private String brwbtntext;
    private Font btnBrwFont;
    
    //二进制内容类型
    private String dataformat = null;
    //二进制内容
    private byte[] datablock = null;

    private boolean mouseOver = false;
    private boolean mouseOverBtn = false;
    private Color backgroundcolor;
    
    //固定
    public static int CORNERRADIUS = 10;
    public static Color BORDERCOLOR = new Color(152,152,152);
    public static Color SHADOWCOLOR = new Color(0.0f, 0.0f, 0.0f, 0.4f);

    /**
     * Only one <code>ChangeEvent</code> is needed per button model
     * instance since the event's only state is the source property.
     * The source of events generated is always "this".
     */
    protected transient ChangeEvent changeEvent = null;

    /** Stores the listeners on this model. */
    protected EventListenerList listenerList = new EventListenerList();

    public ByteBlockModel()
	{
		this.width = 204;
        this.height = 216;
        this.hasdata = false;
        this.btnBrwFont = new Font("微软雅黑", Font.PLAIN, 14);
        this.backgroundcolor = new Color(249,249,249);
        
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

    public String getBrwbtntext() {
        return brwbtntext;
    }

    public void setBrwbtntext(String brwbtntext) {
        this.brwbtntext = brwbtntext;
        //this.fireStateChanged();
    }

    public Font getBtnBrwFont() {
        return btnBrwFont;
    }

    public void setBtnBrwFont(Font btnBrwFont) {
        this.btnBrwFont = btnBrwFont;
    }

    public Color getBackgroundcolor() {
        return backgroundcolor;
    }

    public void setBackgroundcolor(Color backgroundcolor) {
        this.backgroundcolor = backgroundcolor;
    }
    
    public byte[] getDatablock() {
        return datablock;
    }

    public void setDatablock(byte[] datablock) {
        this.datablock = datablock;
        this.fireStateChanged();
    }

    public String getDataformat() {
        return dataformat;
    }

    public void setDataformat(String dataformat) {
        this.dataformat = dataformat;
    }

    public boolean isHasdata() {
        return hasdata;
    }

    public void setHasdata(boolean hasdata) {
        this.hasdata = hasdata;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
        this.fireStateChanged();
    }

    public boolean isMouseOverBtn() {
        return mouseOverBtn;
    }

    public void setMouseOverBtn(boolean mouseOverBtn) {
        this.mouseOverBtn = mouseOverBtn;
        this.fireStateChanged();
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
}
