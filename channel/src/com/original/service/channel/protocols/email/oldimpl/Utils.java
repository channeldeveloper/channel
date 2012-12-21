package com.original.service.channel.protocols.email.oldimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.original.service.channel.Attachment;
import com.original.service.channel.ChannelAccount;
import com.original.service.channel.ChannelMessage;
import com.original.service.channel.Constants;
import com.original.service.channel.protocols.email.model.EMail;
import com.original.service.channel.protocols.email.model.EMailAttachment;

public class Utils {

	
	  /**
     * 根据格式的时间字符串，获取时间
     *
     * @param time
     *            - 转换时间字符串，格式为yyyy-MM-dd HH:mm:ss。
     * @return Date
     */
    public static Date getDate(String time) {
        int year = Integer.parseInt(time.substring(0, 4)) - 1900;
        int month = Integer.parseInt(time.substring(5, 7)) - 1;
        int day = Integer.parseInt(time.substring(8, 10));
        int hh = 0;
        int mm = 0;
        int ss = 0;
        if (time.length() >= 19) {
            hh = Integer.parseInt(time.substring(11, 13));
            mm = Integer.parseInt(time.substring(14, 16));
            ss = Integer.parseInt(time.substring(17, 19));
        }
        return new Date(year, month, day, hh, mm, ss);
    }

    
	/**
	 * 
	 * @param email
	 * @param ca
	 * @return
	 */
    public static ChannelMessage email2Channel(EMail email, ChannelAccount ca)
	{
		
//		getType()
//		getXh()
		
		ChannelMessage msg = new ChannelMessage();
		msg.setMessageID(email.getMsgId());
		
//		msg.setAttachments(emailAtta2ChannelAtts(email.getAttachments()));
		
		msg.setBody(email.getContent());
		msg.setChannelAccount(ca);
		msg.setSize(email.getSize());
		//MIME Type Tables		
		msg.setContentType(Constants.Content_Type_Text_Html);
		
		HashMap<String, Integer> flags = new HashMap<String, Integer>();
		flags.put(Constants.Message_Header_Ctr_EMAIL_Flag_REPLYED,	email.getIsReplay());
		flags.put(Constants.Message_Header_Ctr_EMAIL_Flag_SIGNED, email.getIsSign());
		flags.put(Constants.Message_Header_Ctr_EMAIL_Flag_SEEN,	email.getIsRead());
		flags.put(Constants.Message_Header_Ctr_EMAIL_Flag_DELETED, email.getIsDelete());
		flags.put("isProcess",	email.getIsProcess());//?
		flags.put("isTrash", email.getIsTrash());//?
		msg.setFlags(flags);		
		msg.setFollowedID(null);
		msg.setFromAddr(email.getAddresser());
		
		HashMap<String, String> exts = new HashMap<String, String>();
		exts.put(Constants.Message_Header_Ext_EMAIL_BCC, email.getBcc());
		exts.put(Constants.Message_Header_Ext_EMAIL_CC, email.getCc());
		exts.put(Constants.Message_Header_Ext_EMAIL_ReplyTo, email.getReplayTo());
		exts.put(Constants.Message_Header_Ext_EMAIL_Foler, email.getType());
		msg.setExtensions(exts);
		msg.setSubject(email.getMailtitle());
		msg.setRecievedDate(email.getReceivedtime());
		msg.setToAddr(ca.getAccount().getUser());
		
		return msg;
	}
	
	/**
	 * 
	 * @param emailAtts
	 * @return
	 */
	public static List<Attachment> emailAtta2ChannelAtts(
			List<EMailAttachment> emailAtts) {
		if (emailAtts == null || emailAtts.size() == 0) {
			return null;
		}
		ArrayList<Attachment> atts = new ArrayList<Attachment>();
		for (EMailAttachment emailAtt : emailAtts) {
			Attachment a = new Attachment();
			a.setContentId(emailAtt.getCId());
			a.setFileId(emailAtt.getFileID());
			a.setFileName(emailAtt.getFileName());
			a.setSize(emailAtt.getSize());
			a.setType(emailAtt.getType());
			atts.add(a);
		}
		return atts;
	}
	///////////////////////////////
	
	/**
	 * 
	 * @param msg
	 * @return
	 */
	public  static EMail  channel2Email(ChannelMessage msg)
	{
		EMail email = new EMail();
    	email.setMsgId(msg.getMessageID());
    	email.setContent(msg.getBody());
    	email.setSize(msg.getSize());

    	email.setAddresser(parseHTMLFlags(msg.getFromAddr()));
    	email.setReceiver(parseHTMLFlags(msg.getChannelAccount().getAccount().getUser()));
    	
    	if (msg.getExtensions() != null)
    	{
    		email.setType(msg.getExtensions().get(Constants.Message_Header_Ext_EMAIL_Foler));
    		email.setBcc(parseHTMLFlags(msg.getExtensions().get(Constants.Message_Header_Ext_EMAIL_BCC)));
    		email.setCc(parseHTMLFlags(msg.getExtensions().get(Constants.Message_Header_Ext_EMAIL_CC)));
    		email.setReplayTo(msg.getExtensions().get(Constants.Message_Header_Ext_EMAIL_ReplyTo));
    	}

    	email.setMailtitle(msg.getSubject());
    	email.setSendtime(ChannelMessage.TYPE_SEND.equals(msg.getType()) ? 
    			msg.getSentDate() : msg.getRecievedDate());
		return email;
	}
	
	public static String parseHTMLFlags(String content)
	{
		if(content != null && !content.isEmpty()) {
			content = content.replaceAll("\\<", "&lt;");
			content = content.replaceAll("\\>", "&gt;");
		}
		return content;
	}
	
}
