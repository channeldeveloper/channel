package com.original.serive.channel.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.original.serive.channel.util.ChannelConstants;
import com.original.serive.channel.util.ChannelUtil;

/**
 * Channel显示图片专用对话框
 */
public class ChannelImageDialog extends JDialog
{
	private int imgWidth, imgHeight;// 图片的宽，高
	private String imgURL;// 图片的地址
	private BufferedImage bi;// 缓冲区的图片
	
	private JLabel tip;// 提示标签
	private ShowThread showThread;

	public ChannelImageDialog(String imgURL) {
		setLayout(new BorderLayout());
		setUndecorated(true);
		setSize(300, 200);
		setLocationRelativeTo(getOwner());
		getRootPane().setWindowDecorationStyle(JRootPane.NONE); //不显示标题栏

		this.imgURL = imgURL;
		tip = new JLabel("正在载入图片...退出请按Esc");
		tip.setFont(ChannelConstants.DEFAULT_FONT);
		tip.setHorizontalAlignment(JLabel.CENTER);
		add(tip);
		
		//Esc键监听
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					if (showThread != null && showThread.isAlive()) {
						showThread.interrupt();
					}
					dispose();
				}
			}
		});
		
		//图片拖动监听
		ImgDragListener listener = new ImgDragListener(this);
		addMouseListener(listener);
		addMouseMotionListener(listener);
		showThread = new ShowThread();
		showThread.start();
		
		setModal(true);
		setVisible(true);
	}

	/**
	 * 显示图片的线程
	 */
	private class ShowThread extends Thread {
		public void run() {
			try {
				bi = javax.imageio.ImageIO.read(new URL(imgURL));
			} catch (IOException ex) {
				ex.printStackTrace();
				tip.setText("载入失败！请稍后再试。");
			}
			
			if (bi == null) {
				tip.setText("载入失败！请稍后再试。");
				return;
			}
			
			//添加图片加载面板，同时移除提示标签
			JPanel imgPane = createImgPane();
			
			if (tip != null) {
				remove(tip);
			}
			imgHeight = bi.getHeight();
			imgWidth = bi.getWidth();
			imgPane.setSize(new Dimension(imgWidth, imgHeight));
			setSize(new Dimension(imgWidth, imgHeight));
			
			Dimension dm = Toolkit.getDefaultToolkit().getScreenSize();
			if (imgHeight > dm.height) {
				setLocation((dm.width - imgWidth) / 2, 0);
			} else {
				setLocationRelativeTo(getOwner());
			}
			add(imgPane);
			validate();
			
			// 窗口的鼠标事件处理
			addMouseListener(new MouseAdapter() { 
				public void mousePressed(MouseEvent event) { // 点击鼠标
					triggerEvent(event); // 调用triggerEvent方法处理事件
				}
				public void mouseReleased(MouseEvent event) { // 释放鼠标
					triggerEvent(event);
				}
				private void triggerEvent(MouseEvent event) { // 处理事件
					if (event.isPopupTrigger()) { // 如果是弹出菜单事件(根据平台不同可能不同)
						JPopupMenu popupMenu = createMenu();
						popupMenu.show(event.getComponent(), event.getX(), event.getY()); // 显示菜单
					}
				}
			});
		}
	}
	
	/**
	 * 创建图片加载面板
	 * @return
	 */
	private JPanel createImgPane() {
		return new JPanel()  {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				if (bi != null) {
					g2d.drawImage(bi, 0, 0, this);
				}
			}
		};
	}

	/**
	 * 创建右键菜单
	 */
	private JPopupMenu createMenu() {
		final JFileChooser saveDialog = new JFileChooser();// 保存对话框
		saveDialog.setDialogTitle("选择存放路径");
		final String fileExtendsion = imgURL.substring(imgURL.lastIndexOf(".") + 1);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("图片文件("
				+ fileExtendsion + ")", fileExtendsion);
		saveDialog.setFileFilter(filter);
		File file = new File("未命名." + fileExtendsion);
		saveDialog.setSelectedFile(file);

		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.setFont(ChannelConstants.DEFAULT_FONT.deriveFont(12F));
		
		JMenuItem saveItem = new JMenuItem("保存");
		saveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = saveDialog.showSaveDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					try {

						if (saveDialog.getSelectedFile().exists()) {
							int yesNo = JOptionPane.showConfirmDialog(null,
									"此位置已包含同名文件！是否替换此文件？", "选择",
									JOptionPane.YES_NO_CANCEL_OPTION);
							if (yesNo != JOptionPane.YES_OPTION) {
								return;
							}
						}
						if (ImageIO.write(bi, fileExtendsion, saveDialog.getSelectedFile())) {
							JOptionPane.showMessageDialog(null, "成功保存图片");
						} else {
							JOptionPane.showMessageDialog(null, "保存图片失败！",
									"错误！", JOptionPane.ERROR_MESSAGE);
						}
					} catch (IOException ex) {
						JOptionPane.showMessageDialog(null, "保存图片失败！", "错误！",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		JMenuItem originalItem = new JMenuItem("原图");
		originalItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				ChannelUtil.showBrowser(getOwner(), imgURL.replace("bmiddle", "large"));
				dispose();
			}
		});
		
		JMenuItem exitItem = new JMenuItem("关闭");
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		popupMenu.add(saveItem);
		popupMenu.addSeparator();// 分割线
		popupMenu.add(originalItem);
		popupMenu.addSeparator();// 分割线
		popupMenu.add(exitItem);
		return popupMenu;
	}

	/**
	 * 图片拖动的监听器
	 */
	class ImgDragListener implements MouseInputListener {// 鼠标事件处理
		private JDialog owner;
		Point point = new Point(0, 0); // 坐标点

		public ImgDragListener(JDialog owner) {
			this.owner = owner;
		}

		public void mouseDragged(MouseEvent e) {
			Point newPoint = SwingUtilities.convertPoint(owner, e.getPoint(), getParent());
			setLocation(getX() + (newPoint.x - point.x), 
					getY() + (newPoint.y - point.y));
			point = newPoint; // 更改坐标点
		}

		public void mousePressed(MouseEvent e) {
			point = SwingUtilities.convertPoint(owner, e.getPoint(), owner.getParent()); // 得到当前坐标点
		}

		public void mouseMoved(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
		}
	}
}