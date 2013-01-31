package com.original.client;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

import weibo4j.model.WeiboException;

import com.original.channel.ChannelAccesser;
import com.original.channel.ChannelNativeCache;
import com.original.client.ui.ChannelDesktopPane;
import com.original.client.ui.ChannelToolBar;
import com.original.client.util.ChannelConfig;
import com.original.client.util.ChannelConstants;
import com.original.client.util.ChannelUtil;
import com.original.service.channel.ChannelAccount;
import com.original.service.channel.ChannelMessage;
import com.original.service.channel.core.ChannelException;
import com.original.service.channel.core.ChannelService;
import com.original.service.channel.core.MessageManager;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;

/**
 * 消息渠道Channel用户主界面，也是主线程执行的入口。
 * @author wms
 *
 */
public class ChannelGUI extends JFrame implements ChannelConstants
{	
	public ChannelGUI()
	{
		setSize(CHANNELWIDTH, CHANNELHEIGHT - STATUSBARHEIGHT);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationByPlatform(true);
		setLocation(MARGIN_LEFT, MARGIN_TOP - TOOLBARHEIGHT);
		
		setUndecorated(true);
		setDefaultLookAndFeelDecorated(true);
		getRootPane().setWindowDecorationStyle(JRootPane.NONE); //这里用于不显示标题栏，比较特殊(原因SeaGlassLookAndFeel自带标题栏)
		setResizable(false);
		try
		{
			init();
		}
		catch(Exception exp)
		{
			exp.printStackTrace();
		}
	}
	
	//启动服务
	private ChannelService startService() throws Exception 
	{
		//1. Data 和 View 要分开
		//2. 服务 和 应用(控制) 要分开
		//3. 服务控制自服务，不由第3方应用外部控制，
		//4. 服务不能启动，不影响存库数据(ChannelMessage)访问
		final ChannelService cs =  ChannelAccesser.getChannelService();
		
		//渠道服务的控制内部控制。
		//不联网也能启动，因为需要看历史数据
		Window win = SwingUtilities.getWindowAncestor(this);
		while(!cs.isStartupAll()) {
			try {
				cs.restartService();
			}
			catch(Exception ex) {
				ex.printStackTrace();
				
				if(ex instanceof ChannelException) {
					final ChannelAccount ca = ((ChannelException) ex).getChannelAccount();
					
					switch(((ChannelException) ex).getChannel())
					{
					case WEIBO: //如果出现需要微博授权的提示错误
						try {
							ChannelUtil.showAuthorizeWindow(this, ca.getAccount().getUser(), new WindowAdapter() {
								public void windowClosing(WindowEvent e) //当用户关闭授权浏览器窗口时，表示跳过此错误
								{
									cs.skipService(ca);
								}
							});
						} catch (WeiboException we) {//未知错误，可能网络不通
							cs.skipService(ca);
						}
						break;
						
					case QQ: //如果出现QQ登录需要验证码
						int option = JOptionPane.showConfirmDialog(win, ex.getMessage(),
								"是否重试", JOptionPane.YES_NO_OPTION);
						if(option != JOptionPane.YES_OPTION) {//-1:关闭 1:否 0:是
							cs.skipService(ca);
						}
						break;
						
					case MAIL:
						break;
					}
				}
				else {
					cs.skipAllService();
//					break;
				}
			}
		}
		return cs;
	}

	//初始化应用程序，也方便外部程序整合
	private void init() throws Exception
	{
		//开始应用程序：
		try {
			SeaGlassLookAndFeel sglaf = SeaGlassLookAndFeel.getInstance();
			SeaGlassLookAndFeel.setDefaultFont(DEFAULT_FONT);
			sglaf.initialize();
			
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		
		//使用层面板的方式来布局
		JLayeredPane mp = getLayeredPane();
		//用户头像(第1层)
		ChannelToolBar.ChannelUserHeadLabel userHead = new ChannelToolBar.ChannelUserHeadLabel();		
		ChannelNativeCache.setUserHeadLabel(userHead);

		userHead.setBounds(0, 1, ChannelConfig.getIntValue("userHeadWidth"), //1做了1px下移调整
				ChannelConfig.getIntValue("userHeadHeight"));
		mp.add(userHead, JLayeredPane.DRAG_LAYER);
		
		//工具栏部分(第2层)
		ChannelToolBar toolbar = new ChannelToolBar();
		ChannelNativeCache.setToolBar(toolbar);

		toolbar.setBounds(0, 0, ChannelToolBar.SIZE.width, ChannelToolBar.SIZE.height);
		mp.add(toolbar, JLayeredPane.POPUP_LAYER);
		
		//桌面(最下层)
		ChannelDesktopPane desktop = new ChannelDesktopPane();
		ChannelNativeCache.setDesktop(desktop);

		desktop.setBounds(0,40,ChannelDesktopPane.SIZE.width,
				ChannelDesktopPane.SIZE.height);		
		toolbar.addMessageChangeListener(desktop);
		
		mp.add(desktop, JLayeredPane.DEFAULT_LAYER);
		setVisible(true);
		
		ChannelService cs = startService();
				cs.addMessageListener(desktop);
				
//开始添加信息：
		MessageManager msm =cs.getMsgManager();
		List<ChannelMessage> msgs = msm.getMessagesByFlags(new String[]{ChannelMessage.FLAG_TRASHED, 
				ChannelMessage.FLAG_DRAFT}, 
				new Integer[]{0, 0});
		for (ChannelMessage m : msgs)
		{			
			desktop.initMessage(m); //注意不要使用addMessage()，用途不一样
		}
	}
	
//程序执行入口
	public static void main(String[] args) throws Exception
	{
		ChannelGUI main = new ChannelGUI();
		
		
	}
	
}
