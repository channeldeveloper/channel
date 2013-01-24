package com.original.client.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

import com.original.widget.draw.OriPainter;

/**
 * 图形操作通用类，用于颜色转换，生成图像等操作。若有其他功能，再添加。
 * @author WMS
 * 
 */
public class GraphicsHandler
{
	public static final RenderingHints DEFAULT_RENDERING_HINT_ON = 
			new RenderingHints(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON); 
	
	public static final RenderingHints DEFAULT_RENDERING_HINT_OFF = 
			new RenderingHints(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_OFF); 
	/**
	 * 生成带有默认渲染效果(抗锯齿)的图形对象
	 * @param g
	 * @return
	 */
	public static Graphics2D optimizeGraphics(Graphics g)
	{
return optimizeGraphics(g, DEFAULT_RENDERING_HINT_ON);
	}
	
	/**
	 * 生成带有默认渲染效果的图形对象
	 * @param g 原有图形对象
	 * @param hints 渲染效果
	 * @return
	 */
	public static Graphics2D optimizeGraphics(Graphics g, RenderingHints hints)
	{
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHints(hints);
		return g2d;
	}
	
	/**
	 * 停止图形渲染效果，以减少资源占用
	 * @param g2d
	 */
	public static void suspendRendering(Graphics2D g2d)
	{
		suspendRendering(g2d, DEFAULT_RENDERING_HINT_OFF);
	}
	
	/**
	 * 停止图形渲染效果，以减少资源占用
	 * @param g2d
	 * @param hints 渲染效果，一般为关闭状态，如{@link RenderingHints#VALUE_ANTIALIAS_OFF}等
	 */
	public static void suspendRendering(Graphics2D g2d, RenderingHints hints)
	{
		g2d.setRenderingHints(hints);
	}
	
	//用于生成带有阴影效果的颜色
	public static Color getColor(Color backColor, Color shadowColor, int thick, int i)
	{
		return new Color(
				getColorInt(backColor.getRed(), shadowColor.getRed(), thick, i), 
				getColorInt(backColor.getGreen(), shadowColor.getGreen(), thick, i), 
				getColorInt(backColor.getBlue(), shadowColor.getBlue(), thick, i),
				getColorInt(backColor.getAlpha(), shadowColor.getAlpha(), thick, i));
	}

	//计算颜色的基色(r,g,b,a)相差值的平均块中(a-b)/thick的第i块，一般用于颜色的渐变过渡
	public static int getColorInt(int a, int b, int thick, int i)
	{
		int cc = (int) (a >= b ? a - (a-b)*(i/(double)thick) :
			a + (b - a)*(i/(double)thick));
		return cc;
	}
	
	/**
	 * 填充阴影效果(四周填充)
	 * @param g2d 图形对象
	 * @param thick 阴影厚度
	 * @param width 
	 * @param height
	 * @param radius 阴影(圆角矩形的半径)
	 */
	public static void fillShadow(Graphics2D g2d, int thick, int width, int height, int radius)
	{
		Color backColor = new Color(128, 128, 128);
    	Color apc = new Color(backColor.getRed(), backColor.getGreen(), backColor.getBlue(), 0);
    	
		for(int i=0; i<=thick;i++)
		{
			if(i==thick) {
        		g2d.setColor(backColor);
        	}else {
        		g2d.setColor(GraphicsHandler.getColor(apc, backColor, thick*2, i));
        	}
            g2d.fillRoundRect( i,  i, width - 2 * i, height - 2 * i, radius*2,radius*2);
		}
	}
	
	/**
	 * 填充阴影效果(右下角填充)
	 * @param g2d 图像对象
	 * @param thick 阴影厚度
	 * @param width
	 * @param height
	 * @param radius 阴影(圆角矩形的半径)
	 * @param alpha 阴影透明度
	 */
	public static void fillShadow(Graphics2D g2d, int thick, int width, int height, int radius, float alpha)
	{
		RoundRectangle2D r2d= new RoundRectangle2D.Double(
                thick,
                thick,
                width,
                height,
                radius,
                radius);
        Area fillArea = new Area(r2d);
        OriPainter.drawDropShadow(g2d, fillArea, Color.black,
               thick, thick,-Math.PI/4, alpha);
	}
}