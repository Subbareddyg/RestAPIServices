package com.belk.api.model.productdetails;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * 
 * Unit Testing related to Price class The unit test cases evaluates the way the
 * methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 29 Oct, 2013
 * 
 */


public class TestPrice extends TestCase {

	/**
	 * Test method to test the method that set the price as a key
	 * 
	 */

	@Test
	public final void testSetKey() {

		final Price price = new Price();
		price.setKey("price");
		assertTrue(price.getKey() == "price");

	}

	/**
	 * Method to test the method that set the value
	 * 
	 */

	@Test
	public final void testSetValue() {

		final Price price = new Price();
		price.setValue("38398");
		assertTrue(price.getValue() == "38398");

	}

}
