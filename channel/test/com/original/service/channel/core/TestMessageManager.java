package com.original.service.channel.core;

import java.util.List;

import org.junit.Test;

import com.channel.test.TestBase;
import com.original.service.channel.ChannelMessage;
import com.original.service.people.People;
public class TestMessageManager extends TestBase {

	
	
	
	

	@Test
	public void testGetMessageCount() {
		System.out.println(csc.getMsgManager().getMessageCount());
	}
	
	
	@Test
	public void testGetMessageByPeopleByPaging() {
		
		People p = csc.getPeopleManager().getPeople("宋佳").get(0);
	
		List<ChannelMessage> cm = csc.getMsgManager().getMessageByPeople(p, 0, 5);
		for (ChannelMessage m : cm) {
			System.out.println(m);
		}
		cm = csc.getMsgManager().getMessageByPeople(p, 1, 5);
		for (ChannelMessage m : cm) {
			System.out.println(m);
		}
		cm = csc.getMsgManager().getMessageByPeople(p, 200, 5);
		for (ChannelMessage m : cm) {
			System.out.println(m);
		}
	}
	
	/**
	 * 按照联系人获取消息。
	 * 
	 * 
	 * @param p
	 * @return
	 */
	@Test
	public void testGetMessageByPeople() {
		People p = csc.getPeopleManager().getPeople("宋佳").get(0);
		
		List<ChannelMessage> cm = csc.getMsgManager().getMessageByPeople(p);
		for (ChannelMessage m : cm) {
			System.out.println(m);
		}
		
	}
}
