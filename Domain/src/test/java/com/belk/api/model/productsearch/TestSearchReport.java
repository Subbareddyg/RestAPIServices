package com.belk.api.model.productsearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import junit.framework.TestCase;

/**
 * 
 * Unit Testing related to SearchReport class The unit test cases evaluates the way the
 * methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 26 March, 2014
 * 
 */
public class TestSearchReport extends TestCase {

	/**
	 * Test method to test the method that set the total products.
	 * 
	 */
	@Test
	public final void testSetTotalProducts() {
		final SearchReport searchReport = new SearchReport();		
		searchReport.setTotalProducts("1000");		
		assertTrue(searchReport.getTotalProducts() == "1000");
	}
	
	/**
	 * Test method to test the method that set the total skus.
	 * 
	 */
	@Test
	public final void testSetTotalSkus() {
		final SearchReport searchReport = new SearchReport();		
		searchReport.setTotalSkus("50");		
		assertTrue(searchReport.getTotalSkus() == "50");
	}
	
	/**
	 * Test method to test the method that set the total Keyword.
	 * 
	 */
	@Test
	public final void testSetKeyword() {
		final SearchReport searchReport = new SearchReport();		
		searchReport.setKeyword("shirts");		
		assertTrue(searchReport.getKeyword() == "shirts");
	}
	
	/**
	 * Test method to test the method that set the refinementId.
	 * 
	 */
	@Test
	public final void testSetRefinementId() {
		final SearchReport searchReport = new SearchReport();		
		searchReport.setRefinementId("41023568910");		
		assertTrue(searchReport.getRefinementId() == "41023568910");
	}
	
	/**
	 * Test method to test the method that set the limit.
	 * 
	 */
	@Test
	public final void testSetLimit() {
		final SearchReport searchReport = new SearchReport();		
		searchReport.setLimit("100");		
		assertTrue(searchReport.getLimit() == "100");
	}
	
	/**
	 * Test method to test the method that set the Offset.
	 * 
	 */
	@Test
	public final void testSetOffset() {
		final SearchReport searchReport = new SearchReport();		
		searchReport.setOffset("5");		
		assertTrue(searchReport.getOffset() == "5");
	}

	
	/**
	 * Test method to test the method that set the attribute.
	 * 
	 */
	@Test
	public final void testSetAttribute() {
		final SearchReport searchReport = new SearchReport();		
		final List<SearchAttribute> attributes = new ArrayList<SearchAttribute>();
		final SearchAttribute searchAttribute = new SearchAttribute();		
		searchAttribute.setKey("q");
		searchAttribute.setValues(Arrays.asList("shirts", "bags"));
		searchReport.setAttributes(Arrays.asList(searchAttribute));		
		assertTrue(searchReport.getAttributes().get(0).getKey() == "q");
	}
	
	/**
	 * Test method to test the method that set the sort fields.
	 * 
	 */
	@Test
	public final void testSetSortFields() {
		final SearchReport searchReport = new SearchReport();		
		searchReport.setSortFields(Arrays.asList("name", "listprice"));
		assertTrue(searchReport.getSortFields().get(0) == "name");
	}

}
