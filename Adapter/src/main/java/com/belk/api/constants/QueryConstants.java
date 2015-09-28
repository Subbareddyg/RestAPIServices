/**
 * This class would contain the queries
 * required to be used in DAO classes 
 * 
 */
package com.belk.api.constants;

/**
 * @author AFURXG9
 *
 */
public class QueryConstants {
	
	public static final String GET_ALL_RECOMMENDATIONS = "SELECT * FROM BM_EXTERNAL.COREMETRICS_RECOMMENDATIONS"
			+ " WHERE RECOMMENDATION_TYPE = 'ProductPage' ";
	
	public static final String GET_RECOMMENDATIONS_FOR_PRODUCT = "SELECT * FROM BM_EXTERNAL.COREMETRICS_RECOMMENDATIONS"
			+ " WHERE RECOMMENDATION_TYPE = 'ProductPage' AND TARGET_PRODUCT_ID = :productId";


}
