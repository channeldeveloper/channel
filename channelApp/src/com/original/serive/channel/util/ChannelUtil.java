package com.original.serive.channel.util;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

import sun.swing.SwingUtilities2;

import weibo4j.http.AccessToken;
import weibo4j.model.WeiboException;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserAdapter;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserListener;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserNavigationEvent;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserWindowFactory;

import com.original.serive.channel.server.ChannelAccesser;
import com.original.serive.channel.ui.data.AbstractButtonItem;
import com.original.serive.channel.ui.data.MenuItem;

/**
 * 一些通用算法
 * @author WMS
 *
 */
public class ChannelUtil
{
	/**
	 * 判断文本是否为空
	 * @param text 文本
	 * @return
	 */
	public static boolean isEmpty(String text)
	{
		return isEmpty(text, false);
	}
	
	/**
	 * 判断文本是否为空，如果文本前后有空格，是否去除后判断
	 * @param text 文本
	 * @param trim 是否去除前后空格
	 * @return
	 */
	public static boolean isEmpty(String text, boolean trim)
	{
		return text == null || (trim? text.trim() : text).isEmpty();
	}
	
	/**
	 * 为文本添加指定长度的空格
	 * @param text 文本
	 * @param fm 字体测量工具
	 * @param blankLength 空格长度
	 * @return
	 */
	public static String appendBlank(String text, FontMetrics fm, int blankLength)
	{
		if(text != null) {
			int blankNum = (int)((double)blankLength/fm.stringWidth(" "));
			for(int i=0;i<blankNum;i++)
				text += " ";
		}
		return text;
	}
	
	/**
	 * 截取指定长度subLength(单位px)的字符串
	 * @param text 字符串
	 * @param fm 字体测量工具
	 * @param cutLength 截取长度
	 * @return
	 */
	public static String cutString(String text, FontMetrics fm, int cutLength)
	{
		if(text != null && fm.stringWidth(text) > cutLength)
		{
			int len = 0;
			for(int i=0 ; i<text.length(); i++)
			{
				len += fm.charWidth(text.charAt(i));
				if(len == cutLength) {
					return text.substring(0, i) + "...";
				}
				else if(len > cutLength) {
					return text.substring(0, i-1) + "...";
				}
			}
		}
		return text;
	}
	public static String cutString(String text, JComponent comp)
	{
		if(comp != null && !isEmpty(text)) {
			FontMetrics fm = SwingUtilities2.getFontMetrics(comp, comp.getFont());
			int cutLength = comp.getPreferredSize().width - fm.stringWidth("...");
			return cutString(text, fm, cutLength);
		}
		return null;
	}
	
	/**
	 * 由按钮项目构建按钮
	 * @param item 按钮项目
	 * @return
	 */
	public static JButton createAbstractButton(AbstractButtonItem item)
	{
		if(item != null) {
			JButton button = new JButton(item.getText(), item.getIcon());
			button.setActionCommand(item.getActionCommand());
			if(item.getSelectedIcon() != null)
				button.setSelectedIcon(item.getSelectedIcon());
			if(item.getSize() != null)
				button.setPreferredSize(item.getSize());
			
			if(item.getText() == null) {//如果是图片按钮
				button.setContentAreaFilled(false);
				button.setCursor(ChannelConstants.HAND_CURSOR);
				
				if(item.getSize() == null && item.getIcon() != null) { //如果没有设置大小，则用图片大小
					button.setPreferredSize(new Dimension(item.getIcon().getIconWidth(), 
							item.getIcon().getIconHeight()));
				}
			}
			return button;
		}
		return null;
	}
	
	/**
	 * 由菜单项目构建菜单
	 * @param item 菜单项目
	 * @return
	 */
	public static JMenuItem createMenuItem(MenuItem item) {
		if(item != null) {
			JMenuItem menuItem = new JMenuItem(item.getText(), item.getIcon());
			menuItem.setActionCommand(item.getActionCommand());
			if(item.getSize() != null) {
				menuItem.setPreferredSize(item.getSize());
			}
			return menuItem;
		}
		return null;
	}
	
	/**
	 * 显示微博授权浏览器窗口，注意该窗口一旦打开，将使主程序处于wait()状态，待授权结束后才notify
	 * @throws WeiboException
	 * @return 授权是否成功
	 */
	public static void showAuthorizeWindow(final Window owner, final String account, final WindowListener wlistener) throws WeiboException
	{
		if(owner == null)
			throw new IllegalArgumentException("Authorize window hasn't known it's owner");

		final String authorizeURL = ChannelAccesser.getOauthRemoteURL();
		final JWebBrowser browser = new JWebBrowser();
		final WebBrowserListener listener = new WebBrowserAdapter()
		{
			public void locationChanged(WebBrowserNavigationEvent evt)
			{
				String site = evt.getWebBrowser().getResourceLocation();
				if (site == null || authorizeURL.equals(site)
						|| site.indexOf("code=") == -1) {
					return;
				}
				String code = site.substring(site.lastIndexOf("code=") + 5);
				AccessToken accessToken = null;
				try {
					accessToken = ChannelAccesser.getAccessTokenByCode(code);
				} catch (WeiboException e) {
					e.printStackTrace();
				}
				
				//本地保存授权密钥
				if (accessToken == null) {
					return;
				}
				else {
					ChannelAccesser.storeAccessTokenKey(account, accessToken.getAccessToken());
					SwingUtilities.getWindowAncestor(browser).dispose();//自动关闭浏览器窗口
				}
			}};
			showBrowser(browser, owner, authorizeURL, listener, wlistener);
	}
	
	/**
	 * 显示本地浏览器窗口
	 * @param URL
	 */
	public static void showBrowser(Window owner, String URL)
	{
		showBrowser(owner, URL, null,null);
	}
	public static void showBrowser(Window owner,  final String URL, final WebBrowserListener listener, final WindowListener listener2)
	{
		showBrowser(null, owner, URL, null,null);
	}
	public static void showBrowser(JWebBrowser webBrowser, Window owner,  final String URL, final WebBrowserListener listener, final WindowListener listener2)
	{
NativeInterface.open();
		
		 final JWebBrowser browser = webBrowser == null ? new JWebBrowser() : webBrowser;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					browser.setBarsVisible(false);
					browser.setButtonBarVisible(false);
					browser.setDefaultPopupMenuRegistered(false);
					browser.navigate(URL);
					
					if(listener != null)
					browser.addWebBrowserListener(listener);
					
				} catch (Exception ex) {
					System.err.println(ex);
				}
			}
		});
		
		if(browser != null) {
			final Window window = (Window)WebBrowserWindowFactory.create(owner, browser);
			if(listener2 != null) {
				window.addWindowListener(listener2); //自定义窗体事件
			}
			else {
				window.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						window.dispose();
					}
				});
			}
			
			//使用系统自带的标题栏
			if(window instanceof JDialog) {
				((JDialog) window).setUndecorated(false);
				((JDialog) window).setModal(true);
				((JDialog) window).getRootPane().setWindowDecorationStyle(JRootPane.NONE);
			}
			else if(window instanceof JFrame) {
				((JFrame) window).setUndecorated(false);
				((JFrame) window).getRootPane().setWindowDecorationStyle(JRootPane.NONE);
			}
			
			window.setSize(ChannelConfig.getIntValue("width")*2/3,
					ChannelConfig.getIntValue("height")*2/3);
			if(owner == null)
				window.setLocationRelativeTo(null);
			window.setVisible(true);
//			window.dispose();
		}

		try{
			NativeInterface.runEventPump();
		}
		catch(Exception ex) { }
	}
}
