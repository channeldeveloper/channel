/*
 *  com.original.app.email.db.service.EMailChannelManager.java
 *  
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.service.channel.protocols.email.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Element;

//import com.original.storage.core.object.QueryObject;
//import com.original.storage.service.DataManager;
//import com.original.storage.service.DataQuery;
//import com.original.storage.util.Constants;
//import com.original.storage.util.Utils;
import com.original.util.log.OriLog;

/**
 * (Class Annotation.)
 * 
 * @author Admin
 * @encoding UTF-8
 * @version 1.0
 * @create 2012-3-18 14:48:21
 */

public class EMailChannelManager {
	Logger log = OriLog.getLogger(this.getClass());

	static String EMAILACCOUNT = "EMailChannel";
	static String EMAILACCOUNTINDEX = "EMailChannelIDX";
	String userId = "";

	/**
	 * 构造函数
	 */
	public EMailChannelManager(String _userId) {

		userId = _userId;
	}

//	/**
//	 * 创建或更新邮箱账号
//	 * 
//	 * @param data
//	 * @return
//	 */
//	public String createEMailChannel(EMailChannel data) {
//		if (data == null) {
//			return null;
//		}
//		Element root = Utils
//				.createElement(Constants.DATABASE, Constants.TEMPDB);
//		if (userId != null || userId.length() > 0)
//			root.addAttribute("UId", userId);
//		Element one = data.toElement();
//		if (one != null) {
//			root.add(one);
//		}
//		log.debug("Insert = " + root.asXML());
//		return new DataManager().insertOrupdate(root.asXML());
//	}
//
//	/**
//	 * 修改邮箱账号
//	 * 
//	 * @param data
//	 * @param filter
//	 * @return
//	 */
//	public String updateEMailChannel(EMailChannel data, QueryObject filter) {
//		if (data == null) {
//			return null;
//		}
//		Element root = Utils
//				.createElement(Constants.DATABASE, Constants.TEMPDB);
//		if (userId != null || userId.length() > 0)
//			root.addAttribute("UId", userId);
//		Element update = Utils.createElement(Constants.UPDATE);
//		if (filter != null) {
//			Element filters = Utils.createElement(Constants.FILTER);
//			Element query = filter.toElement();
//			if (query != null) {
//				filters.add(query);
//				update.add(filters);
//			}
//		}
//		Element dataele = data.toElement();
//		if (dataele == null) {
//			return null;
//		}
//		update.add(dataele);
//		root.add(update);
//		log.debug("update = " + root.asXML());
//		return new DataManager().update(root.asXML());
//	}
//
//	/**
//	 * 删除邮箱账号
//	 * 
//	 * @param filter
//	 * @return
//	 */
//	public String deleteEMailChannel(QueryObject filter) {
//		Element root = Utils
//				.createElement(Constants.DATABASE, Constants.TEMPDB);
//		if (userId != null || userId.length() > 0)
//			root.addAttribute("UId", userId);
//		Element collection = Utils.createElement(Constants.COLLECTION,
//				EMAILACCOUNT);
//		if (filter != null) {
//			Element filters = Utils.createElement(Constants.FILTER);
//			Element query = filter.toElement();
//			if (query != null) {
//				filters.add(query);
//				collection.add(filters);
//			}
//		}
//		root.add(collection);
//		log.debug("delete = " + root.asXML());
//		return new DataManager().delete(root.asXML());
//	}
//
//	/**
//	 * 查询所有的邮箱账号信息
//	 * 
//	 * @return
//	 */
//	public List<EMailChannel> queryEMailChannel() {
//		Element root = Utils
//				.createElement(Constants.DATABASE, Constants.TEMPDB);
//		if (userId != null || userId.length() > 0)
//			root.addAttribute("UId", userId);
//		Element collection = Utils.createElement(Constants.COLLECTION,
//				EMAILACCOUNT);
//		root.add(collection);
//		// 执行数据库查询
//		log.debug("query=" + root.asXML());
//		String returnxml = new DataQuery().query(root.asXML());
//		// 解析查询结果
//		root = Utils.getRoot(returnxml);
//		String returnmsg = root.attributeValue(Constants.RETURNMSG);
//		if (returnmsg != null) {
//			log.error("Return Msg = " + returnmsg);
//			return new ArrayList<EMailChannel>();
//		}
//		List collections = root.elements();
//		List<EMailChannel> retlist = new ArrayList<EMailChannel>();
//		for (int i = 0; collections != null && i < collections.size(); i++) {
//			Element ele = (Element) collections.get(i);
//			EMailChannel email = new EMailChannel();
//			email.fromXML(ele);
//			retlist.add(email);
//		}
//		return retlist;
//	}

}
