package com.original.service.channel.protocols.email.services;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.event.EventListenerList;

import com.original.service.channel.AbstractService;
import com.original.service.channel.Account;
import com.original.service.channel.ChannelAccount;
import com.original.service.channel.ChannelMessage;
import com.original.service.channel.Service;
import com.original.service.channel.event.MessageEvent;
import com.original.service.channel.event.MessageListner;

public class EmailService extends AbstractService {

	private EmailSender sender;
	private EmailReceiver receiver;

	public EmailService(String uid, ChannelAccount ca) {
		sender = new EmailSender(uid, ca);
		receiver = new EmailReceiver(ca, this);
	}

	public List<ChannelMessage> get(String action, String query) {
		return null;
	}

//	@Override
//	public void post(String action, List<ChannelMessage> msg) {
//		// TODO Auto-generated method stub
//
//	}
//
//	public void put(String action, List<ChannelMessage> msg) {
//	}

	public void stop() {
	}

	public void suspend() {
	}
	/**
	 * 启动邮件的接受和发送服务。
	 */
	public void start() {
		sender.start();
		receiver.start();
	}

	public void init() {
	}

//	@Override
//	public List<ChannelMessage> delete(String action, String query) {
//		// TODO Auto-generated method stub
//		return null;
//	}
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

	/**
	 * 
	 */
	@Override
	public void put(String action, ChannelMessage msg){		
//		if (action == Constants.ACTION_QUICK_REPLY) { // 快速回复不需要附件等信息
//			msg.setAttachments(null);
//			msg.setExtensions(null);
//		}
		this.sender.send(msg);
		this.parsePeople(msg, true);
	}
	
	/*public void PrePutDeal(String action, ChannelMessage msg) {		
		if (action.equals(Constants.ACTION_SEND))
		{
	
		}
		quickReplyMessage(msg);		
	}
	
	*//**
	 * Forward Email will apend more info: such as 
	 * Subject: 	Fwd: Packt Publishing: You are now unsubscribed
	 * Body:
	 * 	---------- Forwarded message ----------
	 * 	From: packt@packtpub.com <marketing@packtpub.com>
	 * 	Date: Sat, Dec 22, 2012 at 9:45 PM
	 * 	Subject: Packt Publishing: You are now unsubscribed
	 * 	To: franzsoong@gmail.com
	 * @param msg
	 *//*
	private void quickReplyMessage(ChannelMessage msg)
	{
		
		ChannelService cs = ChannelService.instance();
		MessageManager mm = cs.getMsgManager();
//		. Manager msgManager
//	{		
//		if (action
//				.endsWith(com.original.service.channel.Constants.ACTION_QUICK_REPLY)) {
			// Original message id.
			if (msg.getId() != null) {
				ChannelMessage chmsg = mm.getMessage(msg.getId());
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
//		}
	}

	
	*//**
	 * Forward Email will apend more info: such as 
	 * Subject: 	Re: Packt Publishing: You are now unsubscribed
	 * Body:
	 * 	---------- Replied message ----------
	* 	<blockquote style="margin: 0px 0px 0px 0.8ex; border-left-width: 1px; border-left-color: rgb(204, 204, 204); border-left-style: solid; padding-left: 1ex;">
	* 	<u></u>
	*   [原内容]
	* 	</blockquote>
	 * @param msg
	 *//*
	private void replyMessage(ChannelMessage msg)
	{
		
		ChannelService cs = ChannelService.instance();
		MessageManager mm = cs.getMsgManager();
//		. Manager msgManager
//	{		
//		if (action
//				.endsWith(com.original.service.channel.Constants.ACTION_QUICK_REPLY)) {
			// Original message id.
			if (msg.getId() != null) {
				ChannelMessage chmsg = mm.getMessage(msg.getId());
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
//		}
	}
	*//**
	 * Forward Email will apend more info: such as 
	 * Subject: 	Fwd: Packt Publishing: You are now unsubscribed
	 * Body:
	 * 	---------- Forwarded message ----------
	 * 	From: packt@packtpub.com <marketing@packtpub.com>
	 * 	Date: Sat, Dec 22, 2012 at 9:45 PM
	 * 	Subject: Packt Publishing: You are now unsubscribed
	 * 	To: franzsoong@gmail.com
	 * @param msg
	 *//*
	private void forwardMessage(ChannelMessage msg)
	{
		
		ChannelService cs = ChannelService.instance();
		MessageManager mm = cs.getMsgManager();
//		. Manager msgManager
//	{		
//		if (action
//				.endsWith(com.original.service.channel.Constants.ACTION_QUICK_REPLY)) {
			// Original message id.
			if (msg.getId() != null) {
				ChannelMessage chmsg = mm.getMessage(msg.getId());
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
//		}
	}
	
	*/
	
	

	@Override
	public void post(String action, ChannelMessage msg) {
		// TODO Auto-generated method stub
		
		this.sender.send(msg);
		
		ChannelMessage[] cmsg = new ChannelMessage[1];
		//邮件的消息转换为渠道的消息
		cmsg[0] = msg;
		MessageEvent evt = new MessageEvent(null, null,MessageEvent.Type_Updated, cmsg, null,null);
		fireMessageEvent(evt);
		
		
		
	}

	/////////////////////////////////////
	

	
	/**
	 * 临时方法。如果google有联系人服务，目前没有时间处理，缓冲所有受到和发出去的联系人,
	 * 因此第一次调用是空白。不再去查已有消息了。 
	 */	
	@Override
	public List<Account> getContacts() {
		// TODO Auto-generated method stub
		return new ArrayList<Account>(contracts.values());
		
	}
	
	HashMap<String, Account> contracts = new HashMap<String, Account>();
			
	
	/**
	 * 解析邮件的信息为联系人
	 * @param chm
	 */
	protected void parsePeople(ChannelMessage chm, boolean isSend)
	{	
		//如果发送，取发送、抄送、暗送
		if (isSend)
		{
			String toAddr = chm.getToAddr();
			String ccAddr = chm.getCC();
			parseAddr(toAddr);
			parseAddr(ccAddr);
			if (chm.getExtensions() != null)
			{
				String bccAddr = chm.getExtensions().get(ChannelMessage.EXT_EMAIL_BCC);
				parseAddr(bccAddr);
			}
		}
		else
		{
			String fromAddr = chm.getFromAddr();
			parseAddr(fromAddr);
		}
	}
		
	/**
	 * 
	 * @param msgAddr
	 */
	private void parseAddr(String msgAddr)
	{	
		if (msgAddr != null && msgAddr.lastIndexOf("<") != -1) {
			int dimi = msgAddr.lastIndexOf("<");
			String name = msgAddr.substring(0, dimi);
			String addr = msgAddr.substring(dimi + 1, msgAddr.length() - 1);
			if (name != null && name.trim().length() > 0 && addr != null && addr.length() > 0)
			{
				Account ac = new Account();
				ac.setName(name);
				ac.setUser(addr);
				//如果存在将覆盖，如果不存在就插入
				this.contracts.put(addr, ac);
			}
		}
	}
		
	
	
	
	/////////////////////////////////////
}
