/**
 * com.original.app.emai.ParseMail.java
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel.protocols.email.services;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.Message;
import javax.mail.internet.MimeUtility;

import com.original.service.channel.protocols.email.oldimpl.Utils;
import com.original.util.log.OriLog;

/**
 * (Class Annotation.)
 *
 * @author   Administrator
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-4-11 21:52:28
 */
public class MailParseUtil {

    /**
     *
     * @param name
     * @return
     */
    public static String parseBoxName(String name) {
        if (name.equals("inbox")) {
            return "收件箱";
        } else if (name.equals("outbox")) {
            return "发件箱";
        } else if (name.equals("depbox")) {
            return "已删除";
        } else if (name.equals("draftbox")) {
            return "草稿箱";
        } else if(name.equals("sendedbox")){
            return "已发送";
        }else {
            return "垃圾箱";
        }
    }

    /**
     *
     * @param date
     * @return
     */
    public static String parseDate(Date date) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy年MM月dd日 HH:mm (EEEE)");
        String stime = formater.format(date);
        return stime;
    }

    /**
     *
     * @param content
     * @return
     */
    public static String parseContent(String content) {
        if (content.contains("<head>")) {
            int idx = content.indexOf("<head>");
            int idx1 = content.indexOf("</head>");
            content = content.replace(content.substring(idx, idx1 + 7), "");
            return content;
        } else if (content.contains("<HEAD>")) {
            int idx = content.indexOf("<HEAD>");
            int idx1 = content.indexOf("</HEAD>");
            content = content.replace(content.substring(idx, idx1 + 7), "");
        }
        return content;
    }

    /**
     *
     * @param encoding
     * @param addresser
     * @return
     */
    public static String parseAddresser(String encoding, String addresser) {
        try {
            if ("8bit".equalsIgnoreCase(encoding)) {
                return new String(addresser.getBytes("iso-8859-1"), "utf8");
            }
            if (addresser.indexOf("=?x-unknown?") >= 0) {
                addresser = addresser.replace("x-unknown", "utf8");
            }
        } catch (Exception ex) {
            OriLog.getLogger(MailParseUtil.class).error(OriLog.logStack(ex));
        }
        return addresser;
    }

    /**
     *
     * @param encoding
     * @param subject
     * @return
     */
    public static String parseMailSubject(String encoding, String subject) {
        try {
            if ("8bit".equalsIgnoreCase(encoding)) {
                return new String(subject.getBytes("iso-8859-1"), "gb2312");
            }
            if (subject.indexOf("=?x-unknown?") >= 0) {
                subject = subject.replace("x-unknown", "gbk");
            }
            return MimeUtility.decodeText(subject);
        } catch (Exception ex) {
             OriLog.getLogger(MailParseUtil.class).error(OriLog.logStack(ex));
        }
        return subject;
    }

    /**
     *
     * @param name
     * @return
     */
    public static boolean parseMailName(String name) {
        if (name != null && name.matches("^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$")) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param filename
     * @return
     */
    public static String getMessageFileName(String filename) {
        try {
            return MimeUtility.decodeText(filename);
        } catch (Exception ex) {
             OriLog.getLogger(MailParseUtil.class).error(OriLog.logStack(ex));
        }
        return filename;
    }

    /**
     *
     * @param fileType
     * @return
     */
    public static String parseFileType(String fileType) {
		if (fileType.equalsIgnoreCase("image/jpeg")) {
			return "jpg";
		} else if (fileType.equalsIgnoreCase("image/png")) {
			return "png";
		} else if (fileType.equalsIgnoreCase("image/gif")) {
			return "gif";
		} else if (fileType.equalsIgnoreCase("image/bmp")) {
			return "bmp";
		} else if (fileType.equalsIgnoreCase("image/x-icon")) {
			return "icon";
		}
        
        return "txt";
    }

    /**
     *
     * @param msg
     */
    public static void outputMessage(Message msg) {
        try {
            String encoding = msg.getHeader("Content-Transfer-Encoding")[0];
            if ("8bit".equalsIgnoreCase(encoding)) {
                OriLog.getLogger(MailParseUtil.class).debug(new String(msg.getSubject().getBytes("iso-8859-1"), "gb2312"));
            }
        } catch (Exception ex) {
             OriLog.getLogger(MailParseUtil.class).error(OriLog.logStack(ex));
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            msg.writeTo(out);
        } catch (Exception ex) {
             OriLog.getLogger(MailParseUtil.class).error(OriLog.logStack(ex));
        }
        OriLog.getLogger(MailParseUtil.class).debug("EMail Message =================>\n " + new String(out.toByteArray()));
    }
    
    /**
     *
     * @param sendtime
     * @return
     */
    public static String getDateTimeBetween(String sendtime){
        switch (sendtime.length()){
            case 4:
                return sendtime+"-01-01 00:00:00;"+sendtime+"-12-31 23:59:59";
            case 7:
                Date d = Utils.getDate(sendtime+"-01-01");
                d.setMonth(d.getMonth()+1);
                d.setSeconds(d.getSeconds()-1);
                SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return sendtime+"-01 00:00:00;"+formater.format(d);
            case 10:
                return sendtime + " 00:00:00;" + sendtime + " 23:59:59";
            case 13:
                return sendtime + " 00:00;" + sendtime + " 59:59";
            case 16:
                return sendtime + " :00;" + sendtime + " :59";
            default:
                return sendtime+ ";" + sendtime ;
        }
    }
}