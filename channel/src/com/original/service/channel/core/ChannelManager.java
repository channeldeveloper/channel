/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel.core;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.EventListenerList;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Key;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.mapping.Mapper;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;
import com.mongodb.Mongo;
import com.original.service.channel.Account;
import com.original.service.channel.Channel;
import com.original.service.channel.event.ChannelEvent;
import com.original.service.channel.event.ChannelListener;

/**
 * 渠道事例管理器。
 * 
 * 
 * @author cydow
 * @encoding UTF-8
 * @version 1.0
 * @create 2012-11-11 20:17:13
 */
public class ChannelManager {
	java.util.logging.Logger logger ;
	private HashMap<String, Channel> channelMap = new HashMap<String, Channel>();
	Mongo mongo;
	Morphia morphia;
	Datastore ds;
	public static final String ChannelDB = "song";
	public static final String ChannelCollectiion = "channel";
	
	/**
	 * Manager Channel Instance.
	 * @param mongo
	 * @param morphia
	 * @param ds
	 */
	protected ChannelManager(Mongo mongo, Morphia morphia, Datastore ds)
	{
		this.mongo = mongo;
		this.morphia = morphia;
		this.ds = ds;
		init();
	}

	/**
	 * 
	 */
	protected void init()
	{
		logger = Logger.getLogger(this.ChannelDB);
		Query<Channel> chs = ds.find(Channel.class);
		List<Channel> chslist = chs.asList();

		// put all channels into memory for channel is limited.
		for (Channel ch : chslist) {
			logger.log(Level.INFO, " Load Channel form DB " + this.ChannelCollectiion
					+ " " + ch);
			channelMap.put(ch.getName(), ch);
		}
	}
	

	/**
	 * 
	 * @param account
	 * @param type
	 * @return
	 */
	public Channel getChannel(Account account)
	{
		return this.channelMap.get(account.getChannelName());
	}
	
	/**
	 * 
	 * @param channel
	 */
	public void addChannel(Channel channel)
	{	
		if  (channel != null && channel.getName() != null)
		{
			Channel inner = channelMap.get(channel.getName());
			 if (inner == null)
			 {
				 Key key = ds.save(channel);
				 Channel added = ds.getByKey(Channel.class, key);
				 this.channelMap.put(added.getName(), added);
					logger.log(Level.INFO, " Add one Channel to DB " + added);
					return;
			 }
		}
		logger.log(Level.INFO, " Fail to add  one Channel to DB " + channel);
		
	}
	
	/**
	 * 
	 * @param channelId
	 * @return
	 */
	public void deleteChannel(String channelName)
	{
		if  (channelName != null)
		{
			Channel inner = channelMap.get(channelName);		
			if (inner != null)
			{
				//get the channel only one instance
				Channel ch = ds.find(Channel.class, "name", channelName).get();				
				ds.delete(ch);				
			}
		}
	}
	
	/**
	 * 
	 * @param newCh
	 * @return
	 */
	public void updateChannel(Channel newCh)
	{
		if  (newCh != null)
		{
			Channel inner = channelMap.get(newCh.getName());		
			if (inner != null)
			{
				//get the channel only one instance, same object
				if (inner != null)
				{
					try
					{
					UpdateOperations<Channel> ops = updateOperation(newCh);
					Query<Channel>  query = ds.createQuery(Channel.class).field(Mapper.ID_KEY).equal(newCh.getId());					
					ds.update(query, ops, true);
					}
					catch(Exception exp)
					{
						exp.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * 
	 */
	private UpdateOperations<Channel> updateOperation(Channel newCh) throws Exception
	{
		UpdateOperations<Channel> ops = ds.createUpdateOperations(Channel.class);
		Class<?> clazz = newCh.getClass();
		  for (Field field : clazz.getFields()) {
		        Object value = field.get(newCh);
		        ops.set(field.getName(), value);
		    }		
		  return ops;
	}
	
	

	
	//control Channel
	
	public void stopChannel(String channelId){}
	public void suspendChannel(String channelId){}
	public void resumeChannel(String channelId){}
	
	
	///////////////////////////event (^_^)


	
	
    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance 
     * is lazily created using the parameters passed into 
     * the fire method.
     *
     * @param e the event
     * @see EventListenerList
     */
    protected void fireChannelChange(ChannelEvent e) {
        notifyingListeners = true;
        try {
            Object[] listeners = listenerList.getListenerList();
            for (int i = listeners.length-2; i>=0; i-=2) {
                if (listeners[i]==ChannelListener.class) {
                    ((ChannelListener)listeners[i+1]).change(e);
                }
            }
        } finally {
        	notifyingListeners = false;
        }
    }
    
    /**
     * True will notifying listeners.
     */
    private transient boolean notifyingListeners;

    /**
     * The event listener list for the channel.
     */
    protected EventListenerList listenerList = new EventListenerList();

	///////////////////////////event (^_^) 
	
	
}
