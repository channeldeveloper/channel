package com.original.serive.channel.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import atg.taglib.json.util.JSONException;

import com.original.channel.ChannelAppCache;
import com.original.serive.channel.EventConstants;
import com.original.serive.channel.border.InnerShadowBorder;
import com.original.serive.channel.comp.CButton;
import com.original.serive.channel.comp.CLabel;
import com.original.serive.channel.comp.CPanel;
import com.original.serive.channel.comp.CScrollPanel;
import com.original.serive.channel.comp.CTextPane;
import com.original.serive.channel.layout.ChannelGridBagLayoutManager;
import com.original.serive.channel.layout.ChannelGridLayout;
import com.original.serive.channel.layout.VerticalGridLayout;
import com.original.serive.channel.server.ChannelAccesser;
import com.original.serive.channel.ui.data.AbstractButtonItem;
import com.original.serive.channel.ui.data.FontStyle;
import com.original.serive.channel.ui.widget.EditorHandler;
import com.original.serive.channel.ui.widget.FileAttacher;
import com.original.serive.channel.ui.widget.FontChooser;
import com.original.serive.channel.ui.widget.ToolTip;
import com.original.serive.channel.util.ChannelConfig;
import com.original.serive.channel.util.ChannelConstants;
import com.original.serive.channel.util.ChannelUtil;
import com.original.serive.channel.util.IconFactory;
import com.original.service.channel.Attachment;
import com.original.service.channel.ChannelMessage;
import com.original.service.channel.Constants;
import com.original.service.channel.Constants.CHANNEL;
import com.original.service.channel.Utilies;
import com.original.service.channel.core.ChannelService;
import com.original.widget.OTextField;
import com.seaglasslookandfeel.widget.CFileChooser;

/**
 * 新建消息主体面板，区别于{@link ChangeMessageBodyPane}显示消息主体面板。
 * 该新建消息面板目前支持邮件、QQ、Weibo等功能。分上、中两个部分。
 * 其中上部分是功能按钮面板，中部分是编辑面板。对于不同类型的消息，其上、中部分可能不同。设计的时候需要考虑。
 * @author WMS
 *
 */
public class NewMessageBodyPane extends ChannelMessageBodyPane
{
	Top top = new Top(); //顶部功能按钮面板
	Center center = new Center(); //中间编辑面板
	
	private ChannelMessage newMsg = null;
	private LayoutManager childLayout = 
			new VerticalGridLayout(VerticalGridLayout.TOP_TO_BOTTOM, 0, 5, new Insets(5, 0, 5, 0)),
			parentLayout = new CardLayout();
	
	private boolean isParent = true;
	private CHANNEL channel = null;
	
	public NewMessageBodyPane() {
		this(true);
	}
	
	public NewMessageBodyPane(boolean isParent) {
		if(isParent) { //用作父面板，即添加如Mail\QQ\Weibo等子面板
			setLayout(parentLayout);
		}
		else {
			setLayout(childLayout);
			add(top);
			add(center);
		}
		this.isParent = isParent;
	}
	
	/**
	 * 选择消息Channel新建面板
	 * @param channel
	 */
	public void showChild(CHANNEL channel)
	{
		NewMessageBodyPane child = null;
		if(isParent) {
			child = newChild(channel);
			
			if(child != null) {
				child.setMessage(newMsg);
				child.channel = channel;
				((CardLayout)parentLayout).show(this, channel.name());
			}
		}
	}
	private NewMessageBodyPane newChild(CHANNEL channel) {
		NewMessageBodyPane child = null;
		if((child = hasChild(channel)) != null)
		{
			return child;
		}
		
		child = new NewMessageBodyPane(false);
		Top ttop = child.top;
		Center ccenter = child.center;
		switch(channel)
		{
		case WEIBO:
			ttop.setVisible(SAVE_TO_DRAFT, false);
			ttop.setVisible(DELETE, false);
			
			ccenter.setVisible(0, false); //不显示抄送
			ccenter.setVisible(1, false); //不显示主题
			ccenter.setUsable(SET_FONT, false);//禁用字体样式
			ccenter.setUsable(ADD_FILE, false);//禁用附件
			ccenter.setSupportMultiImages(false);//不支持多张图片
			break;
		case QQ:
			ttop.setVisible(SAVE_TO_DRAFT, false);
			ttop.setVisible(DELETE, false);
			
			ccenter.setVisible(0, false); //不显示抄送
			ccenter.setVisible(1, false); //不显示主题
			ccenter.setUsable(ADD_FILE, false);//禁用附件
			ccenter.setOverallStyle(true); //全局样式
			
			break;
			
		case MAIL: //默认邮件
			ttop.setVisible(SAVE_TO_DRAFT, true);
			ttop.setVisible(DELETE, false);
			
			ccenter.setVisible(0, false);//默认不显示抄送，只有点击"分享/抄送"的时候才显示
			ccenter.setOverallStyle(false); //非全局样式
		default:
				break;
		}
		child.setName(channel.name()); //注意这里
		add(channel.name(), child);
		return child;
	}
	
	/**
	 * 父面板中是否有Channel对应的子面板
	 * @param channel
	 * @return
	 */
	public NewMessageBodyPane hasChild(CHANNEL channel)
	{
		if(isParent)
		{
			for(int i=0; i<getComponentCount(); i++)
			{
				Component comp = getComponent(i);
				if(comp instanceof NewMessageBodyPane
						&& comp.getName() == channel.name())
					return ((NewMessageBodyPane)comp);
			}
		}
		return null;
	}
	
	/**
	 * 是否是父面板
	 * @return
	 */
	public boolean isParent() {
		return isParent;
	}
	
	/**
	 * 当前显示的子面板
	 * @return
	 */
	public NewMessageBodyPane currentChild() {
		if(!isParent) 
			return this;
		
		for(int i=0; i<getComponentCount(); i++)
		{
			Component comp = getComponent(i);
			if(comp instanceof NewMessageBodyPane
					&& comp.isVisible())
				return (NewMessageBodyPane)comp;
		}
		return null;
	}
	
	/**
	 * 给当前显示的面板(如微博\QQ\邮件)显示消息
	 * @param msg
	 */
	public void setMessage(ChannelMessage msg)
	{
		this.newMsg = msg;
		if (!isParent && msg != null) {
			center.setCC(msg.getCC());
			center.setSubject(msg.getSubject());
			center.setText(msg.getBody());
			center.setAttachments(msg.getAttachments());
		}
	}
	
	@Override
	public ChannelMessage getChannelMessage() {
		// TODO 自动生成的方法存根
		if (newMsg == null) {
			newMsg = new ChannelMessage();
			newMsg.setAction(Constants.ACTION_NEW);
			switch (channel) {
			case MAIL:
				newMsg.setClazz(ChannelMessage.MAIL);
				break;
			case WEIBO:
				newMsg.setClazz(ChannelMessage.WEIBO);
				break;
			case QQ:
				newMsg.setClazz(ChannelMessage.QQ);
				break;
			}
		}
		return this.newMsg;
	}
	
	/**
	 * 获取消息发送地址。前者为来源地址，后者为发送地址
	 * @return
	 */
	public String[] getMessageAddrs() {
		NewMessageBodyPane body = isParent ? this : (NewMessageBodyPane)this.getParent();
		NewMessageTopBar topBar = (NewMessageTopBar) body.getMessageStatusBar();
		if (topBar != null) {
			return new String[]{topBar.getMessageFrom(), topBar.getMessageTo()};
		}
		return null;
	}
	
	/**
	 * 发送地址是否可编辑。默认回复时是不可编辑；转发时可编辑
	 * @return
	 */
	public boolean isMessageAddrEditable() {
		NewMessageBodyPane body = isParent ? this : (NewMessageBodyPane)this.getParent();
		NewMessageTopBar topBar = (NewMessageTopBar) body.getMessageStatusBar();
		if (topBar != null) {
			return topBar.isEditable();
		}
		return false;
	}

	/**
	 * 编辑消息，即收集当前编辑面板的所有消息：包括主体、内容、字体样式等等。
	 * @return
	 */
	public ChannelMessage editMessage() {		
		ChannelMessage msg = (newMsg = getChannelMessage()).simplyClone();
		
		msg.setType(ChannelMessage.TYPE_SEND);//强制设为发送类型
		msg.setSentDate(new Date());
		msg.setReceivedDate(msg.getSentDate());//设置和发送时间一样
		
		if (newMsg.isWeibo()) {
			msg.setBody(center.getNewAddedText(true));
			
		} else if (newMsg.isQQ()) {
			msg.setBody(center.getNewAddedText(true));

		} else if (newMsg.isMail()) {
			msg.setSubject(center.getSubject());// 主题
			msg.setCC(center.getCC()); //抄送
			msg.setAttachments(center.getAttachments());//附件
			
			msg.setBody(center.getText(false));
		}
		
		String[] msgAddrs = getMessageAddrs(); //获取消息的收发地址
		if (msgAddrs != null && msgAddrs.length > 1) {
			msg.setToAddr(msgAddrs[1]);
			msg.setFromAddr(msgAddrs[0]);
		}
		
		//对于QQ、邮件还有字体样式：
		HashMap<String, String> exts = msg.getExtensions();
		if(newMsg.isQQ()) {
			FontStyle fs = center.getFontStyle();
			try {
				if (exts == null) {
					exts = new HashMap<String, String>();
				}
				exts.put(Constants.QQ_FONT_STYLE, fs.toJSONString());
				msg.setExtensions(exts);
			} catch (JSONException ex) {
				ex.printStackTrace();
			}
		}
		
		return msg;
	}
	
	/**
	 * 检查编辑消息的有效性
	 * @param msg 消息对象
	 * @return
	 */
	public boolean checkVaildMessage(String action, ChannelMessage msg) 
	{
		if(msg != null) {			
			if(msg.getMessageID() == null) {
//				ChannelUtil.showMessageDialog(null, "错误", "消息ID为空！");
//				return false;
				msg.setMessageID("" + System.currentTimeMillis());
			}
			
			if (action == POST) { // 发送
				// 分类型进行判断：
				if (msg.isMail()) {// 邮件必须有主题
					if (ChannelUtil.isEmpty(msg.getSubject(), true)) {
						ChannelUtil.showMessageDialog(null, "错误", "邮件主题不能为空！");
						msg = null;
						return false;
					}
				}

				// 检查消息的收、发地址。发送人可以为空(后台可以自动补填)，但是收件人不能为空
				if (ChannelUtil.isEmpty(msg.getToAddr(), true)) {
					ChannelUtil.showMessageDialog(null, "错误", "没有填写消息的收件人(To：)地址！");
					msg = null; 
					return false;
				}
			}
			
			if (action == POST || action == SAVE_TO_DRAFT) { //发送或者存草稿
				if (ChannelUtil.isEmpty(msg.getBody(), true)) {
					ChannelUtil.showMessageDialog(null, "错误", "消息内容不能为空！");
					msg = null;
					return false;
				}
			}
			
			//检查通过：
			return true;
		}
		return false;
	}
	
	//返回上一面板，即历史面板
	private void returnToHistory() {
		ChannelDesktopPane desktop = ChannelAppCache.getDesktop();
		if (newMsg != null && newMsg.getMessageID() != null) {
			if (newMsg.getAction() == Constants.ACTION_REPLY
					|| newMsg.getAction() == Constants.ACTION_REPOST) {
				desktop.removeShowComp(PREFIX_SHOWANDNEW + newMsg.getContactName());
				
				//下面两种情况，只有一种可能：
				desktop.removeShowComp(PREFIX_SHOW + newMsg.getContactName());
				desktop.removeShowComp(PREFIX_SHOWALL + newMsg.getContactName());
			} else {
				desktop.removeShowComp(PREFIX_NEW + newMsg.getContactName());
			}
		} else {
			desktop.removeShowComp(PREFIX_NEW);
		}
	}

	//顶部功能按钮面板
	public class Top extends CPanel implements ActionListener, EventConstants
	{
		public Top() {
			setLayout(new ChannelGridLayout(2, 0, new Insets(0, 10, 0, 0)));
			setButtomItems(new AbstractButtonItem[] {
				new AbstractButtonItem("发送", POST, null),
				new AbstractButtonItem("存草稿", SAVE_TO_DRAFT, null),
				new AbstractButtonItem("保存", SAVE, null),
				new AbstractButtonItem("删除", DELETE, null),
				new AbstractButtonItem("取消", CANCEL, null),	
			});
		}
		
		/**
		 * 设置按钮项目组，如果面板已有按钮，则全部清除后再添加新按钮项目。
		 * @param buttonItems 按钮项目组
		 */
		public void setButtomItems(AbstractButtonItem[] buttonItems)
		{
			if(buttonItems != null && buttonItems.length > 0) {
				if(this.getComponentCount() > 0)
					this.removeAll();
				if(!this.getComponentOrientation().isLeftToRight())
					this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
				
				addButtonItems(true, buttonItems);
			}
		}
		
		/**
		 * 添加新的按钮至面板中
		 * @param buttonItem 按钮项目
		 * @param leftToRight 如果为true，则添加至最后；否则添加至开头
		 * @return
		 */
		public void addButtonItems(boolean leftToRight, AbstractButtonItem... buttonItems )
		{
			ComponentOrientation co = this.getComponentOrientation();
			if(co.isLeftToRight() != leftToRight) {
				this.setComponentOrientation(leftToRight?ComponentOrientation.LEFT_TO_RIGHT:
					ComponentOrientation.RIGHT_TO_LEFT);
			}
			
			if(buttonItems != null && buttonItems.length > 0)
			{
				for(AbstractButtonItem buttonItem : buttonItems) {
					CButton button = ChannelUtil.createAbstractButton(buttonItem);
					button.addActionListener(this);
					super.add(button);
				}
			}
		}
		
		/**
		 * 设置Top面板中某一按钮控件显示或隐藏
		 * @param actionCommand 按钮名称
		 * @param isVisible
		 */
		public void setVisible(String actionCommand, boolean isVisible)
		{
			for(int i=0; i<getComponentCount(); i++)
			{
				Component child = getComponent(i);
				if(child instanceof AbstractButton
						&& (((AbstractButton) child).getActionCommand() == actionCommand))
				{
					if(child.isVisible() != isVisible)
						child.setVisible(isVisible);
					break;
				}
			}
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(e.getActionCommand() == CANCEL) {
				returnToHistory();
			}
			else if(e.getActionCommand() == POST || 
					e.getActionCommand() == SAVE_TO_DRAFT) { //发送或者存草稿
				
				boolean isLock = false;
				try {
					if (isLock = channelLock.tryLock()) {
						ChannelMessage sendMsg = editMessage();
						if (checkVaildMessage(e.getActionCommand(), sendMsg)) {
							try {
								ChannelService cs = ChannelAccesser.getChannelService();
								if (e.getActionCommand() == POST) {
									cs.put(Constants.ACTION_REPLY, sendMsg); //回复
									
									//添加消息
									ChannelDesktopPane desktop = ChannelAppCache.getDesktop();
									desktop.addMessage(sendMsg);
								} else if (e.getActionCommand() == SAVE_TO_DRAFT) {
									sendMsg.setRepost(isMessageAddrEditable());
									cs.put(Constants.ACTION_PUT_DRAFT, sendMsg); //存草稿
								}
								
								center.clearAll(); //清空所有文本
								returnToHistory();//同时返回上一面板
							} catch (Exception ex) {
								ChannelUtil.showMessageDialog(NewMessageBodyPane.this, "错误", ex);
							}
						}
					}
				} finally {
					if (isLock) {
						channelLock.unlock();
					}
				}
			} 
			else if (e.getActionCommand() == DELETE) {// 删除，这里就是删除草稿
				if (ChannelUtil.confirm(null, "确认删除", "是否删除该草稿？")) {
					ChannelMessage msg = (newMsg = getChannelMessage()).clone();
					msg.setDrafted(true);

					try {
						ChannelService cs = ChannelAccesser.getChannelService();
						cs.trashMessage(msg);

						returnToHistory();
					} catch (Exception ex) {
						ChannelUtil.showMessageDialog(NewMessageBodyPane.this, "错误", ex);
					}
				}
			}
		}
	}
	
	//中间编辑面板
	public class Center extends CPanel implements ActionListener, FocusListener, EventConstants
	{
		ChannelGridBagLayoutManager layoutMgr =
				new ChannelGridBagLayoutManager(this);
		
		Dimension SIZE = new Dimension(ChannelConfig.getIntValue("msgBodyWidth"), 340);
		
		//一些组件，显示或隐藏由消息类型(QQ、微博、邮件等)来决定
		private JTextField  txtCC = new OTextField(),//分享/抄送
				txtSubject = new OTextField(); //主题
		
		//一些功能按钮
		private CPanel control = new CPanel(new ChannelGridLayout(5, 0, new Insets(0, 5, 0, 0)));
		private CButton btnFont =(CButton) ChannelUtil.createAbstractButton(
				new AbstractButtonItem(null, SET_FONT, IconFactory.loadIconByConfig("fontIcon"), null, 
						IconFactory.loadIconByConfig("fontDisabledIcon"), null)), //字体
				btnImage = (CButton) ChannelUtil.createAbstractButton(
						new AbstractButtonItem(null, ADD_IMAGE, IconFactory.loadIconByConfig("imageIcon"), null, 
								IconFactory.loadIconByConfig("imageDisabledIcon"), null)), //图片
				btnFile = (CButton) ChannelUtil.createAbstractButton(
						new AbstractButtonItem(null, ADD_FILE, IconFactory.loadIconByConfig("fileIcon"), null, 
								IconFactory.loadIconByConfig("fileDisabledIcon"), null)),//附件
				btnDebug =  (CButton) ChannelUtil.createAbstractButton(
						new AbstractButtonItem(null, DEBUG, IconFactory.loadIconByConfig("debugIcon"), null, 
								IconFactory.loadIconByConfig("debugDisabledIcon"), null));//调试
		
		//提示控件
		private ToolTip toolTip = new ToolTip();
		
		//文本面板
		private CTextPane content = new CTextPane();
		
		//按钮对应的对话框
		private FontChooser fontChooser = new FontChooser(content);
		private CFileChooser fileChooser = new CFileChooser();
		private EditorHandler handler = new EditorHandler(content);
		private FileAttacher fileAttacher = new FileAttacher();
		
		public Center() {
			layoutMgr.setAnchor(GridBagConstraints.NORTHEAST); //靠右对齐，主要针对标签
			layoutMgr.setInsets(new Insets(0, 0, 2, 2));
			
			jbInit();
		}
		
		//设置和添加组件
		private void jbInit() {
			this.setPreferredSize(SIZE); //固定大小，文本面板如果超出该大小，则添加滚动条
			this.setBorder(new EmptyBorder(0, 10, 0, 10));
			
			layoutMgr.addComToModel(new CLabel("分享/抄送："));
			layoutMgr.addComToModel(txtCC, 1, 1, GridBagConstraints.HORIZONTAL);
			txtCC.addFocusListener(this);
			layoutMgr.newLine();
			
			layoutMgr.addComToModel(new CLabel("主题："));
			layoutMgr.addComToModel(txtSubject, 1, 1, GridBagConstraints.HORIZONTAL);
			layoutMgr.newLine(1);
			
			control.add(btnFont); //字体
			control.add(btnImage);//图像
			control.add(btnFile);//附件
//			control.add(btnDebug);//调试
			btnFont.addActionListener(this);
			btnImage.addActionListener(this);
			btnFile.addActionListener(this);
//			btnDebug.addActionListener(this);
			layoutMgr.addComToModel(control,1,1,GridBagConstraints.HORIZONTAL);
			layoutMgr.newLine();
			
			layoutMgr.addComToModel(new CLabel("内容："));
//			content.setEditorKit(new HTMLEditorKit());
//			content.addHyperlinkListener(new ChannelHyperlinkListener()); //可编辑状态下，不支持超链
			content.setContentType("text/html; charset=gb2312");
			content.setOpaque(false);
			content.setCursor(ChannelConstants.TEXT_CURSOR);
			content.setBackground(new Color(0, 0, 0, 0)); //设置文本面板透明的唯一方法
			CScrollPanel scrollPane = ChannelUtil.createScrollPane(content, Color.gray);
	        scrollPane.setBorder(BorderFactory.createCompoundBorder(
	        		new InnerShadowBorder(), new EmptyBorder(0, 5, 10, 5)));
	        
			layoutMgr.addComToModel(scrollPane, 1, 1, GridBagConstraints.BOTH);
		}
		
		/**
		 * 设置Center面板中某一行显示或隐藏
		 * @param lineIndex 行索引，从0开始编号
		 * @param isVisible
		 */
		public void setVisible(int lineIndex, boolean isVisible)
		{
			layoutMgr.setLineVisible(lineIndex, isVisible);
		}
		
		/**
		 * 判断Center面板中某一行是否显示
		 * @param lineIndex 行索引，从0开始编号
		 * @return
		 */
		public boolean isVisible(int lineIndex)
		{
			return layoutMgr.isLineVisible(lineIndex);
		}
		
		/**
		 * 设置Center面板中某一按钮控件可用或禁用
		 * @param actionCommand 按钮名称
		 * @param isUsable
		 */
		public void setUsable(String actionCommand, boolean isUsable)
		{
			for(int i=0; i<control.getComponentCount(); i++)
			{
				Component child = control.getComponent(i);
				if(child instanceof AbstractButton
						&& (((AbstractButton) child).getActionCommand() == actionCommand))
				{
					if(!isUsable) {
//						child.setCursor(ChannelConstants.DEFAULT_CURSOR);
						((AbstractButton) child).removeActionListener(this);
						child.setEnabled(false);
					}
					else {
//						child.setCursor(ChannelConstants.HAND_CURSOR);
						((AbstractButton) child).addActionListener(this);
						child.setEnabled(true);
					}
					break;
				}
			}
		}
		
		/**
		 * 设置content的样式是否是全局的
		 * @param isOverall
		 */
		public void setOverallStyle(boolean isOverall)
		{
			fontChooser.setOverallStyle(isOverall);
		}
		
		/**
		 * 设置是否支持多张图片插入
		 * @param supportMultiImages
		 */
		public void setSupportMultiImages(boolean supportMultiImages) {
			handler.setSupportMultiImages(supportMultiImages);
		}
		
		/**
		 * 获取当前content中的文本
		 * @param isPlain 是否是纯文本
		 * @return
		 */
		public String getText(boolean isPlain) {
			String text = content.getText();
			return isPlain ? handler.parseHTML(text) : text;
		}
		public void setText(String text) {
			content.setText(text);
			content.setCaretPosition(0);
		}
		
		/**
		 * 获取新增的内容，用于回复或转发中新追加的内容
		 * @param isPlain
		 * @return
		 */
		public String getNewAddedText(boolean isPlain) {
			String text = content.getText();
			text = Utilies.getNewAddedBody(text);
			return isPlain ? handler.parseHTML(text) : text;
		}
		
		/**
		 * 清除content中的文本，并同时恢复默认样式等
		 */
		public void clearText () {
			content.setText(null);
			fontChooser.setFontStyle(null);  //恢复默认样式
			fileChooser.setSelectedFile(null);  //清空选中文件
			fileAttacher.clearAttachments(); //清空所有的附件
		}
		
		/**
		 * 清空文本和所有文本输入框中的内容
		 */
		public void clearAll() {
			txtCC.setText(null);
//			txtCC.removeFocusListener(this);
			txtSubject.setText(null);
			
			clearText();
			setVisible(0, false);//隐藏抄送地址
		}
		
		/**
		 * 获取当前字体选择器的样式
		 * @return
		 */
		public FontStyle getFontStyle() {
			return fontChooser.lookforFontStyle();
		}
		
		/** 获取主题 */
		public String getSubject() {
			return txtSubject.getText();
		}
		public void setSubject(String subject) {
			txtSubject.setText(subject);
			if(subject != null && !subject.isEmpty()) {
				setVisible(1, true);//显示主题行
			}
		}
		
		/**  获取抄送地址 */
		public String getCC() {
			return txtCC.getText();
		}
		public void setCC(String cc) {
			txtCC.setText(cc);
			if(cc != null && !cc.isEmpty()) {
				setVisible(0, true);//显示抄送行
			}
		}
		
		/** 获取附件列表 */
		public List<Attachment> getAttachments() {
			return fileAttacher.convertToAttachments();
		}
		public void setAttachments(List<Attachment> attachments) {
			fileAttacher.setAttachments(attachments);
		}

		public void actionPerformed(ActionEvent e)
		{
			if(SET_FONT == e.getActionCommand()) {
				ChannelUtil.showCustomedDialog(btnFont, "选择字体", false, fontChooser);
			}
			else if(ADD_IMAGE == e.getActionCommand()) {
				if(channel == CHANNEL.QQ) {//QQ目前只显示表情框
					ChannelUtil.showQQFaceDialog(btnImage, "选择表情", false, content);
				}
				else { //其他显示图片选择框
					File imgFile = ChannelUtil.showImageChooserDialog(btnImage, "选择图片", true, fileChooser);
					handler.insertImage(imgFile, 400, 300);
				}
			}
			else if(ADD_FILE == e.getActionCommand()) {
				ChannelUtil.showCustomedDialog(btnFile, "添加附件", true, fileAttacher);
			}
			else if(DEBUG == e.getActionCommand()) {
				System.out.println(content.getText());
			}
		}

		@Override
		public void focusGained(FocusEvent e) {
			// TODO 自动生成的方法存根
			if(e.getComponent() == txtCC) {
				if (!toolTip.isInvokerShow()) {
					toolTip.setToolTipText("多个抄送地址，请以英文字符“;”隔开！");
					toolTip.show(txtCC, 0, txtCC.getY() + 25);
				} else {
					toolTip.removeInvoker(txtCC);
				}
			}
		}

		@Override
		public void focusLost(FocusEvent e) {
			// TODO 自动生成的方法存根
		}
	}
}
