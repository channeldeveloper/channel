package com.original.service.storage;

import com.mongodb.Mongo;

public class ConnPollManager {
	
	static ConnPollManager connpoll = null;
	
	/**
     * 获取数据库连接池对象实例
     * @return
     */
    public static ConnPollManager getInstance() {
        if (connpoll == null) {
            connpoll = new ConnPollManager();
        }
        return connpoll;
    }

    /**
     * 构造函数
     */
    public ConnPollManager() {
    }

    
    /**
     * 获取数据库连接对象
     * @return
     */
    @Deprecated
    public synchronized Mongo getConnect() {
    	return null;
    }
    
    /**
     *  释放数据库连接对象
     * @param m
     */
    @Deprecated
    public synchronized void releaseConnect(Mongo m) {
    }

}
