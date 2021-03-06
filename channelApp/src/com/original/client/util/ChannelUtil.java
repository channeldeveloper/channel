package com.original.client.util;

import iqq.ui.QQFaceDialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.Box.Filler;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JScrollBar;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import weibo4j.http.AccessToken;
import weibo4j.model.WeiboException;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserAdapter;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserListener;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserNavigationEvent;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserWindowFactory;

import com.original.channel.ChannelAccesser;
import com.original.client.border.ShadowBorder;
import com.original.client.layout.VerticalGridLayout;
import com.original.client.ui.ChannelMessageTopBar;
import com.original.client.ui.data.AbstractButtonItem;
import com.original.client.ui.data.MenuItem;
import com.original.client.ui.data.TitleItem;
import com.original.client.ui.widget.FileChooserListener;
import com.original.client.ui.widget.FilePreviewer;
import com.original.client.ui.widget.ImagePane;
import com.original.client.ui.widget.MessageBox;
import com.original.client.ui.widget.ScrollBar;
import com.original.client.ui.widget.ToolTip;
import com.original.widget.OButton;
import com.original.widget.model.LevelButtonModel.BUTTONLEVEL;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import com.seaglasslookandfeel.widget.SGButton;
import com.seaglasslookandfeel.widget.SGColorChooser;
import com.seaglasslookandfeel.widget.SGFileChooser;
import com.seaglasslookandfeel.widget.SGMenuItem;
import com.seaglasslookandfeel.widget.SGOptionPane;
import com.seaglasslookandfeel.widget.SGPanel;
import com.seaglasslookandfeel.widget.SGScrollPane;

/**
 * 一些通用算法，目前主要用于客户端界面应用，服务端类请勿使用。
 * @author WMS
 *
 */
public class ChannelUtil implements ChannelConstants
{
	static ExecutorService executor = Executors.newCachedThreadPool();
	
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
	 * @param old 原对象
	 * @param nw 新对象
	 * @return
	 */
	public static boolean isEqual(Object old, Object nw)
	{
		if(old == nw)
			return true;
		
		return old != null && old.equals(nw);
	}
	
	/**
	 * 创建水平、或垂直方向的指定填充区域(<=0表示任意的宽度、或高度。但是不能同时为负)
	 * @param h 水平填充宽度
	 * @param v 垂直填充高度
	 * @return
	 */
	public static Filler createBlankFillArea(int h, int v)
	{
		if(h < 0 && v < 0)
			throw new IllegalArgumentException("h<0, v<0");
		
		if (h <= 0)
			return (Filler) Box.createVerticalStrut(v);
		if (v <= 0)
			return (Filler) Box.createHorizontalStrut(h);

		return (Filler) Box.createRigidArea(new Dimension(h, v));
	}
	
	/**
	 * 截取指定长度limitLength(单位px)的字符串。已优化算法！！！（取代了原先使用FontMetrics测量每一个字符的方式，效率慢）。
	 * @param text 原字符串
	 * @param font  字体，这里用于获取字体的大小
	 * @param limitLength 截取长度，一般为控件的宽度
	 * @return
	 */
	public static String cutString(String text, Font font, int limitLength)
	{
		if(text == null || text.isEmpty())
			return text;
		
//		if(text != null && fm.stringWidth(text) > cutLength)
		{
			int totalLength = 0, fontSize = font.getSize();
			boolean isDoubleByteChar = false;//是否是双字节字符
			
			for(int i=0 ; i<text.length(); i++)
			{
				char c = text.charAt(i);
				
				//通过判断某一位的字符是否大于0x80，如果大于则为双字节字符，否则为单字节字符
				if(c > 0x80) {
					isDoubleByteChar = true;
				}
				else {
					isDoubleByteChar = false;
				}
				
				totalLength += isDoubleByteChar ? fontSize : fontSize /2 ;
				if(totalLength >= limitLength) {
					return ( isDoubleByteChar ? text.substring(0, i-1) : 
							text.substring(0, i-2) )+ "…";
				}
			}
		}
		return text;
	}
	/**
	 * 按控件的当前显示大小(宽度)来截取字符串
	 * @param text 原字符串
	 * @param comp 控件，一般为JTextComponent的子对象，如JTextPane、JTextField等
	 * @return
	 */
	public static String cutString(String text, JComponent comp)
	{
		if(comp != null && !isEmpty(text)) {
//			FontMetrics fm = SwingUtilities2.getFontMetrics(comp, comp.getFont());
			return cutString(text, comp.getFont(), comp.getPreferredSize().width);
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
	/**
	 * 按照设定的宽度、高度来设置图像的宽度、宽度，保持原图像的宽高比
	 * @param oldWidth 原图像宽度
	 * @param oldHeight 原图像高度
	 * @param width  设定宽度
	 * @param height 设定高度
	 * @return
	 */
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
	 * 由按钮属性对象构建按钮
	 * @param item 按钮属性对象
	 * @return
	 */
	public static SGButton createAbstractButton(AbstractButtonItem item)
	{
		return createAbstractButton(item, SGButton.class);
	}
	/**
	 * 创建应用程序按钮，其实就是按钮样式(使用OButtonUI)不同而已
	 * @param item 按钮属性对象
	 * @return
	 */
	public static OButton createApplicationButton(AbstractButtonItem item)
	{
		OButton button = createAbstractButton(item, OButton.class);
		button.setLevel(BUTTONLEVEL.APPLICATION);
//		button.getModel().getButtonEffect().setDrawBorder(false);
		button.getModel().getButtonEffect().setHasShadow(false);
		return button;
	}
	/**
	 * 由按钮属性对象构建按钮。传参中的按钮类不同，返回的按钮对象也不同
	 * @param item 按钮属性对象
	 * @param clazz 按钮类，继承自AbstractButton，如JButton、JToggleButton等
	 * @return
	 */
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
//				button.setBorderPainted(false);
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
	 * 由菜单属性对象构建菜单
	 * @param item 菜单属性对象
	 * @return
	 */
	public static SGMenuItem createMenuItem(MenuItem item) {
		if(item != null) {
			SGMenuItem menuItem = new SGMenuItem(item.getText(), item.getIcon());
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
	 * 显示微博授权浏览器窗口。待用户输入微博账号和密码后，会自动返回给用户激活码，然后程序自动由激活码生成授权Token
	 * @throws WeiboException
	 * @return 
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
	 * 弹出自定义风格的消息框
	 */
	public static void showMessageDialog(Component parent, String title,
			Object content) {
		MessageBox box = new MessageBox(content);
		
		final SGOptionPane   pane = new SGOptionPane(box.getMessageBox(), SGOptionPane.INFORMATION_MESSAGE,
                SGOptionPane.DEFAULT_OPTION, null,
                null, null);
		
pane.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				// TODO 自动生成的方法存根
				if(evt.getPropertyName() == SGOptionPane.VALUE_PROPERTY) {
					
					if(evt.getNewValue() instanceof Integer 
							&& ((Integer)evt.getNewValue()).intValue() != SGOptionPane.CLOSED_OPTION) {
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
		
		final SGOptionPane   pane = new SGOptionPane(box.getMessageBox(), SGOptionPane.QUESTION_MESSAGE,
                SGOptionPane.YES_NO_OPTION, null,
                null, null);
		pane.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				// TODO 自动生成的方法存根
				if(evt.getPropertyName() == SGOptionPane.VALUE_PROPERTY) {
					
					if(evt.getNewValue() instanceof Integer 
							&& ((Integer)evt.getNewValue()).intValue() != SGOptionPane.CLOSED_OPTION) {
						SwingUtilities.getWindowAncestor(pane).dispose();
					}
				}
			}
		});
		
		showCustomedDialog(parent, title, true, pane);
		return getOptionValue(pane);
		
	}
	
	/**
	 * 是否确认
	 */
	public static boolean confirm(Component parent, String title,
			Object content) {
		return SGOptionPane.YES_OPTION == showConfirmDialog(parent, title, content);
	}
	
	/**
	 * 返回选项面板的选项索引值
	 * @param op 选项面板
	 * @return
	 */
	private static int getOptionValue(SGOptionPane op) {
		Object selectedValue = op.getValue();
		Object[] options = op.getOptions();

		if (selectedValue == null)
			return SGOptionPane.CLOSED_OPTION;
		if (options == null) {
			if (selectedValue instanceof Integer)
				return ((Integer) selectedValue).intValue();
			return SGOptionPane.CLOSED_OPTION;
		}
		for (int counter = 0, maxCounter = options.length; 
				counter < maxCounter; counter++) {
			if (options[counter].equals(selectedValue))
				return counter;
		}
		return SGOptionPane.CLOSED_OPTION;
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
			parent = SGOptionPane.getRootFrame();
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
	public static SGPanel createCustomedPane(final JDialog dialog, final Container child, final String title)
	{
		SGPanel body = new SGPanel(new VerticalGridLayout(
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
		final SGPanel body = createCustomedPane(d, child, title);
		
		d.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		d.setContentPane(body);
		d.pack();
		d.setLocationRelativeTo(parent);
//		checkWindowLocation(d);
//		d.setVisible(true);
		return d;
	}
	
	/**
	 * 检查窗体，如对话框的位置是否超出规定边界
	 */
	public static void checkWindowLocation(Window window)
	{
		Point point = window.getLocation();
		checkWindowLocation(point.x, point.y, window);
	}
	public  static void checkWindowLocation(int x, int y, Window window)
	{
		checkComponentLocation(x, y, window);
	}
	/**
	 * 检查控件的位置是否超出规定边界
	 */
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
						SGPanel container = (SGPanel) dialog.getContentPane();
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
	      SGColorChooser chooserPane) throws HeadlessException {
		if(chooserPane == null) {
			chooserPane = new SGColorChooser();
		}
		SGPanel body = new SGPanel();
		body.setBorder(new ShadowBorder());
		
		final	JDialog dialog = SGColorChooser.createDialog(c, title, modal, chooserPane, null, null);
		dialog.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		
		body.add(dialog.getContentPane());
		dialog.setContentPane(body);
		dialog.pack();
		checkWindowLocation(dialog);
		dialog.setVisible(true);
		return chooserPane.getColor();
	}
	
	/**
	 * 显示自定义图片选择对话框
	 */
	public static File showImageChooserDialog(Component c, String title, boolean modal,
		     SGFileChooser chooserPane)
	{
		return showFileChooserDialog(c, title, modal, chooserPane,
new FileNameExtensionFilter("图片文件(*.bmp, *.gif, *.jpg, *.jpeg, *.png)",
		"bmp", "gif", "jpg", "jpeg", "png"));
	}
	
	/**
	 * 显示自定义文件选择对话框
	 */
	public static File showFileChooserDialog(Component c, String title, boolean modal,
		    SGFileChooser chooserPane, FileFilter filter) {
		if(chooserPane == null) {
			chooserPane = new SGFileChooser();
		}
		if(filter != null) {
			chooserPane.setFileFilter(filter);
			new FilePreviewer(chooserPane);
		}
		chooserPane.setFileSelectionMode(SGFileChooser.FILES_ONLY);
		chooserPane.setOpaque(true);
		
		FileChooserListener fcl = new FileChooserListener(chooserPane);
		chooserPane.addActionListener(fcl);
		showCustomedDialog(c, title, true, chooserPane);
		return fcl.getChooseFile();
	}	
	
	/**
	 * 显示自定义文件保存对话框
	 */
	public static void showFileSaveDialog(Component c, String title, boolean modal,
		     SGFileChooser savePane, File saveFile) {
		if(savePane == null) {
			savePane = new SGFileChooser();
			savePane.setDialogType(SGFileChooser.SAVE_DIALOG);
		}
		savePane.setSelectedFile(saveFile);
		savePane.setFileSelectionMode(SGFileChooser.FILES_ONLY);
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
		SeaGlassLookAndFeel.useOurUIs(dialog.getDefautFacePanel());
	SGPanel body = createCustomedPane(dialog, dialog.getDefautFacePanel(), title);
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
	public static SGScrollPane createScrollPane(Component c) {
		return createScrollPane(c, Color.gray);
	}
	public static SGScrollPane createScrollPane(Component c, Color barColor) {
		SGScrollPane jsp = new SGScrollPane(c, SGScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				SGScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		// 不显示边框，同时设置背景透明
		jsp.setBorder(null);
		jsp.setOpaque(false);
		
		JScrollBar vsb = new ScrollBar(JScrollBar.VERTICAL, barColor);
		vsb.setUnitIncrement(100);
		jsp.setVerticalScrollBar(vsb);
		jsp.setViewportBorder(null);
		jsp.getViewport().setOpaque(false);
		
		return jsp;
	}
	
	/**
	 * 为控件添加提示提示窗
	 * @param c 控件
	 * @param tipText 提示文本
	 */
	public static void addToolTip(final Component c, final String tipText) {
		final ToolTip toolTip = new ToolTip();
		toolTip.setToolTipText(tipText);
		c.addFocusListener(new ToolTipFocusListener(toolTip));
	}
	
	/**
	 * 显示控件的提示文本，如果控件已添加提示窗(见{@link #addToolTip(Component, String)})，则会弹出该提示窗，否则新建一个提示窗
	 * @param c 控件
	 * @param tipText 提示文本，如果为null，则使用原有已添加的提示窗中的文本
	 * @param tipColor 提示文本颜色
	 */
	public static void showToolTip(final Component c, String tipText, Color tipColor) {
		ToolTipFocusListener ttListener = getToolTipFocusListener(c);
		ToolTip tt = null;
		if (ttListener == null) {
			tt = new ToolTip();
		} else {
			tt = ttListener.tt;
			if (tipText == null) 
				tipText = tt.getToolTipText();
		}
		
		if (!tt.isInvokerShow()) {
			tt.setToolTipText(tipText);
			tt.setToolTipColor(tipColor);
			tt.show(c, 0, 30);
		} else {
			tt.removeInvoker(c);
		}
	}
	
	private static ToolTipFocusListener getToolTipFocusListener(Component c) {
		FocusListener[] listeners = c.getFocusListeners();
		if(listeners != null && listeners.length > 0) {
			for(FocusListener listener : listeners) {
				if(listener instanceof ToolTipFocusListener)
					return (ToolTipFocusListener)listener;
			}
		}
		return null;
	}
	
	/**
	 * 获取水平或垂直方向对应的表格渲染器
	 * @param alignment 对齐方向，见{@link SwingConstants}
	 * @return
	 */
	public static ListCellRenderer getAliginCellRenderer(final int alignment)
	{
		return new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				// TODO 自动生成的方法存根
				JLabel cell = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
						cellHasFocus);
				switch(alignment) {
				case TOP:
				case BOTTOM:
					cell.setVerticalAlignment(alignment);
					break;
					
				case LEFT:
				case RIGHT:
					cell.setHorizontalAlignment(alignment);
					break;
				}
				return cell;
			}
		};
	}
	
	public static class ToolTipFocusListener implements FocusListener
	{
		ToolTip tt = null;
		ToolTipFocusListener(ToolTip toolTip)
		{
			tt = toolTip;
		}
		@Override
		public void focusGained(FocusEvent e) {
			if (!tt.isInvokerShow()) {
				tt.show((Component)e.getSource(), 0, 30);
			} else {
				tt.removeInvoker((Component)e.getSource());
			}
		}

		@Override
		public void focusLost(FocusEvent e) {

		}
	}
	
	/**
	 * 执行线程，这里用于客户端界面
	 * @param command 可执行对象
	 */
	public static void exec(Runnable command) {
		executor.execute(command);
	}
	
	/**
	 * 执行线程，这里用于客户端界面
	 * @param call 可执行对象
	 */
	public  static <T> Future<T> exec(Callable<T> call) {
		return executor.submit(call);
	}
}
