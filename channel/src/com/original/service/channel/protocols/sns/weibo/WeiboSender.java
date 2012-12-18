/**
 * com.original.app.emai.EmailService.java
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel.protocols.sns.weibo;

import java.io.File;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.original.service.channel.ChannelAccount;
import com.original.service.channel.protocols.email.services.MailAuthentication;
import com.original.service.channel.protocols.email.vendor.EMailConfig;
import com.original.service.channel.protocols.email.vendor.EMailServer;
import com.original.util.log.OriLog;

/**
 *
 * @author Admin
 */
public class WeiboSender{// extends AbstractProcessingResource {

    Logger log = OriLog.getLogger(this.getClass());
    final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    final String workdir = System.getProperty("user.dir") + "/temp/";
    private MimeMessage mimeMsg;
    private MimeMultipart mp;
    private String messageid = "";
    private MailAuthentication mailAccount = null;
    private String userId = "";
    private List<String> tempfiles = new ArrayList<String>();
    public EMailConfig config = EMailConfig.getEMailConfig();
    
    WeiboSender(String uid, ChannelAccount ca)
    {
    	this.userId = uid;
    	this.mailAccount = new MailAuthentication("", ca.getAccount().getUser(), ca.getAccount().getPassword(), false);        
    }

    public WeiboSender(String _userId) {
        userId = _userId;
        mailAccount = new MailAuthentication("Song XueYong", "franzsoong@gmail.com", "syzb1234", false);
//        config = new EMailConfig(mailAccount);
//        config.loadConfig();
        
    }
    
    public void start()
    {
    	
    }



    /**
     *
     * @return
     */
    public MailAuthentication getMailAccout() {
        return this.mailAccount;
    }

    /**
     * set properties
     */
    private Properties getProperties(EMailServer server) {
        if (server == null) {
            return null;
        }

        Properties props = new Properties();
        if (server.isIssmtpauth()) {
            props.put("mail.smtp.auth", "true");
        } else {
            props.put("mail.smtp.auth", "false");
        }
        props.put("mail.smtp.host", server.getSmtpserver());
        props.put("mail.Transport.protocol", "smtp");
        props.put("mail.smtp.port", "" + server.getSmtpport());

        if ("ssl".equalsIgnoreCase(server.getSecurityprotocol().value())) {
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
            props.put("mail.smtp.socketFactory.port", "" + server.getSmtpport());
            props.put("mail.smtp.socketFactory.fallback", "false");
        } else if ("tls".equalsIgnoreCase(server.getSecurityprotocol().value())) {
            props.put("mail.smtp.starttls.enable", "true");
        }
        return props;
    }

    private String initMessage() {
        mimeMsg = new MimeMessage((Session) null);
        mp = new MimeMultipart("related");
        try {
            mimeMsg.saveChanges();
            messageid = mimeMsg.getMessageID().substring(1, mimeMsg.getMessageID().indexOf(".JavaMail"));
            mp.setSubType("related;type=\"multipart/alternative\"");
            mimeMsg.addHeader("X-Mailer", "DC Mail Sender 2.0");
        } catch (MessagingException ex) {
            log.error(OriLog.logStack(ex));
            return "initMessage:" + ex.getMessage();
        }
        return null;
    }

    private String setSubject(String mailSubject) {
        try {
            mimeMsg.setSubject(mailSubject);
        } catch (Exception e) {
            log.error(OriLog.logStack(e));
            return "Subject:" + e.getMessage();
        }
        return null;
    }

    /**
     * 改变文件内容中嵌入图片的路径
     */
    private String changeDir(List fileList, String content) {
        Document doc = Jsoup.parse(content);
        Elements media = doc.select("img");
        if (media == null || media.isEmpty()) {
            return content;
        }
        for (int i = 0; i < media.size(); i++) {
            Element a = (Element) media.get(i);
            String filename = a.attr("src");//获取本地图片路径
            fileList.add(filename);
            a.removeAttr("src");
            a.attr("src", "cid:Part" + i + "." + messageid);
        }
        return doc.html();
    }

    /**
     * 设置邮件体
     */
    private String setBody(String mailBody) {
        try {
            List fileList = new ArrayList();
            String body = changeDir(fileList, mailBody);
            MimeMultipart mp1 = new MimeMultipart("alternative");
            MimeBodyPart part2 = new MimeBodyPart();
            part2.setContent(body, "text/html;charset=gb2312");
            part2.setHeader("Content-Transfer-Encoding", "base64");
            part2.setDisposition(MimeBodyPart.INLINE);
            mp1.addBodyPart(part2);
            MimeBodyPart text = new MimeBodyPart();
            text.setContent(mp1);
            mp.addBodyPart(text);
            if (fileList.size() > 0) {
                for (int i = 0; i < fileList.size(); i++) {
                    MimeBodyPart part3 = new MimeBodyPart();
                    DataSource source = new FileDataSource(new File(fileList.get(i).toString()));
                    part3.setDataHandler(new DataHandler(source));
                    part3.setFileName(MimeUtility.encodeText(source.getName(), "UTF-8", "B"));
                    part3.setContentID("<" + "Part" + i + "." + messageid + ">");
                    part3.setDisposition(MimeBodyPart.ATTACHMENT);
                    mp.addBodyPart(part3);
                }
            }
        } catch (Exception e) {
            log.error(OriLog.logStack(e));
            return "Body:" + e.getMessage();
        }
        return null;
    }

}
