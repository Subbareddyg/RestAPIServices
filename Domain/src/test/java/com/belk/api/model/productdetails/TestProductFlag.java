package com.belk.api.model.productdetails;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * 
 * Unit Testing related to ProductFlag class The unit test cases evaluates the
 * way the methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 29 Oct, 2013
 * 
 */


public class TestProductFlag extends TestCase {

	/**
	 * Test method to test the method that set the productFlag as a key
	 * 
	 */

	@Test
	public final void testSetKey() {

		final ProductFlag flag = new ProductFlag();
		flag.setKey("productFlag");
		assertTrue(flag.getKey() == "productFlag");

	}

	/**
	 * Method to test the method that set the ProductFlag's value
	 * 
	 */

	@Test
	public final void testSetValue() {

		final ProductFlag flag = new ProductFlag();
		flag.setValue("38398");
		assertTrue(flag.getValue() == "38398");

	}

}
