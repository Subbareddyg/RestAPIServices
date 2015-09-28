package com.belk.api.model.productdetails;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * 
 * Unit Testing related to ChildProduct class The unit test cases evaluates the
 * way the methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 29 Oct, 2013
 * 
 */

public class TestChildProduct extends TestCase {

	/**
	 * Test method to test the method that set the totalProducts as a key
	 */
	@Test
	public final void testSetKey() {

		final ChildProduct product = new ChildProduct();
		product.setKey("product");
		assertTrue(product.getKey() == "product");

	}

	/**
	 * Method to test the method that set the totalProduct's value
	 * 
	 */

	@Test
	public final void testSetValue() {
		final ChildProduct product = new ChildProduct();
		product.setValue("38398");
		assertTrue(product.getValue() == "38398");

	}

}
