package com.original.service.channel.protocols.im.iqq;

/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

import iqq.comm.Auth;
import iqq.comm.Auth.AuthInfo;
import iqq.service.CategoryService;
import iqq.service.LoginService;
import iqq.service.MemberService;

import java.util.EventListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;

import com.original.service.channel.Account;
import com.original.service.channel.ChannelAccount;
import com.original.service.channel.ChannelMessage;
import com.original.service.channel.Service;
import com.original.service.channel.core.ChannelException;
import com.original.service.channel.core.ChannelException.TYPE;
import com.original.service.channel.event.MessageEvent;
import com.original.service.channel.event.MessageListner;
import com.original.util.log.OriLog;

/**
 *  iQQ 服务类
 * @author WMS
 */
public class QQService implements Service{

private static Logger log = OriLog.getLogger(QQService.class);

private static LoginService loginService = LoginService.getInstance();//QQ登陆服务
private static MemberService memberService = MemberService.getInstance();//QQ成员服务
private static CategoryService categoryService = CategoryService.getInstance();//QQ好友服务
	
	private QQSender sender;
	private QQReceiver receiver;
	private ChannelAccount ca;
	private AuthInfo ai = null;//用户登录成功后，会返回一个授权用户信息AuthInfo
	private int status;
	
	public QQService(String uid, ChannelAccount ca)throws ChannelException
	{
		// TODO Auto-generated constructor stub
		this.ca = ca;
		sender = new QQSender(uid, ca);
		receiver = new QQReceiver(ca, this);
		status = CREATED;
		init() ;
	}
	
	//初始QQ服务，这里为打开QQ登录服务
	private void init() throws ChannelException {
		Account account = null;
		if(ca != null && (account = ca.getAccount()) != null)
		{
			HashMap<String, String> loginMap = new HashMap<String, String>();
			loginMap.put("account", account.getUser() + "@qq.com");//注意这里QQ账号只能为申请的N位数字，目前不支持邮箱账号或手机号
			loginMap.put("password", account.getPassword());
			loginMap.put("verifyCode", account.getPassword());//验证码，有时需要用到
			loginMap.put("loginStatus", "online");
			
			try {
				loginService.login(loginMap);
				//获取所有好友的信息，同时绑定账号和Uin
				ai = Auth.getAccountInfo(loginMap.get("account"));
			}
			catch(Exception ex) {
				status = FAILED;
				throw new ChannelException(ca, TYPE.QQ, ex.getMessage() + "\n是否重试？");
			}
		}
	}

	@Override
	public List<ChannelMessage> delete(String action, String query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ChannelMessage> get(String action, String query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void put(String action, List<ChannelMessage> msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void post(String action, List<ChannelMessage> msg) {
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
		// TODO Auto-generated method stub
		sender.start();
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

	@Override
	public int getStatus() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void put(String action, ChannelMessage msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void post(String action, ChannelMessage msg) {
		// TODO Auto-generated method stub
		
	}

	/////////////////////////////////////

}
