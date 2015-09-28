package com.belk.api.logger;

import com.belk.api.constants.CommonConstants;

/**
 * Custom generic logging interface for the application.
 * 
 * Update: correlationId has been added to all the methods as part of CR:
 * 
 * April 14 Release Phase 1 for Enterprise Logger Framework Integration 
 * CorrelationId is used to track the logs at an enterprise
 * level.
 * 
 * @author Mindtree
 * @date Jul 23, 2013
 */
public abstract interface GenericLogger {
	/**
	 * declaring ENTERING String.
	 */
	String ENTERING = CommonConstants.LOGGER_ENTERING;
	/**
	 * declaring EXITING String.
	 */
	String EXITING = CommonConstants.LOGGER_EXITING;

	/**
	 * Marks the entry into a debug method.
	 * 
	 * @param correlationId
	 *            - tracking Id to be logged
	 * @return start time in milliseconds
	 */
	long logMethodEntry(String correlationId);

	/**
	 * Method to be used for logging level as debug.
	 * 
	 * @param paramString
	 *            message to be logged
	 * @param correlationId
	 *            - tracking Id to be logged
	 */
	void debug(String paramString, String correlationId);

	/**
	 * Method to be used for logging level as info.
	 * 
	 * @param paramString
	 *            message to be logged
	 * @param correlationId
	 *            - tracking Id to be logged
	 */
	void info(String paramString, String correlationId);

	/**
	 * Method to be used for logging level as warn.
	 * 
	 * @param paramString
	 *            message to be logged
	 * @param correlationId
	 *            - tracking Id to be logged
	 */
	void warn(String paramString, String correlationId);


	/**
	 * Method to be used for logging level as error only in Api Services Log
	 * 
	 * Resulting ErrorMessage goes only to ApiServices Local Logs
	 * 
	 * @param error
	 *            object to be thrown to the calling method
	 * @param correlationId
	 *            - tracking Id to be logged
	 */
	void error(Throwable error, String correlationId);

	/**
	 * Method to be used for logging level as error
	 * 
	 * in Api Services Logs and also ELF Logs
	 * 
	 * @param apiErrorMessage
	 *            message to be logged
	 * @param error
	 *            object to be thrown to the calling method
	 * @param correlationId
	 *            - tracking Id to be logged
	 */
	void error(String apiErrorMessage, Exception error, String correlationId);

	/**
	 * Method to be used for logging level as fatal.
	 * 
	 * @param paramString
	 *            message to be logged
	 */
	void fatal(String paramString);

	/**
	 * Marks the exit point from a debug method.
	 * 
	 * @param startTime
	 *            start time of method entry
	 * @param correlationId
	 *            - tracking Id to be logged
	 */
	void logMethodExit(long startTime, String correlationId);

	/**
	 * @return status if logging is enabled at debug level
	 */
	boolean isDebugEnabled();

	/**
	 * @return status if logging is enabled at error level
	 */
	boolean isErrorEnabled();

	/**
	 * @return status if logging is enabled at fatal level
	 */
	boolean isFatalEnabled();

	/**
	 * @return status if logging is enabled at info level
	 */
	boolean isInfoEnabled();

	/**
	 * @return status if logging is enabled at warn level
	 */
	boolean isWarnEnabled();
	
	
	/**
	 * This method is used to set the default logger level at run time.
	 * 
	 * @param logLevel
	 *            - level to be set for logger.
	 */
	void setLoggerLevel(String logLevel);
}