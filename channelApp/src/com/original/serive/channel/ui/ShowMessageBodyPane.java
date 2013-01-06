package com.original.serive.channel.ui;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.HTMLEditorKit;

import com.original.serive.channel.EventConstants;
import com.original.serive.channel.layout.ChannelGridBagLayoutManager;
import com.original.serive.channel.layout.ChannelGridLayout;
import com.original.serive.channel.ui.data.AbstractButtonItem;
import com.original.serive.channel.util.ChannelConfig;
import com.original.serive.channel.util.ChannelHyperlinkListener;
import com.original.serive.channel.util.ChannelUtil;
import com.original.service.channel.ChannelMessage;
import com.original.service.channel.Utilies;
import com.original.service.channel.protocols.im.iqq.QQParser;
import com.original.service.channel.protocols.sns.weibo.WeiboParser;
import com.original.widget.OScrollBar;

public class ShowMessageBodyPane extends ChannelMessageBodyPane implements ActionListener, EventConstants
{
	private ChannelGridBagLayoutManager layoutMgr = null;
	
	private JLabel title = new JLabel();
	private JPanel ctrlGroup = new JPanel(new ChannelGridLayout(2, 0, new Insets(0, 0, 0, 0)));
	
	private JTextPane content = new JTextPane();//文本面板
	private JScrollBar scrollBar = new OScrollBar(JScrollBar.VERTICAL, Color.gray); //滚动条，用于文本面板中
	
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
		this.setPreferredSize(new Dimension(ChannelConfig.getIntValue("msgBodyWidth"), 500)); //这里的大小以后再做调整
		
		//设置一些控件的属性
		content.setEditorKit(new HTMLEditorKit());
		content.addHyperlinkListener(new ChannelHyperlinkListener());
		content.setOpaque(false);
		content.setEditable(false);
		content.setBackground(new Color(0, 0, 0, 0)); //设置文本面板透明的唯一方法
		JScrollPane scrollPane = new JScrollPane(content,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBar(scrollBar);
        scrollPane.setBorder(new EmptyBorder(0, 0, 10, 5));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setViewportBorder(null);
        
        this.setDefaultControls();
        
        //开始添加控件
        layoutMgr.addComToModel(title, 1, 1, GridBagConstraints.HORIZONTAL);
        layoutMgr.newLine();
        
        layoutMgr.addComToModel(ctrlGroup, 1, 1, GridBagConstraints.HORIZONTAL);
        layoutMgr.newLine();
        
        layoutMgr.addComToModel(scrollPane, 1, 1, GridBagConstraints.BOTH);
	}
	
	/**
	 * 将消息的内容添加至面板中，会自动分析消息类型同时显示相应内容
	 * @param msg
	 */
	public void setMessageToGUI(ChannelMessage msg)
	{
		if(msg != null) {
			if(ChannelMessage.MAIL.equals(msg.getClazz())) {//是邮件
				title.setText(msg.getSubject());
				//对于邮件类型，再添加一些其他按钮
				addControlItems(false, 
						new AbstractButtonItem("转发", POST, null));
				addControlItems(true, 
						new AbstractButtonItem("设置为垃圾邮件", PUT_INTO_TRASH, null));
				
				parseMail(msg);
			}
			else if(ChannelMessage.QQ.equals(msg.getClazz())) {//是QQ
				title.setVisible(false);
				
				parseQQ(msg);
			}
			else if(ChannelMessage.WEIBO.equals(msg.getClazz())) {//是微博
				title.setVisible(false);
				
				parseWeibo(msg);
			}
		}
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
				JButton button = ChannelUtil.createAbstractButton(item);
				button.addActionListener(this);
				ctrlGroup.add(button);
			}
		}
	}

	//按钮控件的点击事件
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
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
			content.setText(QQParser.parseMessage(msg));
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
