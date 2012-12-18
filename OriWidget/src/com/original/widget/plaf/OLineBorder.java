/*
 *  com.original.widget.plaf.OLineBorder.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget.plaf;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.border.AbstractBorder;

/**
 * (Class Annotation.)
 *
 * @author   Administrator
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-6-16 22:45:30
 */
public class OLineBorder extends AbstractBorder{
    private int thickness;
    private Color clrLine;
    public OLineBorder(int thickness, Color clrLine){
        this.thickness = thickness;
        this.clrLine = clrLine;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
	    // this is where you draw the border
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(clrLine);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        float dash[] = { thickness * 3 };
        g2d.setStroke( new BasicStroke(thickness, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1f, dash, 0));
        //g2d.drawRect(x, y, width, height);
        g2d.drawLine(0, height-thickness, width, height-thickness);

	}

	public Insets getBorderInsets(Component c) {
	    // return the top, bottom, left, right spacing in pixels the border will occupy
        return new Insets(0, thickness, 0, 0);
    }

    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = insets.top = insets.right = insets.bottom = thickness;
        return insets;
    }
    
}
