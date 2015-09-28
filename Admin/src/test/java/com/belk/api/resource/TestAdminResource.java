package com.belk.api.resource;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.belk.api.adapter.manager.AdapterManager;
import com.belk.api.business.service.manager.ServiceManager;
import com.belk.api.constants.EndecaConstants;
import com.belk.api.exception.AdapterException;
import com.belk.api.impl.EndecaAdapter;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.belk.api.model.catalog.Catalog;
import com.belk.api.service.AdminService;
import com.belk.api.util.EndecaLoader;
import com.belk.api.util.TestAdminResourceUtil;
import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * Unit Testing related to AdminResource class is performed. <br />
 * {@link AdminResource} class is written for testing methods in
 * {@link AdminResource} The unit test cases evaluates the way the methods
 * behave for the inputs given.
 * 
 * <br />
 * The test cases associated with AdminResource is written to make the code
 * intact by assuring the code test.
 * 
 * @author Mindtree
 * @date Dec 04, 2013
 * 
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ AdminResource.class, ServiceManager.class, HttpHeaders.class,
	AdminService.class, BaseResource.class, EndecaAdapter.class,
	AdapterManager.class, UriInfo.class,
	UriBuilder.class, EndecaLoader.class })
public class TestAdminResource extends TestCase {

	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Admin");
	{
		LoggerUtil.setLogger(LOGGER);
	}
	AdminResource adminResource = new AdminResource();
	BaseResource baseResource = new BaseResource();
	MultivaluedMap<String, String> reqHeaders = new MultivaluedMapImpl();
	Map<String, String> endecaPropertiesMap = new HashMap<String, String>();

	UriInfo uriInfo;
	HttpHeaders httpHeader;
	URI uri = null;
	UriBuilder uriBuilder;
	Response expectedResponseData = null;
	Response actualResponseData = null;
	/**
	 * 
	 * @throws Exception
	 *             throws Exception
	 */
	@Before
	public final void setUp() throws Exception {
		this.reqHeaders.add("Correlation-Id", "1234567891234567");
		final EndecaLoader endecaLoader = PowerMockito.mock(EndecaLoader.class);
		this.adminResource.setEndecaLoader(endecaLoader);
		this.endecaPropertiesMap.put(EndecaConstants.CATALOG_ROOT_DIMENSION_VALUE, "4294967294");
		this.endecaPropertiesMap.put(EndecaConstants.CATALOG_HOME_DIMENSION_VALUE, "4294922263");
			PowerMockito.when(endecaLoader.getEndecaPropertiesMap()).thenReturn(this.endecaPropertiesMap);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.resource.AdminResource#populateCatalogProductsInCache(HttpHeaders)}
	 */
	@Test(expected = Exception.class)
	public final void testPopulateCatalogProductsInCacheForAdapterException() {
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.adminResource.setServiceManager(serviceManager);
		PowerMockito.mock(Response.class);
		final Response actualResponseData;
		final Response expectedResponseData;
		PowerMockito.mock(Catalog.class);
		final HttpHeaders httpHeaders = PowerMockito.mock(HttpHeaders.class);
		PowerMockito.when(httpHeaders.getRequestHeaders()).thenReturn(
				this.reqHeaders);
		final AdminService adminService = PowerMockito.mock(AdminService.class);
		PowerMockito.when(serviceManager.getService()).thenReturn(adminService);
		PowerMockito.mockStatic(BaseResource.class);
		final BaseResource baseResource = PowerMockito.mock(BaseResource.class);
		final Map<String, String> uriMap = TestAdminResourceUtil.getUriMap();
		final String correlationId = "1234567891234567";
		final String responseType = "XML";
		PowerMockito.when(
				baseResource.getResponseTypeForURIParam(uriMap, correlationId))
				.thenReturn(responseType);
		final ResponseBuilder responseBuilder = new ResponseBuilderImpl();
		responseBuilder.status(204);
		expectedResponseData = responseBuilder.build();
		PowerMockito.when(
				this.adminResource.populateCatalogProductsInCache(httpHeaders))
				.thenThrow(
						new AdapterException("500", "Test JUnit Error", "500",
								correlationId));
		actualResponseData = this.adminResource
				.populateCatalogProductsInCache(httpHeaders);
		assertNotSame(expectedResponseData.getStatus(), actualResponseData.getStatus());
//		assertEquals(expectedResponseData.getStatus(),
//				actualResponseData.getStatus());

	}

	/**
	 * Test method for
	 * {@link com.belk.api.resource.AdminResource#updatePropertiesMap(HttpHeaders, UriInfo)}
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
		this.httpHeader = mock(HttpHeaders.class);

		when(this.httpHeader.getRequestHeaders()).thenReturn(this.reqHeaders);
		stub(this.uriInfo.getAbsolutePathBuilder()).toReturn(this.uriBuilder);
		when(this.uriInfo.getQueryParameters()).thenReturn(uriParameters);

		// ServiceManager is mocked to get the instance of the service.
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.adminResource.setServiceManager(serviceManager);

		// AdminService is mocked to get the instance of the service
		// and is passed
		// as return from call to service manager.
		final AdminService adminService = PowerMockito
				.mock(AdminService.class);

		PowerMockito.when(serviceManager.getService()).thenReturn(
				adminService);


		final ResponseBuilder responseBuilder = new ResponseBuilderImpl();
		responseBuilder.status(200);

		this.expectedResponseData = responseBuilder.build();

		this.actualResponseData = this.adminResource.updatePropertiesMap(
				this.httpHeader, this.uriInfo);

		assertEquals(this.expectedResponseData.getStatus(),
				this.actualResponseData.getStatus());
	}
}
