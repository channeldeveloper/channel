package com.original.serive.channel.util;

import iqq.ui.QQFaceDialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import sun.swing.SwingUtilities2;
import weibo4j.http.AccessToken;
import weibo4j.model.WeiboException;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserAdapter;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserListener;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserNavigationEvent;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserWindowFactory;

import com.original.serive.channel.border.ShadowBorder;
import com.original.serive.channel.layout.VerticalGridLayout;
import com.original.serive.channel.server.ChannelAccesser;
import com.original.serive.channel.ui.ChannelMessageTopBar;
import com.original.serive.channel.ui.data.AbstractButtonItem;
import com.original.serive.channel.ui.data.MenuItem;
import com.original.serive.channel.ui.widget.FileChooserListener;
import com.original.serive.channel.ui.widget.FilePreviewer;

/**
 * 一些通用算法，目前主要用于客户端界面应用，服务端类请勿使用。
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
	 * 比较两个对象是否相等
	 * @param old
	 * @param nw
	 * @return
	 */
	public static boolean isEqual(Object old, Object nw)
	{
		if(old == nw)
			return true;
		
		return old != null && old.equals(nw);
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
	 * 按照设定的宽度、高度来设置图像的宽度、宽度，保持原图像的宽高比
	 * @param image 原图像
	 * @param width  设定宽度
	 * @param height 设定高度
	 * @return
	 */
	public static Dimension adjustImage(ImageIcon image, int width, int height)
	{
		Dimension dim = new Dimension();
		if(image != null && width >0 && height > 0)
		{
			int iconWidth = image.getIconWidth(), iconHeight = image
					.getIconHeight();
			
			//如果设定宽、高度和图片原来的宽、高度一致，就不需要调整了
			if(width == iconWidth && height == iconHeight)
			{
				dim.width = width;
				dim.height = height;
				return dim;
			}

			double d1 = iconWidth / (double) iconHeight, d2 = width
					/ (double) height;

			if (d2 >= d1) { // 以高度为准
				if (iconHeight > height)
					iconHeight = height;
				iconWidth = (int) (iconHeight * d1);
			} else {// 以宽度为准
				if (iconWidth > width)
					iconWidth = width;
				iconHeight = (int) (iconWidth / d1);
			}
			
			dim.width = iconWidth;
			dim.height = iconHeight;
		}
		return dim;
	}
	
	/**
	 * 由按钮项目构建按钮
	 * @param item 按钮项目
	 * @return
	 */
	public static JButton createAbstractButton(AbstractButtonItem item)
	{
		return createAbstractButton(item, JButton.class);
	}
	public static <T extends AbstractButton> T  createAbstractButton(AbstractButtonItem item, Class<T> clazz)
	{
		if(item != null) {
			T button = null;
			try{
				button = clazz.newInstance();
			}
			catch(Exception ex) {
				return button;
			}
			
			button.setText(item.getText());
			button.setIcon(item.getIcon());
			button.setActionCommand(item.getActionCommand());
			if(item.getSelectedIcon() != null)
				button.setSelectedIcon(item.getSelectedIcon());
			if(item.getSize() != null)
				button.setPreferredSize(item.getSize());
			
			if(item.getText() == null) {//如果是图片按钮
				button.setMargin(new Insets(0, 0, 0, 0));
				button.setContentAreaFilled(false);
				button.setCursor(ChannelConstants.HAND_CURSOR);
				
				if(item.getSize() == null && item.getIcon() != null) { //如果没有设置大小，则用图片大小
					button.setPreferredSize(new Dimension(item.getIcon().getIconWidth(), 
							item.getIcon().getIconHeight()));
				}
			}
			item.setSource(button);
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
			item.setSource(menuItem);
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
	
	/**
	 * 弹出对话框
	 */
	public static void showWithDialog(Component parent,
			String title, boolean modal, Container cp) {
		showWithDialog(null, parent, title, modal, cp);
	}
	public static void showWithDialog(Dimension size, Component parent,
			String title, boolean modal, Container child) {
		if (parent == null) {
			parent = JOptionPane.getRootFrame();
		}
		JDialog d = createDialog(parent, title, modal);
		d.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		d.setContentPane(child);

		if (size == null) {
			d.pack();
		} else {
			d.setSize(size);
		}
		d.setLocationRelativeTo(parent);
		d.setVisible(true);
	}
	
	/**
	 * 生成自定义对话框
	 */
	public static JDialog createDialog(Component parent, String title,
			boolean modal) {
		while ((parent != null) && (!(parent instanceof Frame))
				&& (!(parent instanceof Dialog))) {
			if (parent instanceof JPopupMenu) {
				parent = ((JPopupMenu) parent).getInvoker();
			}
			parent = parent.getParent();
		}

		if (parent == null) {
			parent = JOptionPane.getRootFrame();
		}

		if (parent instanceof Frame) {
			return new JDialog((Frame) parent, title, modal);
		} else {
			return new JDialog((Dialog) parent, title, modal);
		}
	}
	
	/**
	 * 创建自定义面板，与Channel主程序面板风格一致
	 */
	public static JPanel createCustomedPane(final JDialog dialog, final Container child)
	{
		JPanel body = new JPanel(new VerticalGridLayout(
				VerticalGridLayout.TOP_TO_BOTTOM,0,0,new Insets(0, 0, 5, 2)));
		body.setBorder(new ShadowBorder());
		
		ChannelMessageTopBar topBar = new ChannelMessageTopBar(true) {
			@Override
			public void doClose() {
				// TODO Auto-generated method stub
				if(dialog instanceof QQFaceDialog)
					((QQFaceDialog) dialog).setTextEditor(null);
				dialog.dispose();
			}

			@Override
			protected void constructStatusBar() {
				// TODO Auto-generated method stub
				setPreferredSize(new Dimension(child.getPreferredSize().width, 25));
			}
		};
		body.add(topBar);
		//顶部栏添加鼠标拖动事件
		ChannelDialogDragListener listener = new ChannelDialogDragListener(dialog);
		topBar.addMouseListener(listener);
		topBar.addMouseMotionListener(listener);
		body.add(child);
		return body;
	}
	
	/**
	 * 创建自定义的弹出对话框，与Channel主程序面板风格一致
	 */
	public static void showCustomedDialog(Component parent, 
			String title, boolean modal,final Container child)
	{
		JDialog d = createDialog(parent, title, modal);
		JPanel body = createCustomedPane(d, child);
		
		d.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		d.setContentPane(body);
		d.pack();
		d.setLocationRelativeTo(parent);
		d.setVisible(true);
	}
	
	/**
	 * 显示自定义颜色选择对话框
	 */
	public static Color showColorChooserDialog(Component c, String title, boolean modal,
	      JColorChooser chooserPane) throws HeadlessException {
		if(chooserPane == null) {
			chooserPane = new JColorChooser();
		}
		JPanel body = new JPanel();
		body.setBorder(new ShadowBorder());
		
		final	JDialog dialog = JColorChooser.createDialog(c, title, modal, chooserPane, null, null);
		dialog.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		body.add(dialog.getContentPane());
		dialog.setContentPane(body);
		dialog.setVisible(true);
		return chooserPane.getColor();
	}
	
	/**
	 * 显示自定义图片选择对话框
	 */
	public static File showImageChooserDialog(Component c, String title, boolean modal,
		     JFileChooser chooserPane)
	{
		return showFileChooserDialog(c, title, modal, chooserPane,
new FileNameExtensionFilter("图片文件(*.bmp, *.gif, *.jpg, *.jpeg, *.png)",
		"bmp", "gif", "jpg", "jpeg", "png"));
	}
	
	/**
	 * 显示自定义文件选择对话框
	 */
	public static File showFileChooserDialog(Component c, String title, boolean modal,
		     JFileChooser chooserPane, FileFilter filter) {
		if(chooserPane == null) {
			chooserPane = new JFileChooser();
		}
		if(filter != null) {
			chooserPane.setFileFilter(filter);
			new FilePreviewer(chooserPane);
		}
		chooserPane.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooserPane.setOpaque(false);
		
		FileChooserListener fcl = new FileChooserListener(chooserPane);
		chooserPane.addActionListener(fcl);
		showCustomedDialog(c, title, true, chooserPane);
		return fcl.getChooseFile();
	}	
	
	/**
	 * 显示QQ表情对话框
	 */
	public static void showQQFaceDialog(Component c, String title, boolean modal, 
			JEditorPane editor)
	{
		QQFaceDialog dialog = QQFaceDialog.getInstance();
		if(dialog.getTextEditor() == editor) {
			dialog.setVisible(true);
			return;
		}
		
		dialog.setTextEditor(editor);
JPanel body = createCustomedPane(dialog, dialog.getDefautFacePanel());
dialog.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
dialog.setContentPane(body);
dialog.pack();
dialog.setLocationRelativeTo(c);
dialog.setVisible(true);
	}
}
