package com.belk.api.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.belk.api.adapter.manager.AdapterManager;
import com.belk.api.cache.CacheManager;
import com.belk.api.constants.EndecaConstants;
import com.belk.api.constants.RequestAttributeConstant;
import com.belk.api.exception.AdapterException;
import com.belk.api.exception.BaseException;
import com.belk.api.exception.ServiceException;
import com.belk.api.impl.EndecaAdapter;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.belk.api.mail.MailClient;
import com.belk.api.model.Dimension;
import com.belk.api.model.catalog.Catalog;
import com.belk.api.model.catalog.Category;
import com.belk.api.service.util.TestCatalogServiceImplUtil;
import com.belk.api.util.EndecaLoader;
import com.belk.api.util.ErrorLoader;
import com.belk.api.util.PropertyReloader;

/**
 * Unit Testing related to CatalogServiceImpl class is performed. <br />
 * {@link CatalogServiceImpl} class is written for testing methods in
 * {@link CatalogServiceImpl} The unit test cases evaluates the way the methods
 * behave for the inputs given.
 * 
 * <br />
 * The test cases associated with CatalogServiceImpl is written to make the code
 * intact by assuring the code test.
 * 
 * @author Mindtree
 * @date Dec 04, 2013
 * 
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ CatalogServiceImpl.class, Catalog.class, Dimension.class,
		AdapterManager.class, CacheManager.class, EndecaAdapter.class,
		Category.class, Validate.class, Jsoup.class, PropertyReloader.class, 
		ErrorLoader.class, EndecaLoader.class, MailClient.class })
public class TestCatalogServiceImpl extends TestCase {

	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}
	CatalogServiceImpl catalogServiceImpl = new CatalogServiceImpl();
	final String correlationId = "1234567891234567";
	Map<String, String> endecaPropertiesMap = new HashMap<String, String>();
	Map<String, String> errorPropertiesMap = new HashMap<>();

	@Override
	@Before
	public final void setUp() throws Exception {
		final EndecaLoader endecaLoader = PowerMockito.mock(EndecaLoader.class);
		this.catalogServiceImpl.setEndecaLoader(endecaLoader);
		this.endecaPropertiesMap.put(EndecaConstants.CATALOG_ROOT_DIMENSION_VALUE, "4294967294");
		this.endecaPropertiesMap.put(EndecaConstants.CATALOG_HOME_DIMENSION_VALUE, "4294922263");
		PowerMockito.when(endecaLoader.getEndecaPropertiesMap()).thenReturn(this.endecaPropertiesMap);
		
		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		this.errorPropertiesMap.put("11421", "An invalid parameter name has been submitted");
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(this.errorPropertiesMap);
		this.catalogServiceImpl.setErrorLoader(errorLoader);
	}
	/**
	 * Test method for
	 * {@link com.belk.api.service.impl.CatalogServiceImpl#getCatalog(java.util.Map,String)}
	 * with not null Dimension fetching from TestCatalogServiceImplUtil
	 * 
	 * @throws AdapterException
	 *             AdapterException
	 * @throws BaseException
	 *             BaseException
	 */
	@Test
	public final void testGetCatalogForNotNullDimension()
			throws AdapterException, BaseException {

		final Map<String, String> requestMapURI = TestCatalogServiceImplUtil
				.getRequestMapUri();

		final CacheManager cacheManager = PowerMockito.mock(CacheManager.class);
		this.catalogServiceImpl.setCacheManager(cacheManager);
		final Category category = PowerMockito.mock(Category.class);

		PowerMockito.when(
				cacheManager.get("catid", Category.class, this.correlationId))
				.thenReturn(category);
		final AdapterManager adapterManager = PowerMockito
				.mock(AdapterManager.class);
		this.catalogServiceImpl.setAdapterManager(adapterManager);
		final EndecaAdapter endecaAdapter = PowerMockito
				.mock(EndecaAdapter.class);
		PowerMockito.when(adapterManager.getAdapter())
				.thenReturn(endecaAdapter);
		

		final Map<Long, Dimension> resultDimensionList = TestCatalogServiceImplUtil
				.getResultDimensionList();
		PowerMockito.when(
				endecaAdapter.makeDimensionQuery(requestMapURI,
						this.correlationId)).thenReturn(resultDimensionList);
		final Catalog expectedCatalog = new Catalog();
		expectedCatalog.setCatalogId(null);
		final Catalog catalog = this.catalogServiceImpl.getCatalog(
				requestMapURI, this.correlationId);
		assertNotNull(catalog);
		assertEquals(catalog.getCatalogId(), expectedCatalog.getCatalogId());
	}

	/**
	 * Test method for
	 * {@link com.belk.api.service.impl.CatalogServiceImpl#getCatalog(java.util.Map,String)}
	 * tests for AdapterException
	 * 
	 * @throws AdapterException
	 *             AdapterException
	 * @throws BaseException
	 *             BaseException
	 */

	@Test(expected = Exception.class)
	public final void testGetCatalogForBaseException() throws AdapterException,
			BaseException {

		final Map<String, String> requestMapURI = TestCatalogServiceImplUtil
				.getRequestMapUri();
		final CacheManager cacheManager = PowerMockito.mock(CacheManager.class);
		this.catalogServiceImpl.setCacheManager(cacheManager);
		final Category category = PowerMockito.mock(Category.class);

		PowerMockito.when(
				cacheManager.get("catid", Category.class, this.correlationId))
				.thenReturn(category);
		final AdapterManager adapterManager = PowerMockito
				.mock(AdapterManager.class);
		this.catalogServiceImpl.setAdapterManager(adapterManager);
		final EndecaAdapter endecaAdapter = PowerMockito
				.mock(EndecaAdapter.class);
		PowerMockito.when(adapterManager.getAdapter())
				.thenReturn(endecaAdapter);
		final Catalog expectedCatalog = new Catalog();
		expectedCatalog.setCatalogId("catId");
		final Catalog catalog = this.catalogServiceImpl.getCatalog(
				requestMapURI, this.correlationId);
		assertNotNull(catalog.getCatalogId());
		assertEquals(catalog.getCatalogId(), expectedCatalog.getCatalogId());

	}

	/**
	 * Test method for
	 * {@link com.belk.api.service.impl.CatalogServiceImpl#getCatalog(java.util.Map,String)}
	 * 
	 * @throws AdapterException
	 *             AdapterException
	 * @throws BaseException
	 *             BaseException
	 */

	@Test(expected = Exception.class)
	public final void tsetGetCatalog() throws AdapterException, BaseException {

		final Map<String, String> requestMapURI = TestCatalogServiceImplUtil
				.getRequestMapUri();
		final CacheManager cacheManager = PowerMockito.mock(CacheManager.class);
		this.catalogServiceImpl.setCacheManager(cacheManager);
		final Category category = PowerMockito.mock(Category.class);

		PowerMockito.when(
				cacheManager.get("catid", Category.class, this.correlationId))
				.thenReturn(category);
		final AdapterManager adapterManager = PowerMockito
				.mock(AdapterManager.class);
		this.catalogServiceImpl.setAdapterManager(adapterManager);
		final EndecaAdapter endecaAdapter = PowerMockito
				.mock(EndecaAdapter.class);
		PowerMockito.when(adapterManager.getAdapter())
				.thenReturn(endecaAdapter);
		final Catalog expectedCatalog = new Catalog();
		expectedCatalog.setCatalogId("catId");
		final Catalog catalog = this.catalogServiceImpl.getCatalog(
				requestMapURI, this.correlationId);
		assertNotNull(catalog.getCatalogId());
		assertEquals(catalog.getCatalogId(), expectedCatalog.getCatalogId());

	}

	/**
	 * Test method for
	 * {@link com.belk.api.service.impl.CatalogServiceImpl#getCatalog(java.util.Map,String)}
	 * with not null category fetching from TestCatalogServiceImplUtil
	 * 
	 * @throws AdapterException
	 *             AdapterException
	 * @throws BaseException
	 *             BaseException
	 */

	@Test
	public final void testGetCatalogForNotNullCategory()
			throws AdapterException, BaseException {
		PowerMockito.mock(CatalogServiceImpl.class);

		final Map<String, String> requestMapURI = TestCatalogServiceImplUtil
				.getRequestMapUri();
		final CacheManager cacheManager = PowerMockito.mock(CacheManager.class);
		this.catalogServiceImpl.setCacheManager(cacheManager);
		Category category = new Category();
		category = TestCatalogServiceImplUtil.getCategory();

		PowerMockito.when(
				cacheManager.get(
						requestMapURI.get(RequestAttributeConstant.CATEGORY_ID),
						Category.class, this.correlationId)).thenReturn(
				category);
		final AdapterManager adapterManager = PowerMockito
				.mock(AdapterManager.class);
		this.catalogServiceImpl.setAdapterManager(adapterManager);
		final EndecaAdapter endecaAdapter = PowerMockito
				.mock(EndecaAdapter.class);
		PowerMockito.when(adapterManager.getAdapter())
				.thenReturn(endecaAdapter);
		final Catalog expectedCatalog = new Catalog();
		expectedCatalog.setCatalogId(null);
		final Catalog catalog = this.catalogServiceImpl.getCatalog(
				requestMapURI, this.correlationId);

		assertNotNull(catalog);
		assertEquals(catalog.getCatalogId(), expectedCatalog.getCatalogId());

	}

	/**
	 * Test method for
	 * {@link com.belk.api.service.impl.CatalogServiceImpl#getCatalog(java.util.Map,String)}
	 * with requestMapURI containing showproduct value as false
	 * 
	 * @throws AdapterException
	 *             AdapterException
	 * @throws BaseException
	 *             BaseException
	 */

	@Test
	public final void testGetCatalogForShowProduct() throws AdapterException,
			BaseException {

		final Map<String, String> requestMapURI = TestCatalogServiceImplUtil
				.getRequestMapUriShowProduct();
		final CacheManager cacheManager = PowerMockito.mock(CacheManager.class);
		this.catalogServiceImpl.setCacheManager(cacheManager);
		Category category = new Category();
		category = TestCatalogServiceImplUtil.getCategory();

		PowerMockito.when(
				cacheManager.get(
						requestMapURI.get(RequestAttributeConstant.CATEGORY_ID),
						Category.class, this.correlationId)).thenReturn(
				category);
		final AdapterManager adapterManager = PowerMockito
				.mock(AdapterManager.class);
		this.catalogServiceImpl.setAdapterManager(adapterManager);
		final EndecaAdapter endecaAdapter = PowerMockito
				.mock(EndecaAdapter.class);
		PowerMockito.when(adapterManager.getAdapter())
				.thenReturn(endecaAdapter);
		final Catalog expectedCatalog = new Catalog();
		expectedCatalog.setCatalogId(null);
		final Catalog catalog = this.catalogServiceImpl.getCatalog(
				requestMapURI, this.correlationId);
		assertNotNull(catalog);
		assertEquals(catalog.getCatalogId(), expectedCatalog.getCatalogId());

	}

	/**
	 * Test method for
	 * {@link com.belk.api.service.impl.CatalogServiceImpl#getCatalog(java.util.Map,String)}
	 * with null Dimension fetching from TestCatalogServiceImplUtil
	 * 
	 * @throws AdapterException
	 *             AdapterException
	 * @throws BaseException
	 *             BaseException
	 */

	@Test(expected = ServiceException.class)
	public final void testGetCatalogForNullDimension() throws AdapterException,
			BaseException {

		final Map<String, String> requestMapURI = TestCatalogServiceImplUtil
				.getRequestMapUri();

		final CacheManager cacheManager = PowerMockito.mock(CacheManager.class);
		this.catalogServiceImpl.setCacheManager(cacheManager);
		final Category category = PowerMockito.mock(Category.class);

		PowerMockito.when(
				cacheManager.get("catid", Category.class, this.correlationId))
				.thenReturn(category);
		final AdapterManager adapterManager = PowerMockito
				.mock(AdapterManager.class);
		this.catalogServiceImpl.setAdapterManager(adapterManager);
		final EndecaAdapter endecaAdapter = PowerMockito
				.mock(EndecaAdapter.class);
		PowerMockito.when(adapterManager.getAdapter())
				.thenReturn(endecaAdapter);

		final Map<Long, Dimension> resultDimensionList = TestCatalogServiceImplUtil
				.getResultDimensionListNullDimension();
		PowerMockito.when(
				endecaAdapter.makeDimensionQuery(requestMapURI,
						this.correlationId)).thenReturn(resultDimensionList);
		final Catalog expectedCatalog = new Catalog();
		expectedCatalog.setCatalogId("catId");
		final Catalog catalog = this.catalogServiceImpl.getCatalog(
				requestMapURI, this.correlationId);
		assertNotNull(catalog);
		assertEquals(catalog.getCatalogId(), expectedCatalog.getCatalogId());

	}

	/**
	 * Test method for
	 * {@link com.belk.api.service.impl.CatalogServiceImpl#getCatalog(java.util.Map,String)}
	 * with null category fetching from TestCatalogServiceImplUtil
	 * 
	 * @throws AdapterException
	 *             AdapterException
	 * @throws BaseException
	 *             BaseException
	 */

	@Test(expected = ServiceException.class)
	public final void testGetCatalogForNullCategory() throws AdapterException,
			BaseException {

		final Map<String, String> requestMapURI = TestCatalogServiceImplUtil
				.getRequestMapShowProductTrue();

		final CacheManager cacheManager = PowerMockito.mock(CacheManager.class);
		this.catalogServiceImpl.setCacheManager(cacheManager);
		final Category category = TestCatalogServiceImplUtil.getCategory();

		PowerMockito.when(
				cacheManager.get("catid", Category.class, this.correlationId))
				.thenReturn(category);
		final AdapterManager adapterManager = PowerMockito
				.mock(AdapterManager.class);
		this.catalogServiceImpl.setAdapterManager(adapterManager);
		final EndecaAdapter endecaAdapter = PowerMockito
				.mock(EndecaAdapter.class);
		PowerMockito.when(adapterManager.getAdapter())
				.thenReturn(endecaAdapter);

		final Map<Long, Dimension> resultDimensionList = TestCatalogServiceImplUtil
				.getResultDimensionList();
		PowerMockito.when(
				endecaAdapter.makeDimensionQuery(requestMapURI,
						this.correlationId)).thenReturn(resultDimensionList);
		final Catalog expectedCatalog = new Catalog();
		expectedCatalog.setCatalogId("catId");
		final Catalog catalog = this.catalogServiceImpl.getCatalog(
				requestMapURI, this.correlationId);

		assertNotNull(catalog);
		assertEquals(catalog.getCatalogId(), expectedCatalog.getCatalogId());
	}
/**
 *  Test method for
	 * {@link com.belk.api.service.impl.CatalogServiceImpl#updatePropertiesMap((java.util.Map,String)}
	 * testing the update feature for the Properties map
 * @throws Exception exception thrown on any execution violation
 */
	@Test
	public final void testUpdatePropertiesMap() throws Exception {
		final Map<String, List<String>>  updateRequestUriMap = new HashMap<String, List<String>>();
		final List<String> propertyList = new ArrayList<String>();
		propertyList.add("error.properties");
		updateRequestUriMap.put("filename", propertyList);
		final List<String> propertyList1 = new ArrayList<String>();
		propertyList1.add("11421:hello|11422:hi");
		updateRequestUriMap.put("propertieslist", propertyList1);
		final String correlationId = "1234567891234567";
		final Map<String, String> errorPropertiesMap = new HashMap<String, String>();
		errorPropertiesMap.put("11421", "An invalid parameter name has been submitted");
		errorPropertiesMap.put("11422", "An invalid parameter value has been submitted");
		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		this.catalogServiceImpl.setErrorLoader(errorLoader);
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(errorPropertiesMap);
		final PropertyReloader propertyReloader = PowerMockito.mock(PropertyReloader.class);
		this.catalogServiceImpl.setPropertyReloader(propertyReloader);
		final MailClient mailClient = PowerMockito.mock(MailClient.class);
		this.catalogServiceImpl.setMailClient(mailClient);
		PowerMockito.doNothing().when(mailClient, "sendEmail", updateRequestUriMap, correlationId, "success");
		this.catalogServiceImpl.updatePropertiesMap(correlationId, updateRequestUriMap);
		assertNotNull(this.catalogServiceImpl);
	}
	
	/**
	 * Test method for
	 * {@link com.belk.api.service.impl.CatalogServiceImpl#getCatalogAsStringForXML((java.util.Map,String)}
	 * testing the retrieval functionality of the Catalog for XML stored in the cache in the form of String
	 * @throws ServiceException Service Exception
	 * @throws AdapterException Adapter  Exception
	 * @throws BaseException Base Exception
	 */
	@Test
	public final void testCatalogAsStringForXML() throws ServiceException, AdapterException, BaseException {
		final String xmlResponse = "<Catalog><categories><category><categoryId>4294922263" 
	            + "</categoryId><name>categoryid</name><parentCategoryId></parentCategoryId>"
				+ "<categoryAttributes><attribute key=\"hasSubCategories\" value=\"\"/>" 
				+ "</categoryAttributes><subCategories></category></categories></Catalog>";
		final Map<String, String> requestURIMap = new HashMap<>();
		requestURIMap.put("catalogId", "888");
		requestURIMap.put("categoryid", "4294922263_XML");
		requestURIMap.put("format", "xml");
		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		this.catalogServiceImpl.setErrorLoader(errorLoader);
		final Map<String, String> errorPropertiesMap = new HashMap<String, String>();
		errorPropertiesMap.put("11421", "An invalid parameter name has been submitted");
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(errorPropertiesMap);
		final CacheManager cacheManager = PowerMockito.mock(CacheManager.class);
		this.catalogServiceImpl.setCacheManager(cacheManager);
		PowerMockito.when(cacheManager.get(requestURIMap.get("categoryid"), String.class, 
				this.correlationId)).thenReturn(xmlResponse);
		final String actualString = this.catalogServiceImpl.getCatalogAsString(requestURIMap, this.correlationId);
		assertEquals(actualString, xmlResponse);
						
	}
	
	/**
	 * Test method for
	 * {@link com.belk.api.service.impl.CatalogServiceImpl#getCatalogAsString((java.util.Map,String)}
	 * testing the retrieval functionality of the Catalog for JSON stored in the cache in the form of String
	 * @throws ServiceException Service Exception
	 * @throws AdapterException Adapter  Exception
	 * @throws BaseException Base Exception
	 */
	@Test
	public final void testCatalogAsStringForJSON() throws ServiceException, AdapterException, BaseException {
		final String jsonResponse = "{\"categories\":[{\"categoryId\":4294922263,\"name\":\"categoryid\",\""
				 + "parentCategoryId\":,\"categoryAttributes\":[{\"key\":"
				 + "\"hasSubCategories\",\"value\":\"\"}]}]}";
		final Map<String, String> requestURIMap = new HashMap<>();
		requestURIMap.put("catalogId", "888");
		requestURIMap.put("categoryid", "4294922263_JSON");
		requestURIMap.put("format", "json");
		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		this.catalogServiceImpl.setErrorLoader(errorLoader);
		final Map<String, String> errorPropertiesMap = new HashMap<String, String>();
		errorPropertiesMap.put("11421", "An invalid parameter name has been submitted");
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(errorPropertiesMap);
		final CacheManager cacheManager = PowerMockito.mock(CacheManager.class);
		this.catalogServiceImpl.setCacheManager(cacheManager);
		PowerMockito.when(cacheManager.get(requestURIMap.get("categoryid"), String.class,
				this.correlationId)).thenReturn(jsonResponse);
		final String actualString = this.catalogServiceImpl.getCatalogAsString(requestURIMap, this.correlationId);
		assertEquals(actualString, jsonResponse);
	}
	
	/**
	 * Test method for
	 * {@link com.belk.api.service.impl.CatalogServiceImpl#getCatalogAsString((java.util.Map,String)}
	 * testing the retrieval functionality of the Catalog for ServiceException when the dimension is null
	 * @throws ServiceException Service Exception
	 * @throws AdapterException Adapter  Exception
	 * @throws BaseException Base Exception
	 */
	@Test(expected = ServiceException.class)
	public final void testCatalogAsStringForNull() throws ServiceException, AdapterException, BaseException {
		final String xmlResponse = null;
		final Map<String, String> requestURIMap = new HashMap<>();
		requestURIMap.put("catalogId", "888");
		requestURIMap.put("categoryid", "4294922263_XML");
		requestURIMap.put("format", "xml");
		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		this.catalogServiceImpl.setErrorLoader(errorLoader);
		final Map<String, String> errorPropertiesMap = new HashMap<String, String>();
		errorPropertiesMap.put("11421", "An invalid parameter name has been submitted");
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(errorPropertiesMap);
		final CacheManager cacheManager = PowerMockito.mock(CacheManager.class);
		this.catalogServiceImpl.setCacheManager(cacheManager);
		PowerMockito.when(cacheManager.get(requestURIMap.get("categoryid"), 
				String.class, this.correlationId)).thenReturn(xmlResponse);
		final String actualString = this.catalogServiceImpl.getCatalogAsString(requestURIMap, this.correlationId);
		assertEquals(actualString, xmlResponse);
						
	}
}
