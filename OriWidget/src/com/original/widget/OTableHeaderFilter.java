/*
 *  com.original.widget.comp.OTableHeaderFilter.java
 * 
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.widget;

import com.original.widget.event.DrawAreaChangeListener;
import com.original.widget.model.TextBlockModel;
import com.original.widget.plaf.OTableHeaderFilterUI;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
 * @author 刘萌萌
 * @encoding UTF-8
 * @version 1.0
 * @create May 28, 2012 1:59:19 PM
 */
public class OTableHeaderFilter extends JTextField implements ComponentListener,
		CustomDrawable, MouseListener, MouseMotionListener,FocusListener {

	private static final String uiClassID = "OTableHeaderFilterUI";
	static {
		UIDefaults defaults = UIManager.getDefaults();
		defaults.put(uiClassID, "com.original.widget.plaf.OTableHeaderFilterUI");
	}

	private static final long serialVersionUID = -6497888280847098839L;
	Dimension size = new Dimension();
	private TextBlockModel model;
	private DrawAreaChangeListener listener;
	public boolean _mouseOver = false;
	private String title = "";
	public boolean _isClicked = true;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	// 初始化对象
	public OTableHeaderFilter(int width, int height) {
		// model
		setModel(new TextBlockModel(TextBlockModel.TextType.TextField));
		setOpaque(false);
		this.setSize(width, height);
		this.setUI(new OTableHeaderFilterUI(this));
		setBorder(BorderFactory.createEmptyBorder(10, 8, 10, 26));
		// mode property listner
		listener = new DrawAreaChangeListener(this);
		this.model.addChangeListener(listener);
		this.addComponentListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addFocusListener(this);
		initComp();
	}

	private void initComp() {

		this.setFont(model.getFont());
		this.setForeground(model.getForecolor());
		this.setBackground(model.getBackgroundcolor());
	}

	// 模型设置部分
	public void setModel(TextBlockModel model) {
		this.model = model;
	}

	public TextBlockModel getModel() {
		return model;
	}

	// 检测程序大小
	@Override
	public void componentResized(ComponentEvent e) {
		resize();
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	public void resize() {
		int width = this.getWidth();
		int height = this.getHeight();
		this.model.setWidth(width);
		this.model.setHeight(height);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		resize();
	}

	// 设置UI
	public void setUI(OTableHeaderFilterUI ui) {
		super.setUI(ui);
	}

	@Override
	public OTableHeaderFilterUI getUI() {
		return (OTableHeaderFilterUI) ui;
	}

	@Override
	public String getUIClassID() {
		return uiClassID;
	}

	// 设置内容信息
	@Override
	public void setText(String text) {
		super.setText(text);
		model.setText(text);
	}

	// 强制绘制-当变化的时候
	@Override
	public void forceCustomDraw() {
		getUI().redraw();
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (_mouseOver) {
			this.setText("");
			_isClicked = true;
			this.getParent().requestFocus();
		}
		else
		{
			_isClicked = false;
			//this.repaint();
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {

		Point pt = e.getPoint();
		Rectangle emptyRect = getUI().getEmptyRect();
		if (emptyRect.contains(pt)) {

			_mouseOver = true;
			this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		} else {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
			_mouseOver = false;
		}

	}

	@Override
	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		
		if(this.getText().length()==0)
		{
			_isClicked = true;
			//this.repaint();
		}
		
	}

}
