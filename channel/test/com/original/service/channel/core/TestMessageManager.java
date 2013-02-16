package com.original.service.channel.core;

import java.util.List;

import org.junit.Test;

import com.channel.test.TestBase;
import com.original.service.channel.ChannelMessage;
import com.original.service.people.People;

//获取消息按照分页（参数页面、每页数量）	Song	2013-2-8	2013-2-9
//"个人信息分页展示：
//获取一个联系人的所有消息，按照时间排序。分页展示。（参数有页面，每个个数）"	Song	2013-2-9	2013-2-10


public class TestMessageManager extends TestBase {

	/**
	 * 获得所有消息的数量。
	 */
	@Test
	public void testGetMessageCount() {
		System.out.println(csc.getMsgManager().getMessageCount());
	}

	/**
	 * 获得某个人的所有消息，按照分页展示。
	 */
	@Test
	public void testGetMessageByPeopleByPaging() {

		People p = csc.getPeopleManager().getPeople("宋佳").get(0);

		List<ChannelMessage> cm = csc.getMsgManager().getMessageByPeople(p, 0,
				5);
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
	 * 获得某个人的所有消息。
	 */
	@Test
	public void testGetMessageByPeople() {
		People p = csc.getPeopleManager().getPeople("宋佳").get(0);

		List<ChannelMessage> cm = csc.getMsgManager().getMessageByPeople(p);
		for (ChannelMessage m : cm) {
			System.out.println(m);
		}
	}
	
//	后台服务	提供统计的接口	Song	2013-2-13	2013-2-14
	@Test
	public void testGetMessageCountByPeople() {
		People p = csc.getPeopleManager().getPeople("宋佳").get(0);

		//获得一个联系人的所有渠道信息数量（有些联系人的账号已经删除，
		//但是Account是内嵌数据，即没有内联，因此不要要考虑账号是否存在
		//查找，仍然查找(Message, clazz)，如果更细致信息，如某一个账号，需要用户传入参数。
		//Pending:  联系人是否可以删除?由于联系人之间通过ID(reference)建立管理，一个人不存在了
		//相关的信息应该不收限制
		List<ChannelMessage> cm = csc.getMsgManager().getMessageByPeople(p);
		for (ChannelMessage m : cm) {
			System.out.println(m);
		}
	}	
	
	//	后台服务	提供统计的接口	Song	2013-2-13	2013-2-14
	//	前台界面	列表顶部显示整个联系过程的统计信息	Wei	2013-2-25	2013-2-25	
	//	后台服务	提供统计的接口	Song	2013-2-13	2013-2-14
	//获得一个联系人的所有渠道信息数量（有些联系人的账号已经删除，
	//但是Account是内嵌数据，即没有内联，因此不要要考虑账号是否存在
	//查找，仍然查找(Message, clazz)，如果更细致信息，如某一个账号，需要用户传入参数。
	//Pending:  联系人是否可以删除?由于联系人之间通过ID(reference)建立管理，一个人不存在了
	//相关的信息应该不收限制
	@Test
	public void testGetMessageClazzCountByPeople() {
		People p = csc.getPeopleManager().getPeople("宋佳").get(0);

//		public static final String QQ = "QQ";
//		public static final String WEIBO = "Weibo";
//		public static final String MAIL = "Mail";
		String[] clazzs = {"QQ", "Weibo","Mail"} ;
		for (int i = 0; i < clazzs.length; i++)
		{
			String clazz = clazzs[i];
			long count = csc.getMsgManager().getMessageCountByPeople(p, clazz);
			System.out.println("clazz:"  + clazz + ")"+ count);
			List<ChannelMessage> cm = csc.getMsgManager().getMessageByPeople(p, clazz);
			for (ChannelMessage m : cm) {
				System.out.println(m);
			}
			
		}
		
	}
	
}
