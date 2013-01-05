/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel;

import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Transient;
import com.google.code.morphia.query.Query;
import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;


/**
 * 存放二进制的文件：图片、视频、office文件、pdf文件。
 * 
 * @author sxy
 * 
 */
@Embedded
public class Attachment {
	public String fileName;	
	private String type;
	public int size;	
	private ObjectId fileId;
	private String contentId;//?
	@Transient
	private String filePath;//Use to send email from  local file.
	private String contentType;//attachment inline
	 
	 /**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	  * 
	  */
	public Attachment()
	{
		
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

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

	/**
	 * @return the fileId
	 */
	public ObjectId getFileId() {
		return fileId;
	}

	/**
	 * @param fileId the fileId to set
	 */
	public void setFileId(ObjectId fileId) {
		this.fileId = fileId;
	}

	/**
	 * @return the contentId
	 */
	public String getContentId() {
		return contentId;
	}

	/**
	 * @param contentId the contentId to set
	 */
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String toString() {
		try {
			Gson gson = new Gson();

			// convert java object to JSON format,
			// and returned as JSON formatted string
			String json = gson.toJson(this);
			return json;
		} catch (Exception exp) {
			return "attachment";
		}

	}
	
	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// Mapping
		Morphia morphia = new Morphia();

		morphia.map(com.original.service.channel.Channel.class);
		// DB
		Mongo mongo = new Mongo("localhost", 27017);
		DB db = mongo.getDB("song");

		// db mapping to object
		Datastore ds = morphia.createDatastore(mongo, "song");
		ds.ensureIndexes();

		// by mongo db
		DBCursor cursor = db.getCollection("attachment").find();
		while (cursor.hasNext()) {
			System.out.println(cursor.next());
		}

		long cc = ds.getCount(Attachment.class);
		System.out.println("cc:" + cc);

		// query and list

		Query<Attachment> chs = ds.find(Attachment.class);
		List<Attachment> chslist = chs.asList();

		Iterator<Attachment> ite = chs.iterator();
		while (ite.hasNext()) {
			System.out.println(ite.next());
		}

		for (int i = 0; i < chslist.size(); i++) {
			System.out.println(chslist.get(i));
		}

	}


}
