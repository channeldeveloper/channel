/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import com.original.service.channel.protocols.email.model.EMail;
import com.original.service.channel.protocols.email.model.EMailAttachment;
import com.original.service.channel.protocols.email.oldimpl.Utils;
import com.original.service.channel.protocols.email.services.MailParseUtil;

/**
 * 渠道的工具类。
 * @author sxy
 *
 */
public class Utilies {
	
	static final String MAIL_ATTACH_DIR = System.getProperty("user.home") + 
			"/channel/mailattach/";
	
	static {
		mkdirs(MAIL_ATTACH_DIR); //如果有其他文件夹，可以事先创建下
	}
	
	public static void mkdirs(String folderDir) {
		if(folderDir != null && !folderDir.isEmpty()) {
			File dir = new File(folderDir);
			if(!dir.exists()) {
				dir.mkdirs();
			}
			else if(dir.isFile()) {
				dir.delete();
				dir.mkdirs();
			}
		}
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
	public static EMail channel2Email(ChannelMessage msg)
	{

		EMail email = new EMail();
		email.setMsgId(msg.getMessageID());
//		email.setAttachmentIds(msg.getAttachments()); Pending franz deal attachment later
		email.setContent(msg.getBody());
		email.setSize(msg.getSize());
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
	
	/**
	 * 将邮件内容中的图片等附件下载到系统缓存
	 * /
	private static String saveAttachmentToTemp(String content, String emailid, EMailAttachment eatt) 
	{
		StreamData sd = FileManager.fetchBinaryFile(new String(eatt.getData()));
		if (eatt.getCId() != null) {
			StringBuilder buffer = new StringBuilder();
			buffer.append(MAIL_ATTACH_DIR).append(emailid).append("/")
					.append(eatt.getFileName());
			String filename = buffer.toString();
			File file = new File(filename);
			content = content.replace("cid:" + eatt.getCId(), file.getName()); //这里只要显示文件名即可，不要显示全部的地址
			try {
				if (!file.isFile()) {
					sd.writeToFile(filename);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return content;
	}
	*/
	public static String parseMail(ChannelMessage msg) {
		return parseMail(Utils.channel2Email(msg));
	}

	/**
	 * 处理邮件内容，即显示邮件的完整内容
	 * @param email
	 */
	public static String parseMail(EMail email) {
		if (email != null) {
			StringBuffer header = new StringBuffer("<font color='#557fc4' style='font-size:13'>");
			header.append("发件人：").append(email.getAddresser()).append("<br>");
			header.append("收件人：").append(email.getReceiver()).append("<br>");
			if(email.getCc() != null) {
				header.append("抄　送：").append(email.getCc()).append("<br>");
			}
			
			String content = email.getContent();
			//附件名
			StringBuilder attach = new StringBuilder();
			String attachNames = null;
			
			List attachList = email.getAttachments();
			String emailid = email.get_id();
			if (attachList != null) {
				for (int l = 0; l < attachList.size(); l++) {
					EMailAttachment eatt = (EMailAttachment) attachList.get(l);
					attach.append(eatt.getFileName()).append(";");
//					content = saveAttachmentToTemp(content, emailid, eatt);
				}
				attachNames = attach.toString();
			} 
			if(attachNames != null) {
				header.append("附　件：").append(attachNames).append("<br>");
			}
			
			if (content.startsWith("<![CDDATA[")) {
				content = content.substring(10, content.length() - 2);
			}
			content = header.append("</font><br>").append(content).toString();
			content = MailParseUtil.parseContent(content);
			return content;
		}
		
		return null;
	}
	
	
	/**
	 * 把消息转换为快速发送、发送、转发的消息。
	 * 
	 * @param msg
	 */
	public static ChannelMessage convertMessageForEmail(String action, ChannelMessage original) {
		if (action.equalsIgnoreCase(Constants.ACTION_SEND)) {
			return original;
		}
		if (action.equalsIgnoreCase(Constants.ACTION_QUICK_REPLY)
				|| action.equalsIgnoreCase(Constants.ACTION_REPLY)) {
			original.setSubject("Re: " + original.getSubject());
			String toAddr = original.getToAddr();
			original.setToAddr(original.getFromAddr());
			original.setFromAddr(toAddr);
			String wrapper0 = " <blockquote style=\"margin: 0px 0px 0px 0.8ex; border-left-width: 1px; border-left-color: rgb(204, 204, 204); border-left-style: solid; padding-left: 1ex;\"><u></u>";
			String wrapper1 = "</blockquote>";
			original.setBody(wrapper0 + original.getBody() + wrapper1);
			return original;
		}
		// * ---------- Forwarded message ----------
		// * From: packt@packtpub.com <marketing@packtpub.com>
		// * Date: Sat, Dec 22, 2012 at 9:45 PM
		// * Subject: Packt Publishing: You are now unsubscribed
		// * To: franzsoong@gmail.com
		if (action.equalsIgnoreCase(Constants.ACTION_FORWARD)) {

			String wrapper = " ---------- Forwarded message ----------"
					+ "<br>";
			wrapper += "From: " + original.getFromAddr() + "<br>";
			wrapper += "Date: " + original.getRecievedDate() + "<br>";
			wrapper += "Subject: " + original.getSubject() + "<br>";
			wrapper += "To: " + original.getToAddr() + "<br>";

			original.setBody(wrapper + original.getBody());
			original.setSubject("Fwd: " + original.getSubject());

			String toAddr = original.getToAddr();
			original.setToAddr(original.getFromAddr());
			original.setFromAddr(toAddr);
			return original;

		}
		return original;
	}

}
