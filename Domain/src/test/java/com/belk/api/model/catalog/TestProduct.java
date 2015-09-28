package com.belk.api.model.catalog;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * 
 * Unit Testing related to Product class The unit test cases evaluates the way
 * the methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 26 March, 2014
 * 
 */
public class TestProduct extends TestCase {

	/**
	 * Method to test setter and getter for productCode.
	 */
	@Test
	public final void testSetProductCode() {
		final Product product = new Product();
		product.setProductCode("12334aaa");
		assertTrue(product.getProductCode() == "12334aaa");
	}

	/**
	 * Method to test setter and getter for productId.
	 */
	@Test
	public final void testSetProductId() {
		final Product product = new Product();
		product.setProductId("aaa111");
		assertTrue(product.getProductId() == "aaa111");
	}
	
	/**
	 * Method to test setter and getter for web id.
	 */
	@Test
	public final void testSetWebId() {
		final Product product = new Product();
		product.setWebId("12345");
		assertTrue(product.getWebId() == "12345");
	}

}
