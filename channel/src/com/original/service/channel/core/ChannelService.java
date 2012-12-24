/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel.core;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.EventListenerList;

import org.bson.types.ObjectId;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.original.service.channel.Attachment;
import com.original.service.channel.Channel;
import com.original.service.channel.ChannelAccount;
import com.original.service.channel.ChannelMessage;
import com.original.service.channel.Service;
import com.original.service.channel.event.ChannelEvent;
import com.original.service.channel.event.ChannelListener;
import com.original.service.channel.event.MessageEvent;
import com.original.service.channel.event.MessageListner;
import com.original.service.channel.event.ServiceEvent;
import com.original.service.channel.event.ServiceListener;
import com.original.service.channel.protocols.email.services.EmailService;
import com.original.service.channel.protocols.im.iqq.QQService;
import com.original.service.channel.protocols.sns.weibo.WeiboService;
import com.original.service.people.People;
import com.original.service.people.PeopleManager;
import com.original.service.profile.Profile;

/**
 * 
 //1. Data 和 View 要分开 //2. 服务 和 应用(控制) 要分开 //3. 服务控制自服务，不由第3方应用外部控制， //4.
 * 服务不能启动，不影响存库数据(离线的ChannelMessage)访问 //5. 服务负责网络的检查和自适应
 * 
 * @author sxy
 * 
 */
public class ChannelService implements Service {

	private ChannelServer channelServer;

	private HashMap<ChannelAccount, Service> serviceMap;
	private MessageManager msgManager;
	private PeopleManager peopleManager;

	private String dbServer = "localhost";
	private String dbServerPort = "27017";
	private String channlDBName = "song";

	private Morphia morphia;
	private Mongo mongo;
	private Datastore ds;

	java.util.logging.Logger logger;

	private Vector<ChannelAccount> failedServiceAccounts = new Vector<ChannelAccount>();

	/**
	 * 
	 */
	public ChannelService() {
		initMongoDB();
		init();
	}

	/**
	 * @return the channelServer
	 */
	public ChannelServer getChannelServer() {
		return channelServer;
	}

	/**
	 * @param channelServer
	 *            the channelServer to set
	 */
	public void setChannelServer(ChannelServer channelServer) {
		this.channelServer = channelServer;
	}

	/**
	 * @return the serviceMap
	 */
	public HashMap<ChannelAccount, Service> getServiceMap() {
		return serviceMap;
	}

	/**
	 * @param serviceMap
	 *            the serviceMap to set
	 */
	public void setServiceMap(HashMap<ChannelAccount, Service> serviceMap) {
		this.serviceMap = serviceMap;
	}

	/**
	 * @return the msgManager
	 */
	public MessageManager getMsgManager() {
		return msgManager;
	}

	/**
	 * @param msgManager
	 *            the msgManager to set
	 */
	public void setMsgManager(MessageManager msgManager) {
		this.msgManager = msgManager;
	}

	/**
	 * @return the dbServer
	 */
	public String getDbServer() {
		return dbServer;
	}

	/**
	 * @param dbServer
	 *            the dbServer to set
	 */
	public void setDbServer(String dbServer) {
		this.dbServer = dbServer;
	}

	/**
	 * @return the dbServerPort
	 */
	public String getDbServerPort() {
		return dbServerPort;
	}

	/**
	 * @param dbServerPort
	 *            the dbServerPort to set
	 */
	public void setDbServerPort(String dbServerPort) {
		this.dbServerPort = dbServerPort;
	}

	/**
	 * @return the channlDBName
	 */
	public String getChannlDBName() {
		return channlDBName;
	}

	/**
	 * @param channlDBName
	 *            the channlDBName to set
	 */
	public void setChannlDBName(String channlDBName) {
		this.channlDBName = channlDBName;
	}

	/**
	 * @return the morphia
	 */
	public Morphia getMorphia() {
		return morphia;
	}

	/**
	 * @param morphia
	 *            the morphia to set
	 */
	public void setMorphia(Morphia morphia) {
		this.morphia = morphia;
	}

	/**
	 * @return the mongo
	 */
	public Mongo getMongo() {
		return mongo;
	}

	/**
	 * @param mongo
	 *            the mongo to set
	 */
	public void setMongo(Mongo mongo) {
		this.mongo = mongo;
	}

	/**
	 * @return the ds
	 */
	public Datastore getDs() {
		return ds;
	}

	/**
	 * @param ds
	 *            the ds to set
	 */
	public void setDs(Datastore ds) {
		this.ds = ds;
	}

	/**
	 * @return the logger
	 */
	public java.util.logging.Logger getLogger() {
		return logger;
	}

	/**
	 * @param logger
	 *            the logger to set
	 */
	public void setLogger(java.util.logging.Logger logger) {
		this.logger = logger;
	}

	/**
	 * @return the notifyingListeners
	 */
	public boolean isNotifyingListeners() {
		return notifyingListeners;
	}

	/**
	 * @param notifyingListeners
	 *            the notifyingListeners to set
	 */
	public void setNotifyingListeners(boolean notifyingListeners) {
		this.notifyingListeners = notifyingListeners;
	}

	/**
	 * @return the listenerList
	 */
	public EventListenerList getListenerList() {
		return listenerList;
	}

	/**
	 * @param listenerList
	 *            the listenerList to set
	 */
	public void setListenerList(EventListenerList listenerList) {
		this.listenerList = listenerList;
	}

	private void initMongoDB() {
		logger = Logger.getLogger("channer");
		morphia = new Morphia();
		morphia.map(ChannelAccount.class);
		morphia.map(Channel.class);
		morphia.map(Profile.class);
		morphia.map(Attachment.class);
		morphia.map(People.class);
		logger.log(Level.INFO, "Mapping POJO to Mongo DB!");

		// DB
		try {
			mongo = new Mongo(dbServer, Integer.valueOf(dbServerPort));
			// db mapping to object
			ds = morphia.createDatastore(mongo, channlDBName);
			ds.ensureIndexes();

		} catch (Exception exp) {
			logger.log(Level.SEVERE,
					"To connect MongoDB Service fail!" + exp.toString());
			logger.log(Level.SEVERE,
					"Channel Service Fails to start When MongoDB connected issues!"
							+ exp.toString());
		}

	}

	// ////////////////////////control and event
	/**
	 * 
	 */

	private void init() {
		msgManager = new MessageManager(morphia, mongo, ds);
		channelServer = new ChannelServer(morphia, mongo, ds);
		peopleManager = new PeopleManager(morphia, mongo, ds);
		serviceMap = new HashMap<ChannelAccount, Service>();
		
		HashMap<String, ChannelAccount> cas = channelServer.getChannelAccounts();
		for (String key : cas.keySet()) {
			ChannelAccount ca = cas.get(key);
			if (serviceMap.containsKey(ca))
				continue; 

			try {
				Service sc = createService(ca);  //一个账户启动一个相应服务，如果启动不成功，需要记录下该账户！！
				if (sc != null) {
					serviceMap.put(ca, sc);
					sc.addMessageListener(new ChannelServiceListener());
				}
			}
			catch(Exception ex) {
				failedServiceAccounts.add(ca);
			}
		}
	}
	
	/**
	 * Pending Use Plug-in register to do this.
	 * 
	 * @param ca
	 * @return
	 */
	public synchronized static Service createService(ChannelAccount ca)
			throws Exception {
		if (ca.getChannel().getName().startsWith("email_")) {
			// 目前先测试邮箱@gmail.com
			// if (ca.getAccount().getUser().indexOf("@gmail.com") != -1 )//test
			// one account
			{
				return new EmailService("Cydow", ca);
			}
		} else if (ca.getChannel().getName().startsWith("im_qq")) {
			return new QQService("Cydow", ca);

		} else if (ca.getChannel().getName().startsWith("sns_weibo")) {
			return new WeiboService("Cydow", ca);
		}
		return null;
	}
	
	/**
	 * 对于未启动成功的服务可以再次重启
	 * @throws Exception
	 */
	public synchronized void restartService() throws Exception {
		if(!failedServiceAccounts.isEmpty()) {
			ChannelAccount ca = failedServiceAccounts.firstElement();
			restartService(ca);
		}
	}
	public synchronized void restartService(ChannelAccount ca) throws Exception {
		Service sc = createService(ca);
		if (sc != null) {
			serviceMap.put(ca, sc);
			failedServiceAccounts.remove(ca);
			sc.addMessageListener(new ChannelServiceListener());
		}
	}
	
	/**
	 * 如果服务未启动成功，用户可以选择跳过
	 * @param ca
	 */
	public void skipService(ChannelAccount ca) {
		failedServiceAccounts.remove(ca);
	}	
	public void skipAllService() {
		failedServiceAccounts.clear();
	}
	
	/**
	 * 判断服务是否都已启动成功
	 * @return
	 */
	public boolean isStartupAll() {
		return failedServiceAccounts.isEmpty();
	}
	
	@Override
	public List<ChannelMessage> get(String action, String query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void put(String action, List<ChannelMessage> msg) {
		// TODO Auto-generated method stub
		if (msg == null || msg.size() == 0) {
			return;
		}
		for (ChannelMessage m : msg) {
			ChannelAccount cha = m.getChannelAccount();
			if (cha != null) {
				Service sc = serviceMap.get(cha);
				sc.put(action, msg);
			}
		}

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void suspend() {
		// TODO Auto-generated method stub

	}

	@Override
	public void start() {

		if (serviceMap == null) {
			return;
		}
		// weired way, but work anyway
		for (ChannelAccount key : serviceMap.keySet()) {
			// System.out.println("Key : " + key.toString() + " Value : "
			// + serviceMap.get(key));
			Service service = serviceMap.get(key);
			service.start();
		}

	}

	@Override
	public void post(String action, List<ChannelMessage> msg) {
		// TODO Auto-generated method stub
		if (msg == null || msg.size() == 0) {
			return;
		}
		for (ChannelMessage m : msg) {
			ChannelAccount cha = m.getChannelAccount();
			if (cha != null) {
				Service sc = serviceMap.get(cha);
				sc.post(action, msg);
			}
		}

	}

	/**
	 * Inner listener
	 * 
	 */
	protected class ChannelServiceListener implements MessageListner,
			ChannelListener, ServiceListener {

		@Override
		public void change(ServiceEvent evnt) {
			// TODO Auto-generated method stub
			System.out.println("ServiceEvent:" + evnt);
		}

		@Override
		public void change(ChannelEvent evnt) {
			// TODO Auto-generated method stub
			System.out.println("ChannelEvent:" + evnt);

			// proxy event to outer

		}

		@Override
		public void change(MessageEvent evnt) {
			// TODO Auto-generated method stub

			// Franz Pending save Message
			ChannelMessage[] chmsgs = evnt.getAdded();
			// 保存联系人
			peopleManager.savePeople(chmsgs);
			// 保存信息
			msgManager.save(chmsgs);

			// proxy fire event
			try {
				fireMessageEvent(evnt);
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}

	}

	// ///////////////////Event///////////////////////
	/**
	 * Notifies all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @param e
	 *            the event
	 * @see EventListenerList
	 */
	protected void fireMessageEvent(MessageEvent e) {
		notifyingListeners = true;
		try {
			// Guaranteed to return a non-null array
			Object[] listeners = listenerList.getListenerList();
			// Process the listeners last to first, notifying
			// those that are interested in this event
			for (int i = listeners.length - 2; i >= 0; i -= 2) {
				if (listeners[i] == MessageListner.class) {
					// Lazily create the event:
					// if (e == null)
					// e = new ListSelectionEvent(this, firstIndex, lastIndex);
					((MessageListner) listeners[i + 1]).change(e);
				}
			}
		} finally {
			notifyingListeners = false;
		}
	}

	/**
	 * Adds a listener for notification of any changes.
	 * 
	 * @param listener
	 *            the <code>MessageListner</code> to add
	 * @see Service#MessageListner
	 */
	public void addMessageListener(MessageListner listener) {
		listenerList.add(MessageListner.class, listener);
	}

	/**
	 * Removes a listener.
	 * 
	 * @param listener
	 *            the <code>MessageListner</code> to add
	 * @see Service#MessageListner
	 */
	@Override
	public void removeMessageListener(MessageListner listener) {
		listenerList.remove(MessageListner.class, listener);
	}

	/**
	 * 
	 * @param listenerType
	 * @return
	 */
	public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
		return listenerList.getListeners(listenerType);
	}

	/**
	 * True will notifying listeners.
	 */
	private transient boolean notifyingListeners;
	/**
	 * The event listener list for the document.
	 */
	protected EventListenerList listenerList = new EventListenerList();

	// ///////////////////////////////////

	@Override
	public int getStatus() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void put(String action, ChannelMessage msg) {
		// TODO Auto-generated method stub
		if (action
				.endsWith(com.original.service.channel.Constants.ACTION_QUICK_REPLY)) {
			// Original message id.
			if (msg.getId() != null) {
				ChannelMessage chmsg = msgManager.getMessage(msg.getId());
				if (chmsg != null) {
					ChannelMessage replyMsg = chmsg.clone();
					replyMsg.setId(null);

					replyMsg.setToAddr(chmsg.getFromAddr());
					replyMsg.setFromAddr(chmsg.getToAddr());
					replyMsg.setBody(msg.getBody());
					replyMsg.setType(ChannelMessage.TYPE_SEND);
					msg = replyMsg;
				}
			}
		}

		if (msg != null) {
			ChannelAccount cha = msg.getChannelAccount();
			if (cha != null) {
				Service sc = serviceMap.get(cha);
				try {
					sc.put(action, msg); //下发消息
					
					//如果下发成功，则需要保存此消息
					msg.setType(ChannelMessage.TYPE_SEND); //强制转换
					msgManager.save(msg);
				}
				catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	@Override
	public void post(String action, ChannelMessage msg) {
		if (msg != null) {
			ChannelAccount cha = msg.getChannelAccount();
			if (cha != null) {
				Service sc = serviceMap.get(cha);
				sc.post(action, msg);
			}
		}

	}

	// //////////search filter order by MessageManager////////

	// ////////////////Update//////////////

	@Override
	public List<ChannelMessage> delete(String action, String query) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 按照消息数据的ID删除。
	 * 
	 * @param msg
	 */
	public void deleteMessage(ObjectId id) {
		// get
		ChannelMessage msg = msgManager.getByID(id);
		// delete
		ds.delete(ChannelMessage.class, id);
		// event to outer
		ChannelMessage[] cmsg = new ChannelMessage[1];
		cmsg[0] = msg;
		MessageEvent evt = new MessageEvent(null, null,
				MessageEvent.Type_Removed, null, cmsg, null);
		fireMessageEvent(evt);

	}

	/**
	 *  按照消息的的ID删除。（消息ID是否唯一，有待讨论).
	 * @param msg
	 */
	public void deleteMessages(String msgId) {
		// get
		Iterator<ChannelMessage> ite = msgManager.getByMessageID(msgId);

		ArrayList<ChannelMessage> bk = new ArrayList<ChannelMessage>();
		while (ite.hasNext()) {
			ChannelMessage chm = ite.next();
			bk.add(chm);
			ds.delete(ChannelMessage.class, chm.getId());
		}
		// delete
//		ds.delete(ChannelMessage.class, ite);
		// event to outer
		ChannelMessage[] msgs = new ChannelMessage[bk.size()];
		bk.toArray(msgs);
		MessageEvent evt = new MessageEvent(null, null,
				MessageEvent.Type_Removed, null, msgs, null);
		fireMessageEvent(evt);
	}
	
	
	/**
	 *  按照Filter 删除消息。
	 * @param msg
	 */
	public void deleteMessages(Filter filter) {
		// get
		Iterator<ChannelMessage> ite = msgManager.getMessage(filter);

		ArrayList<ChannelMessage> bk = new ArrayList<ChannelMessage>();
		while (ite.hasNext()) {
			ChannelMessage chm = ite.next();
			bk.add(chm);
			ds.delete(ChannelMessage.class, chm.getId());		
		}
		// delete by interator (exeption)
//		ds.delete(ChannelMessage.class, ite);
		// event to outer
		ChannelMessage[] msgs = new ChannelMessage[bk.size()];
		bk.toArray(msgs);
		MessageEvent evt = new MessageEvent(null, null,
				MessageEvent.Type_Removed, null, msgs, null);
		fireMessageEvent(evt);
	}

}
