package com.belk.api.model.productsearch;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * 
 * Unit Testing related to Price class The unit test cases evaluates the way the
 * methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 29 Oct, 2013
 * 
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Price.class })
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
