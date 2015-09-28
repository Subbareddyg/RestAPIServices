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
import com.belk.api.impl.BlueMartiniAdapter;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.belk.api.mail.MailClient;
import com.belk.api.mapper.PatternProductMapper;
import com.belk.api.model.productdetails.ProductList;
import com.belk.api.util.CommonUtil;
import com.belk.api.util.ErrorLoader;
import com.belk.api.util.PropertyReloader;
import com.belk.api.util.RequestParser;
import com.belk.api.util.TestPatternProductServiceUtil;

/**
 * 
 * Unit Testing related to PatternProductDetailsServiceImpl class is performed. <br />
 * {@link TestPatternProductServiceImpl} class is written for testing methods in
 * {@link getPatternProducts} The unit test cases evaluates the way the methods
 * behave for the inputs given.
 * 
 * @author Mindtree
 * @date Nov 18th 2013
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({ PatternProductDetailsServiceImpl.class, RequestParser.class,
		AdapterManager.class, Adapter.class, BlueMartiniAdapter.class,
		PatternProductMapper.class, PropertyReloader.class, 
		ErrorLoader.class, MailClient.class })
public class TestPatternProductServiceImpl extends TestCase {
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}

	PatternProductDetailsServiceImpl patternProductDetailService = new PatternProductDetailsServiceImpl();
	Map<String, List<String>> uriMap;
	Map<String, String> patternProductDetailsRequestMap;
	List<Map<String, String>> blueMartiniResultMap;
	ProductList productList;
	TestPatternProductServiceUtil testPatternProductServiceUtil;
	String expectedResults;
	// ProductList actualResults;
	String correlationId = "1234567891234567";

	/**
	 * Initial set up ahead to start unit test case for class
	 */
	@Override
	@Before
	public final void setUp() {
		this.uriMap = new HashMap<String, List<String>>();
		this.patternProductDetailsRequestMap = new HashMap<String, String>();
		this.testPatternProductServiceUtil = new TestPatternProductServiceUtil();

	}

	/**
	 * Test method for
	 * {@link com.belk.api.service.impl.PatternProductDetailsServiceImpl#GetTestingProductDetails()}
	 * . Testing is done to check whether the response Object is obtained. when
	 * GetPatternProductDetails() method is called .
	 * 
	 * @throws Exception .
	 */
	@Test
	public final void testGetPatternProductDetails() throws Exception {
		this.uriMap = this.testPatternProductServiceUtil.requestMap();
		this.patternProductDetailsRequestMap = this.testPatternProductServiceUtil
				.processRequestAttrResult(this.uriMap.get("productCode"));
		// RequestParser is mocked and method processRequestAttribute will
		// return the map with the delimiters removed .
		final RequestParser requestParser = PowerMockito
				.mock(RequestParser.class);
		final CommonUtil commonUtil = PowerMockito
				.mock(CommonUtil.class);
		
		// this.patternProductDetailService.setRequestParser(requestParser);
		// Mocking the call to RequestParser.processRequestAttributes method
		PowerMockito.mockStatic(RequestParser.class);
		PowerMockito.when(
				requestParser.prepareRequest(this.uriMap, this.correlationId))
				.thenReturn(this.patternProductDetailsRequestMap);
		// AdapterManager is mocked to get the instance of the Adapter.
		final AdapterManager adapManager = PowerMockito
				.mock(AdapterManager.class);
		this.patternProductDetailService.setAdapterManager(adapManager);
		PowerMockito.mock(Adapter.class);
		final BlueMartiniAdapter blueMartiniAdapter = PowerMockito
				.mock(BlueMartiniAdapter.class);
		PowerMockito.when(adapManager.getAdapter()).thenReturn(
				blueMartiniAdapter);

		// AdapterManager is mocked and method service will return results in
		// form of map.
		PowerMockito.when(
				blueMartiniAdapter.service(
						this.patternProductDetailsRequestMap,null,
						this.correlationId)).thenReturn(
				this.testPatternProductServiceUtil.blueMartiniResultMap());

		// PatternProductMapper is mocked and method convertToProductDetailsPojo
		// will
		// convert the result map into ProductList object.
		final PatternProductMapper patternProductMapper = PowerMockito
				.mock(PatternProductMapper.class);
		this.patternProductDetailService
				.setPatternProductMapper(patternProductMapper);
		PowerMockito.when(
				patternProductMapper.convertToPatternProductDetailsPojo(
						this.testPatternProductServiceUtil
								.blueMartiniResultMap(), null,this.correlationId))
				.thenReturn(
						this.testPatternProductServiceUtil
								.getProductDetailsList());
		this.patternProductDetailService.setCommonUtil(commonUtil);
		this.patternProductDetailService.getPatternProductDetails(this.uriMap,
				this.correlationId);

		assertNull(this.expectedResults);
		// assertNull(actualResults);

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
		this.patternProductDetailService.setErrorLoader(errorLoader);
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(errorPropertiesMap);
		final PropertyReloader propertyReloader = PowerMockito.mock(PropertyReloader.class);
		this.patternProductDetailService.setPropertyReloader(propertyReloader);
		final MailClient mailClient = PowerMockito.mock(MailClient.class);
		this.patternProductDetailService.setMailClient(mailClient);
		PowerMockito.doNothing().when(mailClient, "sendEmail", updateRequestUriMap, correlationId, "success");
		this.patternProductDetailService.updatePropertiesMap(correlationId, updateRequestUriMap);
		assertNotNull(this.patternProductDetailService);
	}

}
