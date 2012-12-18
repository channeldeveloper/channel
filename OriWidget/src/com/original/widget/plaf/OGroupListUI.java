/*
 *  com.original.widget.plaf.OImagePanelUI.java
 *  
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget.plaf;

import com.original.widget.OGroupList;
import com.original.widget.model.GroupListModel;
import java.awt.*;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;

/**
 * (Class Annotation.)
 *
 * @author   yangkj
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-5-14 13:49:43
 */
public class OGroupListUI extends BasicPanelUI {

    private OGroupList comp;
    private GroupListModel model;

    public static ComponentUI createUI(JComponent c) {
        return new OGroupListUI(c);
    }

    public OGroupListUI(JComponent com) {
        comp = (OGroupList) com;
        model = comp.getModel();
    }

    public void redraw() {
        if (model == null) {
            model = comp.getModel();
        }
        model.setSize(comp.getHeight(), comp.getWidth());
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        if (model == null) {
            model = comp.getModel();
        }
        super.paint(g, c);
    }
}
