package com.original.serive.channel.ui.widget;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.original.serive.channel.util.ChannelUtil;
import com.original.serive.channel.util.GraphicsHandler;

/**
 * 消息提示悬浮框，用于显示简单的文本提示内容
 * @author WMS
 *
 */
public class ToolTip extends JPopupMenu{
	private JMenuItem menu = new JMenuItem();
	public ToolTip() {
		add(menu);
	}
	
	public void setToolTipText(String text) {
		menu.setText(text);
	}
	
	public void setLocation(int x, int y) {
		// TODO 自动生成的方法存根
		Point point = ChannelUtil.checkComponentLocation(x, y, this);
		super.setLocation(point.x, point.y);
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO 自动生成的方法存根
		Graphics2D g2d = GraphicsHandler.optimizeGraphics(g);
		super.paintComponent(g2d);
	}
}
