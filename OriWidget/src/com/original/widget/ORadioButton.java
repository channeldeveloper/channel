/*
 *  com.original.widget.comp.JOTextField.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget;

import com.original.widget.draw.OriPainter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JRadioButton;

/**
 * (Class Annotation.)
 *
 * @author   yangkj
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-5-21 14:01:07
 */
public class ORadioButton extends JRadioButton {

    private static final long serialVersionUID = -6497888280847098839L;
    private int width = 20;
    private Color clr = new Color(61, 197, 253);
    private Color clrEnd = new Color(80, 158, 204);
    private Color clrSelected = new Color(48, 160, 205);
    private Color backcolor = Color.WHITE;
    private Color forecolor = Color.BLACK;
    private int radius = width / 2;
    private Point center = new Point(width / 2, width / 2);
    private int cradius = 4;
    private boolean isAutoIcon = false;
    private boolean isAutoSelectedIcon = false;

    public ORadioButton() {
        this(null, 20);
    }
    public ORadioButton(String txt) {
        this(txt, 20);
    }

    public ORadioButton(int _iconwidth) {
        this(null, _iconwidth);
    }

    public ORadioButton(String txt, int _iconwidth) {
        super(txt, null, false);
        init(_iconwidth);
    }

    private void init(int _iconwidth) {
        width = _iconwidth;
        radius = width / 2;
        center = new Point(width / 2, width / 2);
        cradius = radius / 2;

        this.setSize(new Dimension(20, 20));
        this.setForeground(forecolor);
        this.setBackground(backcolor);
        this.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        if (this.getIcon() == null) {
            isAutoIcon = true;
            this.setIcon(createUnSelectedIcon());
        }
        if (this.getSelectedIcon() == null) {
            isAutoSelectedIcon = true;
            this.setSelectedIcon(createSelectedIcon());
        }
    }

    public void setIconSize(int _iconwidth) {
        if (width != _iconwidth) {
            width = _iconwidth;
            radius = width / 2;
            cradius = radius / 2;
            center = new Point(width / 2, width / 2);
            if (isAutoIcon) {
                this.setIcon(createUnSelectedIcon());
            }
            if (isAutoSelectedIcon) {
                this.setSelectedIcon(createSelectedIcon());
            }
        }
    }

    ImageIcon createSelectedIcon() {
        BufferedImage bi = new BufferedImage(width, width, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bi.createGraphics();
        OriPainter.drawGradidentCircle(g2d, center, radius, clr, clrEnd, 1.8f);
        OriPainter.fillCircleWithSingleColor(g2d, center, cradius, clrSelected);
        g2d.dispose();
        return new ImageIcon(bi);
    }

    ImageIcon createUnSelectedIcon() {
        BufferedImage bi = new BufferedImage(width, width, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bi.createGraphics();
        OriPainter.drawGradidentCircle(g2d, center, radius, clr, clrEnd, 1.8f);
        g2d.dispose();
        return new ImageIcon(bi);
    }
}
