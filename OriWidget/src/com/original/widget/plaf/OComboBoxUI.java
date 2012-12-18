/*
 *  com.original.widget.plaf.OComboBoxUI.java
 *  
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget.plaf;

import com.original.widget.OComboBox;
import com.original.widget.OComboPopup;
import com.original.widget.draw.GeomOperator;
import com.original.widget.draw.OriPainter;
import com.original.widget.model.CustomComboBoxModel;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;

/**
 * (Class Annotation.)
 *
 * @author   yangkj
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-4-20 16:06:50
 */
public class OComboBoxUI extends BasicComboBoxUI {

    public static int CORNERRADIUS = 10;
    private static Color defaultBackground = new Color(249, 249,249);
    public static Color BORDERCOLOR = new Color(152,152,152);
    public static Color SHADOWCOLOR = new Color(0.0f, 0.0f, 0.0f, 0.4f);
    public static Color NORMALARROWCOLOR = new Color(126,126,126);

    
    public static ComponentUI createUI(JComponent c) {
        return new OComboBoxUI(c);
    }

    public OComboBoxUI(JComponent c) {
        OComboBox comp = (OComboBox)c;
        comp.setRenderer(new OComboBoxListCellRender(comp));
    }


    @Override
    public void update(Graphics g, JComponent c){
        OComboBox comp = (OComboBox)c;
        CustomComboBoxModel model = comp.getVizModel();

        if(model.getType()==CustomComboBoxModel.COMBOBOXTYPE.SWITCH){
            int width = comp.getWidth()-1;
            int height = comp.getHeight()-1;

            RoundRectangle2D r2d = new RoundRectangle2D.Double(1,1,width, height,
                    height, height);
            OriPainter.gradientFillArea(g, new Area(r2d),
                    model.backgroudcolorS, model.backgroudcolorE, true);
        }else if(model.getType()==CustomComboBoxModel.COMBOBOXTYPE.INPUT){
            int width = comp.getWidth();
            int height = comp.getHeight();

            //圆角长方形
            RoundRectangle2D r2d = new RoundRectangle2D.Double(
                    1,
                    1,
                    width-2,
                    height-2,
                    CORNERRADIUS,
                    CORNERRADIUS);
            //Draw the background frame.
            OriPainter.fillAreaWithSingleColor(g, new Area(r2d),
                    defaultBackground);
            OriPainter.drawAreaBorderWithSingleColor(g, new Area(r2d),
                    BORDERCOLOR, 1);

            //draw the inner Shadow.
            //calculate the shadow area.
            Area areaOne = new Area(r2d);
            Area areaTwo = GeomOperator.offsetCopy(areaOne, 0, 2.4);
            areaOne.subtract(areaTwo); //areaOne will be the shadow area.
            //generate the shadow image.
            BufferedImage shadow = OriPainter.drawGradientInnerShadow(g,
                    areaOne, CustomComboBoxModel.SHADOWCOLOR, model.backgroudcolor,
                    40);
            //paint the shadow.
            OriPainter.drawImage(g, shadow, 0, 0);
        }
        super.update(g, c);
    }
    @Override
    public void paint(Graphics g, JComponent c){
        OComboBox comp = (OComboBox)c;
        hasFocus = comp.hasFocus();
        if ( !comp.isEditable() ) {
            Rectangle r = rectangleForCurrentValue();
            //paintCurrentValueBackground(g,r,hasFocus);
            paintCurrentValue(g,r,hasFocus, comp);
        }
    }
    public void paintCurrentValue(Graphics g,Rectangle bounds,boolean hasFocus,
            OComboBox comp) {
        ListCellRenderer renderer = comp.getRenderer();
        Component c;
        if ( hasFocus && !isPopupVisible(comp) ) {
            c = renderer.getListCellRendererComponent( listBox,
                                                       comp.getSelectedItem(),
                                                       -1,
                                                       true,
                                                       false );
        }
        else {
            c = renderer.getListCellRendererComponent( listBox,
                                                       comp.getSelectedItem(),
                                                       -1,
                                                       false,
                                                       false );
        }
        c.setFont(comp.getFont());
        if(comp.getVizModel().getType()==CustomComboBoxModel.COMBOBOXTYPE.SWITCH)
            c.setForeground(Color.WHITE);
        else
            c.setForeground(Color.BLACK);
        c.setBackground(new Color(255,255,255,0));

        boolean shouldValidate = false;
        if (c instanceof JPanel)  {
            shouldValidate = true;
        }
        currentValuePane.paintComponent(g,c,comp,bounds.x,bounds.y,
                                        bounds.width,bounds.height, shouldValidate);
    }

    @Override
    protected ComboPopup createPopup() {
        return new OComboPopup(comboBox);
    }

    @Override
    protected JButton createArrowButton() {
        JButton button = null;
        button = new OInnerButton();
        button.setName("ComboBox.arrowButton");
        return button;
    }

    //内部使用的箭头按钮
    class OInnerButton extends JButton{
        CustomComboBoxModel.COMBOBOXTYPE type = CustomComboBoxModel.COMBOBOXTYPE.SWITCH;
        public OInnerButton(){
            this.setOpaque(false);
            setMargin(new Insets(0, 2, 0, 2));
            setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        }

        @Override
        public void paintComponent(Graphics g){
            OComboBox o = (OComboBox)getParent();
            type = o.getVizModel().getType();
            OriPainter.drawDownArrow(g, this.getBounds(), o.getVizModel().arrowcolor, 2.5f);
            
        }
    }

    //绘制函数
    class OComboBoxListCellRender extends DefaultListCellRenderer {
        OComboBox comp;
        Color bg = new Color(46, 156, 202);;
        Color fg = Color.WHITE;
        Color bg2 = new Color(218,218,218);
        public OComboBoxListCellRender(OComboBox comp){
            this.comp = comp;
        }
        @Override
        public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus)
        {
            setComponentOrientation(list.getComponentOrientation());


            if (isSelected) {
                if(comp.getVizModel().getType()==CustomComboBoxModel.COMBOBOXTYPE.SWITCH){
                    setBackground(bg);
                    setForeground(fg);
                }else{
                    setBackground(bg2);
                    setForeground(Color.BLACK);
                }
            }
            else {
                setBackground(new Color(249,249,249));
                setForeground(list.getForeground());
            }

            if (value instanceof Icon) {
                setIcon((Icon)value);
                setText("");
            }
            else {
                setIcon(null);
                setText((value == null) ? "" : value.toString());
            }

            setEnabled(list.isEnabled());
            setFont(list.getFont());
            return this;
        }
    }
}
