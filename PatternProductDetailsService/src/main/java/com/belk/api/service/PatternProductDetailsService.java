package com.belk.api.service;

import java.util.List;
import java.util.Map;

import com.belk.api.business.service.contract.Services;
import com.belk.api.exception.AdapterException;
import com.belk.api.exception.BaseException;
import com.belk.api.exception.ServiceException;
import com.belk.api.model.productdetails.ProductList;

/**
 * This is a contract for service implementation class.
 * 
 * Update: This API has been introduced as part of change request:
 * CR_BELK_API_3.
 * 
 * @author Mindtree
 * @date 5th Nov, 2013
 */
public interface PatternProductDetailsService extends Services {

	/**
	 * This method gets pattern product details matching with all identifiers
	 * passed in the request to blue martini.
	 * 
	 * @param uriMap
	 *            - map of query parameters
	 * @param correlationId to track the request
	 * @return ProductList
	 * @throws BaseException
	 *             The general exception from Domain layer
	 * @throws AdapterException
	 *             The general exception from Adapter layer
	 * 
	 */
	ProductList getPatternProductDetails(Map<String, List<String>> uriMap,
			String correlationId) throws BaseException, AdapterException;

	/**
	 * This method is to invoke appropriate loader to reload map of property file specified in request.
	 * 
	 * @param correlationId
	 *            - correlationId from the request
	 * @param updateRequestUriMap
	 * 			  - The request map
	 * @throws ServiceException
	 *             common exception for service layer
	 */
	void updatePropertiesMap(String correlationId, Map<String, List<String>> updateRequestUriMap)
			throws ServiceException;
}
