/*
 *  com.original.widget.comp.JOTextField.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget;

import com.original.widget.draw.OriPainter;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;

/**
 * (Class Annotation.)
 *
 * @author   yangkj
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-5-21 14:01:07
 */
public class OCheckBox extends JCheckBox {

    private static final long serialVersionUID = -6497888280847098839L;
    private int width = 20;
    private int cornersize = 6;
    private Color clr = new Color(61, 197, 253);
    private Color clrEnd = new Color(80, 158, 204);
    private Color clrSelected = new Color(48, 160, 205);
    private boolean isAutoIcon = false;
    private boolean isAutoSelectedIcon = false;

    public OCheckBox() {
        this(null, 20);
    }

    public OCheckBox(String txt) {
        this(txt, 20);
    }

    public OCheckBox(int _iconwidth) {
        this(null, _iconwidth);
    }

    public OCheckBox(String txt, int _iconwidth) {
        super(txt, null, false);
        init(_iconwidth);
    }

    private void init(int _iconwidth) {
        width = _iconwidth;

        this.setForeground(Color.BLACK);
        this.setBackground(Color.WHITE);
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
        Point ltCorner = new Point(0, 0);
        OriPainter.drawGradidentRoundRect(g2d, ltCorner, width, cornersize, clr, clrEnd, 1.8f);
        OriPainter.drawCheckMark(g2d, new Rectangle(ltCorner.x, ltCorner.y, width, width), clrSelected, 2.6f);
        g2d.dispose();
        return new ImageIcon(bi);
    }

    ImageIcon createUnSelectedIcon() {
        BufferedImage bi = new BufferedImage(width, width, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bi.createGraphics();
        Point ltCorner = new Point(0, 0);
        OriPainter.drawGradidentRoundRect(g2d, ltCorner, width, cornersize, clr, clrEnd, 1.8f);
        g2d.dispose();
        return new ImageIcon(bi);
    }
}