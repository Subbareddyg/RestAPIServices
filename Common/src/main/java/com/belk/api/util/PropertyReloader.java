package com.belk.api.util;

import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.belk.api.constants.CommonConstants;
import com.belk.api.exception.BaseException;
import com.belk.api.loader.manager.LoaderManager;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;

/**
 * This class is used to dynamically identify the Loader Class based on file
 * name supplied and invoke appropriate loader.
 * 
 * @author Mindtree
 * @date March 14, 2014
 */
@Service
public class PropertyReloader implements BeanFactoryAware {

	/**
	 * Creating logger instance.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	private BeanFactory beanFactory;

	/**
	 * This method identifies the loader based on file name passed and calls
	 * appropriate loader to populate values from file into a map.
	 * 
	 * @param updateRequestUriMap
	 *            - The request map
	 * @param correlationId
	 *            - to track the request
	 * @throws BaseException
	 *             - exception thrown from base class
	 */
	public final void updatePropertyMap(final Map<String, List<String>> updateRequestUriMap, final String correlationId)
			throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		String propertyFileName;
		try {
			// Here we check whether request is to update log level.
			if (updateRequestUriMap.containsKey(CommonConstants.LOG_LEVEL)) {
				final String logLevel = StringUtils
						.collectionToCommaDelimitedString(updateRequestUriMap
								.get(CommonConstants.LOG_LEVEL));
				LOGGER.setLoggerLevel(logLevel);
			} else if (updateRequestUriMap.get(CommonConstants.FILE_NAME) != null) {
				propertyFileName = StringUtils
						.collectionToCommaDelimitedString(updateRequestUriMap
								.get(CommonConstants.FILE_NAME));
				final ResourceBundle bundle = ResourceBundle.getBundle(CommonConstants.CONFIGURATIONS_RESOURCE_BUNDLE);
				final String beanName = bundle.getString(propertyFileName);
				final Object bean = this.beanFactory.getBean(beanName);
				if (bean instanceof LoaderManager) {
					this.processUpdatePropertyMapRequest(updateRequestUriMap,
							correlationId, bean);
				} else {
					LOGGER.debug("Bean is not an instance of LoaderManager", correlationId);
					throw new BaseException();
				}
			} else {
				LOGGER.debug("Config change requested for wrong parameters", correlationId);
				throw new BaseException();
			}
		} catch (MissingResourceException e) {
			LOGGER.debug("Resource not found for bundle", correlationId);
			throw new BaseException();
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * @param updateRequestUriMap the request map
	 * @param correlationId to track the request
	 * @param bean the bean corresponding to the property file requested to be updated
	 * @throws BaseException exception thrown from this method
	 */
	private void processUpdatePropertyMapRequest(
			final Map<String, List<String>> updateRequestUriMap,
			final String correlationId, final Object bean) throws BaseException {
		List<String> propertiesList;
		final LoaderManager loaderManager = (LoaderManager) bean;
		if (updateRequestUriMap.containsKey(CommonConstants.PROPERTIES_LIST)) {
			propertiesList = updateRequestUriMap.get(CommonConstants.PROPERTIES_LIST);
			LOGGER.debug("Properties to be updated: " + propertiesList, correlationId);
			// Here we update only the map with values supplied in
			// request
			loaderManager.updateMultiplePropertyInMap(propertiesList, correlationId);
		} else if (updateRequestUriMap
				.containsKey(CommonConstants.RELOAD_DEFAULT_VALUES)
				&& CommonConstants.TRUE_VALUE
				.equals(StringUtils
						.collectionToCommaDelimitedString(updateRequestUriMap
								.get(CommonConstants.RELOAD_DEFAULT_VALUES)))) {
			LOGGER.debug("Entire property map to be reloaded", correlationId);
			// Here we update entire map corresponding to file name
			// passed.
			loaderManager.updateEntirePropertyMap(correlationId);
		} else {
			LOGGER.debug("Config change requested for file with wrong parameters", correlationId);
			throw new BaseException();
		}
	}

	@Override
	public final void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

}
