/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel;

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
	//分类(type)
	private String clazz; //?!
	
	// 基本信息
	// MessageHeader
	// id
	@Id
	private ObjectId id;
	// 信息原始的ID，相对自己的服务器
	@Indexed(value = IndexDirection.ASC, name = "messageID", unique = true, dropDups = false)
	private String messageID;

	private String followedID;
	@Reference
	private ChannelAccount channelAccount;

	@Indexed(value = IndexDirection.ASC, name = "settimestamp", unique = false, dropDups = false)
	private Date sentDate;

	@Indexed(value = IndexDirection.ASC, name = "gettimestamp", unique = false, dropDups = false)
	private Date recievedDate;

	// current draft, sent, unread, read , pending , done, saved, trash.
	private String status;
	// size
	private int size;

	// 控制信息MassageControl	
	private String type = Constants.TYPE_RECEIVED;// send received post comment
	@Indexed(value = IndexDirection.ASC, name = "fromwho", unique = false, dropDups = false)
	private String fromAddr;
	@Indexed(value = IndexDirection.ASC, name = "towho", unique = false, dropDups = false)
	private String toAddr;
	// 头部扩展信息
	private HashMap<String, String> extensions;

	// control flags
	private HashMap<String, Integer> flags;// other controls such as cc bcc

	// 内容信息
	// MessageBody
	private String subject;// 主题，邮件主题，微博主题
	private String contentType;// plaintext, html,xml,json
	private String body;//
	
	@Deprecated
	private String[] attachmentIds;
	
	@Embedded
	private List<Attachment> attachments;

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
	
	public String getContactName() {
		return getPersonName(getContactAddr());
	}
	public String getContactAddr() {
		return TYPE_SEND.equals(type) ? toAddr : fromAddr;
	}
	
	/**
	 * Temp Op
	 * @param emailAddr
	 * @return
	 */
	public static  String getPersonName(String emailAddr)
	{
		if (emailAddr == null) {
			return "Unknown";
		}
		int index = -1;
		if ((index = emailAddr.indexOf("<")) == -1) {
			return emailAddr;
		} else {
			return emailAddr.substring(0, index);
		}
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
	 * @return the flags
	 */
	public HashMap getFlags() {
		return flags;
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

	/**
	 * @return the attachmentIds
	 */
	@Deprecated
	public String[] getAttachmentIds() {
		return attachmentIds;
	}

	/**
	 * @param attachmentIds
	 *            the attachmentIds to set
	 */
	@Deprecated
	public void setAttachmentIds(String[] attachmentIds) {
		this.attachmentIds = attachmentIds;
	}

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
	 * @param extensions the extensions to set
	 */
	public void setExtensions(HashMap<String, String> extensions) {
		this.extensions = extensions;
	}

	/**
	 * @param flags the flags to set
	 */
	public void setFlags(HashMap<String, Integer> flags) {
		this.flags = flags;
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
	 * 
	 * @return
	 */
	public String getClazz()
	{
		if(clazz == null && channelAccount != null) {
			if("email".equalsIgnoreCase(channelAccount.getChannel().getType())) {
				clazz = MAIL;
			}
			else if("sns_weibo".equalsIgnoreCase(channelAccount.getChannel().getName())) {
				clazz = WEIBO;
			}
			else if("im_qq".equalsIgnoreCase(channelAccount.getChannel().getName())) {
				clazz = QQ;
			}
		}
		return clazz;
	}
	public void setClazz(String clazz) {
		this.clazz = clazz;
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
	 * @return the recievedDate
	 */
	public Date getRecievedDate() {
		return recievedDate;
	}

	/**
	 * @param recievedDate
	 *            the recievedDate to set
	 */
	public void setRecievedDate(Date recievedDate) {
		this.recievedDate = recievedDate;
	}
	
	public String getShortMsg()
	{
		clazz = this.getClazz();
		if(MAIL.equals(clazz)) {
			return subject;
		}
		else {
			return body;
		}
	}

	public String getCompleteMsg()
	{
		clazz = this.getClazz();
		if(WEIBO.equals(clazz)) {
			return WeiboParser.parse(this);
		}
		else if(QQ.equals(clazz)) {
			return QQParser.parseMessage(this);
		}
		else {
			return  body;
		}
	}

	public ChannelMessage clone()
	{
		ChannelMessage msg = null;
		try {
			msg = (ChannelMessage)super.clone();
			msg.setFlags(flags == null ? null : (HashMap)flags.clone());
			msg.setExtensions(extensions == null ? null : (HashMap)extensions.clone());
			msg.setAttachmentIds(attachmentIds == null ? null : attachmentIds.clone());
		}
		catch(CloneNotSupportedException ex)
		{
			
		}
		return msg;
	}
}
