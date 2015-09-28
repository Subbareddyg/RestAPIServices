package com.belk.api.util;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.belk.api.constants.CommonConstants;
import com.belk.api.exception.BaseException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;

/**
 * The class to load error.properties into a map during server start up.
 * 
 * @author Mindtree
 * @date March 06, 2014
 */
@Service
public class ErrorLoader extends PropertyLoader implements InitializingBean {

	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	private Map<String, String> errorPropertiesMap;

	private String filePath;

	@Override
	public final void afterPropertiesSet() throws BaseException {
		final String correlationId = CommonConstants.UNIQUE_CORRELATION_ID;
		final long startTime = LOGGER.logMethodEntry(correlationId);
		this.filePath = CommonUtil.getConfigurationFilePath(
				CommonConstants.ERROR_RESOURCE_BUNDLE, CommonConstants.COMMON,
				correlationId);
		this.errorPropertiesMap = loadProperty(this.filePath, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * @return the errorPropertiesMap
	 */
	public final Map<String, String> getErrorPropertiesMap() {
		return this.errorPropertiesMap;
	}

	/**
	 * @param errorPropertiesMap
	 *            the errorPropertiesMap to set
	 */
	public final void setErrorPropertiesMap(final Map<String, String> errorPropertiesMap) {
		this.errorPropertiesMap = errorPropertiesMap;
	}

}