package client.rapid.util;

import org.apache.logging.log4j.LogManager;

import client.rapid.Client;

public class Logger {
	private static final org.apache.logging.log4j.Logger log = LogManager.getLogger();
	private static final String prefix = "[" + Client.getInstance().getName() + "] ";
	
	public static void info(Object object) {
		log.info(prefix + object);
	}
	
	public static void warn(Object object) {
		log.warn(prefix + object);
	}
	
	public static void error(Object object) {
		log.error(prefix + object);
	}
}
