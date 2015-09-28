package com.belk.api.util;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.belk.api.adapter.constants.BlueMartiniAdapterConstants;
import com.belk.api.constants.CommonConstants;
import com.belk.api.exception.BaseException;
import com.belk.api.loader.manager.LoaderManager;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;

/**
 * The class to load httpConnection.properties into a map during server start
 * up.
 * 
 * @author Mindtree
 * @date March 06, 2014
 */
@Service
public class HttpConnectionLoader extends PropertyLoader implements InitializingBean, LoaderManager {

	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	private Map<String, String> httpConnectionPropertiesMap;

	private String filePath;

	@Override
	public final void afterPropertiesSet() throws BaseException {
		final String correlationId = CommonConstants.EMPTY_STRING;
		final long startTime = LOGGER.logMethodEntry(correlationId);
		this.filePath = CommonUtil.getConfigurationFilePath(BlueMartiniAdapterConstants.HTTP_RESOURCE_BUNDLE,
				CommonConstants.BLUE_MARTINI_ADAPTER, correlationId);
		this.httpConnectionPropertiesMap = loadProperty(this.filePath, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * @return the httpConnectionPropertiesMap
	 */
	public final Map<String, String> getHttpConnectionPropertiesMap() {
		return this.httpConnectionPropertiesMap;
	}

	/**
	 * @param httpConnectionPropertiesMap
	 *            the httpConnectionPropertiesMap to set
	 */
	public final void setHttpConnectionPropertiesMap(final Map<String, String> httpConnectionPropertiesMap) {
		this.httpConnectionPropertiesMap = httpConnectionPropertiesMap;
	}
	
	@Override
	public final void updateMultiplePropertyInMap(
			final List<String> propertiesList, final String correlationId)
			throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		this.httpConnectionPropertiesMap = updateProperty(this.httpConnectionPropertiesMap,
				propertiesList, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	@Override
	public final void updateEntirePropertyMap(final String correlationId)
			throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		this.afterPropertiesSet();
		LOGGER.logMethodExit(startTime, correlationId);
	}

}