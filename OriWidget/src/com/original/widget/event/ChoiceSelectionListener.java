/*
 *  com.original.widget.event.BlockSelectionListener.java
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
 * @create   2012-5-21 23:20:06
 */
public interface ChoiceSelectionListener extends EventListener{
    public void choiceSelectChange(ChoiceSelectionEvent e);
}
