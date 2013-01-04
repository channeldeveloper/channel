/**
 * com.original.app.email.ui.actio.ReceiveEmailThread.java
 * 
 * Copyright (c) 2012, Original and/or its affiliates. All rights reserved.
 * ORIGINAL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.original.service.channel.protocols.email.services;

import org.apache.log4j.Logger;

import com.original.util.log.OriLog;

/**
 * (Class Annotation.)
 *
 * @author   Admin
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-3-3 15:56:48
 */
public class EmailReceiveThread extends Thread {

	private Logger log = OriLog.getLogger(EmailReceiveThread.class);
    EmailReceiver receiver;
    private boolean sucessed;

	/**
	 * 
	 * @param _frame
	 */
	public EmailReceiveThread(EmailReceiver receiver) {
		setName("ReceiveEmailThread");
		setDaemon(true);
		setPriority(Thread.MIN_PRIORITY);
		this.receiver = receiver;
	}

    @Override
    public void run() {

    	//every 3000 second to receive email.
        while (true) {
            try {
                synchronized (receiver) {
                	receiver.wait(1000);
                }
            } catch (InterruptedException ex) {
                break;
            }
            //if failed try 3 time, then stop until the user reset this setting.
            sucessed = receiver.receive();

        }
    }
    
    

	
}