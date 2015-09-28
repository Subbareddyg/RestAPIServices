package com.belk.api.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;

/**
 * Unit Testing related to ServiceException class is performed. <br />
 * {@link ServiceException} class is written for testing methods in The unit
 * test cases evaluates the way the methods behave for the inputs given.
 * 
 * @author Mindtree
 * 
 */
public class TestServiceException {
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}
	ServiceException serviceExp;
	String expectedResult;
	String actualResults;
	String correlationId = "1234567891234567";

	/**
	 * The junit test case for ServiceException.
	 * 
	 */
	@Test
	public final void testServiceException() {
		assertEquals(null, new ServiceException("503", "Service unavailable",
				"500", "Value", "500", this.correlationId).getMessage());
	}

	/**
	 * The junit test case for ServiceException. Junit testcase to test based on
	 * code
	 */
	@Test
	public final void testServiceExpErrorCode() {
		this.serviceExp = new ServiceException("503", "Service unavailable",
				"500", "Value", this.correlationId);
		this.expectedResult = "503";
		this.actualResults = this.serviceExp.getErrorCode();
		assertNotNull(this.actualResults);
		assertEquals(this.expectedResult, this.actualResults);
	}

	/**
	 * The junit test case for ServiceException. Junit testcase to test based on
	 * code
	 */
	@Test
	public final void testServiceExp() {
		this.serviceExp = new ServiceException("503", "Service unavailable",
				"Value", this.correlationId);
		this.expectedResult = "503";
		this.actualResults = this.serviceExp.getErrorCode();
		assertNotNull(this.actualResults);
		assertEquals(this.expectedResult, this.actualResults);
	}

}
