package com.original.service.storage;

import com.original.util.log.OriLog;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataSource;
import org.apache.log4j.Logger;

/**
 *  流数据对象。
 *
 * @author   cydow
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-2-13 8:17:13
 */
public class StreamData implements DataSource {

    Logger log = OriLog.getLogger(StreamData.class);
    private String _filename;
    private byte[] data = new byte[0];
    private InputStream in = null;

    /**
     * 构造函数，并指定输入流和文件名
     * @param _in 
     */
    public StreamData(InputStream _in, String filename) {
        _filename = filename;
        in = _in;
        if (in == null && filename != null && filename.length() > 0) {
            try {
                in = new FileInputStream(_filename);
            } catch (Exception e) {
                log.error(OriLog.logStack(e));
            }
        }
        //        this.getData();
    }

    /**
     * 构造函数，并指定输入流和文件名
     * @param filename  - 文件名
     * @param _ins
     */
    public StreamData(String filename, InputStream[] _ins) {
        _filename = filename;
        if (_ins != null) {
            ByteArrayOutputStream fout = null;
            try {
                fout = new ByteArrayOutputStream();
                int totalbytes = 0;
                for (int i = 0; i < _ins.length; i++) {
                    if (_ins[i] != null) {
                        totalbytes += writeOutputStream(fout, _ins[i]);
                    }
                }
                log.debug("read total bytes of the complex data is " + totalbytes);
                data = fout.toByteArray();
                fout.close();
            } catch (Exception ex) {
                log.error(OriLog.logStack(ex));
            } finally {
                try {
                    fout.close();
                } catch (Exception ex) {
                    log.error(OriLog.logStack(ex));
                }
            }
        }
    }

    /**
     * 构造函数，打开指定文件名
     * @param filename  - 文件名
     */
    public StreamData(String filename) {
        this(null, filename);
    }

    /**设置流对象对应的文件名
     *
     * @param filename
     */
    public void setFilename(String filename) {
        this._filename = filename;
    }

    /**获取流对象对应的文件名
     *
     * @return 
     */
    public String getFilename() {
        return this._filename;
    }

    /**
     * 获取输入流对象
     * @return
     */
    public InputStream getInputStream() {
        return new ByteArrayInputStream(getData());
//        return in;
    }

    /**
     * 读输入流对象的所有信息
     * @return
     */
    public byte[] getData() {
        if (data.length > 0) {
            return data;
        }
        try {
            ByteArrayOutputStream fout = new ByteArrayOutputStream();
            writeOutputStream(fout, in);
            fout.close();
            in.close();
            in = null;
            data = fout.toByteArray();
            log.debug("read total bytes of the complex data is " + data.length);
        } catch (IOException ex) {
            log.error(OriLog.logStack(ex));
        }

        return data;
    }

    private int writeOutputStream(OutputStream fout, InputStream fin) throws IOException {
        int totalbytes = 0;
        int ch;
        while ((ch = fin.read()) != -1) {
            fout.write(ch);
            totalbytes++;
        }
        fin.close();
        return totalbytes;
    }

    /**
     * 流对象的所有信息
     * @param  filename
     * @throws IOException
     */
    public void writeToFile(String filename) throws IOException {
        this.getData();
        File f = new File(filename);
        File path = new File(new File(f.getAbsolutePath()).getParent());
        if (!(path.exists() && path.isDirectory())) {
            path.mkdirs();
        }
        FileOutputStream fout = new FileOutputStream(filename);
        fout.write(data);
        fout.close();
        log.debug("write total bytes of the complex data is " + data.length);
    }

    public OutputStream getOutputStream() throws IOException {
        return null;
    }

    public String getContentType() {
        return "application/octet-stream;";
    }

    public String getName() {
        File f = new File(_filename);
        return f.getName();
    }
}
