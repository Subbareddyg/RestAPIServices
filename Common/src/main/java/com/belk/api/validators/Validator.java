package com.belk.api.validators;

import java.util.List;
import java.util.Map;

import com.belk.api.exception.BaseException;

/**
 * Validatoe interface.
 * 
 * @author Mindtree.
 * 
 */
public interface Validator {
	/**
	 * Method to validate the requesting url.
	 * 
	 * @param uriMap
	 *            - query parameters
	 * @param correlationId
	 *            - to track the request
	 * @throws BaseException
	 *             The general exception from Adapter layer
	 */
	void validate(Map<String, List<String>> uriMap, String correlationId)
			throws BaseException;

	/**
	 * method to get the instance of ResourceValidationConfig.
	 * 
	 * @return ResourceValidationConfig
	 */
	ResourceValidationConfig getResourceValidationConfig();

	/**
	 * Method to set the resourceValidationConfig.
	 * 
	 * @param resourceValidationConfig
	 *            - instance of ResourceValidationConfig
	 */
	void setResourceValidationConfig(
			ResourceValidationConfig resourceValidationConfig);

}
