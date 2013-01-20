package com.original.channel;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import com.original.serive.channel.server.ChannelAccesser;
import com.original.serive.channel.ui.ChannelDesktopPane;
import com.original.serive.channel.ui.ChannelToolBar;
import com.original.service.channel.core.ChannelService;

/**
 * 由于集成，需要把ChannelGUI extends JFrame，改造为extends JInternalFrame。
 * 因此
 * 1\主窗口只需启动的代码即可。目前仅仅转移static方法，后面再深入重构造。
 * 2\其他程序使用主窗口需要通过接口，最好没有引用关系。
 * 
 * @author sxy
 * 
 */
public class ChannelAppCache {

	private static Map<String, JComponent> channelNativeStore = new HashMap<String, JComponent>();
	/**
	 * 
	 * @return
	 */
	public static ChannelService getChannelService()
	{
		return ChannelAccesser.getChannelService();
	}

	// 获取一些本地缓存对象：
	// 1、获取工具栏上的用户头像
	public static ChannelToolBar.ChannelUserHeadLabel getUserHeadLabel() {

		return (ChannelToolBar.ChannelUserHeadLabel) channelNativeStore
				.get("ChannelUserHeadLabel");
	}

	// 2、获取工具栏
	public static ChannelToolBar getToolBar() {

		return (ChannelToolBar) channelNativeStore.get("ChannelToolBar");
	}

	// 3、获取桌面
	public static ChannelDesktopPane getDesktop() {

		return (ChannelDesktopPane) channelNativeStore
				.get("ChannelDesktopPane");
	}
	
	// 获取一些本地缓存对象：
		// 1、获取工具栏上的用户头像
		public static void setUserHeadLabel(JComponent comp) {

			channelNativeStore.put("ChannelUserHeadLabel", comp);
		}

		// 2、获取工具栏
		public static void setToolBar(JComponent comp) {

			channelNativeStore.put("ChannelToolBar", comp);
			
		}

		// 3、获取桌面
		public static void setDesktop(JComponent comp) {

			channelNativeStore.put("ChannelDesktopPane", comp);
		}

}
