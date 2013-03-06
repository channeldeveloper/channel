package com.original.client.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;

/**
 * 垂直方向的网格布局。注意组件的方向可以从上而下(默认)，也可以从下而上
 * @author WMS
 * @version 1.1
 */
public class VerticalGridLayout extends ChannelGridLayout
{
	private static final long serialVersionUID = -6068331424825623059L;
	
	/**
	 * 垂直方向的网格布局方式：TOP_TO_BOTTOM(从上而下，默认)；BOTTOM_TO_TOP(从下而上)
	 */
	public static final int TOP_TO_BOTTOM = 0, //从上而下
			BOTTOM_TO_TOP = 1;//从下而上
	private int direction = TOP_TO_BOTTOM;
	
	/**
	 * 默认的垂直方向的网格布局。从上而下
	 */
	public VerticalGridLayout()
	{
		this(TOP_TO_BOTTOM);
	}
	
	/**
	 * 自定义垂直方式的垂直方向的网格布局
	 * @param direction 垂直方式，{@link #TOP_TO_BOTTOM}和{@link #BOTTOM_TO_TOP}
	 */
	public VerticalGridLayout(int direction)
	{
		this.direction = direction;
	}
	
	/**
	 * 自定义垂直方式和页边距的垂直方向的网格布局
	 * @param direction 垂直方式，{@link #TOP_TO_BOTTOM}和{@link #BOTTOM_TO_TOP}
	 * @param insets 页边距，即上左下右的边距
	 */
	public VerticalGridLayout(int direction, Insets insets)
	{
		this.direction = direction;
		this.insets = insets;
	}
	
	/**
	 * 自定义垂直方式和页边距、水平间距和垂直间距的垂直方向的网格布局
	 * @param direction 垂直方式，{@link #TOP_TO_BOTTOM}和{@link #BOTTOM_TO_TOP}
	 * @param hgap 水平间距，此参数无意义，保留
	 * @param vgap 垂直间距
	 * @param insets 页边距，即上左下右的边距
	 */
	public VerticalGridLayout(int direction, int hgap, int vgap, Insets insets)
	{
		this.direction = direction;
		this.insets = insets;
		this.hgap = hgap;
		this.vgap = vgap;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension preferredLayoutSize(Container parent)
	{
		synchronized (parent.getTreeLock()) {
    		Insets insets = this.insets == null ? parent.getInsets() : this.insets;
    		int ncomponents = parent.getComponentCount();

    		int w = 0;
    		int h = 0;
    		for (int i = 0; i < ncomponents; i++)
    		{
    			Component comp = parent.getComponent(i);
    			Dimension d = comp.getPreferredSize();

    			if (i > 0) h += vgap;
    			h += d.height;

    			if (w < d.width) w = d.width;
    		}
    		return new Dimension(insets.left + insets.right + w + hgap * 2, 
    				insets.top + insets.bottom + h);
    	}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension minimumLayoutSize(Container parent)
	{
		synchronized (parent.getTreeLock())
		{
			Insets insets = this.insets == null ? parent.getInsets() : this.insets;
			int ncomponents = parent.getComponentCount();

			int w = 0;
    		int h = 0;
    		for (int i = 0; i < ncomponents; i++)
    		{
    			Component comp = parent.getComponent(i);
    			Dimension d = comp.getMinimumSize();

    			if (i > 0) h += vgap;
    			h += d.height;

    			if (w < d.width) w = d.width;
    		}
    		return new Dimension(insets.left + insets.right + w + hgap * 2, 
    				insets.top + insets.bottom + h);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void layoutContainer(Container parent)
	{
		Insets insets = this.insets == null ? parent.getInsets() : this.insets;
		int ncomponents = parent.getComponentCount();
		
		int w = parent.getWidth() - (insets.left + insets.right);
//		int h = parent.getHeight() - (insets.top + insets.bottom);

		//这里需要注意方向，从而设置子控件的Bounds
		switch(direction)
		{
		case BOTTOM_TO_TOP:
			for (int c = ncomponents,  x = insets.left, y = insets.top ; c > 0 ; c--, y += vgap) {
				Component comp = parent.getComponent(c-1);
				if(!autoAdjust || comp.isVisible()) {
					Dimension dim = comp.getPreferredSize();
					comp.setBounds(x, y, w, dim.height);

					y += dim.height;
				}
				else {
					y -= vgap;
				}
			}
			break;
			
		case TOP_TO_BOTTOM:
		default:
			for (int c = 0,  x = insets.left, y = insets.top ; c < ncomponents ; c++, y += vgap) {
				Component comp = parent.getComponent(c);
				if(!autoAdjust || comp.isVisible()) {
					Dimension dim = comp.getPreferredSize();
					comp.setBounds(x, y, w, dim.height);

					y += dim.height;
				}
				else {
					y -= vgap;
				}
			}
			break;			
		}		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
    public String toString() {
	return getClass().getName() + "[hgap=" + hgap + ",vgap=" + vgap + "]";
    }
}
