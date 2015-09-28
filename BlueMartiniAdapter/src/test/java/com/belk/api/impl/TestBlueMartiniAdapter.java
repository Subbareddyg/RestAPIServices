package com.belk.api.impl;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.belk.api.blueMartini.HttpClientUtil;
import com.belk.api.exception.AdapterException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.belk.api.util.BlueMartiniTestUtil;
import com.belk.api.util.ErrorLoader;
import com.belk.api.util.HttpConnectionLoader;

/**
 * Unit Testing related to Blue Martini Adapter class is performed. <br />
 * {@link TestBlueMartiniAdapter} class is written for testing methods in
 * {@link BlueMartiniAdapter} The unit test cases evaluates the way the methods
 * behave for the inputs given.
 * 
 * <br />
 * The test cases associated with BlueMartiniAdapter is written to make the code
 * intact by assuring the code test.
 * 
 * @author Mindtree
 * @date Nov 20th 2013
 * 
 * 
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({ BlueMartiniAdapter.class, HttpClientUtil.class,ErrorLoader.class })
public class TestBlueMartiniAdapter {

	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}
	String correlationId = "1234567891234567";
	BlueMartiniAdapter blueMartiniAdapter = new BlueMartiniAdapter();
	String requestor = "patternDetails";
	JSONObject blueMartiniResponse;

	/**
	 * 
	 * @throws AdapterException
	 *             From adapter layer
	 * 
	 */

	@Test
	public final void testService() throws AdapterException {
		ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		final Map<String, String> httpConnectionPropertiesMap = BlueMartiniTestUtil.requestParams(); 
		 
		 this.blueMartiniAdapter.setErrorLoader(errorLoader);
		
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(httpConnectionPropertiesMap);
		final BlueMartiniTestUtil blueMartiniTestUtil = new BlueMartiniTestUtil();

		// final BlueMartiniAdapter blueMartiniAdapterspy = PowerMockito.spy(new
		// BlueMartiniAdapter());
		final HttpClientUtil blueMartinihttpUtil = PowerMockito
				.mock(HttpClientUtil.class);

		PowerMockito.when(
				blueMartinihttpUtil
						.getHttpContent(this.requestor,
								blueMartiniTestUtil.requestParams(),
								this.correlationId)).thenReturn(
				blueMartiniTestUtil.blueMartiniResultList());

		this.blueMartiniResponse = blueMartinihttpUtil.getHttpContent(
				this.requestor, blueMartiniTestUtil.requestParams(),
				this.correlationId);
		this.blueMartiniAdapter.setHttpClientUtil(blueMartinihttpUtil);

		// final List<Map<String, String>> conversionResult =
		// this.blueMartiniAdapter.blueMartiniResultsTOListofMap(this.blueMartiniResponse,
		// this.correlationId);

		//OPTIONS Parameter to be added in future
				Map<String,List<String>> optionNodes = null;
				
		this.blueMartiniAdapter.service(blueMartiniTestUtil.requestParams(), optionNodes,
				this.correlationId);

	}

	/**
	 * Method to test the conversion of bluemartini results to List of Map
	 * 
	 * @throws AdapterException
	 *             From adapter layer
	 * 
	 */

	@Test
	public final void testBlueMartiniResultsTOListofMap()
			throws AdapterException {
		HttpConnectionLoader httpConnectionLoader = PowerMockito.mock(HttpConnectionLoader.class);
		ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		final Map<String, String> httpConnectionPropertiesMap = BlueMartiniTestUtil.requestParams(); 
		 
		 this.blueMartiniAdapter.setErrorLoader(errorLoader);
		
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(httpConnectionPropertiesMap);

		final BlueMartiniTestUtil blueMartiniTestUtil = new BlueMartiniTestUtil();

		PowerMockito.spy(new BlueMartiniAdapter());

		final HttpClientUtil blueMartinihttpUtil = PowerMockito
				.mock(HttpClientUtil.class);

		PowerMockito.when(
				blueMartinihttpUtil
						.getHttpContent(this.requestor,
								blueMartiniTestUtil.requestParams(),
								this.correlationId)).thenReturn(
				blueMartiniTestUtil.blueMartiniResultList());

		this.blueMartiniResponse = blueMartinihttpUtil.getHttpContent(
				this.requestor, blueMartiniTestUtil.requestParams(),
				this.correlationId);

		this.blueMartiniAdapter.blueMartiniResultsTOListofMap(
				this.blueMartiniResponse, this.correlationId);

		/*
		 * assertEquals(blueMartiniTestUtil.testBlueMartiniResultsTOListofMap()
		 * .get(0).get("P_product_code"), blueMartiniResultList.get(0)
		 * .get("P_product_code"));
		 */

	}

	
}
