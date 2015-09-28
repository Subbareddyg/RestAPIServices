package com.belk.api.model.productsearch;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * 
 * Unit Testing related to Attribute class The unit test cases evaluates the way
 * the methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 29 Oct, 2013
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Attribute.class)
public class TestAttribute extends TestCase {

	/**
	 * Test method to test the method that set the totalProducts as a key
	 */
	@Test
	public final void testSetKey() {

		final Attribute attribute = new Attribute();
		attribute.setKey("totalProducts");
		assertTrue(attribute.getKey() == "totalProducts");

	}

	/**
	 * Method to test the method that set the totalProduct's value
	 * 
	 */

	@Test
	public final void testSetValue() {
		final Attribute attribute = new Attribute();
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
