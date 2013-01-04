package com.original.service.people;

import java.util.List;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.query.Query;
import com.mongodb.Mongo;
import com.original.service.channel.Account;
import com.original.service.channel.Channel;
import com.original.service.channel.ChannelMessage;
import com.original.service.channel.Constants;

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
//		init();
	}
//
//	HashMap<String, People> contacts;
//
//	/**
//	 * @return the contacts
//	 */
//	public HashMap<String, People> getContacts() {
//		return contacts;
//	}
//
//	/**
//	 * @param contacts
//	 *            the contacts to set
//	 */
//	public void setContacts(HashMap<String, People> contacts) {
//		this.contacts = contacts;
//	}
//
//	/**
//	 * @return the people
//	 */
//	public People getPeople(String name) {
//		return this.contacts.get(name);
//	}

//	/**
//	 * @param name
//	 *            the contacts to set people name
//	 * @param people
//	 *            the contacts to set people
//	 */
//	public void setPeople(String name, People people) {
//		this.contacts.put(name, people);
//	}
	
	
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
	
	/**
	 * 
	 * @param ps
	 */
	public void save(People[] ps)
	{		
		//save Message
		if (ps != null && ps.length > 0)
		{
			for (People p : ps)
			{
				try
				{
					ds.save(p);
				}
				catch(Exception exp)
				{
					exp.printStackTrace();
				}
			}			
		}
	}

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
	 * 
	 * @param chMsg
	 */
	public void savePeople(ChannelMessage chMsg) {
		if (chMsg == null) {
			return;
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
				List<People> people = getPeople(name);
				for (People qp : people) {
					List<Account> accs = qp.getAccounts();
					if (accs != null) {
						for (Account ac : accs) {
							// 已经加入了
							if (ac.getChannelName() != null
									&& ac.getChannelName().equals(ch.getName())
									&& ac.getUser().equalsIgnoreCase(addr)) {
								return;
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
				p.addAccount(ac);
				ds.save(p);
			}
		}
	}
	/**
	 * 
	 * @param chMsgs
	 */
	public void savePeople(ChannelMessage[] chMsgs)
	{		
		//save Message
		if (chMsgs != null && chMsgs.length > 0)
		{
			for (ChannelMessage chmsg : chMsgs)
			{
				savePeople(chmsg);
			}
		}
	}


}
