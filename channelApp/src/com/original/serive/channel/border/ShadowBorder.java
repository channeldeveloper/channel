package com.original.serive.channel.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.Area;

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
	public ShadowBorder()
	{
		this(3);
	}
	public ShadowBorder(int thickness)
	{
		this.thickness = thickness;
	}

	@Override
	public void paintBorder(Component c, Graphics gd, int x, int y, int width,
			int height)
	{
		Color oldColor = gd.getColor();
        Graphics2D g2 = GraphicsHandler.optimizeGraphics(gd);
        java.awt.Paint oldPaint = g2.getPaint();
        if(true)
        {
        	Color backColor = new Color(128, 128, 128);
        	Color apc = new Color(backColor.getRed(), backColor.getGreen(), backColor.getBlue(), 0);
            
            Area a = new Area(new Rectangle(x, y, width, height));
            Area b = new Area(new  Rectangle(x + thickness - 1, y + thickness - 1,  //1px为阴影的向右、下的偏移像素值
            		width - thickness*2, height - thickness*2));
            a.subtract(b);
            g2.setClip(a);
                        
            for(int i = 0; i <= thickness; i++)
            {
            	if(i==thickness) {
            		g2.setColor(backColor);
            	}else {
            		g2.setColor(GraphicsHandler.getColor(apc, backColor, thickness, i));
            	}
                g2.fillRoundRect(x + i, y + i, width - 2 * i, height - 2 * i, thickness*2,thickness*2);
            }
            
            g2.setClip(null);
        }
        
        g2.setPaint(oldPaint);
        gd.setColor(oldColor);
//        g2.setRenderingHints(GraphicsHandler.DEFAULT_RENDERING_HINT_OFF);
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
