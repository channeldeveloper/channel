/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Reference;
import com.google.code.morphia.query.Query;
import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;

/**
 * 1. 我的渠道账号。即渠道和个人账号的对应表，这个数据要到库内。 目前定义，一个渠道的账号，内存实例化一个渠道。即
 * franzsoong@gmail.com soongxueyong@gmail.com 是2个渠道账号实例，即2个渠道处理Thread在处理。
 * 但是渠道的定义只有一个：@gmail.com
 * 
 * 
 * 2. 一个渠道可以有多个帐号(0..n)。 {channel:email.gmai.com}{franzsoong@gmail.com}
 * {channel:email.gmai.com}{soongxueyong@gmail.com}
 * {channel:email.gmai.com}{xueyongsong@gmail.com}
 * 
 * 3. 同样一个账号可以有多个渠道
 * 
 * {channel:email.gmai.com}{franzsoong@gmail.com}
 * {channel:im.gtalk.com}{franzsoong@gmail.com}
 * {channel:sns.google+.com}{franzsoong@gmail.com}
 * 
 * @author sxy
 * 
 */
@Entity(value = "channelAccount", noClassnameStored = true)
public class ChannelAccount implements Serializable{

	@Id
	private ObjectId id;

	@Reference
	private Channel channel;

	@Embedded
	private Account account;

	// open closed trash
	private String status;

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

	// available unavailable meeting
	public String presence;

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
	 * @return the channelId
	 */
	public Channel getChannel() {
		return channel;
	}

	/**
	 * @param channelId
	 *            the channelId to set
	 */
	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}

	/**
	 * @return the presence
	 */
	public String getPresence() {
		return presence;
	}

	/**
	 * @param presence
	 *            the presence to set
	 */
	public void setPresence(String presence) {
		this.presence = presence;
	}

	public String toString() {
		try {
			Gson gson = new Gson();

			// convert java object to JSON format,
			// and returned as JSON formatted string
			String json = gson.toJson(this);
			return json;
		} catch (Exception exp) {
			return "attachment";
		}

	}

	@Override
	public int hashCode()
	{	
		return id == null ? -1 : id.getInc();
	}
	@Override
	public boolean equals(Object obj)
	{
		if(obj == this)
			return true;
		
		if(obj instanceof ChannelAccount) {
			if(((ChannelAccount) obj).getId() == id
					|| (id != null && id.equals(((ChannelAccount) obj).getId())) )
				return true;
		}
		
		return false;
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// Mapping
		Morphia morphia = new Morphia();

		morphia.map(com.original.service.channel.ChannelAccount.class);
		morphia.map(com.original.service.channel.Channel.class);
		morphia.map(com.original.service.profile.Profile.class);
		// DB
		Mongo mongo = new Mongo("localhost", 27017);
		DB db = mongo.getDB("song");

		// db mapping to object
		Datastore ds = morphia.createDatastore(mongo, "song");
		ds.ensureIndexes();

		// by mongo db
		DBCursor cursor = db.getCollection("channelAccount").find();
		while (cursor.hasNext()) {
			System.out.println(cursor.next());
		}

		long cc = ds.getCount(ChannelAccount.class);
		System.out.println("cc:" + cc);

		// query and list

		Query<ChannelAccount> chs = ds.find(ChannelAccount.class);
		List<ChannelAccount> chslist = chs.asList();

		Iterator<ChannelAccount> ite = chs.iterator();
		while (ite.hasNext()) {
			System.out.println(ite.next());
		}

		for (int i = 0; i < chslist.size(); i++) {
			System.out.println(chslist.get(i));
		}

//		init() ;/
	}

	
}
