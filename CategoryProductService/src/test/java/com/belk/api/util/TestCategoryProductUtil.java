package com.belk.api.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.belk.api.model.categoryproduct.Attribute;
import com.belk.api.model.categoryproduct.Categories;
import com.belk.api.model.categoryproduct.Category;
import com.belk.api.model.categoryproduct.ProductSearch;

/**
 * Unit Testing related to CategoryProductUtil class is performed. <br />
 * {@link TestCategoryProductUtil} class is written for testing methods in
 * {@link CategoryProductUtil} The unit test cases evaluates the way the methods
 * behave for the inputs given. This class contains common method used for Unit
 * Testing of CategoryProductMapper class
 * 
 * @author Mindtree
 * @date Oct 25, 2013
 * 
 */

public class TestCategoryProductUtil {

	/**
	 * Method to create request map
	 * 
	 * @return requestMap
	 */

	public final Map<String, String> createRequestMap() {

		final Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("catId", "4294922263");
		requestMap.put("format", "JSON");
		return requestMap;

	}

	/**
	 * Method to create result map
	 * 
	 * @return requestMap
	 */
	public final Map<String, String> createResultMap() {

		final Map<String, String> resultMap = new HashMap<String, String>();

		resultMap.put("productcode", "3203375NA10011");
		resultMap.put("P_available_in_Store", "No");
		resultMap.put("P_available_online", "Yes");
		resultMap.put("P_clearance", "No");
		resultMap.put("P_isPattern", "F");
		resultMap.put("P_listPrice", "64.000000");
		resultMap.put("P_onSale", "No");
		resultMap.put("P_brand", "Derek Heart");
		resultMap.put("P_web_id", " ");
		resultMap.put("P_product_id", " ");
		resultMap.put("P_dept_number", "717");
		resultMap.put("P_color", "Pink");
		resultMap.put("P_dept_number", "717");
		resultMap.put("P_color", "Pink");
		resultMap.put("totalProducts", "152643");
		resultMap.put("totalSkus", "152643");
		resultMap.put("P_path", "/Assortments/Main/Belk_Primary/Shop_All");
		resultMap.put("P_class_Number", "7172");
		resultMap.put("P_bridal_eligible", "F");
		resultMap.put("P_objectId", "1689949383353220");
		resultMap.put("dimentionKey", "4294922263");
		resultMap.put("label", "Home");
		resultMap.put("parentDimentionKey", "4294967294");
		resultMap.put("hasFurtherRefinements", "Yes");

		return resultMap;

	}

	/**
	 * Method to create uri map
	 * 
	 * @return uriMap
	 * 
	 */

	public final Map<String, List<String>> createURIMap() {

		final Map<String, List<String>> uriMap = new HashMap<String, List<String>>();
		final List<String> list1 = new ArrayList<String>();
		list1.add("4294922263");

		final List<String> list2 = new ArrayList<String>();
		list2.add("JSON");

		uriMap.put("catId", list1);
		uriMap.put("format", list2);

		return uriMap;

	}

	/**
	 * Method to create invalid URI map of data inorder to test the exception
	 * 
	 * @return uriMap
	 * 
	 */
	public final Map<String, List<String>> createURIInvalidDataMap() {
		final Map<String, List<String>> uriMap = new HashMap<String, List<String>>();
		final List<String> list1 = new ArrayList<String>();
		list1.add("42949xyz");

		final List<String> list2 = new ArrayList<String>();
		list2.add("JSON");

		uriMap.put("catId", list1);
		uriMap.put("format", list2);

		return uriMap;

	}

	/**
	 * Method to populate category attribute details using the results from
	 * Endeca
	 * 
	 * @param resultMap
	 *            resultMap
	 * @return List<Attribute>
	 * 
	 */

	public final List<Attribute> populateCategoryAttributeDetails(
			final Map<String, String> resultMap) {

		final List<Attribute> categoryAttributesList = new ArrayList<Attribute>();
		final Attribute attribute1 = new Attribute();
		attribute1.setKey("totalProducts");
		attribute1.setValue("38398");

		final Attribute attribute2 = new Attribute();
		attribute1.setKey("totalSkus");
		attribute1.setValue("152643");

		final Attribute attribute3 = new Attribute();
		attribute1.setKey("hasFurtherRefinements");
		attribute1.setValue("Yes");

		categoryAttributesList.add(attribute1);
		categoryAttributesList.add(attribute2);
		categoryAttributesList.add(attribute3);

		return categoryAttributesList;

	}

	/**
	 * Method to populate product main details using the results from Endeca
	 * 
	 * @param resultMap
	 *            resultMap
	 * @param productSearch
	 *            productSearch
	 * 
	 * 
	 */

	public final void populateProductMainDetails(
			final ProductSearch productSearch,
			final Map<String, String> resultMap) {

		final Map<String, String> endecaResultMap = new HashMap<String, String>();

		endecaResultMap.put("productcode", "3203375NA10011");
		endecaResultMap.put("P_web_id", " ");
		endecaResultMap.put("P_product_id", " ");

	}

	/**
	 * Method to populate product search details using the results from Endeca
	 * 
	 * @param resultMap
	 *            resultMap
	 * 
	 * @return ProductSearch
	 * 
	 */

	public final ProductSearch populateProductSearch(
			final Map<String, String> resultMap) {
		final Map<String, String> resultMap2 = this.createResultMap();
		ProductSearch productSearch = null;
		productSearch = new ProductSearch();
		productSearch.setProductCode("3203375NA10011");
		this.populateProductMainDetails(productSearch, resultMap2);
		return productSearch;
	}

	/**
	 * Method to create Endeca Result list using the results from Endeca
	 * 
	 * @return finalResultList
	 * 
	 */

	public final List<List<Map<String, String>>> createEndecaResultMapList() {
		final List<List<Map<String, String>>> finalResultList = new ArrayList<List<Map<String, String>>>();
		final List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		final Map<String, String> resultMap1 = new HashMap<String, String>();

		resultMap1.put("productCode", "3203375NA10011");
		resultMap1.put("P_available_in_Store", "No");
		resultMap1.put("P_available_online", "Yes");
		resultMap1.put("P_clearance", "No");
		resultMap1.put("P_isPattern", "F");
		this.getValuesForFinalResultList(resultMap1);
		resultMap1.put("P_bridal_eligible", "F");
		resultMap1.put("P_objectId", "1689949383353220");

		final Map<String, String> resultMap2 = new HashMap<String, String>();
		resultMap2.put("totalProducts", "38398");
		resultMap2.put("totalSkus", "152643");
		resultMap2.put("hasFurtherRefinements", "Yes");

		final Map<String, String> resultMap3 = new HashMap<String, String>();
		resultMap3.put("dimentionKey", "4294922263");
		resultMap3.put("label", "Home");
		resultMap3.put("parentDimentionKey", "4294967294");
		resultMap3.put("hasFurtherRefinements", "Yes");

		resultList.add(resultMap1);
		resultList.add(resultMap2);
		resultList.add(resultMap3);

		finalResultList.add(resultList);

		return finalResultList;

	}

	/**
	 * Method which returns the values for finalResultList
	 * 
	 * @param resultMap1
	 *            resultMap1
	 */
	private void getValuesForFinalResultList(
			final Map<String, String> resultMap1) {
		resultMap1.put("P_listPrice", "64.000000");
		resultMap1.put("P_onSale", "No");
		resultMap1.put("P_brand", "Derek Heart");
		resultMap1.put("P_web_id", " ");
		resultMap1.put("P_product_id", " ");
		resultMap1.put("P_dept_number", "717");
		resultMap1.put("P_color", "Pink");
		resultMap1.put("P_dept_number", "717");
		resultMap1.put("P_color", "Pink");
		resultMap1.put("P_path", "/Assortments/Main/Belk_Primary/Shop_All");
		resultMap1.put("P_class_Number", "7172");
	}

	/**
	 * Method to create expected category pojo using the results from Endeca
	 * 
	 * @param resultList
	 *            resultList
	 * @return Categories
	 * 
	 */

	public final Categories expectedCategoryPOJO(
			final List<List<Map<String, String>>> resultList) {

		final Categories categories = new Categories();
		ProductSearch productSearch = null;
		final List<Category> categoryList = new ArrayList<Category>();

		final Map<String, String> resultMap = this.createResultMap();
		productSearch = this.populateProductSearch(resultMap);

		final List<ProductSearch> productSearchPojoList = new ArrayList<ProductSearch>();
		productSearchPojoList.add(productSearch);

		final Category category = new Category();
		category.setCategoryId("4294922263");
		category.setName("Home");
		category.setParentCategoryId("4294967294");
		category.setProducts(productSearchPojoList);

		List<Attribute> categoryAttributesList = new ArrayList<Attribute>();
		categoryAttributesList = this
				.populateCategoryAttributeDetails(resultMap);
		category.setCategoryAttributes(categoryAttributesList);
		categoryList.add(category);
		categories.setCategory(categoryList);

		return categories;

	}

	/**
	 * Method to set the vaules for category id, name, parent category id and
	 * return category object
	 * 
	 * @return Categories
	 * 
	 */

	public final Category setCategoryDetails() {

		ProductSearch productSearch = null;

		final Map<String, String> resultMap = this.createResultMap();
		productSearch = this.populateProductSearch(resultMap);

		final List<ProductSearch> productSearchPojoList = new ArrayList<ProductSearch>();
		productSearchPojoList.add(productSearch);

		final Category category = new Category();
		category.setCategoryId("4294922263");
		category.setName("Home");
		category.setParentCategoryId("4294967294");
		category.setProducts(productSearchPojoList);

		List<Attribute> categoryAttributesList = this
				.populateCategoryAttributeDetails(resultMap);
		categoryAttributesList = this
				.populateCategoryAttributeDetails(resultMap);
		category.setCategoryAttributes(categoryAttributesList);

		return category;

	}

}
