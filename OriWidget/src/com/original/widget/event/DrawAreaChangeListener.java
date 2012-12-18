/*
 *  com.original.widget.event.DrawAreaChangeListener.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget.event;

import com.original.widget.CustomDrawable;
import javax.swing.BoundedRangeModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * (Class Annotation.)
 *
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   May 4, 2012 11:37:20 PM
 */
public class DrawAreaChangeListener implements ChangeListener {
    CustomDrawable source;
    public DrawAreaChangeListener(CustomDrawable source){
        this.source = source;
    }
    

    @Override
    public void stateChanged(ChangeEvent e) {
        //System.out.println(e.getSource() instanceof BoundedRangeModel );
        //强制绘制自定义组件
        this.source.forceCustomDraw();
    }

}
