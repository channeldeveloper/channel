package com.original.serive.channel;

/**
 * SWING事件常量接口
 * @author WMS
 *
 */
public interface EventConstants
{
	/* 通用功能 */
	String QUICK_REPLY = "quick reply"; //快速回复
	String REPLY = "reply";//回复
	String SAVE = "save"; //保存
	String DELETE = "delete"; //删除
	String CANCEL = "cancel";//取消
	String POST = "post";//发送
	
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
	String VIEW_TRASH = "view trash";
	
	/* 设置和新建联系人消息 */
	String SETTING = "setting";
	String NEWMSG = "new message";
		
	/* 面板的消息数改变事件 */
	String 
			MAIL_COUNT_CHANGE_PROPERTY = "MAIL",
			WEIBO_COUNT_CHANGE_PROPERTY = "WEIBO",
			QQ_COUNT_CHANGE_PROPERTY = "QQ";
	
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
			ADD_FILE = "add file";
	
	/*  选择联系人 */
	String SELECT_LINKER = "select linker";
	
	/* 分享/抄送 */
	String ADD_CC = "add cc";
}
