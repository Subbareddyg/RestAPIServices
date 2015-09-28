package com.belk.api.validators;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;

/**
 * Validator class to test the correctness of the methods of the
 * {@link ValidationConfigRuleConverter} class.
 * 
 * @author Mindtree
 * @date 20 March, 2013
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ ValidationConfigRuleConverter.class, ResourceValidationConfig.class })
public class TestValidationConfigRuleConverter {

	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}
	
	String correlationId;
	
	/**
	 * Precondition required for Junit test cases for ValidationConfigRuleConverter class.
	 */
	@Before
	public final void setUp() {	
		this.correlationId = "1234567891234567";
	}
	
	/**
	 * Tests the method getResourceValidationConfig
	 * {@link com.belk.api.validators.ValidationConfigRuleConverter#getResourceValidationConfig()} to validate the
	 * input request.
	 * 
	 */
	@Test
	public final void testGetResourceValidationConfig() {
		
		final ValidationConfigRuleConverter validationConfigRuleConverter = PowerMockito.mock(ValidationConfigRuleConverter.class);
		final ValidationConfig validationConfig = new ValidationConfig(); 
		
		validationConfigRuleConverter.getResourceValidationConfig("testproductsearch-validation.xml", this.correlationId);
	}


	/**
	 * Tests the method getValidationPathConfig
	 * {@link com.belk.api.validators.ValidationConfigRuleConverter#getValidationPathConfig()} to validate the
	 * input request.
	 * 
	 */
	@Test
	public final void testGetValidationPathConfig() {
	
		final ValidationConfigRuleConverter validationConfigRuleConverter = PowerMockito.mock(ValidationConfigRuleConverter.class);
		final ValidationConfig validationConfig = new ValidationConfig();
		
		validationConfigRuleConverter.getResourceValidationConfig("validation-config.xml", this.correlationId);
	}
}
