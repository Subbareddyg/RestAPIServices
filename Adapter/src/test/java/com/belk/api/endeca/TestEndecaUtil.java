package com.belk.api.endeca;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.belk.api.exception.AdapterException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.belk.api.model.Dimension;
import com.belk.api.util.AdapterTestUtil;
import com.belk.api.util.EndecaFieldListLoader;
import com.belk.api.util.EndecaLoader;
import com.belk.api.util.EndecaUtil;
import com.belk.api.util.ErrorLoader;
import com.endeca.navigation.DimValIdList;
import com.endeca.navigation.DimensionSearchResult;
import com.endeca.navigation.DimensionSearchResultGroupList;
import com.endeca.navigation.ENEQuery;
import com.endeca.navigation.ENEQueryException;
import com.endeca.navigation.ENEQueryResults;
import com.endeca.navigation.UrlENEQueryParseException;

/**
 * Unit Testing related to Endeca Util class is performed. <br />
 * {@link TestEndecaUtil} class is written for testing methods in
 * {@link EndecaUtil} The unit test cases evaluates the way the methods behave
 * for the inputs given.
 * 
 * <br />
 * The test cases associated with EndecaAdapter is written to make the code
 * intactv by assuring the code test.
 * 
 * 
 * @author Mindtree
 * @date Oct 18 2013
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(EndecaUtil.class)
@PowerMockIgnore("org.apache.log4j.*")
public class TestEndecaUtil extends TestCase {

	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}
	private static final String PRODUCT_SEARCH_ALL = "productSearchAll";
	private static final String TSHIRT = "tshirt";
	private static final String METHOD_CREATESEARCHLIST = "createSearchList";
	private static final String METHOD_ENDECASORTKEY_MAPPER = "endecaSortKeyMapper";
	private static final String METHOD_CHECKSORTKEY = "checkSortKey";
	private static final String METHOD_GET_SORT_KEYS = "getSortKeys";
	private final String correlationId = "12345-67891234567-ASDFXEEETTT";
	private final EndecaUtil endecaUtil = new EndecaUtil();

	/**
	 * Test method to test the EndecaUtil buildRequest method.
	 * 
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 * @throws UrlENEQueryParseException
	 *             - UrlENEQueryParseException
	 */

	@Test
	public final void testBuildRequest() throws AdapterException,
			UrlENEQueryParseException {

		final Map<String, String> endecaRequestmap = AdapterTestUtil
				.createRequestMap();

		final ENEQuery excepetedquery = new ENEQuery();
		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		final EndecaLoader endecaLoader = PowerMockito.mock(EndecaLoader.class);
		final EndecaFieldListLoader endecaFieldListLoader = PowerMockito
				.mock(EndecaFieldListLoader.class);

		Map<String, String> endecaPropertiesMap = new HashMap<String, String>();
		endecaPropertiesMap.put("ENDECA_DEAFULT_ROOTDIM", "4294967294");
		endecaPropertiesMap.put("ENDECA_DEAFULT_LIMIT", "10");
		endecaPropertiesMap.put("ENDECA_DEAFULT_OFFSET", "0");
		endecaLoader.setEndecaPropertiesMap(endecaPropertiesMap);
		this.endecaUtil.setEndecaLoader(endecaLoader);
		this.endecaUtil.setErrorLoader(errorLoader);

		Map<String, String> endecaFieldListpropertiesMap = new HashMap<String, String>();
		endecaFieldListpropertiesMap.put("11421",
				"An invalid parameter name has been submitted");
		endecaFieldListpropertiesMap.put("11422",
				"An invalid parameter value has been submitted");
		endecaFieldListLoader
				.setEndecaFieldListpropertiesMap(endecaFieldListpropertiesMap);
		this.endecaUtil.setEndecaFieldListLoader(endecaFieldListLoader);

		excepetedquery.setNavRollupKey("ROLLUPKEY");

		PowerMockito.spy(new EndecaUtil());

		EndecaRequestContext context = new EndecaRequestContext();
		context.setEndecaRequestMap(endecaRequestmap);
		assertEquals(
				AdapterTestUtil.getEneQuery().getNavRollupKey(),
				this.endecaUtil.buildRequest(context,
						this.correlationId).getNavRollupKey());

	}

	/**
	 * Test method to test the EndecaUtil getSortKeys method
	 * 
	 * @throws Exception
	 *             private spy mock throws Exception if any.
	 */
	@Ignore
	public final void testGetSortKeys() throws Exception {

		final String expected = "P_product_name|0||P_inventory_level|0||P_saleprice|1|SORTKEY_PRD_RATING|0";
		final String inputParam = "name|inventory|-saleprice|toprating";
		final EndecaUtil endecaUtilMockSpy = PowerMockito.spy(new EndecaUtil());

		PowerMockito.doReturn(expected).when(endecaUtilMockSpy,
				METHOD_GET_SORT_KEYS, inputParam, this.correlationId);

	}

	/**
	 * Test method to test the EndecaUtil createSearchList method
	 * 
	 * @throws Exception
	 *             private spy mock throws Exception if any.
	 * 
	 */
	@Test
	public final void testCreateSearchList() throws Exception {

		final EndecaUtil endecaUtilMockSpy = PowerMockito.spy(new EndecaUtil());
		final Map<String, String> endecaRequestmap = AdapterTestUtil
				.createRequestMap();

		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		final EndecaLoader endecaLoader = PowerMockito.mock(EndecaLoader.class);
		final EndecaFieldListLoader endecaFieldListLoader = PowerMockito
				.mock(EndecaFieldListLoader.class);

		Map<String, String> endecaPropertiesMap = new HashMap<String, String>();
		endecaPropertiesMap.put("ENDECA_DEAFULT_ROOTDIM", "4294967294");
		endecaPropertiesMap.put("ENDECA_DEAFULT_LIMIT", "10");
		endecaPropertiesMap.put("ENDECA_DEAFULT_OFFSET", "0");
		endecaLoader.setEndecaPropertiesMap(endecaPropertiesMap);
		this.endecaUtil.setEndecaLoader(endecaLoader);
		this.endecaUtil.setErrorLoader(errorLoader);

		Map<String, String> endecaFieldListpropertiesMap = new HashMap<String, String>();
		endecaFieldListpropertiesMap.put("11421",
				"An invalid parameter name has been submitted");
		endecaFieldListpropertiesMap.put("11422",
				"An invalid parameter value has been submitted");
		endecaFieldListLoader
				.setEndecaFieldListpropertiesMap(endecaFieldListpropertiesMap);
		this.endecaUtil.setEndecaFieldListLoader(endecaFieldListLoader);

		PowerMockito.doNothing().when(endecaUtilMockSpy,
				PowerMockito.method(EndecaUtil.class, METHOD_CREATESEARCHLIST));

		EndecaRequestContext context = new EndecaRequestContext();
		context.setEndecaRequestMap(endecaRequestmap);
		assertNotNull(this.endecaUtil.buildRequest(context,
				this.correlationId));

	}

	/**
	 * Test Method to test LoadDimensionTree The @Ignore annotation has been
	 * added as this test method takes a long time to be executed on the basis
	 * of actual data fetched from Endeca. if required, it can be removed to
	 * consider this method for test as well.
	 * 
	 * @throws AdapterException
	 *             throws AdapterException
	 * @throws ENEQueryException
	 *             throws ENEQueryException
	 */
	@Ignore
	public final void testLoadDimensionTree() throws AdapterException,
			ENEQueryException {
		final AdapterTestUtil adapterTestUtil = new AdapterTestUtil();
		final Map<String, String> endecaRequestmap = AdapterTestUtil
				.createRequestDimensionMap();
		PowerMockito.mock(EndecaUtil.class);

		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		final EndecaLoader endecaLoader = PowerMockito.mock(EndecaLoader.class);

		Map<String, String> endecaPropertiesMap = new HashMap<String, String>();
		endecaPropertiesMap.put("HOST", "69.166.149.138");
		endecaPropertiesMap.put("PORT", "15000");
		endecaLoader.setEndecaPropertiesMap(endecaPropertiesMap);
		this.endecaUtil.setEndecaLoader(endecaLoader);
		this.endecaUtil.setErrorLoader(errorLoader);

		final DimensionTree dimensionTree = new DimensionTree();
		dimensionTree.setEndecaLoader(endecaLoader);
		dimensionTree.setErrorLoader(errorLoader);

		PowerMockito.mock(DimValIdList.class);

		PowerMockito.mock(DimensionSearchResult.class);

		PowerMockito.mock(DimensionSearchResultGroupList.class);

		this.endecaUtil.setDimensionTree(dimensionTree);

		PowerMockito.mock(ENEQueryResults.class);

		final Map<Long, Dimension> actual = this.endecaUtil.loadDimensionTree(
				endecaRequestmap, this.correlationId);

		final Map<Long, Dimension> expected = adapterTestUtil
				.getLongDimensionMap();
		assertNotNull(actual);
		assertEquals(expected, actual);
	}

}
