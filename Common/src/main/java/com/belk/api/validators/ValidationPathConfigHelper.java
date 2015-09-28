package com.belk.api.validators;

import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import com.belk.api.constants.CommonConstants;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;

/**
 * This class contains the data and method to configure the validation file
 * path.
 * 
 * @author Mindtree
 * 
 */
@Component("validationPathConfigHelper")
public final class ValidationPathConfigHelper {

	/**
	 * Validation Path Config
	 */
	public static ValidationConfig validationConfig;

	/**
	 * validationFilePath is the path of the validation files.
	 */
	public static String validationFilePath;

	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	/**
	 * common constants are loaded from the resource bundle.
	 */
	private static final ResourceBundle BUNDLE = ResourceBundle
			.getBundle(CommonConstants.COMMON_RESOURCE_BUNDLE);
	
	/**
	 * To initialize the load properties
	 * 
	 */
	@PostConstruct
	public void init() {
		loadValidationConfigProperties();
	}

	/**
	 * Method to configure all the validations by load the properties.
	 * This method will load all the validation configuration files.
	 * This method will be called on server startup to load all the 
	 * validation configurations. So the correlationId value will be null by that time.
	 */
	public static void loadValidationConfigProperties() {
		// Since there will be no value for
		// correlationId, so passing empty string value
		final long startTime = LOGGER
				.logMethodEntry(CommonConstants.EMPTY_STRING);

		validationFilePath = BUNDLE
				.getString(ValidatorConstant.VALIDATION_FILE_PATH);
		final String validationConfigFilePath = BUNDLE
				.getString(ValidatorConstant.VALIDATION_CONFIG_FILE_NAME);

		if (validationConfigFilePath != null) {
			validationConfig = new ValidationConfigRuleConverter()
					.getValidationPathConfig(validationConfigFilePath,
							CommonConstants.EMPTY_STRING); // Since there will
															// be no value for
			// correlationId, so passing empty string value
		}
		LOGGER.logMethodExit(startTime, CommonConstants.EMPTY_STRING);
	}

}
