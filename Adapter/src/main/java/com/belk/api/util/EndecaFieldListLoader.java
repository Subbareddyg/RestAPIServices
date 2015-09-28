package com.belk.api.util;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.belk.api.constants.CommonConstants;
import com.belk.api.exception.BaseException;
import com.belk.api.loader.manager.LoaderManager;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;

/**
 * The class to load endecafieldlist.properties into a map during server start
 * up.
 * 
 * @author Mindtree
 * @date March 06, 2014
 */
@Service
public class EndecaFieldListLoader extends PropertyLoader implements
		InitializingBean, LoaderManager {

	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	@Autowired
	AdapterUtil adapterUtil;

	private Map<String, String> endecaFieldListpropertiesMap;


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
				CommonConstants.ENDECA_FIELDLIST_RESOURCE_BUNDLE,
				CommonConstants.ADAPTER, correlationId);
		this.endecaFieldListpropertiesMap = loadProperty(this.filePath,
				correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * @return the endecaFieldListpropertiesMap
	 */
	public final Map<String, String> getEndecaFieldListpropertiesMap() {
		return this.endecaFieldListpropertiesMap;
	}

	/**
	 * @param endecaFieldListpropertiesMap
	 *            the endecaFieldListpropertiesMap to set
	 */
	public final void setEndecaFieldListpropertiesMap(
			final Map<String, String> endecaFieldListpropertiesMap) {
		this.endecaFieldListpropertiesMap = endecaFieldListpropertiesMap;
	}

	@Override
	public final void updateMultiplePropertyInMap(
			final List<String> propertiesList, final String correlationId)
			throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		this.endecaFieldListpropertiesMap = updateProperty(
				this.endecaFieldListpropertiesMap, propertiesList,
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
}