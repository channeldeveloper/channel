package com.original.client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.HTMLEditorKit;

import com.original.client.ChannelEvent;
import com.original.client.ui.widget.EditorHandler;
import com.original.client.util.ChannelConfig;
import com.original.client.util.ChannelConstants;
import com.original.client.util.ChannelUtil;
import com.original.service.channel.ChannelMessage;
import com.seaglasslookandfeel.widget.SGScrollPane;

/**
 * 列出当前联系人所有的消息(即已列表的形式显示)。目前设定如下：
 * 所有的消息全部显示在一个JTextPane中，不再是一个消息，一个面板(JTextPane)。
 * 同时，考虑到消息很多时的效率问题，我们使用滚动条动态加载10条显示的方法，类型于分页，
 * 但是JTextPane面板中最多也只显示10条(即1-10 -> 11-20 -> ... 类似于这样的显示方法)。
 * @author WMS
 *
 */
public class ListMessageBodyPane extends ChannelMessageBodyPane
{
	Body body = new Body();
	EditorHandler handler = new EditorHandler(body);
	Dimension size = new Dimension(ChannelConfig.getIntValue("msgBodyWidth"), 400);
	
	public ListMessageBodyPane() {
		this.setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		this.addBody();
	}
	
	//添加Body消息显示面板
	private void addBody() {
		SGScrollPane scrollPane = ChannelUtil.createScrollPane(body);
		scrollPane.setPreferredSize(size);
		this.add(scrollPane, BorderLayout.CENTER);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addMessage(ChannelMessage msg, boolean toFirst) {
		try {
			channelLock.lock();
			
			int paragraphIndex = messageBodyList.size(); //段落索引号，每插入一个消息，就视为一段
			if(paragraphIndex > 0) {
				handler.insertHorizontalLine(size.width,1); //分割线
			}			
			
			//头部
			LTop top = new LTop(); 
			top.createMessageHeader(msg);
			handler.insertCompParagraph(-1, paragraphIndex, top);
			
			//中间
			handler.insertText(paragraphIndex, 
					Center.cutText(body.getFont(), msg.getShortMsg()), body.getStyleCss());
			
			messageBodyList.add(msg);
			
			//底部
			LBottom bottom = new LBottom();
			//设置关联：
			top.paragraphIndex = paragraphIndex;
			top.bottom = bottom;
			
			bottom.paragraphIndex = paragraphIndex;
			bottom.top = top;
		}
		finally {
			channelLock.unlock();
		}
	
	}

	class Body extends JTextPane implements ChannelEvent, ChannelConstants
	{
		public Body() {
			this.setEditorKit(new HTMLEditorKit());//设置html编辑器
			this.setEditable(false);
			this.setFont(DEFAULT_FONT);
		}
		
		public String getStyleCss() {
			return String.format("font-family:%s; " +
					"font-size:%s; " +
					"margin-left:%s; ",
					DEFAULT_FONT_FAMILY, "11px", "35px");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void doQuickReply(ActionEvent ae, boolean isON) {
			// TODO 自动生成的方法存根
			JButton source = (JButton)ae.getSource();
			LTop top = null;
		LBottom bottom = null;
		
		if(source.getParent() instanceof LTop)
		{
			top = (LTop)source.getParent();
			bottom = top.bottom;
		}
		else if(source.getParent() instanceof LBottom)
		{
			bottom = (LBottom)source.getParent();
			top = bottom.top;
		}
		
		int paragraphIndex = top.paragraphIndex;
		ChannelMessage msg = messageBodyList.elementAt(paragraphIndex);
		if(isON) {
			top.setVisible(QUICK_REPLY, false);
			top.setVisible(SHOW_COMPLETE, true);
			top.notifyStatusChange(msg,  STATUS_READ, true); //通知已读
			
			//更新显示内容：
			handler.updateText(paragraphIndex, msg.getCompleteMsg(), body.getStyleCss());
			
			//显示回复框：
			int find = handler.findParentParagraph("text_"+paragraphIndex);
			bottom.showMessageReplyArea();
			handler.insertCompParagraph(find, paragraphIndex, bottom);
		}
		else {
			top.setVisible(QUICK_REPLY, true);
			top.setVisible(SHOW_COMPLETE, false);
		}
			
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void doSave(ActionEvent ae) {
			// TODO 自动生成的方法存根
			
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void doEdit(ActionEvent ae) {
			// TODO 自动生成的方法存根
			
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void doDelete(ActionEvent ae) {
			// TODO 自动生成的方法存根
			
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void doShowComplete(ActionEvent ae) {
			// TODO 自动生成的方法存根
			
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ChannelMessage getBodyMessage() {
			// TODO 自动生成的方法存根
			return null;
		}
		
	}
	
	class LTop extends Top
	{
		LBottom bottom = null;
		int paragraphIndex = -1;
		public String getName() {
			// TODO 自动生成的方法存根
			return "top";
		}

		@Override
		public ChannelEvent getChannelEvent() {
			// TODO 自动生成的方法存根
			return body;
		}
		
	}
	
	class LBottom extends Bottom
	{
		LTop top = null;
		int paragraphIndex = -1;
		public String getName() {
			// TODO 自动生成的方法存根
			return "bottom";
		}
		
		@Override
		public ChannelEvent getChannelEvent() {
			// TODO 自动生成的方法存根
			return body;
		}
	}
}
