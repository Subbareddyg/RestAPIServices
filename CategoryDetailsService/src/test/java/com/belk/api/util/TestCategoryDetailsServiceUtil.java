package com.belk.api.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import com.belk.api.model.categorydetails.Attribute;
import com.belk.api.model.categorydetails.Categories;
import com.belk.api.model.categorydetails.Category;
import com.belk.api.model.categorydetails.SubCategory;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * This class is a helper class for unit testing CatalogServiceImpl
 * 
 * This Util class provides the data for other Test classes in
 * CatalogServiceImpl layer
 * 
 * @author Mindtree
 * @date Oct 25 2013
 */
public class TestCategoryDetailsServiceUtil {

	/**
	 * Test method to createRequestMap which returns requestMap with hardcoded
	 * CategoryId
	 * 
	 * @return Map<String, String> requestMap
	 */

	public static Map<String, String> createRequestMap() {

		final Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("categoryid", "4294922263");
		return requestMap;

	}

	/**
	 * Test method to createURIMap which returns uriMap with hardcoded
	 * CategoryId for testing
	 * 
	 * @return Map<String, String>
	 */
	public static Map<String, List<String>> createCategoryDetailsURIMap() {

		final Map<String, List<String>> uriMap = new HashMap<String, List<String>>();
		final List<String> list1 = new ArrayList<String>();
		list1.add("4294922263");
		uriMap.put("categoryid", list1);
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
		final List<Category> categoryList = new ArrayList<Category>();
		category.setCategoryId("4294922263");
		category.setName("Home");
		category.setParentCategoryId("4294967294");

		final List<SubCategory> subCategoryList = new ArrayList<SubCategory>();
		final List<Attribute> attributeList = new ArrayList<Attribute>();

		final Attribute attribute1 = new Attribute();
		attribute1.setKey("hasSubCategories");
		attribute1.setValue("Yes");

		attributeList.add(attribute1);

		final SubCategory subCategory = new SubCategory();
		subCategory.setCategoryId("4294922262");
		subCategory.setName("Home1");
		subCategory.setParentCategoryId("4294922263");

		final List<Attribute> subAttributeList = new ArrayList<Attribute>();

		final Attribute subAttribute = new Attribute();
		subAttribute.setKey("hasSubCategories");
		subAttribute.setValue("No");

		subAttributeList.add(subAttribute);

		subCategory.setCategoryAttributes(subAttributeList);

		subCategoryList.add(subCategory);

		category.setCategoryAttributes(attributeList);
		category.setSubCategories(subCategoryList);

		categoryList.add(category);

		categories.setCategories(categoryList);

		return categories;

	}

	/**
	 * Method to create endecaResultList which returns EndecaResultList with
	 * hardcoded results from endeca
	 * 
	 * @return List<Map<String, String>> ResultList
	 */

	public static List<List<Map<String, String>>> endecaResultList() {

		final Map<String, String> map1 = new HashMap<String, String>();
		map1.put("dimentionKey", "4294922263");
		map1.put("name", "Shirts");
		map1.put("parentDimentionKey", "4294967294");
		map1.put("hasFurtherRefinements", "Yes");
		map1.put(
				"refinements",
				"dimentionKey^4294920036~name^Shirts~parentDimentionKey^4294920208~hasFurtherRefinements^No");

		final List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		resultList.add(map1);

		final List<List<Map<String, String>>> endecaResultList = new ArrayList<List<Map<String, String>>>();

		endecaResultList.add(resultList);
		return endecaResultList;

	}

	/**
	 * Test method to create EndecaResultList which returns EndecaResultList
	 * with hardcoded results from endeca
	 * 
	 * @return List<Map<String, String>> ResultList
	 */

	public static List<Map<String, String>> resultList() {

		final Map<String, String> map1 = new HashMap<String, String>();
		map1.put("dimentionKey", "4294922263");
		map1.put("name", "Shirts");
		map1.put("parentDimentionKey", "4294967294");
		map1.put("hasFurtherRefinements", "Yes");
		map1.put(
				"refinements",
				"dimentionKey^4294920036~name^Shirts~parentDimentionKey^4294920208~hasFurtherRefinements^No");

		final List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		resultList.add(map1);

		return resultList;

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
	 * Creats the subcategory url map.
	 * 
	 * @return map
	 */

	public static Map<String, List<String>> createSubCategoryDetailsURIMap() {

		final Map<String, List<String>> uriMap = new HashMap<String, List<String>>();
		final List<String> list1 = new ArrayList<String>();
		list1.add("4294922263");

		uriMap.put("categoryid", list1);
		list1.add("4294922262");
		uriMap.put("subcategory", list1);
		return uriMap;

	}

}
