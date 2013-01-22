package com.original.channel;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.original.serive.channel.server.ChannelAccesser;
import com.original.serive.channel.ui.ChannelDesktopPane;
import com.original.serive.channel.ui.ChannelToolBar;
import com.original.serive.channel.util.ChannelConfig;
import com.original.serive.channel.util.ChannelConstants;
import com.original.serive.channel.util.ChannelUtil;
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
public class ChannelIFrame extends JInternalFrame implements ChannelConstants
{	
	
	public ChannelIFrame()
	{
		super();
		setSize(CHANNELWIDTH, CHANNELHEIGHT - STATUSBARHEIGHT);
//		setDefaultCloseOperation(EXIT_ON_CLOSE);
//		setLocationByPlatform(true);
//		setLocation(MARGIN_LEFT, MARGIN_TOP - TOOLBARHEIGHT);
		
//		setUndecorated(true);
//		setDefaultLookAndFeelDecorated(true);
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
	
	
	//程序执行入口
	private void init() throws Exception
	{
		try {
			// 不使用全局的风格，但是单独一些组件可以使用风格,如果使用风，需要初始化一下。
			SeaGlassLookAndFeel sglaf = SeaGlassLookAndFeel.getInstance();
			SeaGlassLookAndFeel.setDefaultFont(DEFAULT_FONT);
			sglaf.initialize();

			//这里可能会影响全局
			UIManager.put("ScrollBar.width", 10); // 滚动条默认宽度
						
		} catch (Exception exp) {
			exp.printStackTrace();
		}
				
		//1. Data 和 View 要分开
		//2. 服务 和 应用(控制) 要分开
		//3. 服务控制自服务，不由第3方应用外部控制，
		//4. 服务不能启动，不影响存库数据(ChannelMessage)访问
		
		final ChannelService cs = ChannelAccesser.getChannelService();
		
		//渠道服务的控制内部控制。
//		while(!cs.isStartupAll()) {
			try {
				cs.restartService();
			}
			catch(Exception ex) {
				ex.printStackTrace();
				
				if(ex instanceof ChannelException) {
					final ChannelAccount ca = ((ChannelException) ex).getChannelAccount();
					Window topWindow = SwingUtilities.getWindowAncestor(this);
					switch(((ChannelException) ex).getChannel())
					{
					case WEIBO: //如果出现需要微博授权的提示错误
						 ChannelUtil.showAuthorizeWindow(topWindow, ca.getAccount().getUser(), new WindowAdapter()
						{
							public void windowClosing(WindowEvent e) //当用户关闭授权浏览器窗口时，表示跳过此错误
							{
								cs.skipService(ca);
							}
						});
						break;
						
					case QQ: //如果出现QQ登录需要验证码
						int option = JOptionPane.showConfirmDialog(topWindow, ex.getMessage(),
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
//		}
		cs.start();
		
		//使用层面板的方式来布局
		JLayeredPane mp = getLayeredPane();
		//用户头像(第1层)
		ChannelToolBar.ChannelUserHeadLabel userHead = new ChannelToolBar.ChannelUserHeadLabel();		
		ChannelAppCache.setUserHeadLabel(userHead);


		userHead.setBounds(0, 1, ChannelConfig.getIntValue("userHeadWidth"), //1做了1px下移调整
				ChannelConfig.getIntValue("userHeadHeight"));
		mp.add(userHead, JLayeredPane.DRAG_LAYER);
		
		//工具栏部分(第2层)
		ChannelToolBar toolbar = new ChannelToolBar();
		ChannelAppCache.setToolBar(toolbar);

		toolbar.setBounds(0, 0, ChannelToolBar.SIZE.width, ChannelToolBar.SIZE.height);
		mp.add(toolbar, JLayeredPane.POPUP_LAYER);
		
		//桌面(最下层)
		ChannelDesktopPane desktop = new ChannelDesktopPane();
		ChannelAppCache.setDesktop(desktop);

		desktop.setBounds(0,40,ChannelDesktopPane.SIZE.width,
				ChannelDesktopPane.SIZE.height);		
		cs.addMessageListener(desktop);
		toolbar.addMessageChangeListener(desktop);
		
		mp.add(desktop, JLayeredPane.DEFAULT_LAYER);
		setVisible(true);
		
		//开始添加信息：
		MessageManager msm =ChannelAccesser.getMsgManager();
		List<ChannelMessage> msgs = msm.getMessagesByFlags(new String[]{ChannelMessage.FLAG_TRASHED, 
				ChannelMessage.FLAG_DRAFT}, 
				new Integer[]{0, 0});
		for (ChannelMessage m : msgs)
		{			
			desktop.initMessage(m); //注意不要使用addMessage()，用途不一样
		}
	}
	
}
