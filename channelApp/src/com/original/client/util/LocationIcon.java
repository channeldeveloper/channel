package com.original.client.util;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * 带有定位效果的图标，可以随时定位图标的位置(location)。
 * @author WMS
 * @version 1.0
 */
public class LocationIcon implements Icon
{
	private int x_pos; //x坐标
	private int y_pos; //y坐标
	private int width; //宽度
	private int height;//高度
	private Icon icon; //图标
	private Rectangle bounds;//边界

	public LocationIcon(Icon icon)
	{
		this.icon = icon;
		width = 16;
		height = 16;
	}

	public LocationIcon(URL url)
	{
		this(new ImageIcon(url));
	}
	
	public LocationIcon(byte[] imgBytes)
	{
		this(new ImageIcon(imgBytes));
	}

	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		this.x_pos = x;
		this.y_pos = y;
		
		if (icon != null)
		{
			icon.paintIcon(c, g, x, y);
		}
	}

	public void setIcon(Icon icon)
	{
		this.icon = icon;
	}
	
	public int getIconWidth()
	{
		return width;
	}
	public int getIconHeight()
	{
		return height;
	}

	public Rectangle getBounds()
	{
		if (bounds == null)
			return new Rectangle(x_pos, y_pos, width, height);
		return bounds;
	}
	public void setBounds(int x, int y, int w, int h)
	{
		bounds = new Rectangle(x, y, w, h);
	}
	public void setBounds(Rectangle bounds)
	{
		this.bounds = bounds;
	}
	public boolean contains(Point p)
	{
		return getBounds().contains(p);
	}

	public ImageIcon getIcon()
	{
		return (ImageIcon)icon;
	}
	public Image getImage() 
	{
		return ((ImageIcon)icon).getImage();
	}
	
	public int getWidth() 
	{
		return ( (ImageIcon)icon).getIconWidth();
	}
	public int getHeight() 
	{
		return ( (ImageIcon)icon).getIconHeight();
	}
}
