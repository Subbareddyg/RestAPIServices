package com.belk.api.model.productdetails;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * 
 * Unit Testing related to ProductHierarchyAttributeList class The unit test cases evaluates the way
 * the methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 26 March, 2014
 * 
 */
public class TestProductHierarchyAttributeList {
	final ProductHierarchyAttributeList productHierarchyAttributeList = new ProductHierarchyAttributeList();

	/**
	 * Test method to test the method getProductHierarchyAttributes.
	 */
	@Test
	public final void testGetProductHierarchyAttributes(){
		final List<ProductHierarchyAttribute> productHierarchyAttributes = new ArrayList<ProductHierarchyAttribute>();
		productHierarchyAttributeList.setProductHierarchyAttributes(productHierarchyAttributes);
		assertEquals(productHierarchyAttributeList.getProductHierarchyAttributes(),productHierarchyAttributes);
	}

}
