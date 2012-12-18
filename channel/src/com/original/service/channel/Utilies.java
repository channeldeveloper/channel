/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel;

import java.lang.reflect.Field;
import java.util.HashMap;

import com.original.service.channel.protocols.email.model.EMail;

/**
 * 渠道的工具类。
 * @author sxy
 *
 */
public class Utilies {

	/**
	 * Copy 2 POJO
	 * @param target
	 * @param source
	 * @throws Exception
	 */
	public static <T> void copyFields(T target, T source) throws Exception{
	    Class<?> clazz = source.getClass();

	    for (Field field : clazz.getFields()) {
	        Object value = field.get(source);
	        field.set(target, value);
	    }
	}
	
	/**
	 * 
	 * @param email
	 * @return
	 */
	public static ChannelMessage email2Channel(EMail email)
	{
//		getType()
//		getXh()
		
		ChannelMessage msg = new ChannelMessage();
		msg.setMessageID(email.getMsgId());
//		msg.setAttachmentIds(email.getAttachments()); Pending franz deal attachment later
		msg.setBody(email.getContent());
//		msg.setChannelAccount(ca);
		msg.setSize(email.getSize());
		//MIME Type Tables		
		msg.setType(ChannelMessage.TYPE_RECEIVED);
		msg.setContentType(Constants.Content_Type_Text_Html);
		msg.setClazz(ChannelMessage.MAIL);
		msg.setDate(email.getSendtime());
		//设置简短消息和完整消息显示
		msg.setShortMsg(email.getMailtitle());
		msg.setCompleteMsg(email.getContent());
		
		HashMap<String, Integer> flags = new HashMap<String, Integer>();
		flags.put(Constants.Message_Header_Ctr_EMAIL_Flag_REPLYED,	email.getIsReplay());
		flags.put(Constants.Message_Header_Ctr_EMAIL_Flag_SIGNED, email.getIsSign());
		flags.put(Constants.Message_Header_Ctr_EMAIL_Flag_SEEN,	email.getIsRead());
		flags.put(Constants.Message_Header_Ctr_EMAIL_Flag_DELETED, email.getIsDelete());
		flags.put("isProcess",	email.getIsProcess());//?
		flags.put("isTrash", email.getIsTrash());//?
		msg.setFlags(flags);		
		msg.setFollowedID(null);
		msg.setFromAddr(email.getAddresser());
		
		HashMap<String, String> exts = new HashMap<String, String>();
		exts.put(Constants.Message_Header_Ext_EMAIL_BCC, email.getBcc());
		exts.put(Constants.Message_Header_Ext_EMAIL_CC, email.getCc());
		exts.put(Constants.Message_Header_Ext_EMAIL_ReplyTo, email.getReplayTo());
		exts.put(Constants.Message_Header_Ext_EMAIL_Foler, email.getType());
		msg.setExtensions(exts);
		msg.setSubject(email.getMailtitle());
		msg.setRecievedDate(email.getReceivedtime());
		msg.setSentDate(email.getSendtime());
//		msg.setToAddr(ca.getAccount().getUser());
		
		
		return msg;
	}
	
	/**
	 * 
	 * @param msg
	 * @return
	 */
	public static EMail channel2Email(ChannelMessage msg)
	{

		EMail email = new EMail();
		email.setMsgId(msg.getMessageID());
//		email.setAttachmentIds(msg.getAttachments()); Pending franz deal attachment later
		email.setContent(msg.getBody());
		email.setSize(msg.getSize());
		email.setSendtime(msg.getDate());
		HashMap<String, Integer> flags = msg.getFlags();
		if (flags != null)
		{
			email.setIsReplay(flags.get(Constants.Message_Header_Ctr_EMAIL_Flag_REPLYED));
			email.setIsSign(flags.get(Constants.Message_Header_Ctr_EMAIL_Flag_SIGNED));
			email.setIsRead(flags.get(Constants.Message_Header_Ctr_EMAIL_Flag_SEEN));
			email.setIsDelete(flags.get(Constants.Message_Header_Ctr_EMAIL_Flag_DELETED));
			email.setIsProcess(flags.get("isProcess"));
			email.setIsTrash(flags.get("isTrash"));
		}
		
		email.setAddresser(msg.getFromAddr());
		HashMap<String, String> exts = msg.getExtensions();
		if (exts != null)
		{
			email.setBcc(exts.get(Constants.Message_Header_Ext_EMAIL_BCC));
			email.setCc(exts.get(Constants.Message_Header_Ext_EMAIL_CC));
			email.setReplayTo(exts.get(Constants.Message_Header_Ext_EMAIL_ReplyTo));
			email.setType(exts.get(Constants.Message_Header_Ext_EMAIL_Foler));
		}
		email.setMailtitle(msg.getSubject());
		email.setReceivedtime(msg.getRecievedDate());
		email.setSendtime(msg.getSentDate());

		return email;
	}

}
