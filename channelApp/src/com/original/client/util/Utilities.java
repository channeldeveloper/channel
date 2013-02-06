package com.original.client.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
import java.nio.Buffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.Box.Filler;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.original.client.border.ShadowBorder;
import com.original.client.layout.VerticalGridLayout;
import com.original.client.ui.ChannelMessageTopBar;
import com.original.client.ui.data.AbstractButtonItem;
import com.original.client.ui.data.MenuItem;
import com.original.client.ui.data.TitleItem;
import com.original.client.ui.widget.MessageBox;
import com.original.client.ui.widget.ToolTip;
import com.original.widget.OButton;
import com.original.widget.OScrollBar;
import com.original.widget.model.LevelButtonModel.BUTTONLEVEL;

public class Utilities implements ChannelConstants
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
	 * 创建水平、或垂直方向的指定填充区域(<=0表示任意的宽度、或高度)
	 * @param h 水平填充宽度
	 * @param v 垂直填充高度
	 * @return
	 */
	public static Filler createBlankFillArea(int h, int v)
	{
		if (h <= 0)
			return (Filler) Box.createVerticalStrut(v);
		if (v <= 0)
			return (Filler) Box.createHorizontalStrut(h);

		return (Filler) Box.createRigidArea(new Dimension(h, v));
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
	public static OButton createApplicationButton(AbstractButtonItem item)
	{
		OButton button = createAbstractButton(item, OButton.class);
		button.setLevel(BUTTONLEVEL.APPLICATION);
//		button.getModel().getButtonEffect().setDrawBorder(false);
		button.getModel().getButtonEffect().setHasShadow(false);
		return button;
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
				button.setBorderPainted(false);
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
	public static JPanel createCustomedPane(final JDialog dialog, final Container child, final String title)
	{
		JPanel body = new JPanel();
		body.setLayout(new VerticalGridLayout(
				VerticalGridLayout.TOP_TO_BOTTOM,0,0,new Insets(0, 0, 5, 2)));
		body.setBorder(new ShadowBorder(2, 10, 0.4f, child.getBackground()));
		
		ChannelMessageTopBar topBar = new ChannelMessageTopBar(true) {
			@Override
			public void doClose() {
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
		final JPanel body = createCustomedPane(d, child, title);
		
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
		dialog.pack();
		checkWindowLocation(dialog);
		dialog.setVisible(true);
		return chooserPane.getColor();
	}
	
//	/**
//	 * 显示自定义图片选择对话框
//	 */
//	public static File showImageChooserDialog(Component c, String title, boolean modal,
//			JFileChooser chooserPane)
//	{
//		return showFileChooserDialog(c, title, modal, chooserPane,
//new FileNameExtensionFilter("图片文件(*.bmp, *.gif, *.jpg, *.jpeg, *.png)",
//		"bmp", "gif", "jpg", "jpeg", "png"));
//	}
//	
//	/**
//	 * 显示自定义文件选择对话框
//	 */
//	public static File showFileChooserDialog(Component c, String title, boolean modal,
//		    JFileChooser chooserPane, FileFilter filter) {
//		if(chooserPane == null) {
//			chooserPane = new JFileChooser();
//		}
//		if(filter != null) {
//			chooserPane.setFileFilter(filter);
//			new FilePreviewer(chooserPane);
//		}
//		chooserPane.setFileSelectionMode(JFileChooser.FILES_ONLY);
//		chooserPane.setOpaque(true);
//		
//		FileChooserListener fcl = new FileChooserListener(chooserPane);
//		chooserPane.addActionListener(fcl);
//		showCustomedDialog(c, title, true, chooserPane);
//		return fcl.getChooseFile();
//	}	
//	
//	public static void showFileSaveDialog(Component c, String title, boolean modal,
//		     JFileChooser savePane, File saveFile) {
//		if(savePane == null) {
//			savePane = new JFileChooser();
//			savePane.setDialogType(JFileChooser.SAVE_DIALOG);
//		}
//		savePane.setSelectedFile(saveFile);
//		savePane.setFileSelectionMode(JFileChooser.FILES_ONLY);
//		savePane.setOpaque(false);
//		
//		FileChooserListener fcl = new FileChooserListener(savePane);
//		savePane.addActionListener(fcl);
//		showCustomedDialog(c, title, true, savePane);
//		
//		File chooseFile = fcl.getChooseFile();
//		if(chooseFile != null) {
//			try {
//				copyBigFile(saveFile, chooseFile); //复制文件
//				showMessageDialog(c, "保存成功", "文件保存成功！");
//			} catch (IOException ex) {
//				// TODO 自动生成的 catch 块
//				showMessageDialog(c, "错误", ex);
//			}
//		}
//	}
	
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
//	public static void showQQFaceDialog(Component c, String title, boolean modal, 
//			JEditorPane editor)
//	{
//		QQFaceDialog dialog = QQFaceDialog.getInstance();
//		if(dialog.getTextEditor() == editor) {
//			dialog.setVisible(true);
//			return;
//		}
//		
//		dialog.setTextEditor(editor);
//	OPanel body = createCustomedPane(dialog, dialog.getDefautFacePanel(), title);
//	dialog.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
//	dialog.setContentPane(body);
//	dialog.pack();
//	dialog.setLocationRelativeTo(c);
//	checkWindowLocation(dialog);
//	dialog.setVisible(true);
//	}
	
	public static void addToolTip(final Component c, final String tipText) {
		final ToolTip toolTip = new ToolTip();
		toolTip.setToolTipText(tipText);
		c.addFocusListener(new ToolTipFocusListener(toolTip));
	}
	
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
	 * 创建带滚动条的面板
	 */
	public static JScrollPane createScrollPane(Component c) {
		return createScrollPane(c, Color.gray);
	}
	public static JScrollPane createScrollPane(Component c, Color barColor) {
		JScrollPane jsp = new JScrollPane(c, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		// 不显示边框，同时设置背景透明
		jsp.setBorder(null);
		jsp.setOpaque(false);
		jsp.setVerticalScrollBar(new OScrollBar(JScrollBar.VERTICAL, barColor));
		jsp.setViewportBorder(null);
		jsp.getViewport().setOpaque(false);
		
		return jsp;
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
}
