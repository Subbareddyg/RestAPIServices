package com.belk.api.util;

import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;

/**
 * Unit Testing related to AdapterUtil class is performed. <br />
 * {@link TestAdapterUtil} class is written for testing methods in
 * {@link AdapterUtil} The unit test cases evaluates the way the methods behave
 * for the inputs given.
 * 
 * @author Mindtree
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ AdapterUtil.class })
public class TestAdapterUtil extends TestCase {

	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}
	AdapterUtil adapterUtil;
	String correlationId = "1234567891234567";

	/**
	 * Initial set up ahead to start unit test case for class
	 */
	@Before
	public final void setUp() {
		this.adapterUtil = new AdapterUtil();
	}

	/**
	 * Test method for
	 * {@link com.belk.api.util.AdapterUtil#populateResponseFields(java.util.Map, correlationId)}
	 * 
	 * @throws Exception
	 * .
	 */
	public final void testPopulateResponseFields() throws Exception {
		Map<String, String> endecaFieldListPropertiesMap = new LinkedHashMap<String, String>();
		endecaFieldListPropertiesMap.put("availableInStore",
				"P_available_in_Store");
		endecaFieldListPropertiesMap.put("availableOnline",
				"P_available_online");
		endecaFieldListPropertiesMap.put("brand", "P_brand");
		assertNotNull(this.adapterUtil.populateResponseFields(
				endecaFieldListPropertiesMap, this.correlationId));
	}

	/**
	 * Test method for
	 * {@link com.belk.api.util.AdapterUtil#mapResponseToGenericKeys(java.util.Map, java.util.Map, correlationId)}
	 * .
	 * @throws Exception 
	 */
	public final void testMapResponseToGenericKeys() throws Exception {
		Map<String, String> adapterResponseMap = new LinkedHashMap<String, String>();
		Map<String, String> responseFields = new LinkedHashMap<String, String>();
		adapterResponseMap.put("P_available_in_Store", "true");
		adapterResponseMap.put("P_available_online", "false");
		adapterResponseMap.put("P_bridal_eligible", "true");
		
		responseFields.put("P_available_in_Store", "availableInStore");
		responseFields.put("P_available_online", "availableOnline");
		responseFields.put("P_bridal_eligible", "bridalEligible");
		
		Map<String, String> expectedResultmap = new LinkedHashMap<String, String>();
		
		expectedResultmap.put("availableInStore", "true");
		expectedResultmap.put("availableOnline", "false");
		expectedResultmap.put("bridalEligible", "true");
		
		Map<String, String> actualResultmap = AdapterUtil.mapResponseToGenericKeys(adapterResponseMap, responseFields, this.correlationId);
		assertNotNull(actualResultmap);
		assertEquals(expectedResultmap, actualResultmap);
	}

}
