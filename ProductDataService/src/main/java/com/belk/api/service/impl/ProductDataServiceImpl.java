/**
 * 
 */
package com.belk.api.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.belk.api.cache.CacheManager;
import com.belk.api.constants.CommonConstants;
import com.belk.api.constants.ErrorConstants;
import com.belk.api.constants.RequestAttributeConstant;
import com.belk.api.exception.AdapterException;
import com.belk.api.exception.BaseException;
import com.belk.api.exception.ServiceException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.mail.MailClient;
import com.belk.api.mapper.ProductMapper;
import com.belk.api.model.productdetails.ProductDetails;
import com.belk.api.model.productdetails.ProductList;
import com.belk.api.service.ProductDataService;
import com.belk.api.service.ProductDetailsService;
import com.belk.api.util.CommonUtil;
import com.belk.api.util.CoremetricsUtil;
import com.belk.api.util.ErrorLoader;
import com.belk.api.util.PropertyReloader;
import com.belk.api.util.RequestParser;

/**
 * @author Mindtree
 * @date Oct 29, 2014
 * 
 */
@Component("ProductDataService")
public class ProductDataServiceImpl implements ProductDataService {

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

	@Autowired
	ProductDetailsService productDetailsService;

	@Autowired
	private CacheManager cacheManager;

	/**
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
	 * @param productMapper
	 *            the productMapper to set
	 */
	public final void setProductMapper(final ProductMapper productMapper) {
		this.productMapper = productMapper;
	}

	/**
	 * @param requestParser
	 *            the requestParser to set
	 */
	public final void setRequestParser(final RequestParser requestParser) {
		this.requestParser = requestParser;
	}

	/**
	 * @param mailClient
	 *            the mailClient to set
	 */
	public final void setMailClient(final MailClient mailClient) {
		this.mailClient = mailClient;
	}

	/**
	 * @param commonUtil
	 *            the commonUtil to set
	 */
	public final void setCommonUtil(final CommonUtil commonUtil) {
		this.commonUtil = commonUtil;
	}

	/**
	 * @param productDetailsService
	 *            the productDetailsService to set
	 */
	public final void setProductDetailsService(
			final ProductDetailsService productDetailsService) {
		this.productDetailsService = productDetailsService;
	}

	/**
	 * @param cacheManager
	 *            the cacheManager to set
	 */
	public final void setCacheManager(final CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	/**
	 * This method gets product details along with the Coremetrics recommended
	 * products list.
	 * 
	 * @param uriMap
	 *            - map of query parameters
	 * @param correlationId
	 *            to track the request
	 * @return ProductList
	 * @throws BaseException
	 *             The general exception from Domain layer
	 * @throws ServiceException
	 *             The general exception thrown from the Service layer
	 * @throws AdapterException
	 *             The general exception from Adapter layer
	 */
	@Override
	public final ProductList getCoremetricsRecommendedProductList(
			final Map<String, List<String>> uriMap, final String correlationId)
			throws BaseException, ServiceException, AdapterException {
		// the target is to make a call to the product details api and get the
		// product details list
		// and put it inside the ProductList pojo
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("Request uri map is " + uriMap, correlationId);

		ProductList productList = null;
		Map<String, String> requestMapURI = null;
		ProductList allProductResponse = null;
		List<ProductDetails> allProductResponseList = null;
		Set<String> parentProductIdSet = null;
		Map<String, String> recommendedToParentProductMap = null;
		requestMapURI = this.requestParser
				.prepareRequest(uriMap, correlationId);
		// get the product ID from the request
		final String parentProductIdList = requestMapURI
				.get(RequestAttributeConstant.PRODUCT_CODE);
		// Create a set of the parent product codes
		if (parentProductIdList != null) {
		parentProductIdSet = new HashSet<String>(
				Arrays.asList(parentProductIdList
						.split(CommonConstants.COMMA_SEPERATOR)));

		final String target = requestMapURI
				.get(RequestAttributeConstant.TARGET);
		final CoremetricsUtil coremetricsUtil = this.prepareCoremetricsUtil();

		List<String> allProductList = null;
		recommendedToParentProductMap = new HashMap<String, String>();
		allProductList = this.populateAllProductCodesToBeFetched(correlationId, parentProductIdSet,
				recommendedToParentProductMap, target, coremetricsUtil);

		uriMap.put(RequestAttributeConstant.PRODUCT_CODE, allProductList);
		

		allProductResponse = this.productDetailsService.getProductDetails(
				uriMap, correlationId);
		if (allProductResponse != null) {
		allProductResponseList = this.populateRecommendedProducts(
				allProductResponse, parentProductIdSet,
				recommendedToParentProductMap);
		} 
		/*else {
			final Iterator<ProductDetails> parentProductListItr = allProductResponse.getProducts().iterator();
			allProductResponseList = new ArrayList<ProductDetails>();
			while (parentProductListItr.hasNext()) {
				allProductResponseList.add(this.clearUnwantedOptionsForParent(parentProductListItr.next()));
			}
			
		}*/
		LOGGER.info("ResultMap " + allProductResponseList, correlationId);
		if (allProductResponseList != null && !allProductResponseList.isEmpty()) {
			productList = new ProductList();
			productList.setProducts(allProductResponseList);
		}
		}
		LOGGER.debug(" Product Pojo List " + productList, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return productList;

	}

	/**
	 * Method to prepare the CoremetricsUtil object for service
	 * 
	 * @return coremetricsUtil object reference
	 */
	private CoremetricsUtil prepareCoremetricsUtil() {
		final CoremetricsUtil coremetricsUtil = new CoremetricsUtil();
		coremetricsUtil.setCacheManager(this.cacheManager);
		coremetricsUtil.setCommonUtil(this.commonUtil);
		return coremetricsUtil;
	}

	/**
	 * Method to prepare the final list of product codes which are to be
	 * presented to product detail service
	 * 
	 * @param correlationId
	 *            to track the request
	 * @param parentProductIdSet
	 *            set of the parent product IDs
	 * @param recommendedToParentProductMap
	 *            contains the mapping relation between a recommended product to
	 *            its parent product
	 * @param target
	 *            the target type
	 * @param coremetricsUtil
	 *            the coremetricsUtil reference
	 * @return allProductList list where all the product codes are appended
	 */
	private List<String> populateAllProductCodesToBeFetched(
			final String correlationId, final Set<String> parentProductIdSet,
			final Map<String, String> recommendedToParentProductMap,
			final String target, final CoremetricsUtil coremetricsUtil) {
		final List<String> allProductList = new ArrayList<String>();
		List<String> recommendedProductIDList;
		final Set<String> parentProductIdToRemoveSet = new HashSet<String>();
		final Iterator<String> parentProductIdSetItr = parentProductIdSet
				.iterator();
		Iterator<String> recommendedProductIDListItr = null;
		while (parentProductIdSetItr.hasNext()) {
			final String parentProductId = parentProductIdSetItr.next();
			recommendedProductIDList = coremetricsUtil
					.getRecommendedProductIDList(parentProductId, target,
							correlationId);
			if (recommendedProductIDList != null) {
				/*
				 * If only recommended products are found in the cache for this
				 * parent product, add the parent product and the recommended
				 * products to the list, otherwise ignore even the parent
				 * product's details to be fetched from Endeca
				 */
				allProductList.add(parentProductId);
				recommendedProductIDListItr = recommendedProductIDList
						.iterator();
				while (recommendedProductIDListItr.hasNext()) {
					recommendedToParentProductMap.put(
							recommendedProductIDListItr.next().trim(),
							parentProductId);
				}
				allProductList.addAll(recommendedProductIDList);
			} else {
				parentProductIdToRemoveSet.add(parentProductId);
			}
		}
		parentProductIdSet.removeAll(parentProductIdToRemoveSet);
		return allProductList;
	}

	/**
	 * Private method to link the recommended products to their respective
	 * parent products
	 * 
	 * @param allProductResponse
	 *            response containing all the products
	 * @param parentProductIdSet
	 *            list of the parent product codes
	 * @param recommendedToParentProductMap
	 *            recommended product to parent product map
	 * @return final product list to be sent in the response
	 */
	private List<ProductDetails> populateRecommendedProducts(
			final ProductList allProductResponse,
			final Set<String> parentProductIdSet,
			final Map<String, String> recommendedToParentProductMap) {
		List<ProductDetails> allProductResponseList;
		final List<ProductDetails> parentProductList = new ArrayList<ProductDetails>();
		final Map<String, List<ProductDetails>> parentwiseRecommendedProductMap = new HashMap<String, List<ProductDetails>>();
		allProductResponseList = allProductResponse.getProducts();
		final Iterator<ProductDetails> allProductResponseListItr = allProductResponseList
				.iterator();

		while (allProductResponseListItr.hasNext()) {
			ProductDetails tempProduct = allProductResponseListItr.next();
			if (parentProductIdSet.contains(tempProduct.getProductCode())) {
				// separating the parent products
				tempProduct = this.clearUnwantedOptionsForParent(tempProduct);
				parentProductList.add(tempProduct);
			} else {
				// separating the recommended products
				final String currentProductCode = tempProduct.getProductCode();
				final String parentProductId = recommendedToParentProductMap
						.get(currentProductCode);
				if (parentwiseRecommendedProductMap
						.containsKey(parentProductId)) {
					parentwiseRecommendedProductMap.get(parentProductId).add(
							tempProduct);
				} else {
					parentwiseRecommendedProductMap.put(parentProductId,
							new ArrayList<ProductDetails>());
					parentwiseRecommendedProductMap.get(parentProductId).add(
							tempProduct);
				}
			}
		}
		final Iterator<ProductDetails> parentProductListItr = parentProductList
				.iterator();
		while (parentProductListItr.hasNext()) {
			final ProductDetails currentProduct = parentProductListItr.next();
			currentProduct.setRecommendations(parentwiseRecommendedProductMap
					.get(currentProduct.getProductCode()));
		}

		return parentProductList;
	}

	/**
	 * Method to clear the unwanted options from the parentProduct
	 * 
	 * @param parentProduct
	 *            parent product to be cleaned up
	 * @return cleaned up parent product
	 */
	private ProductDetails clearUnwantedOptionsForParent(
			final ProductDetails parentProduct) {
		final ProductDetails tempProduct = new ProductDetails();
		tempProduct.setProductCode(parentProduct.getProductCode());
		tempProduct.setProductId(parentProduct.getProductId());
		tempProduct.setWebId(parentProduct.getWebId());
		tempProduct.setVendorId(parentProduct.getVendorId());
		tempProduct.setVendorPartNumber(parentProduct.getVendorPartNumber());
		tempProduct.setName(null);
		tempProduct.setBrand(null);
		tempProduct.setShortDescription(null);
		tempProduct.setLongDescription(null);
		tempProduct.setProductHierarchyAttributes(null);
		tempProduct.setProductPrice(null);
		tempProduct.setRatings(null);
		tempProduct.setProductType(null);
		tempProduct.setIsPattern(null);
		tempProduct.setProductFlags(null);
		tempProduct.setProductMarketingAttributes(null);
		tempProduct.setProductAttributes(null);
		tempProduct.setChildProducts(null);
		tempProduct.setPromotions(null);
		tempProduct.setExtendedAttributes(null);
		tempProduct.setSkus(null);

		return tempProduct;

	}

	@Override
	public final void updatePropertiesMap(final String correlationId,
			final Map<String, List<String>> updateRequestUriMap) throws ServiceException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final Map<String, String> errorPropertiesMap = this.errorLoader
				.getErrorPropertiesMap();
		try {
			this.productDetailsService.updatePropertiesMap(correlationId, updateRequestUriMap);
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
