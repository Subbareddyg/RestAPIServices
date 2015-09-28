package com.belk.api.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.belk.api.exception.BaseException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.belk.api.model.productdetails.Price;
import com.belk.api.model.productdetails.ProductDetails;
import com.belk.api.model.productdetails.ProductList;
import com.belk.api.model.productdetails.SKU;
import com.belk.api.service.constants.MapperConstants;
import com.belk.api.util.CommonUtil;
import com.belk.api.util.TestPatternProductServiceUtil;

/**
 * Unit Testing related to PatternProductMapper class is performed. <br />
 * {@link TestPatternProductMapper} class is written for testing methods in
 * {@link convertToPatternProductDetailsPojo} The unit test cases evaluates the
 * way the methods behave for the inputs given.
 * 
 * @author Mindtree
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(PatternProductMapper.class)
public class TestPatternProductMapper {

	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}
	List<Map<String, String>> endecaResultMap;
	Map<String, String> productDetailsResultMap;
	PatternProductMapper patternProductMapper;
	ProductList productDetailResults;
	List<ProductDetails> productDetailsList;
	ProductDetails productDetails;
	String expectedResults;
	String actualResults;
	String corrId = "1234567891234567";
	CommonUtil commonUtil = new CommonUtil();

	/**
	 * Initial set up ahead to start unit test case for class
	 */
	@Before
	public final void setUp() {
		this.patternProductMapper = new PatternProductMapper();
		this.productDetailResults = new ProductList();
		this.productDetailsList = new ArrayList<ProductDetails>();
		this.productDetails = new ProductDetails();
		this.productDetails.setProductCode("3203375NA10011");
		this.productDetailsList.add(this.productDetails);
		this.productDetailResults.setProducts(this.productDetailsList);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.mapper.PatternProductMapper#convertToProductDetailsPojo()}
	 * . Testing is done to check whether the response Object is obtained. when
	 * convertToProductDetailsPojo() method is called .
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 */
	@Test
	public final void testConvertToProductDetailsPojoWithMutipleValues()
			throws BaseException {

		final CommonUtil commonUtil = PowerMockito.mock(CommonUtil.class);
		this.patternProductMapper.setCommonUtil(commonUtil);
		this.patternProductMapper.getCommonUtil();
		//OPTIONS Parameter to be added in future
				Map<String,List<String>> optionNodes = null;
				
		this.productDetailResults = this.patternProductMapper
				.convertToPatternProductDetailsPojo(
						this.createEndecaResultMap("comma"), optionNodes,this.corrId);
		this.actualResults = this.productDetailResults.getProducts().get(0)
				.getProductCode();
		this.expectedResults = this.productDetailResults.getProducts().get(0)
				.getProductCode();
		assertEquals(this.expectedResults, this.actualResults);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.mapper.PatternProductMapper#convertToProductDetailsPojo()}
	 * . Testing is done to check whether the response Object is obtained. when
	 * convertToProductDetailsPojo() method is called .
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 */
	@Test
	public final void testConvertToProductDetailsPojoWithOutMutipleValues()
			throws BaseException {
		final CommonUtil commonUtil = PowerMockito.mock(CommonUtil.class);
		this.patternProductMapper.setCommonUtil(commonUtil);
		//OPTIONS Parameter to be added in future
				Map<String,List<String>> optionNodes = null;
				
		this.productDetailResults = this.patternProductMapper
				.convertToPatternProductDetailsPojo(this.createEndecaResultMap(" "),optionNodes,
						this.corrId);
		this.actualResults = this.productDetailResults.getProducts().get(0)
				.getProductCode();
		this.expectedResults = this.productDetailResults.getProducts().get(0)
				.getProductCode();
		assertEquals(this.expectedResults, this.actualResults);
	}

	/**
	 * Method to create Endeca Result list using the results from Endeca.
	 * 
	 * @param seperator
	 *            input parameter
	 * @return List<Map<String, String>>
	 * 
	 */

	private List<Map<String, String>> createEndecaResultMap(
			final String seperator) {
		final List<Map<String, String>> resultLt = new ArrayList<Map<String, String>>();
		final Map<String, String> endecaResultMap1 = new HashMap<String, String>();
		final Map<String, String> endecaResultMap2 = new HashMap<String, String>();
		endecaResultMap1.put(MapperConstants.P_PRODUCT_CODE, "3203375NA10011");
		endecaResultMap1.put(MapperConstants.P_AVAILABLE_IN_STORE, "No");
		endecaResultMap1.put(MapperConstants.P_AVAILABLE_ONLINE, "Yes");
		endecaResultMap1.put(MapperConstants.P_CLEARANCE, "No");
		this.createResultMap(endecaResultMap1);
		if ("comma".equals(seperator)) {
			endecaResultMap1
					.put(MapperConstants.P_PATH,
							"/Assortments/Main/Belk_Primary/Shop_All, /Assortments/Main/Belk_Primary");
			endecaResultMap1.put(MapperConstants.P_PATTERN_CODE,
					"1689949383353220, 1689949383353234");
			endecaResultMap1.put(MapperConstants.P_COLOR, "Pink,Black");
			endecaResultMap1.put(MapperConstants.P_IS_PATTERN, "T");
		} else {
			endecaResultMap1.put(MapperConstants.P_PATH,
					"/Assortments/Main/Belk_Primary/Shop_All");
			endecaResultMap1.put(MapperConstants.P_PATTERN_CODE,
					"1689949383353220");
			endecaResultMap1.put(MapperConstants.P_COLOR, "Pink");
			endecaResultMap1.put(MapperConstants.P_IS_PATTERN, "F");
		}

		endecaResultMap2.putAll(endecaResultMap1);
		resultLt.add(endecaResultMap1);
		resultLt.add(endecaResultMap2);
		return resultLt;

	}

	/**
	 * Method to create Endeca Result list using the results from Endeca.
	 * 
	 * @param endecaResultMap1
	 *            input map
	 * 
	 */
	private void createResultMap(final Map<String, String> endecaResultMap1) {
		endecaResultMap1.put(MapperConstants.ON_SALE, "No");
		endecaResultMap1.put(MapperConstants.P_NEW_ARRIVAL, "Yes");
		endecaResultMap1.put(MapperConstants.P_BRAND, "Derek Heart");
		endecaResultMap1.put(MapperConstants.P_WEB_ID, " ");
		endecaResultMap1.put(MapperConstants.P_PRODUCT_ID, " ");
		endecaResultMap1
				.put(MapperConstants.P_PICTURE_URL,
						"http://s7d4.scene7.com/is/image/Belk?layer=0&src=3200048_2988S_A_001_T10L00&layer=comp&");
		endecaResultMap1.put(MapperConstants.P_DEPT_ID, "717");
		endecaResultMap1.put(MapperConstants.P_COLOR, "Pink");
		endecaResultMap1.put(MapperConstants.P_CLASS_NAME, "7172");
		endecaResultMap1.put(MapperConstants.P_IS_BRIDAL, "F");
		endecaResultMap1.put(MapperConstants.P_OBJECT_ID, "1689949383353220");
		endecaResultMap1.put(MapperConstants.P_LIST_PRICE_RANGE, "65.0000");
		endecaResultMap1.put(MapperConstants.P_LISTPRICE, "60.00000");
		endecaResultMap1.put(MapperConstants.P_MIN_LIST_PRICE, "60.0000");
		endecaResultMap1.put(MapperConstants.P_MIN_LIST_PRICE, "58.00000");
		endecaResultMap1.put(MapperConstants.P_SALES_PRICE_RANGE, "65.0000");
		endecaResultMap1.put(MapperConstants.P_SALE_PRICE, "60.00000");
		endecaResultMap1.put(MapperConstants.P_MIN_SALES_PRICE, "60.0000");
		endecaResultMap1.put(MapperConstants.P_MAX_SALES_PRICE, "58.00000");
		endecaResultMap1.put(MapperConstants.P_SKUS_SALE_PRICE, "58.00000");
		endecaResultMap1.put(MapperConstants.P_VENDOR_NUMBER, "3200048");
		endecaResultMap1.put(MapperConstants.P_PRODUCT_NAME, " ");
		endecaResultMap1.put(MapperConstants.P_SHORTDESC, " ");
		endecaResultMap1.put(MapperConstants.P_LONGDESC, " ");
		endecaResultMap1.put(MapperConstants.P_PRODUCT_TYPE, " ");
		endecaResultMap1.put(MapperConstants.P_SKU_NO, "0400672724257");
		endecaResultMap1.put(MapperConstants.P_SKU_UPC, "0786888393846");
		endecaResultMap1.put(MapperConstants.P_SIZE, "Medium (10-13)");
		endecaResultMap1.put(MapperConstants.P_INVENTORY_AVAIL, "163");
		endecaResultMap1.put(MapperConstants.P_INVENTORY_LEVEL, "18");
	}

	/**
	 * Method to populate Product Hierarchy Attributes
	 * 
	 * @throws Exception
	 *             - exception
	 */
	@Test
	public final void testPopulateSkus() throws Exception {
		PowerMockito.spy(new PatternProductMapper());
		final PatternProductMapper patternProductMapper = new PatternProductMapper();
		this.patternProductMapper.setCommonUtil(commonUtil);
		patternProductMapper.setCommonUtil(this.commonUtil);
		new ProductDetails();
		final Map<String, String> endecaResultMap = TestPatternProductServiceUtil
				.getEndecaResultMapForMapperSkus();
		Whitebox.invokeMethod(patternProductMapper, "populateSkus",
				endecaResultMap, this.corrId);
		assertNotNull(patternProductMapper);

	}

	/**
	 * Method to populate Product Hierarchy Attributes
	 * 
	 * @throws Exception
	 *             - exception
	 */
	@Test
	public final void testPopulateSkusForPColor1() throws Exception {
		PowerMockito.spy(new PatternProductMapper());
		this.patternProductMapper.setCommonUtil(commonUtil);
		new ProductDetails();
		final Map<String, String> endecaResultMap = TestPatternProductServiceUtil
				.getEndecaResultMapForSkus();
		Whitebox.invokeMethod(patternProductMapper, "populateSkus",
				endecaResultMap, this.corrId);
		assertNotNull(patternProductMapper);

	}

	/**
	 * Method to populate Path
	 * 
	 * @throws Exception
	 *             - exception
	 */
	@Test
	public final void testPopulatePath() throws Exception {
		PowerMockito.spy(new PatternProductMapper());
		this.patternProductMapper.setCommonUtil(this.commonUtil); 
		
		final List<Price> productPrice = new ArrayList<Price>();
		final Map<String, String> endecaResultMap = TestPatternProductServiceUtil
				.getEndecaResultMapForMapper();
		Whitebox.invokeMethod(patternProductMapper, "populateListPrice",
				endecaResultMap, productPrice, this.corrId);
		assertNotNull(patternProductMapper);

	}

	/**
	 * Method to populate Product Price
	 * 
	 * @throws Exception
	 *             - exception
	 */
	@Test
	public final void testPopulateProductPrice() throws Exception {
		final PatternProductMapper mockSpy = PowerMockito
				.spy(new PatternProductMapper());
		final ProductDetails productDetails = new ProductDetails();
		final Map<String, String> endecaResultMap = new HashMap<String, String>();
		PowerMockito.doNothing().when(mockSpy, "populateProductPrice",
				endecaResultMap, productDetails, this.corrId);

	}

	/**
	 * Method to populate List Price
	 * 
	 * @throws Exception
	 *             - exception
	 */
	@Test
	public final void testPopulateListPrice() throws Exception {
		final PatternProductMapper mockSpy = PowerMockito
				.spy(new PatternProductMapper());
		final List<Price> productPrice = new ArrayList<Price>();
		final Map<String, String> endecaResultMap = new HashMap<String, String>();
		PowerMockito.doNothing().when(mockSpy, "populateListPrice",
				endecaResultMap, productPrice, this.corrId);

	}

	/**
	 * Method to populate Sale Price
	 * 
	 * @throws Exception
	 *             - exception
	 */
	@Test
	public final void testPopulateSalePrice() throws Exception {
		final PatternProductMapper mockSpy = PowerMockito
				.spy(new PatternProductMapper());
		final List<Price> productPrice = new ArrayList<Price>();
		final Map<String, String> endecaResultMap = new HashMap<String, String>();
		PowerMockito.doNothing().when(mockSpy, "populateSalePrice",
				endecaResultMap, productPrice, this.corrId);

	}

	/**
	 * Method to populate Product Flag
	 * 
	 * @throws Exception
	 *             - exception
	 */
	@Test
	public final void testPopulateProductFlag() throws Exception {
		final PatternProductMapper mockSpy = PowerMockito
				.spy(new PatternProductMapper());
		final ProductDetails productDetails = new ProductDetails();
		final Map<String, String> endecaResultMap = new HashMap<String, String>();
		PowerMockito.doNothing().when(mockSpy, "populateProductFlag",
				endecaResultMap, productDetails, this.corrId);

	}

	/**
	 * Method to populate Product Attributes
	 * 
	 * @throws Exception
	 *             - exception
	 */
	@Test
	public final void testPopulateProductAttributes() throws Exception {
		final PatternProductMapper mockSpy = PowerMockito
				.spy(new PatternProductMapper());
		final ProductDetails productDetails = new ProductDetails();
		final Map<String, String> endecaResultMap = new HashMap<String, String>();
		PowerMockito.doNothing().when(mockSpy, "populateProductAttributes",
				endecaResultMap, productDetails, this.corrId);

	}

	/**
	 * Method to populate Child Products
	 * 
	 * @throws Exception
	 *             - exception
	 */
	@Test
	public final void testPopulateChildProducts() throws Exception {
		final PatternProductMapper mockSpy = PowerMockito
				.spy(new PatternProductMapper());
		final ProductDetails productDetails = new ProductDetails();
		final Map<String, String> endecaResultMap = new HashMap<String, String>();
		PowerMockito.doNothing().when(mockSpy, "populateChildProducts",
				endecaResultMap, productDetails, this.corrId);

	}

	/**
	 * Method to populate Color
	 * 
	 * @throws Exception
	 *             - exception
	 */
	@Test
	public final void testPopulateColor() throws Exception {
		PowerMockito.spy(new PatternProductMapper());
		this.patternProductMapper.setCommonUtil(commonUtil);
		final ProductDetails productDetails = new ProductDetails();
		final Map<String, String> endecaResultMap = TestPatternProductServiceUtil
				.getEndecaResultMapForMapper();
		Whitebox.invokeMethod(patternProductMapper, "populateProductFlag",
				endecaResultMap, productDetails, this.corrId);
		assertNotNull(patternProductMapper);

	}

	/**
	 * Method to populate Sku Images
	 * 
	 * @throws Exception
	 *             - exception
	 */
	@Test
	public final void testPopulateSkuImages() throws Exception {
		final PatternProductMapper mockSpy = PowerMockito
				.spy(new PatternProductMapper());
		final SKU sku = new SKU();
		final Map<String, String> endecaResultMap = new HashMap<String, String>();
		PowerMockito.doNothing().when(mockSpy, "populateSkuImages",
				endecaResultMap, sku, this.corrId);

	}

	/**
	 * Method to populate Sku Price
	 * 
	 * @throws Exception
	 *             - exception
	 */
	@Test
	public final void testPopulateSkuPrice() throws Exception {
		PowerMockito.spy(new PatternProductMapper());
		this.patternProductMapper.setCommonUtil(commonUtil);
		new SKU();
		final Map<String, String> endecaResultMap = TestPatternProductServiceUtil
				.getEndecaResultMapForMapper();

		Whitebox.invokeMethod(patternProductMapper, "populateSkus",
				endecaResultMap, this.corrId);
		assertNotNull(patternProductMapper);

	}

	/**
	 * Method to populate Product Details
	 * 
	 * @throws Exception
	 *             - exception
	 */
	@Test
	public final void testPopulateProductDetails() throws Exception {
		final PatternProductMapper mockSpy = PowerMockito
				.spy(new PatternProductMapper());
		final ProductDetails productDetails = new ProductDetails();
		final Map<String, String> endecaResultMap = new HashMap<String, String>();
		PowerMockito.doNothing().when(mockSpy, "populateProductDetails",
				endecaResultMap, null, productDetails, this.corrId);

	}

}
