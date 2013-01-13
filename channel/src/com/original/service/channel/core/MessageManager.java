/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel.core;

import java.util.ArrayList;
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
 * 娓犻亾娑堟伅绠＄悊鍣ㄣ�
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
	}
	/**
	 * 
	 * @param chMsg
	 * @return
	 */
	public boolean save(ChannelMessage chMsg)
	{
		if (chMsg != null && chMsg.getMessageID() != null) {
			try {
				ds.save(chMsg);
				return true;
			} catch (Exception exp) {
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
		// save Message
		if (chMsgs != null && chMsgs.length > 0) {
			for (ChannelMessage chmsg : chMsgs) {
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
				OrderbyDateField);
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
				.order(OrderbyDateField);
		List<ChannelMessage> chmsgs = chmsgQuery.asList();
		return chmsgs;
	}
	private ArrayList<ChannelMessage> EMPTY = new  ArrayList<ChannelMessage>();
 
	/**
	 * get All Messages
	 * 
	 * @return
	 */
	public List<ChannelMessage> getMessagesByFlag(String key, Integer value) {
		if (key == null || value == null)
		{
			return EMPTY;
		}
		//if value == 0 娌℃湁璁剧疆锛屾槸鍚﹀垽鏂缃负0锛屽鏄惁鍥炲浜嗭紝鏄惁鍒犻櫎浜嗭紵
		if (value.intValue() == 0)
		{
			// 娌℃湁璁剧疆
			Query<ChannelMessage> query0 = ds.find(ChannelMessage.class).field("flags.key").doesNotExist().order(OrderbyDateField);;
			//璁捐浜嗕负0
			Query<ChannelMessage> query = ds.find(ChannelMessage.class).field("flags.key").equal(value).order(OrderbyDateField);;
			
			return copyIterator(new IteratorIterator<ChannelMessage>(query0.iterator(), query.iterator()));
		}
		else
		{
		Query<ChannelMessage> chmsgQuery = ds.find(ChannelMessage.class)
				.field("flags.key").equal(value)
				.order(OrderbyDateField);
		List<ChannelMessage> chmsgs = chmsgQuery.asList();
		return chmsgs;
		}
	}

	/**
	 * 
	 * @param iter
	 * @return
	 */
	public static <T> List<T> copyIterator(Iterator<T> iter) {
	    List<T> copy = new ArrayList<T>();
	    while (iter.hasNext())
	        copy.add(iter.next());
	    return copy;
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
	 * 鍗曚竴鏉′欢鏌ヨ
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
	 * 澶氭潯浠舵煡璇�
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
	 *  鏍规嵁 娓犻亾鐨勭被鍨�clazz)鏉ユ煡璇�鐩墠鏈塃mail\SNS(Weibo)\IM(iQQ)
	 * @param channel type
	 * @return
	 */
	public Iterator<ChannelMessage> getByChannelType(String channelType)
	{	
		String field = "clazz";
		String value = channelType;
		String order = OrderbyDateField;
		return get(field, value, order);
	}
	
	
	
	/**
	 *  鏍规嵁鏁版嵁鐨刬d鑾峰緱娑堟伅銆�
	 * @param ObjectId id
	 * @return
	 */
	public ChannelMessage getByID(ObjectId id)
	{
	     return ds.get(ChannelMessage.class, id);
	}
	
	
	/**
	 *  鏍规嵁娑堟伅鐨刬d鑾峰緱娑堟伅銆�搴旇鍙湁涓�釜Message ID).
	 * @param ObjectId id
	 * @return
	 */
	public Iterator<ChannelMessage> getByMessageID(String msgID)
	{
		String field = "messageID";
		String value = msgID;
		String order = OrderbyDateField;
		return get(field, value, order);
	}
	
	/**
	 *  鏍规嵁鐘舵�鑾峰彇娑堟伅.
	 * @param ObjectId status
	 * @return
	 */
	public Iterator<ChannelMessage> getByStatus(String status)
	{
		String field = "status";
		String value = status;
		String order = OrderbyDateField;
		return get(field, value, order);
	}
	
	private static final String OrderbyDateField = "-receivedDate";
	/////////////////Filter///////////////

	/**
	 * 鏍规嵁Filter鑾峰彇娑堟伅.
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
	
	/////////////////鍏ㄦ枃鎼滅礌(Pending)///////////////
	
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

	// ///////////////鍏ㄦ枃鎼滅礌(Pending)///////////////
	
	/**
	 * 妫�煡鏄惁瀛樺簱鍐�
	 * @param messageID
	 * @return
	 */
	public boolean isExist(ObjectId objId)
	{
		//妫�煡鏄惁瀛樺簱鍐�	
		ChannelMessage chm = getByID(objId);
		return chm != null;
	}
	
	/**
	 * 妫�煡鏄惁瀛樺簱鍐�
	 * @param messageID
	 * @return
	 */
	public boolean isExist(String newMsgId)
	{
		//妫�煡鏄惁瀛樺簱鍐�
		Iterator<ChannelMessage> ite = getByMessageID(newMsgId);
		return ite != null && ite.hasNext();
	}
	
}
