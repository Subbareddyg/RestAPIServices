package com.belk.api.logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * This class invokes the instance for GenericLogger.
 * 
 * @author Mindtree
 * @date Jul 23, 2013
 */
public final class LoggerInvoker {
	/**
	 * creating an instance of LoggerInvoker.
	 */
	private static LoggerInvoker loggerInvoker;

	/**
	 * creating an instance of GenericLogger.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	/**
	 * creating an logger class instance.
	 */
	private Class<?> loggerClass = GenericLoggerImpl.class;

	/**
	 * declaring correlationId.
	 */
	private final String correlationId = "";

	/**
	 * Default constructor for the class.
	 */
	private LoggerInvoker() {
	}

	/**
	 * Constructor for invoking a class specific logger.
	 * 
	 * @param logger
	 *            Name of the class for which the logger is being invoked.
	 */
	public LoggerInvoker(final String logger) {
		try {
			this.loggerClass = Class.forName(logger);
		} catch (ClassNotFoundException e) {
			LOGGER.error(e, this.correlationId);

		}
	}

	/**
	 * Method to ensure singleton.
	 * 
	 * @return An instance of the logger invoker.
	 */
	public static synchronized LoggerInvoker getInstance() {
		if (loggerInvoker == null) {
			loggerInvoker = new LoggerInvoker();
		}
		return loggerInvoker;
	}

	/**
	 * Method to ensure singleton.
	 * 
	 * @param logger
	 *            Name of the class for which the logger is being invoked.
	 * @return An instance of the logger invoker.
	 */
	public static synchronized LoggerInvoker getInstance(final String logger) {
		if (loggerInvoker == null) {
			loggerInvoker = new LoggerInvoker(logger);
		}
		return loggerInvoker;
	}

	/**
	 * Method to return an instance of the logger.
	 * 
	 * @param loggerName
	 *            Name of the class for which the logger is being invoked.
	 * @return An instance of the generic logger.
	 */
	public GenericLoggerImpl getLogger(final String loggerName) {
		GenericLoggerImpl genericLogger = null;
		Constructor<?> constructor;
		try {
			constructor = this.loggerClass
					.getConstructor(new Class[] {String.class });
			genericLogger = (GenericLoggerImpl) constructor
					.newInstance(new Object[] {loggerName });
		} catch (SecurityException e) {
			LOGGER.error(e, this.correlationId);
		} catch (NoSuchMethodException e) {
			LOGGER.error(e, this.correlationId);
		} catch (IllegalArgumentException e) {
			LOGGER.error(e, this.correlationId);
		} catch (InstantiationException e) {
			LOGGER.error(e, this.correlationId);
		} catch (IllegalAccessException e) {
			LOGGER.error(e, this.correlationId);
		} catch (InvocationTargetException e) {
			LOGGER.error(e, this.correlationId);
		}

		return genericLogger;
	}
}