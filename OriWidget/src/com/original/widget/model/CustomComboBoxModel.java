/*
 *  com.original.widget.plaf.OComboBoxModel.java
 *  
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget.model;

import java.awt.Color;
import java.awt.Font;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;

/**
 * (Class Annotation.)
 *
 * @author   yangkj
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-5-30 12:42:42
 */
public class CustomComboBoxModel extends DefaultComboBoxModel {

    public static enum COMBOBOXTYPE {
        INPUT, BUTTON, SWITCH
    };
    public static Color SHADOWCOLOR = new Color(0.0f, 0.0f, 0.0f);
    public static Color BORDERCOLOR = new Color(152, 152, 152);
    public Color backgroudcolor = null;
    public Color backgroudcolorS = null;
    public Color backgroudcolorE = null;
    public Color forecolor = null;
    public Color arrowcolor = null;
    public Color bordercolor = null;
    public int cradius = 8;
    public Color itembackcolor = new Color(249, 249, 249);
    public Color itemforecolor = new Color(0, 0, 0);
    public Color itemselectbackcolor = null;
    public Color itemselectforecolor = null;
    public int itemcradius = 8;
    public int rowheight = 30;
    public Font font = null;
    public COMBOBOXTYPE type = COMBOBOXTYPE.INPUT;

    
    public CustomComboBoxModel(COMBOBOXTYPE _type) {
        super();
        type = _type;
        font = new Font("微软雅黑", Font.PLAIN, 16);
        if (_type == COMBOBOXTYPE.INPUT) {
            backgroudcolor = new Color(249, 249, 249);
            backgroudcolorS = new Color(249, 249, 249);
            backgroudcolorE = new Color(249, 249, 249);
            forecolor = Color.BLACK;
            arrowcolor = new Color(126, 126, 126);

            itemselectbackcolor = new Color(218, 218, 218);
            itemselectforecolor = new Color(0, 0, 0);
        } else if (_type == COMBOBOXTYPE.BUTTON) {
            backgroudcolor = new Color(230, 230, 230);
            backgroudcolorS = new Color(236, 236, 236);
            backgroudcolorE = new Color(221, 221, 221);
            forecolor = Color.BLACK;
            bordercolor = new Color(243, 243, 243);
            arrowcolor = new Color(126, 126, 126);
            cradius = 14;

            itemselectbackcolor = new Color(218, 218, 218);
            itemselectforecolor = new Color(0, 0, 0);
        } else {
            backgroudcolor = new Color(63, 175, 222);
            backgroudcolorS = new Color(46, 156, 202);
            backgroudcolorE = new Color(63, 175, 222);
            forecolor = Color.white;
            bordercolor = null;
            arrowcolor = Color.white;
            cradius = 30;

            itemselectbackcolor = new Color(63, 175, 222);
            itemselectforecolor = Color.white;
        }
    }

    /**
     * Constructs a DefaultComboBoxModel object initialized with
     * an array of objects.
     *
     * @param items  an array of Object objects
     */
    public CustomComboBoxModel(final Object items[]) {
        super(items);
    }

    /**
     * Constructs a DefaultComboBoxModel object initialized with
     * a vector.
     *
     * @param v  a Vector object ...
     */
    public CustomComboBoxModel(Vector<?> v) {
        super(v);
    }

    public COMBOBOXTYPE getType() {
        return type;
    }

    public void setType(COMBOBOXTYPE type) {
        this.type = type;
    }
}
