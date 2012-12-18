/*
 *  com.original.widget.event.BlockSelectionEvent.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget.event;

import java.util.Date;
import java.util.EventObject;

/**
 * (Class Annotation.)
 * 用于日期组件选取
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-5-21 23:18:46
 */
public class BlockSelectionEvent extends EventObject {
    private Date dt = null;
    public BlockSelectionEvent(Object source, Date dt ){
        super(source);
        this.dt = dt;
   }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }
}
