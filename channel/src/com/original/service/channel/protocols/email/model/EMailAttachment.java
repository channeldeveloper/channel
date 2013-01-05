/**
 * com.original.app.email.db.servic.EMailAttachment.java
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel.protocols.email.model;

import org.bson.types.ObjectId;

import com.original.service.channel.protocols.email.oldimpl.BaseObject;

/**
 * 邮件附件数据对象。
 *
 * @author   cydow
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-2-13 8:17:13
 */
public class EMailAttachment extends BaseObject {

	private String cType;
    /**
	 * @return the cType
	 */
	public String getCType() {
		return cType;
	}

	/**
	 * @param cType the cType to set
	 */
	public void setCType(String cType) {
		this.cType = cType;
	}
	private Integer Size = null;
    private String FileName = null;
    private String Type = null;
    private String CId = null;
    private String CDir = null;
    private byte[] Data = null;
    private String infoId = null;
    
    private ObjectId fileID;

    /**
	 * @return the fileID
	 */
	public ObjectId getFileID() {
		return fileID;
	}

	/**
	 * @param fileID the fileID to set
	 */
	public void setFileID(ObjectId fileID) {
		this.fileID = fileID;
	}

	public EMailAttachment(){
//        this.set_Name("attachment");
    }
    
    /**
     * 获取文件名
     * @return
     */
    public String getFileName() {
        return FileName;
    }

    /**
     * 获取文件大小
     * @return
     */
    public Integer getSize() {
        return Size;
    }

    /**
     * 获取文件类型
     * @return
     */
    public String getType() {
        return Type;
    }

    /**
     * 获取内容标识
     * @return
     */
    public String getCId() {
        return CId;
    }

    /**
     * 获取文件内容
     * @return
     */
    public byte[] getData() {
        return Data;
    }

    /**
     * 获取信息片断标识
     * @return
     */
    public String getInfoId() {
        return infoId;
    }
    /**
     * 设置文件名
     * @param filename
     */
    public void setFileName(String filename) {
        this.FileName = filename;
    }

    /**
     * 设置文件大小
     * @param size
     */
    public void setSize(Integer size) {
        this.Size = size;
    }

    /**
     * 设置文件类型
     * @param type
     */
    public void setType(String type) {
        this.Type = type;
    }

    /**
     * 设置内容标识
     * @param cid
     */
    public void setCId(String cid) {
        this.CId = cid;
    }

    /**
     * 设置文件内容
     * @param data
     */
    public void setData(byte[] data) {
        this.Data = data;
    }
    /**
     * 设置信息片断标识
     * @param  infoid
     */
    public void setInfoId(String infoid) {
        infoId = infoid;
    }
    

	public String getCDir() {
		return CDir;
	}
	public void setCDir(String cDir) {
		CDir = cDir;
	}
}
