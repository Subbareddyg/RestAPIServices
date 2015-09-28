package com.belk.api.model.catalog;

import java.util.Arrays;
import junit.framework.TestCase;
import org.junit.Test;


/**
 * 
 * Unit Testing related to Category class The unit test cases evaluates the way
 * the methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 26 March, 2014
 * 
 */
public class TestCategory extends TestCase {

	/**
	 * Test method to test the method that set the category id.
	 */
	@Test
	public final void testSetCategoryId() {
		final Category category = new Category();
		category.setCategoryId(420103960);
		assertTrue(category.getCategoryId() == 420103960);
	}
	
	/**
	 * Test method to test the method that set the category name.
	 */
	@Test
	public final void testSetName() {
		final Category category = new Category();
		category.setName("test");
		assertTrue(category.getName() == "test");
	}

	
	/**
	 * Test method to test the method that set the parent category id.
	 */
	@Test
	public final void testSetParentCategoryId() {
		final Category category = new Category();
		category.setParentCategoryId(420103961);
		assertTrue(category.getParentCategoryId() == 420103961);
	}
	
	/**
	 * Test method to test the method that set the category attributes.
	 */
	@Test
	public final void testSetCategoryAttributes() {
		final Category category = new Category();
		final Attribute attribute = new Attribute();
		attribute.setKey("totalProducts");
		attribute.setValue("38398");
		category.setCategoryAttributes(Arrays.asList(attribute));
		assertTrue(category.getCategoryAttributes().get(0).getKey() == "totalProducts");
	}
	
	/**
	 * Test method to test the method that set the sub categories.
	 */
	@Test
	public final void testSetSubCategories() {
		final Category category = new Category();
		final Category subCategory = new Category();
		subCategory.setCategoryId(420136902);
		subCategory.setName("subcategory");
		category.setSubCategories(Arrays.asList(subCategory));
		assertTrue(category.getSubCategories().get(0).getName() == "subcategory");
	}
	
	/**
	 * Test method to test the method that set the products.
	 */
	@Test
	public final void testSetProducts() {
		final Category category = new Category();
		final Product product = new Product();
		product.setProductCode("12334aaa");
		product.setProductId("aaa111");
		category.setProducts(Arrays.asList(product));
		assertTrue(category.getProducts().get(0).getProductId() == "aaa111");
	}

}
