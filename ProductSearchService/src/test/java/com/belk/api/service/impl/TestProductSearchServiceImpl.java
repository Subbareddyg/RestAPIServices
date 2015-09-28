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
import com.belk.api.exception.ServiceException;
import com.belk.api.impl.EndecaAdapter;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.belk.api.mail.MailClient;
import com.belk.api.mapper.SearchMapper;
import com.belk.api.model.productsearch.Search;
import com.belk.api.service.ProductSearchService;
import com.belk.api.util.ErrorLoader;
import com.belk.api.util.PropertyReloader;
import com.belk.api.util.RequestParser;
import com.belk.api.util.TestProductSearchServiceUtil;

/**
 * Unit Testing related to ProductSearchServiceImpl class is performed. <br />
 * {@link TestProductSearchServiceImpl} class is written for testing methods in
 * {@link ProductSearchServiceImpl} The unit test cases evaluates the way the
 * methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date Oct 18 2013
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ ProductSearchServiceImpl.class, RequestParser.class,
	AdapterManager.class, Adapter.class, EndecaAdapter.class,
	SearchMapper.class, PropertyReloader.class,
	ErrorLoader.class, MailClient.class })
public class TestProductSearchServiceImpl extends TestCase {
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}
	String correlationId = "1234567891234567";
	String generateSearchReportMethod = "generateSearchReport";

	ProductSearchServiceImpl productSearchServcie = new ProductSearchServiceImpl();

	/**
	 * Precondition required for Junit test cases.
	 */
	@Override
	@Before
	public final void setUp() {
		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		Map<String, String> errorPropertiesMap = new HashMap<String, String>();
		errorPropertiesMap.put("11523", "internal server error");
		errorLoader.setErrorPropertiesMap(errorPropertiesMap);
		this.productSearchServcie.setErrorLoader(errorLoader);
	}

	/**
	 * Test method to test the searchProducts method in ProductSearchServiceImpl
	 * class
	 * 
	 * @throws Exception
	 *             exception thrown
	 */
	@Test
	public final void testSearchProducts() throws Exception {

		final Map<String, List<String>> searchCriteria = TestProductSearchServiceUtil
				.createURIMap();

		final Search expectedSearch = TestProductSearchServiceUtil
				.createSearchResults();

		final Map<String, String> requestMap = TestProductSearchServiceUtil
				.createRequestMap();

		final List<Map<String, String>> resultMap = TestProductSearchServiceUtil
				.createResultMap();

		final RequestParser requestParser = PowerMockito
				.mock(RequestParser.class);
		// RequestParser class is mocked to static as it contains static method
		PowerMockito.mockStatic(RequestParser.class);

		this.productSearchServcie.setRequestParser(requestParser);

		// mocking the processRequestAttribute method inside the searchProducts
		// method
		// which returns requestMap
		PowerMockito.when(
				requestParser.processRequestAttribute(searchCriteria,
						this.correlationId)).thenReturn(requestMap);

		// mocking the AdapterManager class inside the searchProducts method
		final AdapterManager adapManager = PowerMockito
				.mock(AdapterManager.class);

		this.productSearchServcie.setAdapterManager(adapManager);

		// mocking the EndecaAdapter class inside the searchProducts method
		final EndecaAdapter endecaAdapter = PowerMockito
				.mock(EndecaAdapter.class);

		// mocking the adapManager.getAdapter() inside the searchProducts method
		// which returns endecaAdapter
		PowerMockito.when(adapManager.getAdapter()).thenReturn(endecaAdapter);

		// mocking the adapManager.service(requestMap) inside the searchProducts
		// method
		// which returns resultMap
		PowerMockito
		.when(endecaAdapter.service(requestMap, null,this.correlationId))
		.thenReturn(resultMap);

		// mocking the SearchMapper class inside the searchProducts method
		final SearchMapper searchMapper = PowerMockito.mock(SearchMapper.class);
		this.productSearchServcie.setSearchMapper(searchMapper);

		// mocking the searchMapper.convertToSearchPojo(resultMap) inside the
		// searchProducts method
		PowerMockito
		.when(searchMapper.convertToSearchPojo(resultMap,
				this.correlationId)).thenReturn(expectedSearch);

		final Search actualSearch = this.productSearchServcie.searchProducts(
				searchCriteria, this.correlationId);

		// assert condition to check whether actualSearch is not null
		assertNotNull(actualSearch);

		// assert condition to check whether expectedSearch and actualSearch are
		// equal
		assertEquals(expectedSearch, actualSearch);

	}

	/**
	 * Test method to test the searchProducts method in ProductSearchServiceImpl
	 * which throws adapter exception
	 * 
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * @throws ServiceException
	 *             exception thrown from Service layer
	 */
	@Test
	public final void testSearchProductsAdapterException()
			throws AdapterException, BaseException, ServiceException {
		final Map<String, List<String>> searchCriteria = TestProductSearchServiceUtil
				.createURIMap();

		final Map<String, String> requestMap = TestProductSearchServiceUtil
				.createRequestMap();

		final RequestParser requestParser = PowerMockito
				.mock(RequestParser.class);
		// RequestParser class is mocked to static as it contains static method
		PowerMockito.mockStatic(RequestParser.class);

		this.productSearchServcie.setRequestParser(requestParser);
		// mocking the processRequestAttribute method inside the searchProducts
		// methods
		// which returns requestMap
		PowerMockito.when(
				requestParser.processRequestAttribute(searchCriteria,
						this.correlationId)).thenReturn(requestMap);

		// mocking the AdapterManager class inside the searchProducts method
		final AdapterManager adapManager = PowerMockito
				.mock(AdapterManager.class);

		this.productSearchServcie.setAdapterManager(adapManager);

		// mocking the EndecaAdapter class inside the searchProducts method
		final EndecaAdapter endecaAdapter = PowerMockito
				.mock(EndecaAdapter.class);

		// mocking the endecaAdapteras it throws adapter exception when passes
		// null value to service method
		PowerMockito.doThrow(
				new AdapterException("Adapter Exception", "", "",
						this.correlationId)).when(endecaAdapter);

		endecaAdapter.service(null, null,this.correlationId);

	}

	/**
	 * Test method to test the searchProducts method in ProductSearchServiceImpl
	 * which throws base exception
	 * 
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * @throws Exception
	 *             exception thrown
	 */
	@Test
	public final void testSearchProductsBaseException()
			throws AdapterException, BaseException, Exception {
		final Map<String, List<String>> searchCriteria = TestProductSearchServiceUtil
				.createURIMap();

		final Map<String, String> requestMap = TestProductSearchServiceUtil
				.createRequestMap();

		final List<Map<String, String>> resultMap = TestProductSearchServiceUtil
				.createResultMap();

		final RequestParser requestParser = PowerMockito
				.mock(RequestParser.class);
		// RequestParser class is mocked to static as it contains static method
		PowerMockito.mockStatic(RequestParser.class);

		this.productSearchServcie.setRequestParser(requestParser);
		// mocking the processRequestAttribute method inside the searchProducts
		// methods
		// which returns requestMap
		PowerMockito.when(
				requestParser.processRequestAttribute(searchCriteria,
						this.correlationId)).thenReturn(requestMap);
		// mocking the AdapterManager class inside the searchProducts method
		final AdapterManager adapManager = PowerMockito
				.mock(AdapterManager.class);

		this.productSearchServcie.setAdapterManager(adapManager);

		// mocking the EndecaAdapter class inside the searchProducts method
		final EndecaAdapter endecaAdapter = PowerMockito
				.mock(EndecaAdapter.class);

		// mocking the adapManager.getAdapter() inside the searchProducts method
		PowerMockito.when(adapManager.getAdapter()).thenReturn(endecaAdapter);

		// mocking the adapManager.service(requestMap) inside the searchProducts
		// method
		// which returns resultMap
		PowerMockito
		.when(endecaAdapter.service(requestMap, null,this.correlationId))
		.thenReturn(resultMap);

		// mocking the SearchMapper class inside the searchProducts method
		final SearchMapper searchMapper = PowerMockito.mock(SearchMapper.class);

		// mocking the searchMapper as it throws base exception when passes null
		// value to convertToSearchPojo method
		PowerMockito.doThrow(new BaseException()).when(searchMapper);

		searchMapper.convertToSearchPojo(null, this.correlationId);

	}

	/**
	 * Test method for the private method inside the ProductSearchServiceImpl
	 * class
	 * 
	 * @throws Exception
	 *             exception thrown
	 */
	@Test
	public final void testGenerateSearchReport() throws Exception {
		// mocking the ProductSearchService using powermockito spy for mocking
		// private method
		final ProductSearchService mockSpy = PowerMockito
				.spy(new ProductSearchServiceImpl());

		final Map<String, String> requestMap = TestProductSearchServiceUtil
				.createRequestMap();

		final List<Map<String, String>> resultMap = TestProductSearchServiceUtil
				.createResultMap();

		// mocking the private method inside the ProductSearchServiceImpl class
		PowerMockito.doNothing().when(mockSpy, this.generateSearchReportMethod,
				requestMap, resultMap);

	}

	/**
	 * Test method to test the searchProducts method in ProductSearchServiceImpl
	 * class
	 * 
	 * @throws Exception
	 *             exception thrown
	 */
	@Test
	public final void testSearchProductsForPrice() throws Exception {

		final Map<String, List<String>> searchCriteria = TestProductSearchServiceUtil
				.createURIMap();

		final Search expectedSearch = TestProductSearchServiceUtil
				.createSearchResults();

		final Map<String, String> requestMap = TestProductSearchServiceUtil
				.createRequestMapWithNoPrice();

		final List<Map<String, String>> resultMap = TestProductSearchServiceUtil
				.createResultMap();

		final RequestParser requestParser = PowerMockito
				.mock(RequestParser.class);
		// RequestParser class is mocked to static as it contains static method
		PowerMockito.mockStatic(RequestParser.class);

		this.productSearchServcie.setRequestParser(requestParser);

		// mocking the processRequestAttribute method inside the searchProducts
		// method
		// which returns requestMap
		PowerMockito.when(
				requestParser.processRequestAttribute(searchCriteria,
						this.correlationId)).thenReturn(requestMap);

		// mocking the AdapterManager class inside the searchProducts method
		final AdapterManager adapManager = PowerMockito
				.mock(AdapterManager.class);

		this.productSearchServcie.setAdapterManager(adapManager);

		// mocking the EndecaAdapter class inside the searchProducts method
		final EndecaAdapter endecaAdapter = PowerMockito
				.mock(EndecaAdapter.class);

		// mocking the adapManager.getAdapter() inside the searchProducts method
		// which returns endecaAdapter
		PowerMockito.when(adapManager.getAdapter()).thenReturn(endecaAdapter);

		// mocking the adapManager.service(requestMap) inside the searchProducts
		// method
		// which returns resultMap
		PowerMockito
		.when(endecaAdapter.service(requestMap, null,this.correlationId))
		.thenReturn(resultMap);

		// mocking the SearchMapper class inside the searchProducts method
		final SearchMapper searchMapper = PowerMockito.mock(SearchMapper.class);
		this.productSearchServcie.setSearchMapper(searchMapper);

		// mocking the searchMapper.convertToSearchPojo(resultMap) inside the
		// searchProducts method
		PowerMockito
		.when(searchMapper.convertToSearchPojo(resultMap,
				this.correlationId)).thenReturn(expectedSearch);

		final Search actualSearch = this.productSearchServcie.searchProducts(
				searchCriteria, this.correlationId);

		// assert condition to check whether actualSearch is not null
		assertNotNull(actualSearch);

		// assert condition to check whether expectedSearch and actualSearch are
		// equal
		assertEquals(expectedSearch, actualSearch);

	}

	/**
	 * Test method to test the searchProducts method in ProductSearchServiceImpl
	 * class
	 * 
	 * @throws Exception
	 *             exception thrown
	 */
	@Test(expected = Exception.class)
	public final void testSearchProductsForBaseException() throws Exception {

		final Map<String, List<String>> searchCriteria = TestProductSearchServiceUtil
				.createURIMapForPriceLength();

		final Search expectedSearch = TestProductSearchServiceUtil
				.createSearchResults();

		final Map<String, String> requestMap = TestProductSearchServiceUtil
				.createRequestMap();

		final List<Map<String, String>> resultMap = TestProductSearchServiceUtil
				.createResultMap();

		final RequestParser requestParser = PowerMockito
				.mock(RequestParser.class);
		// RequestParser class is mocked to static as it contains static method
		PowerMockito.mockStatic(RequestParser.class);

		this.productSearchServcie.setRequestParser(requestParser);

		// mocking the processRequestAttribute method inside the searchProducts
		// method
		// which returns requestMap
		PowerMockito.when(
				requestParser.processRequestAttribute(searchCriteria,
						this.correlationId)).thenReturn(requestMap);

		// mocking the AdapterManager class inside the searchProducts method
		final AdapterManager adapManager = PowerMockito
				.mock(AdapterManager.class);

		this.productSearchServcie.setAdapterManager(adapManager);

		// mocking the EndecaAdapter class inside the searchProducts method
		final EndecaAdapter endecaAdapter = PowerMockito
				.mock(EndecaAdapter.class);

		// mocking the adapManager.getAdapter() inside the searchProducts method
		// which returns endecaAdapter
		PowerMockito.when(adapManager.getAdapter()).thenReturn(endecaAdapter);

		// mocking the adapManager.service(requestMap) inside the searchProducts
		// method
		// which returns resultMap
		PowerMockito
		.when(endecaAdapter.service(requestMap, null,this.correlationId))
		.thenReturn(resultMap);

		// mocking the SearchMapper class inside the searchProducts method
		final SearchMapper searchMapper = PowerMockito.mock(SearchMapper.class);
		this.productSearchServcie.setSearchMapper(searchMapper);

		// mocking the searchMapper.convertToSearchPojo(resultMap) inside the
		// searchProducts method
		PowerMockito
		.when(searchMapper.convertToSearchPojo(resultMap,
				this.correlationId)).thenReturn(expectedSearch);

		final Search actualSearch = this.productSearchServcie.searchProducts(
				searchCriteria, this.correlationId);

		// assert condition to check whether actualSearch is not null
		assertNotNull(actualSearch);

		// assert condition to check whether expectedSearch and actualSearch are
		// equal
		assertEquals(expectedSearch, actualSearch);

	}

	/**
	 * Test method to test the searchProducts method in ProductSearchServiceImpl
	 * class
	 * 
	 * @throws Exception
	 *             exception thrown
	 */
	@Test(expected = BaseException.class)
	public final void testSearchProductsForException() throws Exception {

		final Map<String, List<String>> searchCriteria = TestProductSearchServiceUtil
				.createURIMapForNumberFormatException();

		final Search expectedSearch = TestProductSearchServiceUtil
				.createSearchResults();

		final Map<String, String> requestMap = TestProductSearchServiceUtil
				.createRequestMap();

		final List<Map<String, String>> resultMap = TestProductSearchServiceUtil
				.createResultMap();

		final RequestParser requestParser = PowerMockito
				.mock(RequestParser.class);
		// RequestParser class is mocked to static as it contains static method
		PowerMockito.mockStatic(RequestParser.class);

		this.productSearchServcie.setRequestParser(requestParser);

		// mocking the processRequestAttribute method inside the searchProducts
		// method
		// which returns requestMap
		PowerMockito.when(
				requestParser.processRequestAttribute(searchCriteria,
						this.correlationId)).thenReturn(requestMap);

		// mocking the AdapterManager class inside the searchProducts method
		final AdapterManager adapManager = PowerMockito
				.mock(AdapterManager.class);

		this.productSearchServcie.setAdapterManager(adapManager);

		// mocking the EndecaAdapter class inside the searchProducts method
		final EndecaAdapter endecaAdapter = PowerMockito
				.mock(EndecaAdapter.class);

		// mocking the adapManager.getAdapter() inside the searchProducts method
		// which returns endecaAdapter
		PowerMockito.when(adapManager.getAdapter()).thenReturn(endecaAdapter);

		// mocking the adapManager.service(requestMap) inside the searchProducts
		// method
		// which returns resultMap
		PowerMockito
		.when(endecaAdapter.service(requestMap, null,this.correlationId))
		.thenReturn(resultMap);

		// mocking the SearchMapper class inside the searchProducts method
		final SearchMapper searchMapper = PowerMockito.mock(SearchMapper.class);
		this.productSearchServcie.setSearchMapper(searchMapper);

		// mocking the searchMapper.convertToSearchPojo(resultMap) inside the
		// searchProducts method
		PowerMockito
		.when(searchMapper.convertToSearchPojo(resultMap,
				this.correlationId)).thenReturn(expectedSearch);

		final Search actualSearch = this.productSearchServcie.searchProducts(
				searchCriteria, this.correlationId);

		// assert condition to check whether actualSearch is not null
		assertNotNull(actualSearch);

		// assert condition to check whether expectedSearch and actualSearch are
		// equal
		assertEquals(expectedSearch, actualSearch);

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
		this.productSearchServcie.setErrorLoader(errorLoader);
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(errorPropertiesMap);
		final PropertyReloader propertyReloader = PowerMockito.mock(PropertyReloader.class);
		this.productSearchServcie.setPropertyReloader(propertyReloader);
		final MailClient mailClient = PowerMockito.mock(MailClient.class);
		this.productSearchServcie.setMailClient(mailClient);
		PowerMockito.doNothing().when(mailClient, "sendEmail", updateRequestUriMap, correlationId, "success");
		this.productSearchServcie.updatePropertiesMap(correlationId, updateRequestUriMap);
		assertNotNull(this.productSearchServcie);
	}
}
