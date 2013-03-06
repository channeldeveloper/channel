package com.original.client.util;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;

/**
 * Channel消息面板常量类
 * @author WMS
 *
 */
public interface ChannelConstants
{
	/**
	 * 鼠标类常量
	 */
	Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR),
			DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR),
			TEXT_CURSOR = new Cursor(Cursor.TEXT_CURSOR),
			WAIT_CURSOR = new Cursor(Cursor.WAIT_CURSOR);
	
	/**
	 * 默认字体名称常量
	 */
	String DEFAULT_FONT_FAMILY = ChannelConfig.getPropValue("channelFont");
	/**
	 * 默认字体大小常量
	 */
	int DEFAULT_FONT_SIZE = 14;
	/**
	 * 默认字体常量
	 */
	Font DEFAULT_FONT  =  new Font(DEFAULT_FONT_FAMILY, Font.PLAIN, 
			DEFAULT_FONT_SIZE);
	
	/**
	 * 关闭图标常量
	 */
	LocationIcon CLOSE_ICON = 	new LocationIcon(
			IconFactory.loadIconByConfig("closeIcon"));
	
	/**
	 * 颜色类常量
	 */
	Color LIGHT_TEXT_COLOR = new Color(85,127,196),
			MENU_BACKGROUND = new Color(46, 156, 202),
			TITLE_COLOR = new Color(58, 182, 210),
			TRANSLUCENCE_COLOR = new Color(128,128,128,(int)(255*0.4));
	
	/**
	 * 固定中心点常量
	 */
	Point CENTER_POINT = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
	/**
	 * 固定宽度、高度常量
	 */
	int CHANNELWIDTH = ChannelConfig.getIntValue("width"),
			CHANNELHEIGHT = ChannelConfig.getIntValue("height"),
			TOOLBARHEIGHT = ChannelConfig.getIntValue("toolbarHeight"),
			STATUSBARHEIGHT = ChannelConfig.getIntValue("statusbarHeight"),
	DESKTOPHEIGHT = ChannelConfig.getIntValue("desktopHeight"),
	SETTINGPANEWIDTH = ChannelConfig.getIntValue("settingPaneWidth");
			
	/**
	 * 最大可显示页边距
	 */
	int MARGIN_TOP = CENTER_POINT.y - (CHANNELHEIGHT/2 - TOOLBARHEIGHT),
			MARGIN_LEFT = CENTER_POINT.x - CHANNELWIDTH/2,
			MARGIN_BOTTOM = CENTER_POINT.y + (CHANNELHEIGHT/2 - STATUSBARHEIGHT),
			MARGIN_RIGHT = CENTER_POINT.x + CHANNELWIDTH/2;
	
	/**
	 * 消息类型
	 */
	enum TYPE {MAIL, WEIBO, QQ}
	/**
	 * 消息渠道
	 */
	enum CHANNEL{MAIL, QQ, WEIBO}

}
