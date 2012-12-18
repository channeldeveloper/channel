/**
 * com.original.widget.comp.arrowedpanel.event
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget.event;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.original.widget.OArrowedPanel;


/**
 * ArrowModel变化监听器。
 *
 *
 * @author Ni Min,Song Xueyong
 * @version 1.00 2012/4/23
 */
public class ArrowChangeListener implements ChangeListener
{

	OArrowedPanel panel;
	
	public ArrowChangeListener(OArrowedPanel comp)
	{
		panel = comp;
	}
	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		panel.getUI().calcArrowRect();
		panel.repaint();
		
	}

}
