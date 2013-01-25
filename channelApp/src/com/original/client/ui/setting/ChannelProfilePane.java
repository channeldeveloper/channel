package com.original.client.ui.setting;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;

import com.original.client.EventConstants;
import com.original.client.border.TitleLineBorder;
import com.original.client.layout.ChannelGridBagLayoutManager;
import com.original.client.layout.VerticalGridLayout;
import com.original.client.ui.data.AbstractButtonItem;
import com.original.client.util.ChannelConstants;
import com.original.client.util.ChannelUtil;
import com.original.client.util.IconFactory;
import com.original.client.util.LocationIcon;
import com.original.service.channel.Constants.CHANNEL;
import com.original.widget.OCheckBox;
import com.seaglasslookandfeel.widget.SGButton;
import com.seaglasslookandfeel.widget.SGLabel;
import com.seaglasslookandfeel.widget.SGPanel;

/**
 * 消息Channel个人信息维护面板。可以启动、禁用和删除个人信息(账户)
 * @author WMS
 *
 */
public class ChannelProfilePane extends SGPanel implements ChannelConstants, EventConstants
{
	private CHANNEL channel = null; //消息channel，用于区分类型
	
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
	}
	
	/**
	 * 分析当前消息Channel的类型，设置相应的标题信息等
	 */
	private void analyzeChannelType() {
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
	 * 添加鼠标监听事件，这里目前为addIcon添加控制事件
	 */
	private void installMouseListeners() {
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(addIcon.contains(e.getPoint())) {
					addProfileBody();
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
	 * 添加个人账户
	 */
	public void addProfileBody() {
		Body body = new Body();
		add(body);
	}
	
	/**
	 * 删除个人账户
	 * @param body 账户面板
	 */
	private void removeProfileBody(Body body) {
		
	}
	
	public class Body extends SGPanel implements ItemListener
	{
		private ChannelGridBagLayoutManager layoutMgr = 
				new ChannelGridBagLayoutManager(this);
		
		private OCheckBox profCb = new OCheckBox();
		private SGLabel profName = new SGLabel("channel"),
				profStatus = new SGLabel("禁用"),
				profAccount = new SGLabel("channeldeveloper@gmail.com");
		
		private ImageIcon delIcon = IconFactory.loadIconByConfig("fileDelIcon");
		private SGButton profDelButton = ChannelUtil.createAbstractButton(
				new AbstractButtonItem(null, DEL_PROFILE, delIcon));
		
		public Body() {
			layoutMgr.setAnchor(GridBagConstraints.WEST); //靠右对齐，主要针对标签
			layoutMgr.setInsets(new Insets(0, 5, 10, 1));//1px偏移调整
			
			this.createProfileBody();
		}
		
		//创建个人账户面板
		private void createProfileBody() {
			layoutMgr.addComToModel(profCb);
			layoutMgr.addComToModel(profName, 1, 1, GridBagConstraints.HORIZONTAL);
			layoutMgr.addComToModel(profStatus);
			layoutMgr.addComToModel(profDelButton);
			layoutMgr.newLine(1);
			layoutMgr.addComToModel(profAccount);
			
			profAccount.setForeground(LIGHT_TEXT_COLOR);
			profStatus.setForeground(Color.gray);
			profCb.addItemListener(this);
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
	}
}
