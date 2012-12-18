/*
 *  com.original.widget.comp.JOTextField.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget;

import com.original.widget.event.DrawAreaChangeListener;
import com.original.widget.model.TextBlockModel;
import com.original.widget.plaf.OTextAreaUI;
import com.original.widget.plaf.OTextFieldUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

/**
 * (Class Annotation.)
 * * 该类是一个组合类，模拟了对应一个合适的组件内容，
 * 修改记录
 *  1. 按照Swing标准样式做了调整，供第一期使用方便
 * 内嵌了一个JScrollPane和JTextArea
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   May 4, 2012 1:59:19 PM
 */
public class OTextArea extends JPanel {

    private static final String uiClassID = "OTextAreaUI";

    static {
        UIDefaults defaults = UIManager.getDefaults();
        defaults.put(uiClassID, "com.original.widget.plaf.OTextAreaUI");
    }
    private static final long serialVersionUID = -6497888280847098839L;
    Dimension size = new Dimension();
    private TextBlockModel model;
    private DrawAreaChangeListener listener;
    private JScrollPane scrollPane;
    private JTextArea txtArea;

    public OTextArea() {
        this(null);
    }
    //初始化对象

    public OTextArea(String text) {
        initComp(text);
    }

    private void initComp(String text) {
        setModel(new TextBlockModel(TextBlockModel.TextType.TextArea));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        txtArea = new JTextArea();
        scrollPane = new JScrollPane(txtArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBar(new OScrollBar(JScrollBar.VERTICAL, model.getBackgroundcolor()));
        scrollPane.setBorder(null);
        txtArea.setLineWrap(true);
        txtArea.setWrapStyleWord(true);
        txtArea.setFont(model.getFont());
        txtArea.setBackground(model.getBackgroundcolor());
        txtArea.setForeground(model.getForecolor());
        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);

        this.setFont(model.getFont());
        this.setForeground(model.getForecolor());
        this.setBackground(model.getBackgroundcolor());
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
    }

    public String getCaption() {
        return model.getCaption();
    }

    //设置UI
    public void setUI(OTextFieldUI ui) {
        super.setUI(ui);
    }

    @Override
    public OTextAreaUI getUI() {
        return (OTextAreaUI) ui;
    }

    @Override
    public String getUIClassID() {
        return uiClassID;
    }

    //设置内容信息
    public void setText(String text) {
        //super.setText(text);
        model.setText(text);
        txtArea.setText(text);
    }

    public String getText() {
        return txtArea.getText();
    }
}
