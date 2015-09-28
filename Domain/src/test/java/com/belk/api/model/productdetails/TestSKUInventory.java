package com.belk.api.model.productdetails;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * 
 * Unit Testing related to SKUInventory class The unit test cases evaluates the way
 * the methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 26 March, 2014
 * 
 */

public class TestSKUInventory {
	final SKUInventory skuInventory = new SKUInventory();
	/**
	 * Test method to test the method getInventoryLevel.
	 */
	@Test
	public final void testGetInventoryLevel(){
		skuInventory.setInventoryLevel("skuInventory");
		assertEquals(skuInventory.getInventoryLevel(),"skuInventory");
	}

	/**
	 * Test method to test the method getInventoryAvailable.
	 */
	@Test
	public final void testGetInventoryAvailable(){
		skuInventory.setInventoryAvailable("true");
		assertEquals(skuInventory.getInventoryAvailable(),"true");
	}


}
