package com.original.service.channel.protocols.im.iqq;

import iqq.comm.Auth;
import iqq.comm.Auth.AuthInfo;
import iqq.service.MemberService;
import iqq.service.MessageService;
import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;

import com.original.service.channel.ChannelAccount;
import com.original.service.channel.ChannelMessage;
import com.original.service.channel.Constants;

public class QQSender implements Constants{

	private ChannelAccount channelAcc = null;
	
	private static MemberService memberService = MemberService.getInstance();//QQ成员服务
	private static MessageService msgService = MessageService.getIntance();//QQ消息服务
	
	public QQSender(String uid, ChannelAccount ca)
	{
		channelAcc = ca;
	}
	
	public void put(String action, ChannelMessage msg) throws QQException{
		// TODO Auto-generated method stub
		if(action != ACTION_QUICK_REPLY
				&& action != ACTION_REPLY) 
			return;
		
		if(msg != null && channelAcc != null) {
			//获取toAddr的Uin
			String toAddr = msg.getToAddr();
			if(toAddr == null || toAddr.isEmpty())
				throw new QQException("UnKnown QQ Receiver's nickname, please check your sendMsg!");
			
			Long uin = memberService.getUinByNickName(toAddr);
			if(uin == -1L)
				throw new QQException("[" + toAddr + "] is not your friend, please add him/her first!");
			
			String account = channelAcc.getAccount().getUser() + "@qq.com";
			AuthInfo ai = Auth.getAccountInfo(account);
			if(ai == null)
				throw new QQException(account + " has not logged in, please login first!");
			
			if(action == ACTION_QUICK_REPLY) {//快速回复
				msgService.sendMsg(ai, uin, msg.getBody());
			}
			else if(action == ACTION_REPLY) {//回复
				String style = msg.getExtensions().get(QQ_FONT_STYLE);
				JSONObject styleJSON = null;
				try {
					styleJSON = (style == null ? null : new JSONObject(style));
				}
				catch(JSONException ex) {
					
				}
				msgService.sendMsg(ai, uin, msg.getBody(), styleJSON);
			}
		}
	}

}

