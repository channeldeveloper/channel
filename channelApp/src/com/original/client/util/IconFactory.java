package com.original.client.util;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

/**
 * 图标工厂，用于加载图标和获取图标的路径(可能在本地或jar包上下文中)。图标加载后，立即放入内存中。
 * 目前的图标的路径都是在{@link IconFactory#IMAGE_ROOT}下，请注意！！
 * @author WMS
 *
 */
public class IconFactory
{
	private static Map<String, ImageIcon> iconCache = new HashMap<String, ImageIcon>();
	public static final String IMAGE_ROOT = ChannelConfig.getPropValue("iconRoot");
	
	/**
	 * 加载图标，图标的固定路径为{@link #IMAGE_ROOT}
	 * @param iconName 图标名称
	 * @return
	 */
	public static ImageIcon loadIcon(String iconName)
	{
		ImageIcon icon = iconCache.get(iconName);
		if(icon == null) {
			icon = new ImageIcon(getIconURL(iconName));
			iconCache.put(iconName, icon);
		}
		return icon;
	}
	
	/**
	 * 从配置文件中读取图标的路径，并加载。
	 * @param configName 配置文件中图标的键名
	 * @return
	 */
	public static ImageIcon loadIconByConfig(String configName)
	{
		return loadIcon(ChannelConfig.getPropValue(configName));
	}
	
	/**
	 * 获取图标的实际路径，可能为本地或jar包上下文中
	 * @param iconName 图标名称
	 * @return
	 */
	public static URL getIconURL(String iconName)
	{
		return IconFactory.class.getResource(IMAGE_ROOT+iconName);
	}
	
	/**
	 * 获取图标的实际路径，可能为本地或jar包上下文中。和getIconURL(String)作用一样，只是返回参数不一样
	 * @param iconName 图标名称
	 * @see #getIconURL(String)
	 * @return
	 */
	public static String getIconPath(String iconPath)
	{
		File iconFile = new File(getIconURL(iconPath).getPath());
		return iconFile.toURI().toString();
	}

}
