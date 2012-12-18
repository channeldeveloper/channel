/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel.core;

/**
 *过滤器。
* @author cydow
* @encoding UTF-8
* @version 1.0
* @create 2012-11-11 20:17:13
*/
public interface Filter {
	
	
	public String getField();

	public String getValue();

	public String getOrderField();

}