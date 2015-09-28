package com.belk.api.service.impl;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.belk.api.adapter.manager.AdapterManager;
import com.belk.api.cache.CacheManager;
import com.belk.api.constants.CommonConstants;
import com.belk.api.constants.DomainConstants;
import com.belk.api.constants.EndecaConstants;
import com.belk.api.constants.ErrorConstants;
import com.belk.api.constants.RequestAttributeConstant;
import com.belk.api.dao.CoremetricsDAO;
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
import com.belk.api.service.AdminService;
import com.belk.api.transformer.json.JSONProcessor;
import com.belk.api.transformer.xml.XMLProcessor;
import com.belk.api.util.CoremetricsUtil;
import com.belk.api.util.EndecaLoader;
import com.belk.api.util.ErrorLoader;
import com.belk.api.util.PropertyReloader;

/**
 * This class is for serving the implementation of the Admin Services. Updated :
 * Added few comments and changed the name of variables as part of Phase2,
 * April,14 release Update: The implementation has been updated to include
 * methods for the configuration framework.
 * 
 * @author Mindtree
 * @date Nov 29, 2013
 */
@Service
public class AdminServiceImpl implements AdminService {
	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	/**
	 * mailClient is the utility class for sending mails.
	 */
	@Autowired
	MailClient mailClient;

	/**
	 * creating instance of Error Loader class.
	 */
	@Autowired
	private ErrorLoader errorLoader;

	/**
	 * creating instance of Property Reloader class.
	 */
	@Autowired
	private PropertyReloader propertyReloader;

	/**
	 * map of leaf node categories.
	 */
	private Map<String, Category> leafCategoryMap;

	/**
	 * creating instance of adapter manager.
	 */
	@Autowired
	private AdapterManager adapterManager;

	/**
	 * creating instance of cache manager.
	 */
	@Autowired
	private CacheManager cacheManager;
	
	/**
	 * creating instance of Coremetrics DAO.
	 */
	@Autowired
	private CoremetricsDAO coremetricsDAO;
	/**
	 * creatinginstance of XMLProcessor.
	 */
	@Autowired
	private XMLProcessor xmlProcessor;

	/**
	 * creating instance of JSONProcessor.
	 */
	@Autowired
	private JSONProcessor jsonProcessor;

	/**
	 * creating instance of EndecaLoader.
	 */
	@Autowired
	private EndecaLoader endecaLoader;
	
	
	/**
	 * Default Constructor.
	 */
	public AdminServiceImpl() {
	}

	/**
	 * @return the leafCategoryMap
	 */
	public final Map<String, Category> getLeafCategoryMap() {
		return this.leafCategoryMap;
	}

	/**
	 * @param leafCategoryMap
	 *            the leafCategoryMap to set
	 */
	public final void setLeafCategoryMap(
			final Map<String, Category> leafCategoryMap) {
		this.leafCategoryMap = leafCategoryMap;
	}

	/**
	 * @param cacheManager
	 *            the cacheManager to set
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
	 * @param xmlProcessor
	 *            the xmlProcessor to set
	 */
	public final void setXmlProcessor(final XMLProcessor xmlProcessor) {
		this.xmlProcessor = xmlProcessor;
	}

	/**
	 * @param mailClient
	 *            the mailClient to set
	 */
	public final void setMailClient(final MailClient mailClient) {
		this.mailClient = mailClient;
	}

	/**
	 * Method to fetch the dimensions and populate the category objects in the
	 * cache.
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
	 */

	@Override
	public final void populateCategoriesInCache(
			final Map<String, String> requestMapURI, final String correlationId)
			throws BaseException, AdapterException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("Request uri map is " + requestMapURI, correlationId);
		Dimension dimension = null;
		final Catalog catalog = new Catalog();
		final Map<String, String> errorPropertiesMap = this.errorLoader
				.getErrorPropertiesMap();
		final List<Category> resultCategory = new LinkedList<Category>();
		final String showproduct = requestMapURI
				.get(RequestAttributeConstant.SHOW_PRODUCT);

		requestMapURI.put(CommonConstants.TYPE_OF_QUERY_REQUIRED,
				EndecaConstants.DIMENSION_QUERY);

		final EndecaAdapter endecaAdapter = (EndecaAdapter) this.adapterManager
				.getAdapter();

		// removing format from the requestMapURI as it has no relevance in
		// search
		requestMapURI.remove(CommonConstants.REQUEST_TYPE);

		// call to get the dimension objects
		final Map<Long, Dimension> resultDimensionMap = endecaAdapter
				.makeDimensionQuery(requestMapURI, correlationId);
		LOGGER.debug(
				" Result Dimension List size " + resultDimensionMap.size(),
				correlationId);

		dimension = resultDimensionMap.get(Long.parseLong(requestMapURI
				.get(RequestAttributeConstant.CATEGORY_ID)));

		// if there are no dimensions available for the specified category then
		// ServiceException is thrown
		if (null == dimension) {

			throw new ServiceException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String
							.valueOf(ErrorConstants.ERROR_CODE_11523)),
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);

		} else {
			// if dimensions are present, then Category object is loaded with
			// the dimension objects
			final Category categoryPopulate = new Category();

			this.convertDimensionToCategory(dimension, categoryPopulate,
					showproduct, correlationId);
			resultCategory.add(categoryPopulate);

			catalog.setCategories(resultCategory);
			final ByteArrayOutputStream xmlStream = this.xmlProcessor
					.buildXMLResponse(catalog, Catalog.class,
							DomainConstants.CATALOG_BINDING, correlationId);
			final String xmlResponse = new String(xmlStream.toByteArray());

			final String jsonResponse = this.jsonProcessor.buildJSONResponse(
					catalog, correlationId);

			// setting the XML and JSON
			// Added the below code for performance improvement of Catalog API
			this.cacheManager.set(
					String.valueOf(this.endecaLoader.getEndecaPropertiesMap()
							.get(EndecaConstants.CATALOG_HOME_DIMENSION_VALUE))
							+ CommonConstants.UNDERSCORE
							+ CommonConstants.MEDIA_TYPE_XML, xmlResponse,
					correlationId);
			LOGGER.info("XML format is set in the Cache", correlationId);
			this.cacheManager.set(
					String.valueOf(this.endecaLoader.getEndecaPropertiesMap()
							.get(EndecaConstants.CATALOG_HOME_DIMENSION_VALUE))
							+ CommonConstants.UNDERSCORE
							+ CommonConstants.MEDIA_TYPE_JSON, jsonResponse,
					correlationId);
			LOGGER.info("JSON format is set in the Cache", correlationId);

			// setting the leaf categories in the cache
			for (final Entry<String, Category> mapEntries : this.leafCategoryMap
					.entrySet()) {
				this.cacheManager.set(mapEntries.getKey(),
						mapEntries.getValue(), correlationId);
			}
		}
		LOGGER.info(" Categories has been set in Cache ", correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
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
	 */
	private Category convertDimensionToCategory(final Dimension dimension,
			final Category category, final String showproduct,
			final String correlationId) {

		// setting the category properties with the dimension values.
		category.setCategoryId(dimension.getDimensionId());
		category.setName(Jsoup.parse(dimension.getName()).text());
		category.setCategoryAttributes(dimension.getDimensionAttributes());
		category.setParentCategoryId(dimension.getParentDimensionId());

		final List<Category> accumulatedList = new LinkedList<Category>();
		if (dimension.hasDimensions()) {

			// for each subdimension create category object
			for (final Dimension subDimension : dimension.getDimensions()) {
				final Category currentDimensionCategory = new Category();
				accumulatedList.add(this.convertDimensionToCategory(
						subDimension, currentDimensionCategory, showproduct,
						correlationId));
				category.setSubCategories(accumulatedList);
			}
		} else {
			// for leaf dimension populate product data to the category object
			if (showproduct.equals(CommonConstants.TRUE_VALUE)) {
				category.setProducts(dimension.getProducts());

				if (null == this.leafCategoryMap) {
					this.leafCategoryMap = new HashMap<String, Category>();
				}
				this.leafCategoryMap.put(
						String.valueOf(category.getCategoryId()), category);
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
	
	/** 
	 * This method calls the Util class which queries the DB and loads the results to Cache
	 * @param correlationId
	 * 		Correlation Id for the request
	 * @throws ServiceException
	 */
	public final void loadCoremetricsData(final String correlationId) throws ServiceException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final Map<String, String> errorPropertiesMap = this.errorLoader
				.getErrorPropertiesMap();
		try {
			final CoremetricsUtil coremetricsUtil	= new CoremetricsUtil();
			coremetricsUtil.setCoremetricsDao(this.coremetricsDAO);
			coremetricsUtil.getCoremetricsForPdp(correlationId);
			LOGGER.info("Coremetrics job completed", correlationId);
		} catch (BaseException e) {
			throw new ServiceException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String
							.valueOf(ErrorConstants.ERROR_CODE_11523)),
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}
}
