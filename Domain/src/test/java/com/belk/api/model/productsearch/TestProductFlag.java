package com.belk.api.model.productsearch;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * 
 * Unit Testing related to ProductFlag class The unit test cases evaluates the
 * way the methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 29 Oct, 2013
 * 
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ProductFlag.class })
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
