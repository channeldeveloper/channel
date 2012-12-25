package com.original.service.channel.protocols.email.services;
import java.util.Enumeration;

import javax.mail.Address;
import javax.mail.Header;
import javax.mail.event.ConnectionEvent;
import javax.mail.event.ConnectionListener;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;
import javax.mail.internet.MimeMessage;

public class MailServerMonitor implements TransportListener, ConnectionListener {

	/**
	 *
	 */
	public MailServerMonitor() {
		super();
	}



	public void opened(ConnectionEvent e) {
		System.out.println("Connection opened");
	}

	public void disconnected(ConnectionEvent e) {
		System.out.println("Connection disconnected");
	}

	public void closed(ConnectionEvent e) {
		System.out.println("Connection closed");
	}

	public void messageDelivered(TransportEvent e) {
		System.out.println("Message delivered for:");
		MimeMessage mime = (MimeMessage)(e.getMessage());
		
		try
		{
			String msgID = mime.getMessageID();
			Enumeration headers = mime.getAllHeaders();
			
			System.out.println(mime.getHeader("cydow_id"));
			while (headers.hasMoreElements()) {
				Header h = (Header) headers.nextElement();
				System.out.println(h.getName() + ":" + h.getValue());
			}
	            
		}
		catch(Exception exp)
		{
			exp.printStackTrace();
		}
		if (e != null) {
			Address[] a = e.getValidSentAddresses();
			if (a != null && a.length > 0) {
				for (int i = 0; i < a.length; i++) {
					System.out.println(a);
				}
			}
			System.out.println("");
		}
	}

	public void messageNotDelivered(TransportEvent e) {
		System.out.println("Message not delivered for:");
		if (e != null) {
			Address[] a = e.getValidUnsentAddresses();
			if (a != null && a.length > 0) {
				for (int i = 0; i < a.length; i++) {
					System.out.println(a);
				}
			}
			System.out.println("");
		}
	}

	public void messagePartiallyDelivered(TransportEvent e) {
		System.out.println("These addresses are invalid:");
		if (e != null) {
			Address[] a = e.getInvalidAddresses();
			if (a != null && a.length > 0) {
				for (int i = 0; i < a.length; i++) {
					System.out.println(a);
				}
			}
			System.out.println("");
		}
	}

}