package com.belk.api.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.belk.api.constants.CommonConstants;
import com.belk.api.model.productsearch.Attribute;
import com.belk.api.model.productsearch.Dimension;
import com.belk.api.model.productsearch.ParentCategory;
import com.belk.api.model.productsearch.Price;
import com.belk.api.model.productsearch.ProductSearch;
import com.belk.api.model.productsearch.Refinement;
import com.belk.api.model.productsearch.Search;
import com.belk.api.model.productsearch.SearchAttribute;
import com.belk.api.model.productsearch.SearchReport;
import com.belk.api.model.productsearch.SubCategory;

/**
 * Unit Testing related to SearchMapperUtil class is performed. <br />
 * {@link TestSearchMapperUtil} class is written for testing methods in
 * {@link SearchMapperUtil} The unit test cases evaluates the way the methods
 * behave for the inputs given.
 * 
 * @author Mindtree
 * @date Nov 05 2013
 */
public class TestSearchMapperUtil {

	/**
	 * @return resultList
	 */
	public static List<Map<String, String>> createResultMapList() {
		final List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		final Map<String, String> resultMap1 = new HashMap<String, String>();

		createresultMapData(resultMap1);

		final Map<String, String> dimensionMap = new HashMap<String, String>();

		dimensionMap.put("dimensionKey", "123");
		dimensionMap.put("label", "Color");
		dimensionMap.put("multiSelectEnabled", "true");
		dimensionMap.put("multiSelectOperation", "OR");
		dimensionMap
				.put("refinements",
						"refinementId^12~label^Red~count^50|refinementId^14~label^Blue~count^37");
		final Map<String, String> categoryMap = new HashMap<String, String>();

		categoryMap.put("dimensionKey", "123");
		categoryMap.put("label", "category");
		dimensionMap.put("multiSelectEnabled", "false");
		categoryMap
				.put("refinements",
						"refinementId^12~label^Red~count^50|refinementId^14~label^Blue~count^37");

		final Map<String, String> searchReportMap = new HashMap<String, String>();
		searchReportMap.put("totalProducts", "140");
		searchReportMap.put("totalSkus", "2000");
		searchReportMap.put("keyword", "tops");
		searchReportMap.put("categoryId", "4294920110");
		searchReportMap.put("limit", "10");
		searchReportMap.put("offset", "100");
		searchReportMap.put("attributes", "color:blue,red|size:small");

		resultList.add(resultMap1);
		resultList.add(dimensionMap);
		resultList.add(categoryMap);
		resultList.add(searchReportMap);

		return resultList;
	}

	/**
	 * @param resultMap
	 *            Map containing String as a key and String as s value
	 */
	private static void createresultMapData(final Map<String, String> resultMap) {
		resultMap.put("P_available_in_Store", "No");
		resultMap.put("P_available_online", "Yes");
		resultMap.put("P_clearance", "No");
		resultMap.put("P_isPattern", "F");
		resultMap.put("P_listPrice", "64.000000");
		resultMap.put("P_onSale", "No");
		resultMap.put("P_product_code", "3203375NA10011");
		resultMap.put("P_brand", "Nautica");
		resultMap.put("P_product_name", "Ocean Washed Dress Shirt");
		resultMap.put("P_saleprice", "310.00");
		resultMap.put("P_list_price_range", "360.00-400");
		resultMap.put("MIN_LIST_PRICE", "360.00");
		resultMap.put("MAX_LIST_PRICE", "400.00");
		// resultMap1.put("P_sale_price_range", "310.00-320.00");
		resultMap.put("MIN_SALE_PRICE", "310.00");
		resultMap.put("MAX_SALE_PRICE", "320.00");
	}

	/**
	 * @return productSearch
	 */
	public static ProductSearch getProductSearch() {
		final ProductSearch productSearch = new ProductSearch();
		productSearch.setProductCode("3203375NA10011");
		productSearch.setProductId("3203375NA10011");
		productSearch.setWebId("3203375NA10011");
		productSearch.setVendorId("888888");
		productSearch.setName("Ocean Washed Dress Shirt");
		productSearch.setProductPrice(getPriceList());

		return productSearch;
	}

	/**
	 * Method to get price list
	 * 
	 * @return price
	 */
	private static List<Price> getPriceList() {

		final List<Price> priceList = new ArrayList<Price>();
		final Price listPrice = new Price();
		listPrice.setKey("listPrice");
		listPrice.setValue("360.00");

		final Price minListPriceRange = new Price();
		minListPriceRange.setKey("minListPriceRange");
		minListPriceRange.setValue("360.00");
		priceList.add(minListPriceRange);

		final Price maxListPriceRange = new Price();
		maxListPriceRange.setKey("maxListPriceRange");
		maxListPriceRange.setValue("400.00");
		priceList.add(maxListPriceRange);

		final Price salePrice = new Price();
		salePrice.setKey("salePrice");
		salePrice.setValue("310.00");
		priceList.add(salePrice);

		priceList.add(listPrice);
		return priceList;
	}

	/**
	 * Method to get dimension list of a product
	 * 
	 * @return dimensions
	 */
	public static Dimension getDimension() {

		final Dimension dimension = new Dimension();

		final List<Attribute> dimensionAttributes = new ArrayList<Attribute>();

		final Attribute dimAttribute = new Attribute();
		dimAttribute.setKey("dimensionKey");
		dimAttribute.setValue("123");
		dimensionAttributes.add(dimAttribute);

		final List<Refinement> refinements = new ArrayList<Refinement>();
		final Refinement refinement = new Refinement();

		final List<Attribute> refinementAttributes = new ArrayList<Attribute>();

		final Attribute refAttribute1 = new Attribute();
		refAttribute1.setKey("refinementId");
		refAttribute1.setValue("123");

		final Attribute refAttribute2 = new Attribute();
		refAttribute2.setKey("label");
		refAttribute2.setValue("Red");

		refinementAttributes.add(refAttribute1);
		refinementAttributes.add(refAttribute2);

		refinement.setRefinementAttributes(refinementAttributes);

		refinements.add(refinement);

		dimension.setDimensionAttributes(dimensionAttributes);
		dimension.setRefinements(refinements);

		return dimension;
	}

	/**
	 * Method to get search report of a product
	 * 
	 * @return searchReport
	 */
	public static SearchReport getSearchReport() {
		final SearchReport searchReport = new SearchReport();
		searchReport.setTotalProducts("140");
		searchReport.setTotalSkus("100");
		searchReport.setKeyword("tops");
		searchReport.setLimit("10");
		searchReport.setOffset("100");
		searchReport.setAttributes(getSearchAttribute());
		return searchReport;
	}

	/**
	 * Method to get search attribute list of product
	 * 
	 * @return attributesList
	 */
	public static List<SearchAttribute> getSearchAttribute() {

		final List<SearchAttribute> attributesList = new ArrayList<SearchAttribute>();
		final SearchAttribute attribute1 = new SearchAttribute();
		final List<String> attValueList1 = new ArrayList<String>();
		attValueList1.add("blue");
		attValueList1.add("red");
		attribute1.setKey("color");
		attribute1.setValues(attValueList1);

		final SearchAttribute attribute2 = new SearchAttribute();
		final List<String> attValueList2 = new ArrayList<String>();

		attValueList2.add("small");
		attribute2.setKey("size");
		attribute2.setValues(attValueList2);

		attributesList.add(attribute1);
		attributesList.add(attribute2);

		return attributesList;
	}

	/**
	 * Method to create search result object
	 * 
	 * @return search
	 */
	public static Search createSearchResults() {
		final Search search = new Search();
		search.setProducts(getProductSearchList());
		search.setCategories(getCategoriesList());
		search.setDimensions(getDimensionList());
		search.setSearchReport(getSearchReport());
		return search;
	}

	/**
	 * Method to get product search list
	 * 
	 * @return products
	 */
	private static List<ProductSearch> getProductSearchList() {
		final List<ProductSearch> products = new ArrayList<ProductSearch>();
		final ProductSearch productSearch = new ProductSearch();
		productSearch.setProductCode("3203375NA10011");
		productSearch.setName("Ocean Washed Dress Shirt");
		productSearch.setProductPrice(getPriceList());
		products.add(productSearch);
		return products;
	}

	/**
	 * Method to get category list
	 * 
	 * @return List of ParentCategories
	 */
	private static List<ParentCategory> getCategoriesList() {
		final List<ParentCategory> categories = new ArrayList<ParentCategory>();

		final ParentCategory parentCategory = new ParentCategory();

		final List<Attribute> parentCategoryAttributes = new ArrayList<Attribute>();
		final Attribute parentAttribute = new Attribute();
		parentAttribute.setKey("categoryID");
		parentAttribute.setValue("9999");
		parentCategoryAttributes.add(parentAttribute);

		final List<SubCategory> subCategories = new ArrayList<SubCategory>();

		final SubCategory subCategory = new SubCategory();

		final List<Attribute> subCategoryAttributes = new ArrayList<Attribute>();
		final Attribute subAttribute1 = new Attribute();
		subAttribute1.setKey("categoryID");
		subAttribute1.setValue("9999");

		final Attribute subAttribute2 = new Attribute();
		subAttribute2.setKey("categoryName");
		subAttribute2.setValue("Junior");

		subCategoryAttributes.add(subAttribute1);
		subCategoryAttributes.add(subAttribute2);

		subCategory.setCategoryAttributes(subCategoryAttributes);
		subCategories.add(subCategory);
		parentCategory.setCategoryAttributes(parentCategoryAttributes);
		parentCategory.setSubCategories(subCategories);
		categories.add(parentCategory);

		return categories;
	}

	/**
	 * Method to get dimension list of a product
	 * 
	 * @return dimensions
	 */
	private static List<Dimension> getDimensionList() {
		final List<Dimension> dimensions = new ArrayList<Dimension>();

		final Dimension dimension = new Dimension();

		final List<Attribute> dimensionAttributes = new ArrayList<Attribute>();

		final Attribute dimAttribute = new Attribute();
		dimAttribute.setKey("dimensionId");
		dimAttribute.setValue("123");
		dimensionAttributes.add(dimAttribute);

		final List<Refinement> refinements = new ArrayList<Refinement>();
		final Refinement refinement = new Refinement();

		final List<Attribute> refinementAttributes = new ArrayList<Attribute>();

		final Attribute refAttribute1 = new Attribute();
		refAttribute1.setKey("refinementId");
		refAttribute1.setValue("123");

		final Attribute refAttribute2 = new Attribute();
		refAttribute2.setKey("label");
		refAttribute2.setValue("Red");

		refinementAttributes.add(refAttribute1);
		refinementAttributes.add(refAttribute2);

		refinement.setRefinementAttributes(refinementAttributes);

		refinements.add(refinement);

		dimension.setDimensionAttributes(dimensionAttributes);
		dimension.setRefinements(refinements);

		dimensions.add(dimension);

		return dimensions;
	}

	/**
	 * @return resultMap
	 */
	public static Map<String, String> createProductSearchResultMap() {

		final Map<String, String> resultMap1 = new HashMap<String, String>();

		resultMap1.put("P_available_in_Store", "No");
		resultMap1.put("P_available_online", "Yes");
		resultMap1.put("P_clearance", "No");
		resultMap1.put("P_isPattern", "F");
		resultMap1.put("P_listPrice", "64.000000");
		resultMap1.put("P_onSale", "No");
		resultMap1.put("P_product_code", "3203375NA10011");
		resultMap1.put("P_brand", "Nautica");
		resultMap1.put("P_product_name", "Ocean Washed Dress Shirt");
		resultMap1.put("P_saleprice", "310.00");
		resultMap1.put("P_list_price_range", "360.00-400");
		resultMap1.put("MIN_LIST_PRICE", "360.00");
		resultMap1.put("MAX_LIST_PRICE", "400.00");
		resultMap1.put("P_sale_price_range", "310.00-320.00");
		resultMap1.put("MIN_SALE_PRICE", "310.00");
		resultMap1.put("MAX_SALE_PRICE", "320.00");
		resultMap1.put("promotions",
				"text:buy one get one free!|text:gift with purchase!");

		return resultMap1;
	}

	/**
	 * @return dimensionMap
	 */
	public static Map<String, String> createDimensionResultMap() {
		final Map<String, String> dimensionMap = new HashMap<String, String>();

		dimensionMap.put("dimensionKey", "123");
		dimensionMap.put("label", "Color");
		dimensionMap.put("multiSelectEnabled", "true");
		dimensionMap.put("multiSelectOperation", "OR");
		dimensionMap
				.put("refinements",
						"refinementId^12~label^Red~count^50|refinementId^14~label^Blue~count^37");

		return dimensionMap;
	}

	/**
	 * @return searchReportMap
	 */
	public static Map<String, String> createSearchReportResultMap() {
		final Map<String, String> searchReportMap = new HashMap<String, String>();
		searchReportMap.put("totalProducts", "140");
		searchReportMap.put("totalSkus", "2000");
		searchReportMap.put("keyword", "tops");
		searchReportMap.put("categoryId", "4294920110");
		searchReportMap.put("limit", "10");
		searchReportMap.put("offset", "100");
		searchReportMap.put("attributes", "color:blue,red|size:small");

		return searchReportMap;
	}

	/**
	 * @return parentCategory
	 */
	public static ParentCategory getParentCategory() {

		final ParentCategory parentCategory = new ParentCategory();

		final List<Attribute> parentCategoryAttributes = new ArrayList<Attribute>();
		final Attribute parentAttribute = new Attribute();
		parentAttribute.setKey("categoryID");
		parentAttribute.setValue("9999");
		parentCategoryAttributes.add(parentAttribute);

		final List<SubCategory> subCategories = new ArrayList<SubCategory>();

		final SubCategory subCategory = new SubCategory();

		final List<Attribute> subCategoryAttributes = new ArrayList<Attribute>();
		final Attribute subAttribute1 = new Attribute();
		subAttribute1.setKey("categoryID");
		subAttribute1.setValue("9999");

		final Attribute subAttribute2 = new Attribute();
		subAttribute2.setKey("categoryName");
		subAttribute2.setValue("Junior");

		subCategoryAttributes.add(subAttribute1);
		subCategoryAttributes.add(subAttribute2);

		subCategory.setCategoryAttributes(subCategoryAttributes);
		subCategories.add(subCategory);
		parentCategory.setCategoryAttributes(parentCategoryAttributes);
		parentCategory.setSubCategories(subCategories);

		return parentCategory;
	}

	/**
	 * @return attributeList
	 */
	public static List<SearchAttribute> getSearchAttributeList() {
		final String attributeListValue = "color:blue,red|size:small";
		final List<SearchAttribute> attributeList = new ArrayList<SearchAttribute>();
		SearchAttribute attribute = null;
		final StringTokenizer attributeListString = new StringTokenizer(
				attributeListValue, CommonConstants.PIPE_SEPERATOR);
		while (attributeListString.hasMoreTokens()) {
			attribute = new SearchAttribute();
			final StringTokenizer attributeString = new StringTokenizer(
					attributeListString.nextToken(),
					CommonConstants.FIELD_PAIR_SEPARATOR);

			while (attributeString.hasMoreTokens()) {
				attribute.setKey(attributeString.nextToken());
				attribute.setValues(Arrays.asList(attributeString.nextToken()
						.split(CommonConstants.COMMA_SEPERATOR)));
			}
			attributeList.add(attribute);

		}
		return attributeList;

	}

	/**
	 * @return string
	 */
	public static String getAttributeListValue() {
		return "color:blue,red|size:small";
	}

	/**
	 * @return dimensionMap
	 */
	public static Map<String, String> createCateogryResultMap() {
		final Map<String, String> dimensionMap = new HashMap<String, String>();

		dimensionMap.put("dimensionKey", "123");
		dimensionMap.put("label", "category");
		dimensionMap.put("multiSelectEnabled", "false");
		dimensionMap
				.put("refinements",
						"refinementKey^12~label^Red~recordCount^50|refinementKey^14~label^Blue~recordCount^37");

		return dimensionMap;
	}

	/**
	 * 
	 * @return String for tokenizing
	 */
	public static String createDataForSubCategory() {
		return "refinementKey^12~label^Red~recordCount^50|refinementKey^14~label^Blue~recordCount^37";
	}

	/**
	 * 
	 * @return List of SubCategory
	 */
	public static List<SubCategory> getSubCategoryList() {
		final List<SubCategory> subList = new ArrayList<SubCategory>();

		final SubCategory subCategory1 = new SubCategory();

		final Attribute attribute1 = new Attribute();
		attribute1.setKey("refinementId");
		attribute1.setValue("12");

		final List<Attribute> categoryAttributes1 = populateSubCategoryAttribute(attribute1);

		final SubCategory subCategory2 = new SubCategory();
		final Attribute attribute21 = new Attribute();
		attribute21.setKey("refinementId");
		attribute21.setValue("14");

		final Attribute attribute22 = new Attribute();
		attribute22.setKey("label");
		attribute22.setValue("Blue");
		final Attribute attribute23 = new Attribute();
		attribute23.setKey("count");
		attribute23.setValue("37");
		final List<Attribute> categoryAttributes2 = new ArrayList<Attribute>();
		categoryAttributes2.add(attribute21);
		categoryAttributes2.add(attribute22);
		categoryAttributes2.add(attribute23);

		subCategory1.setCategoryAttributes(categoryAttributes1);
		subCategory2.setCategoryAttributes(categoryAttributes2);
		subList.add(subCategory1);
		subList.add(subCategory2);

		return subList;

	}

	/**
	 * @param attribute1
	 *            is the attribute with which the list gets populated
	 * @return Attribute List
	 */
	private static List<Attribute> populateSubCategoryAttribute(
			final Attribute attribute1) {
		final Attribute attribute2 = new Attribute();
		attribute2.setKey("label");
		attribute2.setValue("Red");
		final Attribute attribute3 = new Attribute();
		attribute1.setKey("count");
		attribute1.setValue("50");
		final List<Attribute> categoryAttributes1 = new ArrayList<Attribute>();
		categoryAttributes1.add(attribute1);
		categoryAttributes1.add(attribute2);
		categoryAttributes1.add(attribute3);
		return categoryAttributes1;
	}

	/**
	 * 
	 * @return Refinement List
	 */
	public static List<Refinement> getRefinementList() {
		final List<Refinement> refinementList = new ArrayList<Refinement>();

		final Refinement refinement1 = new Refinement();

		final Attribute attribute1 = new Attribute();
		attribute1.setKey("refinementId");
		attribute1.setValue("12");

		final List<Attribute> categoryAttributes1 = populateSubCategoryAttribute(attribute1);

		final Refinement refinement2 = new Refinement();
		final Attribute attribute21 = new Attribute();
		attribute21.setKey("refinementId");
		attribute21.setValue("14");

		final Attribute attribute22 = new Attribute();
		attribute22.setKey("label");
		attribute22.setValue("Blue");
		final Attribute attribute23 = new Attribute();
		attribute23.setKey("count");
		attribute23.setValue("37");
		final List<Attribute> categoryAttributes2 = new ArrayList<Attribute>();
		categoryAttributes2.add(attribute21);
		categoryAttributes2.add(attribute22);
		categoryAttributes2.add(attribute23);

		refinement1.setRefinementAttributes(categoryAttributes1);
		refinement2.setRefinementAttributes(categoryAttributes2);
		refinementList.add(refinement1);
		refinementList.add(refinement2);

		return refinementList;

	}

}
