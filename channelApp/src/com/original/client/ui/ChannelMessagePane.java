﻿package com.original.client.ui;

import iqq.util.QQEnvironment;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.mongodb.gridfs.GridFSDBFile;
import com.original.client.layout.ChannelGridBagLayoutManager;
import com.original.client.layout.VerticalGridLayout;
import com.original.client.ui.widget.ToolTip;
import com.original.client.util.ChannelConfig;
import com.original.client.util.ChannelConstants;
import com.original.client.util.ChannelUtil;
import com.original.client.util.GraphicsHandler;
import com.original.client.util.IconFactory;
import com.original.client.util.LocationIcon;
import com.original.service.channel.Account;
import com.original.service.channel.ChannelMessage;
import com.original.service.channel.core.ChannelService;
import com.original.service.people.People;
import com.original.service.storage.GridFSUtil;
import com.seaglasslookandfeel.widget.SGLabel;
import com.seaglasslookandfeel.widget.SGPanel;
import com.seaglasslookandfeel.widget.SGPopupMenu;

/**
 * Channel消息面板，由
 * <code>ContactHeadImage<code>联系人头像部分，加上
 * <code>ChannelMessageBodyPane<code>主体Body部分，加上
 * 下方的工具栏<code>ChannelMessageStatusBar</code>
 * 共3个部分组成。
 * 
 * @author WMS
 *
 */
public class ChannelMessagePane extends SGPanel
{
	private ChannelGridBagLayoutManager layoutMgr = 
			new ChannelGridBagLayoutManager(this);
	
	//消息主体面板
		ChannelMessageBodyPane body = null;
	//消息状态栏
		ChannelMessageStatusBar statusBar = null;
		MessageContainer container = null, originContainer = null;
	
	//联系人头像
	private ContactHeader header = new ContactHeader(
			IconFactory.loadIconByConfig("contactHeadIcon"));
	
	//消息箭头，左右两种(左为接受，右为发送)
	private SGLabel leftArrow = new SGLabel(IconFactory.loadIconByConfig("leftArrowIcon")),
			rightArrow = new SGLabel(IconFactory.loadIconByConfig("rightArrowIcon"));
	private Dimension arrowSize = new Dimension(ChannelConfig.getIntValue("arrowWidth"),
			ContactHeader.HEADSIZE.height);
	
	String uid = null; //联系人账号(用户名)，和ChannelMessagePane一一对应。
	boolean showDirection = true; //是否显示消息方向(即是否改变消息面板的布局方向)，如果为false，则只由receive方向
	
	public ChannelMessagePane() {
		this(new ChannelMessageStatusBar());
	}
	
	public ChannelMessagePane(ChannelMessageStatusBar statusBar) {
		this.container = new MessageContainer(statusBar);
		
		layoutMgr.setInsets(new Insets(0, 0, 0, 4));
	}
	
	/**
	 * 设置接受消息方向的布局
	 */
	private void setReceiveMsgLayout() {
		layoutMgr.setAlignment(ChannelGridBagLayoutManager.ALIGN_LEFT);
		leftArrow.setPreferredSize(arrowSize);
		
		//下面添加的子组件
		layoutMgr.addComToModel(header);
		if (showDirection) {
			layoutMgr.addComToModel(leftArrow);
		} else {
			layoutMgr.addCopyRegion(leftArrow);
		}

		layoutMgr.addComToModel(container);

		layoutMgr.addCopyRegion(leftArrow);
		layoutMgr.addCopyRegion(header);
	}
	
	/**
	 * 设置发送消息(回复消息)方向的布局
	 */
	private void setPostMsgLayout() {
		layoutMgr.setAlignment(ChannelGridBagLayoutManager.ALIGN_RIGHT);
		rightArrow.setPreferredSize(arrowSize);
		
		//下面添加的子组件
		layoutMgr.addCopyRegion(header);		
		layoutMgr.addCopyRegion(rightArrow);
		
		layoutMgr.setInsets(new Insets(0, -2, 0, 4)); //这里稍微做了一下偏移调整，偏移的同时也要注意<左右>的控件也需要调整！！！
		layoutMgr.addComToModel(container);
		
		layoutMgr.setInsets(new Insets(0, -2, 0, 4));
		if (showDirection) {
			layoutMgr.addComToModel(rightArrow);
		} else {
			layoutMgr.addCopyRegion(rightArrow);
		}
		
		layoutMgr.setInsets(new Insets(0, 0, 0, 4));
		layoutMgr.addComToModel(header);
	}
	
	/**
	 * 改变当前消息的默认布局方式
	 */
	public void changeMsgLayout() {
		String alignment = layoutMgr.getAlignment();
		if(alignment != ChannelGridBagLayoutManager.ALIGN_LEFT
				&& alignment != ChannelGridBagLayoutManager.ALIGN_RIGHT)
			return;
		
		layoutMgr.reset(true);
		layoutMgr.setInsets(new Insets(0, 0, 0, 4));
		if(alignment == ChannelGridBagLayoutManager.ALIGN_LEFT) {//接受消息方向
			setPostMsgLayout();
		}
		else {//发送(回复)消息方向
			setReceiveMsgLayout();
		}
	}
	
	/**
	 * 改变当前消息的默认布局方式，由消息类型来判断是否要改变
	 * @param msg 消息对象
	 */
	public void changeMsgLayoutIfNeed(ChannelMessage msg) {
		if(msg.isReceived()
				&& ChannelGridBagLayoutManager.ALIGN_RIGHT == layoutMgr.getAlignment())
		{
			changeMsgLayout();
		}
		else if(msg.isSent()
				&& ChannelGridBagLayoutManager.ALIGN_LEFT == layoutMgr.getAlignment())
		{
			changeMsgLayout();
		}
	}
	
	/**
	 * 显示消息于面板中
	 * @param msg
	 */
	private void layoutMessage(ChannelMessage msg) {
		if (showDirection) {
			if (msg.isReceived()) { // 是接受过来的消息
				setReceiveMsgLayout();
			} else if (msg.isSent()) { // 是发送(回复)过去的消息
				setPostMsgLayout();
			}
		} else {
			setReceiveMsgLayout();
			leftArrow.setVisible(false);// 不显示箭头
			rightArrow.setVisible(false);
		}
	}
	
	/**
	 * 初始化消息列表，用于{@link ChannelDesktopPane#initMessage(ChannelMessage)}时
	 * @param msg
	 */
	public void initMessage(ChannelMessage msg) 
	{
		String uName = msg.getContactName();
		if (uid == null) {// 第一次添加
			setUid(msg);
			layoutMessage(msg);
			
			body.initMessage(msg);
		} else if (uid.equals(uName)) {
			body.initMessage(msg);
		}
	}
	
	/**
	 * 添加消息至ChannelMessageBodyPane中
	 * @param msg 消息对象
	 * @param toFirst 是否添加在顶部(即只显示一条)
	 */
	public void addMessage(ChannelMessage msg, boolean toFirst)
	{
		String uName = msg.getContactName();
		if (uid == null) { // 第一次添加，需要设置布局
			setUid(msg);
			layoutMessage(msg);

			body.addMessage(msg, toFirst);
		} else if (uid.equals(uName)) {
			body.addMessage(msg, toFirst);
			if(showDirection) {
				changeMsgLayoutIfNeed(msg); // 检查是否要改变消息布局方向
			}
		}
	}
	
	/**
	 * 新建已知或未知联系人消息。注意和{@link #addMessage(ChannelMessage, boolean)}不一样。
	 * 对应{@link NewMessageBodyPane}面板
	 * @param msg 消息对象
	 */
	public void newMessage(ChannelMessage msg)
	{
		setUid(msg);
		if(statusBar instanceof NewMessageTopBar) {
			((NewMessageTopBar) statusBar).setMessage(msg);
		}
		setPostMsgLayout();
	}
	
	/**
	 * 显示消息的完整内容，对应{@link ShowMessageBodyPane}面板
	 * @param msg
	 */
	public void showMessage(ChannelMessage msg)
	{
		if(msg != null && msg.getMessageID() != null)
		{
			setUid(msg);
			layoutMessage(msg);
		}
	}
	
	public MessageContainer getOriginContainer() {
		return originContainer;
	}
	public void setOriginContainer(MessageContainer originContainer) {
		this.originContainer = originContainer;
	}
	
	/**
	 * 由消息来设置联系人的Uid
	 * @param msg 消息对象
	 */	
	public void setUid(ChannelMessage msg) {
		String uid = msg == null ? null : msg.getContactName();
		if (this.uid == null || !this.uid.equals(uid)) {
			this.uid = uid;
			header.setContactName(uid); // 目前设置联系人用户名即为Uid
			header.setHeadImageIcon(msg); //设置联系人头像
		}
	}

	/**
	 * 获取联系人的Uid
	 * @return
	 */
	public String getUid() {
		return this.uid;
	}

	/**
	 * 消息内容面板，其实就是ChannelMessageBody面板和ChannelMessageStatusBar上下两部分组成
	 * @author WMS
	 *
	 */
	public  class MessageContainer extends SGPanel
	{
		Color bgColor = Color.white;//背景颜色，默认白色
		
		public MessageContainer(ChannelMessageStatusBar msgStatusBar)
		{
			setLayout(new VerticalGridLayout(VerticalGridLayout.TOP_TO_BOTTOM,0,0,new Insets(0, 0, 5, 2)));
			setBackground(Color.white);
			
			//由状态栏的类型来设置不同的控件 及添加方式
			//注意子类在前，父类在后
			if(msgStatusBar instanceof NewMessageTopBar) {
				add(statusBar = msgStatusBar);
				add(body = new NewMessageBodyPane());
			}
			else if(msgStatusBar instanceof ShowMessageTopBar) {
				add(statusBar = msgStatusBar);
				add(body = new ShowMessageBodyPane());
			}
			else if(msgStatusBar instanceof ChannelMessageTopBar){
				add(statusBar = msgStatusBar);
				add(body = new ChannelMessageBodyPane());
			}
			else {
				add(body = new ChannelMessageBodyPane());
				add(statusBar = msgStatusBar);
			}
			
			//分别设置其关联组件
			body.setMessageContainer(this);
			statusBar.setMessageBody(body);
		}
		
		public ChannelMessageBodyPane getMessageBody() {
			return body;
		}
		
		public ChannelMessageStatusBar getMessageStatusBar() {
			return statusBar;
		}
		
		//设置背景效果
		public void setBackground(Color bg)
		{
			if(bg == null) return;

			if(bgColor == null ||
					(bgColor != null && !bgColor.equals(bg))) {
				bgColor = bg;
				//repaint();
			}
		}
		
		//绘制圆角边框及阴影效果
		protected void paintComponent(Graphics g)
		{
			Graphics2D g2d = GraphicsHandler.optimizeGraphics(g);
			int width = getWidth()-2, height = getHeight()-2;

			//绘制阴影2px
			GraphicsHandler.fillShadow(g2d, 4, width, height, 10, 0.2f);

			//填充背景
			g2d.setColor(bgColor);
			g2d.fillRoundRect(0, 0, width, height, 10, 10);
//			GraphicsHandler.suspendRendering(g2d);
		}
	}
	
	/**
	 * 联系人头像标签，可以通用
	 * @author WMS
	 *
	 */
	public static class ContactHeader extends SGPanel
	{
		public static  Dimension SIZE =  new Dimension(78, 100),
				HEADSIZE = new Dimension(72, 72);
		
		private LocationIcon headIcon = null;//联系人头像
		private String contactName = "联系人";//联系人用户名
		private SGPopupMenu headPopupMenu = new ChannelPopupMenu(this),
				contactPopupMenu = new ToolTip();
		private Rectangle headBounds = null,
				contactBounds = new Rectangle(0, SIZE.height-20, SIZE.width, 16);		
		
		public ContactHeader(Icon headImage)
		{
			headIcon = new LocationIcon(headImage);
			this.contactName = contactName == null ? "" : contactName;
			setContactHeader();
		}
		
//		public ContactHeader(URL headImageURL)
//		{
//			headIcon = new LocationIcon(headImageURL);
//			this.contactName = contactName == null ? "" : contactName;
//			setContactHeader();
//		}
//		
//		public ContactHeader(byte[] headImageBytes)
//		{
//			headIcon = new LocationIcon(headImageBytes);
//			this.contactName = contactName == null ? "" : contactName;
//			
//			setContactHeader();
//		}
		
		/**
		 * 设置联系人用户名
		 * @param contactName 联系人用户名，建议不要太长
		 */
		public void setContactName(String contactName)
		{
			this.contactName = contactName;
			repaint();
		}
		
		/**
		 * 自动调整头像的大小，固定72*72。
		 * 同时设置一些控制事件
		 */
		private void setContactHeader()
		{
			if(headIcon != null) {
				Image headImage = headIcon.getImage().
						getScaledInstance(HEADSIZE.width, HEADSIZE.height, Image.SCALE_SMOOTH);
				headIcon = new LocationIcon(new ImageIcon(headImage));
			}
			setPreferredSize(SIZE);
			addMouseMotionListener(new MouseMotionAdapter()
			{
				public void mouseMoved(MouseEvent e)
				{
					if(headBounds == null) {
						headBounds = headIcon.getBounds();
					}
					if (headBounds.contains(e.getPoint())) {
						setCursor(ChannelConstants.HAND_CURSOR);
					} else if (contactBounds.contains(e.getPoint())) {
						if (!ChannelUtil.isEmpty(contactName)) {
							contactPopupMenu.setToolTipText(contactName);
							contactPopupMenu.show(ContactHeader.this, e.getX(), e.getY());
						}
					} else {
						setCursor(ChannelConstants.DEFAULT_CURSOR);
					}
				}
			});
			addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent e)
				{
					if(headBounds == null) {
						headBounds = headIcon.getBounds();
					}
					if(headBounds.contains(e.getPoint())) {
						headPopupMenu.show(ContactHeader.this, 
								-(headPopupMenu.getPreferredSize().width-SIZE.width)/2, SIZE.width);
					}
				}
			});
		}
		
		public LocationIcon getHeadImageIcon()
		{
			return headIcon;
		}
		public String getContactName()
		{
			return contactName;
		}

		//开始绘制用户头像和用户名
		protected void paintComponent(Graphics g)
		{
			RenderingHints hints = GraphicsHandler.DEFAULT_RENDERING_HINT_ON;
			hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			Graphics2D g2d = GraphicsHandler.optimizeGraphics(g, hints);
			
			int width = HEADSIZE.width, height = HEADSIZE.height;
			
			Area outer = new Area(new RoundRectangle2D.Double(0,0,width+4,height+4,10,10));
			Area inner = new Area(new RoundRectangle2D.Double(2,2,width,height,6,6));
			outer.subtract(inner);
			
			//绘制阴影2px
			GraphicsHandler.fillShadow(g2d, 2, width+4, height+4, 10, 0.2f);
			
			//绘制用户头像
			g2d.translate(2, 2);
			g2d.drawImage(headIcon.getImage(), 0, 0, this);
			headIcon.setBounds(0, 0, headIcon.getWidth(), headIcon.getHeight());
			
			//绘制边框
			g2d.translate(-2, -2);
			g2d.setColor(Color.white);
			g2d.fill(outer);
			
			//最后绘制文字
			if(contactName != null) {
				g2d.translate(0, SIZE.height-4);
				g2d.setColor(Color.white);
				g2d.setFont(ChannelConstants.DEFAULT_FONT.deriveFont(16F));//一个中文字宽16px；英文8px
				
				int fontWidth = g.getFontMetrics().stringWidth(contactName);
				if (fontWidth >= SIZE.width) {
					g2d.drawString(contactName, 0, 0);
				} else {
					g2d.drawString(contactName, (SIZE.width - g
							.getFontMetrics().stringWidth(contactName)) / 2, 0);
				}
			}
		}
		
		//设置联系人头像，此算法待优化。以后可能放置在线程中！
		public void setHeadImageIcon(ChannelMessage msg) {
			if(msg != null) {
				People pm =ChannelService.getInstance().getPeopleManager().getPeopleByMessage(msg) ;		
				if (pm != null)
				{		
					String chn = msg.getChannelAccount().getChannel().getName();
					Account acc = pm.getAccountMap().get(chn);
					if (chn != null && acc.getAvatar() != null)
					{
						try {
							GridFSDBFile dbfile = GridFSUtil.getGridFSUtil().getFile(acc.getAvatar());
							String fn = dbfile.getFilename();
							String path = QQEnvironment.getConfigTempDir() + fn;
							File file = new File(path);
							if (!file.exists()) {
								dbfile.writeTo(path);
							}
							ImageIcon image = new ImageIcon(path);
							Image headImage = image.getImage().getScaledInstance(72, 72, Image.SCALE_SMOOTH);
							headIcon.setIcon(new ImageIcon(headImage));
						} catch (IOException ex) {
							
						} 
					}			
				}
			}
		}
	}
}