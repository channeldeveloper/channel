/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel.data;

import java.util.List;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;
import com.original.service.channel.ChannelMessage;
import com.original.service.people.People;

/**
 * Pending: SongXueyong
 *  有无必要为了View显示而维护一个类似View的数据结构Collection。
 *  咨询一下数据库的设计Lao Yang。 
 *   如果所有的消息都这样展示类似短息，有必要按照人来管理。
 *   而weibo、qq、twiter这些SNS都不是按人来管理的，是否颠覆通用的做法，还是不必要这样定义，同Lao yang
 * 
 * 
 * 
 * @author Song Xueyong
 * @encoding UTF-8
 * @version 1.0
 * @create 2013-2-6 20:17:13 
 */
@Entity(value = "PeopleMessages", noClassnameStored = true)
public class PeopleMessages {	
	
	@Reference
	private People people;
	@Reference
	private List<ChannelMessage> messages;
	/**
	 * @return the people
	 */
	public People getPeople() {
		return people;
	}
	/**
	 * @param people the people to set
	 */
	public void setPeople(People people) {
		this.people = people;
	}
	/**
	 * @return the messages
	 */
	public List<ChannelMessage> getMessages() {
		return messages;
	}
	/**
	 * @param messages the messages to set
	 */
	public void setMessages(List<ChannelMessage> messages) {
		this.messages = messages;
	}
}
