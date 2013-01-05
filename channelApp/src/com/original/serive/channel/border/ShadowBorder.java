package com.original.serive.channel.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;

import com.original.serive.channel.util.GraphicsHandler;

/**
 * 带有阴影效果的边框，使面板看起来更有立体感。
 * @author WMS
 * @version 1.0
 *
 */
public class ShadowBorder extends AbstractBorder
{
	private static final long serialVersionUID = -7295761980519089953L;
	
	private int thickness = 3; //阴影厚度	
	private int radius = 10;//阴影圆角半径
	private float alpha = 0.4f;//阴影透明度
	
	private Color fillColor = Color.white;//面板的背景色
	
	public ShadowBorder()
	{
		this(2);
	}
	
	public ShadowBorder(int thickness)
	{
		this(thickness, 10, 0.4f);
	}
	
	/**
	 * 阴影边框(统一圆角)
	 * @param thickness 阴影厚度
	 * @param shadowRadius 阴影圆角半径
	 * @param alpha 阴影的透明度
	 */
	public ShadowBorder(int thickness, int shadowRadius, float alpha)
	{
		this(thickness, 10, 0.4f, Color.white);
	}
	
	/**
	 * 阴影边框(统一圆角)
	 * @param thickness 阴影厚度
	 * @param shadowRadius 阴影圆角半径
	 * @param alpha 阴影的透明度
	 * @param fillColor 面板的背景色，即当面板使用该边框时，也设置其背景色
	 */
	public ShadowBorder(int thickness, int shadowRadius, float alpha, Color fillColor)
	{
		this.thickness = thickness;
		this.radius = shadowRadius;
		this.alpha = alpha;
		this.fillColor = fillColor;
	}

	public int getThickness() {
		return thickness;
	}
	public void setThickness(int thickness) {
		this.thickness = thickness;
	}

	public int getRadius() {
		return radius;
	}
	public void setRadius(int radius) {
		this.radius = radius;
	}

	public float getAlpha() {
		return alpha;
	}
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public Color getFillColor() {
		return fillColor;
	}
	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	@Override
	public void paintBorder(Component c, Graphics gd, int x, int y, int width,
			int height)
	{		
		Graphics2D g2d = GraphicsHandler.optimizeGraphics(gd);

		//绘制阴影2px
		GraphicsHandler.fillShadow(g2d, thickness, width-thickness, height-thickness, radius, alpha);

		//填充背景
		if(fillColor != null) {
			g2d.setColor(fillColor);
			g2d.fillRoundRect(0, 0, width-thickness, height-thickness, radius, radius);
		}
		
//		GraphicsHandler.suspendRendering(g2d);
	}

	/**
     * Returns the insets of the border.
     * @param c the component for which this border insets value applies
     */
	public Insets getBorderInsets(Component c)  {
		return new Insets(0, 0, 0, 0);
	}

    /** 
     * Reinitialize the insets parameter with this Border's current Insets. 
     * @param c the component for which this border insets value applies
     * @param insets the object to be reinitialized
     */
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = insets.top = insets.right = insets.bottom = 0;
        return insets;
    }
	

}
