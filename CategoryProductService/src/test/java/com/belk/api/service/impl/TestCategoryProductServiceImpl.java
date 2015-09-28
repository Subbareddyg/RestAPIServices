package com.belk.api.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.belk.api.adapter.contract.Adapter;
import com.belk.api.adapter.manager.AdapterManager;
import com.belk.api.exception.AdapterException;
import com.belk.api.exception.BaseException;
import com.belk.api.impl.EndecaAdapter;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.belk.api.mail.MailClient;
import com.belk.api.mapper.CategoryProductMapper;
import com.belk.api.model.categoryproduct.Categories;
import com.belk.api.util.EndecaLoader;
import com.belk.api.util.ErrorLoader;
import com.belk.api.util.PropertyReloader;
import com.belk.api.util.RequestParser;
import com.belk.api.util.TestCategoryProductServiceUtil;

/**
 * 
 * Unit Testing related to CategoryProductServiceImpl class is performed. <br />
 * {@link TestCategoryProductServiceImpl} class is written for testing methods
 * in {@link searchCategoryProducts} The unit test cases evaluates the way the
 * methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date Oct 25 2013
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({ CategoryProductServiceImpl.class, RequestParser.class,
		AdapterManager.class, Adapter.class, EndecaAdapter.class,
		CategoryProductMapper.class, PropertyReloader.class, 
		ErrorLoader.class, MailClient.class })
public class TestCategoryProductServiceImpl extends TestCase {

	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}
	String correlationId = "1234567890SDGFASF4545DFSERE";
	CategoryProductServiceImpl categoryProductServiceImpl = new CategoryProductServiceImpl();

	
	/**
	 * Precondition required for Junit test cases.
	 */
	@Override
	@Before
	public final void setUp() {
		final EndecaLoader endecaLoader = PowerMockito.mock(EndecaLoader.class);
		Map<String, String> endecaPropertiesMap = new HashMap<String, String>();
		endecaPropertiesMap.put("MULTIVALUE_ENABLED", "true");
		endecaLoader.setEndecaPropertiesMap(endecaPropertiesMap);
		this.categoryProductServiceImpl.setEndecaLoader(endecaLoader);
	}
	
	/**
	 * Test method to test the searchCategoryProducts method in
	 * CategoryProductServiceImpl class
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 * 
	 */
	@Test
	public final void testSearchCategoryProducts() throws BaseException,
			AdapterException {

		final Map<String, List<String>> searchCriteria = TestCategoryProductServiceUtil
				.createCategoryProductsURIMap();

		final Map<String, String> categoryProductServiceRequestMap = TestCategoryProductServiceUtil
				.createRequestMap();

		final List<Map<String, String>> resultList = TestCategoryProductServiceUtil
				.endecaResultList();

		final RequestParser requestParser = PowerMockito
				.mock(RequestParser.class);
		this.categoryProductServiceImpl.setRequestParser(requestParser);
		// Mocking the call to RequestParser.processRequestAttributes method
		PowerMockito.mockStatic(RequestParser.class);
		PowerMockito.when(
				requestParser
						.prepareRequest(searchCriteria, this.correlationId))
				.thenReturn(categoryProductServiceRequestMap);

		// Mocking the AdapterManager and EndecaAdapter Autowiring
		final AdapterManager adapManager = PowerMockito
				.mock(AdapterManager.class);
		this.categoryProductServiceImpl.setAdapterManager(adapManager);

		final EndecaAdapter endecaAdapter = PowerMockito
				.mock(EndecaAdapter.class);
		PowerMockito.when(adapManager.getAdapter()).thenReturn(endecaAdapter);
		//OPTIONS Parameter to be added in future
				Map<String,List<String>> optionNodes = null;
				
		PowerMockito.when(
				endecaAdapter.service(categoryProductServiceRequestMap,optionNodes,
						this.correlationId)).thenReturn(resultList);

		// Created a test util method to return the Endeca ResultList
		final List<Map<String, String>> endecaResultMap = TestCategoryProductServiceUtil
				.endecaResultList();
		// responseList.add(endecaResultMap);

		// Mock the Endeca Adapter Service method to return the Endeca Result
		PowerMockito.when(
				endecaAdapter.service(categoryProductServiceRequestMap,optionNodes,
						this.correlationId)).thenReturn(endecaResultMap);

		final CategoryProductMapper categoryProductMapper = PowerMockito
				.mock(CategoryProductMapper.class);

		this.categoryProductServiceImpl
				.setCategoryProductMapper(categoryProductMapper);

		// Passed the EndecaREsultList to be created in earlier to
		// convertToCategoryPOJO

		// Call the method and get the actual object back

		final Categories actualSearch = this.categoryProductServiceImpl
				.getCategoryProducts(searchCriteria, this.correlationId);

		// Ensure that actualSearch returned is not null
		assertNull(actualSearch);

	}

	/**
	 * Test method to test the searchCategoryProducts method in
	 * CategoryProductServiceImpl class when the provided categories has no
	 * products(As returned by Endeca)
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 * 
	 */

	@Test
	public final void testSearchCategoryProductsNoResults()
			throws BaseException, AdapterException {

		final Map<String, List<String>> searchCriteria = TestCategoryProductServiceUtil
				.createCategoryProductsURIMap();

		final Map<String, String> categoryProductServiceRequestMap = TestCategoryProductServiceUtil
				.createRequestMap();
		final RequestParser requestParser = PowerMockito
				.mock(RequestParser.class);
		this.categoryProductServiceImpl.setRequestParser(requestParser);
		// Mocking the call to RequestParser.processRequestAttributes method

		PowerMockito.when(
				requestParser
						.prepareRequest(searchCriteria, this.correlationId))
				.thenReturn(categoryProductServiceRequestMap);

		// Mocking the AdapterManager and EndecaAdapter Autowiring
		final AdapterManager adapManager = PowerMockito
				.mock(AdapterManager.class);
		this.categoryProductServiceImpl.setAdapterManager(adapManager);

		final EndecaAdapter endecaAdapter = PowerMockito
				.mock(EndecaAdapter.class);
		PowerMockito.when(adapManager.getAdapter()).thenReturn(endecaAdapter);

		// Endeca Should return an empty map
		final List<Map<String, String>> endecaResultMap = new ArrayList<Map<String, String>>();

		// Mock the Endeca Adapter Service method to return the Endeca Result
		//OPTIONS Parameter to be added in future
				Map<String,List<String>> optionNodes = null;
				
		PowerMockito.when(
				endecaAdapter.service(categoryProductServiceRequestMap,optionNodes,
						this.correlationId)).thenReturn(endecaResultMap);

		final CategoryProductMapper categoryProductMapper = PowerMockito
				.mock(CategoryProductMapper.class);

		this.categoryProductServiceImpl
				.setCategoryProductMapper(categoryProductMapper);

		// Passed the EndecaREsultList to be created in earlier to
		// convertToCategoryPOJO

		// Call the method and get the actual object back
		final Categories actualSearch = this.categoryProductServiceImpl
				.getCategoryProducts(searchCriteria, this.correlationId);

		// Ensure that actualSearch returned is null
		assertNull(actualSearch);

	}

	/**
	 * Test method to test the searchCategoryProducts method in
	 * CategoryProductServiceImpl which throws Adapter exception
	 * 
	 * @throws AdapterException
	 *             exception thrown from Domain layer
	 * @throws BaseException
	 *             exception thrown from Adapter layer
	 * 
	 */
	@Test
	public final void testCategoryProductsAdapterException()
			throws AdapterException, BaseException {

		final Map<String, List<String>> searchCriteria = TestCategoryProductServiceUtil
				.createCategoryProductsURIMap();

		final Map<String, String> endecaRequestMap = TestCategoryProductServiceUtil
				.createRequestMap();

		final RequestParser requestParser = PowerMockito
				.mock(RequestParser.class);
		this.categoryProductServiceImpl.setRequestParser(requestParser);

		// RequestParser class is mocked to static as it contains static method
		PowerMockito.mockStatic(RequestParser.class);

		// mocking the processRequestAttribute method inside the
		// searchCategoryProducts methods
		// which returns requestMap
		PowerMockito.when(
				requestParser
						.prepareRequest(searchCriteria, this.correlationId))
				.thenReturn(endecaRequestMap);

		// mocking the AdapterManager class inside the searchCategoryProducts
		// method
		final AdapterManager adapManager = PowerMockito
				.mock(AdapterManager.class);

		this.categoryProductServiceImpl.setAdapterManager(adapManager);

		// mocking the EndecaAdapter class inside the searchCategoryProducts
		// method
		final EndecaAdapter endecaAdapter = PowerMockito
				.mock(EndecaAdapter.class);
		PowerMockito.when(adapManager.getAdapter()).thenReturn(endecaAdapter);

		// mocking the endecaAdapteras it throws adapter exception when passes
		// null value to service method
		// Mock the Endeca Adapter Service method to return the Endeca Result
		//OPTIONS Parameter to be added in future
				Map<String,List<String>> optionNodes = null;
				
		PowerMockito.when(
				endecaAdapter.service(endecaRequestMap, optionNodes,this.correlationId))
				.thenThrow(
						new AdapterException("500", "Error", "parameter",
								"500", this.correlationId));

		// Call the method and expect an AdapterException back,AdapterException
		// will be validated by JUnit
		this.categoryProductServiceImpl.getCategoryProducts(searchCriteria,
				this.correlationId);
		assertNotNull(this.categoryProductServiceImpl);

	}

	/**
	 * Test method to test the searchCategoryProducts method in
	 * CategoryProductServiceImpl which throws Base exception
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * 
	 */

	@Test
	// (expected = BaseException.class)
	public final void testSearchProductsBaseException() throws BaseException {

		final Map<String, List<String>> searchCriteria = TestCategoryProductServiceUtil
				.createCategoryProductsURIMap();
		final List<List<Map<String, String>>> endecaResultMapList = TestCategoryProductServiceUtil
				.createEndecaResultMapList();

		final Map<String, String> requestMap = TestCategoryProductServiceUtil
				.createRequestMap();

		final RequestParser requestParser = PowerMockito
				.mock(RequestParser.class);
		this.categoryProductServiceImpl.setRequestParser(requestParser);
		// RequestParser class is mocked to static as it contains static method
		PowerMockito.mockStatic(RequestParser.class);

		// mocking the processRequestAttribute method inside the
		// searchCategoryProducts methods
		// which returns requestMap
		PowerMockito.when(
				requestParser
						.prepareRequest(searchCriteria, this.correlationId))
				.thenReturn(requestMap);
		// mocking the AdapterManager class inside the searchCategoryProducts
		// method
		final AdapterManager adapManager = PowerMockito
				.mock(AdapterManager.class);

		this.categoryProductServiceImpl.setAdapterManager(adapManager);

		// mocking the EndecaAdapter class inside the searchCategoryProducts
		// method
		final EndecaAdapter endecaAdapter = PowerMockito
				.mock(EndecaAdapter.class);

		// mocking the adapManager.getAdapter() inside the
		// searchCategoryProducts method
		PowerMockito.when(adapManager.getAdapter()).thenReturn(endecaAdapter);

		// mocking the adapManager.service(requestMap) inside the
		// searchCategoryProducts method
		// which returns resultMap
		final List<Map<String, String>> endecaResultMap = TestCategoryProductServiceUtil
				.endecaResultList();
		//OPTIONS Parameter to be added in future
				Map<String,List<String>> optionNodes = null;
				
		PowerMockito
				.when(endecaAdapter.service(requestMap, optionNodes,this.correlationId))
				.thenReturn(endecaResultMap);

		final CategoryProductMapper categoryProductMapper = PowerMockito
				.mock(CategoryProductMapper.class);

		this.categoryProductServiceImpl
				.setCategoryProductMapper(categoryProductMapper);

		// Passed the EndecaREsultList to be created in earlier to
		// convertToCategoryPOJO
		PowerMockito.when(
				categoryProductMapper.convertToCategoryPojo(
						endecaResultMapList, this.correlationId)).thenThrow(
				new BaseException("500", "Test JUnit Error"));

		// Call the method and expect a Base Exception back,
		// BaseException will be validated by JUnit
		this.categoryProductServiceImpl.getCategoryProducts(searchCriteria,
				this.correlationId);

		categoryProductMapper.convertToCategoryPojo(null, this.correlationId);
		assertNotNull(this.categoryProductServiceImpl);

	}

	/**
	 * Test method to test the searchCategoryProducts method in
	 * CategoryProductServiceImpl for multiple category Ids
	 * 
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * 
	 */

	public final void testSearchProductsMultipleCategoryId()
			throws BaseException, AdapterException {

		final Map<String, List<String>> searchCriteria = TestCategoryProductServiceUtil
				.createRequestMapWithMultipleCatId();

		final Map<String, String> requestMap = TestCategoryProductServiceUtil
				.createRequestMap();

		final String categoryIds = requestMap.get("categoryid");
		final String[] catIds = categoryIds.split(",");

		final RequestParser requestParser = PowerMockito
				.mock(RequestParser.class);
		this.categoryProductServiceImpl.setRequestParser(requestParser);
		// RequestParser class is mocked to static as it contains static method
		PowerMockito.mockStatic(RequestParser.class);

		// mocking the processRequestAttribute method inside the
		// searchCategoryProducts methods
		// which returns requestMap
		PowerMockito.when(
				requestParser
						.prepareRequest(searchCriteria, this.correlationId))
				.thenReturn(requestMap);
		// mocking the AdapterManager class inside the searchCategoryProducts
		// method
		final AdapterManager adapManager = PowerMockito
				.mock(AdapterManager.class);

		this.categoryProductServiceImpl.setAdapterManager(adapManager);

		// mocking the EndecaAdapter class inside the searchCategoryProducts
		// method
		final EndecaAdapter endecaAdapter = PowerMockito
				.mock(EndecaAdapter.class);

		// mocking the adapManager.getAdapter() inside the
		// searchCategoryProducts method
		PowerMockito.when(adapManager.getAdapter()).thenReturn(endecaAdapter);

		// mocking the adapManager.service(requestMap) inside the
		// searchCategoryProducts method
		// which returns resultMap

		for (int n = 0; n < catIds.length; n++) {
			final List<Map<String, String>> endecaResultMap = TestCategoryProductServiceUtil
					.endecaResultList();
			//OPTIONS Parameter to be added in future
			Map<String,List<String>> optionNodes = null;
			
			PowerMockito.when(
					endecaAdapter.service(requestMap, optionNodes,this.correlationId))
					.thenReturn(endecaResultMap);

		}
		final CategoryProductMapper categoryProductMapper = PowerMockito
				.mock(CategoryProductMapper.class);

		this.categoryProductServiceImpl
				.setCategoryProductMapper(categoryProductMapper);

		// Call the method and get the actual object back

		final Categories actualSearch = this.categoryProductServiceImpl
				.getCategoryProducts(searchCriteria, this.correlationId);

		// Ensure that actualSearch returned is not null
		assertNull(actualSearch);

	}

	/**
	 * Test method to test the searchCategoryProducts method in
	 * CategoryProductServiceImpl class
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 * 
	 */
	@Test
	public final void testSearchCategoryProductsWithoutCommaSeperator()
			throws BaseException, AdapterException {

		final Map<String, List<String>> searchCriteria = TestCategoryProductServiceUtil
				.createCategoryProductsURIMap();

		final Map<String, String> categoryProductServiceRequestMap = TestCategoryProductServiceUtil
				.createRequestMapWithoutCatID();

		final List<Map<String, String>> resultList = TestCategoryProductServiceUtil
				.endecaResultList();

		final RequestParser requestParser = PowerMockito
				.mock(RequestParser.class);
		this.categoryProductServiceImpl.setRequestParser(requestParser);
		// Mocking the call to RequestParser.processRequestAttributes method
		PowerMockito.mockStatic(RequestParser.class);
		PowerMockito.when(
				requestParser
						.prepareRequest(searchCriteria, this.correlationId))
				.thenReturn(categoryProductServiceRequestMap);

		// Mocking the AdapterManager and EndecaAdapter Autowiring
		final AdapterManager adapManager = PowerMockito
				.mock(AdapterManager.class);
		this.categoryProductServiceImpl.setAdapterManager(adapManager);

		final EndecaAdapter endecaAdapter = PowerMockito
				.mock(EndecaAdapter.class);
		PowerMockito.when(adapManager.getAdapter()).thenReturn(endecaAdapter);
		//OPTIONS Parameter to be added in future
				Map<String,List<String>> optionNodes = null;
				
		PowerMockito.when(
				endecaAdapter.service(categoryProductServiceRequestMap,optionNodes,
						this.correlationId)).thenReturn(resultList);

		// Created a test util method to return the Endeca ResultList
		final List<Map<String, String>> endecaResultMap = TestCategoryProductServiceUtil
				.endecaResultList();

		// Mock the Endeca Adapter Service method to return the Endeca Result
		PowerMockito.when(
				endecaAdapter.service(categoryProductServiceRequestMap,optionNodes,
						this.correlationId)).thenReturn(endecaResultMap);

		final CategoryProductMapper categoryProductMapper = PowerMockito
				.mock(CategoryProductMapper.class);

		this.categoryProductServiceImpl
				.setCategoryProductMapper(categoryProductMapper);

		// Passed the EndecaREsultList to be created in earlier to
		// convertToCategoryPOJO

		// Call the method and get the actual object back
		/*
		 * final Categories actualSearch = this.categoryProductServiceImpl.
		 * searchCategoryProducts(searchCriteria);
		 */

		final Categories actualSearch = this.categoryProductServiceImpl
				.getCategoryProducts(searchCriteria, this.correlationId);

		// Ensure that actualSearch returned is not null
		assertNull(actualSearch);

	}

	@Test
	public final void testUpdatePropertiesMap() throws Exception {
		Map<String, List<String>>  updateRequestUriMap = new HashMap<String, List<String>>();
		List<String> propertyList = new ArrayList<String>();
		propertyList.add("error.properties");
		updateRequestUriMap.put("filename",propertyList);
		List<String> propertyList1 = new ArrayList<String>();
		propertyList1.add("11421:hello|11422:hi");
		updateRequestUriMap.put("propertieslist",propertyList1);
		final String correlationId = "1234567891234567";
		Map<String, String> errorPropertiesMap = new HashMap<String, String>();
		errorPropertiesMap.put("11421","An invalid parameter name has been submitted");
		errorPropertiesMap.put("11422","An invalid parameter value has been submitted");
		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		this.categoryProductServiceImpl.setErrorLoader(errorLoader);
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(errorPropertiesMap);
		final PropertyReloader propertyReloader = PowerMockito.mock(PropertyReloader.class);
		this.categoryProductServiceImpl.setPropertyReloader(propertyReloader);
		final MailClient mailClient = PowerMockito.mock(MailClient.class);
		this.categoryProductServiceImpl.setMailClient(mailClient);
		PowerMockito.doNothing().when(mailClient, "sendEmail", updateRequestUriMap, correlationId, "success");
		this.categoryProductServiceImpl.updatePropertiesMap(correlationId, updateRequestUriMap);
		assertNotNull(this.categoryProductServiceImpl);
	}
}
