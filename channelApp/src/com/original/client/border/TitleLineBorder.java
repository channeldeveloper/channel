package com.original.client.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.ImageIcon;

import com.original.client.util.LocationIcon;

/**
 * 带有标题、图标和底部分割线的边框
 * @author WMS
 */
public class TitleLineBorder extends SingleLineBorder {
	private static final long serialVersionUID = -5095711498132608466L;

	private String title; // 标题
	private ImageIcon titleIcon;// 标题图标
	private Color titleColor; // 标题颜色
	
	private LocationIcon cornerIcon;// 边角按钮，一般为关闭按钮
	private int marginLeft = 0, marginRight = 0; //边框的左右边距（上下边距自动调整）

	public TitleLineBorder(String title) {
		this(title, null);
	}

	public TitleLineBorder(String title, ImageIcon titleIcon) {
		this(title, titleIcon, null);
	}

	public TitleLineBorder(String title, ImageIcon titleIcon,
			LocationIcon cornerIcon) {
		this(title, titleIcon, cornerIcon, Color.black, Color.black);
	}
	
	public TitleLineBorder(String title, ImageIcon titleIcon,
			LocationIcon cornerIcon, int borderDirection, int borderThickness) {
		this(title, titleIcon, cornerIcon, Color.black, Color.black, borderDirection, borderThickness);
	}

	public TitleLineBorder(String title, ImageIcon titleIcon,
			LocationIcon cornerIcon, Color lineColor, Color titleColor) {
		this(title, titleIcon, cornerIcon, lineColor, titleColor, BOTTOM, 1);
	}
	
	public TitleLineBorder(String title, ImageIcon titleIcon,
			LocationIcon cornerIcon, Color lineColor, Color titleColor, int borderDirection, int borderThickness) {
		super(borderDirection, lineColor, false, borderThickness);
		
		this.title = title;
		this.titleIcon = titleIcon;
		this.titleColor = titleColor;
		
		this.cornerIcon = cornerIcon;
	}
	
	//计算边框顶部的高度，由图标和字体的高度计算而来
	private int calcaulateBorderTopHeight(Component c) {
		return calcaulateBorderTopHeight(c.getFontMetrics(c.getFont()));
	}
	private int calcaulateBorderTopHeight(FontMetrics fm) {
		int topHeight = Math.max(Math.max(
				titleIcon == null ? 0 : titleIcon.getIconHeight(),
				title == null ? 0 : fm.getHeight()
				), 
				cornerIcon == null ? 0 : cornerIcon.getHeight()
		);
		return topHeight;
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		super.paintBorder(c, g, x, y, width, height);
		
		Graphics2D g2d = (Graphics2D) g;
		Font font = g2d.getFont();
		FontMetrics fontMetrics = g2d.getFontMetrics(font);

		int topHeight = calcaulateBorderTopHeight(fontMetrics); // 顶部的高度

		if (titleIcon != null && title != null) {
			//绘制标题图标
			int titleIconX = x, titleIconY = y + (topHeight - titleIcon.getIconHeight()) / 2;
			g.drawImage(titleIcon.getImage(), titleIconX, titleIconY, null);
			
			//绘制标题
			g2d.setFont(font.deriveFont(Font.BOLD).deriveFont(16F));
			g2d.setColor(titleColor);
			g.drawString(title, titleIcon.getIconWidth() + 10, y + (topHeight - fontMetrics.getHeight()/2) );
		} else if (title != null) {
			
			//绘制标题
			g2d.setFont(font.deriveFont(Font.BOLD).deriveFont(16F));
			g2d.setColor(titleColor);
			g.drawString(title, x, y + (topHeight - fontMetrics.getHeight()/2));
		}

		g2d.setFont(font);
		if (cornerIcon != null) { //绘制边角图标
			int cornerIconX =x + width - cornerIcon.getWidth(), 
					cornerIconY = y + (topHeight - cornerIcon.getHeight()) / 2;

			g.drawImage(cornerIcon.getImage(), cornerIconX, cornerIconY, null);
			cornerIcon.setBounds(cornerIconX, cornerIconY, 
					cornerIcon.getWidth(), cornerIcon.getHeight());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void drawLine(Graphics2D g2d, int x1, int y1, int x2, int y2) {
		switch(borderDirection) {
		case TOP:
			int topHeight = calcaulateBorderTopHeight(g2d.getFontMetrics()) + 10;
			super.drawLine(g2d, x1, y1+topHeight, x2, y2+topHeight);
			break;

		default:
			super.drawLine(g2d, x1, y1, x2, y2);
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Insets getBorderInsets(Component c) {
		int topHeight = 	calcaulateBorderTopHeight(c) +
				(borderDirection == TOP ? 10 : 0);
		
		return new Insets(topHeight + 10, marginLeft, 10, marginRight);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Insets getBorderInsets(Component c, Insets insets) {
		int topHeight = calcaulateBorderTopHeight(c) +
				(borderDirection == TOP ? 10 : 0);
		
		insets.top = topHeight + 10;
		insets.left = marginLeft;
		insets.bottom = 10;
		insets.right = marginRight;
		return insets;
	}

	public void setMarginLeft(int marginLeft) {
		this.marginLeft = marginLeft;
	}
	
	public void setMarginRight(int marginRight) {
		this.marginRight = marginRight;
	}

}
