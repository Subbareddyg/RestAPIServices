package com.belk.api.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.belk.api.adapter.manager.AdapterManager;
import com.belk.api.constants.CommonConstants;
import com.belk.api.constants.EndecaConstants;
import com.belk.api.constants.ErrorConstants;
import com.belk.api.constants.MapperConstants;
import com.belk.api.constants.RequestConstants;
import com.belk.api.exception.AdapterException;
import com.belk.api.exception.BaseException;
import com.belk.api.exception.ServiceException;
import com.belk.api.impl.EndecaAdapter;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.mail.MailClient;
import com.belk.api.mapper.ProductMapper;
import com.belk.api.model.productdetails.ProductList;
import com.belk.api.service.ProductDetailsService;
import com.belk.api.util.CommonUtil;
import com.belk.api.util.ErrorLoader;
import com.belk.api.util.PropertyReloader;
import com.belk.api.util.RequestParser;

/**
 * This class gets the details of products from Endeca based on specified
 * request identifier. Initially it will process the input request parameters as
 * per the required Endeca format then calls appropriate service method.
 * 
 * Updated : Added few comments as part of Phase2, April,14 release
 * 
 * @author Mindtree
 * @date Sep 28, 2013
 */
@Service
public class ProductDetailsServiceImpl implements ProductDetailsService {

	/**
	 * Creating logger instance.
	 */
	// This will return a logger instance
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
	 * adapterManager is to create instance of AdapterManager.
	 */
	@Autowired
	AdapterManager adapterManager;

	/**
	 * productMapper is to create instance of ProductMapper.
	 */
	@Autowired
	ProductMapper productMapper;
	/**
	 * requestParser is to create instance of RequestParser.
	 */
	@Autowired
	RequestParser requestParser;

	/**
	 * mailClient is the utility class for sending mails.
	 */
	@Autowired
	MailClient mailClient;

	@Autowired
	CommonUtil commonUtil;

	/**
	 * Default constructor.
	 */
	public ProductDetailsServiceImpl() {
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
	 * @param requestParser
	 *            the requestParser to set
	 */
	public final void setRequestParser(final RequestParser requestParser) {
		this.requestParser = requestParser;
	}

	/**
	 * @param adapterManager
	 *            the adapterManager to set
	 */
	public final void setAdapterManager(final AdapterManager adapterManager) {
		this.adapterManager = adapterManager;
	}

	/**
	 * @param productMapper
	 *            the productMapper to set
	 */
	public final void setProductMapper(final ProductMapper productMapper) {
		this.productMapper = productMapper;
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
	 * Gets the Product Details from Endeca based on product specific
	 * identifiers passed like Belk Style UPC,Vendor Part Number/Vendor
	 * Number,Style Orin,Belk SKU UPC, Vendor SKU UPC and SKU Orin.
	 * 
	 * @param uriMap
	 *            - map of query parameters
	 * @param correlationId
	 *            to track the request
	 * @return ProductList
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * @throws ServiceException
	 *             exception thrown from Service layer
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 */
	@Override
	public final ProductList getProductDetails(
			final Map<String, List<String>> uriMap, final String correlationId)
					throws BaseException, ServiceException, AdapterException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("Request uri map is " + uriMap, correlationId);
		ProductList productList = null;
		Map<String, String> requestMapURI = null;
		List<Map<String, String>> resultMap = null;
		requestMapURI = this.requestParser
				.prepareRequest(uriMap, correlationId);
		// Fetch the value of options parameter
		final String optionValue = requestMapURI.get(CommonConstants.OPTIONS);
		Map<String, List<String>> optionNodes = null;
		if (this.commonUtil.isNotEmpty(optionValue, correlationId)) {
			optionNodes = this.requestParser.convertRequestNodetoMapper(optionValue, correlationId);
			// Remove the Options Parameter from the requestMapUri as it is looked up for Adapter(endeca) query params
			requestMapURI.remove(CommonConstants.OPTIONS);
		}	
		
		final EndecaAdapter endecaAdapter = (EndecaAdapter) this.adapterManager
				.getAdapter();
		// NavERecsPerAggrERec value is set to 2, means that all records(sku's)
		// are returned with each aggregated record.
		//If options parameter is available but does not request for SKUs then do not request endeca for SKUs
		if (optionNodes != null && !optionNodes.get(CommonConstants.OPTIONS).contains(RequestConstants.SKUS)) {
			requestMapURI.put(EndecaConstants.NAVERECS_PER_AGGREREC,
					String.valueOf(EndecaConstants.FLAG_VALUE_ONEPRODUCT));
		} else {
			requestMapURI.put(EndecaConstants.NAVERECS_PER_AGGREREC,
					String.valueOf(EndecaConstants.FLAG_VALUE_ALLPRODUCTS));
		}

		resultMap = endecaAdapter.service(requestMapURI, optionNodes, correlationId);
		LOGGER.info("ResultMap " + resultMap, correlationId);

		if (resultMap != null && !resultMap.isEmpty()) {
			for (Map<String, String> map : resultMap) {
				if (map.containsKey(MapperConstants.PRODUCT_CODE)) {
					productList = this.productMapper
							.convertToProductDetailsPojo(resultMap, optionNodes,
									correlationId);
					break;
				}
			}
		}

		LOGGER.debug(" Product Pojo List " + productList, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return productList;
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
