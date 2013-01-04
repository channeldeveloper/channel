package com.original.service.channel.protocols.email.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.dom4j.Element;

import com.original.service.channel.protocols.email.model.EMail;
import com.original.service.channel.protocols.email.model.EMailAttachment;
import com.original.service.channel.protocols.email.model.EMailManager;
import com.original.service.channel.protocols.email.oldimpl.BaseObject;
import com.original.service.channel.protocols.email.oldimpl.QueryObject;
import com.original.service.channel.protocols.email.oldimpl.Utils;
import com.original.service.storage.Constants;
import com.original.service.storage.FileExchange;
import com.original.service.storage.StreamData;
import com.original.util.log.OriLog;

/**
 * (Class Annotation.)
 *
 * @author   Administrator
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-2-16 9:48:53
 */
public class EMailSaver {

    EMailManager manager = null;
    Logger log = OriLog.getLogger(this.getClass());
    private String userId = "";

    public EMailSaver(String _userId) {
        userId = _userId;
        manager = new EMailManager(_userId);
    }

    @Deprecated
    public String deleteMail(EMail email) {
//        String ids = email.get_id();
//        QueryObject filter = new QueryObject();
//        filter.setJoin(Constants.AND);
//        if (ids == null) {
//            String mailname = email.getMailname();
//            String type = email.getType();
//            filter.addRule(new RuleObject("mailname", mailname));//账户名
//            filter.addRule(new RuleObject("type", type));//邮箱类型
//        } else {
//            if (ids.indexOf(";") > 0) {
//                filter.addRule(new RuleObject(Constants._ID, ids, Constants.$IN, Constants.STRING, ";"));
//            } else {
//                filter.addRule(new RuleObject(Constants._ID, ids));
//            }
//        }
//        return manager.deleteEMail(filter);
    	return null;
    }
/*
    public String sendEMail(Message msg, String username, String type, EMail mail) throws Exception {
        EMail email = handleMessage(msg, username, type, mail);
        return this.sendEMail(email);
    }

    public String sendEMail(Message msg, String username, String type, String emailid) throws Exception {
        EMail email = handleMessage(msg, username, type, emailid);
        return this.sendEMail(email);
    }*/
    
    /*public String sendEMail(EMail email) {
        Element returnroot = null;//Utils.createElement(Constants.DATABASE);
        try {
            EmailSender service = new EmailSender(userId);
            EMailSaver savemail = new EMailSaver(userId);
            String mailname = email.getMailname();
            String mailtype = email.getType();
            String emailid = email.get_id();
            MailAuthentication auth = service.getMailAccout();//.config.getAccounts().get(0);//EMailConfig..fetchAccountFromPIPS(userId, mailname);
            String isCreated = service.createMessage(email);
            if (isCreated == null) {
                String success = null;
                String type = "draftbox";
                if (email.getType().equalsIgnoreCase("outbox") || "reply".equalsIgnoreCase(mailtype)
                        || "replyall".equalsIgnoreCase(mailtype)) {
                    success = service.send(auth);
                    if ("reply".equalsIgnoreCase(mailtype) || "replyall".equalsIgnoreCase(mailtype)) {
                        if (email.getIsReplay() != 1 && emailid != null) {
                            EMail em = new EMail();
                            em.setIsReplay(1);
                            savemail.updateMail(em, emailid);
                        }
                    }
                    type = (success == null ? "sendedbox" : "outbox");
                }
                // new email;
                email.set_id(null);
                String returnxml = saveEMail(service.getMimeMsg(), mailname, type, email);
                if (("draftbox".equalsIgnoreCase(mailtype) || email.getType().equalsIgnoreCase("outbox"))
                        && emailid != null) {
                    savemail.deleteMail(emailid);
                }
                return returnxml;
            } else {
                service.deleteTempfile();
                returnroot.addAttribute(Constants.RETURNMSG, isCreated);
            }
        } catch (Exception e) {
            log.error(OriLog.logStack(e));
            returnroot.addAttribute(Constants.RETURNMSG, e.getMessage());
        }
        return returnroot.asXML();
    }
*/
    /**
     *
     * @param emailid
     */
    @Deprecated
    public String deleteMail(String emailid) {
//        QueryObject filter = new QueryObject();
//        if (emailid.indexOf(";") > 0) {
//            filter.addRule(new RuleObject(Constants._ID, emailid, Constants.$IN, Constants.STRING, ";"));
//        } else {
//            filter.addRule(new RuleObject(Constants._ID, emailid));
//        }
//        return manager.deleteEMail(filter);
    	return "Deprecated!";
    }

    /**
     *
     * @param emailids
     */
    public String deleteMail(List<String> emailids) {
      /*  String ret = "";
        for (String id : emailids) {
            ret = this.deleteMail(id);
            Element retele = Utils.getRoot(ret);
            if (retele.attributeValue(Constants.RETURNMSG) != null) {
                return ret;
            }
        }
        return ret;*/
    	return null;
    }

    /**
     *
     * @param mail
     * @param emailid
     */
    public String updateMail(EMail mail, String emailid) {
    /*    QueryObject filter = new QueryObject();
        filter.addRule(new RuleObject("_id", emailid));

*/
    	return null;
    }

    /**
     *
     * @param mail
     */
    public String updateMail(EMail mail) {
        
    	/*if (mail.getAttachment() != null) {
            List lists = new ArrayList<EMail>();
            lists.add(mail);
            return manager.createEMail(lists);
        }
        String emailid = mail.get_id();
        QueryObject filter = new QueryObject();
        if (emailid.indexOf(";") > 0) {
            filter.addRule(new RuleObject(Constants._ID, emailid, Constants.$IN, Constants.STRING, ";"));
        } else {
            filter.addRule(new RuleObject(Constants._ID, emailid));
        }
        return manager.updateEMail(mail, filter);
        */
    	return null;
    }

    /**
     *
     * @param mail
     * @param emailids
     */
    @Deprecated
    public String updateMail(EMail mail, List<String> emailids) {
//        String ret = "";
//        for (String id : emailids) {
//            ret = this.updateMail(mail, id);
//            Element retele = Utils.getRoot(ret);
//            if (retele.attributeValue(Constants.RETURNMSG) != null) {
//                return ret;
//            }
//        }
//        return ret;
    	return null;
    }

    @Deprecated
    public String save(EMail email) {
//        List<EMail> emails = new ArrayList<EMail>();
//        emails.add(email);
//        return manager.createEMail(emails);
    	return "Deprecated!";
    }

    /**
     *
     * @param msg
     * @param username
     * @param type
     * @param emailid
     * @return
     * @throws Exception
     */
    public String saveEMail(Message msg, String username, String type, EMail mail) throws Exception {
        EMail email = handleMessage(msg, username, type, mail);
        return this.save(email);
    }

    public String saveEMail(Message msg, String username, String type, String emailid) throws Exception {
        EMail email = handleMessage(msg, username, type, emailid);
        return this.save(email);
    }

    private EMail handleMessage(Message msg, String username, String type, EMail mail) throws Exception {
        String encoding = "";
        try {
            encoding = msg.getHeader("Content-Transfer-Encoding")[0];
        } catch (Exception ex) {
            log.error(OriLog.logStack(ex));
        }
        mail.setIsRead(0);
        mail.setIsProcess(0);
        mail.setIsDelete(0);
        mail.setIsReplay(0);
        mail.setIsTrash(0);
        mail.setType(type);
        mail.setMailname(username);
        mail.setMsgId(((MimeMessage) msg).getMessageID());

        String from = getFrom(msg);
        mail.setAddresser(MailParseUtil.parseAddresser(encoding, from));
        mail.setReceiver(getMailAddress(msg, "TO"));
        mail.setCc(getMailAddress(msg, "CC"));
        mail.setBcc(getMailAddress(msg, "BCC"));
        mail.setReplayTo(MailParseUtil.parseAddresser(encoding, from));
        mail.setMailtitle(MailParseUtil.parseMailSubject(encoding, msg.getSubject()));
        if (msg.getSentDate() != null) {
            mail.setSendtime(msg.getSentDate());
        } else {
            mail.setSendtime(new Date());
        }
        String content = getContent(msg);
        mail.setContent("<![CDDATA[" + MailParseUtil.parseContent(content) + "]]");

        return mail;
    }

    private EMail handleMessage(Message msg, String username, String type, String emailid) throws Exception {
        EMail mail = new EMail();
        mail.set_id(emailid);
/*        List<EMailAttachment> emailAttachments = getAttach(msg);
        if (emailAttachments.size() > 0) {
            BaseObject attachment = new BaseObject();
            attachment.set_Name("Attachment");
            for (EMailAttachment attach : emailAttachments) {
                attachment.addList(attach);
            }
            mail.setAttachment(attachment);
        }
*/      
        return this.handleMessage(msg, username, type, mail);
    }

    /**
     * 获得发件人
     */
    private String getFrom(Message msg) throws Exception {
        if (msg.getFrom()[0] != null) {
            String personal = ((InternetAddress) msg.getFrom()[0]).getPersonal();
            String address = ((InternetAddress) msg.getFrom()[0]).getAddress();
            if (personal == null) {
                if (address.contains("@")) {
                    personal = address.substring(0, address.indexOf("@"));
                } else {
                    personal = address;
                }
            } else {
                personal = MimeUtility.decodeText(personal);
            }
            return personal + "<" + address + ">";
        } else {
            return "unknowed";
        }
    }

    /**
     * get mail content
     */
    private String getContent(Part part) throws Exception {
        String a = null;
        if (part.isMimeType("text/plain")) {
            String s = (String) part.getContent();
            return s;
        } else if (part.isMimeType("text/html")) {
            String s = (String) part.getContent();
            return s;
        } else if (part.isMimeType("multipart/*")) {
            StringBuilder sb = new StringBuilder();
            if (part.isMimeType("multipart/alternative")) {
                Multipart mp = (Multipart) part.getContent();
                int index = 0;
                if (mp.getCount() > 1) {
                    index = 1;
                }
                Part tmp = mp.getBodyPart(index);
                if (index == 0) {
                    sb.append(getContent(tmp));
                    return sb.toString();
                }
                if (index == 1) {
                    sb.append(tmp.getContent());
                    return sb.toString();
                }
            } else if (part.isMimeType("multipart/related")) {
                Multipart mp = (Multipart) part.getContent();
                Part tmp = mp.getBodyPart(0);
                String body = getContent(tmp);

                return body;
            } else {
                Multipart mp = (Multipart) part.getContent();
                Part tmp = mp.getBodyPart(0);
                a = getContent(tmp);
            }
            return a;
        } else if (part.isMimeType("message/rfc822")) {
            String s = getContent((Part) part.getContent());
            return s;
        } else {
            return "";
        }
    }

    /**
     * get mail attachment
     * @param part
     * @return
     * @throws Exception
     */
    public List<EMailAttachment> getAttach(Part part) throws Exception {
        List<EMailAttachment> emailAttachments = new ArrayList<EMailAttachment>();
        if (part.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) part.getContent();
            int count = mp.getCount();
            for (int k = 1; count > 1 && k < count; k++) {
                Part mpart = mp.getBodyPart(k);
                String disposition = mpart.getDisposition();
                log.debug("disposition = " + disposition);
                if (disposition != null && (disposition.equals(Part.ATTACHMENT))) {
                    EMailAttachment emailAttachment = new EMailAttachment();
                    if (mpart.getHeader("Content-ID") != null) {
                        emailAttachment.setCId(mpart.getHeader("Content-ID")[0].replace("<", "").replace(">", ""));
                    }
                    String fileName = MailParseUtil.getMessageFileName(mpart.getFileName());
                    String extention = null;
                    if (fileName.contains(".") == true) {
                        extention = fileName.substring(fileName.indexOf(".") + 1);
                        emailAttachment.setFileName(fileName);
                    } else {
                        String contentType = mpart.getContentType().substring(0, mpart.getContentType().indexOf(";"));
                        extention = MailParseUtil.parseFileType(contentType);
                        fileName = fileName + "." + extention;
                        emailAttachment.setFileName(fileName);
                    }
                    emailAttachment.setType(extention);
//                    emailAttachment.setData(saveBinaryData(mpart.getInputStream(), extention));
                    emailAttachment.setSize(mpart.getInputStream().available());
                    emailAttachments.add(emailAttachment);
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            emailAttachments = getAttach((Part) part.getContent());
        }
        return emailAttachments;
    }
    
    
    /**
     * 保存指定应用类型的二进制对象到文件交换区
     * @param data
     * @param application
     * @return
     */
    public static byte[] saveBinaryData(Object data, String application) {
        FileExchange wrap = FileExchange.getInstance();
        String filename = (new ObjectId().toStringMongod()) + ((application == null || application.length() == 0) ? "" : "." + application);
        if (data instanceof String) {
            wrap.addContextVariable(filename, new StreamData(null, (String) data));
        } else if (data instanceof InputStream) {
            wrap.addContextVariable(filename, new StreamData((InputStream) data, filename));
        } else if (data instanceof byte[]) {
            wrap.addContextVariable(filename, new StreamData(new ByteArrayInputStream((byte[]) data), filename));
        }
        return filename.getBytes();
    }

    /**
     *
     * @param msg
     * @param type
     * @return
     * @throws Exception
     */
    public String getMailAddress(Message msg, String type) throws Exception {
        String mailaddr = "";
        String addtype = type.toUpperCase();
        InternetAddress[] address = null;
        if (addtype.equals("TO") || addtype.equals("CC") || addtype.equals("BCC")) {
            if (addtype.equals("TO")) {
                address = (InternetAddress[]) msg.getRecipients(Message.RecipientType.TO);
            } else if (addtype.equals("CC")) {
                address = (InternetAddress[]) msg.getRecipients(Message.RecipientType.CC);
            } else {
                address = (InternetAddress[]) msg.getRecipients(Message.RecipientType.BCC);
            }
            if (address != null) {
                for (int i = 0; i < address.length; i++) {
                    String email = address[i].getAddress();
                    if (email == null) {
                        email = "";
                    } else {
                        email = MimeUtility.decodeText(email);
                    }
                    String personal = address[i].getPersonal();
                    if (personal == null) {
                        personal = "";
                    } else {
                        personal = MimeUtility.decodeText(personal);
                    }
                    String compositeto = personal + "<" + email + ">";
                    mailaddr += ";" + compositeto;
                }
                mailaddr = mailaddr.substring(1);
            }
        } else {
            throw new Exception("Error emailaddr type!");
        }
        return mailaddr;
    }
}
