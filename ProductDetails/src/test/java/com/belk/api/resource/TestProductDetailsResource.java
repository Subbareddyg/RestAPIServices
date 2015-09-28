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
import com.belk.api.exception.ServiceException;
import com.belk.api.model.productdetails.ProductList;
import com.belk.api.service.ProductDetailsService;
import com.belk.api.transformer.xml.XMLProcessor;
import com.belk.api.util.ErrorLoader;
import com.belk.api.util.TestConstants;
import com.belk.api.util.TestProductDetailUtil;
import com.belk.api.validators.BaseValidator;
import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * Unit Testing related to ProductDetailsResource class is performed. <br />
 * {@link TestProductDetailsResource} class is written for testing methods in
 * {@link ProductDetailsResource} The unit test cases evaluates the way the
 * methods behave for the inputs given.
 * 
 * <br />
 * The test cases associated with ProductDetailsResource is written to make the
 * code intact by assuring the code test.
 * 
 * @date Oct 22, 2013
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ ProductDetailsResource.class, UriInfo.class,
	UriBuilder.class, HttpHeaders.class, ServiceManager.class,
	ProductDetailsService.class, BaseResource.class, BaseValidator.class, ErrorLoader.class  })
public class TestProductDetailsResource {

	TestProductDetailUtil testProductDetail;
	UriInfo uriInfo;
	HttpHeaders httpHeader;
	URI uri = null;
	UriBuilder uriBuilder;
	Response expectedResponseData = null;
	Response actualResponseData = null;
	String correlationId = "1234567891234567";
	ProductDetailsResource detailsResource = new ProductDetailsResource();
	MultivaluedMap<String, String> reqHeaders = new MultivaluedMapImpl();
	final String uriInfoPath = "v1/products";
	Map<String, String> errorPropertiesMap = new HashMap<String, String>();
	BaseResource baseResource = new BaseResource();

	/**
	 * Initial set up ahead to start unit test case for class
	 * 
	 * @throws URISyntaxException
	 *             - exception
	 */
	@Before
	public final void setUp() throws URISyntaxException {
		testProductDetail = new TestProductDetailUtil();
		uri = new URI(TestConstants.uri);
		reqHeaders.add("Correlation-Id", "1234567891234567");
		errorPropertiesMap.put("11421","An invalid parameter name has been submitted");
		errorPropertiesMap.put("11422","An invalid parameter value has been submitted");
		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		this.detailsResource.setErrorLoader(errorLoader);
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(errorPropertiesMap);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.resource.ProductDetailsResource#getProductDetails(HttpHeaders,String)}
	 * . Testing is done to check whether the Service object is obtained
	 * correctly
	 * 
	 * @throws Exception
	 *             - exception
	 */
	@Test(expected = WebApplicationException.class)
	public final void testGetProductDetails() throws Exception {
		final MultivaluedMap<String, String> uriParameters = new MultivaluedMapImpl();

		uriParameters.add("styleupc", "1800303AZJL606");
		uriParameters.add("format", "xml");
		uriParameters.add("vendorvpn", "vendor");
		// UriInfo and UriBuilder is mocked to get the instance of the UriInfo
		// which will be input for implementation.
		uriInfo = mock(UriInfo.class);
		uriBuilder = mock(UriBuilder.class);
		httpHeader = mock(HttpHeaders.class);

		when(httpHeader.getRequestHeaders()).thenReturn(reqHeaders);
		stub(uriInfo.getAbsolutePathBuilder()).toReturn(uriBuilder);
		when(uriInfo.getQueryParameters()).thenReturn(uriParameters);
		when(uriInfo.getPath()).thenReturn(uriInfoPath);

		final BaseValidator baseValidator = PowerMockito
				.mock(BaseValidator.class);
		PowerMockito.when(baseValidator.isBaseValidationRequired("PRODUCT_DETAILS", correlationId)).thenReturn(true);
		detailsResource.setBaseValidator(baseValidator);

		// ServiceManager is mocked to get the instance of the service.
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		detailsResource.setServiceManager(serviceManager);

		// ProductDetailsService is mocked to get the instance of the service
		// and is passed
		// as return from call to service manager.
		final ProductDetailsService productDetailsService = PowerMockito
				.mock(ProductDetailsService.class);

		PowerMockito.when(serviceManager.getService()).thenReturn(
				productDetailsService);

		// method getProductDetails will return list of products
		ProductList productList = null;
		PowerMockito.when(
				productDetailsService.getProductDetails(
						testProductDetail.getData(), correlationId))
						.thenReturn(productList);

		// BaseResource is mocked to get the instance of the base resource.
		final BaseResource baseResource = PowerMockito.mock(BaseResource.class);

		final ResponseBuilder responseBuilder = new ResponseBuilderImpl();
		responseBuilder.status(200);

		// mocking call to method buildResponse and returning responsebuilder
		// object
		PowerMockito.when(
				baseResource.buildResponse(
						productList,
						ProductList.class,
						TestConstants.PRODUCT_DETAIL_BINDING, "XML",
						correlationId)).thenReturn(responseBuilder);

		expectedResponseData = responseBuilder.build();

		final XMLProcessor xmlProcessor = new XMLProcessor();
		detailsResource.setXmlProcessor(xmlProcessor);

		actualResponseData = detailsResource.getProductDetails(
				httpHeader, uriInfo);

		assertEquals(expectedResponseData.getStatus(),
				actualResponseData.getStatus());
	}

	/**
	 * Test method for
	 * {@link com.belk.api.resource.ProductDetailsResource#getProductDetails(HttpHeaders,String)}
	 * . Testing is done to check whether the Service object is obtained
	 * correctly
	 * 
	 * @throws Exception
	 *             - exception
	 */
	@Test
	(expected = WebApplicationException.class)
	public final void testGetProductDetailsError() throws Exception {
		final MultivaluedMap<String, String> uriParameters = new MultivaluedMapImpl();
		uriParameters.add("abc", "1800303AZJL606");
		uriParameters.add("format", "xml");

		// UriInfo and UriBuilder is mocked to get the instance of the UriInfo
		// which will be input for implementation.
		uriInfo = mock(UriInfo.class);
		uriBuilder = mock(UriBuilder.class);
		httpHeader = mock(HttpHeaders.class);

		when(httpHeader.getRequestHeaders()).thenReturn(reqHeaders);
		stub(uriInfo.getAbsolutePathBuilder()).toReturn(uriBuilder);
		when(uriInfo.getQueryParameters()).thenReturn(uriParameters);
		when(uriInfo.getPath()).thenReturn(uriInfoPath);

		final BaseValidator baseValidator = PowerMockito
				.mock(BaseValidator.class);
		detailsResource.setBaseValidator(baseValidator);
		PowerMockito.when(baseValidator.isBaseValidationRequired("PRODUCT_DETAILS", correlationId)).thenReturn(true);

		// ServiceManager is mocked to get the instance of the service.
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		detailsResource.setServiceManager(serviceManager);

		// ProductDetailsService is mocked to get the instance of the service
		// and is passed
		// as return from call to service manager.
		final ProductDetailsService productDetailsService = PowerMockito
				.mock(ProductDetailsService.class);

		PowerMockito.when(serviceManager.getService()).thenReturn(
				productDetailsService);

		// method getProductDetails will return list of products
		PowerMockito.when(
				productDetailsService.getProductDetails(
						testProductDetail.getData(), correlationId))
						.thenReturn(testProductDetail.getResponseData());

		// BaseResource is mocked to get the instance of the base resource.
		final BaseResource baseResource = PowerMockito.mock(BaseResource.class);

		final ResponseBuilder responseBuilder = new ResponseBuilderImpl();
		responseBuilder.status(400);

		// mocking call to method buildResponse and returning responsebuilder
		// object
		PowerMockito.when(
				baseResource.buildResponse(
						testProductDetail.getResponseData(),
						ProductList.class,
						TestConstants.PRODUCT_DETAIL_BINDING, "XML",
						correlationId)).thenReturn(responseBuilder);

		actualResponseData = detailsResource.getProductDetails(
				httpHeader, uriInfo);

		responseBuilder.status(404);

		expectedResponseData = responseBuilder.build();

		assertEquals(expectedResponseData.getStatus(),
				actualResponseData.getStatus());
	}
	
	/**
	 * Test method for
	 * {@link com.belk.api.resource.ProductDetailsResource#updatePropertiesMap(HttpHeaders, UriInfo)}
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
		httpHeader = mock(HttpHeaders.class);

		when(httpHeader.getRequestHeaders()).thenReturn(reqHeaders);
		stub(uriInfo.getAbsolutePathBuilder()).toReturn(uriBuilder);
		when(uriInfo.getQueryParameters()).thenReturn(uriParameters);
		when(uriInfo.getPath()).thenReturn(uriInfoPath);
		final BaseValidator baseValidator = PowerMockito.mock(BaseValidator.class);
		this.detailsResource.setBaseValidator(baseValidator);
		PowerMockito.when(baseValidator.updateValidationConfigurations(uriParameters, this.correlationId)).thenReturn(false);

		// ServiceManager is mocked to get the instance of the service.
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		detailsResource.setServiceManager(serviceManager);

		// ProductDetailsService is mocked to get the instance of the service
		// and is passed
		// as return from call to service manager.
		final ProductDetailsService productDetailsService = PowerMockito
				.mock(ProductDetailsService.class);

		PowerMockito.when(serviceManager.getService()).thenReturn(
				productDetailsService);

		/*PowerMockito.when(
				productDetailsService.updatePropertiesMap(correlationId, 
						testProductDetail.getConfigurationData()))
						.thenReturn(testProductDetail.getResponseData());*/

		final ResponseBuilder responseBuilder = new ResponseBuilderImpl();
		responseBuilder.status(200);

		expectedResponseData = responseBuilder.build();

		actualResponseData = detailsResource.updatePropertiesMap(
				httpHeader, uriInfo);

		assertEquals(expectedResponseData.getStatus(),
				actualResponseData.getStatus());
	}
	
}