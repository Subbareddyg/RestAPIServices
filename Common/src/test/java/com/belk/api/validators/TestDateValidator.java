package com.belk.api.validators;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;

/**
 * Validator class to test the correctness of the methods of the
 * {@link BaseValidator} class.
 * 
 * @author Mindtree
 * @date 21 March, 2013
 * 
 */


@RunWith(PowerMockRunner.class)
@PrepareForTest({ DateValidator.class })
public class TestDateValidator {
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}
	DateValidator dateValidator = new DateValidator();
	/**
	 * Method to test isFutureDate.
	 * {@link com.belk.api.validators.DateValidator#isFutureDate(java.lang.String,java.lang.String,java.lang.String)} 
	 */
	@Test
	public final void testIsFutureDate() {
		final String dateToValidate = "21/02/2000";
		final String dateFormat = "dd/mm/yyyy";
		final String correlationId = "123456788";
		final boolean expected = false;
		final boolean actual = this.dateValidator.isFutureDate(dateToValidate, dateFormat, correlationId);
		assertEquals(expected, actual);
		
	}
	/**
	 * Method to test isFutureDate.
	 * {@link com.belk.api.validators.DateValidator#isFutureDate(java.lang.String,java.lang.String,java.lang.String)} 
	 */
	@Test
	public final void testIsFutureDateForTrueValue() {
		final String dateToValidate = "21/02/2017";
		final String dateFormat = "dd/mm/yyyy";
		final String correlationId = "123456788";
		final boolean expected = true;
		final boolean actual = this.dateValidator.isFutureDate(dateToValidate, dateFormat, correlationId);
		assertEquals(expected, actual);
		
	}
	/**
	 * Method to test isBeforreDate.
	 * {@link com.belk.api.validators.DateValidator#isBeforeDate(java.lang.String,java.lang.String,java.lang.String)} 
	 */
	@Test
	public final void testIsBeforeDate() {
		final String dateToValidate = "21/02/2000";
		final String dateFormat = "dd/mm/yyyy";
		final String correlationId = "123456788";
		final boolean expected = true;
		final boolean actual = this.dateValidator.isBeforeDate(dateToValidate, dateFormat, correlationId);
		assertEquals(expected, actual);
	}
	/**
	 * Method to test isBeforreDate.
	 * {@link com.belk.api.validators.DateValidator#isBeforeDate(java.lang.String,java.lang.String,java.lang.String)} 
	 */
	@Test
	public final void testIsBeforeDateForFalseValue() {
		final String dateToValidate = "21/02/2016";
		final String dateFormat = "dd/mm/yyyy";
		final String correlationId = "123456788";
		final boolean expected = false;
		final boolean actual = this.dateValidator.isBeforeDate(dateToValidate, dateFormat, correlationId);
		assertEquals(expected, actual);
	}
	/**
	 * Method to test isBeforreDate.
	 * {@link com.belk.api.validators.DateValidator#isDateValid(java.lang.String,java.lang.String,java.lang.String)} 
	 */
	@Test
	public final void testIsDateValid() {
		
		final String dateToValidate = "21/02/2016";
		final String dateFormat = "dd/mm/yyyy";
		final String correlationId = "123456788";
		final boolean expected = true;
		final boolean actual = this.dateValidator.isDateValid(dateToValidate, dateFormat, correlationId);
		assertEquals(expected, actual);
	}
	/**
	 * Method to test isBeforreDate.
	 * {@link com.belk.api.validators.DateValidator#isDateValid(java.lang.String,java.lang.String,java.lang.String)} 
	 */
	@Test
	public final void testIsDateValidForFalseValue() {
		
		final String dateToValidate = "";
		final String dateFormat = "dd/mm/yyyy";
		final String correlationId = "123456788";
		final boolean expected = false;
		final boolean actual = this.dateValidator.isDateValid(dateToValidate, dateFormat, correlationId);
		assertEquals(expected, actual);
	}
	
	

}
