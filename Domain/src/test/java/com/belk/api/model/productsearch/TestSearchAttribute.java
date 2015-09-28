package com.belk.api.model.productsearch;

import java.util.Arrays;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * 
 * Unit Testing related to SearchAttribute class The unit test cases evaluates the way the
 * methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 26 March, 2014
 * 
 */
public class TestSearchAttribute extends TestCase {

	/**
	 * Test method to test the method that set the key.
	 * 
	 */
	@Test
	public final void testSetKey() {
		final SearchAttribute searchAttribute = new SearchAttribute();
		searchAttribute.setKey("q");		
		assertTrue(searchAttribute.getKey() == "q");
	}
	
	/**
	 * Test method to test the method that set the key.
	 * 
	 */
	@Test
	public final void testSetValues() {
		final SearchAttribute searchAttribute = new SearchAttribute();
		searchAttribute.setValues(Arrays.asList("shirts", "bags"));		
		assertTrue(searchAttribute.getValues().get(0) == "shirts");
	}

}
