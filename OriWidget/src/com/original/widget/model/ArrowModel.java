/**
 *  com.original.widget.comp.arrowedpanel.model;
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget.model;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * Model数据有箭头的方向，位置，宽度，颜色等。
 *
 *
 * @author Ni Min,Song Xueyong
 * @version 1.00 2012/4/23
 */
public class ArrowModel {
	
	private int thickness = 10;
	private String arrowDirection = BorderLayout.EAST;
	private int arrowPos = 95;
	private int arrowWidth = 20;
	private Color lineColor = Color.red;
	private Color panelColor = Color.blue;// lightGray;
    /**
     * Only one <code>ChangeEvent</code> is needed per button model
     * instance since the event's only state is the source property.
     * The source of events generated is always "this".
     */
    protected transient ChangeEvent changeEvent = null;
	
    /** Stores the listeners on this model. */
    protected EventListenerList listenerList = new EventListenerList();
	
	public ArrowModel()
	{
		thickness = 10;
		arrowDirection = BorderLayout.EAST;
		arrowPos = 95;
		arrowWidth = 20;
		lineColor = Color.red;
		panelColor = Color.blue;// lightGray;
	}
	/**
	 * @param thickness the thickness to set
	 */
	public void setThickness(int thickness) {
		this.thickness = thickness;
		
		fireStateChanged();
	}
	/**
	 * @return the thickness
	 */
	public int getThickness() {
		return thickness;
	}
	/**
	 * @param arrowDirection the arrowDirection to set
	 */
	public void setArrowDirection(String arrowDirection) {
		this.arrowDirection = arrowDirection;
		
		fireStateChanged();
	}
	/**
	 * @return the arrowDirection
	 */
	public String getArrowDirection() {
		return arrowDirection;
	}
	/**
	 * @param arrowPos the arrowPos to set
	 */
	public void setArrowPos(int arrowPos) {
		this.arrowPos = arrowPos;
		
		fireStateChanged();
	}
	/**
	 * @return the arrowPos
	 */
	public int getArrowPos() {
		return arrowPos;
	}
	/**
	 * @param arrowWidth the arrowWidth to set
	 */
	public void setArrowWidth(int arrowWidth) {
		this.arrowWidth = arrowWidth;
		
		fireStateChanged();
	}
	/**
	 * @return the arrowWidth
	 */
	public int getArrowWidth() {
		return arrowWidth;
	}
	/**
	 * @param lineColor the lineColor to set
	 */
	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
		
		fireStateChanged();
	}
	/**
	 * @return the lineColor
	 */
	public Color getLineColor() {
		return lineColor;
	}
	/**
	 * @param panelColor the panelColor to set
	 */
	public void setPanelColor(Color panelColor) {
		this.panelColor = panelColor;
		
		fireStateChanged();
	}
	/**
	 * @return the panelColor
	 */
	public Color getPanelColor() {
		return panelColor;
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
