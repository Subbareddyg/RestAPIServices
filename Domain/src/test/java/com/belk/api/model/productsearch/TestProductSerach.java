package com.belk.api.model.productsearch;

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
public class TestProductSerach extends TestCase {

	/**
	 * Method to test setter and getter for productCode
	 */
	@Test
	public final void testProductCode() {
		final ProductSearch productSearch = new ProductSearch();
		productSearch.setProductCode("12334aaa");
		assertTrue(productSearch.getProductCode() == "12334aaa");
	}

	/**
	 * Method to test setter and getter for productId
	 */
	@Test
	public final void testProductId() {
		final ProductSearch productSearch = new ProductSearch();
		productSearch.setProductId("aaa111");
		assertTrue(productSearch.getProductId() == "aaa111");
	}

}
