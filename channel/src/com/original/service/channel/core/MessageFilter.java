/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel.core;

/**
 * 消息过滤
* @author cydow
* @encoding UTF-8
* @version 1.0
* @create 2012-11-11 20:17:13
*/
public class MessageFilter implements Filter {
	
	private String field;
	private String value;
	private String orderField;
	
	/**
	 * 消息查找的过滤。默认按时间倒序排
	 */
	public MessageFilter(String field, String value) {
		this(field, value, MessageManager.OrderbyDateField);
	}
	
	/**
	 * 消息查找的过滤。
	 */
	public MessageFilter(String field, String value, String orderField)
	{
		this.field = field;
		this.value = value;
		this.orderField = orderField;		
	}
	
	public String getField() {
		return field;
	}

	public String getValue() {
		return value;
	}

	public String getOrderField() {
		return orderField;
	}

	@Override
	public String toString() {
		// TODO 自动生成的方法存根
		return "[field=" + field + ", value=" + value + "]";
	}
	
	
}
