package com.original.serive.channel.border;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

/**
 * 虚线框，同样分上下左右四个方向。
 * @author WMS
 *
 */
public class DottedLineBorder extends SingleLineBorder
{
	private static final long serialVersionUID = -2114891558647426825L;
	
	private float[] dashparams = new float[]{3f,4f}; //虚线的长短控制。如果有多个，表示虚线长短、间距等，详见API文档关于BasicStroke。
	
	/**
	 * 绘制虚线框
	 * @param direction 虚线方向
	 */
	public DottedLineBorder(int direction)
	{
		super(direction, Color.gray);
	}
	
	/**
	 * 绘制虚线框
	 * @param direction 虚线方向
	 * @param color 颜色
	 */
	public DottedLineBorder(int direction, Color color)
	{
		super(direction, Color.gray, false);
	}
	
	/**
	 * 绘制虚线框
	 * @param direction 虚线方向
	 * @param color 颜色
	 * @param dashparams   虚线的长短控制。如果有多个，表示虚线长短、间距等，详见API文档关于BasicStroke。
	 */
	public DottedLineBorder(int direction, Color color, float[] dashparams)
	{
		super(direction, Color.gray, false);
		this.dashparams = dashparams;
	}

	/**
	 * 自定义虚线的绘制方法
	 */
	protected void drawLine(Graphics2D g2d, int x1, int y1, int x2, int y2)
	{
		Stroke stroke = g2d.getStroke();
		g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, dashparams, 0));
		super.drawLine(g2d, x1, y1, x2, y2);
		g2d.setStroke(stroke);
	}

}
