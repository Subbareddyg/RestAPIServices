package com.belk.api.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import com.belk.api.model.categoryproduct.Attribute;
import com.belk.api.model.categoryproduct.Categories;
import com.belk.api.model.categoryproduct.Category;
import com.belk.api.model.categoryproduct.ProductSearch;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * This class is a helper class for unit testing CategoryProductServiceImpl
 * 
 * This Util class provides the data for other Test classes in
 * CategoryProductServiceImpl layer
 * 
 * @author Mindtree
 * @date 25 Oct 2013
 * 
 */
public class TestCategoryProductServiceUtil {

	/**
	 * Test method to createRequestMap which returns requestMap with hardcoded
	 * CategoryId
	 * 
	 * @return Map<String, String> requestMap
	 */

	public static Map<String, String> createRequestMap() {

		final Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("categoryid",
				"4294922263,4294922261,4294922260,4294922262");
		return requestMap;

	}

	/**
	 * Test method to createRequestMap with multiple category ids which returns
	 * requestMap with hardcoded CategoryId
	 * 
	 * @return Map<String, String> requestMap
	 */

	public static Map<String, List<String>> createRequestMapWithMultipleCatId() {

		final Map<String, List<String>> requestMap = new HashMap<String, List<String>>();
		final List<String> multipleCatIds = new ArrayList<String>();
		multipleCatIds.add("4294922263");
		multipleCatIds.add("4294922262");
		multipleCatIds.add("4294922287");
		multipleCatIds.add("4294922260");

		requestMap.put("categoryid", multipleCatIds);
		return requestMap;

	}

	/**
	 * Test method to createURIMap which returns uriMap with hardcoded
	 * CategoryId for testing
	 * 
	 * @return Map<String, String>
	 */
	public static Map<String, List<String>> createCategoryProductsURIMap() {

		final Map<String, List<String>> uriMap = new HashMap<String, List<String>>();

		// Creating the Request Map for CategoryProductService Impl
		// getCategoryProducts
		// Consisting of category ids
		final List<String> list1 = new ArrayList<String>();
		list1.add("4294922263,4294922260");

		uriMap.put("categoryIds", list1);
		uriMap.put("format", list1);

		return uriMap;

	}

	/**
	 * Test method to createSearchResults which returns categories object with
	 * hardcoded results from endeca
	 * 
	 * @return Categories
	 * 
	 */

	public static Categories createSearchResults() {
		final Category category = new Category();
		final Categories categories = new Categories();
		category.setCategoryId("4294922263");
		category.setName("Home");
		category.setParentCategoryId("4294967294");

		final List<ProductSearch> products = new ArrayList<ProductSearch>();
		final List<Category> categoryList = new ArrayList<Category>();
		final List<Attribute> attributeList = new ArrayList<Attribute>();
		final ProductSearch productSearch = new ProductSearch();

		final Attribute attribute1 = new Attribute();
		attribute1.setKey("totalProducts");
		attribute1.setValue("38398");

		final Attribute attribute2 = new Attribute();
		attribute2.setKey("totalSkus");
		attribute2.setValue("152643");

		final Attribute attribute3 = new Attribute();
		attribute3.setKey("hasFurtherRefinements");
		attribute3.setValue("Yes");

		attributeList.add(attribute1);
		attributeList.add(attribute2);
		attributeList.add(attribute3);

		productSearch.setProductCode("3203375NA10011");
		productSearch.setProductId("");
		productSearch.setWebId("");

		products.add(productSearch);
		category.setProducts(products);
		category.setCategoryAttributes(attributeList);
		categoryList.add(category);
		categories.setCategory(categoryList);

		return categories;

	}

	/**
	 * Test method to create EndecaResultList which returns EndecaResultList
	 * with hardcoded results from endeca
	 * 
	 * @return List<Map<String, String>> ResultList
	 */

	public static List<Map<String, String>> endecaResultList() {

		final List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		final Map<String, String> resultMap1 = new HashMap<String, String>();

		resultMap1.put("P_product_code", "3203375NA10011");
		resultMap1.put("P_available_in_Store", "No");
		resultMap1.put("P_available_online", "Yes");
		resultMap1.put("P_clearance", "No");
		resultMap1.put("P_isPattern", "F");
		getValuesForEndecaResultList(resultMap1);
		resultMap1.put("P_color", "Pink");
		resultMap1.put("P_dept_number", "717");
		resultMap1.put("P_color", "Pink");
		resultMap1.put("P_path", "/Assortments/Main/Belk_Primary/Shop_All");
		resultMap1.put("P_class_Number", "7172");
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

		return resultList;

	}

	/**
	 * Method to get values for endecaResultList
	 * 
	 * @param resultMap1
	 *            resultMap1
	 */
	private static void getValuesForEndecaResultList(
			final Map<String, String> resultMap1) {
		resultMap1.put("P_listPrice", "64.000000");
		resultMap1.put("P_onSale", "No");
		resultMap1.put("P_brand", "Derek Heart");
		resultMap1.put("P_web_id", " ");
		resultMap1.put("P_product_id", " ");
		resultMap1.put("P_dept_number", "717");
	}

	/**
	 * Method to create multivaluedmap
	 * 
	 * @return uriParametersMap
	 */

	public static MultivaluedMap<String, String> createURIParametersMap() {
		final MultivaluedMap<String, String> uriParametersMap = new MultivaluedMapImpl();

		uriParametersMap.add("categoryid", "4294922263");
		return uriParametersMap;

	}

	/**
	 * Method which creates URI map of data,which resembles the URInfo object
	 * created at run time from URI Information.
	 * 
	 * @return uriMap
	 */

	public static Map<String, List<String>> createURIInvalidDataMap() {

		final Map<String, List<String>> uriMap = new HashMap<String, List<String>>();

		final List<String> list1 = new ArrayList<String>();
		list1.add("4988sht");

		uriMap.put("categoryid", list1);

		return uriMap;

	}

	/**
	 * Method to create Endeca Result list using the results from Endeca
	 * 
	 * @return List<Map<String, String>>
	 * 
	 */

	public static List<List<Map<String, String>>> createEndecaResultMapList() {
		final List<List<Map<String, String>>> finalResultList = new ArrayList<List<Map<String, String>>>();
		final List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		final Map<String, String> resultMap1 = new HashMap<String, String>();

		resultMap1.put("P_product_code", "3203375NA10011");
		resultMap1.put("P_available_in_Store", "No");
		resultMap1.put("P_available_online", "Yes");
		resultMap1.put("P_clearance", "No");
		resultMap1.put("P_isPattern", "F");
		getValuesForEndecaResultList(resultMap1);
		resultMap1.put("P_color", "Pink");
		resultMap1.put("P_dept_number", "717");
		resultMap1.put("P_color", "Pink");
		resultMap1.put("P_path", "/Assortments/Main/Belk_Primary/Shop_All");
		resultMap1.put("P_class_Number", "7172");
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
	 * Test method to createRequestMap which returns requestMap with hardcoded
	 * CategoryId
	 * 
	 * @return Map<String, String> requestMap
	 */

	public static Map<String, String> createRequestMapWithoutCatID() {

		final Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("categoryid", "4294922263");
		return requestMap;

	}

}