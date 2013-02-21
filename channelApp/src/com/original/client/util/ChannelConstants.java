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
	//鼠标类
	Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR),
			DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR),
			TEXT_CURSOR = new Cursor(Cursor.TEXT_CURSOR),
			WAIT_CURSOR = new Cursor(Cursor.WAIT_CURSOR);
	
	//字体类
	String DEFAULT_FONT_FAMILY = ChannelConfig.getPropValue("channelFont");
	int DEFAULT_FONT_SIZE = 14;
	Font DEFAULT_FONT  =  new Font(DEFAULT_FONT_FAMILY, Font.PLAIN, 
			DEFAULT_FONT_SIZE);
	
	//图标类
	LocationIcon CLOSE_ICON = 	new LocationIcon(
			IconFactory.loadIconByConfig("closeIcon"));
	
	//颜色类
	Color LIGHT_TEXT_COLOR = new Color(85,127,196),
			MENU_BACKGROUND = new Color(46, 156, 202),
			TITLE_COLOR = new Color(58, 182, 210),
			TRANSLUCENCE_COLOR = new Color(128,128,128,(int)(255*0.2));
	
	//坐标点
	Point CENTER_POINT = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
	int CHANNELWIDTH = ChannelConfig.getIntValue("width"),
			CHANNELHEIGHT = ChannelConfig.getIntValue("height"),
			TOOLBARHEIGHT = ChannelConfig.getIntValue("toolbarHeight"),
			STATUSBARHEIGHT = ChannelConfig.getIntValue("statusbarHeight"),
	DESKTOPHEIGHT = ChannelConfig.getIntValue("desktopHeight"),
	SETTINGPANEWIDTH = ChannelConfig.getIntValue("settingPaneWidth");
			
	int MARGIN_TOP = CENTER_POINT.y - (CHANNELHEIGHT/2 - TOOLBARHEIGHT),
			MARGIN_LEFT = CENTER_POINT.x - CHANNELWIDTH/2,
			MARGIN_BOTTOM = CENTER_POINT.y + (CHANNELHEIGHT/2 - STATUSBARHEIGHT),
			MARGIN_RIGHT = CENTER_POINT.x + CHANNELWIDTH/2;
	
	//枚举类
	enum TYPE {MAIL, WEIBO, QQ}
	enum CHANNEL{MAIL, QQ, WEIBO}

}
