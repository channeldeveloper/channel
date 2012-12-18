/*
 *  com.original.widget.event.ChoiceSelectionEvent.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget.event;

import java.util.Date;
import java.util.EventObject;

/**
 * (Class Annotation.)
 *
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-6-4 1:18:18
 */
public class ChoiceSelectionEvent  extends EventObject {
    private Object data;
    private int selectedIndex = -1;
    public ChoiceSelectionEvent(Object source, int selectedIndex, Object data ){
        super(source);
        this.data = data;
        this.selectedIndex = selectedIndex;
   }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    
   
    
}
