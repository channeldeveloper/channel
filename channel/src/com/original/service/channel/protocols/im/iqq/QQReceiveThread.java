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
    volatile boolean isRun = true;

	/**
	 * 
	 * @param _frame
	 */
	public QQReceiveThread(QQReceiver receiver) {
		setName("ReceiveQQThread");
		setDaemon(true);
		setPriority(Thread.MIN_PRIORITY);
		this.receiver = receiver;
	}

    @Override
    public void run() {
    	while (isRun) {
            try {
                synchronized (receiver) {
                	receiver.receive();
                	receiver.wait(((int)(Math.random()*3) + 2)*1000);
                }
            } catch (InterruptedException ex) {
                break;
            }
        }
    }

	public boolean isRun() {
		return isRun;
	}

	public void setRun(boolean isRun) {
		this.isRun = isRun;
	}	
    
    
}
