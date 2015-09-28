package com.belk.api.model.productsearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * 
 * Unit Testing related to Dimension class The unit test cases evaluates the way the
 * methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 26 March, 2014
 * 
 */
public class TestDimension extends TestCase {

	/**
	 * Test method to test the method that set the dimension attributes.
	 * 
	 */
	@Test
	public final void testSetDimensionAttributes() {
		final Dimension dimension = new Dimension();
		
		final Attribute attribute = new Attribute();
		attribute.setKey("totalProducts");
		dimension.setDimensionAttributes(Arrays.asList(attribute));		
		assertTrue(dimension.getDimensionAttributes().get(0).getKey() == "totalProducts");
	}
	
	/**
	 * Test method to test the method that set the dimension attributes.
	 * 
	 */
	@Test
	public final void testSetRefinements() {
		final Dimension dimension = new Dimension();
		final Refinement refinement = new Refinement();
		final Attribute attribute = new Attribute();
		attribute.setKey("totalProducts");
		final List<Attribute> refinementAttributes = new ArrayList<Attribute>();
		refinementAttributes.add(attribute);
		refinement.setRefinementAttributes(refinementAttributes);
		dimension.setRefinements(Arrays.asList(refinement));
		assertTrue(dimension.getRefinements().get(0).getRefinementAttributes()
				.get(0).getKey() == "totalProducts");
	}
	


}
