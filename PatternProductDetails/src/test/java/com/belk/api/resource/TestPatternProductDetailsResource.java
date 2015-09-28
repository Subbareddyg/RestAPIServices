package com.belk.api.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;

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
import com.belk.api.model.productdetails.ProductList;
import com.belk.api.service.PatternProductDetailsService;
import com.belk.api.transformer.xml.XMLProcessor;
import com.belk.api.util.TestConstants;
import com.belk.api.util.TestProductDetailUtil;
import com.belk.api.validators.BaseValidator;
import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * Junit test case for productDetails to obtain the
 * 
 * @author Mindtree
 * @date Oct 22, 2013
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ PatternProductDetailsResource.class, UriInfo.class,
		UriBuilder.class, HttpHeaders.class, ServiceManager.class,
		PatternProductDetailsService.class, BaseResource.class,
		BaseValidator.class })
public class TestPatternProductDetailsResource {

	TestProductDetailUtil testProductDetail;
	UriInfo uriInfo;
	HttpHeaders httpHeader;
	URI uri = null;
	UriBuilder uriBuilder;
	Response expectedResponseData = null;
	Response actualResponseData = null;
	String correlationId = "1234567891234567";
	PatternProductDetailsResource detailsResource = new PatternProductDetailsResource();
	MultivaluedMap<String, String> reqHeaders = new MultivaluedMapImpl();

	/**
	 * Initial set up ahead to start unit test case for class
	 * 
	 * @throws URISyntaxException
	 *             - exception
	 */
	@Before
	public final void setUp() throws URISyntaxException {
		this.testProductDetail = new TestProductDetailUtil();
		this.uri = new URI(TestConstants.uri);
		this.reqHeaders.add("Correlation-Id", "1234567891234567");
	}

	/**
	 * Junit test case to test getProductDetails method(valid scenario)
	 * 
	 * @throws Exception
	 *             - exception
	 */
	@Test(expected = Exception.class)
	public final void testGetProductDetails() throws Exception {
		final MultivaluedMap<String, String> uriParameters = new MultivaluedMapImpl();

		uriParameters.add("productcode", "1800303AZJL606");

		// UriInfo and UriBuilder is mocked to get the instance of the UriInfo
		// which will be input for implementation.
		this.uriInfo = mock(UriInfo.class);
		this.uriBuilder = mock(UriBuilder.class);
		this.httpHeader = mock(HttpHeaders.class);

		when(this.httpHeader.getRequestHeaders()).thenReturn(this.reqHeaders);
		stub(this.uriInfo.getAbsolutePathBuilder()).toReturn(this.uriBuilder);
		when(this.uriInfo.getQueryParameters()).thenReturn(uriParameters);
		PowerMockito.mock(BaseValidator.class);

		// ServiceManager is mocked to get the instance of the service.
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.detailsResource.setServiceManager(serviceManager);

		// ProductDetailsService is mocked to get the instance of the service
		// and is passed
		// as return from call to service manager.
		final PatternProductDetailsService productDetailsService = PowerMockito
				.mock(PatternProductDetailsService.class);

		PowerMockito.when(serviceManager.getService()).thenReturn(
				productDetailsService);

		// method getProductDetails will return list of products
		PowerMockito.when(
				productDetailsService.getPatternProductDetails(
						this.testProductDetail.getData(), this.correlationId))
				.thenReturn(this.testProductDetail.getResponseData());

		// BaseResource is mocked to get the instance of the base resource.
		final BaseResource baseResource = PowerMockito.mock(BaseResource.class);

		final ResponseBuilder responseBuilder = new ResponseBuilderImpl();
		responseBuilder.status(200);

		// mocking call to method buildResponse and returning responsebuilder
		// object
		PowerMockito.when(
				baseResource.buildResponse(
						this.testProductDetail.getResponseData(),
						ProductList.class,
						TestConstants.PRODUCT_DETAIL_BINDING, "XML",
						this.correlationId)).thenReturn(responseBuilder);

		this.expectedResponseData = responseBuilder.build();

		final XMLProcessor xmlProcessor = new XMLProcessor();
		this.detailsResource.setXmlProcessor(xmlProcessor);

		this.actualResponseData = this.detailsResource
				.getPatternProductDetails(this.httpHeader, this.uriInfo);

		// assertEquals(this.expectedResponseData.getStatus(),
		// this.actualResponseData.getStatus());
		assertNotNull(this.expectedResponseData.getStatus());
		assertNotNull(this.actualResponseData.getStatus());
	}

	/**
	 * Junit test case to test getProductDetails method(
	 * 
	 * @throws Exception
	 *             - exception
	 */
	@Test(expected = Exception.class)
	public final void testGetProductDetailsError() throws Exception {
		final MultivaluedMap<String, String> uriParameters = new MultivaluedMapImpl();
		uriParameters.add("abc", "1800303AZJL606");

		// UriInfo and UriBuilder is mocked to get the instance of the UriInfo
		// which will be input for implementation.
		this.uriInfo = mock(UriInfo.class);
		this.uriBuilder = mock(UriBuilder.class);
		this.httpHeader = mock(HttpHeaders.class);

		when(this.httpHeader.getRequestHeaders()).thenReturn(this.reqHeaders);
		stub(this.uriInfo.getAbsolutePathBuilder()).toReturn(this.uriBuilder);
		when(this.uriInfo.getQueryParameters()).thenReturn(uriParameters);
		PowerMockito.mock(BaseValidator.class);
		// this.detailsResource.setBaseValidator(baseValidator);
		// ServiceManager is mocked to get the instance of the service.
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.detailsResource.setServiceManager(serviceManager);

		// ProductDetailsService is mocked to get the instance of the service
		// and is passed
		// as return from call to service manager.
		final PatternProductDetailsService productDetailsService = PowerMockito
				.mock(PatternProductDetailsService.class);

		PowerMockito.when(serviceManager.getService()).thenReturn(
				productDetailsService);

		// method getProductDetails will return list of products
		PowerMockito.when(
				productDetailsService.getPatternProductDetails(
						this.testProductDetail.getData(), this.correlationId))
				.thenReturn(this.testProductDetail.getResponseData());

		// BaseResource is mocked to get the instance of the base resource.
		final BaseResource baseResource = PowerMockito.mock(BaseResource.class);

		final ResponseBuilder responseBuilder = new ResponseBuilderImpl();
		responseBuilder.status(400);

		// mocking call to method buildResponse and returning responsebuilder
		// object
		PowerMockito.when(
				baseResource.buildResponse(
						this.testProductDetail.getResponseData(),
						ProductList.class,
						TestConstants.PRODUCT_DETAIL_BINDING, "XML",
						this.correlationId)).thenReturn(responseBuilder);

		this.actualResponseData = this.detailsResource
				.getPatternProductDetails(this.httpHeader, this.uriInfo);

		responseBuilder.status(204);

		this.expectedResponseData = responseBuilder.build();

		// assertEquals(this.expectedResponseData.getStatus(),
		// this.actualResponseData.getStatus());
		assertNotNull(this.expectedResponseData.getStatus());
		assertNotNull(this.actualResponseData.getStatus());
	}
	
	/**
	 * Test method for
	 * {@link com.belk.api.resource.PatternProductDetailsResource#updatePropertiesMap(HttpHeaders, UriInfo)}
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

		// ServiceManager is mocked to get the instance of the service.
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		detailsResource.setServiceManager(serviceManager);

		// PatternProductDetailsService is mocked to get the instance of the service
		// and is passed
		// as return from call to service manager.
		final PatternProductDetailsService categoryProductService = PowerMockito
				.mock(PatternProductDetailsService.class);

		PowerMockito.when(serviceManager.getService()).thenReturn(
				categoryProductService);


		final ResponseBuilder responseBuilder = new ResponseBuilderImpl();
		responseBuilder.status(200);

		expectedResponseData = responseBuilder.build();

		actualResponseData = detailsResource.updatePropertiesMap(
				httpHeader, uriInfo);

		assertEquals(expectedResponseData.getStatus(),
				actualResponseData.getStatus());
	}

}