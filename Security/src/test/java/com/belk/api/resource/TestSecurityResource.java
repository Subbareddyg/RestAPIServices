/**
 * 
 */
package com.belk.api.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.belk.api.business.service.manager.ServiceManager;
import com.belk.api.constants.CommonConstants;
import com.belk.api.exception.BaseException;
import com.belk.api.security.AuthenticationDetails;
import com.belk.api.service.SecurityService;
import com.belk.api.transformer.xml.XMLProcessor;
import com.belk.api.util.ErrorLoader;
import com.belk.api.util.TestSecurityResourceUtil;
import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * @author Mindtree
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ SecurityResource.class, ServiceManager.class,
	SecurityService.class, BaseResource.class, UriInfo.class,
	HttpHeaders.class, ErrorLoader.class })
public class TestSecurityResource {

	HttpHeaders httpHeaders;
	UriInfo uriInfo;
	URI uri = null;
	UriBuilder uriBuilder;
	Response responseData = null;
	Response actualSearchResults = null;
	final String correlationId = "1234567890SDGFASF4545DFSERE";
	final MultivaluedMap<String, String> reqHeaders = new MultivaluedMapImpl();
	SecurityResource securityResource = new SecurityResource();
	final String uriInfoPath = "v1/security";
	Response expectedResponseData = null;
	Response actualResponseData = null;

	/**
	 * Method which executes before each test
	 * 
	 * @throws URISyntaxException
	 *             URISyntaxException
	 */

	@Before
	public final void setUp() throws URISyntaxException {
		this.reqHeaders.add(CommonConstants.CORRELATION_ID,
				"1234567890SDGFASF4545DFSERE");
		this.reqHeaders.add(CommonConstants.ENCRYPTED_APP_ID,
				"jPoZgE/P1isq5CK2Iusrpg==");
		this.reqHeaders.add(CommonConstants.ENCRYPTED_KEY_PARAMS,
				"6xqFmUh0zZSG/ZsFVCF7povoRCxppipgADcYmSm3BGE=");
	}

	/**
	 * Test method for
	 * {@link com.belk.api.resource.SecurityResource#getAuthToken(javax.ws.rs.core.HttpHeaders, javax.ws.rs.core.UriInfo)}
	 * .
	 * 
	 * @throws BaseException
	 *             exception thrown from this method
	 */
	@Test
	public final void testGetAuthToken() throws BaseException {
		final Map<String, List<String>> uriMap = TestSecurityResourceUtil
				.createURIMap();
		final MultivaluedMap<String, String> uriParameters = TestSecurityResourceUtil
				.createURIParametersMap();
		this.httpHeaders = mock(HttpHeaders.class);
		this.uriInfo = mock(UriInfo.class);
		this.uriBuilder = mock(UriBuilder.class);
		when(this.httpHeaders.getRequestHeaders()).thenReturn(this.reqHeaders);
		stub(this.uriInfo.getAbsolutePathBuilder()).toReturn(this.uriBuilder);
		when(this.uriInfo.getQueryParameters()).thenReturn(uriParameters);
		when(this.uriInfo.getPath()).thenReturn(this.uriInfoPath);

		// ServiceManager is mocked to get the instance of the service.
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.securityResource.setServiceManager(serviceManager);

		// CategoryDetailsService is mocked to get the instance of the service
		final SecurityService securityService = PowerMockito
				.mock(SecurityService.class);
		PowerMockito.when(serviceManager.getService()).thenReturn(
				securityService);
		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		Map<String, String> errorPropertiesMap = TestSecurityResourceUtil
				.getErrorPropertiesMap();
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(
				errorPropertiesMap);
		this.securityResource.setErrorLoader(errorLoader);
		BaseResource baseResourceMock = PowerMockito.mock(BaseResource.class);
		baseResourceMock.setErrorLoader(errorLoader);

		AuthenticationDetails authToken = null;

		String encryptedAppId = this.reqHeaders
				.get(CommonConstants.ENCRYPTED_APP_ID).get(0).toString();
		String encryptedKeyParams = this.reqHeaders
				.get(CommonConstants.ENCRYPTED_KEY_PARAMS).get(0).toString();
		PowerMockito.when(
				securityService.processRequest(encryptedAppId,
						encryptedKeyParams, this.correlationId)).thenReturn(
								authToken);

		// BaseResource is mocked to get the instance of the base resource.
		final BaseResource baseResource = PowerMockito.mock(BaseResource.class);
		/*PowerMockito.when(
				this.securityResource.getResponseType(uriMap,
						this.correlationId))
						.thenReturn("xml");*/
		/*
		 * PowerMockito.when( baseResource.getResponseType(uriMap,
		 * this.correlationId)) .thenReturn("xml");
		 */
		final ResponseBuilder responseBuilder = new ResponseBuilderImpl();
		responseBuilder.status(200);

		// mocking call to method buildResponse and return responsebuilder
		// object
		PowerMockito.when(
				baseResource.buildResponse(
						TestSecurityResourceUtil.createAuthToken(),
						AuthenticationDetails.class, "security-binding", "XML",
						this.correlationId)).thenReturn(responseBuilder);

		this.responseData = responseBuilder.build();

		final XMLProcessor xmlProcessor = new XMLProcessor();
		this.securityResource.setXmlProcessor(xmlProcessor);

		final Response actualSearchResults = this.securityResource
				.getAuthToken(this.httpHeaders, this.uriInfo);

		assertNotNull(actualSearchResults);

		assertNotNull(actualSearchResults.getStatus());
		assertNotNull(actualSearchResults.getMetadata());
		assertEquals(200, actualSearchResults.getStatus());
	}

	/**
	 * Test method for
	 * {@link com.belk.api.resource.SecurityResource#updatePropertiesMap(javax.ws.rs.core.HttpHeaders, javax.ws.rs.core.UriInfo)}
	 * .
	 */
	@Test
	public final void testUpdatePropertiesMap() {
		final MultivaluedMap<String, String> uriParameters = new MultivaluedMapImpl();

		uriParameters.add("filename", "error.properties");
		uriParameters.add("format", "xml");
		uriParameters.add("propertieslist", "11523:hello|11436:hi");
		uriParameters.add("loglevel", "info");
		// UriInfo and UriBuilder is mocked to get the instance of the UriInfo
		// which will be input for implementation.
		this.uriInfo = mock(UriInfo.class);
		this.uriBuilder = mock(UriBuilder.class);
		this.httpHeaders = mock(HttpHeaders.class);

		when(this.httpHeaders.getRequestHeaders()).thenReturn(this.reqHeaders);
		stub(this.uriInfo.getAbsolutePathBuilder()).toReturn(this.uriBuilder);
		when(this.uriInfo.getQueryParameters()).thenReturn(uriParameters);
		when(this.uriInfo.getPath()).thenReturn(this.uriInfoPath);
		// ServiceManager is mocked to get the instance of the service.
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.securityResource.setServiceManager(serviceManager);
		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		Map<String, String> errorPropertiesMap = TestSecurityResourceUtil
				.getErrorPropertiesMap();
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(
				errorPropertiesMap);
		this.securityResource.setErrorLoader(errorLoader);
		BaseResource baseResourceMock = PowerMockito.mock(BaseResource.class);
		baseResourceMock.setErrorLoader(errorLoader);
		// CategoryDetailsService is mocked to get the instance of the service
		// and is passed
		// as return from call to service manager.
		final SecurityService securityServiceMock = PowerMockito
				.mock(SecurityService.class);

		PowerMockito.when(serviceManager.getService()).thenReturn(
				securityServiceMock);

		final ResponseBuilder responseBuilder = new ResponseBuilderImpl();
		responseBuilder.status(200);

		this.expectedResponseData = responseBuilder.build();

		this.actualResponseData = this.securityResource.updatePropertiesMap(
				this.httpHeaders, this.uriInfo);

		assertEquals(this.expectedResponseData.getStatus(),
				this.actualResponseData.getStatus());
	}

}
