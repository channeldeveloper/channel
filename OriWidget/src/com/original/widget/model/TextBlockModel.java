/**
 *  com.original.widget.comp.arrowedpanel.model;
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget.model;

import java.awt.Color;
import java.awt.Font;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * 对TextObject建模，他用于JOTextField,JOTextArea,
 * JOPassword主要包含：
 * Text, MaxLength, PasswordChar, Readonly,
 * SelStart, SelLength, SelText, CaretPos
 *
 * Width, Height, IsMultiLine, Color, Font
 * CornerRadius
 * InnerShadowSize
 * InnerShadowPadding
 * InnerShadowDirection
 * InnerShadowColor
 * InnerShadowTransparency
 *
 *
 * @author Changjian Hu
 * @version 1.00 2012/4/23
 */
public class TextBlockModel {

    public static enum TextType {

        TextField, Password, TextArea, HtmlEditor
    };
    //数据
    private String text;
    private int maxlength = -1;
    private char PasswordChar;
    private String caption="";
    //模式
    private boolean readonly = false;
    //选择相关
    private int selstart = -1;
    private int sellength = 0;
    private String seltext = null;
    private int caretpos = -1;
    //外观
    private int width = 50;
    private int height = 30;
    private boolean ismultiline = false;
    private Color forecolor;
    private Color backgroundcolor;
    private Font font;
    private boolean useasfilter;
    //固定
    public static int CORNERRADIUS = 10;
    public static Color BORDERCOLOR = new Color(152, 152, 152);
    public static Color SHADOWCOLOR = Color.black;//new Color(0.0f, 0.0f, 0.0f, 0.4f);
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
     * @param type
     */
    public TextBlockModel(TextType type) {
        this.forecolor = Color.BLACK;
        this.backgroundcolor = new Color(237,237,237);
//        this.font = new Font("微软雅黑", Font.PLAIN, 16);
        if (type == TextType.Password) {
            this.PasswordChar = '*';
        } else if (type == TextType.TextArea) {
            this.ismultiline = true;
        }
        this.useasfilter = true;
    }

    public boolean isUseasfilter() {
        return useasfilter;
    }

    public void setUseasfilter(boolean useasfilter) {
        this.useasfilter = useasfilter;
    }

    
    public char getPasswordChar() {
        return PasswordChar;
    }

    public void setCaption(String _caption) {
        this.caption = _caption;
    }

    public String getCaption() {
        return this.caption;
    }

    public void setPasswordChar(char PasswordChar) {
        this.PasswordChar = PasswordChar;
        fireStateChanged();
    }

    public Color getBackgroundcolor() {
        return backgroundcolor;
    }

    public void setBackgroundcolor(Color backgroundcolor) {
        this.backgroundcolor = backgroundcolor;
        fireStateChanged();
    }

    public int getCaretpos() {
        return caretpos;
    }

    public void setCaretpos(int caretpos) {
        this.caretpos = caretpos;
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

    public boolean isIsmultiline() {
        return ismultiline;
    }

    public void setIsmultiline(boolean ismultiline) {
        this.ismultiline = ismultiline;
        fireStateChanged();
    }

    public int getMaxlength() {
        return maxlength;
    }

    public void setMaxlength(int maxlength) {
        this.maxlength = maxlength;
        fireStateChanged();
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
        fireStateChanged();
    }

    public int getSellength() {
        return sellength;
    }

    public void setSellength(int sellength) {
        this.sellength = sellength;
        fireStateChanged();
    }

    public int getSelstart() {
        return selstart;
    }

    public void setSelstart(int selstart) {
        this.selstart = selstart;
        fireStateChanged();
    }

    public String getSeltext() {
        return seltext;
    }

    public void setSeltext(String seltext) {
        this.seltext = seltext;
        fireStateChanged();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
