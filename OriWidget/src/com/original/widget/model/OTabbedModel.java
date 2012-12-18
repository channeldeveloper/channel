/*
 *  com.original.widget.model.OTabbedModel.java
 *  
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget.model;

import java.awt.Color;
import java.awt.Insets;
import javax.swing.DefaultSingleSelectionModel;

/**
 * (Class Annotation.)
 *
 * @author   Cydow
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-11-15 14:05:13
 */
public class OTabbedModel extends DefaultSingleSelectionModel {

    private Color selectedColorSet1 = new Color(242, 251, 253);
    private Color selectedColorSet2 = new Color(211, 234, 240);
    private Color defaultColorSet1 = new Color(234, 234, 234);
    private Color defaultColorSet2 = new Color(241, 241, 241);
    private Color dividerColor = new Color(211, 234, 240);
    private Insets contentInsets = new Insets(6, 0, 0, 0);
    private Color forceColor = Color.BLACK;
    private Color unSelectedColor = Color.WHITE;
    private int div_height = 31;

    public int getDividerHeight() {
        return div_height;
    }

    public void setDividerHeight(int hight) {
        div_height = hight;
        fireStateChanged();
    }

    public Insets getContentInsets() {
        return contentInsets;
    }

    public void setContentInsets(Insets sets) {
        contentInsets = sets;
        fireStateChanged();
    }

    public Color getForceColor() {
        return forceColor;
    }

    public void setForceColor(Color color) {
        forceColor = color;
        fireStateChanged();
    }

    public Color getUnselectedColor() {
        return unSelectedColor;
    }

    public void setUnselectedColor(Color color) {
        unSelectedColor = color;
        fireStateChanged();
    }

    public Color getDividerColor() {
        return dividerColor;
    }

    public void setDividerColor(Color color) {
        dividerColor = color;
        fireStateChanged();
    }

    public Color getSelectColorSet1() {
        return selectedColorSet1;
    }

    public Color getSelectColorSet2() {
        return selectedColorSet2;
    }

    public void setSelectedColorSet(Color color1, Color color2) {
        selectedColorSet1 = color1;
        if (color2 != null) {
            selectedColorSet2 = color2;
        }
        fireStateChanged();
    }

    public Color getDefaultColorSet1() {
        return defaultColorSet1;
    }

    public Color getDefaultColorSet2() {
        return defaultColorSet2;
    }

    public void setDefaultColorSet(Color color1, Color color2) {
        defaultColorSet1 = color1;
        if (color2 != null) {
            defaultColorSet2 = color2;
        }
        fireStateChanged();
    }
}
