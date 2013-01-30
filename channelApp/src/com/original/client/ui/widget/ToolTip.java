package com.original.client.ui.widget;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Hashtable;

import com.original.client.util.GraphicsHandler;
import com.original.client.util.Utilities;
import com.seaglasslookandfeel.widget.SGMenuItem;
import com.seaglasslookandfeel.widget.SGPopupMenu;

/**
 * 消息提示悬浮框，用于显示简单的文本提示内容
 * @author WMS
 *
 */
public class ToolTip extends SGPopupMenu{
	public static final String SHOW_STATUS_PROPERTY = "show status"; //invoker的显示状态，如果有其他属性，待可以扩充。
	private static Hashtable<Component, Hashtable<String, Object>> clientProperties = 
			new Hashtable<Component, Hashtable<String,Object>>();
	
	private static Hashtable<Component, ToolTip> clientBindsToolTips = 
			new Hashtable<Component, ToolTip>();
	
	private SGMenuItem menu = new SGMenuItem();
	private Component invoker = null;
	public ToolTip() {
		add(menu);
	}

	public void setToolTipText(String text) {
		menu.setText(text);
	}
	public String getToolTipText() {
		return menu.getText();
	}
	
	public void setToolTipColor(Color fg) {
		menu.setForeground(fg);
	}
	public Color getToolTipColor() {
		return menu.getForeground();
	}
	
	public void setLocation(int x, int y) {
		// TODO 自动生成的方法存根
		Point point = Utilities.checkComponentLocation(x, y, this);
		super.setLocation(point.x, point.y);
	}

	@Override
	public void show(Component invoker, int x, int y) {
		// TODO 自动生成的方法存根
		if (this.invoker == null) {
			this.invoker = invoker;
		}
		
		if (this.invoker == invoker) {
//			if (isInvokerShow()) {
//				return;
//			}
		} else {
			putInvokerShowStatus(this.invoker, false);
		}
		
		this.invoker = invoker;
		putInvokerShowStatus(this.invoker, true);
		super.show(invoker, x, y);
	}
	
	private void putInvokerShowStatus(Component invoker, boolean show) {
		putInvokerProperty(invoker, SHOW_STATUS_PROPERTY, show ? Boolean.TRUE
				: Boolean.FALSE);
	}
	private void removeInvokerShowStatus(Component invoker) {
		removeInvokerProperty(invoker, SHOW_STATUS_PROPERTY);
	}
	
	private void putInvokerProperty(Component invoker, String propKey, Object propValue)
	{
		if (invoker != null && propKey != null) {
			Hashtable<String, Object> properties = clientProperties
					.get(invoker);
			if (properties == null)
				properties = new Hashtable<String, Object>();

			properties.put(propKey, propValue);
			clientProperties.put(invoker, properties);
		}
	}
	private void removeInvokerProperty(Component invoker, String propKey)
	{
		if (invoker != null && propKey != null) {
			Hashtable<String, Object> properties = clientProperties.get(invoker);
			if(properties == null)
				return;
			
			if (properties.containsKey(propKey)) {
				properties.remove(propKey);
				clientProperties.put(invoker, properties);
			}
		}
	}
	
	private Object getInvokerPropValue(Component invoker, String propKey) {
		if (invoker != null && propKey != null) {
			Hashtable<String, Object> properties = clientProperties
					.get(invoker);
			if (properties != null) {
				return properties.get(propKey);
			}
		}
		return null;
	}
	public boolean isInvokerShow() {
		Boolean showStatus = (Boolean)getInvokerPropValue(invoker, SHOW_STATUS_PROPERTY);
		return showStatus != null && showStatus.booleanValue();
	}
	
	private void removeInvoker() {
		removeInvokerShowStatus(invoker);
		setInvoker(this.invoker = null);
	}
	public void removeInvoker(Component invoker) {
		if(this.invoker == invoker) {
			removeInvoker();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO 自动生成的方法存根
		Graphics2D g2d = GraphicsHandler.optimizeGraphics(g);
		super.paintComponent(g2d);
	}
}
 