/*
 *  com.original.widget.event.ObjDocumentEvent.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget.event;

import javax.swing.event.DocumentEvent;
import javax.swing.text.Document;
import javax.swing.text.Element;

/**
 * (Class Annotation.)
 *
 * @author   Administrator
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-7-12 21:55:33
 */
public class ObjDocumentEvent implements DocumentEvent {
    int xtype;
    String chgInput;

    public ObjDocumentEvent(String chgInput, int xtype){
        this.chgInput = chgInput;
        this.xtype = xtype;
    }

    public String getChgInput() {
        return chgInput;
    }

    public void setChgInput(String chgInput) {
        this.chgInput = chgInput;
    }

    public int getXtype() {
        return xtype;
    }

    public void setXtype(int xtype) {
        this.xtype = xtype;
    }

    
    

    @Override
    public int getOffset() {
        return -1;
    }

    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public Document getDocument() {
        return null;
    }

    @Override
    public EventType getType() {
        return null;
    }

    @Override
    public ElementChange getChange(Element elem) {
        return null;
    }

}
