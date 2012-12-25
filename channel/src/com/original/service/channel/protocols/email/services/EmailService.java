package com.original.service.channel.protocols.email.services;

import java.util.EventListener;
import java.util.List;

import javax.swing.event.EventListenerList;

import com.original.service.channel.ChannelAccount;
import com.original.service.channel.ChannelMessage;
import com.original.service.channel.Constants;
import com.original.service.channel.Service;
import com.original.service.channel.event.MessageEvent;
import com.original.service.channel.event.MessageListner;

public class EmailService implements Service {

	private EmailSender sender;
	private EmailReceiver receiver;

	public EmailService(String uid, ChannelAccount ca) {
		sender = new EmailSender(uid, ca);
		receiver = new EmailReceiver(ca, this);
	}

	public List<ChannelMessage> get(String action, String query) {
		return null;
	}

	@Override
	public void post(String action, List<ChannelMessage> msg) {
		// TODO Auto-generated method stub

	}

	public void put(String action, List<ChannelMessage> msg) {
	}

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

	@Override
	public List<ChannelMessage> delete(String action, String query) {
		// TODO Auto-generated method stub
		return null;
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
		//发送消息
		if (action.equalsIgnoreCase(Constants.ACTION_QUICK_REPLY))
		{
			msg.setSubject("Re:" + (msg.getSubject() == null ? "" : msg.getSubject()));
		}
		
		this.sender.send(msg);
		//派发事件给监听（存盘、更新视图）
		ChannelMessage[] cmsg = new ChannelMessage[1];
		//邮件的消息转换为渠道的消息
		cmsg[0] = msg;
		//
		MessageEvent evt = new MessageEvent(null, null,MessageEvent.Type_Added, cmsg, null,null);
		fireMessageEvent(evt);
		
		
	}

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
}
