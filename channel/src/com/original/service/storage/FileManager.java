package com.original.service.storage;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import com.original.util.log.OriLog;

/**
 * 数据库文件管理对象。
 *
 * @author   cydow
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-2-13 8:17:13
 */
public class FileManager {

    static void changeListInfo(List _list) {
        if (_list == null) {
            return;
        }
        int count = _list.size();
        for (int i = 0; i < count; i++) {
            String fname = (String) _list.get(i);
            String[] fnames = fname.split(":");
            if (fnames.length > 1) {
                _list.set(i, fnames[0]);
                for (int j = 1; j < fnames.length; j++) {
                    _list.add(fnames[j]);
                }
            }
        }
    }

    /**
     * 更新二进制信息
     *
     * 处理逻辑：
     * 	比较修改前后数据库中二进制信息变化情况， 如果在修改前存在而修改后不存在
     * 	或在修改前存在而修改后不同， 则需要删除旧的二进制信息；
     * 	如果在修改前不存在而修改后存在或在修改前存在而修改后不同，
     * 	则需要保存二进制信息到数据库。
     *
     * @param oldfilelist
     *            - 已经存在的二进制列表信息
     * @param newfilelist
     *            - 修改后存在的二进制列表信息
     */
    public static String updateBinaryFile(List newfilelist, List oldfilelist, String userId) {
        if (newfilelist == null || oldfilelist == null) {
            return null;
        }
        if (oldfilelist.isEmpty() && newfilelist.isEmpty()) {
            return null;
        }
        changeListInfo(newfilelist);
        changeListInfo(oldfilelist);

        for (int i = 0; i < oldfilelist.size(); i++) {
            String fname = (String) oldfilelist.get(i);
            if (!newfilelist.contains(fname)) {
                String retinfo = deleteBinaryFile(fname);
                if (retinfo != null) {
                    return retinfo;
                }
            }
        }
        for (int i = 0; i < newfilelist.size(); i++) {
            String fname = (String) newfilelist.get(i);
            String retinfo = writeBinaryFile(fname, userId);
            if (retinfo != null) {
                return retinfo;
            }
        }
        return null;
    }

    public static String writeBinaryFile(String fname, String userId) {
        FileExchange _wrap = FileExchange.getInstance();
        StreamData data = _wrap.getContextVariable(fname, true);
        if (data != null) {
            InputStream is = data.getInputStream();
            if (is != null) {
                String retinfo = writeBinaryFile(is, fname, userId);
                if (retinfo != null) {
                    return retinfo;
                }
            } else {
                OriLog.getLogger(FileManager.class).debug("not set StreamData (" + fname + ")!");
            }
        }
        return null;
    }

    /**
     * 写二进制数据到文件数据库
     *
     * @param in
     *            - 二进制流
     * @param filename
     *            - 文件名
     */
    public static String writeBinaryFile(InputStream in, String filename, String userId) {
        Mongo m = ConnPollManager.getInstance().getConnect();
        if (m == null) {
            OriLog.getLogger(FileManager.class).error("writeBinaryFile is error!");
            return "connect db is failure.";
        }
        try {
            OriLog.getLogger(FileManager.class).debug("Savefile  (" + filename + ")....");
            GridFS fs = new GridFS(m.getDB(Constants.FILEDB));
            List<GridFSDBFile> files = fs.find(filename);
            String fileid = "";
            if (files.size() > 0) {
                fileid = (String) (files.get(0).get(Constants._ID));
                fs.remove(filename);
            } else {
//                fileid = DBUtils.getSequenceId(m, Constants.FILEDB, Constants.FILEDB);
                if (userId != null && userId.length() > 0) {
                    fileid = userId + "_" + fileid;
                }
            }
            GridFSFile mongofile = fs.createFile(in);
            mongofile.put(Constants._ID, fileid);
            mongofile.put("filename", filename);
            mongofile.put("uploadDate", new Date());
            mongofile.save();
            in.close();
        } catch (Exception e1) {
            OriLog.getLogger(FileManager.class).error(OriLog.logStack(e1));
            return e1.getMessage();
        } finally {
            if (m != null) {
                ConnPollManager.getInstance().releaseConnect(m);
            }
            m = null;
        }
        return null;
    }

    /**
     * 根据文件名，删除文件
     *
     * @param filename
     *            - 文件名
     */
    public static String deleteBinaryFile(String filename) {
        String[] fnames = filename.split(":");
        Mongo m = ConnPollManager.getInstance().getConnect();
        if (m == null) {
            return "connect db is failure.";
        }
        try {
            GridFS fs = new GridFS(m.getDB(Constants.FILEDB));
            for (int i = 0; i < fnames.length; i++) {
                OriLog.getLogger(FileManager.class).debug("Delete file (" + fnames[i] + ")....");
                fs.remove(fnames[i]);
            }
        } catch (Exception e1) {
            OriLog.getLogger(FileManager.class).error(OriLog.logStack(e1));
            return e1.getMessage();
        } finally {
            if (m != null) {
                ConnPollManager.getInstance().releaseConnect(m);
            }
            m = null;
        }
        return null;
    }

    /**
     * 根据文件表信息，获取数据流并保存到文件交换上下文
     *
     * @param filelist
     *            - 二进制列表信息
     */
    public static void fetchBinaryFile(List filelist) {
        if (filelist.size() > 0) {
            //获取二进制数据，并保存到 Context 中
            FileExchange _wrap = FileExchange.getInstance();
            for (int i = 0; i < filelist.size(); i++) {
                String fname = (String) filelist.get(i);
                StreamData in = fetchBinaryFile(fname);
                if (in == null) {
                    continue;
                }
                _wrap.addContextVariable(fname, in);
            }
        }
        return;
    }

    /**
     * 根据文件名，获取数据流对象
     *
     * @param filename
     *            - 文件名
     * @return StreamData
     * 		- 数据流对象
     */
    public static StreamData fetchBinaryFile(String filename) {
        OriLog.getLogger(FileManager.class).debug("Getfile  (" + filename + ")....");
        StreamData in = null;
        Mongo m = ConnPollManager.getInstance().getConnect();
        if (m == null) {
            return null;
        }
        try {
            GridFS fs = new GridFS(m.getDB(Constants.FILEDB));
            String[] fnames = filename.split(":");
            InputStream[] ins = new InputStream[fnames.length];
            for (int i = 0; i < fnames.length; i++) {
                List<GridFSDBFile> mongofiles = fs.find(fnames[i]);
                if (mongofiles.size() > 0) {
                    GridFSDBFile mongofile = mongofiles.get(0);
                    ins[i] = mongofile.getInputStream();
                } else {
                    OriLog.getLogger(FileManager.class).error("not find file(" + fnames[i] + ") from db!");
                    ins[i] = null;
                }
            }
            in = new StreamData(null, ins);
        } catch (Exception e1) {
            OriLog.getLogger(FileManager.class).error(OriLog.logStack(e1));
        } finally {
            if (m != null) {
                ConnPollManager.getInstance().releaseConnect(m);
            }
            m = null;
        }
        return in;
    }
}
