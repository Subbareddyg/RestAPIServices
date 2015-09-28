package com.belk.api.mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.regex.PatternSyntaxException;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.belk.api.constants.CommonConstants;
import com.belk.api.constants.DomainConstants;
import com.belk.api.constants.ErrorConstants;
import com.belk.api.constants.MapperConstants;
import com.belk.api.constants.ProductConstant;
import com.belk.api.exception.BaseException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.model.productsearch.ChildProduct;
import com.belk.api.model.productsearch.ChildProductList;
import com.belk.api.model.productsearch.Attribute;
import com.belk.api.model.productsearch.Dimension;
import com.belk.api.model.productsearch.ParentCategory;
import com.belk.api.model.productsearch.Price;
import com.belk.api.model.productsearch.ProductFlag;
import com.belk.api.model.productsearch.ProductSearch;
import com.belk.api.model.productsearch.Promotion;
import com.belk.api.model.productsearch.Ratings;
import com.belk.api.model.productsearch.Refinement;
import com.belk.api.model.productsearch.Search;
import com.belk.api.model.productsearch.SearchAttribute;
import com.belk.api.model.productsearch.SearchReport;
import com.belk.api.model.productsearch.SubCategory;
import com.belk.api.util.CommonUtil;
import com.belk.api.util.ErrorLoader;

/**
 * Mapper class for the Search which converts endeca result to List of pojos.
 * Updated : Added few comments and changed the name of variables as part of
 * Phase2, April,14 release Update: added product flag for hasDealPromo for
 * API-416
 * Updated: Changes made to add new flag hasMoreColors as per API-445.
 * 
 * @author Mindtree
 * @date 04 Oct, 2013
 */
@Service
public class SearchMapper {
	/**
	 * LOGGER is for logging the logs.
	 */
	// This will return logger instance.
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	/**
	 * creating instance of Error Loader class.
	 */
	@Autowired
	ErrorLoader errorLoader;

	/**
	 * commonUtil is reference for common util.
	 */
	@Autowired
	CommonUtil commonUtil;

	/**
	 * Default constructor for search mapper.
	 */
	public SearchMapper() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the commonUtil refrence.
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
	 * Method to convert the response from adapter to the desired Search POJO.
	 * 
	 * @param resultList
	 *            to set.
	 * @param correlationId
	 *            to track the request.
	 * @return Search.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */

	public final Search convertToSearchPojo(
			final List<Map<String, String>> resultList,
			final String correlationId) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		// search is a mandatory tag so an empty object is created.
		final Search search = new Search();
		SearchReport searchReport = null;
		final List<Dimension> dimensionList = new ArrayList<Dimension>();
		final List<ProductSearch> productSearchPojoList = new ArrayList<ProductSearch>();
		ProductSearch productSearch = null;
		Dimension dimension = null;
		final List<ParentCategory> categories = new ArrayList<ParentCategory>();
		final Map<String, String> errorPropertiesMap = this.errorLoader
				.getErrorPropertiesMap();

		try {
			for (Map<String, String> resultMap : resultList) {

				if (resultMap.containsKey(MapperConstants.PRODUCT_CODE)) {
					// Method to populate all the product related details
					productSearch = this.populateProductSearch(resultMap,
							correlationId);
					productSearchPojoList.add(productSearch);
				}

				if (resultMap.containsKey(DomainConstants.DIMENSION_KEY)) {
					// Method to populate all Dimension details
					dimension = this.populateDimension(resultMap, categories,
							correlationId);
					// Every Dimension returned is added back to the Dimension
					// List

					dimensionList.add(dimension);
				}

				if (resultMap.containsKey(CommonConstants.TOTAL_PRODUCTS)) {
					// Method to populate the search report of products obtained
					// from key word search
					searchReport = this.populateSearchReport(resultMap,
							correlationId);
				}

			}
			search.setProducts(productSearchPojoList);
			if (!categories.isEmpty()) {
				search.setCategories(categories);
			}
			// If the Dimension List is greater than zero then
			// the value is set.Else set the value as null
			// becos Dimensions is an optional field(if there is no dimension
			// no tag should be populated)
			if (!dimensionList.isEmpty()) {
				search.setDimensions(dimensionList);
			} else {
				search.setDimensions(null);
			}
			search.setSearchReport(searchReport);
		} catch (PatternSyntaxException e) {
			LOGGER.error(e, correlationId);
			throw new BaseException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String
							.valueOf(ErrorConstants.ERROR_CODE_11523)),
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);
		}
		LOGGER.debug("Search Report after converting to pojo : " + search,
				correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return search;
	}

	/**
	 * Method to populate the dimension attributes of a dimension.
	 * 
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Adapter.
	 * @param categories
	 *            list of parent category elements.
	 * @param correlationId
	 *            to track the request.
	 * @return A dimension entry.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private Dimension populateDimension(
			final Map<String, String> adapterResultMap,
			final List<ParentCategory> categories, final String correlationId)
			throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("adapterResultMap : " + adapterResultMap, correlationId);
		final Dimension dimension = new Dimension();
		ParentCategory parentCategory = null;
		final List<Attribute> dimensionAttributes = new ArrayList<Attribute>();
		Attribute attribute = new Attribute();
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(DomainConstants.DIMENSION_KEY),
				correlationId)) {
			attribute = new Attribute();
			attribute.setKey(DomainConstants.DIMENSION_KEY);
			attribute
					.setValue(Jsoup
							.parse(adapterResultMap
									.get(DomainConstants.DIMENSION_KEY)).text());
			dimensionAttributes.add(attribute);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(DomainConstants.DIMENSION_LABEL),
				correlationId)) {
			attribute = new Attribute();
			attribute.setKey(DomainConstants.DIMENSION_LABEL);
			attribute.setValue(Jsoup.parse(
					adapterResultMap.get(DomainConstants.DIMENSION_LABEL))
					.text());
			dimensionAttributes.add(attribute);
			if (adapterResultMap.get(DomainConstants.DIMENSION_LABEL)
					.equalsIgnoreCase(DomainConstants.CATEGORY)) {
				parentCategory = this.populateCategory(adapterResultMap,
						correlationId);
				categories.add(parentCategory);
			}
		}
		this.populateExtendedDimensionOptions(adapterResultMap, correlationId,
				dimension, dimensionAttributes);
		dimension.setDimensionAttributes(dimensionAttributes);
		LOGGER.debug("Dimensions  : " + dimension, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return dimension;
	}

	/**
	 * Method to populate the additional dimension attributes.
	 * 
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Adapter.
	 * @param dimension
	 *            reference of dimension.
	 * @param correlationId
	 *            to track the request.
	 * @param dimensionAttributes
	 *            reference of dimension attributes
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateExtendedDimensionOptions(
			final Map<String, String> adapterResultMap,
			final String correlationId, final Dimension dimension,
			final List<Attribute> dimensionAttributes) throws BaseException {
		Attribute attribute;
		final long startTime = LOGGER.logMethodEntry(correlationId);
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(DomainConstants.MULTI_SELECT_ENABLED),
				correlationId)) {
			attribute = new Attribute();
			attribute.setKey(DomainConstants.MULTI_SELECT_ENABLED);
			attribute.setValue(adapterResultMap
					.get(DomainConstants.MULTI_SELECT_ENABLED));
			dimensionAttributes.add(attribute);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(DomainConstants.MULTI_SELECT_OPERATION),
				correlationId)) {
			attribute = new Attribute();
			attribute.setKey(DomainConstants.MULTI_SELECT_OPERATION);
			attribute.setValue(adapterResultMap
					.get(DomainConstants.MULTI_SELECT_OPERATION));
			dimensionAttributes.add(attribute);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(DomainConstants.REFINEMENTS),
				correlationId)) {
			dimension.setRefinements(this.tokenizeRefinementList(
					adapterResultMap.get(DomainConstants.REFINEMENTS),
					correlationId));
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate the category attributes of a dimension for a product.
	 * 
	 * @param adapterResultMap
	 *            adapter response.
	 * @param correlationId
	 *            to track the request.
	 * @return parent category element.
	 * @throws BaseException
	 *             the exception thrown from the domain layer.
	 */
	private ParentCategory populateCategory(
			final Map<String, String> adapterResultMap,
			final String correlationId) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final ParentCategory parentCategory = new ParentCategory();
		Attribute attribute = null;
		final List<Attribute> parentCatAttributes = new ArrayList<Attribute>();
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(DomainConstants.DIMENSION_KEY),
				correlationId)) {
			attribute = new Attribute();
			attribute.setKey(DomainConstants.CATEGORY_ID);
			attribute.setValue(adapterResultMap
					.get(DomainConstants.DIMENSION_KEY));
			parentCatAttributes.add(attribute);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(DomainConstants.DIMENSION_LABEL),
				correlationId)) {
			attribute = new Attribute();
			attribute.setKey(DomainConstants.CATEGORY_NAME);
			attribute.setValue(adapterResultMap
					.get(DomainConstants.DIMENSION_LABEL));
			parentCatAttributes.add(attribute);
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(DomainConstants.REFINEMENTS),
				correlationId)) {
			parentCategory.setSubCategories(this.tokenizeSubCategoryList(
					adapterResultMap.get(DomainConstants.REFINEMENTS),
					correlationId));
		}
		parentCategory.setCategoryAttributes(parentCatAttributes);
		LOGGER.logMethodExit(startTime, correlationId);
		return parentCategory;
	}

	/**
	 * Method to populate all the product related details using the result
	 * obtained from Adapter.
	 * 
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Adapter.
	 * @param correlationId
	 *            to track the request.
	 * @return a ProductSearch object.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private ProductSearch populateProductSearch(
			final Map<String, String> adapterResultMap,
			final String correlationId) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("adapterResultMap is: " + adapterResultMap, correlationId);
		final ProductSearch productSearch = new ProductSearch();
		// Populate the product attributes.
		this.populateProductMainDetails(productSearch, adapterResultMap,
				correlationId);
		// populate list of product price values.
		this.populateProductPrice(productSearch, adapterResultMap,
				correlationId);
		this.populateProductMiscDetails(productSearch, adapterResultMap,
				correlationId);
		// populates list of product flags
		this.populateProductFlag(productSearch, adapterResultMap, correlationId);
		this.populateProductPromotionDetails(productSearch, adapterResultMap,
				correlationId);
		// populates the list of product attributes
		this.populateProductAttributes(productSearch, adapterResultMap,
				correlationId);
		// // populates the list of product extended attributes
		this.populateProductExtendedAttributes(productSearch, adapterResultMap,
				correlationId);
		// populates child products
		this.populateChildProducts(adapterResultMap, productSearch,
				correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return productSearch;
	}

	/**
	 * Method to populate the product's main details like product code, vendor.
	 * info, etc in the product search object
	 * 
	 * @param productSearch
	 *            the product search reference.
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from.
	 *            Adapter
	 * @param correlationId
	 *            to track the request.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateProductMainDetails(final ProductSearch productSearch,
			final Map<String, String> adapterResultMap,
			final String correlationId) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("The adapterResultMap : " + adapterResultMap, correlationId);

		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.PRODUCT_CODE),
				correlationId)) {
			productSearch.setProductCode(adapterResultMap
					.get(MapperConstants.PRODUCT_CODE));
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil
				.isNotEmpty(adapterResultMap.get(MapperConstants.PRODUCT_ID),
						correlationId)) {
			productSearch.setProductId(adapterResultMap
					.get(MapperConstants.PRODUCT_ID));
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.WEB_ID), correlationId)) {
			productSearch
					.setWebId(adapterResultMap.get(MapperConstants.WEB_ID));
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.VENDOR_ID), correlationId)) {
			productSearch.setVendorId(adapterResultMap
					.get(MapperConstants.VENDOR_ID));
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.VENDOR_STYLE),
				correlationId)) {
			productSearch.setVendorPartNumber(adapterResultMap
					.get(MapperConstants.VENDOR_STYLE));
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.BRAND), correlationId)) {
			// To convert brand name with special characters.
			productSearch.setBrand(Jsoup.parse(
					adapterResultMap.get(MapperConstants.BRAND)).text());
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.PRODUCT_NAME),
				correlationId)) {
			productSearch.setName(Jsoup.parse(
					adapterResultMap.get(MapperConstants.PRODUCT_NAME)).text());
		}
		// method added to avoid cyclometric complex.
		this.populateProductPattern(adapterResultMap, productSearch,
				correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate product type and productpattern.
	 * 
	 * @param adapterResultMap
	 *            adapterResult.
	 * @param productSearch
	 *            object to which the productSearch data gets added.
	 * @param correlationId
	 *            to track the request.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateProductPattern(
			final Map<String, String> adapterResultMap,
			final ProductSearch productSearch, final String correlationId)
			throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final String patternCode = this.getProductPatternCode(adapterResultMap,
				correlationId);
		// it will check "P_product_type".
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil
				.isNotEmpty(adapterResultMap.get(MapperConstants.IS_PATTERN),
						correlationId)) {
			this.populateProductType(productSearch,
					adapterResultMap.get(MapperConstants.IS_PATTERN),
					patternCode, correlationId);
		}

		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to get productpattern codes.
	 * 
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Adapter.
	 * @param correlationId
	 *            to track the request.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 * @return pattern code.
	 */
	private String getProductPatternCode(
			final Map<String, String> adapterResultMap,
			final String correlationId) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("adapterResultMap is " + adapterResultMap, correlationId);
		String patternCode = null;
		if (adapterResultMap.containsKey(MapperConstants.PATTERN_CODE)) {
			patternCode = adapterResultMap.get(MapperConstants.PATTERN_CODE);
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return patternCode;
	}

	/**
	 * Method to populate the product's miscellaneous details in the product
	 * search object.
	 * 
	 * @param productSearch
	 *            the product search reference.
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Adapter.
	 * @param correlationId
	 *            to track the request.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateProductMiscDetails(final ProductSearch productSearch,
			final Map<String, String> adapterResultMap,
			final String correlationId) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final Ratings ratings = new Ratings();
		LOGGER.info("adapterResultMap : " + adapterResultMap, correlationId);
		/*
		 * if (this.commonUtil.isNotEmpty(
		 * adapterResultMap.get(MapperConstants.OVERALL_RATING), correlationId))
		 * { // Once rating is enabled the rating object to be set with the //
		 * corresponding values productSearch.setRatings(ratings); }
		 */
		productSearch.setRatings(ratings);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate the Product Price.
	 * 
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Adapter.
	 * @param productSearch
	 *            product search reference passed.
	 * @param correlationId
	 *            to track the request.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateProductPrice(final ProductSearch productSearch,
			final Map<String, String> adapterResultMap,
			final String correlationId) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final List<Price> productPrice = new ArrayList<Price>();
		this.populateListPrice(adapterResultMap, productPrice, correlationId);
		this.populateSalePrice(adapterResultMap, productPrice, correlationId);
		LOGGER.debug("Output from populateProductPrice method : "
				+ productPrice, correlationId);
		productSearch.setProductPrice(productPrice);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate the Product List Price.
	 * 
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Adapter.
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
		LOGGER.info("The adapterResultMap is: " + adapterResultMap,
				correlationId);
		Price price = null;
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
	 * Method to populate extended list price.
	 * 
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Adapter.
	 * @param productPrice
	 *            List of Product price.
	 * @param correlationId
	 *            to track the request.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateExtendedListPrice(
			final Map<String, String> adapterResultMap,
			final List<Price> productPrice, final String correlationId)
			throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("The adapterResultMap is: " + adapterResultMap,
				correlationId);
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
	 *            Adapter.
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
		LOGGER.info("The adapterResultMap is " + adapterResultMap,
				correlationId);
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
		this.populateExtendedSalePrice(adapterResultMap, productPrice,
				correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * 
	 * @param adapterResultMap
	 *            adapterResultMap Map containing the product's properties as
	 *            obtained from Adapter.
	 * @param productPrice
	 *            List of Product price.
	 * @param correlationId
	 *            to track the request.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateExtendedSalePrice(
			final Map<String, String> adapterResultMap,
			final List<Price> productPrice, final String correlationId)
			throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		Price price = null;
		LOGGER.info("The adapterResultMap is " + adapterResultMap,
				correlationId);
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
	 *            Adapter.
	 * @param productSearch
	 *            product search reference passed.
	 * @param correlationId
	 *            to track the request.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateProductFlag(final ProductSearch productSearch,
			final Map<String, String> adapterResultMap,
			final String correlationId) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("AdapterResultMap : " + adapterResultMap, correlationId);
		final List<ProductFlag> productFlagList = new ArrayList<ProductFlag>();
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
		this.populateAdditionalProductFlag(adapterResultMap, correlationId,
				productFlagList);
		// To avoid cyclomatic complexity extracted the implementation to
		// different method
		this.populateProductFlagExtended(adapterResultMap, correlationId,
				productFlagList);
		productSearch.setProductFlags(productFlagList);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate additional Product Flags.
	 * 
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Adapter.
	 * @param correlationId
	 *            to track the request.
	 * @param productFlagList
	 *            the list to which the product flags gets added.
	 */
	private void populateAdditionalProductFlag(
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
					correlationId));
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
	 * Method to populate additional Product Flags.
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
		LOGGER.info("AdapterResultMap : " + adapterResultMap, correlationId);
		ProductFlag productFlag;
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
					correlationId));
			productFlagList.add(productFlag);
		} else {
			// added No Value as it is a mandatory tag
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.NEW_ARRIVAL);
			productFlag.setValue(CommonConstants.FLAG_NO_VALUE);
			productFlagList.add(productFlag);
		}
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
			// added No Value as it is a mandatory tag
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.ON_SALE);
			productFlag.setValue(CommonConstants.FLAG_NO_VALUE);
			productFlagList.add(productFlag);
		}

		// Added for API-416.
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.HAS_DEAL_PROMO),
				correlationId)) {
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

		// Added for API-445.
		// check if the value from the adapterResultMap is not null and if the
		// MAX_COLOR_DESC and MIN_COLOR_DESC are different.
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.MIN_COLOR_DESC),
				correlationId)
				&& this.commonUtil.isNotEmpty(
						adapterResultMap.get(MapperConstants.MAX_COLOR_DESC),
						correlationId)
				&& !adapterResultMap.get(MapperConstants.MIN_COLOR_DESC)
						.equalsIgnoreCase(
								adapterResultMap
										.get(MapperConstants.MAX_COLOR_DESC))) {
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.HAS_MORE_COLORS);
			productFlag.setValue(CommonConstants.FLAG_YES_VALUE);
			productFlagList.add(productFlag);
		} else {
			// added No Value as it is a mandatory tag
			productFlag = new ProductFlag();
			productFlag.setKey(ProductConstant.HAS_MORE_COLORS);
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
	 *            Adapter.
	 * @param productSearch
	 *            product search reference passed.
	 * @param correlationId
	 *            to track the request.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateProductAttributes(final ProductSearch productSearch,
			final Map<String, String> adapterResultMap,
			final String correlationId) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("ResultMap is: " + adapterResultMap, correlationId);
		Attribute attribute = null;
		final List<Attribute> productAttributeList = new ArrayList<Attribute>();
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.PRODUCT_MAIN_IMAGE_URL),
				correlationId)) {
			attribute = new Attribute();
			attribute.setKey(ProductConstant.PRODUCT_IMAGE);
			attribute.setValue(adapterResultMap
					.get(MapperConstants.PRODUCT_MAIN_IMAGE_URL));
			productAttributeList.add(attribute);
		} else {
			// added empty string as its mandatory tag.
			attribute = new Attribute();
			attribute.setKey(ProductConstant.PRODUCT_IMAGE);
			attribute.setValue(CommonConstants.EMPTY_STRING);
			productAttributeList.add(attribute);
		}

		if (!productAttributeList.isEmpty()) {
			productSearch.setProductAttributes(productAttributeList);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate Extended attributes for a product.
	 * 
	 * @param productSearch
	 *            product search reference passed.
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Adapter.
	 * @param correlationId
	 *            to track the request.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateProductExtendedAttributes(
			final ProductSearch productSearch,
			final Map<String, String> adapterResultMap,
			final String correlationId) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("ResultMap is" + adapterResultMap, correlationId);
		final List<Attribute> extendedAttributes = new ArrayList<Attribute>();
		if (!extendedAttributes.isEmpty()) {
			productSearch.setExtendedAttributes(extendedAttributes);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate the Child Products.
	 * 
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Endeca.
	 * @param productSearch
	 *            product search reference passed.
	 * @param correlationId
	 *            to track the request.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateChildProducts(
			final Map<String, String> adapterResultMap,
			final ProductSearch productSearch, final String correlationId)
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
			productSearch.setChildProducts(childProductList);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate promotion details for a product.
	 * 
	 * @param productSearch
	 *            product search reference passed.
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Adapter.
	 * @param correlationId
	 *            to track the request.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	private void populateProductPromotionDetails(
			final ProductSearch productSearch,
			final Map<String, String> adapterResultMap,
			final String correlationId) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("Adapter Result Map : " + adapterResultMap, correlationId);
		final List<Promotion> promotions = new ArrayList<Promotion>();
		Promotion promotion = null;
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil
				.isNotEmpty(adapterResultMap.get(MapperConstants.PROMOTIONS),
						correlationId)) {

			final StringTokenizer promotionListString = new StringTokenizer(
					adapterResultMap.get(MapperConstants.PROMOTIONS),
					CommonConstants.PIPE_SEPERATOR);
			while (promotionListString.hasMoreElements()) {
				final StringTokenizer promotionString = new StringTokenizer(
						promotionListString.nextToken(),
						CommonConstants.FIELD_PAIR_SEPARATOR);
				while (promotionString.hasMoreElements()) {
					promotion = new Promotion();
					promotion.setKey(promotionString.nextToken());
					promotion.setValue(promotionString.nextToken());
					promotions.add(promotion);
				}
			}

		}

		if (!promotions.isEmpty()) {
			productSearch.setPromotions(promotions);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to create a search report of all products like total products,
	 * total skus etc.. using the result obtained from adapter.
	 * 
	 * @param adapterResultMap
	 *            Map containing search report values.
	 * @param correlationId
	 *            to track the request.
	 * @return a search report object.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 * @throws PatternSyntaxException
	 *             The exception thrown due to string split from Domain Layer.
	 */
	private SearchReport populateSearchReport(
			final Map<String, String> adapterResultMap,
			final String correlationId) throws BaseException,
			PatternSyntaxException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("Adapter Result Map is: " + adapterResultMap, correlationId);
		final SearchReport searchReport = new SearchReport();

		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(CommonConstants.TOTAL_PRODUCTS),
				correlationId)) {
			searchReport.setTotalProducts(adapterResultMap
					.get(CommonConstants.TOTAL_PRODUCTS));
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil
				.isNotEmpty(adapterResultMap.get(CommonConstants.TOTAL_SKUS),
						correlationId)) {
			searchReport.setTotalSkus(adapterResultMap
					.get(CommonConstants.TOTAL_SKUS));
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(CommonConstants.KEYWORD), correlationId)) {
			final String keyword = adapterResultMap
					.get(CommonConstants.KEYWORD);

			if (null != keyword) {
				searchReport.setKeyword(Jsoup.parse(keyword).text());
			}
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(DomainConstants.REFINEMENT_ID),
				correlationId)) {
			searchReport.setRefinementId(adapterResultMap
					.get(DomainConstants.REFINEMENT_ID));
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(DomainConstants.LIMIT), correlationId)) {
			searchReport.setLimit(adapterResultMap.get(DomainConstants.LIMIT));
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(DomainConstants.OFFSET), correlationId)) {
			searchReport
					.setOffset(adapterResultMap.get(DomainConstants.OFFSET));
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil
				.isNotEmpty(adapterResultMap.get(DomainConstants.ATTRIBUTES),
						correlationId)) {
			searchReport.setAttributes(this.tokenizeAttributeList(
					adapterResultMap.get(DomainConstants.ATTRIBUTES),
					correlationId));
		}
		// check if the value from the adapterResultMap is not null
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(DomainConstants.SORT_FIELDS),
				correlationId)) {
			searchReport.setSortFields(Arrays.asList(adapterResultMap.get(
					DomainConstants.SORT_FIELDS).split(
					CommonConstants.COMMA_SEPERATOR)));
		} else {
			final List<String> sortFieldList = new ArrayList<String>();
			searchReport.setSortFields(sortFieldList);
		}

		LOGGER.logMethodExit(startTime, correlationId);
		return searchReport;
	}

	/**
	 * Method to create a list of attributes from a delimited string of
	 * attribute values.
	 * 
	 * @param attributeListValue
	 *            to set.
	 * @param correlationId
	 *            to track the request.
	 * @return attributelist
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	public final List<SearchAttribute> tokenizeAttributeList(
			final String attributeListValue, final String correlationId)
			throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("attributeListValue : " + attributeListValue, correlationId);
		final List<SearchAttribute> attributeList = new ArrayList<SearchAttribute>();
		SearchAttribute attribute = null;
		final Map<String, String> errorPropertiesMap = this.errorLoader
				.getErrorPropertiesMap();
		final StringTokenizer attributeListString = new StringTokenizer(
				attributeListValue, CommonConstants.PIPE_SEPERATOR);
		try {
			while (attributeListString.hasMoreTokens()) {
				attribute = new SearchAttribute();
				final StringTokenizer attributeString = new StringTokenizer(
						attributeListString.nextToken(),
						CommonConstants.FIELD_PAIR_SEPARATOR);

				while (attributeString.hasMoreTokens()) {
					attribute.setKey(attributeString.nextToken());
					attribute.setValues(Arrays
							.asList(attributeString.nextToken().split(
									CommonConstants.COMMA_SEPERATOR)));
				}
				attributeList.add(attribute);
			}
		} catch (NoSuchElementException e) {
			LOGGER.error(e, correlationId);
			throw new BaseException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String
							.valueOf(ErrorConstants.ERROR_CODE_11523)),
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);
		}
		LOGGER.debug("attributeList : " + attributeList, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return attributeList;
	}

	/**
	 * Method to create a list of refinements from a delimited string of
	 * refinement values.
	 * 
	 * @param refinementListValue
	 *            delimited string representing refinement list values.
	 * @param correlationId
	 *            to track the request.
	 * @return refinementEntrylist list of refinements.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	public final List<Refinement> tokenizeRefinementList(
			final String refinementListValue, final String correlationId)
			throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final List<Refinement> refinementList = new ArrayList<Refinement>();
		LOGGER.info("refinementListValue is being tokenized ", correlationId);
		final StringTokenizer stRefinementList = new StringTokenizer(
				refinementListValue, CommonConstants.PIPE_SEPERATOR);
		final Map<String, String> errorPropertiesMap = this.errorLoader
				.getErrorPropertiesMap();

		try {
			while (stRefinementList.hasMoreTokens()) {
				final Refinement refinement = new Refinement();
				final List<Attribute> refinementAttrList = new ArrayList<Attribute>();
				final StringTokenizer stRefinement = new StringTokenizer(
						stRefinementList.nextToken(),
						CommonConstants.TILDE_SEPERATOR);
				while (stRefinement.hasMoreTokens()) {
					final StringTokenizer stringToken = new StringTokenizer(
							stRefinement.nextToken(),
							CommonConstants.FIELD_PAIR_MAPPER_SEPARATOR);
					final Attribute attribute = new Attribute();
					while (stringToken.hasMoreElements()) {
						attribute.setKey(stringToken.nextToken());
						attribute.setValue(Jsoup.parse(stringToken.nextToken())
								.text());
					}
					refinementAttrList.add(attribute);
				}
				refinement.setRefinementAttributes(refinementAttrList);
				refinementList.add(refinement);

			}

		} catch (NoSuchElementException e) {
			LOGGER.error(e, correlationId);
			throw new BaseException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String
							.valueOf(ErrorConstants.ERROR_CODE_11523)),
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return refinementList;
	}

	/**
	 * Method to create a list of sub categories from a delimited string of sub
	 * category values.
	 * 
	 * @param subCategoryListValue
	 *            delimited string representing sub category list values.
	 * @param correlationId
	 *            to track the request.
	 * @return subCategoryList list of sub categories.
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	public final List<SubCategory> tokenizeSubCategoryList(
			final String subCategoryListValue, final String correlationId)
			throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final List<SubCategory> subCategoryList = new ArrayList<SubCategory>();
		LOGGER.info("subCategoryListValue : " + subCategoryListValue,
				correlationId);
		final StringTokenizer stSubCategoryList = new StringTokenizer(
				subCategoryListValue, CommonConstants.PIPE_SEPERATOR);
		final Map<String, String> errorPropertiesMap = this.errorLoader
				.getErrorPropertiesMap();
		try {
			while (stSubCategoryList.hasMoreTokens()) {
				final SubCategory subCategory = new SubCategory();
				final List<Attribute> catgeoryAttrList = new ArrayList<Attribute>();
				final StringTokenizer stSubCategory = new StringTokenizer(
						stSubCategoryList.nextToken(),
						CommonConstants.TILDE_SEPERATOR);
				while (stSubCategory.hasMoreTokens()) {
					final StringTokenizer stringToken = new StringTokenizer(
							stSubCategory.nextToken(),
							CommonConstants.FIELD_PAIR_MAPPER_SEPARATOR);
					final Attribute attribute = new Attribute();
					while (stringToken.hasMoreElements()) {
						final String key = stringToken.nextToken();
						if (DomainConstants.REFINEMENT_KEY.equals(key)) {
							attribute.setKey(DomainConstants.CATEGORY_ID);
						} else if (DomainConstants.DIMENSION_LABEL.equals(key)) {
							attribute.setKey(DomainConstants.CATEGORY_NAME);
						} else if (DomainConstants.RECORD_COUNT.equals(key)) {
							attribute.setKey(CommonConstants.COUNT);
						}
						attribute.setValue(stringToken.nextToken());
					}
					catgeoryAttrList.add(attribute);
				}
				subCategory.setCategoryAttributes(catgeoryAttrList);
				subCategoryList.add(subCategory);

			}

		} catch (NoSuchElementException e) {
			LOGGER.error(e, correlationId);
			throw new BaseException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String
							.valueOf(ErrorConstants.ERROR_CODE_11523)),
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return subCategoryList;
	}

	/**
	 * Method to populate product type based on pattern.
	 * 
	 * @param productSearch
	 *            product search reference passed.
	 * @param patternFlag
	 *            value corresponding to is pattern flag from Adapter
	 * @param patternCode
	 *            Child product codes.
	 * @param correlationId
	 *            to track the request.
	 */
	private void populateProductType(final ProductSearch productSearch,
			final String patternFlag, final String patternCode,
			final String correlationId) {
		LOGGER.info("Is pattern flag value : " + patternFlag, correlationId);
		final long startTime = LOGGER.logMethodEntry(correlationId);
		// Checks for pattern status.
		if (ProductConstant.ISPATTERN_TRUE.equals(patternFlag)) {
			productSearch
					.setProductType(ProductConstant.PRODUCT_TYPE_ISPATTERN_TRUE);
		}
		if (ProductConstant.ISPATTERN_FALSE.equals(patternFlag)
				&& patternCode != null) {
			productSearch
					.setProductType(ProductConstant.PRODUCT_TYPE_ISPATTERN_FALSE_NOTEMPTY);
		}
		if (ProductConstant.ISPATTERN_FALSE.equals(patternFlag)
				&& patternCode == null) {
			productSearch
					.setProductType(ProductConstant.PRODUCT_TYPE_ISPATTERN_FALSE_EMPTY);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}
}
