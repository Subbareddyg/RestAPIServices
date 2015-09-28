/**
 * 
 */
package com.belk.api.util;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.belk.api.constants.CommonConstants;
import com.belk.api.exception.BaseException;
import com.belk.api.loader.manager.LoaderManager;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;

/**
 * The class to load dbConnection.properties into a map during server start
 * up
 * @author AFURXG9
 *
 */
@Component("dbConnectionLoader")
public class DBConnectionLoader extends PropertyLoader implements
InitializingBean, LoaderManager {
	
	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	@Autowired
	AdapterUtil adapterUtil;

	private Map<String, String> dbConnectionPropertiesMap;

	private String filePath;

	/**
	 * @param adapterUtil
	 *            the adapterUtil to set
	 */
	public final void setAdapterUtil(final AdapterUtil adapterUtil) {
		this.adapterUtil = adapterUtil;
	}

	@Override
	public final void afterPropertiesSet() throws BaseException {
		final String correlationId = CommonConstants.EMPTY_STRING;
		final long startTime = LOGGER.logMethodEntry(correlationId);
		this.filePath = CommonUtil.getConfigurationFilePath(
				CommonConstants.DB_CONNECTION_RESOURCE_BUNDLE,
				CommonConstants.ADAPTER, correlationId);
		this.dbConnectionPropertiesMap = loadProperty(this.filePath,
				correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	
	
	@Override
	public final void updateMultiplePropertyInMap(
			final List<String> propertiesList, final String correlationId)
			throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		this.dbConnectionPropertiesMap = updateProperty(
				this.dbConnectionPropertiesMap, propertiesList,
				correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	@Override
	public final void updateEntirePropertyMap(final String correlationId)
			throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		this.afterPropertiesSet();
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * @return the dbConnectionPropertiesMap
	 */
	public final Map<String, String> getDbConnectionPropertiesMap() {
		return this.dbConnectionPropertiesMap;
	}

	/**
	 * @param dbConnectionPropertiesMap the dbConnectionPropertiesMap to set
	 */
	public final void setDbConnectionPropertiesMap(
			final Map<String, String> dbConnectionPropertiesMap) {
		this.dbConnectionPropertiesMap = dbConnectionPropertiesMap;
	}

}
