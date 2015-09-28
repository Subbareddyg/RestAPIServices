package com.belk.api.service;

import java.util.List;
import java.util.Map;

import com.belk.api.business.service.contract.Services;
import com.belk.api.exception.AdapterException;
import com.belk.api.exception.BaseException;
import com.belk.api.exception.ServiceException;
import com.belk.api.model.categorydetails.Categories;

/**
 * This is a contract for service implementation class.
 * 
 * 
 * @author Mindtree
 * @date Nov 06, 2013
 */
public interface CategoryDetailsService extends Services {

	/**
	 * The method to get the category details based on the identifiers passed in
	 * the request.
	 * 
	 * @param uriMap
	 *            map of query parameters
	 * @param correlationId
	 *            to track the request
	 * @return Categories
	 * @throws BaseException
	 *             The domain level exception thrown from this method
	 * @throws ServiceException
	 *             The Service level exception thrown from this method
	 * @throws AdapterException
	 *             The Adapter level exception thrown from this method
	 */
	Categories getCategoryDetails(Map<String, List<String>> uriMap,
			String correlationId) throws BaseException, ServiceException,
			AdapterException;

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
