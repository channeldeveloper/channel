package com.original.client.layout;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.Box.Filler;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

import com.original.client.border.SingleLineBorder;
import com.original.client.util.ChannelUtil;

/**
 * Channel网格包布局管理器。使用此布局管理器，如果控件已有其他布局(LayoutManager)，会强制替换成GridBagLayout。
 * @author WMS
 * @version 1.1
 * @since 1.0 增加了insets，即子控件的间距设置；anchor，即子控件的摆放位置(东南西北，以及对角线处4个位置)
 */
public class ChannelGridBagLayoutManager
{
	private Insets insets = new Insets(0, 5, 0, 5);
	private GridBagConstraints constraints 
	 = new GridBagConstraints(GridBagConstraints.RELATIVE, 0,
			 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, 
				GridBagConstraints.NONE, insets, 0, 0);
	
	//对齐方式
	public static final String ALIGN_LEFT = "left", ALIGN_RIGHT = "right";
	private String alignment = ALIGN_LEFT;
	
	private JComponent parent = null;
	
	/**
	 * 为控件添加网格包布局管理器。以后添加子控件时，直接使用addComToModel即可。
	 * @param parent
	 */
	public ChannelGridBagLayoutManager(JComponent parent)
	{
		this.parent=parent;
		if( !(this.parent.getLayout() instanceof GridBagLayout))
		{
			this.parent.setLayout(new GridBagLayout());
		}
	}
	
	/**
	 * 添加组件至父面板。组件的宽高固定，不随父面板的宽高改变而改变。
	 * @param com 组件
	 */
	public void addComToModel(Component com)
    {
        addComToModel(com, 1, 1, GridBagConstraints.NONE);
    }
	
    public void addComToModel(Component com, int gridWidth, int gridHeight, int fill)
    {
        addComToModel(com, constraints.gridx + constraints.gridwidth, constraints.gridy, gridWidth, gridHeight, fill);
    }

    public void addComToModel(Component com, int gridX, int gridY, int gridWidth, int gridHeight, int fill)
    {
        constraints.gridx = gridX;
        constraints.gridy = gridY;
        constraints.gridwidth = gridWidth;
        constraints.gridheight = gridHeight;
        switch(fill)
        {
        case GridBagConstraints.NONE: 
            constraints.weightx = 0.0D;
            constraints.weighty = 0.0D;
            break;

        case GridBagConstraints.HORIZONTAL:
            constraints.weightx = 1.0D;
            constraints.weighty = 0.0D;
            break;

        case GridBagConstraints.VERTICAL:
            constraints.weightx = 0.0D;
            constraints.weighty = 1.0D;
            break;

        case GridBagConstraints.BOTH:
        default:
            constraints.weightx = 1.0D;
            constraints.weighty = 1.0D;
            break;
        }
        constraints.fill = fill;
        parent.add(com, constraints);
    }
    
    /**
     * 添加透明填充区域
     * @param horizonWidth 水平宽度
     * @param verticalHeight 垂直高度
     */
    public void addFillerToModel(int horizonWidth, int verticalHeight)
	{
		addComToModel(ChannelUtil.createBlankFillArea(horizonWidth, verticalHeight),
				1, 1, GridBagConstraints.NONE);
	}
	
    /**
     * 添加水平线，同时自动换行
     * @param lineThickness 水平线厚度(>=1)
     * @param lineColor 水平线颜色
     */
	public void addHorizonLineToModel(int lineThickness, Color lineColor) {
		addHorizonLineToModel(lineThickness, lineColor, 1);
	}
	/**
	 * 添加水平线，同时自动换行。可以设置水平线所占N(N=gridWidth)个控件的区域
	 * @param lineThickness 水平线厚度(>=1)
	 * @param lineColor  水平线颜色
	 * @param gridWidth 水平线所占N个控件的区域(N=gridWidth)
	 */
	public void addHorizonLineToModel(int lineThickness, Color lineColor, int gridWidth) {
		addHorizonLineToModel(lineThickness, lineColor, gridWidth, 0);
	}
	/**
	 * 添加水平线，同时自动换行(可以设置下一行是与哪一个控件对齐，默认为0，即与第一个控件对齐)。同时，可以设置水平线所占N(N=gridWidth)个控件的区域
	 * @param lineThickness 水平线厚度(>=1)
	 * @param lineColor  水平线颜色
	 * @param gridWidth 水平线所占N个控件的区域(N=gridWidth)
	 * @param nextLineGridX 设置下一行是与哪一个控件对齐，默认为0，即与第一个控件对齐
	 */
	public void addHorizonLineToModel(int lineThickness, Color lineColor, int gridWidth, int nextLineGridX) {
		Filler filler = ChannelUtil.createBlankFillArea(1, lineThickness < 1 ? 1 : lineThickness);
		filler.setBorder(new SingleLineBorder(SingleLineBorder.BOTTOM, lineColor, false, lineThickness));
		addComToModel(filler, gridWidth, 1, GridBagConstraints.HORIZONTAL);
		newLine(nextLineGridX);
	}
    
    /**
     * 重设GridBagConstraints参数，默认不清空父面板的所有子件
     */
    public void reset() 
    {
    	reset(false);
    }
    /**
     * 重设GridBagConstraints参数
     * @param removeAll 是否也清空parent添加的子件
     */
    public void reset(boolean removeAll) 
    {
    	constraints.gridx=GridBagConstraints.RELATIVE;
    	constraints.gridy=0;
    	constraints.gridwidth=1;
    	constraints.gridheight=1;
    	constraints.weightx=0.0;
    	constraints.weighty=0.0;
    	constraints.anchor=GridBagConstraints.NORTHEAST;
    	constraints.fill= GridBagConstraints.NONE;
    	
    	if(removeAll && parent != null) {
    		parent.removeAll();
    		parent.validate();
    	}
    }
    
    /**
     * 切换至下一行
     * @return
     */
    public GridBagConstraints newLine()
    {
        return newLine(0);
    }
    
    /**
     * 切换至下一行第gridX的位置，即与下一行哪一个控件对齐
     * @param gridX 对齐控件的索引号
     * @return
     */
    public GridBagConstraints newLine(int gridX)
    {
    	constraints.gridx = gridX;
    	constraints.gridwidth = 0;
    	constraints.gridy++;
        return constraints;
    }
    
    /**
     * 隐藏或显示某一行
     * @param gridY 行索引，从0开始编号
     * @param 如果为true，则是显示；否则隐藏
     */
    public void setLineVisible(int gridY, boolean isVisible)
    {
    	if(gridY < 0 || gridY > constraints.gridy)
    		return;
    	
    	GridBagLayout layout = (GridBagLayout)this.parent.getLayout();
    	for(int i=0; i<parent.getComponentCount(); i++)
    	{
    		Component comp = parent.getComponent(i);
    		if(comp.isVisible() != isVisible && layout.getConstraints(comp).gridy == gridY)
    		{
    			comp.setVisible(isVisible);
    			if(comp instanceof JTextComponent && !isVisible) {
    				((JTextComponent) comp).setText(null);
    			}
    		}
    	}
    }
    
    /**
     * 判断某一行是否可见。只有这一行所有控件都不可见时，才为false。
     * @param gridY 行索引，从0开始编号
     */
    public boolean isLineVisible(int gridY)
    {
    	if(gridY < 0 || gridY > constraints.gridy)
    		return false;
    	
    	GridBagLayout layout = (GridBagLayout)this.parent.getLayout();
    	for(int i=0; i<parent.getComponentCount(); i++)
    	{
    		Component comp = parent.getComponent(i);
    		if(comp.isVisible()  && layout.getConstraints(comp).gridy == gridY)
    		{
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * 复制控件一样大小的填充区域
     * @param comp
     * @return
     */
    public Filler copyRegion(JComponent comp)
    {
    	if(comp != null) {
    		Filler filler = (Filler)Box.createRigidArea(comp.getPreferredSize());
    		return filler;
    	}
    	return null;
    }
    /**
     * 添加控件一样大小的填充区域
     * @param comp
     */
    public void addCopyRegion(JComponent comp)
    {
    	if(comp != null) {
    		addComToModel(copyRegion(comp));
    	}
    }
    
	public String getAlignment()
	{
		return alignment;
	}	
	public void setAlignment(String alignment)
	{
		this.alignment = alignment;
	}

	public Insets getInsets()
	{
		return insets;
	}
	public void setInsets(Insets insets)
	{
		this.insets = insets;
		constraints.insets = insets;
	}
	
	public void setAnchor(int anchor)
	{
		constraints.anchor = anchor;
	}	
	public int getAnchor()
	{
		return constraints.anchor;
	}

	public GridBagConstraints getConstraints()
	{
		return constraints;
	}
	public void setConstraints(GridBagConstraints constraints)
	{
		this.constraints = constraints;
	}

	public JComponent getParent()
	{
		return parent;
	}
	public void setParent(JComponent parent)
	{
		this.parent = parent;
	}    
}
