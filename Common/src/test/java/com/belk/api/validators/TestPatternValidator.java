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
 * {@link PatternValidator} class.
 * 
 * @author Mindtree
 * @date 21 March, 2013
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ PatternValidator.class })
public class TestPatternValidator {
	
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}
	 final String pattern = "";
	 final String splitter = "";
	final PatternValidator patternValidator = new PatternValidator(this.pattern, this.splitter);
	/**
	 * Method to test validate().
	 * {@link com.belk.api.validators.PatternValidator#validate(java.lang.String,java.lang.String,java.lang.String)} 
	 */
	@Test
	public final void testValidate() {
		
		final String inputParamValue = "shirts";
		final String correlationId = "";
		final boolean expected = false;
		final boolean actual = this.patternValidator.validate(inputParamValue, correlationId);
		assertEquals(actual, expected);
	}
	/**
	 * Method to test validate().
	 * {@link com.belk.api.validators.PatternValidator#validate(java.lang.String,java.lang.String,java.lang.String)} 
	 */
	@Test
	public final void testValidateForNull() {
		
		final String inputParamValue = "";
		final String correlationId = "";
		final boolean expected = false;
		final boolean actual = this.patternValidator.validate(inputParamValue, correlationId);
		assertEquals(actual, expected);
	}

}
