/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.people;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.query.Query;
import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.original.service.channel.Account;

/**
 * 
 * @author sxy
 * 
 */
@Entity(value = "people", noClassnameStored = true)
public class People {

	@Id
	private ObjectId id;
	// 代表联系人系统用名(不同渠道可能使用不同的名称，这里选择1个或者自定义一个）
	private String name;	
	// 代表联系人头像(不同渠道可能使用不同的名称，这里选择1个或者自定义一个）
	private ObjectId avatar;
	//对应的多个渠道
	@Embedded
	private List<Account> accounts;
	

	
	/**
	 * @return the id
	 */
	public ObjectId getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(ObjectId id) {
		this.id = id;
	}



	// pending
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	

	/**
	 * @return the avatar
	 */
	public ObjectId getAvatar() {
		return avatar;
	}

	/**
	 * @param avatar
	 *            the avatar to set
	 */
	public void setAvatar(ObjectId avatar) {
		this.avatar = avatar;
	}


	
	/**
	 * @return the accounts
	 */
	public List<Account> getAccounts() {
		return accounts;
	}

	/**
	 * @param accounts the accounts to set
	 */
	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}
	
	/**
	 * @param accounts the accounts to set
	 */
	public void addAccount(Account newAccount) {
		if (accounts == null)
		{
			accounts = new ArrayList<Account>();
		}
		if (!accounts.contains(newAccount))
		{
			accounts.add(newAccount);
		}
	}

	public String toString() {
		try {
			Gson gson = new Gson();

			// convert java object to JSON format,
			// and returned as JSON formatted string
			String json = gson.toJson(this);
			return json;
		} catch (Exception exp) {
			return "channel";
		}

	}
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// Mapping
		Morphia morphia = new Morphia();

		morphia.map(People.class);
		// DB
		Mongo mongo = new Mongo("localhost", 27017);
		DB db = mongo.getDB("song");

		// db mapping to object
		Datastore ds = morphia.createDatastore(mongo, "song");
		ds.ensureIndexes();

		// by mongo db
		DBCursor cursor = db.getCollection("people").find();
		while (cursor.hasNext()) {
			System.out.println(cursor.next());
		}
		
		//set data
		People song = new People();
		
		song.setName("Song XueYong");
		song.setAvatar(null);
		
		List<Account> acs = new ArrayList<Account>();
		Account acc = new Account();
		acs.add(acc);
		acc.setChannelName("gmail");
		acc.setUser("franzsoong@gmail.com");
		acc.setPassword("sxyb1234");
		song.setAccounts(acs);
		
		ds.save(song);
		
		 song = new People();
		song.setName("宋砚之");
		song.setAvatar(null);
		acs  = new ArrayList<Account>();
		acc = new Account();
		acc.setChannelName("mailqq");
		acc.setUser("franzsong@qq.com");
		acc.setPassword("syzb%1234%");
		acs.add(acc);
		acc = new Account();
		acc.setChannelName("mail163");
		acc.setUser("franzsong@163.com");
		acc.setPassword("syzb!1234!");
		acs.add(acc);
		song.setAccounts(acs);
		
		ds.save(song);
		
		//get
		long cc = ds.getCount(People.class);
		System.out.println("cc:" + cc);

		// query and list

		Query<People> chs = ds.find(People.class);
		List<People> chslist = chs.asList();

		Iterator<People> ite = chs.iterator();
		while (ite.hasNext()) {
			System.out.println(ite.next());
		}

		for (int i = 0; i < chslist.size(); i++) {
			System.out.println(chslist.get(i));
		}
	}
	
	
	





}
