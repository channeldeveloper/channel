package com.original.service.storage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.original.util.log.OriLog;

/**
 *  流数据对象。
 *
 * @author   cydow
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-2-13 8:17:13
 */
public class FSDataSource implements DataSource {
	
	private GridFS fs;
	private ObjectId fileId;
	private String fileName;
	Logger log = OriLog.getLogger(FSDataSource.class);
	public FSDataSource(GridFS fs, ObjectId fileId, String fileName)
	{
		this.fs = fs;
		this.fileId = fileId;
		this.fileName = fileName;
	}

    /**设置流对象对应的文件名
     *
     * @param filename
     */
    public void setFilename(String filename) {
        this.fileName = filename;
    }

    /**获取流对象对应的文件名
     *
     * @return 
     */
    public String getFilename() {
        return this.fileName;
    }

    /**
     * This method returns an <code>InputStream</code> representing
     * the data and throws the appropriate exception if it can
     * not do so.  Note that a new <code>InputStream</code> object must be
     * returned each time this method is called, and the stream must be
     * positioned at the beginning of the data.
     *
     * @return an InputStream
     */
    public InputStream getInputStream() throws IOException
    {
    	try
    	{
    	GridFSDBFile fsdbFile = GridFSUtil.getGridFSUtil().getFile(fileId,  fs);
    	return fsdbFile.getInputStream();
    	}
    	catch(Exception exp)
    	{
    		 exp.printStackTrace();
    	}
    	return null;
    }
    

    /**
     * This method returns an <code>OutputStream</code> where the
     * data can be written and throws the appropriate exception if it can
     * not do so.  Note that a new <code>OutputStream</code> object must
     * be returned each time this method is called, and the stream must
     * be positioned at the location the data is to be written.
     *
     * @return an OutputStream
     */
    public OutputStream getOutputStream() throws IOException
    {
    	 throw new UnsupportedOperationException("getOutputStream");
    	
    }

    /**
     * This method returns the MIME type of the data in the form of a
     * string. It should always return a valid type. It is suggested
     * that getContentType return "application/octet-stream" if the
     * DataSource implementation can not determine the data type.
     *
     * @return the MIME Type
     */
    public String getContentType()
    {
    	return "application/octet-stream;";
    }
    
    

    /**
     * Return the <i>name</i> of this object where the name of the object
     * is dependant on the nature of the underlying objects. DataSources
     * encapsulating files may choose to return the filename of the object.
     * (Typically this would be the last component of the filename, not an
     * entire pathname.)
     *
     * @return the name of the object.
     */
    public String getName(){
    	return fileName;
    }

    
    /**
     * 流对象的所有信息
     * @param  filename
     * @throws IOException
     */
    public void writeToFile(String filename) throws IOException {
    	
    	try
    	{
	    	GridFSDBFile fsdbFile = GridFSUtil.getGridFSUtil().getFile(fileId,  fs);
			// Save loaded image from database into new image file
			FileOutputStream outputImage = new FileOutputStream(filename);
			fsdbFile.writeTo(outputImage);
			outputImage.close();
    	}
    	catch(Exception exp)
    	{
    		exp.printStackTrace();
    	}
	
    }

}
