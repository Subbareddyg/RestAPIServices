package com.belk.api.model.categoryproduct;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * 
 * Unit Testing related to CategoryAttribute class The unit test cases evaluates
 * the way the methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 29 Oct, 2013
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ CategoryAttribute.class })
public class TestCategoryAttribute extends TestCase {
	/**
	 * Test method for
	 * {@link com.belk.api.model.categoryproduct.categoryAttribute#setHasSubCategories(String)}
	 */
	@Test
	public final void testSetHasSubCategories() {

		final CategoryAttribute categoryAttribute = new CategoryAttribute();
		categoryAttribute.setHasSubCategories("Yes");
		assertTrue(categoryAttribute.getHasSubCategories() == "Yes");

	}

	/**
	 * Test method for
	 * {@link com.belk.api.model.categoryproduct.categoryAttribute#setHasSubCategories(String)}
	 */
	@Test
	public final void testGetHasSubCategories() {

		final CategoryAttribute categoryAttribute = new CategoryAttribute();
		categoryAttribute.setHasSubCategories("Yes");
		assertTrue(categoryAttribute.getHasSubCategories() == "Yes");

	}

	/**
	 * Test method for
	 * {@link com.belk.api.model.categoryproduct.categoryAttribute#setTotalProducts(String)}
	 */
	@Test
	public final void testSetTotalProducts() {

		final CategoryAttribute categoryAttribute = new CategoryAttribute();
		categoryAttribute.setTotalProducts("38398");
		assertTrue(categoryAttribute.getTotalProducts() == "38398");

	}

	/**
	 * Test method for
	 * {@link com.belk.api.model.categoryproduct.categoryAttribute#getTotalProducts(String)}
	 */
	@Test
	public final void testGetTotalProducts() {

		final CategoryAttribute categoryAttribute = new CategoryAttribute();
		categoryAttribute.setTotalProducts("38398");
		assertTrue(categoryAttribute.getTotalProducts() == "38398");

	}

	/**
	 * Test method for
	 * {@link com.belk.api.model.categoryproduct.categoryAttribute#getTotalSkus()}
	 */
	@Test
	public final void testSetTotalSkus() {

		final CategoryAttribute categoryAttribute = new CategoryAttribute();
		categoryAttribute.setTotalSkus("38398");
		assertTrue(categoryAttribute.getTotalSkus() == "38398");

	}

	/**
	 * Test method for
	 * {@link com.belk.api.model.categoryproduct.categoryAttribute#getTotalSkus()}
	 */
	@Test
	public final void testGetTotalSkus() {

		final CategoryAttribute categoryAttribute = new CategoryAttribute();
		categoryAttribute.setTotalSkus("152643");
		assertTrue(categoryAttribute.getTotalSkus() == "152643");
	}

}
