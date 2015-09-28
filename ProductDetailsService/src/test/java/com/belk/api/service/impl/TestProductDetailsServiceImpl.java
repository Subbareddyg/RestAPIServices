package com.belk.api.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.belk.api.adapter.contract.Adapter;
import com.belk.api.adapter.manager.AdapterManager;
import com.belk.api.constants.ErrorConstants;
import com.belk.api.exception.AdapterException;
import com.belk.api.impl.EndecaAdapter;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.belk.api.mail.MailClient;
import com.belk.api.mapper.ProductMapper;
import com.belk.api.model.productdetails.ProductList;
import com.belk.api.service.util.TestServiceUtil;
import com.belk.api.util.CommonUtil;
import com.belk.api.util.ErrorLoader;
import com.belk.api.util.PropertyReloader;
import com.belk.api.util.RequestParser;

/**
 * Unit Testing related to ProductDetailsServiceImpl class is performed. <br />
 * {@link TestProductDetailsServiceImpl} class is written for testing methods in
 * {@link getProductDetails} The unit test cases evaluates the way the methods
 * behave for the inputs given.
 * 
 * @author Mindtree
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ ProductDetailsServiceImpl.class, RequestParser.class,
		AdapterManager.class, Adapter.class, EndecaAdapter.class,
		ProductMapper.class, PropertyReloader.class, 
		ErrorLoader.class, MailClient.class })
public class TestProductDetailsServiceImpl extends TestCase {

	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}

	ProductDetailsServiceImpl productDetailService = new ProductDetailsServiceImpl();
	Map<String, List<String>> uriMap;
	Map<String, String> productDetailsRequestMap;
	List<Map<String, String>> endecaResultMap;
	ProductList prdList;
	TestServiceUtil testServiceUtil;
	String expectedResults;
	String actualResults;
	String correlationId = "1234567891234567";

	/**
	 * Initial set up ahead to start unit test case for class
	 */
	@Override
	@Before
	public final void setUp() {
		this.uriMap = new HashMap<String, List<String>>();
		this.productDetailsRequestMap = new HashMap<String, String>();
		this.testServiceUtil = new TestServiceUtil();

	}

	/**
	 * Test method for
	 * {@link com.belk.api.service.impl.ProductDetailsServiceImpl#GetProductDetails()}
	 * . Testing is done to check whether the response Object is obtained. when
	 * GetProductDetails() method is called .
	 * 
	 * @throws Exception
	 *             exception thrown
	 */
	@Ignore
	public final void testGetProductDetails() throws Exception {
		this.uriMap = this.testServiceUtil.requestMap();
		this.productDetailsRequestMap = this.testServiceUtil
				.processRequestAttrResult(this.uriMap.get("styleupc"));
		// RequestParser is mocked and method processRequestAttribute will
		// return the map with the delimiters removed .
		final RequestParser requestParser = PowerMockito
				.mock(RequestParser.class);
		this.productDetailService.setRequestParser(requestParser);
		PowerMockito.when(
				requestParser.prepareRequest(this.uriMap, this.correlationId))
				.thenReturn(this.productDetailsRequestMap);
		// AdapterManager is mocked to get the instance of the Adapter.
		final AdapterManager adapManager = PowerMockito
				.mock(AdapterManager.class);
		this.productDetailService.setAdapterManager(adapManager);
		PowerMockito.mock(Adapter.class);
		final EndecaAdapter endecaAdapter = PowerMockito
				.mock(EndecaAdapter.class);
		PowerMockito.when(adapManager.getAdapter()).thenReturn(endecaAdapter);

		// AdapterManager is mocked and method service will return results in
		// form of map.
		PowerMockito.when(
				endecaAdapter.service(this.productDetailsRequestMap,null,
						this.correlationId)).thenReturn(
				this.testServiceUtil.endecaResultMap());

		// ProductMapper is mocked and method convertToProductDetailsPojo will
		// convert the result map into ProductList object.
		final ProductMapper productMapper = PowerMockito
				.mock(ProductMapper.class);
		this.productDetailService.setProductMapper(productMapper);
		PowerMockito.when(
				productMapper.convertToProductDetailsPojo(
						this.testServiceUtil.endecaResultMap(),null,
						this.correlationId)).thenReturn(
				this.testServiceUtil.getProductDetailsList());
		this.actualResults = this.productDetailService
				.getProductDetails(this.uriMap, this.correlationId)
				.getProducts().get(0).getProductCode();
		this.expectedResults = this.testServiceUtil.getProductDetailsList()
				.getProducts().get(0).getProductCode();
		assertEquals(this.expectedResults, this.actualResults);

	}
	
	/**
	 * Test method for
	 * {@link com.belk.api.service.impl.ProductDetailsServiceImpl#GetProductDetails()}
	 * . Testing is done to check whether the response Object is obtained and
	 * empty. when GetProductDetails() method is called .
	 * 
	 * @throws Exception
	 *             exception thrown
	 */
	@Test
	public final void testGetProductDetailsEmpty() throws Exception {

		this.uriMap = this.testServiceUtil.requestMap();

		final List<Map<String, String>> resultMapEmpty = new ArrayList<Map<String, String>>();
		// RequestParser is mocked and method processRequestAttribute will
		// return the map with the delimiters removed .
		final RequestParser requestParser = PowerMockito
				.mock(RequestParser.class);
		this.productDetailService.setRequestParser(requestParser);
		final CommonUtil commonUtil = PowerMockito
				.mock(CommonUtil.class);
		this.productDetailService.setCommonUtil(commonUtil);
		

		// AdapterManager is mocked to get the instance of the Adapter.
		final AdapterManager adapManager = PowerMockito
				.mock(AdapterManager.class);
		this.productDetailService.setAdapterManager(adapManager);
		PowerMockito.mock(Adapter.class);
		final EndecaAdapter endecaAdapter = PowerMockito
				.mock(EndecaAdapter.class);
		PowerMockito.when(adapManager.getAdapter()).thenReturn(endecaAdapter);

		// AdapterManager is mocked and method service will return results in
		// form of map.
		PowerMockito.when(
				endecaAdapter.service(this.productDetailsRequestMap,null,
						this.correlationId)).thenReturn(this.endecaResultMap);

		// ProductMapper is mocked and method convertToProductDetailsPojo will
		// convert the result map into ProductList object.
		final ProductMapper productMapper = PowerMockito
				.mock(ProductMapper.class);
		this.productDetailService.setProductMapper(productMapper);
		PowerMockito.when(
				productMapper.convertToProductDetailsPojo(resultMapEmpty,null,
						this.correlationId)).thenReturn(null);
		this.productDetailService.getProductDetails(this.uriMap,
				this.correlationId);
		assertEquals(null, this.actualResults);

	}

	/**
	 * Test method for
	 * {@link com.belk.api.service.impl.ProductDetailsServiceImpl#GetProductDetails()}
	 * . Testing is done to check whether the response Object is obtained and
	 * empty. when GetProductDetails() method is called and throws Adapater
	 * Exception .
	 * 
	 * @throws Exception
	 *             exception thrown
	 */
	@Test(expected = NullPointerException.class)
	public final void testGetPrdDetthrowsExpection() throws Exception {
		this.uriMap = this.testServiceUtil.requestMap();

		final List<Map<String, String>> resultMapEmpty = new ArrayList<Map<String, String>>();
		// RequestParser is mocked and method processRequestAttribute will
		// return the map with the delimiters removed .
		final RequestParser requestParser = PowerMockito
				.mock(RequestParser.class);
		PowerMockito.when(
				requestParser.processRequestAttribute(this.uriMap,
						this.correlationId)).thenReturn(
				this.productDetailsRequestMap);

		// AdapterManager is mocked to get the instance of the Adapter.
		final AdapterManager adapManager = PowerMockito
				.mock(AdapterManager.class);
		this.productDetailService.setAdapterManager(adapManager);
		PowerMockito.mock(Adapter.class);
		final EndecaAdapter endecaAdapter = PowerMockito
				.mock(EndecaAdapter.class);
		PowerMockito.when(adapManager.getAdapter()).thenReturn(endecaAdapter);

		// AdapterManager is mocked and method service will return results in
		// form of map.

		PowerMockito.doThrow(
				new AdapterException(
						ErrorConstants.HTTP_STATUS_CODE_EMPTY_SEARCH_RESULT,
						ErrorConstants.ERRORDESC_RECORD_NOT_FOUND, "parameter",
						"500", this.correlationId)).when(EndecaAdapter.class);
		PowerMockito.when(
				endecaAdapter.service(this.productDetailsRequestMap,null,
						this.correlationId)).thenReturn(this.endecaResultMap);

		// ProductMapper is mocked and method convertToProductDetailsPojo will
		// convert the result map into ProductList object.
		final ProductMapper productMapper = PowerMockito
				.mock(ProductMapper.class);
		this.productDetailService.setProductMapper(productMapper);
		PowerMockito.when(
				productMapper.convertToProductDetailsPojo(resultMapEmpty,null,
						this.correlationId)).thenReturn(null);
		assertEquals(null, this.productDetailService.getProductDetails(
				this.uriMap, this.correlationId));

	}
	
	/*public final void testUpdatePropertiesMap() throws Exception {
		Map<String, List<String>>  updateRequestUriMap = new HashMap<String, List<String>>();
		List<String> propertyList = new ArrayList<String>();
		propertyList.add("error.properties");
		updateRequestUriMap.put("filename",propertyList);
		List<String> propertyList1 = new ArrayList<String>();
		propertyList1.add("11421:hello|11422:hi");
		updateRequestUriMap.put("propertieslist",propertyList1);
		final String correlationId = "1234567891234567";
		final ErrorLoader errorLoader = new ErrorLoader();
		Map<String, String> errorPropertiesMap = new HashMap<String, String>();
		errorPropertiesMap.put("11421","An invalid parameter name has been submitted");
		errorPropertiesMap.put("11422","An invalid parameter value has been submitted");
		errorLoader.setErrorPropertiesMap(errorPropertiesMap);
		this.productDetailService.setErrorLoader(errorLoader);
		final PropertyReloader propertyReloader = PowerMockito.mock(PropertyReloader.class);
		this.productDetailService.setPropertyReloader(propertyReloader);
		final MailClient mailClient = PowerMockito.mock(MailClient.class);
		this.productDetailService.setMailClient(mailClient);
		this.productDetailService.updatePropertiesMap(correlationId, updateRequestUriMap);
		assertNotNull(this.productDetailService);
	}*/
}
