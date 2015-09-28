package com.belk.api.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import com.belk.api.constants.CommonConstants;
import com.belk.api.model.categorydetails.Attribute;
import com.belk.api.model.categorydetails.Categories;
import com.belk.api.model.categorydetails.Category;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * This class is a helper class for unit testing CategoryDetailsResource
 * 
 * This Util class provides the data for other Test classes in
 * CategoryDetailsResource layer
 * 
 * @author Mindtree
 * @date Oct 25 2013
 */
public class TestCategoryDetailsResourceUtil {

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

		return uriMap;

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

		return uriMap;

	}

	/**
	 * Test method to create SearchResults which returns categories object with
	 * hardcoded results from endeca
	 * 
	 * @return Categories
	 */

	public static Categories createCategoryDetailsResults() {
		final Category category = new Category();
		final Categories categories = new Categories();
		category.setCategoryId("4294922263");
		category.setName("Home");
		category.setParentCategoryId("4294967294");

		final List<Category> categoryList = new ArrayList<Category>();
		final List<Attribute> attributeList = new ArrayList<Attribute>();

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

		category.setCategoryAttributes(attributeList);
		categoryList.add(category);
		categories.setCategories(categoryList);

		return categories;

	}

	/**
	 * Test method to create EndecaResultList which returns EndecaResultList
	 * with hardcoded results from endeca
	 * 
	 * 
	 * @return List<Map<String, String>> resultList
	 */

	public static List<Map<String, String>> endecaResultList() {

		final Map<String, String> map1 = new HashMap<String, String>();
		map1.put("productCode", "1800303AZJL606");
		map1.put("totalProducts", "38398");
		map1.put("totalSkus", "152643");
		map1.put("productId", "");
		map1.put("webId", "");

		final Map<String, String> map2 = new HashMap<String, String>();
		map2.put("productCode", "1800303AZJL606");
		map2.put("totalProducts", "38398");
		map2.put("totalSkus", "152643");
		map2.put("productId", "");
		map2.put("webId", "");

		final Map<String, String> map3 = new HashMap<String, String>();
		map3.put("productCode", "1800303AZJL606");
		map3.put("totalProducts", "38398");
		map3.put("totalSkus", "152643");
		map3.put("productId", "");
		map3.put("webId", "");

		final Map<String, String> map4 = new HashMap<String, String>();
		map4.put("productCode", "1800303AZJL606");
		map4.put("totalProducts", "38398");
		map4.put("totalSkus", "152643");
		map4.put("productId", "");
		map4.put("webId", "");

		final List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		resultList.add(map1);
		resultList.add(map2);
		resultList.add(map3);
		resultList.add(map4);

		return resultList;

	}

	/**
	 * Method to create multivaluedmap
	 * 
	 * @return uriParametersMap
	 */
	public static MultivaluedMap<String, String> createURIParametersMap() {

		final MultivaluedMap<String, String> uriParametersMap = new MultivaluedMapImpl();
		uriParametersMap.add("catalogid", "4294922263");
		uriParametersMap.add("Correlation-Id", "12344");
		uriParametersMap.add("format", "xml");
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
		final List<String> list = new ArrayList<String>();
		list.add("format");
		uriMap.put("format", list);

		final List<String> list2 = new ArrayList<String>();
		list2.add("12345");
		uriMap.put("catalogId", list2);

		return uriMap;

	}

	/**
	 * Method to create dimension list of a product
	 * 
	 * @return productDetailsMap
	 */
	public static List<Map<String, String>> createDimensionList() {

		final Map<String, String> productDetailsMap = new HashMap<String, String>();

		productDetailsMap.put("P_available_in_Store", "No");
		productDetailsMap.put("P_available_online", "Yes");
		productDetailsMap.put("P_brand", "Gold Toe&reg");
		productDetailsMap.put("P_bridal_eligible", "F");
		productDetailsMap.put("P_dept_number", "364");
		productDetailsMap.put("p_INVENTORY_AVAIL", "163");
		productDetailsMap.put("P_inventory_level", "18");
		productDetailsMap.put("P_isPattern", "F");
		productDetailsMap.put("P_listPrice", "9.000000");
		productDetailsMap.put("P_new_arraival", "No");
		productDetailsMap.put("P_product_code", "32000482988S");

		final Map<String, String> productsAndSkuMap = new HashMap<String, String>();

		productsAndSkuMap.put("totalProducts", "4767");
		productsAndSkuMap.put("totalSkus", "30942");

		final Map<String, String> dimensionDetails = new HashMap<String, String>();

		dimensionDetails.put("dimentionKey", "4294920208");
		dimensionDetails.put("label", "Men");
		dimensionDetails.put("parentDimentionKey", "4294922263");
		dimensionDetails.put("hasFurtherRefinements", "Yes");

		final List<Map<String, String>> dimensionList = new ArrayList<Map<String, String>>();

		dimensionList.add(dimensionDetails);
		dimensionList.add(productsAndSkuMap);
		dimensionList.add(productDetailsMap);

		return dimensionList;
	}

	/**
	 * Method to create request http headers map
	 * 
	 * @return reqHeaders
	 */
	public static MultivaluedMap<String, String> createRequestHeadersMap() {
		final MultivaluedMap<String, String> reqHeaders = new MultivaluedMapImpl();
		reqHeaders.add(CommonConstants.CORRELATION_ID, "1234567890");
		return reqHeaders;
	}

	/**
	 * Method returns the multivaluedMap
	 * 
	 * @return MultivaluedMap<String, String>
	 */
	public static MultivaluedMap<String, String> createURIParametersMapWithCategory() {
		final MultivaluedMap<String, String> uriParametersMap = new MultivaluedMapImpl();

		uriParametersMap.add("catalogid", "4294922263");

		return uriParametersMap;
	}

	public static Map<String, String> getErrorPropertiesMap() {
		 Map<String, String> errorPropertiesMap = new HashMap<String,String>();
		 errorPropertiesMap.put("11421","An invalid parameter name has been submitted");
			errorPropertiesMap.put("11422","An invalid parameter value has been submitted");
			errorPropertiesMap.put("11423","The maximum number of values for this parameter has been exceeded");
			errorPropertiesMap.put("11424","An invalid combination of parameters has been supplied");
			errorPropertiesMap.put("11425","A required value was not supplied");
			errorPropertiesMap.put("11426","The requested method is not available");
			errorPropertiesMap.put("11427","The API you requested does not exist");
			errorPropertiesMap.put("11428","The supplied API key is invalid");
			errorPropertiesMap.put("11429","The requested method requires authentication");
			errorPropertiesMap.put("11430","The requested service has been removed; please upgrade to the current version");
			errorPropertiesMap.put("11431","An upgrade to the service terms is required");
			errorPropertiesMap.put("11432","The requested method is not allowed");
			errorPropertiesMap.put("11433","The supplied API key has expired");
			errorPropertiesMap.put("11434","The account associated with the supplied API key has been suspended");
			errorPropertiesMap.put("11435","The account associated with the supplied API key does not have access to this method");
			errorPropertiesMap.put("11521","The request to a required resource has timed-out");
			errorPropertiesMap.put("11522","An unknown error has occurred");
			errorPropertiesMap.put("11523","There has been an internal service error");
			errorPropertiesMap.put("11436","No record could be found based on the specified criteria");
			errorPropertiesMap.put("1235","11421");
			errorPropertiesMap.put("8","11422");

		return errorPropertiesMap;
	}

}
