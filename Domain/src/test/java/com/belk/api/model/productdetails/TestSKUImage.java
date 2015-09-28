package com.belk.api.model.productdetails;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * 
 * Unit Testing related to SKUImage class The unit test cases evaluates the way
 * the methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 26 March, 2014
 * 
 */

public class TestSKUImage {

	final SKUImage skuImage = new SKUImage();

	/**
	 * Test method to test the method getkey.
	 */
	@Test
	public final void testGetKey(){
		skuImage.setKey("skuImage");
		assertEquals(skuImage.getKey(),"skuImage");

	}
	/**
	 * Test method to test the method getValue.
	 */
	@Test
	public final void testGetValue(){
		skuImage.setValue("Image");
		assertEquals(skuImage.getValue(),"Image");
	}

}
