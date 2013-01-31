package com.original.client.border;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.Paint;

import javax.swing.border.AbstractBorder;

import com.original.client.util.GraphicsHandler;

/**
 * 单线边框，起分割线的作用。可以有上、左、下、右四个方向。
 * @author WMS
 * @version 1.1
 * @since 1.0 增加了带有渐变效果
 */
public class SingleLineBorder extends AbstractBorder
{
	private static final long serialVersionUID = -3126026196602525188L;

	public static final int TOP = 0,
			LEFT = 1,
			RIGHT = 2,
			BOTTOM = 3;
	
	protected int borderDirection = TOP; //Border的方向
	protected Color borderColor = Color.gray;//Border的颜色
	protected boolean drawGradient = false;//是否带有渐变效果
	protected Color edgeColor = new Color(255, 255, 255, 0);
	
	private int thickness = 1;
	
	public SingleLineBorder(int direction)
	{
		this(direction, Color.gray);
	}
	
	public SingleLineBorder(int direction, Color color)
	{
		this(direction, Color.gray, false);
	}
	
	public SingleLineBorder(int direction, Color color, boolean drawGradient)
	{
		this(direction, color, drawGradient, 1);
	}
	
	public SingleLineBorder(int direction, Color color, boolean drawGradient, int thickness)
	{
		this.borderDirection = direction;
		this.borderColor = color;
		this.drawGradient = drawGradient;
		this.thickness = thickness;
	}
	
	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height)
	{
		Graphics2D g2d = GraphicsHandler.optimizeGraphics(g);
		
		switch(borderDirection)
		{
		case LEFT:
			if (!drawGradient)
			{
				g2d.setColor(borderColor);

			} else
			{
				Paint paint = new LinearGradientPaint(x, y, x, y + height,
						new float[] { 0.0f, 0.5f, 1.0f }, 
						new Color[] { edgeColor, borderColor, edgeColor });
				g2d.setPaint(paint);
			}
			drawLine(g2d, x+thickness, y, x+thickness, y + height);
			break;
		case BOTTOM:
			if (!drawGradient)
			{
				g2d.setColor(borderColor);

			} else
			{
				Paint paint = new LinearGradientPaint(x, y+height, x+width, y+height,
						new float[] { 0.0f, 0.5f, 1.0f }, 
						new Color[] { edgeColor, borderColor, edgeColor });
				g2d.setPaint(paint);
			}
			drawLine(g2d, x, y+height-thickness, x+width, y+height-thickness); 
			break;
		case RIGHT:
			if (!drawGradient)
			{
				g2d.setColor(borderColor);

			} else
			{
				Paint paint = new LinearGradientPaint(x+width, y, x+width, y+height,
						new float[] { 0.0f, 0.5f, 1.0f }, 
						new Color[] { edgeColor, borderColor, edgeColor });
				g2d.setPaint(paint);
			}
			drawLine(g2d, x+width-thickness, y, x+width-thickness, y+height); 
			break;
		case TOP:
		default:
			if (!drawGradient)
			{
				g2d.setColor(borderColor);

			} else
			{
				Paint paint = new LinearGradientPaint(x, y, x+width, y,
						new float[] { 0.0f, 0.5f, 1.0f }, 
						new Color[] { edgeColor, borderColor, edgeColor });
				g2d.setPaint(paint);
			}
			drawLine(g2d, x, y+thickness, x+width, y+thickness); 
			break;
		}
		
		GraphicsHandler.suspendRendering(g2d);
	}
	
	/**
	 * 绘制直线方法。子类可以覆写此方法，来绘制不同样式的直线
	 * @param g2d 图形对象
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	protected void drawLine(Graphics2D g2d, int x1, int y1, int x2, int y2)
	{
		if(thickness > 1) {
			g2d.setStroke(new BasicStroke(thickness));
		}
		g2d.drawLine(x1, y1, x2, y2);
	}

	/**
     * Returns the insets of the border.
     * @param c the component for which this border insets value applies
     */
	public Insets getBorderInsets(Component c)  {
		switch(borderDirection)
		{
		case TOP:
			return new Insets(1, 0, 0, 0);
		case LEFT:
			return new Insets(0, 1, 0, 0);
		case BOTTOM:
			return new Insets(0, 0, 1, 0);
		case RIGHT:
			return new Insets(0, 0, 0, 1);
		}
		return new Insets(0, 0, 0, 0);
		
	}

    /** 
     * Reinitialize the insets parameter with this Border's current Insets. 
     * @param c the component for which this border insets value applies
     * @param insets the object to be reinitialized
     */
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = insets.top = insets.right = insets.bottom = 0;
        switch(borderDirection)
		{
		case TOP:
			insets.top = 1; break;
		case LEFT:
			insets.left= 1; break;
		case BOTTOM:
			insets.bottom=1; break; 
		case RIGHT:
			insets.right =1; break;
		}
        return insets;
    }

}
