/**
 * com.original.service.channel.protocols.sns.weibo.WeiboReceiver
 *
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel.protocols.sns.weibo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import weibo4j.Timeline;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.WeiboException;

import com.original.service.channel.Account;
import com.original.service.channel.Attachment;
import com.original.service.channel.ChannelAccount;
import com.original.service.channel.ChannelMessage;
import com.original.service.channel.Constants;
import com.original.service.channel.core.ChannelService;
import com.original.service.channel.core.MessageManager;
import com.original.service.channel.event.MessageEvent;
import com.original.service.storage.GridFSUtil;
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
	private HashMap<String, Boolean> cacheMsg;//cache message id
	
	private ChannelAccount ca;
	private Timeline timeLine = new Timeline();//用于获取微博

	public WeiboReceiver(ChannelAccount ca, WeiboService ws) {

		this.ca = ca;
		backgroud = new WeiboReceiveThread(this);
		cacheMsg = new HashMap<String, Boolean>();
		weiboService = ws;
	}
	/**
	 * 后台线程启动。
	 */
	public void start()
	{
		backgroud.start();
	}

	/**
	 * 
	 * @return
	 */
	public boolean receive() {
		try {
			return recceiveMessages();
		} catch (Exception ex) {
			
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
		
		ChannelService csc = ChannelService.getInstance();
		MessageManager msm = csc.getMsgManager();
		
		int size = statuses.size();
		for (int i = 0; i < size; i++) {
			try {
				Status status = statuses.get(i);
				boolean existing = cacheMsg.containsKey(status.getMid());
				// 已经放入池内，并且已经解析完成。
				//这一步减少数据库的查询工作
				if (existing && cacheMsg.get(status.getMid())) {
					continue;
				}
				//检查是否存库内
				if (msm.isExist(status.getMid())){	
					continue;
				}
				
				ChannelMessage[] cmsg = new ChannelMessage[1];
				cmsg[0] = convertStatus2Message(status);
				MessageEvent evt = new MessageEvent(null, null,MessageEvent.Type_Added, cmsg, null,null);
				weiboService.fireMessageEvent(evt);
				
				// 解析完毕，内存缓存。
				cacheMsg.put(status.getMid(), Boolean.TRUE);
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
//		HashMap<String, Integer> flags = new HashMap<String, Integer>();
//		flags.put(Constants.Weibo_REPOST_COUNT,	status.getRepostsCount());
//		flags.put(Constants.Weibo_COMMENT_COUNT, status.getCommentsCount());
//		msg.setFlags(flags);	
		msg.setFollowedID(null);
		msg.setFromAddr(status.getUser().getName());
		
		//消息来源
		HashMap<String, String> exts = new HashMap<String, String>();
//		exts.put(Constants.Weibo_SOURCE_URL, status.getSource().getUrl());
//		exts.put(Constants.Weibo_SOURCE_NAME, status.getSource().getName());
		
		exts.put(Constants.Weibo_ThumbNail_Pic, status.getThumbnailPic());
		exts.put(Constants.Weibo_Middle_Pic, status.getBmiddlePic());
		exts.put(Constants.Weibo_Original_Pic, status.getOriginalPic());
		msg.setExtensions(exts);
		msg.setReceivedDate(status.getCreatedAt());
		msg.setToAddr(ca.getAccount().getUser());
		
		//download thumbnailpi as attachment:
		Attachment attach = downloadThumbnail(status.getThumbnailPic());
		if(attach != null) {
			List<Attachment> attachs = msg.getAttachments();
			if(attachs == null) {
				attachs = new ArrayList<Attachment>();
			}
			attachs.add(attach);
			msg.setAttachments(attachs);
		}
		
		return msg;
	}
	
	private Attachment downloadThumbnail(String thumbnailPicURL) {//下载图片，分数据库和本地保存。本地作为缓存
		if(thumbnailPicURL == null || "".equals(thumbnailPicURL))
			return null;
		
		String thumbnailPicName = null;
		try {
			thumbnailPicName = thumbnailPicURL.substring(
					thumbnailPicURL.lastIndexOf("/")+1, thumbnailPicURL.length());
		}
		catch(Exception ex) {
			return null;
		}
		
		File file = new File(WeiboService.SNS_WEIBO_THUMBNAIL_TEMPDIR, thumbnailPicName);
		if (file.exists()) {
			return null;
		}
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdir();
		}
        InputStream is = null;
        HttpURLConnection httpConnection = null;
        try {
            // 构造HTTP连接
        	URL url = new URL(thumbnailPicURL);
    		httpConnection = (HttpURLConnection)url.openConnection();
    		httpConnection.setConnectTimeout(5 * 1000);//set time-out 5s
 is = httpConnection.getInputStream();
    		
            if (is == null) {
                return null;
            }
            
           ObjectId id = (ObjectId) GridFSUtil.getGridFSUtil().saveFile(is, thumbnailPicName);
           //保存本地：
           GridFSUtil.getGridFSUtil().writeFile(id, file);
           
           Attachment attach = new Attachment();
           attach.setFileId(id);
           attach.setFileName(thumbnailPicName);
           attach.setFilePath(thumbnailPicURL);
           return attach;
           
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            // 完毕，关闭所有链接
			if (is != null) {
				try {
					is.close();
				} catch (IOException ex) {
				}
			}
			if (httpConnection != null) {
				httpConnection.disconnect();
			}
        }
        return null;
	}
}
