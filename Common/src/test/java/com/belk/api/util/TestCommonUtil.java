package com.belk.api.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;

import com.belk.api.constants.CommonConstants;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
/**
 * Unit Testing related to CommonUtil class is performed. <br />
 * {@link TestCommonUtil} class is written for testing methods in {@link CommonUtil}
 *  The unit test cases evaluates the way the methods behave for the inputs given.
 * @author Mindtree
 *
 */
public class TestCommonUtil {
	private static final GenericLogger LOGGER = LoggingHelper.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}
	CommonUtil commonUtil;
	String expectedResults;
	String actualResults;
	String correlationId = "1234567891234567";

	/**
	 * Initial set up ahead to start unit test case for class
	 */
	@Before
	public final void setUp() {
		commonUtil = new CommonUtil();
	}
	/**
	 * common constants are loaded from the resource bundle.
	 */
	private static final ResourceBundle BUNDLE = ResourceBundle
			.getBundle(CommonConstants.CONFIGURATIONS_RESOURCE_BUNDLE);

	/**
	 * Test method for {@link com.belk.api.util#Format(String)}.
	 *  Testing is done to check whether the required data is formatted.
	 * when format( String str) method is called .
	 */
	@Test
	public final void testFormat() {
		expectedResults = "5.12";
		actualResults = commonUtil.format("5.1234", correlationId);
		assertEquals(expectedResults, actualResults);
	}

	/**
	 * Test method for {@link com.belk.api.util#Format(String)}.
	 *  Testing is done to check whether the required data is obtained.
	 * when format( String str) method is called .
	 */
	@Test
	public final void testFormatEmpty() {
		expectedResults = "";
		actualResults = commonUtil.format("", correlationId);
		assertEquals(expectedResults, actualResults);
	}

	/**
	 * Test method for {@link com.belk.api.util#ConvertToFlag(String)}.
	 *  Testing is done when Flag is not equal to Yes or No.
	 * when ConvertToFlag(String str) method is called .
	 */
	@Test
	public final void testConvertToFlag() {
		expectedResults = "data";
		actualResults = commonUtil.convertToFlag("data", correlationId);
		assertEquals(expectedResults, actualResults);
	}

	/**
	 * Test method for {@link com.belk.api.util#ConvertToFlag(String)}.
	 *  Testing is done when Flag is  equal to Yes.
	 * when ConvertToFlag(String str) method is called .
	 */
	@Test
	public final void testConvertToFlagT() {
		expectedResults = "T";
		actualResults = commonUtil.convertToFlag("Yes", correlationId);
		assertEquals(expectedResults, actualResults);
	}

	/**
	 * Test method for {@link com.belk.api.util#ConvertToFlag(String)}.
	 *  Testing is done when Flag is  equal to No.
	 * when ConvertToFlag(String str) method is called .
	 */
	@Test
	public final void testConvertToFlagF() {
		expectedResults = "F";
		actualResults = commonUtil.convertToFlag("No", correlationId);
		assertEquals(expectedResults, actualResults);
	}
	public static Map<String, List<String>>  requestUriMap() {
		Map<String, List<String>>  updateRequestUriMap = new HashMap<String, List<String>>();
		List<String> propertyList = new ArrayList<String>();
		propertyList.add("info");
		updateRequestUriMap.put("loglevel",propertyList);

		return updateRequestUriMap;
	}

	@Test
	public final void testGetConfigurationFilePath(){
		String actual = commonUtil.getConfigurationFilePath("error.properties", "common", correlationId);
		String expected = BUNDLE.getString("configurations.files.path")+"/common/error.properties";
		assertEquals(actual,expected);
	}
	@Test
	public final void testIsNotEmpty(){
		assertEquals(false, commonUtil.isNotEmpty("", correlationId));
	}

	@Test
	public final void testIsNotEmptyForTrue(){
		assertEquals(true, commonUtil.isNotEmpty("test", correlationId));
	}

	public static  Map<String, String> getErrorPropertiesMap(){
		Map<String, String> errorPropertiesMap = new HashMap<String, String>();
		errorPropertiesMap.put("11421","An invalid parameter name has been submitted");
		errorPropertiesMap.put("11422","An invalid parameter value has been submitted");
		errorPropertiesMap.put("11423","The maximum number of values for this parameter has been exceeded");
		errorPropertiesMap.put("11424","An invalid combination of parameters has been supplied");
		errorPropertiesMap.put("11425","A required value was not supplied");
		errorPropertiesMap.put("11426","The requested method is not available");
		errorPropertiesMap.put("11427","The API you requested does not exist");
		errorPropertiesMap.put("11428","The supplied API key is invalid");
		errorPropertiesMap.put("11429","The requested method requires authentication");
		errorPropertiesMap.put("11430","The requested service has been removed; please upgrade to the current version");
		errorPropertiesMap.put("11431","An upgrade to the service terms is required");
		errorPropertiesMap.put("11432","The requested method is not allowed");
		errorPropertiesMap.put("11433","The supplied API key has expired");
		errorPropertiesMap.put("11434","The account associated with the supplied API key has been suspended");
		errorPropertiesMap.put("11435","The account associated with the supplied API key does not have access to this method");
		errorPropertiesMap.put("11521","The request to a required resource has timed-out");
		errorPropertiesMap.put("11522","An unknown error has occurred");
		errorPropertiesMap.put("11523","There has been an internal service error");
		errorPropertiesMap.put("11436","No record could be found based on the specified criteria");
		errorPropertiesMap.put("1235","11421");
		errorPropertiesMap.put("8","11422");
		return errorPropertiesMap;
	}

	public static Map<String, List<String>> getRequestUriMap() {
		Map<String, List<String>>  updateRequestUriMap = new HashMap<String, List<String>>();
		List<String> propertyList = new ArrayList<String>();
		propertyList.add("endeca.properties");
		updateRequestUriMap.put("filename",propertyList);
		List<String> propertyList1 = new ArrayList<String>();
		propertyList1.add("11421:hello|11422:hi");
		updateRequestUriMap.put("propertieslist",propertyList1);

		return updateRequestUriMap;
	}
	public static Map<String, List<String>> getRequestUriMapWithFileName() {
		Map<String, List<String>>  updateRequestUriMap = new HashMap<String, List<String>>();
		List<String> propertyList = new ArrayList<String>();
		propertyList.add("endeca.properties");
		List<String> reloadDefaultValues = new ArrayList<String>();
		propertyList.add("true");
		updateRequestUriMap.put("filename",propertyList);
		updateRequestUriMap.put("reloaddefaultvalues", reloadDefaultValues);
		return updateRequestUriMap;
	}
}
