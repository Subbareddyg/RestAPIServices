package com.belk.api.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.belk.api.business.service.manager.ServiceManager;
import com.belk.api.cache.CacheManager;
import com.belk.api.constants.EndecaConstants;
import com.belk.api.exception.AdapterException;
import com.belk.api.exception.BaseException;
import com.belk.api.exception.ServiceException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.belk.api.model.catalog.Catalog;
import com.belk.api.service.CatalogService;
import com.belk.api.service.impl.CatalogServiceImpl;
import com.belk.api.transformer.json.JSONProcessor;
import com.belk.api.transformer.xml.XMLProcessor;
import com.belk.api.util.EndecaLoader;
import com.belk.api.util.TestCatalogResourceUtil;
import com.belk.api.validators.BaseValidator;
import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * Unit Testing related to CatalogResource class is performed. <br />
 * {@link CatalogResource} class is written for testing methods in
 * {@link CatalogResource} The unit test cases evaluates the way the methods
 * behave for the inputs given.
 * 
 * <br />
 * The test cases associated with CatalogResource is written to make the code
 * intactv by assuring the code test.
 * 
 * @author Mindtree
 * @date Dec 04, 2013
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ CatalogResource.class, ServiceManager.class,
	HttpHeaders.class, Response.class, Catalog.class, CatalogService.class,
	XMLProcessor.class, CacheManager.class, BaseValidator.class, EndecaLoader.class })
public class TestCatalogResource {

	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Catalog");
	UriInfo uriInfo;
	UriBuilder uriBuilder;
	HttpHeaders httpHeaders;
	CatalogResource catalogResource = new CatalogResource();

	BaseResource baseResource = new BaseResource();

	CatalogServiceImpl catalogService = new CatalogServiceImpl();
	Map<String, String> endecaPropertiesMap = new HashMap<String, String>();
	

	MultivaluedMap<String, String> reqHeaders = new MultivaluedMapImpl();

	Response expectedResponseData = null;
	Response actualResponseData = null;
	private final String correlationId = "1234567891234567";

	{
		LoggerUtil.setLogger(LOGGER);
	}

	/**
	 * 
	 * @throws Exception
	 *             throws Exception
	 */
	@Before
	public final void setUp() throws Exception {
		this.reqHeaders.add("Correlation-Id", "1234567891234567");
		final EndecaLoader endecaLoader = PowerMockito.mock(EndecaLoader.class);
		this.catalogResource.setEndecaLoader(endecaLoader);
		this.endecaPropertiesMap.put(EndecaConstants.CATALOG_ROOT_DIMENSION_VALUE, "4294967294");
		PowerMockito.when(endecaLoader.getEndecaPropertiesMap()).thenReturn(this.endecaPropertiesMap);
		
		
	}

	/**
	 * Test method for
	 * {@link com.belk.api.resource.CatalogResource#getCatalog(String,String,HttpHeaders)}
	 * tests for exception
	 * 
	 * @throws BaseException
	 *             BaseException
	 * @throws AdapterException
	 *             AdapterException
	 * @throws ServiceException
	 *             ServiceException
	 */
	@Test 
	public final void testGetCatalogForBaseException() throws BaseException,
	AdapterException, ServiceException {
		final MultivaluedMap<String, String> uriParameters = TestCatalogResourceUtil
				.createURIParametersMap();

		final MultivaluedMap<String, String> reqHeaders = TestCatalogResourceUtil
				.createRequestHeadersMap();
		this.httpHeaders = mock(HttpHeaders.class);
		// UriInfo and UriBuilder is mocked to get the instance of the UriInfo
		// which will be input for implementation.
		this.uriInfo = mock(UriInfo.class);
		this.uriBuilder = mock(UriBuilder.class);
		stub(this.httpHeaders.getRequestHeaders()).toReturn(reqHeaders);
		stub(this.uriInfo.getAbsolutePathBuilder()).toReturn(this.uriBuilder);
		when(this.uriInfo.getPathParameters()).thenReturn(uriParameters);
		when(this.uriInfo.getQueryParameters()).thenReturn(uriParameters);
		this.catalogResource.setUriInfo(this.uriInfo);
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.catalogResource.setServiceManager(serviceManager);
		final BaseValidator baseValidator = PowerMockito
				.mock(BaseValidator.class);
		this.catalogResource.setBaseValidator(baseValidator);
		PowerMockito.when(baseValidator.isBaseValidationRequired("CATALOG", this.correlationId)).thenReturn(false);
		final Response actualResponseData;
		final Response expectedResponseData;
		final XMLProcessor xmlProcessor = new XMLProcessor();
		this.baseResource.setXmlProcessor(xmlProcessor);
		final HttpHeaders httpHeaders = PowerMockito.mock(HttpHeaders.class);
		PowerMockito.when(httpHeaders.getRequestHeaders()).thenReturn(
				this.reqHeaders);
		PowerMockito.mock(Response.class);

		final Catalog catalog = PowerMockito.mock(Catalog.class);

		final CatalogService catalogService = PowerMockito
				.mock(CatalogService.class);
		PowerMockito.when(serviceManager.getService()).thenReturn(
				catalogService);
		final Map<String, String> uriMap = TestCatalogResourceUtil.getUriMap();
		PowerMockito
		.when(catalogService.getCatalog(uriMap, this.correlationId))
		.thenReturn(catalog);
		PowerMockito.mockStatic(BaseResource.class);
		final String responseType = "XML";
		PowerMockito.mock(BaseResource.class);
		final JSONProcessor jsonProcessor = new JSONProcessor();
		this.catalogResource.setJsonProcessor(jsonProcessor);
		final BaseResource baseResource = PowerMockito.mock(BaseResource.class);
		PowerMockito.when(
				baseResource.getResponseTypeForURIParam(uriMap,
						this.correlationId)).thenReturn(responseType);
		final ResponseBuilder responseBuilder = new ResponseBuilderImpl();
		responseBuilder.status(400);
		expectedResponseData = responseBuilder.build();

		actualResponseData = this.catalogResource.getCatalog("catalogId",
				"XML", httpHeaders);
		assertNotNull(expectedResponseData.getStatus());

	}

	/**
	 * Test method for
	 * {@link com.belk.api.resource.CatalogResource#getCatalogWithAllCategories(String,String,HttpHeaders)}
	 * tests for exception
	 * 
	 * @throws BaseException
	 *             BaseException
	 * @throws AdapterException
	 *             AdapterException
	 * @throws ServiceException
	 *             ServiceException
	 */
	@Test(expected = Exception.class)
	public final void testGetCatalogWithAllCategoriesForBaseException()
			throws BaseException, AdapterException, ServiceException {
		final MultivaluedMap<String, String> uriParameters = TestCatalogResourceUtil
				.createURIParametersMap();

		final MultivaluedMap<String, String> reqHeaders = TestCatalogResourceUtil
				.createRequestHeadersMap();
		this.httpHeaders = mock(HttpHeaders.class);
		// UriInfo and UriBuilder is mocked to get the instance of the UriInfo
		// which will be input for implementation.
		this.uriInfo = mock(UriInfo.class);
		this.uriBuilder = mock(UriBuilder.class);
		stub(this.httpHeaders.getRequestHeaders()).toReturn(reqHeaders);
		stub(this.uriInfo.getAbsolutePathBuilder()).toReturn(this.uriBuilder);
		when(this.uriInfo.getPathParameters()).thenReturn(uriParameters);
		when(this.uriInfo.getQueryParameters()).thenReturn(uriParameters);
		this.catalogResource.setUriInfo(this.uriInfo);
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		final BaseValidator baseValidator = PowerMockito
				.mock(BaseValidator.class);
		this.catalogResource.setBaseValidator(baseValidator);
		PowerMockito.when(baseValidator.isBaseValidationRequired("CATALOG", this.correlationId)).thenReturn(false);
		final Response actualResponseData;
		final Response expectedResponseData;
		this.catalogResource.setServiceManager(serviceManager);
		final HttpHeaders httpHeaders = PowerMockito.mock(HttpHeaders.class);
		PowerMockito.when(httpHeaders.getRequestHeaders()).thenReturn(
				this.reqHeaders);
		PowerMockito.mock(Response.class);
		final Catalog catalog = PowerMockito.mock(Catalog.class);

		final CatalogService catalogService = PowerMockito
				.mock(CatalogService.class);
		PowerMockito.when(serviceManager.getService()).thenReturn(
				catalogService);
		final Map<String, String> uriMap = TestCatalogResourceUtil.getUriMap();
		PowerMockito
		.when(catalogService.getCatalog(uriMap, this.correlationId))
		.thenReturn(catalog);
		PowerMockito.mockStatic(BaseResource.class);
		final String responseType = "XML";
		final BaseResource baseResource = PowerMockito.mock(BaseResource.class);
		PowerMockito.when(
				baseResource.getResponseTypeForURIParam(uriMap,
						this.correlationId)).thenReturn(responseType);
		final ResponseBuilder responseBuilder = new ResponseBuilderImpl();
		responseBuilder.status(404);
		expectedResponseData = responseBuilder.build();

		actualResponseData = this.catalogResource.getCatalogWithAllCategories(
				"catalogId", "XML", httpHeaders);
		assertEquals(actualResponseData.getStatus(),
				expectedResponseData.getStatus());
		assertNotNull(expectedResponseData.getStatus());
	}

	/**
	 * Test method for
	 * {@link com.belk.api.resource.CatalogResource#getCatalogWithSpecificCategory(String,String,String,HttpHeaders)}
	 * tests for exception
	 * 
	 * @throws ServiceException
	 *             ServiceException
	 * @throws AdapterException
	 *             AdapterException
	 * @throws BaseException
	 *             BaseException
	 */
	@Test(expected = Exception.class)
	public final void testGetCatalogWithSpecificCategoryStringStringStringString()
			throws ServiceException, AdapterException, BaseException {
		final MultivaluedMap<String, String> uriParameters = TestCatalogResourceUtil
				.createURIParametersMap();

		final MultivaluedMap<String, String> reqHeaders = TestCatalogResourceUtil
				.createRequestHeadersMap();
		this.httpHeaders = mock(HttpHeaders.class);
		// UriInfo and UriBuilder is mocked to get the instance of the UriInfo
		// which will be input for implementation.
		this.uriInfo = mock(UriInfo.class);
		this.uriBuilder = mock(UriBuilder.class);
		stub(this.httpHeaders.getRequestHeaders()).toReturn(reqHeaders);
		stub(this.uriInfo.getAbsolutePathBuilder()).toReturn(this.uriBuilder);
		when(this.uriInfo.getPathParameters()).thenReturn(uriParameters);
		when(this.uriInfo.getQueryParameters()).thenReturn(uriParameters);
		this.catalogResource.setUriInfo(this.uriInfo);
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		final BaseValidator baseValidator = PowerMockito
				.mock(BaseValidator.class);
		this.catalogResource.setBaseValidator(baseValidator);
		PowerMockito.when(baseValidator.isBaseValidationRequired("CATALOG", this.correlationId)).thenReturn(false);
		final Response actualResponseData;
		final Response expectedResponseData;
		this.catalogResource.setServiceManager(serviceManager);
		final HttpHeaders httpHeaders = PowerMockito.mock(HttpHeaders.class);
		PowerMockito.when(httpHeaders.getRequestHeaders()).thenReturn(
				this.reqHeaders);
		PowerMockito.mock(Response.class);
		final Catalog catalog = PowerMockito.mock(Catalog.class);

		final CatalogService catalogService = PowerMockito
				.mock(CatalogService.class);
		PowerMockito.when(serviceManager.getService()).thenReturn(
				catalogService);
		final Map<String, String> uriMap = TestCatalogResourceUtil.getUriMap();
		PowerMockito
		.when(catalogService.getCatalog(uriMap, this.correlationId))
		.thenReturn(catalog);
		PowerMockito.mockStatic(BaseResource.class);
		final String responseType = "XML";
		final BaseResource baseResource = PowerMockito.mock(BaseResource.class);
		PowerMockito.when(
				baseResource.getResponseTypeForURIParam(uriMap,
						this.correlationId)).thenReturn(responseType);
		final ResponseBuilder responseBuilder = new ResponseBuilderImpl();
		responseBuilder.status(404);
		expectedResponseData = responseBuilder.build();

		actualResponseData = this.catalogResource
				.getCatalogWithSpecificCategory("catalogId", "categoryId",
						"showproduct", "XML", httpHeaders);
		assertEquals(actualResponseData.getStatus(),
				expectedResponseData.getStatus());
		assertNotNull(expectedResponseData.getStatus());

	}

	/**
	 * Test method for
	 * {@link com.belk.api.resource.CatalogResource#updatePropertiesMap(HttpHeaders, UriInfo)}
	 * . Testing is done to check whether the Service object is obtained
	 * correctly
	 * 
	 * @throws Exception
	 *             - exception
	 */
	@Ignore
	@Test
	public final void testUpdatePropertiesMap() throws Exception {
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

		// ServiceManager is mocked to get the instance of the service.
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.catalogResource.setServiceManager(serviceManager);

		final BaseValidator baseValidator = PowerMockito.mock(BaseValidator.class);
		this.catalogResource.setBaseValidator(baseValidator);
		PowerMockito.when(baseValidator.updateValidationConfigurations(uriParameters, this.correlationId)).thenReturn(false);
		// CatalogService is mocked to get the instance of the service
		// and is passed
		// as return from call to service manager.
		final CatalogService catalogService = PowerMockito
				.mock(CatalogService.class);

		PowerMockito.when(serviceManager.getService()).thenReturn(
				catalogService);

		final ResponseBuilder responseBuilder = new ResponseBuilderImpl();
		responseBuilder.status(200);

		this.expectedResponseData = responseBuilder.build();

		this.actualResponseData = this.catalogResource.updatePropertiesMap(
				this.httpHeaders, this.uriInfo);

		assertEquals(this.expectedResponseData.getStatus(),
				this.actualResponseData.getStatus());
	}
	
	
}
