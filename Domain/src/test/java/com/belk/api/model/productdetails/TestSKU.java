package com.belk.api.model.productdetails;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * 
 * Unit Testing related to SKU class The unit test cases evaluates the way
 * the methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 26 March, 2014
 * 
 */

public class TestSKU {

	final SKU sku = new SKU();

	/**
	 * Test method to test the method getSkuCode.
	 */
	@Test
	public final void testGetSkuCode(){
		sku.setSkuCode("sku");
		assertEquals(sku.getSkuCode(),"sku");
	}
	/**
	 * Test method to test the method getUpcCode.
	 */
	@Test
	public final void testGetUpcCode(){
		sku.setUpcCode("upc");
		assertEquals(sku.getUpcCode(),"upc");
	}
	
	/**
	 * Test method to test the method getSkuId.
	 */
	@Test
	public final void testGetSkuId(){
		sku.setSkuId("skuid");
		assertEquals(sku.getSkuId(),"skuid");
	}
	
	/**
	 * Test method to test the method getOrinSku.
	 */
	@Test
	public final void testGetOrinSku(){
		sku.setOrinSku("orinSku");
		assertEquals(sku.getOrinSku(),"orinSku");
	}
	/**
	 * Test method to test the method getSkuMainAttributes.
	 */
	@Test
	public final void testGetSkuMainAttributes(){
		final SKUMain skuMain = new SKUMain();
		final List<SKUMain> skuMainAttributes = new ArrayList<SKUMain>();
		skuMainAttributes.add(skuMain);
		sku.setSkuMainAttributes(skuMainAttributes);
		assertEquals(sku.getSkuMainAttributes(),skuMainAttributes);

	}
	/**
	 * Test method to test the method getSkuPrice.
	 */
	@Test
	public final void testGetSkuPrice(){
		final Price price = new Price();
		final List<Price> skuPrice = new ArrayList<Price>();
		skuPrice.add(price);
		sku.setSkuPrice(skuPrice);
		assertEquals(sku.getSkuPrice(),skuPrice);
	}
	/**
	 * Test method to test the method getProductSKUImages.
	 */
	@Test
	public final void testGetProductSKUImages(){
		final ProductSKUImage productSKUImage = new ProductSKUImage();
		final List<ProductSKUImage> productSKUImages = new ArrayList<ProductSKUImage>();
		productSKUImages.add(productSKUImage);
		sku.setProductSKUImages(productSKUImages);
		assertEquals(sku.getProductSKUImages(),productSKUImages);
	}
	/**
	 * Test method to test the method getSkuInventory.
	 */
	@Test
	public final void testGetSkuInventory(){
		final SKUInventory skuInventory = new SKUInventory();
		sku.setSkuInventory(skuInventory);
		assertEquals(sku.getSkuInventory(),skuInventory);
	}
	/**
	 * Test method to test the method getSkuImages.
	 */
	@Test
	public final void testGetSkuImages(){
		final SKUImageList skuImages = new SKUImageList();
		sku.setSkuImages(skuImages);
		assertEquals(sku.getSkuImages(),skuImages);
	}
	/**
	 * Test method to test the method getExtendedAttribute.
	 */
	@Test
	public final void testGetExtendedAttribute(){
		final ExtendedAttribute extendedAttribute = new ExtendedAttribute();
		final List<ExtendedAttribute> extendedAttributeList = new ArrayList<ExtendedAttribute>();
		extendedAttributeList.add(extendedAttribute);
		sku.setExtendedAttribute(extendedAttributeList);
		assertEquals(sku.getExtendedAttribute(),extendedAttributeList);
	}
}
