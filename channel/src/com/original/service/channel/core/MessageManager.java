/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel.core;

import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.query.Query;
import com.mongodb.Mongo;
import com.original.service.channel.ChannelMessage;

/**
 * 渠道消息管理器。
 * 
 * 
 * @author cydow
 * @encoding UTF-8
 * @version 1.0
 * @create 2012-11-11 20:17:13
 */

public class MessageManager {
	
	private Mongo mongo;
	private Morphia morphia;
	private Datastore ds;


	/**
	 * Message Manager
	 * @param mongo
	 * @param morphia
	 * @param ds
	 */
	public MessageManager(Morphia morphia, Mongo mongo, Datastore ds)
	{
		this.mongo = mongo;
		this.morphia = morphia;
		this.ds = ds;
//		init();
	}
	/**
	 * 
	 * @param chMsg
	 * @return
	 */
	public boolean save(ChannelMessage chMsg)
	{
		if (chMsg != null)
		{
			try
			{
				ds.save(chMsg);
				return true;
			}
			catch(Exception exp)
			{
				exp.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param chMsgs
	 */
	public void save(ChannelMessage[] chMsgs)
	{		
		//save Message
		if (chMsgs != null && chMsgs.length > 0)
		{
			for (ChannelMessage chmsg : chMsgs)
			{
				try
				{
					ds.save(chmsg);
				}
				catch(Exception exp)
				{
					
				}
			}			
		}
		
	}

	// find
	/**
	 * get All Messages
	 * 
	 * @return
	 */
	public List<ChannelMessage> getMessages() {
		Query<ChannelMessage> chmsgQuery = ds.find(ChannelMessage.class).order(
				"recievedDate");
		List<ChannelMessage> chmsgs = chmsgQuery.asList();

		return chmsgs;
	}
	
	/**
	 * get All Messages
	 * 
	 * @return
	 */
	public ChannelMessage getMessage(ObjectId id) {
		
		 return ds.get(ChannelMessage.class, id);
	}
	

	
	/**
	 * get All Messages
	 * 
	 * @return
	 */
	public List<ChannelMessage> getMessagesByFrom(String fromAddr) {
		Query<ChannelMessage> chmsgQuery = ds.find(ChannelMessage.class)
				.field("fromAddr").endsWithIgnoreCase(fromAddr)
				.order("recievedDate");
		List<ChannelMessage> chmsgs = chmsgQuery.asList();
		return chmsgs;
	}

	/**
	 * get messages by one fromAddr value of findFileName.
	 * @param findFieldName
	 * @param orderFieldName
	 * @param fromAddr
	 * @return
	 */
	public List<ChannelMessage> getMessagesByFrom(String findFieldName,
			String orderFieldName, String fromAddr) {
		if (findFieldName == null || fromAddr == null) {
			return null;
		}
		if (orderFieldName == null) {
			Query<ChannelMessage> chmsgQuery = ds.find(ChannelMessage.class)
					.field(findFieldName).endsWithIgnoreCase(fromAddr);
			List<ChannelMessage> chmsgs = chmsgQuery.asList();
			return chmsgs;
		} else {
			Query<ChannelMessage> chmsgQuery = ds.find(ChannelMessage.class)
					.field(findFieldName).endsWithIgnoreCase(fromAddr)
					.order(orderFieldName);
			List<ChannelMessage> chmsgs = chmsgQuery.asList();
			return chmsgs;
		}

	}
}
