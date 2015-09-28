/**
 * 
 */
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
 * The class to load security.properties into a map during server start up.
 * 
 * @author Mindtree
 * @date 29 May, 2014
 */
@Service
public class SecurityLoader extends PropertyLoader implements
InitializingBean, LoaderManager {

	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	private String filePath;
	private Map<String, String> securityPropertiesMap;

	/**
	 * @return the securityPropertiesMap
	 */
	public final Map<String, String> getSecurityPropertiesMap() {
		return this.securityPropertiesMap;
	}

	/**
	 * @param securityPropertiesMap
	 *            the securityPropertiesMap to set
	 */
	public final void setSecurityPropertiesMap(
			final Map<String, String> securityPropertiesMap) {
		this.securityPropertiesMap = securityPropertiesMap;
	}

	@Override
	public final void updateEntirePropertyMap(final String correlationId)
			throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		this.afterPropertiesSet();
		LOGGER.logMethodExit(startTime, correlationId);
	}

	@Override
	public final void updateMultiplePropertyInMap(
			final List<String> propertiesList,
			final String correlationId) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		this.securityPropertiesMap = updateProperty(this.securityPropertiesMap,
				propertiesList, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	@Override
	public final void afterPropertiesSet() throws BaseException {
		final String correlationId = CommonConstants.EMPTY_STRING;
		this.filePath = CommonUtil.getConfigurationFilePath(
				CommonConstants.SECURITY_RESOURCE_BUNDLE,
				CommonConstants.SECURITY, correlationId);
		this.setSecurityPropertiesMap(loadProperty(this.filePath,
				correlationId));

	}

}
