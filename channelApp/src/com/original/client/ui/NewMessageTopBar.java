package com.original.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

import com.original.client.EventConstants;
import com.original.client.layout.ChannelGridBagLayoutManager;
import com.original.client.layout.ChannelGridLayout;
import com.original.client.ui.ChannelMessagePane.MessageContainer;
import com.original.client.ui.data.AbstractButtonItem;
import com.original.client.util.ChannelConstants;
import com.original.client.util.ChannelUtil;
import com.original.client.util.IconFactory;
import com.original.service.channel.ChannelMessage;
import com.original.service.channel.Constants.CHANNEL;
import com.original.widget.OTextField;
import com.seaglasslookandfeel.widget.SGButton;
import com.seaglasslookandfeel.widget.SGLabel;
import com.seaglasslookandfeel.widget.SGPanel;

/**
 * 新建消息顶部栏，可以新建已知或未知联系人消息
 * @author WMS
 *
 */
public class NewMessageTopBar extends ChannelMessageTopBar implements ActionListener, FocusListener, EventConstants
{	
	AbstractButtonItem newMail = new AbstractButtonItem(null, POST_MAIL, 
			IconFactory.loadIconByConfig("sendMailIcon"),  IconFactory.loadIconByConfig("sendMailSelectedIcon"), IconFactory.loadIconByConfig("sendMailDisabledIcon"), 
			new Dimension(40, 40)), 
			newQQ = new AbstractButtonItem(null, POST_QQ, 
					IconFactory.loadIconByConfig("sendQQIcon"),	IconFactory.loadIconByConfig("sendQQSelectedIcon"), IconFactory.loadIconByConfig("sendQQDisabledIcon"), 
					new Dimension(40, 40)),
			newWeibo = new AbstractButtonItem(null, POST_WEIBO, 
					IconFactory.loadIconByConfig("sendWeiboIcon"), 	IconFactory.loadIconByConfig("sendWeiboSelectedIcon"), IconFactory.loadIconByConfig("sendWeiboDisabledIcon"), 
					new Dimension(40, 40));
			
	SGButton btnCC = ChannelUtil.createAbstractButton(
			new AbstractButtonItem("分享/抄送", ADD_CC, null));
	SGButton btnLinker = ChannelUtil.createAbstractButton(
			new AbstractButtonItem(null, SELECT_LINKER, IconFactory.loadIconByConfig("linkerIcon")));
	
	private boolean editable = false; //是否可编辑。如果为true，则联系人地址可以编辑(文本框)；否则只显示(标签)
	//由editable来决定是用lbMsgTo还是txtMsgTo(中)
	private SGLabel lbMsgTo = new SGLabel();
	private JTextField txtMsgTo = new OTextField();
	
	private MessageButtonGroup mbg = new MessageButtonGroup();//按钮控制组(左)
	private SGPanel control = new  SGPanel(); //按钮控制面板，如选择联系人、添加分享/抄送等(右)
	
	private ChannelGridBagLayoutManager layoutMgr = 
			new ChannelGridBagLayoutManager(this);
	
	private ChannelMessage newMsg = null;
	
	/**
	 * 新建消息顶部栏
	 * @param editable 是否可编辑。
	 */
	public NewMessageTopBar(boolean editable) {
		super(null, true);
		this.editable = editable;
		
		this.layoutMgr.setAnchor(GridBagConstraints.CENTER);
		this.layoutMgr.setInsets(new Insets(2, 5, 0, 5));
	}
	
	/**
	 * 构建状态栏
	 */
	protected void constructStatusBar() {
		//设置布局样式和大小
		setPreferredSize(new Dimension(SIZE.width, 55));
		
		SGPanel left = new SGPanel(new ChannelGridLayout(0, 0, new Insets(0,0, 0, 0)));
		left.add(mbg.add(newMail));
		left.add(mbg.add(newQQ));
		left.add(mbg.add(newWeibo));
		layoutMgr.addComToModel(left);
		
		SGPanel center = new SGPanel(new BorderLayout(5,0));
		center.setBorder(new EmptyBorder(10, 0, 0, 0));
		SGLabel lbTo = new SGLabel("To：");
		lbTo.setForeground(ChannelConstants.LIGHT_TEXT_COLOR);
		lbTo.setVerticalAlignment(SGLabel.TOP);
		center.add(lbTo, BorderLayout.WEST);
		if(!this.editable) {
			center.add(lbMsgTo, BorderLayout.CENTER);
		}
		else {
			txtMsgTo.addFocusListener(this);
			center.add(txtMsgTo, BorderLayout.CENTER);
		}
		layoutMgr.addComToModel(center,1,1,GridBagConstraints.HORIZONTAL);
		
		ChannelGridLayout controlLayout = new ChannelGridLayout(-10, 0, new Insets(0, 0, 0, 0));
		controlLayout.setAutoAdjust(false); //不自动调整
		control.setLayout(controlLayout);
		control.add(btnLinker);
		control.add(btnCC);
		layoutMgr.addComToModel(control);
		
		setMessage2GUI();
	}
	
	/**
	 * 初始化一些控件，如设置图标，颜色等。
	 */
	protected void initStatusBar() {		
		Color color = ChannelConstants.LIGHT_TEXT_COLOR;
		Cursor cursor = ChannelConstants.HAND_CURSOR;
		
		btnCC.setCursor(cursor);
		btnCC.setContentAreaFilled(false);
		btnCC.setForeground(color);
		btnCC.addActionListener(this);
		
		btnLinker.addActionListener(this);
		
		lbMsgTo.setHorizontalAlignment(SGLabel.LEFT);
		lbMsgTo.setForeground(color);
	}

	public boolean isEditable()
	{
		return editable;
	}
	/**
	 * 设置当前消息顶部栏是否可编辑
	 */
	public void setEditable(boolean editable)
	{
		if(this.editable != editable) {
			this.editable = editable;
			if(editable) {
				remove(lbMsgTo);
				add(txtMsgTo, BorderLayout.CENTER);
			}
			else {
				remove(txtMsgTo);
				add(lbMsgTo, BorderLayout.CENTER);
			}
			validate();//重置布局
		}
	}
	
	/**
	 * 设置Center面板中某一按钮控件显示或隐藏
	 * @param actionCommand 按钮名称
	 * @param isVisible
	 */
	public void setVisible(String actionCommand, boolean isVisible)
	{
		for(int i=0; i<control.getComponentCount(); i++)
		{
			Component child = control.getComponent(i);
			if(child instanceof AbstractButton
					&& (((AbstractButton) child).getActionCommand() == actionCommand))
			{
				if(child.isVisible() != isVisible)
					child.setVisible(isVisible);
				break;
			}
		}
	}
	
	/**
	 * 给当前显示的面板(如微博\QQ\邮件)显示消息，外界调用的主接口
	 * @param msg
	 */
	public void setMessage(ChannelMessage msg)
	{
		this.newMsg = msg;
	}
	private void setMessage2GUI() {
		if (newMsg != null) {
			setMessageTo(newMsg.getContactAddr());

			// 同时设置Body父面板的消息
			NewMessageBodyPane body = (NewMessageBodyPane) getMessageBody();
			body.setMessage(newMsg);

			if (newMsg.isMail()) {
				setEnabled(newMail.getActionCommand());
				newMail.getSource().doClick();
			} else if (newMsg.isQQ()) {
				setEnabled(newQQ.getActionCommand());
				newQQ.getSource().doClick();
			} else if (newMsg.isWeibo()) {
				setEnabled(newWeibo.getActionCommand());
				newWeibo.getSource().doClick();
			}
		} else {
			setMessageTo(null);
			
			newMail.getSource().doClick();//默认打开邮件
		}
	}
	
	//暂时的处理方法：
	private void setEnabled(String actionCommand) {
		Enumeration<AbstractButton> buttons = mbg.getElements();
		while (buttons.hasMoreElements()) {
			AbstractButton button = buttons.nextElement();

			if (actionCommand == POST_MAIL && button == newMail.getSource()) {
				newMail.setEnabled(true);
				continue;

			} else if (actionCommand == POST_WEIBO
					&& button == newWeibo.getSource()) {
				newWeibo.setEnabled(true);
				continue;

			} else if (actionCommand == POST_QQ && button == newQQ.getSource()) {
				newQQ.setEnabled(true);
				continue;
			}

			button.setEnabled(false);
		}
	}
	
	/**
	 * 设置消息发送地址，即收件人的邮箱地址、QQ账号或微博账号等
	 * @param to
	 */
	private void setMessageTo(String to)
	{
		if (editable) {
			txtMsgTo.setText(to);
			setVisible(SELECT_LINKER, true);
		} else {
			lbMsgTo.setText(to);
			setVisible(SELECT_LINKER, false);
		}
	}
	
	/**
	 * 获取消息的发送地址，即收件人的邮箱地址、QQ账号或微博账号等
	 * @return
	 */
	public String getMessageTo() {
		if (editable) {
			return txtMsgTo.getText();
		} else {
			return lbMsgTo.getText();
		}
	}
	
	/**
	 * 获取消息的来源地址，即发送人的邮箱地址、QQ账号或微博账号等
	 * @return
	 */
	public String getMessageFrom() {
		if (editable) {
			return null;
		} else {
			return !newMsg.isSent() ? newMsg.getToAddr() : newMsg.getFromAddr();
		}
	}
	
	//右边控制按钮的操作
	@Override
	public void actionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub
		if(ADD_CC == evt.getActionCommand()) { //添加分享/抄送，功能就是显示或隐藏"分享/抄送"文本框
			NewMessageBodyPane body = (NewMessageBodyPane)getMessageBody();
			NewMessageBodyPane child = body.currentChild();
			if(child != null) {
				if(child.center.isVisible(0)) {
					child.center.setVisible(0, false);
				}
				else {
					child.center.setVisible(0, true);
				}
			}
		}
		else if(SELECT_LINKER == evt.getActionCommand()) {//选择联系人
			
		}
	}
	
	@Override
	public void focusGained(FocusEvent e) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO 自动生成的方法存根
		if(txtMsgTo == e.getComponent()) {
			NewMessageBodyPane body = (NewMessageBodyPane)getMessageBody();
			MessageContainer container = body.getMessageContainer();
			ChannelMessagePane parent = (ChannelMessagePane)container.getParent();
			
//			parent.setUid(ChannelMessage.getContactName(txtMsgTo.getText()));
		}
	}

	/**
	 * 自定义消息按钮组，该按钮组具有排他的功能。
	 * @author WMS
	 */
	public class MessageButtonGroup extends ButtonGroup implements ActionListener
	{
		Hashtable<AbstractButton,Icon> icons = new Hashtable<AbstractButton, Icon>(),
				selectedIcons = new Hashtable<AbstractButton, Icon>();
		
		public AbstractButton add(final AbstractButtonItem item)
		{
			if(item != null) {
				final SGButton button = ChannelUtil.createAbstractButton(item);
				final ButtonModel model  = new JToggleButton.ToggleButtonModel() {
					public void setSelected(boolean b)
					{
						super.setSelected(b);
						if(b) {
							button.setIcon(selectedIcons.get(button));
						}
						else {
							button.setIcon(icons.get(button));
						}
					}
				};
				model.setActionCommand(button.getActionCommand());
				button.setModel(model);
				add(button);
				return button;
			}
			return null;
		}
		
	    public void add(AbstractButton b) {
	    	if(b != null) {
	    		icons.put(b,b.getIcon());
	    		selectedIcons.put(b, b.getSelectedIcon());
	    		
	    		b.addActionListener(this);
	    		super.add(b);
	    	}
	    }
	 
	    public void remove(AbstractButton b) {
	       if(b != null) {
	    	   b.removeActionListener(this);
	    	   super.remove(b);
	    	   
	    	   icons.remove(b);
	    	   selectedIcons.remove(b);
	       }
	    }

		public void actionPerformed(ActionEvent evt)
		{
			NewMessageBodyPane body = (NewMessageBodyPane)getMessageBody();
			if(evt.getActionCommand() == POST_MAIL) {
				setVisible(ADD_CC, true);//只有邮件显示分享/抄送
				body.showChild(CHANNEL.MAIL);
			}
			else if(evt.getActionCommand() == POST_QQ) {
				setVisible(ADD_CC, false);
				body.showChild(CHANNEL.QQ);
			}
			else if(evt.getActionCommand() == POST_WEIBO) {
				setVisible(ADD_CC, false);
				body.showChild(CHANNEL.WEIBO);
			}
		}
	}	
}
