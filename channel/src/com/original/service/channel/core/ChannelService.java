/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel.core;


import java.util.ArrayList;
import java.util.Collection;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.swing.event.EventListenerList;

import org.bson.types.ObjectId;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;
import com.mongodb.Mongo;
import com.original.service.channel.AbstractService;
import com.original.service.channel.Account;
import com.original.service.channel.Attachment;
import com.original.service.channel.Channel;
import com.original.service.channel.ChannelAccount;
import com.original.service.channel.ChannelMessage;
import com.original.service.channel.Constants;
import com.original.service.channel.Constants.CHANNEL;
import com.original.service.channel.Protocol;
import com.original.service.channel.SeriveManager;
import com.original.service.channel.Service;
import com.original.service.channel.config.Initializer;
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
public final class ChannelService extends AbstractService implements SeriveManager {
	private Logger logger;

	//渠道的管理
	private ChannelManager channelManager;
	//账号的管理
	private AccountManager accountManager;
	//信息的管理
	private MessageManager messageManager;
	//信息联系人的管理
	private PeopleManager peopleManager;

	private HashMap<ChannelAccount, Service> serviceMap = new HashMap<ChannelAccount, Service>();


	private String dbServer = Constants.Channel_DB_Server;
	private int dbServerPort = Constants.Channel_DB_Server_Port;
	private String channlDBName = Constants.Channel_DB_Name;

	private Morphia morphia;
	private Mongo mongo;
	private Datastore ds;

	private Initializer initializer;
	private Vector<ChannelAccount> failedServiceAccounts = new Vector<ChannelAccount>();
	private static ExecutorService executor = Executors.newSingleThreadExecutor();
	private static boolean isRunError = false, //是否启动出错
			isStart = false;//服务是否已启动
	
	private static ChannelService singleton;
//	private static ThreadManager threadPool;
	
	/**
	 * 
	 * @return
	 */
	public synchronized static ChannelService getInstance()
	{
		if (singleton == null)
		{
			singleton = new ChannelService();
		}
		return singleton;
	}

	
	public SeriveManager getChannelService()
	{
		return this;
	}
	/**
	 * 
	 */
	private ChannelService() {
		initMongoDB();
		//将启动服务放入线程中。
		init();
	}

//	/**
//	 * @return the channelServer
//	 */
//	public ChannelServer getChannelServer() {
//		return channelServer;
//	}
//
//	/**
//	 * @param channelServer
//	 *            the channelServer to set
//	 */
//	public void setChannelServer(ChannelServer channelServer) {
//		this.channelServer = channelServer;
//	}

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
		return messageManager;
	}

	/**
	 * @param msgManager
	 *            the msgManager to set
	 */
	public void setMsgManager(MessageManager msgManager) {
		this.messageManager = msgManager;
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
	public int getDbServerPort() {
		return dbServerPort;
	}

	/**
	 * @param dbServerPort
	 *            the dbServerPort to set
	 */
	public void setDbServerPort(int dbServerPort) {
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

	private void initMongoDB() { //暂时不置于线程中
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
			mongo = new Mongo(dbServer, dbServerPort);
			// db mapping to object
			ds = morphia.createDatastore(mongo, channlDBName);
			ds.ensureIndexes();

		} catch (Exception exp) {
			logger.log(Level.SEVERE,
					"To connect MongoDB Service fail!" + exp.toString());
			isRunError = true;
			return;
		}
		
		initializer = new Initializer(mongo);
		try {
			initializer.init(false);
		} catch (Exception exp) {
			logger.log(Level.SEVERE, "To init channel db fail!" + exp.toString());
			isRunError = true;
			return;
		}
		
		messageManager = new MessageManager(this, morphia, mongo, ds);
		channelManager =  new ChannelManager(mongo, morphia, ds);
		accountManager = new AccountManager(mongo, morphia, ds, channelManager);
		peopleManager = new PeopleManager(this, morphia, mongo, ds);
	}

	private void init() { //启动channel服务，置于线程中进行
		
		Runnable initServiceTask = new Runnable() {
			public void run() {
				if(isRunError) {
					return;
				}
				
				HashMap<String, ChannelAccount> cas = accountManager.getChAccountMap();
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
						ex.printStackTrace();
						failedServiceAccounts.add(ca);
					}
				}
				
				//立即启动，不能外部调用，否则起不来！
				start();
			}
		};
		
		executor.execute(initServiceTask);
	}
	
	/**
	 * @return the peopleManager
	 */
	public PeopleManager getPeopleManager() {
		return peopleManager;
	}

	/**
	 * @param peopleManager the peopleManager to set
	 */
	public void setPeopleManager(PeopleManager peopleManager) {
		this.peopleManager = peopleManager;
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
			return new EmailService("Cydow", ca);
			
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
	
//	@Override
//	public List<ChannelMessage> get(String action, String query) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
//	@Deprecated	
//	@Override
//	public void put(String action, List<ChannelMessage> msg) {
//		
//		// TODO Auto-generated method stub
//		if (msg == null || msg.size() == 0) {
//			return;
//		}
//		for (ChannelMessage m : msg) {
//			ChannelAccount cha = m.getChannelAccount();
//			if (cha != null) {
//				Service sc = serviceMap.get(cha);
//				sc.put(action, msg);
//			}
//		}
//	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void suspend() {
		// TODO Auto-generated method stub

	}

	/**
	 * 渠道服务管理，不需要内部控制。
	 */
	@Override
	@Deprecated 
	public synchronized void start() {
		//Pending:收件装载账号，
		//如果账号变化（是否有push服务，还是定期去取？）
		//需要发账号变化事件，每个消息默认都必须有peopleID
		peopleManager.loadContracts();
		//
		if (isStart)
			return;

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
		isStart = true;
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
			if(evnt.getType() == MessageEvent.Type_Added) {
				ChannelMessage[] chmsgs = evnt.getAdded();
				for (int i = 0; i < chmsgs.length; i++)
				{
					// 保存联系人
					People contract = peopleManager.getPeopleByMessage(chmsgs[0]);
					//Every message must have peopleId(fromAddr)
					//Pending (Service to do weibo service , bad code pattern, later push down to weibo service)
					if (contract == null)
					{
						contract = peopleManager.savePeople(chmsgs[0]);
					}
					if (contract != null)
					{
						chmsgs[0].setPeopleId(contract.getId());
						// 保存信息
						messageManager.save(chmsgs[0]);
					}
					
				}
				fireMessageEvent(evnt); // notify to GUI App to add message only when save successfully!
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
		
		//Pending Song Xueyong: 逻辑这些不处理，推到具体的实现来处理。
		//1、存草稿看成具体渠道的一个服务，比如邮件的存草稿
		//2、邮件发出去之后是否存库，有msg的ID决定，如果ID存在，不存，只是更新状态
		//3、如果邮件发出去，是新构建的邮件，没有ID，要存盘
		//4、注意每个Message 必须有peopleid!!!
		if (msg != null) {
			//Must have PeopleID
			if (msg.getPeopleId() == null)
			{
//				String toAddr = msg.getToAddr();
				People pp = peopleManager.getPeopleByMessage(msg);
				if (pp == null)
				{
					pp = peopleManager.savePeople(msg);
				}
				if (pp == null)
				{
					//Pending Exception definition for channel
					throw new IllegalArgumentException(msg.getToAddr());
				}
				msg.setPeopleId(pp.getId());
			}
			
			 //1、存草稿
			if (action == Constants.ACTION_PUT_DRAFT) {
				if (msg.getId() != null) {//删除原有的草稿，可能用户再次保存草稿！					
					this.deleteMessage(msg.getId());
				}
				msg.setMessageID(getRandomMessageID());//设置一个随机的消息id
				msg.setDrafted(true);
				this.messageManager.save(msg);
				return;
			}
			
			//2、一般情况，即保存和下发消息
			ChannelAccount cha = msg.getChannelAccount();
			if(cha == null) { //如果为空，则获取默认账户。一般为新建消息时
				cha = getDefaultAccount(msg.getClazz());
			}			
			if (cha != null) {
				Service sc = serviceMap.get(cha);
				//预处理消息的发送id，防止出现id一致，无法保存的情况。(消息id是唯一索引！)
				preSendProcess(action, msg);				
				try {
					if (msg.getFromAddr() == null) {
						msg.setFromAddr(cha.getAccount().getUser());
					}					
					PutTask task = new PutTask(sc, action, msg);
					Future monitor = ThreadManager.getInstance().submit(task);
					
					//1、自己给自己发(特殊情况)，不保存数据库：
					if(msg.getToAddr().equals(cha.getAccount().getUser()))
						return;					
					//2、快速回复或完整回复，目前设定微博不需要保存，其他都保存：
					if(action == Constants.ACTION_QUICK_REPLY ||
							action == Constants.ACTION_REPLY)
					{
						if (msg.isWeibo()) 
						{
							return;						
						}
					}

					if (msg.getId() != null) { // 删除可能存在的消息的原始草稿信息					
						this.deleteMessage(msg.getId());
					}
					msg.setType(ChannelMessage.TYPE_SEND);//强制转换类型
					msg.setProcessed(true);
					messageManager.save(msg);
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
			
			preSendProcess(action, msg);
			
			ChannelAccount cha = msg.getChannelAccount();
			if (cha != null) {
				Service sc = serviceMap.get(cha);
				sc.post(action, msg);
			}
		}

	}

	/**
	 * Pre deal 
	 */
	private void preSendProcess(String action, ChannelMessage msg)
	{
		String msgId = msg.getMessageID();
		//forward, resend, transfer, quickreply
		if (msgId != null)
		{
			UUID idOne = UUID.randomUUID();
			msg.setMessageID(msgId +'$' + idOne);//add separator '$' for some use.
		}
	}
	private String getRandomMessageID()
	{
		long millis = System.currentTimeMillis();
		UUID idOne = UUID.randomUUID();
		return millis + "$" + idOne;
	}
	
	
	/**
	 * 按照消息数据的ID删除。
	 * 
	 * @param msg
	 */
	public void deleteMessage(ObjectId id) {
		// get
		ChannelMessage msg = messageManager.getByID(id);
		// delete
		ds.delete(ChannelMessage.class, id);
		// event to outer
		ChannelMessage[] cmsg = new ChannelMessage[1];
		cmsg[0] = msg;
		MessageEvent evt = new MessageEvent(null, null,
				MessageEvent.Type_Deleted, null, cmsg, null);
		fireMessageEvent(evt);

	}

	/**
	 *  按照消息的的ID删除。（消息ID是否唯一，有待讨论).
	 * @param msg
	 */
	public void deleteMessage(String msgId) {
		// get
		Iterator<ChannelMessage> ite = messageManager.getByMessageID(msgId);

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
				MessageEvent.Type_Deleted, null, msgs, null);
		fireMessageEvent(evt);
	}
	
	
	/**
	 *  按照Filter 删除消息。
	 * @param msg
	 */
	public void deleteMessages(Filter filter) {
		// get
		Iterator<ChannelMessage> ite = messageManager.getMessage(filter);

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
				MessageEvent.Type_Deleted, null, msgs, null);
		fireMessageEvent(evt);
	}
	
	public ChannelAccount getDefaultAccount(String msgClazz) {
		if (Constants.MAIL.equals(msgClazz)) {
			return getDefaultAccount(CHANNEL.MAIL);
		} else if (Constants.QQ.equals(msgClazz)) {
			return getDefaultAccount(CHANNEL.QQ);
		} else if (Constants.WEIBO.equals(msgClazz)) {
			return getDefaultAccount(CHANNEL.WEIBO);
		}
		return null;
	}
	
	public ChannelAccount getDefaultAccount(CHANNEL channel) {
		Query<ChannelAccount> q = ds.createQuery(ChannelAccount.class);
		switch (channel) {
		case MAIL:
			Pattern pattern = Pattern.compile("^email_.+$");// 以email_打头
			q.filter("account.channelName", pattern);
			break;

		case QQ:
			q.field("account.channelName").equal("im_qq");
			break;

		case WEIBO:
			q.field("account.channelName").equal("sns_weibo");
			break;
		}
		return q.get();
	}
	
	/**
	 * 
	 * @param userName
	 * @return
	 */
	public ChannelAccount getAccount(String userName) {
		Query<ChannelAccount> q = ds.createQuery(ChannelAccount.class).field("account.user").endsWithIgnoreCase(userName);
		return q.get();
	}

//	@Override
//	public ChannelAccount getChannelAccount() {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
	//注意不需要再次查询数据库：
	public List<Account> getAccounts() {
		return accountManager.getAccounts();
	}
	
	public void addChannelFromAccount(Account acc) {
		if(hasChannelByAccount(acc)) 
			return;
		
		Channel ch = new Channel();
		ch.setName(acc.getChannelName());
		ch.setVendor(acc.getVendor());
		ch.setType("email");
		ch.setStatus("running");
		ch.setDomain(acc.getDomain());
		Protocol[] protocols = new Protocol[2];
		protocols[0] = new Protocol(); //接收服务器
		protocols[0].setName(acc.getRecvProtocol());
		protocols[0].setType("incoming");
		protocols[0].setServer(acc.getRecvServer());
		protocols[0].setPort(acc.getRecvPort());
		
		protocols[1] = new Protocol();//发送服务器
		protocols[1].setName(acc.getSendProtocol());
		protocols[1].setType("outgoing");
		protocols[1].setServer(acc.getSendServer());
		protocols[1].setPort(acc.getSendPort());
		ch.setProtocols(protocols);
		
		ch.setContentType(new String[]{"html"});
		ch.setActions(new String[]{"send","receive","reply","quick reply"});
		
		ds.save(ch);
	}
	
	public void delChannelByAccount(Account acc) {//
		if(acc != null && acc.getUser() != null) {
			Query<ChannelAccount> q = ds.createQuery(ChannelAccount.class).field("account.user").equal(acc.getUser());
			q.disableValidation();
			ds.delete(q);
		}
	}
	
	public void pushAccountToProfile(Account acc) {//添加一个账户至profile中
		if(acc != null) {
			Query<Profile> q = ds.createQuery(Profile.class); //这里目前只有一个
			UpdateOperations<Profile> ops = ds.createUpdateOperations(
					Profile.class).add("accounts", acc) ;
			
			ds.update(q, ops, true);
		}
	}
	public void popAccountFromProfile(Account acc) {//从profile中移除一个账户，注意覆盖Account类的equals()方法
		if(acc != null) {
			Query<Profile> q = ds.createQuery(Profile.class); //这里目前只有一个
			UpdateOperations<Profile> ops = ds.createUpdateOperations(
					Profile.class).removeAll("accounts", acc);
			ds.update(q, ops, false);
		}
	}
	public boolean hasAccountInProfile(Account acc) {//是否存在此账户
		if (acc != null) {
			Query<Profile> q = ds.createQuery(Profile.class).retrievedFields(true, "accounts");
			Profile profile = q.get();
			if (profile != null) {
				Account[] accounts = profile.getAccounts();
				if (accounts != null && accounts.length > 0) {
					for (Account account : accounts) {
						if (account.equals(acc))
							return true;
					}
				}
			}
		}

		return false;
	}
	public boolean hasChannelByAccount(Account acc) {
		if(acc != null && acc.getName() != null) {
			Query<Channel> q = ds.createQuery(Channel.class).field("name").endsWithIgnoreCase(acc.getName());
			return q.iterator().hasNext();
		}
		return false;
	}
	
	// ///////////////////
	/**
	 * 把消息放入到垃圾桶内。如果消息已经在垃圾桶或者草稿箱中，将删除。
	 * 
	 * @param msg
	 */
	public void trashMessage(ChannelMessage msg) {
		// TODO Auto-generated method stub
		if (msg == null) {
			return;
		}
		
		Integer trashFlag = msg.getFlags() != null ? msg.getFlags().get(ChannelMessage.FLAG_TRASHED) : null;
		Integer draftFlag = msg.getFlags() != null ? msg.getFlags().get(ChannelMessage.FLAG_DRAFT) : null;
		if (trashFlag != null && trashFlag.intValue() == 1) {// 已经在垃圾箱里面，删除
			this.deleteMessage(msg.getId());
		} else if (draftFlag != null && draftFlag.intValue() == 1) {// 草稿，删除
			this.deleteMessage(msg.getId());
		} else {
			this.updateMessageFlag(msg, ChannelMessage.FLAG_TRASHED, 1);
		}
	}

	/**
	 * 更新消息的状态(发送、接受 2种）
	 * 
	 * @param msg
	 * @param newValue
	 */
	public void updateMessageStatus(ChannelMessage msg, String newValue) {

		// TODO Auto-generated method stub
		if (newValue == null || msg == null) {
			return;
		}
		ObjectId id = msg.getId();
		if (id == null) {
			return;
		}
		Query<ChannelMessage> chmsgQuery = ds.find(ChannelMessage.class)
				.field("id").equal(id);

		UpdateOperations<ChannelMessage> ops = ds.createUpdateOperations(
				ChannelMessage.class).set("status", newValue);
		ds.update(chmsgQuery, ops, true);

		ChannelMessage newMsg = ds.find(ChannelMessage.class).field("id").equal(id).get();
		// fire event
		MessageEvent evt = new MessageEvent(this, null,
				MessageEvent.Type_Updated, null, null,
				new ChannelMessage[] { newMsg });
		this.fireMessageEvent(evt);
	}
	
	public void updateMessageFlag(ChannelMessage msg, String key,
			Object newValue) {
		updateMessageFlags(msg, new String[]{key}, new Object[]{newValue});
	}

	/**
	 * 更新消息控制（。
	 * 
	 * @param msg
	 * @param key
	 * @param newValue
	 */
	public void updateMessageFlags(ChannelMessage msg, String[] keys,
			Object[] newValues) {

		// TODO Auto-generated method stub
		if (keys == null || newValues == null || msg == null) {
			return;
		}
		ObjectId id = msg.getId();
		if (id == null) {
			return;
		}
		
		int size= Math.min(keys.length, newValues.length);
		if(size < 1) {
			return;
		}
		
		Query<ChannelMessage> chmsgQuery = ds.find(ChannelMessage.class)
				.field("id").equal(id);

		UpdateOperations<ChannelMessage> ops = ds.createUpdateOperations(
				ChannelMessage.class);
		ops.disableValidation();
		for(int i = 0; i<size; i++) {
			ops = ops.set("flags." + keys[i], newValues[i]);
		}
		
		ds.update(chmsgQuery, ops, true);
		ChannelMessage newMsg = ds.find(ChannelMessage.class).field("id").equal(id).get();

		// fire event
		MessageEvent evt = new MessageEvent(this, null,
				MessageEvent.Type_Updated, null, null,
				new ChannelMessage[] { newMsg });
		this.fireMessageEvent(evt);
	}

	/**
	 * 更新消息。
	 * 
	 * @param msg
	 * @param newAtts
	 */
	public void updateMessage(ChannelMessage msg,
			HashMap<String, Object> newAtts) {
		// TODO Auto-generated method stub
		if (newAtts == null || newAtts.size() == 0 || msg == null) {
			return;
		}
		ObjectId id = msg.getId();
		if (id == null) {
			return;
		}
		Query<ChannelMessage> chmsgQuery = ds.find(ChannelMessage.class)
				.field("id").equal(id);

		for (String key : newAtts.keySet()) {
			UpdateOperations<ChannelMessage> ops = ds.createUpdateOperations(
					ChannelMessage.class).set(key, newAtts.get(key));
			ds.update(chmsgQuery, ops);
		}
		ChannelMessage newMsg = ds.find(ChannelMessage.class).field("id").equal(id).get();
		// fire event
		MessageEvent evt = new MessageEvent(this, null,
				MessageEvent.Type_Updated, null, null,
				new ChannelMessage[] { newMsg });
		this.fireMessageEvent(evt);

	}
	
	// /////////////////////////////
	
	private class PutTask  implements Callable
	{
		
		String action;
		ChannelMessage msg;
		Service oneChannel;
		PutTask(Service oneChannel, String action, ChannelMessage msg)
		{		
			this.oneChannel = oneChannel;
			this.action = action;
			this.msg = msg;
		}

		@Override
		public Object call() {
			// TODO Auto-generated method stub
			try{
				oneChannel.put(action, msg);			
			}
			catch(Exception exp)
			{
				exp.printStackTrace();
			}
			return msg;
		}
		
	}
	
	
	/**
	 * 
	 * @author sxy
	 *
	 */
	private class MonitorRunable  implements Runnable
	{
		Future monitor;
		MonitorRunable(Future monitor)
		{	
			this.monitor = monitor;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (monitor.isDone())
			{
				//fire Event to outer listner, the message deal over.
				try {
					Thread.currentThread().join(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
	}

	/**
	 * 
	 */
	@Override
	public  List<Account> getContacts() {
		// TODO Auto-generated method stub
		synchronized (serviceMap) {
			ArrayList<Account> all = new ArrayList<Account>();
			Collection<Service> scm = serviceMap.values();		
			for (Service sc : scm)
			{			
				try
				{
					if (sc != null && sc.getContacts() != null ){
					all.addAll(sc.getContacts());
					}
				}
				catch(Exception exp)
				{
					exp.printStackTrace();
				}			
			}
			return all;
		}
	}
	
	/**
	 * 
	 * @param channelName
	 * @return
	 */
	public Service getService(ChannelAccount ca)
	{
		return this.serviceMap.get(ca);
	}

	////////////////////////////////////
//	渠道管理	启动、停用、禁用，安装，卸载。	Song	2013-2-11	2013-2-12		
//	账号的管理	渠道多账号的管理	Song	2013-2-12	2013-2-13		


	@Override
	public void add(Service svc) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void delelte(Service svc) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void update(Service svc) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Service get(String name) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public java.util.Iterator<Service> Iterator() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void start(Service svc) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void stop(Service svc) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void suspend(Service svc) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void resume(Service svc) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void getStatus(Service svc) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean isRunning(Service svc) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
