/**
 * com.original.widget.comp.arrowedpanel.event
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget.event;

import javax.swing.event.ChangeEvent;


/**
 * TimeAxix Change Event
 *
 *
 * @author Ni Min,Song Xueyong
 * @version 1.00 2012/4/23
 */
public class TimeAxisChangeEvent extends ChangeEvent {
    public static enum CHANGETYPE  {TimeViewChange, ViewBlockChange, TotalChange};
    private CHANGETYPE type;

    public TimeAxisChangeEvent(Object source, CHANGETYPE type) {
        super(source);
        this.type = type;
    }

    public CHANGETYPE getType() {
        return type;
    }

    public void setType(CHANGETYPE type) {
        this.type = type;
    }
}

