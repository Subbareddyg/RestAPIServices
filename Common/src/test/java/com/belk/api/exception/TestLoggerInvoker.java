package com.belk.api.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.belk.api.constants.ErrorConstants;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.GenericLoggerImpl;
import com.belk.api.logger.LoggerInvoker;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;

/**
 * Unit Testing related to AdapterException class is performed. <br />
 * {@link AdapterException} class is written for testing methods in The unit
 * test cases evaluates the way the methods behave for the inputs given.
 * 
 * @author Mindtree
 * 
 */
public class TestLoggerInvoker {
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}
	AdapterException adapterException;
	String expectedResult;
	String actualResults;
	String correlationId = "1234567891234567";
	String logger = "com.belk.api.adapter.contract.Adapter";

	/**
	 * Test method for {@link com.belk.api.logger.LoggerInvoker#getInstance()}.
	 */
	@Test
	public final void testLoggerInvokerGetInstanceForNull() {

		final LoggerInvoker invoker = LoggerInvoker.getInstance(this.logger);
		assertNotNull(invoker);

	}

	/**
	 * Test method for {@link com.belk.api.logger.LoggerInvoker#getInstance()}.
	 */
	@Test
	public final void testLoggerInvokerGetInstance() {

		final LoggerInvoker invoker = LoggerInvoker.getInstance(this.logger);
		assertNotNull(invoker);

	}

	/**
	 * Test method for {@link com.belk.api.logger.LoggerInvoker#getErrorDesc()}.
	 * The junit test case for AdapterException with code and description Junit
	 * testcase to test based on description.
	 */
	@Test
	public final void adapterException() {
		final String errorDesc = ErrorConstants.ERRORDESC_INVALID_URL_PARAMETERS;
		this.adapterException = new AdapterException(
				ErrorConstants.HTTP_STATUS_CODE_INVALID_URL_PARAMETERS,
				ErrorConstants.ERRORDESC_INVALID_URL_PARAMETERS,
				ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
				this.correlationId);
		this.expectedResult = errorDesc;
		this.actualResults = this.adapterException.getErrorDesc();
		assertNotNull(this.actualResults);
		assertEquals(this.expectedResult, this.actualResults);

	}

	/**
	 * Test method for {@link com.belk.api.logger.LoggerInvoker#getErrorCode()}.
	 * The junit test case for AdapterException with code and description Junit
	 * testcase to test based on errorId.
	 */
	@Test
	public final void adapterExceptionErrorId() {
		final String expectedResult = ErrorConstants.HTTP_STATUS_CODE_INVALID_URL_PARAMETERS;
		this.adapterException = new AdapterException(
				ErrorConstants.HTTP_STATUS_CODE_INVALID_URL_PARAMETERS,
				ErrorConstants.ERRORDESC_INVALID_URL_PARAMETERS,
				ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
				this.correlationId);

		this.actualResults = this.adapterException.getErrorCode();
		assertNotNull(this.actualResults);
		assertEquals(expectedResult, this.actualResults);
	}

	/**
	 * The junit test case for ClassNotFoundException with code and description.
	 * 
	 * @throws ClassNotFoundException
	 *             ClassNotFoundException
	 */
	@Test
	// (expected = ClassNotFoundException.class)
	public final void testLoggerInvoker() throws ClassNotFoundException {

		Class.forName(this.logger);
		final LoggerInvoker loggerInvoker = new LoggerInvoker(this.logger);
		assertNotNull(loggerInvoker);

	}

	/**
	 * The junit test case for ClassNotFoundException with code and description.
	 * 
	 * @throws ClassNotFoundException
	 *             ClassNotFoundException
	 * 
	 */
	@Test(expected = ClassNotFoundException.class)
	public final void testLoggerInvokerForException()
			throws ClassNotFoundException {
		final String className = "Adapter";
		Class.forName(className);
		new LoggerInvoker(className);

	}

	/**
	 * Test method for
	 * {@link com.belk.api.logger.LoggerInvoker#getSourceClass()}.
	 */

	@Test
	public final void testGetSourceClass() {

		final String result = LoggingHelper.getSourceClass();
		assertNotNull(result);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.logger.LoggerInvoker#getSourceMethod()}.
	 */

	@Test
	public final void testGetSourceMethod() {

		final String result = LoggingHelper.getSourceMethod();
		assertNotNull(result);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.logger.LoggerInvoker#getFormattedSource()}.
	 */
	@Test
	public final void testGetFormattedSource() {

		final String result = LoggingHelper.getFormattedSource();
		assertNotNull(result);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.logger.LoggerInvoker#getFormattedSource()}.
	 */
	@Test
	public final void testGetFormattedExceptionSource() {

		final String result = LoggingHelper.getFormattedExceptionSource();

		assertNotNull(result);
	}

	/**
	 * Test method for {@link com.belk.api.logger.LoggerInvoker#getLogger()}.
	 * Tests the method getLogger for NoSuchMethodException
	 */
	@Test(expected = NullPointerException.class)
	public final void testGetLoggerForNoSuchMethodException() {
		final LoggerInvoker loggerInvoker = new LoggerInvoker(this.logger);
		GenericLoggerImpl genericLogger = new GenericLoggerImpl(this.logger);
		genericLogger = loggerInvoker.getLogger(this.logger);
		assertNotNull(genericLogger);
	}

}
