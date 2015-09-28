package com.belk.api.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Mindtree
 * @date Dec 5, 2013
 */
public class TestAdminResourceUtil {
	/**
	 * Method returns Map<String,String>
	 * 
	 * @return Map<String,String>
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

}
