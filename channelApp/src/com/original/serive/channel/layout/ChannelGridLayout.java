package com.original.serive.channel.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

/**
 * Channel专用的网格布局，与传统的网格布局不同的是，ChannelGridLayout带有边距设置且单行显示。
 * @author WMS
 * @version 1.1
 * @since 1.0 修正了子控件不可见时，仍有边界(bounds)的Bug，同时增加了面板添加方向(isLeftToRight)的控制
 *
 */
public class ChannelGridLayout implements LayoutManager, java.io.Serializable {
	
	private static final long serialVersionUID = -4382635535050340598L;
	
	/**
     * This is the horizontal gap (in pixels) which specifies the space
     * between columns.  They can be changed at any time.
     * This should be a non-negative integer.
     *
     * @serial
     * @see #getHgap()
     * @see #setHgap(int)
     */
    int hgap;
    /**
     * This is the vertical gap (in pixels) which specifies the space
     * between rows.  They can be changed at any time.
     * This should be a non negative integer.
     *
     * @serial
     * @see #getVgap()
     * @see #setVgap(int)
     */
    int vgap;
    
    static final Insets DEFAULT_INSETS = new Insets(0, 0, 0, 5); 
    Insets insets;
    
    /**
     * When child compoent is invisible, you can set 'autoAdjust' to show or hide its bounds.
     * if true, then hide; else show its bounds.
     */
    boolean autoAdjust = true;
    

    /**
     * Creates a grid layout with a default of one column per component,
     * in a single row.
     * @since JDK1.1
     */
    public ChannelGridLayout() {
	this(0, 0, DEFAULT_INSETS);
    }
    
    public ChannelGridLayout(Insets insets) {
    	this(0, 0, insets);
    }

    /**
     * Creates a grid layout with the specified number of rows and 
     * columns. All components in the layout are given equal size. 
     * <p>
     * In addition, the horizontal and vertical gaps are set to the 
     * specified values. Horizontal gaps are placed between each
     * of the columns. Vertical gaps are placed between each of
     * the rows. 
     * <p>
     * One, but not both, of <code>rows</code> and <code>cols</code> can 
     * be zero, which means that any number of objects can be placed in a 
     * row or in a column. 
     * <p>
     * All <code>GridLayout</code> constructors defer to this one.
     * @param     hgap   the horizontal gap
     * @param     vgap   the vertical gap
     * @exception   IllegalArgumentException  if the value of both
     *			<code>rows</code> and <code>cols</code> is 
     *			set to zero
     */
    public ChannelGridLayout(int hgap, int vgap, Insets insets) {
	this.hgap = hgap;
	this.vgap = vgap;
	this.insets = insets;
    }

    /**
     * Gets the horizontal gap between components.
     * @return       the horizontal gap between components
     * @since        JDK1.1
     */
    public int getHgap() {
	return hgap;
    }
    
    /**
     * Sets the horizontal gap between components to the specified value.
     * @param        hgap   the horizontal gap between components
     * @since        JDK1.1
     */
    public void setHgap(int hgap) {
	this.hgap = hgap;
    }
    
    /**
     * Gets the vertical gap between components.
     * @return       the vertical gap between components
     * @since        JDK1.1
     */
    public int getVgap() {
	return vgap;
    }
    
    /**
     * Sets the vertical gap between components to the specified value.
     * @param         vgap  the vertical gap between components
     * @since        JDK1.1
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
     * <p>
     * The preferred width of a grid layout is the largest preferred 
     * width of all of the components in the container times the number of 
     * columns, plus the horizontal padding times the number of columns 
     * minus one, plus the left and right insets of the target container. 
     * <p>
     * The preferred height of a grid layout is the largest preferred 
     * height of all of the components in the container times the number of 
     * rows, plus the vertical padding times the number of rows minus one, 
     * plus the top and bottom insets of the target container. 
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
     * <p>
     * The minimum width of a grid layout is the largest minimum width 
     * of all of the components in the container times the number of columns, 
     * plus the horizontal padding times the number of columns minus one, 
     * plus the left and right insets of the target container. 
     * <p>
     * The minimum height of a grid layout is the largest minimum height 
     * of all of the components in the container times the number of rows, 
     * plus the vertical padding times the number of rows minus one, plus 
     * the top and bottom insets of the target container. 
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
     * <p>
     * This method reshapes the components in the specified target 
     * container in order to satisfy the constraints of the 
     * <code>GridLayout</code> object. 
     * <p>
     * The grid layout manager determines the size of individual 
     * components by dividing the free space in the container into 
     * equal-sized portions according to the number of rows and columns 
     * in the layout. The container's free space equals the container's 
     * size minus any insets and any specified horizontal or vertical 
     * gap. All components in a grid layout are given the same size. 
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
