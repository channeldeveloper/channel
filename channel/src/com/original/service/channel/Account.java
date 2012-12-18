/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel;

import com.google.code.morphia.annotations.Embedded;
import com.google.gson.Gson;

/**
 * 
 * @author sxy
 *
 */
@Embedded
public class Account {
	
	private String user;
	//MD5 存在信息库需要加密
	private String password;
	//This is needed for we can literally parse user account such as 189787878, maybe im_qq, maybe phone mumber.
	private String channelName;//Unique
	/**
	 * @return the ChannelName
	 */
	public String getChannelName() {
		return channelName;
	}
	/**
	 * @param ChannelName the ChannelName to set
	 */
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
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
	
	public String toKey() {
		StringBuffer keys = new StringBuffer(channelName);
		keys.append("_").append(user);
		return keys.toString();
	}
	
	@Override
	public int hashCode()
	{
		return toKey().hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == this)
			return true;
		
		return obj instanceof Account && 
				((Account)obj).toKey().equals(toKey());
	}
	
}
