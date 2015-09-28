package com.belk.api.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
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
import com.belk.api.constants.CommonConstants;
import com.belk.api.exception.AdapterException;
import com.belk.api.exception.BaseException;
import com.belk.api.exception.ServiceException;
import com.belk.api.model.categoryproduct.Categories;
import com.belk.api.service.CategoryProductService;
import com.belk.api.transformer.xml.XMLProcessor;
import com.belk.api.util.ErrorLoader;
import com.belk.api.util.TestCategoryProductResourceUtil;
import com.belk.api.validators.BaseValidator;
import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * Junit test cases for CategoryProductResource
 * 
 * @author Mindtree
 * @date Oct 25, 2013
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ CategoryProductResource.class, ServiceManager.class,
		CategoryProductService.class, BaseResource.class, HttpHeaders.class, 
		BaseValidator.class, ErrorLoader.class, XMLProcessor.class })
public class TestCategoryProductResource {

	UriInfo uriInfo;
	URI uri = null;
	UriBuilder uriBuilder;
	Response responseData = null;
	Response actualSearchResults = null;
	HttpHeaders httpHeaders;
	String correlationId = "1234567890SDGFASF4545DFSERE";
	final String categoryId = "4294922263";
	MultivaluedMap<String, String> reqHeaders = new MultivaluedMapImpl();
	CategoryProductResource categoryProductResource = new CategoryProductResource();
	final String uriInfoPath = "v1/categories";
	Response expectedResponseData = null;
	Response actualResponseData = null;
	XMLProcessor xmlProcessor;

	/**
	 * Method which executes before each test
	 * 
	 * @throws URISyntaxException
	 *             - urisyntax exception
	 * @throws UnsupportedEncodingException
	 *             - unsupportedencoding exception
	 */

	@Before
	public final void setUp() throws URISyntaxException,
			UnsupportedEncodingException {
		final String url = "http://localhost:8080/CategoryProduct/v1/categories/4294922263/products";
		this.uri = new URI(URLEncoder.encode(url, "UTF-8"));
		this.reqHeaders.add(CommonConstants.CORRELATION_ID,
				"1234567890SDGFASF4545DFSERE");
		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		Map<String, String> errorPropertiesMap = new HashMap<String, String>();
		errorPropertiesMap.put("11523", "internal server error");
		errorLoader.setErrorPropertiesMap(errorPropertiesMap);
		categoryProductResource.setErrorLoader(errorLoader);
		BaseResource baseResource = PowerMockito.mock(BaseResource.class);
		baseResource.setErrorLoader(errorLoader);
		xmlProcessor =  PowerMockito.mock(XMLProcessor.class);
		xmlProcessor.setErrorLoader(errorLoader);
	}

	/**
	 * Junit test case to test getCategoryProducts method
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 */

	@Test
	(expected = WebApplicationException.class)
	public final void testGetCategoryProducts() throws BaseException,
			AdapterException {

		TestCategoryProductResourceUtil.createURIMap();
		final MultivaluedMap<String, String> uriParameters = TestCategoryProductResourceUtil
				.createURIParametersMap();

		final MultivaluedMap<String, String> reqHeaders = TestCategoryProductResourceUtil
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
		when(this.uriInfo.getPath()).thenReturn(this.uriInfoPath);

		// Mocking the BaseValidator Method
	    final BaseValidator baseValidator = PowerMockito.mock(BaseValidator.class);
		this.categoryProductResource.setBaseValidator(baseValidator);
		
		// ServiceManager is mocked to get the instance of the service.
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.categoryProductResource.setServiceManager(serviceManager);

		// CategoryProductService is mocked to get the instance of the service
		final CategoryProductService categoryProductService = PowerMockito
				.mock(CategoryProductService.class);

		PowerMockito.when(serviceManager.getService()).thenReturn(
				categoryProductService);

		Categories categoryProducts = null;
		// method searchCategoryProducts will return list of categories which
		// also contains the high level
		// product details
		PowerMockito.when(
				categoryProductService.getCategoryProducts(uriParameters,
						this.correlationId)).thenReturn(
								categoryProducts);

		// BaseResource is mocked to get the instance of the base resource.
		final BaseResource baseResource = PowerMockito.mock(BaseResource.class);

		final ResponseBuilder responseBuilder = new ResponseBuilderImpl();
		responseBuilder.status(200);

		// mocking call to method buildResponse and return responsebuilder
		// object
		PowerMockito.when(
				baseResource.buildResponse(categoryProducts, Categories.class,
						"categoryProduct-binding", "XML", this.correlationId))
				.thenReturn(responseBuilder);

		this.responseData = responseBuilder.build();
		this.categoryProductResource.setXmlProcessor(xmlProcessor);

		final Response actualSearchResults = this.categoryProductResource
				.getCategoryProducts(this.httpHeaders, this.categoryId,
						this.uriInfo);

		assertNotNull(actualSearchResults);
		assertNotNull(actualSearchResults.getEntity());
		assertNotNull(actualSearchResults.getStatus());

		assertEquals(200, actualSearchResults.getStatus());

	}

	/**
	 * Junit test case to test getCategoryProducts method for handling
	 * webapplication exception
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 */

	@Test
	(expected = WebApplicationException.class)
	public final void testGetCategoryProductsForWebApplicationException()
			throws BaseException, AdapterException {

		final Map<String, List<String>> uriMap = TestCategoryProductResourceUtil
				.createURIInvalidDataMap();
		final MultivaluedMap<String, String> uriParameters = TestCategoryProductResourceUtil
				.createURIParametersMap();

		final MultivaluedMap<String, String> reqHeaders = TestCategoryProductResourceUtil
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
		when(this.uriInfo.getPath()).thenReturn(this.uriInfoPath);

		// Mocking the BaseValidator Method
	    final BaseValidator baseValidator = PowerMockito.mock(BaseValidator.class);
		this.categoryProductResource.setBaseValidator(baseValidator);
		
		// ServiceManager is mocked to get the instance of the service.
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.categoryProductResource.setServiceManager(serviceManager);

		// CategoryProductService is mocked to get the instance of the service
		// and is passed
		// as return from call to service manager.
		final CategoryProductService categoryProductService = PowerMockito
				.mock(CategoryProductService.class);

		PowerMockito.when(serviceManager.getService()).thenReturn(
				categoryProductService);

		// method searchCategoryProducts will return list of categories
		// including the high level
		// product details
		PowerMockito.when(
				categoryProductService.getCategoryProducts(uriMap,
						this.correlationId)).thenReturn(
				TestCategoryProductResourceUtil.createCategoryProductResults());

		// BaseResource is mocked to get the instance of the base resource.
		final BaseResource baseResource = PowerMockito.mock(BaseResource.class);

		final ResponseBuilder responseBuilder = new ResponseBuilderImpl();
		responseBuilder.status(200);

		// mocking call to method buildResponse and return responsebuilder
		// object
		PowerMockito.when(
				baseResource.buildResponse(TestCategoryProductResourceUtil
						.createCategoryProductResults(), Categories.class,
						"categoryProduct-binding", "XML",
						reqHeaders.get(CommonConstants.CORRELATION_ID)
								.toString())).thenReturn(responseBuilder);

		this.responseData = responseBuilder.build();
		final Response actualSearchResults = this.categoryProductResource
				.getCategoryProducts(this.httpHeaders, this.categoryId,
						this.uriInfo);

		assertNotNull(actualSearchResults);
		assertNotNull(actualSearchResults.getEntity());
		assertNotNull(actualSearchResults.getStatus());
		assertNotNull(actualSearchResults.getMetadata());
		assertEquals(200, actualSearchResults.getStatus());

	}

	/**
	 * Junit test case to test getCategoryProducts method for handling Adapter
	 * Exception
	 * 
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 */

	@Test(expected = WebApplicationException.class)
	public final void testGetCategoryProductsForAdapterException()
			throws BaseException, AdapterException {

		TestCategoryProductResourceUtil.createURIMap();
		final MultivaluedMap<String, String> uriParameters = TestCategoryProductResourceUtil
				.createURIParametersMap();

		final MultivaluedMap<String, String> reqHeaders = TestCategoryProductResourceUtil
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
		when(this.uriInfo.getPath()).thenReturn(this.uriInfoPath);

		// Mocking the BaseValidator Method
	    final BaseValidator baseValidator = PowerMockito.mock(BaseValidator.class);
		this.categoryProductResource.setBaseValidator(baseValidator);
		
		// ServiceManager is mocked to get the instance of the service.
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.categoryProductResource.setServiceManager(serviceManager);

		// CategoryProductService is mocked to get the instance of the service
		// and is passed
		// as return from call to service manager.
		final CategoryProductService categoryProductService = PowerMockito
				.mock(CategoryProductService.class);

		PowerMockito.when(serviceManager.getService()).thenReturn(
				categoryProductService);

		// method searchCategoryProducts will return list of categories
		// including the high level
		// product details
		PowerMockito.when(
				categoryProductService.getCategoryProducts(uriParameters,
						this.correlationId)).thenThrow(
				new AdapterException("500", "Test JUnit Error", "parameter",
						"500", this.correlationId));

		// BaseResource is mocked to get the instance of the base resource.
		PowerMockito.mock(BaseResource.class);

		final Response actualSearchResults = this.categoryProductResource
				.getCategoryProducts(this.httpHeaders, this.categoryId,
						this.uriInfo);

		assertNotNull(actualSearchResults);
		assertNotNull(actualSearchResults.getEntity());
		assertNotNull(actualSearchResults.getStatus());
		assertNotNull(actualSearchResults.getMetadata());
		assertEquals(200, actualSearchResults.getStatus());

	}

	/**
	 * Junit test case to test getCategoryProducts method for handling
	 * BaseException
	 * 
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 */
	@Test
	(expected = WebApplicationException.class)
	public final void testGetCategoryProductsForBaseException()
			throws BaseException, AdapterException {

		final MultivaluedMap<String, String> uriParameters = TestCategoryProductResourceUtil
				.createURIParametersMap();

		final MultivaluedMap<String, String> reqHeaders = TestCategoryProductResourceUtil
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
		when(this.uriInfo.getPath()).thenReturn(this.uriInfoPath);

		// Mocking the BaseValidator Method
	    final BaseValidator baseValidator = PowerMockito.mock(BaseValidator.class);
		this.categoryProductResource.setBaseValidator(baseValidator);
		
		// ServiceManager is mocked to get the instance of the service.
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.categoryProductResource.setServiceManager(serviceManager);

		// CategoryProductService is mocked to get the instance of the service
		// and is passed
		// as return from call to service manager.
		final CategoryProductService categoryProductService = PowerMockito
				.mock(CategoryProductService.class);

		PowerMockito.when(serviceManager.getService()).thenReturn(
				categoryProductService);

		// method searchCategoryProducts will return list of categories
		// including the high level
		// product details
		PowerMockito.when(
				categoryProductService.getCategoryProducts(uriParameters,
						this.correlationId)).thenThrow(
				new BaseException("500", "Test JUnit Error", "parameter",
						"500", this.correlationId));

		// BaseResource is mocked to get the instance of the base resource.
		PowerMockito.mock(BaseResource.class);

		final Response actualSearchResults = this.categoryProductResource
				.getCategoryProducts(this.httpHeaders, this.categoryId,
						this.uriInfo);

		assertNotNull(actualSearchResults);
		assertNotNull(actualSearchResults.getEntity());
		assertNotNull(actualSearchResults.getStatus());
		assertNotNull(actualSearchResults.getMetadata());
		assertEquals(200, actualSearchResults.getStatus());

	}

	/**
	 * Junit test case to test getCategoryProducts method for handling
	 * BaseException
	 * 
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 */
	@Test
	(expected = WebApplicationException.class)
	public final void testGetCategoryProductsForValidationException()
			throws BaseException, AdapterException {

		TestCategoryProductResourceUtil.createURIMap();
		final MultivaluedMap<String, String> uriParameters = TestCategoryProductResourceUtil
				.createURIParametersMap();

		final MultivaluedMap<String, String> reqHeaders = TestCategoryProductResourceUtil
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
		when(this.uriInfo.getPath()).thenReturn(this.uriInfoPath);

		// Mocking the BaseValidator Method
	    final BaseValidator baseValidator = PowerMockito.mock(BaseValidator.class);
		this.categoryProductResource.setBaseValidator(baseValidator);
		
		// ServiceManager is mocked to get the instance of the service.
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.categoryProductResource.setServiceManager(serviceManager);

		// CategoryProductService is mocked to get the instance of the service
		// and is passed
		// as return from call to service manager.
		final CategoryProductService categoryProductService = PowerMockito
				.mock(CategoryProductService.class);

		PowerMockito.when(serviceManager.getService()).thenReturn(
				categoryProductService);

		// method searchCategoryProducts will return list of categories
		// including the high level
		// product details

		// BaseResource is mocked to get the instance of the base resource.
		PowerMockito.mock(BaseResource.class);

		final Response actualSearchResults = this.categoryProductResource
				.getCategoryProducts(this.httpHeaders, this.categoryId,
						this.uriInfo);

		assertNotNull(actualSearchResults);
		assertNotNull(actualSearchResults.getEntity());
		assertNotNull(actualSearchResults.getStatus());
		assertNotNull(actualSearchResults.getMetadata());
		assertEquals(200, actualSearchResults.getStatus());

	}

	/**
	 * Junit test case to test getCategoryProducts method for handling
	 * BaseException
	 * 
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 */
	@Test
	(expected = WebApplicationException.class)
	public final void testGetCategoryProductsForServiceException()
			throws BaseException, AdapterException {

		TestCategoryProductResourceUtil.createURIMap();
		final MultivaluedMap<String, String> uriParameters = TestCategoryProductResourceUtil
				.createURIParametersMap();

		final MultivaluedMap<String, String> reqHeaders = TestCategoryProductResourceUtil
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
		when(this.uriInfo.getPath()).thenReturn(this.uriInfoPath);

		// Mocking the BaseValidator Method
	    final BaseValidator baseValidator = PowerMockito.mock(BaseValidator.class);
		this.categoryProductResource.setBaseValidator(baseValidator);
		
		// ServiceManager is mocked to get the instance of the service.
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.categoryProductResource.setServiceManager(serviceManager);

		// CategoryProductService is mocked to get the instance of the service
		// and is passed
		// as return from call to service manager.
		final CategoryProductService categoryProductService = PowerMockito
				.mock(CategoryProductService.class);

		PowerMockito.when(serviceManager.getService()).thenReturn(
				categoryProductService);

		// method searchCategoryProducts will return list of categories
		// including the high level
		// product details
		PowerMockito.when(
				categoryProductService.getCategoryProducts(uriParameters,
						this.correlationId)).thenThrow(
				new ServiceException("500", "Test JUnit Error", "500",
						this.correlationId));

		// BaseResource is mocked to get the instance of the base resource.
		PowerMockito.mock(BaseResource.class);

		final Response actualSearchResults = this.categoryProductResource
				.getCategoryProducts(this.httpHeaders, this.categoryId,
						this.uriInfo);

		assertNotNull(actualSearchResults);
		assertNotNull(actualSearchResults.getEntity());
		assertNotNull(actualSearchResults.getStatus());
		assertNotNull(actualSearchResults.getMetadata());
		assertEquals(200, actualSearchResults.getStatus());

	}

	/**
	 * Test method for
	 * {@link com.belk.api.resource.CategoryProductResource#updatePropertiesMap(HttpHeaders, UriInfo)}
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
		uriInfo = mock(UriInfo.class);
		uriBuilder = mock(UriBuilder.class);
		httpHeaders = mock(HttpHeaders.class);

		when(httpHeaders.getRequestHeaders()).thenReturn(reqHeaders);
		stub(uriInfo.getAbsolutePathBuilder()).toReturn(uriBuilder);
		when(uriInfo.getQueryParameters()).thenReturn(uriParameters);
		when(uriInfo.getPath()).thenReturn(uriInfoPath);

		// ServiceManager is mocked to get the instance of the service.
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		categoryProductResource.setServiceManager(serviceManager);

		final BaseValidator baseValidator = PowerMockito.mock(BaseValidator.class);
		categoryProductResource.setBaseValidator(baseValidator);
		
		PowerMockito.when(baseValidator.updateValidationConfigurations(uriParameters, correlationId)).thenReturn(false);
		// CategoryProductService is mocked to get the instance of the service
		// and is passed
		// as return from call to service manager.
		final CategoryProductService categoryProductService = PowerMockito
				.mock(CategoryProductService.class);

		PowerMockito.when(serviceManager.getService()).thenReturn(
				categoryProductService);


		final ResponseBuilder responseBuilder = new ResponseBuilderImpl();
		responseBuilder.status(200);

		expectedResponseData = responseBuilder.build();

		actualResponseData = categoryProductResource.updatePropertiesMap(
				httpHeaders, uriInfo);

		assertEquals(expectedResponseData.getStatus(),
				actualResponseData.getStatus());
	}
}
