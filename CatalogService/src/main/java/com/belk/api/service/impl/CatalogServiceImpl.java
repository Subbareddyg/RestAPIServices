package com.belk.api.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.belk.api.adapter.manager.AdapterManager;
import com.belk.api.cache.CacheManager;
import com.belk.api.constants.CommonConstants;
import com.belk.api.constants.EndecaConstants;
import com.belk.api.constants.ErrorConstants;
import com.belk.api.constants.RequestAttributeConstant;
import com.belk.api.exception.AdapterException;
import com.belk.api.exception.BaseException;
import com.belk.api.exception.ServiceException;
import com.belk.api.impl.EndecaAdapter;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.mail.MailClient;
import com.belk.api.model.Dimension;
import com.belk.api.model.catalog.Catalog;
import com.belk.api.model.catalog.Category;
import com.belk.api.service.CatalogService;
import com.belk.api.util.EndecaLoader;
import com.belk.api.util.ErrorLoader;
import com.belk.api.util.PropertyReloader;

/**
 * This class searches the catalog details from Endeca based on request
 * parameter with the given criteria. Jsoup.parse() has been used to handle the
 * special characters as required.
 * 
 * Update: Added few comments and changed the name of variables as part of
 * Phase2, April,14 release. Update: The implementation has been updated to
 * include methods for the configuration framework.
 * 
 * @author Mindtree
 * @date Nov 25, 2013
 */
@Service
public class CatalogServiceImpl implements CatalogService {

	/**
	 * LOGGER to get the logs.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	/**
	 * creating instance of Error Loader class.
	 */
	@Autowired
	ErrorLoader errorLoader;

	/**
	 * creating instance of Property Reloader class.
	 */
	@Autowired
	PropertyReloader propertyReloader;

	/**
	 * mailClient is the utility class for sending mails.
	 */
	@Autowired
	MailClient mailClient;

	/**
	 * private instance of AdapterManager.
	 */
	@Autowired
	private AdapterManager adapterManager;
	/**
	 * private instance of CacheManager.
	 */
	@Autowired
	private CacheManager cacheManager;

	/**
	 * private instance of EndecaLoader.
	 */
	@Autowired
	private EndecaLoader endecaLoader;

	/**
	 * Default constructor.
	 */
	public CatalogServiceImpl() {
	}

	/**
	 * @param cacheManager
	 *            the cacheManager to set.
	 */
	public final void setCacheManager(final CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	/**
	 * for setting the autowired object.
	 * 
	 * @param adapterManager
	 *            the adapterManager to set
	 */
	public final void setAdapterManager(final AdapterManager adapterManager) {
		this.adapterManager = adapterManager;
	}

	/**
	 * setter method for the errorLoader.
	 * 
	 * @param errorLoader
	 *            the errorLoader to set
	 */
	public final void setErrorLoader(final ErrorLoader errorLoader) {
		this.errorLoader = errorLoader;
	}

	/**
	 * @param propertyReloader
	 *            the propertyReloader to set
	 */
	public final void setPropertyReloader(
			final PropertyReloader propertyReloader) {
		this.propertyReloader = propertyReloader;
	}

	/**
	 * @param endecaLoader
	 *            the endecaLoader to set
	 */
	public final void setEndecaLoader(final EndecaLoader endecaLoader) {
		this.endecaLoader = endecaLoader;
	}

	/**
	 * @param mailClient
	 *            the mailClient to set
	 */
	public final void setMailClient(final MailClient mailClient) {
		this.mailClient = mailClient;
	}

	/**
	 * Method to fetch the dimensions and populate the category objects.
	 * 
	 * @param requestMapURI
	 *            has the information like catalogId, categoryId, showproduct
	 *            field
	 * @param correlationId
	 *            correlationId from the request
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 * @return catalog object
	 */
	@Override
	public final Catalog getCatalog(final Map<String, String> requestMapURI,
			final String correlationId) throws BaseException, AdapterException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("Request uri map is " + requestMapURI, correlationId);
		Catalog catalog = new Catalog();
		final List<Category> resultCategory = new LinkedList<Category>();
		final Map<String, String> errorPropertiesMap = this.errorLoader
				.getErrorPropertiesMap();
		Dimension dimension = null;
		final Long key = Long.parseLong(requestMapURI
				.get(RequestAttributeConstant.CATEGORY_ID));

		final String showproduct = requestMapURI
				.get(RequestAttributeConstant.SHOW_PRODUCT);

		requestMapURI.put(RequestAttributeConstant.SHOW_PRODUCT,
				CommonConstants.FALSE_VALUE);
		requestMapURI.put(CommonConstants.TYPE_OF_QUERY_REQUIRED,
				EndecaConstants.DIMENSION_QUERY);

		final Category categoryForCache = this.cacheManager.get(
				requestMapURI.get(RequestAttributeConstant.CATEGORY_ID),
				Category.class, correlationId);
		// first checked in the cache
		if (null != categoryForCache) {
			if (showproduct.equals(CommonConstants.FALSE_VALUE)) {
				categoryForCache.setProducts(null);
			}
			resultCategory.add(categoryForCache);
			catalog.setCategories(resultCategory);
		} else {
			final EndecaAdapter endecaAdapter = (EndecaAdapter) this.adapterManager
					.getAdapter();
			requestMapURI.remove(CommonConstants.REQUEST_TYPE);
			final Map<Long, Dimension> resultDimensionMap = endecaAdapter
					.makeDimensionQuery(requestMapURI, correlationId);
			LOGGER.debug(
					" Result Dimension Map size " + resultDimensionMap.size(),
					correlationId);
			dimension = resultDimensionMap.get(key);

			if (null == dimension) { /*
			 * This indicates that this is an invalid
			 * category ID requested for that is neither
			 * present in Endeca nor present in the
			 * cache; Setting catalog = null so that
			 * BaseResource handles the record not found
			 * error message
			 */
				catalog = null;
			} else if (!dimension.hasDimensions()) { /*
			 * This indicates that this
			 * category is a leaf
			 * category that has been
			 * added to Endeca after
			 * cache was updated
			 */

				throw new ServiceException(
						String.valueOf(ErrorConstants.ERROR_CODE_11523),
						errorPropertiesMap.get(String
								.valueOf(ErrorConstants.ERROR_CODE_11523)),
								ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
								correlationId);
			} else {
				Category categoryPopulate = new Category();
				categoryPopulate = this.convertDimensionToCategory(dimension,
						categoryPopulate, showproduct, correlationId);
				resultCategory.add(categoryPopulate);
				catalog.setCategories(resultCategory);
			}

		}

		LOGGER.debug(" Catalog Pojo " + catalog, correlationId);

		LOGGER.logMethodExit(startTime, correlationId);
		return catalog;

	}

	/**
	 * This method converts the dimension object to the category object. Note:
	 * No loggers have been kept in this method to avoid memory overflow issues
	 * due to recursive execution of this method.
	 * 
	 * @param dimension
	 *            dimension object
	 * @param category
	 *            category object
	 * @param showproduct
	 *            conditional value sent for getting the product details
	 * @param correlationId
	 *            - correlationId from the request
	 * @return converted category object
	 * @throws ServiceException
	 *             service layer exception
	 */
	private Category convertDimensionToCategory(final Dimension dimension,
			final Category category, final String showproduct,
			final String correlationId) throws ServiceException {

		category.setCategoryId(dimension.getDimensionId());
		category.setName(Jsoup.parse(dimension.getName()).text());
		category.setCategoryAttributes(dimension.getDimensionAttributes());
		category.setParentCategoryId(dimension.getParentDimensionId());

		final List<Category> accumulatedList = new LinkedList<Category>();
		final Map<String, String> errorPropertiesMap = this.errorLoader
				.getErrorPropertiesMap();
		// recursing over the dimension if the dimension has sub dimension to
		// create category object
		if (dimension.hasDimensions()) {
			for (final Dimension subDimension : dimension.getDimensions()) {
				final Category currentDimensionCategory = new Category();
				accumulatedList.add(this.convertDimensionToCategory(
						subDimension, currentDimensionCategory, showproduct,
						correlationId));
				category.setSubCategories(accumulatedList);
			}
		} else {
			// if leaf dimension, get the category object from cache
			if (showproduct.equals(CommonConstants.TRUE_VALUE)) {
				final Category cacheCategory = this.cacheManager.get(
						String.valueOf(dimension.getDimensionId()),
						Category.class, correlationId);
				if (null != cacheCategory) {
					category.setProducts(cacheCategory.getProducts());
				} else {
					// if the leaf dimension is not available in the cache then
					// throw ServiceException
					throw new ServiceException(
							String.valueOf(ErrorConstants.ERROR_CODE_11523),
							errorPropertiesMap.get(String
									.valueOf(ErrorConstants.ERROR_CODE_11523)),
									ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
									correlationId);
				}
			}
		}
		return category;
	}

	@Override
	public final void updatePropertiesMap(final String correlationId,
			final Map<String, List<String>> updateRequestUriMap)
					throws ServiceException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final Map<String, String> errorPropertiesMap = this.errorLoader
				.getErrorPropertiesMap();
		try {
			this.propertyReloader.updatePropertyMap(updateRequestUriMap,
					correlationId);
			this.mailClient.sendEmail(updateRequestUriMap, correlationId,
					CommonConstants.SUCCESS);
		} catch (BaseException e) {
			this.mailClient.sendEmail(updateRequestUriMap, correlationId,
					CommonConstants.FAILURE);
			throw new ServiceException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String
							.valueOf(ErrorConstants.ERROR_CODE_11523)),
							ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
							correlationId);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	@Override
	public final String getCatalogAsString(
			final Map<String, String> requestMapURI, final String correlationId)
					throws BaseException, ServiceException, AdapterException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final Map<String, String> errorPropertiesMap = this.errorLoader
				.getErrorPropertiesMap();
		String catalogData = null;
		final String cacheKey = this.getCacheKeyForCatalog(requestMapURI
				.get(CommonConstants.REQUEST_TYPE), correlationId);
		catalogData = this.cacheManager.get(cacheKey, String.class,
				correlationId);
		if (null != catalogData) {
			LOGGER.logMethodExit(startTime, correlationId);
			return catalogData;

		} else {
			throw new ServiceException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String
							.valueOf(ErrorConstants.ERROR_CODE_11523)),
							ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
							correlationId);
		}
	}

	/**
	 * Method to prepare the key specific to the return type, to be fetched from
	 * cache.
	 * 
	 * @param responseType
	 *            The type for which the catalog data is to be fetched from
	 *            cache
	 *  @param correlationId  - correlationId from the request
	 * @return the key to be fetched from cache
	 */
	private String getCacheKeyForCatalog(final String responseType, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final String cacheKey = this.endecaLoader.getEndecaPropertiesMap().get(
				EndecaConstants.CATALOG_HOME_DIMENSION_VALUE)
				+ CommonConstants.UNDERSCORE + responseType.toUpperCase();
		LOGGER.logMethodExit(startTime, correlationId);
		return cacheKey;
	}
}
