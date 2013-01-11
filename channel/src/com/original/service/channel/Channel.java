/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Transient;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.utils.IndexDirection;
import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;

/**
 * 渠道对象， 渠道的ID: Server_Domain_Name / Serial_Numbermail.163.com/0001 渠道的分类:
 * email, im, sns. 渠道本身的系统状态: no support, uninit, active, suppend, stopped
 * 一个渠道包含服务(Vendor)信息: gmail.com 支持的协议://smtp, pop3,imap4 支持的内容类型//plain text /
 * html/xml/mime.... 支持的操作，send receive, comment, post....\ 安全策略：(待深入)。SSL
 * Passwrd HTTPS oauth....?
 * 
 * 
 * 
 * @author Song Xueyong
 * @encoding UTF-8
 * @version 1.0
 * @create 2012-11-11 20:17:13
 * 
 */
// 存在系统库。
@Entity(value = "channel", noClassnameStored = true)
public class Channel {

	/**
	 * default constructor.
	 */
	public Channel() {

	}

	@Id
	private ObjectId id;

	@Indexed(value = IndexDirection.ASC, name = "channelname", unique = false, dropDups = false)
	private String name;

	private String vendor;

	private String type;

	private String Status;

	private String domain;

	private int port;

	@Embedded
	private Protocol[] protocols;

	private String[] contentType;// html

	private String[] actions;

	private String policy;// pending

	private HashMap<String, String> extensions;

	@Transient
	private ChannelContext channelContext;

	/**
	 * @return the channelContext
	 */
	public ChannelContext getChannelContext() {
		return channelContext;
	}

	/**
	 * @param channelContext
	 *            the channelContext to set
	 */
	public void setChannelContext(ChannelContext channelContext) {
		this.channelContext = channelContext;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
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

	/**
	 * @return the status
	 */
	public String getStatus() {
		return Status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		Status = status;
	}

	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * @param domain
	 *            the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the protocols
	 */
	public Protocol[] getProtocols() {
		return protocols;
	}

	/**
	 * @param protocols
	 *            the protocols to set
	 */
	public void setProtocols(Protocol[] protocols) {
		this.protocols = protocols;
	}

	/**
	 * @return the authPolicy
	 */
	public String getPolicy() {
		return policy;
	}

	/**
	 * @param authPolicy
	 *            the authPolicy to set
	 */
	public void setPolicy(String policy) {
		this.policy = policy;
	}

	/**
	 * @return the extensions
	 */
	public HashMap<String, String> getExtensions() {
		return extensions;
	}

	/**
	 * @param extensions
	 *            the extensions to set
	 */
	public void setExtensions(HashMap<String, String> extensions) {
		this.extensions = extensions;
	}

	/**
	 * @return the contentType
	 */
	public String[] getContentType() {
		return contentType;
	}

	/**
	 * @param contentType
	 *            the contentType to set
	 */
	public void setContentType(String[] contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the actions
	 */
	public String[] getActions() {
		return actions;
	}

	/**
	 * @param actions
	 *            the actions to set
	 */
	public void setActions(String[] actions) {
		this.actions = actions;
	}

	/**
	 * @return the vendor
	 */
	public String getVendor() {
		return vendor;
	}

	/**
	 * @param vendor
	 *            the vendor to set
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

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


}
