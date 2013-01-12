package com.original.serive.channel.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.original.serive.channel.layout.ChannelGridBagLayoutManager;
import com.original.serive.channel.layout.VerticalGridLayout;
import com.original.serive.channel.util.ChannelConfig;
import com.original.serive.channel.util.ChannelConstants;
import com.original.serive.channel.util.GraphicsHandler;
import com.original.serive.channel.util.IconFactory;
import com.original.serive.channel.util.LocationIcon;
import com.original.service.channel.ChannelMessage;

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
public class ChannelMessagePane extends JPanel
{
	private ChannelGridBagLayoutManager layoutMgr = 
			new ChannelGridBagLayoutManager(this);
	
	//消息主体面板
		ChannelMessageBodyPane body = null;
	//消息状态栏
		ChannelMessageStatusBar statusBar = null;
		MessageContainer container = null;
	
	//联系人头像
	private ContactHeader header = new ContactHeader(
			IconFactory.loadIconByConfig("contactHeadIcon"));
	
	//消息箭头，左右两种(左为接受，右为发送)
	private JLabel leftArrow = new JLabel(IconFactory.loadIconByConfig("leftArrowIcon")),
			rightArrow = new JLabel(IconFactory.loadIconByConfig("rightArrowIcon"));
	private Dimension arrowSize = new Dimension(ChannelConfig.getIntValue("arrowWidth"),
			ContactHeader.HEADSIZE.height);
	
	String uid = null; //联系人账号(用户名)，和ChannelMessagePane一一对应。
	
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
		layoutMgr.addComToModel(leftArrow);

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
		layoutMgr.addComToModel(rightArrow);
		
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
	 * 初始化消息列表，用于{@link ChannelDesktopPane#initMessage(ChannelMessage)}时
	 * @param msg
	 */
	public void initMessage(ChannelMessage msg) 
	{
		String uName = msg.getContactName();
		if (uid == null) {// 第一次添加
			uid = uName;
			header.setContactName(uName);

			if (msg.isReceived()) { // 是接受过来的消息
				setReceiveMsgLayout();
			} else if (msg.isSent()) { // 是发送(回复)过去的消息
				setPostMsgLayout();
			}

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
			uid = uName;

			body.addMessage(msg, toFirst);
			header.setContactName(uName);

			if (msg.isReceived()) { // 是接受过来的消息
				setReceiveMsgLayout();
			} else if (msg.isSent()) { // 是发送(回复)过去的消息
				setPostMsgLayout();
			}
		} else if (uid.equals(uName)) {
			body.addMessage(msg, toFirst);
			changeMsgLayoutIfNeed(msg); // 检查是否要改变消息布局方向
		}
	}
	
	/**
	 * 新建已知或未知联系人消息。注意和{@link #addMessage(ChannelMessage, boolean)}不一样。
	 * 对应{@link NewMessageBodyPane}面板
	 * @param msg 消息对象
	 */
	public void newMessage(ChannelMessage msg)
	{
		header.setContactName(msg == null ? null : msg.getContactName());
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
			header.setContactName(msg.getContactName());
			setReceiveMsgLayout();
		}
	}
	
	/**
	 * 消息内容面板，其实就是ChannelMessageBody面板和ChannelMessageStatusBar上下两部分组成
	 * @author WMS
	 *
	 */
	public  class MessageContainer extends JPanel
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
	public static class ContactHeader extends JPanel
	{
		private LocationIcon headImageIcon = null;//联系人头像
		private String contactName = "联系人";//联系人用户名
		private ChannelPopupMenu popupMenu = new ChannelPopupMenu(this);
		
		public static  Dimension SIZE =  new Dimension(78, 100),
				HEADSIZE = new Dimension(72, 72);
		
		public ContactHeader(Icon headImage)
		{
			headImageIcon = new LocationIcon(headImage);
			this.contactName = contactName == null ? "" : contactName;
			setContactHeader();
		}
		
//		public ContactHeader(URL headImageURL)
//		{
//			headImageIcon = new LocationIcon(headImageURL);
//			this.contactName = contactName == null ? "" : contactName;
//			setContactHeader();
//		}
//		
//		public ContactHeader(byte[] headImageBytes)
//		{
//			headImageIcon = new LocationIcon(headImageBytes);
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
			if(headImageIcon != null) {
				Image headImage = headImageIcon.getImage().
						getScaledInstance(HEADSIZE.width, HEADSIZE.height, Image.SCALE_SMOOTH);
				headImageIcon = new LocationIcon(new ImageIcon(headImage));
			}
			setPreferredSize(SIZE);
			addMouseMotionListener(new MouseMotionAdapter()
			{
				public void mouseMoved(MouseEvent e)
				{
					if(headImageIcon.getBounds().contains(e.getPoint())) {
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
					if(headImageIcon.getBounds().contains(e.getPoint())) {
						popupMenu.show(ContactHeader.this, 
								-(popupMenu.getPreferredSize().width-SIZE.width)/2,
								SIZE.width);
					}
				}
			});
		}
		
		public LocationIcon getHeadImageIcon()
		{
			return headImageIcon;
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
			g2d.drawImage(headImageIcon.getImage(), 0, 0, this);
			headImageIcon.setBounds(0, 0, headImageIcon.getWidth(), headImageIcon.getHeight());
			
			//绘制边框
			g2d.translate(-2, -2);
			g2d.setColor(Color.white);
			g2d.fill(outer);
			
			//最后绘制文字
			if(contactName != null) {
				g2d.translate(0, SIZE.height-4);
				g2d.setColor(Color.white);
				g2d.setFont(ChannelConstants.DEFAULT_FONT.deriveFont(16F));
				g2d.drawString(contactName, 
						(SIZE.width-g.getFontMetrics().stringWidth(contactName))/2, 0);
			}
		}
	}
}
