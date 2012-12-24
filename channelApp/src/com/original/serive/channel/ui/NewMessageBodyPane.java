package com.original.serive.channel.ui;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import com.original.serive.channel.ChannelGUI;
import com.original.serive.channel.EventConstants;
import com.original.serive.channel.border.InnerShadowBorder;
import com.original.serive.channel.layout.ChannelGridBagLayoutManager;
import com.original.serive.channel.layout.ChannelGridLayout;
import com.original.serive.channel.layout.VerticalGridLayout;
import com.original.serive.channel.ui.data.AbstractButtonItem;
import com.original.serive.channel.util.ChannelConfig;
import com.original.serive.channel.util.ChannelUtil;
import com.original.serive.channel.util.IconFactory;
import com.original.service.channel.ChannelMessage;
import com.original.widget.OButton;
import com.original.widget.OScrollBar;
import com.original.widget.OTextField;

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
	
	public NewMessageBodyPane() {
		setLayout(new VerticalGridLayout(VerticalGridLayout.TOP_TO_BOTTOM, 0, 5, new Insets(5, 0, 5, 0)));
		add(top);
		add(center);
	}
	
	/**
	 * 给当前新建消息面板体添加消息
	 * @param msg
	 */
	public void setMessage2GUI(ChannelMessage msg)
	{
		newMsg = msg;
		//下面开始处理消息：
		
	}
	
	//顶部功能按钮面板
	public class Top extends JPanel implements ActionListener, EventConstants
	{
		OButton btnCancel = new OButton("取消");
		public Top() {
			setLayout(new ChannelGridLayout(2, 0, new Insets(0, 10, 0, 0)));
			setButtomItems(new AbstractButtonItem[] {
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
					JButton button = ChannelUtil.createAbstractButton(buttonItem);
					button.addActionListener(this);
					super.add(button);
				}
			}
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(e.getActionCommand() == CANCEL) {
				ChannelDesktopPane desktop = (ChannelDesktopPane)ChannelGUI.channelNativeStore.get("ChannelDesktopPane");
				if(newMsg != null) {
					desktop.removeShowComp(PREFIX_NEW + newMsg.getContactName(), true);
				}
			}
		}
	}
	
	//中间编辑面板
	public class Center extends JPanel implements ActionListener, EventConstants
	{
		ChannelGridBagLayoutManager layoutMgr =
				new ChannelGridBagLayoutManager(this);
		
		Dimension SIZE = new Dimension(ChannelConfig.getIntValue("msgBodyWidth"),
				380);
		
		//一些组件，显示或隐藏由消息类型(QQ、微博、邮件等)来决定
		private JTextField  txtCC = new OTextField(),//分享/抄送
				txtSubject = new OTextField(); //主题
		
		//一些功能按钮
		private JButton btnFont =(JButton) ChannelUtil.createAbstractButton(
				new AbstractButtonItem(null, SET_FONT, IconFactory.loadIconByConfig("fontIcon"))), //字体
				btnImage = (JButton) ChannelUtil.createAbstractButton(
						new AbstractButtonItem(null, ADD_IMAGE, IconFactory.loadIconByConfig("imageIcon"))), //图片
				btnFile = (JButton) ChannelUtil.createAbstractButton(
						new AbstractButtonItem(null, ADD_FILE, IconFactory.loadIconByConfig("fileIcon")));//附件
		
		//文本面板
		private JTextPane content = new JTextPane();
		
		public Center() {
			layoutMgr.setAnchor(GridBagConstraints.NORTHEAST); //靠右对齐，主要针对标签
			layoutMgr.setInsets(new Insets(0, 0, 2, 2));
			
			jbInit();
		}
		
		//设置和添加组件
		private void jbInit() {
			this.setPreferredSize(SIZE); //固定大小，文本面板如果超出该大小，则添加滚动条
			this.setBorder(new EmptyBorder(0, 10, 0, 10));
			
			layoutMgr.addComToModel(new JLabel("分享/抄送："));
			layoutMgr.addComToModel(txtCC, 1, 1, GridBagConstraints.HORIZONTAL);
			layoutMgr.newLine();
			
			layoutMgr.addComToModel(new JLabel("主题："));
			layoutMgr.addComToModel(txtSubject, 1, 1, GridBagConstraints.HORIZONTAL);
			layoutMgr.newLine(1);
			
			JPanel ctrlpane = new JPanel(new ChannelGridLayout(5, 0, new Insets(0, 5, 0, 0)));
			ctrlpane.add(btnFont);
			ctrlpane.add(btnImage);
			ctrlpane.add(btnFile);
			layoutMgr.addComToModel(ctrlpane,1,1,GridBagConstraints.HORIZONTAL);
			layoutMgr.newLine();
			
			layoutMgr.addComToModel(new JLabel("内容："));
			content.setOpaque(false);
			content.setBackground(new Color(0, 0, 0, 0)); //设置文本面板透明的唯一方法
			JScrollPane scrollPane = new JScrollPane(content,
	                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	        scrollPane.setVerticalScrollBar(new OScrollBar(JScrollBar.VERTICAL, Color.gray));
	        scrollPane.setBorder(BorderFactory.createCompoundBorder(
	        		new InnerShadowBorder(), new EmptyBorder(0, 5, 10, 5)));
	        scrollPane.setOpaque(false);
	        scrollPane.getViewport().setOpaque(false);
	        scrollPane.setViewportBorder(null);
	        
			layoutMgr.addComToModel(scrollPane, 1, 1, GridBagConstraints.BOTH);
		}

		public void actionPerformed(ActionEvent e)
		{
			System.out.println(e.getActionCommand());
		}
	}

}
