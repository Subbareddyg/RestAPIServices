/**
 * 
 */
package com.belk.api.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.belk.api.constants.ErrorConstants;
import com.belk.api.exception.BaseException;
import com.belk.api.exception.ServiceException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.belk.api.mail.MailClient;
import com.belk.api.security.AuthenticationDetails;
import com.belk.api.security.SecurityUtil;
import com.belk.api.util.CommonUtil;
import com.belk.api.util.ErrorLoader;
import com.belk.api.util.PropertyReloader;
import com.belk.api.util.SecurityLoader;

/**
 * @author Mindtree
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ SecurityServiceImpl.class, SecurityUtil.class,
	ErrorLoader.class, CommonUtil.class, SecurityLoader.class,
	PropertyReloader.class, MailClient.class })
public class TestSecurityServiceImpl extends TestCase {

	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}
	String correlationId = "1234567891234567";

	SecurityServiceImpl securityService = new SecurityServiceImpl();

	ErrorLoader errorLoader;

	Map<String, String> errorPropertiesMap;

	// SecurityUtil securityUtil;

	/*
	 * ErrorLoader errorLoader;
	 * 
	 * CommonUtil commonUtil;
	 * 
	 * SecurityLoader securityLoader;
	 * 
	 * CacheManager cacheManager;
	 */

	// SecurityUtil securityUtil = new SecurityUtil();


	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	@Before
	public final void setUp() throws Exception {
		errorLoader = PowerMockito.mock(ErrorLoader.class);
		errorPropertiesMap = new HashMap<String, String>();
		errorPropertiesMap.put("11523", "internal server error");
		errorLoader.setErrorPropertiesMap(errorPropertiesMap);
		this.securityService.setErrorLoader(errorLoader);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.service.impl.SecurityServiceImpl#processRequest(java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 * 
	 * @throws Exception
	 *             exception
	 */

	public final void testProcessRequest() throws Exception {
		AuthenticationDetails actualAuthenticationDetails;
		final String testEncryptedAppId = "jPoZgE/P1isq5CK2Iusrpg==";
		final String testEncryptedKeyParams = "6xqFmUh0zZSG/ZsFVCF7povoRCxppipgADcYmSm3BGE=";

		SecurityUtil securityUtilMock = PowerMockito.mock(SecurityUtil.class);
		this.securityService.setSecurityUtils(securityUtilMock);

		PowerMockito
		.when(
				securityUtilMock.getCallingApplicationId(testEncryptedAppId,
						this.correlationId)).thenReturn("ConfigUI");
		PowerMockito.when(
				securityUtilMock.getAppSpecificEncryptionKey("ConfigUI",
						this.correlationId)).thenReturn("c0nfig!234");
		PowerMockito.when(
				securityUtilMock.decrypt(testEncryptedKeyParams, "c0nfig!234",
						this.correlationId)).thenReturn(
								"ConfigUI:admin:" + System.currentTimeMillis());

		PowerMockito.when(
				securityUtilMock.isRequestAlive(System.currentTimeMillis(),
						this.correlationId))
						.thenReturn(true);
		PowerMockito.when(
				securityUtilMock.generateAuthToken(
						"ConfigUI:admin:" + System.currentTimeMillis(),
						this.correlationId)).thenReturn("TestToken");
		/*PowerMockito.doNothing().when(
				securityUtilMock.storeAuthTokenInCache("ConfigUI:admin",
						"TestToken", this.correlationId));*/

		/*PowerMockito.doNothing().when(securityUtilMock,
				"storeAuthTokenInCache", "ConfigUI:admin", "TestToken",
				this.correlationId);*/

		PowerMockito.when(securityUtilMock.encrypt("TestToken", "c0nfig!234", this.correlationId))
		.thenReturn("TestEncryption");


		actualAuthenticationDetails = this.securityService.processRequest(
				testEncryptedAppId, testEncryptedKeyParams, this.correlationId);

		assertNotNull(actualAuthenticationDetails);
	}

	/**
	 * Test method for testing whether the proper error message is thrown when a
	 * request generated more than 60 secs back is sent.
	 * 
	 * @throws BaseException
	 *             exception
	 * 
	 */

	public final void testExpiredRequest() throws BaseException {
		final String testEncryptedAppId = "jPoZgE/P1isq5CK2Iusrpg==";
		final String testEncryptedKeyParams = "6xqFmUh0zZSG/ZsFVCF7povoRCxppipgADcYmSm3BGE=";

		SecurityUtil securityUtilMock = PowerMockito.mock(SecurityUtil.class);
		this.securityService.setSecurityUtils(securityUtilMock);

		PowerMockito.when(
				securityUtilMock.getCallingApplicationId(testEncryptedAppId,
						this.correlationId)).thenReturn("ConfigUI");
		PowerMockito.when(
				securityUtilMock.getAppSpecificEncryptionKey("ConfigUI",
						this.correlationId)).thenReturn("c0nfig!234");
		PowerMockito.when(
				securityUtilMock.decrypt(testEncryptedKeyParams, "c0nfig!234",
						this.correlationId)).thenReturn(
								"ConfigUI:admin:" + System.currentTimeMillis());

		try {
			this.securityService.processRequest(testEncryptedAppId,
					testEncryptedKeyParams, this.correlationId);
		} catch (BaseException e) {
			assertEquals(
					String.valueOf(ErrorConstants.ERROR_CODE_EXPIRED_REQUEST),
					e.getErrorCode());
		}
	}

	/**
	 * Test method for testing whether the proper error message is thrown when
	 * at least one of the mandatory params are absent in the request.
	 * 
	 * @throws BaseException
	 *             exception thrown from this method
	 * 
	 */

	public final void testMandatoryRequestParams() throws BaseException {
		final String testEncryptedAppId = "jPoZgE/P1isq5CK2Iusrpg==";

		SecurityUtil securityUtilMock = PowerMockito.mock(SecurityUtil.class);
		this.securityService.setSecurityUtils(securityUtilMock);

		PowerMockito.when(
				securityUtilMock.getCallingApplicationId(testEncryptedAppId,
						this.correlationId)).thenReturn("ConfigUI");
		PowerMockito.when(
				securityUtilMock.getAppSpecificEncryptionKey("ConfigUI",
						this.correlationId)).thenReturn("c0nfig!234");
		PowerMockito.when(
				securityUtilMock
				.decrypt(null, "c0nfig!234", this.correlationId))
				.thenReturn("ConfigUI:admin:" + System.currentTimeMillis());

		try {
			this.securityService.processRequest(testEncryptedAppId, null,
					this.correlationId);
		} catch (ServiceException e) {
			assertEquals(String.valueOf(ErrorConstants.ERROR_CODE_11425),
					e.getErrorCode());
		}
	}

	/**
	 * Test method for {@link com.belk.api.service.impl.SecurityServiceImpl#updatePropertiesMap(java.lang.String, java.util.Map)}.
	 * @throws Exception exception thrown from this method.
	 */

	public final void testUpdatePropertiesMap() throws Exception {

		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(
				errorPropertiesMap);
		Map<String, List<String>> updateRequestUriMap = new HashMap<String, List<String>>();
		List<String> propertyList = new ArrayList<String>();
		propertyList.add("error.properties");
		updateRequestUriMap.put("filename", propertyList);
		List<String> propertyList1 = new ArrayList<String>();
		propertyList1.add("11421:hello|11422:hi");
		updateRequestUriMap.put("propertieslist", propertyList1);
		final String correlationId = "1234567891234567";
		PropertyReloader propertyReloaderMock = PowerMockito
				.mock(PropertyReloader.class);
		PowerMockito.doNothing().when(propertyReloaderMock,
				"updatePropertyMap", updateRequestUriMap, correlationId);
		MailClient mailClient = PowerMockito.mock(MailClient.class);
		this.securityService.setMailClient(mailClient);
		PowerMockito.doNothing().when(mailClient, "sendEmail",
				updateRequestUriMap, correlationId, "success");
		this.securityService.updatePropertiesMap(correlationId,
				updateRequestUriMap);
		assertNotNull(this.securityService);
	}

}
