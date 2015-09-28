package com.belk.api.service;

import java.util.List;
import java.util.Map;

import com.belk.api.business.service.contract.Services;
import com.belk.api.exception.BaseException;
import com.belk.api.exception.ServiceException;
import com.belk.api.security.AuthenticationDetails;

/**
 * This is a contract for the Security services to be implemented.
 * 
 * @author Mindtree
 * @date May 28, 2014
 */
public interface SecurityService extends Services {

	/**
	 * Processes the request to serve with an authentication token.
	 * 
	 * @param encryptedAppId
	 *            The encrypted application id
	 * @param encryptedKeyParams
	 *            The encrypted application parameters
	 * @param correlationId
	 *            to track the request
	 * @return an AuthenticationDetails object consisting the authentication
	 *         token.
	 * @throws BaseException
	 *             The exception thrown from this method
	 */
	AuthenticationDetails processRequest(String encryptedAppId,
			String encryptedKeyParams, String correlationId)
					throws BaseException;

	/**
	 * This method is to invoke appropriate loader to reload map of property
	 * file specified in request.
	 * 
	 * @param correlationId
	 *            - correlationId from the request
	 * @param updateRequestUriMap
	 *            - The request map
	 * @throws ServiceException
	 *             common exception for service layer
	 */
	void updatePropertiesMap(String correlationId,
			Map<String, List<String>> updateRequestUriMap)
			throws ServiceException;

}
