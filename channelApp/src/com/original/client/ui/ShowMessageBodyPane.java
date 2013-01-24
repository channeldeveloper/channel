package com.original.client.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.HTMLEditorKit;

import com.original.channel.ChannelNativeCache;
import com.original.client.EventConstants;
import com.original.client.layout.ChannelGridBagLayoutManager;
import com.original.client.layout.ChannelGridLayout;
import com.original.client.ui.data.AbstractButtonItem;
import com.original.client.util.ChannelConfig;
import com.original.client.util.ChannelHyperlinkListener;
import com.original.client.util.ChannelUtil;
import com.original.service.channel.ChannelMessage;
import com.original.service.channel.Constants;
import com.original.service.channel.Utilies;
import com.original.service.channel.protocols.im.iqq.QQParser;
import com.original.service.channel.protocols.sns.weibo.WeiboParser;
import com.seaglasslookandfeel.widget.SGButton;
import com.seaglasslookandfeel.widget.SGLabel;
import com.seaglasslookandfeel.widget.SGPanel;
import com.seaglasslookandfeel.widget.SGScrollPane;
import com.seaglasslookandfeel.widget.SGTextPane;

public class ShowMessageBodyPane extends ChannelMessageBodyPane implements ActionListener, EventConstants
{
	private ChannelGridBagLayoutManager layoutMgr = null;
	
	private SGLabel title = new SGLabel();
	private SGPanel ctrlGroup = new SGPanel(new ChannelGridLayout(2, 0, new Insets(0, 0, 0, 0)));
	private SGTextPane content = new SGTextPane();//文本面板
	
	private ChannelMessage newMsg = null;
	private ChannelMessageBodyPane.Body origin = null;
	
	public ShowMessageBodyPane() 
	{
		layoutMgr = new ChannelGridBagLayoutManager(this);//使用网格包布局
		layoutMgr.setAnchor(GridBagConstraints.WEST);
		layoutMgr.setInsets(new Insets(5, 0, 0, 0));
		
		constructMessageBody();
	}
	
	/**
	 * 构建消息主体，共分为3块：标题(只用于邮件)、控件组(操作按钮组)、消息内容(带滚动条的文本面板)
	 */
	private void constructMessageBody() 
	{
		this.setBorder(new EmptyBorder(new Insets(0, 45, 0, 5))); //设置页边距
		this.setPreferredSize(new Dimension(ChannelConfig.getIntValue("msgBodyWidth"), 
				ChannelConfig.getIntValue("desktopHeight") - 130)); 
		
		//设置一些控件的属性
		content.setEditorKit(new HTMLEditorKit());
		content.addHyperlinkListener(new ChannelHyperlinkListener());
		content.setOpaque(false);
		content.setEditable(false);
		content.setBackground(new Color(0, 0, 0, 0)); //设置文本面板透明的唯一方法
		SGScrollPane scrollPane = ChannelUtil.createScrollPane(content, Color.gray);
        scrollPane.setBorder(new EmptyBorder(0, 0, 10, 5));
        
        //开始添加控件
        layoutMgr.addComToModel(title, 1, 1, GridBagConstraints.HORIZONTAL);
        layoutMgr.newLine();
        layoutMgr.addComToModel(ctrlGroup, 1, 1, GridBagConstraints.HORIZONTAL);
        layoutMgr.newLine();
        layoutMgr.addComToModel(scrollPane, 1, 1, GridBagConstraints.BOTH);
        
        setDefaultControls();
	}
	
	/**
	 * 将消息的内容添加至面板中，会自动分析消息类型同时显示相应内容
	 * @param msg
	 */
	public void setMessageToGUI(ChannelMessage msg)
	{
		if(msg != null) {
			if(msg.isMail()) {//是邮件
				title.setText(msg.getSubject());
				//对于邮件类型，再添加一些其他按钮
				addControlItems(false, 
						new AbstractButtonItem("转发", REPOST, null));
//				addControlItems(true, 
//						new AbstractButtonItem("设置为垃圾邮件", PUT_INTO_TRASH, null));
				
				parseMail(msg);
			}
			else if(msg.isQQ()) {//是QQ
				title.setVisible(false);
				
				parseQQ(msg);
			}
			else if(msg.isWeibo()) {//是微博
				title.setVisible(false);
				
				parseWeibo(msg);
			}
			
			if (msg.hasProcessed()) {// 已经回复，不再显示回复按钮
				setControlItemVisible(REPLY, false);
			}
			
			this.newMsg = msg;
		}
	}
	
	@Override
	public ChannelMessage getChannelMessage() {
		// TODO 自动生成的方法存根
		return this.newMsg;
	}
	
	public void setOriginMessageBody(ChannelMessageBodyPane.Body body) {
		this.origin = body;
	}

	/**
	 * 设置默认的控制按钮组(从左到右：回复-保存-删除)
	 */
	private void setDefaultControls( )
	{
		addControlItems(true, new AbstractButtonItem[]{
			new AbstractButtonItem("回复", REPLY, null),
			new AbstractButtonItem("保存", SAVE, null),
			new AbstractButtonItem("删除", DELETE, null),
		});
	}
	
	/**
	 * 设置控制按钮显示或隐藏
	 * @param actionCommand 按钮名称
	 * @param isVisible 是否可见。如果为true，则可见；否则隐藏。
	 */
	public void setControlItemVisible(String actionCommand, boolean isVisible)
	{
		for(int i=0; i<ctrlGroup.getComponentCount(); i++)
		{
			Component comp = ctrlGroup.getComponent(i);
			if(comp instanceof AbstractButton
					&& ((AbstractButton) comp).getActionCommand() == actionCommand)
			{
				if(comp.isVisible() != isVisible)
					comp.setVisible(isVisible);
				return;
			}
		}
	}
	
	/**
	 * 添加控制按钮
	 * @param isLeftToRight 如果为true，则按钮添加至最后，否则按钮添加至最前
	 * @param items 按钮(可多个)
	 */
	public void addControlItems(boolean isLeftToRight, AbstractButtonItem... items)
	{
		if(items != null && items.length > 0) {
			ComponentOrientation co = this.getComponentOrientation();
			if(co.isLeftToRight() != isLeftToRight) {
				this.setComponentOrientation(isLeftToRight ? ComponentOrientation.LEFT_TO_RIGHT :
					ComponentOrientation.RIGHT_TO_LEFT);
			}
			
			for(AbstractButtonItem item : items)
			{
				SGButton button = ChannelUtil.createAbstractButton(item);
				button.addActionListener(this);
				ctrlGroup.add(button);
			}
		}
	}

	//按钮控件的点击事件
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		ChannelDesktopPane desktop = ChannelNativeCache.getDesktop();
		if (DELETE == e.getActionCommand()) {// 删除
			if (origin != null) {
				origin.doDelete();
			}
			desktop.removeShowComp(PREFIX_SHOW + newMsg.getContactAddr());
		} else if (REPLY == e.getActionCommand()) {// 回复
			ChannelMessage msg = editMessage(REPLY);
			msg.setAction(Constants.ACTION_REPLY);

			ChannelMessagePane cmp = new ChannelMessagePane(new NewMessageTopBar(false));
			cmp.newMessage(msg);
			desktop.addOtherShowComp(PREFIX_SHOWANDNEW + msg.getContactName(), cmp);

		} else if (REPOST == e.getActionCommand()) { // 转发
			ChannelMessage msg = editMessage(REPOST);
			msg.setAction(Constants.ACTION_REPOST);

			ChannelMessagePane cmp = new ChannelMessagePane(new NewMessageTopBar(true));
			cmp.newMessage(msg);
			desktop.addOtherShowComp(PREFIX_SHOWANDNEW + msg.getContactName(), cmp);
		}
		
	}
	
	//编辑回复或转发的消息
	private ChannelMessage editMessage(String action) {
		ChannelMessage msg = null;
		if(this.newMsg != null) {
			msg = this.newMsg.clone();
			
			if(action == REPOST) {
				msg.setFromAddr(null); //转发时，无法确定收件人，由用户自己输入。
			}
			
			if (msg.isMail()) {// 邮件
				if (action == REPLY) {
					msg.setSubject("Re：" + msg.getSubject());
				} else if (action == REPOST) {
					msg.setSubject("转发：" + msg.getSubject());
				}

				msg.setBody(Utilies.getMailReplyArea()
						+ Utilies.getSeparatorFlags()
						+ Utilies.getBody(content.getText()));
				
			} else if (msg.isWeibo() || msg.isQQ()) { // 微博 或 QQ
				msg.setBody(Utilies.getMailReplyArea()
						+ Utilies.getSeparatorFlags()
						+ Utilies.getBody(content.getText()));
			}
		}
		return msg;
	}
	
	/* --------------------- 下面都是由ChannelMessage来处理消息内容 ---------------------*/
	private void parseMail(ChannelMessage msg)
	{
		if(msg != null && msg.getMessageID() != null) {
			content.setText(Utilies.parseMail(msg));
			content.setCaretPosition(0); //使滚动条保持在顶部
		}
	}
	
	private void parseQQ(ChannelMessage msg)
	{
		if(msg != null && msg.getMessageID() != null) {
			content.setText(QQParser.parseMessage(msg, true));
			content.setCaretPosition(0); //使滚动条保持在顶部
		}
	}
	
	private void parseWeibo(ChannelMessage msg)
	{
		if(msg != null && msg.getMessageID() != null) {
			content.setText(WeiboParser.parse(msg));
			content.setCaretPosition(0); //使滚动条保持在顶部
		}
	}
	
}