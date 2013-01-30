package com.original.client.ui.setting;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.original.client.EventConstants;
import com.original.client.layout.ChannelGridBagLayoutManager;
import com.original.client.ui.data.AbstractButtonItem;
import com.original.client.util.ChannelConstants;
import com.original.client.util.Utilities;
import com.original.widget.OButton;
import com.original.widget.OCheckBox;
import com.original.widget.OComboBox;
import com.original.widget.ORadioButton;

/**
 * 消息Channel设置 - 策略维护面板
 * @author WMS
 *
 */
public class ChannelStrategyPane extends JPanel implements ActionListener, ItemListener, ChannelConstants, EventConstants
{
	CHANNEL channel = null;//消息channel
	ChannelGridBagLayoutManager layoutMgr =
new ChannelGridBagLayoutManager(this);
	
	//用于邮件收、发服务器设置(注意，可以编辑！)：
	private OComboBox comMaxVolumn = new OComboBox(new Object[]{"1","2","5","10","20","30","40","50"}),
			comMaxAttachSize = new OComboBox(new Object[]{"1","2","5","10","20","30","40","50"});
	
	private ORadioButton rbSignEnabled = new ORadioButton("启用"),//签名
			rbSignDisabled= new ORadioButton("禁用"),
			rbReceiptEnabled = new ORadioButton("启动"), //回执
			rbReceiptDisabled = new ORadioButton("禁用");
	
	private OButton btnOK = Utilities.createApplicationButton(new AbstractButtonItem("确定", CONFIRM, null)),
			btnCancel = Utilities.createApplicationButton(new AbstractButtonItem("取消", CANCEL, null));
	
	private ChannelProfilePane profile = null; //主面板
	
	private String[] weeks = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};//星期
	
	public ChannelStrategyPane(CHANNEL channel)
	{
		this.channel = channel;
		this.layoutMgr.setAnchor(GridBagConstraints.WEST);
		this.layoutMgr.setInsets(new Insets(5, 0, 5, 20));
		
		this.constructAccountPane();
		this.setOpaque(false);
	}
	
	@Override
	public void addNotify() {
		// TODO 自动生成的方法存根
		super.addNotify();
		btnOK.requestFocus();
	}

	/**
	 * 构建账户维护面板
	 */
	private void constructAccountPane() {
		JLabel lbWritingSetting = createTitle("写信设置");
		layoutMgr.addComToModel(lbWritingSetting, 8, 1, GridBagConstraints.NONE);
		layoutMgr.newLine();
		
		layoutMgr.addFillerToModel(30, 0);//创建一个水平填充区域
		JLabel lbSign = new JLabel("发信时显示签名：");
		layoutMgr.addComToModel(lbSign,2,1,GridBagConstraints.HORIZONTAL);
		layoutMgr.addComToModel(rbSignEnabled);
		layoutMgr.addComToModel(rbSignDisabled);
		rbSignDisabled.setSelected(true);
		ButtonGroup signBtnGroup = new ButtonGroup();
		signBtnGroup.add(rbSignEnabled);
		signBtnGroup.add(rbSignDisabled);
		layoutMgr.newLine();
		
		layoutMgr.addFillerToModel(30, 0);//创建一个水平填充区域
		JLabel lbReceipt = new JLabel("要求已读回执：");
		layoutMgr.addComToModel(lbReceipt,2,1,GridBagConstraints.HORIZONTAL);
		layoutMgr.addComToModel(rbReceiptEnabled);
		layoutMgr.addComToModel(rbReceiptDisabled);
		rbReceiptDisabled.setSelected(true);
		ButtonGroup receiptBtnGroup = new ButtonGroup();
		receiptBtnGroup.add(rbReceiptEnabled);
		receiptBtnGroup.add(rbReceiptDisabled);
		layoutMgr.newLine();
		layoutMgr.addHorizonLineToModel(1, LIGHT_TEXT_COLOR, 8);//水平线
		
		
		JLabel lbVolumnSetting = createTitle("容量设置");
		layoutMgr.addComToModel(lbVolumnSetting, 8, 1, GridBagConstraints.NONE);
		layoutMgr.newLine();
		
		layoutMgr.addFillerToModel(30, 0);//创建一个水平填充区域
		JLabel lbMaxVolumn = new JLabel("最大容量：");
		layoutMgr.addComToModel(lbMaxVolumn,2,1,GridBagConstraints.HORIZONTAL);
		JPanel maxVolumnPane = new JPanel(new BorderLayout(10,0));
		maxVolumnPane.add(comMaxVolumn, BorderLayout.CENTER);
		comMaxVolumn.setRenderer(Utilities.getAliginCellRenderer(JLabel.RIGHT));
		maxVolumnPane.add(new JLabel("MB"), BorderLayout.EAST);
		layoutMgr.addComToModel(maxVolumnPane,2,1,GridBagConstraints.HORIZONTAL);
		layoutMgr.newLine();
		layoutMgr.addHorizonLineToModel(1, LIGHT_TEXT_COLOR, 8);//水平线
		
		JLabel lbAttachSetting = createTitle("附件设置");
		layoutMgr.addComToModel(lbAttachSetting, 8, 1, GridBagConstraints.NONE);
		layoutMgr.newLine();
		
		layoutMgr.addFillerToModel(30, 0);//创建一个水平填充区域
		JLabel lbMaxAttachVolumn = new JLabel("单个邮件最大附件：");
		layoutMgr.addComToModel(lbMaxAttachVolumn,2,1,GridBagConstraints.HORIZONTAL);
		JPanel maxAttachVolumnPane = new JPanel(new BorderLayout(10,0));
		maxAttachVolumnPane.add(comMaxAttachSize, BorderLayout.CENTER);
		comMaxAttachSize.setRenderer(Utilities.getAliginCellRenderer(JLabel.RIGHT));
		maxAttachVolumnPane.add(new JLabel("MB"), BorderLayout.EAST);
		layoutMgr.addComToModel(maxAttachVolumnPane,2,1,GridBagConstraints.HORIZONTAL);
		layoutMgr.newLine();
		layoutMgr.addHorizonLineToModel(1, LIGHT_TEXT_COLOR, 8);//水平线
		
		JLabel lbWeekSetting = createTitle("禁用时间设置");
		layoutMgr.addComToModel(lbWeekSetting, 8, 1, GridBagConstraints.NONE);
		layoutMgr.newLine();
		
		layoutMgr.addFillerToModel(30, 0);//创建一个水平填充区域
		for(int i=1; i<=7; i++) {//添加星期复选框
			OCheckBox cbWeek = new OCheckBox(weeks[i-1]);
			cbWeek.setName("week"+i);
			layoutMgr.addComToModel(cbWeek);
		}
		layoutMgr.newLine();
		layoutMgr.addHorizonLineToModel(1,LIGHT_TEXT_COLOR, 8, 3);//水平线
		
		JPanel ctrlPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		ctrlPane.add(btnOK);
		ctrlPane.add(btnCancel);
		btnOK.setFont(DEFAULT_FONT);
		btnOK.addActionListener(this);
		
		btnCancel.setFont(DEFAULT_FONT);
		btnCancel.addActionListener(this);
		layoutMgr.addComToModel(ctrlPane, 5, 1, GridBagConstraints.HORIZONTAL);
		layoutMgr.newLine();
	}
	
	//创建标题
	private JLabel createTitle(String titleText) {
		JLabel lbTitle = new JLabel(titleText);
		lbTitle.setFont(DEFAULT_FONT.deriveFont(Font.BOLD).deriveFont(16F));
		lbTitle.setForeground(TITLE_COLOR);
		return lbTitle;
	}
	
	//设置个人信息主面板
	public void setProfilePane(ChannelProfilePane profile) {
		this.profile = profile;
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

	//保存和取消事件控制
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自动生成的方法存根
		if(e.getActionCommand()== CANCEL) {
			ChannelSettingPane csp = null;
			if(profile != null && (csp = profile.getSettingPane()) != null) {
				csp.removeComponent(channel.name());
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO 自动生成的方法存根
		
	}	
	
}
