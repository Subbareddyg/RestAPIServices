package com.belk.api.model.categoryproduct;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * 
 * Unit Testing related to Categories class The unit test cases evaluates the
 * way the methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 29 Oct, 2013
 * 
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Categories.class })
public class TestCategories extends TestCase {

	/**
	 * Test method to test the method that set the Category
	 * 
	 */
	@Test
	public final void testSetCategory() {

		final Categories categories = new Categories();
		final List<Category> categoryList = new ArrayList<Category>();
		categories.setCategory(categoryList);
		assertTrue(categories.getCategory() == categoryList);

	}

	/**
	 * Test method to test the method that return the Category
	 * 
	 */

	@Test
	public final void testGetCategory() {

		final Categories categories = new Categories();
		final List<Category> categoryList = new ArrayList<Category>();
		categories.setCategory(categoryList);
		assertTrue(categories.getCategory() == categoryList);

	}

}
