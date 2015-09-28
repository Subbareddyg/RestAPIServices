package com.belk.api.util;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * This class is a helper class for unit testing BaseResource
 * 
 * This Util class provides the data for other Test classes in BaseResource
 * layer
 * 
 * @author Mindtree Date Dec 06
 */
/**
 * @author Mindtree
 * @date Jul 27, 2013
 */
public class TestBaseResourceUtil {
	/**
	 * Test method to get uriMap which returns uriMap
	 * 
	 * @return uriMap
	 */
	public static Map<String, String> getUriMap() {
		final Map<String, String> uriMap = new HashMap<String, String>();
		uriMap.put("format", "format");

		return uriMap;

	}

	/**
	 * Test method to get uriMap which returns uriMap with hadrcoded catid
	 * 
	 * @return uriMap
	 */
	public static Map<String, String> getUriMapForNoRequestType() {
		final Map<String, String> uriMap = new HashMap<String, String>();
		uriMap.put("catid", "catid");
		return uriMap;
	}

	/**
	 * Test method to get uriMap which returns uriMap with hadrcoded value XML
	 * 
	 * @return uriMap
	 */
	public static Map<String, String> getUriMapWithXML() {
		final Map<String, String> uriMap = new HashMap<String, String>();
		uriMap.put("format", "XML");
		// uriMap.put(key, value)
		return uriMap;
	}
	
	
	/**
	 * @return MultivaluedMap
	 */
	/**
	 * Test method to get MultivaluedMap which returns Map with hadrcoded value XML
	 * 
	 * @return MultivaluedMap
	 */
	public static MultivaluedMap<String, String> getMultivaludMap() {
		final MultivaluedMap<String, String> uriParameters = new MultivaluedMapImpl();
		uriParameters.add("q", "handbag");
		uriParameters.add("Correlation-Id", "1234567891234567");
		uriParameters.add("catid", "catid");

		return uriParameters;
	}

}
