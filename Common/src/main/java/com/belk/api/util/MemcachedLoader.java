package com.belk.api.util;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.belk.api.constants.CommonConstants;
import com.belk.api.exception.BaseException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;

/**
 * The class to load memcached.properties into a map during server start up.
 * 
 * @author Mindtree
 * @date March 06, 2014
 */
@Service
public class MemcachedLoader extends PropertyLoader implements InitializingBean {

	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();
	
	private Map<String, String> memcachePropertiesMap;

	private String filePath;

	@Override
	public final void afterPropertiesSet() throws BaseException {
		final String correlationId = CommonConstants.EMPTY_STRING;
		final long startTime = LOGGER.logMethodEntry(correlationId);
		this.filePath = CommonUtil.getConfigurationFilePath(CommonConstants.CACHE_RESOURCE_BUNDLE, 
				CommonConstants.COMMON, correlationId);
		this.memcachePropertiesMap = loadProperty(this.filePath, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * @return the memcachePropertiesMap
	 */
	public final Map<String, String> getMemcachePropertiesMap() {
		return this.memcachePropertiesMap;
	}

	/**
	 * @param memcachePropertiesMap
	 *            the memcachePropertiesMap to set
	 */
	public final void setMemcachePropertiesMap(final Map<String, String> memcachePropertiesMap) {
		this.memcachePropertiesMap = memcachePropertiesMap;
	}

}