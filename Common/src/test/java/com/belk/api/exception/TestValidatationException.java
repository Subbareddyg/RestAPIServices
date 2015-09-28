package com.belk.api.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;

/**
 * Unit Testing related to ValidatationException class is performed. <br />
 * {@link ValidatationException} class is written for testing methods in The
 * unit test cases evaluates the way the methods behave for the inputs given.
 * 
 * @author Mindtree
 * 
 */
public class TestValidatationException {
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}
	ValidationException validationException;
	String expectedResults;
	String actualResults;
	String correlationId = "1234567891234567";

	/**
	 * The junit test case for ValidatationException with code and description
	 * Junit testcase to test based on description.
	 */
	@Test
	public final void validatationException() {
		final String errorDesc = "Bad Request:Request cannot be fulfilled because of incorrect url.";
		this.validationException = new ValidationException(
				"400",
				"Bad Request:Request cannot be fulfilled because of incorrect url.",
				"500", "Value", "500", this.correlationId);
		this.expectedResults = errorDesc;
		this.actualResults = this.validationException.getErrorDesc();
		assertNotNull(this.actualResults);
		assertEquals(this.expectedResults, this.actualResults);

	}

	/**
	 * The junit test case for ValidatationException with code and description
	 * Junit testcase to test based on errorId.
	 */
	@Test
	public final void validatationExceptionErrorId() {
		final String errorId = "400";
		this.validationException = new ValidationException(
				"400",
				"Bad Request:Request cannot be fulfilled because of incorrect url.",
				"500", "Value", "500", this.correlationId);
		this.expectedResults = errorId;
		this.actualResults = this.validationException.getErrorCode();
		assertNotNull(this.actualResults);
		assertEquals(this.expectedResults, this.actualResults);
	}

	/**
	 * The junit test case for ValidatationException with code and description
	 * Junit testcase to test based on errorId.
	 */
	@Test
	public final void validExpfiveArgumentsErrorId() {
		final String errorId = "400";
		this.validationException = new ValidationException(
				"400",
				"Bad Request:Request cannot be fulfilled because of incorrect url.",
				"500", "Value", this.correlationId);
		this.expectedResults = errorId;
		this.actualResults = this.validationException.getErrorCode();
		assertNotNull(this.actualResults);
		assertEquals(this.expectedResults, this.actualResults);
	}

	/**
	 * The junit test case for ValidatationException with code and description
	 * Junit testcase to test based on description.
	 */
	@Test
	public final void validExpfiveArgumentsErrorDesc() {
		final String errorDesc = "Bad Request:Request cannot be fulfilled because of incorrect url.";
		this.validationException = new ValidationException(
				"400",
				"Bad Request:Request cannot be fulfilled because of incorrect url.",
				"500", "Value", this.correlationId);
		this.expectedResults = errorDesc;
		this.actualResults = this.validationException.getErrorDesc();
		assertNotNull(this.actualResults);
		assertEquals(this.expectedResults, this.actualResults);

	}

	/**
	 * The junit test case for ValidatationException with code and description
	 * Junit testcase to test based on errorId.
	 */
	@Test
	public final void validExpfourArgumentsErrorId() {
		final String errorId = "400";
		this.validationException = new ValidationException(
				"400",
				"Bad Request:Request cannot be fulfilled because of incorrect url.",
				"500", this.correlationId);
		this.expectedResults = errorId;
		this.actualResults = this.validationException.getErrorCode();
		assertNotNull(this.actualResults);
		assertEquals(this.expectedResults, this.actualResults);
	}

	/**
	 * The junit test case for ValidatationException with code and description
	 * Junit testcase to test based on description.
	 */
	@Test
	public final void validExpfourArgumentsErrorDesc() {
		final String errorDesc = "Bad Request:Request cannot be fulfilled because of incorrect url.";
		this.validationException = new ValidationException(
				"400",
				"Bad Request:Request cannot be fulfilled because of incorrect url.",
				"500", this.correlationId);
		this.expectedResults = errorDesc;
		this.actualResults = this.validationException.getErrorDesc();
		assertNotNull(this.actualResults);
		assertEquals(this.expectedResults, this.actualResults);

	}

}
