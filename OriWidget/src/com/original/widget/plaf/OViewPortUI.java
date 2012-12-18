/*
 *  com.original.widget.plaf.OViewPortUI.java
 *  
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget.plaf;

import com.original.widget.draw.OriPainter;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Area;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicViewportUI;

/**
 * (Class Annotation.)
 *
 * @author   yangkj
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-6-5 15:11:58
 */
@Deprecated
public class OViewPortUI extends BasicViewportUI {

    public static ComponentUI createUI(JComponent c) {
        return new OButtonUI(c);
    }

    public OViewPortUI(JComponent com) {
    }

    //here, I will override all things
    //paint, paintText, paintIcon, paintFocus,paintButtonPressed
    @Override
    public void update(Graphics g, JComponent c) {
        if (c.isOpaque()) {
            //g.setColor(c.getBackground());
            Color clr = new Color(61, 197, 253);
            Color clrEnd = new Color(80, 158, 204);
            OriPainter.gradientFillArea(g, new Area(new Rectangle(0,0,c.getWidth(),c.getHeight())), clr, clrEnd, false);
        }
        paint(g, c);
    }
}
