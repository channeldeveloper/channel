package com.original.service.channel.protocols.im.iqq;

import iqq.comm.Auth;
import iqq.comm.Auth.AuthInfo;
import iqq.model.Message;
import iqq.model.MessageDetail;
import iqq.service.MessageService;

import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import weibo4j.model.WeiboException;
import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONObject;

import com.original.service.channel.Account;
import com.original.service.channel.ChannelAccount;
import com.original.service.channel.ChannelMessage;
import com.original.service.channel.Constants;
import com.original.service.channel.core.ChannelService;
import com.original.service.channel.event.MessageEvent;
import com.original.util.log.OriLog;

/**
 * QQ接受主程序，用于定时接受QQ最新的消息，同时将消息转换成统一的ChannelMessage消息对象，下发给QQService。
 * QQService将此消息对象再通知给其下面的监听器，从而实现最终ChannelMessage消息对象的添加、更新等操作。
 * 具体的操作，请见{@link ChannelService#ChannelServiceListener}监听器中的相关方法。
 * @author WMS
 */
public class QQReceiver {

	Logger log = OriLog.getLogger(this.getClass());
	private QQReceiveThread backgroud;
	private HashMap<String, Boolean> cacheMsg;//cache message id
	private QQService qqservice;
	
	private ChannelAccount ca;
	private MessageService msgService = MessageService.getIntance(); //QQ消息服务类

	public QQReceiver(ChannelAccount ca, QQService qs) {

		this.ca = ca;
		cacheMsg = new HashMap<String, Boolean>();
		backgroud = new QQReceiveThread(this);
		qqservice = qs;
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
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return false;
	}
	
//List 用来缓存 MessageID
	/**
	 * 接受QQ消息，这里目前只获取<好友>的消息(群或讨论组忽略)
	 * @return
	 */
	private boolean  recceiveMessages() throws WeiboException{
		if(ca != null && qqservice != null) {
			try {
				Account account = ca.getAccount();
				AuthInfo ai = Auth.getAccountInfo(account.getUser()+"@qq.com");
				JSONObject ret = msgService.openMessageChannel(ai);
				int retcode = ret.getInt("retcode");
				if (retcode == 0) {
	                 JSONArray result = ret.getJSONArray("result");
	                 for (int i = 0; i < result.length(); i++) {
	                     String poll_type = result.getJSONObject(i).getString("poll_type");
	                     JSONObject value = result.getJSONObject(i).getJSONObject("value");
	                     if ("message".equals(poll_type)) {// 好友消息
	                         try {
	                        	 parse( msgService.receiveMsgOnly(ai, value));
	                         } catch (Exception ex) {
	                        	 ex.printStackTrace();
	                         }
	                     } else if ("group_message".equals(poll_type)) {// 群消息，不做处理
//	                    	 msgService.receiveGroupMsg(ai, value);
	                     }
	                 }
	             }
				return true;
			 }
			 catch(Exception ex)
			 {
				 ex.printStackTrace();
				 log.error(ex.getMessage(), ex);
			 }
		}
		return false;
	}
	
	private void parse(Message qqMsg) {
		if (qqMsg == null) return;
				
		try {
			String newMsgId = Long.toString(qqMsg.getId());
			
			// check this msg is existing or not
			boolean existing = cacheMsg.containsKey(newMsgId);
			// 已经放入池内，并且已经解析完成
			if (existing && cacheMsg.get(newMsgId)) {
				return;
			}
			
			ChannelMessage[] cmsg = new ChannelMessage[1];
			cmsg[0] = convertMessage2Message(qqMsg);
			MessageEvent evt = new MessageEvent(null, null,MessageEvent.Type_Added, cmsg, null,null);
			qqservice.fireMessageEvent(evt);
			// 解析完毕，内存缓存。
			cacheMsg.put(newMsgId, Boolean.TRUE);

		} catch (Exception e) {
			log.error(OriLog.logStack(e));
		}

	}
	
	private ChannelMessage convertMessage2Message(Message qqMsg)
	{		
		ChannelMessage msg = new ChannelMessage();
		msg.setMessageID(Long.toString(qqMsg.getId()));
		
		msg.setChannelAccount(ca);
		//MIME Type Tables		
		msg.setContentType(Constants.Content_Type_Text_Html);
		msg.setType(ChannelMessage.TYPE_RECEIVED);
		msg.setClazz(ChannelMessage.QQ);
		//设置消息
		MessageDetail detail = qqMsg.getMsgDetail();
		msg.setBody(detail.getShortMsg());
		
		HashMap<String, Integer> flags = new HashMap<String, Integer>();
		//
		msg.setFlags(flags);	
		msg.setFollowedID(null);
		msg.setFromAddr(qqMsg.getMember().getNickname()); //这里目前使用QQ昵称(QQ号没有任何意思)
		
		//这里保存表情、图片、自定义表情等信息
		HashMap<String, String> exts = new HashMap<String, String>();
		//
		for(Entry<String, String> entry : detail.getFaces().entrySet()) { //表情
			exts.put(entry.getKey(), entry.getValue());
		}
		for(Entry<String, String> entry : detail.getOffpics().entrySet()) { //图片
			exts.put(entry.getKey(), entry.getValue());
		}
		for(Entry<String, String> entry : detail.getCfaces().entrySet()) { //自定义表情
			exts.put(entry.getKey(), entry.getValue());
		}
		msg.setExtensions(exts);
		msg.setRecievedDate(qqMsg.getCreateDate());
		msg.setToAddr(ca.getAccount().getUser());
		
		return msg;
	}
}
