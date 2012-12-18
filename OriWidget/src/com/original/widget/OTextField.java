/*
 *  com.original.widget.comp.JOTextField.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget;

import com.original.widget.event.DrawAreaChangeListener;
import com.original.widget.model.TextBlockModel;
import com.original.widget.plaf.OTextFieldUI;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

/**
 * (Class Annotation.)
 * 修改记录
 * 1. 按照标准Swing做一下构造类处理
 * 
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   May 4, 2012 1:59:19 PM
 */
public class OTextField extends JTextField implements MouseListener,
        MouseMotionListener{

    private static final String uiClassID = "OTextFieldUI";

    static {
        UIDefaults defaults = UIManager.getDefaults();
        defaults.put(uiClassID, "com.original.widget.plaf.OTextFieldUI");
    }
    private static final long serialVersionUID = -6497888280847098839L;
    Dimension size = new Dimension();
    private TextBlockModel model;
    private DrawAreaChangeListener listener;

    public OTextField() {
        this(null);
    }

    //初始化对象
    public OTextField(String text) {
        super(text);
        initComp();
    }

    private void initComp() {
        setModel(new TextBlockModel(TextBlockModel.TextType.TextField));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        //mode property listner
        this.setFont(model.getFont());
        this.setForeground(model.getForecolor());
        this.setBackground(model.getBackgroundcolor());

        addMouseListener(this);
        addMouseMotionListener(this);
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

    //模型设置部分
    public void setModel(TextBlockModel model) {
        this.model = model;
    }

    public TextBlockModel getModel() {
        return model;
    }

    //设置UI
    public void setUI(OTextFieldUI ui) {
        super.setUI(ui);
    }

    @Override
    public OTextFieldUI getUI() {
        return (OTextFieldUI) ui;
    }

    @Override
    public String getUIClassID() {
        return uiClassID;
    }

    @Override
    public void setFont(Font f){
        super.setFont(f);
        if ( model != null)
            model.setFont(f);
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

    public void setFilterFlag(boolean flag){
        model.setUseasfilter(flag);
        repaint();
    }
}
