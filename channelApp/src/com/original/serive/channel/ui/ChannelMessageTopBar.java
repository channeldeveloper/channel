package com.original.serive.channel.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import com.original.serive.channel.ChannelGUI;
import com.original.serive.channel.util.ChannelConstants;
import com.original.serive.channel.util.GraphicsHandler;
import com.original.serive.channel.util.LocationIcon;
import com.original.service.channel.ChannelMessage;

/**
 * 顶部状态栏。和{@link ChannelMessageStatusBar}一样，也是用于汇总消息信息，只是位置在顶部。
 * @author WMS
 *
 */
public class ChannelMessageTopBar extends ChannelMessageStatusBar
{
	private LocationIcon closeIcon = null;
	private boolean drawSeparateLine = true; //是否显示分割线
	
	public ChannelMessageTopBar() {
		this(true);
	}
	
	public ChannelMessageTopBar(boolean drawSeparateLine) {
		this(ChannelConstants.CLOSE_ICON, drawSeparateLine);
	}
	
	public ChannelMessageTopBar(final LocationIcon closeIcon, final boolean drawSeparateLine)
	{
		this.closeIcon = closeIcon;
		this.drawSeparateLine = drawSeparateLine;
	}
	
	/**
	 * 初始化一些控件，如设置图标，颜色等。
	 */
	protected void initStatusBar () {
		//下面是一些鼠标事件，用于关闭当前面板操作
		if(this.closeIcon != null) {
			addMouseMotionListener(new MouseMotionAdapter()
			{
				public void mouseMoved(MouseEvent e)
				{
					if(closeIcon.getBounds().contains(e.getPoint())) {
						setCursor(ChannelConstants.HAND_CURSOR);
					}
					else {
						setCursor(ChannelConstants.DEFAULT_CURSOR);
					}
				}
			});
			addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent e)
				{
					if(closeIcon.getBounds().contains(e.getPoint())) {
						doClose();
					}
				}
			});
		}
	}
	
	/**
	 * 构建状态栏
	 */
	protected void constructStatusBar() {
		setPreferredSize(SIZE);
	}

	/**
	 * 关闭操作
	 */
	public void doClose() {
		ChannelMessageBodyPane body = this.getMessageBody();
		ChannelDesktopPane desktop = (ChannelDesktopPane)ChannelGUI.channelNativeStore.get("ChannelDesktopPane");
		if(body != null)
		{
			ChannelMessageBodyPane.Body child, origin = null;
			if(body.messageBodyList.isEmpty() || body.getComponentCount() == 0) {
				desktop.showDefaultComp();
			}
			else {
				if(body.getComponent(0) instanceof ChannelMessageBodyPane.Body) {
					child = (ChannelMessageBodyPane.Body)body.getComponent(0);
					if( (origin = child.origin) != null ) //关闭的时候也要通知origin
					{
						child.copyTo(origin);
					}
					
					ChannelMessage newMsg = child.iMsg;
					desktop.removeShowComp("SHOWALL_"+newMsg.getContactName(), true);
				}
			}
		}
	}

	/**
	 * 自定义背景
	 */
	protected void paintComponent(Graphics g)
	{
		RenderingHints hints = GraphicsHandler.DEFAULT_RENDERING_HINT_ON;
	hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		Graphics2D g2d = GraphicsHandler.optimizeGraphics(g, hints);
		g2d.setColor(Color.white);
		
		int width = getWidth(), height = getHeight();
		g2d.fillRoundRect(0, 0, width, height, 10, 10);
		if(closeIcon != null) {
			g2d.drawImage(closeIcon.getImage(), width-closeIcon.getWidth()-12, (height-closeIcon.getHeight())/2, this);
			closeIcon.setBounds(width-closeIcon.getWidth()-12, (height-closeIcon.getHeight())/2,
					closeIcon.getWidth(), closeIcon.getHeight());
		}
		
		if(drawSeparateLine) {
			g2d.setColor(Color.gray);
			g2d.drawLine(0, height-1, width, height-1);
		}
	}
}
