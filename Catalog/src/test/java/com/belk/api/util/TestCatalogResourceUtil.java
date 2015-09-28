package com.belk.api.util;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import com.belk.api.constants.CommonConstants;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * This class is a helper class for unit testing CatalogResource
 * 
 * This Util class provides the data for other Test classes in CatalogResource
 * layer
 * 
 * @author Mindtree
 * @date Dec 5, 2013
 */
public class TestCatalogResourceUtil {

	/**
	 * Method returns Map<String, String>
	 * 
	 * @return Map<String, String>
	 */
	public static Map<String, String> getUriMap() {
		final Map<String, String> uriMap = new HashMap<String, String>();
		uriMap.put("catalogid", "catalogid");
		uriMap.put("showproduct", "false");
		uriMap.put("format", "xml");
		uriMap.put("rootDimension", "4294967294");
		uriMap.put("catid", "4294922263");
		return uriMap;

	}

	/**
	 * @return MultivaluedMap<String, String> reqHeaders
	 */
	public static final MultivaluedMap<String, String> getMultiValuedMap() {
		final MultivaluedMap<String, String> reqHeaders = null;

		reqHeaders.add("Correlation-Id", "123");
		return reqHeaders;
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

}
