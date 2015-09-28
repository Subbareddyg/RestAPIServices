package com.belk.api.service;

import java.util.List;
import java.util.Map;

import com.belk.api.business.service.contract.Services;
import com.belk.api.exception.AdapterException;
import com.belk.api.exception.BaseException;
import com.belk.api.exception.ServiceException;
import com.belk.api.model.productsearch.Search;

/**
 * This is a contractor for service implementation class.
 * 
 * @author Mindtree
 * @date Jul 25, 2013
 */
public interface ProductSearchService extends Services {

	/**
	 * The method searches the products based search criteria for the given
	 * input.
	 * 
	 * @param searchCriteria
	 *            The request map containing the search criteria
	 * @param correlationId
	 *            to track the request
	 * @return List of products
	 * @throws BaseException
	 *             exception thrown from Domain layer
	 * @throws AdapterException
	 *             exception thrown from Adapter layer
	 */
	Search searchProducts(Map<String, List<String>> searchCriteria,
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
