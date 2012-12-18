/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel.core;

import java.util.HashMap;
import java.util.logging.Logger;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.original.service.channel.Account;
import com.original.service.channel.Attachment;
import com.original.service.channel.Channel;
import com.original.service.channel.ChannelAccount;
import com.original.service.channel.ChannelMessage;


/**
 * 渠道服务： 说明： 1 渠道是渠道服务商提供一个消息服务。 2 每个渠道用户可以注册账号，账号是渠道如无的接入点。 3 每个接入点，需要建立连接才能工作.
 * 4 渠道的管理是服务商的管理，链接的管理是渠道接入点的管理。 5 渠道事例可以是一个，但是连接是多个。 6 系统提供的所有渠道保存在系统中。 7
 * 我的渠道：是渠道和自己账号和密码的关联。 8 我收到的信息，比如gmail
 * 朋友发信给我的163账号，我的渠道就是163.回复给gmail也是同163渠道完成。 比如gmail
 * 朋友发信给我的163账号，我的渠道就是163.回复给qq短息，回复给QQ用户，是通过我的QQ渠道发给朋友 也就是说，如果我没有QQ
 * IM渠道，及无法通过163.com email渠道给他发。
 * 
 * 8. Pending ? 是否能够实现163渠道直接发qq IM 渠道，需要163.com 有gateway支持163.com到qq IM。
 * 
 * 1. 启动实例化。 1) 渠道模版的的实例化。 2) 渠道管理器和渠道事例的实例化。 3) 渠道信息管理器的实例化和渠道消息的装入(Lazy)。
 * 
 * 4) 人脉信息的读入（从人脉模块），本次实现模拟。 5) 启动渠道监控后台任务(Thread task open source quartz).
 * (Ques? 我的渠道是163.com，我收到是gmail的邮件，渠道实际是事例至少2个，
 * 
 * 2. 渠道生命周期管理 
 * 0)渠道注册 1)渠道启动 2)渠道暂停 3)渠道关闭 4)渠道删除 5)渠道后台任务执行，事件Event激发
 * 
 * 3. 消息的处理。 
 * 1）接受消息， 2）发送消息、转发 3）删除消息 4）消息入库。 5）附件读入 6）附件的上传。
 * use this command to import config json file.
 * c:\mongodb\bin>mongoimport.exe --db song --collection profile  --jsonArray c:\profile.json
 * 
 * @author cydow
 * @encoding UTF-8
 * @version 1.0
 * @create 2012-11-11 20:17:13
 */
public class ChannelServer {
	
	java.util.logging.Logger logger;	
	private ChannelManager chMger;
	private ChannelAccountManager chAccountMger;	
	private Morphia morphia;
	private Mongo mongo;
	private Datastore ds;
	
	public ChannelServer(Morphia morphia, Mongo mongo, Datastore ds)
	{
		//1 DB and morphia init
		logger = Logger.getLogger("channer");
		this.morphia = morphia;
		this.mongo = mongo;
		this.ds = ds;

		init();
	}
	
	/**
	 * 
	 */
	public void init() {

		//1 channel manager 
		chMger = new ChannelManager(mongo, morphia, ds);

		//2 load profile (system multi-account)
		chAccountMger = new ChannelAccountManager(mongo, morphia, ds, chMger);
		
	}
	
	/**
	 * 
	 * @param userName
	 * @param channelMap
	 * @return
	 */
	public Channel getChannel(Account account, HashMap<String, Channel> channelMap)
	{
		return channelMap.get(account.getUser());	
	}
	
	private void inits() throws Exception
	{
		
//		  Mongo mongo = new Mongo("localhost");
//		  
//	        mongo.dropDatabase("channel");
//	        Morphia morphia = new Morphia();
//
//	        morphia.mapPackage("demo.gettingStarted.MyEntity");//mapping class to db collection.
//	        
//	        Datastore datastore = morphia.createDatastore(mongo, "channel");//create db
//	        
//	        datastore.ensureCaps();// //creates capped collections from @Entity
//	        datastore.ensureIndexes();// //creates indexes from @Index annotations in your entities
	        
	        //get a collection's document.
//	        MyEntity entity = datastore.get(MyEntity.class, "id");
	        
		//create db and collcetion
		
		//object mapping
		
		//reading people from db
		//reading channel from db
		
		//reading channel from config and add new channel
		//reading channel account from profile to add new channel acount
		
	}

	// Channel
	/**
	 * 
	 * @param account
	 * @param type
	 * @return
	 */
	public ChannelAccount getChannel(Account account, String type) {
		return null;
	}

	/**
	 * 
	 * @param type
	 * @param vendor
	 * @return
	 */
	public ChannelAccount getChannel(String type, String vendor) {
		
		return null;
	}

	/**
	 * 
	 * @param account
	 * @return
	 */
	public ChannelAccount[] getChannel(Account account) {
		
		return null;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, ChannelAccount> getChannelAccounts() {
		
		return chAccountMger.getChAccountMap();
	}

	// operation
	/**
	 * 
	 * @param channel
	 * @param message
	 */
	public void sendMessage(Channel channel, ChannelMessage message) {
	}

	/**
	 * 
	 * @param messageID
	 * @return
	 */
	public ChannelMessage receiveMessage(String messageID) {
		
		return null;
	}

	/**
	 * 
	 * @param messageID
	 */
	public void deleteMessage(String messageID) {
		
	}

	// query

	// public

	/**
	 * 
	 * @return
	 */
	public ChannelMessage[] getMessages() {
		
		return null;
	}

	/**
	 * 
	 * @param contract
	 * @return
	 */
	public ChannelMessage[] getMessage(Account contract) {
		
		return null;
	}

	/**
	 * 
	 * @param messageID
	 * @return
	 */
	public ChannelMessage getMessage(String messageID) {
		
		return null;
	}

	/**
	 * 
	 * @param channelID
	 * @return
	 */
	public ChannelMessage[] getMessages(String channelAccountID) {
		
		return null;
	}

	// attachment
	// attachment uuid
	public String sendAttachment(Attachment attach) {
		
		return null;
	}

	// uuid
	public void getAttachment(String uuid) {
	}
	
	
	
	
	


}
