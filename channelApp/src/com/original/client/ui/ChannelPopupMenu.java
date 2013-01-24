package com.original.client.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.border.EmptyBorder;

import com.original.channel.ChannelNativeCache;
import com.original.client.EventConstants;
import com.original.client.border.SingleLineBorder;
import com.original.client.layout.ChannelGridLayout;
import com.original.client.ui.ChannelMessagePane.ContactHeader;
import com.original.client.ui.data.AbstractButtonItem;
import com.original.client.util.ChannelUtil;
import com.original.client.util.GraphicsHandler;
import com.original.client.util.IconFactory;
import com.original.service.channel.ChannelMessage;
import com.original.service.channel.Constants;
import com.seaglasslookandfeel.widget.SGButton;
import com.seaglasslookandfeel.widget.SGPanel;
import com.seaglasslookandfeel.widget.SGPopupMenu;

/**
 * 消息Channel浮窗，用于点击联系人头像时，自动浮出消息的快捷通道菜单。点击任意一个菜单，
 * 主界面Desktop自动切换至该菜单对应的快捷通道(面板)。
 * 目前快捷通道有3个：邮件、QQ、微博。
 * 
 * 考虑到该浮窗会在多处使用，这里单独设计一个通用类。
 * @author WMS
 *
 */
public class ChannelPopupMenu extends SGPopupMenu implements EventConstants
{	
	private SGButton channel4QQ = ChannelUtil.createAbstractButton(
			new AbstractButtonItem(null, CHANNEL_FOR_QQ,
					IconFactory.loadIconByConfig("qqChannelIcon"), new Dimension(40, 40))),
			channel4Weibo = ChannelUtil.createAbstractButton(
					new AbstractButtonItem(null, CHANNEL_FOR_WEIBO, 
							IconFactory.loadIconByConfig("weiboChannelIcon"),new Dimension(40, 40))),
			channel4Mail = ChannelUtil.createAbstractButton(
					new AbstractButtonItem(null, CHANNEL_FOR_MAIL, 
							IconFactory.loadIconByConfig("mailChannelIcon"),new Dimension(40, 40)));
	private ContactHeader contactHeader = null;
	private MenuPane mp = new MenuPane();
	
	public ChannelPopupMenu(ContactHeader header) {
		setBorder(new EmptyBorder(3, 3, 2, 2));
		constructPopupMenu();
		
		this.contactHeader = header;
	}
	
	/**
	 * 构建浮窗的菜单项，其实每个菜单项就是一个按钮
	 */
	private void constructPopupMenu() {	
		channel4QQ.setBorder(new SingleLineBorder(SingleLineBorder.RIGHT,Color.gray));
		mp.add(channel4QQ, new Rectangle(0, 0, 40, 40));
//		addJSeparator();
		channel4Weibo.setBorder(new SingleLineBorder(SingleLineBorder.RIGHT,Color.gray));
		mp.add(channel4Weibo, new Rectangle(40, 0, 40, 40));
//		addJSeparator();
		mp.add(channel4Mail, new Rectangle(80, 0, 40, 40));
		
		add(mp);
	}
	
	@Override
	public void show(Component invoker, int x, int y) {
		//判断可用的快捷通道，不可用的通道变灰(禁用)处理：
		//暂时的处理方法，判断消息类型是哪一种：
		ChannelMessage msg = getMessage();
		if(msg != null) {
			if (msg.isMail()) {
				setEnabled(CHANNEL_FOR_MAIL);
			} else if (msg.isWeibo()) {
				setEnabled(CHANNEL_FOR_WEIBO);
			} else if (msg.isQQ()) {
				setEnabled(CHANNEL_FOR_QQ);
			}
		}
		
		super.show(invoker, x, y);
	}
	
	//暂时的处理方法：
	public void setEnabled(String actionCommand)
	{
		for (int i = 0; i < mp.getComponentCount(); i++) {
			if (actionCommand == CHANNEL_FOR_MAIL
					&& mp.getComponent(i) == channel4Mail) {
				channel4Mail.setEnabled(true);
				continue;
				
			} else if (actionCommand == CHANNEL_FOR_WEIBO
					&& mp.getComponent(i) == channel4Weibo) {
				channel4Weibo.setEnabled(true);
				continue;
				
			} else if (actionCommand == CHANNEL_FOR_QQ
					&& mp.getComponent(i) == channel4QQ) {
				channel4QQ.setEnabled(true);
				continue;
			}
			mp.getComponent(i).setEnabled(false);
		}
	}

	/**
	 * 获取当前弹窗对应的消息主体面板
	 * @return
	 */
	private ChannelMessageBodyPane getMessageBody() {
		if(contactHeader != null) {
			ChannelMessagePane parent = (ChannelMessagePane)contactHeader.getParent();
			return parent.body;
		}
		return null;
	}
	
	/**
	 * 获取当前弹窗对应的消息对象。注意，弹窗对应的消息主体面板不同，获得的消息对象也不同。
	 * @return
	 */
	private ChannelMessage getMessage() {
		ChannelMessageBodyPane body = getMessageBody();
		return body.getChannelMessage();
	}
	
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2d = GraphicsHandler.optimizeGraphics(g);
		int width = getWidth(), height = getHeight();
		
		g2d.setColor(Color.lightGray);
		g2d.fillRoundRect(0, 0, width, height, 10, 10);
		GraphicsHandler.suspendRendering(g2d);
	}
	
	static class MenuPane extends SGPanel implements ActionListener
	{
		Rectangle focusBounds = null; //这里自定义选中的区域(即获得焦点的区域)，模仿菜单被选中的效果
		public MenuPane() {
			setLayout(new ChannelGridLayout(new Insets(0, 0, 0, 0)));
		}
		
		public void add(final Component comp, Rectangle bounds)
		{
			if(comp instanceof AbstractButton) {
				((AbstractButton) comp).addActionListener(this);
				((AbstractButton) comp).addMouseListener(new MouseAdapter()
				{
					public void mouseEntered(MouseEvent e)
					{focusBounds = ((AbstractButton) comp).getBounds();
//					repaint();
					}

					public void mouseExited(MouseEvent e)
					{focusBounds = null;
//					repaint();
					}
				});
			}
			super.add(comp);
		}
		
		//当添加的组件为AbstractButton时，可能会有点击触发事件
		public void actionPerformed(ActionEvent e)
		{
			ChannelPopupMenu popMenu = (ChannelPopupMenu)this.getParent();
			//隐藏弹窗和取消选中的快捷通道
			popMenu.setVisible(false);
			focusBounds = null;
			
			ChannelMessage msg = popMenu.getMessage();
			if(msg != null) {
				ChannelMessage newMsg = msg.simplyClone();
				newMsg.setAction(Constants.ACTION_NEW);
				
				ChannelMessagePane cmp = new ChannelMessagePane(new NewMessageTopBar(false));				
				cmp.newMessage(newMsg); 
				ChannelDesktopPane desktop = ChannelNativeCache.getDesktop();
				desktop.addOtherShowComp(PREFIX_NEW+newMsg.getContactName(),  cmp);
			}
		}

		protected void paintComponent(Graphics g)
		{
			Graphics2D g2d = GraphicsHandler.optimizeGraphics(g);
			int width = getWidth()-1, height = getHeight()-1;
			//阴影效果1px
			GraphicsHandler.fillShadow(g2d, 2, width, height, 8, 0.4f);
			
			//填充背景色
			g2d.setColor(Color.white);
			g2d.fillRoundRect(0, 0, width, height, 8, 8);
			if(focusBounds != null)
			{
				g2d.setColor( new Color(46,156,202));
				g2d.fillRect(focusBounds.x, focusBounds.y, focusBounds.width, focusBounds.height);
			}
			GraphicsHandler.suspendRendering(g2d);
		}
	}
}
