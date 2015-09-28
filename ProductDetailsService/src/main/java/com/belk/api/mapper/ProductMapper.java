package com.belk.api.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.belk.api.constants.CommonConstants;
import com.belk.api.constants.DomainConstants;
import com.belk.api.constants.ErrorConstants;
import com.belk.api.constants.MapperConstants;
import com.belk.api.constants.ProductConstant;
import com.belk.api.constants.RequestConstants;
import com.belk.api.exception.BaseException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.model.productdetails.Attribute;
import com.belk.api.model.productdetails.ChildProduct;
import com.belk.api.model.productdetails.ChildProductList;
import com.belk.api.model.productdetails.ColorSwatchAttribute;
import com.belk.api.model.productdetails.Price;
import com.belk.api.model.productdetails.ProductDetails;
import com.belk.api.model.productdetails.ProductFlag;
import com.belk.api.model.productdetails.ProductHierarchyAttribute;
import com.belk.api.model.productdetails.ProductList;
import com.belk.api.model.productdetails.ProductSKUImage;
import com.belk.api.model.productdetails.Promotion;
import com.belk.api.model.productdetails.Ratings;
import com.belk.api.model.productdetails.SKU;
import com.belk.api.model.productdetails.SKUImage;
import com.belk.api.model.productdetails.SKUImageList;
import com.belk.api.model.productdetails.SKUInventory;
import com.belk.api.model.productdetails.SKUMain;
import com.belk.api.util.CommonUtil;
import com.belk.api.util.DomainLoader;
import com.belk.api.util.ErrorLoader;

/**
 * Mapper class for the Product Details converts endeca result to List of
 * pojos.. Update: swatch image url has been added as a part of CR:
 * CR_BELK_API_9. Updated : Added few comments and changed the name of variables
 * as part of Phase2, April,14 release
 * Update: added product flag for hasDealPromo for API-416
 * 
 * @author Mindtree
 * @date 04 Oct, 2013
 */
@Service
public class ProductMapper {
	/**
	 * LOGGER is for logging the logs.
	 */
	// This will return logger instance.
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	@Autowired
	/**
	 * creating instance of Domain Loader class.
	 */
	DomainLoader domainLoader;

	/**
	 * creating instance of Error Loader class.
	 */
	@Autowired
	ErrorLoader errorLoader;

	/**
	 * commonUtil is the reference of common util.
	 */
	@Autowired
	CommonUtil commonUtil;

	/**
	 * Default constructor for ProductMapper.
	 */
	public ProductMapper() {
		// TODO Auto-generated constructor stub

	}

	/**
	 * @param domainLoader
	 *            the domainLoader to set
	 */
	public final void setDomainLoader(final DomainLoader domainLoader) {
		this.domainLoader = domainLoader;
	}

	/**
	 * @return the commonUtil.
	 */
	public final CommonUtil getCommonUtil() {
		return this.commonUtil;
	}

	/**
	 * @param commonUtil
	 *            the commonUtil to set.
	 */
	public final void setCommonUtil(final CommonUtil commonUtil) {
		this.commonUtil = commonUtil;
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
	 * Method to convert the response from adapter to the desired ProductList
	 * POJO.
	 * 
	 * @param resultList
	 *            to set.
	 * @param optionNodes
	 *            list of optionNodes to set
	 * @param correlationId
	 *            to track the request.
	 * @return ProductList.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	public final ProductList convertToProductDetailsPojo(
			final List<Map<String, String>> resultList,
			final Map<String, List<String>> optionNodes,
			final String correlationId) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final ProductList productList = new ProductList();
		final List<ProductDetails> productPojoList = new ArrayList<ProductDetails>();
		ProductDetails productDetails = null;
		List<SKU> skuList = null;
		LOGGER.info("resultList : " + resultList, correlationId);
		// Temporary object created to check whether iterating object is same or
		// not
		ProductDetails compareProductDetails = new ProductDetails();
		compareProductDetails.setProductCode(CommonConstants.EMPTY_STRING);
		List<String> optionValues = null;
		if (optionNodes != null) {
			optionValues = optionNodes.get(CommonConstants.OPTIONS);
		}
		final boolean optionMode = this.isOptionMode(optionNodes);
		for (Map<String, String> responseMap : resultList) {
			if (responseMap.containsKey(MapperConstants.PRODUCT_CODE)) {

				if (!compareProductDetails.getProductCode().equals(
						responseMap.get(MapperConstants.PRODUCT_CODE))) {

					productDetails = this.populateProductDetailsPojo(
							correlationId, optionValues,
							responseMap);

					// populates list of skus associated with the product into
					// product details object
					// If option mode is requested, Check if SKU is requested for
					if (!optionMode
							|| optionValues.contains(RequestConstants.SKUS)) {
						final SKU sku = this.populateSkus(responseMap,
								correlationId);
						skuList = new ArrayList<SKU>();
						skuList.add(sku);
						productDetails.setSkus(skuList);
					}
					// every product detail object is being added to product
					// list
					productPojoList.add(productDetails);
				} else {
					// If option mode is requested, Check if SKU is requested for
					if (!optionMode
							|| optionValues.contains(RequestConstants.SKUS)) {
						final SKU sku = this.populateSkus(responseMap,
								correlationId);
						skuList.add(sku);
						productDetails.setSkus(skuList);
					}
				}
				compareProductDetails = productDetails;
			}
		}
		productList.setProducts(productPojoList);
		LOGGER.debug("Output from Product Mapper : " + productList,
				correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return productList;
	}

	/**Method returns whether options have been requested for or not.
	 * @param optionNodes Map of option nodes present in the request
	 * @return whether options have been requested or not
	 */
	private boolean isOptionMode(final Map<String, List<String>> optionNodes) {
		boolean optionMode = false;
		// Determine if request is in "OPTIONS" mode
		if (optionNodes != null && !optionNodes.isEmpty()) {
			optionMode = true;
		}
		return optionMode;
	}



	/**
	 * Method to populate the product details from the response map.
	 * @param correlationId
	 * 			tracking id
	 * @param optionNodes list of option nodes
	 * @param responseMap
	 * 			response map
	 * @return
	 * 			populated product details object
	 * @throws BaseException
	 * 			throws BaseException
	 */
	private ProductDetails populateProductDetailsPojo(final String correlationId, final List<String> optionNodes,
			final Map<String, String> responseMap) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		ProductDetails productDetails;
		productDetails = new ProductDetails();
		boolean optionMode = false;

		if (optionNodes != null && !optionNodes.isEmpty()) {
			optionMode = true;
		}

		this.populateProductDetails(responseMap, productDetails, optionNodes,
				correlationId);

		if (!optionMode || optionNodes.contains(RequestConstants.PRODUCT_TYPE)) {
			this.populateProductType(responseMap, productDetails,
					correlationId);
		}

		// populates list of product hierarchy attributes into
		// product details object
		if (!optionMode || optionNodes.contains(RequestConstants.HIERARCHY)) {
			this.populateProductHierarchyAttributes(responseMap,
					productDetails, correlationId);
		}

		// populates list of product price values into product
		// details object
		if (!optionMode || optionNodes.contains(RequestConstants.PRODUCT_PRICE)) {
			this.populateProductPrice(responseMap, productDetails,
					correlationId);
		}

		// populates list of product flags into product details
		// object
		if (!optionMode || optionNodes.contains(RequestConstants.PRODUCT_FLAG)) {
			this.populateProductFlag(responseMap, productDetails,
					correlationId);
		}

		// populates list of product attributes into product details
		// object
		if (!optionMode
				|| optionNodes.contains(RequestConstants.PRODUCT_ATTRIBUTES)) {
			this.populateProductAttributes(responseMap, productDetails,
					correlationId);
		}

		// populates child products into product details object
		if (!optionMode
				|| optionNodes.contains(RequestConstants.CHILD_PRODUCTS)) {
			this.populateChildProducts(responseMap, productDetails,
					correlationId);
		}

		// populate Rating into product details object
		if (!optionMode || optionNodes.contains(RequestConstants.RATINGS)) {
			this.populateRating(productDetails, correlationId);
		}

		// populate promotions into product details object
		if (!optionMode || optionNodes.contains(RequestConstants.PROMOTIONS)) {
			this.populatePromotions(productDetails, correlationId);
		}

		LOGGER.logMethodExit(startTime, correlationId);
		return productDetails;
	}

	/**
	 * Method to populate product promotions.
	 * 
	 * @param productDetails
	 *            product details reference passed.
	 * @param correlationId
	 *            to track the request.
	 */
	private void populatePromotions(final ProductDetails productDetails,
			final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		// promotions object is created as its a mandatory tag.
		final List<Promotion> promotions = new ArrayList<Promotion>();
		productDetails.setPromotions(promotions);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate the Product hierarchy attributes.
	 * 
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Endeca.
	 * @param productDetails
	 *            product details reference passed.
	 * @param correlationId
	 *            to track the request.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateProductHierarchyAttributes(
			final Map<String, String> adapterResultMap,
			final ProductDetails productDetails, final String correlationId)
					throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("adapterResultMap : " + adapterResultMap, correlationId);
		ProductHierarchyAttribute productHierarchyAttribute = null;
		List<ProductHierarchyAttribute> productHierarchyAttributeList = new ArrayList<ProductHierarchyAttribute>();
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.CLASS_NUMBER),
				correlationId)) {
			productHierarchyAttribute = new ProductHierarchyAttribute();
			productHierarchyAttribute.setKey(ProductConstant.CLASS);
			productHierarchyAttribute.setValue(adapterResultMap
					.get(MapperConstants.CLASS_NUMBER));
			productHierarchyAttributeList.add(productHierarchyAttribute);
		} else {
			// added empty string as its mandatory tag.
			productHierarchyAttribute = new ProductHierarchyAttribute();
			productHierarchyAttribute.setKey(ProductConstant.CLASS);
			productHierarchyAttribute.setValue(CommonConstants.EMPTY_STRING);
			productHierarchyAttributeList.add(productHierarchyAttribute);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.DEPARTMENT_NUMBER),
				correlationId)) {
			productHierarchyAttribute = new ProductHierarchyAttribute();
			productHierarchyAttribute.setKey(ProductConstant.DEPT_ID);
			productHierarchyAttribute.setValue(adapterResultMap
					.get(MapperConstants.DEPARTMENT_NUMBER));
			productHierarchyAttributeList.add(productHierarchyAttribute);
		} else {
			// added empty string as its mandatory tag.
			productHierarchyAttribute = new ProductHierarchyAttribute();
			productHierarchyAttribute.setKey(ProductConstant.DEPT_ID);
			productHierarchyAttribute.setValue(CommonConstants.EMPTY_STRING);
			productHierarchyAttributeList.add(productHierarchyAttribute);
		}
		this.populateExtendedProductHierarchyAttributes(adapterResultMap,
				correlationId, productHierarchyAttributeList);
		LOGGER.debug("Output from populateProductHierarchyAttributes method : "
				+ productHierarchyAttributeList, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);

		if (productHierarchyAttributeList.isEmpty()) {
			productHierarchyAttributeList = null;
		}

		productDetails
		.setProductHierarchyAttributes(productHierarchyAttributeList);
	}

	/**
	 * Method to populate the Product hierarchy attributes.
	 * 
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Endeca.
	 * @param productHierarchyAttributeList
	 *            product hierarchy attributeList reference passed.
	 * @param correlationId
	 *            to track the request.
	 */
	private void populateExtendedProductHierarchyAttributes(
			final Map<String, String> adapterResultMap,
			final String correlationId,
			final List<ProductHierarchyAttribute> productHierarchyAttributeList) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		ProductHierarchyAttribute productHierarchyAttribute = null;
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.PATH), correlationId)) {
			this.populatePath(productHierarchyAttributeList,
					adapterResultMap.get(MapperConstants.PATH), correlationId);

		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(DomainConstants.CATEGORY), correlationId)) {
			productHierarchyAttribute = new ProductHierarchyAttribute();
			productHierarchyAttribute.setKey(ProductConstant.CATEGORY);
			productHierarchyAttribute.setValue(adapterResultMap
					.get(DomainConstants.CATEGORY));
			productHierarchyAttributeList.add(productHierarchyAttribute);
		} else {
			// added empty string as its mandatory tag.
			productHierarchyAttribute = new ProductHierarchyAttribute();
			productHierarchyAttribute.setKey(ProductConstant.CATEGORY);
			productHierarchyAttribute.setValue(CommonConstants.EMPTY_STRING);
			productHierarchyAttributeList.add(productHierarchyAttribute);
		}
		LOGGER.debug(
				"Output from populateExtendedProductHierarchyAttributes method : "
						+ productHierarchyAttributeList, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to add multiple paths to the Product Hierarchy attribute.
	 * 
	 * @param productHierarchyAttributeList
	 *            productHierarchy Attribute List.
	 * @param productPath
	 *            multiple path value.
	 * @param correlationId
	 *            to track the request.
	 */
	private void populatePath(
			final List<ProductHierarchyAttribute> productHierarchyAttributeList,
			final String productPath, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		ProductHierarchyAttribute productHierarchyAttribute;
		LOGGER.info("productHierarchyAttributeList : "
				+ productHierarchyAttributeList, correlationId);
		if (productPath.contains(CommonConstants.COMMA_SEPERATOR)) {
			final String[] arr = productPath
					.split(CommonConstants.COMMA_SEPERATOR);
			// Looping to get each "CategoryParentage" value.
			for (int i = 0; i < arr.length; i++) {
				productHierarchyAttribute = new ProductHierarchyAttribute();
				productHierarchyAttribute
				.setKey(ProductConstant.CATEGORY_PARENTAGE + (i + 1));
				productHierarchyAttribute.setValue(arr[i]);
				productHierarchyAttributeList.add(productHierarchyAttribute);
			}
		} else {
			productHierarchyAttribute = new ProductHierarchyAttribute();
			productHierarchyAttribute
			.setKey(ProductConstant.CATEGORY_PARENTAGE);
			productHierarchyAttribute.setValue(productPath);
			productHierarchyAttributeList.add(productHierarchyAttribute);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate the Product Price.
	 * 
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Endeca.
	 * @param productDetails
	 *            product details reference passed.
	 * @param correlationId
	 *            to track the request.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateProductPrice(
			final Map<String, String> adapterResultMap,
			final ProductDetails productDetails, final String correlationId)
					throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("adapterResultMap : " + adapterResultMap, correlationId);
		final List<Price> productPrice = new ArrayList<Price>();
		this.populateListPrice(adapterResultMap, productPrice, correlationId);
		this.populateSalePrice(adapterResultMap, productPrice, correlationId);
		LOGGER.debug("Output from populateProductPrice method : "
				+ productPrice, correlationId);
		productDetails.setProductPrice(productPrice);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate the Product List Price.
	 * 
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Endeca.
	 * @param productPrice
	 *            List of Product price.
	 * @param correlationId
	 *            to track the request.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateListPrice(final Map<String, String> adapterResultMap,
			final List<Price> productPrice, final String correlationId)
					throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		Price price = null;
		LOGGER.info("adapterResultMap : " + adapterResultMap, correlationId);
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.LIST_PRICE_RANGE),
				correlationId)) {
			price = new Price();
			price.setKey(ProductConstant.LIST_PRICE_RANGE);
			price.setValue(adapterResultMap
					.get(MapperConstants.LIST_PRICE_RANGE));
			productPrice.add(price);
		} else {
			// added empty string as its mandatory tag.
			price = new Price();
			price.setKey(ProductConstant.LIST_PRICE_RANGE);
			price.setValue(CommonConstants.EMPTY_STRING);
			productPrice.add(price);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil
				.isNotEmpty(adapterResultMap.get(MapperConstants.LIST_PRICE),
						correlationId)) {
			price = new Price();
			price.setKey(ProductConstant.LIST_PRICE);
			price.setValue(this.commonUtil.format(
					adapterResultMap.get(MapperConstants.LIST_PRICE),
					correlationId));
			productPrice.add(price);
		} else {
			// added empty string as its mandatory tag.
			price = new Price();
			price.setKey(ProductConstant.LIST_PRICE);
			price.setValue(CommonConstants.EMPTY_STRING);
			productPrice.add(price);
		}
		this.populateExtendedListPrice(adapterResultMap, productPrice,
				correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate the Product List Price.
	 * 
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Endeca.
	 * @param productPrice
	 *            List of Product price.
	 * @param correlationId
	 *            to track the request.
	 */
	private void populateExtendedListPrice(
			final Map<String, String> adapterResultMap,
			final List<Price> productPrice, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		Price price = null;
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.MIN_LIST_PRICE),
				correlationId)) {
			price = new Price();
			price.setKey(ProductConstant.MINLIST_PRICE);
			price.setValue(this.commonUtil.format(
					adapterResultMap.get(MapperConstants.MIN_LIST_PRICE),
					correlationId));
			productPrice.add(price);
		} else {
			// added empty string as its mandatory tag.
			price = new Price();
			price.setKey(ProductConstant.MINLIST_PRICE);
			price.setValue(CommonConstants.EMPTY_STRING);
			productPrice.add(price);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.MAX_LIST_PRICE),
				correlationId)) {
			price = new Price();
			price.setKey(ProductConstant.MAXLIST_PRICE);
			price.setValue(this.commonUtil.format(
					adapterResultMap.get(MapperConstants.MAX_LIST_PRICE),
					correlationId));
			productPrice.add(price);
		} else {
			// added empty string as its mandatory tag.
			price = new Price();
			price.setKey(ProductConstant.MAXLIST_PRICE);
			price.setValue(CommonConstants.EMPTY_STRING);
			productPrice.add(price);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate the Product Sale Price.
	 * 
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Endeca.
	 * @param productPrice
	 *            List of Product price.
	 * @param correlationId
	 *            to track the request.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateSalePrice(final Map<String, String> adapterResultMap,
			final List<Price> productPrice, final String correlationId)
					throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		Price price = null;
		LOGGER.info("adapterResultMap : " + adapterResultMap, correlationId);
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.SALE_PRICE_RANGE),
				correlationId)) {
			price = new Price();
			price.setKey(ProductConstant.SALE_PRICE_RANGE);
			price.setValue(adapterResultMap
					.get(MapperConstants.SALE_PRICE_RANGE));
			productPrice.add(price);
		} else {
			// added empty string as its mandatory tag.
			price = new Price();
			price.setKey(ProductConstant.SALE_PRICE_RANGE);
			price.setValue(CommonConstants.EMPTY_STRING);
			productPrice.add(price);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil
				.isNotEmpty(adapterResultMap.get(MapperConstants.SALE_PRICE),
						correlationId)) {
			final String salePrice = adapterResultMap
					.get(MapperConstants.SALE_PRICE);
			if (salePrice.contains(CommonConstants.COMMA_SEPERATOR)) {
				final String[] salePriceList = salePrice
						.split(CommonConstants.COMMA_SEPERATOR);
				for (int p = 0; p < salePriceList.length; p++) {
					price = new Price();
					price.setKey(ProductConstant.SALE_PRICE);
					price.setValue(this.commonUtil.format(salePriceList[p],
							correlationId));
					productPrice.add(price);
				}

			} else {

				price = new Price();
				price.setKey(ProductConstant.SALE_PRICE);
				price.setValue(this.commonUtil.format(salePrice, correlationId));
				productPrice.add(price);
			}
		} else {
			// added empty string as its mandatory tag.
			price = new Price();
			price.setKey(ProductConstant.SALE_PRICE);
			price.setValue(CommonConstants.EMPTY_STRING);
			productPrice.add(price);
		}

		// Added this method to avoid cyclomatic complexity
		this.populateMinMaxSalePrice(productPrice, correlationId,
				adapterResultMap);
		LOGGER.logMethodExit(startTime, correlationId);

	}

	/**
	 * Method to populate Min sale price and Max sale price
	 * 
	 * @param productPrice
	 *            has product price
	 * @param correlationId
	 *            for tracking the request
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Adapter
	 */
	private void populateMinMaxSalePrice(final List<Price> productPrice,
			final String correlationId,
			final Map<String, String> adapterResultMap) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		Price price = null;
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.MIN_SALE_PRICE),
				correlationId)) {
			price = new Price();
			price.setKey(ProductConstant.MIN_SALE_PRICE);
			price.setValue(this.commonUtil.format(
					adapterResultMap.get(MapperConstants.MIN_SALE_PRICE),
					correlationId));
			productPrice.add(price);
		} else {
			// added empty string as its mandatory tag.
			price = new Price();
			price.setKey(ProductConstant.MIN_SALE_PRICE);
			price.setValue(CommonConstants.EMPTY_STRING);
			productPrice.add(price);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.MAX_SALE_PRICE),
				correlationId)) {
			price = new Price();
			price.setKey(ProductConstant.MAX_SALE_PRICE);
			price.setValue(this.commonUtil.format(
					adapterResultMap.get(MapperConstants.MAX_SALE_PRICE),
					correlationId));
			productPrice.add(price);
		} else {
			// added empty string as its mandatory tag.
			price = new Price();
			price.setKey(ProductConstant.MAX_SALE_PRICE);
			price.setValue(CommonConstants.EMPTY_STRING);
			productPrice.add(price);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate the Product Flags.
	 * 
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Endeca.
	 * @param productDetails
	 *            product details reference passed.
	 * @param correlationId
	 *            to track the request.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateProductFlag(
			final Map<String, String> adapterResultMap,
			final ProductDetails productDetails, final String correlationId)
					throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final List<ProductFlag> productFlagList = new ArrayList<ProductFlag>();
		LOGGER.info("adapterResultMap : " + adapterResultMap, correlationId);
		ProductFlag productFlag = null;
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.AVAILABLE_IN_STORE),
				correlationId)) {
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.AVAILABLE_INSTORE);
			productFlag.setValue(this.commonUtil.convertToFlag(
					adapterResultMap.get(MapperConstants.AVAILABLE_IN_STORE),
					correlationId));
			productFlagList.add(productFlag);
		} else {
			// added No Value as it is a mandatory tag
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.AVAILABLE_INSTORE);
			productFlag.setValue(CommonConstants.FLAG_NO_VALUE);
			productFlagList.add(productFlag);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.AVAILABLE_ONLINE),
				correlationId)) {
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.AVAILABLE_ONLINE);
			productFlag.setValue(this.commonUtil.convertToFlag(
					adapterResultMap.get(MapperConstants.AVAILABLE_ONLINE),
					correlationId));
			productFlagList.add(productFlag);
		} else {
			// added No Value as it is a mandatory tag
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.AVAILABLE_ONLINE);
			productFlag.setValue(CommonConstants.FLAG_NO_VALUE);
			productFlagList.add(productFlag);
		}
		this.populateExtendedProductFlag(adapterResultMap, correlationId,
				productFlagList);
		// To avoid cyclomatic complexity extracted the implementation to
		// different method
		this.populateProductFlagExtended(adapterResultMap, correlationId,
				productFlagList);
		productDetails.setProductFlags(productFlagList);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate the extended Product Flags.
	 * 
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Adapter.
	 * @param correlationId
	 *            to track the request.
	 * @param productFlagList
	 *            the list to which the product flags gets added.
	 */
	private void populateExtendedProductFlag(
			final Map<String, String> adapterResultMap,
			final String correlationId, final List<ProductFlag> productFlagList) {
		ProductFlag productFlag = null;
		final long startTime = LOGGER.logMethodEntry(correlationId);
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.BRIDAL_ELIGIBLE),
				correlationId)) {
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.IS_BRIDAL);
			productFlag.setValue(this.commonUtil.convertToFlag(
					adapterResultMap.get(MapperConstants.BRIDAL_ELIGIBLE),
					correlationId)); // added empty string as its mandatory tag.
			productFlagList.add(productFlag);
		} else {
			// added No Value as it is a mandatory tag
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.IS_BRIDAL);
			productFlag.setValue(CommonConstants.FLAG_NO_VALUE);
			productFlagList.add(productFlag);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate the extended Product Flags.
	 * 
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Adapter.
	 * @param correlationId
	 *            to track the request.
	 * @param productFlagList
	 *            the list to which the product flags gets added.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateProductFlagExtended(
			final Map<String, String> adapterResultMap,
			final String correlationId, final List<ProductFlag> productFlagList)
					throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		ProductFlag productFlag = null;
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.CLEARANCE), correlationId)) {
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.CLEARANCE);
			productFlag.setValue(this.commonUtil.convertToFlag(
					adapterResultMap.get(MapperConstants.CLEARANCE),
					correlationId));
			productFlagList.add(productFlag);
		} else {
			// added No Value as it is a mandatory tag
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.CLEARANCE);
			productFlag.setValue(CommonConstants.FLAG_NO_VALUE);
			productFlagList.add(productFlag);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.NEW_ARRIVAL),
				correlationId)) {
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.NEW_ARRIVAL);
			productFlag.setValue(this.commonUtil.convertToFlag(
					adapterResultMap.get(MapperConstants.NEW_ARRIVAL),
					correlationId)); // added empty string as its mandatory tag.
			productFlagList.add(productFlag);
		} else {
			// added No Value as it is a mandatory tag
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.NEW_ARRIVAL);
			productFlag.setValue(CommonConstants.FLAG_NO_VALUE);
			productFlagList.add(productFlag);
		}
		this.populateExtendedProductFlagAttribute(adapterResultMap,
				correlationId, productFlagList);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate Product flag attributes.
	 * 
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Adapter.
	 * @param correlationId
	 *            to track the request.
	 * @param productFlagList
	 *            the list to which the product flags gets added.
	 */
	private void populateExtendedProductFlagAttribute(
			final Map<String, String> adapterResultMap,
			final String correlationId, final List<ProductFlag> productFlagList) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		ProductFlag productFlag = null;
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.ON_SALE), correlationId)) {
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.ON_SALE);
			productFlag.setValue(this.commonUtil.convertToFlag(
					adapterResultMap.get(MapperConstants.ON_SALE),
					correlationId));
			productFlagList.add(productFlag);
		} else {
			//added No Value as it is a mandatory tag.
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.ON_SALE);
			productFlag.setValue(CommonConstants.FLAG_NO_VALUE);
			productFlagList.add(productFlag);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.IS_DROP_SHIP),
				correlationId)) {
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.IS_DROP_SHIP);
			productFlag.setValue(this.commonUtil.convertToFlag(
					adapterResultMap.get(MapperConstants.IS_DROP_SHIP),
					correlationId));
			productFlagList.add(productFlag);
		} else {
			//added No Value as it is a mandatory tag.
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.IS_DROP_SHIP);
			productFlag.setValue(CommonConstants.FLAG_NO_VALUE);
			productFlagList.add(productFlag);
		}

		//Added for API-416.
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.HAS_DEAL_PROMO), correlationId)) {
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.HAS_DEAL_PROMO);
			productFlag.setValue(this.commonUtil.convertToFlag(
					adapterResultMap.get(MapperConstants.HAS_DEAL_PROMO),
					correlationId));
			productFlagList.add(productFlag);
		} else {
			// added No Value as it is a mandatory tag
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.HAS_DEAL_PROMO);
			productFlag.setValue(CommonConstants.FLAG_NO_VALUE);
			productFlagList.add(productFlag);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate the Product Attributes.
	 * 
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Endeca.
	 * @param productDetails
	 *            product details reference passed.
	 * @param correlationId
	 *            to track the request.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateProductAttributes(
			final Map<String, String> adapterResultMap,
			final ProductDetails productDetails, final String correlationId)
					throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("adapterResultMap : " + adapterResultMap, correlationId);
		final List<Attribute> productAttributeList = new ArrayList<Attribute>();
		Attribute productAttribute;
		this.populateProductCopyTexts(adapterResultMap, correlationId,
				productAttributeList);
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.PRODUCT_MAIN_IMAGE_URL),
				correlationId)) {
			productAttribute = new Attribute();
			productAttribute.setKey(ProductConstant.PRODUCT_IMAGE);
			productAttribute.setValue(adapterResultMap
					.get(MapperConstants.PRODUCT_MAIN_IMAGE_URL));
			productAttributeList.add(productAttribute);
		} else {
			// added empty string as its mandatory tag.
			LOGGER.error(
					new Throwable(MapperConstants.PRODUCT_MAIN_IMAGE_URL
							+ " field is empty for:"
							+ adapterResultMap
							.get(MapperConstants.PRODUCT_CODE)
							.toString()), correlationId);
			productAttribute = new Attribute();
			productAttribute.setKey(ProductConstant.PRODUCT_IMAGE);
			productAttribute.setValue(CommonConstants.EMPTY_STRING);
			productAttributeList.add(productAttribute);
		}

		this.populateExtendedProductAttribute(adapterResultMap, correlationId,
				productAttributeList);
		LOGGER.debug("Output from populateProductAttributes method : "
				+ productAttributeList, correlationId);
		productDetails.setProductAttributes(productAttributeList);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**Method to populate product copy texts
	 * @param adapterResultMap Map containing the adapter results
	 * @param correlationId to track the request
	 * @param productAttributeList list of product attributes
	 */
	private void populateProductCopyTexts(
			final Map<String, String> adapterResultMap,
			final String correlationId,
			final List<Attribute> productAttributeList) {
		Attribute productAttribute = null;
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.PRODUCT_COPY_TEXT_1),
				correlationId)) {
			productAttribute = new Attribute();
			productAttribute.setKey(ProductConstant.COPY_LINE_1);
			productAttribute.setValue(adapterResultMap
					.get(MapperConstants.PRODUCT_COPY_TEXT_1));
			productAttributeList.add(productAttribute);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.PRODUCT_COPY_TEXT_2),
				correlationId)) {
			productAttribute = new Attribute();
			productAttribute.setKey(ProductConstant.COPY_LINE_2);
			productAttribute.setValue(adapterResultMap
					.get(MapperConstants.PRODUCT_COPY_TEXT_2));
			productAttributeList.add(productAttribute);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.PRODUCT_COPY_TEXT_3),
				correlationId)) {
			productAttribute = new Attribute();
			productAttribute.setKey(ProductConstant.COPY_LINE_3);
			productAttribute.setValue(adapterResultMap
					.get(MapperConstants.PRODUCT_COPY_TEXT_3));
			productAttributeList.add(productAttribute);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.PRODUCT_COPY_TEXT_4),
				correlationId)) {
			productAttribute = new Attribute();
			productAttribute.setKey(ProductConstant.COPY_LINE_4);
			productAttribute.setValue(adapterResultMap
					.get(MapperConstants.PRODUCT_COPY_TEXT_4));
			productAttributeList.add(productAttribute);
		}
	}

	/**
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Endeca.
	 * @param productAttributeList
	 *            product attribute list reference passed.
	 * @param correlationId
	 *            to track the request.
	 */
	private void populateExtendedProductAttribute(
			final Map<String, String> adapterResultMap,
			final String correlationId,
			final List<Attribute> productAttributeList) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		Attribute productAttribute = null;
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.DEFAULT_SKU),
				correlationId)) {
			productAttribute = new Attribute();
			productAttribute.setKey(ProductConstant.DEFAULT_SKU);
			productAttribute.setValue(adapterResultMap
					.get(MapperConstants.DEFAULT_SKU));
			productAttributeList.add(productAttribute);
		} else {
			// added empty string as its mandatory tag.
			productAttribute = new Attribute();
			productAttribute.setKey(ProductConstant.DEFAULT_SKU);
			productAttribute.setValue(CommonConstants.EMPTY_STRING);
			productAttributeList.add(productAttribute);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil
				.isNotEmpty(adapterResultMap.get(MapperConstants.SHOW_COLOR),
						correlationId)) {
			productAttribute = new Attribute();
			productAttribute.setKey(ProductConstant.SHOW_COLOR);
			productAttribute.setValue(adapterResultMap
					.get(MapperConstants.SHOW_COLOR));
			productAttributeList.add(productAttribute);
		} else {
			// added empty string as its mandatory tag.
			productAttribute = new Attribute();
			productAttribute.setKey(ProductConstant.SHOW_COLOR);
			productAttribute.setValue(CommonConstants.EMPTY_STRING);
			productAttributeList.add(productAttribute);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.SHOW_SIZE), correlationId)) {
			productAttribute = new Attribute();
			productAttribute.setKey(ProductConstant.SHOW_SIZE);
			productAttribute.setValue(adapterResultMap
					.get(MapperConstants.SHOW_SIZE));
			productAttributeList.add(productAttribute);
		} else {
			// added empty string as its mandatory tag.
			productAttribute = new Attribute();
			productAttribute.setKey(ProductConstant.SHOW_SIZE);
			productAttribute.setValue(CommonConstants.EMPTY_STRING);
			productAttributeList.add(productAttribute);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate the Child Products.
	 * 
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Endeca.
	 * @param productDetails
	 *            product details reference passed.
	 * @param correlationId
	 *            to track the request.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateChildProducts(
			final Map<String, String> adapterResultMap,
			final ProductDetails productDetails, final String correlationId)
					throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("adapterResultMap : " + adapterResultMap, correlationId);
		final ChildProductList childProductList = new ChildProductList();
		final List<ChildProduct> childList = new ArrayList<ChildProduct>();
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.PATTERN_CODE),
				correlationId)) {
			final String value = adapterResultMap
					.get(MapperConstants.PATTERN_CODE);
			if (value.contains(CommonConstants.COMMA_SEPERATOR)) {
				final String[] productIds = value
						.split(CommonConstants.COMMA_SEPERATOR);
				for (final String productId : productIds) {
					final ChildProduct childProduct = new ChildProduct();
					childProduct.setKey(ProductConstant.CHILD_PRODUCT_ID);
					childProduct.setValue(productId);
					childList.add(childProduct);
				}
			} else {
				final ChildProduct childProduct = new ChildProduct();
				childProduct.setKey(ProductConstant.CHILD_PRODUCT_ID);
				childProduct.setValue(value);
				childList.add(childProduct);
			}
		}

		childProductList.setChildProduct(childList);
		// childProductList.setCollectionType(""); //need to populate once we
		// get key value from adapter
		LOGGER.debug("Output from populateChildProducts method : "
				+ childProductList, correlationId);
		if (!childList.isEmpty()) {
			productDetails.setChildProducts(childProductList);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate the Product Skus.
	 * 
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Endeca.
	 * @param correlationId
	 *            to track the request.
	 * @return List of Product skus.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private SKU populateSkus(final Map<String, String> adapterResultMap,
			final String correlationId) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("adapterResultMap : " + adapterResultMap, correlationId);
		final SKU sku = new SKU();
		final List<SKU> skuList = new ArrayList<SKU>();
		final List<SKUMain> skuMainList = new ArrayList<SKUMain>();
		final SKUInventory skuInventory = new SKUInventory();

		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.SKU_CODE), correlationId)) {
			sku.setSkuCode(adapterResultMap.get(MapperConstants.SKU_CODE));
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.SKU_UPC), correlationId)) {
			sku.setUpcCode(adapterResultMap.get(MapperConstants.SKU_UPC));
		}
		// check if the value from the adapterResultMap is not null
				if (this.commonUtil.isNotEmpty(
						adapterResultMap.get(MapperConstants.SKU_ID), correlationId)) {
					sku.setSkuId(adapterResultMap.get(MapperConstants.SKU_ID));
				}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.COLOR), correlationId)) {
			this.populateColor(skuMainList,
					adapterResultMap.get(MapperConstants.COLOR), correlationId);

		} else {
			// added empty string as its mandatory tag.
			final SKUMain skuMain = new SKUMain();
			skuMain.setKey(ProductConstant.COLOR);
			skuMain.setValue(CommonConstants.EMPTY_STRING);
			skuMainList.add(skuMain);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil
				.isNotEmpty(adapterResultMap.get(MapperConstants.COLOR_CODE),
						correlationId)) {
			final SKUMain skuMain = new SKUMain();
			skuMain.setValue(adapterResultMap.get(MapperConstants.COLOR_CODE));
			skuMain.setKey(ProductConstant.COLOR_CODE);
			skuMainList.add(skuMain);
		} else {
			// added empty string as its mandatory tag.
			final SKUMain skuMain = new SKUMain();
			skuMain.setKey(ProductConstant.COLOR_CODE);
			skuMain.setValue(CommonConstants.EMPTY_STRING);
			skuMainList.add(skuMain);
		}
		this.populateExtendedSkus(adapterResultMap, correlationId, sku,
				skuMainList, skuInventory);
		sku.setSkuMainAttributes(skuMainList);
		this.populateSkuPrice(adapterResultMap, sku, correlationId);
		this.populateSkuImages(adapterResultMap, sku, correlationId);
		LOGGER.debug("Output from populateSkus method : " + skuList,
				correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return sku;
	}

	/**
	 * Method to populate the Product Skus.
	 * 
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Endeca.
	 * @param correlationId
	 *            to track the request.
	 * @param sku
	 *            sku reference.
	 * @param skuMainList
	 *            sku list reference.
	 * @param skuInventory
	 *            sku inventory reference.
	 * 
	 */
	private void populateExtendedSkus(
			final Map<String, String> adapterResultMap,
			final String correlationId, final SKU sku,
			final List<SKUMain> skuMainList, final SKUInventory skuInventory) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.COMP_SIZE), correlationId)) {
			final SKUMain skuMain = new SKUMain();
			skuMain.setKey(ProductConstant.SIZE);
			skuMain.setValue(adapterResultMap.get(MapperConstants.COMP_SIZE));
			skuMainList.add(skuMain);
		} else {
			// added empty string as its mandatory tag.
			final SKUMain skuMain = new SKUMain();
			skuMain.setKey(ProductConstant.SIZE);
			skuMain.setValue(CommonConstants.EMPTY_STRING);
			skuMainList.add(skuMain);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.SIZE_CODE), correlationId)) {
			final SKUMain skuMain = new SKUMain();
			skuMain.setKey(ProductConstant.SIZE_CODE);
			skuMain.setValue(adapterResultMap.get(MapperConstants.SIZE_CODE));
			skuMainList.add(skuMain);
		} else {
			// added empty string as its mandatory tag.
			final SKUMain skuMain = new SKUMain();
			skuMain.setKey(ProductConstant.SIZE_CODE);
			skuMain.setValue(CommonConstants.EMPTY_STRING);
			skuMainList.add(skuMain);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.INVENTORY_AVAILABLE),
				correlationId)) {
			skuInventory.setInventoryAvailable(adapterResultMap
					.get(MapperConstants.INVENTORY_AVAILABLE));
			sku.setSkuInventory(skuInventory);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.INVENTORY_LEVEL),
				correlationId)) {
			skuInventory.setInventoryLevel(adapterResultMap
					.get(MapperConstants.INVENTORY_LEVEL));
			sku.setSkuInventory(skuInventory);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * /**Method to add multiple color to the SKUs.
	 * 
	 * @param skuMainList
	 *            SKU list.
	 * @param caolorValue
	 *            multiple color values.
	 * @param correlationId
	 *            to track the request.
	 */
	private void populateColor(final List<SKUMain> skuMainList,
			final String caolorValue, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("skuMainList : " + skuMainList, correlationId);
		// check if the value from the adapterResultMap is not null
		if (caolorValue.contains(CommonConstants.COMMA_SEPERATOR)) {
			final String[] colorArray = caolorValue
					.split(CommonConstants.COMMA_SEPERATOR);
			for (int i = 0; i < colorArray.length; i++) {
				final SKUMain skuMain = new SKUMain();
				skuMain.setKey(ProductConstant.COLOR);
				skuMain.setValue(colorArray[i]);
				skuMainList.add(skuMain);
			}
		} else {
			// added empty string as its mandatory tag.
			final SKUMain skuMain = new SKUMain();
			skuMain.setKey(ProductConstant.COLOR);
			skuMain.setValue(caolorValue);
			skuMainList.add(skuMain);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate the sku Images.
	 * 
	 * @param endecaResultMap
	 *            Map containing the product's properties as obtained from
	 *            Endeca.
	 * @param sku
	 *            product sku reference passed.
	 * @param correlationId
	 *            to track the request.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateSkuImages(final Map<String, String> endecaResultMap,
			final SKU sku, final String correlationId) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("Endeca Result Map : " + endecaResultMap, correlationId);
		final SKUImageList skuImageList = new SKUImageList();
		final List<ProductSKUImage> productSKUImages = new ArrayList<ProductSKUImage>();
		final List<ColorSwatchAttribute> colorSwatchAttributes = new ArrayList<ColorSwatchAttribute>();
		final Map<String, String> errorPropertiesMap = this.errorLoader
				.getErrorPropertiesMap();
		int imageCount = 1;
		if (endecaResultMap.containsKey(MapperConstants.PRODUCT_IMAGE_COUNT) && StringUtils.isNotBlank(endecaResultMap
				.get(MapperConstants.PRODUCT_IMAGE_COUNT))) {
			try {
				imageCount = Integer.parseInt(endecaResultMap
						.get(MapperConstants.PRODUCT_IMAGE_COUNT));
			} catch (NumberFormatException e) {
				throw new BaseException(
						String.valueOf(ErrorConstants.ERROR_CODE_11523),
						errorPropertiesMap.get(String
								.valueOf(ErrorConstants.ERROR_CODE_11523)),
								ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
								correlationId);
			}
		} else {
			LOGGER.error(
					new Throwable(MapperConstants.PRODUCT_IMAGE_COUNT
							+ " field is empty for:"
							+ endecaResultMap.get(MapperConstants.PRODUCT_CODE)
							.toString()), correlationId);
		}

		// To obtain product image URL from endeca Result Map
		final String productImageURL = endecaResultMap
				.get(MapperConstants.PRODUCT_MAIN_IMAGE_URL);
		if (StringUtils.isNotBlank(productImageURL)) {
			this.populateProductSkuImage(skuImageList, productSKUImages,
					imageCount, productImageURL, correlationId);
			this.populateExtendedSkuImages(endecaResultMap, sku, correlationId,
					colorSwatchAttributes, skuImageList);
		}
		sku.setSkuImages(skuImageList);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate the sku Images.
	 * 
	 * @param endecaResultMap
	 *            Map containing the product's properties as obtained from
	 *            Endeca.
	 * @param sku
	 *            product sku reference passed.
	 * @param correlationId
	 *            to track the request.
	 * @param colorSwatchAttributes
	 *            product color swatch attribute reference.
	 * @param skuImageList
	 *            product sku image list.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateExtendedSkuImages(
			final Map<String, String> endecaResultMap, final SKU sku,
			final String correlationId,
			final List<ColorSwatchAttribute> colorSwatchAttributes,
			final SKUImageList skuImageList) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		// To obtain swatch image URL from endeca Result Map
		final String swatchImageURL = endecaResultMap
				.get(MapperConstants.PRODUCT_SWATCH_URL);
		final Map<String, String> domainPropertiesMap = this.domainLoader
				.getDomainPropertiesMap();
		if (swatchImageURL != null) {
			final ColorSwatchAttribute smallSwatchAttribute = new ColorSwatchAttribute();
			smallSwatchAttribute.setKey(ProductConstant.SMALL);
			if (swatchImageURL.contains(domainPropertiesMap
					.get(ProductConstant.SKU_COLOR_SWATCH_SMALL))) {
				smallSwatchAttribute.setValue(swatchImageURL);
			} else {
				smallSwatchAttribute.setValue(swatchImageURL
						+ domainPropertiesMap
						.get(ProductConstant.SKU_COLOR_SWATCH_SMALL));
			}
			colorSwatchAttributes.add(smallSwatchAttribute);
			final ColorSwatchAttribute bigSwatchAttribute = new ColorSwatchAttribute();
			bigSwatchAttribute.setKey(ProductConstant.BIG);
			if (swatchImageURL.contains(domainPropertiesMap
					.get(ProductConstant.SKU_COLOR_SWATCH_SMALL))) {
				bigSwatchAttribute.setValue(swatchImageURL.replace(
						domainPropertiesMap
						.get(ProductConstant.SKU_COLOR_SWATCH_SMALL),
						domainPropertiesMap
						.get(ProductConstant.SKU_COLOR_SWATCH_BIG)));
			} else {
				bigSwatchAttribute.setValue(swatchImageURL
						+ domainPropertiesMap
						.get(ProductConstant.SKU_COLOR_SWATCH_BIG));
			}
			colorSwatchAttributes.add(bigSwatchAttribute);
			skuImageList.setColorSwatchAttributes(colorSwatchAttributes);
		} else {
			// added empty string as its mandatory tag.
			LOGGER.error(
					new Throwable(MapperConstants.PRODUCT_SWATCH_URL
							+ " field is empty for:"
							+ endecaResultMap.get(MapperConstants.PRODUCT_CODE)
							.toString()), correlationId);
			final ColorSwatchAttribute smallSwatchAttribute = new ColorSwatchAttribute();
			smallSwatchAttribute.setKey(ProductConstant.SMALL);
			smallSwatchAttribute.setValue(CommonConstants.EMPTY_STRING);
			final ColorSwatchAttribute bigSwatchAttribute = new ColorSwatchAttribute();
			bigSwatchAttribute.setKey(ProductConstant.BIG);
			bigSwatchAttribute.setValue(CommonConstants.EMPTY_STRING);
			colorSwatchAttributes.add(smallSwatchAttribute);
			colorSwatchAttributes.add(bigSwatchAttribute);
			skuImageList.setColorSwatchAttributes(colorSwatchAttributes);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate Product Sku Images.
	 * 
	 * @param skuImageList
	 *            list of SKU reference passed.
	 * @param productSKUImages
	 *            list of SKU images.
	 * @param imageCount
	 *            number of images for particular SKU.
	 * @param productImageURL
	 *            image URL passed.
	 * @param correlationId
	 *            to track the request.
	 */
	private void populateProductSkuImage(final SKUImageList skuImageList,
			final List<ProductSKUImage> productSKUImages, final int imageCount,
			final String productImageURL, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("Product Image URL : " + productImageURL, correlationId);
		final Map<String, String> domainPropertiesMap = this.domainLoader
				.getDomainPropertiesMap();
		// Looping happens based on image count.
		for (int i = 0; i < imageCount; i++) {
			final ProductSKUImage productSKUImage = new ProductSKUImage();
			final List<SKUImage> skuImageAttributes = new ArrayList<SKUImage>();
			// ImageUrl is replaced from "_A_" with alphabet character based on
			// index value.
			final String modifiedImageURL = productImageURL.replace(
					ProductConstant.SKU_IMAGE_CONSTANT_CHAR,
					CommonConstants.UNDERSCORE + ProductConstant.CHAR_ARRAY[i]
							+ CommonConstants.UNDERSCORE);
			final SKUImage mainSkuImage = new SKUImage();
			// key is set to "mainProductImage" and it's value.
			mainSkuImage.setKey(ProductConstant.SKU_MAIN_PRODUCT_IMAGE);
			mainSkuImage.setValue(modifiedImageURL);
			skuImageAttributes.add(mainSkuImage);
			this.populateProductSKUImageExtended(domainPropertiesMap,
					productSKUImage, skuImageAttributes, modifiedImageURL, correlationId);
			// productSKUImage.setDefaultImage(""); //TODO:need to populate once
			// we get key value from endeca
			productSKUImages.add(productSKUImage);
		}
		//set empty string to main image, alternate and zoom image as they mandatory tags.
		if (imageCount < 1) {
			final List<SKUImage> skuImageAttributes = new ArrayList<SKUImage>();

			//set empty string to main image url as it is mandatory tag and there is no value for it.
			final SKUImage mainSkuImage = new SKUImage();
			mainSkuImage.setKey(ProductConstant.SKU_MAIN_PRODUCT_IMAGE);
			mainSkuImage.setValue(CommonConstants.EMPTY_STRING);
			skuImageAttributes.add(mainSkuImage);

			//set empty string to alternate image url as it is mandatory tag and there is no value for it.
			final SKUImage swatchSkuImage = new SKUImage();
			swatchSkuImage
			.setKey(ProductConstant.SKU_ALTERNATE_VIEW_SWATCH_IMAGE);
			swatchSkuImage.setValue(CommonConstants.EMPTY_STRING);
			skuImageAttributes.add(swatchSkuImage);

			//set empty string to zoom image url as it is mandatory tag and there is no value for it.
			final SKUImage zoomSkuImage = new SKUImage();
			zoomSkuImage.setKey(ProductConstant.SKU_ZOOM_PRODUCT_IMAGE);
			zoomSkuImage.setValue(CommonConstants.EMPTY_STRING);
			skuImageAttributes.add(zoomSkuImage);
		}
		skuImageList.setProductSKUImages(productSKUImages);
		LOGGER.logMethodExit(startTime, correlationId);

	}


	/**
	 * Extended Method to populate Product Sku Images.
	 * 
	 * @param domainPropertiesMap
	 *            domainPropertiesMap passed.
	 * @param productSKUImage
	 *            list of SKU images.
	 * @param skuImageAttributes
	 *             SKU Image Attributes.
	 * @param modifiedImageURL
	 *            image URL passed.
	 * @param correlationId
	 *            to track the request.
	 */

	private void populateProductSKUImageExtended(
			final Map<String, String> domainPropertiesMap,
			final ProductSKUImage productSKUImage,
			final List<SKUImage> skuImageAttributes,
			final String modifiedImageURL, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final SKUImage swatchSkuImage = new SKUImage();
		// swatchSkuImage is set with the key "alternateViewSwatch".
		swatchSkuImage
		.setKey(ProductConstant.SKU_ALTERNATE_VIEW_SWATCH_IMAGE);
		// Checks if the modifiedImageURL contains "$P_Prod$".
		if (modifiedImageURL.contains(domainPropertiesMap
				.get(ProductConstant.SKU_IMAGE_MAIN))) {
			// Replace "$P_Prod$" with "$P_SWATCH$"
			swatchSkuImage
			.setValue(modifiedImageURL.replace(
					domainPropertiesMap
					.get(ProductConstant.SKU_IMAGE_MAIN),
					domainPropertiesMap
					.get(ProductConstant.SKU_IMAGE_ALTERNATE)));
		} else {
			swatchSkuImage.setValue(modifiedImageURL
					+ domainPropertiesMap
					.get(ProductConstant.SKU_IMAGE_ALTERNATE));
		}
		skuImageAttributes.add(swatchSkuImage);
		final SKUImage zoomSkuImage = new SKUImage();
		// zoomSkuImage is set with the key "zoomProductImage".
		zoomSkuImage.setKey(ProductConstant.SKU_ZOOM_PRODUCT_IMAGE);
		// Checks if the modifiedImageURL contains "$P_Prod$".
		if (modifiedImageURL.contains(domainPropertiesMap
				.get(ProductConstant.SKU_IMAGE_MAIN))) {
			// Replace "$P_Prod$" with "$PROD_DETAIL_ZOOM$"
			zoomSkuImage
			.setValue(modifiedImageURL.replace(domainPropertiesMap
					.get(ProductConstant.SKU_IMAGE_MAIN),
					domainPropertiesMap
					.get(ProductConstant.SKU_IMAGE_ZOOM)));
		} else {
			zoomSkuImage.setValue(modifiedImageURL
					+ domainPropertiesMap
					.get(ProductConstant.SKU_IMAGE_ZOOM));
		}
		skuImageAttributes.add(zoomSkuImage);
		productSKUImage.setSkuImageAttribute(skuImageAttributes);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate the sku Price.
	 * 
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Endeca.
	 * @param sku
	 *            product sku reference passed.
	 * @param correlationId
	 *            to track the request.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateSkuPrice(final Map<String, String> adapterResultMap,
			final SKU sku, final String correlationId) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final List<Price> skuPrice = new ArrayList<Price>();
		Price price = null;

		// check if the value from the adapterResultMap is not null
		if (this.commonUtil
				.isNotEmpty(adapterResultMap.get(MapperConstants.LIST_PRICE),
						correlationId)) {
			price = new Price();
			price.setKey(ProductConstant.SKU_LIST_PRICE);
			price.setValue(this.commonUtil.format(
					adapterResultMap.get(MapperConstants.LIST_PRICE),
					correlationId));
			skuPrice.add(price);
		} else {
			// added empty string as its mandatory tag.
			price = new Price();
			price.setKey(ProductConstant.SKU_LIST_PRICE);
			price.setValue(CommonConstants.EMPTY_STRING);
			skuPrice.add(price);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.SKU_SALE_PRICE),
				correlationId)) {
			final String skuSalePrice = adapterResultMap
					.get(MapperConstants.SKU_SALE_PRICE);
			if (skuSalePrice.contains(CommonConstants.COMMA_SEPERATOR)) {
				final String[] salePriceList = skuSalePrice
						.split(CommonConstants.COMMA_SEPERATOR);
				for (int p = 0; p < salePriceList.length; p++) {
					price = new Price();
					price.setKey(ProductConstant.SKU_SALE_PRICE);
					price.setValue(this.commonUtil.format(salePriceList[p],
							correlationId));
					skuPrice.add(price);
				}
			} else {
				price = new Price();
				price.setKey(ProductConstant.SKU_SALE_PRICE);
				price.setValue(this.commonUtil.format(skuSalePrice,
						correlationId));
				skuPrice.add(price);
			}

		} else {
			// added empty string as its mandatory tag.
			price = new Price();
			price.setKey(ProductConstant.SKU_SALE_PRICE);
			price.setValue(CommonConstants.EMPTY_STRING);
			skuPrice.add(price);
		}

		sku.setSkuPrice(skuPrice);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate the Product Details.
	 * 
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Endeca.
	 * @param productDetails
	 *            product details reference passed.
	 * @param optionNodes
	 *            list of optionNodes to set
	 * @param correlationId
	 *            to track the request.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateProductDetails(
			final Map<String, String> adapterResultMap,
			final ProductDetails productDetails, final List<String> optionNodes,
			final String correlationId)
					throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("Endeca Result Map is " + adapterResultMap, correlationId);
		// check if the value from the adapterResultMap is not null
		boolean optionMode = false;
		if (optionNodes != null && !optionNodes.isEmpty()) {
			optionMode = true;
		}

		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.PRODUCT_CODE),
				correlationId)) {
			productDetails.setProductCode(adapterResultMap
					.get(MapperConstants.PRODUCT_CODE));
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.VENDOR_ID),
				correlationId)) {
			productDetails.setVendorId(adapterResultMap
					.get(MapperConstants.VENDOR_ID));
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.VENDOR_STYLE),
				correlationId)) {
			productDetails.setVendorPartNumber(adapterResultMap
					.get(MapperConstants.VENDOR_STYLE));
		}
		// check if the value from the adapterResultMap is not null
		if (!optionMode || optionNodes.contains(RequestConstants.BRAND)) {
			if (this.commonUtil.isNotEmpty(
					adapterResultMap.get(MapperConstants.BRAND), correlationId)) {
				// To convert brand name with special characters.
				productDetails.setBrand(Jsoup.parse(
						adapterResultMap.get(MapperConstants.BRAND)).text());
			}
		} else if (optionMode && !optionNodes.contains(RequestConstants.BRAND)) {
			productDetails.setBrand(null);
		}
		// check if the value from the adapterResultMap is not null
		if (!optionMode || optionNodes.contains(RequestConstants.NAME)) {
			if (this.commonUtil.isNotEmpty(
					adapterResultMap.get(MapperConstants.PRODUCT_NAME),
					correlationId)) {
				productDetails.setName(Jsoup.parse(
						adapterResultMap.get(MapperConstants.PRODUCT_NAME)).text());
			}
		} else if (optionMode && !optionNodes.contains(RequestConstants.NAME)) {
			productDetails.setName(null);
		}
		// method added to avoid cyclometric complex.
		this.populateProductDesc(adapterResultMap, productDetails, optionNodes,
				correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}



	/**
	 * Method to populate a product type
	 * 
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Endeca.
	 * @param productDetails
	 *            product details reference passed.
	 * @param correlationId
	 *            to track the request.
	 * @throws BaseException
	 *             The general exception thrown from this layer.
	 */
	private void populateProductType(
			final Map<String, String> adapterResultMap,
			final ProductDetails productDetails, final String correlationId)
					throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("adapterResultMap : " + adapterResultMap, correlationId);
		final String patternCode = this.getProductPatternCode(adapterResultMap,
				correlationId);
		if (this.commonUtil
				.isNotEmpty(adapterResultMap.get(MapperConstants.IS_PATTERN),
						correlationId)) {
			productDetails.setIsPattern(adapterResultMap
					.get(MapperConstants.IS_PATTERN));
			this.populateProductType(productDetails,
					adapterResultMap.get(MapperConstants.IS_PATTERN),
					patternCode, correlationId);
		} else {
			LOGGER.error(new Throwable("isPattern field is empty for:"
					+ adapterResultMap.get(MapperConstants.PRODUCT_CODE)
					.toString()), correlationId);
		}
		LOGGER.logMethodExit(startTime, correlationId);

	}

	/**
	 * Method to get productDesc and productpattern codes.
	 * 
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Endeca.
	 * @param productDetails
	 *            product details object.
	 * @param optionNodes
	 *            list of optionNodes to set
	 * @param correlationId
	 *            to track the request.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 * 
	 */
	private void populateProductDesc(
			final Map<String, String> adapterResultMap,
			final ProductDetails productDetails, final List<String> optionNodes,
			final String correlationId)
					throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("adapterResultMap : " + adapterResultMap, correlationId);
		boolean optionMode = false;
		if (optionNodes != null && !optionNodes.isEmpty()) {
			optionMode = true;
		}
		// check if the value from the adapterResultMap is not null
		if (!optionMode || optionNodes.contains(RequestConstants.SHORT_DESC)) {
			if (this.commonUtil
					.isNotEmpty(adapterResultMap
							.get(MapperConstants.PRODUCT_SHORT_DESCRIPTION),
							correlationId)) {
				productDetails.setShortDescription(Jsoup.parse(
						adapterResultMap
						.get(MapperConstants.PRODUCT_SHORT_DESCRIPTION))
						.text());
			}
		} else if (optionMode
				&& !optionNodes.contains(RequestConstants.SHORT_DESC)) {
			productDetails.setShortDescription(null);
		}
		// check if the value from the adapterResultMap is not null
		if (!optionMode || optionNodes.contains(RequestConstants.LONG_DESC)) {
			if (this.commonUtil.isNotEmpty(
					adapterResultMap.get(MapperConstants.PRODUCT_LONG_DESCRIPTION),
					correlationId)) {
				productDetails.setLongDescription(Jsoup.parse(
						adapterResultMap
						.get(MapperConstants.PRODUCT_LONG_DESCRIPTION))
						.text());
			}
		} else if (optionMode
				&& !optionNodes.contains(RequestConstants.LONG_DESC)) {
			productDetails.setLongDescription(null);
		}

		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to get productpattern codes.
	 * 
	 * @param endecaResultMap
	 *            Map containing the product's properties as obtained from
	 *            Endeca.
	 * @param correlationId
	 *            to track the request.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 * @return pattern code.
	 */
	private String getProductPatternCode(
			final Map<String, String> endecaResultMap,
			final String correlationId) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("The Endeca Result Map is " + endecaResultMap,
				correlationId);
		String patternCode = null;
		if (endecaResultMap.containsKey(MapperConstants.PATTERN_CODE)) {
			patternCode = endecaResultMap.get(MapperConstants.PATTERN_CODE);
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return patternCode;
	}

	/**
	 * Method to populate product type based on pattern.
	 * 
	 * @param productDetails
	 *            product details reference passed.
	 * @param patternFlag
	 *            product type flag.
	 * @param patternCode
	 *            Child product codes.
	 * @param correlationId
	 *            to track the request.
	 * 
	 */
	private void populateProductType(final ProductDetails productDetails,
			final String patternFlag, final String patternCode,
			final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("Is pattern flag value : " + patternFlag, correlationId);
		// Checks for pattern status.
		if (ProductConstant.ISPATTERN_TRUE.equals(patternFlag)) {
			productDetails
			.setProductType(ProductConstant.PRODUCT_TYPE_ISPATTERN_TRUE);
		}
		if (ProductConstant.ISPATTERN_FALSE.equals(patternFlag)
				&& patternCode != null) {
			productDetails
			.setProductType(ProductConstant.PRODUCT_TYPE_ISPATTERN_FALSE_NOTEMPTY);
		}
		if (ProductConstant.ISPATTERN_FALSE.equals(patternFlag)
				&& patternCode == null) {
			productDetails
			.setProductType(ProductConstant.PRODUCT_TYPE_ISPATTERN_FALSE_EMPTY);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate ratings.
	 * 
	 * @param productDetails
	 *            product details reference passed.
	 * @param correlationId
	 *            to track the request.
	 */
	private void populateRating(final ProductDetails productDetails,
			final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		// created new object as its an mandatory tag.
		final Ratings rating = new Ratings();
		productDetails.setRatings(rating);
		LOGGER.logMethodExit(startTime, correlationId);
	}

}
