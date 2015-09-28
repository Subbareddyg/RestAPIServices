package com.belk.api.model.productdetails;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * 
 * Unit Testing related to ColorSwatchAttribute class The unit test cases
 * evaluates the way the methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 29 Oct, 2013
 * 
 */

public class TestColorSwatchAttribute extends TestCase {

	/**
	 * Test method to test the method that set the key
	 */
	@Test
	public final void testSetKey() {

		final ColorSwatchAttribute colorSwatchAttribute = new ColorSwatchAttribute();
		colorSwatchAttribute.setKey("");
		assertTrue(colorSwatchAttribute.getKey() == "");

	}

	/**
	 * Method to test the method that set the value
	 * 
	 */

	@Test
	public final void testSetValue() {
		final ColorSwatchAttribute colorSwatchAttribute = new ColorSwatchAttribute();
		colorSwatchAttribute.setValue("38398");
		assertTrue(colorSwatchAttribute.getValue() == "38398");

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
