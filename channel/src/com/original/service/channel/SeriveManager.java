/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel;

import java.util.Iterator;

/**
 * 渠道服务接口，实现了一下基本功能
 * 1服务的CRUD:
 * a获取某个服务。
 * b增加某个服务
 * c删除某个服务（配置）
 * d修改某个服务
 * 
 * 2. 服务的lifecycle控制：
 * a服务启动
 * b服务运行（正常）
 * c服务运行错误（网络错误）
 * d服务的暂停
 * e服务的重启
 * f服务的禁用
 * 
 * 3. 服务的plugin机制
 * a内置服务（默认支持）
 * b服务的安装
 * c服务的卸载
 * d服务的版本控制
 * 
 * @author sxy
 *
 */
public interface SeriveManager {
	/**
	 * To add a service into the channel service manager.
	 * @param svc
	 */
	public void  add(Service svc);
	
	/**
	 * To delete a service from the channel service manager.
	 * @param svc
	 */
	public void  delelte(Service svc);
	
	/**
	 * To update a service in the channel service manager.
	 * @param svc
	 */
	public void  update(Service svc);
	
	/**
	 * To get a service from the channel service manager.
	 * @param svc
	 */
	public Service get(String name);
	
	
	/**
	 * To iterator all services from the channel service manager.
	 * @param svc
	 */
	public Iterator<Service> Iterator();
	
	/**
	 * To start a service.
	 * @param svc
	 */
	public void start(Service svc);
	/**
	 * To stop a service.
	 * @param svc
	 */
	public void stop(Service svc);
	/**
	 * To suspend a service.
	 * @param svc
	 */
	public void suspend(Service svc);
	/**
	 * To resume a service.
	 * @param svc
	 */
	public void resume(Service svc);
	/**
	 * To get status of  a service.
	 * @param svc
	 */
	public void getStatus(Service svc);
	/**
	 * To check a service whether running or not.
	 * @param svc
	 */
	public boolean isRunning(Service svc);
	
	//All control 
	
	
	/**
	 * To start a service.
	 * @param svc
	 */
	public void start();
	/**
	 * To stop a service.
	 * @param svc
	 */
	public void stop();
	/**
	 * To suspend a service.
	 * @param svc
	 */
	public void suspend();
	/**
	 * To resume a service.
	 * @param svc
	 */
	public void resume();
	
	/**
	 * All running are running status.
	 * @param svc
	 */
	public boolean isRunning();

	//Pending Develop Light-OSGI to develop it.
//
//	 * 3. 服务的plugin机制
//	 * a内置服务（默认支持）
//	 * b服务的安装
//	 * c服务的卸载
//	 * d服务的版本控制
	 
	

}
