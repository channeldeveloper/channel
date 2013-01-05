/**
 * com.original.app.email.db.servic.EMail.java
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel.protocols.email.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import com.original.service.channel.protocols.email.oldimpl.BaseObject;

/**
 * 邮件数据对象。
 *
 * @author   cydow
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-2-13 8:17:13
 */
public class EMail extends BaseObject {

    private String type = null;
    private int size;
    
    /**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	private String mailname = null;
    private Date sendtime = null;
    private Date receivedtime = null;
    /**
	 * @return the receivedtime
	 */
	public Date getReceivedtime() {
		return receivedtime;
	}

	/**
	 * @param receivedtime the receivedtime to set
	 */
	public void setReceivedtime(Date receivedtime) {
		this.receivedtime = receivedtime;
	}

	private String mailtitle = null;
    private String addresser = null;
    private String receiver = null;
    private String cc= null;
    private String bcc = null;
    private String replayTo = null;
    private String content = null;
    private List<EMailAttachment> attachments;
    private Map<String, String> attachParts;
	private BaseObject attachment = null;
    private Integer isReplay = null;
    private Integer isSign = null;
    private Integer isRead = null;
    private Integer isDelete = null;
    private String infoId = null;
    private Integer isProcess = null;
    private Integer isTrash = null;
    private ImageIcon newMailIcon = null;
    private ImageIcon attachIcon = null;
    private ImageIcon replyIcon = null;
    private ImageIcon storageIcon = null;
    private String msgId = null;
//    private Integer xh = null; what to do ?

    /**
     * 构造函数
     */
    public EMail() {
//        this.setClassByKey("attachment", EMailAttachment.class);
    }

    /**
     * 获取账户名，如:111@126.com
     * @return
     */
    public String getMailname() {
        return mailname;
    }

    /**
     * 获取邮件类型，inbox（收件箱）、outbox（发件箱）、draftbox（草稿箱）
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * 获取发送时间
     * @return
     */
    public Date getSendtime() {
        return sendtime;
    }

    /**
     * 获取邮件主题
     * @return
     */
    public String getMailtitle() {
        return mailtitle;
    }

    /**
     * 获取发件人
     * @return
     */
    public String getAddresser() {
        return addresser;
    }

    /**
     * 获取收件人
     * @return
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * 获取抄送人
     * @return
     */
    public String getCc() {
        return cc;
    }

    /**
     * 获取密送人
     * @return
     */
    public String getBcc() {
        return bcc;
    }

    /**
     * 获取回复人
     * @return
     */
    public String getReplayTo() {
        return replayTo;
    }

    /**
     * 获取内容
     * @return
     */
    public String getContent() {
        return content;
    }

    /**
     * 获取附件
     * @return
     */
    public BaseObject getAttachment() {
        return attachment;
    }

    /**
     * 获取回复标识，0-未回复，1-已回复
     * @return
     */
    public Integer getIsReplay() {
        return isReplay;
    }

    /**
     * 获取重要性标识，0 -不重要，1-重要
     * @return
     */
    public Integer getIsSign() {
        return isSign;
    }

    /**
     * 获取阅读标识， 0- 未阅读，1-已阅读
     * @return
     */
    public Integer getIsRead() {
        return isRead;
    }

    /**
     * 获取删除状态， 0- 正常，1-已删除
     * @return
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    /**
     * 获取信息片断标识
     * @return
     */
    public String getInfoId() {
        return infoId;
    }

    /**
     * 获取处理标识， 0- 未处理，1-已处理
     * @return
     */
    public Integer getIsProcess() {
        return isProcess;
    }

    /**
     * 获取垃圾邮件状态， 0- 正常，1-垃圾邮件
     * @return
     */
    public Integer getIsTrash() {
        return isTrash;
    }

    /**
     * 
     * @return
     */
    public String getMsgId(){
        return msgId;
    }

    /**
     *
     * @param _msgId
     */
    public void setMsgId(String _msgId){
        msgId = _msgId;
    }
    
//    /**
//     *
//     * @return
//     */
//    public Integer getXh(){
//        return xh;
//    }
//
//    /**
//     *
//     * @param _xh
//     */
//    public void setXh(Integer _xh){
//        xh = _xh;
//    }

    /**
     * 设置账号名
     * @param Mailname
     */
    public void setMailname(String Mailname) {
        mailname = Mailname;
    }

    /**
     * 设置邮件类型
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 设置发送时间
     * @param sendtime
     */
    public void setSendtime(Date sendtime) {
        this.sendtime = sendtime;
    }

    /**
     * 设置邮件主题
     * @param mailtitle
     */
    public void setMailtitle(String mailtitle) {
        this.mailtitle = mailtitle;
    }

    /**
     * 设置发件人
     * @param addresser
     */
    public void setAddresser(String addresser) {
        this.addresser = addresser;
    }

    /**
     * 设置收件人
     * @param receiver
     */
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    /**
     * 设置抄送人
     * @param cC
     */
    public void setCc(String cc) {
        this.cc = cc;
    }

    /**
     * 设置密送人
     * @param bCC
     */
    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    /**
     * 设置回复人
     * @param replayto
     */
    public void setReplayTo(String replayto) {
        this.replayTo = replayto;
    }

    /**
     * 设置内容
     * @param content
     */
    public void setContent(String content) {
//        if ( content.startsWith("<![CDDATA[")){
//            int len = content.length();
//            this.content = content.substring(10,len-2);
        this.content = content;
//        }
//        else
//            this.content = "<![CDDATA["+content+"]]";
    }

    /**
     * 设置附件
     * @param attachment
     */
    public void setAttachment(BaseObject attachment) {
        this.attachment = attachment;
    }

    /**
     * 设置回复标识
     * @param isReplay
     */
    public void setIsReplay(Integer isReplay) {
        this.isReplay = isReplay;
    }

    /**
     * 设置重要性标识
     * @param sign
     */
    public void setIsSign(Integer sign) {
        this.isSign = sign;
    }

    /**
     * 设置阅读标识
     * @param isread
     */
    public void setIsRead(Integer isread) {
        this.isRead = isread;
    }

    /**
     * 设置删除状态， 0- 正常，1-已删除
     * @param  isdelete
     */
    public void setIsDelete(Integer isdelete) {
        isDelete = isdelete;
    }

    /**
     * 设置信息片断标识
     * @param  infoid
     */
    public void setInfoId(String infoid) {
        infoId = infoid;
    }

    /**
     * 设置处理标识， 0- 未处理，1-已处理
     * @param isprocess
     */
    public void setIsProcess(Integer isprocess) {
        isProcess = isprocess;
    }

    /**
     * 设置垃圾邮件状态， 0- 正常，1-垃圾邮件
     * @param istrash
     */
    public void setIsTrash(Integer istrash) {
        isTrash = istrash;
    }

    /**
     *
     * @return
     */
    public ImageIcon attach() {
//        System.out.println("==============>attach="+(attachment.get_List()==null?"---":attachment.get_List().size()));
//        if (attachment != null && attachment.get_List().size() > 0) {
//            return this.attachIcon;
//        }
        return null;
    }

    /**
     *
     * @return
     */
    public ImageIcon status() {
//        System.out.println("==============>status: delete="+isDelete+",type="+type+",read="+isRead+",infoid="+infoId+",replay="+isReplay);
        if (this.isDelete == 1) {
            return null;
        }
        if (this.type.equals("inbox") && this.isRead == 0) {
            return this.newMailIcon;
        }
        if (this.type.equals("inbox") || this.type.equals("sendedbox")) {
            if (this.infoId == null) {
                return this.storageIcon;
            }
            if (this.isReplay == 1) {
                return this.replyIcon;
            }
        }
        return null;
    }

    /**
     *
     * @param _newMailIcon
     * @param _storageIcon
     * @param _replyIcon
     * @param _attachIcon
     */
    public void setImageIcon(ImageIcon _newMailIcon, ImageIcon _storageIcon,
            ImageIcon _replyIcon, ImageIcon _attachIcon) {
        newMailIcon = _newMailIcon;
        storageIcon = _storageIcon;
        replyIcon = _replyIcon;
        attachIcon = _attachIcon;
    }

    public ImageIcon getStorageIcon(){
        return null;
    }
    public ImageIcon getNewMailIcon(){
        return null;
    }
    public ImageIcon getAttachIcon(){
        return null;
    }
    public ImageIcon getReplyIcon(){
        return null;
    }

    public void addAttachment(EMailAttachment att){
//        if ( attachment == null ) attachment = new BaseObject();
//        attachment.addList(att);
    }
    
    /**
   	 * @return the attachments
   	 */
   	public List<EMailAttachment> getAttachments() {
   		return attachments;
   	}

   	/**
   	 * @param attachments the attachments to set
   	 */
   	public void setAttachments(List<EMailAttachment> attachments) {
   		this.attachments = attachments;
   	}

	public Map<String, String> getAttachParts() {
		return attachParts;
	}
	public void setAttachParts(Map<String, String> attachParts) {
		this.attachParts = attachParts;
	}
}
