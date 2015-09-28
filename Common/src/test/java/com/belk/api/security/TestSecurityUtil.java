/**
 * 
 */
package com.belk.api.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.belk.api.cache.CacheManager;
import com.belk.api.constants.CommonConstants;
import com.belk.api.constants.ErrorConstants;
import com.belk.api.exception.BaseException;
import com.belk.api.util.CommonUtil;
import com.belk.api.util.ErrorLoader;
import com.belk.api.util.SecurityLoader;

/**
 * @author Mindtree
 *
 */
@RunWith(PowerMockRunner.class)
public class TestSecurityUtil {

	SecurityUtil securityUtil = new SecurityUtil();
	String correlationId;
	String encryptedString;
	String correctEncryptionKey;
	String incorrectEncryptionKey;
	String originalString;
	Map<String, String> securityPropertiesMap = new HashMap<String, String>();
	Map<String, String> errorPropertiesMap = new HashMap<String, String>();

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.correlationId = "123456";
		this.encryptedString = "jPoZgE/P1isq5CK2Iusrpg==";
		this.correctEncryptionKey = "Belk!234";
		this.incorrectEncryptionKey = "1234";
		this.originalString = "ConfigUI";
		this.securityPropertiesMap.put(CommonConstants.MAX_TOKEN_GAP_TIME,
				"60000");
		this.securityPropertiesMap.put(CommonConstants.COMMON_ENCRYPTION_KEY,
				this.correctEncryptionKey);
		this.securityPropertiesMap.put("ConfigUI_EK", "c0nfig!234");

		errorPropertiesMap = new HashMap<String, String>();
		errorPropertiesMap.put("11523",
				"There has been an internal service error");
		errorPropertiesMap.put("11524",
				"The supplied authentication token is invalid");
		errorPropertiesMap.put("11525",
				"The supplied authentication token has expired");
		errorPropertiesMap.put("11526",
				"The request is made beyond the accepted interval");

	}

	/**
	 * Test method for
	 * {@link com.belk.api.security.SecurityUtil#decrypt(java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 * 
	 * @throws BaseException
	 *             exception thrown from this method
	 */
	@Test
	public final void testDecrypt() throws BaseException {
		ErrorLoader errorLoaderMock = PowerMockito.mock(ErrorLoader.class);
		errorLoaderMock.setErrorPropertiesMap(errorPropertiesMap);
		this.securityUtil.setErrorLoader(errorLoaderMock);
		CommonUtil commonUtilMock = PowerMockito.mock(CommonUtil.class);
		this.securityUtil.setCommonUtil(commonUtilMock);
		SecurityLoader securityLoaderMock = PowerMockito
				.mock(SecurityLoader.class);
		securityLoaderMock.setSecurityPropertiesMap(this.securityPropertiesMap);
		this.securityUtil.setSecurityLoader(securityLoaderMock);

		String expectedString = "ConfigUI";
		String actualString = this.securityUtil.decrypt(encryptedString,
				correctEncryptionKey,
				correlationId);
		assertEquals(expectedString, actualString);
	}

	/**
	 * Test method to check decrypt() for exceptional conditions
	 * 
	 */
	@Test
	public void testDecryptForExceptions() {
		ErrorLoader errorLoaderMock = PowerMockito.mock(ErrorLoader.class);
		errorLoaderMock.setErrorPropertiesMap(errorPropertiesMap);
		this.securityUtil.setErrorLoader(errorLoaderMock);
		CommonUtil commonUtilMock = PowerMockito.mock(CommonUtil.class);
		this.securityUtil.setCommonUtil(commonUtilMock);
		SecurityLoader securityLoaderMock = PowerMockito
				.mock(SecurityLoader.class);
		securityLoaderMock.setSecurityPropertiesMap(this.securityPropertiesMap);
		this.securityUtil.setSecurityLoader(securityLoaderMock);

		// Test for invalid key
		try {
			String actualString = this.securityUtil.decrypt("Invalid String",
					correctEncryptionKey,
					correlationId);
		} catch (BaseException e) {
			assertEquals(
					String.valueOf(ErrorConstants.ERROR_CODE_INVALID_TOKEN),
					e.getErrorCode());
		}

		// Test for null key
		try {
			String actualString = this.securityUtil.decrypt("",
					correctEncryptionKey, correlationId);
		} catch (BaseException e) {
			assertEquals(String.valueOf(ErrorConstants.ERROR_CODE_11425),
					e.getErrorCode());
		}

	}

	/**
	 * Test method for
	 * {@link com.belk.api.security.SecurityUtil#encrypt(java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 * 
	 * @throws BaseException
	 *             exception thrown from this method
	 */
	@Test
	public final void testEncrypt() throws BaseException {
		ErrorLoader errorLoaderMock = PowerMockito.mock(ErrorLoader.class);
		errorLoaderMock.setErrorPropertiesMap(errorPropertiesMap);
		this.securityUtil.setErrorLoader(errorLoaderMock);
		CommonUtil commonUtilMock = PowerMockito.mock(CommonUtil.class);
		this.securityUtil.setCommonUtil(commonUtilMock);
		SecurityLoader securityLoaderMock = PowerMockito
				.mock(SecurityLoader.class);
		securityLoaderMock.setSecurityPropertiesMap(this.securityPropertiesMap);
		this.securityUtil.setSecurityLoader(securityLoaderMock);

		String expectedStringWithAppSpecificEK = this.encryptedString;
		String actualString = this.securityUtil.encrypt(originalString,
				correctEncryptionKey, correlationId);
		assertEquals(expectedStringWithAppSpecificEK, actualString);

		String expectedStringWithCEK = "jPoZgE/P1isq5CK2Iusrpg==";
		actualString = this.securityUtil.encrypt("ConfigUI",
				"", this.correlationId);
		assertEquals(expectedStringWithCEK, actualString);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.security.SecurityUtil#generateAuthToken(java.lang.String, java.lang.String)}
	 * .
	 * 
	 * @throws BaseException
	 *             exception thrown from this method
	 */
	@Test
	public final void testGenerateAuthToken() throws BaseException {
		ErrorLoader errorLoaderMock = PowerMockito.mock(ErrorLoader.class);
		errorLoaderMock.setErrorPropertiesMap(errorPropertiesMap);
		this.securityUtil.setErrorLoader(errorLoaderMock);
		CommonUtil commonUtilMock = PowerMockito.mock(CommonUtil.class);
		this.securityUtil.setCommonUtil(commonUtilMock);
		SecurityLoader securityLoaderMock = PowerMockito
				.mock(SecurityLoader.class);
		securityLoaderMock.setSecurityPropertiesMap(this.securityPropertiesMap);
		this.securityUtil.setSecurityLoader(securityLoaderMock);
		String authToken = null;
		authToken = this.securityUtil.generateAuthToken(this.originalString,
				this.correlationId);
		assertNotNull(authToken);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.security.SecurityUtil#isRequestAlive(long, java.lang.String)}
	 * .
	 * 
	 * @throws BaseException
	 *             exception thrown from this method
	 */
	@Test
	public final void testIsRequestAlive() throws BaseException {

		long aliveTimestamp = System.currentTimeMillis();
		long expiredTimestamp = System.currentTimeMillis() - 72000;

		SecurityLoader securityLoaderMock = PowerMockito
				.mock(SecurityLoader.class);
		securityLoaderMock.setSecurityPropertiesMap(this.securityPropertiesMap);
		this.securityUtil.setSecurityLoader(securityLoaderMock);

		boolean isAlive = false;
		isAlive = this.securityUtil.isRequestAlive(aliveTimestamp,
				this.correlationId);
		assertEquals(isAlive, true);

		isAlive = this.securityUtil.isRequestAlive(expiredTimestamp,
				this.correlationId);
		assertEquals(isAlive, false);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.security.SecurityUtil#getCallingApplicationId(java.lang.String, java.lang.String)}
	 * .
	 * 
	 * @throws BaseException
	 *             exception thrown from this method
	 */
	@Test
	public final void testGetCallingApplicationId() throws BaseException {
		ErrorLoader errorLoaderMock = PowerMockito.mock(ErrorLoader.class);
		errorLoaderMock.setErrorPropertiesMap(errorPropertiesMap);
		this.securityUtil.setErrorLoader(errorLoaderMock);
		CommonUtil commonUtilMock = PowerMockito.mock(CommonUtil.class);
		this.securityUtil.setCommonUtil(commonUtilMock);
		SecurityLoader securityLoaderMock = PowerMockito
				.mock(SecurityLoader.class);
		securityLoaderMock.setSecurityPropertiesMap(this.securityPropertiesMap);
		this.securityUtil.setSecurityLoader(securityLoaderMock);

		String expectedString = "ConfigUI";
		String actualString = this.securityUtil.getCallingApplicationId(
				encryptedString, correlationId);
		assertEquals(expectedString, actualString);
	}

	/**
	 * Test method for {@link com.belk.api.security.SecurityUtil#getAppSpecificEncryptionKey(java.lang.String, java.lang.String)}.
	 */
	@Test
	public final void testGetAppSpecificEncryptionKey() {
		String expectedAppSpecificEK = "c0nfig!234";
		SecurityLoader securityLoaderMock = PowerMockito
				.mock(SecurityLoader.class);
		securityLoaderMock.setSecurityPropertiesMap(this.securityPropertiesMap);
		this.securityUtil.setSecurityLoader(securityLoaderMock);

		String actualAppSpecifiEK = this.securityUtil
				.getAppSpecificEncryptionKey("ConfigUI", this.correlationId);
		assertEquals(expectedAppSpecificEK, actualAppSpecifiEK);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.security.SecurityUtil#validateToken(java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 * @throws Exception exception thrown from this method
	 */
	@Test
	public void testValidateToken() throws Exception {
		final String testEncryptedAppId = "jPoZgE/P1isq5CK2Iusrpg==";
		final String testEncryptedKeyParams = "6xqFmUh0zZSG/ZsFVCF7prxJyYFUGWj6oL4FzIB01bc=";
		ErrorLoader errorLoaderMock = PowerMockito.mock(ErrorLoader.class);
		errorLoaderMock.setErrorPropertiesMap(errorPropertiesMap);
		this.securityUtil.setErrorLoader(errorLoaderMock);
		CacheManager cacheManagerMock = PowerMockito.mock(CacheManager.class);
		this.securityUtil.setCacheManager(cacheManagerMock);
		CommonUtil commonUtilMock = PowerMockito.mock(CommonUtil.class);
		this.securityUtil.setCommonUtil(commonUtilMock);
		SecurityLoader securityLoaderMock = PowerMockito
				.mock(SecurityLoader.class);
		securityLoaderMock.setSecurityPropertiesMap(this.securityPropertiesMap);
		this.securityUtil.setSecurityLoader(securityLoaderMock);
		boolean isTokenValid = false;

		try {
			/*PowerMockito.when(cacheManagerMock, "get", "ConfigUI:admin",
					String.class, this.correlationId).thenReturn("TestToken");*/
			org.mockito.Mockito.when(cacheManagerMock.get("ConfigUI:admin",
					String.class, this.correlationId)).thenReturn("TestToken");
			isTokenValid = this.securityUtil.validateToken(
					testEncryptedAppId, testEncryptedKeyParams, this.correlationId);

			assertEquals(true, isTokenValid);
		} catch (BaseException e) {
			assertEquals(false, isTokenValid);
		}

	}

	@Test
	public void testValidateTokenForException() throws Exception {
		final String testEncryptedAppId = "jPoZgE/P1isq5CK2Iusrpg==";
		final String testEncryptedKeyParams = "6xqFmUh0zZSG/ZsFVCF7prxJyYFUGWj6oL4FzIB01bc=";
		ErrorLoader errorLoaderMock = PowerMockito.mock(ErrorLoader.class);
		errorLoaderMock.setErrorPropertiesMap(errorPropertiesMap);
		this.securityUtil.setErrorLoader(errorLoaderMock);
		CacheManager cacheManagerMock = PowerMockito.mock(CacheManager.class);
		this.securityUtil.setCacheManager(cacheManagerMock);
		CommonUtil commonUtilMock = PowerMockito.mock(CommonUtil.class);
		this.securityUtil.setCommonUtil(commonUtilMock);
		SecurityLoader securityLoaderMock = PowerMockito
				.mock(SecurityLoader.class);
		securityLoaderMock.setSecurityPropertiesMap(this.securityPropertiesMap);
		this.securityUtil.setSecurityLoader(securityLoaderMock);
		boolean isTokenValid;
		try {
			// Test for null cache element
			/*PowerMockito.when(cacheManagerMock, "get", "ConfigUI:admin",
					String.class, this.correlationId).thenReturn(null);*/
			org.mockito.Mockito.when(cacheManagerMock.get("ConfigUI:admin",
					String.class, this.correlationId)).thenReturn(null);
			isTokenValid = this.securityUtil.validateToken(
					testEncryptedAppId, testEncryptedKeyParams,
					this.correlationId);
		} catch (BaseException e) {
			assertEquals(
					String.valueOf(ErrorConstants.ERROR_CODE_EXPIRED_TOKEN),
					e.getErrorCode());
		}
	}

	/**
	 * Test method for
	 * {@link com.belk.api.security.SecurityUtil#storeAuthTokenInCache(java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 * 
	 * @throws Exception
	 *             exception thrown from this method
	 */
	@Test
	public final void testStoreAuthTokenInCache() throws Exception {
		CacheManager cacheManagerMock = PowerMockito.mock(CacheManager.class);
		this.securityUtil.setCacheManager(cacheManagerMock);

		PowerMockito.doNothing().when(cacheManagerMock, "set",
				"ConfigUI:admin", "TestToken", 120, this.correlationId);
	}

}
