package com.original.widget.draw;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;

import org.jdesktop.swingx.painter.AbstractAreaPainter;

/**
 * 
 */
public class BackgroudTransparentPainter extends AbstractAreaPainter<Object>
{
	float alpha = 1f; 
	Shape shape;
	/**
	 * Creates a new MattePainter with "null" as the paint used
	 */
	public BackgroudTransparentPainter(float alpha, Shape shape)
	{
		this.alpha = alpha;
		this.shape = shape;
	}

	/**
	 * Create a new MattePainter for the given Paint. This can be a
	 * GradientPaint (the gradient will not grow when the component becomes
	 * larger unless you use the paintStretched boolean property),
	 * TexturePaint, Color, or other Paint instance.
	 * 
	 * @param paint
	 *                Paint to fill with
	 */
	public BackgroudTransparentPainter(Paint paint)
	{
		super(paint);
	}

	/**
	 * Create a new MattePainter for the given Paint. This can be a
	 * GradientPaint (the gradient will not grow when the component becomes
	 * larger unless you use the paintStretched boolean property),
	 * TexturePaint, Color, or other Paint instance.
	 * 
	 * @param paint
	 *                Paint to fill with
	 * @param paintStretched
	 *                indicates if the paint should be stretched
	 */
	public BackgroudTransparentPainter(Paint paint, boolean paintStretched)
	{
		super(paint);
		this.setPaintStretched(paintStretched);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doPaint(Graphics2D g2d, Object component, int width,
			int height)
	{
		Paint p = ((JComponent) component).getBackground();
//		if (alpha == 0.0f)
//		{
//			return ;
//		}

		if (p != null)
		{
			Insets insets = getInsets();
			int w = width - insets.left - insets.right;
			int h = height - insets.top - insets.bottom;

			Composite oldComp = g2d.getComposite();
			
			Composite alphaComp = AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, alpha);
			g2d.setComposite(alphaComp);

			g2d.translate(insets.left, insets.top);
			g2d.setPaint(p);
			g2d.fill(provideShape(g2d, component, w, h));

			g2d.setComposite(oldComp);

		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Shape provideShape(Graphics2D g, Object comp, int width,
			int height)
	{
		if (shape == null)
		{
			return new Rectangle(0, 0, width, height);
		}
		if (shape instanceof RoundRectangle2D.Double)
		{
			((RoundRectangle2D.Double)shape).width = width;
			((RoundRectangle2D.Double)shape).height = height;
		}
		return shape;
	}

}
