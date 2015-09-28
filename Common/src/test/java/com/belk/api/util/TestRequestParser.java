package com.belk.api.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;

/**
 * Unit Testing related to RequestParser class is performed. <br />
 * {@link TestRequestParser} class is written for testing methods in {@link RequestParser}
 *  The unit test cases evaluates the way the methods behave for the inputs given.
 * @author Mindtree	
 *
 */
public class TestRequestParser {
    
	Map<String, String> mapDetails = null;
	Map<String, List<String>> uriMap = null;
	Map<String, String> expectedResults;
	Map<String, String> actualResults;
	String correlationId = "1234567891234567";
	/**
	 * Pre Requirements for Unit Testing this RequestParser class.
	 * @throws Exception - exception
	 */
	@Before
	public final void setUp() throws Exception {
		this.mapDetails = new LinkedHashMap<String, String>();
		this.uriMap = new LinkedHashMap<String, List<String>>();
		final GenericLogger logger = LoggingHelper.getLogger("Junit");
		{
			LoggerUtil.setLogger(logger);
		}
	}
	
	/**
	 * Test method for {@link com.belk.api.util.RequestParser#processRequestAttribute(java.util.Map)}.
	 *  Testing is done to check whether the required  Map. 
	 * when processRequestAttribute() method is called .
	 */
	@Test
	public final void testProcessRequestAttribute() {
		final RequestParser requestParser = new RequestParser();
	    this.mapDetails = this.processedAttributeMap();
		this.uriMap = this.getRequestURIMap();
		this.expectedResults = this.mapDetails;
		this.actualResults = requestParser.processRequestAttribute(this.uriMap, this.correlationId);
		assertEquals(this.expectedResults, this.actualResults);
       
	}
	/**
	 * Test method for {@link com.belk.api.util.RequestParser#processRequestAttribute(java.util.Map)}.
	 *  Testing is done to check whether the required  Map. 
	 * when processRequestAttribute() method is called .
	 */
	@Test
	public final void testprepareRequest() {	
		final RequestParser requestParser = new RequestParser();
	    this.mapDetails = this.processedAttributeMap();
		this.uriMap = this.getRequestURIMapWithouttoken();
		this.expectedResults = this.mapDetails;
		this.actualResults = requestParser.prepareRequest(this.uriMap, this.correlationId);
		assertEquals(this.expectedResults, this.actualResults);
       
	}

  /**
   *This method forms the Request Map has required. 
   * @return Map<String, List<String>> is returned to simulate the URIMAP
   */
	
	protected final Map<String, List<String>> getRequestURIMap() {
		final Map<String, List<String>> staticResourceMap = new HashMap<String, List<String>>();
		final List<String> valuesforMapOne = new ArrayList<String>();
		final List<String> valuesforMapTwo = new ArrayList<String>();
		final List<String> valuesforMapThree = new ArrayList<String>();
		 final List<String> valuesforMapFour = new ArrayList<String>();

		final String attr = "color:blue,red|brand:HUE";
		 final String q = "poloshirts";
		final String offsets = "10";
		final String sortKeys = "name|-inventory";
		
		valuesforMapOne.add(attr);
		valuesforMapTwo.add(q);
		valuesforMapThree.add(offsets);
		valuesforMapFour.add(sortKeys);

		staticResourceMap.put("attr", valuesforMapOne);
		staticResourceMap.put("q", valuesforMapTwo);
		staticResourceMap.put("offset", valuesforMapThree);
		staticResourceMap.put("sortKeys", valuesforMapFour);
	
		return staticResourceMap;
		
	}
	 /**
	   *This method forms the Request Map has required. 
	   * @return Map<String, List<String>> is returned to simulate the URIMAP
	   */
		
		protected final Map<String, List<String>> getRequestURIMapWithouttoken() {
			final Map<String, List<String>> staticResourceMap = new HashMap<String, List<String>>();
			final List<String> valuesforMapOne = new ArrayList<String>();
			final List<String> valuesforMapTwo = new ArrayList<String>();
			final List<String> valuesforMapThree = new ArrayList<String>();
			final List<String> valuesforMapFour = new ArrayList<String>();
			final List<String> valuesforMapFive = new ArrayList<String>();
            valuesforMapOne.add("blue,red");
			valuesforMapTwo.add("HUE");
			valuesforMapThree.add("poloshirts");
			valuesforMapFour.add("10");
			valuesforMapFive.add("name|-inventory");
            staticResourceMap.put("color", valuesforMapOne);
			staticResourceMap.put("brand", valuesforMapTwo);
			staticResourceMap.put("q", valuesforMapThree);
			staticResourceMap.put("offset", valuesforMapFour);
			staticResourceMap.put("sortKeys", valuesforMapFive);
		
			return staticResourceMap;
			
		}
	/**
	 *This method forms the Map has required. 
	 * @return Map<String, String> which simulates the requestAttributeMap
	 */
	protected final Map<String, String> processedAttributeMap() {
		final Map<String, String> attributeMap = new LinkedHashMap<String, String>();
		
		attributeMap.put("color", "blue,red");
		attributeMap.put("brand", "HUE");
		attributeMap.put("q", "poloshirts");
		attributeMap.put("offset", "10");
		attributeMap.put("sortKeys", "name|-inventory");
		return attributeMap;
	}
}
