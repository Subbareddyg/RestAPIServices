/**
 * 
 */
package com.belk.api.mapper;

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

import com.belk.api.exception.BaseException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.belk.api.model.productsearch.ParentCategory;
import com.belk.api.model.productsearch.Price;
import com.belk.api.model.productsearch.ProductSearch;
import com.belk.api.model.productsearch.Search;
import com.belk.api.util.CommonUtil;
import com.belk.api.util.ErrorLoader;
import com.belk.api.util.TestSearchMapperUtil;

/**
 * Mapper class to test the correctness of the methods of the
 * {@link SearchMapper} class
 * 
 * @author Mindtree
 * @date 05 Nov, 2013
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ SearchMapper.class, ParentCategory.class, ErrorLoader.class })
public class TestSearchMapper extends TestCase {
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}
	private final String populateProductSearch = "populateProductSearch";
	private final String populateSearchReport = "populateSearchReport";
	private final String populateProductMainDetails = "populateProductMainDetails";
	private final String populateProductPrice = "populateProductPrice";
	private final String populateProductMiscDetails = "populateProductMiscDetails";
	private final String populateProductFlag = "populateProductFlag";
	private final String populateProductPromotionDetails = "populateProductPromotionDetails";
	private final String populateProductAttributes = "populateProductAttributes";
	private final String populateProductExtendedAttributes = "populateProductExtendedAttributes";
	private final String populateListPrice = "populateListPrice";
	private final String populateSalePrice = "populateSalePrice";
	private final String populateCategory = "populateCategory";
	private final String correlationId = "7329759830925";

	/**
	 * @throws java.lang.Exception
	 */
	private SearchMapper searchMapper;
	private ParentCategory parentCategory;
	private SearchMapper mapperSpy;
	private CommonUtil commonUtil;

	/**
	 * setting up objects which would be used in the test class
	 * 
	 * @throws Exception
	 *             when tried with PowerMockito for spying Exception will be
	 *             thrown on error
	 */
	@Override
	@Before
	public final void setUp() throws Exception {
		searchMapper = new SearchMapper();
		parentCategory = new ParentCategory();
		mapperSpy = PowerMockito.spy(new SearchMapper());
		commonUtil = new CommonUtil();
		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		Map<String, String> errorPropertiesMap = new HashMap<String, String>();
		errorPropertiesMap.put("11523", "internal server error");
		errorLoader.setErrorPropertiesMap(errorPropertiesMap);
		this.searchMapper.setErrorLoader(errorLoader);
		this.mapperSpy.setErrorLoader(errorLoader);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.mapper.SearchMapper#tokenizeAttributeList(java.lang.String)}
	 * .
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 */

	// (expected = BaseException.class)
	@Test
	public final void testTokenizeAttributeList() throws BaseException {
		final int actualSize = TestSearchMapperUtil.getSearchAttribute().size();

		assertEquals(
				actualSize,
				searchMapper.tokenizeAttributeList(
						TestSearchMapperUtil.getAttributeListValue(),
						correlationId).size());

	}

	/**
	 * Test method to test the convertToSearchPojo method in SearchMapper
	 * 
	 * @throws Exception
	 *             - exception
	 */
	@Test
	public final void testConvertToSearchPojo() throws Exception {

		PowerMockito.mockStatic(SearchMapper.class);


		Map<String, String> resultMap;

		resultMap = TestSearchMapperUtil.createProductSearchResultMap();

		final CommonUtil commonUtil = PowerMockito.mock(CommonUtil.class);

		searchMapper.setCommonUtil(commonUtil);

		mapperSpy.setCommonUtil(commonUtil);

		PowerMockito.when(mapperSpy, populateProductSearch, resultMap,
				correlationId).thenReturn(
						TestSearchMapperUtil.getProductSearch());

		resultMap = TestSearchMapperUtil.createDimensionResultMap();

		resultMap = TestSearchMapperUtil.createSearchReportResultMap();

		final Search actualSearch = searchMapper.convertToSearchPojo(
				TestSearchMapperUtil.createResultMapList(), correlationId);

		assertNotNull(actualSearch);

		// assertEquals(expectedSearch, actualSearch);

	}

	/**
	 * Test method to test the populateProductSearch method in SearchMapper
	 * 
	 * @throws Exception
	 *             - exception
	 */
	@Test
	public final void testPopulateProductSearch() throws Exception {

		final SearchMapper mockSpy = PowerMockito.spy(new SearchMapper());

		final ProductSearch productSearch = new ProductSearch();

		PowerMockito.doNothing().when(mockSpy, populateProductMainDetails,
				productSearch,
				TestSearchMapperUtil.createProductSearchResultMap(),
				correlationId);

		PowerMockito.doNothing().when(mockSpy, populateProductPrice,
				productSearch,
				TestSearchMapperUtil.createProductSearchResultMap(),
				correlationId);

		PowerMockito.doNothing().when(mockSpy, populateProductMiscDetails,
				productSearch,
				TestSearchMapperUtil.createProductSearchResultMap(),
				correlationId);

		PowerMockito.doNothing().when(mockSpy, populateProductFlag,
				productSearch,
				TestSearchMapperUtil.createProductSearchResultMap(),
				correlationId);

		PowerMockito.doNothing().when(mockSpy,
				populateProductPromotionDetails, productSearch,
				TestSearchMapperUtil.createProductSearchResultMap(),
				correlationId);

		PowerMockito.doNothing().when(mockSpy, populateProductAttributes,
				productSearch,
				TestSearchMapperUtil.createProductSearchResultMap(),
				correlationId);

		PowerMockito.doNothing().when(mockSpy,
				populateProductExtendedAttributes, productSearch,
				TestSearchMapperUtil.createProductSearchResultMap(),
				correlationId);

		final CommonUtil commonUtil = PowerMockito.mock(CommonUtil.class);
		mockSpy.setCommonUtil(commonUtil);

		PowerMockito.when(mockSpy, populateProductSearch,
				TestSearchMapperUtil.createProductSearchResultMap(),
				correlationId).thenReturn(
						TestSearchMapperUtil.getProductSearch());

	}

	/**
	 * Test method to test the populateProductMainDetails method in SearchMapper
	 * 
	 * @throws Exception
	 *             - exception
	 * 
	 */
	@Test
	public final void testPopulateProductMainDetails() throws Exception {

		final SearchMapper mockSpy = PowerMockito.spy(new SearchMapper());

		final ProductSearch productSearch = new ProductSearch();

		PowerMockito.doNothing().when(mockSpy, populateProductMainDetails,
				productSearch,
				TestSearchMapperUtil.createProductSearchResultMap(),
				correlationId);
	}

	/**
	 * Test method to test the populateProductMainDetails method in SearchMapper
	 * 
	 * @throws Exception
	 *             - exception
	 * 
	 */
	@Test
	public final void testPopulateProductPrice() throws Exception {

		final SearchMapper mockSpy = PowerMockito.spy(new SearchMapper());

		final List<Price> productPrice = new ArrayList<Price>();

		final ProductSearch productSearch = new ProductSearch();

		PowerMockito.doNothing().when(mockSpy, populateListPrice,
				TestSearchMapperUtil.createProductSearchResultMap(),
				productPrice, correlationId);

		PowerMockito.doNothing().when(mockSpy, populateSalePrice,
				TestSearchMapperUtil.createProductSearchResultMap(),
				productPrice, correlationId);

		PowerMockito.doNothing().when(mockSpy, populateProductPrice,
				productSearch,
				TestSearchMapperUtil.createProductSearchResultMap(),
				correlationId);

	}

	/**
	 * @throws Exception
	 *             - exception
	 * 
	 */
	@Test
	public final void testPopulateListPrice() throws Exception {

		final SearchMapper mockSpy = PowerMockito.spy(new SearchMapper());

		final List<Price> productPrice = new ArrayList<Price>();

		PowerMockito.doNothing().when(mockSpy, populateListPrice,
				TestSearchMapperUtil.createProductSearchResultMap(),
				productPrice, correlationId);

	}

	/**
	 * @throws Exception
	 *             - exception
	 * 
	 */
	@Test
	public final void testPpopulateSalePrice() throws Exception {

		final SearchMapper mockSpy = PowerMockito.spy(new SearchMapper());

		final List<Price> productPrice = new ArrayList<Price>();

		PowerMockito.doNothing().when(mockSpy, populateSalePrice,
				TestSearchMapperUtil.createProductSearchResultMap(),
				productPrice, correlationId);

	}

	/**
	 * @throws Exception
	 *             - exception
	 * 
	 */
	@Test
	public final void testPopulateProductMiscDetails() throws Exception {

		final SearchMapper mockSpy = PowerMockito.spy(new SearchMapper());

		final ProductSearch productSearch = new ProductSearch();

		PowerMockito.doNothing().when(mockSpy, populateProductMiscDetails,
				productSearch,
				TestSearchMapperUtil.createProductSearchResultMap(),
				correlationId);

	}

	/**
	 * @throws Exception
	 *             - exception
	 * 
	 */
	@Test
	public final void testPopulateProductFlag() throws Exception {

		final SearchMapper mockSpy = PowerMockito.spy(new SearchMapper());

		final ProductSearch productSearch = new ProductSearch();

		PowerMockito.doNothing().when(mockSpy, populateProductFlag,
				productSearch,
				TestSearchMapperUtil.createProductSearchResultMap(),
				correlationId);

	}

	/**
	 * @throws Exception
	 *             - exception
	 * 
	 */
	@Test
	public final void testPopulateProductPromotionDetails() throws Exception {

		final SearchMapper mockSpy = PowerMockito.spy(new SearchMapper());

		final ProductSearch productSearch = new ProductSearch();

		PowerMockito.doNothing().when(mockSpy,
				populateProductPromotionDetails, productSearch,
				TestSearchMapperUtil.createProductSearchResultMap(),
				correlationId);

	}

	/**
	 * @throws Exception
	 *             - exception
	 * 
	 */
	@Test
	public final void testPopulateProductAttributes() throws Exception {

		final SearchMapper mockSpy = PowerMockito.spy(new SearchMapper());

		final ProductSearch productSearch = new ProductSearch();

		PowerMockito.doNothing().when(mockSpy, populateProductAttributes,
				productSearch,
				TestSearchMapperUtil.createProductSearchResultMap(),
				correlationId);

	}

	/**
	 * @throws Exception
	 *             - exception
	 * 
	 */
	@Test
	public final void testPopulateProductExtendedAttributes() throws Exception {

		final SearchMapper mockSpy = PowerMockito.spy(new SearchMapper());

		final ProductSearch productSearch = new ProductSearch();

		PowerMockito.doNothing().when(mockSpy,
				populateProductExtendedAttributes, productSearch,
				TestSearchMapperUtil.createProductSearchResultMap(),
				correlationId);

	}

	/**
	 * @throws Exception
	 *             - exception
	 * 
	 */
	@Test
	public final void testPopulateDimension() throws Exception {

		mapperSpy.setCommonUtil(commonUtil);

		final List<ParentCategory> categories = new ArrayList<ParentCategory>();

		PowerMockito.when(mapperSpy, "populateDimension",
				TestSearchMapperUtil.createProductSearchResultMap(),
				categories, correlationId).thenReturn(
						TestSearchMapperUtil.getDimension());

	}

	/**
	 * @throws Exception
	 *             - exception
	 * 
	 */
	@Test
	public final void testPopulateSearchReport() throws Exception {

		final SearchMapper mockSpy = PowerMockito.spy(new SearchMapper());
		mockSpy.setCommonUtil(commonUtil);
		PowerMockito.when(mockSpy, populateSearchReport,
				TestSearchMapperUtil.createProductSearchResultMap(),
				correlationId).thenReturn(
						TestSearchMapperUtil.getSearchReport());
	}

	/**
	 * Test method for
	 * {@link com.belk.api.mapper.SearchMapper#tokenizeAttributeList(java.lang.String)}
	 * .
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 */
	@Test
	public final void testTokenizeSubCategoryList() throws BaseException {
		final int actualSize = TestSearchMapperUtil.getSubCategoryList().size();

		assertEquals(
				actualSize,
				searchMapper.tokenizeSubCategoryList(
						TestSearchMapperUtil.createDataForSubCategory(),
						correlationId).size());

	}

	/**
	 * Test method for
	 * {@link com.belk.api.mapper.SearchMapper#tokenizeAttributeList(java.lang.String)}
	 * .
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 */
	@Test
	public final void testTokenizeRefinementList() throws BaseException {
		final int actualSize = TestSearchMapperUtil.getRefinementList().size();

		assertEquals(
				actualSize,
				searchMapper.tokenizeRefinementList(
						TestSearchMapperUtil.createDataForSubCategory(),
						correlationId).size());

	}

}
