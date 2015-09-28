package com.belk.api.loader.manager;

import java.util.List;

import com.belk.api.exception.BaseException;

/**
 * The Loader factory to return the reference of any loader implementation
 * class.
 * 
 * @author Mindtree
 * @date March 13, 2014
 */
public interface LoaderManager {

	/**
	 * This method is to load the entire map for property file passed.
	 * 
	 * @param correlationId
	 *            - correlationId from request
	 * @throws BaseException
	 *             - Exception thrown from base class.
	 */
	void updateEntirePropertyMap(final String correlationId) throws BaseException;

	/**
	 * This method is to update value in the map for multiple keys passed in the
	 * request.
	 * 
	 * @param propertiesList
	 *            - list containing multiple key:value pair which needs to be
	 *            updated.
	 * @param correlationId
	 *            - correlationId from request
	 * @throws BaseException
	 *             - Exception thrown from base class.
	 */
	void updateMultiplePropertyInMap(final List<String> propertiesList, final String correlationId) throws BaseException;
}
