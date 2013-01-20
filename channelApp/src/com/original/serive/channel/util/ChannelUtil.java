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
import java.awt.Point;
import java.awt.Window;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.Buffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JScrollBar;
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
import com.original.serive.channel.comp.CButton;
import com.original.serive.channel.comp.CMenuItem;
import com.original.serive.channel.comp.CPanel;
import com.original.serive.channel.comp.CPopupMenu;
import com.original.serive.channel.comp.CScrollPanel;
import com.original.serive.channel.layout.VerticalGridLayout;
import com.original.serive.channel.server.ChannelAccesser;
import com.original.serive.channel.ui.ChannelMessageTopBar;
import com.original.serive.channel.ui.data.AbstractButtonItem;
import com.original.serive.channel.ui.data.MenuItem;
import com.original.serive.channel.ui.data.TitleItem;
import com.original.serive.channel.ui.widget.FileChooserListener;
import com.original.serive.channel.ui.widget.FilePreviewer;
import com.original.serive.channel.ui.widget.ImagePane;
import com.original.serive.channel.ui.widget.MessageBox;
import com.original.widget.OScrollBar;

/**
 * 一些通用算法，目前主要用于客户端界面应用，服务端类请勿使用。
 * @author WMS
 *
 */
public class ChannelUtil implements ChannelConstants
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
					return text.substring(0, i) + "…";
				}
				else if(len > cutLength) {
					return text.substring(0, i-1) + "…";
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
			return adjustImage(image.getIconWidth(), image.getIconHeight(), width, height);
		}
		return dim;
	}
	public static Dimension adjustImage(int oldWidth, int oldHeight, int width, int height)
	{
		Dimension dim = new Dimension();
		if(oldWidth >0 && oldHeight > 0 && width >0 && height > 0)
		{			
			//如果设定宽、高度和图片原来的宽、高度一致，就不需要调整了
			if(width == oldWidth && height == oldHeight)
			{
				dim.width = width;
				dim.height = height;
				return dim;
			}

			double d1 = oldWidth / (double) oldHeight, d2 = width
					/ (double) height;

			if (d2 >= d1) { // 以高度为准
				if (oldHeight > height)
					oldHeight = height;
				oldWidth = (int) (oldHeight * d1);
			} else {// 以宽度为准
				if (oldWidth > width)
					oldWidth = width;
				oldHeight = (int) (oldWidth / d1);
			}
			
			dim.width = oldWidth;
			dim.height = oldHeight;
		}
		return dim;
	}
	
	/**
	 * 由按钮项目构建按钮
	 * @param item 按钮项目
	 * @return
	 */
	public static CButton createAbstractButton(AbstractButtonItem item)
	{
		return createAbstractButton(item, CButton.class);
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
			if(item.getDisabledIcon() != null)
				button.setDisabledIcon(item.getDisabledIcon());
			if(item.getSize() != null)
				button.setPreferredSize(item.getSize());
			
			if(item.getText() == null) {//如果是图片按钮
				button.setMargin(new Insets(0, 0, 0, 0));
				button.setContentAreaFilled(false);
				button.setCursor(HAND_CURSOR);
				
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
	public static CMenuItem createMenuItem(MenuItem item) {
		if(item != null) {
			CMenuItem menuItem = new CMenuItem(item.getText(), item.getIcon());
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
	public static void showAuthorizeWindow(final Window owner, final String account, final WindowListener listener) throws WeiboException
	{
		if(owner == null)
			throw new IllegalArgumentException("Authorize window hasn't known it's owner");

		final String authorizeURL = ChannelAccesser.getOauthRemoteURL();
		final JWebBrowser browser = new JWebBrowser();
		final WebBrowserListener wlistener = new WebBrowserAdapter()
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
			
			try {
				Window window = showBrowser(browser, owner, authorizeURL, wlistener);
				window.addWindowListener(listener);
				window.setVisible(true);
			}
			catch(Exception ex) {
				throw new WeiboException(ex);
			}
			
	}
	
	/**
	 * 显示本地浏览器窗口
	 * @param URL
	 */
	public static Window showBrowser(Window owner, String URL) throws Exception
	{
		return showBrowser(owner, URL, null);
	}
	public static Window showBrowser(Window owner,  final String URL, final WebBrowserListener listener) throws Exception
	{
		return showBrowser(null, owner, URL, listener);
	}
	public static Window showBrowser(JWebBrowser webBrowser, Window owner,  final String URL, final WebBrowserListener listener) throws Exception
	{
NativeInterface.open();
		
		 final JWebBrowser browser = webBrowser == null ? new JWebBrowser() : webBrowser;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				browser.setBarsVisible(false);
				browser.setButtonBarVisible(false);
				browser.setDefaultPopupMenuRegistered(false);
				browser.navigate(URL);
				
				if(listener != null) {
					browser.addWebBrowserListener(listener);
				}
			}
		});
		
		final Window window = (Window)WebBrowserWindowFactory.create(owner, browser);			
		//使用系统自带的标题栏
		if (window instanceof JDialog) {
			((JDialog) window).setUndecorated(false);
			((JDialog) window).setModal(true);
			((JDialog) window).getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		} else if (window instanceof JFrame) {
			((JFrame) window).setUndecorated(false);
			((JFrame) window).getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		}
		
		window.setSize(CHANNELWIDTH*2/3, CHANNELHEIGHT*2/3);
		window.setMaximumSize(new Dimension(CHANNELWIDTH, CHANNELHEIGHT-STATUSBARHEIGHT));
		if(owner == null)
			window.setLocationRelativeTo(null);
		checkWindowLocation(window);

		try{
			NativeInterface.runEventPump();
		}
		catch(Exception ex) { }
		
		return window;
	}
	
	/**
	 * 弹出对话框，默认使用UIManager#getDefaultLookandFeel主题
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
	 * 弹出自定义风格的消息框
	 */
	public static void showMessageDialog(Component parent, String title,
			Object content) {
		MessageBox box = new MessageBox(content);
		
		final JOptionPane   pane = new JOptionPane(box.getMessageBox(), JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.DEFAULT_OPTION, null,
                null, null);
		
pane.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				// TODO 自动生成的方法存根
				if(evt.getPropertyName() == JOptionPane.VALUE_PROPERTY) {
					
					if(evt.getNewValue() instanceof Integer 
							&& ((Integer)evt.getNewValue()).intValue() != JOptionPane.CLOSED_OPTION) {
						SwingUtilities.getWindowAncestor(pane).dispose();
					}
				}
			}
		});
		
		showCustomedDialog(parent, title, true, pane);
	}
	
	/**
	 * 弹出自定义风格的确认框
	 */
	public static int showConfirmDialog(Component parent, String title,
			Object content) {
		
MessageBox box = new MessageBox(content);
		
		final JOptionPane   pane = new JOptionPane(box.getMessageBox(), JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION, null,
                null, null);
		pane.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				// TODO 自动生成的方法存根
				if(evt.getPropertyName() == JOptionPane.VALUE_PROPERTY) {
					
					if(evt.getNewValue() instanceof Integer 
							&& ((Integer)evt.getNewValue()).intValue() != JOptionPane.CLOSED_OPTION) {
						SwingUtilities.getWindowAncestor(pane).dispose();
					}
				}
			}
		});
		
		showCustomedDialog(parent, title, true, pane);
		return getOptionValue(pane);
		
	}
	public static boolean confirm(Component parent, String title,
			Object content) {
		return JOptionPane.YES_OPTION == showConfirmDialog(parent, title, content);
	}
	
	/**
	 * 返回选项面板的选项索引值
	 * @param op 选项面板
	 * @return
	 */
	private static int getOptionValue(JOptionPane op) {
		Object selectedValue = op.getValue();
		Object[] options = op.getOptions();

		if (selectedValue == null)
			return JOptionPane.CLOSED_OPTION;
		if (options == null) {
			if (selectedValue instanceof Integer)
				return ((Integer) selectedValue).intValue();
			return JOptionPane.CLOSED_OPTION;
		}
		for (int counter = 0, maxCounter = options.length; 
				counter < maxCounter; counter++) {
			if (options[counter].equals(selectedValue))
				return counter;
		}
		return JOptionPane.CLOSED_OPTION;
	}
	
	/**
	 * 生成自定义对话框
	 */
	public static JDialog createDialog(Component parent, String title,
			boolean modal) {
		while ((parent != null) && (!(parent instanceof Frame))
				&& (!(parent instanceof Dialog))) {
			if (parent instanceof CPopupMenu) {
				parent = ((CPopupMenu) parent).getInvoker();
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
	public static CPanel createCustomedPane(final JDialog dialog, final Container child, final String title)
	{
		CPanel body = new CPanel(new VerticalGridLayout(
				VerticalGridLayout.TOP_TO_BOTTOM,0,0,new Insets(0, 0, 5, 2)));
		body.setBorder(new ShadowBorder(2, 10, 0.4f, child.getBackground()));
		
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
		topBar.addTitleItem(new TitleItem(title).setFontSize(15).setColor(Color.black));
		body.add(topBar);
		//顶部栏添加鼠标拖动事件
		ChannelDialogDragListener listener = new ChannelDialogDragListener(dialog);
		topBar.addMouseListener(listener);
		topBar.addMouseMotionListener(listener);
		if(child.isOpaque() && child instanceof JComponent)
			((JComponent)child).setOpaque(false);
		body.add(child);
		return body;
	}
	
	/**
	 * 创建自定义的弹出对话框，与Channel主程序面板风格一致
	 */
	public static void showCustomedDialog(Component parent, 
			String title, boolean modal,final Container child)
	{
		JDialog dialog = createCustomedDialog(parent, title, modal, child);
		checkWindowLocation(dialog);
		dialog.setVisible(true);
	}
	public static JDialog createCustomedDialog(Component parent, 
			String title, boolean modal,final Container child)
	{
		JDialog d = createDialog(parent, title, modal);
		final CPanel body = createCustomedPane(d, child, title);
		
		d.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		d.setContentPane(body);
		d.pack();
		d.setLocationRelativeTo(parent);
//		checkWindowLocation(d);
//		d.setVisible(true);
		return d;
	}
	
	public static void checkWindowLocation(Window window)
	{
		Point point = window.getLocation();
		checkWindowLocation(point.x, point.y, window);
	}
	
	public  static void checkWindowLocation(int x, int y, Window window)
	{
		checkComponentLocation(x, y, window);
	}
	
	public static Point checkComponentLocation(int x, int y, Component comp)
	{
		if (x < MARGIN_LEFT)
			x = MARGIN_LEFT;
		else if (x + comp.getWidth() > MARGIN_RIGHT)
			x = MARGIN_RIGHT - comp.getWidth();
		else if (x > MARGIN_RIGHT)
			x = MARGIN_RIGHT;

		if (y < MARGIN_TOP)
			y = MARGIN_TOP;
		else if (y + comp.getHeight() > MARGIN_BOTTOM)
			y = MARGIN_BOTTOM - comp.getHeight();
		else if (y > MARGIN_BOTTOM)
			y = MARGIN_BOTTOM;
		
		Point p = new Point(x, y);
		if (comp instanceof Window) {
			comp.setLocation(p);
		}
		return p;
	}
	
	/**
	 * 显示图片对话框
	 */
	public static void showImageDialog(final String imgURL)
	{
		BufferedImage image = null;
		try {
			image = ImageIO.read(new URL(imgURL));
		} catch (Exception ex) {
			System.err.println("加载图片失败：" + ex);
		}

		if (image != null) {
			final ImagePane ip = new ImagePane();
			final JDialog dialog = createCustomedDialog(null, "查看原图", true, ip);
			
			Thread loading = new Thread(new Runnable() {// 图片加载线程
				public void run() {
					ip.setBackground(imgURL);
					if (ip.getBackgroundImage() != null) {
						CPanel container = (CPanel) dialog.getContentPane();
						container.remove(ip);
						container.add(ip.getScrollImagePane());
						container.validate();

						dialog.pack();
						checkWindowLocation(dialog);
					}
				}
			}, "Loading Image");
			loading.start();
			
			checkWindowLocation(dialog);
			dialog.setVisible(true);
		}
	}
	
	/**
	 * 显示自定义颜色选择对话框
	 */
	public static Color showColorChooserDialog(Component c, String title, boolean modal,
	      JColorChooser chooserPane) throws HeadlessException {
		if(chooserPane == null) {
			chooserPane = new JColorChooser();
		}
		CPanel body = new CPanel();
		body.setBorder(new ShadowBorder());
		
		final	JDialog dialog = JColorChooser.createDialog(c, title, modal, chooserPane, null, null);
		dialog.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		body.add(dialog.getContentPane());
		dialog.setContentPane(body);
		checkWindowLocation(dialog);
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
	
	public static void showFileSaveDialog(Component c, String title, boolean modal,
		     JFileChooser savePane, File saveFile) {
		if(savePane == null) {
			savePane = new JFileChooser();
			savePane.setDialogType(JFileChooser.SAVE_DIALOG);
		}
		savePane.setSelectedFile(saveFile);
		savePane.setFileSelectionMode(JFileChooser.FILES_ONLY);
		savePane.setOpaque(false);
		
		FileChooserListener fcl = new FileChooserListener(savePane);
		savePane.addActionListener(fcl);
		showCustomedDialog(c, title, true, savePane);
		
		File chooseFile = fcl.getChooseFile();
		if(chooseFile != null) {
			try {
				copyBigFile(saveFile, chooseFile); //复制文件
				showMessageDialog(c, "保存成功", "文件保存成功！");
			} catch (IOException ex) {
				// TODO 自动生成的 catch 块
				showMessageDialog(c, "错误", ex);
			}
		}
	}
	
	/**
	 * 复制文件操作。将源文件复制到目标文件(夹)指定的路径。如果目标文件存在，则覆盖。
	 * @param sourceFile 源文件
	 * @param targetFile 目标文件(夹)
	 */
	public static void copyFile(File sourceFile, File targetFile)
			throws IOException {
		if (sourceFile == null || !sourceFile.exists()) {
			throw new IOException("源文件[" + sourceFile + "]不存在或没有访问权限！");
		}

		if (targetFile == null) {
			throw new IOException("无法指定目标文件的位置！");
		} else if (targetFile.isDirectory()) {
			targetFile = new File(targetFile, sourceFile.getName());
		}

		File parentFile = null;
		if (!(parentFile = targetFile.getParentFile()).exists()) {
			parentFile.mkdirs();
		}

		if (sourceFile.isDirectory()) // 允许源文件是空目录的情况。
		{
			if (sourceFile.listFiles().length == 0) {
				targetFile.mkdir();
				return;
			} else {
				throw new IOException("源文件[" + sourceFile + "]是目录，不允许直接复制！");
			}
		}

		BufferedInputStream in = null;
		BufferedOutputStream out = null;

		try {
			in = new BufferedInputStream(new FileInputStream(sourceFile));
			out = new BufferedOutputStream(new FileOutputStream(targetFile, false));

			byte[] buffer = new byte[1024 * 1024];
			int read = -1;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}

			out.flush();
		} catch (Exception ex) {
			throw new IOException(ex.getMessage());
		} finally {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
		}
	}
	
	/**
	 * 复制大文件操作。将源文件复制到目标文件(夹)指定的路径。如果目标文件存在，则覆盖。
	 * 利用JAVA内存映射机制快速高效地复制文件。注意映射时，要考虑JVM内存的承受能力，不要一次全部映射(文件大小可能远大于JVM内存大小)。
	 * @param sourceFile 源文件
	 * @param targetFile 目标文件(夹)
	 */
	public static void copyBigFile(File sourceFile, File targetFile)
			throws IOException {
		if (sourceFile == null || !sourceFile.exists()) {
			throw new IOException("源文件[" + sourceFile + "]不存在或没有访问权限！");
		}
		if (sourceFile.isDirectory() || sourceFile.length() <= 10 * 1024 * 1024) {
			copyFile(sourceFile, targetFile);
			return;
		}

		if (targetFile == null) {
			throw new IOException("无法指定目标文件的位置！");
		} else if (targetFile.isDirectory()) {
			targetFile = new File(targetFile, sourceFile.getName());
		}

		File parentFile = null;
		if (!(parentFile = targetFile.getParentFile()).exists()) {
			parentFile.mkdirs();
		}

		// 上面初始化源、目标文件后，开始进行复制文件。
		RandomAccessFile rafi = null;
		RandomAccessFile rafo = null;

		MappedByteBuffer mbbi = null;
		MappedByteBuffer mbbo = null;

		try {
			rafi = new RandomAccessFile(sourceFile, "r"); // 源文件读
			rafo = new RandomAccessFile(targetFile, "rw");// 目标文件读写，没有只写的方式
			if (targetFile.exists()) {
				rafo.setLength(0L);// 如果目标存在则覆盖。然后重新写。
			}

			FileChannel fci = rafi.getChannel(); // 文件取通道
			FileChannel fco = rafo.getChannel(); // 文件写通道

			long length = 0, position = 0; // 每次读取字节长度 和 当前读取位置
			long remaining = fci.size(); // 剩余字节长度

			long freeMemorySize = Runtime.getRuntime().freeMemory();
			while (remaining > 0) {
				length = Math.min(remaining, freeMemorySize);
				mbbi = fci.map(MapMode.READ_ONLY, position, length);
				mbbo = fco.map(MapMode.READ_WRITE, position, length);

				for (int i = 0; i < length; i++) {
					mbbo.put(mbbi.get(i));
				}

				position += length;
				remaining -= length;

				// 每次读写完毕后，释放内存空间，否则会报文件已被占用或打开的错误。
				// MappedByteBuffer自身的一个Bug。
				releaseMappedBuffer(mbbi);
				releaseMappedBuffer(mbbo);
			}
		} catch (Exception ex) {
			throw new IOException(ex.getMessage());
		} finally {
			if (rafi != null) {
				rafi.close();
			}
			if (rafo != null) {
				rafo.close();
			}
		}
	}
	
	/**
	 * 清除MapperBuffer占用的内存空间(MappedByteBuffer自身Bug)
	 * @param buffer
	 */
	private static void releaseMappedBuffer(final Buffer buffer) {
		if(buffer != null)
		{
			AccessController.doPrivileged(new PrivilegedAction<Object>() {
				public Object run() 
				{
					try {
						Method cleanerMethod = buffer.getClass().getMethod("cleaner");
						if(cleanerMethod != null)
						{
							cleanerMethod.setAccessible(true);
							Object cleanerObj = cleanerMethod.invoke(buffer);

							Method cleanMethod = (cleanerObj == null) ? null :
								cleanerObj.getClass().getMethod("clean");
							if(cleanMethod != null)
							{
								cleanMethod.invoke(cleanerObj);
							}
						}
					} catch (Throwable ex) {
						// TODO Auto-generated catch block
					}
					return null;
				}
			});
		}		
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
	CPanel body = createCustomedPane(dialog, dialog.getDefautFacePanel(), title);
	dialog.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
	dialog.setContentPane(body);
	dialog.pack();
	dialog.setLocationRelativeTo(c);
	checkWindowLocation(dialog);
	dialog.setVisible(true);
	}
	
	/**
	 * 创建带滚动条的面板
	 */
	public static CScrollPanel createScrollPane(Component c) {
		return createScrollPane(c, Color.gray);
	}
	public static CScrollPanel createScrollPane(Component c, Color barColor) {
		CScrollPanel jsp = new CScrollPanel(c, CScrollPanel.VERTICAL_SCROLLBAR_AS_NEEDED,
				CScrollPanel.HORIZONTAL_SCROLLBAR_NEVER);

		// 不显示边框，同时设置背景透明
		jsp.setBorder(null);
		jsp.setOpaque(false);
		jsp.setVerticalScrollBar(new OScrollBar(JScrollBar.VERTICAL, barColor));
		jsp.setViewportBorder(null);
		jsp.getViewport().setOpaque(false);
		
		return jsp;
	}
}
