package com.original.client.ui.widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JScrollBar;

import com.original.client.util.ChannelConstants;
import com.original.client.util.GraphicsHandler;
import com.original.widget.OScrollBar;
import com.seaglasslookandfeel.widget.SGPanel;
import com.seaglasslookandfeel.widget.SGScrollPane;

public class ImagePane extends SGPanel implements ChannelConstants
{
	private BufferedImage background = null;
	private String initTips = "正在加载图片，请稍等……";
	
	public ImagePane()
	{		
		//设置初始大小，用于显示提示信息initTips
		setPreferredSize(new Dimension(250, 100));
	}
	
	private void adjustImageScale() {
		if(background != null) {
			int width = background.getWidth(),
					height = background.getHeight();
			
			//这里不处理高度
			double scale = width / (double)height;
			if(width > (int) (CHANNELWIDTH * 0.8) )
			{
				width = (int) (CHANNELWIDTH * 0.8);
				height = (int) (width / scale);
				
				//重新生成调整后宽、高度的图像
				BufferedImage newBackground = new BufferedImage(width, height, background.getType());
				Graphics2D g2d = newBackground.createGraphics();
				g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
						RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				
				double scaleX = width / background.getWidth(),
						scaleY = height / background.getHeight();
				g2d.drawRenderedImage(newBackground, 
						AffineTransform.getScaleInstance(scaleX, scaleY));
				g2d.dispose();
				
				background = newBackground;
			}
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		RenderingHints hints = GraphicsHandler.DEFAULT_RENDERING_HINT_ON;
		hints.put(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				
		Graphics2D g2d = GraphicsHandler.optimizeGraphics(g);
		
		int width = getWidth(), height = getHeight();
		if (background != null) {
			int imgWidth = background.getWidth(),
					imgHeight = background.getHeight();
			
			g2d.drawImage(background, (width-imgWidth)/2, (height-imgHeight)/2, this);
		}
		else {
			
			int fontWidth = g2d.getFontMetrics().stringWidth(initTips),
					fontHeight = g2d.getFont().getSize();
			
			g2d.drawString(initTips, (width-fontWidth)/2, (height - fontHeight) / 2);
		}
		
		GraphicsHandler.suspendRendering(g2d);
	}

	public BufferedImage getBackgroundImage() {
		return background;
	}
	public void setBackground(BufferedImage image) {
		this.background = image;
		this.adjustImageScale();
		this.setPreferredSize(new Dimension(background.getWidth(), background.getHeight()));
	}
	public void setBackground(String imageURL) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new URL(imageURL));
		} catch (Exception ex) {
			System.err.println("加载图片失败：" + ex);
		}
		
		if(image != null) setBackground(image);
	}
	
	/**
	 * 如果图片的高度超出预设高度，可以为当前图像面板创建滚动条。
	 */
	public JComponent getScrollImagePane() {
		if(background != null && background.getHeight() > DESKTOPHEIGHT - 30) 
		{
			SGScrollPane scrollPane = new SGScrollPane(this, SGScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					SGScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			
			// 不显示边框，同时设置背景透明
			scrollPane.setBorder(null);
			scrollPane.setOpaque(false);
			scrollPane.setVerticalScrollBar(new OScrollBar(JScrollBar.VERTICAL, Color.gray));
			scrollPane.setViewportBorder(null);
			scrollPane.getViewport().setOpaque(false);
			
			//设置滚动面板的大小
			scrollPane.setPreferredSize(new Dimension(background.getWidth(), 
					DESKTOPHEIGHT - 30));
			
			return scrollPane;
		}
		return this;
	}
}
