package com.belk.api.validators;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import com.belk.api.constants.CommonConstants;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;

/**
 * Validator class to test the correctness of the methods of the
 * {@link ValidationPathConfigHelper} class.
 * 
 * @author Mindtree
 * @date 20 March, 2013
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ ValidationPathConfigHelper.class, ValidationConfig.class })
public class TestValidationPathConfigHelper extends TestCase {

	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}
	/**
	 * common constants are loaded from the resource bundle.
	 */
	private static final ResourceBundle BUNDLE = ResourceBundle
			.getBundle(CommonConstants.COMMON_RESOURCE_BUNDLE);
	String correlationId;	
	
	/**
	 * Precondition required for Junit test cases for ValidationPathConfigHelper class.
	 */
	@Before
	public final void setUp() {	
		this.correlationId = "1234567891234567";
	}
	
	/**
	 * Tests the method loadValidationConfigProperties
	 * {@link com.belk.api.validators.ValidationPathConfigHelper#loadValidationConfigProperties()} toload
	 * the validation configuration.
	 * 
	 */
	@Test
	public final void testLoadValidationConfigProperties() {
		
		final ValidationPathConfigHelper validationPathConfigHelper = PowerMockito.mock(ValidationPathConfigHelper.class);
		final ValidationConfig validationConfig = new ValidationConfig(); 
		validationPathConfigHelper.validationConfig = validationConfig;
		validationPathConfigHelper.validationFilePath = BUNDLE
				.getString(ValidatorConstant.VALIDATION_FILE_PATH);
		
		final Map<String, String> map = new HashMap<String, String>();
		map.put("v1/products/search", "testproductsearch-validation.xml");
		
		validationPathConfigHelper.validationConfig.setValidatorPathConfigMap(map);
		
		ValidationPathConfigHelper.loadValidationConfigProperties();
	}

}
