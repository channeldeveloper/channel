/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.original.service.channel.protocols.email.model.EMail;
import com.original.service.channel.protocols.email.model.EMailAttachment;
import com.original.service.channel.protocols.email.services.MailParseUtil;

/**
 * 渠道的工具类。
 * @author sxy
 *
 */
public class Utilies {
	
	public static final String TEMP_DIR = System.getProperty("java.io.tmpdir");
	
	/**
	 * get email attachment temp store dir
	 * @param fileID
	 * @param fileName
	 * @return
	 */
	public static String getTempDir(Object fileID, String fileName) {
//		int suffix = fileName.lastIndexOf(".");
		
//		return new File(TEMP_DIR, fileID
//				+ (suffix == -1 ? "" : fileName.substring(suffix))).toURI().toString();
		return new File(TEMP_DIR, fileName).toURI().toString();
	}
	public static String getTempDir(EMailAttachment attach) {
		return getTempDir(attach.getFileID(), attach.getFileName());
	}
	
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
		
		msg.setRecievedDate(email.getSendtime());
//		msg.setToAddr(ca.getAccount().getUser());
		return msg;
	}
	
	/**
	 * 
	 * @param msg
	 * @return
	 */
	public  static EMail  channel2Email(ChannelMessage msg)
	{
		EMail email = new EMail();
    	email.setMsgId(msg.getMessageID());
    	email.setContent(msg.getBody());
    	email.setSize(msg.getSize());

    	email.setAddresser(parseHTMLFlags(msg.getFromAddr()));
    	email.setReceiver(parseHTMLFlags(msg.getChannelAccount().getAccount().getUser()));
    	
    	if (msg.getExtensions() != null)
    	{
    		email.setType(msg.getExtensions().get(Constants.Message_Header_Ext_EMAIL_Foler));
    		email.setBcc(parseHTMLFlags(msg.getExtensions().get(Constants.Message_Header_Ext_EMAIL_BCC)));
    		email.setCc(parseHTMLFlags(msg.getExtensions().get(Constants.Message_Header_Ext_EMAIL_CC)));
    		email.setReplayTo(msg.getExtensions().get(Constants.Message_Header_Ext_EMAIL_ReplyTo));
    	}

    	email.setMailtitle(msg.getSubject());
    	email.setSendtime(ChannelMessage.TYPE_SEND.equals(msg.getType()) ? 
    			msg.getSentDate() : msg.getRecievedDate());
    	
    	//where're attachments??
    	email.setAttachments(parseAttachments(msg.getAttachments()));
		return email;
	}
	
	public static List<EMailAttachment> parseAttachments(List<Attachment> attaches)
	{
		List<EMailAttachment> mailAttaches = null;
    	if(attaches != null && !attaches.isEmpty()) {
    		mailAttaches = new ArrayList<EMailAttachment>(attaches.size());
    		EMailAttachment mailAttach = null;
    		for(Attachment attach : attaches) {
    			if(attach.getContentId() == null) {//if has cid, then see it as inline not attachment!!!
    				mailAttach = new EMailAttachment();

    				mailAttach.setCId(attach.getContentId());
    				mailAttach.setFileID(attach.getFileId());
    				mailAttach.setFileName(attach.getFileName());
    				mailAttach.setSize(attach.getSize());
    				mailAttach.setType(attach.getType());
    				mailAttaches.add(mailAttach);
    			}
    		}
    	}
    	return mailAttaches;
	}
	
	public static String parseHTMLFlags(String content)
	{
		if(content != null && !content.isEmpty()) {
			content = content.replaceAll("\\<", "&lt;");
			content = content.replaceAll("\\>", "&gt;");
		}
		return content;
	}
	
	public static String parseMail(ChannelMessage msg) {
		return parseMail(channel2Email(msg));
	}

	/**
	 * 处理邮件内容，即显示邮件的完整内容
	 * @param email
	 */
	public static String parseMail(EMail email) {
		if (email != null) {
			StringBuffer header = new StringBuffer("<div style=\"font-size: 10px;color:#557fc4;background:#efefef;padding:8px;\">");
			header.append("发件人：").append(email.getAddresser()).append("<br>");
			header.append("收件人：").append(email.getReceiver()).append("<br>");
			if(email.getCc() != null) {
				header.append("抄　送：").append(email.getCc()).append("<br>");
			}
			
			String content = email.getContent();
			//附件名
			StringBuilder attach = new StringBuilder();
			List<EMailAttachment> attachList = email.getAttachments();
			if (attachList != null) {
				for (int l = 0; l < attachList.size(); l++) {
					EMailAttachment eatt = (EMailAttachment) attachList.get(l);
					if(eatt != null) {
						attach.append(String.format("<a href=\"%s\" style=\"color:green; text-decoration: underline;\">", getTempDir(eatt)));
						attach.append(eatt.getFileName()).append("</a>; ");
					}
				}
			} 
			if(attach.length() > 0) {
				header.append("附　件：").append(attach).append("<br>");
			}
			
			if (content.startsWith("<![CDDATA[")) {
				content = content.substring(10, content.length() - 2);
			}
			content = header.append("</div><div style=\"padding:10px 0 10px 0;\">").append(content).toString();
			content = MailParseUtil.parseContent(content);
			return content;
		}
		
		return null;
	}
	
	/**
	 * 回复邮件(新邮件)和原始邮件的分割线
	 * @return
	 */
	public static String getMailSeparatorFlags() {
		return "<div style=\"font-size: 10px;font-family: Arial Narrow;padding:50px 0 0 0;\">" +
				"------------------ 原始邮件 ------------------</div>";
	}
	
	

}
