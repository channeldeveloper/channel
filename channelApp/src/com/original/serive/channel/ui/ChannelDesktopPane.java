package com.original.serive.channel.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import com.original.serive.channel.layout.VerticalGridLayout;
import com.original.serive.channel.util.ChannelConfig;
import com.original.serive.channel.util.ChannelUtil;
import com.original.serive.channel.util.IconFactory;
import com.original.service.channel.ChannelMessage;
import com.original.service.channel.event.MessageEvent;
import com.original.service.channel.event.MessageListner;
import com.original.widget.OScrollBar;

/**
 * Channel用户桌面，该桌面具有切换功能，有默认显示面板(即消息列表面板)，可以切换至其他面板
 * (使用{@link ChannelDesktopPane#addOtherShowComp()即可})。
 * 
 * @author WMS
 *
 */
public class ChannelDesktopPane extends JPanel implements MessageListner
{
	private CardLayout layoutMgr = new CardLayout(); //卡片布局，带有切换功能
	public static Dimension SIZE = new Dimension(ChannelConfig.getIntValue("width"), 
			ChannelConfig.getIntValue("desktopHeight"));
	public static ImageIcon BACKGROUND = IconFactory.loadIconByConfig("background");
	
	public static JPanel DEFAULT_PANE = new JPanel(); //桌面默认显示的面板，消息列表面板，带滚动条
	public static JScrollBar DEFAULT_SCROLLBAR = 
			new OScrollBar(JScrollBar.VERTICAL, new Color(225,240,240)); //默认显示面板的滚动条
	
	public static LayoutManager DEFAULT_DOWN_LAYOUT = //默认布局方式
			new VerticalGridLayout(VerticalGridLayout.BOTTOM_TO_TOP, 0, 8, new Insets(0, 0, 0, 0)), 
			DEFAULT_UP_LAYOUT = 
			new VerticalGridLayout(VerticalGridLayout.TOP_TO_BOTTOM, 0, 8, new Insets(0, 0, 0, 0));
	
	public static Lock channelLock = new ReentrantLock(); //用于控制消息的添加，即initMessage()和addMessage()的同步
	
	public ChannelDesktopPane() {
		setLayout(layoutMgr);
		DEFAULT_PANE.setLayout(DEFAULT_UP_LAYOUT);
		
		addDefaultShowComp(DEFAULT_PANE);
	}
	
	/**
	 * 初始化桌面消息显示列表，注意和addMessage使用的布局方式不同，用途也不同
	 * @param msg
	 */
	public void initMessage(ChannelMessage msg)
	{
		if(!checkMsgValidity(msg))
			return;
		
		channelLock.lock();
		try {
			ChannelMessagePane msgContainer = null;
//			if (DEFAULT_UP_LAYOUT != DEFAULT_PANE.getLayout()) {
//				DEFAULT_PANE.setLayout(DEFAULT_UP_LAYOUT);
//			}

			if ((msgContainer = findMessage(msg)) == null) {
				msgContainer = new ChannelMessagePane();
				msgContainer.initMessage(msg);
				DEFAULT_PANE.add(msgContainer);
				DEFAULT_PANE.validate();
			} else {
				msgContainer.initMessage(msg);
			}
		} finally {
			channelLock.unlock();
		}
	}
	
	/**
	 * 添加消息，这里是消息添加的入口，使用时最好由线程调用。
	 * @param msg
	 */
	public void addMessage(ChannelMessage msg)
	{
		if (!checkMsgValidity(msg))
			return;

		channelLock.lock();
		try {
			ChannelMessagePane msgContainer = null;
			if ((msgContainer = findMessage(msg)) == null) {
				msgContainer = new ChannelMessagePane();
			}
			msgContainer.addMessage(msg, true);
			
//			if (DEFAULT_DOWN_LAYOUT != DEFAULT_PANE.getLayout()) {
//				DEFAULT_PANE.setLayout(DEFAULT_DOWN_LAYOUT);
//			}
			DEFAULT_PANE.add(msgContainer, 0);
			DEFAULT_PANE.validate();

			//如果当前显示界面已经切换到<显示全部>面板，则该面板也要添加最新消息
			JPanel showComp = (JPanel) currentShowComp();
			if (showComp != DEFAULT_PANE
					&& showComp.getComponentCount() > 0
					&& (showComp = (JPanel) showComp.getComponent(0)) instanceof ChannelMessagePane) {
				msgContainer = (ChannelMessagePane) showComp;
				msgContainer.addMessage(msg, false);
			}
		}
		finally {
			channelLock.unlock();
		}
	}
	
	/**
	 * 查找消息面板。由于消息面板是和联系人Uid绑定，所以添加消息时，需要事先查找一下该Uid有没有消息面板。
	 * 有则使用该面板添加消息。
	 * @param msg 消息对象
	 * @return
	 */
	public ChannelMessagePane findMessage(ChannelMessage msg)
	{
		if(!checkMsgValidity(msg))
			return null;
		
		for(int i=0; i<DEFAULT_PANE.getComponentCount(); i++)
		{
			Component comp = DEFAULT_PANE.getComponent(i);
			if(comp instanceof ChannelMessagePane)
			{
				String uid = ((ChannelMessagePane) comp).uid;
				String uName = msg.getContactName();
				if(uid != null && uid.equals(uName))
					return (ChannelMessagePane)comp;
			}
		}
		return null;
	}
	
	/**
	 * 这里对消息的有效性进行检查，以后其他面板，如{@link ChannelMessagePane}添加消息时，将不再做检查。
	 * 
	 * @param msg 消息报文
	 * @return
	 */
	public boolean checkMsgValidity(ChannelMessage msg)
	{
		//这里目前只检查是否为空，以及标识Id是否为空
		if(msg == null || msg.getMessageID() == null)
			throw new IllegalArgumentException("Invalid Message! Cause by it or it's ID is null.");
		
		return true;
	}
	
	/**
	 * 获取当前显示的面板
	 * @return
	 */
	public Component currentShowComp() {
		Component comp = null;
		for(int i=0; i<getComponentCount(); i++)
		{
			comp = getComponent(i);
			if(comp.isVisible()) {
				if(comp instanceof JScrollPane) {
					comp = ((JScrollPane) comp).getViewport().getView();
				}
				break;
			}
		}
		return comp;
	}
	
	/**
	 * 添加默认显示的面板
	 * @param comp
	 */
	private void addDefaultShowComp(JComponent comp)
	{
		comp.setOpaque(false);
		JScrollPane jsp = new JScrollPane(comp,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		jsp.setVerticalScrollBar(DEFAULT_SCROLLBAR);
		jsp.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 5));
		
		//设置滚动面板透明
		jsp.setViewportBorder(null);
		jsp.setOpaque(false);
		jsp.getViewport().setOpaque(false);
		
		jsp.setName("DEFAULT");
		add("DEFAULT", jsp);
	}
	
	/**
	 * 添加其他面板，并同时显示该面板
	 * @param name 面板名称，唯一
	 * @param comp
	 */
	public void addOtherShowComp(String name, JComponent comp)
	{
		if(name == null)
			return;
		
		//如果已有该面板，则显示
		if(indexOfShowComp(name) != -1) {
			showComp(name);
			return;
		}
		
		JPanel otherPane = new JPanel(DEFAULT_DOWN_LAYOUT); 
		otherPane.add(comp);
		otherPane.setOpaque(false);
		//为载体添加滚动条
		JScrollPane jsp =  ChannelUtil.createScrollPane(otherPane, new Color(225,240,240));
		jsp.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 5));
		jsp.setName(name);
		
		add(name, jsp);
		showComp(name);
	}
	
	/**
	 * 显示默认面板
	 */
	public void showDefaultComp()
	{
		showComp("DEFAULT");
	}
	
	/**
	 * 显示其他面板
	 * @param name 面板名称
	 */
	public void showComp(String name)
	{
		layoutMgr.show(this, name);	
	}
	
	/**
	 * 是否已包含name对应的面板
	 * @param name 面板名称
	 * @return
	 */
	public int indexOfShowComp(String name)
	{
		if(name != null) {
			for(int i=0; i<getComponentCount();i++) {
				if(name.equals(getComponent(i).getName()))
					return i;
			}
		}
		return -1;
	}
	
	/**
	 * 移除其他面板
	 * @param name 面板名称
	 * @param showDefault 移除后是否显示默认面板
	 */
	public void removeShowComp(String name, boolean showDefault)
	{
		int index = -1;
		if(name == null || "DEFAULT".equals(name)
				|| (index = indexOfShowComp(name)) == -1)
			return;
		
		remove(index);
		if(showDefault)
			showDefaultComp();
	}

	@Override
	public void paintComponent(Graphics g)
	{
//		Graphics2D g2d = GraphicsHandler.optimizeGraphics(g);
//		
//		Area bounds = new Area(new Rectangle(0, 0, getWidth(), getHeight()));
//		Color backColor = new Color(35,85,105);
//		g2d.setColor(backColor);
//		
//		int radius = (int)(getWidth()/2);
//		Point2D center = new Point(getWidth()/2, getHeight()/2);
//		RadialGradientPaint paint = new RadialGradientPaint(center, radius,  
//                new float[]{0.0f, 1.0f}, new Color[]{backColor.brighter().brighter(), backColor});  
//		g2d.setPaint(paint);
//		g2d.fill(bounds);
//		GraphicsHandler.suspendRendering(g2d);
		g.drawImage(BACKGROUND.getImage(), 0, 0, this);
	}

	@Override
	public void change(MessageEvent evnt)
	{
		if(evnt.getType() == MessageEvent.Type_Added) {
			addMessage(evnt.getAdded()[0]);
		}
	}
}
