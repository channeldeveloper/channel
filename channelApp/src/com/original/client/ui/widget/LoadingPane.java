package com.original.client.ui.widget;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.RootPaneContainer;

import com.original.client.util.ChannelConstants;
import com.original.client.util.GraphicsHandler;
import com.original.client.util.IconFactory;

/**
 * 加载动画面板
 * @author WMS
 *
 */
public class LoadingPane extends JPanel implements ChannelConstants
{
	private ImageIcon loadingImage = IconFactory.loadIconByConfig("loading");//动态加载图片
	
	private Dimension size = new Dimension(CHANNELWIDTH
			, DESKTOPHEIGHT);
	
	private JComponent invoker;//调用当前加载动画面板的面板
	private RootPaneContainer rp;
	
	private Lock loadingLock = new ReentrantLock();
	
	public LoadingPane(JComponent invoker) {
		this.invoker = invoker;
		this.setPreferredSize(size);
	}

	private void setRootPaneContainer() {
		if (invoker == null || rp != null)
			return;

		if (invoker instanceof RootPaneContainer)
			rp = (RootPaneContainer) invoker;

		Component parent = invoker.getParent();
		while (!(parent instanceof RootPaneContainer))
			parent = parent.getParent();

		rp = (RootPaneContainer) parent;
	}
	
	public void loading()  {
		boolean isLock = false;
		try {
			if (isLock = loadingLock.tryLock()) {
				loadIt();
			}
		} finally {
			if (isLock) {
				loadingLock.unlock();
			}
		}
	}
	
	private void loadIt() 
	{
		if(rp == null)
			setRootPaneContainer();
		
		if (rp != null) {
			rp.setGlassPane(this);
			this.setVisible(true);
			this.setCursor(TEXT_CURSOR);
			this.repaint();

			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
			}

			this.setCursor(DEFAULT_CURSOR);
			this.setVisible(false);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = GraphicsHandler.optimizeGraphics(g);

		Composite oldComposite = g2d.getComposite();
		Composite newComposite = AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, 0.2f);
		g2d.setComposite(newComposite);

		int width = this.getWidth(), height = this.getHeight();
		g2d.setColor(TRANSLUCENCE_COLOR);
		g2d.fillRect(0, 0, width, height);
		g2d.setComposite(oldComposite);

		Graphics2D gx = (Graphics2D) g.create();
		gx.setRenderingHints(GraphicsHandler.IMAGE_RENDERING_HINT_ON);
		int imageWidth = loadingImage.getIconWidth(), imageHeight = loadingImage
				.getIconHeight();
		gx.drawImage(loadingImage.getImage(), (width - imageWidth) / 2,
				(height - imageHeight) / 2, this);
		gx.dispose();

		GraphicsHandler.suspendRendering(g2d);
	}
	
}
