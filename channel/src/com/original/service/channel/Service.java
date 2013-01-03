/**
 * 
 */
package com.original.service.channel;

import java.util.List;

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
	public abstract List<ChannelMessage> delete(String action, String query);
	
	public abstract List<ChannelMessage> get(String action, String query);

	public abstract void put(String action, List<ChannelMessage> msg);
	
	public abstract void post(String action, List<ChannelMessage> msg);
	
	public abstract void put(String action, ChannelMessage msg) throws Exception;
	
	public abstract void post(String action, ChannelMessage msg);



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
