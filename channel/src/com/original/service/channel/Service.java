/**
 * 
 */
package com.original.service.channel;

import java.util.HashMap;
import java.util.List;

import org.bson.types.ObjectId;

import com.original.service.channel.event.MessageListner;
import com.original.service.people.People;

/**
 * @author Song Xueyong
 *
 */
public interface Service {
	
	public final static int CREATED = 0x0001;
	public final static int STARTED = 0x0002;	
	public final static int SUSPEND = 0x0004;
	public final static int STOPPING = 0x0008;
	public final static int CLOSED = 0x000F;
	public final static int FAILED = 0xFFFF;
	
	public int getStatus();

	//crud
//	public abstract List<ChannelMessage> delete(String action, String query);
	
//	public abstract List<ChannelMessage> get(String action, String query);

	public abstract void put(String action, List<ChannelMessage> msg);
	
	public abstract void post(String action, List<ChannelMessage> msg);
	
	public abstract void put(String action, ChannelMessage msg) throws Exception;
	
	public abstract void post(String action, ChannelMessage msg);


	/**
	 *   删除消息通过消息的ID，删除后回发事件给监听器，类型为MessageEvent.Type_Deleted。
	 * @param id
	 */
	public void deleteMessage(ObjectId id);
	
	/**
	 *  按照消息的的ID删除。（消息ID是唯一).
	 * @param msg
	 */
	public void deleteMessage(String msgId);
	
	
	/**
	 * 把消息放入到垃圾桶内。如果消息一经在垃圾桶，将删除。
	 * @param msg
	 */
	public abstract void trashMessage(ChannelMessage msg);	

	/**
	 * 更新消息的状态(发送、接受 2种）
	 * @param msg
	 * @param newValue
	 */
	public abstract void updateMessageStatus(ChannelMessage msg, String newValue);
	
	/**
	 * 更新消息控制（。
	 * @param msg
	 * @param key
	 * @param newValue
	 */
	public abstract void updateMessageFlag(ChannelMessage msg, String key, Object newValue);
	
	/**
	 * 更新消息。
	 * @param msg
	 * @param newAtts
	 */
	public abstract void updateMessage(ChannelMessage msg, HashMap<String, Object> newAtts);


	//lifecycle
	public abstract void stop();

	public abstract void suspend();

	public abstract void start();
	
	//listener
	
    /**
     * Registers the given observer to begin receiving notifications
     * when changes are made to the document.
     *
     * @param listener the observer to register
     * @see Service#addMessageListener
     */
    public void addMessageListener(MessageListner listener);
    
    /**
     * Registers the given observer to begin receiving notifications
     * when changes are made to the document.
     *
     * @param listener the observer to register
     * @see Service#removeMessageListener
     */
    public void removeMessageListener(MessageListner listener);
	
	

    /**
     * 获得资金的账号信息。
     * @return
     */
    public ChannelAccount getChannelAccount();
    
    /**
     * 获得渠道的联系人列表。
     * @return
     */
    public List<People> getContacts();
	

}
