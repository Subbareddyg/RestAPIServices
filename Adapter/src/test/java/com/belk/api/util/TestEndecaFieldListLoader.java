package com.belk.api.util;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.belk.api.exception.BaseException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;

/**
 * Unit Testing related to EndecaFieldListLoader class is performed. <br />
 * {@link TestEndecaFieldListLoader} class is written for testing methods in
 * {@link EndecaFieldListLoader} The unit test cases evaluates the way the methods
 * behave for the inputs given.
 * 
 * <br />
 * The test cases associated with EndecaFieldListLoader is written to make the code
 * intactve by assuring the code test.
 * 
 * @author Mindtree
 * @date Dec 04, 2013
 * 
 * 
 */
/**
 * @author M1025020
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ EndecaFieldListLoader.class, PropertyLoader.class,
		RequestParser.class })
public class TestEndecaFieldListLoader {

	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}
	final EndecaFieldListLoader endecaFieldListLoader = new EndecaFieldListLoader();
	final String correlationId = "1233445";
	
	/**
	 * This method unit tests the UpdateEntirePropertyMap method of EndecaFieldListLoader.<br />
	 * the correctness of UpdateEntirePropertyMap is tested in this method.
	 * 
	 * @throws BaseException
	 *             if there are any exception occurred.
	 */
	@Test
	public final void testUpdateEntirePropertyMap() throws BaseException {
		final AdapterUtil adapterUtil = PowerMockito.mock(AdapterUtil.class);
		this.endecaFieldListLoader.setAdapterUtil(adapterUtil);
		this.endecaFieldListLoader.updateEntirePropertyMap(this.correlationId);
		assertNotNull(this.endecaFieldListLoader);

	}

	/**
	 * This method unit tests the UpdateMultiplePropertyInMap method of EndecaFieldListLoader.<br />
	 * the correctness of UpdateMultiplePropertyInMap is tested in this method.
	 * 
	 * @throws BaseException
	 *             if there are any exception occurred.
	 */
	@Test(expected = Exception.class)
	public final void testUpdateMultiplePropertyInMap() throws BaseException {
		final List<String> propertiesList = new ArrayList<String>();
		propertiesList.add("11421:hello|11422:hi");
		final AdapterUtil adapterUtil = PowerMockito.mock(AdapterUtil.class);
		this.endecaFieldListLoader.setAdapterUtil(adapterUtil);
		Map<String, String> endecaFieldListpropertiesMap = new HashMap<String, String>();
		endecaFieldListpropertiesMap.put("11421",
				"An invalid parameter name has been submitted");
		endecaFieldListpropertiesMap.put("11422",
				"An invalid parameter value has been submitted");
		RequestParser requestParser = PowerMockito.mock(RequestParser.class);
		PropertyLoader propertyLoader = PowerMockito.mock(PropertyLoader.class);
		propertyLoader.setRequestParser(requestParser);
		this.endecaFieldListLoader
				.setEndecaFieldListpropertiesMap(endecaFieldListpropertiesMap);
		this.endecaFieldListLoader.getEndecaFieldListpropertiesMap();
		this.endecaFieldListLoader.updateMultiplePropertyInMap(propertiesList,
				this.correlationId);
		assertNotNull(this.endecaFieldListLoader);
	}

}
