package com.original.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.border.EmptyBorder;
import javax.swing.event.SwingPropertyChangeSupport;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import com.original.channel.ChannelAccesser;
import com.original.channel.ChannelNativeCache;
import com.original.client.ChannelEvent;
import com.original.client.EventConstants;
import com.original.client.border.DottedLineBorder;
import com.original.client.border.SingleLineBorder;
import com.original.client.layout.VerticalGridLayout;
import com.original.client.ui.ChannelMessagePane.MessageContainer;
import com.original.client.util.ChannelConfig;
import com.original.client.util.ChannelConstants;
import com.original.client.util.ChannelHyperlinkListener;
import com.original.client.util.ChannelUtil;
import com.original.client.util.IconFactory;
import com.original.service.channel.ChannelMessage;
import com.original.service.channel.Constants;
import com.original.service.channel.Utilies;
import com.original.service.channel.core.ChannelService;
import com.original.widget.OTextField;
import com.seaglasslookandfeel.widget.SGButton;
import com.seaglasslookandfeel.widget.SGLabel;
import com.seaglasslookandfeel.widget.SGPanel;
import com.seaglasslookandfeel.widget.SGTextPane;

/**
 * 消息Channel主体面板，就是显示消息的面板，默认显示第一条最新的消息(即JList中的第一条记录)。
 * 可以展开和折叠JList查看和隐藏消息。
 * @author WMS
 *
 */
public class ChannelMessageBodyPane extends SGPanel implements EventConstants
{
	protected Vector<ChannelMessage> messageBodyList = new Vector<ChannelMessage>();
	
	private MessageContainer container = null; //当前面板的载体，即父面板
	private PropertyChangeSupport changeSupport =
			new SwingPropertyChangeSupport(this);
	
	protected static ChannelService channelService = ChannelAccesser.getChannelService();
	protected Lock channelLock = new ReentrantLock();
	
	public ChannelMessageBodyPane()
	{
		//这里设置messageBody面板的布局方式为从下而上的垂直布局方式
		setLayout(new VerticalGridLayout(VerticalGridLayout.BOTTOM_TO_TOP,
				0,0,new Insets(0, 0, 0, 0)));
	}
	
	/**
	 * 设置当前面板的载体面板
	 * @param container
	 */
	public void setMessageContainer(MessageContainer container)
	{
		this.container = container;
	}
	public MessageContainer getMessageContainer()
	{
		return this.container;
	}
	
	/**
	 * 获取当前面板对应的状态栏
	 * @return
	 */
	public ChannelMessageStatusBar getMessageStatusBar() 
	{
		ChannelMessageStatusBar statusBar = null;
		if (container != null) {
			statusBar = container.getMessageStatusBar();
		} else {
			PropertyChangeListener[] listeners = changeSupport.getPropertyChangeListeners();
			for (int i = 0; i < listeners.length; i++) {
				if (listeners[i] instanceof ChannelMessageStatusBar) {
					statusBar = (ChannelMessageStatusBar) listeners[i];
					break;
				}
			}
		}
		return statusBar;
	}
	
	/**
	 * 添加消息改变事件监听器，当Body面板一有消息添加时，将会通知添加的事件监听器
	 * @param listener
	 */
	public void addMessageChangeListener(PropertyChangeListener listener)
	{
		if(listener != null)
			changeSupport.addPropertyChangeListener(listener);
	}
	
	/**
	 * 通知消息属性发生改变，给所有的事件监听器
	 * @param changePropertyName 消息改变属性名称，属性名称都在{@link EventConstants}常量中
	 * @param oldValue 旧值
	 * @param newValue 新值
	 */
	private void fireMessageChange(String changePropertyName, Object oldValue, Object newValue) {
		PropertyChangeListener[] listeners = changeSupport.getPropertyChangeListeners();
		for(int i=0; i<listeners.length; i++)
		{
			listeners[i].propertyChange(
					new PropertyChangeEvent(this, changePropertyName, oldValue, newValue));
		}
	}
	public void fireMessageChange(ChannelMessage msg, int value) {
		if (msg.isMail()) {
			fireMessageChange(MAIL_COUNT_CHANGE_PROPERTY, 0, value);
		} else if (msg.isQQ()) {
			fireMessageChange(QQ_COUNT_CHANGE_PROPERTY, 0, value);
		} else if (msg.isWeibo()) {
			fireMessageChange(WEIBO_COUNT_CHANGE_PROPERTY, 0, value);
		}
	}

	/**
	 * 初始化消息列表，用于{@link ChannelDesktopPane#initMessage(ChannelMessage)}时
	 * @param msg
	 */
	public void initMessage(ChannelMessage msg) 
	{
		if(messageBodyList.isEmpty()) { //消息列表为空，表示还没有子面板，需要创建一个；以后直接保存在messageBodyList中，不再创建
			ChannelMessageBodyPane.Body body = new ChannelMessageBodyPane.Body(msg, true);
			body.setBorder(new EmptyBorder(0, 10, 0, 10));
			this.add(body);
		}
		
		messageBodyList.add(0, msg);
		fireMessageChange(msg, 1);
	}
	

	/**
	 * 当前面板List中添加消息，默认添加在第一行。
	 * @param msg 消息对象
	 * @param toFirst 是否添加在顶部(即只显示一条)
	 */
	public void addMessage(ChannelMessage msg, boolean toFirst) 
	{		
		ChannelMessageBodyPane.Body body = new ChannelMessageBodyPane.Body(msg, toFirst);
		if(toFirst) {
			body.setBorder(new EmptyBorder(0, 10, 0, 10));
			
			this.removeAll();
			this.add(body);
			this.validate();
			
			messageBodyList.add(msg);
		}
		else {
			if(this.getComponentCount() > 0) { //下余面板的边框，稍微复杂点
				body.setBorder(BorderFactory.createCompoundBorder(
						new EmptyBorder(0, 10, 0, 10), 
						new DottedLineBorder(DottedLineBorder.BOTTOM, new Color(213, 213, 213), new float[]{3f,4f})));
			}
			else { //第一个面板的边框
				body.setBorder(BorderFactory.createCompoundBorder(
						new EmptyBorder(0, 10, 0, 10), 
						new SingleLineBorder(SingleLineBorder.BOTTOM, new Color(0, 0, 0, 0), true)));
			}
			if(this.getLayout() instanceof VerticalGridLayout) {//程序有时运行时，不知为何变成CardLayout布局
				this.add(body);
				this.validate();
			}
		}
		fireMessageChange(msg, 1);
	}
	
	/**
	 * 当前面板List中移除消息body面板
	 * @param msg 消息对象
	 */
	public void removeMessage(ChannelMessage msg) {
		Body body = findMessageBody(msg);
		if(body != null) {
			removeMessageBody(body, body.toFirst);
		}
	}
	
	/**
	 * 从当前面板中移除body面板对象。
	 * @param body 消息body面板
	 * @param toFirst 是否添加在顶部(即只显示一条)
	 */
	private void removeMessageBody(ChannelMessageBodyPane.Body body, boolean toFirst)
	{
		Body old = (Body)this.getComponent(0);
		if (old == body && !toFirst) {// 是最后一个body
			old = (Body) this.getComponent(1); // 重新设置倒数第2个body的边框
			old.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(0,
					10, 0, 10), new SingleLineBorder(SingleLineBorder.BOTTOM,
					new Color(0, 0, 0, 0), true)));
		}
		
		this.remove(body);
		this.validate();
		
		notifyToChangeMessage(body.iMsg, toFirst);
	}
	
	/**
	 * 由消息对象查找其对应的消息body面板
	 * @param msg 消息对象
	 * @return
	 */
	public ChannelMessageBodyPane.Body findMessageBody(ChannelMessage msg)
	{
		Body child = null;
		for(int i=0; i<this.getComponentCount(); i++) {
			child = (Body)this.getComponent(i);
			if(child.iMsg.equals(msg)) {
				break;
			}
		}
		return child;
	}
	
	/**
	 * 通知父面板改变当前消息的显示，以及消息的状态属性等等。用在removeMessageBody()之后
	 * @param msg 消息对象
	 */
	private void notifyToChangeMessage(ChannelMessage msg, boolean toFirst)
	{
		ChannelDesktopPane desktop = ChannelNativeCache.getDesktop();
		if (toFirst) { // 如果只显示一条，即顶部显示
			messageBodyList.remove(messageBodyList.size() - 1);
			if (messageBodyList.isEmpty()) {
				
				Container child = this, parent = container;
				while (!(parent instanceof JViewport)) {
					parent.remove(child);
					child = parent;
					parent = parent.getParent();
				}
				parent.validate();
				
			} else {
				ChannelMessage newMsg = messageBodyList.get(messageBodyList.size() - 1);
				ChannelMessageBodyPane.Body body = new ChannelMessageBodyPane.Body(newMsg, toFirst);
				body.setBorder(new EmptyBorder(0, 10, 0, 10));

				//显示倒数第2条消息
					this.removeAll();
					this.add(body);
					this.validate();

				ChannelMessagePane parent = (ChannelMessagePane) container.getParent();
				parent.changeMsgLayoutIfNeed(newMsg);
				parent.validate();
				
				//消息数-1
				fireMessageChange(newMsg, -1);
			}
		} else {
			Container parent = container;
			while (!(parent instanceof JViewport)) {
				parent = parent.getParent();
			}
			parent.validate();

			if (this.getComponentCount() == 0) {
				desktop.removeShowComp(PREFIX_SHOWALL 	+ msg.getContactName());
			} else {
				desktop.validate();
			}
			
			ChannelMessagePane cmp  = (ChannelMessagePane)container.getParent();
			MessageContainer originContainer = cmp.getOriginContainer();
			if(originContainer != null) {
				ChannelMessageBodyPane originBody = originContainer.getMessageBody();
				ChannelMessage firstMsg = originBody.getChannelMessage();
				if (firstMsg.equals(msg)) { // 是第一条消息
					originBody.notifyToChangeMessage(firstMsg, true); //从面板和消息List中移除
					
					//同时检查是否需要改变面板的消息方向
//					ChannelMessagePane originParent = (ChannelMessagePane) originContainer.getParent();
//					originParent.changeMsgLayoutIfNeed(firstMsg);
				} else { // 否则只改变原消息数
					originBody.getChannelMessages().remove(msg);//只从消息List中移除
					
					originBody.fireMessageChange(firstMsg, -1);
				}
			}
		}
		desktop.repaint();//刷新桌面
	}
	
	/**
	 * 显示所有的消息，即展开消息面板
	 */
	public void showAllMessage()
	{
		ChannelMessage newMsg = null;
//		ChannelMessagePane nw = new ChannelMessagePane(new ChannelMessageTopBar());
		ChannelMessagePane nw = new ChannelMessagePane(new ListMessageTopBar());
		nw.showDirection = false; //不显示消息方向
		
		for (ChannelMessage msg : messageBodyList) {
			if (newMsg == null) {
				newMsg = msg;
			}
			nw.addMessage(msg, false);
		}
		nw.setOriginContainer(container);

		ChannelDesktopPane desktop = ChannelNativeCache.getDesktop();
		desktop.addOtherShowComp(PREFIX_SHOWALL + newMsg.getContactName(), nw);
	}
	
	/**
	 * 获取当前消息主体面板中的一条消息，默认第一条。
	 * @return
	 */
	public ChannelMessage getChannelMessage() {
		if (!messageBodyList.isEmpty()) {
			return messageBodyList.lastElement();
		} else {
			int msgCount = this.getComponentCount();
			if (msgCount > 0) {
				Body body = (Body) this.getComponent(msgCount - 1);
				return body.iMsg;
			}
		}
		return null;
	}
	public Vector<ChannelMessage> getChannelMessages() {
		return messageBodyList;
	}
	
	//主体部分，即下面3个部分的整合。
	public class Body extends SGPanel implements ChannelEvent, EventConstants
	{
		//上中下3个子控件(面板)
		Top top = new Top();
		Center center = new Center();
		Bottom bottom = new Bottom();
		
		ChannelMessage iMsg;//消息对象
		boolean toFirst; //是否添加至顶部(即是否只显示1条)
		
		public Body() { }
		public Body(ChannelMessage msg, boolean toFirst)
		{
			this.iMsg = msg;
			this.toFirst = toFirst;
			
			setPreferredSize(new Dimension(ChannelConfig.getIntValue("msgBodyWidth"),  
					top.SIZE.height + center.SIZE.height + (!bottom.isVisible()?0:bottom.SIZE.height)));
			setLayout(new BorderLayout(5,5));
			
			//添加子控件
			add(top, BorderLayout.NORTH);
			
			add(center.getCenterScrollPane(), BorderLayout.CENTER);
			
			add(bottom, BorderLayout.SOUTH);
			
			analyzeMessage(msg);
		}
		
		/**
		 * 分析消息来源类型，以及方向等属性
		 * @param msg 消息对象
		 */
		private void analyzeMessage(ChannelMessage msg)
		{
			//设置背景(由消息来源类型来区分)
			if(msg.isReceived())
				setBackground(Color.white);
			else
				setBackground(new Color(186, 212, 229));

			//为子控件赋值
			top.createMessageHeader(msg);
			center.showMessagePart(msg);
		}
		
		//设置背景
		public void setBackground(Color bg)
		{			
			if (!toFirst) // 如果是全部添加，则设置自己的背景色
			{
				super.setBackground(bg);
			} else if (container != null) // 如果是添加最新一条(第一条)，则设置父面板的背景色
			{
				container.setBackground(bg);
			}
		}
		
		//设置大小，只改变高度(宽度仍保持不变)
		public void autoAdjustHeight(boolean isOn)
		{
			Dimension dim = this.getPreferredSize();
			dim.height = top.SIZE.height
					+ (isOn ? center.getPreferredScrollableViewportHeight() : center.SIZE.height-10)
					+ (bottom.isVisible() ? bottom.SIZE.height + 5 * 3 : 5 * 2); // 5px为垂直间距
			setPreferredSize(dim);
		}
		
		/**
		 * 打开或关闭快速回复功能
		 * @param isOn 若为true，则是打开；否则为关闭
		 */
		public void doQuickReply(ActionEvent ae, boolean isOn) {
			if(isOn) {
				top.setVisible(QUICK_REPLY, false);
				top.setVisible(SHOW_COMPLETE, true);
				top.notifyStatusChange(iMsg, STATUS_READ, true); //通知已读
				center.showComplete(iMsg);
				bottom.showMessageReplyArea();
				
				autoAdjustHeight(true);
			}
			else {
				top.setVisible(QUICK_REPLY, true);
				top.setVisible(SHOW_COMPLETE, false);
				center.showMessagePart(iMsg);
				bottom.hideReplyContent();
				
				autoAdjustHeight(false);
			}
		}
		
		public ChannelMessage getBodyMessage() {
			// TODO 自动生成的方法存根
			return iMsg;
		}
		
		//编辑
		public void doEdit(ActionEvent ae) {
			ChannelMessage msg = iMsg.clone();
			
			ChannelMessagePane cmp = null;
			if (msg.isRepost()) {// 是转发
				msg.setAction(Constants.ACTION_REPOST);
				cmp = new ChannelMessagePane(new NewMessageTopBar(true));
			} else {// 默认回复
				msg.setAction(Constants.ACTION_REPLY);
				cmp = new ChannelMessagePane(new NewMessageTopBar(false));
			}
			cmp.newMessage(msg);
			
			ChannelDesktopPane desktop = ChannelNativeCache.getDesktop();
			desktop.addOtherShowComp(PREFIX_SHOWANDNEW + msg.getContactName(), cmp);
		}
		
		//保存
		public void doSave(ActionEvent ae) {
			
		}
		
		//删除
		public void doDelete(ActionEvent ae) {
			if (ChannelUtil.confirm(null, "确认删除", "是否删除该消息？")) {
				try {
					channelService.trashMessage(iMsg); //先从数据库里面删除
					
					//再从界面删除
					removeMessageBody(this, toFirst);
				} catch (Exception ex) {
					ChannelUtil.showMessageDialog(null, "错误", ex);
				}
			}
		}
		
		//完整信息
		public void doShowComplete(ActionEvent ae) {
			ChannelMessage newMsg = iMsg.clone();
			ChannelMessagePane nw =  new ChannelMessagePane(new ShowMessageTopBar(newMsg));
			nw.showMessage(newMsg);
			((ShowMessageBodyPane)nw.body).setMessageToGUI(newMsg);
			((ShowMessageBodyPane)nw.body).setOriginMessageBody(this);

			ChannelDesktopPane desktop = ChannelNativeCache.getDesktop();
			desktop.addOtherShowComp(PREFIX_SHOW+newMsg.getContactName(), nw);
		}
	}
	
	//头部面板
	class Top extends SGPanel implements ActionListener, EventConstants
	{
		Dimension SIZE = new Dimension(ChannelConfig.getIntValue("msgBodyWidth")-20, 45); 
		private SimpleDateFormat messageFormat = new SimpleDateFormat("MM月dd日 HH:mm");//消息时间格式
		private SGLabel messageHeader = new SGLabel();
		
		//一些控制按钮
		private SGButton btnReply = createCtrlButton("快速回复", QUICK_REPLY),
				btnSave = createCtrlButton("保存", SAVE),
				btnDel = createCtrlButton("删除", DELETE),
				btnEdit = createCtrlButton("编辑", EDIT),
				btnShowAll = createCtrlButton("完整信息", SHOW_COMPLETE);
		
		public Top() { setOpaque(false); }
		
		public ChannelEvent getChannelEvent() {
			return (Body)this.getParent();
		}
		
		/**
		 * 设置消息头部内容，同时显示在标签中
		 * @param msg 消息对象
		 */
		private void setMessageHeader(ChannelMessage msg)
		{
			if(msg != null && msg.getMessageID() != null)
			{
				notifyStatusChange(msg, STATUS_UNKNOWN, false);
				
				messageHeader.setForeground(ChannelConstants.LIGHT_TEXT_COLOR);
				messageHeader.setFont(ChannelConstants.DEFAULT_FONT);
				//目前messageHeader只显示收发的时间
				if(msg.isSent()) {
					messageHeader.setText(msg.getSentDate() == null ? "" : messageFormat.format(msg.getSentDate()));
				} else {
					messageHeader.setText(msg.getReceivedDate() == null ? "" : messageFormat.format(msg.getReceivedDate()));
				}
				messageHeader.setIconTextGap(10);
				messageHeader.setHorizontalTextPosition(SGLabel.RIGHT);
			}
		}
		
		/**
		 * 通知消息状态发生改变，目前主要更换图标。
		 */
		public void notifyStatusChange(ChannelMessage msg, String statusConstant, boolean notifyDB)
		{
			String icon = "Icon"; //图标名称
			if (msg.isQQ()) 	icon = "QQ" + icon;
			else if (msg.isWeibo())	icon = "Weibo" + icon;
			else if (msg.isMail())	icon = "Mail" + icon;

			if (statusConstant == STATUS_UNKNOWN) {// 未知状态，则需要解析msg。用于初始添加时。
				if (msg.hasProcessed()) {
					statusConstant = STATUS_POST;
				} else if (msg.hasRead()) {
					statusConstant = STATUS_READ;
				} else {
					statusConstant = STATUS_UNREAD;
				}
			}
			
			if (statusConstant == STATUS_UNREAD)
				icon = "default" + icon;
			else if (statusConstant == STATUS_READ)
				icon = "read" + icon;
			else if (statusConstant == STATUS_POST)
				icon = "post" + icon;
			
			if(notifyDB) {
				if(statusConstant == STATUS_READ) {
					if(!msg.hasRead()) {
						channelService.updateMessageFlag(msg, ChannelMessage.FLAG_SEEN, "1");
						msg.setRead(true);
					}
				} else if(statusConstant == STATUS_POST) {
					if(!msg.hasProcessed()) {
						channelService.updateMessageFlag(msg, ChannelMessage.FLAG_DONE, "1");
						msg.setProcessed(true);
					}
				}
			}
			
			messageHeader.setIcon(IconFactory.loadIconByConfig(icon));
		}
		
		/**
		 * 由消息对象创建消息头部
		 * @param msg 消息对象
		 */
		public void createMessageHeader(ChannelMessage msg)
		{			
			if(msg != null && msg.getMessageID() != null)
			{				
				setMessageHeader(msg);//分析消息来源
				setLayout(null); //空布局，从而精确定位控件
				setPreferredSize(SIZE);
				
				//开始添加控件
				Dimension dim = messageHeader.getPreferredSize();
				messageHeader.setBounds(0, 10, dim.width, dim.height);
				add(messageHeader);
				
				btnReply.setBounds(SIZE.width - (60 * 2 + 85), 10, 85, 28);//1
				add(btnReply);
				btnShowAll.setBounds(SIZE.width - (60 * 2 + 85), 10, 85, 28);//2
				add(btnShowAll);
				btnEdit.setBounds(SIZE.width - (60 * 2 + 85), 10, 85, 28);//3
				add(btnEdit);
				
				btnSave.setBounds(SIZE.width-60*2, 10, 60, 28);
				add(btnSave);
				
				btnDel.setBounds(SIZE.width-60, 10, 60, 28);
				add(btnDel);
				
				//下面设置一些按钮的可见性：
				btnEdit.setVisible(false);//1、编辑按钮只有在草稿箱中的信息显示时，才可见
				
				if (msg.hasProcessed()) { //2、快速回复和完整信息，不能同时出现
					btnReply.setVisible(false);
				} else {
					btnShowAll.setVisible(false);
				}
				
				if (msg.hasTrashed()) {//3、已在垃圾箱中
					btnReply.setVisible(false);
					btnSave.setVisible(false);
					
					btnShowAll.setVisible(true);
					btnShowAll.setBounds(SIZE.width-60-85, 10, 85, 28);
				}
				
				if(msg.hasDrafted()) {//4、已在草稿箱中
					btnReply.setVisible(false);
					btnSave.setVisible(false);
					btnShowAll.setVisible(false);
					
					btnEdit.setVisible(true);
					btnEdit.setBounds(SIZE.width-60*2, 10, 60, 28);
				}
			}
		}
		
		/**
		 * 创建带有控制功能的按钮
		 * @param text 按钮名称
		 * @param actionCommand 按钮名称(国际化)
		 * @return
		 */
		public SGButton createCtrlButton(String text, String actionCommand)
		{
			SGButton button = new SGButton(text);
			button.setMargin(new Insets(0, 0, 0, 0));
			button.setActionCommand(actionCommand);
			button.addActionListener(this);
			return button;
		}
		
		/**
		 * 显示或隐藏功能按钮
		 * @param actionCommand 按钮名称(国际化)
		 * @param isVisible 是否可见
		 */
		public void setVisible(String actionCommand, boolean isVisible)
		{
			for(int i=0; i<getComponentCount(); i++)
			{
				Component comp = getComponent(i);
				if(comp instanceof AbstractButton
						&& ((AbstractButton) comp).getActionCommand() == actionCommand)
				{
					if(comp.isVisible() != isVisible)
						comp.setVisible(isVisible);
					return;
				}
			}
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{			
			ChannelEvent ce = this.getChannelEvent();
			
			if (QUICK_REPLY == e.getActionCommand()) { // 快速回复
				ce.doQuickReply(e, true);
			} else if (SAVE == e.getActionCommand()) { // 保存
				ce.doSave(e);
			} else if (EDIT == e.getActionCommand()) { // 编辑
				ce.doEdit(e);
			} else if (DELETE == e.getActionCommand()) { // 删除
				ce.doDelete(e);
			} else if (SHOW_COMPLETE == e.getActionCommand()) { // 完整显示
				ce.doShowComplete(e);
			}
		}
	}
	
	//中间面板
	static class Center extends SGTextPane implements  EventConstants
	{
		static Dimension SIZE = new Dimension(ChannelConfig.getIntValue("msgBodyWidth")-60,  25);
		EditorKit editorKit = createDefaultEditorKit();
		HTMLEditorKit	htmlEditorKit = new HTMLEditorKit();		
		
		JScrollPane centerScrollPane = null;
		public Center()
		{
			//设置默认样式，等面板显示完整内容，再设置new HTMLEditorKit()
			setEditorKit(new HTMLEditorKit());//设置样式为HTML编辑样式
			StyleSheet sheet= htmlEditorKit.getStyleSheet();
			sheet.addRule("a {text-decoration: none; color: blue; }");
			this.setFont(ChannelConstants.DEFAULT_FONT);
			
			setEditable(false);//不可编辑
			setOpaque(false);
			setBackground(new Color(0, 0, 0, 0));//当上面透明无效时，这是唯一的办法
			setBorder(new EmptyBorder(0, 45, 0, 10));
			addHyperlinkListener(new ChannelHyperlinkListener());//支持超链
		}
		
		public JScrollPane getCenterScrollPane() {
			if (centerScrollPane == null) {
				centerScrollPane = ChannelUtil.createScrollPane(this);
				centerScrollPane
						.setPreferredSize(new Dimension(SIZE.width, 185));
			}
			return centerScrollPane;
		}
		
		//获取Center面板相对合适的高度
		public int getPreferredScrollableViewportHeight() {
			return Math.min(this.getPreferredScrollableViewportSize().height, 
					getCenterScrollPane().getPreferredSize().height);
		}
		
		/**
		 * 这里覆盖原来的setText()方法，目的是当文本超出面板宽度时，会自动截取，多余部分以省略号代替
		 */
		public void setText(String text)
		{
			text = cutText(getFont(), text);
			super.setText(text);
		}
		
		public static String cutText(Font font, String text) 
		{
			if(text == null || text.isEmpty()) return text;
			//清除换行符：\r \n <BR>等
			text = text.replaceAll("\r|\n|\\<BR\\>|\\<br\\>|<p>|</p>", "");
			text = text.replaceAll("<img.*?>", "[图片]");
			
			//如果文本的长度超出面板的宽度，则多余的部分会被截断，以“…”代替。
			//假设全部是中文，每个中文字14px，面板宽度≈700px，所以最多只显示50个中文字。
			text = text.length() <= 50 ? text : ChannelUtil.cutString(text, font, SIZE.width-14*2);
			return text;
		}
		
		/**
		 * 显示完整的消息内容
		 * @param msg 消息对象
		 */
		public void showComplete(ChannelMessage msg) {
			if(msg != null) {				
				super.setText(msg.getCompleteMsg());
				setCaretPosition(0);
			}
		}

		/**
		 * 显示消息内容(区别于showComplete()，这里只是显示一行信息，超出的部分用省略号代替)
		 * @param msg
		 */
		public void showMessagePart(ChannelMessage msg)
		{
			if(msg != null) {
				setText(msg.getShortMsg());
				setCaretPosition(0);
			}
		}		
	}
	
	//底部面板，就是一个回复框面板
	class Bottom extends SGPanel implements ActionListener, EventConstants
	{
		Dimension SIZE = new Dimension(ChannelConfig.getIntValue("msgBodyWidth")-60, 30);
		
		private SGButton btnReply = new SGButton("发送"), btnCancel = new SGButton("取消");
		private OTextField replyTextField = new OTextField();
		
		public Bottom()  {setVisible(false);}//默认不可见
		
		public ChannelEvent getChannelEvent() {
			return (Body)this.getParent();
		}
		
		public void showMessageReplyArea()
		{
			setLayout(null);
			setPreferredSize(SIZE);
			
			replyTextField.setBounds(40, 2, SIZE.width-60*2, 32);
			add(replyTextField);
			
			btnReply.setBounds(40+(SIZE.width-60*2), 0, 60, 28);
			add(btnReply);
			
			btnCancel.setBounds(40+(SIZE.width-60), 0, 60, 28);
			add(btnCancel);
			
			btnReply.addActionListener(this);
			btnCancel.addActionListener(this);
			
			setVisible(true);
		}
		
		public void hideReplyContent() {
			replyTextField.setText(null);
			setVisible(false);
		}
		
		public void setReplyContent(String text) {
			replyTextField.setText(text);
		}
		
		public String getReplyContent() {
			return replyTextField.getText();
		}
		
		//回复和取消事件
		public void actionPerformed(ActionEvent e)
		{
			ChannelEvent ce = this.getChannelEvent();
			
			if(e.getSource() == btnCancel) { //取消
				ce.doQuickReply(e, false);
			}
			else if(e.getSource() == btnReply) {//回复
				String replyContent = getReplyContent().trim();
				if(!ChannelUtil.isEmpty(replyContent, true)) //检查回复内容是否为空
				{
					//首先开闭回复
					ce.doQuickReply(e, false);
					
					//再新建消息
					ChannelMessage oldMsg = ce.getBodyMessage();
					ChannelMessage newMsg = oldMsg.clone();
					
					newMsg.setId(null); //注意，这里是关键
					newMsg.setType(ChannelMessage.TYPE_SEND);
					newMsg.setSentDate(new Date());
					newMsg.setReceivedDate(newMsg.getSentDate());//设置和发送时间一样
					newMsg.setBody(replyContent);
					newMsg.setToAddr(oldMsg.getFromAddr()); //交换一下发送和接受人的顺序
					newMsg.setFromAddr(oldMsg.getToAddr());
					newMsg.setProcessed(true);//设置已处理状态
					
					//邮件单独处理：
					if(newMsg.isMail()) {
						newMsg.setSubject("Re：" + oldMsg.getSubject());
						newMsg.setBody(replyContent + Utilies.getSeparatorFlags()
								+ Utilies.parseMail(oldMsg, false));
					}
					
					try {
						ChannelService cs = 	ChannelAccesser.getChannelService();
						cs.put(Constants.ACTION_QUICK_REPLY, newMsg);

						//最后回复消息
						ChannelDesktopPane desktop = ChannelNativeCache.getDesktop();
						desktop.addMessage(newMsg);
					}
					catch (Exception ex) {
						ChannelUtil.showMessageDialog(this, "错误", ex);
					}
				}
			}
		}
	}
}