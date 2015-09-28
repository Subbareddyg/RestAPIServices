package com.belk.api.util;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.belk.api.constants.CommonConstants;
import com.belk.api.exception.BaseException;
import com.belk.api.loader.manager.LoaderManager;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;

/**
 * The class to load endeca.properties into a map during server start up.
 * 
 * @author Mindtree
 * @date March 06, 2014
 */
@Service
public class EndecaLoader extends PropertyLoader implements InitializingBean,
		LoaderManager {

	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	private Map<String, String> endecaPropertiesMap;

	private String filePath;

	@Override
	public final void afterPropertiesSet() throws BaseException {
		final String correlationId = CommonConstants.EMPTY_STRING;
		final long startTime = LOGGER.logMethodEntry(correlationId);
		this.filePath = CommonUtil.getConfigurationFilePath(
				CommonConstants.ENDECA_RESOURCE_BUNDLE,
				CommonConstants.ADAPTER, correlationId);
		this.endecaPropertiesMap = loadProperty(this.filePath, correlationId);

		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * @return the endecaPropertiesMap
	 */
	public final Map<String, String> getEndecaPropertiesMap() {
		return this.endecaPropertiesMap;
	}

	/**
	 * @param endecaPropertiesMap
	 *            the endecaPropertiesMap to set
	 */
	public final void setEndecaPropertiesMap(
			final Map<String, String> endecaPropertiesMap) {
		this.endecaPropertiesMap = endecaPropertiesMap;
	}

	@Override
	public final void updateMultiplePropertyInMap(
			final List<String> propertiesList, final String correlationId)
			throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		this.endecaPropertiesMap = updateProperty(this.endecaPropertiesMap,
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