package com.belk.api.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.belk.api.constants.CommonConstants;
import com.belk.api.constants.EndecaConstants;
import com.belk.api.exception.AdapterException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.endeca.content.ContentItem;
import com.endeca.content.Property;
import com.endeca.content.ene.ENEContentResults;
import com.endeca.content.ene.NavigationRecords;
import com.endeca.navigation.AggrERecList;
import com.endeca.navigation.DimVal;
import com.endeca.navigation.DimValList;
import com.endeca.navigation.Dimension;
import com.endeca.navigation.DimensionList;
import com.endeca.navigation.ENEQueryResults;
import com.endeca.navigation.Navigation;
import com.endeca.navigation.PropertyMap;

/**
 * Unit Testing related to EndecaResponseProcessor class is performed. <br />
 * {@link TestURIToEndecaTransformer} class is written for testing methods in
 * {@link EndecaResponseProcessor} The unit test cases evaluates the way the
 * methods behave for the inputs given.
 * 
 * <br />
 * The test cases associated with EndecaAdapter is written to make the code
 * intactv by assuring the code test.
 * 
 * @author Mindtree
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ EndecaResponseProcessor.class, AggrERecList.class,
	ENEQueryResults.class, Navigation.class, DimValList.class,
	EndecaFieldListLoader.class, AggrERecList.class, AdapterUtil.class,
	ContentItem.class, NavigationRecords.class, Property.class,
	DimensionList.class, Dimension.class, DimValList.class, DimVal.class,
	PropertyMap.class })
public class TestEndecaResponseProcessor extends TestCase {
	public static final String POPULATE_PRODUCT_LIST = "populateProductsList";
	public static final String PREPARE_ENDECA_QUERY = "prepareEndecaQuery";
	public static final String QUERY_ENDECA = "queryEndeca";
	public static final String POPULATE_DIMENSION_LIST = "populateDimensionListWithRefinements";
	public static final String POPULATE_DIMENSION_LIST_WITHOUT_REFINEMENTS = "populateDimensionListWithoutRefinements";
	private static final String ERROR_DESCRIPTION_11523 = "There has been an internal service error";

	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}
	String correlationId = "1234567891234567";
	/*@Mock
	Navigation navigation;*/

	EndecaResponseProcessor responseProcessor = new EndecaResponseProcessor();

	/**This test is ignored as it is already covered by the testResponseProcessor() test.
	 * Test method for
	 * {@link com.belk.api.util.EndecaResponseProcessor#processResponse (java.util.Map, com.endeca.navigation.ENEQueryResults)}
	 * .
	 * 
	 * @throws Exception
	 *             - exception
	 */
	@Ignore
	@Test
	public final void testProcessResponse() throws Exception {
		EndecaResponseProcessor endecaResponseProcessorSpy = PowerMockito
				.spy(new EndecaResponseProcessor());
		EndecaFieldListLoader endecaFieldListLoaderMock = PowerMockito
				.mock(EndecaFieldListLoader.class);
		endecaResponseProcessorSpy
		.setEndecaFieldListLoader(endecaFieldListLoaderMock);
		Navigation nav = PowerMockito.mock(Navigation.class);
		final AdapterTestUtil adapterTestUtil = new AdapterTestUtil();
		Map<String, String> requestMap = adapterTestUtil.getEndecaInputMap();

		final Map<String, String> endecaMap = new LinkedHashMap<String, String>();
		ENEQueryResults eneQueryResults = PowerMockito
				.mock(ENEQueryResults.class);
		PowerMockito.mock(AggrERecList.class);

		PowerMockito.when(eneQueryResults.getNavigation()).thenReturn(nav);
		PowerMockito.when(nav.getAggrERecs()).thenReturn(
				adapterTestUtil.getEneQueryResults().getNavigation()
				.getAggrERecs());
		final List<Map<String, String>> endecaList = adapterTestUtil
				.getResponseList();
		endecaMap.put(CommonConstants.TOTAL_PRODUCTS,
				String.valueOf(nav.getTotalNumAggrERecs()));
		endecaMap.put(CommonConstants.TOTAL_SKUS,
				String.valueOf(nav.getTotalNumERecs()));
		endecaList.add(endecaMap);
		AggrERecList aggrList = nav.getAggrERecs();
		PowerMockito.doReturn(adapterTestUtil.getResponseList()).when(
				endecaResponseProcessorSpy,
				POPULATE_PRODUCT_LIST, endecaList,
				aggrList, this.correlationId);
		assertNotNull(endecaResponseProcessorSpy.responseProcessor(requestMap,
				eneQueryResults, this.correlationId));
	}

	/**
	 * Test method for
	 * {@link com.belk.api.util.EndecaResponseProcessor#processResponse (java.util.Map, com.endeca.navigation.ENEQueryResults)}
	 * .
	 * 
	 * @throws Exception
	 *             exception
	 */
	@Test
	public final void testResponseProcessor() throws Exception {
		EndecaResponseProcessor endecaResponseProcessorSpy = PowerMockito
				.spy(new EndecaResponseProcessor());
		ErrorLoader errorLoaderSpy = PowerMockito.spy(new ErrorLoader());
		Map<String, String> errorPropertiesMap = new HashMap<String, String>();
		errorPropertiesMap.put("11523", ERROR_DESCRIPTION_11523);
		errorLoaderSpy.setErrorPropertiesMap(errorPropertiesMap);
		endecaResponseProcessorSpy.setErrorLoader(errorLoaderSpy);
		// Navigation navMock = PowerMockito.mock(Navigation.class);
		AdapterTestUtil adapterTestUtil = new AdapterTestUtil();
		Navigation navigation = adapterTestUtil.getEneQueryResults()
				.getNavigation();
		final Map<String, String> endecaMap = new LinkedHashMap<String, String>();
		ENEQueryResults eneQueryResultsMock = PowerMockito
				.mock(ENEQueryResults.class);
		EndecaFieldListLoader endecaFieldListLoaderMock = PowerMockito
				.mock(EndecaFieldListLoader.class);
		endecaResponseProcessorSpy
		.setEndecaFieldListLoader(endecaFieldListLoaderMock);
		Map<String, String> endecaGenericFieldListPropertiesMap = adapterTestUtil
				.getEndecaGenericFieldListPropertiesMap();
		PowerMockito.doReturn(endecaGenericFieldListPropertiesMap).when(
				endecaFieldListLoaderMock,
				"getEndecaGenericFieldListPropertiesMap");

		/*AggrERecList aggrERecListMock = PowerMockito.mock(AggrERecList.class);*/
		PowerMockito.when(eneQueryResultsMock.getNavigation()).thenReturn(
				navigation);
		/*PowerMockito.when(navigation.getAggrERecs()).thenReturn(
				navigation.getAggrERecs());*/
		/*PowerMockito.when(navMock.getAggrERecs()).thenReturn(aggrERecListMock);*/
		final List<Map<String, String>> endecaList = adapterTestUtil
				.getResponseList();
		endecaMap.put(CommonConstants.TOTAL_PRODUCTS,
				String.valueOf(navigation.getTotalNumAggrERecs()));
		endecaMap.put(CommonConstants.TOTAL_SKUS,
				String.valueOf(navigation.getTotalNumERecs()));
		endecaList.add(endecaMap);
		AggrERecList aggrList = navigation.getAggrERecs();
		/*PowerMockito.doReturn(adapterTestUtil.getResponseList()).when(
				endecaResponseProcessorSpy,
				"populateProductsList", endecaList,
				aggrERecListMock, this.correlationId);
		PowerMockito.doReturn(adapterTestUtil.getResponseList()).when(
				endecaResponseProcessorSpy, "populateCompleteDimensionList",
				endecaList, navigation, this.correlationId);
		PowerMockito.doReturn(adapterTestUtil.getResponseList()).when(
				endecaResponseProcessorSpy,
				"populateDescriptorDimensionList",
				AdapterTestUtil.getEndecaDimCheckMap(), endecaList, navigation,
				this.correlationId);*/
		Map<String, String> requestMap = AdapterTestUtil.getEndecaDimCheckMap();
		assertNotNull(endecaResponseProcessorSpy.responseProcessor(
				requestMap, eneQueryResultsMock,
				this.correlationId));

		requestMap.put("dim", "false");
		assertNotNull(endecaResponseProcessorSpy.responseProcessor(requestMap,
				eneQueryResultsMock, this.correlationId));

		requestMap.remove("dim");
		assertNotNull(endecaResponseProcessorSpy.responseProcessor(requestMap,
				eneQueryResultsMock, this.correlationId));
	}

	/**
	 * Test method for
	 * {@link com.belk.api.util.EndecaResponseProcessor#processResponse (java.util.Map, com.endeca.navigation.ENEQueryResults)}
	 * .
	 * 
	 * @throws Exception
	 *             exception
	 */
	@Test(expected = AdapterException.class)
	public final void testContentResponseProcessor() throws Exception {
		EndecaResponseProcessor endecaResponseProcessorSpy = PowerMockito
				.spy(new EndecaResponseProcessor());
		ErrorLoader errorLoaderSpy = PowerMockito.spy(new ErrorLoader());
		Map<String, String> errorPropertiesMap = new HashMap<String, String>();
		errorPropertiesMap.put("11523", ERROR_DESCRIPTION_11523);
		errorLoaderSpy.setErrorPropertiesMap(errorPropertiesMap);
		endecaResponseProcessorSpy.setErrorLoader(errorLoaderSpy);
		// Navigation navMock = PowerMockito.mock(Navigation.class);
		AdapterTestUtil adapterTestUtil = new AdapterTestUtil();
		Navigation navigation = adapterTestUtil.getEneQueryResults()
				.getNavigation();
		final Map<String, String> endecaMap = new LinkedHashMap<String, String>();
		ENEQueryResults eneQueryResults = PowerMockito
				.mock(ENEQueryResults.class);
		ENEContentResults eneContentResultsMock = PowerMockito
				.mock(ENEContentResults.class);
		EndecaFieldListLoader endecaFieldListLoaderMock = PowerMockito
				.mock(EndecaFieldListLoader.class);
		endecaResponseProcessorSpy
		.setEndecaFieldListLoader(endecaFieldListLoaderMock);
		Map<String, String> endecaGenericFieldListPropertiesMap = adapterTestUtil
				.getEndecaGenericFieldListPropertiesMap();
		PowerMockito.doReturn(endecaGenericFieldListPropertiesMap).when(
				endecaFieldListLoaderMock,
				"getEndecaGenericFieldListPropertiesMap");

		/*AggrERecList aggrERecListMock = PowerMockito.mock(AggrERecList.class);*/
		PowerMockito.when(eneContentResultsMock.getENEQueryResults())
		.thenReturn(eneQueryResults);
		PowerMockito.when(eneQueryResults.containsNavigation())
		.thenReturn(true);
		PowerMockito.when(eneQueryResults.getNavigation()).thenReturn(
				navigation);
		ContentItem contentItemMock = PowerMockito.mock(ContentItem.class);
		Property centerColumnPropertyMock = PowerMockito.mock(Property.class);
		PowerMockito.when(
				contentItemMock.getProperty(EndecaConstants.CENTER_COLUMN))
				.thenReturn(centerColumnPropertyMock);

		ContentItem recordsCartridgeMock = PowerMockito.mock(ContentItem.class);
		List<ContentItem> centerColumnCartridgeList = new ArrayList<ContentItem>();
		centerColumnCartridgeList.add(recordsCartridgeMock);
		PowerMockito.when(
				contentItemMock.getProperty(EndecaConstants.CENTER_COLUMN)
				.getValue()).thenReturn(centerColumnCartridgeList);
		PowerMockito.when(recordsCartridgeMock.getTemplateId()).thenReturn(
				"RecordsBNB");
		NavigationRecords navigationRecordsMock = PowerMockito
				.mock(NavigationRecords.class);
		Property navRecordPropertyMock = PowerMockito.mock(Property.class);
		PowerMockito.when(
				recordsCartridgeMock
				.getProperty(EndecaConstants.NAVIGATION_RECORDS))
				.thenReturn(navRecordPropertyMock);
		PowerMockito.when(
				recordsCartridgeMock.getProperty(
						EndecaConstants.NAVIGATION_RECORDS).getValue())
						.thenReturn(navigationRecordsMock);
		PowerMockito.when(eneContentResultsMock.getContent()).thenReturn(
				contentItemMock);

		/*PowerMockito.doReturn(1).when(
				navigationRecordsMock.getAggrERecs().size());*/
		AggrERecList aggrList = navigation.getAggrERecs();
		PowerMockito.when(navigationRecordsMock.getAggrERecs()).thenReturn(
				aggrList);
		/*PowerMockito.when(navigation.getAggrERecs()).thenReturn(
				navigation.getAggrERecs());*/
		/*PowerMockito.when(navMock.getAggrERecs()).thenReturn(aggrERecListMock);*/
		final List<Map<String, String>> endecaList = adapterTestUtil
				.getResponseList();
		endecaMap.put(CommonConstants.TOTAL_PRODUCTS,
				String.valueOf(navigation.getTotalNumAggrERecs()));
		endecaMap.put(CommonConstants.TOTAL_SKUS,
				String.valueOf(navigation.getTotalNumERecs()));
		endecaList.add(endecaMap);
		/*PowerMockito.doReturn(adapterTestUtil.getResponseList()).when(
				endecaResponseProcessorSpy,
				"populateProductsList", endecaList,
				aggrERecListMock, this.correlationId);
		PowerMockito.doReturn(adapterTestUtil.getResponseList()).when(
				endecaResponseProcessorSpy, "populateCompleteDimensionList",
				endecaList, navigation, this.correlationId);
		PowerMockito.doReturn(adapterTestUtil.getResponseList()).when(
				endecaResponseProcessorSpy,
				"populateDescriptorDimensionList",
				AdapterTestUtil.getEndecaDimCheckMap(), endecaList, navigation,
				this.correlationId);*/


		Map<String, String> requestMap = AdapterTestUtil.getEndecaDimCheckMap();
		assertNotNull(endecaResponseProcessorSpy.contentResponseProcessor(
				requestMap, eneContentResultsMock, this.correlationId));

		// Second set of test cases for the left nav cartridge
		Property leftColumnPropertyMock = PowerMockito.mock(Property.class);
		PowerMockito.when(
				contentItemMock.getProperty(EndecaConstants.LEFT_COLUMN))
				.thenReturn(leftColumnPropertyMock);
		// ContentItem contItemMock = PowerMockito.mock(ContentItem.class);
		List<ContentItem> leftColumnCartridgeList = new ArrayList<ContentItem>();
		leftColumnCartridgeList.add(contentItemMock);
		PowerMockito.when(
				contentItemMock.getProperty(EndecaConstants.LEFT_COLUMN)
				.getValue()).thenReturn(leftColumnCartridgeList);
		Property refinementPropertyMock = PowerMockito.mock(Property.class);
		PowerMockito.when(
				contentItemMock.getProperty(EndecaConstants.REFINEMENTS))
				.thenReturn(refinementPropertyMock);
		DimensionList refinementDimensionsMock = PowerMockito
				.mock(DimensionList.class);
		Dimension currentDimMock = PowerMockito.mock(Dimension.class);

		// refinementDimensions.add(currentDimMock);
		PowerMockito.when(refinementDimensionsMock.size()).thenReturn(1);
		PowerMockito.when(refinementDimensionsMock.get(0)).thenReturn(
				currentDimMock);

		Property facetDisplayLabelMock = PowerMockito.mock(Property.class);
		PowerMockito
		.when(contentItemMock
				.getProperty(EndecaConstants.FACETDISPLAY_LABEL))
				.thenReturn(facetDisplayLabelMock);
		PowerMockito.when(
				contentItemMock.getProperty(EndecaConstants.FACETDISPLAY_LABEL)
				.getValue()).thenReturn("category");
		PowerMockito.when(currentDimMock.getName()).thenReturn("category");
		PowerMockito.when(currentDimMock.getId()).thenReturn(
				Long.parseLong("4294889360"));
		DimValList dimensionValListMock = PowerMockito.mock(DimValList.class);
		DimVal refinementMock = PowerMockito.mock(DimVal.class);
		PowerMockito.when(refinementMock.getId()).thenReturn(
				Long.parseLong("4294852688"));
		PowerMockito
		.when(refinementMock.getName())
		.thenReturn(
				"/Assortments/Boutiques/Boutiques_Primary/wk24_711_BB_July_Home_Book_Offers/BedinaBag");
		PropertyMap propertyMapMock = PowerMockito.mock(PropertyMap.class);
		PowerMockito.when(refinementMock.getProperties()).thenReturn(
				propertyMapMock);
		PowerMockito.when(
				refinementMock.getProperties().get(
						EndecaConstants.DIM_REFINMENT_COUNT)).thenReturn("18");
		List<DimVal> mockList = new ArrayList<DimVal>();
		mockList.add(refinementMock);

		PowerMockito.when(dimensionValListMock.iterator()).thenReturn(
				mockList.iterator());
		DimVal refinementParentMock = PowerMockito.mock(DimVal.class);
		PowerMockito.when(currentDimMock.getRefinementParent()).thenReturn(
				refinementParentMock);
		PowerMockito.when(
				currentDimMock.getRefinementParent().isMultiSelectAnd())
				.thenReturn(true);
		PowerMockito.when(
				currentDimMock.getRefinementParent().isMultiSelectOr())
				.thenReturn(false);
		dimensionValListMock.add(refinementMock);
		PowerMockito.when(currentDimMock.getRefinements()).thenReturn(
				dimensionValListMock);
		PowerMockito.when(
				contentItemMock.getProperty(EndecaConstants.REFINEMENTS)
				.getValue()).thenReturn(refinementDimensionsMock);

		assertNotNull(endecaResponseProcessorSpy.contentResponseProcessor(
				requestMap, eneContentResultsMock, this.correlationId));
		// Third set of test cases for the negative conditionals.
		PowerMockito.when(eneContentResultsMock.getContent()).thenReturn(null);

		PowerMockito.doReturn(null).when(endecaResponseProcessorSpy,
				"getNavigationRecords", contentItemMock);
		assertNotNull(endecaResponseProcessorSpy.contentResponseProcessor(
				requestMap, eneContentResultsMock, this.correlationId));

		PowerMockito.when(eneQueryResults.containsNavigation()).thenReturn(
				false);
		// TODO: This would throw error till the time the else block for
		// endecaResults.containsNavigation() in contentResponseProcessor() is
		// written to handle the null state of the navigation object.
		assertNotNull(endecaResponseProcessorSpy.contentResponseProcessor(
				requestMap, eneContentResultsMock, this.correlationId));



	}


	/**
	 * Test method for
	 * {@link com.belk.api.util.EndecaResponseProcessor#processResponse (java.util.Map, com.endeca.navigation.ENEQueryResults)}
	 * .
	 * 
	 * @throws Exception
	 *             - exception
	 */

	@Test(expected = Exception.class)
	public final void testPopulateCompleteDimensionList() throws Exception {
		PowerMockito.spy(new EndecaResponseProcessor());
		final Navigation nav = PowerMockito.mock(Navigation.class);
		final DimensionList dimensions = PowerMockito.mock(DimensionList.class);
		final AdapterTestUtil adapterTestUtil = new AdapterTestUtil();
		final Map<String, String> dimensionMap = adapterTestUtil
				.getDimensionListMap();
		final Dimension dimension = PowerMockito.mock(Dimension.class);
		final ENEQueryResults eneQueryResults = PowerMockito
				.mock(ENEQueryResults.class);
		final DimValList refinementList = PowerMockito.mock(DimValList.class);

		PowerMockito.when(eneQueryResults.getNavigation()).thenReturn(nav);

		PowerMockito.when(nav.getRefinementDimensions()).thenReturn(dimensions);
		final List<Map<String, String>> dimensionList = adapterTestUtil
				.getResponseList();

		PowerMockito.when(dimension.getRefinements())
		.thenReturn(refinementList);

		dimensionList.add(dimensionMap);

		final EndecaResponseProcessor endecaResponseProcessor1 = PowerMockito
				.mock(EndecaResponseProcessor.class);

		PowerMockito.when(
				endecaResponseProcessor1.populateCompleteDimensionList(
						dimensionList, nav, this.correlationId)).thenReturn(
								dimensionList);
		assertNotNull(this.responseProcessor.populateCompleteDimensionList(
				adapterTestUtil.getResponseList(), nav, this.correlationId));
	}

	/**
	 * Test method for
	 * {@link com.belk.api.util.EndecaResponseProcessor#populateDescriptorDimensionList(java.util.Map,ENEQueryResults)}
	 * .
	 * 
	 * @throws Exception
	 *             - exception
	 */

	@Test(expected = Exception.class)
	public final void testPopulateDescriptorDimensionList() throws Exception {
		PowerMockito.spy(new EndecaResponseProcessor());
		final Navigation nav = PowerMockito.mock(Navigation.class);
		final DimensionList dimensions = PowerMockito.mock(DimensionList.class);
		final Dimension dimension = PowerMockito.mock(Dimension.class);
		final AdapterTestUtil adapterTestUtil = new AdapterTestUtil();
		final Map<String, String> dimensionMap = adapterTestUtil
				.getDimensionListMap();
		final ENEQueryResults eneQueryResults = PowerMockito
				.mock(ENEQueryResults.class);
		PowerMockito.mock(AggrERecList.class);

		final Map<String, String> request = null;

		PowerMockito.when(nav.getDescriptorDimensions()).thenReturn(dimensions);
		PowerMockito.when(eneQueryResults.getNavigation()).thenReturn(nav);

		PowerMockito.when(nav.getRefinementDimensions()).thenReturn(dimensions);

		final DimValList refineList = PowerMockito.mock(DimValList.class);
		PowerMockito.when(dimension.getRefinements()).thenReturn(refineList);

		final List<Map<String, String>> dimensionList = adapterTestUtil
				.getResponseList();

		final DimValList refinementList = PowerMockito.mock(DimValList.class);
		PowerMockito.when(dimension.getRefinements())
		.thenReturn(refinementList);

		dimensionList.add(dimensionMap);

		PowerMockito.when(
				this.responseProcessor.populateDescriptorDimensionList(request,
						dimensionList, nav, this.correlationId)).thenReturn(
								dimensionList);
		assertNotNull(this.responseProcessor.populateDescriptorDimensionList(
				request, adapterTestUtil.getResponseList(), nav,
				this.correlationId));
	}

	/**
	 * Test method for
	 * {@link com.belk.api.util.EndecaResponseProcessor#populateMultiSelectForDimension (java.util.Map,String,Dimension)}
	 * .
	 * 
	 * @throws Exception
	 *             - exception
	 */
	/*
	 * @Test public final void testPopulateMultiSelectForDimension() throws
	 * Exception {
	 * 
	 * final Map<String, String> endecaMap = new LinkedHashMap<String,
	 * String>(); final Dimension dimension =
	 * PowerMockito.mock(Dimension.class); final DimVal dimVal =
	 * PowerMockito.mock(DimVal.class);
	 * PowerMockito.when(dimension.getRefinementParent()).thenReturn(dimVal);
	 * PowerMockito.spy(new EndecaResponseProcessor());
	 * Whitebox.invokeMethod(this.responseProcessor,
	 * "populateMultiSelectForDimension", endecaMap,
	 * dimension,this.correlationId);
	 * 
	 * }
	 *//**
	 * Test method for
	 * {@link com.belk.api.util.EndecaResponseProcessor#populateRefinementList (Dimension,java.util.Map,String,correlationId)}
	 * .
	 * 
	 * @throws Exception
	 *             - exception
	 */
	/*
	 * @Test public final void testPopulateRefinementList() throws Exception {
	 * final Map<String, String> endecaMap = new LinkedHashMap<String,
	 * String>(); final Dimension dimension =
	 * PowerMockito.mock(Dimension.class);
	 * 
	 * final DimValList dimValList = PowerMockito.mock(DimValList.class);
	 * 
	 * PowerMockito.when(dimension.getRefinements()).thenReturn(dimValList);
	 * PowerMockito.spy(new EndecaResponseProcessor());
	 * Whitebox.invokeMethod(this.responseProcessor, "populateRefinementList",
	 * dimension, endecaMap, this.correlationId);
	 * 
	 * }
	 *//**
	 * Test method for
	 * {@link com.belk.api.util.EndecaResponseProcessor#getMultipleEndecaValues (PropertyMap,java.util.Map,correlationId)}
	 * .
	 * 
	 * @throws Exception
	 *             - exception
	 */
	/*
	 * @Test public final void testGetMultipleEndecaValues() throws Exception{
	 * final Map<String, String> endecaMap = new LinkedHashMap<String,
	 * String>(); final PropertyMap propertiesMapERec =
	 * PowerMockito.mock(PropertyMap.class); PowerMockito.spy(new
	 * EndecaResponseProcessor()); Whitebox.invokeMethod(this.responseProcessor,
	 * "getMultipleEndecaValues", propertiesMapERec, endecaMap,
	 * this.correlationId); }
	 */
	// /**
	// * Test method for
	// * {@link
	// com.belk.api.util.EndecaResponseProcessor#getRefinementForCompleteDimension
	// (Dimension,StringBuffer,Iterator,correlationId)}
	// * @throws Exception
	// */
	// @Test
	// public final void testGetRefinementForCompleteDimension() throws
	// Exception{
	// final Dimension dimension = PowerMockito.mock(Dimension.class);
	// final StringBuffer refinementString =
	// PowerMockito.mock(StringBuffer.class);
	// final Iterator refinementIterator = PowerMockito.mock(Iterator.class);
	// Whitebox.invokeMethod(this.responseProcessor,
	// "getRefinementForCompleteDimension",dimension,refinementString,refinementIterator,this.correlationId);
	// }
}
