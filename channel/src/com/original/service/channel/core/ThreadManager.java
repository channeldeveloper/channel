/**
 * @className
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel.core;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import com.original.service.channel.ChannelMessage;

/**
 * 这里定义了一个Thread管理器，进行统一的后台任务管理。
 * 网络任务一般异步提交任务。
 * 服务初始化、
 * 信息发送
 * 信息的获取
 * 信息的解析
 * 联系人的获取
 * 自己的profile的获取 
 * 
 * @author sxy
 * 
 */
public class ThreadManager {
	
	private static ExecutorService threadService;
	private static boolean running = true;
	
	  /**
     * 获取默认的线程执行服务
     *
     * @return 默认的线程执行服务 - Executors.newCachedThreadPool(getThreadFactory())
     */
    private static synchronized ExecutorService getExecutorService() {
    	if (threadService == null)
    	{
    		threadService =  Executors.newFixedThreadPool(10, new ChannelThreadFactory());
    	}
    	return threadService;
    }
    
	/**
	 * Thread 工程方法，默认级别最低。同时是一个Demon
	 * @author sxy
	 *
	 */
	private static class ChannelThreadFactory implements ThreadFactory {
		int count = 0;
		String name = "Channel Service Thread ";
	    public Thread newThread(Runnable r) {
	        Thread thread = new Thread(r);	     
	        thread.setName(name + count);
	        count++;
	        thread.setPriority(Thread.MIN_PRIORITY);
	        return thread;
	    }
	}
	
	
	 /**
     * 停止服务
     */
    public static synchronized void shutdown() {
        if (running) {
            try {
                getExecutorService().shutdown();
                threadService = null;
            } finally {              
                running = false;
            }
        }
    }

    /**
     * 执行并提交一个线程任务
     *
     * @param r 要运行的线程 线程执行类
     * @return f 该线程的Future返回
     */
   public static synchronized Future<?> submit(Callable call) {
        //getThreadFactory().newThread(r).start();
		Future<ChannelMessage> f = null;
		try {
			f = getExecutorService().submit(call);
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			running = true;
		}
		return f;
        
    }

}
