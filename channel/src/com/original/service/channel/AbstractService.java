package com.original.service.channel;

import java.util.HashMap;
import java.util.List;

import org.bson.types.ObjectId;

import com.original.service.channel.event.MessageListner;

/**
 * 抽象实现，避免修改接口带来集成实现不同。
 * @author sxy
 *
 */
public abstract class AbstractService implements Service{

	@Override
	public int getStatus() {
		// TODO Auto-generated method stub
		return 0;
	}

//	@Override
//	public List<ChannelMessage> delete(String action, String query) {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	@Override
//	public List<ChannelMessage> get(String action, String query) {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	@Override
//	public void put(String action, List<ChannelMessage> msg) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void post(String action, List<ChannelMessage> msg) {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public void put(String action, ChannelMessage msg) throws Exception {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addMessageListener(MessageListner listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeMessageListener(MessageListner listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ChannelAccount getChannelAccount() {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public List<People> getContacts() {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public void deleteMessage(ObjectId id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteMessage(String msgId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void trashMessage(ChannelMessage msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateMessageStatus(ChannelMessage msg, String newValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateMessageFlag(ChannelMessage msg, String key,
			Object newValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateMessage(ChannelMessage msg,
			HashMap<String, Object> newAtts) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Account> getContacts() {
		// TODO Auto-generated method stub
		return null;
	}

}
