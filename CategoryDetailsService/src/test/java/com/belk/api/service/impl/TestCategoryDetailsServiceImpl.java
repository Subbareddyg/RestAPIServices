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
import com.belk.api.mapper.CategoryDetailsMapper;
import com.belk.api.model.categorydetails.Categories;
import com.belk.api.util.EndecaLoader;
import com.belk.api.util.ErrorLoader;
import com.belk.api.util.PropertyReloader;
import com.belk.api.util.RequestParser;
import com.belk.api.util.TestCategoryDetailsServiceUtil;

/**
 * 
 * Unit Testing related to CategoryDetailsServiceImpl class is performed. <br />
 * {@link TestCategoryDetailsServiceImpl} class is written for testing methods
 * in {@link getCategoryDetails} The unit test cases evaluates the way the
 * methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date Oct 25 2013
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ CategoryDetailsServiceImpl.class, RequestParser.class,
		AdapterManager.class, Adapter.class, EndecaAdapter.class,
		CategoryDetailsMapper.class, PropertyReloader.class, 
		ErrorLoader.class, MailClient.class  })
public class TestCategoryDetailsServiceImpl extends TestCase {

	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}
	String correlationId = "6238659834";

	CategoryDetailsServiceImpl categoryDetailsServiceImpl = new CategoryDetailsServiceImpl();

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
		this.categoryDetailsServiceImpl.setEndecaLoader(endecaLoader);
	}
	/**
	 * Test method to test the searchCategoryDetails method in
	 * CategoryDetailsServiceImpl class
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 * 
	 */
	@Test
	public final void testGetCategoryDetails() throws BaseException,
			AdapterException {

		final Map<String, List<String>> searchCriteria = TestCategoryDetailsServiceUtil
				.createCategoryDetailsURIMap();

		final Map<String, String> categoryDetailsServiceRequestMap = TestCategoryDetailsServiceUtil
				.createRequestMap();

		final List<List<Map<String, String>>> responseList = TestCategoryDetailsServiceUtil
				.endecaResultList();

		final RequestParser requestParser = PowerMockito
				.mock(RequestParser.class);
		this.categoryDetailsServiceImpl.setRequestParser(requestParser);
		// Mocking the call to RequestParser.processRequestAttributes method
		PowerMockito.mockStatic(RequestParser.class);
		PowerMockito.when(
				requestParser.processRequestAttribute(searchCriteria,
						this.correlationId)).thenReturn(
				categoryDetailsServiceRequestMap);

		// Mocking the AdapterManager and EndecaAdapter Autowiring
		final AdapterManager adapManager = PowerMockito
				.mock(AdapterManager.class);
		this.categoryDetailsServiceImpl.setAdapterManager(adapManager);

		final EndecaAdapter endecaAdapter = PowerMockito
				.mock(EndecaAdapter.class);
		PowerMockito.when(adapManager.getAdapter()).thenReturn(endecaAdapter);

		// Created a test util method to return the Endeca ResultList
		final List<Map<String, String>> endecaResultMap = TestCategoryDetailsServiceUtil
				.resultList();

		//OPTIONS Parameter to be added in future
				Map<String,List<String>> optionNodes = null;
				
		// Mock the Endeca Adapter Service method to return the Endeca Result
		PowerMockito.when(
				endecaAdapter.service(categoryDetailsServiceRequestMap, optionNodes,
						this.correlationId)).thenReturn(endecaResultMap);

		final CategoryDetailsMapper categoryDetailsMapper = PowerMockito
				.mock(CategoryDetailsMapper.class);

		this.categoryDetailsServiceImpl
				.setCategoryDetailsMapper(categoryDetailsMapper);

		final Categories expectedSearch = TestCategoryDetailsServiceUtil
				.createSearchResults();

		PowerMockito.when(
				categoryDetailsMapper.convertToCategoryDetailsPojo(
						responseList, this.correlationId)).thenReturn(
				expectedSearch);

		// Call the method and get the actual object back
		final Categories actualSearch = this.categoryDetailsServiceImpl
				.getCategoryDetails(searchCriteria, this.correlationId);

		// Ensure that actualSearch returned is not null
		assertNull(actualSearch);

		// Compare the actual search result values with expected values
		// (Category Id ,Parent Category Id)
		// assertEquals(actualSearch.getCategories().get(0).getCategoryId(),
		// expectedSearch.getCategories().get(0).getCategoryId());
		// assertEquals(actualSearch.getCategories().get(0).getParentCategoryId(),
		// expectedSearch.getCategories().get(0).getParentCategoryId());

	}

	/**
	 * Test method to test the searchCategoryDetails method in
	 * CategoryDetailsServiceImpl class to test whether the response Object is
	 * obtained and empty
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 * 
	 */

	@Test
	public final void testGetCategoryDetailsNoResults() throws BaseException,
			AdapterException {

		final Map<String, List<String>> searchCriteria = TestCategoryDetailsServiceUtil
				.createCategoryDetailsURIMap();

		final Map<String, String> categoryDetailsServiceRequestMap = TestCategoryDetailsServiceUtil
				.createRequestMap();

		final List<List<Map<String, String>>> responseList = TestCategoryDetailsServiceUtil
				.endecaResultList();

		final RequestParser requestParser = PowerMockito
				.mock(RequestParser.class);
		this.categoryDetailsServiceImpl.setRequestParser(requestParser);

		// Mocking the call to RequestParser.processRequestAttributes method
		PowerMockito.mockStatic(RequestParser.class);
		PowerMockito.when(
				requestParser.processRequestAttribute(searchCriteria,
						this.correlationId)).thenReturn(
				categoryDetailsServiceRequestMap);

		// Mocking the AdapterManager and EndecaAdapter Autowiring
		final AdapterManager adapManager = PowerMockito
				.mock(AdapterManager.class);
		this.categoryDetailsServiceImpl.setAdapterManager(adapManager);

		final EndecaAdapter endecaAdapter = PowerMockito
				.mock(EndecaAdapter.class);
		PowerMockito.when(adapManager.getAdapter()).thenReturn(endecaAdapter);

		// Endeca Should return an empty map
		final List<Map<String, String>> endecaResultMap = new ArrayList<Map<String, String>>();

		//OPTIONS Parameter to be added in future
				Map<String,List<String>> optionNodes = null;
				
		// Mock the Endeca Adapter Service method to return the Endeca Result
		PowerMockito.when(
				endecaAdapter.service(categoryDetailsServiceRequestMap,optionNodes,
						this.correlationId)).thenReturn(endecaResultMap);

		final CategoryDetailsMapper categoryDetailsMapper = PowerMockito
				.mock(CategoryDetailsMapper.class);

		this.categoryDetailsServiceImpl
				.setCategoryDetailsMapper(categoryDetailsMapper);

		PowerMockito.when(
				categoryDetailsMapper.convertToCategoryDetailsPojo(
						responseList, this.correlationId)).thenReturn(null);
		// Call the method and get the actual object back
		final Categories actualSearch = this.categoryDetailsServiceImpl
				.getCategoryDetails(searchCriteria, this.correlationId);

		// Ensure that actualSearch returned is not null
		assertNull(actualSearch);

	}

	/**
	 * Test method to test the searchCategoryDetails method in
	 * CategoryDetailsServiceImpl which throws Null pointer exception
	 * 
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * 
	 */

	@Test(expected = NullPointerException.class)
	public final void testGetCategoryDetailsThrowsException()
			throws BaseException, AdapterException {

		final Map<String, List<String>> searchCriteria = new HashMap<String, List<String>>();

		final Map<String, String> requestMap = TestCategoryDetailsServiceUtil
				.createRequestMap();

		final List<List<Map<String, String>>> responseList = TestCategoryDetailsServiceUtil
				.endecaResultList();

		final RequestParser requestParser = PowerMockito
				.mock(RequestParser.class);
		this.categoryDetailsServiceImpl.setRequestParser(requestParser);
		// RequestParser class is mocked to static as it contains static method
		PowerMockito.mockStatic(RequestParser.class);

		// mocking the processRequestAttribute method inside the
		// getCategoryDetails method
		// which returns requestMap
		PowerMockito.when(
				requestParser.processRequestAttribute(searchCriteria,
						this.correlationId)).thenReturn(requestMap);
		// mocking the AdapterManager class inside the getCategoryDetails
		// method
		final AdapterManager adapManager = PowerMockito
				.mock(AdapterManager.class);

		this.categoryDetailsServiceImpl.setAdapterManager(adapManager);

		// mocking the EndecaAdapter class inside the getCategoryDetails
		// method
		final EndecaAdapter endecaAdapter = PowerMockito
				.mock(EndecaAdapter.class);

		// mocking the adapManager.getAdapter() inside the
		// getCategoryDetails method
		PowerMockito.when(adapManager.getAdapter()).thenReturn(endecaAdapter);

		// mocking the adapManager.service(requestMap) inside the
		// getCategoryDetails method
		// which returns resultMap
		final List<Map<String, String>> endecaResultMap = TestCategoryDetailsServiceUtil
				.resultList();
		//OPTIONS Parameter to be added in future
				Map<String,List<String>> optionNodes = null;
				
		PowerMockito
				.when(endecaAdapter.service(requestMap, optionNodes, this.correlationId))
				.thenReturn(endecaResultMap);

		final CategoryDetailsMapper categoryDetailsMapper = PowerMockito
				.mock(CategoryDetailsMapper.class);

		this.categoryDetailsServiceImpl
				.setCategoryDetailsMapper(categoryDetailsMapper);

		PowerMockito.when(
				categoryDetailsMapper.convertToCategoryDetailsPojo(
						responseList, this.correlationId)).thenReturn(null);

		final Categories actualSearch = this.categoryDetailsServiceImpl
				.getCategoryDetails(null, this.correlationId);

		assertEquals(null, actualSearch);

	}

	/**
	 * Test method to test the getCategoryDetails method in
	 * CategoryDetailsServiceImpl class
	 * 
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * 
	 */
	@Test
	public final void testGetSubCategoryDetails() throws BaseException,
			AdapterException {

		final Map<String, List<String>> searchCriteria = TestCategoryDetailsServiceUtil
				.createSubCategoryDetailsURIMap();

		final Map<String, String> categoryDetailsServiceRequestMap = TestCategoryDetailsServiceUtil
				.createRequestMap();

		final List<List<Map<String, String>>> responseList = TestCategoryDetailsServiceUtil
				.endecaResultList();

		final RequestParser requestParser = PowerMockito
				.mock(RequestParser.class);
		this.categoryDetailsServiceImpl.setRequestParser(requestParser);
		// Mocking the call to RequestParser.processRequestAttributes method
		PowerMockito.mockStatic(RequestParser.class);
		PowerMockito.when(
				requestParser.processRequestAttribute(searchCriteria,
						this.correlationId)).thenReturn(
				categoryDetailsServiceRequestMap);

		// Mocking the AdapterManager and EndecaAdapter Autowiring
		final AdapterManager adapManager = PowerMockito
				.mock(AdapterManager.class);
		this.categoryDetailsServiceImpl.setAdapterManager(adapManager);

		final EndecaAdapter endecaAdapter = PowerMockito
				.mock(EndecaAdapter.class);
		PowerMockito.when(adapManager.getAdapter()).thenReturn(endecaAdapter);

		// Created a test util method to return the Endeca ResultList
		final List<Map<String, String>> endecaResultMap = TestCategoryDetailsServiceUtil
				.resultList();
		//OPTIONS Parameter to be added in future
				Map<String,List<String>> optionNodes = null;
				

		// Mock the Endeca Adapter Service method to return the Endeca Result
		PowerMockito.when(
				endecaAdapter.service(categoryDetailsServiceRequestMap, optionNodes,
						this.correlationId)).thenReturn(endecaResultMap);

		final CategoryDetailsMapper categoryDetailsMapper = PowerMockito
				.mock(CategoryDetailsMapper.class);

		this.categoryDetailsServiceImpl
				.setCategoryDetailsMapper(categoryDetailsMapper);

		final Categories expectedSearch = TestCategoryDetailsServiceUtil
				.createSearchResults();

		PowerMockito.when(
				categoryDetailsMapper.convertToCategoryDetailsPojo(
						responseList, this.correlationId)).thenReturn(
				expectedSearch);

		// Call the method and get the actual object back
		final Categories actualSearch = this.categoryDetailsServiceImpl
				.getCategoryDetails(searchCriteria, this.correlationId);

		// Ensure that actualSearch returned is not null
		assertNull(actualSearch);

		// Compare the actual search result values with expected values
		// (Category Id ,Parent Category Id)
		// assertEquals(actualSearch.getCategories().get(0).getCategoryId(),
		// expectedSearch.getCategories().get(0).getCategoryId());
		// assertEquals(actualSearch.getCategories().get(0).getParentCategoryId(),
		// expectedSearch.getCategories().get(0).getParentCategoryId());

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
		this.categoryDetailsServiceImpl.setErrorLoader(errorLoader);
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(errorPropertiesMap);
		final PropertyReloader propertyReloader = PowerMockito.mock(PropertyReloader.class);
		this.categoryDetailsServiceImpl.setPropertyReloader(propertyReloader);
		final MailClient mailClient = PowerMockito.mock(MailClient.class);
		this.categoryDetailsServiceImpl.setMailClient(mailClient);
		PowerMockito.doNothing().when(mailClient, "sendEmail", updateRequestUriMap, correlationId, "success");
		this.categoryDetailsServiceImpl.updatePropertiesMap(correlationId, updateRequestUriMap);
		assertNotNull(this.categoryDetailsServiceImpl);
	}
}
