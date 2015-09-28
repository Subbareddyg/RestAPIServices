package com.belk.api.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.belk.api.adapter.manager.AdapterManager;
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
import com.belk.api.mapper.CategoryProductMapper;
import com.belk.api.model.categoryproduct.Categories;
import com.belk.api.service.CategoryProductService;
import com.belk.api.util.EndecaLoader;
import com.belk.api.util.ErrorLoader;
import com.belk.api.util.PropertyReloader;
import com.belk.api.util.RequestParser;

/**
 * This class searches/provides the high level product details from Endeca based
 * on request parameter(category id) within the given request uri map. Initially
 * it will process the input request parameters as per the required Endeca
 * format, then calls appropriate service method.
 * 
 * Updated : Added few comments and changed the name of variables as part of
 * Phase2, April,14 release
 * 
 * @author Mindtree
 * @date Oct 13, 2013
 */

@Service
public class CategoryProductServiceImpl implements CategoryProductService {
	/**
	 * creating instance of logger.
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
	 * creating instance of Endeca Loader class.
	 */
	@Autowired
	EndecaLoader endecaLoader;

	/**
	 * mailClient is the utility class for sending mails.
	 */
	@Autowired
	MailClient mailClient;

	/**
	 * creating instance of adapter manager.
	 */
	@Autowired
	private AdapterManager adapterManager;

	/**
	 * creating instance of request parser.
	 */
	@Autowired
	private RequestParser requestParser;

	/**
	 * creating instance of category product mapper.
	 */
	@Autowired
	private CategoryProductMapper categoryProductMapper;

	/**
	 * Default Constructor.
	 */
	public CategoryProductServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param requestParser
	 *            the requestParser to set
	 */
	public final void setRequestParser(final RequestParser requestParser) {
		this.requestParser = requestParser;
	}

	/**
	 * @param adapterManager
	 *            The adapterManager to set
	 */
	public final void setAdapterManager(final AdapterManager adapterManager) {
		this.adapterManager = adapterManager;
	}

	/**
	 * @param categoryProductMapper
	 *            The categoryProductMapper to set
	 */

	public final void setCategoryProductMapper(
			final CategoryProductMapper categoryProductMapper) {
		this.categoryProductMapper = categoryProductMapper;
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
	 * setter method for the endecaLoader.
	 * 
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
	 * The method to search/provide the high level product details based on
	 * query parameters(XML/JSON) and path parameter(categoryId) for the given
	 * criteria.
	 * 
	 * @param categoryProductDetailsUriMap
	 *            map of query parameters
	 * @param correlationId
	 *            to track the request
	 * @return category/list of category with category name, category id, parent
	 *         category and high level product details for the given category
	 *         identifier/identifiers
	 * 
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 * @throws BaseException
	 *             The domain level exception thrown from this method
	 * 
	 */

	@Override
	public final Categories getCategoryProducts(
			final Map<String, List<String>> categoryProductDetailsUriMap,
			final String correlationId) throws BaseException, AdapterException {

		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("Request uri map is " + categoryProductDetailsUriMap,
				correlationId);
		final Map<String, String> endecaPropertiesMap = this.endecaLoader
				.getEndecaPropertiesMap();
		Categories categoryProducts = null;
		List<Map<String, String>> categoryProductEndecaResultList = null;

		if (categoryProductDetailsUriMap
				.containsKey(CommonConstants.REQUEST_TYPE)) {
			categoryProductDetailsUriMap.remove(CommonConstants.REQUEST_TYPE);
		}
		// Parse the request
		final Map<String, String> categoryProductRequestMapURI = this.requestParser
				.prepareRequest(categoryProductDetailsUriMap, correlationId);
		final List<List<Map<String, String>>> categoryProductResponseList = new ArrayList<List<Map<String, String>>>();

		/*
		 * To get the dimension values(category id, category name, parent
		 * category) and high level product details for the given category id,
		 * we have to set dim value to true and NavErecsPerAggrerec to 1. Here
		 * we do not need refinements.
		 */

		categoryProductRequestMapURI.put(EndecaConstants.NAVERECS_PER_AGGREREC,
				String.valueOf(EndecaConstants.FLAG_VALUE_ONEPRODUCT));
		categoryProductRequestMapURI.put(EndecaConstants.DIM,
				CommonConstants.TRUE_VALUE);
		final EndecaAdapter endecaAdapter = (EndecaAdapter) this.adapterManager
				.getAdapter();

		final String categoryIds = categoryProductRequestMapURI
				.get(RequestAttributeConstant.CATEGORY_ID);
		//OPTIONS Parameter to be added in future
		final Map<String, List<String>> optionNodes = null;
		// separate comma for the multiple category ids in the request map
		if (categoryIds.contains(CommonConstants.COMMA_SEPERATOR)) {
			// If flag multi value is enabled then do multiple queries to endeca
			if (!endecaPropertiesMap.get(EndecaConstants.MULTIVALUE_ENABLED)
					.equals(CommonConstants.TRUE_VALUE)) {
				final String[] catIds = categoryIds
						.split(CommonConstants.COMMA_SEPERATOR);
				for (int n = 0; n < catIds.length; n++) {
					categoryProductRequestMapURI.put(
							RequestAttributeConstant.CATEGORY_ID, catIds[n]);
					// Calling service method to get the result from Endeca
					categoryProductEndecaResultList = endecaAdapter.service(
							categoryProductRequestMapURI, optionNodes, correlationId);
					if (categoryProductEndecaResultList != null
							&& !categoryProductEndecaResultList.isEmpty()) {
						categoryProductResponseList
								.add(categoryProductEndecaResultList);
					}
				}
			}
		} else {
			// Calling service method to get the result from Endeca
			categoryProductEndecaResultList = endecaAdapter.service(
					categoryProductRequestMapURI, optionNodes, correlationId);
			if (categoryProductEndecaResultList != null
					&& !categoryProductEndecaResultList.isEmpty()) {
				categoryProductResponseList
						.add(categoryProductEndecaResultList);
			}
		}
		LOGGER.info("ResultList " + categoryProductEndecaResultList,
				correlationId);

		// Calling populateCategoriesMethod that returns the category details
		// pojo
		categoryProducts = this.populateCategories(categoryProductResponseList,
				correlationId);

		LOGGER.logMethodExit(startTime, correlationId);
		return categoryProducts;
	}

	/**
	 * Method to populate categoryProducts.
	 * 
	 * @param responseList
	 *            to set
	 * @param correlationId
	 *            - to track the request
	 * @return categories
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private Categories populateCategories(
			final List<List<Map<String, String>>> responseList,
			final String correlationId) throws BaseException {
		Categories categoryProductDetailsPojo = null;
		if (responseList != null && !responseList.isEmpty()) {
			for (List<Map<String, String>> response : responseList) {
				if (response != null && response.size() > 1) {
					// calling the categoryProductMapper class which contains
					// the methods to returns java objects
					categoryProductDetailsPojo = this.categoryProductMapper
							.convertToCategoryPojo(responseList, correlationId);
				}
			}
		}
		return categoryProductDetailsPojo;
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
}
