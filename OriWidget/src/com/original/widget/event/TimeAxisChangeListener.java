/**
 * com.original.widget.comp.arrowedpanel.event
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget.event;

import com.original.widget.OTimeAxis;
import com.original.widget.model.TimeAxisModel;
import java.awt.event.AdjustmentEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * TimeAxis变化监听器。
 * Updated by Changjian HU at 06-14/2012
 *
 * @author Ni Min,Song Xueyong
 *
 * @version 1.00 2012/4/23
 */
public class TimeAxisChangeListener implements ChangeListener{
    OTimeAxis axis = null;
    //pulic AxisChangeListener
    public TimeAxisChangeListener(OTimeAxis comp){
        axis = comp;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        TimeAxisChangeEvent event = (TimeAxisChangeEvent)e;
        axis.fireChange(event.getType());
    }


}
