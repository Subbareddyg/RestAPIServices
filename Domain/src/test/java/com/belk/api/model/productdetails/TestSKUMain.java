package com.belk.api.model.productdetails;

import static org.junit.Assert.assertEquals;

import org.junit.Test;



/**
 * 
 * Unit Testing related to SKUMain class The unit test cases evaluates the way
 * the methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 26 March, 2014
 * 
 */

public class TestSKUMain {

	SKUMain skuMain = new SKUMain();
	/**
	 * Test method to test the method getKey.
	 */
	@Test
	public final void testGetKey(){
		skuMain.setKey("sku");
		assertEquals(skuMain.getKey(),"sku");
	}

	/**
	 * Test method to test the method getValue.
	 */
	@Test
	public final void testGetValue(){
		skuMain.setValue("shirt");
		assertEquals(skuMain.getValue(),"shirt");
	}

}
