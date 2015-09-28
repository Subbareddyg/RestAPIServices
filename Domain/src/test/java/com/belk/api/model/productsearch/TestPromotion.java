package com.belk.api.model.productsearch;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * 
 * Unit Testing related to Promotion class The unit test cases evaluates the way
 * the methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 29 Oct, 2013
 * 
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Promotion.class })
public class TestPromotion extends TestCase {

	/**
	 * Method to test setter and getter for Key
	 */
	@Test
	public final void testSetKey() {

		final Promotion promotion = new Promotion();
		promotion.setKey("promotion");
		assertTrue(promotion.getKey() == "promotion");

	}

	/**
	 * Method to test setter and getter for value
	 */
	@Test
	public final void testSetValue() {

		final Promotion promotion = new Promotion();
		promotion.setValue("38398");
		assertTrue(promotion.getValue() == "38398");

	}

}
