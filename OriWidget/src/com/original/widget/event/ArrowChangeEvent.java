/**
 * com.original.widget.comp.arrowedpanel.event
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget.event;

import javax.swing.event.ChangeEvent;


/**
 * ArrowModel变化派发事件。
 *
 *
 * @author Ni Min,Song Xueyong
 * @version 1.00 2012/4/23
 */
public class ArrowChangeEvent extends ChangeEvent {
    /**
     * Constructs a ArrowChangeEvent object.
     *
     * @param source  the Object that is the source of the event
     *                (typically <code>this</code>)
     */
    public ArrowChangeEvent(Object source) {
        super(source);
    }
}

