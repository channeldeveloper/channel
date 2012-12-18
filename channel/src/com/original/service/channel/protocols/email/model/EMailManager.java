/**
 * com.original.app.email.db.servic.EMailManager.java
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel.protocols.email.model;

import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Element;

//import com.original.storage.core.object.QueryObject;
//import com.original.storage.service.DataManager;
//import com.original.storage.util.Constants;
//import com.original.storage.util.Utils;
import com.original.util.log.OriLog;

/**
 * 邮件管理对象。
 *
 * @author   cydow
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-2-13 8:17:13
 */
public class EMailManager {

    Logger log = OriLog.getLogger(this.getClass());
    public static String EMAIL = "EMail";
    static String EMAILIDX = "EMailIdx";
    String userId = "";

    /**
     * 构造函数
     */
    public EMailManager(String _userId) {       
        userId = _userId;
    }

//    /**
//     * 创建邮件数据
//     * @param datas
//     * @return
//     */
//    public String createEMail(List<EMail> datas) {
//        if (datas == null || datas.isEmpty()) {
//            return null;
//        }
//        Element root = Utils.createElement(Constants.DATABASE, Constants.TEMPDB);
//        if (userId != null || userId.length() > 0) {
//            root.addAttribute("UId", userId);
//        }
//        for (int i = 0; i < datas.size(); i++) {
//            EMail mail = datas.get(i);
//            Element one = mail.toElement();
//            if (one != null) {
//                root.add(one);
//            }
//        }
//        log.debug("Insert = " + root.asXML());
//        return new DataManager().insertOrupdate(root.asXML());
//    }
//
//    /**
//     * 按照条件修改邮件数据
//     * @param data
//     * @param filter
//     * @return
//     */
//    public String updateEMail(EMail data, QueryObject filter) {
//        if (data == null) {
//            return null;
//        }
//        Element root = Utils.createElement(Constants.DATABASE, Constants.TEMPDB);
//        if (userId != null || userId.length() > 0) {
//            root.addAttribute("UId", userId);
//        }
//        Element update = Utils.createElement(Constants.UPDATE);
//        if (filter != null) {
//            Element filters = Utils.createElement(Constants.FILTER);
//            Element query = filter.toElement();
//            if (query != null) {
//                filters.add(query);
//                update.add(filters);
//            }
//        }
//        Element dataele = data.toElement();
//        if (dataele == null) {
//            return null;
//        }
//        update.add(dataele);
//        root.add(update);
//        log.debug("update = " + root.asXML());
//        return new DataManager().update(root.asXML());
//    }
//
//    /**
//     * 按照条件删除邮件数据
//     * @param filter
//     * @return
//     */
//    public String deleteEMail(QueryObject filter) {
//        Element root = Utils.createElement(Constants.DATABASE, Constants.TEMPDB);
//        if (userId != null || userId.length() > 0) {
//            root.addAttribute("UId", userId);
//        }
//        Element collection = Utils.createElement(Constants.COLLECTION, EMAIL);
//        if (filter != null) {
//            Element filters = Utils.createElement(Constants.FILTER);
//            Element query = filter.toElement();
//            if (query != null) {
//                filters.add(query);
//                collection.add(filters);
//            }
//        }
//        root.add(collection);
//        log.debug("delete = " + root.asXML());
//        return new DataManager().delete(root.asXML());
//    }


}
