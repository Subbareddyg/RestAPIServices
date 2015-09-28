package com.belk.api.model.categorydetails;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * 
 * Unit Testing related to SubCategory class The unit test cases evaluates the
 * way the methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 29 Oct, 2013
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ SubCategory.class })
public class TestSubCategory extends TestCase {

	/**
	 * Method to test the categoryId
	 */
	@Test
	public final void testCategoryId() {
		final SubCategory subCategory = new SubCategory();
		subCategory.setCategoryId("");
		assertTrue(subCategory.getCategoryId() == "");
	}

	/**
	 * Method to test Name
	 */
	@Test
	public final void testName() {
		final SubCategory subCategory = new SubCategory();
		subCategory.setName("Category");
		assertTrue(subCategory.getName() == "Category");
	}

	/**
	 * Method to test ParentCategoryId
	 */
	@Test
	public final void testParentCategoryId() {
		final SubCategory subCategory = new SubCategory();
		subCategory.setParentCategoryId("");
		assertTrue(subCategory.getParentCategoryId() == "");
	}

	/**
	 * Test method to test the method that return the categoryAttributes.
	 * 
	 */
	@Test
	public final void testSetCategoryAttributes() {

		final SubCategory subCategory = new SubCategory();
		final List<Attribute> attributeList = new ArrayList<Attribute>();
		subCategory.setCategoryAttributes(attributeList);
		assertTrue(subCategory.getCategoryAttributes() == attributeList);

	}
}
