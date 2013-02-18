package com.original.client.ui.setting;

import java.awt.Color;
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
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.original.client.EventConstants;
import com.original.client.layout.ChannelGridBagLayoutManager;
import com.original.client.ui.data.AbstractButtonItem;
import com.original.client.util.ChannelConstants;
import com.original.client.util.ChannelUtil;
import com.original.service.channel.Account;
import com.original.widget.OButton;
import com.original.widget.OPasswordField;
import com.original.widget.ORadioButton;
import com.original.widget.OTextField;

/**
 * 消息Channel设置 - 账户维护面板
 * @author WMS
 *
 */
public class ChannelAccountPane extends JPanel implements ActionListener, ItemListener, ChannelConstants, EventConstants
{
	CHANNEL channel = null;//消息channel
	ChannelGridBagLayoutManager layoutMgr =
new ChannelGridBagLayoutManager(this);
	
	//一些控件：
	private JTextField txtName = new OTextField(),
			txtAccount = new OTextField(),
			txtPassword = new OPasswordField();
	
	//用于邮件收、发服务器设置：
	private JTextField txtRecvServer = new OTextField(),
			txtSendServer = new OTextField(),
			txtRecvPort = new OTextField(),
			txtSendPort = new OTextField();
	private JLabel lbRecvServer  = new JLabel(),
			lbSendServer  = new JLabel();
	
	private ORadioButton rbPop3 = new ORadioButton("POP3"),
			rbImap= new ORadioButton("IMAP"),
			rbSmtp = new ORadioButton("SMTP");
	
	private OButton btnOK = ChannelUtil.createApplicationButton(new AbstractButtonItem("确定", CONFIRM, null)),
			btnCancel = ChannelUtil.createApplicationButton(new AbstractButtonItem("取消", CANCEL, null));
	
	private ChannelProfilePane profile = null; //主面板
	
	public ChannelAccountPane(CHANNEL channel)
	{
		this.channel = channel;
		this.layoutMgr.setAnchor(GridBagConstraints.WEST);
		this.layoutMgr.setInsets(new Insets(5, 0, 5, 10));
		
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
		JLabel lbAccount = createTitle("账户信息");
		layoutMgr.addComToModel(lbAccount, 6, 1, GridBagConstraints.NONE);
		layoutMgr.newLine();
		
		layoutMgr.addFillerToModel(30, 0);//创建一个水平填充区域
		JLabel lbUserName = new JLabel("用  户  名：");
		layoutMgr.addComToModel(lbUserName);
		txtName.setColumns(24);
		ChannelUtil.addToolTip(txtName, "可输入任意的名称，不可为空！");
		layoutMgr.addComToModel(txtName, 4, 1, GridBagConstraints.HORIZONTAL);
		layoutMgr.newLine();
		
		JLabel lbLoginAccount = new JLabel("登录账户：");
		layoutMgr.addFillerToModel(30, 0);
		layoutMgr.addComToModel(lbLoginAccount);
		txtAccount.setColumns(24);
		ChannelUtil.addToolTip(txtAccount, channel==CHANNEL.QQ ? 
				"您登录的QQ账号，注意不支持手机和邮件地址！" : 
				"您登录时的账户，如微博账号、邮箱地址等");
		layoutMgr.addComToModel(txtAccount, 4, 1, GridBagConstraints.HORIZONTAL);
		layoutMgr.newLine();
		
		JLabel lbLoginPassword = new JLabel("登录密码：");
		layoutMgr.addFillerToModel(30, 0);
		layoutMgr.addComToModel(lbLoginPassword);
		txtPassword.setColumns(24);
		ChannelUtil.addToolTip(txtPassword, "登陆账户对应的密码，输入时请注意大小写键有没有打开！");
		layoutMgr.addComToModel(txtPassword, 4, 1, GridBagConstraints.HORIZONTAL);
		
		//下面是服务器信息，只对邮件有效：
		if (channel == CHANNEL.MAIL) {
			layoutMgr.newLine();
			JLabel lbServerInfo = createTitle("服务器信息");
			layoutMgr.addComToModel(lbServerInfo, 6, 1, GridBagConstraints.NONE);
			layoutMgr.newLine();

			layoutMgr.addFillerToModel(30, 0);
			layoutMgr.addComToModel(lbRecvServer);
			lbRecvServer.setText("POP3服务器：");
			layoutMgr.addComToModel(rbPop3);
			rbPop3.setSelected(true);
			rbPop3.addItemListener(this);
			layoutMgr.addComToModel(rbImap);
			rbImap.addItemListener(this);
			layoutMgr.addFillerToModel(50, 0); //空白填充
			layoutMgr.addFillerToModel(100, 0); //空白填充
			layoutMgr.newLine(1);
			ButtonGroup recvServerBtnGroup = new ButtonGroup(); //按钮组
			recvServerBtnGroup.add(rbPop3);
			recvServerBtnGroup.add(rbImap);
			layoutMgr.addComToModel(txtRecvServer, 3, 1, GridBagConstraints.HORIZONTAL);
			layoutMgr.addComToModel(new JLabel("端口："));
			layoutMgr.addComToModel(txtRecvPort, 1, 1, GridBagConstraints.HORIZONTAL);
			txtRecvPort.setInputVerifier(new IntegerVerifier());
			ChannelUtil.addToolTip(txtRecvPort, "邮件接受服务器的端口号，整数值！");
			layoutMgr.newLine();

			layoutMgr.addFillerToModel(30, 0);
			layoutMgr.addComToModel(lbSendServer);
			lbSendServer.setText("SMTP服务器：");
			layoutMgr.newLine(1);
			layoutMgr.addComToModel(txtSendServer, 3, 1, GridBagConstraints.HORIZONTAL);
			layoutMgr.addComToModel(new JLabel("端口："));
			layoutMgr.addComToModel(txtSendPort, 1, 1, GridBagConstraints.HORIZONTAL);
			txtSendPort.setInputVerifier(new IntegerVerifier());
			ChannelUtil.addToolTip(txtSendPort, "邮件发送服务器的端口号，整数值！");
			layoutMgr.newLine(4);
		} else {
			layoutMgr.newLine(2);
		}
		
		JPanel ctrlPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		ctrlPane.add(btnOK);
		ctrlPane.add(btnCancel);
		btnOK.setFont(DEFAULT_FONT);
		btnOK.addActionListener(this);
		
		btnCancel.setFont(DEFAULT_FONT);
		btnCancel.addActionListener(this);
		layoutMgr.addComToModel(ctrlPane, 3, 1, GridBagConstraints.HORIZONTAL);
		layoutMgr.newLine();
	}
	
	//创建标题
	private JLabel createTitle(String titleText) {
		JLabel lbTitle = new JLabel(titleText);
		lbTitle.setFont(DEFAULT_FONT.deriveFont(Font.BOLD).deriveFont(16F));
		lbTitle.setForeground(TITLE_COLOR);
		return lbTitle;
	}
	
	private Account createAccount() {
		Account acc = new Account();
		acc.setName(txtName.getText());
		acc.setUser(txtAccount.getText());
		acc.setPassword(txtPassword.getText());
		switch(channel) {
		case MAIL:
			acc.setChannelName("email_"); //暂时先这么设定，交给ChannelSettingPane#checkProfileAccount检查时补充。
			
			//设置收、发服务器及其端口：
			acc.setRecvServer(txtRecvServer.getText());
			acc.setRecvPort(txtRecvPort.getText());
			acc.setSendServer(txtSendServer.getText());
			acc.setSendPort(txtSendPort.getText());
			acc.setRecvProtocol("POP3");//目前都设置为POP3
			acc.setSendProtocol("SMTP");
			break;
		case QQ:
			acc.setChannelName("im_qq");
			break;
		case WEIBO:
			acc.setChannelName("sns_weibo");
			break;
		}
		
		return acc;
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
		if (e.getActionCommand() == CANCEL) {
			ChannelSettingPane csp = null;
			if (profile != null && (csp = profile.getSettingPane()) != null) {
				csp.removeComponent(channel.name());
			}
		} else if (e.getActionCommand() == CONFIRM) {
			Account acc = createAccount();
			if(profile != null) {
				if(profile.addProfileBody(acc))
					btnCancel.doClick();
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			if (e.getSource() == rbPop3) {
				lbRecvServer.setText("POP3服务器：");
			} else if (e.getSource() == rbImap) {
				lbRecvServer.setText("IMAP服务器：");
			}
		}
	}	
	
	class IntegerVerifier extends InputVerifier
	{
		@Override
		public boolean verify(JComponent input) {
			// TODO 自动生成的方法存根
			String value = ((JTextField)input).getText();
			return value.isEmpty() || value.matches("\\d+");
		}

		@Override
		public boolean shouldYieldFocus(JComponent input) {
			// TODO 自动生成的方法存根
			boolean isValid = verify(input);
			if(!isValid) { //下面注意顺序：
				input.requestFocus();
				ChannelUtil.showToolTip(input, null, Color.red);
			}
			return isValid;
		}
	}
}
