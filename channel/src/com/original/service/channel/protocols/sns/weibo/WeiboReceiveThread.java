/**
 * com.original.app.email.ui.actio.ReceiveEmailThread.java
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel.protocols.sns.weibo;

/**
 * (Class Annotation.)
 *
 * @author   Admin
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-3-3 15:56:48
 */
public class WeiboReceiveThread extends Thread {
    WeiboReceiver receiver;

	/**
	 * 
	 * @param _frame
	 */
	public WeiboReceiveThread(WeiboReceiver receiver) {
		setName("ReceiveWeiboThread");
		setDaemon(true);
		setPriority(Thread.MIN_PRIORITY);
		this.receiver = receiver;
	}

    @Override
    public void run() {

    	//every 5min to receive weibo.
        while (true) {
            try {
                synchronized (receiver) {
                	receiver.receive();
                	receiver.wait(5 * 60 * 1000);
                }
            } catch (InterruptedException ex) {
                break;
            }
        }
    }	
}