package com.original.serive.channel.ui;

import java.awt.Dimension;
import java.text.SimpleDateFormat;

import javax.swing.JLabel;

import com.original.serive.channel.ChannelGUI;
import com.original.serive.channel.util.ChannelConstants;
import com.original.serive.channel.util.IconFactory;
import com.original.service.channel.ChannelMessage;

/**
 * 显示完整消息顶部栏，可以用于QQ、微博、邮件等消息的完整显示
 * @author WMS
 *
 */
public class ShowMessageTopBar extends ChannelMessageTopBar
{
	private SimpleDateFormat messageFormat = new SimpleDateFormat("MM月dd日 HH:mm");//消息时间格式
	private JLabel messageHeader = new JLabel();
	
	private ChannelMessage channelMsg = null;//当前显示消息对象
	
	public ShowMessageTopBar(ChannelMessage msg)
	{
		super(false);
		channelMsg = msg;
	}

	@Override
	protected void initStatusBar() {
		super.initStatusBar();
		if(channelMsg != null && channelMsg.getMessageID() != null)
		{
			String msgClazz = channelMsg.getClazz();
			if (ChannelMessage.QQ.equals(msgClazz))
			{
				messageHeader.setIcon(IconFactory.loadIconByConfig("defaultQQIcon"));
			} else if (ChannelMessage.WEIBO.equals(msgClazz))
			{
				messageHeader.setIcon(IconFactory.loadIconByConfig("defaultWeiboIcon"));
			} else if (ChannelMessage.MAIL.equals(msgClazz))
			{
				messageHeader.setIcon(IconFactory.loadIconByConfig("defaultMailIcon"));
			}
			
			messageHeader.setForeground(ChannelConstants.LIGHT_TEXT_COLOR);
			messageHeader.setFont(ChannelConstants.DEFAULT_FONT);
			messageHeader.setText(channelMsg.getRecievedDate() == null ? "" : messageFormat.format(channelMsg.getRecievedDate()));
			messageHeader.setIconTextGap(10);
			messageHeader.setHorizontalTextPosition(JLabel.RIGHT);
		}
	}

	@Override
	protected void constructStatusBar() {
		// TODO Auto-generated method stub
		setLayout(null); //空布局，从而精确定位控件
		setPreferredSize(new Dimension(SIZE.width-20, SIZE.height+15));
		
		Dimension dim = messageHeader.getPreferredSize();
		messageHeader.setBounds(10, 10, dim.width, dim.height);
		add(messageHeader);
	}

	@Override
	public void doClose() {
		ChannelDesktopPane desktop = (ChannelDesktopPane)ChannelGUI.channelNativeStore
				.get("ChannelDesktopPane");
		
		if(channelMsg != null) {
			desktop.removeShowComp(PREFIX_SHOW + channelMsg.getContactName(), true);
		}
		else {
			desktop.showDefaultComp();
		}
	}
	
}
