/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;
import com.original.service.channel.Constants;

/**
 * 渠道数据初始化。
 * 
 * @author sxy
 * 
 */
public class Initializer {

	java.util.logging.Logger logger;
	private DB db;
	private Mongo mongo;

	/**
	 * 
	 * @param mongo
	 * @param db
	 */
	public Initializer(Mongo mongo)  {
		this.mongo = mongo;		
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void init(boolean force) throws Exception
	{
		try {
			logger = Logger.getLogger("Channel Service");
			if (mongo == null)
			{
				mongo = new Mongo(Constants.Channel_DB_Server,
						Constants.Channel_DB_Server_Port);
			}
			db = mongo.getDB(Constants.Channel_DB_Name);
		
			// init profile
			String collectionName = Constants.Channel_Collection_Profile;
			String fileName = "/com/original/service/channel/config/profile.json";
			initProfile(db, collectionName, fileName, force);
			
			// init channel
			collectionName = Constants.Channel_Collection_Channel;
			fileName = "/com/original/service/channel/config/channel.json";
			initPeople(db, collectionName, fileName, force);
			
			// init people
			collectionName = Constants.Channel_Collection_People;
			fileName = "/com/original/service/channel/config/people.json";
			initChannel(db, collectionName, fileName, force);

		} catch (Exception exp) {
			throw exp;
		}
	}

	/**
	 * 
	 * @param db
	 * @param collectionName
	 * @param fileName
	 * @param force
	 * @throws IOException
	 */
	public void initProfile(DB db, String collectionName, String fileName,
			boolean force) {
		try {
			initCollection(db, collectionName, fileName, force);
		} catch (IOException exp) {
			exp.printStackTrace();
		}

	}

	/**
	 * 
	 * @param db
	 * @param collectionName
	 * @param fileName
	 * @param force
	 * @throws IOException
	 */
	public void initPeople(DB db, String collectionName, String fileName,
			boolean force) {
		try {
			initCollection(db, collectionName, fileName, force);
		} catch (IOException exp) {
			exp.printStackTrace();
		}
	}

	/**
	 * 
	 * @param db
	 * @param collectionName
	 * @param fileName
	 * @param force
	 * @throws IOException
	 */
	public void initChannel(DB db, String collectionName, String fileName,
			boolean force)  throws Exception{
		try {
			initCollection(db, collectionName, fileName, force);
		} catch (Exception exp) {
			throw exp;
		}

	}

	/**
	 * 
	 * @param db
	 * @param collectionName
	 * @param fileName
	 * @param force
	 * @throws IOException
	 */
	private void initCollection(DB db, String collectionName, String fileName,
			boolean force) throws IOException {
		DBCollection collection = db.getCollection(collectionName);
		if (collection.getCount() > 0 && !force) {
			logger.info(collectionName + " had existed!");
			return;
		}
		
		if (force && collection.getCount() > 0)
		{			
			collection.drop();
			logger.info("force to init, drop the collection:" + collectionName);
		}
		InputStream is = Initializer.class.getResourceAsStream(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuffer fileText = new StringBuffer();
		String line;
		while ((line = br.readLine()) != null) {
			fileText.append(line);
		}
		logger.info(collectionName + " Read:" + fileText);
		// System.out.println(profile);
		br.close();
		if (fileText != null) {
			// convert JSON to DBObject directly
			List<String> list = parseJsonItemsFile(fileText);
			for (String txt : list)
			{
				logger.info(collectionName + " init item:" + txt);
				DBObject dbObject = (DBObject) JSON.parse(txt);
				collection.insert(dbObject);
			}
		}
		logger.info(collectionName + " init Done:" + collection.count());
	}

	/**
	 * 
	 * @param sb
	 * @return
	 */
	private List<String> parseJsonItemsFile(StringBuffer sb)
	{
		ArrayList<String> dim = new ArrayList<String>();
		if (sb != null && sb.length() >0)
		{			
			int s0 = 0;			
			int stack = 0;
			for (int i = 0; i< sb.length(); i++)
			{
				if (sb.charAt(i) == '{')
				{
					stack++;			
				}
				if (sb.charAt(i) == '}')
				{
					stack--;
					if (stack == 0)
					{
						dim.add(sb.substring(s0, i+1));
						s0 = i+1;
					}
				}
			}
		}
		return dim;
	}
	public static void main(String... aArgs) {
		try {
			Initializer init = new Initializer(null);
			init.init(false);
		} catch (Exception exp) {
			exp.printStackTrace();
		}

	}
}
