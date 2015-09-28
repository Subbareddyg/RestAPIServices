package com.belk.api.model.productsearch;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * 
 * Unit Testing related to ParentCategory class The unit test cases evaluates the way the
 * methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 29 Oct, 2013
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ ParentCategory.class })
public class TestParentCategory extends TestCase {

	/**
	 * Test method to test the method that set the categories.
	 * 
	 */
	@Test
	public final void testSetCategoryAttributes() {
		final ParentCategory parentCategory = new ParentCategory();

		final Attribute attribute = new Attribute();
		attribute.setKey("totalProducts");

		final List<Attribute> categoryAttributes = new ArrayList<Attribute>();

		categoryAttributes.add(attribute);

		parentCategory.setCategoryAttributes(categoryAttributes);

		assertTrue(parentCategory.getCategoryAttributes().get(0).getKey() == "totalProducts");

	}

	/**
	 * Test method to test the method that set the sub categories.
	 * 
	 */
	@Test
	public final void testSetSubCategories() {
		final ParentCategory parentCategory = new ParentCategory();

		final Attribute attribute = new Attribute();
		attribute.setKey("totalProducts");

		final List<Attribute> categoryAttributes = new ArrayList<Attribute>();
		categoryAttributes.add(attribute);

		final SubCategory subCategory = new SubCategory();
		subCategory.setCategoryAttributes(categoryAttributes);

		final List<SubCategory> subCategories = new ArrayList<SubCategory>();
		subCategories.add(subCategory);
		parentCategory.setSubCategories(subCategories);

		assertTrue(parentCategory.getSubCategories().get(0)
				.getCategoryAttributes().get(0).getKey() == "totalProducts");

	}

}
