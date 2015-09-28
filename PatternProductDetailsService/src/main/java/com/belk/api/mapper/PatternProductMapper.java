package com.belk.api.mapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.belk.api.constants.CommonConstants;
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
import com.belk.api.service.constants.MapperConstants;
import com.belk.api.util.CommonUtil;

/**
 * Mapper class for the Pattern Product Details.
 * 
 * Update: This API has been introduced as part of change request:
 * CR_BELK_API_3.
 * 
 * Update: This class has been updated to handle single log file per service and
 * mapping the properties(ATR_product_description, vendor part number, copy line
 * text etc.), as part of CR: April 2014
 * 
 * 
 * 
 * @author Mindtree
 * @date 04 Oct, 2013
 */
@Service
public class PatternProductMapper {

	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	/**
	 * creating instance of common utility class.
	 */
	@Autowired
	CommonUtil commonUtil;

	/**
	 * Default Constructor.
	 */
	public PatternProductMapper() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the commonUtil
	 */
	public final CommonUtil getCommonUtil() {
		return this.commonUtil;
	}

	/**
	 * @param commonUtil
	 *            the commonUtil to set
	 */
	public final void setCommonUtil(final CommonUtil commonUtil) {
		this.commonUtil = commonUtil;
	}

	/**
	 * Method to convert the response from adapter to the desired ProductList
	 * POJO.
	 * 
	 * @param blueMartiniResultList
	 *            result list from blue martini.
	 * @param optionNodes
	 *            list of optionNodes to set
	 * @param correlationId
	 *            to track the request.
	 * @return ProductList
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	public final ProductList convertToPatternProductDetailsPojo(
			final List<Map<String, String>> blueMartiniResultList,
			final Map<String, List<String>> optionNodes,
			final String correlationId) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final ProductList productList = new ProductList();
		final List<ProductDetails> productPojoList = new ArrayList<ProductDetails>();
		ProductDetails productDetails = null;
		List<SKU> skuList = null;

		// Temporary object created to check whether iterating object is same or
		// not
		ProductDetails compareProductDetails = new ProductDetails();
		compareProductDetails.setProductCode("");
		List<String> optionValues = null;
		if (optionNodes != null) {
			optionValues = optionNodes.get(CommonConstants.OPTIONS);
		}
		boolean optionMode = false;
		// Determine if request is in "OPTIONS" mode
		if (optionNodes != null && !optionNodes.isEmpty()) {
			optionMode = true;
		}

		for (Map<String, String> responseMap : blueMartiniResultList) {
			if (responseMap.containsKey(MapperConstants.P_PRODUCT_CODE)) {

				if (!compareProductDetails.getProductCode().equals(
						responseMap.get(MapperConstants.P_PRODUCT_CODE))) {

					productDetails = this.populatePatternProductDetailsPojo(
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


	/**
	 * Extended Method to convert the response from adapter to the desired
	 * ProductList POJO.
	 * 
	 * @param correlationId
	 *            to track the request.
	 * @param optionNodes
	 *            list of optionNodes to set
	 * @param responseMap
	 *            Response Map
	 * @return ProductList
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private ProductDetails populatePatternProductDetailsPojo(
			final String correlationId, final List<String> optionNodes,
			final Map<String, String> responseMap)
					throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		ProductDetails productDetails;
		productDetails = new ProductDetails();
		boolean optionMode = false;

		if (optionNodes != null && !optionNodes.isEmpty()) {
			optionMode = true;
		}


		this.populateProductDetails(responseMap, optionNodes, productDetails,
				correlationId);

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
		// rating object is created as its a mandatory tag.
		final Ratings rating = new Ratings();
		productDetails.setRatings(rating);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate the Product heirarchy attributes.
	 * 
	 * @param blueMartiniResultMap
	 *            Map containing the product's properties as obtained from
	 *            blueMartini
	 * @param productDetails
	 *            product details reference passed
	 * @param correlationId
	 *            to track the request.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateProductHierarchyAttributes(
			final Map<String, String> blueMartiniResultMap,
			final ProductDetails productDetails, final String correlationId)
					throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		ProductHierarchyAttribute productHierarchyAttribute = null;
		// new object is create as the productHierarchyAttributeList is a
		// mandatory tag.
		final List<ProductHierarchyAttribute> productHierarchyAttributeList = new ArrayList<ProductHierarchyAttribute>();
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				blueMartiniResultMap.get(MapperConstants.P_CLASS_NAME),
				correlationId)) {
			productHierarchyAttribute = new ProductHierarchyAttribute();
			productHierarchyAttribute.setKey(ProductConstant.CLASS);
			productHierarchyAttribute.setValue(blueMartiniResultMap
					.get(MapperConstants.P_CLASS_NAME));
			productHierarchyAttributeList.add(productHierarchyAttribute);
		} else {
			// added empty string as its mandatory tag.
			productHierarchyAttribute = new ProductHierarchyAttribute();
			productHierarchyAttribute.setKey(ProductConstant.CLASS);
			productHierarchyAttribute.setValue(CommonConstants.EMPTY_STRING);
			productHierarchyAttributeList.add(productHierarchyAttribute);
		}
		this.populateExtendedProductHierarchyAttributes(blueMartiniResultMap,
				correlationId, productHierarchyAttributeList);
		LOGGER.debug("Output from populateProductHierarchyAttributes method : "
				+ productHierarchyAttributeList, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		productDetails
		.setProductHierarchyAttributes(productHierarchyAttributeList);
	}

	/**
	 * Method to populate additional Product heirarchy attributes.
	 * 
	 * @param blueMartiniResultMap
	 *            Map containing the product's properties as obtained from
	 *            blueMartini
	 * @param productHierarchyAttributeList
	 *            product hierarchy attributeList refrence passed.
	 * @param correlationId
	 *            to track the request.
	 */
	private void populateExtendedProductHierarchyAttributes(
			final Map<String, String> blueMartiniResultMap,
			final String correlationId,
			final List<ProductHierarchyAttribute> productHierarchyAttributeList) {
		ProductHierarchyAttribute productHierarchyAttribute = null;
		final long startTime = LOGGER.logMethodEntry(correlationId);
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				blueMartiniResultMap.get(MapperConstants.P_DEPT_ID),
				correlationId)) {
			productHierarchyAttribute = new ProductHierarchyAttribute();
			productHierarchyAttribute.setKey(ProductConstant.DEPT_ID);
			productHierarchyAttribute.setValue(blueMartiniResultMap
					.get(MapperConstants.P_DEPT_ID));
			productHierarchyAttributeList.add(productHierarchyAttribute);

		} else {
			// added empty string as its mandatory tag.
			productHierarchyAttribute = new ProductHierarchyAttribute();
			productHierarchyAttribute.setKey(ProductConstant.DEPT_ID);
			productHierarchyAttribute.setValue(CommonConstants.EMPTY_STRING);
			productHierarchyAttributeList.add(productHierarchyAttribute);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				blueMartiniResultMap.get(MapperConstants.P_CATEGORY),
				correlationId)) {
			productHierarchyAttribute = new ProductHierarchyAttribute();
			productHierarchyAttribute.setKey(ProductConstant.CATEGORY);
			productHierarchyAttribute.setValue(blueMartiniResultMap
					.get(MapperConstants.P_CATEGORY));
			productHierarchyAttributeList.add(productHierarchyAttribute);
		} else {
			// added empty string as its mandatory tag.
			productHierarchyAttribute = new ProductHierarchyAttribute();
			productHierarchyAttribute.setKey(ProductConstant.CATEGORY);
			productHierarchyAttribute.setValue(CommonConstants.EMPTY_STRING);
			productHierarchyAttributeList.add(productHierarchyAttribute);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate the Product Price.
	 * 
	 * @param blueMartiniResultMap
	 *            Map containing the product's properties as obtained from
	 *            BlueMartini
	 * @param productDetails
	 *            product details reference passed
	 * @param correlationId
	 *            to track the request
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateProductPrice(
			final Map<String, String> blueMartiniResultMap,
			final ProductDetails productDetails, final String correlationId)
					throws BaseException {
		LOGGER.debug("blueMartiniResultMap  : " + blueMartiniResultMap,
				correlationId);
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final List<Price> productPrice = new ArrayList<Price>();
		this.populateListPrice(blueMartiniResultMap, productPrice,
				correlationId);
		this.populateSalePrice(blueMartiniResultMap, productPrice,
				correlationId);
		LOGGER.debug("Output from populateProductPrice method : "
				+ productPrice, correlationId);
		productDetails.setProductPrice(productPrice);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate the Product List Price.
	 * 
	 * @param blueMartiniResultMap
	 *            Map containing the product's properties as obtained from
	 *            BlueMartini
	 * @param productPrice
	 *            List of Product price
	 * @param correlationId
	 *            to track the request
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateListPrice(
			final Map<String, String> blueMartiniResultMap,
			final List<Price> productPrice, final String correlationId)
					throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("blueMartiniResultMap  : " + blueMartiniResultMap,
				correlationId);
		Price price = null;
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				blueMartiniResultMap.get(MapperConstants.P_LIST_PRICE_RANGE),
				correlationId)) {
			price = new Price();
			price.setKey(ProductConstant.LIST_PRICE_RANGE);
			price.setValue(blueMartiniResultMap
					.get(MapperConstants.P_LIST_PRICE_RANGE));
			productPrice.add(price);
		} else {
			// added empty string as its mandatory tag.
			price = new Price();
			price.setKey(ProductConstant.LIST_PRICE_RANGE);
			price.setValue(CommonConstants.EMPTY_STRING);
			productPrice.add(price);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				blueMartiniResultMap.get(MapperConstants.P_LISTPRICE),
				correlationId)) {
			price = new Price();
			price.setKey(ProductConstant.LIST_PRICE);
			price.setValue(blueMartiniResultMap
					.get(MapperConstants.P_LISTPRICE));
			productPrice.add(price);
		} else {
			// added empty string as its mandatory tag.
			price = new Price();
			price.setKey(ProductConstant.LIST_PRICE);
			price.setValue(CommonConstants.EMPTY_STRING);
			productPrice.add(price);
		}
		this.populateExtendedListPrice(blueMartiniResultMap, productPrice,
				correlationId);
		LOGGER.debug("productPrice  : " + productPrice, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate additional Product List Price.
	 * 
	 * @param blueMartiniResultMap
	 *            Map containing the product's properties as obtained from
	 *            BlueMartini
	 * @param productPrice
	 *            List of Product price
	 * @param correlationId
	 *            to track the request
	 */
	private void populateExtendedListPrice(
			final Map<String, String> blueMartiniResultMap,
			final List<Price> productPrice, final String correlationId) {
		Price price = null;
		final long startTime = LOGGER.logMethodEntry(correlationId);
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				blueMartiniResultMap.get(MapperConstants.P_MIN_LIST_PRICE),
				correlationId)) {
			price = new Price();
			price.setKey(ProductConstant.MINLIST_PRICE);
			price.setValue(blueMartiniResultMap
					.get(MapperConstants.P_MIN_LIST_PRICE));
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
				blueMartiniResultMap.get(MapperConstants.P_MAX_LIST_PRICE),
				correlationId)) {
			price = new Price();
			price.setKey(ProductConstant.MAXLIST_PRICE);
			price.setValue(blueMartiniResultMap
					.get(MapperConstants.P_MAX_LIST_PRICE));
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
	 * @param blueMartiniResultMap
	 *            Map containing the product's properties as obtained from
	 *            BlueMartini
	 * @param productPrice
	 *            List of Product price
	 * @param correlationId
	 *            to track the request
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateSalePrice(
			final Map<String, String> blueMartiniResultMap,
			final List<Price> productPrice, final String correlationId)
					throws BaseException {

		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("blueMartiniResultMap  : " + blueMartiniResultMap,
				correlationId);
		Price price = null;
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				blueMartiniResultMap.get(MapperConstants.P_SALES_PRICE_RANGE),
				correlationId)) {
			price = new Price();
			price.setKey(ProductConstant.SALE_PRICE_RANGE);
			price.setValue(blueMartiniResultMap
					.get(MapperConstants.P_SALES_PRICE_RANGE));
			productPrice.add(price);
		} else {
			// added empty string as its mandatory tag.
			price = new Price();
			price.setKey(ProductConstant.SALE_PRICE_RANGE);
			price.setValue(CommonConstants.EMPTY_STRING);
			productPrice.add(price);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				blueMartiniResultMap.get(MapperConstants.P_SALE_PRICE),
				correlationId)) {
			price = new Price();
			price.setKey(ProductConstant.SALE_PRICE);
			price.setValue(blueMartiniResultMap
					.get(MapperConstants.P_SALE_PRICE));
			productPrice.add(price);
		} else {
			// added empty string as its mandatory tag.
			price = new Price();
			price.setKey(ProductConstant.SALE_PRICE);
			price.setValue(CommonConstants.EMPTY_STRING);
			productPrice.add(price);
		}
		this.populateExtendedSalePrice(blueMartiniResultMap, productPrice,
				correlationId);
		LOGGER.debug("productPrice  : " + productPrice, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate additional Product Sale Price.
	 * 
	 * @param blueMartiniResultMap
	 *            Map containing the product's properties as obtained from
	 *            BlueMartini
	 * @param productPrice
	 *            List of Product price
	 * @param correlationId
	 *            to track the request
	 */
	private void populateExtendedSalePrice(
			final Map<String, String> blueMartiniResultMap,
			final List<Price> productPrice, final String correlationId) {
		Price price = null;
		final long startTime = LOGGER.logMethodEntry(correlationId);
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				blueMartiniResultMap.get(MapperConstants.P_MIN_SALES_PRICE),
				correlationId)) {
			price = new Price();
			price.setKey(ProductConstant.MIN_SALE_PRICE);
			price.setValue(blueMartiniResultMap
					.get(MapperConstants.P_MIN_SALES_PRICE));
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
				blueMartiniResultMap.get(MapperConstants.P_MAX_SALES_PRICE),
				correlationId)) {
			price = new Price();
			price.setKey(ProductConstant.MAX_SALE_PRICE);
			price.setValue(blueMartiniResultMap
					.get(MapperConstants.P_MAX_SALES_PRICE));
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
	 * @param blueMartiniResultMap
	 *            Map containing the product's properties as obtained from
	 *            BlueMartini
	 * @param productDetails
	 *            product details reference passed
	 * @param correlationId
	 *            to track the request
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateProductFlag(
			final Map<String, String> blueMartiniResultMap,
			final ProductDetails productDetails, final String correlationId)
					throws BaseException {

		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("blueMartiniResultMap  : " + blueMartiniResultMap,
				correlationId);
		final List<ProductFlag> productFlagList = new ArrayList<ProductFlag>();
		ProductFlag productFlag = null;
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				blueMartiniResultMap.get(MapperConstants.P_ON_SALE),
				correlationId)) {
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.ON_SALE);
			productFlag.setValue(blueMartiniResultMap
					.get(MapperConstants.P_ON_SALE));
			productFlagList.add(productFlag);
		} else {
			// set "f" for value as its mandatory tag.
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.ON_SALE);
			productFlag.setValue(CommonConstants.FLAG_NO_VALUE);
			productFlagList.add(productFlag);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				blueMartiniResultMap.get(MapperConstants.P_AVAILABLE_IN_STORE),
				correlationId)) {
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.AVAILABLE_INSTORE);
			productFlag.setValue(blueMartiniResultMap
					.get(MapperConstants.P_AVAILABLE_IN_STORE));
			productFlagList.add(productFlag);
		} else {
			// set "f" for value as its mandatory tag.
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.AVAILABLE_INSTORE);
			productFlag.setValue(CommonConstants.FLAG_NO_VALUE);
			productFlagList.add(productFlag);
		}
		this.populateProductFlagEtended(blueMartiniResultMap, correlationId,
				productFlagList);
		this.populateExtendedProductFlag(blueMartiniResultMap, correlationId,
				productFlagList);
		productDetails.setProductFlags(productFlagList);
		LOGGER.debug("productDetails  : " + productDetails, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate the extended Product Flags.
	 * 
	 * @param blueMartiniResultMap
	 *            Map containing the product's properties as obtained from
	 *            BlueMartini
	 * @param productFlagList
	 *            product flag list reference passed
	 * @param correlationId
	 *            to track the request
	 */
	private void populateProductFlagEtended(
			final Map<String, String> blueMartiniResultMap,
			final String correlationId, final List<ProductFlag> productFlagList) {
		ProductFlag productFlag = null;
		final long startTime = LOGGER.logMethodEntry(correlationId);
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				blueMartiniResultMap.get(MapperConstants.P_AVAILABLE_ONLINE),
				correlationId)) {
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.AVAILABLE_ONLINE);
			productFlag.setValue(blueMartiniResultMap
					.get(MapperConstants.P_AVAILABLE_ONLINE));
			productFlagList.add(productFlag);
		} else {
			// set "f" for value as its mandatory tag.
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.AVAILABLE_ONLINE);
			productFlag.setValue(CommonConstants.FLAG_NO_VALUE);
			productFlagList.add(productFlag);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				blueMartiniResultMap.get(MapperConstants.P_CLEARANCE),
				correlationId)) {
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.CLEARANCE);
			productFlag.setValue(blueMartiniResultMap
					.get(MapperConstants.P_CLEARANCE));
			productFlagList.add(productFlag);
		} else {
			// set "f" for value as its mandatory tag.
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.CLEARANCE);
			productFlag.setValue(CommonConstants.FLAG_NO_VALUE);
			productFlagList.add(productFlag);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate the Product Flags.
	 * 
	 * @param blueMartiniResultMap
	 *            Map containing the product's properties as obtained from
	 *            BlueMartini
	 * @param productFlagList
	 *            product flag list reference passed
	 * @param correlationId
	 *            to track the request
	 */
	private void populateExtendedProductFlag(
			final Map<String, String> blueMartiniResultMap,
			final String correlationId, final List<ProductFlag> productFlagList) {
		ProductFlag productFlag = null;
		final long startTime = LOGGER.logMethodEntry(correlationId);
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				blueMartiniResultMap.get(MapperConstants.P_NEW_ARRIVAL),
				correlationId)) {
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.NEW_ARRIVAL);
			productFlag.setValue(blueMartiniResultMap
					.get(MapperConstants.P_NEW_ARRIVAL));
			productFlagList.add(productFlag);
		} else {
			// set "f" for value as its mandatory tag.
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.NEW_ARRIVAL);
			productFlag.setValue(CommonConstants.FLAG_NO_VALUE);
			productFlagList.add(productFlag);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				blueMartiniResultMap.get(MapperConstants.P_IS_BRIDAL),
				correlationId)) {
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.IS_BRIDAL);
			productFlag.setValue(blueMartiniResultMap
					.get(MapperConstants.P_IS_BRIDAL));
			productFlagList.add(productFlag);
		} else {
			// set "f" for value as its mandatory tag.
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.IS_BRIDAL);
			productFlag.setValue(CommonConstants.FLAG_NO_VALUE);
			productFlagList.add(productFlag);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				blueMartiniResultMap.get(MapperConstants.P_IS_DROP_SHIP),
				correlationId)) {
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.IS_DROP_SHIP);
			productFlag.setValue(this.commonUtil.convertToFlag(
					blueMartiniResultMap.get(MapperConstants.P_IS_DROP_SHIP),
					correlationId));
			productFlagList.add(productFlag);
		} else {
			// set "f" for value as its mandatory tag.
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.IS_DROP_SHIP);
			productFlag.setValue(CommonConstants.FLAG_NO_VALUE);
			productFlagList.add(productFlag);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate the Product Attributes.
	 * 
	 * @param blueMartiniResultMap
	 *            Map containing the product's properties as obtained from
	 *            BlueMartini
	 * @param productDetails
	 *            product details reference passed
	 * @param correlationId
	 *            to track the request
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateProductAttributes(
			final Map<String, String> blueMartiniResultMap,
			final ProductDetails productDetails, final String correlationId)
					throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("blueMartiniResultMap  : " + blueMartiniResultMap,
				correlationId);
		final List<Attribute> productAttributeList = new ArrayList<Attribute>();
		Attribute productAttribute = null;

		if (blueMartiniResultMap.get(MapperConstants.P_COPY_TEXT1) != null) {
			productAttribute = new Attribute();
			productAttribute.setKey(ProductConstant.COPY_LINE_1);
			productAttribute.setValue(blueMartiniResultMap
					.get(MapperConstants.P_COPY_TEXT1));
			productAttributeList.add(productAttribute);
		}
		if (blueMartiniResultMap.get(MapperConstants.P_COPY_TEXT2) != null) {
			productAttribute = new Attribute();
			productAttribute.setKey(ProductConstant.COPY_LINE_2);
			productAttribute.setValue(blueMartiniResultMap
					.get(MapperConstants.P_COPY_TEXT2));
			productAttributeList.add(productAttribute);
		}
		if (blueMartiniResultMap.get(MapperConstants.P_COPY_TEXT3) != null) {
			productAttribute = new Attribute();
			productAttribute.setKey(ProductConstant.COPY_LINE_3);
			productAttribute.setValue(blueMartiniResultMap
					.get(MapperConstants.P_COPY_TEXT3));
			productAttributeList.add(productAttribute);
		}
		this.populateExtendedProductAttributes(blueMartiniResultMap,
				correlationId, productAttributeList);
		if (!productAttributeList.isEmpty()) {
			productDetails.setProductAttributes(productAttributeList);
		}
		LOGGER.debug("Output from populateProductAttributes method : "
				+ productDetails, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate the Product Attributes.
	 * 
	 * @param blueMartiniResultMap
	 *            Map containing the product's properties as obtained from
	 *            BlueMartini
	 * @param productAttributeList
	 *            product attributeList reference passed
	 * @param correlationId
	 *            to track the request
	 */
	private void populateExtendedProductAttributes(
			final Map<String, String> blueMartiniResultMap,
			final String correlationId,
			final List<Attribute> productAttributeList) {
		Attribute productAttribute = null;
		final long startTime = LOGGER.logMethodEntry(correlationId);
		if (blueMartiniResultMap.get(MapperConstants.P_COPY_TEXT4) != null) {
			productAttribute = new Attribute();
			productAttribute.setKey(ProductConstant.COPY_LINE_4);
			productAttribute.setValue(blueMartiniResultMap
					.get(MapperConstants.P_COPY_TEXT4));
			productAttributeList.add(productAttribute);
		}
		if (blueMartiniResultMap.get(MapperConstants.P_COPY_TEXT5) != null) {
			productAttribute = new Attribute();
			productAttribute.setKey(ProductConstant.COPY_LINE_5);
			productAttribute.setValue(blueMartiniResultMap
					.get(MapperConstants.P_COPY_TEXT5));
			productAttributeList.add(productAttribute);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				blueMartiniResultMap.get(MapperConstants.P_DEFAULT_SKU),
				correlationId)) {
			productAttribute = new Attribute();
			productAttribute.setKey(ProductConstant.DEFAULT_SKU);
			productAttribute.setValue(blueMartiniResultMap
					.get(MapperConstants.P_DEFAULT_SKU));
			productAttributeList.add(productAttribute);
		} else {
			// added empty string as its mandatory tag.
			productAttribute = new Attribute();
			productAttribute.setKey(ProductConstant.DEFAULT_SKU);
			productAttribute.setValue(CommonConstants.EMPTY_STRING);
			productAttributeList.add(productAttribute);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				blueMartiniResultMap.get(MapperConstants.P_SHOW_COLOR),
				correlationId)) {
			productAttribute = new Attribute();
			productAttribute.setKey(ProductConstant.SHOW_COLOR);
			productAttribute.setValue(blueMartiniResultMap
					.get(MapperConstants.P_SHOW_COLOR));
			productAttributeList.add(productAttribute);
		} else {
			// added empty string as its mandatory tag.
			productAttribute = new Attribute();
			productAttribute.setKey(ProductConstant.SHOW_COLOR);
			productAttribute.setValue(CommonConstants.EMPTY_STRING);
			productAttributeList.add(productAttribute);
		}
		this.populateExtendedProductAttribute(blueMartiniResultMap,
				correlationId, productAttributeList);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate the extended Product Attributes.
	 * 
	 * @param blueMartiniResultMap
	 *            Map containing the product's properties as obtained from
	 *            BlueMartini
	 * @param productAttributeList
	 *            product attributeList reference passed
	 * @param correlationId
	 *            to track the request
	 */
	private void populateExtendedProductAttribute(
			final Map<String, String> blueMartiniResultMap,
			final String correlationId,
			final List<Attribute> productAttributeList) {
		Attribute productAttribute = null;
		final long startTime = LOGGER.logMethodEntry(correlationId);
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				blueMartiniResultMap.get(MapperConstants.P_SHOW_SIZE),
				correlationId)) {
			productAttribute = new Attribute();
			productAttribute.setKey(ProductConstant.SHOW_SIZE);
			productAttribute.setValue(blueMartiniResultMap
					.get(MapperConstants.P_SHOW_SIZE));
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
	 * @param blueMartiniResultMap
	 *            Map containing the product's properties as obtained from
	 *            BlueMartini
	 * @param productDetails
	 *            product details reference passed
	 * @param correlationId
	 *            to track the request
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateChildProducts(
			final Map<String, String> blueMartiniResultMap,
			final ProductDetails productDetails, final String correlationId)
					throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("blueMartiniResultMap  : " + blueMartiniResultMap,
				correlationId);
		ChildProductList childProductList = null;
		final List<ChildProduct> childList = new ArrayList<ChildProduct>();

		final Iterator childProductIterator = blueMartiniResultMap.entrySet()
				.iterator();
		while (childProductIterator.hasNext()) {
			final Map.Entry<String, String> childProductEntry = (Map.Entry<String, String>) childProductIterator
					.next();
			final String key = childProductEntry.getKey();
			final String value = childProductEntry.getValue();

			if (MapperConstants.P_PATTERN_CODE.equals(key)) {
				childProductList = new ChildProductList();
				if (value.contains(",")) {
					final String[] productIds = value.split(",");
					for (String productId : productIds) {
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
				childProductList.setChildProduct(childList);
			}

		}

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
	 * @param blueMartiniResultMap
	 *            Map containing the product's properties as obtained from
	 *            BlueMartini
	 * @return List of Product skus
	 * @param correlationId
	 *            to track the request
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private SKU populateSkus(final Map<String, String> blueMartiniResultMap,
			final String correlationId) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("blueMartiniResultMap  : " + blueMartiniResultMap,
				correlationId);
		final SKU sku = new SKU();
		final List<SKU> skuList = new ArrayList<SKU>();
		final List<SKUMain> skuMainList = new ArrayList<SKUMain>();
		SKUInventory skuInventory = new SKUInventory();

		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				blueMartiniResultMap.get(MapperConstants.P_SKU_NO),
				correlationId)) {
			sku.setSkuCode(blueMartiniResultMap.get(MapperConstants.P_SKU_NO));
		}
		// check if the value from the adapterResultMap is not null, populate short description for sku
		if (this.commonUtil.isNotEmpty(
				blueMartiniResultMap.get(MapperConstants.P_SKU_SHORTDESC),
				correlationId)) {
			sku.setSkuShortDesc(blueMartiniResultMap.get(MapperConstants.P_SKU_SHORTDESC));
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				blueMartiniResultMap.get(MapperConstants.P_SKU_UPC),
				correlationId)) {
			sku.setUpcCode(blueMartiniResultMap.get(MapperConstants.P_SKU_UPC));
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				blueMartiniResultMap.get(MapperConstants.P_SKU_ID),
				correlationId)) {
			sku.setSkuId(blueMartiniResultMap.get(MapperConstants.P_SKU_ID));
		}
		this.populateColor(blueMartiniResultMap, skuMainList, correlationId);
		this.populateExtendedSuks(blueMartiniResultMap, correlationId,
				skuMainList);
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				blueMartiniResultMap.get(MapperConstants.P_INVENTORY_AVAIL),
				correlationId)) {
			skuInventory = new SKUInventory();
			skuInventory.setInventoryAvailable(blueMartiniResultMap
					.get(MapperConstants.P_INVENTORY_AVAIL));
			sku.setSkuInventory(skuInventory);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				blueMartiniResultMap.get(MapperConstants.P_INVENTORY_LEVEL),
				correlationId)) {
			skuInventory = new SKUInventory();
			skuInventory.setInventoryLevel(blueMartiniResultMap
					.get(MapperConstants.P_INVENTORY_LEVEL));
			sku.setSkuInventory(skuInventory);
		}
		sku.setSkuMainAttributes(skuMainList);
		this.populateSkuPrice(blueMartiniResultMap, sku, correlationId);
		this.populateSkuImages(blueMartiniResultMap, sku, correlationId);

		LOGGER.debug("Output from populateSkus method : " + skuList,
				correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return sku;
	}

	/**
	 * Method to populate the Product Skus.
	 * 
	 * @param blueMartiniResultMap
	 *            Map containing the product's properties as obtained from
	 *            BlueMartini
	 * @param skuMainList
	 *            sku main list refrence.
	 * @param correlationId
	 *            to track the request
	 */
	private void populateExtendedSuks(
			final Map<String, String> blueMartiniResultMap,
			final String correlationId, final List<SKUMain> skuMainList) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				blueMartiniResultMap.get(MapperConstants.P_COLOR_CODE),
				correlationId)) {
			final SKUMain skuMain = new SKUMain();
			skuMain.setKey(ProductConstant.COLOR_CODE);
			skuMain.setValue(blueMartiniResultMap
					.get(MapperConstants.P_COLOR_CODE));
			skuMainList.add(skuMain);

		} else {
			// added empty string as its mandatory tag.
			final SKUMain skuMain = new SKUMain();
			skuMain.setKey(ProductConstant.COLOR_CODE);
			skuMain.setValue(CommonConstants.EMPTY_STRING);
			skuMainList.add(skuMain);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil
				.isNotEmpty(blueMartiniResultMap.get(MapperConstants.P_SIZE),
						correlationId)) {
			final SKUMain skuMain = new SKUMain();
			skuMain.setKey(ProductConstant.SIZE);
			skuMain.setValue(blueMartiniResultMap.get(MapperConstants.P_SIZE));
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
				blueMartiniResultMap.get(MapperConstants.P_SIZE_CODE),
				correlationId)) {
			final SKUMain skuMain = new SKUMain();
			skuMain.setKey(ProductConstant.SIZE_CODE);
			skuMain.setValue(blueMartiniResultMap
					.get(MapperConstants.P_SIZE_CODE));
			skuMainList.add(skuMain);
		} else {
			// added empty string as its mandatory tag.
			final SKUMain skuMain = new SKUMain();
			skuMain.setKey(ProductConstant.SIZE_CODE);
			skuMain.setValue(CommonConstants.EMPTY_STRING);
			skuMainList.add(skuMain);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method obtains product colors.
	 * 
	 * @param blueMartiniResultMap
	 *            map conatining product
	 * @param skuMainList
	 *            List of skus
	 * @param correlationId
	 *            for tracking the request
	 */
	private void populateColor(final Map<String, String> blueMartiniResultMap,
			final List<SKUMain> skuMainList, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("blueMartiniResultMap  : " + skuMainList, correlationId);
		Boolean colorFlag = false;
		SKUMain skuMain = null;
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				blueMartiniResultMap.get(MapperConstants.P_COLOR),
				correlationId)) {
			colorFlag = true;
			skuMain = new SKUMain();
			skuMain.setKey(ProductConstant.COLOR);
			skuMain.setValue(blueMartiniResultMap.get(MapperConstants.P_COLOR));
			skuMainList.add(skuMain);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				blueMartiniResultMap.get(MapperConstants.P_COLOR2),
				correlationId)) {
			colorFlag = true;
			skuMain = new SKUMain();
			skuMain.setKey(ProductConstant.COLOR);
			skuMain.setValue(blueMartiniResultMap.get(MapperConstants.P_COLOR2));
			skuMainList.add(skuMain);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				blueMartiniResultMap.get(MapperConstants.P_COLOR3),
				correlationId)) {
			colorFlag = true;
			skuMain = new SKUMain();
			skuMain.setKey(ProductConstant.COLOR);
			skuMain.setValue(blueMartiniResultMap.get(MapperConstants.P_COLOR3));
			skuMainList.add(skuMain);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				blueMartiniResultMap.get(MapperConstants.P_COLOR1),
				correlationId)) {
			colorFlag = true;
			skuMain = new SKUMain();
			skuMain.setKey(ProductConstant.COLOR);
			skuMain.setValue(blueMartiniResultMap.get(MapperConstants.P_COLOR1));
			skuMainList.add(skuMain);
		}
		this.populateExtendedColor(skuMainList, correlationId, colorFlag);
		LOGGER.debug("Output from populateColor method : " + skuMainList,
				correlationId);
		LOGGER.logMethodExit(startTime, correlationId);

	}

	/**
	 * Method obtains product colors.
	 * 
	 * @param skuMainList
	 *            List of skus
	 * @param correlationId
	 *            for tracking the request
	 * @param colorFlag
	 *            for deciding whether color is present or not.
	 */
	private void populateExtendedColor(final List<SKUMain> skuMainList,
			final String correlationId, final Boolean colorFlag) {
		SKUMain skuMain = null;
		final long startTime = LOGGER.logMethodEntry(correlationId);
		if (colorFlag == false) {
			skuMain = new SKUMain();
			skuMain.setKey(ProductConstant.COLOR);
			skuMain.setValue(CommonConstants.EMPTY_STRING);
			skuMainList.add(skuMain);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate the sku Images.
	 * 
	 * @param blueMartiniResultMap
	 *            Map containing the product's properties as obtained from
	 *            BlueMartini
	 * @param sku
	 *            product sku reference passed
	 * @param correlationId
	 *            to track the request
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateSkuImages(
			final Map<String, String> blueMartiniResultMap, final SKU sku,
			final String correlationId) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("blueMartiniResultMap : " + blueMartiniResultMap,
				correlationId);
		final SKUImageList skuImageList = new SKUImageList();
		final List<ProductSKUImage> productSKUImages = new ArrayList<ProductSKUImage>();
		final ProductSKUImage productSKUImage = new ProductSKUImage();
		final List<SKUImage> skuImageAttributes = new ArrayList<SKUImage>();
		final List<ColorSwatchAttribute> colorSwatchAttributes = new ArrayList<ColorSwatchAttribute>();

		final ColorSwatchAttribute smallSwatchAttribute = new ColorSwatchAttribute();
		smallSwatchAttribute.setKey(ProductConstant.SMALL);
		smallSwatchAttribute.setValue(CommonConstants.EMPTY_STRING);
		colorSwatchAttributes.add(smallSwatchAttribute);

		final ColorSwatchAttribute bigSwatchAttribute = new ColorSwatchAttribute();
		bigSwatchAttribute.setKey(ProductConstant.BIG);
		bigSwatchAttribute.setValue(CommonConstants.EMPTY_STRING);
		colorSwatchAttributes.add(bigSwatchAttribute);

		skuImageList.setColorSwatchAttributes(colorSwatchAttributes);

		this.populateExtendedSkuImages(blueMartiniResultMap, correlationId,
				skuImageAttributes);

		productSKUImage.setSkuImageAttribute(skuImageAttributes);
		productSKUImages.add(productSKUImage);
		skuImageList.setProductSKUImages(productSKUImages);
		sku.setSkuImages(skuImageList);
		LOGGER.debug("Output from populateSkuImages method : " + skuImageList,
				correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate the sku Images.
	 * 
	 * @param blueMartiniResultMap
	 *            Map containing the product's properties as obtained from
	 *            BlueMartini
	 * @param skuImageAttributes
	 *            product sku image attributes reference passed
	 * @param correlationId
	 *            to track the request
	 */
	private void populateExtendedSkuImages(
			final Map<String, String> blueMartiniResultMap,
			final String correlationId, final List<SKUImage> skuImageAttributes) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				blueMartiniResultMap.get(MapperConstants.P_PICTURE_URL),
				correlationId)) {
			final SKUImage skuImage = new SKUImage();
			skuImage.setKey(ProductConstant.SKU_MAIN_PRODUCT_IMAGE);
			skuImage.setValue(blueMartiniResultMap
					.get(MapperConstants.P_PICTURE_URL));
			skuImageAttributes.add(skuImage);

		} else {
			// added empty string as its mandatory tag.
			final SKUImage skuImage = new SKUImage();
			skuImage.setKey(ProductConstant.SKU_MAIN_PRODUCT_IMAGE);
			skuImage.setValue(CommonConstants.EMPTY_STRING);
			skuImageAttributes.add(skuImage);

		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(blueMartiniResultMap
				.get(ProductConstant.SKU_ZOOM_PRODUCT_IMAGE), correlationId)) {
			final SKUImage skuImage = new SKUImage();
			skuImage.setKey(ProductConstant.SKU_ZOOM_PRODUCT_IMAGE);
			skuImage.setValue(blueMartiniResultMap
					.get(ProductConstant.SKU_ZOOM_PRODUCT_IMAGE));
			skuImageAttributes.add(skuImage);

		} else {
			// added empty string as its mandatory tag.
			final SKUImage skuImage = new SKUImage();
			skuImage.setKey(ProductConstant.SKU_ZOOM_PRODUCT_IMAGE);
			skuImage.setValue(CommonConstants.EMPTY_STRING);
			skuImageAttributes.add(skuImage);

		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(blueMartiniResultMap
				.get(ProductConstant.SKU_ALTERNATE_VIEW_SWATCH_IMAGE),
				correlationId)) {
			final SKUImage skuImage = new SKUImage();
			skuImage.setKey(ProductConstant.SKU_ALTERNATE_VIEW_SWATCH_IMAGE);
			skuImage.setValue(blueMartiniResultMap
					.get(ProductConstant.SKU_ALTERNATE_VIEW_SWATCH_IMAGE));
			skuImageAttributes.add(skuImage);

		} else {
			// added empty string as its mandatory tag.
			final SKUImage skuImage = new SKUImage();
			skuImage.setKey(ProductConstant.SKU_ALTERNATE_VIEW_SWATCH_IMAGE);
			skuImage.setValue(CommonConstants.EMPTY_STRING);
			skuImageAttributes.add(skuImage);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate the sku Price.
	 * 
	 * @param blueMartiniResultMap
	 *            Map containing the product's properties as obtained from
	 *            BlueMartini
	 * @param sku
	 *            product sku reference passed
	 * @param correlationId
	 *            to track the request
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateSkuPrice(
			final Map<String, String> blueMartiniResultMap, final SKU sku,
			final String correlationId) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("blueMartiniResultMap : " + blueMartiniResultMap,
				correlationId);
		final List<Price> skuPrice = new ArrayList<Price>();
		Price price = null;
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				blueMartiniResultMap.get(MapperConstants.P_LISTPRICE),
				correlationId)) {
			price = new Price();
			price.setKey(ProductConstant.SKU_LIST_PRICE);
			price.setValue(this.commonUtil.format(
					blueMartiniResultMap.get(MapperConstants.P_LISTPRICE),
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
				blueMartiniResultMap.get(MapperConstants.P_SKUS_SALE_PRICE),
				correlationId)) {
			price = new Price();
			price.setKey(ProductConstant.SKU_SALE_PRICE);
			price.setValue(this.commonUtil.format(
					blueMartiniResultMap.get(MapperConstants.P_SKUS_SALE_PRICE),
					correlationId));
			skuPrice.add(price);
		} else {
			// added empty string as its mandatory tag.
			price = new Price();
			price.setKey(ProductConstant.SKU_SALE_PRICE);
			price.setValue(CommonConstants.EMPTY_STRING);
			skuPrice.add(price);
		}
		sku.setSkuPrice(skuPrice);
		LOGGER.debug("Output from populateSkuPrice method : " + skuPrice,
				correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate the Product Details.
	 * 
	 * @param blueMartiniResultMap
	 *            Map containing the product's properties as obtained from
	 *            BlueMartini
	 * @param optionNodes
	 *            list of optionNodes to set
	 * @param productDetails
	 *            product details reference passed
	 * @param correlationId
	 *            to track the request
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateProductDetails(
			final Map<String, String> blueMartiniResultMap, final List<String> optionNodes,
			final ProductDetails productDetails, final String correlationId)
					throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("blueMartiniResultMap : " + blueMartiniResultMap,
				correlationId);
		boolean optionMode = false;
		if (optionNodes != null && !optionNodes.isEmpty()) {
			optionMode = true;
		}

		final Iterator productDetailsIterator = blueMartiniResultMap.entrySet()
				.iterator();
		while (productDetailsIterator.hasNext()) {
			final Map.Entry<String, String> productDetailsEntry = (Map.Entry<String, String>) productDetailsIterator
					.next();

			// JSoup to parse the HTML Content and return the text value
			if (MapperConstants.P_PRODUCT_CODE.equals(productDetailsEntry
					.getKey())) {
				productDetails.setProductCode(productDetailsEntry.getValue());
			} else if (MapperConstants.P_VENDOR_NUMBER
					.equals(productDetailsEntry.getKey())) {
				productDetails.setVendorId(productDetailsEntry.getValue());
			} else if (MapperConstants.P_BRAND.equals(productDetailsEntry
					.getKey())) {
				this.populateProductBrand(optionNodes, productDetails, optionMode,
						productDetailsEntry);
			} else if (MapperConstants.P_PRODUCT_NAME
					.equals(productDetailsEntry.getKey())) {
				this.populateProductName(optionNodes, productDetails, optionMode,
						productDetailsEntry);
			} else if (MapperConstants.P_SHORTDESC.equals(productDetailsEntry
					.getKey())) {
				this.populateProductShortDescription(optionNodes, productDetails,
						optionMode, productDetailsEntry);
			} else if (MapperConstants.P_LONGDESC.equals(productDetailsEntry
					.getKey())) {
				this.populateProductLongDescription(optionNodes, productDetails,
						optionMode, productDetailsEntry);
			} else if (MapperConstants.P_PRODUCT_TYPE
					.equals(productDetailsEntry.getKey())) {
				if (!optionMode
						|| optionNodes.contains(RequestConstants.PRODUCT_TYPE)) {
					productDetails.setProductType(productDetailsEntry.getValue());
				}
			} else if (MapperConstants.P_VENDOR_STYLE
					.equals(productDetailsEntry.getKey())) {
				productDetails.setVendorPartNumber(productDetailsEntry
						.getValue());
			}
		}
		LOGGER.debug("Output from populateProductDetails method : "
				+ blueMartiniResultMap, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * @param optionNodes list of optionNodes to set
	 * @param productDetails product details reference passed
	 * @param optionMode specifies whether option mode is present in the request or not
	 * @param productDetailsEntry product details entry reference passed
	 */
	private void populateProductLongDescription(final List<String> optionNodes,
			final ProductDetails productDetails, final boolean optionMode,
			final Map.Entry<String, String> productDetailsEntry) {
		if (!optionMode
				|| optionNodes.contains(RequestConstants.LONG_DESC)) {
			productDetails.setLongDescription(Jsoup.parse(
					productDetailsEntry.getValue()).text());
		} else if (optionMode
				&& !optionNodes.contains(RequestConstants.LONG_DESC)) {
			productDetails.setLongDescription(null);
		}
	}

	/**
	 * @param optionNodes list of optionNodes to set
	 * @param productDetails product details reference passed
	 * @param optionMode specifies whether option mode is present in the request or not
	 * @param productDetailsEntry product details entry reference passed
	 */
	private void populateProductShortDescription(
			final List<String> optionNodes,
			final ProductDetails productDetails, final boolean optionMode,
			final Map.Entry<String, String> productDetailsEntry) {
		if (!optionMode
				|| optionNodes.contains(RequestConstants.SHORT_DESC)) {
			productDetails.setShortDescription(Jsoup.parse(
					productDetailsEntry.getValue()).text());
		} else if (optionMode
				&& !optionNodes.contains(RequestConstants.SHORT_DESC)) {
			productDetails.setShortDescription(null);
		}
	}

	/**
	 * @param optionNodes list of optionNodes to set
	 * @param productDetails product details reference passed
	 * @param optionMode specifies whether option mode is present in the request or not
	 * @param productDetailsEntry product details entry reference passed
	 */
	private void populateProductName(final List<String> optionNodes,
			final ProductDetails productDetails, final boolean optionMode,
			final Map.Entry<String, String> productDetailsEntry) {
		if (!optionMode || optionNodes.contains(RequestConstants.NAME)) {
			productDetails.setName(Jsoup.parse(
					productDetailsEntry.getValue()).text());
		} else if (optionMode
				&& !optionNodes.contains(RequestConstants.NAME)) {
			productDetails.setName(null);
		}
	}

	/**
	 * @param optionNodes list of optionNodes to set
	 * @param productDetails product details reference passed
	 * @param optionMode specifies whether option mode is present in the request or not
	 * @param productDetailsEntry product details entry reference passed
	 */
	private void populateProductBrand(final List<String> optionNodes,
			final ProductDetails productDetails, final boolean optionMode,
			final Map.Entry<String, String> productDetailsEntry) {
		if (!optionMode || optionNodes.contains(RequestConstants.BRAND)) {
			productDetails.setBrand(Jsoup.parse(
					productDetailsEntry.getValue()).text());
		} else if (optionMode
				&& !optionNodes.contains(RequestConstants.BRAND)) {
			productDetails.setBrand(null);
		}
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
		// created new object as its an mandatory tag.
		final List<Promotion> promotions = new ArrayList<Promotion>();
		productDetails.setPromotions(promotions);
		LOGGER.logMethodExit(startTime, correlationId);
	}
}
