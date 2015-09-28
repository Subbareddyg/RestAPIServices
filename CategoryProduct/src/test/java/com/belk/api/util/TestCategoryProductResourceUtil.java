package com.belk.api.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import com.belk.api.constants.CommonConstants;
import com.belk.api.model.categoryproduct.Attribute;
import com.belk.api.model.categoryproduct.Categories;
import com.belk.api.model.categoryproduct.Category;
import com.belk.api.model.categoryproduct.ProductSearch;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * This class is a helper class for unit testing CategoryProductResource
 * 
 * This Util class provides the data for other Test classes in
 * CategoryProductResource layer
 * 
 * @author Mindtree
 * @date 25 Oct 2013
 */
public class TestCategoryProductResourceUtil {

	/**
	 * Test method to create RequestMap which returns requestMap with hardcoded
	 * CategoryId
	 * 
	 * @return Map<String, String> requestMap
	 */

	public static Map<String, String> createRequestMap() {

		final Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("catId", "4294922263");
		return requestMap;

	}

	/**
	 * Test method to create URIMap which returns uriMap with hardcoded
	 * CategoryId
	 * 
	 * @return Map<String, String>
	 */

	public static Map<String, List<String>> createURIMap() {
		final Map<String, List<String>> uriMap = new HashMap<String, List<String>>();

		// Setting the Path Parameters for JUnit Test
		final List<String> list1 = new ArrayList<String>();
		list1.add("4294922263");

		uriMap.put("catId", list1);

		final List<String> list2 = new ArrayList<String>();
		list2.add("12345");

		uriMap.put("catalogId", list2);

		final List<String> list3 = new ArrayList<String>();
		list3.add("xml");

		uriMap.put("type", list3);

		final List<String> list4 = new ArrayList<String>();
		list4.add("100000");

		uriMap.put("limit", list4);

		return uriMap;

	}

	/**
	 * Test method to create SearchResults which returns categories object with
	 * hardcoded results from endeca
	 * 
	 * @return Categories
	 */

	public static Categories createCategoryProductResults() {
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

		productSearch.setProductCode("1800303AZJL606");
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
	 * Method to create multivaluedmap
	 * 
	 * @return uriParametersMap
	 */
	public static MultivaluedMap<String, String> createURIParametersMap() {

		final MultivaluedMap<String, String> uriParametersMap = new MultivaluedMapImpl();

		uriParametersMap.add("catId", "4294922263");
		uriParametersMap.add("format", "xml");
		uriParametersMap.add("limit", "100000");
		uriParametersMap.add("catalogid", "8888");

		return uriParametersMap;

	}

	/**
	 * Method to create URI map of data,which resembles the URInfo object
	 * created at run time from URI Information.
	 * 
	 * @return uriMap
	 */

	public static Map<String, List<String>> createURIInvalidDataMap() {

		final Map<String, List<String>> uriMap = new HashMap<String, List<String>>();

		final List<String> list1 = new ArrayList<String>();
		list1.add("4988sht");

		uriMap.put("catid", list1);

		return uriMap;

	}

	/**
	 * Method to create request http headers map
	 * 
	 * @return reqHeaders
	 */
	public static MultivaluedMap<String, String> createRequestHeadersMap() {
		final MultivaluedMap<String, String> reqHeaders = new MultivaluedMapImpl();
		reqHeaders.add(CommonConstants.CORRELATION_ID,
				"1234567890SDGFASF4545DFSERE");
		return reqHeaders;
	}

	/**
	 * Test method to create URIMap which returns uriMap with hardcoded
	 * CategoryId
	 * 
	 * @return Map<String, String>
	 */

	public static Map<String, List<String>> createURIMapWithCatalogId() {

		final Map<String, List<String>> uriMap = new HashMap<String, List<String>>();

		// Setting the Path Parameters for JUnit Test
		final List<String> list1 = new ArrayList<String>();
		list1.add("4294922263");

		uriMap.put("catId", list1);

		final List<String> list2 = new ArrayList<String>();
		list2.add("12345");

		uriMap.put("catalogId", list2);

		final List<String> list3 = new ArrayList<String>();
		list3.add("xml");

		uriMap.put("type", list3);

		final List<String> list4 = new ArrayList<String>();
		list4.add("100000");

		uriMap.put("limit", list4);

		return uriMap;

	}

	/**
	 * Test method to create URIMap which returns uriMap with hardcoded
	 * CategoryId
	 * 
	 * @return Map<String, String>
	 */

	public static Map<String, List<String>> createURIMapWithCatalogIdForException() {

		final Map<String, List<String>> uriMap = new HashMap<String, List<String>>();

		// Setting the Path Parameters for JUnit Test
		final List<String> list1 = new ArrayList<String>();
		list1.add("4294922263");

		uriMap.put("catId", list1);

		final List<String> list2 = new ArrayList<String>();
		list2.add("12345");

		uriMap.put("catalogid", list2);

		final List<String> list3 = new ArrayList<String>();
		list3.add("xml");

		uriMap.put("type", list3);

		final List<String> list4 = new ArrayList<String>();
		list4.add("100000");

		uriMap.put("limit", list4);

		return uriMap;

	}

}
