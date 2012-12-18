/**
 * com.original.app.emai.MailAuthentication.java
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel.protocols.email.services;

import javax.mail.PasswordAuthentication;

/**
 *
 * @author Admin
 */
public class MailAuthentication extends javax.mail.Authenticator {

    private String userId;
    private String strUser;
    private String strPwd;
    private boolean delMail;

    /**
     *
     * @param user
     * @param password
     * @param _delmail 
     */
    public MailAuthentication(String userId, String user, String password, boolean _delmail) {
        this.userId = userId.trim();
        this.strUser = user.trim();
        this.strPwd = password.trim();
        delMail = _delmail;
    }

    /**
     *
     * @return
     */
    public boolean getDeleteMail() {
        return delMail;
    }

    /**
     *
     * @return
     */
    public String getUserName() {
        return strUser;
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return strPwd;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String _userId) {
        this.userId = _userId;
    }

    /**
     *
     * @return
     */
    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(strUser, strPwd);
    }

    @Override
    public String toString() {
        return this.strUser;
    }
}
