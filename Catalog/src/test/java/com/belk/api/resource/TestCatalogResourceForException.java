package com.belk.api.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.belk.api.business.service.manager.ServiceManager;
import com.belk.api.cache.CacheManager;
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
 * @author Mindtree Date Dec 13
 * 
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({ CatalogResource.class, ServiceManager.class,
		HttpHeaders.class, Response.class, Catalog.class, CatalogService.class,
		XMLProcessor.class, CacheManager.class })
public class TestCatalogResourceForException {

	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Catalog");
	{
		LoggerUtil.setLogger(LOGGER);
	}

	CatalogResource catalogResource = new CatalogResource();

	BaseResource baseResource = new BaseResource();

	CatalogServiceImpl catalogService = new CatalogServiceImpl();

	MultivaluedMap<String, String> reqHeaders = new MultivaluedMapImpl();

	/**
	 * 
	 * @throws Exception
	 *             throws Exception
	 */
	@Before
	public final void setUp() throws Exception {
		this.reqHeaders.add("Correlation-Id", "1234567891234567");
	}

	/**
	 * Test method for
	 * {@link com.belk.api.resource.CatalogResource#getCatalog(String,String,HttpHeaders)}
	 * tests for validation exception
	 * 
	 * @throws BaseException
	 *             BaseException
	 * @throws AdapterException
	 *             AdapterException
	 * @throws ServiceException
	 *             ServiceException
	 */
	@Test(expected = Exception.class)
	public final void testGetCatalogValidationException() throws BaseException,
			AdapterException, ServiceException {
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.catalogResource.setServiceManager(serviceManager);
		final Response actualResponseData;
		final Response expectedResponseData;
		final XMLProcessor xmlProcessor = new XMLProcessor();
		this.baseResource.setXmlProcessor(xmlProcessor);
		final HttpHeaders httpHeaders = PowerMockito.mock(HttpHeaders.class);
		PowerMockito.when(httpHeaders.getRequestHeaders()).thenReturn(
				this.reqHeaders);

		final JSONProcessor jsonProcessor = new JSONProcessor();
		this.catalogResource.setJsonProcessor(jsonProcessor);

		final ResponseBuilder responseBuilder = new ResponseBuilderImpl();
		responseBuilder.status(404);
		expectedResponseData = responseBuilder.build();

		final CacheManager cacheManager = PowerMockito.mock(CacheManager.class);
		this.catalogService.setCacheManager(cacheManager);

		actualResponseData = this.catalogResource.getCatalog("catalogid",
				"XML", httpHeaders);
		assertEquals(actualResponseData.getStatus(),
				expectedResponseData.getStatus());
		assertNotNull(expectedResponseData.getStatus());

	}

	/**
	 * Test method for
	 * {@link com.belk.api.resource.CatalogResource#getCatalogWithAllCategories(String,String,HttpHeaders)}
	 * tests for validation exception
	 * 
	 * @throws BaseException
	 *             BaseException
	 * @throws AdapterException
	 *             AdapterException
	 * @throws ServiceException
	 *             ServiceException
	 */
	@Test(expected = Exception.class)
	public final void testGetCatalogWithAllCategoriesForValidationException()
			throws BaseException, AdapterException, ServiceException {
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		final Response actualResponseData;
		final Response expectedResponseData;
		this.catalogResource.setServiceManager(serviceManager);
		final HttpHeaders httpHeaders = PowerMockito.mock(HttpHeaders.class);
		PowerMockito.when(httpHeaders.getRequestHeaders()).thenReturn(
				this.reqHeaders);

		final ResponseBuilder responseBuilder = new ResponseBuilderImpl();
		responseBuilder.status(404);
		expectedResponseData = responseBuilder.build();

		final CacheManager cacheManager = PowerMockito.mock(CacheManager.class);
		this.catalogService.setCacheManager(cacheManager);

		actualResponseData = this.catalogResource.getCatalogWithAllCategories(
				"catalogId", "XML", httpHeaders);
		assertEquals(actualResponseData.getStatus(),
				expectedResponseData.getStatus());
		assertNotNull(expectedResponseData.getStatus());
	}

	/**
	 * Test method for
	 * {@link com.belk.api.resource.CatalogResource#getCatalogWithSpecificCategory(String,String,String,HttpHeaders)}
	 * tests for validation exception
	 * 
	 * @throws ServiceException
	 *             ServiceException
	 * @throws AdapterException
	 *             AdapterException
	 * @throws BaseException
	 *             BaseException
	 */
	@Test(expected = Exception.class)
	public final void testGetCatalogWithSpecificCategoryStringStringString()
			throws ServiceException, AdapterException, BaseException {
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		final Response actualResponseData;
		final Response expectedResponseData;
		this.catalogResource.setServiceManager(serviceManager);
		final HttpHeaders httpHeaders = PowerMockito.mock(HttpHeaders.class);
		PowerMockito.when(httpHeaders.getRequestHeaders()).thenReturn(
				this.reqHeaders);

		final ResponseBuilder responseBuilder = new ResponseBuilderImpl();
		responseBuilder.status(404);
		expectedResponseData = responseBuilder.build();

		final CacheManager cacheManager = PowerMockito.mock(CacheManager.class);
		this.catalogService.setCacheManager(cacheManager);

		actualResponseData = this.catalogResource
				.getCatalogWithSpecificCategory("catalogId", "12345678",
						"showproduct", "XML", httpHeaders);
		
		assertEquals(actualResponseData.getStatus(),
				expectedResponseData.getStatus());
		assertNotNull(expectedResponseData.getStatus());

	}

	/**
	 * Test method for
	 * {@link com.belk.api.resource.CatalogResource#getCatalogWithSpecificCategory(String,String,String,String,HttpHeaders)}
	 * tests for exception
	 * 
	 * @throws BaseException
	 *             baseException
	 */
	@Test(expected = Exception.class)
	public final void testGetCatalogWithSpecificCategoryStringStringStringString()
			throws BaseException {
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		final Response actualResponseData;
		final Response expectedResponseData;
		this.catalogResource.setServiceManager(serviceManager);
		final HttpHeaders httpHeaders = PowerMockito.mock(HttpHeaders.class);
		PowerMockito.when(httpHeaders.getRequestHeaders()).thenReturn(
				this.reqHeaders);

		PowerMockito.when(serviceManager.getService()).thenReturn(
				this.catalogService);

		final ResponseBuilder responseBuilder = new ResponseBuilderImpl();
		responseBuilder.status(404);
		expectedResponseData = responseBuilder.build();

		final CacheManager cacheManager = PowerMockito.mock(CacheManager.class);
		this.catalogService.setCacheManager(cacheManager);

		actualResponseData = this.catalogResource
				.getCatalogWithSpecificCategory("catalogId", "12345678",
						"showproduct", "XML", httpHeaders);
		assertEquals(actualResponseData.getStatus(),
				expectedResponseData.getStatus());
		assertNotNull(expectedResponseData.getStatus());

	}
}
