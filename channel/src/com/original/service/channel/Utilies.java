/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.original.service.channel.protocols.email.model.EMail;
import com.original.service.channel.protocols.email.model.EMailAttachment;
import com.original.service.channel.protocols.email.model.EMailParser;

/**
 * 渠道的工具类。
 * @author sxy
 *
 */
public class Utilies {
	
	public static final String TEMP_DIR = System.getProperty("user.dir") 
			+ File.separator + "temp"; //临时目录
	
	public static final int IMAGE_WIDTH = 300, IMAGE_HEIGHT = 225;//图片的宽、高度(4:3)
	public static final int MAX_IMAGE_WIDTH = 700, MAX_IMAGE_HEIGHT = 525;//最大图片的宽、高度(4:3)
	public static final DecimalFormat IMAGE_FORMATTER = new DecimalFormat("0.00");
	
	/**
	 * get email attachment temp store dir
	 * @param fileID
	 * @param fileName
	 * @return
	 */
	public static String getTempDir(Object fileID, String fileName) {
//		int suffix = fileName.lastIndexOf(".");
		
//		return new File(TEMP_DIR, fileID
//				+ (suffix == -1 ? "" : fileName.substring(suffix))).toURI().toString();
		File tempDir = new File(TEMP_DIR);
		if (!tempDir.exists()) {
			tempDir.mkdir();
		}
		tempDir = new File(TEMP_DIR, fileName);
		return tempDir.toURI().toString();
	}
	public static String getTempDir(EMailAttachment attach) {
		return getTempDir(attach.getFileID(), attach.getFileName());
	}
	
	/**
	 * Copy 2 POJO
	 * @param target
	 * @param source
	 * @throws Exception
	 */
	public static <T> void copyFields(T target, T source) throws Exception{
	    Class<?> clazz = source.getClass();

	    for (Field field : clazz.getFields()) {
	        Object value = field.get(source);
	        field.set(target, value);
	    }
	}
	
	/**
	 * 自动缩放图片的大小
	 * @param oldWidth
	 * @param oldHeight
	 * @return 缩小后的大小，分别为宽度、高度、缩放比例
	 */
	public static String[] scale(int oldWidth, int oldHeight) {
		//如果设定宽、高度和图片原来的宽、高度一致，就不需要调整了
		if(oldWidth == IMAGE_WIDTH && oldHeight == IMAGE_HEIGHT)
			return new String[]{""+oldWidth, ""+oldHeight, "1.0"};

		double d1 = oldWidth / (double) oldHeight, 
				d2 = IMAGE_WIDTH / (double) IMAGE_HEIGHT;

		double scale = 1.0d;
		String scaleValue = "1.0";
		if (d2 >= d1) { // 以高度为准
			if (oldHeight > IMAGE_HEIGHT) {
				scale = IMAGE_HEIGHT / (double)oldHeight;
				scaleValue = IMAGE_FORMATTER.format(scale);
				
				oldHeight = IMAGE_HEIGHT;
				oldWidth = (int) (oldHeight * d1);
			}
		} else {// 以宽度为准
			if (oldWidth > IMAGE_WIDTH) {
				scale = IMAGE_WIDTH / (double)oldWidth;
				scaleValue = IMAGE_FORMATTER.format(scale);
				
				oldWidth = IMAGE_WIDTH;
				oldHeight = (int) (oldWidth / d1);
			}
		}
		return new String[]{""+oldWidth, ""+oldHeight, scaleValue};
	}
	/**
	 * 还原原始图片的宽、高度
	 * @param scales
	 * @return
	 */
	public static int[] unScale(String[] scales)
	{
		if(scales != null && scales.length == 3)
		{
			 int width = Integer.parseInt(scales[0]),
        			 height = Integer.parseInt(scales[1]);
			 float scale = Float.parseFloat(scales[2]);
        	 if(scale == 1.0f)
        		 return new int[]{width, height};
        	 
        	 //计算原始宽、高度
        	 width = (int)(width / scale);
        	 height = (int)(height/ scale);
        	 
        	 if(width > MAX_IMAGE_WIDTH) {
        		 height = MAX_IMAGE_WIDTH * height / width;
        		 width = MAX_IMAGE_WIDTH;
        	 }
        	 return new int[]{width, height};
		}
		return null;
	}
	
	/**
	 * 
	 * @param email
	 * @return
	 */
	public static ChannelMessage email2Channel(EMail email)
	{		
		ChannelMessage msg = new ChannelMessage();
		msg.setMessageID(email.getMsgId());
//		msg.setAttachmentIds(email.getAttachments()); Pending franz deal attachment later
		msg.setBody(email.getContent());
//		msg.setChannelAccount(ca);
		msg.setSize(email.getSize());
		//MIME Type Tables		
		msg.setType(ChannelMessage.TYPE_RECEIVED);
		msg.setContentType(Constants.Content_Type_Text_Html);
		msg.setClazz(ChannelMessage.MAIL);
		
		HashMap<String, Integer> flags = new HashMap<String, Integer>();
		flags.put(ChannelMessage.FLAG_REPLYED,	email.getIsReplay());
		flags.put(ChannelMessage.FLAG_SIGNED, email.getIsSign());
		flags.put(ChannelMessage.FLAG_SEEN,	email.getIsRead());
		flags.put(ChannelMessage.FLAG_DELETED, email.getIsDelete());
		flags.put("isProcess",	email.getIsProcess());//?
		flags.put("isTrash", email.getIsTrash());//?
		msg.setFlags(flags);		
		msg.setFollowedID(null);
		msg.setFromAddr(email.getAddresser());
		
		HashMap<String, String> exts = new HashMap<String, String>();
		exts.put(ChannelMessage.EXT_EMAIL_BCC, email.getBcc());
		exts.put(ChannelMessage.EXT_EMAIL_CC, email.getCc());
		exts.put(ChannelMessage.EXT_EMAIL_ReplyTo, email.getReplayTo());
		exts.put(ChannelMessage.EXT_EMAIL_Foler, email.getType());
		msg.setExtensions(exts);
		msg.setSubject(email.getMailtitle());
		
		msg.setReceivedDate(email.getSendtime());
//		msg.setToAddr(ca.getAccount().getUser());
		return msg;
	}
	
	/**
	 * 
	 * @param msg
	 * @return
	 */
	public  static EMail  channel2Email(ChannelMessage msg)
	{
		EMail email = new EMail();
    	email.setMsgId(msg.getMessageID());
    	email.setContent(EMailParser.showComplete(msg.getBody()));
    	email.setSize(msg.getSize());

    	email.setAddresser(parseHTMLFlags(msg.getFromAddr()));
    	email.setReceiver(parseHTMLFlags(msg.getChannelAccount().getAccount().getUser()));
    	
    	if (msg.getExtensions() != null)
    	{
    		email.setType(msg.getExtensions().get(ChannelMessage.EXT_EMAIL_Foler));
    		email.setBcc(parseHTMLFlags(msg.getExtensions().get(ChannelMessage.EXT_EMAIL_BCC)));
    		email.setCc(parseHTMLFlags(msg.getExtensions().get(ChannelMessage.EXT_EMAIL_CC)));
    		email.setReplayTo(msg.getExtensions().get(ChannelMessage.EXT_EMAIL_ReplyTo));
    	}

    	email.setMailtitle(msg.getSubject());
    	email.setSendtime(msg.isSent() ? msg.getSentDate() : msg.getReceivedDate());
    	
    	//where're attachments??
    	email.setAttachments(parseAttachments(msg.getAttachments()));
		return email;
	}
	
	public static List<EMailAttachment> parseAttachments(List<Attachment> attaches)
	{
		List<EMailAttachment> mailAttaches = null;
    	if(attaches != null && !attaches.isEmpty()) {
    		mailAttaches = new ArrayList<EMailAttachment>(attaches.size());
    		EMailAttachment mailAttach = null;
    		for(Attachment attach : attaches) {
    			if(attach.getContentId() == null) {//if has cid, then see it as inline not attachment!!!
    				mailAttach = new EMailAttachment();

    				mailAttach.setCId(attach.getContentId());
    				mailAttach.setFileID(attach.getFileId());
    				mailAttach.setFileName(attach.getFileName());
    				mailAttach.setSize(attach.getSize());
    				mailAttach.setType(attach.getType());
    				mailAttaches.add(mailAttach);
    			}
    		}
    	}
    	return mailAttaches;
	}
	
	public static String parseHTMLFlags(String content)
	{
		if(content != null && !content.isEmpty()) {
			content = content.replaceAll("\\<", "&lt;");
			content = content.replaceAll("\\>", "&gt;");
		}
		return content;
	}
	
	public static String parseMail(ChannelMessage msg) {
		return parseMail(msg, true);
	}
	
	public static String parseMail(ChannelMessage msg, boolean addHTMLFlag) {
		String content = parseMail(channel2Email(msg));
		return addHTMLFlag ? addHTMLFlag(content) : content;
	}
	
	public static String addHTMLFlag(String body) {
		StringBuffer html = new StringBuffer("<html><body>");
		html.append(body);
		html.append("</body></html>");
		return html.toString();
	}
	
	public static String getBody(String html) {
		if (html == null || html.isEmpty())
			return html;

		int index0 = html.indexOf("<body>"), index1 = html.indexOf("</body>");

		if (index0 != -1) {
			if (index1 == -1)
				index1 = html.length();
			html = html.substring(index0 + 6, index1);
		}
		return html;
	}
	
	public static String getNewAddedBody(String html) {
		if (html == null || html.isEmpty())
			return html;

		Matcher matcher = Pattern.compile(getSeparatorRegex()).matcher(html);
		if (matcher.find()) {
			html = html.substring(0, matcher.start());
		}
		if (html.indexOf("</body>") == -1) // 未正确关闭
		{
			html += "\r\n</body>\r\n</html>";
		}
		return html;
		
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream("c:/html.txt")));
		char[] buffer = new char[1024];
		int read  = -1;
		StringBuffer sb = new StringBuffer();
		while((read = br.read(buffer)) != -1) {
			sb.append(buffer, 0, read);
		}
		br.close();
		System.out.println(getNewAddedBody(sb.toString()));
	}

	/**
	 * 处理邮件内容，即显示邮件的完整内容
	 * @param email
	 */
	public static String parseMail(EMail email) {
		if (email != null) {
			//这部分后面会被接内容时会删除
			StringBuffer header = new StringBuffer("<div style=\"font-size: 10px;color:#557fc4;background:#efefef;padding:8px;\">");
			header.append("发件人：").append(email.getAddresser()).append("<br>");
			header.append("收件人：").append(email.getReceiver()).append("<br>");
			if(email.getCc() != null) {
				header.append("抄　送：").append(email.getCc()).append("<br>");
			}			
			String content = email.getContent();
			//附件名
			StringBuilder attach = new StringBuilder();
			List<EMailAttachment> attachList = email.getAttachments();
			if (attachList != null) {
				for (int l = 0; l < attachList.size(); l++) {
					EMailAttachment eatt = (EMailAttachment) attachList.get(l);
					if(eatt != null) {
						attach.append(String.format("<a href=\"%s\" style=\"color:green; text-decoration: underline;\">", getTempDir(eatt)));
						attach.append(eatt.getFileName()).append("</a>; ");
					}
				}
			} 
			if(attach.length() > 0) {
				header.append("附　件：").append(attach).append("<br>");
			}
			
			if (content.startsWith("<![CDDATA[")) {
				content = content.substring(10, content.length() - 2);
			}
			content = header.append("</div><div style=\"padding:10px 0 10px 0;\">").append(content).append("</div>").toString();
			return content;
		}
		
		return null;
	}
	
	public static String getSeparatorFlags() {
		return "<div style=\"font-size: 10px;font-family: Arial Narrow;padding:50px 0 0 0;\">" +
				"-------------------原始内容-------------------</div>";
	}
	
	public static String getSeparatorRegex() {
		//<div style="padding-left: 0; padding-right: 0; padding-bottom: 0; padding-top: 50px">
//	      -------------------&#21407;&#22987;&#20869;&#23481;-------------------
//	      </div>
		return "<div style=\".*?\">([\\s\\S]*)-------------------" +
				"&#21407;&#22987;&#20869;&#23481;-------------------([\\s\\S]*)</div>";
		
		//&#21407;&#22987;&#20869;&#23481; = 原始内容
	}
	
	/**
	 * 回复或转发邮件时的新内容区域，位于getMailSeparatorFlags()上面。用于添加附加内容！
	 * @return
	 */
	public static String getMailReplyArea() {
		return "<div><br></div><div><br></div>";
	}
	
}
