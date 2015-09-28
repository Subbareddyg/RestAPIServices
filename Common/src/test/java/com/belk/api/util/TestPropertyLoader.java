package com.belk.api.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.belk.api.constants.CommonConstants;
import com.belk.api.exception.BaseException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PropertyLoader.class,RequestParser.class})
public class TestPropertyLoader {

	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}
	String correlationId;
	final PropertyLoader propertyLoader = new PropertyLoader();
	/**
	 * common constants are loaded from the resource bundle.
	 */
	private static final ResourceBundle BUNDLE = ResourceBundle
			.getBundle(CommonConstants.CONFIGURATIONS_RESOURCE_BUNDLE);
	/**
	 * Precondition required for Junit test cases for ValidationConfigRuleConverter class.
	 */
	@Before
	public final void setUp() {
		correlationId = "1234567891234567";
	}

	@Test(expected=BaseException.class)
	public final void testLoadPropertyForBaseException() throws BaseException{
		Map<String, String> propertyMap = propertyLoader.loadProperty("", correlationId);

		assertEquals(propertyMap,TestCommonUtil.getErrorPropertiesMap());

	}
	@Test
	public final void testLoadProperty() throws BaseException{
		System.out.println(BUNDLE.getString("configurations.files.path"));
		String filePath = BUNDLE.getString("configurations.files.path")+"/common/error.properties";
		Map<String, String> propertyMap  =	propertyLoader.loadProperty(filePath, correlationId);
		Assert.assertTrue(propertyMap.size() > 0);
	}

	@Test
	public final void testUpdatePropertyMap() throws BaseException{
		TestCommonUtil.requestUriMap();
		Map<String,String> propertiesMap = TestCommonUtil.getErrorPropertiesMap();

		List<String> propertiesList = new ArrayList<String>();
		propertiesList.add("11523:junk|11435:xyz");
		//RequestParser requestParser = PowerMockito.mock(RequestParser.class);
		RequestParser requestParser = new RequestParser();
		propertyLoader.setRequestParser(requestParser);
		//PowerMockito.when(requestParser.processRequestAttribute(parseMap, correlationId)).thenReturn(propertiesMap);
		Map<String, String> actual = propertyLoader.updateProperty(propertiesMap, propertiesList, correlationId);
		assertNotNull(actual);
	}
	@Test(expected=BaseException.class)
	public final void testUpdatePropertyMapForBaseException() throws BaseException{
		Map<String,String> propertiesMap = TestCommonUtil.getErrorPropertiesMap();

		List<String> propertiesList = new ArrayList<String>();
		propertiesList.add("0000");
		//RequestParser requestParser = PowerMockito.mock(RequestParser.class);
		RequestParser requestParser = new RequestParser();
		propertyLoader.setRequestParser(requestParser);
		Map<String, String> actual = propertyLoader.updateProperty(propertiesMap, propertiesList, correlationId);
		assertEquals(actual,TestCommonUtil.getErrorPropertiesMap());
	}
}
