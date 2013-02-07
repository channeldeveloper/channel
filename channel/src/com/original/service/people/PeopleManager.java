package com.original.service.people;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;
import com.mongodb.Mongo;
import com.original.service.channel.Account;
import com.original.service.channel.Channel;
import com.original.service.channel.ChannelAccount;
import com.original.service.channel.ChannelMessage;
import com.original.service.channel.Constants;
import com.original.service.channel.core.ChannelService;
import com.original.service.channel.core.MessageManager;
import com.original.service.channel.core.ThreadManager;

public class PeopleManager {
	
	private Mongo mongo;
	private Morphia morphia;
	private Datastore ds;
	ChannelService cs;


	/**
	 * PeopleManager
	 * @param mongo
	 * @param morphia
	 * @param ds
	 */
	public PeopleManager(ChannelService cs, Morphia morphia, Mongo mongo, Datastore ds)
	{
		this.cs = cs;
		this.mongo = mongo;
		this.morphia = morphia;
		this.ds = ds;
		init();
	}
	
	/**
	 *  初始化。
	 */
	private void init()
	{
		try
		{
			LoadContractsTask task = new LoadContractsTask();		
			ThreadManager.getInstance().submit(task);
		}
		catch(Exception exp)
		{
			exp.printStackTrace();
		}
		
	}
	/**
	 * 通过Service获取联系人。
	 */
	public void loadContracts()
	{
		//1 调用各自的渠道服务ChannelService 获得联系人。
		ChannelService cs  = ChannelService.getInstance();
		List<Account> all = cs.getContacts();
		for (Account ac : all)
		{
			String chName = ac.getChannelName();
			if (chName == null)
			{
				continue;
			}
			//2 调用PeopleManager获取所有的人员
			People pp = ds.find(People.class).field("accountMap." + chName +".user").endsWith(ac.getUser()).get();
			//不存在
			//3 更新people中的account的数据，判断条件账号一致
			//4 如果找不到人员，就添加一个People
			if (pp == null)
			{				
				People people  = new People();
				people.setName(ac.getName());
				people.setAvatar(ac.getAvatar());
				HashMap <String, Account> acMap = new HashMap<String, Account>();
				acMap.put(chName, ac);
				people.setAccountMap(acMap);				
				ds.save(people);
			}
			else
			{	
				HashMap<String, Account> acMap = pp.getAccountMap();
				if (acMap == null)
				{
					acMap = new HashMap<String, Account>();
				}
//				Account oldAc = acMap.get(chName);
				//update
				acMap.put(chName, ac);
				UpdateOperations<People> ops = ds.createUpdateOperations(
						People.class).set("accountMap", acMap);
				ds.update(pp, ops);				
			}		
		}
		
		
		//消息的处理
		//收
		//消息的收取，根据渠道，根据账号，去超找人员，找到后，设置一个人员的ID，
		//如果找不到，添加一个新的人员，把人员的ID设置到消息。
		//旧数据，如果人员的ID不存在，认为该人员是一个匿名人,并记录下来，View显示为发信地址
		//发
		//发出去的消息，发的人是自己，收的人选择联系人，自然People的Id,如果手的人手手工输入，匿名人,记录下来。
		
		//联系人的实现，微博，有微博的方法，iQQ有自己的方法，邮件一般有支持联系人服务，但是目前简单实现，缓冲所有的收件和发件人
		
	}
	
	/**
	 * 
	 * @param p
	 * @return
	 */
	public boolean save(People p)
	{
		if (p != null)
		{
			try
			{
				ds.save(p);
				return true;
			}
			catch(Exception exp)
			{
				exp.printStackTrace();
			}
		}
		return false;
	}


	// find
//	/**
//	 * get All Messages
//	 * 
//	 * @return
//	 */
//	public List<People> getPeople() {
//		Query<People> chmsgQuery = ds.find(People.class).order(
//				"name");
//		List<People> chmsgs = chmsgQuery.asList();
//
//		return chmsgs;
//	}
	
	/**
	 * get All Messages
	 * 
	 * @return
	 */
	public List<People> getPeople(String name) {
		Query<People> chmsgQuery = ds.find(People.class)
				.field("name").endsWithIgnoreCase(name)
				.order("name");
		List<People> chmsgs = chmsgQuery.asList();
		return chmsgs;
	}
	
	/**
	 * get All Messages
	 * 
	 * @return
	 */
	public People getPeopleByMessage(ChannelMessage chm) {
		ObjectId pid = chm.getPeopleId();
		if (pid != null) {
			return ds.get(People.class, pid);
		}//旧数据
		try
		{
			Query<People> q= ds.find(People.class);
			ChannelAccount ca = chm.getChannelAccount();
			if (ca != null)
			{
				if (ca.getChannel() != null && ca.getAccount() != null)
				{
					if (chm.isReceived() && chm.getFromAddr() != null)
					{
						return q.field("accountMap." + ca.getChannel().getName() + "." +"name").equal(chm.getFromAddr()).get();
					}
					if (!chm.isReceived() && chm.getToAddr() != null)
					{
						return q.field("accountMap." + ca.getChannel().getName() + "." +"name").equal(chm.getToAddr()).get();
					}
				}
			}
		}
		catch(Exception exp)
		{
			exp.printStackTrace();		
		}
		return null;
	}
	
	/**
	 * get All Messages
	 * 
	 * @return
	 */
	public People getPeopleByChannel(String chName, String channelAccount) {
		try{
			return ds.find(People.class).field("accountMap." + chName+".user").equal(channelAccount).get();
		}
		catch(Exception exp)
		{
			return null;
		}
	}
	

	/**
	 * 
	 * @param chMsg
	 */
	public People savePeople(ChannelMessage chMsg) {
		try {
			if (chMsg == null) {
				return null;
			}
			boolean isReceived = chMsg.isReceived();
			String addr = isReceived ? chMsg.getFromAddr() : chMsg.getToAddr();
			// by name (same as addr)
			if (addr != null) {
				People qp = getPeopleByChannel(chMsg.getChannelAccount()
						.getChannel().getName(), addr);
				if (qp != null) {
					return qp;
				}
			}
			String name = addr;
			// Franz Song<franzsong@gmail.com>
			Channel ch = chMsg.getChannelAccount().getChannel();
			if (ch.getType().equals(Constants.Channel_Type_Email)) {
				String emailAddr = addr;
				if (emailAddr != null && emailAddr.lastIndexOf("<") != -1) {
					int dimi = emailAddr.lastIndexOf("<");
					name = emailAddr.substring(0, dimi);
					addr = emailAddr
							.substring(dimi + 1, emailAddr.length() - 1);
				}
			}
			// search account[]
			People qp = getPeopleByChannel(chMsg.getChannelAccount()
					.getChannel().getName(), addr);
			if (qp != null && qp.getAccountMap() != null) {
				Collection<Account> accs = qp.getAccountMap().values();
				if (accs != null) {
					for (Account ac : accs) {
						// 已经加入了
						if (ac.getChannelName() != null
								&& ac.getChannelName().equals(ch.getName())
								&& ac.getUser().equalsIgnoreCase(addr)) {
							return qp;
						}
					}
				}
			}

			People p = new People();
			p.setName(name);
			Account ac = new Account();
			ac.setUser(addr);
			ac.setChannelName(chMsg.getChannelAccount().getChannel().getName());
			HashMap<String, Account> acMap = new HashMap<String, Account>();
			acMap.put(ac.getChannelName(), ac);
			p.setAccountMap(acMap);
			ds.save(p);
			return p;

		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return null;
	}
//	/**
//	 * 
//	 * @param chMsgs
//	 */
//	public void savePeople(ChannelMessage[] chMsgs)
//	{		
//		//save Message
//		if (chMsgs != null && chMsgs.length > 0)
//		{
//			for (ChannelMessage chmsg : chMsgs)
//			{
//				savePeople(chmsg);
//			}
//		}
//	}
	
	// /////////////////////////////
	
		private class LoadContractsTask  implements Runnable
		{
			private LoadContractsTask()
			{
				
			}

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
					PeopleManager.this.loadContracts();
				}
				catch(Exception exp)
				{
					exp.printStackTrace();
				}
				
			}
			
		}
		
		//////////////For Later Tasks//////////////
		
	//		获取所有联系人（按照名称，按照消息的ReceivedTime先后顺序）排序
	//		获取一个联系人的所有消息，按照时间排序。
	//		获取消息按照分页（参数页面、每页数量）

		/**
		 * 获取联系人的数量。
		 * 
		 * @return
		 */
		public long getPeopleCount() {
			return ds.getCount(People.class);
		}

		/**
		 * 获取所有的联系人，按照姓名排序。
		 * 
		 * @return
		 */
		public List<People> getPeople() {
			Query<People> chmsgQuery = ds.find(People.class).order(
					"name");
			List<People> chmsgs = chmsgQuery.asList();

			return chmsgs;
		}
		
		/**
		 * 按照分页，每个页面的数量，获取的联系人。
		 * 		
		 * 
		 * @param page
		 * @param pageCount
		 * @return
		 */
		public List<People> getPeople(int page, int pageCount) {
			Query<People> chmsgQuery = ds.find(People.class).order(
					"name");
			
			//offset(1000);
			int offset = page * pageCount;
			if (offset > 0)
			{
				chmsgQuery = chmsgQuery.offset(offset).limit(pageCount);
			}
			else
			{
				chmsgQuery = chmsgQuery.limit(pageCount);
			}
			List<People> chmsgs = chmsgQuery.asList();

			return chmsgs;
		}
		
		/**
		 * pending 按照消息的ReceivedTime先后顺序.
		 * 
		 * 1、该数据是变化，不应该时时调用
		 * 2、数据没有按照人来分类管理，获取的性能不高
		 * 3、Client初始化化可以调用。
//		 * 4、后面设计需要考虑，是否建立一个Collection表。PeopleMessages 
		 * 
		 * Pending Song Xueyong: Message received by time sequence, but not by people.
		 * So to getting Messages by people has not good performance!
		 * 
		 * @return
		 */
		/**
		 * 获取所有的联系人，按照收到信息的先后排序。
		 * @return
		 */
	public List<People> getPeopleByDate() {
		// 由于消息多，联系人少，因此按照先联系人来获取。获取联系人后去取第一个消息，按照时间排序（默认消息都是时间存储，猜想应该快）。
		List<People> people = getPeople();
		MessageManager mm = cs.getMsgManager();

		// Pending 做法好复杂
		HashMap<ChannelMessage, People> maps = new HashMap<ChannelMessage, People>();
		for (People p : people) {
			List<ChannelMessage> msg = mm.getMessageByPeople(p);
			if (msg != null && msg.size() > 0) {
				maps.put(msg.get(0), p);
			}
		}

		//排序
		ChannelMessage[] cms = new ChannelMessage[maps.size()];
		maps.keySet().toArray(cms);
		List<ChannelMessage> msgList = Arrays.asList(cms);

		Collections.sort(msgList, new Comparator<ChannelMessage>() {
			public int compare(ChannelMessage o1, ChannelMessage o2) {
				Date a = o1.getReceivedDate();
				Date b = o2.getReceivedDate();
				//逆序
				return b.compareTo(a);
			}
		});
		//太复杂了
		people = new ArrayList<People>();
		for (ChannelMessage cm : msgList)
		{
			people.add(maps.get(cm));			
		}		
		return people;

	}
	
	/**
	 * pending 按照消息的ReceivedTime先后顺序.
	 * 
	 * 1、该数据是变化，不应该时时调用 2、数据没有按照人来分类管理，获取的性能不高 3、Client初始化化可以调用。 // *
	 * 4、后面设计需要考虑，是否建立一个Collection表。PeopleMessages
	 * 
	 * Pending Song Xueyong: Message received by time sequence, but not by
	 * people. So to getting Messages by people has not good performance!
	 * 
	 * @return
	 */
	/**
	 * 获取所有的联系人，按照分页和每页数量，按照收到信息的先后排序，。
	 * 
	 * @return
	 */	
	public List<People> getPeopleByDate(int page, int pageCount) {
		
		List<People> people = getPeopleByDate();

		if (page * pageCount < people.size()) {
			return people.subList(page * pageCount,
					Math.min(people.size(), page * pageCount +pageCount));
		}
		return EMPTY;

	}

	private List<People> EMPTY = new ArrayList<People>();

}
