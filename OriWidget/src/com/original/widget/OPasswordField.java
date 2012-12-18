/*
 *  com.original.widget.comp.JOTextField.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget;

import com.original.widget.event.DrawAreaChangeListener;
import com.original.widget.model.TextBlockModel;
import com.original.widget.plaf.OPasswordFieldUI;
import com.original.widget.plaf.OTextFieldUI;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.BorderFactory;
import javax.swing.JPasswordField;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

/**
 * (Class Annotation.)
 *
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   May 4, 2012 1:59:19 PM
 */
public class OPasswordField extends JPasswordField implements MouseListener,
        MouseMotionListener{

    private static final String uiClassID = "OPasswordFieldUI";

    static {
        UIDefaults defaults = UIManager.getDefaults();
        defaults.put(uiClassID, "com.original.widget.plaf.OPasswordFieldUI");
    }
    private static final long serialVersionUID = -6497888280847098839L;
    Dimension size = new Dimension();
    private TextBlockModel model;
    private DrawAreaChangeListener listener;

    public OPasswordField() {
        this(null);
    }

    //初始化对象
    public OPasswordField(String text) {
        //model
        super(text);
        initComp();
    }

    private void initComp() {
        setModel(new TextBlockModel(TextBlockModel.TextType.Password));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        this.setFont(model.getFont());
        this.setForeground(model.getForecolor());
        this.setBackground(model.getBackgroundcolor());

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    //模型设置部分
    public void setModel(TextBlockModel model) {
        this.model = model;
    }

    public TextBlockModel getModel() {
        return model;
    }

    public void setCaption(String _caption) {
        model.setCaption(_caption);
        if(_caption.length()>0){
            setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 28));
            repaint();
        }
    }

    public String getCaption() {
        return model.getCaption();
    }
    //设置UI

    public void setUI(OTextFieldUI ui) {
        super.setUI(ui);
    }

    @Override
    public OPasswordFieldUI getUI() {
        return (OPasswordFieldUI) ui;
    }

    @Override
    public String getUIClassID() {
        return uiClassID;
    }

    //设置内容信息
    @Override
    public void setText(String text) {
        super.setText(text);
        model.setText(text);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        getUI().procMouseClick(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
        getUI().procMouseExit();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
        getUI().procMouseMoved(e);
    }
}
