package com.original.client.ui.setting;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.original.channel.ChannelNativeCache;
import com.original.client.EventConstants;
import com.original.client.border.TitleLineBorder;
import com.original.client.layout.VerticalGridLayout;
import com.original.client.ui.ChannelDesktopPane;
import com.original.client.util.ChannelConstants;
import com.original.client.util.ChannelUtil;
import com.original.client.util.GraphicsHandler;
import com.original.client.util.IconFactory;
import com.original.client.util.LocationIcon;
import com.original.service.channel.Constants.CHANNEL;
import com.seaglasslookandfeel.widget.SGPanel;

public class ChannelSettingPane extends SGPanel implements ChannelConstants, EventConstants
{
	//测试用关闭按钮：
	private LocationIcon closeIcon = new LocationIcon(IconFactory.loadIconByConfig("closeIcon"));
	
	private ChannelProfilePane mailProfile = new ChannelProfilePane(CHANNEL.MAIL),
			qqProfile = new ChannelProfilePane(CHANNEL.QQ),
			weiboProfile = new ChannelProfilePane(CHANNEL.WEIBO);
	
	public ChannelSettingPane() {
		setPreferredSize(new Dimension(SETTINGPANEWIDTH, DESKTOPHEIGHT));
		setLayout(new VerticalGridLayout(VerticalGridLayout.TOP_TO_BOTTOM, 0, 0, new Insets(60, 40, 0, 40)));
		setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(20, 40, 20, 40), 
				new TitleLineBorder("渠道设置", null, closeIcon, LIGHT_TEXT_COLOR, Color.BLACK, TitleLineBorder.TOP, 2)));
		
		this.installMouseListeners();//添加鼠标监听事件
		this.installComponents();//添加初始控件
	}
	
	private void installMouseListeners() {
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(closeIcon.contains(e.getPoint())) {
					ChannelDesktopPane desktop = ChannelNativeCache.getDesktop();
					desktop.removeShowComp(PREFIX_SETTING);
				}
			}
		});
		
		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				if (closeIcon.contains(e.getPoint())) {
					setCursor(HAND_CURSOR);
				} else {
					setCursor(DEFAULT_CURSOR);
				}
			}
		});
	}
	
	private void installComponents() {
		SGPanel mainPane = new SGPanel(new VerticalGridLayout(
				VerticalGridLayout.TOP_TO_BOTTOM, 0, 10, new Insets(0, 0, 0, 0)));
		
		mainPane.add(mailProfile);
		mainPane.add(qqProfile);
		mainPane.add(weiboProfile);
		
		JScrollPane jsp  = ChannelUtil.createScrollPane(mainPane, Color.gray);
		jsp.setPreferredSize(new Dimension(SETTINGPANEWIDTH, DESKTOPHEIGHT-60));
		add(jsp);
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
