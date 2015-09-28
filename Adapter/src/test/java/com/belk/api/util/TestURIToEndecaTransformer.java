package com.belk.api.util;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import com.belk.api.constants.EndecaConstants;
import com.belk.api.constants.RequestAttributeConstant;
import com.belk.api.exception.AdapterException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;

/**
 * Unit Testing related to URIToEndecaTransformer class is performed. <br />
 * {@link TestURIToEndecaTransformer} class is written for testing methods in {@link URIToEndecaTransformer}
 *  The unit test cases evaluates the way the methods behave for the inputs given.
 *  
 *  <br />The test cases associated with EndecaAdapter is written to make the code intactv
 *  by assuring the code test.
 * @author Mindtree
 * @date Oct 18 2013
 */
public class TestURIToEndecaTransformer {
	public static final String SEARCH_KEY_URI = "q";	
	public static final String SEARCH_TERM_URI = "tshirt";
	public static final String SEARCH_KEY_ENDECA = "productSearchAll";	
	public static final String SEARCH_TERM_ENDECA = "tshirt";
	private static final GenericLogger LOGGER = LoggingHelper.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}
	String correlationId = "1234567891234567";
	/**
	 * Test method to test whether the URI is correctly transforming as expected to Endeca keys
	 * @throws AdapterException exception thrown from Adapter layer
	 */
	@Test
	public final void testQueryToURIEndecaMapper() throws AdapterException {		
		 
		final Map<String, String> uriMap = new HashMap<String, String>();
		final EndecaFieldListLoader endecaFieldListLoader = PowerMockito.mock(EndecaFieldListLoader.class);
		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		final EndecaLoader endecaLoaderMock = PowerMockito.mock(EndecaLoader.class);
		final Map<String, String> endecaFieldListpropertiesMap = new HashMap<String, String>();
		endecaFieldListpropertiesMap.put(SEARCH_KEY_URI, SEARCH_KEY_ENDECA);
		endecaFieldListLoader.setEndecaFieldListpropertiesMap(endecaFieldListpropertiesMap);
		final URIToEndecaTransformer uriToEndecaTransformer = new URIToEndecaTransformer();
		uriToEndecaTransformer.setEndecaFieldListLoader(endecaFieldListLoader);
		uriToEndecaTransformer.setEndecaLoader(endecaLoaderMock);
		uriToEndecaTransformer.setErrorLoader(errorLoader);
		uriMap.put(SEARCH_KEY_URI, SEARCH_TERM_URI);
		
		final Map<String, String> actualMap = new HashMap<String, String>();
		actualMap.put(SEARCH_KEY_ENDECA, SEARCH_TERM_ENDECA);
		
		assertEquals(uriToEndecaTransformer.queryToURIEndecaContext(uriMap, null, this.correlationId)
				.getEndecaRequestMap(), actualMap);
		
		final Map<String, String> endecaPropertiesMap = new HashMap<String, String>();
		endecaPropertiesMap.put(EndecaConstants.CATALOG_ROOT_DIMENSION_VALUE, "4294889360");
		PowerMockito.doReturn(endecaPropertiesMap).when(endecaLoaderMock.getEndecaPropertiesMap());
		
		actualMap.put(RequestAttributeConstant.CATEGORY_ID, "some wrong value");
		assertEquals(uriToEndecaTransformer.queryToURIEndecaContext(uriMap, null, this.correlationId)
				.getEndecaRequestMap(), actualMap);
		
		actualMap.put(RequestAttributeConstant.CATEGORY_ID, "root");
		assertEquals(uriToEndecaTransformer.queryToURIEndecaContext(uriMap, null, this.correlationId)
				.getEndecaRequestMap(), actualMap);
		
		actualMap.put("some wrong request param", "some wrong value");
		assertEquals(uriToEndecaTransformer.queryToURIEndecaContext(uriMap, null, this.correlationId)
				.getEndecaRequestMap(), actualMap);
	}

}
 