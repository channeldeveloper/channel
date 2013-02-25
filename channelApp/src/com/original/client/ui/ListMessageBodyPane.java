package com.original.client.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.List;

import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.HTMLEditorKit;

import com.original.channel.ChannelNativeCache;
import com.original.client.ChannelEvent;
import com.original.client.ui.widget.EditorHandler;
import com.original.client.ui.widget.ScrollBar;
import com.original.client.util.ChannelConfig;
import com.original.client.util.ChannelConstants;
import com.original.client.util.ChannelUtil;
import com.original.client.util.LoadingPainter;
import com.original.service.channel.ChannelMessage;
import com.original.service.channel.Constants;
import com.seaglasslookandfeel.widget.SGScrollPane;

/**
 * 列出当前联系人所有的消息(即已列表的形式显示)。目前设定如下：
 * 所有的消息全部显示在一个JTextPane中，不再是一个消息，一个面板(JTextPane)。
 * 同时，考虑到消息很多时的效率问题，我们使用滚动条动态加载10条显示的方法，类型于分页，
 * 但是JTextPane面板中最多也只显示10条(即1-10 -> 11-20 -> ... 类似于这样的显示方法)。
 * @author WMS
 *
 */
public class ListMessageBodyPane extends ChannelMessageBodyPane implements AdjustmentListener
{
	Body body = new Body();
	EditorHandler handler = new EditorHandler(body);
	LoadingPainter painter = new LoadingPainter(); //加载动画
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
		scrollPane.getVerticalScrollBar().addAdjustmentListener(this);//滚动条事件
		this.add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addMessage(final ChannelMessage msg, boolean toFirst) {
		try {
			channelLock.lock();

			if(toFirst) {
			Runnable joinedTask = 	new Runnable() {
					public void run() {
						body.relativeIndex ++;
						messageBodyList.add(0, msg);
						
			//先删除底部：
						if(body.count >= 10)
							removeMessage(body.endParagraphIndex, 1);
			//再顶部插入：
						insertMessage(true, --body.startParagraphIndex, msg);
					}
				};
				
				painter.repaint(this, joinedTask);
			}
			else {

				int paragraphIndex = ++body.endParagraphIndex; //段落索引号，每插入一个消息，就视为一段。
				insertMessage(false, paragraphIndex, msg);

				//始终保持滚动条位于顶部显示
				body.setCaretPosition(0);
			}
		}
		finally {
			channelLock.unlock();
		}
	}
	
	private void insertMessage(boolean reverseOrder, int startParagraphIndex, ChannelMessage msg)
	{
		try {
			channelLock.lock();
			
			if(reverseOrder) { //反序插入
				//分割线
				handler.insertHorizontalLine(1, startParagraphIndex, size.width, 1);
				
				// 底部：初始不显示
				
				// 中间
				String text = Center.cutText(body.getFont(), msg.getShortMsg());
				handler.insertText(1, startParagraphIndex,
						text,
						body.getBackgroundStyleCss(!msg.isReceived()),
						body.getTextStyleCss());
				
				//下面的方法只是修正反序插入时，文本和水平分割线之间存在间接的Bug，待以后有好的方法，再做优化！
				handler.updateText(startParagraphIndex,
						text, 	
						body.getBackgroundStyleCss(!msg.isReceived()),
						body.getTextStyleCss());
				
				// 头部
				ParagraphTop top = new ParagraphTop();
				top.createMessageHeader(msg);
				top.paragraphIndex = startParagraphIndex;
				handler.insertCompParagraph(1, startParagraphIndex, top,
						body.getBackgroundStyleCss(!msg.isReceived()));
			}
			
			else {
				// 头部
				ParagraphTop top = new ParagraphTop();
				top.createMessageHeader(msg);
				top.paragraphIndex = startParagraphIndex;
				handler.insertCompParagraph(-1, startParagraphIndex, top,
						body.getBackgroundStyleCss(!msg.isReceived()));		
				
				// 中间
				handler.insertText(-1, startParagraphIndex,
						Center.cutText(body.getFont(), msg.getShortMsg()),
						body.getBackgroundStyleCss(!msg.isReceived()),
						body.getTextStyleCss());
				
				// 底部：初始不显示
				
				//分割线
				handler.insertHorizontalLine(-1, startParagraphIndex, size.width, 1);
			}
			
			body.count ++;
			
		} finally {
			channelLock.unlock();
		}
		
	}
	
	/**
	 * 从指定位置删除消息
	 * @param startParagraphIndex 指定位置 >=0 
	 * @param count 如果>0,表示向下删count；否则表示向上删count; =0 则立即返回
	 */
	private void removeMessage(int startParagraphIndex, int count) {
		if(count == 0) return;
		
		try {
			channelLock.lock();
			
			if(count < 0) //向上删count
			{
				for(int i=0; i<-count; i++) {
					int paragraphIndex = startParagraphIndex - i;
					
					handler.removeHorizontalLine(paragraphIndex);//if exists, then will be deleted!
					handler.clearParagraph("bottom" , paragraphIndex); // delete by force!
					handler.removeText(paragraphIndex);
					handler.clearParagraph("top" , paragraphIndex);
				}
				
				body.count -= -count;
			}
			else {//向下删count，注意向上删的顺序相反
				for(int i=0; i<count; i++) {
					int paragraphIndex = startParagraphIndex + i;
					
					handler.clearParagraph("top" , paragraphIndex);
					handler.removeText(paragraphIndex);
					handler.clearParagraph("bottom" , paragraphIndex); // delete by force!
					handler.removeHorizontalLine(paragraphIndex);//if exists, then will be deleted!
				}
				
				body.count -= count;
			}
			
		}
		finally {
			channelLock.unlock();
		}
		
	}
	
	
//滚动条事件：这里分上翻页和下翻页两种控制
	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		ScrollBar osb =(ScrollBar) e.getSource();
		if(osb.getScrollBarValue() == osb.getValue())
			return;
		
		if (osb.getValue() != 0 && osb.getValue() > osb.getScrollBarValue()
				&& osb.getValue() == (osb.getMaximum() - osb.getVisibleAmount())) {
			//底部 -> 下翻页
			doLoadingNextPage(3);//设置1~4之间
		}
		
		if(osb.getValue() == 0 && osb.getScrollBarValue() > 0) {
			//顶部 -> 上翻页
			doLoadingPrevPage(3);//设置1~4之间
		}
		
		osb.setScrollBarValue(osb.getValue());
		
	}
	
	//上翻页
	private void doLoadingPrevPage(final int count) {
		if (count == 0) return;
		if (count < 0) {
			doLoadingNextPage(-count);
			return;
		}
		
		Runnable joinedTask = new Runnable() {
			@Override
			public void run() {
				int total = messageBodyList.size(); //总数
				if (total == 0
						|| body.startParagraphIndex + body.relativeIndex <= 0) // 没有数据或者没有加载
					return;
				  
				int prevCount = Math.min(count, body.startParagraphIndex+ body.relativeIndex ); 
				body.endParagraphIndex -= prevCount;
				body.startParagraphIndex -= prevCount;
				
				//获取
				final List<ChannelMessage> subMsgs = 
				messageBodyList.subList(body.startParagraphIndex+body.relativeIndex, body.startParagraphIndex+prevCount+body.relativeIndex);
				
				removeMessage(body.endParagraphIndex+1, prevCount);
				for(int i=prevCount; i>0; i--) { //注意反序
					insertMessage(true, body.startParagraphIndex+i-1, subMsgs.get(i-1));
				}
				
				try {
					Thread.sleep(1000);  //sleep 1s
				} 
				catch (InterruptedException ex) { }
			}
		};
		painter.repaint(this, joinedTask);
	}
	
	//下翻页
	private void doLoadingNextPage(final int count) {
		if (count == 0) return;
		if (count < 0) {
			doLoadingPrevPage(-count);
			return;
		}
		
		Runnable joinedTask = new Runnable() {
			@Override
			public void run() {
				int total = messageBodyList.size(); //总数
				int available = 0; //剩余数
				if (total == 0
						|| (available = total - (body.endParagraphIndex + body.relativeIndex + 1)) == 0) // 没有数据或者已经读完
					return;
				
				int nextCount = Math.min(count, available); 
				body.endParagraphIndex += nextCount;
				body.startParagraphIndex += nextCount;
				
				//获取
				final List<ChannelMessage> subMsgs = 
				messageBodyList.subList(body.endParagraphIndex-nextCount+body.relativeIndex + 1, 
						body.endParagraphIndex+body.relativeIndex+1);
				
				removeMessage(body.startParagraphIndex-1, -nextCount);
				for(int i=1; i<=nextCount; i++) {
					insertMessage(false, body.endParagraphIndex-nextCount+ i, subMsgs.get(i-1));
				}
				
				try {
					Thread.sleep(1000);  //sleep 1s
				} 
				catch (InterruptedException ex) { }
				
			}
		};
		painter.repaint(this, joinedTask);
	}

	@Override
	protected void paintChildren(Graphics g) {
		super.paintChildren(g);
		
		painter.paint(g, this.getWidth(), this.getHeight());
		
	}

	class Body extends JTextPane implements ChannelEvent, ChannelConstants
	{
		ParagraphTop top;
		ParagraphBottom bottom;
		
		int startParagraphIndex = 0,
				endParagraphIndex = -1;
		
		int relativeIndex = 0;//相对于messageBodyList原元素的索引。
		int count = 0;//消息数
		
		public Body() {
			this.setEditorKit(new HTMLEditorKit());//设置html编辑器
			this.setEditable(false);
			this.setFont(DEFAULT_FONT);
		}
		
		//统一的文本样式CSS
		public String getTextStyleCss() {
			return String.format("font-family:%s; " +
					"font-size:%s; " +
					"margin-left:%s; ", 
					DEFAULT_FONT_FAMILY, "11px", "35px");
		}
		
		public String getBackgroundStyleCss(boolean colored) {//colored是否需要着色
			return String.format("background-color:%s;", 
					colored ? "#bad4e5" : "#ffffff");
		}
		
		public void notifyEventOwner(ActionEvent ae) {
			Component cp = (Component) ae.getSource();
			Component parent = cp.getParent();
			if (parent instanceof ParagraphTop) {
				top = (ParagraphTop) parent;
				bottom = top.bottom;
			} else if (parent instanceof ParagraphBottom) {
				bottom = (ParagraphBottom) parent;
				top = bottom.top;
			}
		}
		
		//显示底部回复框
		public void setParagraphBottomVisible(boolean isVisible) {
			if (isVisible) {

				if (bottom == null) {
					bottom = new ParagraphBottom();
				}
				if (bottom.top == null && top != null) {
					bottom.top = top;
					bottom.paragraphIndex = top.paragraphIndex;
				}
				bottom.showMessageReplyArea();
			} else {
				if (bottom != null) {
					bottom.top = null;
					bottom.paragraphIndex = -1;
					bottom = null;
				}
			}
		}		

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void doQuickReply(boolean isON) {

			int paragraphIndex = getBodyMessageIndex();
			ChannelMessage msg =getBodyMessage();

			if (isON) {
				top.setVisible(QUICK_REPLY, false);
				top.setVisible(SHOW_COMPLETE, true);
				top.notifyStatusChange(msg, STATUS_READ, true); // 通知已读

				// 更新显示内容：
				handler.updateText(paragraphIndex, msg.getCompleteMsg(),
						body.getBackgroundStyleCss(!msg.isReceived()),
						body.getTextStyleCss());

				// 显示回复框：
				setParagraphBottomVisible(true);
				int find = handler.findParentParagraph("text" , paragraphIndex);
				handler.insertCompParagraph(find, paragraphIndex, bottom, 
						body.getBackgroundStyleCss(!msg.isReceived()) );
			} else {
				top.setVisible(QUICK_REPLY, true);
				top.setVisible(SHOW_COMPLETE, false);
				
				// 更新显示内容：
				handler.updateText(paragraphIndex, 
						Center.cutText(body.getFont(), msg.getShortMsg()), 
						body.getBackgroundStyleCss(!msg.isReceived()),
						body.getTextStyleCss());
				
				//隐藏底部：
				setParagraphBottomVisible(false);
				if (bottom != null) {
					handler.removeCompParagraph(paragraphIndex, bottom);
				} else {
					handler.clearParagraph("bottom" , paragraphIndex);
				}
			}
			
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void doSave() {
			
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void doEdit() {
			ChannelMessage msg =getBodyMessage();
			ChannelMessage newMsg = msg.clone();

			ChannelMessagePane cmp = null;
			if (newMsg.isRepost()) {// 是转发
				newMsg.setAction(Constants.ACTION_REPOST);
				cmp = new ChannelMessagePane(new NewMessageTopBar(true));
			} else {// 默认回复
				newMsg.setAction(Constants.ACTION_REPLY);
				cmp = new ChannelMessagePane(new NewMessageTopBar(false));
			}
			cmp.newMessage(newMsg);

			ChannelDesktopPane desktop = ChannelNativeCache.getDesktop();
			desktop.addOtherShowComp(PREFIX_SHOWANDNEW + newMsg.getContactName(), cmp);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void doDelete() {
			if (ChannelUtil.confirm(null, "确认删除", "是否删除该消息？")) {
				try {
					int paragraphIndex = getBodyMessageIndex();
					ChannelMessage msg = getBodyMessage();
					
					channelService.trashMessage(msg); //先从数据库里面删除
					
					//再从界面删除：
					handler.removeCompParagraph(paragraphIndex, top);
					handler.removeText(paragraphIndex);
					if (bottom != null) {
						handler.removeCompParagraph(paragraphIndex, bottom);
					} else {
						handler.clearParagraph("bottom" , paragraphIndex); // delete by force!
					}
					handler.removeHorizontalLine(paragraphIndex);//if exists, then will be deleted!
					
					//同时改变原有消息数：
					notifyToChangeMessage(msg, false);
				} catch (Exception ex) {
					ChannelUtil.showMessageDialog(null, "错误", ex);
				}
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void doShowComplete() {
			ChannelMessage msg =getBodyMessage();
			ChannelMessage newMsg = msg.clone();
			
			ChannelMessagePane nw =  new ChannelMessagePane(new ShowMessageTopBar(newMsg));
			nw.showMessage(newMsg);
			((ShowMessageBodyPane)nw.body).setMessageToGUI(newMsg);
			((ShowMessageBodyPane)nw.body).setOriginChannelEvent(this);

			ChannelDesktopPane desktop = ChannelNativeCache.getDesktop();
			desktop.addOtherShowComp(PREFIX_SHOW+newMsg.getContactName(), nw);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ChannelMessage getBodyMessage() {
			int paragraphIndex = top.paragraphIndex;
			ChannelMessage msg = messageBodyList.elementAt(paragraphIndex);
			return msg;
		}
		
		public int getBodyMessageIndex() {
			int paragraphIndex = top.paragraphIndex;
			return paragraphIndex;
		}
		
	}
	
	class ParagraphTop extends Top
	{
		ParagraphBottom bottom = null;
		int paragraphIndex = -1;
		
		@Override
		public String getName() {
			return "top";
		}

		@Override
		public ChannelEvent getChannelEvent() {
			return body;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				channelLock.lock();

				body.notifyEventOwner(e);
				super.actionPerformed(e);
			} finally {
				channelLock.unlock();
			}
			
		}
		
	}
	
	
	class ParagraphBottom extends Bottom {
		ParagraphTop top = null;
		int paragraphIndex = -1;

		@Override
		public String getName() {
			return "bottom";
		}

		@Override
		public ChannelEvent getChannelEvent() {
			return body;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				channelLock.lock();

				body.notifyEventOwner(e);
				super.actionPerformed(e);
			} finally {
				channelLock.unlock();
			}
		}
	}
}
