/**
 * 
 */
package com.belk.configui.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.springframework.stereotype.Service;

import com.belk.api.constants.CommonConstants;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.belk.configui.constants.ConfigUIConstants;

/**
 * @author Mindtree
 * @date Mar 24, 2014 This is a util class containing several utility methods.
 */
@Service
public class ConfigurationUtil {

	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger(CommonConstants.CONFIGURATION_WEB_APP_LOGGER);
	{
		LoggerUtil.setLogger(LOGGER);
	}

	/**
	 * This method is to form absolute path of property file passed.
	 * 
	 * @return absolute path to property file passed.
	 */
	public static final String getConfigurationFilePath() {
		final String correlationId = ConfigUIConstants.BLANK;
		String propertyFilePath = null;
		try {
			final ResourceBundle bundle = ResourceBundle
					.getBundle(ConfigUIConstants.CONFIGURATION_PROPERTY);
			propertyFilePath = bundle.getString("configuration.files.path");
		} catch (MissingResourceException error) {
			LOGGER.error(error, correlationId);
		}
		return propertyFilePath;

	}

	/**
	 * This method is to form absolute path of the validation property files.
	 * 
	 * @return absolute path to validation property file path.
	 */
	public final String getValidationFilePath() {
		final String correlationId = ConfigUIConstants.BLANK;
		String propertyFilePath = null;
		try {
			final ResourceBundle bundle = ResourceBundle
					.getBundle(ConfigUIConstants.CONFIGURATION_PROPERTY);
			propertyFilePath = bundle.getString("validation.file.path");
		} catch (MissingResourceException error) {
			LOGGER.error(error, correlationId);
		}
		return propertyFilePath;

	}

}
