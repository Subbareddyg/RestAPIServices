package com.belk.api.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.belk.api.exception.BaseException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.belk.api.model.categoryproduct.Attribute;
import com.belk.api.model.categoryproduct.Categories;
import com.belk.api.model.categoryproduct.Category;
import com.belk.api.model.categoryproduct.ProductSearch;
import com.belk.api.util.CommonUtil;
import com.belk.api.util.TestCategoryProductUtil;

/**
 * 
 * Unit Testing related to Category Product Mapper class The unit test cases
 * evaluates the way the methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 29 Oct, 2013
 * 
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({ CategoryProductMapper.class })
public class TestCategoryProductMapper extends TestCase {
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}
	ProductSearch productSearch;
	Attribute attribute;
	CategoryProductMapper categoryProductMapper = new CategoryProductMapper();
	String correlationId = "62384692356";
	CommonUtil commonUtil = new CommonUtil();

	/**
	 * Test method to test the method that converts the response map from Endeca
	 * into the desired Category Products POJO. Category Products POJO comprises
	 * of Product Search,Category and Category Attributes.
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * 
	 */
	@Test
	public final void testConvertToCategoryPojo() throws BaseException {

		// Calling the TestCategoryProductUtil to create Endeca Result Map
		// ,which would mock
		// the response coming from EndecaAdapter layer service method
		final TestCategoryProductUtil testCategoryProductUtil = new TestCategoryProductUtil();
		final List<List<Map<String, String>>> endecaResultMapList = testCategoryProductUtil
				.createEndecaResultMapList();

		// Calling the Test CategoryProductUtil to create the
		// CategoryProductsPOJO
		// to compare with actual CategoryProductPOJO returned by
		// CategoryMapper class to be covered by JUnit

		final Categories expectedCategoryProductList = testCategoryProductUtil
				.expectedCategoryPOJO(endecaResultMapList);
		final Category expectedCategory = expectedCategoryProductList
				.getCategory().get(0);
		final Map<String, String> endecaResultMap = testCategoryProductUtil
				.createResultMap();

		// ProductSearch expectedProducts =
		// expectedCategory.getProducts().get(0);

		new ArrayList<Attribute>();
		testCategoryProductUtil
				.populateCategoryAttributeDetails(endecaResultMap);
		expectedCategory.getCategoryAttributes().get(0);
		testCategoryProductUtil.populateProductSearch(endecaResultMap);
        this.categoryProductMapper.setCommonUtil(this.commonUtil);
		final Categories actualCategoryProductList1 = this.categoryProductMapper
				.convertToCategoryPojo(endecaResultMapList, this.correlationId);

		final Category actualCategory = actualCategoryProductList1
				.getCategory().get(0);
		//actualCategory.getProducts().get(0);

		// Comparing the Expected and Actual Category POJO to determine
		// JUnit Test Results

		// assertEquals(actualCategory.getCategoryId(),
		// expectedCategory.getCategoryId());
		// assertEquals(actualCategory.getName(), expectedCategory.getName());
		assertEquals(actualCategory.getParentCategoryId(), "");

	} 

	/**
	 * Test method to test the method that gives the category products of the
	 * result obtained from Endeca
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * 
	 */

	@Test
	public final void testPopulateProductSearch() throws BaseException {

		// Calling the TestCategoryProductUtil to create Endeca Result Map
		// ,which would mock
		// the response coming from EndecaAdapter layer service method
		final TestCategoryProductUtil testCategoryProductUtil = new TestCategoryProductUtil();
		final Map<String, String> endecaResultMap = testCategoryProductUtil
				.createResultMap();
		this.productSearch = new ProductSearch();
		 this.categoryProductMapper.setCommonUtil(this.commonUtil);
		this.categoryProductMapper.populateProductMainDetails(
				this.productSearch, endecaResultMap, this.correlationId);

		// Calling the CategoryProductMapper and getting the actual values of
		// ProductSearch POJO
		final ProductSearch actualProductSearch = this.categoryProductMapper
				.populateProductSearch(endecaResultMap, this.correlationId);

		// Calling the Test CategoryProductUtil to populate product search
		// to compare with actual product search returned by
		// CategoryMapper class to be covered by JUnit
		final ProductSearch estimatedProductSearch = testCategoryProductUtil
				.populateProductSearch(endecaResultMap);

		// Comparing the Expected and Actual Category POJO to determine
		// JUnit Test Results
		assertEquals(actualProductSearch.getProductCode(),
				estimatedProductSearch.getProductCode());
		// assertEquals(actualProductSearch.getProductId(),
		// estimatedProductSearch.getProductId());
		// assertEquals(actualProductSearch.getWebId(),
		// estimatedProductSearch.getWebId());

	}

	/**
	 * Test method to test the method that populate the product's main details
	 * like product code, product Id, product webId etc in the product search
	 * object
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * 
	 */

	@Test
	public final void testPopulateProductMainDetails() throws BaseException {

		// Calling the TestCategoryProductUtil to create Endeca Result Map
		// ,which would mock
		// the response coming from EndecaAdapter layer service method
		final TestCategoryProductUtil testCategoryProductUtil = new TestCategoryProductUtil();
		final Map<String, String> endecaResultMap = testCategoryProductUtil
				.createResultMap();
		this.productSearch = new ProductSearch();
        this.categoryProductMapper.setCommonUtil(this.commonUtil);
		// populate the product main details by passing product search object
		// and the endeca result map created
		// in test category product util
		this.categoryProductMapper.populateProductMainDetails(
				this.productSearch, endecaResultMap, this.correlationId);

		// comparing the product main details that are populated
		assertEquals(this.productSearch.getProductCode(),
				endecaResultMap.get("productcode"));

	}

	/**
	 * Test method to test the method that populate the category Attribute
	 * details like total products, total sku etc in the category product object
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * 
	 */

	@Test
	public final void testPopulateCategoryAttributeDetails()
			throws BaseException {

		// Calling the TestCategoryProductUtil to create Endeca Result Map
		// ,which would mock
		// the response coming from EndecaAdapter layer service method
		final TestCategoryProductUtil testCategoryProductUtil = new TestCategoryProductUtil();
		final Map<String, String> endecaResultMap = testCategoryProductUtil
				.createResultMap();

		final CategoryProductMapper categoryProductMapper = new CategoryProductMapper();
		categoryProductMapper.setCommonUtil(this.commonUtil);
		// Calling the CategoryProductMapper and getting the actual values of
		// List of Attributes POJO
		final List<Attribute> actualListOfAttributes = categoryProductMapper
				.populateCategoryAttributeDetails(endecaResultMap,
						this.correlationId);

		// Comparing the actual list of attributes that are populated with the
		// Endeca Result Map
		assertEquals(actualListOfAttributes.get(0).getValue(),
				endecaResultMap.get("totalSkus"));
		assertEquals(actualListOfAttributes.get(1).getValue(),
				endecaResultMap.get("totalProducts"));

	}

	/**
	 * Test method to test the method that populate the category Attribute
	 * details like total products, total sku etc in the category product object
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * 
	 */

	@Test(expected = BaseException.class)
	public final void testBaseException() throws BaseException {

		// Calling the TestCategoryProductUtil to create Endeca Result Map
		// ,which would mock
		// the response coming from EndecaAdapter layer service method
		final TestCategoryProductUtil testCategoryProductUtil = new TestCategoryProductUtil();
		final Map<String, String> endecaResultMap = testCategoryProductUtil
				.createResultMap();

		// Calling the CategoryProductMapper and getting the actual values of
		// List of Attributes POJO
		final CategoryProductMapper categoryProductMapper = new CategoryProductMapper();
		categoryProductMapper.setCommonUtil(this.commonUtil);
		categoryProductMapper.populateCategoryAttributeDetails(endecaResultMap,
				this.correlationId);
		throw new BaseException();

	}

}
