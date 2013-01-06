/**
 * com.original.service.channel.protocols.sns.weibo.WeiboReceiver
 *
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel.protocols.sns.weibo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import weibo4j.Timeline;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.WeiboException;

import com.original.service.channel.Account;
import com.original.service.channel.ChannelAccount;
import com.original.service.channel.ChannelMessage;
import com.original.service.channel.Constants;
import com.original.service.channel.core.ChannelService;
import com.original.service.channel.event.MessageEvent;
import com.original.util.log.OriLog;

/**
 * 微博接受主程序，用于定时接受微博最新的消息，同时将消息转换成统一的ChannelMessage消息对象，下发给WeiboService。
 * WeiboService将此消息对象再通知给其下面的监听器，从而实现最终ChannelMessage消息对象的添加、更新等操作。
 * 具体的操作，请见{@link ChannelService#ChannelServiceListener}监听器中的相关方法。
 * @author WMS
 */
public class WeiboReceiver {

	Logger log = OriLog.getLogger(this.getClass());
	private WeiboReceiveThread backgroud;
	private WeiboService weiboService;
	
	private ChannelAccount ca;
	private Timeline timeLine = new Timeline();//用于获取微博
	

	public WeiboReceiver(ChannelAccount ca, WeiboService ws) {

		this.ca = ca;
		backgroud = new WeiboReceiveThread(this);
		weiboService = ws;
	}
	/**
	 * 后台线程启动。
	 */
	public void start()
	{
//		backgroud.start();
	}

	/**
	 * 
	 * @return
	 */
	public boolean receive() {
		try {
			return recceiveMessages();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return false;
	}
	
//List 用来缓存 MessageID
	/**
	 * 接受微博消息，这里目前只获取<我发表的微博>和<我关注的微博>信息
	 * @return
	 */
	private boolean  recceiveMessages() throws WeiboException{
		if(ca != null && weiboService != null) {
			Map<Account, String> accessTokens = weiboService.getAccessTokens();
			String token = accessTokens.get(ca.getAccount());
			if(timeLine.getToken() ==  null && 
					(token != null && token.length() == 32)) {//有效的Token值
				timeLine.setToken(token);
			}
			
			StatusWapper sw = timeLine.getFriendsTimeline();
			parse(sw.getStatuses());
			return true;
		}
		return false;
	}
	
	private void parse(List<Status> statuses) {
		if (statuses == null || statuses.isEmpty()) {
			return;
		}
		int size = statuses.size();
				
		for (int i = 0; i < size; i++) {
			try {
				Status status = statuses.get(i);
				
				ChannelMessage[] cmsg = new ChannelMessage[1];
				cmsg[0] = convertStatus2Message(status);
				MessageEvent evt = new MessageEvent(null, null,MessageEvent.Type_Added, cmsg, null,null);
				weiboService.fireMessageEvent(evt);

			} catch (Exception e) {
				e.printStackTrace();
				log.error(OriLog.logStack(e));
			}
		}

	}
	
	private ChannelMessage convertStatus2Message(Status status)
	{		
		ChannelMessage msg = new ChannelMessage();
		msg.setMessageID(status.getMid());
//		msg.setAttachmentIds(email.getAttachments()); Pending franz deal attachment later
		msg.setBody(status.getText());
		msg.setChannelAccount(ca);
		//MIME Type Tables		
		msg.setContentType(Constants.Content_Type_Text_Html);
		msg.setType(ChannelMessage.TYPE_RECEIVED);
		msg.setClazz(ChannelMessage.WEIBO);
		
		//转发数和评论数
		HashMap<String, Integer> flags = new HashMap<String, Integer>();
		flags.put(Constants.Weibo_REPOST_COUNT,	status.getRepostsCount());
		flags.put(Constants.Weibo_COMMENT_COUNT, status.getCommentsCount());
		msg.setFlags(flags);	
		msg.setFollowedID(null);
		msg.setFromAddr(status.getUser().getName());
		
		//消息来源
		HashMap<String, String> exts = new HashMap<String, String>();
		exts.put(Constants.Weibo_SOURCE_URL, status.getSource().getUrl());
		exts.put(Constants.Weibo_SOURCE_NAME, status.getSource().getName());
		
		exts.put(Constants.Weibo_ThumbNail_Pic, status.getThumbnailPic());
		exts.put(Constants.Weibo_Middle_Pic, status.getBmiddlePic());
		exts.put(Constants.Weibo_Original_Pic, status.getOriginalPic());
		msg.setExtensions(exts);
		msg.setRecievedDate(status.getCreatedAt());
		msg.setToAddr(ca.getAccount().getUser());
		
		return msg;
	}
}
