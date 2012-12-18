package com.original.serive.channel.ui.data;

import java.awt.Dimension;
import java.io.Serializable;

import javax.swing.Icon;

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
	
	private transient Dimension size;
	
	public AbstractButtonItem(String text, String actionCommand, Icon icon)
	{
		this(text,actionCommand,icon,null);
	}
	
	public AbstractButtonItem(String text, String actionCommand, Icon icon, Dimension size)
	{
		this(text,actionCommand,icon,icon,size);
	}
	
	public AbstractButtonItem(String text, String actionCommand, Icon icon,
			Icon selectedIcon, Dimension size)
	{
		super();
		this.text = text;
		this.actionCommand = actionCommand;
		this.icon = icon;
		this.selectedIcon = selectedIcon;
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
}
