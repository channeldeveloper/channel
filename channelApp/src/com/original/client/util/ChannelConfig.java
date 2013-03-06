package com.original.client.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * 获取Channel的一些默认配置属性，其中属性配置文件在
 * "/com/original/serive/channel/config.properties"下。
 * @author wms
 *
 */
public class ChannelConfig
{
	/** 渠道管理配置信息文件的相对路径  */
	public static String CHANNEL_CONFIG_PROPERTIES = 
			"/com/original/client/config.properties";
	
	private static Properties prop = new Properties();
	static {
		try
		{
			prop.load(ChannelConfig.class.getResourceAsStream(CHANNEL_CONFIG_PROPERTIES));
		} catch (IOException e)
		{
			throw new RuntimeException("Cann't find channel config properties!");
		}
	}
	
	/**
	 * 由键获取属性的值
	 * @param key 键
	 * @return
	 */
	public static String getPropValue(String key)
	{
		return getDecodedPropValue(
				prop.getProperty(key)
		);
	}
	
	/**
	 * 由键获取属性的值，如果查询不到键，则使用默认值
	 * @param key 键 
	 * @param defaultValue 默认值
	 * @return
	 */
	public static String getPropValue(String key, String defaultValue)
	{	
		return getDecodedPropValue(
				prop.getProperty(key, defaultValue)
		);
	}
	
	/**
	 * 获取解码后的值
	 * @param value 原值
	 * @return
	 */
	public static String getDecodedPropValue(String value)
	{
		try {
			//如果是默认编码ISO8859-1
			if (value != null && !value.isEmpty()
					&& value.equals(new String(value.getBytes("ISO8859-1"),	"ISO8859-1")))
			{
				value = new String(value.getBytes("ISO8859-1"), "UTF-8");
			}
		} catch (UnsupportedEncodingException ex)
		{
			
		}
		return value;		
	}
	
	/**
	 * 获取键对应的整数值
	 * @param key 键
	 * @return
	 */
	public static int getIntValue(String key)
	{
		return getIntValue(key, 0);
	}
	
	/**
	 * 获取键对应的整数值，如果查找不到键，则使用默认值
	 * @param key 键
	 * @return
	 */
	public static int getIntValue(String key, int defaultValue)
	{
		try
		{
			return Integer.parseInt(getPropValue(key, ""+defaultValue));
		} catch (Exception ex)
		{
			return defaultValue;
		}
	}
}
