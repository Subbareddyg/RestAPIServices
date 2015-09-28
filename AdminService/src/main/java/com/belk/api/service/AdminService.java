package com.belk.api.service;

import java.util.List;
import java.util.Map;

import com.belk.api.business.service.contract.Services;
import com.belk.api.exception.AdapterException;
import com.belk.api.exception.BaseException;
import com.belk.api.exception.ServiceException;

/**
 * This is a contract for the Admin services to be implemented.
 * Update: The contract has been updated to include methods for the configuration framework.
 * 
 * @author Mindtree
 * @date Nov 29, 2013
 */
public interface AdminService extends Services {
	/**
	 * The method searches the catalogs based on search criteria for the given
	 * input.
	 * 
	 * @param uriMap
	 *            The request map containing the search criteria
	 * @param correlationId
	 *            - correlationId from the request
	 * @throws BaseException
	 *             this is the application level exception thrown if any
	 *             exception occurred
	 * @throws ServiceException
	 *             common exception for service layer
	 * @throws AdapterException
	 *             common exception for adapter layer
	 */
	void populateCategoriesInCache(Map<String, String> uriMap, String correlationId)
			throws BaseException, ServiceException, AdapterException;

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
	
	/**
	 * This method triggers the DAO for loading the Coremetrics data and sets the 
	 * product relation to Cache
	 * @param correlationId
	 * @throws ServiceException
	 * 
	 */
	void loadCoremetricsData(String correlationId) throws ServiceException;
}
