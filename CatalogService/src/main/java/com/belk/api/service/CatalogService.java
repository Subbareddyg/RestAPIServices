package com.belk.api.service;

import java.util.List;
import java.util.Map;

import com.belk.api.business.service.contract.Services;
import com.belk.api.exception.AdapterException;
import com.belk.api.exception.BaseException;
import com.belk.api.exception.ServiceException;
import com.belk.api.model.catalog.Catalog;

/**
 * This is a contract for the catalog services to be implemented.
 * Update: The contract has been updated to include methods for the configuration framework.
 * 
 * @author Mindtree
 * @date Nov 06, 2013
 */
public interface CatalogService extends Services {
	/**
	 * The method searches the catalogs based search criteria for the given
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
	 * @return Catalog Catalog pojo after the details of the catalog are
	 *         retrieved
	 */
	Catalog getCatalog(final Map<String, String> uriMap,
			final String correlationId) throws BaseException, ServiceException,
			AdapterException;

	/**
	 * Method to invoke appropriate loader to reload map of property file specified in request.
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

	/** Method to get the catalog data directly as a formatted string.
	 * @param uriMap
	 * 			 - The request map containing the search criteria
	 * @param correlationId
	 *            - correlationId from the request
	 * @return
	 * 			- The converted Catalog in the format of XML.
	 * @throws BaseException
	 * 			- application exception
	 * @throws ServiceException
	 *            - common exception for service layer
	 * @throws AdapterException
	 * 			- common exception for adapter layer
	 */
	String getCatalogAsString(final Map<String, String> uriMap,
			final String correlationId) throws BaseException, ServiceException,
			AdapterException;

}
