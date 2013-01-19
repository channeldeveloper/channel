package com.original.service.people;

import java.util.Collection;
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
import com.original.service.channel.core.ThreadManager;

public class PeopleManager {
	
	private Mongo mongo;
	private Morphia morphia;
	private Datastore ds;


	/**
	 * PeopleManager
	 * @param mongo
	 * @param morphia
	 * @param ds
	 */
	public PeopleManager(Morphia morphia, Mongo mongo, Datastore ds)
	{
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
	private void loadContracts()
	{
		//1 调用各自的渠道服务ChannelService 获得联系人。
		ChannelService cs  = ChannelService.getInstance();
		List<Account> all = cs.getContacts();
		for (Account ac : all)
		{
			String chName = ac.getChannelName();
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
//	
//	/**
//	 * 
//	 * @param ps
//	 */
//	public void save(People[] ps)
//	{		
//		//save Message
//		if (ps != null && ps.length > 0)
//		{
//			for (People p : ps)
//			{
//				try
//				{
//					ds.save(p);
//				}
//				catch(Exception exp)
//				{
//					exp.printStackTrace();
//				}
//			}			
//		}
//	}

	// find
	/**
	 * get All Messages
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
					return q.field("accountMap." + ca.getChannel().getName() + "." +"name").equal(chm.getFromAddr()).get();
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
		
		return ds.find(People.class).field("accountMap." + chName).equal(channelAccount).get();
	}
	

	/**
	 * 
	 * @param chMsg
	 */
	public People savePeople(ChannelMessage chMsg) {
		if (chMsg == null) {
			return null;
		}

		// Franz Song<franzsong@gmail.com>
		Channel ch = chMsg.getChannelAccount().getChannel();
		if (ch.getType().equals(Constants.Channel_Type_Email)) {
			String fromAddr = chMsg.getFromAddr();
			if (fromAddr != null && fromAddr.lastIndexOf("<") != -1) {
				int dimi = fromAddr.lastIndexOf("<");
				String name = fromAddr.substring(0, dimi);
				String addr = fromAddr.substring(dimi + 1, fromAddr.length() - 1);

				// how to get by one sentence
				People qp = getPeopleByChannel(chMsg.getChannelAccount().getChannel().getName(), addr);//getPeople(name);
				if (qp != null && qp.getAccountMap() != null)
				{
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
				ac.setChannelName(chMsg.getChannelAccount().getChannel()
						.getName());
//				p.addAccount(ac);
//				people.setName(ac.getName());
//				people.setAvatar(ac.getAvatar());
				HashMap <String, Account> acMap = new HashMap<String, Account>();
				acMap.put(ac.getChannelName(), ac);
				p.setAccountMap(acMap);	
				
				ds.save(p);
				return p;
			}
		}
		else
		{
			People qp = getPeopleByChannel(chMsg.getChannelAccount().getChannel().getName(), chMsg.getFromAddr());
			return qp;
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

}
