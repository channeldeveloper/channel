package com.original.client.ui.setting;

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
import javax.swing.JTextField;

import com.original.client.EventConstants;
import com.original.client.layout.ChannelGridBagLayoutManager;
import com.original.client.ui.data.AbstractButtonItem;
import com.original.client.util.ChannelConstants;
import com.original.client.util.Utilities;
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
			txtSendServer = new OTextField();
	private JLabel lbRecvServer  = new JLabel(),
			lbSendServer  = new JLabel();
	
	private ORadioButton rbPop3 = new ORadioButton("POP3"),
			rbImap= new ORadioButton("IMAP"),
			rbSmtp = new ORadioButton("SMTP");
	
	private OButton btnOK = Utilities.createApplicationButton(new AbstractButtonItem("确定", CONFIRM, null)),
			btnCancel = Utilities.createApplicationButton(new AbstractButtonItem("取消", CANCEL, null));
	
	public ChannelAccountPane(CHANNEL channel)
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
		JLabel lbAccount = createTitle("账户信息");
		layoutMgr.addComToModel(lbAccount, 5, 1, GridBagConstraints.NONE);
		layoutMgr.newLine();
		
		layoutMgr.addFillerToModel(30, 0);//创建一个水平填充区域
		JLabel lbUserName = new JLabel("用  户  名：");
		layoutMgr.addComToModel(lbUserName);
		txtName.setColumns(24);
		Utilities.showToolTip(txtName, "可输入任意的名称，不可为空！");
		layoutMgr.addComToModel(txtName, 3, 1, GridBagConstraints.HORIZONTAL);
		layoutMgr.newLine();
		
		JLabel lbLoginAccount = new JLabel("登录账户：");
		layoutMgr.addFillerToModel(30, 0);
		layoutMgr.addComToModel(lbLoginAccount);
		txtAccount.setColumns(24);
		Utilities.showToolTip(txtAccount, channel==CHANNEL.QQ ? 
				"您登录的QQ账号，注意不支持手机和邮件地址！" : 
				"您登录时的账户，如微博账号、邮箱地址等");
		layoutMgr.addComToModel(txtAccount, 3, 1, GridBagConstraints.HORIZONTAL);
		layoutMgr.newLine();
		
		JLabel lbLoginPassword = new JLabel("登录密码：");
		layoutMgr.addFillerToModel(30, 0);
		layoutMgr.addComToModel(lbLoginPassword);
		txtPassword.setColumns(24);
		Utilities.showToolTip(txtPassword, "登陆账户对应的密码，输入时请注意大小写键有没有打开！");
		layoutMgr.addComToModel(txtPassword, 3, 1, GridBagConstraints.HORIZONTAL);
		
		//下面是服务器信息，只对邮件有效：
		if (channel == CHANNEL.MAIL) {
			layoutMgr.newLine();
			JLabel lbServerInfo = createTitle("服务器信息");
			layoutMgr.addComToModel(lbServerInfo, 5, 1, GridBagConstraints.NONE);
			layoutMgr.newLine();

			layoutMgr.addFillerToModel(30, 0);
			layoutMgr.addComToModel(lbRecvServer);
			lbRecvServer.setText("POP3服务器：");
			layoutMgr.addComToModel(rbPop3);
			rbPop3.setSelected(true);
			rbPop3.addItemListener(this);
			layoutMgr.addComToModel(rbImap);
			rbImap.addItemListener(this);
			layoutMgr.newLine(1);
			ButtonGroup recvServerBtnGroup = new ButtonGroup(); //按钮组
			recvServerBtnGroup.add(rbPop3);
			recvServerBtnGroup.add(rbImap);
			layoutMgr.addComToModel(txtRecvServer, 4, 1, GridBagConstraints.HORIZONTAL);
			layoutMgr.newLine();

			layoutMgr.addFillerToModel(30, 0);
			layoutMgr.addComToModel(lbSendServer);
			lbSendServer.setText("SMTP服务器：");
			layoutMgr.newLine(1);
			layoutMgr.addComToModel(txtSendServer, 4, 1, GridBagConstraints.HORIZONTAL);
			layoutMgr.newLine(2);
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
}
