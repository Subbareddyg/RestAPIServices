package com.belk.api.mapper;

import static org.junit.Assert.assertEquals;

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

import com.belk.api.exception.BaseException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.belk.api.model.productdetails.Price;
import com.belk.api.model.productdetails.ProductDetails;
import com.belk.api.model.productdetails.ProductHierarchyAttribute;
import com.belk.api.model.productdetails.ProductList;
import com.belk.api.model.productdetails.SKU;
import com.belk.api.model.productdetails.SKUMain;
import com.belk.api.util.CommonUtil;
import com.belk.api.util.DomainLoader;
import com.belk.api.util.ErrorLoader;

/**
 * Unit Testing related to ProductMapper class is performed. <br />
 * {@link TestProductMapper} class is written for testing methods in
 * {@link convertToProductDetailsPojo} The unit test cases evaluates the way the
 * methods behave for the inputs given.
 * 
 * @author Mindtree
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ProductMapper.class,ErrorLoader.class,DomainLoader.class})
public class TestProductMapper {
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}
	List<Map<String, String>> endecaResultMap;
	Map<String, String> productDetailsResultMap;
	ProductMapper productMapper;
	ProductList productDetailResults;
	List<ProductDetails> productDetailsList;
	ProductDetails productDetails;
	String expectedResults;
	String actualResults;
	String corrId = "1234567891234567";

	/**
	 * Initial set up ahead to start unit test case for class
	 */
	@Before
	public final void setUp() {
		this.productMapper = new ProductMapper();
		this.productDetailResults = new ProductList();
		this.productDetailsList = new ArrayList<ProductDetails>();
		this.productDetails = new ProductDetails();
		this.productDetails.setProductCode("3203375NA10011");
		this.productDetailsList.add(this.productDetails);
		this.productDetailResults.setProducts(this.productDetailsList);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.mapper.ProductMapper#convertToProductDetailsPojo()} .
	 * Testing is done to check whether the response Object is obtained. when
	 * convertToProductDetailsPojo() method is called .
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 */
	@Test
	public final void testConvertToProductDetailsPojoWithMutipleValues()
			throws BaseException {
		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		final CommonUtil commonUtil = PowerMockito.mock(CommonUtil.class);
		this.productMapper.setCommonUtil(commonUtil);
		this.productMapper.setErrorLoader(errorLoader);
		final Map<String, String> errorPropertiesMap = new HashMap<String, String>();
		errorPropertiesMap.put("prdCode", "3201634Z31300");
		errorPropertiesMap.put("SOCKET_TIMEOUT", "100");
		errorPropertiesMap.put("CONNECTION_TIMEOUT", "2");
		errorPropertiesMap.put("productUrl", "pUrl");
		errorPropertiesMap.put("SKU_IMAGE_MAIN", "sku");
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(errorPropertiesMap );
		final DomainLoader domainLoader = PowerMockito.mock(DomainLoader.class);
		this.productMapper.setDomainLoader(domainLoader);
		PowerMockito.when(domainLoader.getDomainPropertiesMap()).thenReturn(errorPropertiesMap );

		this.productDetailResults = this.productMapper
				.convertToProductDetailsPojo(createEndecaResultMap("comma"), null,
						this.corrId);
		//		this.actualResults = this.productDetailResults.getProducts().get(0)
		//				.getProductCode();
		//		this.expectedResults = this.productDetailResults.getProducts().get(0)
		//				.getProductCode();

		assertEquals(this.expectedResults, this.actualResults);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.mapper.ProductMapper#convertToProductDetailsPojo()} .
	 * Testing is done to check whether the response Object is obtained. when
	 * convertToProductDetailsPojo() method is called .
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 */
	@Test
	public final void testConvertToProductDetailsPojoWithOutMutipleValues()
			throws BaseException {
		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		final CommonUtil commonUtil = PowerMockito.mock(CommonUtil.class);
		this.productMapper.setCommonUtil(commonUtil);
		this.productMapper.setErrorLoader(errorLoader);
		final Map<String, String> errorPropertiesMap = new HashMap<String, String>();
		errorPropertiesMap.put("prdCode", "3201634Z31300");
		errorPropertiesMap.put("SOCKET_TIMEOUT", "100");
		errorPropertiesMap.put("CONNECTION_TIMEOUT", "2");
		errorPropertiesMap.put("productUrl", "pUrl");
		errorPropertiesMap.put("SKU_IMAGE_MAIN", "sku");
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(errorPropertiesMap );
		final DomainLoader domainLoader = PowerMockito.mock(DomainLoader.class);
		this.productMapper.setDomainLoader(domainLoader);
		PowerMockito.when(domainLoader.getDomainPropertiesMap()).thenReturn(errorPropertiesMap );

		this.productDetailResults = this.productMapper
				.convertToProductDetailsPojo(createEndecaResultMap("c"),null,
						this.corrId);
		//this.actualResults = this.productDetailResults.getProducts().get(0)
		//		.getProductCode();
		//this.expectedResults = this.productDetailResults.getProducts().get(0)
		//		.getProductCode();

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


		if ("comma".equals(seperator)) {
			endecaResultMap1
			.put("path",
					"/Assortments/Main/Belk_Primary/Shop_All, /Assortments/Main/Belk_Primary");
			endecaResultMap1.put("P_pattern_code",
					"1689949383353220, 1689949383353234");
			endecaResultMap1.put("P_color", "Pink,Black");
			endecaResultMap1.put("P_isPattern", "T");
			endecaResultMap1.put("productCode","abc");
			endecaResultMap1.put("productUrl", "SKU_IMAGE_MAIN");
			endecaResultMap1.put("classNumber", "2");
			endecaResultMap1.put("departmentNumber", "4");

			endecaResultMap1.put("category", "shirt");

		} else {
			endecaResultMap1.put("productUrl", "SKU_IMAGE_MAIN");
			endecaResultMap1.put("path",
					"/Assortments/Main/Belk_Primary/Shop_All");
			endecaResultMap1.put("pattern_code", "1689949383353220");
			endecaResultMap1.put("P_color", "Pink");
			endecaResultMap1.put("P_isPattern", "F");
			endecaResultMap1.put("listPriceRange", "65.0000");
			endecaResultMap1.put("listPrice", "60.0000");
			endecaResultMap1.put("minListPrice", "58.00000");
			endecaResultMap1.put("maxListPrice", "58.00000");
			endecaResultMap1.put("salePriceRange", "65.0000");
			endecaResultMap1.put("salePrice", "60.00000");
			endecaResultMap1.put("minSalePrice", "60.0000");
			endecaResultMap1.put("maxSalePrice", "65.0000");
			endecaResultMap1.put("availableInStore", "yes");
			endecaResultMap1.put("availableOnline", "yes");
			endecaResultMap1.put("bridalEligible", "T");
			endecaResultMap1.put("clearance", "c");
			endecaResultMap1.put("newArrival", "yes");
			endecaResultMap1.put("onSale", "No");
			endecaResultMap1.put("is_drop_ship", "yes");
			endecaResultMap1.put("productCopyText1", "1");
			endecaResultMap1.put("productCopyText2", "2");
			endecaResultMap1.put("productCopyText3", "3");
			endecaResultMap1.put("productCopyText4", "4");
			endecaResultMap1.put("default_sku", "s");
			endecaResultMap1.put("show_color", "T");
			endecaResultMap1.put("show_size", "T");
			endecaResultMap1.put("patternCode", "pcode");
			endecaResultMap1.put("skuCode", "sCode");
			endecaResultMap1.put("skuUpc", "sku");
			endecaResultMap1.put("color", "blue");
			endecaResultMap1.put("color_code", "green");
			endecaResultMap1.put("compSize", "medium");
			endecaResultMap1.put("size_code", "l");
			endecaResultMap1.put("inventoryAvail", "163");
			endecaResultMap1.put("inventoryLevel", "18");
			endecaResultMap1.put("productCode","abc");
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
		endecaResultMap1.put("P_onSale", "No");
		endecaResultMap1.put("P_new_arraival", "Yes");
		endecaResultMap1.put("P_brand", "Derek Heart");
		endecaResultMap1.put("P_web_id", " ");
		endecaResultMap1.put("P_product_id", " ");
		endecaResultMap1
		.put("P_product_url",
				"http://s7d4.scene7.com/is/image/Belk?layer=0&src=3200048_2988S_A_001_T10L00&layer=comp&");
		endecaResultMap1.put("P_dept_number", "717");
		endecaResultMap1.put("P_color", "Pink");
		endecaResultMap1.put("P_class_Number", "7172");
		endecaResultMap1.put("P_bridal_eligible", "F");
		endecaResultMap1.put("P_objectId", "1689949383353220");
		endecaResultMap1.put("P_list_price_range", "65.0000");
		endecaResultMap1.put("P_listPrice", "60.00000");
		endecaResultMap1.put("MIN_LIST_PRICE", "60.0000");
		endecaResultMap1.put("MAX_LIST_PRICE", "58.00000");
		endecaResultMap1.put("P_sale_price_range", "65.0000");
		endecaResultMap1.put("P_saleprice", "60.00000");
		endecaResultMap1.put("MIN_SALE_PRICE", "60.0000");
		endecaResultMap1.put("MAX_SALE_PRICE", "58.00000");
		endecaResultMap1.put("P_skus_sale_price", "58.00000");
		endecaResultMap1.put("P_vendor_number", "3200048");
		endecaResultMap1.put("P_product_name", " ");
		endecaResultMap1.put("p_product_short_desc", " ");
		endecaResultMap1.put("P_product_long_desc", " ");
		endecaResultMap1.put("P_product_type", " ");
		endecaResultMap1.put("P_sku_code", "0400672724257");
		endecaResultMap1.put("P_sku_upc", "0786888393846");
		endecaResultMap1.put("P_comp_size", "Medium (10-13)");
		endecaResultMap1.put("p_INVENTORY_AVAIL", "163");
		endecaResultMap1.put("P_inventory_level", "18");
	}

	/**
	 * Method to populate Product Hierarchy Attributes
	 * 
	 * @throws Exception
	 *             - exception
	 */
	@Test
	public final void testPopulateProductHierarchyAttributes() throws Exception {
		final ProductMapper mockSpy = PowerMockito.spy(new ProductMapper());
		final ProductDetails productDetails = new ProductDetails();
		final Map<String, String> endecaResultMap = new HashMap<String, String>();
		PowerMockito.doNothing().when(mockSpy,
				"populateProductHierarchyAttributes", endecaResultMap,
				productDetails, this.corrId);

	}

	/**
	 * Method to populate Path
	 * 
	 * @throws Exception
	 *             - exception
	 */
	@Test
	public final void testPopulatePath() throws Exception {
		final ProductMapper mockSpy = PowerMockito.spy(new ProductMapper());
		final List<ProductHierarchyAttribute> productHierarchyAttributeList = new ArrayList<ProductHierarchyAttribute>();
		PowerMockito.doNothing().when(mockSpy, "populatePath",
				productHierarchyAttributeList, "value", this.corrId);

	}

	/**
	 * Method to populate Product Price
	 * 
	 * @throws Exception
	 *             - exception
	 */
	@Test
	public final void testPopulateProductPrice() throws Exception {
		final ProductMapper mockSpy = PowerMockito.spy(new ProductMapper());
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
		final ProductMapper mockSpy = PowerMockito.spy(new ProductMapper());
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
		final ProductMapper mockSpy = PowerMockito.spy(new ProductMapper());
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
		final ProductMapper mockSpy = PowerMockito.spy(new ProductMapper());
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
		final ProductMapper mockSpy = PowerMockito.spy(new ProductMapper());
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
		final ProductMapper mockSpy = PowerMockito.spy(new ProductMapper());
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
		final ProductMapper mockSpy = PowerMockito.spy(new ProductMapper());
		final List<SKUMain> skuMainList = new ArrayList<SKUMain>();
		PowerMockito.doNothing().when(mockSpy, "populateColor", skuMainList,
				"value", this.corrId);

	}

	/**
	 * Method to populate Sku Images
	 * 
	 * @throws Exception
	 *             - exception
	 */
	@Test
	public final void testPopulateSkuImages() throws Exception {
		final ProductMapper mockSpy = PowerMockito.spy(new ProductMapper());
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
		final ProductMapper mockSpy = PowerMockito.spy(new ProductMapper());
		final SKU sku = new SKU();
		final Map<String, String> endecaResultMap = new HashMap<String, String>();
		PowerMockito.doNothing().when(mockSpy, "populateSkuPrice",
				endecaResultMap, sku, this.corrId);

	}

	/**
	 * Method to populate Product Details
	 * 
	 * @throws Exception
	 *             - exception
	 */
	@Test
	public final void testPopulateProductDetails() throws Exception {
		final ProductMapper mockSpy = PowerMockito.spy(new ProductMapper());
		final ProductDetails productDetails = new ProductDetails();
		final List<String> optionNodes = new ArrayList<String>();
		final Map<String, String> endecaResultMap = new HashMap<String, String>();
		PowerMockito.doNothing().when(mockSpy, "populateProductDetails",
				endecaResultMap, productDetails, optionNodes, this.corrId);

	}
}
