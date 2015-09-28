package com.belk.api.model.productdetails;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * 
 * Unit Testing related to ChildProductList class The unit test cases evaluates
 * the way the methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 29 Oct, 2013
 * 
 */

public class TestChildProductList extends TestCase {

	/**
	 * Test method to test the method that set the totalProducts as a key
	 */
	@Test
	public final void testSetChildProduct() {

		final ChildProduct product = new ChildProduct();
		product.setKey("product");
		product.setValue("112");
		final ChildProductList childProductList = new ChildProductList();
		final List<ChildProduct> childProducts = new ArrayList<ChildProduct>();
		childProducts.add(product);

		childProductList.setChildProduct(childProducts);
		final String expected = "product";
		final String actual = childProductList.getChildProduct().get(0)
				.getKey();
		assertEquals(expected, actual);

	}

	/**
	 * Test method for
	 * {@link com.belk.api.model.productdetails.ChildProductList#getCollectionType(String)}
	 */
	@Test
	public final void testCollectionType() {
		final ChildProductList childProductList = new ChildProductList();
		childProductList.setCollectionType("");
		assertTrue(childProductList.getCollectionType() == "");
	}
}
