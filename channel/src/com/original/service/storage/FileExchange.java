package com.original.service.storage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.original.util.log.OriLog;

/**
 * 上下文文件交换对象
 *
 * @author   cydow
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-2-13 8:17:13
 */
public class FileExchange {

    static FileExchange wrap = null;
    Logger log = OriLog.getLogger(FileExchange.class);
    Map<String, StreamData> _context = new HashMap<String, StreamData>();

    /**
     * 获取文件交换对象实例
     * @return
     */
    public static FileExchange getInstance() {
        if (wrap == null) {
            wrap = new FileExchange();
        }
        return wrap;
    }

    /**
     * 添加上下文文件交换数据
     * @param identifier
     * @param streamdata
     */
    public void addContextVariable(String identifier, StreamData streamdata) {
        if (_context.containsKey(identifier)) {
//            StreamData data = _context.get(identifier);
//            data.close();
            _context.remove(identifier);
        }
        if ( streamdata == null ) return;
        if (streamdata.getFilename() == null || streamdata.getFilename().length() == 0 ) streamdata.setFilename(identifier);
        _context.put(identifier, streamdata);
    }

    /**
     * 获取上下文的流数据
     * @param identifier
     * @return
     */
    public StreamData getContextVariable(String identifier) {
        return this.getContextVariable(identifier, false);
    }
    
    /**
     * 获取上下文的流数据
     * @param identifier
     * @param isRemove
     * @return
     */
    public StreamData getContextVariable(String identifier, boolean isRemove) {
        if (_context.containsKey(identifier)) {
            StreamData data = _context.get(identifier);
            if (isRemove) {
                _context.remove(identifier);
            }
            return data;
        } else {
            return null;
        }
    }

    /**
     * 获取上下文文件交换缓存
     * @return
     */
    public Map getContext() {
        return _context;
    }

    /**
     *  清除上下文文件交换缓存
     */
    public void clearContext() {
        if (_context != null) {
            int size = _context.size();
            Iterator<Map.Entry<String, StreamData>> valset = (size > 0) ? _context.entrySet().iterator()
                    : null;
            while (valset != null && valset.hasNext()) {
                Map.Entry<String, StreamData> entry = valset.next();
//                entry.getValue().close();
            }
            _context.clear();
        }
    }

    /**
     * 一般函数
     * 为示例起见，这里将输入的param和设置变量中的内容进行
     * 一个简单运算:根据param来确定选取那个Binary对象来处理。
     * @param param
     * @param filename
     * @throws IOException
     */
    public void processing(String param, String filename) throws IOException {
        StreamData streamdata = getContextVariable(param);
        if (streamdata != null) {
            streamdata.writeToFile(filename);
        } else {
            log.debug("no relevant data exists");
        }
    }
}