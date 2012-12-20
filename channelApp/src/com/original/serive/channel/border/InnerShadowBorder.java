package com.original.serive.channel.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.border.AbstractBorder;

import com.original.widget.draw.GeomOperator;
import com.original.widget.draw.OriPainter;
import com.original.widget.model.TextBlockModel;

/**
 * 内阴影边框，一般用于文本框(TextField)或者文本域(TextArea)以及文本面板(TextPane)中。
 * @author WMS
 *
 */
public class InnerShadowBorder extends AbstractBorder
{
	private static final long serialVersionUID = 8309874243859631592L;
	
	private Color backColor = new Color(237,237,237);
	
	public InnerShadowBorder () 
	{
		this(new Color(237, 237, 237));
	}
	
	public InnerShadowBorder(Color backColor)
	{
		this.backColor = backColor;
	}
	
	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height)
	{
		 RoundRectangle2D r2d = new RoundRectangle2D.Double(
	                TextBlockModel.CORNERRADIUS/2,
	               0,// TextBlockModel.CORNERRADIUS/2,
	                width-TextBlockModel.CORNERRADIUS,
	                height-TextBlockModel.CORNERRADIUS,
	                TextBlockModel.CORNERRADIUS,
	                TextBlockModel.CORNERRADIUS);
		 
	        OriPainter.drawAreaBorderWithSingleColor(g, new Area(r2d),
	                TextBlockModel.BORDERCOLOR, 1);
	        
	        //draw the inner Shadow.
	        //calculate the shadow area.
	        Area areaOne = new Area(r2d);
	        Area areaTwo = GeomOperator.offsetCopy(areaOne, 0, 2.4);
	        areaOne.subtract(areaTwo); //areaOne will be the shadow area.
	        //generate the shadow image.
	        BufferedImage shadow = OriPainter.drawGradientInnerShadow(g,
	                areaOne, TextBlockModel.SHADOWCOLOR, backColor,
	                40);
	        //paint the shadow.
	        OriPainter.drawImage(g, shadow, 0, 0);
	}

	/**
     * Returns the insets of the border.
     * @param c the component for which this border insets value applies
     */
	public Insets getBorderInsets(Component c)  {
		return new Insets(1,1, 1, 1);
	}

    /** 
     * Reinitialize the insets parameter with this Border's current Insets. 
     * @param c the component for which this border insets value applies
     * @param insets the object to be reinitialized
     */
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = insets.top = insets.right = insets.bottom = 1;
        return insets;
    }
}
