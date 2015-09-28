package com.belk.api.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;

import com.belk.api.constants.CommonConstants;
import com.belk.api.constants.ErrorConstants;
import com.belk.api.exception.BaseException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;

/**
 * The class to load property file into a map.
 * 
 * @author Mindtree
 * @date March 13, 2014
 */
public class PropertyLoader {

	/**
	 * Creating logger instance.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	/**
	 * requestParser is to create instance of RequestParser.
	 */
	@Autowired
	RequestParser requestParser;

	/**
	 * @param requestParser
	 *            the requestParser to set
	 */
	public final void setRequestParser(final RequestParser requestParser) {
		this.requestParser = requestParser;
	}

	/**
	 * This method reads the property file passed as input and puts it into a
	 * map.
	 * 
	 * @param filePath
	 *            - property file path.
	 * @param correlationId
	 *            - correlationId from request
	 * @return property map
	 * @throws BaseException
	 *             - Exception thrown from Common Layer
	 */
	public final Map<String, String> loadProperty(final String filePath, final String correlationId) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
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
		} catch (IOException e) {
			throw new BaseException();
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return propertyMap;
	}

	/**
	 * This method updates value in the map supplied for multiple keys passed in the request.
	 * 
	 * @param propertiesMap
	 *            - map to be updated.
	 * @param propertiesList
	 *            - list containing multiple key:value pair which needs to be updated.
	 * @param correlationId
	 *            - correlationId from request
	 * @return updated property map
	 * @throws BaseException
	 *             - Exception thrown from Common Layer
	 */
	public final Map<String, String> updateProperty(final Map<String, String> propertiesMap, final List<String> propertiesList,
			final String correlationId) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		Map<String, String> propertyMap = new HashMap<String, String>();
		Map<String, String> parsedMap = new HashMap<String, String>();
		final Map<String, List<String>> requestMap = new HashMap<String, List<String>>();
		requestMap.put(CommonConstants.KEYWORD, propertiesList);
		parsedMap = this.requestParser.processRequestAttribute(requestMap, correlationId);
		propertyMap = propertiesMap;
		String value = null;
		for (Object key : parsedMap.keySet()) {
			/*The following lines of code have been added only to handle the
			 * bluemartini URL containing the string http:// in it */
			if (propertyMap.containsKey(key)) {
				if (((String) key).equalsIgnoreCase(CommonConstants.PATTERN_DETAILS_URL)) {
					value = CommonConstants.HTTP_PREPEND_STRING + parsedMap.get(key);
				} else {
					value = parsedMap.get(key);
				}
				propertyMap.put(key.toString(), value);
			} else {
				LOGGER.debug("Failed to find a match for the key: " + (String) key, correlationId);
				throw new BaseException(String.valueOf(ErrorConstants.ERROR_CODE_11523), correlationId);
			}
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return propertyMap;
	}

}
