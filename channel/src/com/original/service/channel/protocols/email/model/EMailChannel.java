/*
 *  com.original.app.email.db.service.EMailServer.java
 *  
 *  Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 *  ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.original.service.channel.protocols.email.model;

import com.original.service.channel.protocols.email.oldimpl.BaseObject;

/**
 * (Class Annotation.)
 *
 * @author   Admin
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-3-18 14:35:09
 */

public class EMailChannel extends BaseObject {
    private String mailname = null;
    private String mailtype = null;
    private String smtpserver = null;
    private Integer smtpport = null;
    private String popserver = null;
    private Integer popport = null;
    private String issmtpauth = null;
    private String securityprotocol = null;

    /**
     * 获取邮箱名，如:126.com
     * @return
     */
    public String getMailname() {
        return mailname;
    }

    /**
     * 获取SMTP 服务器
     * @return
     */
    public String getSmtpserver() {
        return smtpserver;
    }

    /**
     * 获取SMTP端口
     * @return
     */
    public Integer getSmtpport() {
        return smtpport;
    }

    /**
     * 获取POP 服务器
     * @return
     */
    public String getPopserver() {
        return popserver;
    }

    /**
     *  获取POP端口
     * @return
     */
    public Integer getPopport() {
        return popport;
    }

    /**
     * 获取收件协议 pop3 ，pop ，IMAP
     * @return
     */
    public String getMailtype() {
        return mailtype;
    }

    /**
     * 获取是否认证 , 
     * @return
     */
    public String getIssmtpauth() {
        return issmtpauth;
    }


    /**
     * 获取安全协议  none,tls,ssl
     * @return
     */
    public String getSecurityprotocol() {
        return securityprotocol;
    }

    /**
     * 设置账号名
     * @param Mailname
     */
    public void setMailname(String Mailname) {
        mailname = Mailname;
    }

    /**
     * 设置SMTP服务器
     * @param SmtpServer
     */
    public void setSmtpserver(String SmtpServer) {
        smtpserver = SmtpServer;
    }

    /**
     * 设置SMTP端口
     * @param SmtpPort
     */
    public void setSmtpport(Integer SmtpPort) {
        smtpport = SmtpPort;
    }

    /**
     * 设置POP服务器
     * @param PopServer
     */
    public void setPopserver(String PopServer) {
        popserver = PopServer;
    }

    /**
     * 设置POP端口
     * @param PopPort
     */
    public void setPopport(Integer PopPort) {
        popport = PopPort;
    }

    /**
     * 设置收件协议
     * @param MailType
     */
    public void setMailtype(String MailType) {
        mailtype = MailType;
    }

    /**
     * 设置是否认证 false , true
     * @param IsSMTPAuth
     */
    public void setIssmtpauth(String IsSMTPAuth) {
        issmtpauth = IsSMTPAuth;
    }

    /**
     * 设置安全协议
     * @param SecurityProtocol
     */
    public void setSecurityprotocol(String SecurityProtocol) {
        securityprotocol = SecurityProtocol;
    }
}
