/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Embedded;
import com.google.gson.Gson;

/**
 * 
 * @author sxy
 *
 */
@Embedded
public class Account {
	//用户名称
	private String user;
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the authToken
	 */
	public String getAuthToken() {
		return authToken;
	}
	/**
	 * @param authToken the authToken to set
	 */
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
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
	 * @param avatar the avatar to set
	 */
	public void setAvatar(ObjectId avatar) {
		this.avatar = avatar;
	}

	//账号Id(如QQ的QQ号码）
	private String userId;
	//MD5 存在信息库需要加密
	private String password;
	//认证的Token，有些登录不需要密码，通过auth来完成。
	private String authToken;
	//账号的名称，一般同账号不同，如QQ(franzsong@qq.com; 123456; Song Xueyong)
	private String name;
	//头像（当前的头像）
	private ObjectId avatar;
	//This is needed for we can literally parse user account such as 189787878, maybe im_qq, maybe phone mumber.
	private String channelName;//Unique
	
	
	//private 
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
