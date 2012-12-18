/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel.event;

import com.original.service.channel.Service;


/**
 * Channel CRUD event.
 *
 * @author  Song XueYong
 */
public class ServiceEvent {
	
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
	 * @return the beforeStatus
	 */
	public int getBeforeStatus() {
		return beforeStatus;
	}


	/**
	 * @param beforeStatus the beforeStatus to set
	 */
	public void setBeforeStatus(int beforeStatus) {
		this.beforeStatus = beforeStatus;
	}


	/**
	 * @return the nowStatus
	 */
	public int getNowStatus() {
		return nowStatus;
	}


	/**
	 * @param nowStatus the nowStatus to set
	 */
	public void setNowStatus(int nowStatus) {
		this.nowStatus = nowStatus;
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


	private Service service;
	private int beforeStatus;
	private int nowStatus;
	private int type;

	
	public ServiceEvent(Service service, int type, int beforeStatus, int nowStatus)
	{
		this.service = service;
		this.type = type;	
		this.beforeStatus = beforeStatus;		
		this.nowStatus = nowStatus;		
	}

	
}
