/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel.core;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

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
		if (chMsg != null && chMsg.getMessageID() != null)
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
				save(chmsg);
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
				"-recievedDate");
		List<ChannelMessage> chmsgs = chmsgQuery.asList();

		return chmsgs;
	}
	
	public List<ChannelMessage> getMessages(Filter filter) {
		
		Query<ChannelMessage> chmsgQuery = ds.find(ChannelMessage.class);
		if(filter != null && filter.getField() != null) {
			chmsgQuery = chmsgQuery.filter(filter.getField(), filter.getValue());
		}
		if(filter != null && filter.getOrderField() != null) {
			chmsgQuery = chmsgQuery.order(filter.getOrderField() );
		}
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
				.order("-recievedDate");
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
	
	
	/////////////////Search///////////////
	/**
	 * 单一条件查询
	 * @param field
	 * @param value
	 * @param order
	 * @return
	 */
	private Iterator<ChannelMessage> get(String field, String value, String order)
	{
		Query<ChannelMessage> q = ds.createQuery(ChannelMessage.class)
				.field(field).endsWith(value).order(order);
		return q.iterator();
	}
	
	/**
	 * 多条件查询
	 * @param fields
	 * @param values
	 * @param order
	 * @return
	 */
	private Iterator<ChannelMessage> get(String[] fields, String[] values, String[] order)
	{
		Query<ChannelMessage> query = ds.createQuery(ChannelMessage.class);
		if(fields != null && values != null) {
			int size = Math.min(fields.length, values.length);
			
			if(size > 0) {
				for(int i=0; i<size; i++) {
					query = query.filter(fields[i], values[i]);
				}
			}
		}
		
		int size = 0;
		if(order != null && (size = order.length) > 0) {
			for(int i=0 ; i<size; i++) {
				query = query.order(order[i]);
			}
		}
		
		return query.iterator();
	}
	
	/**
	 *  根据 渠道的类型(clazz)来查询。目前有Email\SNS(Weibo)\IM(iQQ)
	 * @param channel type
	 * @return
	 */
	public Iterator<ChannelMessage> getByChannelType(String channelType)
	{	
		String field = "clazz";
		String value = channelType;
		String order = "-recievedDate";
		return get(field, value, order);
	}
	
	
	
	/**
	 *  根据数据的id获得消息。
	 * @param ObjectId id
	 * @return
	 */
	public ChannelMessage getByID(ObjectId id)
	{
	     return ds.get(ChannelMessage.class, id);
	}
	
	
	/**
	 *  根据消息的id获得消息。(应该只有一个Message ID).
	 * @param ObjectId id
	 * @return
	 */
	public Iterator<ChannelMessage> getByMessageID(String msgID)
	{
		String field = "messageID";
		String value = msgID;
		String order = "-recievedDate";
		return get(field, value, order);
	}
	
	/**
	 *  根据状态获取消息.
	 * @param ObjectId status
	 * @return
	 */
	public Iterator<ChannelMessage> getByStatus(String status)
	{
		String field = "status";
		String value = status;
		String order = "-recievedDate";
		return get(field, value, order);
	}
	
	
	/////////////////Filter///////////////

	/**
	 * 根据Filter获取消息.
	 * 
	 * @param ObjectId
	 *            status
	 * @return
	 */
	public Iterator<ChannelMessage> getMessage(Filter filter) {
		// all messages
		if (filter == null) {
			return ds.createQuery(ChannelMessage.class).iterator();
		}
		String field = filter.getField();
		String value = filter.getValue();
		String order = filter.getOrderField();
		return get(field, value, order);
	}
	
	public Iterator<ChannelMessage> getMessage(Filter... filters) {
		if(filters == null || filters.length < 1)
			return getMessage((Filter)null);
		else if(filters.length == 1) 
			return getMessage(filters[0]);
		
		Vector<String> fields = new Vector<String>(),
				values = new Vector<String>(),
				orders = new Vector<String>();
		
		for(Filter filter : filters) {
			fields.add(filter.getField());
			values.add(filter.getValue());
			orders.add(filter.getOrderField());
		}
		
		return get(fields.toArray(new String[fields.size()]),
				values.toArray(new String[values.size()]), 
				orders.toArray(new String[orders.size()]));
	}
	
	/////////////////全文搜素(Pending)///////////////
	
	/**
	 * Temporary Full Text Search . (Here should use Full Text Search Engines)
	 * 
	 * @param ObjectId
	 *            status
	 * @return
	 */
	public Iterator<ChannelMessage> search(String text) {
		
		// all messages
		String field = "subject";
		String value = text;
		Query<ChannelMessage> q = ds.createQuery(ChannelMessage.class)
				.field(field).contains(value);
		Iterator<ChannelMessage> ite1 = q.iterator();

		// all messages
		field = "body";
		value = text;
		q = ds.createQuery(ChannelMessage.class).field(field).contains(value);
		Iterator<ChannelMessage> ite2 = q.iterator();
		
		
		// all messages
		field = "fromAddr";
		value = text;
		q = ds.createQuery(ChannelMessage.class).field(field).contains(value);
		Iterator<ChannelMessage> ite3 = q.iterator();
		
		
		// all messages
		field = "toAddr";
		value = text;
		q = ds.createQuery(ChannelMessage.class).field(field).contains(value);
		Iterator<ChannelMessage> ite4 = q.iterator();
				
		return new IteratorIterator<ChannelMessage>(ite1, ite2, ite3, ite4);
				
	}
	
	
	// combine interators into 1 iterator

	private class IteratorIterator<T> implements Iterator<T> {
		private final Iterator<T> is[];
		private int current;

		public IteratorIterator(Iterator<T>... iterators) {
			is = iterators;
			current = 0;
		}

		public boolean hasNext() {
			while (current < is.length && !is[current].hasNext())
				current++;

			return current < is.length;
		}

		public T next() {
			while (current < is.length && !is[current].hasNext())
				current++;

			return is[current].next();
		}

		public void remove() { /* not implemented */
		}

		// /* Sample use */
		// public static void main(String... args)
		// {
		// Iterator<Integer> a = Arrays.asList(1,2,3,4).iterator();
		// Iterator<Integer> b = Arrays.asList(10,11,12).iterator();
		// Iterator<Integer> c = Arrays.asList(99, 98, 97).iterator();
		//
		// Iterator<Integer> ii = new IteratorIterator<Integer>(a,b,c);
		//
		// while ( ii.hasNext() )
		// System.out.println(ii.next());
		// }
	}

	// ///////////////全文搜素(Pending)///////////////
	
	
	
	

	
	
	
	
	
}
