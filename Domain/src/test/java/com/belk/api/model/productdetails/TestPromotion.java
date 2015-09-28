package com.belk.api.model.productdetails;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * 
 * Unit Testing related to Promotion class The unit test cases evaluates the way
 * the methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 29 Oct, 2013
 * 
 */


public class TestPromotion extends TestCase {

	/**
	 * Method to test setKey() and getKey()
	 */
	@Test
	public final void testSetKey() {

		final Promotion promotion = new Promotion();
		promotion.setKey("promotion");
		assertTrue(promotion.getKey() == "promotion");

	}

	/**
	 * Method to test setValue() and getValue()
	 */
	@Test
	public final void testSetValue() {

		final Promotion promotion = new Promotion();
		promotion.setValue("38398");
		assertTrue(promotion.getValue() == "38398");
	}

}
