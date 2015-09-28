package com.belk.api.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.belk.api.constants.ErrorConstants;
import com.belk.api.error.handler.ResponseDetails;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;

/**
 * Unit Testing related to Error class is performed. <br />
 * {@link TestResponseDetails} class is written for testing methods in The unit test cases
 * evaluates the way the methods behave for the inputs given.
 * 
 * @author Mindtree
 * 
 */
public class TestResponseDetails {
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}
	ResponseDetails error;
	String errDesc = ErrorConstants.ERRORDESC_INTERNAL_ERROR;
	String expectedResult;
	String actualResults;
	String correlationId = "1234567891234567";

	/**
	 * The junit test case for ErrorException with code, description, field
	 * parameters and parametervalues Junit testcase to test based on
	 * description.
	 */
	@Test
	public final void testErrorDescription() {
		this.error = new ResponseDetails("500", ErrorConstants.ERRORDESC_INTERNAL_ERROR,
				"errorFieldParameter", "errorParameterValue");
		this.expectedResult = this.errDesc;
		this.actualResults = this.error.getDescription();
		assertNotNull(this.actualResults);
		assertEquals(this.expectedResult, this.actualResults);
	}

	/**
	 * The junit test case for ErrorException description Junit testcase to test
	 * based on description.
	 */
	@Test
	public final void testErrorDesc() {
		this.error = new ResponseDetails("500", null, "errorFieldParameter",
				"errorParameterValue");
		this.error.setDescription(ErrorConstants.ERRORDESC_INTERNAL_ERROR);
		this.expectedResult = this.errDesc;
		this.actualResults = this.error.getDescription();
		assertNotNull(this.actualResults);
		assertEquals(this.expectedResult, this.actualResults);
	}


	/**
	 * The junit test case for ErrorException with code Junit testcase to test
	 * based on code.
	 */
	@Test
	public final void testErrorCode() {
		this.error = new ResponseDetails();
		this.error.setCode("500");
		this.expectedResult = "500";
		this.actualResults = this.error.getCode();
		assertNotNull(this.actualResults);
		assertEquals(this.expectedResult, this.actualResults);
	}

	/**
	 * The junit test case for ErrorException with FieldParameter Junit testcase
	 * to test based on FieldParameter.
	 */
	@Test
	public final void testErrorFieldParameter() {
		this.error = new ResponseDetails();
		this.error.setParametername("errorFieldParameter");
		this.expectedResult = "errorFieldParameter";
		this.actualResults = this.error.getParametername();
		assertNotNull(this.actualResults);
		assertEquals(this.expectedResult, this.actualResults);
	}

	/**
	 * The junit test case for ErrorException with ParameterValue Junit testcase
	 * to test based on ParameterValue.
	 */
	@Test
	public final void testErrorParameterValue() {
		this.error = new ResponseDetails();
		this.error.setParametervalue("errorParameterValue");
		this.expectedResult = "errorParameterValue";
		this.actualResults = this.error.getParametervalue();
		assertNotNull(this.actualResults);
		assertEquals(this.expectedResult, this.actualResults);
	}

	/**
	 * The junit test case for ErrorException with HttpStatus Junit testcase to
	 * test based on HttpStatus.
	 */

	@Test
	public final void testHttpStatus() {
		this.error = new ResponseDetails();
		this.error.setHttpStatus("500");
		this.expectedResult = "500";
		this.actualResults = this.error.getHttpStatus();
		assertNotNull(this.actualResults);
		assertEquals(this.expectedResult, this.actualResults);
	}

}
