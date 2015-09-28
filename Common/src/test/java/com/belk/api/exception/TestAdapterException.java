package com.belk.api.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.belk.api.constants.ErrorConstants;
import com.belk.api.logger.GenericLogger;
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
public class TestAdapterException {
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}
	AdapterException adapterException;
	String expectedResult;
	String actualResults;
	String correlationId = "1234567891234567";

	/**
	 * The junit test case for AdapterException with code and description Junit
	 * testcase to test based on description.
	 */
	@Test
	public final void adapterException() {
		final String errorDesc = ErrorConstants.ERRORDESC_INVALID_URL_PARAMETERS;
		this.adapterException = new AdapterException(
				ErrorConstants.HTTP_STATUS_CODE_INVALID_URL_PARAMETERS,
				ErrorConstants.ERRORDESC_INVALID_URL_PARAMETERS, "parameter",
				ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
				this.correlationId);
		this.expectedResult = errorDesc;
		this.actualResults = this.adapterException.getErrorDesc();
		assertEquals(this.expectedResult, this.actualResults);

	}

	/**
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
	 * The junit test case for AdapterException with code and description Junit
	 * testcase to test based on errorCode.
	 */
	@Test
	public final void testAdapterExceptionWithFiveArguments() {
		this.adapterException = new AdapterException(
				ErrorConstants.HTTP_STATUS_CODE_INVALID_URL_PARAMETERS,
				ErrorConstants.ERRORDESC_INVALID_URL_PARAMETERS, "Parameter",
				"500", this.correlationId);
		final String expectedResult = ErrorConstants.HTTP_STATUS_CODE_INVALID_URL_PARAMETERS;
		this.actualResults = this.adapterException.getErrorCode();
		assertNotNull(this.actualResults);
		assertEquals(expectedResult, this.actualResults);
	}

	/**
	 * The junit test case for AdapterException with code and description Junit
	 * testcase to test based on errorCode.
	 */
	@Test
	public final void testAdapterExceptionWithSixArguments() {
		this.adapterException = new AdapterException(
				ErrorConstants.HTTP_STATUS_CODE_INVALID_URL_PARAMETERS,
				ErrorConstants.ERRORDESC_INVALID_URL_PARAMETERS, "Parameter",
				"Value", "500", this.correlationId);
		final String expectedResult = ErrorConstants.HTTP_STATUS_CODE_INVALID_URL_PARAMETERS;
		this.actualResults = this.adapterException.getErrorCode();
		assertNotNull(this.actualResults);
		assertEquals(expectedResult, this.actualResults);
	}

}
