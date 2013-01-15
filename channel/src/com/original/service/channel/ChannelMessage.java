/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Reference;
import com.google.code.morphia.utils.IndexDirection;
import com.google.gson.Gson;
import com.original.service.channel.protocols.email.model.EMailParser;
import com.original.service.channel.protocols.im.iqq.QQParser;
import com.original.service.channel.protocols.sns.weibo.WeiboParser;

/**
 * 渠道的信息对象，一个消息分消息的Header，消息的控制部分Control， 消息的内容Content。 Header是消息的标识，标识消息的主体。
 * Control是消息的控制部分，是同消息具体的渠道相关，必须改渠道的操作。如邮件的回执，如微博的关注。
 * Content是消息的内容。消息的内容分plainText\HTML两种，消息同时含有附件。
 * 
 * 所有的Message存放在临时库(Info Template Storage DB)的中。 临时库为的Messages
 * Collection保存所有的Message信息。 一条Message为一个Document。 ID的定义：channelID,
 * peopleID,Serial-Numb. 比如：gtalk.google.com/SongXueyonggoogle.com/im_id
 * gmail.com/franzsoong@google.com/mail_id
 * 
 * 
 * @author cydow
 * @encoding UTF-8
 * @version 1.0
 * @create 2012-11-11 20:17:13
 */
@Entity(value = "messages", noClassnameStored = true)
public class ChannelMessage implements Cloneable, Constants{

	
	// 基本信息 MessageHeader
	@Id
	private ObjectId id;
	// 信息原始的ID，相对自己的服务器,，唯一定义，（Now MessageID from server , franz pending channelID+messageID)
	@Indexed(value = IndexDirection.ASC, name = "messageID", unique = true, dropDups = false)
	private String messageID;
	//followed id for session
	private String followedID;
	@Reference
	private ChannelAccount channelAccount;
	//send date
	@Indexed(value = IndexDirection.ASC, name = "settimestamp", unique = false, dropDups = false)
	private Date sentDate;
	//received (franz pending 拼写错误)
	@Indexed(value = IndexDirection.ASC, name = "gettimestamp", unique = false, dropDups = false)
	private Date receivedDate;
	// received, send , draft 
	private String status;
	// size
	private int size;
	// 控制信息MassageControl	
	private String type = Constants.TYPE_RECEIVED;// send received post comment
	@Indexed(value = IndexDirection.ASC, name = "fromwho", unique = false, dropDups = false)
	private String fromAddr;
	@Indexed(value = IndexDirection.ASC, name = "towho", unique = false, dropDups = false)
	private String toAddr;
	// 头部扩展信息， other controls such as cc bcc
	private HashMap<String, String> extensions;
	// control flags
	//	current draft, sent, unread, read , pending , done, saved, trash.
	private HashMap<String, Integer> flags;
	// 内容信息 MessageBody
	// 主题，邮件主题，微博主题
	private String subject;
	// plaintext, html,xml,json
	private String contentType;
	//内容
	private String body;
	@Embedded
	private List<Attachment> attachments;	
	//pending 类(type)
	private String clazz; 	
	//常量
	public static final String EXT_EMAIL_CC = "CC";
	public static final String EXT_EMAIL_BCC = "BCC";
	public static final String EXT_EMAIL_ReplyTo = "ReplyTo";
	public static final String EXT_EMAIL_Foler = "Foler";	
	//控制 0,1
	public static final String FLAG_REPLYED = "REPLYED";//是否回复了 0未，1是
	public static final String FLAG_SIGNED  = "SIGNED";	//是否签名  0未，1是
	public static final String FLAG_SEEN = "SEEN";//是否看过 0未，1是
	public static final String FLAG_DELETED = "DELETED";//是否删除 0未，1是
	public static final String FLAG_PROCESSED  = "PROCESSED";	//是否处理  0未，1是（邮件原来的）
	public static final String FLAG_DONE = "DONE";//是否处理过 0未，1是 （程序的）
	public static final String FLAG_TRASHED = "TRASHED";//是否垃圾 0未，1是 isTrash
	
	public static final String FLAG_DRAFT = "DRAFT";//是否草稿 0未，1是
	public static final String FLAG_FLAGGED = "FLAGGED";//是否旗标 0未，1是
	public static final String FLAG_RECENT = "RECENT";//是否最新 0未，1是
	public static final String FLAG_SAVED = "SAVED";//是否存库了 0未，1是
	
	/**
	 * default constructor.
	 */
	public ChannelMessage() {
	}

	/**
	 * @return the id
	 */
	public ObjectId getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(ObjectId id) {
		this.id = id;
	}

	/**
	 * @return the followedID
	 */
	public String getFollowedID() {
		return followedID;
	}

	/**
	 * @param followedID
	 *            the followedID to set
	 */
	public void setFollowedID(String followedID) {
		this.followedID = followedID;
	}

	/**
	 * @return the channelID
	 */
	public ChannelAccount getChannelAccount() {
		return channelAccount;
	}

	/**
	 * @param channelID
	 *            the channelID to set
	 */
	public void setChannelAccount(ChannelAccount channelAccount) {
		this.channelAccount = channelAccount;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	public boolean isReceived() {
		return TYPE_RECEIVED.equals(type);
	}
	public boolean isSent() {
		return TYPE_SEND.equals(type);
	}
	
	public String getContactName() {
		return getContactName(getContactAddr());
	}
	public String getContactAddr() {
		return TYPE_SEND.equals(type) ? toAddr : fromAddr;
	}
	
	public static  String getContactName(String toAddr)
	{
		if (toAddr == null || toAddr.trim().isEmpty()) {
			return "Unknown";
		}
		int index = -1;
		if ((index = toAddr.indexOf("<")) == -1) {
			index = toAddr.indexOf("@");
			if (index == -1) {
				return toAddr.trim();
			}
		}
		
		return toAddr.substring(0, index).trim();
	}

	/**
	 * @return the fromAddr
	 */
	public String getFromAddr() {
		return fromAddr;
	}

	/**
	 * @param fromAddr
	 *            the fromAddr to set
	 */
	public void setFromAddr(String fromAddr) {
		this.fromAddr = fromAddr;
	}

	/**
	 * @return the toAddr
	 */
	public String getToAddr() {
		return toAddr;
	}

	/**
	 * @param toAddr
	 *            the toAddr to set
	 */
	public void setToAddr(String toAddr) {
		this.toAddr = toAddr;
	}

	/**
	 * @return the extension
	 */
	public HashMap<String, String> getExtensions() {
		return extensions;
	}


	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType
	 *            the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body
	 *            the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

//	/**
//	 * @return the attachmentIds
//	 */
//	public String[] getAttachmentIds() {
//		return attachmentIds;
//	}
//
//	/**
//	 * @param attachmentIds
//	 *            the attachmentIds to set
//	 */
//	public void setAttachmentIds(String[] attachmentIds) {
//		this.attachmentIds = attachmentIds;
//	}
	
	/**
	 * @return the attachments
	 */
	public List<Attachment> getAttachments() {
		return attachments;
	}

	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	/**
	 * @return the messageID
	 */
	public String getMessageID() {
		return messageID;
	}

	/**
	 * @param messageID
	 *            the messageID to set
	 */
	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}
	
	/**
	 * 添加抄送人地址，多个以英文';'隔开
	 * @param cc
	 */
	public void setCC(String cc) {
		if(cc != null &&  !cc.isEmpty()) {
			if(extensions == null) {
				extensions = new HashMap<String, String>();
			}
			extensions.put(EXT_EMAIL_CC, cc);
		}
	}
	
	public String getClazz()
	{
		if (clazz == null && channelAccount != null) {
			if ("email".equalsIgnoreCase(channelAccount.getChannel().getType())) {
				clazz = MAIL;
			} else if ("sns_weibo".equalsIgnoreCase(channelAccount.getChannel().getName())) {
				clazz = WEIBO;
			} else if ("im_qq".equalsIgnoreCase(channelAccount.getChannel().getName())) {
				clazz = QQ;
			}
		}
		return clazz;
	}
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	
	public boolean isQQ() {
		return QQ.equals(getClazz());
	}
	public boolean isWeibo() {
		return WEIBO.equals(getClazz());
	}
	public boolean isMail() {
		return MAIL.equals(getClazz());
	}

	/**
	 * 
	 */
	public String toString() {
		try {
			Gson gson = new Gson();

			// convert java object to JSON format,
			// and returned as JSON formatted string
			String json = gson.toJson(this);
			return json;
		} catch (Exception exp) {
			return "channel";
		}

	}

	/**
	 * @return the sentDate
	 */
	public Date getSentDate() {
		return sentDate;
	}

	/**
	 * @param sentDate
	 *            the sentDate to set
	 */
	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	/**
	 * 拼写错误。
	 * @return the receivedDate
	 */	
	public Date getReceivedDate() {
		return receivedDate;
	}

	/**
	 * 拼写错误。
	 * @param receivedDate
	 *            the receivedDate to set
	 */
	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}
	
	
	public String getShortMsg() {
		if (isMail()) {
			return subject;
		} else {
			return body;
		}
	}	

	public String getCompleteMsg()
	{
		return getCompleteMsg(false);
	}
	public String getCompleteMsg(boolean showCompleteImage) {
		if (isWeibo()) {
			return WeiboParser.parse(this); // 微博不做任何处理
		} else if (isQQ()) {
			return QQParser.parseMessage(this, showCompleteImage);
		} else {
			return EMailParser.parseMessage(this, showCompleteImage);
		}
	}

	public ChannelMessage clone()
	{
		ChannelMessage msg = null;
		try {
			msg = (ChannelMessage) super.clone();
			msg.setFlags(flags == null ? null : (HashMap) flags.clone());
			msg.setExtensions(extensions == null ? null : (HashMap) extensions.clone());
			msg.setAttachments(attachments == null ? null	: (List) (((ArrayList) attachments).clone()));
		} catch (CloneNotSupportedException ex) {

		}
		return msg;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ChannelMessage) { //这里视messageID唯一
			return messageID != null
					&& messageID.equals(((ChannelMessage) obj).getMessageID());
		}
		return false;
	}

	/**
	 * @return the flags
	 */
	public HashMap<String, Integer> getFlags() {
		return flags;
	}
	/**
	 * @param flags the flags to set
	 */
	public void setFlags(HashMap<String, Integer> flags) {
		this.flags = flags;
	}
	/**
	 * @param extensions the extensions to set
	 */
	public void setExtensions(HashMap<String, String> extensions) {
		this.extensions = extensions;
	}
	
	public boolean hasRead() {
		Integer read = flags == null ? null : flags.get(FLAG_SEEN);
		return read != null && read.intValue() == 1;
	}

	public void setRead(boolean read) {
		if (flags == null)
			flags = new HashMap<String, Integer>();
		flags.put(FLAG_SEEN, read ? new Integer(1) : new Integer(0));
	}
	
	public boolean hasProcessed() {
		Integer processed = flags == null ? null : flags.get(FLAG_DONE);
		return processed != null && processed.intValue() == 1;
	}

	public void setProcessed(boolean processed) {
		if (flags == null)
			flags = new HashMap<String, Integer>();
		flags.put(FLAG_DONE, processed ? new Integer(1) : new Integer(0));
	}
	
	public void setDrafted(boolean drafted) {
		if (flags == null)
			flags = new HashMap<String, Integer>();
		flags.put(FLAG_DRAFT, drafted ? new Integer(1) : new Integer(0));
	}
	
}
