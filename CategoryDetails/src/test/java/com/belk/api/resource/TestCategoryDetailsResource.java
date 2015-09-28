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

import javax.ws.rs.WebApplicationException;
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
import com.belk.api.exception.AdapterException;
import com.belk.api.exception.BaseException;
import com.belk.api.exception.ServiceException;
import com.belk.api.exception.ValidationException;
import com.belk.api.model.categorydetails.Categories;
import com.belk.api.service.CategoryDetailsService;
import com.belk.api.transformer.xml.XMLProcessor;
import com.belk.api.util.ErrorLoader;
import com.belk.api.util.TestCategoryDetailsResourceUtil;
import com.belk.api.validators.BaseValidator;
import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * Unit Testing related to CategoryDetailsResource class is performed. <br />
 * {@link CategoryDetailsResource} class is written for testing methods in
 * {@link CategoryDetailsResource} The unit test cases evaluates the way the
 * methods behave for the inputs given.
 * 
 * <br />
 * The test cases associated with CatalogServiceImpl is written to make the code
 * intactv by assuring the code test.
 * 
 * 
 * @author Mindtree
 * @date Oct 25, 2013
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ CategoryDetailsResource.class, ServiceManager.class,
	CategoryDetailsService.class, BaseResource.class, UriInfo.class,
	HttpHeaders.class, BaseValidator.class, ErrorLoader.class })
public class TestCategoryDetailsResource {

	HttpHeaders httpHeaders;
	UriInfo uriInfo;
	URI uri = null;
	UriBuilder uriBuilder;
	Response responseData = null;
	Response actualSearchResults = null;
	final String categoryId = "4294922263";
	final String correlationId = "1234567890SDGFASF4545DFSERE";
	final MultivaluedMap<String, String> reqHeaders = new MultivaluedMapImpl();
	CategoryDetailsResource categoryDetailsResource = new CategoryDetailsResource();
	final String uriInfoPath = "v1/categories";
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
	}

	/**
	 * Junit test case to test getCategoryDetails method
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 * @throws ValidationException
	 *             - exception thrown from validation layer
	 * @throws ServiceException
	 *             exception thrown from Service layer
	 * 
	 */

	@Test(expected = WebApplicationException.class)
	public final void testGetCategoryDetails() throws BaseException,
	AdapterException, ValidationException, ServiceException {
		final Map<String, List<String>> uriMap = TestCategoryDetailsResourceUtil
				.createURIMap();
		final MultivaluedMap<String, String> uriParameters = TestCategoryDetailsResourceUtil
				.createURIParametersMap();
		this.httpHeaders = mock(HttpHeaders.class);
		this.uriInfo = mock(UriInfo.class);
		this.uriBuilder = mock(UriBuilder.class);
		when(this.httpHeaders.getRequestHeaders()).thenReturn(this.reqHeaders);
		stub(this.uriInfo.getAbsolutePathBuilder()).toReturn(this.uriBuilder);
		when(this.uriInfo.getQueryParameters()).thenReturn(uriParameters);
		when(this.uriInfo.getPath()).thenReturn(this.uriInfoPath);

		// Mocking the BaseValidator Method
		final BaseValidator baseValidator = PowerMockito
				.mock(BaseValidator.class);
		this.categoryDetailsResource.setBaseValidator(baseValidator);
		PowerMockito.when(baseValidator.isBaseValidationRequired("CATEGORY_DETAILS", this.correlationId)).thenReturn(true);
		// ServiceManager is mocked to get the instance of the service.
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.categoryDetailsResource.setServiceManager(serviceManager);

		// CategoryDetailsService is mocked to get the instance of the service
		final CategoryDetailsService categoryDetailsService = PowerMockito
				.mock(CategoryDetailsService.class);
		PowerMockito.when(serviceManager.getService()).thenReturn(
				categoryDetailsService);
		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		Map<String, String> errorPropertiesMap = TestCategoryDetailsResourceUtil.getErrorPropertiesMap();
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(errorPropertiesMap);
		this.categoryDetailsResource.setErrorLoader(errorLoader);
		BaseResource baseResourceMock = PowerMockito.mock(BaseResource.class);
		baseResourceMock.setErrorLoader(errorLoader);
		// method getCategoryDetails will return list of categories which
		// also contains the high level
		// product details
		Categories categories = null;
		PowerMockito.when(
				categoryDetailsService.getCategoryDetails(uriParameters,
						this.correlationId)).thenReturn(
								categories);

		// BaseResource is mocked to get the instance of the base resource.
		final BaseResource baseResource = PowerMockito.mock(BaseResource.class);

		final ResponseBuilder responseBuilder = new ResponseBuilderImpl();
		responseBuilder.status(200);

		// mocking call to method buildResponse and return responsebuilder
		// object
		PowerMockito.when(
				baseResource.buildResponse(TestCategoryDetailsResourceUtil
						.createCategoryDetailsResults(), Categories.class,
						"categoryDetails-binding", "XML", this.correlationId))
						.thenReturn(responseBuilder);

		this.responseData = responseBuilder.build();

		final XMLProcessor xmlProcessor = new XMLProcessor();
		this.categoryDetailsResource.setXmlProcessor(xmlProcessor);

		final Response actualSearchResults = this.categoryDetailsResource
				.getCategoryDetails(this.httpHeaders, this.categoryId,
						this.uriInfo);

		assertNotNull(actualSearchResults);

		assertNotNull(actualSearchResults.getStatus());
		assertNotNull(actualSearchResults.getMetadata());
		assertEquals(200, actualSearchResults.getStatus());

	}

	/**
	 * Junit test case to test getCategoryDetails method Adapter Exception
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 * @throws ValidationException
	 *             exception thrown from validation layer
	 * @throws ServiceException
	 *             exception thrown from Service layer
	 */
	@Test
	(expected = WebApplicationException.class)
	public final void testGetCategoryDetailsAdapterException()
			throws BaseException, AdapterException, ValidationException,
			ServiceException {

		final MultivaluedMap<String, String> uriParameters = TestCategoryDetailsResourceUtil
				.createURIParametersMap();
		final MultivaluedMap<String, String> reqHeaders = TestCategoryDetailsResourceUtil
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
		this.categoryDetailsResource.setBaseValidator(baseValidator);
		PowerMockito.when(baseValidator.isBaseValidationRequired("CATEGORY_DETAILS", this.correlationId)).thenReturn(true);
		// ServiceManager is mocked to get the instance of the service.
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.categoryDetailsResource.setServiceManager(serviceManager);

		// CategoryDetailsService is mocked to get the instance of the service
		final CategoryDetailsService categoryDetailsService = PowerMockito
				.mock(CategoryDetailsService.class);

		PowerMockito.when(serviceManager.getService()).thenReturn(
				categoryDetailsService);
		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		Map<String, String> errorPropertiesMap = TestCategoryDetailsResourceUtil.getErrorPropertiesMap();
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(errorPropertiesMap);
		this.categoryDetailsResource.setErrorLoader(errorLoader);
		BaseResource baseResourceMock = PowerMockito.mock(BaseResource.class);
		baseResourceMock.setErrorLoader(errorLoader);
		// method getCategoryDetails will return list of categories which
		// also contains the high level
		// product details
		PowerMockito.when(
				categoryDetailsService.getCategoryDetails(uriParameters,
						this.correlationId)).thenThrow(
								new AdapterException("500", "Test JUnit Error", "500",
										this.correlationId));

		// BaseResource is mocked to get the instance of the base resource.
		PowerMockito.mock(BaseResource.class);

		final Response actualSearchResults = this.categoryDetailsResource
				.getCategoryDetails(this.httpHeaders, this.categoryId,
						this.uriInfo);

		assertNotNull(actualSearchResults);
		assertNotNull(actualSearchResults.getStatus());
		assertNotNull(actualSearchResults.getMetadata());
		assertEquals(404, actualSearchResults.getStatus());

	}

	/**
	 * Junit test case to test getCategoryDetails method Base Exception
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 * @throws ValidationException
	 *             exception thrown from validation layer
	 * @throws ServiceException
	 *             exception thrown from Service layer
	 * 
	 */
	@Test
	(expected = WebApplicationException.class)
	public final void testGetCategoryDetailsBaseException()
			throws BaseException, AdapterException, ValidationException,
			ServiceException {

		TestCategoryDetailsResourceUtil.createURIMap();
		final MultivaluedMap<String, String> uriParameters = TestCategoryDetailsResourceUtil
				.createURIParametersMap();

		final MultivaluedMap<String, String> reqHeaders = TestCategoryDetailsResourceUtil
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
		this.categoryDetailsResource.setBaseValidator(baseValidator);
		PowerMockito.when(baseValidator.isBaseValidationRequired("CATEGORY_DETAILS", this.correlationId)).thenReturn(true);
		// ServiceManager is mocked to get the instance of the service.
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.categoryDetailsResource.setServiceManager(serviceManager);

		// CategoryDetailsService is mocked to get the instance of the service
		final CategoryDetailsService categoryDetailsService = PowerMockito
				.mock(CategoryDetailsService.class);

		PowerMockito.when(serviceManager.getService()).thenReturn(
				categoryDetailsService);
		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		Map<String, String> errorPropertiesMap = TestCategoryDetailsResourceUtil.getErrorPropertiesMap();
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(errorPropertiesMap);
		this.categoryDetailsResource.setErrorLoader(errorLoader);
		// method getCategoryDetails will return list of categories which
		// also contains the high level
		// product details
		PowerMockito.when(
				categoryDetailsService.getCategoryDetails(uriParameters,
						this.correlationId)).thenThrow(
								new BaseException("500", this.correlationId));

		// BaseResource is mocked to get the instance of the base resource.
		BaseResource baseResourceMock = PowerMockito.mock(BaseResource.class);
		baseResourceMock.setErrorLoader(errorLoader);
		final Response actualSearchResults = this.categoryDetailsResource
				.getCategoryDetails(this.httpHeaders, this.categoryId,
						this.uriInfo);

		assertNotNull(actualSearchResults);
		assertNotNull(actualSearchResults.getStatus());
		assertNotNull(actualSearchResults.getMetadata());
		assertEquals(404, actualSearchResults.getStatus());

	}

	/**
	 * Junit test case to test getCategoryDetails method Service Exception
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 * @throws ValidationException
	 *             exception thrown from validation layer
	 * @throws ServiceException
	 *             exception thrown from Service layer
	 * 
	 */
	@Test
	(expected = WebApplicationException.class)
	public final void testGetCategoryDetailsServiceException()
			throws BaseException, AdapterException, ValidationException,
			ServiceException {

		TestCategoryDetailsResourceUtil.createURIMap();
		final MultivaluedMap<String, String> uriParameters = TestCategoryDetailsResourceUtil
				.createURIParametersMap();
		final MultivaluedMap<String, String> reqHeaders = TestCategoryDetailsResourceUtil
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
		this.categoryDetailsResource.setBaseValidator(baseValidator);
		PowerMockito.when(baseValidator.isBaseValidationRequired("CATEGORY_DETAILS", this.correlationId)).thenReturn(true);
		// ServiceManager is mocked to get the instance of the service.
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.categoryDetailsResource.setServiceManager(serviceManager);
		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		Map<String, String> errorPropertiesMap = TestCategoryDetailsResourceUtil.getErrorPropertiesMap();
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(errorPropertiesMap);
		this.categoryDetailsResource.setErrorLoader(errorLoader);
		BaseResource baseResourceMock = PowerMockito.mock(BaseResource.class);
		baseResourceMock.setErrorLoader(errorLoader);
		// CategoryDetailsService is mocked to get the instance of the service
		final CategoryDetailsService categoryDetailsService = PowerMockito
				.mock(CategoryDetailsService.class);

		PowerMockito.when(serviceManager.getService()).thenReturn(
				categoryDetailsService);

		// method getCategoryDetails will return list of categories which
		// also contains the high level
		// product details
		PowerMockito.when(
				categoryDetailsService.getCategoryDetails(uriParameters,
						this.correlationId)).thenThrow(
								new ServiceException("500", "Test JUnit Error", "500",
										this.correlationId));

		// BaseResource is mocked to get the instance of the base resource.
		PowerMockito.mock(BaseResource.class);

		final Response actualSearchResults = this.categoryDetailsResource
				.getCategoryDetails(this.httpHeaders, this.categoryId,
						this.uriInfo);

		assertNotNull(actualSearchResults);
		assertNotNull(actualSearchResults.getStatus());
		assertNotNull(actualSearchResults.getMetadata());
		assertEquals(404, actualSearchResults.getStatus());

	}

	/**
	 * Junit test case to test getCategoryDetails method Validatation Exception
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 * @throws ValidationException
	 *             exception thrown from validation layer
	 * @throws ServiceException
	 *             exception thrown from Service layer
	 * 
	 */
	@Test
	(expected = WebApplicationException.class)
	public final void testGetCategoryDetailsValidationException()
			throws BaseException, AdapterException, ValidationException,
			ServiceException {

		final MultivaluedMap<String, String> uriParameters = TestCategoryDetailsResourceUtil
				.createURIParametersMap();

		final MultivaluedMap<String, String> reqHeaders = TestCategoryDetailsResourceUtil
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
		this.categoryDetailsResource.setBaseValidator(baseValidator);
		PowerMockito.when(baseValidator.isBaseValidationRequired("CATEGORY_DETAILS", this.correlationId)).thenReturn(true);
		// ServiceManager is mocked to get the instance of the service.
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.categoryDetailsResource.setServiceManager(serviceManager);

		// CategoryDetailsService is mocked to get the instance of the service
		final CategoryDetailsService categoryDetailsService = PowerMockito
				.mock(CategoryDetailsService.class);

		PowerMockito.when(serviceManager.getService()).thenReturn(
				categoryDetailsService);
		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		Map<String, String> errorPropertiesMap = TestCategoryDetailsResourceUtil.getErrorPropertiesMap();
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(errorPropertiesMap);
		this.categoryDetailsResource.setErrorLoader(errorLoader);
		BaseResource baseResourceMock = PowerMockito.mock(BaseResource.class);
		baseResourceMock.setErrorLoader(errorLoader);
		// method getCategoryDetails will return list of categories which
		// also contains the high level
		// product details
		PowerMockito.when(
				categoryDetailsService.getCategoryDetails(uriParameters,
						this.correlationId)).thenThrow(
								new ValidationException("500", "Test JUnit Error", "500",
										this.correlationId));

		// BaseResource is mocked to get the instance of the base resource.
		PowerMockito.mock(BaseResource.class);

		final Response actualSearchResults = this.categoryDetailsResource
				.getCategoryDetails(this.httpHeaders, this.categoryId,
						this.uriInfo);

		assertNotNull(actualSearchResults);
		assertNotNull(actualSearchResults.getStatus());
		assertNotNull(actualSearchResults.getMetadata());
		assertEquals(404, actualSearchResults.getStatus());

	}

	/**
	 * Junit test case to test getSubCategoryDetails method
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 * @throws ValidationException
	 *             exception thrown from validation layer
	 * @throws ServiceException
	 *             exception thrown from Service layer
	 * 
	 */

	@Test(expected = WebApplicationException.class)
	public final void testGetSubCategoryDetails() throws BaseException,
	AdapterException, ValidationException, ServiceException {

		final Map<String, List<String>> uriMap = TestCategoryDetailsResourceUtil
				.createURIMap();
		final MultivaluedMap<String, String> uriParameters = TestCategoryDetailsResourceUtil
				.createURIParametersMap();

		this.httpHeaders = mock(HttpHeaders.class);
		// UriInfo and UriBuilder is mocked to get the instance of the UriInfo
		// which will be input for implementation.
		this.uriInfo = mock(UriInfo.class);
		this.uriBuilder = mock(UriBuilder.class);

		stub(this.httpHeaders.getRequestHeaders()).toReturn(this.reqHeaders);
		stub(this.uriInfo.getAbsolutePathBuilder()).toReturn(this.uriBuilder);
		when(this.uriInfo.getQueryParameters()).thenReturn(uriParameters);
		when(this.uriInfo.getPath()).thenReturn(this.uriInfoPath);

		// Mocking the BaseValidator Method
		final BaseValidator baseValidator = PowerMockito.mock(BaseValidator.class);
		this.categoryDetailsResource.setBaseValidator(baseValidator);
		PowerMockito.when(baseValidator.isBaseValidationRequired("CATEGORY_DETAILS", this.correlationId)).thenReturn(true);
		// ServiceManager is mocked to get the instance of the service.
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.categoryDetailsResource.setServiceManager(serviceManager);
		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		Map<String, String> errorPropertiesMap = TestCategoryDetailsResourceUtil.getErrorPropertiesMap();
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(errorPropertiesMap);
		this.categoryDetailsResource.setErrorLoader(errorLoader);
		// CategoryDetailsService is mocked to get the instance of the service
		final CategoryDetailsService categoryDetailsService = PowerMockito
				.mock(CategoryDetailsService.class);

		PowerMockito.when(serviceManager.getService()).thenReturn(
				categoryDetailsService);

		// method getCategoryDetails will return list of categories which
		// also contains the high level
		// product details
		Categories categories = null;
		PowerMockito.when(
				categoryDetailsService.getCategoryDetails(uriParameters,
						this.correlationId)).thenReturn(
								categories);

		// BaseResource is mocked to get the instance of the base resource.
		final BaseResource baseResource = PowerMockito.mock(BaseResource.class);
		baseResource.setErrorLoader(errorLoader);
		final ResponseBuilder responseBuilder = new ResponseBuilderImpl();
		responseBuilder.status(200);

		// mocking call to method buildResponse and return responsebuilder
		// object
		PowerMockito.when(
				baseResource.buildResponse(TestCategoryDetailsResourceUtil
						.createCategoryDetailsResults(), Categories.class,
						"categoryDetails-binding", "XML",
						this.reqHeaders.get(CommonConstants.CORRELATION_ID)
						.toString())).thenReturn(responseBuilder);

		this.responseData = responseBuilder.build();

		final XMLProcessor xmlProcessor = new XMLProcessor();
		this.categoryDetailsResource.setXmlProcessor(xmlProcessor);

		final Response actualSearchResults = this.categoryDetailsResource
				.getSubCategoryDetails(this.httpHeaders, this.categoryId,
						this.uriInfo);

		assertNotNull(actualSearchResults);

		assertNotNull(actualSearchResults.getStatus());
		assertNotNull(actualSearchResults.getMetadata());
		assertEquals(200, actualSearchResults.getStatus());

	}

	/**
	 * Junit test case to test getSubCategoryDetails method Validatation
	 * Exception
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 * @throws ValidationException
	 *             exception thrown from validation layer
	 * @throws ServiceException
	 *             exception thrown from Service layer
	 * 
	 */

	@Test
	(expected = WebApplicationException.class)
	public final void testGetSubCategoryDetailsValidatationException()
			throws BaseException, AdapterException, ValidationException,
			ServiceException {

		final MultivaluedMap<String, String> uriParameters = TestCategoryDetailsResourceUtil
				.createURIParametersMap();

		final MultivaluedMap<String, String> reqHeaders = TestCategoryDetailsResourceUtil
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
		this.categoryDetailsResource.setBaseValidator(baseValidator);
		PowerMockito.when(baseValidator.isBaseValidationRequired("CATEGORY_DETAILS", this.correlationId)).thenReturn(true);
		// ServiceManager is mocked to get the instance of the service.
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.categoryDetailsResource.setServiceManager(serviceManager);

		// CategoryDetailsService is mocked to get the instance of the service
		final CategoryDetailsService categoryDetailsService = PowerMockito
				.mock(CategoryDetailsService.class);

		PowerMockito.when(serviceManager.getService()).thenReturn(
				categoryDetailsService);

		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		Map<String, String> errorPropertiesMap = TestCategoryDetailsResourceUtil.getErrorPropertiesMap();
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(errorPropertiesMap);
		this.categoryDetailsResource.setErrorLoader(errorLoader);
		BaseResource baseResourceMock = PowerMockito.mock(BaseResource.class);
		baseResourceMock.setErrorLoader(errorLoader);
		// method getCategoryDetails will return list of categories which
		// also contains the high level
		// product details
		PowerMockito.when(
				categoryDetailsService.getCategoryDetails(uriParameters,
						this.correlationId)).thenThrow(
								new ValidationException("500", "Test JUnit Error", "500",
										this.correlationId));

		// BaseResource is mocked to get the instance of the base resource.


		final Response actualSearchResults = this.categoryDetailsResource
				.getSubCategoryDetails(this.httpHeaders, this.categoryId,
						this.uriInfo);

		assertNotNull(actualSearchResults);
		assertNotNull(actualSearchResults.getStatus());
		assertNotNull(actualSearchResults.getMetadata());
		assertEquals(404, actualSearchResults.getStatus());

	}

	/**
	 * Junit test case to test getSubCategoryDetails method Adapter Exception
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 * @throws ValidationException
	 *             exception thrown from validation layer
	 * @throws ServiceException
	 *             exception thrown from Service layer
	 * 
	 */

	@Test
	(expected = WebApplicationException.class)
	public final void testGetSubCategoryDetailsAdapterException()
			throws BaseException, AdapterException, ValidationException,
			ServiceException {

		TestCategoryDetailsResourceUtil.createURIMap();
		final MultivaluedMap<String, String> uriParameters = TestCategoryDetailsResourceUtil
				.createURIParametersMap();

		final MultivaluedMap<String, String> reqHeaders = TestCategoryDetailsResourceUtil
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
		this.categoryDetailsResource.setBaseValidator(baseValidator);
		PowerMockito.when(baseValidator.isBaseValidationRequired("CATEGORY_DETAILS", this.correlationId)).thenReturn(true);
		// ServiceManager is mocked to get the instance of the service.
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.categoryDetailsResource.setServiceManager(serviceManager);

		// CategoryDetailsService is mocked to get the instance of the service
		final CategoryDetailsService categoryDetailsService = PowerMockito
				.mock(CategoryDetailsService.class);

		PowerMockito.when(serviceManager.getService()).thenReturn(
				categoryDetailsService);
		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		Map<String, String> errorPropertiesMap = TestCategoryDetailsResourceUtil.getErrorPropertiesMap();
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(errorPropertiesMap);
		this.categoryDetailsResource.setErrorLoader(errorLoader);
		BaseResource baseResourceMock = PowerMockito.mock(BaseResource.class);
		baseResourceMock.setErrorLoader(errorLoader);
		// method getCategoryDetails will return list of categories which
		// also contains the high level
		// product details
		PowerMockito.when(
				categoryDetailsService.getCategoryDetails(uriParameters,
						this.correlationId)).thenThrow(
								new AdapterException("500", "Test JUnit Error", "500",
										this.correlationId));

		// BaseResource is mocked to get the instance of the base resource.


		final Response actualSearchResults = this.categoryDetailsResource
				.getSubCategoryDetails(this.httpHeaders, this.categoryId,
						this.uriInfo);

		assertNotNull(actualSearchResults);
		assertNotNull(actualSearchResults.getStatus());
		assertNotNull(actualSearchResults.getMetadata());
		assertEquals(404, actualSearchResults.getStatus());

	}

	/**
	 * Junit test case to test getSubCategoryDetails method BaseException
	 * Exception
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 * @throws ValidationException
	 *             exception thrown from validation layer
	 * @throws ServiceException
	 *             exception thrown from Service layer
	 * 
	 */

	@Test
	(expected = WebApplicationException.class)
	public final void testGetSubCategoryDetailsBaseException()
			throws BaseException, AdapterException, ValidationException,
			ServiceException {

		TestCategoryDetailsResourceUtil.createURIMap();
		final MultivaluedMap<String, String> uriParameters = TestCategoryDetailsResourceUtil
				.createURIParametersMap();

		final MultivaluedMap<String, String> reqHeaders = TestCategoryDetailsResourceUtil
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
		this.categoryDetailsResource.setBaseValidator(baseValidator);
		PowerMockito.when(baseValidator.isBaseValidationRequired("CATEGORY_DETAILS", this.correlationId)).thenReturn(true);
		// ServiceManager is mocked to get the instance of the service.
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.categoryDetailsResource.setServiceManager(serviceManager);

		// CategoryDetailsService is mocked to get the instance of the service
		final CategoryDetailsService categoryDetailsService = PowerMockito
				.mock(CategoryDetailsService.class);

		PowerMockito.when(serviceManager.getService()).thenReturn(
				categoryDetailsService);
		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		Map<String, String> errorPropertiesMap = TestCategoryDetailsResourceUtil.getErrorPropertiesMap();
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(errorPropertiesMap);
		this.categoryDetailsResource.setErrorLoader(errorLoader);
		BaseResource baseResourceMock = PowerMockito.mock(BaseResource.class);
		baseResourceMock.setErrorLoader(errorLoader);
		// method getCategoryDetails will return list of categories which
		// also contains the high level
		// product details
		PowerMockito.when(
				categoryDetailsService.getCategoryDetails(uriParameters,
						this.correlationId)).thenThrow(
								new BaseException("500", "Test JUnit Error"));

		// BaseResource is mocked to get the instance of the base resource.


		final Response actualSearchResults = this.categoryDetailsResource
				.getSubCategoryDetails(this.httpHeaders, this.categoryId,
						this.uriInfo);

		assertNotNull(actualSearchResults);
		assertNotNull(actualSearchResults.getStatus());
		assertNotNull(actualSearchResults.getMetadata());
		assertEquals(404, actualSearchResults.getStatus());

	}

	/**
	 * Junit test case to test getSubCategoryDetails method Service Exception
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 * @throws ValidationException
	 *             exception thrown from validation layer
	 * @throws ServiceException
	 *             exception thrown from Service layer
	 * 
	 */
	@Test
	(expected = WebApplicationException.class)
	public final void testGetSubCategoryDetailserviceException()
			throws BaseException, AdapterException, ValidationException,
			ServiceException {

		final MultivaluedMap<String, String> uriParameters = TestCategoryDetailsResourceUtil
				.createURIParametersMap();
		final MultivaluedMap<String, String> reqHeaders = TestCategoryDetailsResourceUtil
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
		this.categoryDetailsResource.setBaseValidator(baseValidator);
		PowerMockito.when(baseValidator.isBaseValidationRequired("CATEGORY_DETAILS", this.correlationId)).thenReturn(true);
		// ServiceManager is mocked to get the instance of the service.
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.categoryDetailsResource.setServiceManager(serviceManager);

		// CategoryDetailsService is mocked to get the instance of the service
		final CategoryDetailsService categoryDetailsService = PowerMockito
				.mock(CategoryDetailsService.class);

		PowerMockito.when(serviceManager.getService()).thenReturn(
				categoryDetailsService);
		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		Map<String, String> errorPropertiesMap = TestCategoryDetailsResourceUtil.getErrorPropertiesMap();
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(errorPropertiesMap);
		this.categoryDetailsResource.setErrorLoader(errorLoader);
		BaseResource baseResourceMock = PowerMockito.mock(BaseResource.class);
		baseResourceMock.setErrorLoader(errorLoader);
		// method getCategoryDetails will return list of categories which
		// also contains the high level
		// product details
		PowerMockito.when(
				categoryDetailsService.getCategoryDetails(uriParameters,
						this.correlationId)).thenThrow(
								new ServiceException("500", "Test JUnit Error", "500",
										this.correlationId));

		// BaseResource is mocked to get the instance of the base resource.


		final Response actualSearchResults = this.categoryDetailsResource
				.getSubCategoryDetails(this.httpHeaders, this.categoryId,
						this.uriInfo);

		assertNotNull(actualSearchResults);
		assertNotNull(actualSearchResults.getStatus());
		assertNotNull(actualSearchResults.getMetadata());
		assertEquals(404, actualSearchResults.getStatus());

	}

	/**
	 * Test method for
	 * {@link com.belk.api.resource.CategoryDetailsResource#updatePropertiesMap(HttpHeaders, UriInfo)}
	 * . Testing is done to check whether the Service object is obtained
	 * correctly
	 * 
	 * @throws Exception
	 *             - exception
	 */

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
		final BaseValidator baseValidator = PowerMockito.mock(BaseValidator.class);
		this.categoryDetailsResource.setBaseValidator(baseValidator);
		PowerMockito.when(baseValidator.isBaseValidationRequired("CATEGORY_DETAILS", this.correlationId)).thenReturn(true);
		// ServiceManager is mocked to get the instance of the service.
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		categoryDetailsResource.setServiceManager(serviceManager);
		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		Map<String, String> errorPropertiesMap = TestCategoryDetailsResourceUtil.getErrorPropertiesMap();
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(errorPropertiesMap);
		this.categoryDetailsResource.setErrorLoader(errorLoader);
		BaseResource baseResourceMock = PowerMockito.mock(BaseResource.class);
		baseResourceMock.setErrorLoader(errorLoader);
		PowerMockito.when(
				baseResourceMock.validateRequest(uriParameters,
						this.correlationId)).thenReturn(true);
		PowerMockito.doNothing().when(baseResourceMock, "processException",
				new BaseException(), "xml", this.correlationId);
		// CategoryDetailsService is mocked to get the instance of the service
		// and is passed
		// as return from call to service manager.
		final CategoryDetailsService categoryDetailsService = PowerMockito
				.mock(CategoryDetailsService.class);

		PowerMockito.when(serviceManager.getService()).thenReturn(
				categoryDetailsService);


		final ResponseBuilder responseBuilder = new ResponseBuilderImpl();
		responseBuilder.status(200);

		expectedResponseData = responseBuilder.build();

		actualResponseData = categoryDetailsResource.updatePropertiesMap(
				httpHeaders, uriInfo);

		/*assertEquals(expectedResponseData.getStatus(),
				actualResponseData.getStatus());*/
	}
}
