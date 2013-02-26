package com.original.client;

/**
 * SWING事件常量接口
 * @author WMS
 *
 */
public interface EventConstants
{
	/*  一些常用功能的前缀 */
	String PREFIX_SHOW = "SHOW_",
			PREFIX_SHOWALL = "SHOWALL_",
			PREFIX_NEW = "NEW_",
			PREFIX_SETTING = "SETTING_",
			PREFIX_SHOWANDNEW = "SHOW_NEW_";
	
	
	/* 通用功能 */
	String QUICK_REPLY = "quick reply"; //快速回复
	String REPLY = "reply";//回复
	String SAVE = "save"; //保存
	String EDIT = "edit"; //编辑
	String DELETE = "delete"; //删除
	String CANCEL = "cancel";//取消
	String POST = "post";//发送
	String REPOST = "repost";//转发
	
	/* 邮件专用功能 */
	String SHOW_COMPLETE = "show complete";//查看完整
	String PUT_INTO_TRASH = "put into trash";//设置垃圾邮件
	String SAVE_TO_DRAFT = "save to draft";//存草稿
	
	/* 过滤查看功能 */
	String VIEW_ALL_TYPE = "view all type";
	String VIEW_ALL_STATUS = "view all status";
	String VIEW_UNREAD = "view unread";
	String VIEW_MAIL = "view mail";
	String VIEW_QQ = "view qq";
	String VIEW_WEIBO = "view weibo";
	String VIEW_CONTACT = "view contact";
	String VIEW_UNDO = "view undo";
	String VIEW_DRAFT = "view draft";
	String VIEW_TRASH = "view trash";
	String TYPE_CHANGE_PROPERTY = "type change property",
			STATUS_CHANGE_PROPERTY = "status change property",
			SEARCHTEXT_CHANGE_PROPERTY = "search text change property";
	
	/* 消息状态 */
	String STATUS_UNKNOWN = "unknown";
	String STATUS_UNREAD = "unread";
	String STATUS_READ = "read";
	String STATUS_POST = "post";
	
	/* 设置和新建联系人消息 */
	String SETTING = "setting";
	String NEW = "new";
		
	/* 面板的消息数改变事件 */
	String 
			MAIL_COUNT_CHANGE_PROPERTY = "MAIL",
			WEIBO_COUNT_CHANGE_PROPERTY = "WEIBO",
			QQ_COUNT_CHANGE_PROPERTY = "QQ";
//	String MAIL_STATUS_CHANGE_PROPERTY = "MAIL",
//			WEIBO_STATUS_CHANGE_PROPERTY = "WEIBO",
//			QQ_STATUS_CHANGE_PROPERTY = "QQ";
	String MAIL_UNREAD_CHANGE_PROPERTY = "MAIL_UNREAD",
			WEIBO_UNREAD_CHANGE_PROPERTY = "WEIBO_UNREAD",
			QQ_UNREAD_CHANGE_PROPERTY = "QQ_UNREAD";
	
	/* 面板的状态改变事件 */
	String LAST_SHOW_COMPONENT = "last show component",//component
			CURRENT_SHOW_COMPONENT = "current show component",//component
			SCROLLBAR_SHOW_STATUS = "scrollbar show status";//Boolean	
	
	/* 联系人快捷通道 */
	String CHANNEL_FOR_QQ = "QQ",
			CHANNEL_FOR_WEIBO = "WEIBO",
			CHANNEL_FOR_MAIL = "MAIL";
	
	/* 发送消息 */
	String POST_MAIL = "post mail",
			POST_QQ = "post qq",
			POST_WEIBO = "post weibo";
	
	/* 文本编辑功能，如添加图片、附件，设置字体等等 */
	String SET_FONT = "set font",
			ADD_IMAGE = "add image",
			ADD_FILE = "add file",
	DEBUG = "debug";
	
	/*  选择联系人 */
	String SELECT_LINKER = "select linker";
	
	/* 分享/抄送 */
	String ADD_CC = "add cc";
	
	/* 字体样式设置 */
	String BOLD_STYLE = "bold",
			ITALIC_STYLE = "italic",
			UNDERLINE_STYLE = "underline";
	String CHOOSE_COLOR = "choose color";
	
	/* 添加、删除附件 */
	String ADD_ATTACHMENT = "add attachment",
			DEL_ATTACHMENT = "del attachment";
	
	/* 添加、删除账户 */
	String ADD_PROFILE  = "add profile",
			DEL_PROFILE = "del profile";
	
	String CONFIRM = "confirm";//确认
}
