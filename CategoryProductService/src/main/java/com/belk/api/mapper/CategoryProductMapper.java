package com.belk.api.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.belk.api.constants.CommonConstants;
import com.belk.api.constants.DomainConstants;
import com.belk.api.constants.MapperConstants;
import com.belk.api.exception.BaseException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.model.categoryproduct.Attribute;
import com.belk.api.model.categoryproduct.Categories;
import com.belk.api.model.categoryproduct.Category;
import com.belk.api.model.categoryproduct.ProductSearch;
import com.belk.api.util.CommonUtil;

/**
 * Mapper class for the CategoryProduct.
 * 
 * Update: This class has been updated to fix the review comments, as part of
 * CR: April 2014
 * 
 * Updated : Added few comments and changed the name of variables as part of
 * Phase2, April,14 release
 * 
 * Updated : Code modified to handle empty values(hasSubcategories, totalSkus,
 * totalProducts), as part of Mandatory tags CR
 * 
 * @author Mindtree
 * @date 25 Oct, 2013
 */
@Service
public class CategoryProductMapper {
	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	@Autowired
	CommonUtil commonUtil;

	/**
	 * Default Constructor.
	 */
	public CategoryProductMapper() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param commonUtil
	 *            the commonUtil to set
	 */
	public final void setCommonUtil(final CommonUtil commonUtil) {
		this.commonUtil = commonUtil;
	}

	/**
	 * Method to convert the response map from Adapter into the desired Category
	 * Products POJO. Category Products POJO comprises of Product
	 * Search,Category and Category Attributes.
	 * 
	 * @param responseList
	 *            It is the response list
	 * @param correlationId
	 *            to track the request
	 * @return Categories Pojo which is formed from the response for the
	 *         requested criteria
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */

	public final Categories convertToCategoryPojo(
			final List<List<Map<String, String>>> responseList,
			final String correlationId) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info(
				"Resultlist passing as a parameter to the method convertToCategoryPojo"
						+ responseList, correlationId);
		final Categories categories = new Categories();

		ProductSearch productSearch = null;
		final List<Category> catList = new ArrayList<Category>();
		List<Attribute> categoryAttributesList = new ArrayList<Attribute>();

		// To loop each list(result is list of map) from the list of the
		// response(list of list of map) obtained from adapter.
		for (List<Map<String, String>> resultList : responseList) {
			final List<ProductSearch> productSearchPojoList = new ArrayList<ProductSearch>();
			final Category category = new Category();
			for (Map<String, String> responseMap : resultList) {

				if (responseMap.containsKey(MapperConstants.PRODUCT_CODE)) {
					// Populate product attributes
					productSearch = this.populateProductSearch(responseMap,
							correlationId);
					productSearchPojoList.add(productSearch);
				}

				if (responseMap.containsKey(CommonConstants.TOTAL_PRODUCTS)) {
					// Populates Category Attributes.
					categoryAttributesList = this
							.populateCategoryAttributeDetails(responseMap,
									correlationId);

				}
				this.dimensionDetails(responseMap, category,
						categoryAttributesList, correlationId);

				category.setProducts(productSearchPojoList);
				category.setCategoryAttributes(categoryAttributesList);

			}
			catList.add(category);
		}
		categories.setCategory(catList);
		LOGGER.info("categories : " + categories.toString(), correlationId);

		LOGGER.logMethodExit(startTime, correlationId);
		return categories;
	}

	/**
	 * Method to create a product search of the result obtained from Adapter.
	 * 
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Adapter
	 * @param correlationId
	 *            to track the request
	 * @return a ProductSearch object
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */

	public final ProductSearch populateProductSearch(
			final Map<String, String> adapterResultMap,
			final String correlationId) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("adapterResultMap : " + adapterResultMap, correlationId);
		final ProductSearch productSearch = new ProductSearch();
		// Populates the product attributes.
		this.populateProductMainDetails(productSearch, adapterResultMap,
				correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return productSearch;
	}

	/**
	 * Method to populate the product's main details like product code, product
	 * Id, product webId etc in the product search object.
	 * 
	 * @param productSearch
	 *            the product search reference
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Adapter
	 * @param correlationId
	 *            to track the request
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */
	public final void populateProductMainDetails(
			final ProductSearch productSearch,
			final Map<String, String> adapterResultMap,
			final String correlationId) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("AdapterResultMap is: " + adapterResultMap, correlationId);
		// checks for Product code if it contains it set the
		// Product code.
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.PRODUCT_CODE),
				correlationId)) {
			productSearch.setProductCode(adapterResultMap
					.get(MapperConstants.PRODUCT_CODE));
		}

		// checks for Product id if it contains it set the
		// Product id.
		if (this.commonUtil
				.isNotEmpty(adapterResultMap.get(MapperConstants.PRODUCT_ID),
						correlationId)) {
			productSearch.setProductId(adapterResultMap
					.get(MapperConstants.PRODUCT_ID));
		}

		// checks for web id if it contains it set the
		// web id.
		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(MapperConstants.WEB_ID), correlationId)) {
			productSearch
					.setWebId(adapterResultMap.get(MapperConstants.WEB_ID));
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to populate the category Attribute details like total products,
	 * total sku etc in the category product object.
	 * 
	 * @param adapterResultMap
	 *            Map containing the product's properties as obtained from
	 *            Adapter
	 * @param correlationId
	 *            to track the request
	 * @return List<Attribute> contains key value pairs
	 * 
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */

	public final List<Attribute> populateCategoryAttributeDetails(
			final Map<String, String> adapterResultMap,
			final String correlationId) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("AdapterResultMap : " + adapterResultMap, correlationId);
		final List<Attribute> categoryAttributesList = new ArrayList<Attribute>();

		if (this.commonUtil.isNotEmpty(
				adapterResultMap.get(CommonConstants.TOTAL_PRODUCTS),
				correlationId)) {
			// checks for total products if it contains it set the
			// total products.
			final Attribute attribute = new Attribute();
			attribute.setKey(CommonConstants.TOTAL_PRODUCTS);
			attribute.setValue(adapterResultMap
					.get(CommonConstants.TOTAL_PRODUCTS));
			categoryAttributesList.add(attribute);
		} else {
			// setting Total Products value as zero if empty value returned from
			// Endeca
			final Attribute attribute = new Attribute();
			attribute.setKey(CommonConstants.TOTAL_PRODUCTS);
			attribute.setValue(CommonConstants.ZERO_VALUE);
			categoryAttributesList.add(attribute);
		}

		if (this.commonUtil
				.isNotEmpty(adapterResultMap.get(CommonConstants.TOTAL_SKUS),
						correlationId)) {
			// checks for total skus if it contains it set the
			// total skus.
			final Attribute attribute = new Attribute();
			attribute.setKey(CommonConstants.TOTAL_SKUS);
			attribute
					.setValue(adapterResultMap.get(CommonConstants.TOTAL_SKUS));
			categoryAttributesList.add(attribute);
		} else {
			// setting Total SKUS value as zero if empty value returned from
			// Endeca
			final Attribute attribute = new Attribute();
			attribute.setKey(CommonConstants.TOTAL_SKUS);
			attribute.setValue(CommonConstants.ZERO_VALUE);
			categoryAttributesList.add(attribute);
		}

		LOGGER.logMethodExit(startTime, correlationId);
		return categoryAttributesList;
	}

	/**
	 * This method sets the category name and information about subcategory.
	 * 
	 * @param responseMap
	 *            It is response Map
	 * @param category
	 *            Category Object
	 * @param categoryAttributesList
	 *            Category Attribute List
	 * @param correlationId
	 *            CorrelationId from request
	 */
	private void dimensionDetails(final Map<String, String> responseMap,
			final Category category,
			final List<Attribute> categoryAttributesList,
			final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("AdapterResultMap to obtain Dimension: " + responseMap,
				correlationId);
		if (responseMap.containsKey(DomainConstants.DIMENSION_KEY)) {
			// checks for dimensionKey if it contains it set the
			// categoryId.

			if (this.commonUtil.isNotEmpty(
					responseMap.get(DomainConstants.DIMENSION_KEY),
					correlationId)) {
				category.setCategoryId(responseMap
						.get(DomainConstants.DIMENSION_KEY));
			}
			if (this.commonUtil.isNotEmpty(
					responseMap.get(DomainConstants.DIMENSION_LABEL),
					correlationId)) {
				// checks for label if it contains it set the name.
				category.setName(Jsoup.parse(
						responseMap.get(DomainConstants.DIMENSION_LABEL))
						.text());
			}
			if (this.commonUtil.isNotEmpty(
					responseMap.get(DomainConstants.PARENT_DIMENSION_KEY),
					correlationId)) {
				// checks for parentDimensionKey if it contains it sets
				// the parentCategoryId.
				category.setParentCategoryId(responseMap
						.get(DomainConstants.PARENT_DIMENSION_KEY));
			}

			if (this.commonUtil.isNotEmpty(
					responseMap.get(DomainConstants.HAS_FURTHER_REFINEMENTS),
					correlationId)) {
				// checks for hasFurtherRefinements if contains then
				// sets the value for subcategory.
				final Attribute attribute = new Attribute();
				attribute.setKey(CommonConstants.HAS_SUBCATEGORIES);
				attribute.setValue(Jsoup.parse(
						responseMap
								.get(DomainConstants.HAS_FURTHER_REFINEMENTS))
						.text());
				categoryAttributesList.add(0, attribute);
			} else {
				// setting hasSubcategories value as No if empty value returned
				// from Endeca
				final Attribute attribute = new Attribute();
				attribute.setKey(CommonConstants.HAS_SUBCATEGORIES);
				attribute.setValue(CommonConstants.NO_VALUE);
				categoryAttributesList.add(0, attribute);
			}

		}
		LOGGER.logMethodExit(startTime, correlationId);
	}
}
