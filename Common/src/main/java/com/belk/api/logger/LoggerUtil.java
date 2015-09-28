package com.belk.api.logger;

/**
 * Utility class having the reference of the logger object.
 * 
 * Update: This class has been updated to handle single log file per service, as
 * part of CR: CR_BELK_API_6
 * 
 * @author Mindtree
 * @date Nov 20 2013
 */
public final class LoggerUtil {
	/**
	 * creating an instance of logger.
	 */
	public static GenericLogger logger;

	/**
	 * Default Constructor.
	 */
	private LoggerUtil() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the logger
	 */
	public static GenericLogger getLogger() {
		return logger;
	}

	/**
	 * @param logger
	 *            the logger to set
	 */
	public static void setLogger(final GenericLogger logger) {
		LoggerUtil.logger = logger;
	}

}
