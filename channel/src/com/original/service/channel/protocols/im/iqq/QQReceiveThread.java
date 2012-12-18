package com.original.service.channel.protocols.im.iqq;

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
public class QQReceiveThread extends Thread {

	private Logger log = OriLog.getLogger(QQReceiveThread.class);
    QQReceiver receiver;
    private boolean sucessed;

	/**
	 * 
	 * @param _frame
	 */
	public QQReceiveThread(QQReceiver receiver) {
		setName("ReceiveEmailThread");
		setDaemon(true);
		setPriority(Thread.MIN_PRIORITY);
		this.receiver = receiver;
	}

    @Override
    public void run() {

    	//every 10 *1000 second to receive email.
        while (true) {
            try {
                synchronized (receiver) {
                	receiver.receive();
                	receiver.wait(2 * 1000);
                }
            } catch (InterruptedException ex) {
                break;
            }
        }
    }	
}
