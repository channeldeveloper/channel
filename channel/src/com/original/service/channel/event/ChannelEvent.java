/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel.event;

import com.original.service.channel.Channel;
import com.original.service.channel.Service;


/**
 * Channel CRUD event.
 *
 * @author  Song XueYong
 */
public class ChannelEvent {

	public static int Event_Add = 0;
	public static int Event_Remove = 0;
	public static int Event_Update = 0;
	
	private Service service;
	private int type;
	private Channel[] added;
	private Channel[] removed;
	private Channel[] updated;
	
	public ChannelEvent(Service service, int type, Channel[] added, Channel[] removed, Channel[] updated)
	{
		this.service = service;
		this.type = type;
		this.added = added;
		this.removed = removed;
		this.updated = updated;
	}

	/**
	 * @return the event_Add
	 */
	public static int getEvent_Add() {
		return Event_Add;
	}

	/**
	 * @param event_Add the event_Add to set
	 */
	public static void setEvent_Add(int event_Add) {
		Event_Add = event_Add;
	}

	/**
	 * @return the event_Remove
	 */
	public static int getEvent_Remove() {
		return Event_Remove;
	}

	/**
	 * @param event_Remove the event_Remove to set
	 */
	public static void setEvent_Remove(int event_Remove) {
		Event_Remove = event_Remove;
	}

	/**
	 * @return the event_Update
	 */
	public static int getEvent_Update() {
		return Event_Update;
	}

	/**
	 * @param event_Update the event_Update to set
	 */
	public static void setEvent_Update(int event_Update) {
		Event_Update = event_Update;
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
	public Channel[] getAdded() {
		return added;
	}

	/**
	 * @param added the added to set
	 */
	public void setAdded(Channel[] added) {
		this.added = added;
	}

	/**
	 * @return the removed
	 */
	public Channel[] getRemoved() {
		return removed;
	}

	/**
	 * @param removed the removed to set
	 */
	public void setRemoved(Channel[] removed) {
		this.removed = removed;
	}

	/**
	 * @return the updated
	 */
	public Channel[] getUpdated() {
		return updated;
	}

	/**
	 * @param updated the updated to set
	 */
	public void setUpdated(Channel[] updated) {
		this.updated = updated;
	}
	
}
