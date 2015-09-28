package com.belk.api.model.productsearch;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * 
 * Unit Testing related to Refinement class The unit test cases evaluates the way the
 * methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 26 March, 2014
 * 
 */
public class TestRefinement extends TestCase {

	/**
	 * Test method to test the method that set the refinement attributes.
	 * 
	 */
	@Test
	public final void testSetRefinementAttributes() {
		final Refinement refinement = new Refinement();		
		final Attribute attribute = new Attribute();
		attribute.setKey("totalProducts");		
		final List<Attribute> refinementAttributes = new ArrayList<Attribute>();		
		refinementAttributes.add(attribute);		
		refinement.setRefinementAttributes(refinementAttributes);				
		assertTrue(refinement.getRefinementAttributes().get(0).getKey() == "totalProducts");
	}

}
