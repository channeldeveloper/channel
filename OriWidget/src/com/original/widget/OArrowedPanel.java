/**
 * com.original.widget.comp.arrowedpanel.control
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.widget;

/**
 * @(#)ArrowedPanel.java
 *
 * 带箭头的面板（Panel），可定义箭头的方向和位置，及高度、宽度、箭头高度、线条颜色、面板颜色
 * @author Ni Min,Song Xueyong
 * @version 1.00 2012/4/23
 */

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import com.original.widget.event.ArrowChangeListener;
import com.original.widget.model.ArrowModel;
import com.original.widget.plaf.OArrowedPanelUI;

/**
 *  有箭头指向的组件。
 * @author Ni Min,Song Xueyong
 *
 */
public class OArrowedPanel extends JPanel {
	
	/**
	 * @see #getUIClassID
	 * @see #readObject
	 */
	private static final String uiClassID = "OArrowedPanelUI";
	static 
	{
		UIDefaults defaults = UIManager.getDefaults();
		defaults.put(uiClassID, "com.original.widget.plaf.OArrowedPanelUI");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6497888280847098839L;
	Dimension size = new Dimension();
	private ArrowModel model;
	private ArrowChangeListener listener;

	/**
	 * 有箭头指向的组件。
	 * @param width
	 * @param height
	 */
	public OArrowedPanel(int width, int height) {
		//model
		setModel(new ArrowModel());
		//size
		this.setPanelSize(width, height);
		setLayout(null);		
		//mode property listner
		listener  = new ArrowChangeListener(this);
		this.model.addChangeListener(listener);
	}
	
	/**
	 * 
	 * @param width
	 * @param height
	 */
	public OArrowedPanel(double width, double height) {
		setModel(new ArrowModel());
		this.setPanelSize(width, height);
		setLayout(null);
	}

	// //////////////////UI机制///////////////////////

	

	/**
	 * Returns the look and feel (L&F) object that renders this component.
	 * 
	 * @return the PanelUI object that renders this component
	 * @since 1.4
	 */
	public OArrowedPanelUI getUI() {
		return (OArrowedPanelUI) ui;
	}

	/**
	 * Sets the look and feel (L&F) object that renders this component.
	 * 
	 * @param ui
	 *            the PanelUI L&F object
	 * @see UIDefaults#getUI
	 * @since 1.4
	 * @beaninfo bound: true hidden: true attribute: visualUpdate true
	 *           description: The UI object that implements the Component's
	 *           LookAndFeel.
	 */
	public void setUI(OArrowedPanelUI ui) {
		super.setUI(ui);
	}

	/**
	 * Returns a string that specifies the name of the L&F class that renders
	 * this component.
	 * 
	 * @return "PanelUI"
	 * @see JComponent#getUIClassID
	 * @see UIDefaults#getUI
	 * @beaninfo expert: true description: A string that specifies the name of
	 *           the L&F class.
	 */
	public String getUIClassID() {
		return uiClassID;
	}

	// //////////////////UI机制///////////////////////
	// Model functions begin
	/**
	 * setPanelSize(int width, int height) 设置面板尺寸（宽度、高度），整数版本
	 * 
	 * @param width
	 * @param height
	 */
	public void setPanelSize(int width, int height) {
		super.setSize(width, height);

		size.setSize(width, height);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
//		calcArrowRect();
		this.repaint();
	}

	/**
	 * setPanelSize(double width, double height) 设置面板尺寸（宽度、高度），浮点数版本
	 * 
	 * @param width
	 * @param height
	 */
	public void setPanelSize(double width, double height) {
		setPanelSize((int) width, (int) height);
	}

	/**
	 * setArrowDirection(String arrowDirection)
	 * Direction定义同BorderLayout中定义的EAST,WEST,NORTH,SOUTH,CENTER（如果为CENTER则无箭头）
	 * 
	 * @param arrowDirection
	 */
	public void setArrowDirection(String arrowDirection) {
		
		getModel().setArrowDirection(arrowDirection);
	}

	/**
	 * setThickness(int thickness) 设置箭头高度
	 * 
	 * @param thickness
	 */
	public void setThickness(int thickness) {
		getModel().setThickness(thickness);
	}

	/**
	 * setArrowPos(int arrowPos) 设置箭头位置
	 * 
	 * @param arrowPos
	 */
	public void setArrowPos(int arrowPos) {
		getModel().setArrowPos(arrowPos);
	}

	/**
	 * setArrowWidth(int arrowWidth) 设置箭头（底部）宽度
	 * 
	 * @param arrowWidth
	 */
	public void setArrowWidth(int arrowWidth) {
		getModel().setArrowWidth(arrowWidth);
	}

	/**
	 * setLineColor(Color lineColor) 设置边线颜色
	 * 
	 * @param lineColor
	 */
	public void setLineColor(Color lineColor) {
		getModel().setLineColor(lineColor);
	}

	/**
	 * setPanelColor(Color panelColor) 设置面板颜色
	 * 
	 * @param panelColor
	 */
	public void setPanelColor(Color panelColor) {
		getModel().setPanelColor(panelColor);
	}

	

	/**
	 * @param model the model to set
	 */
	public void setModel(ArrowModel model) {
		this.model = model;
	}

	/**
	 * @return the model
	 */
	public ArrowModel getModel() {
		return model;
	}

}
