package com.belk.api.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
import com.belk.api.mapper.SearchMapper;
import com.belk.api.model.productsearch.Search;
import com.belk.api.service.ProductSearchService;
import com.belk.api.util.ErrorLoader;
import com.belk.api.util.PropertyReloader;
import com.belk.api.util.RequestParser;

/**
 * This class searches the products from Endeca based on request parameter with
 * the given criteria. Initially it will process the input request parameters as
 * per the required Endeca format then calls appropriate service method.
 * 
 * Updated : Added few comments and loggers as part of Phase2, April,14 release
 * 
 * @author Mindtree
 * @date Jul 25, 2013
 */
@Service
public class ProductSearchServiceImpl implements ProductSearchService {

	/**
	 * Creating logger instance.
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
	 * searchMapper is to create instance of SearchMapper.
	 */
	@Autowired
	private SearchMapper searchMapper;

	/**
	 * adapterManager is to create instance of AdapterManager.
	 */
	@Autowired
	private AdapterManager adapterManager;

	/**
	 * requestParser is to create instance of RequestParser.
	 */
	@Autowired
	private RequestParser requestParser;

	/**
	 * Default constructor.
	 */
	public ProductSearchServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param searchMapper
	 *            the searchMapper to set
	 */
	public final void setSearchMapper(final SearchMapper searchMapper) {
		this.searchMapper = searchMapper;
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
	 * @param mailClient
	 *            the mailClient to set
	 */
	public final void setMailClient(final MailClient mailClient) {
		this.mailClient = mailClient;
	}

	/**
	 * searches the products based on query parameters for the given criteria.
	 * 
	 * @param searchCriteria
	 *            The request map containing the search criteria
	 * @param correlationId
	 *            to track the request
	 * @return List of products
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * @throws ServiceException
	 *             exception thrown from Service layer
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 */

	@Override
	public final Search searchProducts(
			final Map<String, List<String>> searchCriteria,
			final String correlationId) throws BaseException, ServiceException,
			AdapterException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("Request uri map is " + searchCriteria, correlationId);
		Search searchList = null;
		if (searchCriteria.containsKey(RequestAttributeConstant.ATTR)) {
			final String inputParameter = StringUtils
					.collectionToCommaDelimitedString(searchCriteria
							.get(RequestAttributeConstant.ATTR));

			final String[] pipeSeparated = inputParameter.split("\\|");
			for (final String pipeSeparatedString : pipeSeparated) {
				if (pipeSeparatedString
						.contains(RequestAttributeConstant.PRICE)) {
					this.buildPriceRange(searchCriteria, pipeSeparatedString,
							correlationId);
				}
			}
		}
		// Added this condition to remove dim attribute if its value is false
		if (searchCriteria.containsKey(RequestAttributeConstant.DIM)) {
			final List<String> dimvalue = searchCriteria
					.get(RequestAttributeConstant.DIM);
			if (dimvalue.get(0).equalsIgnoreCase(CommonConstants.FALSE_VALUE)) {
				searchCriteria.remove(RequestAttributeConstant.DIM);
			}
		}
		final Map<String, String> requestMapURI = this.requestParser
				.processRequestAttribute(searchCriteria, correlationId);
		List<Map<String, String>> resultMap = null;
		final EndecaAdapter endecaAdapter = (EndecaAdapter) this.adapterManager
				.getAdapter();
		requestMapURI.put(EndecaConstants.REFINEMENTS_REQUIRED,
				CommonConstants.TRUE_VALUE);
		//OPTIONS Parameter to be added in future
		final Map<String, List<String>> optionNodes = null;
		resultMap = endecaAdapter.service(requestMapURI, optionNodes, correlationId);
		this.generateSearchReport(requestMapURI, resultMap);
		LOGGER.debug("ResultMap " + resultMap, correlationId);
		if ((resultMap != null) && (!resultMap.isEmpty())) {
			searchList = this.searchMapper.convertToSearchPojo(resultMap,
					correlationId);
			LOGGER.info("The search list  : "
					+ searchList.getSearchReport().getTotalProducts(),
					correlationId);
		}

		LOGGER.logMethodExit(startTime, correlationId);
		return searchList;
	}

	/**
	 * The method to populate the search report map with values obtained from
	 * the request and the response.
	 * 
	 * @param request
	 *            The map containing the request parameters
	 * @param resultMap
	 *            List returned with search report properties added
	 */
	private void generateSearchReport(final Map<String, String> request,
			final List<Map<String, String>> resultMap) {
		Map<String, String> reportMap = null;

		for (final Map<String, String> map : resultMap) {
			if (map.containsKey(CommonConstants.TOTAL_PRODUCTS)) {
				reportMap = this.getReportMap(request);
				final StringBuilder attributes = new StringBuilder();
				if (request.containsKey(RequestAttributeConstant.COLOR)) {
					attributes.append(CommonConstants.COLOR);
					attributes.append(CommonConstants.FIELD_PAIR_SEPARATOR);
					attributes.append(request
							.get(RequestAttributeConstant.COLOR));
					attributes.append(CommonConstants.PIPE_SEPERATOR);
				}
				if (request.containsKey(RequestAttributeConstant.SIZE)) {
					attributes.append(CommonConstants.SIZE);
					attributes.append(CommonConstants.FIELD_PAIR_SEPARATOR);
					attributes.append(request
							.get(RequestAttributeConstant.SIZE));
					attributes.append(CommonConstants.PIPE_SEPERATOR);
				}
				if (request.containsKey(RequestAttributeConstant.BRAND)) {
					attributes.append(CommonConstants.BRAND);
					attributes.append(CommonConstants.FIELD_PAIR_SEPARATOR);
					attributes.append(request
							.get(RequestAttributeConstant.BRAND));
					attributes.append(CommonConstants.PIPE_SEPERATOR);
				}
				this.requestAttributePrice(request, attributes);
				if (attributes.length() != 0) {
					reportMap.put(DomainConstants.ATTRIBUTES, attributes
							.toString().substring(0, attributes.length() - 1));
				}

				String sortFields = "";
				if (request.containsKey(RequestAttributeConstant.SORT)) {
					sortFields = request.get(RequestAttributeConstant.SORT);
				}
				if (!"".equals(sortFields)) {
					reportMap.put(DomainConstants.SORT_FIELDS, sortFields
							.replace(CommonConstants.EMPTY_VALUE,
									CommonConstants.COMMA_SEPERATOR));
				}
				map.putAll(reportMap);
				break;
			}
		}

	}

	/**
	 * Method extracted to avoid checkstyle issue.
	 * 
	 * @param request
	 *            The map containing the request parameters
	 * @return reportMap
	 */
	private Map<String, String> getReportMap(final Map<String, String> request) {
		Map<String, String> reportMap;
		reportMap = new LinkedHashMap<String, String>();
		reportMap.put(CommonConstants.KEYWORD,
				request.get(RequestAttributeConstant.QUERY_SEARCH));
		reportMap.put(DomainConstants.REFINEMENT_ID,
				request.get(RequestAttributeConstant.REFINEMENT_ID));
		reportMap.put(DomainConstants.LIMIT,
				request.get(RequestAttributeConstant.LIMIT));
		reportMap.put(DomainConstants.OFFSET,
				request.get(RequestAttributeConstant.OFFSET));
		return reportMap;
	}

	/**
	 * The method will build the query for price range.
	 * 
	 * @param searchCriteria
	 *            The map containing the request parameters
	 * @param pipeSeparatedString
	 *            Price value from the request
	 * 
	 * @param correlationId
	 *            correlationId from request
	 * @throws BaseException
	 *             throws BaseException
	 */
	private void buildPriceRange(
			final Map<String, List<String>> searchCriteria,
			final String pipeSeparatedString, final String correlationId)
					throws BaseException {
		final String[] commaSeparated = pipeSeparatedString
				.split(CommonConstants.FIELD_PAIR_SEPARATOR);
		final String[] priceData = commaSeparated[1]
				.split(CommonConstants.COMMA_SEPERATOR);
		final Map<String, String> errorPropertiesMap = this.errorLoader
				.getErrorPropertiesMap();
		if (priceData.length == 2) {
			try {
				Double.parseDouble(priceData[0]);
				Double.parseDouble(priceData[1]);
			} catch (final NumberFormatException e) {
				LOGGER.error(e, correlationId);
				throw new BaseException(
						String.valueOf(ErrorConstants.ERROR_CODE_11422),
						errorPropertiesMap.get(String
								.valueOf(ErrorConstants.ERROR_CODE_11422)),
								RequestAttributeConstant.PRICE, priceData[0]
										+ CommonConstants.COMMA_SEPERATOR
										+ priceData[1],
										ErrorConstants.HTTP_STATUS_CODE_INVALID_URL_PARAMETERS,
										correlationId);
			}

			final String priceRange = commaSeparated[0]
					+ CommonConstants.FIELD_PAIR_SEPARATOR
					+ EndecaConstants.FILTER_PRICE + EndecaConstants.BETWEEN
					+ CommonConstants.EMPTY_VALUE + priceData[0]
							+ CommonConstants.EMPTY_VALUE + priceData[1];
			final List<String> attrList = new ArrayList<String>();
			final String data = searchCriteria
					.get(RequestAttributeConstant.ATTR)
					.get(0)
					.replace(commaSeparated[0] + ":" + commaSeparated[1],
							priceRange);
			attrList.add(data);
			searchCriteria.remove(RequestAttributeConstant.ATTR);
			searchCriteria.put(RequestAttributeConstant.ATTR, attrList);
			// Filtering based on price should be range based and if not
			// it will be a validation error
		} else {
			throw new BaseException(
					String.valueOf(ErrorConstants.ERROR_CODE_11422),
					errorPropertiesMap.get(String
							.valueOf(ErrorConstants.ERROR_CODE_11422)),
							RequestAttributeConstant.ATTR, pipeSeparatedString,
							ErrorConstants.HTTP_STATUS_CODE_INVALID_URL_PARAMETERS,
							correlationId);

		}

	}

	/**
	 * Method to form the query for price.
	 * 
	 * @param request
	 *            request map
	 * @param attributes
	 *            String Buffer
	 */
	private void requestAttributePrice(final Map<String, String> request,
			final StringBuilder attributes) {
		if (request.containsKey(RequestAttributeConstant.PRICE)) {
			if (request.get(RequestAttributeConstant.PRICE).contains(
					EndecaConstants.FILTER_PRICE)) {
				final String requestPriceValue = request.get(
						RequestAttributeConstant.PRICE).replace(
								EndecaConstants.FILTER_PRICE + EndecaConstants.BETWEEN
								+ CommonConstants.EMPTY_VALUE,
								CommonConstants.EMPTY_STRING);
				attributes.append(CommonConstants.PRICE);
				attributes.append(CommonConstants.FIELD_PAIR_SEPARATOR);
				attributes.append(requestPriceValue.replace(
						CommonConstants.EMPTY_VALUE,
						CommonConstants.COMMA_SEPERATOR));
				attributes.append(CommonConstants.PIPE_SEPERATOR);
			} else {
				attributes.append(CommonConstants.PRICE);
				attributes.append(CommonConstants.FIELD_PAIR_SEPARATOR);
				attributes.append(request.get(RequestAttributeConstant.PRICE));
				attributes.append(CommonConstants.PIPE_SEPERATOR);
			}
		}

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
