package com.original.serive.channel.util;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

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
			TEXT_CURSOR = new Cursor(Cursor.TEXT_CURSOR);
	
	//字体类
	Font DEFAULT_FONT  =  new Font(
			ChannelConfig.getPropValue("channelFont"),  Font.PLAIN, 14);
	
	//图标类
	LocationIcon CLOSE_ICON = 	new LocationIcon(
			IconFactory.loadIconByConfig("closeIcon"));
	
	//颜色类
	Color LIGHT_TEXT_COLOR = new Color(85,127,196);
	
	//枚举类
	enum TYPE {MAIL, WEIBO, QQ}

}
