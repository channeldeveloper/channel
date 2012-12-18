package com.original.serive.channel.ui.data;

/**
 * 菜单项名称数据类
 * @author WMS
 *
 */
public class MenuItem extends AbstractButtonItem
{
	private static final long serialVersionUID = 3107934469908813114L;
	
	/** 是否自动添加分割线 */
	private boolean addSeparator;
	
	public MenuItem(String text, String actionCommand)
	{
		this(text, actionCommand, false);
	}
	
	public MenuItem(String text, String actionCommand, boolean addSeparator)
	{
		super(text,actionCommand,null);
		this.addSeparator = addSeparator;
	}

	public boolean isAddSeparator()
	{
		return addSeparator;
	}
	public void setAddSeparator(boolean addSeparator)
	{
		this.addSeparator = addSeparator;
	}
}
