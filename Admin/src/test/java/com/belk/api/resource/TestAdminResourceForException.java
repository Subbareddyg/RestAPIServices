package com.belk.api.resource;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

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

import com.belk.api.adapter.manager.AdapterManager;
import com.belk.api.business.service.manager.ServiceManager;
import com.belk.api.constants.EndecaConstants;
import com.belk.api.impl.EndecaAdapter;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.belk.api.model.catalog.Catalog;
import com.belk.api.service.AdminService;
import com.belk.api.service.impl.AdminServiceImpl;
import com.belk.api.util.EndecaLoader;
import com.belk.api.util.TestAdminResourceUtil;
import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * 
 * @author Mindtree Date Dec 18th
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ AdminResource.class, ServiceManager.class, HttpHeaders.class,
		AdminService.class, BaseResource.class, EndecaAdapter.class,
		AdapterManager.class, EndecaLoader.class })
public class TestAdminResourceForException {

	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Admin");
	{
		LoggerUtil.setLogger(LOGGER);
	}

	AdminResource adminResource = new AdminResource();
	BaseResource baseResource = new BaseResource();
	AdminServiceImpl adminService = new AdminServiceImpl();
	MultivaluedMap<String, String> reqHeaders = new MultivaluedMapImpl();
	Map<String, String> endecaPropertiesMap = new HashMap<String, String>();

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
	 * tests the method PopulateCatalogProductsInCache for Service exception
	 */

	@Test
	public final void testPopulateCatalogProductsInCacheForServiceException() {
		final ServiceManager serviceManager = PowerMockito
				.mock(ServiceManager.class);
		this.adminResource.setServiceManager(serviceManager);
		final Response response = PowerMockito.mock(Response.class);
		final Response actualResponseData;
		final Response expectedResponseData;
		final Catalog catalog = PowerMockito.mock(Catalog.class);
		final HttpHeaders httpHeaders = PowerMockito.mock(HttpHeaders.class);
		PowerMockito.when(httpHeaders.getRequestHeaders()).thenReturn(
				this.reqHeaders);
		final AdminService adminServiceMock = PowerMockito
				.mock(AdminService.class);

		final AdapterManager adapterManagerMock = PowerMockito
				.mock(AdapterManager.class);
		this.adminService.setAdapterManager(adapterManagerMock);
		final EndecaAdapter endecaAdapterMock = PowerMockito
				.mock(EndecaAdapter.class);
		when(adapterManagerMock.getAdapter()).thenReturn(endecaAdapterMock);
		PowerMockito.when(serviceManager.getService()).thenReturn(
				this.adminService);
		final Map<String, String> uriMap = TestAdminResourceUtil.getUriMap();
		final String correlationId = "1234567891234567";
		final String responseType = "XML";
		// PowerMockito.when(
		final ResponseBuilder responseBuilder = new ResponseBuilderImpl();
		responseBuilder.status(200);
		expectedResponseData = responseBuilder.build();

		actualResponseData = this.adminResource
				.populateCatalogProductsInCache(httpHeaders);
		assertEquals(expectedResponseData.getStatus(),
				actualResponseData.getStatus());

	}

}
