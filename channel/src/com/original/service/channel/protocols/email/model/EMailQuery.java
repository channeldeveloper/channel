/**
 * com.original.app.email.db.servic.EMailQuery.java
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel.protocols.email.model;

import org.apache.log4j.Logger;

import com.original.util.log.OriLog;

/**
 * 邮件查询对象。
 *
 * @author   cydow
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-2-13 8:17:13
 */
public class EMailQuery {

    Logger log = OriLog.getLogger(this.getClass());
    static String EMAIL = "EMail";
    String userId = "";

    /**
     * 构造函数
     */
    public EMailQuery(String _userId) {
        userId = _userId;
    }

//    /**
//     * 按照条件查询邮件数据
//     * @param results
//     * @param filter
//     * @param fetch
//     * @param sort
//     * @return
//     */
//    public String queryEMail(QueryObject filter, List<String> fetch, HashMap<String, String> sort) {
//        return queryEMail(filter, null, null, fetch, sort);
//    }
//
//    /**
//     * 按照条件查询邮件数据
//     * @param results
//     * @param filter
//     * @param nPage
//     * @param nPerPage
//     * @param fetch
//     * @param sort
//     * @return
//     */
//    public String queryEMail(QueryObject filter, Integer nPage, Integer nPerPage, List<String> fetch, HashMap<String, String> sort) {
//        Element root = Utils.createElement(Constants.DATABASE, Constants.TEMPDB);
//        if ( userId != null || userId.length() > 0 )
//            root.addAttribute("UId", userId);
//        Element collection = Utils.createElement(Constants.COLLECTION, EMAIL);
//        // 转换查询条件
//        if (filter != null) {
//            Element filters = Utils.createElement(Constants.FILTER);
//            Element query = filter.toElement();
//            if (query != null) {
//                filters.add(query);
//                collection.add(filters);
//            }
//        }
//        if ( nPage != null ){
//            collection.add(Utils.createElement(Constants.PAGENUMBER, null, "" + nPage));
//        }
//        if ( nPerPage != null){
//            collection.add(Utils.createElement(Constants.NPERPAGE, null, "" + nPerPage));
//        }
//        // 生成获取的字段信息
//        if (fetch != null) {
//            Element fetchele = Utils.createElement(Constants.FETCH);
//            for (String s : fetch) {
//                Element one = Utils.createElement(Constants.FIELD, s,"1");
//                fetchele.add(one);
//            }
//            collection.add(fetchele);
//        }
//        // 生成排序的字段信息
//        if (sort != null) {
//            Element sortele = Utils.createElement(Constants.SORT);
//            for (Entry<String, String> obj : sort.entrySet()) {
//                Element one = Utils.createElement(Constants.FIELD, obj.getKey(), obj.getValue());
//                sortele.add(one);
//            }
//            collection.add(sortele);
//        }
//        root.add(collection);
//        // 执行数据库查询
//        log.debug("query=" + root.asXML());
//        String returnxml = new DataQuery().query(root.asXML());
//        // 解析查询结果
//        root = Utils.getRoot(returnxml);
//        String returnmsg =  root.attributeValue(Constants.RETURNMSG);
//        if ( returnmsg != null ){
//            log.error("Return Msg = "+returnmsg);
//        }
//        return returnxml;
//    }
//    
//    /**
//     * 按照条件查询邮件数据
//     * @param results
//     * @param filter
//     * @param fetch
//     * @param sort
//     * @return
//     */
//    public int queryEMail(List<EMail> results, QueryObject filter, List<String> fetch, HashMap<String, String> sort) {
//        return queryEMail(results,filter, null, null, fetch, sort);
//    }
//
//    /**
//     * 按照条件查询邮件数据
//     * @param results
//     * @param filter
//     * @param nPage
//     * @param nPerPage
//     * @param fetch
//     * @param sort
//     * @return
//     */
//    public int queryEMail(List<EMail> results,QueryObject filter, Integer nPage, Integer nPerPage, List<String> fetch, HashMap<String, String> sort) {
//       
//        String returnxml = queryEMail(filter, nPage, nPerPage, fetch, sort);
//        // 解析查询结果
//        Element root = Utils.getRoot(returnxml);
//        String returnmsg =  root.attributeValue(Constants.RETURNMSG);
//        if ( returnmsg != null ){
//            log.error("Return Msg = "+returnmsg);
//            return -1;
//        }
//        
//        int total = Integer.parseInt(root.attributeValue(Constants.TOTAL));
//        log.debug("Total Records = "+total+", Return Records = "+root.attributeValue(Constants.SIZE));
//        List collections = root.elements();
//        for (int i = 0; collections != null && i < collections.size(); i++) {
//            Element ele = (Element) collections.get(i);
//            EMail email = new EMail();
//            email.fromXML(ele);
//            results.add(email);
//        }
//        return total;
//    }
//
//
//    /**
//     * 按照附件data字段信息获取实际的附件数据
//     * @param data
//     * @return
//     */
//    public byte[] queryAttachment(byte[] data) {
//        if (data == null) {
//            return null;
//        }
//        StreamData sd = FileManager.fetchBinaryFile(new String(data));
//        if (sd != null) {
//            return sd.getData();
//        }
//        return null;
//    }
}
