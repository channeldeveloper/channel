package com.original.client.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

/**
 * Channel专用的网格布局，与传统的网格布局(GridLayout)不同的是，ChannelGridLayout带有边距设置且单行显示。
 * 即只能从左往右，或者从右往左显示（自动由面板的getComponentOrientation().isLeftToRight()属性来判断）。
 * 默认是从左往右显示。
 * @author WMS
 * @version 1.1
 * @since 1.0 修正了子控件不可见时，仍有边界(bounds)的Bug，同时增加了面板添加方向(isLeftToRight)的控制
 *
 */
public class ChannelGridLayout implements LayoutManager, java.io.Serializable {
	
	private static final long serialVersionUID = -4382635535050340598L;
	
	/**
	 * 水平间距
	 */
    int hgap;

    /**
     * 垂直间距，目前无意义，可以是任意值
     */
    int vgap;
    
    static final Insets DEFAULT_INSETS = new Insets(0, 0, 0, 5); 
    /**
     * 面板的页边距，即使用ChannelGridLayout布局后，可以同时设置面板的上下左右的边距。
     */
    Insets insets;
    
    /**
     * 是否自动调节子控件的显示区域。有时，子控件设置隐藏后，父面板中仍保留子控件的区域（空白区域）。
     * 使用此属性来控制是否自动保留或隐藏该空白区域。根据需要，这里不强制去除(隐藏)。
     * When child component is invisible, you can set 'autoAdjust' to show or hide its bounds.
     * if true, then hide; else show its bounds. 
     */
    boolean autoAdjust = true;
    

    /**
     * 使用默认页边距的网格布局
     */
    public ChannelGridLayout() {
	this(0, 0, DEFAULT_INSETS);
    }
    
    /**
     * 自定义页边距的网格布局
     * @param insets 页边距，即上左下右的边距
     */
    public ChannelGridLayout(Insets insets) {
    	this(0, 0, insets);
    }

    /**
     * 自定义页边距、水平和垂直间距的网格布局
     * @param hgap 水平间距，可以<0
     * @param vgap 垂直间距，此参数为意义，保留
     * @param insets 页边距，即上左下右的边距
     */
    public ChannelGridLayout(int hgap, int vgap, Insets insets) {
    	this.hgap = hgap;
    	this.vgap = vgap;
    	this.insets = insets;
    }

    /**
     * 获取控件间的水平间距
     * @return       the horizontal gap between components
     */
    public int getHgap() {
	return hgap;
    }
    
    /**
     * 设置控件间的水平间距
     * @param        hgap   the horizontal gap between components
     */
    public void setHgap(int hgap) {
	this.hgap = hgap;
    }
    
    /**
     * 获取控件间的垂直间距
     * @return       the vertical gap between components
     */
    public int getVgap() {
	return vgap;
    }
    
    /**
     * 设置控件间的垂直间距
     * @param         vgap  the vertical gap between components
     */
    public void setVgap(int vgap) {
	this.vgap = vgap;
    }

    public Insets getInsets()
	{
		return insets;
	}
	public void setInsets(Insets insets)
	{
		this.insets = insets;
	}
	
	public boolean isAutoAdjust() 
	{
		return autoAdjust;
	}
	public void setAutoAdjust(boolean autoAdjust) 
	{
		this.autoAdjust = autoAdjust;
	}

	/**
     * Adds the specified component with the specified name to the layout.
     * @param name the name of the component
     * @param comp the component to be added
     */
    public void addLayoutComponent(String name, Component comp) {
    }

    /**
     * Removes the specified component from the layout. 
     * @param comp the component to be removed
     */
    public void removeLayoutComponent(Component comp) {
    }

    /** 
     * Determines the preferred size of the container argument using 
     * this grid layout. 
     * 
     * @param     parent   the container in which to do the layout
     * @return    the preferred dimensions to lay out the 
     *                      subcomponents of the specified container
     * @see       java.awt.GridLayout#minimumLayoutSize 
     * @see       java.awt.Container#getPreferredSize()
     */
    public Dimension preferredLayoutSize(Container parent) {
    	synchronized (parent.getTreeLock()) {
    		Insets insets = this.insets == null ? parent.getInsets() : this.insets;
    		int ncomponents = parent.getComponentCount();

    		int w = 0;
    		int h = 0;
    		for (int i = 0; i < ncomponents; i++)
    		{
    			Component comp = parent.getComponent(i);
    			Dimension d = comp.getPreferredSize();

    			if (i > 0) w += hgap;
    			w += d.width;

    			if (h < d.height) h = d.height;
    		}
    		return new Dimension(insets.left + insets.right + w, 
    				insets.top + insets.bottom + h + vgap * 2);
    	}
    }

    /**
     * Determines the minimum size of the container argument using this 
     * grid layout. 
     *  
     * @param       parent   the container in which to do the layout
     * @return      the minimum dimensions needed to lay out the 
     *                      subcomponents of the specified container
     * @see         java.awt.GridLayout#preferredLayoutSize
     * @see         java.awt.Container#doLayout
     */
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

				if (i > 0) w += hgap;
				w += d.width;
				
				if (h < d.height) h = d.height;
			}
			return new Dimension(insets.left + insets.right + w, 
					insets.top + insets.bottom + h + vgap * 2);
		}
	}

    /** 
     * Lays out the specified container using this layout. 
     *  
     * @param      parent   the container in which to do the layout
     * @see        java.awt.Container
     * @see        java.awt.Container#doLayout
     */
	public void layoutContainer(Container parent)
	{
		Insets insets = this.insets == null ? parent.getInsets() : this.insets;
		int ncomponents = parent.getComponentCount();
		
//		int w = parent.getWidth() - (insets.left + insets.right);
		int h = parent.getHeight() - (insets.top + insets.bottom);
		
		boolean leftToRight = parent.getComponentOrientation().isLeftToRight();
		if(leftToRight) //是从左往右的方向
		{

			for (int c = 0,  x = insets.left, y = insets.top ; c < ncomponents ; c++, x += hgap) {
				Component comp = parent.getComponent(c);
				if(!autoAdjust || comp.isVisible()) {
					Dimension dim = comp.getPreferredSize();
					comp.setBounds(x, y, dim.width, h);

					x += dim.width;
				}
				else {
					x -= hgap;
				}
			}
		}
		else {
			for (int c = ncomponents,  x = insets.left, y = insets.top ; c > 0 ; c++, x += hgap) {
				Component comp = parent.getComponent(c-1);
				if(!autoAdjust || comp.isVisible()) {
					Dimension dim = comp.getPreferredSize();
					comp.setBounds(x, y, dim.width, h);

					x += dim.width;
				}
				else {
					x -= hgap;
				}
			}
		}
	}
    
    /**
     * Returns the string representation of this grid layout's values.
     * @return     a string representation of this grid layout
     */
    public String toString() {
	return getClass().getName() + "[hgap=" + hgap + ",vgap=" + vgap + "]";
    }
}
