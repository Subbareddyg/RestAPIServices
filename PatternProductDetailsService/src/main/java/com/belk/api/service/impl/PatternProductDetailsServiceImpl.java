package com.belk.api.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.belk.api.adapter.manager.AdapterManager;
import com.belk.api.constants.CommonConstants;
import com.belk.api.constants.ErrorConstants;
import com.belk.api.exception.AdapterException;
import com.belk.api.exception.BaseException;
import com.belk.api.exception.ServiceException;
import com.belk.api.impl.BlueMartiniAdapter;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.mail.MailClient;
import com.belk.api.mapper.PatternProductMapper;
import com.belk.api.model.productdetails.ProductList;
import com.belk.api.service.PatternProductDetailsService;
import com.belk.api.util.CommonUtil;
import com.belk.api.util.ErrorLoader;
import com.belk.api.util.PropertyReloader;
import com.belk.api.util.RequestParser;

/**
 * This class gets the details of pattern products from blue martini based on
 * specified request identifier. Initially it will process the input request
 * parameters as per the required blue martini format then calls appropriate
 * service method.
 * 
 * Update: This API has been introduced as part of change request:
 * CR_BELK_API_3.
 * 
 * Update: This class has been updated to handle single log file per service and
 * mapping the properties(ATR_product_description, vendor part number, copy line
 * text etc.), as part of CR: April 2014
 * 
 * @author Mindtree
 * @date Nov 13th, 2013
 */
@Service
public class PatternProductDetailsServiceImpl implements
PatternProductDetailsService {

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
	 * creating instance of adapter manager.
	 */
	@Autowired
	AdapterManager adapterManager;

	/**
	 * requestParser is to create instance of RequestParser.
	 */
	@Autowired
	RequestParser requestParser;

	/**
	 * creating instance of pattern product mapper.
	 */
	@Autowired
	PatternProductMapper patternProductMapper;

	/**
	 * mailClient is the utility class for sending mails.
	 */
	@Autowired
	MailClient mailClient;

	/**
	 * Instance of Common Util.
	 */
	@Autowired
	CommonUtil commonUtil;

	/**
	 * Default Constructor.
	 */
	public PatternProductDetailsServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the commonUtil
	 */
	public final CommonUtil getCommonUtil() {
		return this.commonUtil;
	}

	/**
	 * @param commonUtil the commonUtil to set
	 */
	public final void setCommonUtil(final CommonUtil commonUtil) {
		this.commonUtil = commonUtil;
	}


	/**
	 * @param adapterManager
	 *            the adapterManager to set.
	 */
	public final void setAdapterManager(final AdapterManager adapterManager) {
		this.adapterManager = adapterManager;
	}

	/**
	 * @param patternProductMapper
	 *            the patternProductMapper to set.
	 */
	public final void setPatternProductMapper(
			final PatternProductMapper patternProductMapper) {
		this.patternProductMapper = patternProductMapper;
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
	 * Gets the Pattern Product Details from Blue Martini based on product
	 * specific identifiers passed.
	 * 
	 * @param uriMap
	 *            - map of query parameters
	 * @return List<Map<String, String>> - ProductList
	 * @param correlationId
	 *            to track the request
	 * @throws BaseException
	 *             Exception thrown from Domain layer
	 * @throws AdapterException
	 *             Exception thrown from Adapter layer
	 * 
	 */
	@Override
	public final ProductList getPatternProductDetails(
			final Map<String, List<String>> uriMap, final String correlationId)
					throws BaseException, AdapterException {

		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("Request uri map is " + uriMap, correlationId);
		List<Map<String, String>> blueMartiniResult = null;
		Map<String, String> patternProductDetailsRequestURIMap = null;
		ProductList patternProductDetailsList = null;
		final RequestParser requestParser = new RequestParser();

		// The request that is to be sent to blue martini which returns map of
		// query parameters
		patternProductDetailsRequestURIMap = requestParser.prepareRequest(
				uriMap, correlationId);
		LOGGER.info("Request map to blue martini "
				+ patternProductDetailsRequestURIMap, correlationId);
		final String optionValue = patternProductDetailsRequestURIMap
				.get(CommonConstants.OPTIONS);
		Map<String, List<String>> optionNodes = null;
		if (this.commonUtil.isNotEmpty(optionValue, correlationId)) {
			optionNodes = this.requestParser.convertRequestNodetoMapper(optionValue, correlationId);
			// Remove the Options Parameter from the requestMapUri as it is looked up for Adapter(endeca) query params
			patternProductDetailsRequestURIMap.remove(CommonConstants.OPTIONS);
		}

		// Calling getAdapter method to call the BlueMartini Adapter
		final BlueMartiniAdapter blueMartiniAdapter = (BlueMartiniAdapter) this.adapterManager
				.getAdapter();

		// Calling service method to get the BlueMartini results
		blueMartiniResult = blueMartiniAdapter.service(
				patternProductDetailsRequestURIMap, optionNodes, correlationId);
		LOGGER.info("BlueMartini Result" + blueMartiniResult, correlationId);

		patternProductDetailsList = this.patternProductMapper
				.convertToPatternProductDetailsPojo(blueMartiniResult,
						optionNodes,
						correlationId);

		LOGGER.debug(" Pattern Product Pojo List " + patternProductDetailsList,
				correlationId);
		LOGGER.logMethodExit(startTime, correlationId);

		return patternProductDetailsList;
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
