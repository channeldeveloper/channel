/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.widget.plaf;

import com.original.widget.OComboBoxButton;
import com.original.widget.OComboPopup;
import com.original.widget.draw.GeomOperator;
import com.original.widget.draw.OriPainter;
import com.original.widget.model.CustomComboBoxModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;

/**
 * ComboBoxButton的类
 * @author Changjian
 */
public class OComboBoxButtonUI extends BasicComboBoxUI {
    public static int CORNERRADIUS = 10;
    private static Color defaultBackground = new Color(230, 230,230);
    public static Color BORDERCOLOR = new Color(152,152,152);
    public static Color SHADOWCOLOR = new Color(0.0f, 0.0f, 0.0f, 0.4f);
    public static Color NORMALARROWCOLOR = new Color(126,126,126);

    private OClickInnerButton clickButton = null;
    boolean mouseOverButton = false;

    private Area areaArrow = null;
    public static ComponentUI createUI(JComponent c) {
        return new OComboBoxButtonUI(c);
    }

    public OComboBoxButtonUI(JComponent c) {
        OComboBoxButton comp = (OComboBoxButton)c;
        comp.setRenderer(new OComboBoxListCellRender());
        //System.out.println(comp.getLayout());

        //comp.add(new JButton("hi"));
        //System.out.println(comp.getEditor().getEditorComponent().getBounds());

    }
    

    @Override
    public void update(Graphics g, JComponent c){
        OComboBoxButton comp = (OComboBoxButton)c;
        CustomComboBoxModel model = comp.getVizModel();


        //System.out.println(arrowButton.getBounds());

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

        Area fillArea = new Area(r2d);
        fillArea= GeomOperator.centerFixShrinkCopy(fillArea, 1.8);
        Area arrow = (Area)fillArea.clone();
        Rectangle rectArrow = arrowButton.getBounds();
        
        rectArrow.y -= 20;  rectArrow.width = width-rectArrow.x;
        rectArrow.height += height;
        fillArea.subtract(new Area(rectArrow));
        //arrow =  GeomOperator.centerFixShrinkCopy(arrow, 1.8);
        //fillArea = GeomOperator.centerFixShrinkCopy(fillArea, 1.8);
        arrow.subtract(fillArea);
        //Draw the background frame.
        OriPainter.fillAreaWithSingleColor(g, new Area(r2d),
                defaultBackground);
        OriPainter.drawAreaBorderWithSingleColor(g, new Area(r2d),
                BORDERCOLOR, 1);
        GeomOperator.offset(fillArea, 0.5, 0.5);
        GeomOperator.offset(arrow, 0.5, 0.5);
        
        //fillArea = GeomOperator.shrinkCopy(fillArea, -2);
        if(clickButton==null){
            Rectangle rect = fillArea.getBounds();
            clickButton = new OClickInnerButton (fillArea);
            clickButton.setBounds(1, 3,
                    rect.width, rect.height);
            comp.add(clickButton);
            clickButton.addActionListener(new OButtonListener(comp));
            //
        }else{
            Rectangle rect = fillArea.getBounds();
            Insets insets = getInsets();

            clickButton.setButtonShape(fillArea);
            //clickButton.setBounds(fillArea.getBounds());
            clickButton.setBounds(1,3,
                    rect.width, rect.height);
        }
        //arrow.subtract(fillArea);
        if(areaArrow==null){
            areaArrow = GeomOperator.offsetCopy(arrow,  -arrow.getBounds().x,
                    0);
        }
        OriPainter.gradientFillArea(g, arrow,
                    new Color(49,249,249),new Color(10,210,210), true);
        
        super.update(g, c);
    }
  
    @Override
    public void paint(Graphics g, JComponent c){
        OComboBoxButton comp = (OComboBoxButton)c;

        hasFocus = comp.hasFocus();
        if ( !comp.isEditable()  && clickButton!=null) {
            Rectangle r = rectangleForCurrentValue();
            //paintCurrentValueBackground(g,r,hasFocus);
            //clickButton.setText(comp.toString());
            paintCurrentValue(g,r,hasFocus, comp);
        }
        comp.paintComponents(g);
    }
    public void paintCurrentValue(Graphics g,Rectangle bounds,boolean hasFocus,
            OComboBoxButton comp) {
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
        c.setForeground(Color.BLACK);
        c.setBackground(new Color(255,255,255,0));
        clickButton.setText(((JLabel)c).getText());
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
    class OButtonListener implements ActionListener {
        OComboBoxButton comp;
        OButtonListener(OComboBoxButton comp) {
            this.comp = comp;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            comp.fireActionEvent(e);
        }
}

    //内部使用的箭头按钮
    class OInnerButton extends JButton{
        public OInnerButton(){
            this.setOpaque(false);
            setMargin(new Insets(0, 2, 0, 2));
            setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        }

        @Override
        public void paintComponent(Graphics g){
            
            //OriPainter.fillAreaWithSingleColor(g, areaArrow,
            //        new Color(49,249,249));
            if(this.getModel().isPressed())
                OriPainter.drawDownArrow(g, this.getBounds(), Color.BLUE, 2.5f);
            else if(this.getModel().isRollover())
                OriPainter.drawDownArrow(g, this.getBounds(), Color.WHITE, 2.5f);
            else
                OriPainter.drawDownArrow(g, this.getBounds(), NORMALARROWCOLOR, 2.5f);
        }
    }

    //内部使用的点击按钮
    class OClickInnerButton extends JButton{
        Area fillArea = null;
        public OClickInnerButton(Area area){
            this.setOpaque(false);
            this.fillArea = area;
            GeomOperator.offset(fillArea, -area.getBounds().x/2,
                    -area.getBounds().y);
            setMargin(new Insets(2, 2, 2, 2));
            setBorder(BorderFactory.createEmptyBorder(2, 2, 12, 2));
        }

        public void setButtonShape(Area area){
            this.fillArea = area;
            GeomOperator.offset(fillArea, -area.getBounds().x/2,
                    -area.getBounds().y);
            repaint();
        }
        @Override
        public void paintComponent(Graphics g){
            if(this.getModel().isPressed())
                OriPainter.gradientFillArea(g, fillArea,
                    new Color(210,210,210),new Color(210,210,210), true);
            else if(this.getModel().isRollover())
                OriPainter.gradientFillArea(g, fillArea,
                    new Color(210,210,210),new Color(249,249,249), true);
            else
                OriPainter.gradientFillArea(g, fillArea,
                    new Color(249,249,249),new Color(210,210,210), true);

            OriPainter.drawAreaBorderWithSingleColor(g, fillArea,
                new Color(218,218,218), 1);

            OriPainter.drawStringInAreaAlign(g, fillArea, getText(), new Font("微软雅黑", Font.PLAIN, 14),
                    Color.BLACK, JTextField.LEFT_ALIGNMENT, CORNERRADIUS);
            //OriPainter.drawStringInArea(g, new Font("微软雅黑", Font.PLAIN, 14),
            //        fillArea, this.getText(), Color.black);
        }
    }

    //绘制函数
    class OComboBoxListCellRender extends DefaultListCellRenderer {
        Color bg = new Color(46, 156, 202);;
        Color fg = Color.WHITE;
        Color bg2 = new Color(218,218,218);
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
                setBackground(bg2);
                setForeground(Color.BLACK);

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
    //
}
