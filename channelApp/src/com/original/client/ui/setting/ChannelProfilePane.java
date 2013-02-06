package com.original.client.ui.setting;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.original.client.EventConstants;
import com.original.client.border.TitleLineBorder;
import com.original.client.layout.ChannelGridBagLayoutManager;
import com.original.client.layout.VerticalGridLayout;
import com.original.client.ui.data.AbstractButtonItem;
import com.original.client.util.ChannelConstants;
import com.original.client.util.IconFactory;
import com.original.client.util.LocationIcon;
import com.original.client.util.Utilities;
import com.original.service.channel.Account;
import com.original.widget.OCheckBox;
import com.original.widget.OTabbedPane;

/**
 * 消息Channel个人信息维护面板。可以启动、禁用和删除个人信息(账户)
 * @author WMS
 *
 */
public class ChannelProfilePane extends JPanel implements ChannelConstants, EventConstants
{
	private CHANNEL channel = null; //消息channel，用于区分类型
	private ChannelSettingPane csp = null;//父面板
	
	//channel类型图标
	private ImageIcon titleIcon4QQ = IconFactory.loadIconByConfig("readQQIcon"),
			titleIcon4Mail = IconFactory.loadIconByConfig("readMailIcon"),
			titleIcon4Weibo = IconFactory.loadIconByConfig("readWeiboIcon");
	
	//增加、删除图标
	private LocationIcon addIcon = new LocationIcon(IconFactory.loadIconByConfig("fileAddIcon"));
	
	public ChannelProfilePane(CHANNEL channel) {
		this.channel = channel;
		
		this.analyzeChannelType(); //分析channel类型
		this.installMouseListeners(); //添加鼠标控制事件
		this.setLayout(new VerticalGridLayout(VerticalGridLayout.TOP_TO_BOTTOM,
				0, 20, new Insets(40, 30, 0, 0)));
		this.setOpaque(false);
	}
	
	/**
	 * 分析当前消息Channel的类型，设置相应的标题信息等
	 */
	private void analyzeChannelType() {
		this.setFont(DEFAULT_FONT);
		switch(channel) {
		case MAIL:
			this.setBorder(new TitleLineBorder("邮件", titleIcon4Mail, addIcon, LIGHT_TEXT_COLOR, TITLE_COLOR));
			break;
			
		case WEIBO:
			this.setBorder(new TitleLineBorder("微博", titleIcon4Weibo, addIcon, LIGHT_TEXT_COLOR, TITLE_COLOR));
			break;
			
		case QQ:
			this.setBorder(new TitleLineBorder("即时通讯", titleIcon4QQ, addIcon, LIGHT_TEXT_COLOR, TITLE_COLOR));
			break;
		}
	}
	
	/**
	 * 设置当前面板的父面板，即设置面板
	 * @param csp
	 */
	public void setSettingPane(ChannelSettingPane csp) {
		this.csp = csp;
	}
	public ChannelSettingPane getSettingPane() {
		return this.csp;
	}
	
	/**
	 * 添加鼠标监听事件，这里目前为addIcon添加控制事件
	 */
	private void installMouseListeners() {
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(addIcon.contains(e.getPoint())) {
					showProfileTabPane();
				}
			}
		});
		
		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				if (addIcon.contains(e.getPoint())) {
					setCursor(HAND_CURSOR);
				} else {
					setCursor(DEFAULT_CURSOR);
				}
			}
		});
	}
	
	/**
	 * 初始化添加个人账户
	 */
	public void initProfileBody(Account acc) {
		Body body = new Body(acc);
		add(body);
		this.validate();
	}
	
	/**
	 * 手动添加个人账户
	 */
	public boolean addProfileBody(Account acc) {
		if(acc != null) {
			
			try { //先数据库添加后，再界面添加
				if (csp != null)
					csp.addProfileAccount(acc);
			} catch (Exception ex) {
				Utilities.showMessageDialog(this, "错误", ex.getMessage());
				return false;
			}
			
			Body body = new Body(acc);
			add(body);
			
			Component comp = this;
			while (true) { //反复刷新界面
				comp.validate();
				comp = comp.getParent();
				if (comp == null || comp instanceof ChannelSettingPane)
					break;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 显示账户添加和策略设置选项面板
	 */
	private void showProfileTabPane() {
		if(this.csp != null) {
			OTabbedPane otp = new OTabbedPane();
			JPanel accountPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
			ChannelAccountPane cap = new ChannelAccountPane(channel);
			cap.setProfilePane(this);
			accountPane.add( cap);
			accountPane.setOpaque(false);
			accountPane.setPreferredSize(new Dimension(SETTINGPANEWIDTH, DESKTOPHEIGHT-125));
			otp.addTab("账户", accountPane, null, false);
			
			if(channel == CHANNEL.MAIL) { //目前设定邮件才有【策略】
				JPanel strategyPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
				ChannelStrategyPane csp = new ChannelStrategyPane(channel);
				csp.setProfilePane(this);
				strategyPane.add( csp);
				strategyPane.setOpaque(false);
				strategyPane.setPreferredSize(new Dimension(SETTINGPANEWIDTH, DESKTOPHEIGHT-125));
				otp.addTab("策略", strategyPane, null, false);
				otp.setSelectedIndex(0);//默认选中账户
			}
			
			this.csp.addComponent(channel.name(), otp);
		}
	}
	
	/**
	 * 删除个人账户
	 * @param body 账户面板
	 */
	private boolean removeProfileBody(Body body) {
if(body != null && body.account != null) {
			
			try { //先数据库添加后，再界面添加
				if (csp != null)
					csp.removeProfileAccount(body.account);
			} catch (Exception ex) {
				Utilities.showMessageDialog(this, "错误", ex.getMessage());
				return false;
			}
			
			this.remove(body);//先删除子界面
			Component comp = this;
			while (true) { //再反复刷新父界面
				comp.validate();
				comp = comp.getParent();
				if (comp == null || comp instanceof ChannelSettingPane)
					break;
			}
			return true;
		}
		return false;
		
	}
	
	public class Body extends JPanel implements ItemListener, ActionListener
	{
		private ChannelGridBagLayoutManager layoutMgr = 
				new ChannelGridBagLayoutManager(this);
		
		private OCheckBox profCb = new OCheckBox();
		private JLabel profName = new JLabel(),
				profStatus = new JLabel("禁用"),
				profAccount = new JLabel();
		
		private ImageIcon delIcon = IconFactory.loadIconByConfig("fileDelIcon");
		private JButton profDelButton = Utilities.createAbstractButton(
				new AbstractButtonItem(null, DEL_PROFILE, delIcon));
		
		Account account = null; //当前账户
		
		public Body(Account account) {
			this.account = account;
			layoutMgr.setAnchor(GridBagConstraints.WEST); //靠右对齐，主要针对标签
			layoutMgr.setInsets(new Insets(0, 5, 10, 1));//1px偏移调整
			
			this.createProfileBody();
			this.setOpaque(false);
		}
		
		//创建个人账户面板
		private void createProfileBody() {
			layoutMgr.addComToModel(profCb);
			layoutMgr.addComToModel(profName, 1, 1, GridBagConstraints.HORIZONTAL);
			layoutMgr.addComToModel(profStatus);
			layoutMgr.addComToModel(profDelButton);
			profDelButton.addActionListener(this);
			layoutMgr.newLine(1);
			layoutMgr.addComToModel(profAccount);
			
			profAccount.setForeground(LIGHT_TEXT_COLOR);
			profStatus.setForeground(Color.gray);
			profCb.addItemListener(this);
			
			if(account != null) {
				profName.setText(account.getName());
				profAccount.setText(account.getUser());
			}
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				profStatus.setText("启用");
				profStatus.setForeground(Color.black);

				// may be update DB
			} else if (e.getStateChange() == ItemEvent.DESELECTED) {
				profStatus.setText("禁用");
				profStatus.setForeground(Color.gray);

				// may be update DB
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == profDelButton)			 {
				if(Utilities.confirm(null, "确认删除", "是否删除此账户？")) {
				removeProfileBody(this);
				}
			}
		}
		
		//统一字体
		@Override
		public void add(Component comp, Object constraints) {
			// TODO 自动生成的方法存根
			comp.setFont(DEFAULT_FONT);
			if(comp instanceof JComponent)
				((JComponent) comp).setOpaque(false);
			super.add(comp, constraints);
		}

	}
}
