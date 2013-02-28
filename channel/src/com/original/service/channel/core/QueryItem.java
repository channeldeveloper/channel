package com.original.service.channel.core;

import java.io.Serializable;

/**
 * 自定义查询项
 * @author WMS
 *
 */
public class QueryItem implements Serializable
{
	private String[] keys; //flags标识对应的key和value
	private Integer[] values;
	
	private Filter[] filters; //自定义查询条件，关系是"equals"
	
	private String text;//查询内容，目前设定查询内容是从ChannelMessage中 的subject\body\fromAddr\toAddr里面查找text
	//关系是"or"
	
	private Class queryClass = null;
	
	public QueryItem() {
		
	}

	public QueryItem(String[] keys, Integer[] values) {
		this(keys, values, null);
	}
	
	public QueryItem(String[] keys, Integer[] values, Filter[] filters) {
		this(keys, values, filters, null);
	}

	public QueryItem(String[] keys, Integer[] values, Filter[] filters, String text) {
		this.keys = keys;
		this.values = values;
		this.filters = filters;
		this.text = text;
	}

	public String[] getKeys() {
		return keys;
	}
	public void setKeys(String... keys) {
		this.keys = keys;
	}

	public Integer[] getValues() {
		return values;
	}
	public void setValues(Integer... values) {
		this.values = values;
	}

	public Filter[] getFilters() {
		return filters;
	}
	public void setFilters(Filter... filters) {
		this.filters = filters;
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public <T> Class<T> getQuertClass() {
		return queryClass;
	}
	public <T> void setQueryClass(Class<T> queryClass) {
		this.queryClass = queryClass;
	}
}
