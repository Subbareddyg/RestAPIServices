package com.belk.api.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.when;

import java.net.URI;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.belk.api.adapter.manager.AdapterManager;
import com.belk.api.business.service.manager.ServiceManager;
import com.belk.api.exception.BaseException;
import com.belk.api.impl.EndecaAdapter;
import com.belk.api.model.categorydetails.Categories;
import com.belk.api.service.CategoryDetailsService;
import com.belk.api.service.impl.CategoryDetailsServiceImpl;
import com.belk.api.transformer.xml.XMLProcessor;
import com.belk.api.util.EndecaUtil;
import com.belk.api.util.RequestParser;
import com.belk.api.util.TestCategoryDetailsResourceUtil;
import com.belk.api.util.URIToEndecaTransformer;
import com.endeca.navigation.ENEQueryResults;
import com.endeca.navigation.Navigation;
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
 * @author Mindtree Date Dec 17th
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ CategoryDetailsResource.class, ServiceManager.class,
		CategoryDetailsServiceImpl.class, BaseResource.class, UriInfo.class,
		HttpHeaders.class, AdapterManager.class, EndecaAdapter.class,
		URIToEndecaTransformer.class, EndecaUtil.class, ENEQueryResults.class,
		Navigation.class })
public class TestCategoryDetailsResourceForException {

	HttpHeaders httpHeaders;
	UriInfo uriInfo;
	URI uri = null;
	UriBuilder uriBuilder;
	Response responseData = null;
	Response actualSearchResults = null;
	final String categoryId = "4294922263";
	final String correlationId = "1234567890SDGFASF4545DFSERE";
	MultivaluedMap<String, String> reqHeaders = new MultivaluedMapImpl();
	CategoryDetailsResource categoryDetailsResource = new CategoryDetailsResource();
	CategoryDetailsServiceImpl categoryDetailsService = new CategoryDetailsServiceImpl();


	/**
	 * Test method for
	 * {@link com.belk.api.service.impl.CategoryDetailsResource#getCategoryDetails(HttpHeaders,String,UriInfo)}
	 * 
	 * @throws BaseException
	 *             BaseException
	 */
	@Test(expected = Exception.class)
	public final void testGetCategoryDetailsForValidationException()
			throws BaseException {

		final MultivaluedMap<String, String> uriParameters = TestCategoryDetailsResourceUtil
				.createURIParametersMapWithCategory();
		this.httpHeaders = mock(HttpHeaders.class);
		this.uriInfo = mock(UriInfo.class);
		this.uriBuilder = mock(UriBuilder.class);
		when(this.httpHeaders.getRequestHeaders()).thenReturn(this.reqHeaders);
		stub(this.uriInfo.getAbsolutePathBuilder()).toReturn(this.uriBuilder);
		when(this.uriInfo.getQueryParameters()).thenReturn(uriParameters);

		// ServiceManager is mocked to get the instance of the service. final
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.categoryDetailsResource.setServiceManager(serviceManager);

		// CategoryDetailsService is mocked to get the instance of the service
		// //
		final CategoryDetailsService categoryDetailsService = PowerMockito //
				.mock(CategoryDetailsService.class);
		PowerMockito.when(serviceManager.getService()).thenReturn(
				categoryDetailsService);

		// BaseResource is mocked to get the instance of the base resource.
		// final
		final BaseResource baseResource = PowerMockito.mock(BaseResource.class);

		final ResponseBuilder responseBuilder = new ResponseBuilderImpl();
		responseBuilder.status(200);

		// mocking call to method buildResponse and return responsebuilder //
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
		assertNotNull(actualSearchResults.getEntity());
		assertNotNull(actualSearchResults.getStatus());
		assertNotNull(actualSearchResults.getMetadata());
		assertEquals(200, actualSearchResults.getStatus());

	}

	/**
	 * Test method for
	 * {@link com.belk.api.service.impl.CategoryDetailsResource#getCategoryDetails(HttpHeaders,String,UriInfo)}
	 * 
	 * @throws BaseException
	 *             BaseException
	 */
	@Test(expected = Exception.class)
	public final void testGetCategoryDetailsForAdapterException()
			throws BaseException {

		final MultivaluedMap<String, String> uriParameters = TestCategoryDetailsResourceUtil
				.createURIParametersMap();
		this.mockForCategoryDetailForException(uriParameters);

		final EndecaAdapter endecaAdapter = new EndecaAdapter();

		final URIToEndecaTransformer uriToEndecaTransformer = new URIToEndecaTransformer();
		endecaAdapter.setUriToEndecaTransformer(uriToEndecaTransformer);
		
		PowerMockito.mock(CategoryDetailsServiceImpl.class);

		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.categoryDetailsResource.setServiceManager(serviceManager);
		final AdapterManager adapterManagerMock = PowerMockito
				.mock(AdapterManager.class);
		this.categoryDetailsService.setAdapterManager(adapterManagerMock);
		PowerMockito.mock(EndecaAdapter.class);
		when(adapterManagerMock.getAdapter()).thenReturn(endecaAdapter);

		PowerMockito.when(serviceManager.getService()).thenReturn(
				this.categoryDetailsService);

		final BaseResource baseResource = PowerMockito.mock(BaseResource.class);

		final ResponseBuilder responseBuilder = new ResponseBuilderImpl();
		responseBuilder.status(200);

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
		assertNotNull(actualSearchResults.getEntity());
		assertNotNull(actualSearchResults.getStatus());
		assertNotNull(actualSearchResults.getMetadata());
		assertEquals(200, actualSearchResults.getStatus());

	}

	/**
	 * 
	 * @param uriParameters
	 *            uriParameters
	 */
	private void mockForCategoryDetailForException(
			final MultivaluedMap<String, String> uriParameters) {
		this.reqHeaders = TestCategoryDetailsResourceUtil
				.createURIParametersMap();
		this.httpHeaders = mock(HttpHeaders.class);
		this.uriInfo = mock(UriInfo.class);
		this.uriBuilder = mock(UriBuilder.class);
		when(this.httpHeaders.getRequestHeaders()).thenReturn(this.reqHeaders);
		stub(this.uriInfo.getAbsolutePathBuilder()).toReturn(this.uriBuilder);
		when(this.uriInfo.getQueryParameters()).thenReturn(uriParameters);
		final RequestParser requestParser = new RequestParser();
		this.categoryDetailsService.setRequestParser(requestParser);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.service.impl.CategoryDetailsResource#getSubCategoryDetails(HttpHeaders,String,UriInfo)}
	 * 
	 * @throws BaseException
	 *             BaseException
	 */
	@Test(expected = Exception.class)
	public final void testGetSubCategoryDetailsForValidationException()
			throws BaseException {

		final MultivaluedMap<String, String> uriParameters = TestCategoryDetailsResourceUtil
				.createURIParametersMapWithCategory();
		this.httpHeaders = mock(HttpHeaders.class);
		this.uriInfo = mock(UriInfo.class);
		this.uriBuilder = mock(UriBuilder.class);
		when(this.httpHeaders.getRequestHeaders()).thenReturn(this.reqHeaders);
		stub(this.uriInfo.getAbsolutePathBuilder()).toReturn(this.uriBuilder);
		when(this.uriInfo.getQueryParameters()).thenReturn(uriParameters);

		// ServiceManager is mocked to get the instance of the service. final
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.categoryDetailsResource.setServiceManager(serviceManager);

		// CategoryDetailsService is mocked to get the instance of the service
		// //
		final CategoryDetailsService categoryDetailsService = PowerMockito //
				.mock(CategoryDetailsService.class);
		PowerMockito.when(serviceManager.getService()).thenReturn(
				categoryDetailsService);

		// BaseResource is mocked to get the instance of the base resource.
		// final
		final BaseResource baseResource = PowerMockito.mock(BaseResource.class);

		final ResponseBuilder responseBuilder = new ResponseBuilderImpl();
		responseBuilder.status(200);

		// mocking call to method buildResponse and return responsebuilder //
		PowerMockito.when(
				baseResource.buildResponse(TestCategoryDetailsResourceUtil
						.createCategoryDetailsResults(), Categories.class,
						"categoryDetails-binding", "XML", this.correlationId))
				.thenReturn(responseBuilder);

		this.responseData = responseBuilder.build();

		final XMLProcessor xmlProcessor = new XMLProcessor();
		this.categoryDetailsResource.setXmlProcessor(xmlProcessor);

		final Response actualSearchResults = this.categoryDetailsResource
				.getSubCategoryDetails(this.httpHeaders, this.categoryId,
						this.uriInfo);

		assertNotNull(actualSearchResults);
		assertNotNull(actualSearchResults.getEntity());
		assertNotNull(actualSearchResults.getStatus());
		assertNotNull(actualSearchResults.getMetadata());
		assertEquals(200, actualSearchResults.getStatus());

	}

	/**
	 * * Test method for
	 * {@link com.belk.api.service.impl.CategoryDetailsResource#getSubCategoryDetails(HttpHeaders,String,UriInfo)}
	 * 
	 * @throws BaseException
	 *             BaseException
	 */
	@Test(expected = Exception.class)
	public final void testGetSubCategoryDetailsForAdapterException()
			throws BaseException {

		final MultivaluedMap<String, String> uriParameters = TestCategoryDetailsResourceUtil
				.createURIParametersMap();
		this.mockForCategoryDetailForException(uriParameters);

		final EndecaAdapter endecaAdapter = new EndecaAdapter();

		final URIToEndecaTransformer uriToEndecaTransformer = new URIToEndecaTransformer();
		endecaAdapter.setUriToEndecaTransformer(uriToEndecaTransformer);

		PowerMockito.mock(CategoryDetailsServiceImpl.class);

		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.categoryDetailsResource.setServiceManager(serviceManager);
		final AdapterManager adapterManagerMock = PowerMockito
				.mock(AdapterManager.class);
		this.categoryDetailsService.setAdapterManager(adapterManagerMock);
		PowerMockito.mock(EndecaAdapter.class);
		when(adapterManagerMock.getAdapter()).thenReturn(endecaAdapter);

		PowerMockito.when(serviceManager.getService()).thenReturn(
				this.categoryDetailsService);

		final BaseResource baseResource = PowerMockito.mock(BaseResource.class);

		final ResponseBuilder responseBuilder = new ResponseBuilderImpl();
		responseBuilder.status(200);

		PowerMockito.when(
				baseResource.buildResponse(TestCategoryDetailsResourceUtil
						.createCategoryDetailsResults(), Categories.class,
						"categoryDetails-binding", "XML", this.correlationId))
				.thenReturn(responseBuilder);

		this.responseData = responseBuilder.build();

		final XMLProcessor xmlProcessor = new XMLProcessor();
		this.categoryDetailsResource.setXmlProcessor(xmlProcessor);

		final Response actualSearchResults = this.categoryDetailsResource
				.getSubCategoryDetails(this.httpHeaders, this.categoryId,
						this.uriInfo);

		assertNotNull(actualSearchResults);
		assertNotNull(actualSearchResults.getEntity());
		assertNotNull(actualSearchResults.getStatus());
		assertNotNull(actualSearchResults.getMetadata());
		assertEquals(200, actualSearchResults.getStatus());

	}

}
