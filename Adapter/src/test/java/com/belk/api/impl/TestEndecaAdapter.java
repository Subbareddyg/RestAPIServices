package com.belk.api.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.belk.api.constants.CommonConstants;
import com.belk.api.constants.EndecaConstants;
import com.belk.api.constants.ErrorConstants;
import com.belk.api.endeca.EndecaRequestContext;
import com.belk.api.exception.AdapterException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.belk.api.model.Dimension;
import com.belk.api.util.AdapterTestUtil;
import com.belk.api.util.EndecaLoader;
import com.belk.api.util.EndecaResponseProcessor;
import com.belk.api.util.EndecaUtil;
import com.belk.api.util.ErrorLoader;
import com.belk.api.util.URIToEndecaTransformer;
import com.endeca.content.ene.ENEContentQuery;
import com.endeca.content.ene.ENEContentResults;
import com.endeca.navigation.DimValIdList;
import com.endeca.navigation.ENEQuery;
import com.endeca.navigation.ENEQueryException;
import com.endeca.navigation.ENEQueryResults;
import com.endeca.navigation.ERecSearch;
import com.endeca.navigation.ERecSearchList;
import com.endeca.navigation.HttpENEConnection;

/**
 * Unit Testing related to Endeca Adapter class is performed. <br />
 * {@link TestEndecaAdapter} class is written for testing methods in
 * {@link EndecaAdapter} The unit test cases evaluates the way the methods
 * behave for the inputs given.
 * 
 * <br />
 * The test cases associated with EndecaAdapter is written to make the code
 * intact by assuring the code test.
 * 
 * @author Mindtree
 * @date Oct 26 2013
 * 
 * 
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({ EndecaAdapter.class, URIToEndecaTransformer.class,
	ErrorLoader.class, EndecaUtil.class, EndecaLoader.class,
	EndecaRequestContext.class, ENEQuery.class,
	EndecaResponseProcessor.class, HttpENEConnection.class,
	ENEContentQuery.class, ENEContentResults.class, ERecSearchList.class,
	ERecSearch.class
	/*
	 * , , AggrERecList.class,
	 */
})
public class TestEndecaAdapter {

	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}

	private static final String POPULATE_PRODUCT_LIST = "populateProductsList";
	private static final String POPULATE_DIMENSION_LIST = "populateDimensionList";
	private static final String QUERY_ENDECA_METHOD = "queryEndeca";
	private static final String ERROR_DESCRIPTION_11523 = "There has been an internal service error";

	String correlationId = "1234567891234567";
	EndecaAdapter endecaAdapter = new EndecaAdapter();

	/**
	 * 
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 * @throws ENEQueryException
	 *             exception thrown from Endeca layer
	 */
	@Test
	public final void testMakeDimensionQuery() throws AdapterException,
	ENEQueryException {
		final Map<String, String> requestMap = AdapterTestUtil
				.getRequestDimensionMap();

		final EndecaUtil endecaUtil = PowerMockito.mock(EndecaUtil.class);
		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		this.endecaAdapter.setEndecaUtil(endecaUtil);
		this.endecaAdapter.setErrorLoader(errorLoader);
		final Map<Long, Dimension> endecaMap = AdapterTestUtil.getEndecaMap();
		PowerMockito.when(
				endecaUtil.loadDimensionTree(requestMap, this.correlationId))
				.thenReturn(endecaMap);
		final Map<Long, Dimension> actualMap = this.endecaAdapter
				.makeDimensionQuery(requestMap, this.correlationId);

		assertNotNull(this.endecaAdapter);
		assertEquals(actualMap, endecaMap);

	}

	/**
	 * This method unit tests the prepareEndecaQuery method of EndecaAdapter.<br />
	 * the correctness of preparingEndecaQuery is tested in this method.
	 * 
	 * @throws Exception
	 *             if there are any exception occurred.
	 */
	@Test
	public final void testPrepareEndecaQuery() throws Exception {
		// Mocking EndecaAdapter with spy technique as we are testing for a
		// private method --> endecaAdapterspy

		final AdapterTestUtil adapterTestUtil = new AdapterTestUtil();
		final Map<String, String> getMyMap = adapterTestUtil.getMyMap();

		final EndecaAdapter endecaAdapterspy = PowerMockito
				.spy(new EndecaAdapter());

		// Mocking EndecaUtil as the buildRequest is a final method -->
		// endecaUtil
		final EndecaUtil endecaUtil = PowerMockito.mock(EndecaUtil.class);

		EndecaRequestContext context = new EndecaRequestContext();
		context.setEndecaRequestMap(getMyMap);

		// Checking the dependent method of prepapreEndecaQuery for correctness.
		PowerMockito.when(endecaUtil.buildRequest(context, this.correlationId))
		.thenReturn(this.getEneQuery());

		// Checking the correctness of the actual private method.
		PowerMockito.doReturn(this.getEneQuery()).when(endecaAdapterspy,
				"prepareEndecaQuery", context, this.correlationId);

		assertNotNull(endecaUtil.buildRequest(context, this.correlationId));

	}

	
	/**
	 * This method unit tests the queryEndeca method of EndecaAdapter for Null
	 * scenario.<br />
	 * the correctness of queryEndeca for Null is tested in this method.
	 * 
	 * @throws Exception
	 *             if there are any exception occurred other than Adapter layer.
	 */
	@Test
	public final void testQueryEndecaForNull() throws Exception {
		// Mocking EndecaAdapter with spy technique as we are testing for a
		// private method --> endecaAdapterspy
		final EndecaAdapter endecaAdapterspy = PowerMockito
				.spy(new EndecaAdapter());
		final AdapterTestUtil adapterTestUtil = new AdapterTestUtil();
		EndecaRequestContext context = new EndecaRequestContext();
		Map<String, String> myRequestMap = adapterTestUtil.getRequestMap();

		context.setEndecaRequestMap(myRequestMap);
		context.setUriRequestMap(myRequestMap);
		final ENEQuery eneQuery = adapterTestUtil.getNullEneuery();
		// Checking the correctness of the actual private method.
		PowerMockito.doThrow(
				new AdapterException(String
						.valueOf(ErrorConstants.ERROR_CODE_11523),
						ERROR_DESCRIPTION_11523,
						ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
						this.correlationId)).when(endecaAdapterspy,
				QUERY_ENDECA_METHOD, context, eneQuery, this.correlationId);
	}

	/**
	 * This method unit tests the queryEndeca method of EndecaAdapter for Null
	 * scenario.<br />
	 * the correctness of queryEndeca for Null is tested in this method.
	 * 
	 * @throws Exception
	 *             if there are any exception occurred other than Adapter layer.
	 */
	@Test(expected = Exception.class)
	public final void testQueryEndecaForAdapterException() throws Exception {
		// Mocking EndecaAdapter with spy technique as we are testing for a
		// private method --> endecaAdapterspy
		final EndecaAdapter endecaAdapterspy = PowerMockito
				.spy(new EndecaAdapter());
		final AdapterTestUtil adapterTestUtil = new AdapterTestUtil();

		final ENEQuery eneQuery = adapterTestUtil.getNullEneuery();
		// Checking the correctness of the actual private method.
		PowerMockito.when(endecaAdapterspy, QUERY_ENDECA_METHOD, eneQuery,
				this.correlationId).thenThrow(
						new AdapterException(String
								.valueOf(ErrorConstants.ERROR_CODE_11523),
								ERROR_DESCRIPTION_11523,
								ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
								this.correlationId));
	}

	/**
	 * This method unit tests the service method of EndecaAdapter
	 * 
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 * @throws Exception
	 *             - exception
	 */

	@Test
	public final void testService() throws Exception
	// throws AdapterException, Exception
	{
		EndecaAdapter endecaAdapterspy = PowerMockito
				.spy(new EndecaAdapter());
		ErrorLoader errorLoaderSpy = PowerMockito.spy(new ErrorLoader());
		Map<String, String> errorPropertiesMap = new HashMap<String, String>();
		errorPropertiesMap.put("11523", ERROR_DESCRIPTION_11523);
		errorLoaderSpy.setErrorPropertiesMap(errorPropertiesMap);
		AdapterTestUtil adapterTestUtil = new AdapterTestUtil();
		EndecaRequestContext context = new EndecaRequestContext();
		Map<String, String> myRequestMap = adapterTestUtil.getRequestMap();

		context.setEndecaRequestMap(myRequestMap);
		context.setUriRequestMap(myRequestMap);
		URIToEndecaTransformer uriToEndecaTransformerSpy = PowerMockito
				.mock(URIToEndecaTransformer.class);
		PowerMockito.when(
				uriToEndecaTransformerSpy.queryToURIEndecaContext(
						myRequestMap, null,
						this.correlationId)).thenReturn(context);
		endecaAdapterspy.setUriToEndecaTransformer(uriToEndecaTransformerSpy);
		EndecaResponseProcessor endecaResponseProcessorMock = PowerMockito
				.mock(EndecaResponseProcessor.class);
		final EndecaLoader endecaLoaderSpy = PowerMockito.spy(new EndecaLoader());
		EndecaUtil endecaUtilMock = PowerMockito.mock(EndecaUtil.class);
		endecaAdapterspy.setEndecaUtil(endecaUtilMock);
		endecaAdapterspy.setErrorLoader(errorLoaderSpy);
		PowerMockito.doReturn(this.getEneQuery()).when(
				endecaUtilMock,
				"buildRequest", context, this.correlationId);


		PowerMockito.doReturn(this.getEneQuery()).when(endecaAdapterspy,
				"prepareEndecaQuery", context,
				this.correlationId);

		ENEQueryResults eneQueryResults = PowerMockito
				.mock(ENEQueryResults.class);
		ENEContentResults eneContentResults = PowerMockito
				.mock(ENEContentResults.class);

		final List<Map<String, String>> eneQueryResultsAsList = new ArrayList<Map<String, String>>();

		// Mocks the call to this.queryEndeca()


		endecaAdapterspy
		.setEndecaResponseProcessor(endecaResponseProcessorMock);
		Map<String, String> endecaPropertiesMap = new HashMap<String, String>();
		endecaPropertiesMap.put("HOST", "69.166.149.171");
		endecaPropertiesMap.put("PORT", "15000");
		endecaLoaderSpy.setEndecaPropertiesMap(endecaPropertiesMap);
		endecaAdapterspy.setEndecaLoader(endecaLoaderSpy);

		ENEQueryResults endecaResultsMock = PowerMockito
				.mock(ENEQueryResults.class);
		HttpENEConnection endecaConnMock = PowerMockito
				.mock(HttpENEConnection.class);
		ENEQuery eneQueryMock = PowerMockito.mock(ENEQuery.class);
		ENEContentQuery contentQueryMock = PowerMockito
				.mock(ENEContentQuery.class);
		ENEContentResults eneContentResultsMock = PowerMockito
				.mock(ENEContentResults.class);

		PowerMockito.doReturn(eneContentResultsMock).when(contentQueryMock,
				"execute");

		PowerMockito.doReturn(endecaResultsMock).when(endecaConnMock, "query",
				eneQueryMock);

		EndecaResponseProcessor endecaResponseProcessor = PowerMockito
				.mock(EndecaResponseProcessor.class);
		endecaAdapterspy.setEndecaResponseProcessor(endecaResponseProcessor);
		PowerMockito.doReturn(eneQueryResultsAsList).when(
				endecaResponseProcessor, "responseProcessor",
				adapterTestUtil.getRequestMap(), eneQueryResults,
				this.correlationId);
		PowerMockito.doReturn(eneQueryResultsAsList).when(
				endecaResponseProcessor, "contentResponseProcessor",
				adapterTestUtil.getRequestMap(), eneContentResults,
				this.correlationId);
		PowerMockito.doReturn(eneQueryResultsAsList).when(endecaAdapterspy,
				"queryEndeca", context, this.getEneQuery(), this.correlationId);



		assertNotNull(endecaAdapterspy.service(myRequestMap, null,
				this.correlationId));

		/*
		 * PowerMockito.verifyPrivate(endecaAdapterspy).invoke("prepareEndecaQuery"
		 * , context, this.correlationId);
		 * PowerMockito.verifyPrivate(endecaAdapterspy
		 * ).invoke(QUERY_ENDECA_METHOD, context, this.correlationId);
		 */


		//context.setEndecaRequestMap(myRequestMap);
		myRequestMap.put("stratify", "true");
		assertNotNull(endecaAdapterspy.service(myRequestMap, null,
				this.correlationId));

	}



	/**
	 * This method is for sample test data of EneQuery
	 * 
	 * @return {@link ENEQuery} object
	 */
	public ENEQuery getEneQuery() {
		ENEQuery eneQuery = new ENEQuery();

		final ERecSearchList searches = new ERecSearchList();
		ERecSearch eRecSearch;
		String searchKey = null;
		String searchTerms = null;
		final String MATCH_MODE = "mode matchany";
		final String DEFAULT_OFFSET = "0";
		final AdapterTestUtil adapterTestUtil = new AdapterTestUtil();
		final Map<String, String> endecaRequest = adapterTestUtil
				.getServiceRequestMap();
		final Iterator<Map.Entry<String, String>> endecaIteatorMap = endecaRequest
				.entrySet().iterator();
		while (endecaIteatorMap.hasNext()) {
			final Entry<String, String> entry = endecaIteatorMap.next();
			searchKey = entry.getKey();
			searchTerms = entry.getValue();
			if (searchTerms.contains(CommonConstants.COMMA_SEPERATOR)) {
				searchTerms = searchTerms.replace(
						CommonConstants.COMMA_SEPERATOR, " ");
			}
			if (EndecaConstants.DIM.equalsIgnoreCase(searchKey)) {
				if (CommonConstants.TRUE_VALUE.equalsIgnoreCase(searchTerms)) {
					eneQuery.setNavAllRefinements(true);
				}
			} else if (!EndecaConstants.CATEGORY_ID.equalsIgnoreCase(searchKey)
					&& !EndecaConstants.OFFSET.equalsIgnoreCase(searchKey)
					&& !EndecaConstants.LIMIT.equalsIgnoreCase(searchKey)) {
				eRecSearch = new ERecSearch(searchKey, searchTerms, MATCH_MODE);
				searches.add(eRecSearch);
			}
		}
		eneQuery.setNavERecSearches(searches);
		final DimValIdList dimValIdList = new DimValIdList(DEFAULT_OFFSET);
		eneQuery.setNavDescriptors(dimValIdList);
		eneQuery.setNavRollupKey(EndecaConstants.ROLL_UP_KEY);
		eneQuery.setNavERecSearches(searches);

		return eneQuery;
	}

}
