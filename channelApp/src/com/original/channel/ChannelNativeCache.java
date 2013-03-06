package com.original.channel;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import com.original.client.ui.ChannelDesktopPane;
import com.original.client.ui.ChannelToolBar;

/**
 * 由于集成，需要把ChannelGUI extends JFrame，改造为extends JInternalFrame。
 * 因此
 * 1\主窗口只需启动的代码即可。目前仅仅转移static方法，后面再深入重构造。
 * 2\其他程序使用主窗口需要通过接口，最好没有引用关系。
 * 
 * @author sxy
 * 
 */
public class ChannelNativeCache {

	private static Map<String, JComponent> cache = new HashMap<String, JComponent>();

	/**
	 * 获取一些本地缓存对象：
	 * 1、获取工具栏上的用户头像
	 * @return
	 */
	public static ChannelToolBar.ChannelUserHeadLabel getUserHeadLabel() {
		return (ChannelToolBar.ChannelUserHeadLabel) cache
				.get("ChannelUserHeadLabel");
	}

	/**
	 * 2、获取工具栏
	 * @return
	 */
	public static ChannelToolBar getToolBar() {
		return (ChannelToolBar) cache.get("ChannelToolBar");
	}

	/**
	 * 获取桌面
	 * @return
	 */
	public static ChannelDesktopPane getDesktop() {
		return (ChannelDesktopPane) cache.get("ChannelDesktopPane");
	}

	/**
	 * 设置一些本地缓存对象：
	 * 1、设置工具栏上的用户头像
	 * @param comp 用户头像控件
	 */
	public static void setUserHeadLabel(JComponent comp) {
		cache.put("ChannelUserHeadLabel", comp);
	}

	/**
	 * 设置工具栏
	 * @param comp 工具栏控件
	 */
	public static void setToolBar(JComponent comp) {
		cache.put("ChannelToolBar", comp);
	}

	/**
	 * 设置桌面
	 * @param comp 桌面控件
	 */
	public static void setDesktop(JComponent comp) {
		cache.put("ChannelDesktopPane", comp);
	}

}
