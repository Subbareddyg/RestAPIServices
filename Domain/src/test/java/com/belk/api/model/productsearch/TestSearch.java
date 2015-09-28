package com.belk.api.model.productsearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * 
 * Unit Testing related to Search class The unit test cases evaluates the way the
 * methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 26 March, 2014
 * 
 */
public class TestSearch extends TestCase {

	/**
	 * Test method to test the method that set the products.
	 * 
	 */
	@Test
	public final void testSetProducts() {
		final Search search = new Search();
		
		final List<ProductSearch> products = new ArrayList<ProductSearch>();
		final ProductSearch productSearch = new ProductSearch();
		productSearch.setProductCode("12345");
		productSearch.setName("shirts");		
		products.add(productSearch);
		
		search.setProducts(products);
		
		assertTrue(search.getProducts().get(0).getName() == "shirts");

	}
	
	/**
	 * Test method to test the method that set the categories.
	 * 
	 */
	@Test
	public final void testSetCategories() {
		final Search search = new Search();
		
		final List<ParentCategory> categories = new ArrayList<ParentCategory>();
		final ParentCategory parentCategory = new ParentCategory();
		final Attribute attribute = new Attribute();
		attribute.setKey("totalProducts");
		
		final List<Attribute> categoryAttributes = new ArrayList<Attribute>();
		
		categoryAttributes.add(attribute);
		
		parentCategory.setCategoryAttributes(categoryAttributes);
		categories.add(parentCategory);
		
		search.setCategories(categories);
		
		assertTrue(search.getCategories().get(0).getCategoryAttributes().get(0).getKey() == "totalProducts");

	}

	/**
	 * Test method to test the method that set the dimensions.
	 * 
	 */
	@Test
	public final void testSetDimensions() {
		final Search search = new Search();
		
		final List<Dimension> dimensions = new ArrayList<Dimension>();
		final Dimension dimension = new Dimension();
		
		final Attribute attribute = new Attribute();
		attribute.setKey("totalProducts");
		
		final List<Attribute> dimensionAttributes = new ArrayList<Attribute>();		
		dimensionAttributes.add(attribute);
		
		dimension.setDimensionAttributes(dimensionAttributes);
		
		search.setDimensions(Arrays.asList(dimension));
		
		assertTrue(search.getDimensions().get(0).getDimensionAttributes().get(0).getKey() == "totalProducts");

	}
	
	
	/**
	 * Test method to test the method that set the SearchReport.
	 * 
	 */
	@Test
	public final void testSetSearchReport() {
		final Search search = new Search();
		
		final SearchReport searchReport = new SearchReport();
		searchReport.setTotalProducts("1000");
		searchReport.setKeyword("shirts");
				
		search.setSearchReport(searchReport);
		
		assertTrue(search.getSearchReport().getKeyword() == "shirts");

	}
	
}
