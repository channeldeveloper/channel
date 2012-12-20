package com.original.serive.channel.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import com.original.serive.channel.EventConstants;
import com.original.serive.channel.layout.ChannelGridBagLayoutManager;
import com.original.serive.channel.layout.ChannelGridLayout;
import com.original.serive.channel.ui.data.AbstractButtonItem;
import com.original.serive.channel.util.ChannelConstants;
import com.original.serive.channel.util.ChannelUtil;
import com.original.serive.channel.util.IconFactory;

/**
 * 新建消息顶部栏，可以新建已知或未知联系人消息
 * @author WMS
 *
 */
public class NewMessageTopBar extends ChannelMessageTopBar implements EventConstants
{	
	AbstractButtonItem newMail = new AbstractButtonItem(null, POST_MAIL, 
			IconFactory.loadIconByConfig("sendMailIcon"),  IconFactory.loadIconByConfig("sendMailSelectedIcon"), 
			new Dimension(40, 40)), 
			newQQ = new AbstractButtonItem(null, POST_QQ, 
					IconFactory.loadIconByConfig("sendQQIcon"),	IconFactory.loadIconByConfig("sendQQSelectedIcon"),
					new Dimension(40, 40)),
			newWeibo = new AbstractButtonItem(null, POST_WEIBO, 
					IconFactory.loadIconByConfig("sendWeiboIcon"), 	IconFactory.loadIconByConfig("sendWeiboSelectedIcon"),
					new Dimension(40, 40));
	
	JButton btnCC = new JButton("分享/抄送");
	
	private boolean editable = false; //是否可编辑。如果为true，则联系人地址可以编辑(文本框)；否则只显示(标签)
	//由editable来决定是用lbMsgTo还是txtMsgTo
	private JLabel lbMsgTo = new JLabel();
	private JTextField txtMsgTo = new JTextField();
	
	private ChannelGridBagLayoutManager layoutMgr = 
			new ChannelGridBagLayoutManager(this);
	
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
		
		JPanel left = new JPanel(new ChannelGridLayout(0, 0, new Insets(0,0, 0, 0)));
		MessageButtonGroup mbg = new MessageButtonGroup();//按钮控制组
		left.add(mbg.add(newMail));
		left.add(mbg.add(newQQ));
		left.add(mbg.add(newWeibo));
		layoutMgr.addComToModel(left);
		
		JPanel center = new JPanel(new BorderLayout(5,0));
		center.setPreferredSize(new Dimension(450, 35));//450无意义，主要就是设定输入框的高度
		JLabel lbTo = new JLabel("To：");
		lbTo.setForeground(ChannelConstants.LIGHT_TEXT_COLOR);
		center.add(lbTo, BorderLayout.WEST);
		if(!this.editable) {
			center.add(lbMsgTo, BorderLayout.CENTER);
		}
		else {
			center.add(txtMsgTo, BorderLayout.CENTER);
		}
		layoutMgr.addComToModel(center,1,1,GridBagConstraints.HORIZONTAL);
		
		layoutMgr.addComToModel(btnCC);//这里如果有其他按钮，可以放入面板(FlowLayout.RIGHT布局)中。
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
		
		lbMsgTo.setHorizontalAlignment(JLabel.LEFT);
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
	 * 设置消息发送地址，即联系人的邮箱地址、QQ账号或微博账号等
	 * @param to
	 */
	public void setMessageTo(String to)
	{
		if(editable) {
			txtMsgTo.setText(to);
		}
		else {
			lbMsgTo.setText(to);
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
		
		public AbstractButton add(AbstractButtonItem item)
		{
			if(item != null) {
				final	JButton button = (JButton)ChannelUtil.createAbstractButton(item);
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

		public void actionPerformed(ActionEvent e)
		{
			System.out.println(e.getActionCommand());
		}
	}	
}
