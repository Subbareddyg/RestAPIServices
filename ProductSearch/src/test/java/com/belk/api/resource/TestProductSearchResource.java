package com.belk.api.resource;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
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
import com.belk.api.exception.AdapterException;
import com.belk.api.exception.BaseException;
import com.belk.api.exception.ServiceException;
import com.belk.api.model.productsearch.Search;
import com.belk.api.service.ProductSearchService;
import com.belk.api.transformer.json.JSONProcessor;
import com.belk.api.transformer.xml.XMLProcessor;
import com.belk.api.util.ErrorLoader;
import com.belk.api.util.TestProductSearchUtil;
import com.belk.api.validators.BaseValidator;
import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * Unit Testing related to ProductSearchResource class is performed. <br />
 * {@link TestProductSearchResource} class is written for testing methods in
 * {@link ProductSearchResource} The unit test cases evaluates the way the
 * methods behave for the inputs given.
 * 
 * 
 * @author Mindtree
 * @date Oct 25, 2013
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ ProductSearchResource.class, BaseValidator.class,
		UriInfo.class, UriBuilder.class, HttpHeaders.class,
		ServiceManager.class, ProductSearchService.class, BaseResource.class, ErrorLoader.class, XMLProcessor.class, Search.class })
public class TestProductSearchResource {

	HttpHeaders httpHeaders;
	UriInfo uriInfo;
	URI uri = null;
	UriBuilder uriBuilder;
	Response responseData = null;
	String correlationId = "1234567891234567";
	Response expectedResponseData = null;
	Response actualResponseData = null;
	ProductSearchResource prodSearchResource = new ProductSearchResource();
	TestProductSearchUtil testProductSearchUtil = new TestProductSearchUtil();
	Map<String, String> errorPropertiesMap = new HashMap<String, String>();
	BaseResource baseResource = new BaseResource();

	MultivaluedMap<String, String> reqHeaders = new MultivaluedMapImpl();
	final String uriInfoPath = "v1/products/search";

	/**
	 * Method which executes before each test
	 * 
	 * @throws URISyntaxException
	 *             URISyntaxException thrown
	 */
	@Before
	public final void setUp() throws URISyntaxException {
		this.reqHeaders.add("Correlation-Id", "1234567891234567");
		this.errorPropertiesMap.put("11421", "An invalid parameter name has been submitted");
		this.errorPropertiesMap.put("11422", "An invalid parameter value has been submitted");
		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		this.prodSearchResource.setErrorLoader(errorLoader);
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(this.errorPropertiesMap);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.resource.ProductSearchResource#searchProducts(HttpHeaders, URIInfo)}
	 * . Testing is done to check whether the response Object is obtained. when
	 * searchProducts() method is called .
	 * 
	 * @throws Exception
	 *             exception thrown
	 */
	@Test (expected = WebApplicationException.class)
	public final void testSearchProducts() throws Exception {
		final MultivaluedMap<String, String> uriParameters = new MultivaluedMapImpl();

		uriParameters.add("q", "handbag");

		// UriInfo and UriBuilder is mocked to get the instance of the UriInfo
		// which will be input for implementation.
		this.uriInfo = mock(UriInfo.class);
		this.uriBuilder = mock(UriBuilder.class);
		this.httpHeaders = mock(HttpHeaders.class);

		when(this.httpHeaders.getRequestHeaders()).thenReturn(this.reqHeaders);
		stub(this.uriInfo.getAbsolutePathBuilder()).toReturn(this.uriBuilder);
		when(this.uriInfo.getQueryParameters()).thenReturn(uriParameters);
		when(this.uriInfo.getPath()).thenReturn(this.uriInfoPath);

		final BaseValidator baseValidator = PowerMockito.mock(BaseValidator.class);
		this.prodSearchResource.setBaseValidatorValidator(baseValidator);
		PowerMockito.when(baseValidator.updateValidationConfigurations(uriParameters, this.correlationId)).thenReturn(false);

		// PowerMockito.doNothing().when(prdtSearchMockSpy, "preValidation" ,
		// this.uriInfo, this.correlationId);

		// ServiceManager is mocked to get the instance of the service.
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.prodSearchResource.setServiceManager(serviceManager);
		PowerMockito.mockStatic(BaseResource.class);
		// ProductSearchService is mocked to get the instance of the service
		// and is passed
		// as return from call to service manager.
		final ProductSearchService productSearchService = PowerMockito
				.mock(ProductSearchService.class);

		PowerMockito.when(serviceManager.getService()).thenReturn(
				productSearchService);
		final Search search = null;
		// method getProductDetails will return list of products
		PowerMockito.when(
				productSearchService.searchProducts(uriParameters,
						this.correlationId)).thenReturn(
			search);

		// BaseResource is mocked to get the instance of the base resource.
		final BaseResource baseResource = PowerMockito.mock(BaseResource.class);

		final ResponseBuilder responseBuilder = new ResponseBuilderImpl();
		responseBuilder.status(200);
		final XMLProcessor xmlProcessor = PowerMockito.mock(XMLProcessor.class);
		this.baseResource.setXmlProcessor(xmlProcessor);
		// mocking call to method buildResponse and returning responsebuilder
		// object
		PowerMockito.when(
				baseResource.buildResponse(
						search,
						Search.class, "productsearch-binding", "XML",
						this.correlationId)).thenReturn(responseBuilder);

		this.expectedResponseData = responseBuilder.build();

		

		this.actualResponseData = this.prodSearchResource.searchProducts(
				this.httpHeaders, this.uriInfo);

		assertEquals(this.expectedResponseData.getStatus(),
				this.actualResponseData.getStatus());

	}

	/**
	 * *Test method for
	 * {@link com.belk.api.resource.ProductSearchResource#searchProducts(HttpHeaders, URIInfo)}
	 * . Testing is done to check whether the response Object is obtained. when
	 * searchProducts() method is called .
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 */
	@Test(expected = Exception.class)
	public final void testSearchProductsForAdapterException()
			throws BaseException, AdapterException {
		// Mocking the UriInfo Object to return URI Query Parameters for Product
		// Search
		final MultivaluedMap<String, String> uriParameters = TestProductSearchUtil
				.createURIParametersMap();
		final MultivaluedMap<String, String> reqHeaders = TestProductSearchUtil
				.createRequestHeadersMap();
		this.httpHeaders = mock(HttpHeaders.class);
		this.uriInfo = mock(UriInfo.class);
		this.uriBuilder = mock(UriBuilder.class);
		stub(this.httpHeaders.getRequestHeaders()).toReturn(reqHeaders);
		stub(this.uriInfo.getAbsolutePathBuilder()).toReturn(this.uriBuilder);
		when(this.uriInfo.getQueryParameters()).thenReturn(uriParameters);
		when(this.uriInfo.getPath()).thenReturn(this.uriInfoPath);

		// mocking the BaseValidator class inside the searchProducts
		// method
		final BaseValidator baseValidator = PowerMockito
				.mock(BaseValidator.class);
		this.prodSearchResource.setBaseValidatorValidator(baseValidator);
		// mocking the ServiceManager class inside the searchProducts method
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.prodSearchResource.setServiceManager(serviceManager);

		// mocking the ProductSearchService class inside the searchProducts
		// method
		final ProductSearchService productSearchServcie = PowerMockito
				.mock(ProductSearchService.class);

		// mocking the serviceManager.getService method inside the
		// searchProducts methods
		// which returns productSearchServcie
		PowerMockito.when(serviceManager.getService()).thenReturn(
				productSearchServcie);

		// mocking the productSearchServcie.searchProducts method with
		// uriParameters
		// parameter inside the searchProducts method
		// which throws AdapterException
		PowerMockito.when(
				productSearchServcie.searchProducts(uriParameters,
						this.correlationId)).thenThrow(
				new AdapterException("500",
						"JUnit Mock AdapterConnection Issue", "",
						this.correlationId));

		PowerMockito.mock(BaseResource.class);
		final JSONProcessor jsonProcessor = new JSONProcessor();
		this.prodSearchResource.setJsonProcessor(jsonProcessor);

		final Response actualSearchResults = this.prodSearchResource
				.searchProducts(this.httpHeaders, this.uriInfo);
		org.junit.Assert.assertNotNull(actualSearchResults);

	}

	/**
	 * Test method for
	 * {@link com.belk.api.resource.ProductSearchResource#searchProducts(HttpHeaders, URIInfo)}
	 * . Testing is done to check whether the response Object is obtained. when
	 * searchProducts() method is called . service exception is handled
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 */
	@Test(expected = Exception.class)
	public final void testSearchProductsForBaseException()
			throws BaseException, AdapterException {
		// Mocking the UriInfo Object to return URI Query Parameters for Product
		// Search
		final MultivaluedMap<String, String> uriParameters = TestProductSearchUtil
				.createURIParametersMap();
		final MultivaluedMap<String, String> reqHeaders = TestProductSearchUtil
				.createRequestHeadersMap();
		this.httpHeaders = mock(HttpHeaders.class);
		this.uriInfo = mock(UriInfo.class);
		this.uriBuilder = mock(UriBuilder.class);
		stub(this.httpHeaders.getRequestHeaders()).toReturn(reqHeaders);
		stub(this.uriInfo.getAbsolutePathBuilder()).toReturn(this.uriBuilder);
		when(this.uriInfo.getQueryParameters()).thenReturn(uriParameters);
		when(this.uriInfo.getPath()).thenReturn(this.uriInfoPath);
		// mocking the BaseValidator class inside the searchProducts
		// method
		final BaseValidator baseValidator = PowerMockito
				.mock(BaseValidator.class);
		this.prodSearchResource.setBaseValidatorValidator(baseValidator);
		// mocking the ServiceManager class inside the searchProducts method
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.prodSearchResource.setServiceManager(serviceManager);

		// mocking the ProductSearchService class inside the searchProducts
		// method
		final ProductSearchService productSearchServcie = PowerMockito
				.mock(ProductSearchService.class);

		// mocking the serviceManager.getService method inside the
		// searchProducts methods
		// which returns productSearchServcie
		PowerMockito.when(serviceManager.getService()).thenReturn(
				productSearchServcie);

		// mocking the productSearchServcie.searchProducts method with
		// uriParameters
		// parameter inside the searchProducts method
		// which throws Base Exception
		PowerMockito.when(
				productSearchServcie.searchProducts(uriParameters,
						this.correlationId)).thenThrow(
				new BaseException("500", "JUnit Mock Base Exception"));

		PowerMockito.mock(BaseResource.class);
		final JSONProcessor jsonProcessor = new JSONProcessor();
		this.prodSearchResource.setJsonProcessor(jsonProcessor);

		final Response actualSearchResults = this.prodSearchResource
				.searchProducts(this.httpHeaders, this.uriInfo);
		org.junit.Assert.assertNotNull(actualSearchResults);

	}

	/**
	 * Test method for
	 * {@link com.belk.api.resource.ProductSearchResource#searchProducts(HttpHeaders, URIInfo)}
	 * . Testing is done to check whether the response Object is obtained. when
	 * searchProducts() method is called . service exception is handled
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 */
	@Test(expected = Exception.class)
	public final void testSearchProductsForServiceException()
			throws BaseException, AdapterException {
		// Mocking the UriInfo Object to return URI Query Parameters for Product
		// Search
		final MultivaluedMap<String, String> uriParameters = TestProductSearchUtil
				.createURIParametersMap();
		final MultivaluedMap<String, String> reqHeaders = TestProductSearchUtil
				.createRequestHeadersMap();
		this.httpHeaders = mock(HttpHeaders.class);
		this.uriInfo = mock(UriInfo.class);
		this.uriBuilder = mock(UriBuilder.class);
		stub(this.httpHeaders.getRequestHeaders()).toReturn(reqHeaders);
		stub(this.uriInfo.getAbsolutePathBuilder()).toReturn(this.uriBuilder);
		when(this.uriInfo.getQueryParameters()).thenReturn(uriParameters);
		when(this.uriInfo.getPath()).thenReturn(this.uriInfoPath);
		// mocking the BaseValidator class inside the searchProducts
		// method
		final BaseValidator baseValidator = PowerMockito
				.mock(BaseValidator.class);
		this.prodSearchResource.setBaseValidatorValidator(baseValidator);
		// mocking the ServiceManager class inside the searchProducts method
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.prodSearchResource.setServiceManager(serviceManager);

		// mocking the ProductSearchService class inside the searchProducts
		// method
		final ProductSearchService productSearchServcie = PowerMockito
				.mock(ProductSearchService.class);
		// mocking the serviceManager.getService method inside the
		// searchProducts methods
		// which returns productSearchServcie
		PowerMockito.when(serviceManager.getService()).thenReturn(
				productSearchServcie);

		// mocking the productSearchServcie.searchProducts method with
		// uriParameters
		// parameter inside the searchProducts method
		// which throws Service Exception
		PowerMockito.when(
				productSearchServcie.searchProducts(uriParameters,
						this.correlationId)).thenThrow(
				new ServiceException("500", "JUnit Mock Service Exception", "",
						this.correlationId));
		PowerMockito.mock(BaseResource.class);
		final JSONProcessor jsonProcessor = new JSONProcessor();
		this.prodSearchResource.setJsonProcessor(jsonProcessor);

		final Response actualSearchResults = this.prodSearchResource
				.searchProducts(this.httpHeaders, this.uriInfo);
		org.junit.Assert.assertNotNull(actualSearchResults);

	}

	/**
	 * Test method for
	 * {@link com.belk.api.resource.ProdSearchResource#updatePropertiesMap(HttpHeaders, UriInfo)}
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
		when(this.uriInfo.getPath()).thenReturn(this.uriInfoPath);
		final BaseValidator baseValidator = PowerMockito.mock(BaseValidator.class);
		this.prodSearchResource.setBaseValidatorValidator(baseValidator);
		PowerMockito.when(baseValidator.updateValidationConfigurations(uriParameters, this.correlationId)).thenReturn(false);
		// ServiceManager is mocked to get the instance of the service.
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.prodSearchResource.setServiceManager(serviceManager);

		// ProductSearchService is mocked to get the instance of the service
		// and is passed
		// as return from call to service manager.
		final ProductSearchService productSearchService = PowerMockito
				.mock(ProductSearchService.class);

		PowerMockito.when(serviceManager.getService()).thenReturn(
				productSearchService);


		final ResponseBuilder responseBuilder = new ResponseBuilderImpl();
		responseBuilder.status(200);

		this.expectedResponseData = responseBuilder.build();

		this.actualResponseData = this.prodSearchResource.updatePropertiesMap(
				this.httpHeaders, this.uriInfo);

		assertEquals(this.expectedResponseData.getStatus(),
				this.actualResponseData.getStatus());
	}
}