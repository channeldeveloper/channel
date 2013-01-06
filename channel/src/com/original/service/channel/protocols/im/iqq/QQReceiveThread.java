package com.original.service.channel.protocols.im.iqq;


/**
 * (Class Annotation.)
 *
 * @author   Admin
 * @encoding UTF-8
 * @version  1.0
 * @create   2012-3-3 15:56:48
 */
public class QQReceiveThread extends Thread {

    QQReceiver receiver;

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
    	
    }	
}
