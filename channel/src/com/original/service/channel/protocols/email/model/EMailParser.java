/**
 * com.original.app.email.db.servic.EMailQuery.java
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel.protocols.email.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.original.service.channel.Utilies;
import com.original.service.channel.protocols.email.services.MailParseUtil;
import com.original.service.storage.GridFSUtil;
import com.original.util.log.OriLog;


/**
 * (Class Annotation.)
 *
 * @author   Administrator
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-2-16 9:48:53
 */
public class EMailParser {

    EMailManager manager = null;
    Logger log = OriLog.getLogger(this.getClass());
    private String userId = "";

    public EMailParser(String _userId) {
        userId = _userId;
        manager = new EMailManager(_userId);
    }
//
//    private EMail handleMessage(Message msg, String username, String type, EMail mail) throws Exception {
//        String encoding = "utf8";
//        try {
////            encoding = msg.getHeader("Content-Transfer-Encoding")[0];
//        } catch (Exception ex) {
//            log.error(OriLog.logStack(ex));
//        }
//        mail.setIsRead(0);
//        mail.setIsProcess(0);
//        mail.setIsDelete(0);
//        mail.setIsReplay(0);
//        mail.setIsTrash(0);
//        mail.setType(type);
//        mail.setMailname(username);
//        mail.setMsgId(((MimeMessage) msg).getMessageID());
//
//        String from = getFrom(msg);
//        mail.setAddresser(MailParseUtil.parseAddresser(encoding, from));
//        mail.setReceiver(getMailAddress(msg, "TO"));
//        mail.setCc(getMailAddress(msg, "CC"));
//        mail.setBcc(getMailAddress(msg, "BCC"));
//        mail.setReplayTo(MailParseUtil.parseAddresser(encoding, from));
//        mail.setMailtitle(MailParseUtil.parseMailSubject(encoding, msg.getSubject()));
//        if (msg.getSentDate() != null) {
//            mail.setSendtime(msg.getSentDate());
//        } else {
//            mail.setSendtime(new Date());
//        }
//        String content = getContent(msg);
//        mail.setContent("<![CDDATA[" + MailParseUtil.parseContent(content) + "]]");
//
//        return mail;
//    }
//
//    private EMail handleMessage(Message msg, String username, String type, String emailid) throws Exception {
//        EMail mail = new EMail();
//        mail.set_id(emailid);
//        List<EMailAttachment> emailAttachments = getAttach(msg);
//        if (emailAttachments.size() > 0) {
//            BaseObject attachment = new BaseObject();
//            attachment.set_Name("Attachment");
//            for (EMailAttachment attach : emailAttachments) {
//                attachment.addList(attach);
//            }
//            mail.setAttachment(attachment);
//        }
//        return this.handleMessage(msg, username, type, mail);
//    }

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
	 * 
	 *附件的格式
	  *Content-Type: application/octet-stream; charset="ISO-8859-1"; name="Innovation.png" 
	 * Content-Disposition: attachment; filename="Innovation.png" 
	 * Content-Transfer-Encoding: base64
	 * iVBORw0KGgoAAAAN AAAAAAAAAAAAAA==
	 * 
	 * 
	 * 内嵌图片
	 * ------=_NextPart_50E82649_D5CD4EC0_1CFFCFF2 、
	 * Content-Type: application/octet-stream; name="E2E042E4@41707E5B.4926E850.png"
	 * Content-Transfer-Encoding: base64 
	 * Content-ID: <E2E042E4@41707E5B.4926E850.png>
	 * 
	 * iVBORw0KGgoAAAANSUhEUgAAAKAAA AAAC==
	 * ------=_NextPart_50E82649_D5CD4EC0_1CFFCFF2
	 * 
	 * get mail attachment
	 * 
	 * @param part
	 * @return
	 * @throws Exception
	 */
    private List<EMailAttachment> getAttach(Part part) throws Exception {
        List<EMailAttachment> list = new ArrayList<EMailAttachment>();
        if (part.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) part.getContent();
            int count = mp.getCount();
            for (int k = 1; count > 1 && k < count; k++) {
				Part mpart = mp.getBodyPart(k);
				String fileName = null;
				EMailAttachment attachment = null;

				String disposition = mpart.getDisposition();
				if ((disposition != null) && (disposition.equals(Part.ATTACHMENT) 
								|| disposition.equals(Part.INLINE))) {
					attachment = new EMailAttachment();
					attachment.setCType(disposition);
					if (mpart.getHeader("Content-ID") != null) {
						attachment.setCId(mpart.getHeader("Content-ID")[0].replace("<", "").replace(">", ""));
					}
					fileName = MailParseUtil.getMessageFileName(mpart.getFileName());
					
				} else { // 处理内嵌图片的附件问题，dispose可能为null
					String contype = mpart.getContentType();
					if (contype.toLowerCase().indexOf("application") != -1
							|| contype.toLowerCase().indexOf("name") != -1) {
						// Properties p = getContenttypeAndCharset("Content-Type", mpart.getContentType());
						String[] cid = mpart.getHeader("Content-ID");
						if (cid != null && cid.length > 0) {
							attachment = new EMailAttachment();
							attachment.setCType(Part.INLINE);

							if (mpart.getHeader("Content-ID") != null) {
								attachment.setCId(mpart.getHeader("Content-ID")[0].replace("<", "").replace(">", ""));
								fileName = attachment.getCId();// MailParseUtil.getMessageFileName(mpart.getFileName());
							}
						}
					}
				}

				if(attachment == null) continue;
				
				String extention = null;
				if (fileName.contains(".")) {
					extention = fileName.substring(fileName.lastIndexOf(".") + 1);
					attachment.setFileName(fileName);
				} else {
					String contentType = mpart.getContentType().substring(0, mpart.getContentType().indexOf(";"));
					extention = MailParseUtil.parseFileType(contentType);
					fileName = fileName + "." + extention;
					attachment.setFileName(fileName);
				}
				attachment.setType(extention);

				// store to db
				Object fileID = GridFSUtil.getGridFSUtil().saveFile(mpart.getInputStream(), fileName);
				attachment.setFileID((ObjectId) fileID);
				attachment.setSize(mpart.getInputStream().available());

				// save to native file
				String tempDir = Utilies.getTempDir(fileID, fileName); // fileID is unique
				File tempFile = new File(new URI(tempDir));
				if(!tempFile.exists() || tempFile.length() != attachment.getSize()) {
					GridFSUtil.getGridFSUtil().writeFile((ObjectId) fileID, tempDir);
				}
				if (attachment.getCId() != null) {
					attachment.setCDir(tempDir);
				}
				list.add(attachment);
			}
        } else if (part.isMimeType("message/rfc822")) {
            list = getAttach((Part) part.getContent());
        }
        return list;
    }
    
    private Properties getContenttypeAndCharset(String hdr, String str) {
		boolean basicValueSet = false;
		Properties props = new Properties();
		int colonIdx = str.indexOf(':');
		for (StringTokenizer st = new StringTokenizer(
				str.substring(colonIdx + 1), ";"); st.hasMoreTokens();) {
			String avp = st.nextToken().trim();
			int equalIdx = avp.indexOf('=');
			if (equalIdx == -1) {
				if (!avp.equals("")) {
					if (!basicValueSet) {
						props.put(hdr, avp);
						basicValueSet = true;
					} else {
						props.put(avp.toLowerCase(), avp.toLowerCase());
					}
				}
			} else {
				String attr = avp.substring(0, equalIdx).trim().toLowerCase();
				String val = avp.substring(equalIdx + 1).trim();
				val = val.replaceFirst("^\"", "").replaceFirst("\"$", "");
				props.put(attr, val);
			}

		}
		return props;
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
    ///////////////////////////////////New Parse///////////////////////////////
//    parse.saveEMail(msgs[i], account.getUserName(), "inbox", (String) null); old methods
    /**
     * Convert MimeMessage to EMailMessage, then proxy to Message, then save it to MongoDB
     * @param msg
     * @param username
     * @param type
     */
    public EMail parseMessage(MimeMessage msg, String username, String type, String emailid) throws Exception 
    {
    	   EMail mail = new EMail();
           mail.set_id(msg.getMessageID());
           mail.setSize(msg.getSize());

           //save attachment
           parseAttachments(msg, mail);
           //atachment (save to temp file).
           //read message.
           parseMail(msg, username, type, mail);
           return mail;
    }
    
    /**
     * 
     * @param msg
     * @param mail
     */
    private void parseAttachments(MimeMessage msg, EMail mail)
    {
    	try
    	{
	    	List<EMailAttachment> emailAttachments = getAttach(msg);
	    	 mail.setAttachments(emailAttachments);
	    	 
	    	 if(!emailAttachments.isEmpty()) {//process cid:part
	    		 Map<String, String> attachParts = new HashMap<String, String>();
	    		 for(EMailAttachment attach : emailAttachments) {
		    		 if(attach.getCId() != null) {
		    			 attachParts.put("cid:"+attach.getCId(), attach.getCDir());
		    		 }
		    	 }
	    		 mail.setAttachParts(attachParts);
	    	 }
	       
    	}
    	catch(Exception exp)
    	{
    		exp.printStackTrace();
    	}
    }
    
    /**
     * get mail attachment
     * @param part
     * @return
     * @throws Exception
     */
    public List<EMailAttachment> parseAttachment(Part part) throws Exception {
        List<EMailAttachment> emailAttachments = new ArrayList<EMailAttachment>();
        if (part.isMimeType("multipart/*")) {
        	//get content
            Multipart mp = (Multipart) part.getContent();
            //count
            int count = mp.getCount();
            for (int k = 1; count > 1 && k < count; k++) {
                Part mpart = mp.getBodyPart(k);
                String disposition = mpart.getDisposition();
                log.debug("disposition = " + disposition);
                //attachment (pending franzsong, how to control message, seened need answer).
                if (disposition != null && (disposition.equals(Part.ATTACHMENT))) {
                    EMailAttachment emailAttachment = new EMailAttachment();
                    if (mpart.getHeader("Content-ID") != null) {//content id
                        emailAttachment.setCId(mpart.getHeader("Content-ID")[0].replace("<", "").replace(">", ""));
                    }
                    //file name
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
                    //file type
                    emailAttachment.setType(extention);
                    //data to byte[]
                    emailAttachment.setFileID((ObjectId)saveFile(mpart.getInputStream(), fileName));
                    emailAttachment.setSize(mpart.getInputStream().available());
                    emailAttachments.add(emailAttachment);
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            emailAttachments = parseAttachment((Part) part.getContent());
        }
        return emailAttachments;
    }
    
    /**
     * 保存指定应用类型的二进制对象到文件交换区
     * @param inputStream
     * @param application
     * @return
     */
    private static ObjectId saveFile(InputStream inputStream, String fileName) throws Exception{
      
    	GridFSUtil  gridFSUtil = GridFSUtil.getGridFSUtil();
    	Object fileId = gridFSUtil.saveFile(inputStream, fileName);
    	return (ObjectId)fileId;//mustbe
    }
    
    /**
     * 
     * @param msg
     * @param username
     * @param type
     * @param mail
     * @return
     * @throws Exception
     */
    private EMail parseMail(Message msg, String username, String type, EMail mail) throws Exception {
    	//编码类型
        String encoding = "utf8";
        try {
//            encoding = msg.getHeader("Content-Transfer-Encoding")[0];
        } catch (Exception ex) {
            log.error(OriLog.logStack(ex));
        }
        //未读
        mail.setIsRead(0);
        //未处理
        mail.setIsProcess(0);
        //not deleted
        mail.setIsDelete(0);
        //not replyed
        mail.setIsReplay(0);
        
        //flag
        mail.setIsTrash(0);
        mail.setType(type);
        mail.setMailname(username);
        mail.setMsgId(((MimeMessage) msg).getMessageID());

        String from = getFrom(msg);
        //Pending Franzsong All this as People in system
        mail.setAddresser(MailParseUtil.parseAddresser(encoding, from));
        mail.setReceiver(getMailAddress(msg, "TO"));
        mail.setCc(getMailAddress(msg, "CC"));
        mail.setBcc(getMailAddress(msg, "BCC"));
        mail.setReplayTo(MailParseUtil.parseAddresser(encoding, from));
        mail.setMailtitle(MailParseUtil.parseMailSubject(encoding, msg.getSubject()));
        mail.setReceivedtime(msg.getReceivedDate());
        if (msg.getSentDate() != null) {
            mail.setSendtime(msg.getSentDate());
        } else {
            mail.setSendtime(new Date());
        }
        String content = MailParseUtil.parseContent(getContent(msg));
//        mail.setContent("<![CDDATA[" + MailParseUtil.parseContent(content) + "]]");
        content = parseCidParts(content, mail);
        mail.setContent(content);

        return mail;
    }    
    
    private String parseCidParts(String content, EMail mail) {
    	Map<String, String> attachParts = mail.getAttachParts();
    	if(attachParts == null ||
    			attachParts.isEmpty())
    		return content;
    	
//    	for(Map.Entry<String, String> entry : attachParts.entrySet())
//    	{
//    		content = content.replace(entry.getKey(), entry.getValue());
//    	}
    	 Document doc = Jsoup.parse(content);
         Elements media = doc.select("img");
         if (media == null || media.isEmpty()) {
             return content;
         }
         
         for (int i = 0; i < media.size(); i++) {
             Element a = (Element) media.get(i);
             String cid = a.attr("src");
             String cdir = null;
             if(cid != null && cid.startsWith("cid:")) {
            	 cdir = attachParts.get(cid);
             	a.removeAttr("src");
             	a.attr("src", cdir);
             }
             
             if(cdir == null ||
            		 (a.hasAttr("width") && a.hasAttr("height")) )
            	 continue;
             
             BufferedImage image = readBufferedImage(cdir);
             if(image == null) continue;
             
             if(!a.hasAttr("width")) {
            	 a.attr("width", "" + image.getWidth());
             }
             if(!a.hasAttr("height")) {
            	 a.attr("height", "" + image.getHeight());
             }
         }
         content =  doc.html();
    	return content;
    }
    
	private BufferedImage readBufferedImage(String URL) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(new URI(URL)));
		} catch (Exception ex) {

		}
		return image;
	}
    ///////////////////////////////////New Parse///////////////////////////////
}
