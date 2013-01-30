/**
 * com.original.app.emai.EmailService.java
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel.protocols.sns.weibo;

import weibo4j.Timeline;
import weibo4j.Users;
import weibo4j.http.ImageItem;
import weibo4j.model.Status;
import weibo4j.model.User;
import weibo4j.model.WeiboException;

import com.original.service.channel.ChannelAccount;
import com.original.service.channel.ChannelMessage;
import com.original.service.channel.Constants;

/**
 *
 * @author Admin
 */
public class WeiboSender implements Constants{

	private ChannelAccount channelAcc = null;

	public WeiboSender(String uid, ChannelAccount ca)
	{
		this.channelAcc = ca;
	}
	
	/**
	 * 下发一条微博，注意区别发送(回复)类型：快速回复是纯文本，而回复是HTML。
	 * @param action 回复类型 {@link Constants#ACTION_QUICK_REPLY}和{@link Constants#ACTION_REPLY}
	 * @param msg 消息对象
	 */
	public void send(String action, ChannelMessage msg) throws WeiboException
	{
		if(msg == null || (action != ACTION_QUICK_REPLY
				&& action != ACTION_REPLY)) 
			return;
		
		if (msg.getAction() == ACTION_REPLY && action == ACTION_REPLY) {// 这里如果是回复微博，视为快速回复
			action = ACTION_QUICK_REPLY;
		}
		
		String accessToken = WeiboService.readToken(channelAcc);
		if(accessToken != null) {
			Timeline timeLine = new Timeline();
			timeLine.setToken(accessToken);
			
			boolean is_comment = !channelAcc.getAccount().getUser().equals(msg.getToAddr()); //是否评论原微博
			
			String uid = channelAcc.getAccount().getUserId();
			if (!is_comment && uid != null) { // 可能上面is_comment的值不一定对，这里再次比较用户昵称与收信人是否一致。
				Users users = new Users();
				users.setToken(accessToken);
				User u = users.showUserById(uid);
				is_comment = u.getScreenName().equals(msg.getToAddr());
			}
			
			if(action == ACTION_QUICK_REPLY) { //快速回复。客户要求：回复(评论)给原微博后，自己微博也能显示该回复(评论)信息。
				//其实，转发可以实现该功能，效果是一致的。即，转发时添加转发内容（视为回复内容），同时评论给原微博。
				String mid = getMID(msg.getMessageID()); //原微博id
				Status status = timeLine.showStatus(mid);
				if(status != null) {
					Status retweetedStatus = status.getRetweetedStatus();
					
					if(retweetedStatus != null) { //此微博是转发的微博
						msg.setBody(msg.getBody() +"//@" + msg.getToAddr() + ": " + status.getText() );			
					}
					timeLine.Repost(mid, msg.getBody(), is_comment?new Integer(1) : new Integer(0)); 
				}
			}
			else if(action == ACTION_REPLY) { //回复，其实就是发表一份微博，只是在最前面加上“@某人：”
				Object[] items = WeiboParser.parseUTF8(msg, is_comment); //自己给自己发，不需要@自己
				if(items != null && items.length == 2) {
					ImageItem imgItem = items[1] == null ? null :  new ImageItem((byte[])items[1]);
					if(imgItem == null) {
						timeLine.UpdateStatus((String)items[0]);
					}
					else {
						timeLine.UploadStatus((String)items[0], imgItem);
					}
				}
			}
		}
		else {
			throw new WeiboException("Missing Weibo accessToken!");
		}
	}
	
	private String getMID(String messageID) {
		if (messageID == null || messageID.isEmpty())
			return messageID;

		int splitIndex = messageID.indexOf("$");
		if (splitIndex == -1)
			splitIndex = messageID.length();

		return messageID.substring(0, splitIndex);
	}

}
