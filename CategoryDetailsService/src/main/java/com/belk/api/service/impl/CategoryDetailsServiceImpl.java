package com.belk.api.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.belk.api.adapter.manager.AdapterManager;
import com.belk.api.constants.CommonConstants;
import com.belk.api.constants.DomainConstants;
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
import com.belk.api.mapper.CategoryDetailsMapper;
import com.belk.api.model.categorydetails.Categories;
import com.belk.api.service.CategoryDetailsService;
import com.belk.api.util.EndecaLoader;
import com.belk.api.util.ErrorLoader;
import com.belk.api.util.PropertyReloader;
import com.belk.api.util.RequestParser;

/**
 * This class gets the details of categories from Endeca based on specified
 * request identifier. Initially it will process the input request parameters as
 * per the required Endeca format then calls appropriate service method.
 * 
 * Updated : Added few comments and changed the name of variables as part of
 * Phase2, April,14 release
 * 
 * @author Mindtree
 * @date Nov 06, 2013
 */
@Service
public class CategoryDetailsServiceImpl implements CategoryDetailsService {
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
	 * creating instance of category details mapper.
	 */
	@Autowired
	private CategoryDetailsMapper categoryDetailsMapper;

	/**
	 * creating instance of request parser.
	 */
	@Autowired
	private RequestParser requestParser;

	/**
	 * Default Constructor.
	 */
	public CategoryDetailsServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param adapterManager
	 *            the adapterManager to set
	 */
	public final void setAdapterManager(final AdapterManager adapterManager) {
		this.adapterManager = adapterManager;
	}

	/**
	 * @param requestParser
	 *            the requestParser to set
	 */
	public final void setRequestParser(final RequestParser requestParser) {
		this.requestParser = requestParser;
	}

	/**
	 * @param categoryDetailsMapper
	 *            the categoryDetailsMapper to set
	 */
	public final void setCategoryDetailsMapper(
			final CategoryDetailsMapper categoryDetailsMapper) {
		this.categoryDetailsMapper = categoryDetailsMapper;
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
	 * The method to get the category details based on the identifiers passed in
	 * the request.
	 * 
	 * @param uriMap
	 *            map of query parameters
	 * @param correlationId
	 *            to track the request
	 * @return Categories
	 * @throws BaseException
	 *             The domain level exception thrown from this method
	 * @throws ServiceException
	 *             The Service level exception thrown from this method
	 * @throws AdapterException
	 *             The Adapter level exception thrown from this method
	 */
	@Override
	public final Categories getCategoryDetails(
			final Map<String, List<String>> uriMap, final String correlationId)
					throws BaseException, ServiceException, AdapterException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("Request uri map is " + uriMap, correlationId);
		Categories categories = null;
		List<Map<String, String>> resultList = null;
		final List<List<Map<String, String>>> responseList = new ArrayList<List<Map<String, String>>>();
		final Map<String, String> endecaPropertiesMap = this.endecaLoader
				.getEndecaPropertiesMap();
		if (uriMap.containsKey(CommonConstants.REQUEST_TYPE)) {
			uriMap.remove(CommonConstants.REQUEST_TYPE);
		}
		final Map<String, String> requestMapURI = this.requestParser
				.processRequestAttribute(uriMap, correlationId);
		requestMapURI.put(EndecaConstants.DIM, CommonConstants.TRUE_VALUE);
		// Check if the request is to send sub category Details, then
		// 'Refinements required' value
		// is set to true in requestMap.
		if (uriMap.containsKey(CommonConstants.SUB_CATEGORY)) {
			requestMapURI.put(EndecaConstants.REFINEMENTS_REQUIRED_LEVEL,
					CommonConstants.TRUE_VALUE);
			requestMapURI.remove(CommonConstants.SUB_CATEGORY);
		}
		//OPTIONS Parameter to be added in future
		final Map<String, List<String>> optionNodes = null;
		final EndecaAdapter endecaAdapter = (EndecaAdapter) this.adapterManager
				.getAdapter();
		/*
		 * Checking for multiselect value enabled or not in endeca properties
		 * file, if value is false then do multiple calls to endeca based on the
		 * requestMap.
		 */
		if (!endecaPropertiesMap.get(EndecaConstants.MULTIVALUE_ENABLED)
				.equals(CommonConstants.TRUE_VALUE)) {
			final String[] categoryIds = requestMapURI
					.get(RequestAttributeConstant.CATEGORY_ID).toString()
					.split(CommonConstants.COMMA_SEPERATOR);
			// Multiple values are present for categoryId.
			for (int catLength = 0; catLength < categoryIds.length; catLength++) {
				requestMapURI.put(RequestAttributeConstant.CATEGORY_ID,
						categoryIds[catLength]);
				// Multiple call to endeca based on list of category present in
				// request map.
				resultList = endecaAdapter
						.service(requestMapURI, optionNodes, correlationId);
				if (resultList != null && !resultList.isEmpty()) {
					responseList.add(resultList);
				}
			}
		} else {
			resultList = endecaAdapter.service(requestMapURI, optionNodes, correlationId);
			// Each result from endeca is added into the List.
			if (resultList != null && !resultList.isEmpty()) {
				responseList.add(resultList);
			}
		}
		if (!responseList.isEmpty()) {
			LOGGER.debug("Response list size: " + responseList.size(),
					correlationId);
			categories = this.populateCategories(responseList, correlationId);
		} else {
			LOGGER.debug("Response list is null ", correlationId);
		}

		LOGGER.logMethodExit(startTime, correlationId);
		return categories;

	}

	/**
	 * Method to populate categories.
	 * 
	 * @param responseList
	 *            Result set from endeca
	 * @param correlationId
	 *            to track the request
	 * @return categories
	 * @throws BaseException
	 *             The exception thrown from this method.
	 */
	private Categories populateCategories(
			final List<List<Map<String, String>>> responseList,
			final String correlationId) throws BaseException {
		Categories categories = null;
		for (List<Map<String, String>> response : responseList) {
			for (Map<String, String> map : response) {
				if (map.containsKey(DomainConstants.DIMENSION_KEY)) {
					categories = this.categoryDetailsMapper
							.convertToCategoryDetailsPojo(responseList,
									correlationId);
					break;
				}
			}
		}

		return categories;
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
