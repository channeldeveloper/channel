/*
 *  com.original.widget.ODatetimeEditor.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget;

import com.original.widget.date.OriDate;
import com.original.widget.event.BlockSelectionEvent;
import com.original.widget.event.BlockSelectionListener;
import com.original.widget.event.DrawAreaChangeListener;
import com.original.widget.model.DatetimeModel;
import com.original.widget.plaf.ODatetimeEditorUI;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;
import javax.swing.JPanel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

/**
 * (Class Annotation.)
 *
 * @author   Administrator
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-5-21 23:16:36
 */
public class ODatetimeEditor extends JPanel implements MouseListener, KeyListener,
    BlockSelectionListener {

    private static final String uiClassID = "ODatetimeEditorUI";
    static
    {
            UIDefaults defaults = UIManager.getDefaults();
            defaults.put(uiClassID, "com.original.widget.plaf.ODatetimeEditorUI");
    }

    private static final long serialVersionUID = -6497888280847098839L;
    Dimension size = new Dimension();
    private DatetimeModel model;
    private DrawAreaChangeListener listener;

    /**
     * constructor.
     */
    public ODatetimeEditor(){
      init();
    }
    /**
     * initialize some event handlers.
     */
    private void init(){
        model = new DatetimeModel();
        Dimension size = new Dimension(200,40);
        this.setMinimumSize(size);
        this.setPreferredSize(size);
        setOpaque(false);
        setBorder(null);
        addMouseListener(this);
        addKeyListener(this);
    }
    @Override
    public boolean isFocusTraversable(){
        return true;
    }
     //模型设置部分
    public void setModel(DatetimeModel model) {
        this.model = model;
    }
     public DatetimeModel getModel() {
        return model;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point pt = this.getLocationOnScreen();
        getUI().procMouseClick(e, pt, this);
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
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void keyPressed(KeyEvent e) {
         char c= e.getKeyChar();
        if(Character.isDefined(c) &&
                Character.getType(c)==Character.CONTROL){
            if(c==KeyEvent.VK_ESCAPE){
                getUI().hideInnerWin(this);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void blockSelectChange(BlockSelectionEvent e) {
        Date dt = e.getDt();
        //System.out.println(current_date);
        model.setCurdate(dt);
        getUI().hideInnerWin(this);
        repaint();
    }

    public void resize(){
        int width = this.getWidth();
        int height = this.getHeight();
        this.model.setWidth(width);
        this.model.setHeight(height);
    }
    

    //设置UI
    public void setUI(ODatetimeEditorUI ui) {
		super.setUI(ui);
	}

    @Override
    public ODatetimeEditorUI getUI() {
		return (ODatetimeEditorUI) ui;
	}

    @Override
    public String getUIClassID() {
		return uiClassID;
	}

    //增加对应内容用于处理编辑
    public void setDate(String sDate){
        model.setCurdate(OriDate.getDate(sDate, "yyyy-MM-dd HH:mm:ss"));
    }

    public String getDate(){
        return OriDate.formatDate(model.getCurdate(), "yyyy-MM-dd HH:mm:ss");
    }

    public void setFullFlag(boolean flag){
        model.setIsfull(flag);
        repaint();
    }
}
