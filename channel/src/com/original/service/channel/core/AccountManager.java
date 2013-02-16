/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.types.ObjectId;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.query.Query;
import com.mongodb.Mongo;
import com.original.service.channel.Account;
import com.original.service.channel.Channel;
import com.original.service.channel.ChannelAccount;
import com.original.service.channel.Constants;
import com.original.service.profile.Profile;

/**
 * 渠道事例管理器。
 * 
 * 
 * @author cydow
 * @encoding UTF-8
 * @version 1.0
 * @create 2012-11-11 20:17:13
 */
public class AccountManager {

	private java.util.logging.Logger logger;
	/**
	 * @return the logger
	 */
	public java.util.logging.Logger getLogger() {
		return logger;
	}

	/**
	 * @param logger the logger to set
	 */
	public void setLogger(java.util.logging.Logger logger) {
		this.logger = logger;
	}

	/**
	 * @return the chAccountMap
	 */
	public HashMap<String, ChannelAccount> getChAccountMap() {
		return chAccountMap;
	}

	/**
	 * @param chAccountMap the chAccountMap to set
	 */
	public void setChAccountMap(HashMap<String, ChannelAccount> chAccountMap) {
		this.chAccountMap = chAccountMap;
	}

	/**
	 * @return the mongo
	 */
	public Mongo getMongo() {
		return mongo;
	}

	/**
	 * @param mongo the mongo to set
	 */
	public void setMongo(Mongo mongo) {
		this.mongo = mongo;
	}

	/**
	 * @return the morphia
	 */
	public Morphia getMorphia() {
		return morphia;
	}

	/**
	 * @param morphia the morphia to set
	 */
	public void setMorphia(Morphia morphia) {
		this.morphia = morphia;
	}

	/**
	 * @return the ds
	 */
	public Datastore getDs() {
		return ds;
	}

	/**
	 * @param ds the ds to set
	 */
	public void setDs(Datastore ds) {
		this.ds = ds;
	}

	/**
	 * @return the chMg
	 */
	public ChannelManager getChMg() {
		return chMg;
	}

	/**
	 * @param chMg the chMg to set
	 */
	public void setChMg(ChannelManager chMg) {
		this.chMg = chMg;
	}

	/**
	 * @return the channeldb
	 */
	public static String getChanneldb() {
		return Constants.Channel_DB_Name;
	}

	/**
	 * @return the channelcollectiion
	 */
	public static String getChannelcollectiion() {
		return Constants.Channel_Collection_Channel;
	}

	private HashMap<String, ChannelAccount> chAccountMap = new HashMap<String, ChannelAccount>();
	private List<Account> accountList = new ArrayList<Account>();
	private Mongo mongo;
	private Morphia morphia;
	private Datastore ds;
	private ChannelManager chMg;


	/**
	 * Manager Channel Instance.
	 * 
	 * @param mongo
	 * @param morphia
	 * @param ds
	 */
	protected AccountManager(Mongo mongo, Morphia morphia, Datastore ds, ChannelManager chMg) {
		this.mongo = mongo;
		this.morphia = morphia;
		this.ds = ds;
		this.chMg = chMg;
		init();
	}

	/**
	 * 
	 */
	protected void init() {
		logger = Logger.getLogger(Constants.Channel_DB_Name);
		// load profile (system multi-account)
//		List<Account> allAccount = new ArrayList<Account>();
		HashMap<ObjectId, Profile> profileMap = new HashMap<ObjectId, Profile>();
		Query<Profile> profiles = ds.find(Profile.class);
		List<Profile> profileList = profiles.asList();
		for (Profile profile : profileList) {
			logger.log(Level.INFO, " Load Profile form DB " + " " + profile);
			profileMap.put(profile.getId(), profile);
			if (profile.getAccounts() != null) {
				for (int i = 0; i < profile.getAccounts().length; i++) {
					accountList.add(profile.getAccounts()[i]);
				}
			}
		}

		// load channelAccount (system multi-account)
		
		Query<ChannelAccount> chAccounts = ds.find(ChannelAccount.class);
		List<ChannelAccount> chAccountList = chAccounts.asList();
		for (ChannelAccount chAccount : chAccountList) {
//			logger.log(Level.INFO, " Load Channel Account form DB "
//					+ " " + chAccount);
			if (chAccount.getAccount() != null) {
				String key = getKey(chAccount);
				System.out.println("key :" + key);
				//key 
				if (key != null)
				{
					chAccountMap.put(key, chAccount);
				}
			}
		}
		// read from profile and recreate new account
		if (accountList.size() > 0) {
			for (Account account : accountList) {
				
				String key = getKey(account);
				if (!chAccountMap.containsKey(key)) {
					Channel ch = chMg.getChannel(account);
					// support
					if (ch != null) {
						ChannelAccount chAccount = new ChannelAccount();
						chAccount.setAccount(account);
						chAccount.setChannel(ch);
						chAccount.setPresence("avaible");
						chAccount.setStatus("opening");
						ds.save(chAccount);
						logger.log(
								Level.INFO,
								" Add channel Account "
										+ account.getChannelName() + " "
										+ account.getUser());
					} else {
						logger.log(
								Level.INFO,
								" Not support channel "
										+ account.getChannelName() + " "
										+ account.getUser());
					}
				}
			}
		}
	}
	
	
	/**
	 * 
	 * @param channelType
	 * @param accountUser
	 */
   public ChannelAccount getChannelAccount(String channelName, String accountUser)
   {
	   if (channelName == null || accountUser == null)
	   {
		   return null;
	   }
	   String key = accountUser + DELIMITER + channelName;
	   return this.chAccountMap.get(key);

   }
   
	/**
	 * 
	 * @param channelType
	 * @param accountUser
	 */
	public int getChannelAccountCount() {

		return this.chAccountMap.size();

	}
  
  /**
	 * 
	 * @param channelType
	 * @param accountUser
	 */
public Collection<ChannelAccount> getChannelAccounts()
{
	return this.chAccountMap.values();

}

public List<Account> getAccounts() {
	return this.accountList;
}
   

/**
 * 
 * @param cha
 * @return
 */
	private String getKey(Account cha) {
		if (cha == null || cha.getChannelName() == null
				|| cha.getUser() == null) {
			return null;
		}
		return cha.getUser() + DELIMITER
				+ cha.getChannelName();
	}
/**
 * 
 * @param cha
 * @return
 */
	private String getKey(ChannelAccount cha) {
		if (cha == null || cha.getAccount() == null
				|| cha.getAccount().getUser() == null
				|| cha.getAccount().getChannelName() == null) {
			return null;
		}
		return cha.getAccount().getUser() + DELIMITER
				+ cha.getAccount().getChannelName();
	}
	
	private static final String DELIMITER  = ":";


}
