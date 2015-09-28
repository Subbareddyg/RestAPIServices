package com.belk.api.model.categoryproduct;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * 
 * Unit Testing related to ProductSearch class The unit test cases evaluates the
 * way the methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 29 Oct, 2013
 * 
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ProductSearch.class })
public class TestProductSearch extends TestCase {

	/**
	 * Test method to test the method that set the product code
	 * 
	 */
	@Test
	public final void testSetProductCode() {

		final ProductSearch productSearch = new ProductSearch();
		productSearch.setProductCode("3203375NA10011");
		assertTrue(productSearch.getProductCode() == "3203375NA10011");

	}

	/**
	 * Test method to test the method that return the product code
	 * 
	 */
	@Test
	public final void testGetProductCode() {

		final ProductSearch productSearch = new ProductSearch();
		productSearch.setProductCode("3203375NA10011");
		assertTrue(productSearch.getProductCode() == "3203375NA10011");

	}

	/**
	 * Test method to test the method that set the product Id
	 * 
	 */
	@Test
	public final void testSetProductId() {

		final ProductSearch productSearch = new ProductSearch();
		productSearch.setProductId(" ");
		assertTrue(productSearch.getProductId() == " ");

	}

	/**
	 * Test method to test the method that return the product Id
	 * 
	 */

	@Test
	public final void testGetProductId() {

		final ProductSearch productSearch = new ProductSearch();
		productSearch.setProductId(" ");
		assertTrue(productSearch.getProductId() == " ");

	}

	/**
	 * Test method to test the method that set the web Id
	 * 
	 */

	@Test
	public final void testSetWebId() {

		final ProductSearch productSearch = new ProductSearch();
		productSearch.setWebId(" ");
		assertTrue(productSearch.getWebId() == " ");

	}

	/**
	 * Test method to test the method that return the web Id
	 * 
	 */

	@Test
	public final void testGetWebId() {

		final ProductSearch productSearch = new ProductSearch();
		productSearch.setWebId(" ");
		assertTrue(productSearch.getWebId() == " ");

	}

}
