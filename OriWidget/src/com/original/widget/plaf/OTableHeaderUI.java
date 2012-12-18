/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.original.widget.plaf;

import com.original.widget.OTableHeader;
import com.original.widget.draw.OriPainter;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Area;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableHeaderUI;

/**
 * 
 * @author liummb
 */
public class OTableHeaderUI extends BasicTableHeaderUI {

    public static Color BORDERCOLOR = new Color(152, 152, 152);
    
    public static ComponentUI createUI(JComponent c) {
        return new OTableHeaderUI();
    }
    public OTableHeaderUI() {
    }

    @Override
    public void paint(Graphics g, JComponent c) {

        OTableHeader header1 = (OTableHeader) c;
        int length = 0;

        for (int i = 0; i < header1.getColumnModel().getColumnCount(); i++) {

            header1.getHeaderComs().get(i).setBounds(length, 0,
                    header1.getColumnModel().getColumn(i).getWidth() - 2, 40);

            header1.getHeaderComs().get(i).repaint();

            length += header1.getColumnModel().getColumn(i).getWidth();

            //System.out.println("1-----"+header.getColumnModel().getColumn(i).getWidth());
        }
        Color clr = new Color(61, 197, 253);
        Color clrEnd = new Color(80, 158, 204);
        //OriPainter.gradientFillArea(g, new Area(c.getBounds()), clr, clrEnd, false);

        // super.paint(g,c);
    }
}
