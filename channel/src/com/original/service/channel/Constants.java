/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.service.channel;

/**
 * Constants for Channel Services .
 * 1、列举常用的渠道和协议。
 * 2、渠道服务支持注册渠道（渠道的分类、类型、协议），支持自定义渠道。(Pending)
 * 
 * @author Song XueYong
 */
public interface Constants {	
	
	//DB and collection names
	public static String Channel_DB_Server = "localhost";
	public static int     Channel_DB_Server_Port = 27017;
	public static String Channel_DB_Name = "tempdb";//tempdb later change to 
	public static String Channel__SYSTEM_DB = "sysdb";//system db later change to 
	public static String Channel_Collection_Message = "messages";
	public static String Channel_Collection_Channel = "channel";
	public static String Channel_Collection_ChannelAccount = "channelAccount";
	public static String Channel_Collection_Profile = "profile";
	public static String Channel_Collection_People = "people";
	
	
	public enum CHANNEL{MAIL, QQ, WEIBO}
	
	public static final String QQ = "QQ";
	public static final String WEIBO = "Weibo";
	public static final String MAIL = "Mail";

	public static final String TYPE_SEND = "send";
	public static final String TYPE_RECEIVED = "received";
	public static final String TYPE_POST = "post";
	public static final String TYPE_COMMENT = "comment";
	
	public static final String CHANEL_NAME_GMAIL = "email_gmail";//type _name
	public static final String CHANEL_NAME_MAILQQ = "email_qq";
	public static final String CHANEL_NAME_MAIL163 = "email_163";
	public static final String CHANEL_NAME_GTALK = "im_gtalk";
	public static final String CHANEL_NAME_IMQQ = "im_qq";
	public static final String CHANEL_NAME_WEIBO = "sns_weibo";


	// channel categary (硬件渠道的实现机理可能同软件渠道不同，故先分类(Pending))。
	/**
	 * Hardware channels, such as bluetooth, camera,video recorder,printer, etc. 
	 */
	public static final String CHANEL_SW = "software channel";
	/**
	 * Sofeware channels, such as email, im,twitter, qq, weibo, facebook, etc. 
	 */
	public static final String CHANEL_HW = "hardware channel";

	//channel type
	/**
	 * Channel email type.
	 */
	public static final String CHANEL_EMAIL = "email";
	/**
	 * Channel im type.
	 */
	public static final String CHANEL_IM = "im";
	/**
	 * Channel sns type.
	 */
	public static final String CHANEL_SNS = "sns";
	/**
	 * Channel web service type.
	 */
	public static final String CHANEL_WS = "web service";

	//channel protocols
	/**
	 * Channel POP3 protocol.
	 */
	public static final String CHANNEL_PROTOCOL_POP3 = "POP3";
	/**
	  * Channel IMAP4 protocol.
	 */
	public static final String CHANNEL_PROTOCOL_IMAP4 = "IMAP4";
	/**
	  * Channel SMTP protocol.
	 */
	public static final String CHANNEL_PROTOCOL_SMTP = "SMTP";
	/**
	 * Channel XMPP protocol.
	 */
	public static final String CHANNEL_PROTOCOL_XMPP = "XMPP";
	/**
	  * Channel WebQQ3 protocol.
	 */
	public static final String CHANNEL_PROTOCOL_WEBQQ = "WebQQ3";
	
	
	/**
	 * Presence  available.
	 */
	public static final String PRESENCE_AVAILABLE = "available";
	/**
	  * Presence  unavailable.
	 */
	public static final String PRESENCE_UNAVAILABLE = "unavailable";
	/**
	  * Presence  on meeting.
	 */
	public static final String PRESENCE_ONMEETING = "on meeting";
	/**
	 * Presence  exit.
	 */
	public static final String PRESENCE_EXIT = "exit";
	/**
	  * Presence  offline.
	 */
	public static final String PRESENCE_OFFLINE = "offline";
	
	//action for channel	
	public static final String ACTION_RECEIVE = "receive";
	public static final String ACTION_SEND = "send";
	

	public static final String ACTION_REPLY = "reply";//回复
	public static final String ACTION_QUICK_REPLY = "quick reply";//快速回复
	public static final String ACTION_FORWARD = "forward";
	public static final String ACTION_PUT_DRAFT="Draft";
	
	public static final String ACTION_NEW = "new";//新建
	public static final String ACTION_POST = "post";//发送
	public static final String ACTION_REPOST = "repost";//转发
	public static final String ACTION_COMMENT = "comment";
	
	public static final String ACTION_CHAT = "chat";
	public static final String ACTION_GROUPCHAT = "groupchat";
	
	
	public static final String Content_Type_Text_Html = "text/html";
	public static final String Content_Type_Text_plain = "text/plain";

	public static final String Channel_Type_Email = "email";
	public static final String Channel_Type_IM = "email";
	public static final String Channel_Type_SNS = "email";

	/* ---------------------------------------------------- Weibo -------------------------------------------------------------*/
//	String Weibo_REPOST_COUNT = "weibo repost count";
//	String Weibo_COMMENT_COUNT = "weibo comment count";
//	
//	String Weibo_SOURCE_URL = "weibo source url";
//	String Weibo_SOURCE_NAME = "weibo source name";
	
	String Weibo_ThumbNail_Pic = "weibo thumbnail pic"; //微博小图片地址
	String Weibo_Middle_Pic = "weibo middle pic";//微博中型图片地址
	String Weibo_Original_Pic = "weibo original pic";//微博原始图片地址
	
	/* ---------------------------------------------------- QQ -------------------------------------------------------------*/
	String QQ_FONT_STYLE = "QQ Font Style";//qq字体样式
}
