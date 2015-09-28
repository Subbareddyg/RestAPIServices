package com.belk.api.model.productdetails;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * 
 * Unit Testing related to ProductList class The unit test cases evaluates the way
 * the methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 26 March, 2014
 * 
 */

public class TestProductList {

	final ProductList productList = new ProductList();
	/**
	 * Test method to test the method getProducts.
	 */
	@Test
	public final void testGetProducts(){
		final List<ProductDetails> products = new ArrayList<ProductDetails>();
		productList.setProducts(products);
		assertEquals(productList.getProducts(),products);
	}


}
