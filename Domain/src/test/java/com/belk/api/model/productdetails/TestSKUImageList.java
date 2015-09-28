package com.belk.api.model.productdetails;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * 
 * Unit Testing related to SKUImageList class The unit test cases evaluates the way
 * the methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 26 March, 2014
 * 
 */

public class TestSKUImageList {
	final SKUImageList skuImageList = new SKUImageList();

	/**
	 * Test method to test the method getColorSwatchAttributes.
	 */
	@Test
	public final void testGetColorSwatchAttributes(){
		final ColorSwatchAttribute colorSwatchAttribute = new ColorSwatchAttribute();
		final List<ColorSwatchAttribute>colorSwatchAttributeList = new ArrayList<ColorSwatchAttribute>();
		colorSwatchAttributeList.add(colorSwatchAttribute);
		skuImageList.setColorSwatchAttributes(colorSwatchAttributeList);
		assertEquals(skuImageList.getColorSwatchAttributes(),colorSwatchAttributeList);

	}
	/**
	 * Test method to test the method getProductSKUImages.
	 */
	@Test
	public final void testGetProductSKUImages(){
		final ProductSKUImage productSKUImage = new ProductSKUImage();
		final List<ProductSKUImage>productSKUImageList = new ArrayList<ProductSKUImage>();
		productSKUImageList.add(productSKUImage);
		skuImageList.setProductSKUImages(productSKUImageList);
		assertEquals(skuImageList.getProductSKUImages(),productSKUImageList);
	}
}
