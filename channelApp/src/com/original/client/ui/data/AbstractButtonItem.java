package com.original.client.ui.data;

import java.awt.Dimension;
import java.io.Serializable;

import javax.swing.AbstractButton;
import javax.swing.Icon;

/**
 * 按钮属性对象数据类
 * @author WMS
 *
 */
public class AbstractButtonItem implements Serializable
{
	private static final long serialVersionUID = -3786407435229848051L;

	/** 按钮名称  */
	private String text;
	
	/** 按钮名称(国际化) */
	private String actionCommand;
	
	/** 图标 */
	private transient Icon icon;
	
	/** 选中图标或点击后图标 */
	private transient Icon selectedIcon;

	/** 禁用后的图标 */
	private transient Icon disabledIcon;
	
	/** 按钮大小，可以自定义 */
	private transient Dimension size;
	
	private transient AbstractButton source;
	
	public AbstractButtonItem(String text, String actionCommand, Icon icon)
	{
		this(text,actionCommand,icon,null);
	}
	
	public AbstractButtonItem(String text, String actionCommand, Icon icon, Dimension size)
	{
		this(text,actionCommand,icon,null,size);
	}
	
	public AbstractButtonItem(String text, String actionCommand, Icon icon,
			Icon selectedIcon, Dimension size)
	{
		this(text,actionCommand,icon,selectedIcon,null,size);
	}
	
	public AbstractButtonItem(String text, String actionCommand, Icon icon,
			Icon selectedIcon, Icon disabledIcon, Dimension size)
	{
		super();
		this.text = text;
		this.actionCommand = actionCommand;
		this.icon = icon;
		this.selectedIcon = selectedIcon;
		this.disabledIcon = disabledIcon;
		this.size = size;
	}
	
	public String getText()
	{
		return text;
	}
	public void setText(String text)
	{
		this.text = text;
	}

	public String getActionCommand()
	{
		return actionCommand;
	}
	public void setActionCommand(String actionCommand)
	{
		this.actionCommand = actionCommand;
	}

	public Icon getIcon()
	{
		return icon;
	}
	public void setIcon(Icon icon)
	{
		this.icon = icon;
	}

	public Icon getSelectedIcon()
	{
		return selectedIcon;
	}
	public void setSelectedIcon(Icon selectedIcon)
	{
		this.selectedIcon = selectedIcon;
	}

	public Icon getDisabledIcon() 
	{
		return disabledIcon;
	}
	public void setDisabledIcon(Icon disabledIcon) 
	{
		this.disabledIcon = disabledIcon;
	}

	public Dimension getSize()
	{
		return size;
	}
	public void setSize(Dimension size)
	{
		this.size = size;
	}
	public void setSize(int width, int height)
	{
		this.size = new Dimension(width, height);
	}

	public AbstractButton getSource() {
		return source;
	}
	public void setSource(AbstractButton source) {
		this.source = source;
	}
	
	public void setEnabled(boolean enabled) {
		if (source != null) {
			source.setEnabled(enabled);
		}
	}
}
