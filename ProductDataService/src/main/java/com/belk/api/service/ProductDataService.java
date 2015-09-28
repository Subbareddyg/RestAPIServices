/**
 * 
 */
package com.belk.api.service;

import java.util.List;
import java.util.Map;

import com.belk.api.business.service.contract.Services;
import com.belk.api.exception.AdapterException;
import com.belk.api.exception.BaseException;
import com.belk.api.exception.ServiceException;
import com.belk.api.model.productdetails.ProductList;

/** This is the contract for the service implementation class of ProductData service
 * @author Mindtree
 * @date 29 Oct, 2014
 *
 */
public interface ProductDataService extends Services {
	
	/**
	 * This method gets product details along with the Coremetrics recommended products list.
	 *  
	 * @param uriMap
	 *            - map of query parameters
	 * @param correlationId
	 *            to track the request
	 * @return ProductList
	 * @throws BaseException
	 *             The general exception from Domain layer
	 * @throws AdapterException
	 *             The general exception from Adapter layer
	 */
	ProductList getCoremetricsRecommendedProductList(final Map<String, List<String>> uriMap,
			final String correlationId) throws BaseException, AdapterException;

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
	void updatePropertiesMap(String correlationId,
			Map<String, List<String>> updateRequestUriMap) throws ServiceException;

}
