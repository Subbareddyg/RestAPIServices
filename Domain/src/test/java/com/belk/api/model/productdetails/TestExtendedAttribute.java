package com.belk.api.model.productdetails;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * 
 * Unit Testing related to ExtendedAttribute class The unit test cases evaluates
 * the way the methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 29 Oct, 2013
 * 
 */
public class TestExtendedAttribute extends TestCase {

	/**
	 * Test method to test the method that set the key
	 */
	@Test
	public final void testSetKey() {

		final ExtendedAttribute attribute = new ExtendedAttribute();
		attribute.setKey("");
		assertTrue(attribute.getKey() == "");

	}

	/**
	 * Method to test the method that set the value
	 * 
	 */

	@Test
	public final void testSetValue() {
		final ExtendedAttribute attribute = new ExtendedAttribute();
		attribute.setValue("38398");
		assertTrue(attribute.getValue() == "38398");

	}

	/**
	 * Method to test the method that returns totalProducts as a key
	 * 
	 */

	@Test
	public final void testGetKey() {

		final Attribute attribute = new Attribute();
		attribute.setKey("totalProducts");
		assertTrue(attribute.getKey() == "totalProducts");

	}

	/**
	 * Method to test the method that returns totalProduct's value
	 * 
	 */

	@Test
	public final void testGetValue() {

		final Attribute attribute = new Attribute();
		attribute.setValue("38398");
		assertTrue(attribute.getValue() == "38398");

	}

}
