/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel.event;

import com.original.service.channel.Channel;
import com.original.service.channel.ChannelMessage;
import com.original.service.channel.Service;


/**
 * Message CRUD event.
 *
 * @author  Song XueYong
 */
public  class MessageEvent {
	
	
	public static int Type_Added = 0;
	public static int Type_Removed = 1;
	public static int Type_Updated = 2;
	
	private Service service;	
	private Channel channel;	
	private int type;
	private ChannelMessage[] added;
	private ChannelMessage[] removed;
	private ChannelMessage[] updated;
	
	public MessageEvent(Service service, Channel channel, int type, ChannelMessage[] added, ChannelMessage[] removed, ChannelMessage[] updated)
	{
		this.service = service;
		this.channel = channel;
		this.type = type;
		this.added = added;
		this.removed = removed;
		this.updated = updated;
	}
	/**
	 * @return the service
	 */
	public Service getService() {
		return service;
	}
	/**
	 * @param service the service to set
	 */
	public void setService(Service service) {
		this.service = service;
	}
	/**
	 * @return the channel
	 */
	public Channel getChannel() {
		return channel;
	}
	/**
	 * @param channel the channel to set
	 */
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * @return the added
	 */
	public ChannelMessage[] getAdded() {
		return added;
	}
	/**
	 * @param added the added to set
	 */
	public void setAdded(ChannelMessage[] added) {
		this.added = added;
	}
	/**
	 * @return the removed
	 */
	public ChannelMessage[] getRemoved() {
		return removed;
	}
	/**
	 * @param removed the removed to set
	 */
	public void setRemoved(ChannelMessage[] removed) {
		this.removed = removed;
	}
	/**
	 * @return the updated
	 */
	public ChannelMessage[] getUpdated() {
		return updated;
	}
	/**
	 * @param updated the updated to set
	 */
	public void setUpdated(ChannelMessage[] updated) {
		this.updated = updated;
	}
	
	
	
}
