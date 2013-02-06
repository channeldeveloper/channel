package com.original.client.ui.setting;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.original.channel.ChannelAccesser;
import com.original.client.EventConstants;
import com.original.client.border.TitleLineBorder;
import com.original.client.layout.VerticalGridLayout;
import com.original.client.util.ChannelConstants;
import com.original.client.util.GraphicsHandler;
import com.original.client.util.Utilities;
import com.original.service.channel.Account;
import com.original.service.channel.core.ChannelService;

public class ChannelSettingPane extends JPanel implements ChannelConstants, EventConstants
{
	//测试用关闭按钮：
//	private LocationIcon closeIcon = new LocationIcon(IconFactory.loadIconByConfig("closeIcon"));
	
	private ChannelProfilePane mailProfile = new ChannelProfilePane(CHANNEL.MAIL),
			qqProfile = new ChannelProfilePane(CHANNEL.QQ),
			weiboProfile = new ChannelProfilePane(CHANNEL.WEIBO);
	
	private CardLayout cardLayMgr = new CardLayout();
	
	public ChannelSettingPane() {
		setPreferredSize(new Dimension(SETTINGPANEWIDTH, DESKTOPHEIGHT));
//		setLayout(new VerticalGridLayout(VerticalGridLayout.TOP_TO_BOTTOM, 0, 0, new Insets(60, 40, 0, 40)));
		setLayout(cardLayMgr);
		setFont(DEFAULT_FONT);
		setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(20, 40, 20, 40), 
				new TitleLineBorder("渠道设置", null, null, LIGHT_TEXT_COLOR, Color.BLACK, TitleLineBorder.TOP, 2)));
		
		this.installMouseListeners();//添加鼠标监听事件
		this.installComponents();//添加初始控件
	}
	
	private void installMouseListeners() {
//		this.addMouseListener(new MouseAdapter() {
//			public void mouseClicked(MouseEvent e) {
//				if(closeIcon.contains(e.getPoint())) {
//					
//				}
//			}
//		});
//		
//		this.addMouseMotionListener(new MouseMotionAdapter() {
//			public void mouseMoved(MouseEvent e) {
//				if (closeIcon.contains(e.getPoint())) {
//					setCursor(HAND_CURSOR);
//				} else {
//					setCursor(DEFAULT_CURSOR);
//				}
//			}
//		});
	}
	
	private void installComponents() {
		JPanel mainPane = new JPanel();
		mainPane.setLayout(new VerticalGridLayout(
				VerticalGridLayout.TOP_TO_BOTTOM, 0, 10, new Insets(0, 0, 0, 0)));
		mainPane.setOpaque(false);
		
		mainPane.add(mailProfile);
		mainPane.add(qqProfile);
		mainPane.add(weiboProfile);
		
		mailProfile.setSettingPane(this);
		qqProfile.setSettingPane(this);
		weiboProfile.setSettingPane(this);
		
		JScrollPane jsp  = Utilities.createScrollPane(mainPane, Color.gray);
		jsp.setPreferredSize(new Dimension(SETTINGPANEWIDTH, DESKTOPHEIGHT-60));
		jsp.setName("DEFAULT");
		add("DEFAULT", jsp);
		
		cardLayMgr.show(this, "DEFAULT");
	}
	
	public void addComponent(String name, Component c)
	{
		JPanel otherPane = new JPanel();
		otherPane.setLayout(new VerticalGridLayout(
				VerticalGridLayout.TOP_TO_BOTTOM, 0, 0, new Insets(0, 0, 0, 0)));
		otherPane.setOpaque(false);
		otherPane.add(c);
		
		JScrollPane jsp  = Utilities.createScrollPane(otherPane, Color.gray);
		jsp.setPreferredSize(new Dimension(SETTINGPANEWIDTH, DESKTOPHEIGHT-60));
		jsp.setName(name);
		add(name, jsp);
		
		cardLayMgr.show(this, name);
	}

	public void removeComponent(String name)
	{
		int  compIndex =  findComponent(name);
		if(compIndex == -1)
			return;
		
		remove(compIndex);
		cardLayMgr.show(this, "DEFAULT");
	}

	public int findComponent(String name) {
		for (int i = 0; i < this.getComponentCount(); i++) {
			Component c = this.getComponent(i);
			if (name != null && name.equals(c.getName())) {
				return i;
			}
		}
		return -1;
	}
	
	public void initProfileAccount() {
		// 读取数据库profile - > accounts
		List<Account> account = ChannelAccesser.getAccounts();
		int accountSize = 0;
		if (account != null && (accountSize = account.size()) > 0) {
			for (int i = 0; i < accountSize; i++) {
				Account acc = account.get(i);
				CHANNEL channel = analyzeAccountChannel(acc);
				if(channel == null) continue;
				switch (channel) {
				case MAIL:
					mailProfile.initProfileBody(acc);
					break;
				case WEIBO:
					weiboProfile.initProfileBody(acc);
					break;
				case QQ:
					qqProfile.initProfileBody(acc);
					break;
				}
			}
		}
	}
	
	//添加个人账户，由于这里没有界面添加，就直接保存数据库
	public boolean addProfileAccount(Account acc) throws Exception{
		checkProfileAccount(acc);
		
		//检查通过后，再次检查数据库是否有重复账户：
		ChannelService cs = ChannelAccesser.getChannelService();
		if(cs.hasAccountInProfile(acc))
			throw new Exception("此账号已存在~");
		
		cs.pushAccountToProfile(acc);
		if(CHANNEL.MAIL == analyzeAccountChannel(acc)) {//保存channel
			cs.addChannelFromAccount(acc);
		}
		
		return true;
	}
	
	public boolean removeProfileAccount(Account acc) throws Exception{
		ChannelService cs = ChannelAccesser.getChannelService();
		cs.popAccountFromProfile(acc);
		
		cs.delChannelByAccount(acc);
		return true;
	}
	
	public boolean checkProfileAccount(Account acc) throws Exception {
		if (acc == null || Utilities.isEmpty(acc.getName(), true))
			throw new IllegalArgumentException("用户名不能为空，请检查！");

		if (Utilities.isEmpty(acc.getUser(), true))
			throw new IllegalArgumentException("登录账户不能为空！");

		if (Utilities.isEmpty(acc.getChannelName()))
			throw new IllegalArgumentException("未知账户类型~");

		CHANNEL channel = analyzeAccountChannel(acc);
		if (channel == CHANNEL.MAIL) {// 如果是邮件，还要检查一下收、发服务器及其端口有没有设置。
			String loginAccount = acc.getUser();
			if (!loginAccount.contains("@"))
				throw new IllegalArgumentException("非法的邮件地址~");
			else {
				acc.setDomain(loginAccount.substring(loginAccount.indexOf("@") + 1));
				String vendor = acc.getDomain();
				if (vendor.indexOf(".") != -1) {
					vendor = vendor.substring(0, vendor.indexOf("."));
				}
				acc.setVendor(vendor);
				acc.setChannelName("email_" + vendor);
			}
			
			if (Utilities.isEmpty(acc.getRecvServer()))
				throw new IllegalArgumentException("未设置邮件接受服务器(POP3\\IMAP)地址~");
			if (Utilities.isEmpty(acc.getRecvPort()))
				throw new IllegalArgumentException("未设置邮件接受服务器(POP3\\IMAP)端口号~");

			if (Utilities.isEmpty(acc.getSendServer()))
				throw new IllegalArgumentException("未设置邮件发送服务器(SMTP)地址~");
			if (Utilities.isEmpty(acc.getSendPort()))
				throw new IllegalArgumentException("未设置邮件发送服务器(SMTP)端口号~");
		}
		
		//其他待添加：
		return true;
	}
	
	private CHANNEL analyzeAccountChannel(Account acc) {
		String channelName = null;
		if (acc != null && (channelName = acc.getChannelName()) != null) {
			if (channelName.startsWith("email_"))
				return CHANNEL.MAIL;
			else if (channelName.equals("sns_weibo"))
				return CHANNEL.WEIBO;
			else if (channelName.equals("im_qq"))
				return CHANNEL.QQ;
		}
		return null;
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = GraphicsHandler.optimizeGraphics(g);
		int width = getWidth(), height = getHeight();
		//填充背景
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, width, height);
	}

}
