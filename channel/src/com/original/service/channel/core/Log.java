package com.original.service.channel.core;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {
	
	static Logger log = Logger.getLogger(Log.class.getName());
	
    public static void logDebug(String message) {
			log.log(Level.ALL, message);
	}

	public static void logInfo(String message) {
		log.log(Level.INFO, message);
	}


}
