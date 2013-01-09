package com.original.service.channel.protocols.sns.weibo;

/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;

import weibo4j.model.WeiboException;

import com.original.service.channel.AbstractService;
import com.original.service.channel.Account;
import com.original.service.channel.ChannelAccount;
import com.original.service.channel.ChannelMessage;
import com.original.service.channel.Constants;
import com.original.service.channel.Constants.CHANNEL;
import com.original.service.channel.Service;
import com.original.service.channel.core.ChannelException;
import com.original.service.channel.event.MessageEvent;
import com.original.service.channel.event.MessageListner;
import com.original.service.people.People;
import com.original.util.log.OriLog;

/**
 * 新浪微博服务类
 * @author WMS
 *
 */
public class WeiboService extends AbstractService {

	private static Logger log = OriLog.getLogger(WeiboService.class);
	
	private WeiboSender sender;
	private WeiboReceiver receiver;
	private ChannelAccount ca;
	
	//微博授权AccessToken本地保存文件名
	public static File SNS_WEIBO_OAUTH = new File(System.getProperty("user.home"), 
			"channel/sns_weibo_oauth.dat");
	private static Map<Account, String> accessTokens = new HashMap<Account, String>();
	private static Properties weiboProp = new Properties();
	
	static {
		try {
			InputStream in = new FileInputStream(SNS_WEIBO_OAUTH);
			weiboProp.load(in); //将文件读至内存中
			in.close();
		}
		catch(Exception ex) {
			log.error(ex.getMessage());
		}
	}
	
	public WeiboService(String uid, ChannelAccount ca) throws ChannelException{
		this.ca = ca;
		sender = new WeiboSender(uid, ca);
		receiver = new WeiboReceiver(ca, this);
		
		init();
	}
	
	/**
	 * 初始化WeiboService，这里为验证Weibo的accessToken是否有效(是否存在以及是否过期)。
	 * 如果无效，需要重新授权。
	 */
	private synchronized void  init() throws ChannelException{
		Account account = null;
		if(ca != null && (account = ca.getAccount()) != null)
		{
			String token = accessTokens.get(account);
			if(token == null) {
				token = weiboProp.getProperty(account.getUser());
			}
			
			checkValidAccessToken(ca, token);
			accessTokens.put(account, token);
		}
	}
	
	/**
	 * 检查授权Token是否已失效
	 * @param token
	 * @return
	 * @throws WeiboException
	 */
	private void checkValidAccessToken(ChannelAccount account, String token) throws ChannelException
	{
		if(token == null || token.length() < 32) {
			ChannelException ex = new ChannelException(account,CHANNEL.WEIBO,
					"You need to authorize your weibo account first " +
					"before using WeiboService!");
			throw ex;
		}
		
		try {
			weibo4j.Account wa = new weibo4j.Account();
			wa.setToken(token);
			wa.getUid();
			//收集表情，注意放在线程中。
			WeiboParser.collectionEmotions(token);
		}
		catch(WeiboException ex) {
			if ("expired_token".equals(ex.getError()) || 21327 == ex.getErrorCode())
			{
				throw new ChannelException(account ,CHANNEL.WEIBO,
						"Weibo AccessToken has been expired, " +
						"you need to reauthorize your weibo account again!");
			} else {
			    throw new ChannelException(null, CHANNEL.WEIBO,  ex);
			}
		}
	}
	
	public Map<Account, String> getAccessTokens()
	{
		return accessTokens;
	}
	
	public synchronized static void storeToken(String account, String token)
	{
		if(account != null && token != null && token.length() == 32) {
			weiboProp.setProperty(account, token);
			try
			{
				if(!SNS_WEIBO_OAUTH.exists()) {
					SNS_WEIBO_OAUTH.getParentFile().mkdirs();
					SNS_WEIBO_OAUTH.createNewFile();
				}
				FileOutputStream out = new FileOutputStream(SNS_WEIBO_OAUTH,false);
				weiboProp.store(out, null);
				out.close();
			} catch (Exception ex)
			{
				ex.printStackTrace();
			} 
		}
	}
	
	public synchronized static String readToken(ChannelAccount ca) {
		Account account = null;
		if(ca != null && (account = ca.getAccount()) != null) {
			return readToken(account.getUser());
		}
		return null;
	}
	public synchronized static String readToken(String account) {
		if(account != null) {
			return weiboProp.getProperty(account);
		}
		return null;
	}	
	public synchronized static String readOneToken() {
		Enumeration<Object> tokens = weiboProp.elements();
		if(tokens.hasMoreElements() ) {
			return (String)tokens.nextElement();
		}
		return null;
	}

//	@Override
//	public List<ChannelMessage> delete(String action, String query) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<ChannelMessage> get(String action, String query) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public void put(String action, List<ChannelMessage> msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void post(String action, List<ChannelMessage> msg) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * 下发一条微博，注意区别发送(回复)类型：快速回复是纯文本，而回复是HTML。
	 * @param action 回复类型 {@link Constants#ACTION_QUICK_REPLY}和{@link Constants#ACTION_REPLY}
	 * @param msg 消息对象
	 */
	@Override
	public void put(String action, ChannelMessage msg) throws Exception{
		// TODO Auto-generated method stub
		sender.send(action, msg);
	}
	
	@Override
	public void post(String action, ChannelMessage msg) {
		// TODO Auto-generated method stub
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
		receiver.start();
	}

	/////////////////////Event///////////////////////
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
	@Override
	public int getStatus() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	/////////////////////////////////////
	
	@Override
	public ChannelAccount getChannelAccount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<People> getContacts() {
		// TODO Auto-generated method stub
		return null;
	}
}
