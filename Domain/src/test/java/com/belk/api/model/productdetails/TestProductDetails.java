package com.belk.api.model.productdetails;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * 
 * Unit Testing related to ProductDetails class The unit test cases evaluates
 * the way the methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 29 Oct, 2013
 * 
 */


public class TestProductDetails extends TestCase {
	final ProductDetails productDetails = new ProductDetails();
	/**
	 * Method to test   getProductCode method
	 */
	@Test
	public final void testProductCode() {

		productDetails.setProductCode("12334aaa");
		assertTrue(productDetails.getProductCode() == "12334aaa");
	}

	/**
	 * Method to test   getProductId method
	 */
	@Test
	public final void testProductId() {
		final ProductDetails productDetails = new ProductDetails();
		productDetails.setProductId("aaa111");
		assertTrue(productDetails.getProductId() == "aaa111");
	}
	/**
	 * Method to test   getWebId method.
	 */
	@Test
	public final void testGetWebId(){
		productDetails.setWebId("webId");
		assertEquals(productDetails.getWebId(),"webId");
	}

	/**
	 * Method to test   getVendorId method.
	 */
	@Test
	public final void testGetVendorId(){
		productDetails.setVendorId("vendorId");
		assertEquals(productDetails.getVendorId(),"vendorId");
	}
	/**
	 * Method to test   getLongDescription method.
	 */
	@Test
	public final void testGetLongDescription(){
		productDetails.setLongDescription("LongDescription");
		assertEquals(productDetails.getLongDescription(),"LongDescription");
	}
	/**
	 * Method to test   getName method.
	 */
	@Test
	public final void testGetName(){
		productDetails.setName("LongDescription");
		assertEquals(productDetails.getName(),"LongDescription");
	}
	/**
	 * Method to test   getBrand method.
	 */
	@Test
	public final void testGetBrand(){
		productDetails.setBrand("Brand");
		assertEquals(productDetails.getBrand(),"Brand");
	}
	/**
	 * Method to test   getVendorPartNumber method.
	 */
	@Test
	public final void testGetVendorPartNumber(){
		productDetails.setVendorPartNumber("vendorPartNumber");
		assertEquals(productDetails.getVendorPartNumber(),"vendorPartNumber");
	}
	/**
	 * Method to test   getShortDescription method.
	 */
	@Test
	public final void testGetShortDescription(){
		productDetails.setShortDescription("shortDescription");
		assertEquals(productDetails.getShortDescription(),"shortDescription");
	}
	/**
	 * Method to test   getProductHierarchyAttributes method.
	 */
	@Test
	public final void testGetProductHierarchyAttributes(){
		final List<ProductHierarchyAttribute> productHierarchyAttributesList = new ArrayList<ProductHierarchyAttribute>();
		productDetails.setProductHierarchyAttributes(productHierarchyAttributesList);
		assertEquals(productDetails.getProductHierarchyAttributes(),productHierarchyAttributesList);
	}
	/**
	 * Method to test   getProductPrice method.
	 */
	@Test
	public final void testGetProductPrice(){
		final List<Price> productPrice =  new ArrayList<Price>();

		productDetails.setProductPrice(productPrice);
		assertEquals(productDetails.getProductPrice(),productPrice);
	}
	/**
	 * Method to test   getRatings method.
	 */
	@Test
	public final void testGetRatings(){
		final Ratings ratings = new Ratings();
		productDetails.setRatings(ratings);
		assertEquals(productDetails.getRatings(),ratings);

	}
	/**
	 * Method to test   getProductType method.
	 */
	@Test
	public final void testGetProductType(){
		productDetails.setProductType("productType");
		assertEquals(productDetails.getProductType(),"productType");
	}
	/**
	 * Method to test   getIsPattern method.
	 */
	@Test
	public final void testGetIsPattern(){
		productDetails.setIsPattern("isPattern");
		assertEquals(productDetails.getIsPattern(),"isPattern");
	}
	/**
	 * Method to test   GetProductFlags method.
	 */
	@Test
	public final void testGetProductFlags(){
		final List<ProductFlag> productFlags = new ArrayList<ProductFlag>();
		productDetails.setProductFlags(productFlags);
		assertEquals(productDetails.getProductFlags(),productFlags);
	}
	/**
	 * Method to test  getProductMarketingAttributes method.
	 */
	@Test
	public final void testGetProductMarketingAttributes(){
		final List<MarketingAttribute> productMarketingAttributes = new ArrayList<MarketingAttribute>();
		productDetails.setProductMarketingAttributes(productMarketingAttributes);
		assertEquals(productDetails.getProductMarketingAttributes(),productMarketingAttributes);
	}
	/**
	 * Method to test getProductAttributes method.
	 */
	@Test
	public final void testGetProductAttributes(){
		final List<Attribute> productAttributes = new ArrayList<Attribute>();
		productDetails.setProductAttributes(productAttributes);
		assertEquals(productDetails.getProductAttributes(),productAttributes);
	}
	/**
	 * Method to test  getChildProducts method.
	 */
	@Test
	public final void testGetChildProducts(){
		final ChildProductList childProducts = new  ChildProductList();
		productDetails.setChildProducts(childProducts);
		assertEquals(productDetails.getChildProducts(),childProducts);
	}
	/**
	 * Method to test setProductId and getPromotions method.
	 */
	@Test
	public final void testGetPromotions(){
		final List<Promotion> promotions = new ArrayList<Promotion>();
		productDetails.setPromotions(promotions);
		assertEquals(productDetails.getPromotions(),promotions);
	}
	/**
	 * Method to test   getExtendedAttributes method.
	 */
	@Test
	public final void testGetExtendedAttributes(){
		final List<ExtendedAttribute> extendedAttributes = new ArrayList<ExtendedAttribute>();
		productDetails.setExtendedAttributes(extendedAttributes);
		assertEquals(productDetails.getExtendedAttributes(),extendedAttributes);
	}
	/**
	 * Method to test   getSkus method.
	 */
	@Test
	public final void testGetSkus(){
		final List<SKU> skus = new ArrayList<SKU>();
		productDetails.setSkus(skus);
		assertEquals(productDetails.getSkus(),skus);
	}
}
