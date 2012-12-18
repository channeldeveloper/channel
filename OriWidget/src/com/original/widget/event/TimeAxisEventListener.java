/*
 *  com.original.widget.event.TimeAxisEventListener.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget.event;

import java.util.EventListener;

/**
 * (Class Annotation.)
 *
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-6-20 1:47:25
 */
public interface TimeAxisEventListener extends EventListener{
    public void timeAxisChanged(TimeAxisEvent e);
}
