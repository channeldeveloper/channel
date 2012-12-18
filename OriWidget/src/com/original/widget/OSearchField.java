/*
 *  com.original.widget.OSearchField.java
 *
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget;

import com.original.widget.event.DrawAreaChangeListener;
import com.original.widget.model.TextBlockModel;
import com.original.widget.plaf.OSearchFieldUI;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

/**
 * (Class Annotation.)
 *
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-5-21 21:55:07
 */
public class OSearchField extends JTextField implements MouseListener,
        MouseMotionListener{

    private static final String uiClassID = "OSearchFieldUI";
    static
    {
        UIDefaults defaults = UIManager.getDefaults();
        defaults.put(uiClassID, "com.original.widget.plaf.OSearchFieldUI");
    }

    private static final long serialVersionUID = -6497888280847098839L;
    Dimension size = new Dimension();
    private TextBlockModel model;
    private DrawAreaChangeListener listener;



    //初始化对象
    public OSearchField() {
         //model
        initComp();
    }

    private void initComp(){
        setModel(new TextBlockModel(TextBlockModel.TextType.TextField));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 34, 10, 32));
        //mode property listner
        addMouseListener(this);
        addMouseMotionListener(this);
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

     public void setCaption(String _caption){
         model.setCaption(_caption);
     }
     public String getCaption(){
         return model.getCaption();
     }
    
    //设置UI
    public void setUI(OSearchFieldUI ui) {
		super.setUI(ui);
	}

    @Override
    public OSearchFieldUI getUI() {
		return (OSearchFieldUI) ui;
	}

    @Override
    public String getUIClassID() {
		return uiClassID;
	}

    //设置内容信息
    @Override
    public void setText(String text){
        super.setText(text);
        model.setText(text);
    }

    
    @Override
    public void mouseClicked(MouseEvent e) {
        getUI().procMouseClick(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
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
