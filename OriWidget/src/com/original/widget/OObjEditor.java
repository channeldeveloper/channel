/*
 *  com.original.widget.OObjEditor.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget;

import com.original.widget.datadef.OriObject;
import com.original.widget.datadef.OriParticipant;
import com.original.widget.event.DrawAreaChangeListener;
import com.original.widget.event.ObjDocumentEvent;
import com.original.widget.model.ObjectMesModel;
import com.original.widget.model.ObjectMesModel.OInnerObject;
import com.original.widget.plaf.OObjEditorUI;
import com.original.widget.plaf.OThumbImageUI;
import javax.swing.JPanel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.event.DocumentListener;
/**
 * (Class Annotation.)
 *  对象输入组件
 *   v1, 只实现基本的功能，以后可以根据这个版本进行修改
 * @author   Changjian Hu
 * @encoding UTF-8
 * @version  1.0
 * @create   May 6, 2012 11:23:58 PM
 */
public class OObjEditor extends JPanel implements MouseListener,
    KeyListener, MouseMotionListener {
    private static final String uiClassID = "OObjEditorUI";
	static
	{
		UIDefaults defaults = UIManager.getDefaults();
		defaults.put(uiClassID, "com.original.widget.plaf.OObjEditorUI");
	}

    private static final long serialVersionUID = -6497888280847098839L;
	Dimension size = new Dimension();
	private ObjectMesModel model;
    private DrawAreaChangeListener listener; 


    /**
     * constructor.
     */
    public OObjEditor(){
        
        init();
    }
    //设置和读取Object信息
    public String getInValue(){
        return model.getValue();
    }
    public void setInValue(List<OriObject> data){
        model.setValue(data);
    }

    /**
     * initialize some event handlers.
     */
    private void init(){
        setModel(new ObjectMesModel());
		setOpaque(false);
		setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        setFont(model.getFont());
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        new Thread(){
            @Override
            public void run(){
            while(true){
                repaint();
                getUI().resetCaretFlag();
                //m_cursor_sparkling_light   =   !m_cursor_sparkling_light;
                try {
                    sleep(250);
                }catch (InterruptedException   ex)   {
                }
            }
        }}.start();
    }
    /**
     * 这个函数决定了JPanel可以处理KeyEvent类型的事件
     * @return
     */
    @Override
    public boolean isFocusTraversable(){
        return true;
    }

     //模型设置部分
    public void setModel(ObjectMesModel model) {
        this.model = model;
    }
     public ObjectMesModel getModel() {
        return model;
    }

    
    

    
    //设置UI
    public void setUI(OThumbImageUI ui) {
		super.setUI(ui);
	}
    
    @Override
    public OObjEditorUI getUI() {
		return (OObjEditorUI) ui;
	}

    @Override
    public String getUIClassID() {
		return uiClassID;
	}
    
    @Override
    public void mouseClicked(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        requestFocus();
        getUI().procMousePressed(e);
        fireDocumentChanged(null, 2);
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        getUI().procMouseReleased(e);
        repaint();
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
        char c= e.getKeyChar();
        if(Character.isDefined(c) &&
                Character.getType(c)!=Character.CONTROL){
            getUI().procKeyTyped(e, this.getGraphics());
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        getUI().procKeyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        getUI().procMouseDragger(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    //API Area
    public void replaceCurrent(String title){
        this.requestFocus();
        getUI().replaceCurrent(title, this.getGraphics());
        repaint();
    }

    public void makeInnerObject(Map<String, Object> obj){
        this.requestFocus();
        getUI().replaceCurrent(obj, this.getGraphics());
        repaint();
    }
    public void appendObject(OriObject obj){
        this.requestFocus();
        getUI().replaceCurrent(obj, this.getGraphics());
        repaint();
    }

    //响应编辑事件
    public void fireDocumentChanged(OInnerObject obj, int type){
        for(DocumentListener lis: this._listeners){
            if(obj!=null)
                lis.insertUpdate(new ObjDocumentEvent(obj.getDisplay(), type));
            else
                lis.insertUpdate(new ObjDocumentEvent("", type));
        }
        //System.out.println(obj.getDisplay());
    }

    //增加事件处理机制
    List<DocumentListener> _listeners = new ArrayList<DocumentListener>();
    public synchronized void addDocumentListener(DocumentListener listener) {
        _listeners.add(listener);
    }
    public synchronized void removeDocumentListener(DocumentListener listener) {
        _listeners.remove(listener);
    }

    //增加两个函数用于标准的其他应用
    public void setText(String sText){
        //System.out.println(input);
        List<OriObject> data = OriParticipant.parseParticipant(sText);
        this.setInValue(data);
    }

    public String getText(){
        return this.getInValue();
    }
}
