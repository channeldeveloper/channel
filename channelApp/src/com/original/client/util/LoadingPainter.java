package com.original.client.util;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;

/**
 * 动画绘制，用于在当前面板中显示加载动画图片(gif)，来模拟一个加载进度以增加用户体验。
 * @author WMS
 *
 */
public class LoadingPainter implements ChannelConstants
{
	private static ImageIcon loadingImage = IconFactory.loadIconByConfig("loading");//动态加载图片
	private volatile boolean isLoading = false;
	
	public void repaint(final Component cp) {
		repaint(cp, null);
	}
	
	public void repaint(final Component cp, final Runnable joinedTask) {
		Runnable loading = new Runnable() {

			@Override
			public void run() {
				paintImpl(cp, joinedTask == null ? 
						null : new LoadingRunnable(joinedTask)
				);
			}
		};
		
		Thread loadingThread = new Thread(loading);
		loadingThread.start();
	}
	
	
	private synchronized void paintImpl(final Component cp, final LoadingRunnable lr) {
		if(lr != null) {
			Thread joinedThread = new Thread(lr); //let joinThread start first!
			joinedThread.start();
		}
		
		int cnt = 0; //由于是动画gif，所以需要循环绘制，否则只是一张静态图片。
		//目前这里绘制10次，来模拟动画效果
		isLoading = true;
		while (isLoading) {

			cp.repaint();
			cnt++;

			try {
				Thread.sleep(100); //这里的时间，可以自己调整。100*10 = 1s
			} catch (InterruptedException ex) {

			}

			if (lr != null && !lr.isRunning()) { //如果加载任务LoadingRunnable不为空，则必须等待其完成后，才结束加载动画
				isLoading = false;
			}
			
			else if (lr == null && cnt >= 10) {//反之，只有加载计数器cnt=10，即加载10次就停止
				isLoading = false;
			}
			
		}
		
		cp.repaint();
	}
	
	public void paint(Graphics g, int width, int height) {
		if(isLoading) {
			Graphics2D g2d = GraphicsHandler.optimizeGraphics(g);

			Composite oldComposite = g2d.getComposite();
			Composite newComposite = AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, 0.5f);
			g2d.setComposite(newComposite);

			g2d.setColor(TRANSLUCENCE_COLOR);
			g2d.fillRect(0, 0, width, height);
			g2d.setComposite(oldComposite);

			Graphics2D gx = (Graphics2D) g.create();
			int imageWidth = loadingImage.getIconWidth(), imageHeight = loadingImage
					.getIconHeight();
			gx.drawImage(loadingImage.getImage(), (width - imageWidth) / 2,
					(height - imageHeight) / 2, null);
			gx.dispose();

			GraphicsHandler.suspendRendering(g2d);
		}
	}	
	
	class LoadingRunnable implements Runnable {
		private Runnable runnable = null;
		private boolean isRunning = false;

		LoadingRunnable(Runnable runnable) {
			this.runnable = runnable;
		}

		@Override
		public void run() {
			if (runnable != null) {
				isRunning = true;
				
				try {
					runnable.run(); //wait running completed!
				}
				finally {
					isLoading = false; //if error or not, isLoading will be false both!
				}
			}
		}

		public boolean isRunning() {
			return isRunning;
		}
	}
}
