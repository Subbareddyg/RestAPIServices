package com.belk.api.model.productsearch;

import java.util.Arrays;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * 
 * Unit Testing related to Category class The unit test cases evaluates the way
 * the methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 29 Oct, 2013
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ Category.class })
public class TestCategory extends TestCase {

	/**
	 * Test method to test the method that set the Category Id
	 * 
	 */

	@Test
	public final void testSetCategoryId() {

		final Category category = new Category();
		category.setCategoryId("4294922263");
		assertTrue(category.getCategoryId() == "4294922263");

	}

	/**
	 * Test method to test the method that return the Category Id
	 * 
	 */

	@Test
	public final void testGetCategoryId() {

		final Category category = new Category();
		category.setCategoryId("4294922263");
		assertTrue(category.getCategoryId() == "4294922263");

	}

	/**
	 * Test method to test the method that set the Category Name
	 * 
	 */

	@Test
	public final void testSetName() {

		final Category category = new Category();
		category.setName("Home");
		assertTrue(category.getName() == "Home");

	}

	/**
	 * Test method to test the method that return the Category Name
	 * 
	 */
	@Test
	public final void testGetName() {

		final Category category = new Category();
		category.setName("Home");
		assertTrue(category.getName() == "Home");
	}
	
	/**
	 * Test method to test the method that return the Categories
	 * 
	 */

	@Test
	public final void testSetSubCategories() {

		final Category category = new Category();
		final Category subCategeory = new Category();
		subCategeory.setCategoryId("4294922263");
		subCategeory.setName("Home");
		category.setSubCategories(Arrays.asList(subCategeory));
		assertTrue(category.getSubCategories().get(0).getCategoryId() == "4294922263");

	}

}
