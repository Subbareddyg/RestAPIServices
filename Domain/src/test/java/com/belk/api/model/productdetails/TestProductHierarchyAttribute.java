package com.belk.api.model.productdetails;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * 
 * Unit Testing related to ProductHierarchyAttribute class The unit test cases
 * evaluates the way the methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 29 Oct, 2013
 * 
 */

public class TestProductHierarchyAttribute extends TestCase {

	/**
	 * Test method to test the method that set the totalProducts as a key
	 */
	@Test
	public final void testSetKey() {

		final ProductHierarchyAttribute attribute = new ProductHierarchyAttribute();
		attribute.setKey("totalProducts");
		assertTrue(attribute.getKey() == "totalProducts");

	}

	/**
	 * Method to test the method that set the totalProduct's value
	 * 
	 */

	@Test
	public final void testSetValue() {
		final ProductHierarchyAttribute attribute = new ProductHierarchyAttribute();
		attribute.setValue("38398");
		assertTrue(attribute.getValue() == "38398");

	}

}
