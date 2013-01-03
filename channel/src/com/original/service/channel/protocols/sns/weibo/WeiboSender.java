/**
 * com.original.app.emai.EmailService.java
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel.protocols.sns.weibo;

import org.apache.log4j.Logger;

import weibo4j.Comments;
import weibo4j.Timeline;
import weibo4j.http.ImageItem;
import weibo4j.model.WeiboException;

import com.original.service.channel.ChannelAccount;
import com.original.service.channel.ChannelMessage;
import com.original.service.channel.Constants;
import com.original.util.log.OriLog;

/**
 *
 * @author Admin
 */
public class WeiboSender implements Constants{

	private   Logger log = OriLog.getLogger(this.getClass());
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
		
		String accessToken = WeiboService.readToken(channelAcc);
		if(accessToken != null) {
			if(action == ACTION_QUICK_REPLY) { //快速回复
				Comments comments = new Comments();
				comments.setToken(accessToken);
//				comments.createComment(WeiboParser.parseUTF8(msg.getBody()), 
//						msg.getMessageID()); //注意这里的MessageID就是原微博ID，如假包换
				comments.createComment(msg.getBody(), 
						msg.getMessageID()); //注意这里的MessageID就是原微博ID，如假包换
			}
			else if(action == ACTION_REPLY) { //回复，其实就是发表一份微博，只是在最前面加上“@某人：”
				Timeline timeLine = new Timeline();
				timeLine.setToken(accessToken);
				
				ImageItem imgItem = WeiboParser.parseUTF8(msg);
				timeLine.UploadStatus(imgItem.getText(), imgItem);
			}
		}
		else {
			throw new WeiboException("Missing Weibo accessToken!");
		}
	}

}
