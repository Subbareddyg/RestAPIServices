package com.belk.api.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is a helper class for unit testing BaseValidator
 * 
 * This Util class provides the data for other Test classes in BaseValidator
 * layer
 * 
 * @author Mindtree
 * @date March 21, 2014
 */
public class TestBaseValidatorUtil {

	/**
	 * Test method to get uriMap which returns uriMap
	 * 
	 * @return uriMap
	 */
	public static Map<String, List<String>> getUriMap() {
		final Map<String, List<String>> uriMap = new HashMap<String, List<String>>();
		final List qParams = new ArrayList();
		qParams.add("shirts");
		
		final List attrParams = new ArrayList();
		attrParams.add("color:blue|size:small");
		
		final List sortParams = new ArrayList();
		sortParams.add("name|listprice");
		
		uriMap.put("q", qParams);
		uriMap.put("attr", attrParams);	
		uriMap.put("sort", sortParams);

		return uriMap;

	}

	/**
	 * @return validatorPathConfigMap
	 */
	public static Map<String, String> getValidatorPathConfigMap() {
		final Map<String, String> validatorPathConfigMap = new HashMap<String, String>();
		validatorPathConfigMap.put("v1/products/search", "testproductsearch-validation.xml");
		return validatorPathConfigMap;
	}
	
	/**
	 * Test method to get uriMap which returns uriMap
	 * 
	 * @return uriMap
	 */
	public static Map<String, List<String>> getUriMapWithNullKeyValue() {
		final Map<String, List<String>> uriMap = new HashMap<String, List<String>>();
		final String paramValue = null;
		
		final List qParams = new ArrayList();
		qParams.add(paramValue);
		
		final List attrParams = new ArrayList();
		attrParams.add("color:blue|size:small");
		
		final List sortParams = new ArrayList();
		sortParams.add("name|listprice");
		
		uriMap.put("q", qParams);
		uriMap.put("attr", attrParams);	
		uriMap.put("sort", sortParams);

		return uriMap;

	}
	
	
	/**
	 * Test method to get uriMap which returns uriMap
	 * 
	 * @return uriMap
	 */
	public static Map<String, List<String>> getUriMapWithEmptyKeyValue() {
		final Map<String, List<String>> uriMap = new HashMap<String, List<String>>();		
		
		final List qParams = new ArrayList();
		qParams.add(" ");
		
		final List attrParams = new ArrayList();
		attrParams.add("color:blue|size:small");
		
		final List sortParams = new ArrayList();
		sortParams.add("name|listprice");
		
		uriMap.put("q", qParams);
		uriMap.put("attr", attrParams);	
		uriMap.put("sort", sortParams);

		return uriMap;

	}
	
	/**
	 * Test method to get uriMap which returns uriMap
	 * 
	 * @return uriMap
	 */
	public static Map<String, List<String>> getUriMapWithNotNumeric() {
		final Map<String, List<String>> uriMap = new HashMap<String, List<String>>();		
		
		final List limitparams = new ArrayList();
		limitparams.add("abcd");

		uriMap.put("limit", limitparams);		

		return uriMap;

	}
	
	/**
	 * Test method to get uriMap which returns uriMap
	 * 
	 * @return uriMap
	 */
	public static Map<String, List<String>> getUriMapWithInvalidKey() {
		final Map<String, List<String>> uriMap = new HashMap<String, List<String>>();		
		
		final List qParams = new ArrayList();
		qParams.add("shirts");
		
		uriMap.put("a", qParams);	

		return uriMap;

	}
	
	/**
	 * Test method to get uriMap which returns uriMap
	 * 
	 * @return uriMap
	 */
	public static Map<String, List<String>> getUriMapWithMaximumValue() {
		final Map<String, List<String>> uriMap = new HashMap<String, List<String>>();		
		
		final List qParams = new ArrayList();
		qParams.add("shirtsandhandbags");
		
		uriMap.put("a", qParams);	

		return uriMap;

	}
	
	/**
	 * Test method to get uriMap which returns uriMap
	 * 
	 * @return uriMap
	 */
	public static Map<String, List<String>> getUriMapWithSortParam() {
		final Map<String, List<String>> uriMap = new HashMap<String, List<String>>();		
		
		final List qParams = new ArrayList();
		qParams.add("shirts");
		
		final List attrParams = new ArrayList();
		attrParams.add("color:blue|size:small");
		
		final List sortParams = new ArrayList();
		sortParams.add("name|-listprice");
		
		uriMap.put("q", qParams);
		uriMap.put("attr", attrParams);	
		uriMap.put("sort", sortParams);

		return uriMap;

	}
	
	/**
	 * Test method to get uriMap which returns uriMap
	 * 
	 * @return uriMap
	 */
	public static Map<String, List<String>> getUriMapWithInvalidSortParam() {
		final Map<String, List<String>> uriMap = new HashMap<String, List<String>>();		
		
		final List qParams = new ArrayList();
		qParams.add("shirts");
		
		final List attrParams = new ArrayList();
		attrParams.add("color:blue|size:small");
		
		final List sortParams = new ArrayList();
		sortParams.add("name|abcd");
		
		uriMap.put("q", qParams);
		uriMap.put("attr", attrParams);	
		uriMap.put("sort", sortParams);

		return uriMap;

	}
	
	/**
	 * @return apiServicesConfigMap
	 */
	public static Map<String, Boolean> getApiServicesConfigMap() {
		final Map<String, Boolean> apiServicesConfigMap = new HashMap<String, Boolean>();
		apiServicesConfigMap.put("PRODUCT_SEARCH", true);
		return apiServicesConfigMap;
	}
	
	/**
	 * method to get uriMap which returns uriMap
	 * 
	 * @return uriMap
	 */
	public static Map<String, List<String>> getUriMapWithNotAlphabet() {
		final Map<String, List<String>> uriMap = new HashMap<String, List<String>>();		
		
		final List dimParams = new ArrayList();
		dimParams.add("10");
		
		uriMap.put("dim", dimParams);	

		return uriMap;
	}
	
	/**
	 * method to get error properties map.
	 * 
	 * @return errorPropertiesMap
	 */

	public static Map<String, String> getErrorPropertiesMap() {
		final Map<String, String> errorPropertiesMap = new HashMap();
		errorPropertiesMap.put("1235", "11421");
		errorPropertiesMap.put("1", "11425");
		errorPropertiesMap.put("2", "11425");
		errorPropertiesMap.put("3", "11422");
		errorPropertiesMap.put("4", "11422");
		errorPropertiesMap.put("5", "11422");
		errorPropertiesMap.put("6", "11422");
		errorPropertiesMap.put("7", "11422");
		errorPropertiesMap.put("8", "11422");
		errorPropertiesMap.put("9", "11422");
		errorPropertiesMap.put("10", "11422");
		errorPropertiesMap.put("11", "11422");
		errorPropertiesMap.put("12", "11422");
		errorPropertiesMap.put("13", "11425");		
		return errorPropertiesMap;
	}

	
}
