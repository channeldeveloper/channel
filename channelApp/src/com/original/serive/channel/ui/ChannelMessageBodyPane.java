package com.original.serive.channel.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.SwingPropertyChangeSupport;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import com.original.serive.channel.ChannelGUI;
import com.original.serive.channel.EventConstants;
import com.original.serive.channel.border.DottedLineBorder;
import com.original.serive.channel.email.MailContentPane;
import com.original.serive.channel.layout.VerticalGridLayout;
import com.original.serive.channel.ui.ChannelMessagePane.MessageContainer;
import com.original.serive.channel.util.ChannelConfig;
import com.original.serive.channel.util.ChannelConstants;
import com.original.serive.channel.util.ChannelHyperlinkListener;
import com.original.serive.channel.util.ChannelUtil;
import com.original.serive.channel.util.IconFactory;
import com.original.service.channel.ChannelMessage;
import com.original.service.channel.protocols.email.services.MailAuthentication;
import com.original.service.channel.protocols.sns.weibo.WeiboParser;

/**
 * 消息Channel主体面板，就是显示消息的面板，默认显示第一条最新的消息(即JList中的第一条记录)。
 * 可以展开和折叠JList查看和隐藏消息。
 * @author WMS
 *
 */
public class ChannelMessageBodyPane extends JPanel
{
	Vector<ChannelMessage> messageBodyList = new Vector<ChannelMessage>();
	
	private MessageContainer container = null; //当前面板的载体，即父面板
	private PropertyChangeSupport changeSupport =
			new SwingPropertyChangeSupport(this);
	
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
	public void fireMessageChange(String changePropertyName, Object oldValue, Object newValue) {
		PropertyChangeListener[] listeners = changeSupport.getPropertyChangeListeners();
		for(int i=0; i<listeners.length; i++)
		{
			listeners[i].propertyChange(
					new PropertyChangeEvent(this, changePropertyName, oldValue, newValue));
		}
	}

	/**
	 * 当前面板List中添加消息，默认添加在第一行。
	 * @param msg 消息对象
	 * @param addFirst 若为true则只添加最新一条(第一条)；若为false则添加全部
	 */
	public void addMessage(ChannelMessage msg, boolean addFirst, boolean showAll) 
	{
		ChannelMessageBodyPane.Body body = new ChannelMessageBodyPane.Body(msg, addFirst, showAll);
		
		if(addFirst) {		
			this.removeAll();
			
			body.setBorder(new EmptyBorder(0, 10, 0, 10));
			this.add(body);
			this.validate();
			
			messageBodyList.add(msg); //二次保存
		}
		else {
			if(this.getComponentCount() > 0) { //下余面板的边框，稍微复杂点
				body.setBorder(BorderFactory.createCompoundBorder(
						new EmptyBorder(0, 10, 0, 10), 
						new DottedLineBorder(DottedLineBorder.BOTTOM, new Color(213, 213, 213), new float[]{3f,4f})));
			}
			else { //第一个面板的边框
				body.setBorder(new EmptyBorder(0, 10, 0, 10));
			}
			this.add(body);
			this.validate();
		}		
	}
	
	/**
	 * 从当前面板List中移除消息。注意如果此消息已经持久化存储(如数据库已保存)，也需要删除。
	 * 
	 * @param msg 消息对象
	 */
	public void removeMessage(ChannelMessage msg)
	{
		for(int i=0; i<this.getComponentCount(); i++)
		{
			Component comp = this.getComponent(i);
			if(comp instanceof Body && ((Body) comp).iMsg.equals(msg))
			{
				remove(i);
				break;
			}
		}
		
		for(Iterator<ChannelMessage> i = messageBodyList.iterator(); i.hasNext();)
		{
			if(i.next().equals(msg))
			{
				i.remove();
				break;
			}
		}
	}
	
	/**
	 * 显示所有的消息，即展开消息面板
	 */
	public void showAllMessage()
	{
		if(!messageBodyList.isEmpty()) {
			ChannelMessage newMsg = messageBodyList.firstElement();

			if(container != null)
			{
				ChannelMessagePane old =  (ChannelMessagePane)container.getParent();
				ChannelMessagePane nw =  new ChannelMessagePane(new ChannelMessageTopBar());
				for(ChannelMessage msg : messageBodyList)
				{
					nw.addMessage(msg, false,false);
				}
				
				for(int i = 0; i< Math.min(old.body.getComponentCount(), nw.body.getComponentCount()); i++)
				{
					Body oldBody = (Body)old.body.getComponent(i);
					Body nwBody = (Body)nw.body.getComponent(i);
					oldBody.copyTo(nwBody);
				}
				
				ChannelDesktopPane desktop = (ChannelDesktopPane)ChannelGUI.channelNativeStore.get("ChannelDesktopPane");
				desktop.addOtherShowComp("SHOW_"+newMsg.getContactName(), nw);
			}
		}
	}
	
	/* ---------------------------------- 下面是面板的构成，使用人员无需关心 -------------------------------------------*/
	//主体部分，即下面3个部分的整合。
	public class Body extends JPanel implements EventConstants
	{
		//上中下3个子控件(面板)
		Top top = new Top();
		Center center = new Center();
		Bottom bottom = new Bottom();
		ChannelMessage iMsg;//消息对象

		Body origin = null; //当使用copyTo(Body newBody)方法时，newBody.origin = this; 其实就是this的一个副本
		boolean addFirst;
		
		public Body() { }
		public Body(ChannelMessage msg, boolean addFirst, boolean showAll)
		{
			this.iMsg = msg;
			this.addFirst = addFirst;
			
			setPreferredSize(new Dimension(ChannelConfig.getIntValue("msgBodyWidth"),  
							top.SIZE.height + center.SIZE.height + (!bottom.isVisible()?0:bottom.SIZE.height)));
			setLayout(new BorderLayout(5,5));
//			setLayout(new VerticalGridLayout(VerticalGridLayout.TOP_TO_BOTTOM,0,5,new Insets(0, 10, 0, 0)));
			
			//添加子控件
			add(top, BorderLayout.NORTH);
			
			add(center, BorderLayout.CENTER);
			
			add(bottom, BorderLayout.SOUTH);
//			add(top);
//			add(center);
//			add(bottom);
			
			analyzeMessage(msg);
			if(showAll) {
				doQuickReply(true);
			}
		}
		
		/**
		 * 分析消息来源类型，以及方向等属性
		 * @param msg 消息对象
		 */
		private void analyzeMessage(ChannelMessage msg)
		{
			//设置背景(由消息来源类型来区分)
			if(ChannelMessage.TYPE_RECEIVED.equals(msg.getType()))
				setBackground(Color.white);
			else
				setBackground(new Color(186, 212, 229));

			if(ChannelMessage.MAIL.equals(msg.getClazz())) {
				fireMessageChange(MAIL_COUNT_CHANGE_PROPERTY, 0, 1);
			}
			else if(ChannelMessage.QQ.equals(msg.getClazz())) {
				fireMessageChange(QQ_COUNT_CHANGE_PROPERTY, 0, 1);
			}
			else if(ChannelMessage.WEIBO.equals(msg.getClazz())) {
				fireMessageChange(WEIBO_COUNT_CHANGE_PROPERTY, 0, 1);
			}
			
			//为子控件赋值
			top.createMessageHeader(msg);
			center.showMessagePart(msg);
		}
		
		//设置背景
		public void setBackground(Color bg)
		{			
			if(!addFirst) //如果是全部添加，则设置自己的背景色\
			{
				super.setBackground(bg);
			}
			else if(container != null) //如果是添加最新一条(第一条)，则设置父面板的背景色
			{
				container.setBackground(bg);
			}
		}
		
		//设置大小，只改变高度(宽度仍保持不变)
		public void autoAdjustHeight()
		{
			Dimension dim = this.getPreferredSize();
			dim.height = top.SIZE.height + 
					center.SIZE.height + 
					(bottom.isVisible() ? bottom.SIZE.height + 5*3: 5*2); //5px为垂直间距
			setPreferredSize(dim);
		}
		
		/**
		 * 打开或关闭快速回复功能
		 * @param isOn 若为true，则是打开；否则为关闭
		 */
		public void doQuickReply(boolean isOn) {
			if(isOn) {
				top.setVisible(QUICK_REPLY, false);
				top.setVisible(SHOW_COMPLETE, true);
				center.showComplete(iMsg);
				bottom.showMessageReplyArea();
				autoAdjustHeight();
				putClientProperty(QUICK_REPLY, Boolean.TRUE);
				
//				if(container != null && origin == null) {//origin == null 表示是主面板
//					ChannelMessagePane cmp = (ChannelMessagePane)container.getParent();
//					ChannelDesktopPane desktop = (ChannelDesktopPane)
//							ChannelGUI.channelNativeStore.get("ChannelDesktopPane");
//					desktop.setHighlightComp(cmp);
//				}
			}
			else {
				top.setVisible(QUICK_REPLY, true);
				top.setVisible(SHOW_COMPLETE, false);
				center.showMessagePart(iMsg);
				bottom.hideReplyContent();
				autoAdjustHeight();
				putClientProperty(QUICK_REPLY, Boolean.FALSE);
				
//				if(origin == null) {//origin == null 表示是主面板
//					ChannelDesktopPane desktop = (ChannelDesktopPane)
//							ChannelGUI.channelNativeStore.get("ChannelDesktopPane");
//					desktop.setHighlightComp(null);
//				}
			}
		}
		
		//保存
		public void doSave() {
			
		}
		
		//删除
		public void doDelete() {
			
		}
		
		//完整信息
		public void doShowAllComplete() {
					ChannelMessagePane nw =  new ChannelMessagePane(new ChannelMessageTopBar());
						ChannelMessage newMsg = iMsg.clone();
						nw.addMessage(newMsg, false,true);
					
					ChannelDesktopPane desktop = (ChannelDesktopPane)ChannelGUI.channelNativeStore.get("ChannelDesktopPane");
					desktop.addOtherShowComp("SHOW_"+newMsg.getContactName(), nw);
		}
		
		// 完整信息 for email
		public void doShowComplete() {
			ChannelMessage newMsg = iMsg.clone();
			if (newMsg.getChanneAccount().getChannel().getType()
					.equals("email")) {
				MailAuthentication mailAuth = new MailAuthentication("cydow",
						newMsg.getChanneAccount().getAccount().getUser(),
						newMsg.getChanneAccount().getAccount().getPassword(),
						false);// (String userId, String user, String password,
								// boolean _delmail) {
				
				UIManager.put("ScrollBar.width",10);
				
				MailContentPane nw = new MailContentPane(mailAuth);
				nw.setDataToGUI(newMsg);
				// ChannelMessagePane nw = new ChannelMessagePane(new
				// ChannelMessageTopBar());
				// ChannelMessage newMsg = iMsg.clone();
				// nw.addMessage(newMsg, false,true);

				ChannelDesktopPane desktop = (ChannelDesktopPane) ChannelGUI.channelNativeStore
						.get("ChannelDesktopPane");
				desktop.addOtherShowComp("SHOW_" + newMsg.getContactName(), nw);
			} else {
				doShowAllComplete();
			}
		}
		
		//将当前面板的一些属性复制到newBody中。
		public void copyTo(Body newBody)
		{
			if(newBody == null || newBody == this)
				return;
			
			if(this.origin != newBody) {
				newBody.origin = this;
			}
			
			//快速回复属性复制
			if(this.getClientProperty(QUICK_REPLY) == Boolean.TRUE) { //表示已经快速回复了，那么newBody也需要快速回复的功能
				if(newBody.getClientProperty(QUICK_REPLY) != Boolean.TRUE) //如果newBody未快速回复
				{					
					newBody.doQuickReply(true);
				}
				newBody.bottom.setReplyContent(this.bottom.getReplyContent());
			}
			else {
				if(newBody.getClientProperty(QUICK_REPLY) == Boolean.TRUE) //如果newBody已快速回复
				{
					newBody.doQuickReply(false);
				}
				newBody.bottom.setReplyContent(null);
			}
			
			//其他
		}
	}
	
	//头部面板
	class Top extends JPanel implements ActionListener, EventConstants
	{
		private SimpleDateFormat messageFormat = new SimpleDateFormat("MM月dd日 HH:mm");//消息时间格式
		private JLabel messageHeader = new JLabel();
		
		//一些控制按钮
		private JButton btnReply = createCtrlButton("快速回复", QUICK_REPLY),
				btnSave = createCtrlButton("保存", SAVE),
				btnDel = createCtrlButton("删除", DELETE),
				btnShowAll = createCtrlButton("完整信息", SHOW_COMPLETE);
		
		Dimension SIZE = new Dimension(ChannelConfig.getIntValue("msgBodyWidth")-20, 45); 
		
		public Top() { setOpaque(false);btnShowAll.setVisible(false);}
		
		/**
		 * 设置消息头部内容，同时显示在标签中
		 * @param msg 消息对象
		 */
		private void setMessageHeader(ChannelMessage msg)
		{
			if(msg != null && msg.getMessageID() != null)
			{
				if (ChannelMessage.QQ.equals((msg.getClazz())))
				{
					messageHeader.setIcon(IconFactory.loadIconByConfig("defaultQQIcon"));
				} else if (ChannelMessage.WEIBO.equals((msg.getClazz())))
				{
					messageHeader.setIcon(IconFactory.loadIconByConfig("defaultWeiboIcon"));
				} else if (ChannelMessage.MAIL.equals((msg.getClazz())))
				{
					messageHeader.setIcon(IconFactory.loadIconByConfig("defaultMailIcon"));
				}
				
				messageHeader.setForeground(new Color(85,127,196));
				messageHeader.setFont(ChannelConstants.DEFAULT_FONT);
				messageHeader.setText(
						ChannelUtil.appendBlank(ChannelUtil.isEmpty(msg.getFromAddr())?"":msg.getFromAddr(),
								getFontMetrics(getFont()), 30).concat(msg.getDate() ==null ?"":
									messageFormat.format(msg.getDate()))
						 );
				messageHeader.setIconTextGap(10);
				messageHeader.setHorizontalTextPosition(JLabel.RIGHT);
			}
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
				
				if(ChannelMessage.TYPE_SEND != msg.getType()) { //已经回复了，就不再有回复功能
					btnReply.setBounds(SIZE.width-(60*2+85), 10, 85, 28);
					add(btnReply);
				}
				
				btnShowAll.setBounds(SIZE.width-(60*2+85), 10, 85, 28);
				add(btnShowAll);
				
				btnSave.setBounds(SIZE.width-60*2, 10, 60, 28);
				add(btnSave);
				
				btnDel.setBounds(SIZE.width-60, 10, 60, 28);
				add(btnDel);
			}
		}
		
		/**
		 * 创建带有控制功能的按钮
		 * @param text 按钮名称
		 * @param actionCommand 按钮名称(国际化)
		 * @return
		 */
		public JButton createCtrlButton(String text, String actionCommand)
		{
			JButton button = new JButton(text);
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
			Body body = (Body)this.getParent();
			if(QUICK_REPLY == e.getActionCommand()) { //快速回复
				body.doQuickReply(true);
			}
			else if(SAVE == e.getActionCommand())  { //保存
				body.doSave();
			}
			else if(DELETE == e.getActionCommand()) { //删除
				body.doDelete();
			}
			else if(SHOW_COMPLETE == e.getActionCommand()) { //完整显示
				body.doShowComplete();
			}
		}
	}
	
	//中间面板
	class Center extends JEditorPane implements  EventConstants
	{
		EditorKit editorKit = createDefaultEditorKit();
		HTMLEditorKit	htmlEditorKit = new HTMLEditorKit();		
		//默认大小
		Dimension SIZE = new Dimension(ChannelConfig.getIntValue("msgBodyWidth")-60,  25);		
		public Center()
		{
			//设置默认样式，等面板显示完整内容，再设置new HTMLEditorKit()
//			setEditorKit(new HTMLEditorKit());//设置样式为HTML编辑样式
			StyleSheet sheet= htmlEditorKit.getStyleSheet();
			sheet.addRule("a {text-decoration: none; color: blue; }");
			
			setEditable(false);//不可编辑
			setOpaque(false);
			setBackground(new Color(0, 0, 0, 0));//当上面透明无效时，这是唯一的办法
			setBorder(new EmptyBorder(0, 45, 0, 10));
			addHyperlinkListener(new ChannelHyperlinkListener());//支持超链
		}
		
		/**
		 * 这里覆盖原来的setText()方法，目的是当文本超出面板宽度时，会自动截取，多余部分以省略号代替
		 */
		public void setText(String text)
		{
			FontMetrics fm = getFontMetrics(getFont());
			int width = SIZE.width;
			int textWidth = fm.stringWidth(text);
			
			int limitLength = width-fm.stringWidth("……"); //不能超出limitLength的长度
			if(textWidth >= limitLength) {
				text = ChannelUtil.subString(text, fm, limitLength) + "……";
			}
			super.setText(text);
		}
		
		/**
		 * 显示完整的消息内容
		 * @param msg 消息对象
		 */
		public void showComplete(ChannelMessage msg) {
			if(msg != null) {
				if(htmlEditorKit != getEditorKit())
				{
					setEditorKit(htmlEditorKit);
				}
				
				//对不同消息类型进行单独处理
				if(ChannelMessage.WEIBO.equals(msg.getClazz())) {
					super.setText(WeiboParser.uniformWithoutImage(msg)); //先设置文本再获取高度
					SIZE.height = super.getPreferredScrollableViewportSize().height  +
							WeiboParser.getImageHeight(msg) ;
					super.setText(WeiboParser.uniform(msg));
				}
				else {
					super.setText(msg.getCompleteMsg()); //先设置文本再获取高度
					SIZE.height = super.getPreferredScrollableViewportSize().height;
				}
			}
		}

		/**
		 * 显示消息内容(区别于showComplete()，这里只是显示一行信息，超出的部分用省略号代替)
		 * @param msg
		 */
		public void showMessagePart(ChannelMessage msg)
		{
			if(msg != null) {
				if(editorKit != getEditorKit()) 
				{
					setEditorKit(editorKit);
				}
				setText(msg.getShortMsg());
				SIZE.height =15;
			}
		}		
	}
	
	//底部面板，就是一个回复框面板
	class Bottom extends JPanel implements ActionListener, EventConstants
	{
		private JButton btnReply = new JButton("发送"),
				btnCancel = new JButton("取消");
		private JTextField replyTextField = new JTextField();
		
		Dimension SIZE = new Dimension(ChannelConfig.getIntValue("msgBodyWidth")-60, 30);
		public Bottom() 
		{
			setVisible(false);//默认不可见
		}
		
		public void showMessageReplyArea()
		{
			setLayout(null);
			setPreferredSize(SIZE);
			
			replyTextField.setBounds(40, 0, SIZE.width-60*2, 28);
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
			Body body = (Body)this.getParent();
			if(e.getSource() == btnCancel) { //取消
				body.doQuickReply(false);
			}
			else if(e.getSource() == btnReply) {//回复
				String replyContent = getReplyContent().trim();
				if(!ChannelUtil.isEmpty(replyContent)) //检查回复内容是否为空
				{
					//首先开闭回复
					body.doQuickReply(false);
					
					//再新建消息
					ChannelMessage newMsg = body.iMsg.clone();
					newMsg.setType(ChannelMessage.TYPE_SEND);
					newMsg.setBody(replyContent);
					newMsg.setShortMsg(newMsg.getBody());
					newMsg.setCompleteMsg(newMsg.getBody());
					
					//最后回复消息
					ChannelDesktopPane desktop = (ChannelDesktopPane)
							ChannelGUI.channelNativeStore.get("ChannelDesktopPane");
					desktop.addMessage(newMsg);
				}
			}
		}
	}
}
