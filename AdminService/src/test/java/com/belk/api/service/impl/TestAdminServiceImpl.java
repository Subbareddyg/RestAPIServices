package com.belk.api.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response.ResponseBuilder;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.belk.api.adapter.manager.AdapterManager;
import com.belk.api.cache.CacheManager;
import com.belk.api.constants.EndecaConstants;
import com.belk.api.exception.BaseException;
import com.belk.api.impl.EndecaAdapter;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.belk.api.mail.MailClient;
import com.belk.api.model.Dimension;
import com.belk.api.model.catalog.Catalog;
import com.belk.api.model.catalog.Category;
import com.belk.api.transformer.xml.XMLProcessor;
import com.belk.api.util.EndecaLoader;
import com.belk.api.util.ErrorLoader;
import com.belk.api.util.PropertyReloader;
import com.belk.api.util.TestAdminServiceImplUtil;
import com.endeca.navigation.ENEQueryException;

/**
 * Unit Testing related to AdminServiceImpl class is performed. <br />
 * {@link AdminServiceImpl} class is written for testing methods in
 * {@link AdminServiceImpl} The unit test cases evaluates the way the methods
 * behave for the inputs given.
 * 
 * <br />
 * The test cases associated with AdminServiceImpl is written to make the code
 * intactv by assuring the code test.
 * 
 * @author Mindtree
 * @date Dec 04, 2013
 * 
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ AdminServiceImpl.class, Dimension.class,
		AdapterManager.class, EndecaAdapter.class, Category.class, PropertyReloader.class, 
		ErrorLoader.class, EndecaLoader.class, XMLProcessor.class, Catalog.class, MailClient.class })
public class TestAdminServiceImpl extends TestCase {

	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}
	ByteArrayOutputStream xmlResponseStream;
	String xmlResposne;
	byte[] bytes;
	Map<String, String> endecaPropertiesMap = new HashMap<String, String>();
	Map<String, String> errorPropertiesMap = new HashMap<>();
	AdminServiceImpl adminServiceImpl = new AdminServiceImpl();
	ErrorLoader errorLoader;

	@Mock
	ResponseBuilder responseBuilder;
	
/**
 * performs the implementation setup for the test cases 
 */
	@Before
	public final void setUp() {
		this.xmlResponseStream = new ByteArrayOutputStream();
		this.xmlResposne = "<?xml version='1.0' encoding='UTF-8'?><Catalog/>";
		
		 this.errorLoader = PowerMockito.mock(ErrorLoader.class);
		 this.errorPropertiesMap.put("11421", "An invalid parameter name has been submitted");
		 this.errorPropertiesMap.put("11422", "An invalid parameter value has been submitted");
		PowerMockito.when(this.errorLoader.getErrorPropertiesMap()).thenReturn(this.errorPropertiesMap);
		this.adminServiceImpl.setErrorLoader(this.errorLoader);
		
	}
	
	/**
	 * Test method for
	 * {@link com.belk.api.service.AdminServiceImpl#populateCategoriesInCache(java.util.Map, String)}
	 * 
	 * @throws BaseException
	 *             throws exception from Domain layer
	 * @throws ENEQueryException
	 *             throws exception from endeca
	 * @throws IOException 
	 */
	@Ignore
	public final void testGetCatalog() throws BaseException, ENEQueryException, IOException {
		final Map<String, Category> leafCategoryMap = TestAdminServiceImplUtil
				.getLeafCategoryMap();
		this.adminServiceImpl.setLeafCategoryMap(leafCategoryMap);
		final AdapterManager adapterManager = PowerMockito
				.mock(AdapterManager.class);
		this.adminServiceImpl.setAdapterManager(adapterManager);
		final CacheManager cacheManager = PowerMockito.mock(CacheManager.class);
		this.adminServiceImpl.setCacheManager(cacheManager);
		final EndecaAdapter endecaAdapter = PowerMockito
				.mock(EndecaAdapter.class);
		PowerMockito.when(adapterManager.getAdapter())
				.thenReturn(endecaAdapter);
		final EndecaLoader endecaLoader = PowerMockito.mock(EndecaLoader.class);
		this.adminServiceImpl.setEndecaLoader(endecaLoader);
		this.endecaPropertiesMap.put(EndecaConstants.CATALOG_ROOT_DIMENSION_VALUE, "4294967294");
		this.endecaPropertiesMap.put(EndecaConstants.CATALOG_HOME_DIMENSION_VALUE, "4294922263");
		PowerMockito.when(endecaLoader.getEndecaPropertiesMap()).thenReturn(this.endecaPropertiesMap);
		
		this.errorPropertiesMap.put("11421", "An invalid parameter name has been submitted");
		PowerMockito.when(this.errorLoader.getErrorPropertiesMap()).thenReturn(this.errorPropertiesMap);
		final Dimension dimension = PowerMockito.mock(Dimension.class);
		final Map<String, String> requestMapURI = TestAdminServiceImplUtil
				.getRequestMapUri();
		final String correlationId = "1234567891234567";
		final Map<Long, Dimension> resultDimensionList = TestAdminServiceImplUtil
				.getResultDimensionList();
		PowerMockito.when(
				endecaAdapter.makeDimensionQuery(requestMapURI, correlationId))
				.thenReturn(resultDimensionList);
		final XMLProcessor xmlProcessor = PowerMockito.mock(XMLProcessor.class);
		this.adminServiceImpl.setXmlProcessor(xmlProcessor);		
		final Catalog catalog = PowerMockito.mock(Catalog.class);
//		this.bytes = this.xmlResposne.getBytes();
//		this.xmlResponseStream.write(this.bytes);
//		stub(this.responseBuilder.toString()).toReturn(this.xmlResposne);
//				
//		PowerMockito.when(xmlProcessor.buildXMLResponse(catalog, catalog.getClass(), 
//		"catalog-binding",correlationId)).thenReturn(this.xmlResponseStream);
		
//		PowerMockito.when(xmlResponseStream.toByteArray()).thenReturn(xmlResposne);
		this.adminServiceImpl.populateCategoriesInCache(requestMapURI, correlationId);
		assertNotNull(this.adminServiceImpl);

	}

	/**
	 * Test method for
	 * {@link com.belk.api.service.AdminServiceImpl#populateCategoriesInCache(java.util.Map, String)}
	 * for BaseException
	 * 
	 * @throws BaseException
	 *             throws exception from Domain layer
	 * @throws ENEQueryException
	 *             throws exception from endeca
	 */
	@Test(expected = BaseException.class)
	public final void testGetCatalogForBaseException() throws BaseException,
			ENEQueryException {
		final Map<String, Category> leafCategoryMap = TestAdminServiceImplUtil
				.getLeafCategoryMap();
		this.adminServiceImpl.setLeafCategoryMap(leafCategoryMap);
		this.adminServiceImpl.getLeafCategoryMap();
		final AdapterManager adapterManager = PowerMockito
				.mock(AdapterManager.class);
		this.adminServiceImpl.setAdapterManager(adapterManager);
		final CacheManager cacheManager = PowerMockito.mock(CacheManager.class);
		this.adminServiceImpl.setCacheManager(cacheManager);
		final EndecaAdapter endecaAdapter = PowerMockito
				.mock(EndecaAdapter.class);
		PowerMockito.when(adapterManager.getAdapter())
				.thenReturn(endecaAdapter);
		final EndecaLoader endecaLoader = PowerMockito.mock(EndecaLoader.class);
		this.adminServiceImpl.setEndecaLoader(endecaLoader);
		this.endecaPropertiesMap.put(EndecaConstants.CATALOG_ROOT_DIMENSION_VALUE, "4294967294");
		this.endecaPropertiesMap.put(EndecaConstants.CATALOG_HOME_DIMENSION_VALUE, "4294922263");
		PowerMockito.when(endecaLoader.getEndecaPropertiesMap()).thenReturn(this.endecaPropertiesMap);
		
		this.errorPropertiesMap.put("11421", "An invalid parameter name has been submitted");
		PowerMockito.when(this.errorLoader.getErrorPropertiesMap()).thenReturn(this.errorPropertiesMap);
		PowerMockito.mock(Dimension.class);
		final Map<String, String> requestMapURI = TestAdminServiceImplUtil
				.getRequestMapUriForNullDimension();
		final String correlationId = "1234567891234567";
		final Map<Long, Dimension> resultDimensionList = TestAdminServiceImplUtil
				.getResultDimensionListForNullDimension();
		PowerMockito.when(
				endecaAdapter.makeDimensionQuery(requestMapURI, correlationId))
				.thenReturn(resultDimensionList);
		this.adminServiceImpl.populateCategoriesInCache(requestMapURI, correlationId);
		assertNotNull(this.adminServiceImpl);

	}

	/**
	 * Test method for
	 * {@link com.belk.api.service.AdminServiceImpl#convertDimensionToCategory(Dimension, Category,String,String)}
	 * 
	 * 
	 * @throws Exception
	 *             exception
	 */
	@Test
	public final void testConvertDimensionToCategory() throws Exception {
		final AdminServiceImpl mockSpy = PowerMockito
				.spy(new AdminServiceImpl());
		final Dimension dimension = TestAdminServiceImplUtil.getNullDimension();
		final Category category = new Category();
		final String showproduct = "true";
		final String correlationId = "1234@belk@mindtrre";
		Whitebox.invokeMethod(mockSpy, "convertDimensionToCategory", dimension,
				category, showproduct, correlationId);

	}

	/**
	 * Test method for
	 * {@link com.belk.api.service.AdminServiceImpl#updatePropertiesMap(String, Map)}
	 * 
	 * 
	 * @throws Exception
	 *             exception
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
		final PropertyReloader propertyReloader = PowerMockito.mock(PropertyReloader.class);
		this.adminServiceImpl.setPropertyReloader(propertyReloader);
		final MailClient mailClient = PowerMockito.mock(MailClient.class);
		this.adminServiceImpl.setMailClient(mailClient);
		PowerMockito.doNothing().when(mailClient, "sendEmail", updateRequestUriMap, correlationId, "success");
		this.adminServiceImpl.updatePropertiesMap(correlationId, updateRequestUriMap);
		assertNotNull(this.adminServiceImpl);
	}
	
}
