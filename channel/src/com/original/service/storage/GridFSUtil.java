package com.original.service.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.original.service.channel.Constants;

/**
 * 
 * http://www.mongodb.org/display/DOCS/GridFS+Specification GridFS Specification
 * 
 * @author sxy
 * 
 */
public final class GridFSUtil {
	private Mongo mongo;	
	private GridFS fs;
	private DB db;
	
	/**
	 * 
	 * @return
	 */
	public GridFS getFS()
	{
		return fs;
	}
	public static GridFSUtil  getGridFSUtil()
	{
		if (singleton == null)
		{
			singleton = new GridFSUtil(Constants.Channel_DB_Server, Constants.Channel_DB_Server_Port, Constants.Channel_DB_Name);
		}
		return singleton;
	}
	private static GridFSUtil singleton;
	
	/**
	 * 
	 * @param dbName
	 */
	private GridFSUtil(String host, int port, String dbName) {
		try {
			mongo = new Mongo(host, port);
			db = mongo.getDB(dbName);
			// Create GridFS object
			fs = new GridFS(db);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	
	/**
	 * Save.
	 * @param file
	 * @return
	 */
	public Object saveFile(InputStream in, String fileName) throws Exception{

		int size = (int) in.available();
		byte[] buffer = new byte[size];
	
		in.read(buffer);
		in.close();

		// Create GridFS object and save into database

		GridFSInputFile gfsin = fs.createFile(buffer);
		gfsin.setFilename(fileName);
		gfsin.save();
		return gfsin.getId();
	}
	/**
	 * Save.
	 * @param file
	 * @return
	 */
	public Object saveFile(File file) throws Exception{

		int size = (int) file.length();
		byte[] buffer = new byte[size];
		FileInputStream in = new FileInputStream(file);
		in.read(buffer);
		in.close();

		// Create GridFS object and save into database

		GridFSInputFile gfsin = fs.createFile(buffer);
		gfsin.setFilename(file.getName());
		gfsin.save();
		return gfsin.getId();
	}

	/**
	 * Save
	 * @param fileName
	 * @param filedata
	 * @return
	 */
	public Object saveFile(String fileName, byte[] filedata) {

		// Create GridFS object and save into database

		GridFSInputFile gfsin = fs.createFile(filedata);
		gfsin.setFilename(fileName);

		gfsin.save();

		return gfsin.getId();
	}

	/**
	 * Get.
	 * @param id
	 * @return
	 */
	public  GridFSDBFile getFile(Object id)
	{
		// Find saved image
		GridFSDBFile out = fs.findOne(new BasicDBObject("_id", id));
		return out;
	}

	/**
	 * 
	 * @param uri
	 * @param mongo
	 * @param db
	 * @return
	 * @throws Exception
	 */
	public Object saveFile(URI uri, GridFS fs) throws Exception {

		if (uri == null) {
			throw new NullPointerException("Parameters uri  is null!");
		}
		if (fs == null) {
			throw new NullPointerException("Parameters fs is null!");
		}

		// file to byte[]
		File file = new File(uri);
		int size = (int) file.length();
		byte[] buffer = new byte[size];
		FileInputStream in = new FileInputStream(file);
		in.read(buffer);
		in.close();

		// Create GridFS object and save into database

		GridFSInputFile gfsin = fs.createFile(buffer);
		gfsin.setFilename(file.getName());

		gfsin.save();

		return gfsin.getId();
	}

	/**
	 * 
	 * @param uri
	 * @param mongo
	 * @param db
	 * @return
	 * @throws Exception
	 */
	public  GridFSDBFile getFile(Object id, GridFS fs) throws Exception {

		if (id == null) {
			throw new NullPointerException("Parameters id  is null!");
		}
		if (fs == null) {
			throw new NullPointerException("Parameters fs is null!");
		}

		// Find saved image
		GridFSDBFile out = fs.findOne(new BasicDBObject("_id", id));
		return out;

	}

	/**
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public byte[] readFile(String filePath) throws Exception {
		File file = new File(filePath);
		int size = (int) file.length();
		byte[] buffer = new byte[size];
		FileInputStream in = new FileInputStream(file);
		in.read(buffer);
		in.close();
		return buffer;
	}
	
	/**
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public void writeFile(ObjectId dbfileID, String filePath) throws Exception {
		GridFSDBFile out = fs.findOne(new BasicDBObject("_id", dbfileID));// one
		FileOutputStream fOutStream = new FileOutputStream(
				filePath);
		out.writeTo(fOutStream);
		fOutStream.close();
	}

	public static void main(String[] args) throws Exception {
		// Load our image
		byte[] imageBytes = GridFSUtil.getGridFSUtil().readFile("C:/profile.json");
		// Connect to database
		Mongo mongo = new Mongo("127.0.0.1");
		String dbName = "GridFSTestJava";
		DB db = mongo.getDB(dbName);
		// Create GridFS object
		GridFS fs = new GridFS(db);
		// Save image into database
		GridFSInputFile in = fs.createFile(imageBytes);// file sytem
		in.save();

		System.out.println(in.getId().getClass());
		// Find saved image
		GridFSDBFile out = fs.findOne(new BasicDBObject("_id", in.getId()));// one
																			// file

		// Save loaded image from database into new image file
		FileOutputStream outputImage = new FileOutputStream(
				"C:/profile-bk.json");
		out.writeTo(outputImage);
		outputImage.close();
	}
}