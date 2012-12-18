/*
 *  com.original.widget.event.TimeAxisEvent.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget.event;

import com.original.widget.event.TimeAxisChangeEvent.CHANGETYPE;
import com.original.widget.model.TimeAxisModel;
import java.util.EventObject;

/**
 * (Class Annotation.)
 *
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-6-20 1:48:29
 */
public class TimeAxisEvent extends EventObject {
    TimeAxisModel data;
    TimeAxisChangeEvent.CHANGETYPE type;
    public TimeAxisEvent(Object source, TimeAxisModel data, TimeAxisChangeEvent.CHANGETYPE type){
        super(source);
        this.data = data;
        this.type = type;
   }

    public CHANGETYPE getType() {
        return type;
    }

    public void setType(CHANGETYPE type) {
        this.type = type;
    }

    

    public TimeAxisModel getData() {
        return data;
    }

    public void setData(TimeAxisModel data) {
        this.data = data;
    }
    
}
