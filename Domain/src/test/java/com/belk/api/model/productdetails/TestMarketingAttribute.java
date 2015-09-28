package com.belk.api.model.productdetails;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
/**
 * 
 * Unit Testing related to MarketingAttribute class The unit test cases evaluates
 * the way the methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 29 Oct, 2013
 * 
 */
public class TestMarketingAttribute {

	final MarketingAttribute  marketingAttribute  = new MarketingAttribute ();
	/**
	 * Test method to test the method getKey.
	 */
	@Test
	public final void testGetKey(){
		marketingAttribute.setKey("marketingAttribute");
		assertEquals(marketingAttribute.getKey(),"marketingAttribute");
	}

	/**
	 * Test method to test the method getValue.
	 */
	@Test
	public final void testGetValue(){
		marketingAttribute.setValue("marketingAttribute");
		assertEquals(marketingAttribute.getValue(),"marketingAttribute");
	}

}
