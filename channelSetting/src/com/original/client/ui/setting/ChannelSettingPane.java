package com.original.client.ui.setting;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.original.client.EventConstants;
import com.original.client.border.TitleLineBorder;
import com.original.client.layout.VerticalGridLayout;
import com.original.client.util.ChannelConstants;
import com.original.client.util.GraphicsHandler;
import com.original.client.util.Utilities;

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
	
	public void addProfile() {
		mailProfile.addProfileBody();
//		qqProfile.addProfileBody();
		weiboProfile.addProfileBody();
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
