/**
 * 
 */
package com.belk.api.service.util;

import java.util.HashMap;
import java.util.Map;

import com.belk.api.constants.CommonConstants;
import com.belk.api.exception.BaseException;
import com.belk.api.security.SecurityUtil;

/**
 * @author Mindtree
 *
 */
public class TestSecurityServiceUtil {
	static String correlationId = "1234567891234567";
	private static SecurityUtil securityUtil = new SecurityUtil();

	/**
	 * Method to generate a test auth token
	 * 
	 * @return test auth token
	 * @throws BaseException
	 *             exception thrown from this method
	 */


	public static String getAuthToken() throws BaseException {
		final String authToken = securityUtil.generateAuthToken(
				"ConfigUI:admin:"
						+ System.currentTimeMillis(),
						correlationId);
		return authToken;
	}

	/**
	 * Method to generate a test app specific EK
	 * 
	 * @return test app specific EK
	 */
	public static String getAppSpecificEK() {
		final String appSpecificEK = "c0nfig!234";
		return appSpecificEK;
	}

	/**
	 * Method to return a test security properties map.
	 * 
	 * @return map
	 */
	public static Map<String, String> getTestSecurityPropertiesMap() {
		final Map<String, String> securityPropertiesMap = new HashMap<String, String>();
		securityPropertiesMap.put(CommonConstants.COMMON_ENCRYPTION_KEY,
				"Belk!234");
		securityPropertiesMap.put("ConfigUI_EK", "c0nfig!234");
		securityPropertiesMap.put("MAX_TOKEN_GAP_TIME", "60000");
		securityPropertiesMap.put("TOKEN_EXPIRY_TIME", "120");
		return securityPropertiesMap;
	}

}
