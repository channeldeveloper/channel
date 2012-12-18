/*
 *  com.original.widget.event.ScrollChangeListener.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget.event;

import com.original.widget.OScrollBar;
import java.awt.event.AdjustmentEvent;
import javax.swing.BoundedRangeModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * (Class Annotation.)
 *
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   May 7, 2012 11:57:02 PM
 */
public class ScrollChangeListener implements ChangeListener {
    OScrollBar source;
    public ScrollChangeListener(OScrollBar source){
        this.source = source;
    }
    @Override
    public void stateChanged(ChangeEvent e) {
        Object obj = e.getSource();
        if(obj instanceof BoundedRangeModel){
            BoundedRangeModel model = (BoundedRangeModel)obj;
            int id = AdjustmentEvent.ADJUSTMENT_VALUE_CHANGED;
            int type = AdjustmentEvent.TRACK;
            int value = model.getValue();
            
            boolean isAdjusting = model.getValueIsAdjusting();
            source.fireAdjustmentValueChanged(id, type, value, isAdjusting);
        }
    }

}
