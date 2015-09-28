package com.belk.api.model.catalog;

import java.util.Arrays;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * 
 * Unit Testing related to Catalog class The unit test cases evaluates the way
 * the methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 26 March, 2014
 * 
 */
public class TestCatalog extends TestCase {

	/**
	 * Test method to test the method that set the catalog id.
	 */
	@Test
	public final void testSetCatalogId() {
		final Catalog catalog = new Catalog();
		catalog.setCatalogId("42010369780");		
		assertTrue(catalog.getCatalogId() == "42010369780");

	}
	
	/**
	 * Test method to test the method that set the catalog name.
	 */
	@Test
	public final void testSetCatalogName() {
		final Catalog catalog = new Catalog();
		catalog.setCatalogName("testCatalog");		
		assertTrue(catalog.getCatalogName() == "testCatalog");

	}
	

	/**
	 * Test method to test the method that set the categories.
	 */
	@Test
	public final void testSetCategories() {
		final Catalog catalog = new Catalog();
		final Category category = new Category();
		category.setCategoryId(420103960);
		category.setName("testCategory");		
		catalog.setCategories(Arrays.asList(category));		
		assertTrue(catalog.getCategories().get(0).getName() == "testCategory");

	}

}
