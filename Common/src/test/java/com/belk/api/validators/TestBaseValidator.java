package com.belk.api.validators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import com.belk.api.constants.CommonConstants;
import com.belk.api.exception.BaseException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.belk.api.util.ErrorLoader;
import com.belk.api.util.TestBaseValidatorUtil;

/**
 * Validator class to test the correctness of the methods of the
 * {@link BaseValidator} class.
 * 
 * @author Mindtree
 * @date 20 March, 2013
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ BaseValidator.class, ErrorLoader.class,
		ValidationPathConfigHelper.class, ValidationConfig.class })
public class TestBaseValidator extends TestCase {
	
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
	String uriInfoPath;
	BaseValidator baseValidator;

	
	
	/**
	 * Precondition required for Junit test cases for BaseResource class.
	 */
	@Before
	public final void setUp() {	
		this.correlationId = "1234567891234567";
		this.uriInfoPath = "";
		this.baseValidator = new BaseValidator();
	}
	
	/**
	 * Tests the method validate
	 * {@link com.belk.api.validators.BaseValidator#validate()} to validate the
	 * input request.
	 * 
	 * @throws BaseException
	 *             - Exception thrown from Common Layer
	 * @throws Exception
	 *             - Exception
	 */
	@Test
	public final void testValidate() throws BaseException, Exception {
		PowerMockito.mockStatic(BaseValidator.class);
		
		final BaseValidator baseValidatorspy = PowerMockito
				.spy(new BaseValidator());
		
		final Map<String, List<String>> uriMap = TestBaseValidatorUtil.getUriMap();
		
		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		final Map<String, String> errorPropertiesMap = PowerMockito.mock(HashMap.class);
		errorLoader.setErrorPropertiesMap(errorPropertiesMap);
		this.baseValidator.setErrorLoader(errorLoader);		
		PowerMockito.doNothing().when(baseValidatorspy,
				"updateValidatorRules", this.correlationId, this.uriInfoPath);
		
		final ResourceValidationConfig resourceValidationConfig = PowerMockito.mock(ResourceValidationConfig.class);
		this.baseValidator.setResourceValidationConfig(resourceValidationConfig);
		
	
		final ErrorResponse errorResponse = PowerMockito.mock(ErrorResponse.class);
		final List qParams = new ArrayList();
		qParams.add("shirts");
				
		PowerMockito.doReturn(errorResponse).when(baseValidatorspy,
				"validateParameterValues", qParams, "q", this.correlationId);
				
		final String uriInfoPath = "v1/products/search";
		this.baseValidator.uriInfoPath = uriInfoPath;
		
		final ValidationPathConfigHelper validationPathConfigHelper = PowerMockito.mock(ValidationPathConfigHelper.class);
		final ValidationConfig validationConfig = new ValidationConfig(); 
		validationPathConfigHelper.validationConfig = validationConfig;
		validationPathConfigHelper.validationFilePath = BUNDLE
				.getString(ValidatorConstant.VALIDATION_FILE_PATH);
		
		final Map<String, String> validatorPathConfigMap = TestBaseValidatorUtil.getValidatorPathConfigMap();
	
		validationPathConfigHelper.validationConfig.setValidatorPathConfigMap(validatorPathConfigMap);
		
		this.baseValidator.validate(uriMap, this.correlationId);
		
	}
	/**
	 * Tests the method doRegexCheck
	 * {@link com.belk.api.validators.BaseValidator#doRegexCheck()} to validate the
	 * input request.
	 * 
	 * @throws Exception
	 *             - Exception
	 */
	@Test
	public final void testDoRegexCheck() throws Exception {
		final String value = "handbag";
		final String key = "q";
		final Regex regex =  new Regex();
		regex.setRegex("");
		regex.setRegexCheck(true);
		regex.setSplitter(",");
		
		final BaseValidator baseValidatorspy = PowerMockito
				.spy(new BaseValidator());

		PowerMockito.doReturn(null).when(baseValidatorspy,
				"doRegexCheck", value, key, this.correlationId, regex);
		
	}
	
	/**
	 * Tests the method doRangeCheck
	 * {@link com.belk.api.validators.BaseValidator#doRangeCheck()} to validate the
	 * input request.
	 * 
	 * @throws Exception
	 *             - Exception
	 */
	@Test
	public final void testDoRangeCheck() throws Exception {
		final String value = "handbag";
		final String key = "q";
		final Range range =  new Range();
		range.setMinimum(1);
		range.setMaximum(10);

		final BaseValidator baseValidatorspy = PowerMockito
				.spy(new BaseValidator());
		
	    PowerMockito.doReturn(null).when(baseValidatorspy,
			 "doRangeCheck", value, key, this.correlationId, range);
		
	}
	
	/**
	 * Tests the method doPreviousDateCheck
	 * {@link com.belk.api.validators.BaseValidator#doPreviousDateCheck()} to validate the
	 * input request.
	 * 
	 * @throws Exception
	 *             - Exception
	 */
	@Test
	public final void testDoPreviousDateCheck() throws Exception {
		final String value = "handbag";
		final String key = "q";
		final String format = "dd-MM-yy";

		final BaseValidator baseValidatorspy = PowerMockito
				.spy(new BaseValidator());
		
	    PowerMockito.doReturn(null).when(baseValidatorspy,
			 "doPreviousDateCheck", value, key, this.correlationId, format);
		
	}
	
	/**
	 * Tests the method doFutureDateCheck
	 * {@link com.belk.api.validators.BaseValidator#doFutureDateCheck()} to validate the
	 * input request.
	 * 
	 * @throws Exception
	 *             - Exception
	 */
	@Test
	public final void testDoFutureDateCheck() throws Exception {
		final String value = "handbag";
		final String key = "q";
		final String format = "dd-MM-yy";

		final BaseValidator baseValidatorspy = PowerMockito
				.spy(new BaseValidator());
		
	    PowerMockito.doReturn(null).when(baseValidatorspy,
			 "doFutureDateCheck", value, key, this.correlationId, format);
		
	}
	
	/**
	 * Tests the method doDateCheck
	 * {@link com.belk.api.validators.BaseValidator#doDateCheck()} to validate the
	 * input request.
	 * 
	 * @throws Exception
	 *             - Exception
	 */
	@Test
	public final void testDoDateCheck() throws Exception {
		final String value = "handbag";
		final String key = "q";
		final String format = "dd-MM-yy";

		final BaseValidator baseValidatorspy = PowerMockito
				.spy(new BaseValidator());
		
	    PowerMockito.doReturn(null).when(baseValidatorspy,
			 "doDateCheck", value, key, this.correlationId, format);
		
	}
	
	/**
	 * Tests the method doEmailCheck
	 * {@link com.belk.api.validators.BaseValidator#doEmailCheck()} to validate the
	 * input request.
	 * 
	 * @throws Exception
	 *             - Exception
	 */
	@Test
	public final void testDoEmailCheck() throws Exception {
		final String value = "abc@abc.com";
		final String key = "q";

		final BaseValidator baseValidatorspy = PowerMockito
				.spy(new BaseValidator());
		
	    PowerMockito.doReturn(null).when(baseValidatorspy,
			 "doEmailCheck", value, key, this.correlationId);
		
	}
	
	/**
	 * Tests the method doMaxheck
	 * {@link com.belk.api.validators.BaseValidator#doMaxCheck()} to validate the
	 * input request.
	 * 
	 * @throws Exception
	 *             - Exception
	 */
	@Test
	public final void testDoMaxCheck() throws Exception {
		final String value = "shirts";
		final String key = "q";
		final int refValue = 10;

		final BaseValidator baseValidatorspy = PowerMockito
				.spy(new BaseValidator());
		
	    PowerMockito.doReturn(null).when(baseValidatorspy,
			 "doMaxCheck", value, key, this.correlationId, refValue);
		
	}
	
	/**
	 * Tests the method doAlphaNumericCheck
	 * {@link com.belk.api.validators.BaseValidator#doAlphaNumericCheck()} to validate the
	 * input request.
	 * 
	 * @throws Exception
	 *             - Exception
	 */
	@Test
	public final void testDoAlphaNumericCheck() throws Exception {
		final String value = "shirts1234";
		final String key = "q";

		final BaseValidator baseValidatorspy = PowerMockito
				.spy(new BaseValidator());
		
	    PowerMockito.doReturn(null).when(baseValidatorspy,
			 "doAlphaNumericCheck", value, key, this.correlationId);
		
	}
	
	/**
	 * Tests the method doAlphabetCheck
	 * {@link com.belk.api.validators.BaseValidator#doAlphabetCheck()} to validate the
	 * input request.
	 * 
	 * @throws Exception
	 *             - Exception
	 */
	@Test
	public final void testDoAlphabetCheck() throws Exception {
		final String value = "shirts";
		final String key = "q";

		final BaseValidator baseValidatorspy = PowerMockito
				.spy(new BaseValidator());
		
	    PowerMockito.doReturn(null).when(baseValidatorspy,
			 "doAlphabetCheck", value, key, this.correlationId);
		
	}
	
	/**
	 * Tests the method doNumericCheck
	 * {@link com.belk.api.validators.BaseValidator#doNumericCheck()} to validate the
	 * input request.
	 * 
	 * @throws Exception
	 *             - Exception
	 */
	@Test
	public final void testDoNumericCheck() throws Exception {
		final String value = "100";
		final String key = "limit";

		final BaseValidator baseValidatorspy = PowerMockito
				.spy(new BaseValidator());
		
	    PowerMockito.doReturn(null).when(baseValidatorspy,
			 "doNumericCheck", value, key, this.correlationId);
		
	}
	
	/**
	 * Tests the method doEmptyCheck
	 * {@link com.belk.api.validators.BaseValidator#doEmptyheck()} to validate the
	 * input request.
	 * 
	 * @throws Exception
	 *             - Exception
	 */
	@Test
	public final void testDoEmptyCheck() throws Exception {
		final String value = " ";
		final String key = "q";

		final BaseValidator baseValidatorspy = PowerMockito
				.spy(new BaseValidator());
		
	    PowerMockito.doReturn(null).when(baseValidatorspy,
			 "doEmptyCheck", value, key, this.correlationId);
		
	}
	
	/**
	 * Tests the method doNullCheck
	 * {@link com.belk.api.validators.BaseValidator#doNullCheck()} to validate the
	 * input request.
	 * 
	 * @throws Exception
	 *             - Exception
	 */
	@Test
	public final void testDoNullCheck() throws Exception {
		final String value = null;
		final String key = "q";

		final BaseValidator baseValidatorspy = PowerMockito
				.spy(new BaseValidator());
		
	    PowerMockito.doReturn(null).when(baseValidatorspy,
			 "doNullCheck", value, key, this.correlationId);
		
	}
	
	/**
	 * Tests the method executeMandatoryFieldValidation
	 * {@link com.belk.api.validators.BaseValidator#executeMandatoryFieldValidation()} to validate the
	 * input request.
	 * 
	 * @throws Exception
	 *             - Exception
	 */
	@Test
	public final void testExecuteMandatoryFieldValidation() throws Exception {
		final String value = "handbag";
		final String key = "q";
		final String format = "dd-MM-yy";

		final Map<String, List<String>> uriMap = new HashMap<String, List<String>>();
		
		final List params = new ArrayList();
		params.add("shirts");
		
		uriMap.put("q", params);
		
		final BaseValidator baseValidatorspy = PowerMockito
				.spy(new BaseValidator());
		
	    PowerMockito.doNothing().when(baseValidatorspy,
			 "executeMandatoryFieldValidation", uriMap, this.correlationId);
		
	}
	
	/**
	 * Tests the method validateParameterValues
	 * {@link com.belk.api.validators.BaseValidator#validateParameterValues()} to validate the
	 * input request.
	 * 
	 * @throws Exception
	 *             - Exception
	 */
	@Test
	public final void testValidateParameterValues() throws Exception {
		final String value = "handbag";
		final String key = "q";
			
		final List params = new ArrayList();
		params.add("shirts");
			
		final BaseValidator baseValidatorspy = PowerMockito
				.spy(new BaseValidator());
		
	    PowerMockito.doReturn(null).when(baseValidatorspy,
			 "validateParameterValues", params, key, this.correlationId);
		
	}
	
	/**
	 * Tests the method doAttributeParamCheck
	 * {@link com.belk.api.validators.BaseValidator#doAttributeParamCheck()} to validate the
	 * input request.
	 * 
	 * @throws Exception
	 *             - Exception
	 */
	@Test
	public final void testDoAttributeParamCheck() throws Exception {
		final String value = "color:blue,white|size:small|brand:Baxter Designs";
		final String key = "attr";
			
		final List params = new ArrayList();
		params.add("shirts");
			
		final BaseValidator baseValidatorspy = PowerMockito
				.spy(new BaseValidator());
			
	    PowerMockito.doReturn(null).when(baseValidatorspy,
			 "doAttributeParamCheck", key, value, this.correlationId);
		
	}
	
	/**
	 * Tests the method doSortParamCheck
	 * {@link com.belk.api.validators.BaseValidator#doSortParamCheck()} to validate the
	 * input request.
	 * 
	 * @throws Exception
	 *             - Exception
	 */
	@Test
	public final void testDoSortParamCheck() throws Exception {
		final String value = "name|-listprice";
		final String key = "sort";
					
		final BaseValidator baseValidatorspy = PowerMockito
				.spy(new BaseValidator());
			
	    PowerMockito.doReturn(null).when(baseValidatorspy,
			 "doSortParamCheck", key, value, this.correlationId);
		
	}
	
	/**
	 * Tests the method updateValidatorRules
	 * {@link com.belk.api.validators.BaseValidator#updateValidatorRules()} to validate the
	 * validation of validator rules.
	 * 
	 * @throws Exception
	 *             - Exception
	 */
	@Test
	public final void testUpdateValidatorRules() throws Exception {
		final String value = "handbag";
		final String key = "q";
			
		final List params = new ArrayList();
		params.add("shirts");
			
		final BaseValidator baseValidatorspy = PowerMockito
				.spy(new BaseValidator());
			
	    PowerMockito.doNothing().when(baseValidatorspy,
			 "updateValidatorRules", this.uriInfoPath, this.correlationId);
		
	}
	
	/**
	 * Tests the method validate
	 * {@link com.belk.api.validators.BaseValidator#validate()} to validate the
	 * input request.
	 * 
	 * @throws BaseException
	 *             - Exception thrown from Common Layer
	 * @throws Exception
	 *             - Exception
	 */
	@Test
	(expected = BaseException.class)
	public final void testValidateNullCheck() throws BaseException, Exception {
		PowerMockito.mockStatic(BaseValidator.class);
		
		final BaseValidator baseValidatorspy = PowerMockito
				.spy(new BaseValidator());
		
		final Map<String, List<String>> uriMap = TestBaseValidatorUtil.getUriMapWithNullKeyValue();

		final ErrorLoader errorLoader = new ErrorLoader();
		final Map<String, String> errorPropertiesMap = TestBaseValidatorUtil.getErrorPropertiesMap();
		errorLoader.setErrorPropertiesMap(errorPropertiesMap);
		this.baseValidator.setErrorLoader(errorLoader);	
		PowerMockito.doNothing().when(baseValidatorspy,
				"updateValidatorRules", this.correlationId, this.uriInfoPath);
		
		final ResourceValidationConfig resourceValidationConfig = PowerMockito.mock(ResourceValidationConfig.class);
		this.baseValidator.setResourceValidationConfig(resourceValidationConfig);
		
		final String key = "q";
		final String paramValue = null;
		
		final List qParams = new ArrayList();
		qParams.add(paramValue);
				
		final ErrorResponse errorResponse = PowerMockito.mock(ErrorResponse.class);
				
		PowerMockito.doReturn(errorResponse).when(baseValidatorspy,
				"validateParameterValues", qParams, key, this.correlationId);
			
		final String uriInfoPath = "v1/products/search";
		this.baseValidator.uriInfoPath = uriInfoPath;
		
		final ValidationPathConfigHelper validationPathConfigHelper = PowerMockito.mock(ValidationPathConfigHelper.class);
		final ValidationConfig validationConfig = new ValidationConfig();

		validationPathConfigHelper.validationConfig = validationConfig;
		validationPathConfigHelper.validationFilePath = BUNDLE
				.getString(ValidatorConstant.VALIDATION_FILE_PATH);
		
		final Map<String, String> validatorPathConfigMap = TestBaseValidatorUtil.getValidatorPathConfigMap();
		
		validationPathConfigHelper.validationConfig.setValidatorPathConfigMap(validatorPathConfigMap);
		
		this.baseValidator.validate(uriMap, this.correlationId);
		
	}
	
	/**
	 * Tests the method validate
	 * {@link com.belk.api.validators.BaseValidator#validate()} to validate the
	 * input request.
	 * 
	 * @throws BaseException
	 *             - Exception thrown from Common Layer
	 * @throws Exception
	 *             - Exception
	 */
	@Test
	(expected = BaseException.class)
	public final void testValidateAlphabetCheck() throws BaseException, Exception {
		PowerMockito.mockStatic(BaseValidator.class);
		
		final BaseValidator baseValidatorspy = PowerMockito
				.spy(new BaseValidator());
		
		final Map<String, List<String>> uriMap = TestBaseValidatorUtil.getUriMapWithNotAlphabet();

		final ErrorLoader errorLoader = new ErrorLoader();
		final Map<String, String> errorPropertiesMap = TestBaseValidatorUtil.getErrorPropertiesMap();
		errorLoader.setErrorPropertiesMap(errorPropertiesMap);		
		this.baseValidator.setErrorLoader(errorLoader);	
		PowerMockito.doNothing().when(baseValidatorspy,
				"updateValidatorRules", this.correlationId, this.uriInfoPath);
		
		final ResourceValidationConfig resourceValidationConfig = PowerMockito.mock(ResourceValidationConfig.class);
		this.baseValidator.setResourceValidationConfig(resourceValidationConfig);
	
		final String key = "dim";
		final String paramValue = "shirts";
		
		final List dimParams = new ArrayList();
		dimParams.add("10");
		
		final ErrorResponse errorResponse = PowerMockito.mock(ErrorResponse.class);
				
		PowerMockito.doReturn(errorResponse).when(baseValidatorspy,
				"validateParameterValues", dimParams, key, this.correlationId);
				
		final String uriInfoPath = "v1/products/search";
		this.baseValidator.uriInfoPath = uriInfoPath;
		
		final ValidationPathConfigHelper validationPathConfigHelper = PowerMockito.mock(ValidationPathConfigHelper.class);
		final ValidationConfig validationConfig = new ValidationConfig();

		validationPathConfigHelper.validationConfig = validationConfig;
		validationPathConfigHelper.validationFilePath = BUNDLE
				.getString(ValidatorConstant.VALIDATION_FILE_PATH);
		
		final Map<String, String> validatorPathConfigMap = TestBaseValidatorUtil.getValidatorPathConfigMap();
		
		validationPathConfigHelper.validationConfig.setValidatorPathConfigMap(validatorPathConfigMap);
		
		this.baseValidator.validate(uriMap, this.correlationId);
		
	}
	
	/**
	 * Tests the method validate
	 * {@link com.belk.api.validators.BaseValidator#validate()} to validate the
	 * input request.
	 * 
	 * @throws BaseException
	 *             - Exception thrown from Common Layer
	 * @throws Exception
	 *             - Exception
	 */
	@Test
	(expected = BaseException.class)
	public final void testValidateNumericCheck() throws BaseException, Exception {
		PowerMockito.mockStatic(BaseValidator.class);
		
		final BaseValidator baseValidatorspy = PowerMockito
				.spy(new BaseValidator());
		
		final Map<String, List<String>> uriMap = TestBaseValidatorUtil.getUriMapWithNotNumeric();

		final ErrorLoader errorLoader = new ErrorLoader();
		final Map<String, String> errorPropertiesMap = TestBaseValidatorUtil.getErrorPropertiesMap();
		errorLoader.setErrorPropertiesMap(errorPropertiesMap);		
		this.baseValidator.setErrorLoader(errorLoader);	
		PowerMockito.doNothing().when(baseValidatorspy,
				"updateValidatorRules", this.correlationId, this.uriInfoPath);
		
		final ResourceValidationConfig resourceValidationConfig = PowerMockito.mock(ResourceValidationConfig.class);
		this.baseValidator.setResourceValidationConfig(resourceValidationConfig);
					
		final ErrorResponse errorResponse = PowerMockito.mock(ErrorResponse.class);
		
		final List limitparams = new ArrayList();
		limitparams.add("abcd");
				
		PowerMockito.doReturn(errorResponse).when(baseValidatorspy,
				"validateParameterValues", limitparams, "limit", this.correlationId);
						
		final String uriInfoPath = "v1/products/search";
		this.baseValidator.uriInfoPath = uriInfoPath;
		
		final ValidationPathConfigHelper validationPathConfigHelper = PowerMockito.mock(ValidationPathConfigHelper.class);
		final ValidationConfig validationConfig = new ValidationConfig();

		validationPathConfigHelper.validationConfig = validationConfig;
		validationPathConfigHelper.validationFilePath = BUNDLE
				.getString(ValidatorConstant.VALIDATION_FILE_PATH);
		
		final Map<String, String> validatorPathConfigMap = TestBaseValidatorUtil.getValidatorPathConfigMap();
		
		validationPathConfigHelper.validationConfig.setValidatorPathConfigMap(validatorPathConfigMap);
		
		this.baseValidator.validate(uriMap, this.correlationId);
		
	}
	
	/**
	 * Tests the method validate
	 * {@link com.belk.api.validators.BaseValidator#validate()} to validate the
	 * input request.
	 * 
	 * @throws BaseException
	 *             - Exception thrown from Common Layer
	 * @throws Exception
	 *             - Exception
	 */
	@Test
	(expected = BaseException.class)
	public final void testValidateInValidKey() throws BaseException, Exception {
		PowerMockito.mockStatic(BaseValidator.class);
		
		final BaseValidator baseValidatorspy = PowerMockito
				.spy(new BaseValidator());
		
		final Map<String, List<String>> uriMap = TestBaseValidatorUtil.getUriMapWithInvalidKey();

		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		final Map<String, String> errorPropertiesMap = PowerMockito.mock(HashMap.class);
		errorLoader.setErrorPropertiesMap(errorPropertiesMap);
		this.baseValidator.setErrorLoader(errorLoader);	
		PowerMockito.doNothing().when(baseValidatorspy,
				"updateValidatorRules", this.correlationId, this.uriInfoPath);
		
		final ResourceValidationConfig resourceValidationConfig = PowerMockito.mock(ResourceValidationConfig.class);
		this.baseValidator.setResourceValidationConfig(resourceValidationConfig);
		
		final String key = "a";
		final String paramValue = "shirts";
		
		final List qParams = new ArrayList();
		qParams.add("shirts");
		
		final ErrorResponse errorResponse = PowerMockito.mock(ErrorResponse.class);
				
		PowerMockito.doReturn(errorResponse).when(baseValidatorspy,
				"validateParameterValues", qParams, key, this.correlationId);
				
		final String uriInfoPath = "v1/products/search";
		this.baseValidator.uriInfoPath = uriInfoPath;
		
		final ValidationPathConfigHelper validationPathConfigHelper = PowerMockito.mock(ValidationPathConfigHelper.class);
		final ValidationConfig validationConfig = new ValidationConfig();
				
		validationPathConfigHelper.validationConfig = validationConfig;
		validationPathConfigHelper.validationFilePath = BUNDLE
				.getString(ValidatorConstant.VALIDATION_FILE_PATH);
		
		final Map<String, String> validatorPathConfigMap = TestBaseValidatorUtil.getValidatorPathConfigMap();		
		validationPathConfigHelper.validationConfig.setValidatorPathConfigMap(validatorPathConfigMap);
		
		this.baseValidator.validate(uriMap, this.correlationId);
		
	}
	
	/**
	 * Tests the method validate
	 * {@link com.belk.api.validators.BaseValidator#validate()} to validate the
	 * input request.
	 * 
	 * @throws BaseException
	 *             - Exception thrown from Common Layer
	 * @throws Exception
	 *             - Exception
	 */
	@Test
	(expected = BaseException.class)
	public final void testValidateEmptyCheck() throws BaseException, Exception {
		PowerMockito.mockStatic(BaseValidator.class);
		
		final BaseValidator baseValidatorspy = PowerMockito
				.spy(new BaseValidator());
		
		final Map<String, List<String>> uriMap = TestBaseValidatorUtil.getUriMapWithEmptyKeyValue();

		final ErrorLoader errorLoader = new ErrorLoader();
		final Map<String, String> errorPropertiesMap = TestBaseValidatorUtil.getErrorPropertiesMap();
		errorLoader.setErrorPropertiesMap(errorPropertiesMap);
		this.baseValidator.setErrorLoader(errorLoader);
		PowerMockito.doNothing().when(baseValidatorspy,
				"updateValidatorRules", this.correlationId, this.uriInfoPath);
		
		final ResourceValidationConfig resourceValidationConfig = PowerMockito.mock(ResourceValidationConfig.class);
		this.baseValidator.setResourceValidationConfig(resourceValidationConfig);
		final String key = "q";
		final String paramValue = "shirts";
		
		final List qParams = new ArrayList();
		qParams.add("");
			
		final ErrorResponse errorResponse = PowerMockito.mock(ErrorResponse.class);
				
		PowerMockito.doReturn(errorResponse).when(baseValidatorspy,
				"validateParameterValues", qParams, key, this.correlationId);
				
		final String uriInfoPath = "v1/products/search";
		this.baseValidator.uriInfoPath = uriInfoPath;
		
		final ValidationPathConfigHelper validationPathConfigHelper = PowerMockito.mock(ValidationPathConfigHelper.class);
		final ValidationConfig validationConfig = new ValidationConfig();

		validationPathConfigHelper.validationConfig = validationConfig;
		validationPathConfigHelper.validationFilePath = BUNDLE
				.getString(ValidatorConstant.VALIDATION_FILE_PATH);
		
		final Map<String, String> validatorPathConfigMap = TestBaseValidatorUtil.getValidatorPathConfigMap();		
		validationPathConfigHelper.validationConfig.setValidatorPathConfigMap(validatorPathConfigMap);
		
		this.baseValidator.validate(uriMap, this.correlationId);

		
	}
	
	/**
	 * Tests the method validate
	 * {@link com.belk.api.validators.BaseValidator#validate()} to validate the
	 * input request.
	 * 
	 * @throws BaseException
	 *             - Exception thrown from Common Layer
	 * @throws Exception
	 *             - Exception
	 */
	@Test
	(expected = BaseException.class)
	public final void testValidateMaxCheck() throws BaseException, Exception {
		PowerMockito.mockStatic(BaseValidator.class);
		
		final BaseValidator baseValidatorspy = PowerMockito
				.spy(new BaseValidator());
		
		final Map<String, List<String>> uriMap = TestBaseValidatorUtil.getUriMapWithMaximumValue();

		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		final Map<String, String> errorPropertiesMap = PowerMockito.mock(HashMap.class);
		errorLoader.setErrorPropertiesMap(errorPropertiesMap);
		this.baseValidator.setErrorLoader(errorLoader);	
		PowerMockito.doNothing().when(baseValidatorspy,
				"updateValidatorRules", this.correlationId, this.uriInfoPath);
		
		final ResourceValidationConfig resourceValidationConfig = PowerMockito.mock(ResourceValidationConfig.class);
		this.baseValidator.setResourceValidationConfig(resourceValidationConfig);
		
		final String key = "q";
		final String paramValue = "shirtsandbags";
		
		final List qParams = new ArrayList();
		qParams.add(paramValue);
			
		final ErrorResponse errorResponse = PowerMockito.mock(ErrorResponse.class);
				
		PowerMockito.doReturn(errorResponse).when(baseValidatorspy,
				"validateParameterValues", qParams, key, this.correlationId);		
		
		final String uriInfoPath = "v1/products/search";
		this.baseValidator.uriInfoPath = uriInfoPath;
		
		final ValidationPathConfigHelper validationPathConfigHelper = PowerMockito.mock(ValidationPathConfigHelper.class);
		final ValidationConfig validationConfig = new ValidationConfig(); 

		validationPathConfigHelper.validationConfig = validationConfig;
		validationPathConfigHelper.validationFilePath = BUNDLE
				.getString(ValidatorConstant.VALIDATION_FILE_PATH);
		
		final Map<String, String> validatorPathConfigMap = TestBaseValidatorUtil.getValidatorPathConfigMap();		
		validationPathConfigHelper.validationConfig.setValidatorPathConfigMap(validatorPathConfigMap);
				
		this.baseValidator.validate(uriMap, this.correlationId);
	}

	/**
	 * Tests the method validate
	 * {@link com.belk.api.validators.BaseValidator#validate()} to validate the
	 * input request.
	 * 
	 * @throws BaseException
	 *             - Exception thrown from Common Layer
	 * @throws Exception
	 *             - Exception
	 */
	@Test
	public final void testValidateSortParam() throws BaseException, Exception {
		PowerMockito.mockStatic(BaseValidator.class);
		
		final BaseValidator baseValidatorspy = PowerMockito
				.spy(new BaseValidator());
		
		final Map<String, List<String>> uriMap = TestBaseValidatorUtil.getUriMapWithSortParam();

		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		final Map<String, String> errorPropertiesMap = PowerMockito.mock(HashMap.class);
		errorLoader.setErrorPropertiesMap(errorPropertiesMap);
		this.baseValidator.setErrorLoader(errorLoader);	
		PowerMockito.doNothing().when(baseValidatorspy,
				"updateValidatorRules", this.correlationId, this.uriInfoPath);
		
		final ResourceValidationConfig resourceValidationConfig = PowerMockito.mock(ResourceValidationConfig.class);
		this.baseValidator.setResourceValidationConfig(resourceValidationConfig);
				
		final List sortParams = new ArrayList();
		sortParams.add("name|-listprice");
			
		final ErrorResponse errorResponse = PowerMockito.mock(ErrorResponse.class);
				
		PowerMockito.doReturn(errorResponse).when(baseValidatorspy,
				"validateParameterValues", sortParams, "sort", this.correlationId);
				
		final String uriInfoPath = "v1/products/search";
		this.baseValidator.uriInfoPath = uriInfoPath;
		
		final ValidationPathConfigHelper validationPathConfigHelper = PowerMockito.mock(ValidationPathConfigHelper.class);
		final ValidationConfig validationConfig = new ValidationConfig();
				
		validationPathConfigHelper.validationConfig = validationConfig;
		validationPathConfigHelper.validationFilePath = BUNDLE
				.getString(ValidatorConstant.VALIDATION_FILE_PATH);
		
		final Map<String, String> validatorPathConfigMap = TestBaseValidatorUtil.getValidatorPathConfigMap();		
		validationPathConfigHelper.validationConfig.setValidatorPathConfigMap(validatorPathConfigMap);
				
		this.baseValidator.validate(uriMap, this.correlationId);
	}
	
	/**
	 * Tests the method validate
	 * {@link com.belk.api.validators.BaseValidator#validate()} to validate the
	 * input request.
	 * 
	 * @throws BaseException
	 *             - Exception thrown from Common Layer
	 * @throws Exception
	 *             - Exception
	 */
	@Test
	(expected = BaseException.class)
	public final void testValidateSortInvalidParam() throws BaseException, Exception {
		PowerMockito.mockStatic(BaseValidator.class);
		
		final BaseValidator baseValidatorspy = PowerMockito
				.spy(new BaseValidator());
		
		final Map<String, List<String>> uriMap = TestBaseValidatorUtil.getUriMapWithInvalidSortParam();

		final ErrorLoader errorLoader = new ErrorLoader();
		final Map<String, String> errorPropertiesMap = TestBaseValidatorUtil.getErrorPropertiesMap();
		errorLoader.setErrorPropertiesMap(errorPropertiesMap);
		this.baseValidator.setErrorLoader(errorLoader);	
		PowerMockito.doNothing().when(baseValidatorspy,
				"updateValidatorRules", this.correlationId, this.uriInfoPath);
		
		final ResourceValidationConfig resourceValidationConfig = PowerMockito.mock(ResourceValidationConfig.class);
		this.baseValidator.setResourceValidationConfig(resourceValidationConfig);

		final List sortParams = new ArrayList();
		sortParams.add("name|abcd");
		
		final ErrorResponse errorResponse = PowerMockito.mock(ErrorResponse.class);
				
		PowerMockito.doReturn(errorResponse).when(baseValidatorspy,
				"validateParameterValues", sortParams, "sort", this.correlationId);
				
		final String uriInfoPath = "v1/products/search";
		this.baseValidator.uriInfoPath = uriInfoPath;
		
		final ValidationPathConfigHelper validationPathConfigHelper = PowerMockito.mock(ValidationPathConfigHelper.class);
		final ValidationConfig validationConfig = new ValidationConfig();

		validationPathConfigHelper.validationConfig = validationConfig;
		validationPathConfigHelper.validationFilePath = BUNDLE
				.getString(ValidatorConstant.VALIDATION_FILE_PATH);
		
		final Map<String, String> validatorPathConfigMap = TestBaseValidatorUtil.getValidatorPathConfigMap();		
		validationPathConfigHelper.validationConfig.setValidatorPathConfigMap(validatorPathConfigMap);
				
		this.baseValidator.validate(uriMap, this.correlationId);
	}
	
	/**
	 * Tests the method validate
	 * {@link com.belk.api.validators.BaseValidator#validate()} to validate the
	 * input request.
	 * 
	 * @throws BaseException
	 *             - Exception thrown from Common Layer
	 * @throws Exception
	 *             - Exception
	 */
	@Ignore
	//(expected = BaseException.class)
	public final void testValidateResourceValidConfig() throws BaseException, Exception {
		PowerMockito.mockStatic(BaseValidator.class);
		
		final BaseValidator baseValidatorspy = PowerMockito
				.spy(new BaseValidator());
		
		final Map<String, List<String>> uriMap = TestBaseValidatorUtil.getUriMap();

		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		final Map<String, String> errorPropertiesMap = PowerMockito.mock(HashMap.class);
		errorLoader.setErrorPropertiesMap(errorPropertiesMap);
		this.baseValidator.setErrorLoader(errorLoader);	
		PowerMockito.doNothing().when(baseValidatorspy,
				"updateValidatorRules", this.correlationId, this.uriInfoPath);
		
		final ResourceValidationConfig resourceValidationConfig = PowerMockito.mock(ResourceValidationConfig.class);
		this.baseValidator.setResourceValidationConfig(resourceValidationConfig);
		
		final List sortParams = new ArrayList();
		sortParams.add("name|abcd");	
		
		final ErrorResponse errorResponse = PowerMockito.mock(ErrorResponse.class);
				
		PowerMockito.doReturn(errorResponse).when(baseValidatorspy,
				"validateParameterValues", sortParams, "sort", this.correlationId);
		
		final ValidationPathConfigHelper validationPathConfigHelper = PowerMockito.mock(ValidationPathConfigHelper.class);
		final ValidationConfig validationConfig = new ValidationConfig();
		validationPathConfigHelper.validationConfig = validationConfig;
		validationPathConfigHelper.validationFilePath = BUNDLE
				.getString(ValidatorConstant.VALIDATION_FILE_PATH);
		
		final Map<String, String> validatorPathConfigMap = TestBaseValidatorUtil.getValidatorPathConfigMap();		
		validationPathConfigHelper.validationConfig.setValidatorPathConfigMap(validatorPathConfigMap);
				
		this.baseValidator.validate(uriMap, this.correlationId);
	}
	
	/**
	 * Tests the method validate
	 * {@link com.belk.api.validators.BaseValidator#validate()} to validate the
	 * input request.
	 * 
	 * @throws BaseException
	 *             - Exception thrown from Common Layer
	 * @throws Exception
	 *             - Exception
	 */
	@Ignore
	@Test
	//(expected = BaseException.class)
	public final void testValidateExecuteMandatoryFieldValidation() throws BaseException, Exception {
		PowerMockito.mockStatic(BaseValidator.class);
		
		final BaseValidator baseValidatorspy = PowerMockito
				.spy(new BaseValidator());
		
		final Map<String, List<String>> uriMap = new HashMap<String, List<String>>();

		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		final Map<String, String> errorPropertiesMap = PowerMockito.mock(HashMap.class);
		errorLoader.setErrorPropertiesMap(errorPropertiesMap);
		this.baseValidator.setErrorLoader(errorLoader);	
		PowerMockito.doNothing().when(baseValidatorspy,
				"updateValidatorRules", this.correlationId, this.uriInfoPath);
		
		final ResourceValidationConfig resourceValidationConfig = PowerMockito.mock(ResourceValidationConfig.class);
		this.baseValidator.setResourceValidationConfig(resourceValidationConfig);

		final String key = "cataloga";
		
		final List catalogParams = new ArrayList();
		catalogParams.add("888");
		
		uriMap.put(key, catalogParams);
		
		final ErrorResponse errorResponse = PowerMockito.mock(ErrorResponse.class);
				
		PowerMockito.doReturn(errorResponse).when(baseValidatorspy,
				"validateParameterValues", catalogParams, key, this.correlationId);				
		
		final ValidationPathConfigHelper validationPathConfigHelper = PowerMockito.mock(ValidationPathConfigHelper.class);
		final ValidationConfig validationConfig = new ValidationConfig();

		validationPathConfigHelper.validationConfig = validationConfig;
		validationPathConfigHelper.validationFilePath = BUNDLE
				.getString(ValidatorConstant.VALIDATION_FILE_PATH);
		
		final Map<String, String> map = new HashMap<String, String>();
		map.put("v1/catalog", "catalog-validation.xml");
		
		validationPathConfigHelper.validationConfig.setValidatorPathConfigMap(map);	
				
		this.baseValidator.validate(uriMap, this.correlationId);
	}
	
	/**
	 * Tests the method isBaseValidationRequired
	 * {@link com.belk.api.validators.BaseValidator#isBaseValidationRequired()} to validate the
	 * input request.
	 * 
	 */
	@Test
	public final void testIsBaseValidationRequired() throws BaseException{
		assertEquals(false, this.baseValidator.isBaseValidationRequired(
				CommonConstants.PRODUCT_SEARCH, this.correlationId));
	}
	
	/**
	 * Tests the method isBaseValidationRequired
	 * {@link com.belk.api.validators.BaseValidator#isBaseValidationRequired()} to validate the
	 * input request.
	 * 
	 */
	@Test
	public final void testIsBaseValidationRequiredTrue() throws BaseException{
		PowerMockito.mockStatic(BaseValidator.class);
		
		final ValidationPathConfigHelper validationPathConfigHelper = PowerMockito.mock(ValidationPathConfigHelper.class);
		final ValidationConfig validationConfig = new ValidationConfig();
		validationPathConfigHelper.validationConfig = validationConfig;
		validationPathConfigHelper.validationFilePath = BUNDLE
				.getString(ValidatorConstant.VALIDATION_FILE_PATH);
		
		final Map<String, Boolean> apiServicesConfigMap = TestBaseValidatorUtil
				.getApiServicesConfigMap();
		validationPathConfigHelper.validationConfig
				.setApiServicesConfigMap(apiServicesConfigMap);

		assertEquals(true, this.baseValidator.isBaseValidationRequired(
				CommonConstants.PRODUCT_SEARCH, this.correlationId));

	}
		


}
