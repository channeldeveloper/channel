/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.people;

import java.util.HashMap;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.gson.Gson;
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
	//缺省的设置，是一个组合搭配，不是固定的一个
	
	//对应的多个渠道(channel_name , account)
	@Embedded
	private HashMap<String, Account> accountMap;


	
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
	 * @return the accountMap
	 */
	public HashMap<String, Account> getAccountMap() {
		return accountMap;
	}

	/**
	 * @param accountMap the accountMap to set
	 */
	public void setAccountMap(HashMap<String, Account> accountMap) {
		this.accountMap = accountMap;
	}

	public String toString() {
		try {
			Gson gson = new Gson();
			String json = gson.toJson(this);
			return json;
		} catch (Exception exp) {
			return "channel";
		}

	}
	


}
