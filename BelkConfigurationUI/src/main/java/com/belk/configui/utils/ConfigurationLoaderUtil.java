/**
 * 
 */
package com.belk.configui.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.belk.api.constants.CommonConstants;
import com.belk.api.exception.BaseException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;

/**
 * @author Mindtree
 * @date Apr 14, 2014
 * 
 */
@Service
public class ConfigurationLoaderUtil implements InitializingBean {

	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	private String filePath;
	private Map<String, String> baseUrlMap;

	/**
	 * @return the baseUrlMap
	 */
	public final Map<String, String> getBaseUrlMap() {
		return this.baseUrlMap;
	}

	/**
	 * @param baseUrlMap
	 *            the baseUrlMap to set
	 */
	public final void setBaseUrlMap(final Map<String, String> baseUrlMap) {
		this.baseUrlMap = baseUrlMap;
	}

	@Override
	public final void afterPropertiesSet() throws Exception {
		final String correlationId = CommonConstants.EMPTY_STRING;
		this.filePath = this.getConfigurationFile(
				CommonConstants.CONFIG_UI_URL, correlationId);
		this.setBaseUrlMap(this.loadProperty(this.filePath, correlationId));
	}

	/**
	 * Method to load the property map from an externally located property file.
	 * 
	 * @param filePath
	 *            External filepath from where the property file is to be read
	 * @param correlationId
	 *            to track the request
	 * @return Map of properties
	 * @throws BaseException
	 *             Exception thrown from this method
	 */
	private Map<String, String> loadProperty(final String filePath,
			final String correlationId) throws BaseException {
		final Map<String, String> propertyMap = new HashMap<String, String>();
		final Properties props = new Properties();
		try {
			final File file = new File(filePath);
			final InputStream inputStream = new FileInputStream(file);
			props.load(inputStream);
			for (Object key : props.keySet()) {
				final String keyStr = key.toString();
				propertyMap.put(keyStr, props.getProperty(keyStr));
			}
		} catch (IOException error) {
			LOGGER.error(error, correlationId);
			throw new BaseException();
		}
		return propertyMap;
	}

	/**
	 * Method to get the configuration file path.
	 * 
	 * @param fileName
	 *            The file to be loaded
	 * @param correlationId
	 *            to track the request
	 * @return The physical path for the file requested
	 */
	private String getConfigurationFile(final String fileName,
			final String correlationId) {
		final StringBuffer finalFilePath = new StringBuffer();
		final ResourceBundle bundle = ResourceBundle
				.getBundle(CommonConstants.CONFIGURATION_UI_RESOURCE_BUNDLE);
		final String propertyFilePath = bundle
				.getString("configuration.baseUrls.path");
		finalFilePath.append(propertyFilePath);
		finalFilePath.append("/");
		finalFilePath.append(fileName);
		return finalFilePath.toString();

	}

}
