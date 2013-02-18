package com.original.client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FontMetrics;

import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.HTMLEditorKit;

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
	Dimension size = new Dimension(ChannelConfig.getIntValue("msgBodyWidth"), 350);
	
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
		Top top = new Top();
		top.createMessageHeader(msg);
		
		try {
			handler.insertCompParagraph(0, top);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		handler.insertText(Center.cutText(body.getFontMetrics(), msg.getShortMsg()));
		
		Bottom bottom = new Bottom();
		bottom.showMessageReplyArea();
		try {
			handler.insertCompParagraph(0, bottom);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		handler.insertHorizontalLine(size.width,1);
	}

	class Body extends JTextPane implements ChannelConstants
	{
		public Body() {
			this.setEditorKit(new HTMLEditorKit());//设置html编辑器
			this.setEditable(false);
			this.setFont(DEFAULT_FONT);
		}
		
		public FontMetrics getFontMetrics() {
			return this.getFontMetrics(DEFAULT_FONT);
		}
	}

}
