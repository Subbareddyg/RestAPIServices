package com.belk.api.model.productdetails;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
/**
 * 
 * Unit Testing related to ProductSKUImage class The unit test cases evaluates the way
 * the methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 26 March, 2014
 * 
 */
public class TestProductSKUImage {
	final ProductSKUImage productSKUImage = new ProductSKUImage();
	/**
	 * Test method to test the method getSkuImageAttribute.
	 */
	@Test
	public final void testGetSkuImageAttribute(){
		final List<SKUImage> skuImageAttribute = new ArrayList<SKUImage>();
		productSKUImage.setSkuImageAttribute(skuImageAttribute);
		assertEquals(productSKUImage.getSkuImageAttribute(),skuImageAttribute);
	}

	/**
	 * Test method to test the method getDefaultImage.
	 */
	@Test
	public final void testGetDefaultImage(){
		productSKUImage.setDefaultImage("skuImage");
		assertEquals(productSKUImage.getDefaultImage(),"skuImage");
	}

}
