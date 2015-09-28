package com.belk.api.mapper;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.belk.api.exception.BaseException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.belk.api.model.categorydetails.Categories;
import com.belk.api.model.categorydetails.SubCategory;
import com.belk.api.util.CommonUtil;
import com.belk.api.util.TestCategoryDetailsMapperUtil;

/**
 * @author Mindtree
 * @date Nov 14 2013
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ CategoryDetailsMapper.class })
public class TestCategoryDetailsMapper extends TestCase {
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}
	CategoryDetailsMapper categoryDetailsMapper = new CategoryDetailsMapper();
	String correlationId = "123456789";
	CommonUtil commonUtil = new CommonUtil();

	/**
	 * Test method to test the convertToCategoryDetailsPojo method in
	 * CategoryDetailsMapper
	 * 
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 */
	@Test
	public final void testConvertToCategoryDetailPojo() throws BaseException {

		// final Categories categoriesResult;

		final List<List<Map<String, String>>> responseList = TestCategoryDetailsMapperUtil
				.endecaResultList();
        this.categoryDetailsMapper.setCommonUtil(this.commonUtil);
		this.categoryDetailsMapper.convertToCategoryDetailsPojo(responseList,
				this.correlationId);

		final Categories actualCategoriesResult = TestCategoryDetailsMapperUtil
				.createCategoriesResult();

		assertNotNull(actualCategoriesResult);
		// assertEquals(expectedCategoriesResult.getCategories().get(0)
		// .getCategoryId(), actualCategoriesResult.getCategories().get(0)
		// .getCategoryId());
	}

	/**
	 * Test method to test the tokenizeSubCategoryList method in
	 * CategoryDetailsMapper
	 * 
	 * @throws Exception
	 *             Exception
	 */
	@Test
	public final void testTokenizeSubCategoryList() throws Exception {

		TestCategoryDetailsMapperUtil.endecaResultList();
		final String refinementListValue = TestCategoryDetailsMapperUtil
				.getRefinementListValue();
		final CategoryDetailsMapper categoryDetailsMapperSpy = PowerMockito
				.spy(new CategoryDetailsMapper());

		// final List<SubCategory> expectedSubCategoriesList =
		// this.categoryDetailsMapper
		// .tokenizeSubCategoryList(refinementListValue, this.correlationId);
		PowerMockito.when(categoryDetailsMapperSpy, "tokenizeSubCategoryList",
				refinementListValue, this.correlationId);
		final List<SubCategory> actualSubCategoriesList = TestCategoryDetailsMapperUtil
				.getSubCategoriesList();

		assertNotNull(actualSubCategoriesList);

	}

}
