package com.belk.api.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.belk.api.constants.ErrorConstants;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;

/**
 * Unit Testing related to BaseException class is performed. <br />
 * {@link TestBaseException} class is written for testing methods in The unit
 * test cases evaluates the way the methods behave for the inputs given.
 * 
 * @author Mindtree
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ BaseException.class, Exception.class })
public class TestBaseException {
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}
	BaseException baseException;
	String errDesc = ErrorConstants.ERRORDESC_INTERNAL_ERROR;
	String expectedResult;
	String actualResults;
	String correlationId = "1234567891234567";
	String httpStatus = "invalid";
	String errorParameterValue = "5567";
	String errorFieldParameter = "500";

	/**
	 * The junit test case for BaseException with code and description Junit
	 * testcase to test based on description.
	 */
	@Test
	public final void testBaseExceptionErrorcode() {
		this.baseException = new BaseException("500", this.errDesc,
				this.errorFieldParameter, "Value", this.correlationId);
		this.expectedResult = this.errorFieldParameter;
		this.actualResults = this.baseException.getErrorFieldParameter();
		assertNotNull(this.actualResults);
		assertEquals(this.expectedResult, this.actualResults);

	}

	/**
	 * The junit test case for BaseException with code and description. Junit
	 * testcase to test based on code.
	 */
	@Test
	public final void testBaseExceptionErrorDesc() {
		this.baseException = new BaseException("500", this.errDesc, "5567",
				"Value", this.correlationId);
		this.expectedResult = this.errDesc;
		this.actualResults = this.baseException.getErrorDesc();
		assertNotNull(this.actualResults);
		assertEquals(this.expectedResult, this.actualResults);

	}

	/**
	 * The junit test case for BaseException with code. Junit testcase to test
	 * based on code.
	 */
	@Test
	public final void testBaseExceptionDesc() {
		this.baseException = new BaseException("500", this.errDesc,
				this.errorParameterValue, "5567", "Value", this.correlationId);
		this.baseException.setErrorFieldParameter(this.errorParameterValue);
		this.baseException.setErrorParameterValue(this.errorParameterValue);
		this.expectedResult = this.errorParameterValue;
		this.actualResults = this.baseException.getErrorParameterValue();
		assertNotNull(this.actualResults);
		assertEquals(this.expectedResult, this.actualResults);

	}

	/**
	 * The junit test case for BaseException with description. Junit testcase to
	 * test based on description.
	 */
	@Test
	public final void testBaseExceptiontwoAgruments() {
		this.baseException = new BaseException("500", this.errDesc);
		this.expectedResult = "500";
		this.actualResults = this.baseException.getErrorCode();
		assertNotNull(this.actualResults);
		assertEquals(this.expectedResult, this.actualResults);

	}

	/**
	 * The junit test case for BaseException with description. Junit testcase to
	 * test based on Exception message.
	 */

	@Test
	public final void testBaseExceptionWithException() {
		final Exception cause = PowerMockito.mock(Exception.class);
		this.baseException = new BaseException(cause, "500", this.errDesc);
		this.expectedResult = cause.getMessage();
		this.actualResults = this.baseException.getMessage();
		assertNull(this.actualResults);
		assertEquals(this.expectedResult, this.actualResults);

	}

	/**
	 * The junit test case for BaseException with description. Junit testcase to
	 * test based on Exception message.
	 */

	@Test
	public final void testBaseExceptionWithThreeArguments() {
		final Exception cause = PowerMockito.mock(Exception.class);
		this.baseException = new BaseException(cause, "500", this.errDesc);
		this.expectedResult = cause.getMessage();
		this.actualResults = this.baseException.getMessage();
		assertNull(this.actualResults);
		assertEquals(this.expectedResult, this.actualResults);

	}

	/**
	 * The junit test case for BaseException with description. Junit testcase to
	 * test based on Exception message.
	 */

	@Test
	public final void testBaseExceptionWithFourArguments() {
		this.baseException = new BaseException("500", this.errDesc,
				this.httpStatus, this.correlationId);
		this.baseException.setErrorDesc(this.errDesc);
		this.baseException.setErrorCode("500");
		this.baseException.setHttpStatus(this.httpStatus);
		this.expectedResult = this.httpStatus;
		this.actualResults = this.baseException.getHttpStatus();
		assertNotNull(this.actualResults);
		assertEquals(this.expectedResult, this.actualResults);
	}

	/**
	 * The junit test case for BaseException with description. Junit testcase to
	 * for BaseException.
	 */

	@Test
	public final void testBaseException() {
		this.baseException = new BaseException();
	}

	/**
	 * Test method for
	 * {@link com.belk.api.exception.BaseException#getCorrelationId()}.
	 */

	@Test
	public final void testGetCorrelationId() {
		final BaseException baseException = new BaseException();
		baseException.setCorrelationId("123456789");
		assertTrue(baseException.getCorrelationId() == "123456789");
	}

	/**
	 * Test method for
	 * {@link com.belk.api.exception.BaseException#setCorrelationId()}.
	 */

	@Test
	public final void testSetCorrelationId() {
		final BaseException baseException = new BaseException();
		baseException.setCorrelationId("123456789");
		assertTrue(baseException.getCorrelationId() == "123456789");
	}

}
