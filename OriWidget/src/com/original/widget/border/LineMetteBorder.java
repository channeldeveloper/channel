/*
 *  com.original.widget.border.LineMetteBorder.java
 *  
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget.border;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import javax.swing.Icon;
import javax.swing.border.EmptyBorder;

/**
 * (Class Annotation.)
 *
 * @author   yangkj
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-6-11 17:05:57
 */
public class LineMetteBorder extends EmptyBorder {

    protected Color color;
    protected Icon tileIcon;
    private float[] dash = {2.0f};
    private float miterlimit = 10.0f;

    public LineMetteBorder(int top, int left, int bottom, int right, Color matteColor) {
        super(top, left, bottom, right);
        this.color = matteColor;
    }

    public void setDash(float[] _dash){
        dash = _dash;
    }

    public void setmiterlimit(float _miterlimit){
        miterlimit = _miterlimit;
    }
    /**
     * Paints the matte border.
     */
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Insets insets = getBorderInsets(c);
        Color oldColor = g.getColor();
        g.translate(x, y);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        if (insets.top > 0) {
            BasicStroke linetype = new BasicStroke(insets.top, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, miterlimit, dash, 0.0f);
            g2d.setStroke(linetype);
            g2d.drawLine(0, insets.top / 2, width, insets.top / 2);
        }
        if (insets.left > 0) {
            BasicStroke linetype = new BasicStroke(insets.left, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, miterlimit, dash, 0.0f);
            g2d.setStroke(linetype);
            g2d.drawLine(insets.left / 2, 0, insets.left / 2, height);
        }
        if (insets.bottom > 0) {
            BasicStroke linetype = new BasicStroke(insets.bottom, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, miterlimit, dash, 0.0f);
            g2d.setStroke(linetype);
            g2d.drawLine(0, height - insets.bottom / 2, width, height - insets.bottom / 2);
        }
        if (insets.right > 0) {
            BasicStroke linetype = new BasicStroke(insets.right, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, miterlimit, dash, 0.0f);
            g2d.setStroke(linetype);
            g2d.drawLine(width - insets.right / 2, 0, width - insets.right / 2, height);
        }

        g.translate(-x, -y);
        g.setColor(oldColor);

    }

    /**
     * Returns the insets of the border.
     * @param c the component for which this border insets value applies
     * @since 1.3
     */
    @Override
    public Insets getBorderInsets(Component c) {
        return getBorderInsets();
    }

    /**
     * Reinitialize the insets parameter with this Border's current Insets.
     * @param c the component for which this border insets value applies
     * @param insets the object to be reinitialized
     * @since 1.3
     */
    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        return computeInsets(insets);
    }

    /**
     * Returns the insets of the border.
     * @return
     * @since 1.3
     */
    @Override
    public Insets getBorderInsets() {
        return computeInsets(new Insets(0, 0, 0, 0));
    }

    /* should be protected once api changes area allowed */
    private Insets computeInsets(Insets insets) {
        if (tileIcon != null && top == -1 && bottom == -1 && left == -1 && right == -1) {
            int w = tileIcon.getIconWidth();
            int h = tileIcon.getIconHeight();
            insets.top = h;
            insets.right = w;
            insets.bottom = h;
            insets.left = w;
        } else {
            insets.left = left;
            insets.top = top;
            insets.right = right;
            insets.bottom = bottom;
        }
        return insets;
    }

    /**
     * Returns the color used for tiling the border or null
     * if a tile icon is being used.
     * @return
     * @since 1.3
     */
    public Color getMatteColor() {
        return color;
    }

    /**
     * Returns the icon used for tiling the border or null
     * if a solid color is being used.
     * @return
     * @since 1.3
     */
    public Icon getTileIcon() {
        return tileIcon;
    }

    /**
     * Returns whether or not the border is opaque.
     */
    @Override
    public boolean isBorderOpaque() {
        // If a tileIcon is set, then it may contain transparent bits
        return color != null;
    }
}
