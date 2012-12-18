/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.original.widget.plaf;

import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicTableUI;

import com.original.widget.OComplexTable;

/**
 * 
 * @author liummb
 */
public class OComplexTableUI extends BasicTableUI {

    private OComplexTable table1;

    public static OComplexTableUI createUI(JComponent c) {
        return new OComplexTableUI(c);
    }

    public OComplexTableUI(JComponent com) {
        table1 = (OComplexTable) com;
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);
    }

}
