package com.belk.api.model.productsearch;

import java.util.Arrays;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * 
 * Unit Testing related to ProductSearch class The unit test cases evaluates the
 * way the methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 29 Oct, 2013
 * 
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ProductSearch.class })
public class TestProductSearch extends TestCase {

	/**
	 * Method to test setter and getter for productCode.
	 */
	@Test
	public final void testSetProductCode() {
		final ProductSearch productSearch = new ProductSearch();
		productSearch.setProductCode("12334aaa");
		assertTrue(productSearch.getProductCode() == "12334aaa");
	}

	/**
	 * Method to test setter and getter for productId.
	 */
	@Test
	public final void testSetProductId() {
		final ProductSearch productSearch = new ProductSearch();
		productSearch.setProductId("aaa111");
		assertTrue(productSearch.getProductId() == "aaa111");
	}
	
	/**
	 * Method to test setter and getter for vendorPartNumber.
	 */
	@Test
	public final void testSetVendorPartNumber() {
		final ProductSearch productSearch = new ProductSearch();
		productSearch.setVendorPartNumber("3S0SA");
		assertTrue(productSearch.getVendorPartNumber() == "3S0SA");
	}
	
	/**
	 * Method to test setter and getter for web id.
	 */
	@Test
	public final void testSetWebId() {
		final ProductSearch productSearch = new ProductSearch();
		productSearch.setWebId("12345");
		assertTrue(productSearch.getWebId() == "12345");
	}
	
	/**
	 * Method to test setter and getter for vendor id.
	 */
	@Test
	public final void testSetVendorId() {
		final ProductSearch productSearch = new ProductSearch();
		productSearch.setVendorId("12345");
		assertTrue(productSearch.getVendorId() == "12345");
	}
	
	/**
	 * Method to test setter and getter for brand.
	 */
	@Test
	public final void testSetBrand() {
		final ProductSearch productSearch = new ProductSearch();
		productSearch.setBrand("testbrand");
		assertTrue(productSearch.getBrand() == "testbrand");
	}
	
	/**
	 * Method to test setter and getter for name.
	 */
	@Test
	public final void testSetName() {
		final ProductSearch productSearch = new ProductSearch();
		productSearch.setName("testName");
		assertTrue(productSearch.getName() == "testName");
	}
	
	/**
	 * Method to test setter and getter for product price.
	 */
	@Test
	public final void testSetProductPrice() {
		final ProductSearch productSearch = new ProductSearch();
		final Price price = new Price();
		price.setKey("price");
		price.setValue("38398");
		productSearch.setProductPrice(Arrays.asList(price));
		assertTrue(productSearch.getProductPrice().get(0).getValue() == "38398");
	}

	
	/**
	 * Method to test setter and getter for product ratings.
	 */
	@Test
	public final void testSetRatings() {
		final ProductSearch productSearch = new ProductSearch();
		final Ratings ratings = new Ratings();
		ratings.setOverallRating("Good");
		ratings.setReviewCount("10");
		productSearch.setRatings(ratings);
		assertTrue(productSearch.getRatings().getReviewCount() == "10");
	}
	
	/**
	 * Method to test setter and getter for product type.
	 */
	@Test
	public final void testSetProductType() {
		final ProductSearch productSearch = new ProductSearch();
		productSearch.setProductType("producttype");
		assertTrue(productSearch.getProductType() == "producttype");
	}
	
	/**
	 * Method to test setter and getter for product flags.
	 */
	@Test
	public final void testSetProductFlags() {
		final ProductSearch productSearch = new ProductSearch();
		final ProductFlag productFlag = new ProductFlag();
		productFlag.setKey("productFlag");
		productFlag.setValue("38398");
		productSearch.setProductFlags(Arrays.asList(productFlag));
		assertTrue(productSearch.getProductFlags().get(0).getValue() == "38398");
	}
	
	/**
	 * Method to test setter and getter for promotions.
	 */
	@Test
	public final void testSetPromotions() {
		final ProductSearch productSearch = new ProductSearch();
		final Promotion promotion = new Promotion();
		promotion.setKey("promotion");
		promotion.setValue("38398");
		productSearch.setPromotions(Arrays.asList(promotion));
		assertTrue(productSearch.getPromotions().get(0).getValue() == "38398");
	}

	
	/**
	 * Method to test setter and getter for product attributes.
	 */
	@Test
	public final void testSetProductAttributes() {
		final ProductSearch productSearch = new ProductSearch();
		final Attribute attribute = new Attribute();
		attribute.setKey("totalProducts");
		attribute.setValue("38398");
		productSearch.setProductAttributes(Arrays.asList(attribute));
		assertTrue(productSearch.getProductAttributes().get(0).getValue() == "38398");
	}
	
	/**
	 * Method to test setter and getter for product attributes.
	 */
	@Test
	public final void testSetExtendedAttributes() {
		final ProductSearch productSearch = new ProductSearch();
		final Attribute attribute = new Attribute();
		attribute.setKey("totalProducts");
		attribute.setValue("38398");
		productSearch.setExtendedAttributes(Arrays.asList(attribute));
		assertTrue(productSearch.getExtendedAttributes().get(0).getValue() == "38398");
	}


}
