/**
 * This is the interface for Coremetrics. This interface would retrieve data from 
 * coremetrics_recommendataion table
 * 
 */
package com.belk.api.dao;

import java.util.List;
import java.util.Map;

import com.belk.api.exception.AdapterException;

/**
 * @author AFURXG9
 *
 */

public interface CoremetricsDAO  {

	/** This method is to get Recommendations for all Products
	 * that are in the coremetrics_recommendataion table
	 *
	 * @param correlationId
	 * @return results in a List<Map>
	 * @throws AdapterException
	 */
	void getRecommendationsForPdp(final String correlationId) throws AdapterException;
	
	/** This method gets Recommendation for a specific product. 
	 * 	
	 * NOTE : The method should not be used as per the design initially 
	 * put forward. Adding the method for any future deviance.		
	 *
	 * @param productId
	 * @param correlationId
	 * @return results in a List<Map>
	 * @throws AdapterException
	 */
	List<Map<String, Object>> getProductRecommendationForPdp(final String productId, final String correlationId)
	throws AdapterException;
		
}
