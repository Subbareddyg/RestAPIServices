package com.belk.api.logger;

/**
 * Helper class for logging.
 * 
 * @author Mindtree
 * @date Jul 23, 2013
 */
public final class LoggingHelper {
	

	/**
	 * Default Constructor.
	 */
	private LoggingHelper() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Method to get a logger instance for a particular class.
	 * 
	 * @param loggerName
	 *            Name of the class for which the logger is being invoked.
	 * @return Instance of a logger.
	 */
	public static GenericLogger getLogger(final String loggerName) {
		return LoggerInvoker.getInstance().getLogger(loggerName);
	}

	/**
	 * Method to get a formatted output of the stack trace element.
	 * 
	 * @return A formatted log string.
	 */
	public static String getFormattedSource() {
		final StackTraceElement traceElement = java.lang.Thread.currentThread()
				.getStackTrace()[3];
		return " Class: " + traceElement.getClassName() + ", Method is: "
				+ traceElement.getMethodName() + ",Line No :"
				+ traceElement.getLineNumber();
	}

	/**
	 * Method to get a formatted output of the stack trace element.
	 * 
	 * @return A formatted log string.
	 */
	public static String getFormattedExceptionSource() {
		final StackTraceElement traceElement = java.lang.Thread.currentThread()
				.getStackTrace()[3];
		return "Exception occurred in Class: " + traceElement.getClassName()
				+ ", Method: " + traceElement.getMethodName()
				+ ", Line Number: " + traceElement.getLineNumber();
	}

	/**
	 * Method to get the name of the class from which the exception occurred.
	 * 
	 * @return The class name from which the exception occurred.
	 */
	public static String getSourceClass() {
		return java.lang.Thread.currentThread().getStackTrace()[3]
				.getClassName();
	}

	/**
	 * Method to get the name of the method from which the exception occurred.
	 * 
	 * @return The method name from which the exception occurred.
	 */
	public static String getSourceMethod() {
		return java.lang.Thread.currentThread().getStackTrace()[3]
				.getMethodName();
	}

}