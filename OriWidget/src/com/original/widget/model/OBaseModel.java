/*
 *  com.original.widget.model.OBaseModel.java
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
 * @author   yangkj
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-5-14 14:17:17
 */
public class OBaseModel {
    //外观

    private int width = 50;
    private int height = 30;
    private Color forecolor;
    private Color backgroundcolor;
    private Font font;
    //固定
    public static int CORNERRADIUS = 10;
    public static Color BORDERCOLOR = new Color(152, 152, 152);
    public static Color SHADOWCOLOR = new Color(0.0f, 0.0f, 0.0f, 0.4f);
    /**
     * Only one <code>ChangeEvent</code> is needed per button model
     * instance since the event's only state is the source property.
     * The source of events generated is always "this".
     */
    protected transient ChangeEvent changeEvent = null;
    /** Stores the listeners on this model. */
    protected EventListenerList listenerList = new EventListenerList();

    /**
     * Constructor.
     */
    public OBaseModel() {
        this(50,30);
    }
    
    /**
     * Constructor.
     * @param _width
     * @param _height
     */
    public OBaseModel(int _width, int _height) {
        this.width = _width;
        this.height = _height;
        this.forecolor = Color.BLACK;
        this.backgroundcolor = new Color(249, 249, 249);
        this.font = new Font("微软雅黑", Font.PLAIN, 16);
    }

    public Color getBackgroundcolor() {
        return backgroundcolor;
    }

    public void setBackgroundcolor(Color backgroundcolor) {
        this.backgroundcolor = backgroundcolor;
        fireStateChanged();
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
        fireStateChanged();
    }

    public Color getForecolor() {
        return forecolor;
    }

    public void setForecolor(Color forecolor) {
        this.forecolor = forecolor;
        fireStateChanged();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        fireStateChanged();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        fireStateChanged();
    }

    public void setSize(int height, int width) {
        this.height = height;
        this.width = width;
    }

    /**
     * {@inheritDoc}
     * @param l
     */
    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    /**
     * {@inheritDoc}
     * @param l 
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
        return (ChangeListener[]) listenerList.getListeners(
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
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                // Lazily create the event:
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }
}
