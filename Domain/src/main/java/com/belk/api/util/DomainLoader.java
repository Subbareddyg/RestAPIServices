package com.belk.api.util;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.belk.api.constants.CommonConstants;
import com.belk.api.exception.BaseException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;

/**
 * The class to load domain.properties into a map during server start up.
 * 
 * @author Mindtree
 * @date March 06, 2014
 */
@Service
public class DomainLoader extends PropertyLoader implements InitializingBean {

	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	private Map<String, String> domainPropertiesMap;

	private String filePath;

	@Override
	public final void afterPropertiesSet() throws BaseException {
		final String correlationId = CommonConstants.EMPTY_STRING;
		final long startTime = LOGGER.logMethodEntry(correlationId);
		this.filePath = CommonUtil.getConfigurationFilePath(
				CommonConstants.DOMAIN_RESOURCE_BUNDLE, CommonConstants.DOMAIN,
				correlationId);
		this.domainPropertiesMap = loadProperty(this.filePath, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * @return the domainPropertiesMap
	 */
	public final Map<String, String> getDomainPropertiesMap() {
		return this.domainPropertiesMap;
	}

	/**
	 * @param domainPropertiesMap
	 *            the domainPropertiesMap to set
	 */
	public final void setDomainPropertiesMap(final Map<String, String> domainPropertiesMap) {
		this.domainPropertiesMap = domainPropertiesMap;
	}

}