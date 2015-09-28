package com.belk.api.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.belk.api.constants.CommonConstants;
import com.belk.api.constants.EndecaConstants;
import com.belk.api.constants.ErrorConstants;
import com.belk.api.constants.RequestAttributeConstant;
import com.belk.api.endeca.EndecaRequestContext;
import com.belk.api.exception.AdapterException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;

/**
 * The transformer class for mapping the request parameters of any services
 * accordingly to the corresponding Endeca keys.
 * Updated : Added few comments and changed the name of variables as part of Phase2, April,14 release
 * Update: Segregation of Constants as per code review comments
 * 
 * @author Mindtree
 * @date September 30, 2013
 */
@Service
public class URIToEndecaTransformer {

	/**
	 * creating instance of logger.
	 */
	// this will return the logger instance
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	/**
	 * creating instance of endeca FieldList Loader Loader class.
	 */
	@Autowired
	EndecaFieldListLoader endecaFieldListLoader;

	/**
	 * creating instance of Error Loader class.
	 */
	@Autowired
	ErrorLoader errorLoader;

	/**
	 * creating instance of Endeca Loader class.
	 */
	@Autowired
	EndecaLoader endecaLoader;

	/**
	 * Default Constructor.
	 */
	public URIToEndecaTransformer() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * setter method for the endecaLoader.
	 * 
	 * @param endecaLoader the endecaLoader to set
	 */
	public final void setEndecaLoader(final EndecaLoader endecaLoader) {
		this.endecaLoader = endecaLoader;
	}

	/**
	 * setter method for the endecaFieldListLoader.
	 * 
	 * @param endecaFieldListLoader the endecaFieldListLoader to set
	 */
	public final void setEndecaFieldListLoader(final EndecaFieldListLoader endecaFieldListLoader) {
		this.endecaFieldListLoader = endecaFieldListLoader;
	}

	/**
	 * setter method for the errorLoader.
	 * 
	 * @param errorLoader the errorLoader to set
	 */
	public final void setErrorLoader(final ErrorLoader errorLoader) {
		this.errorLoader = errorLoader;
	}

	/**
	 * The method to map entries from the request uri map to equivalent Endeca keys.
	 * 
	 * @param uriMap
	 *            Map containing uri parameters
	 * @param optionNodes 
	 * 			  the list of option nodes
	 * @param correlationId
	 *            to track the request
	 * @return requestMap The map containing the request parameter values mapped
	 *         to the corresponding Endeca keys.
	 * @throws AdapterException general exception thrown from adapter layer.
	 */
	public final EndecaRequestContext queryToURIEndecaContext(
			final Map<String, String> uriMap, final Map<String, List<String>> optionNodes, final String correlationId)
					throws AdapterException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final Map<String, String> endecaPropertiesMap = this.endecaFieldListLoader.getEndecaFieldListpropertiesMap();
		final Map<String, String> errorPropertiesMap = this.errorLoader.getErrorPropertiesMap();
		final EndecaRequestContext requestContext = new EndecaRequestContext();
		LOGGER.debug("Request Map before URI to EndecaMapper:" + uriMap, correlationId);
		String requestKey = null;
		String key = null;
		String requestValue = null;
		final Map<String, String> endecaRequestMap = new HashMap<String, String>();
		try {
			//Setting the uri request param to a new map
			requestContext.setUriRequestMap(this.processUriRequestMap(uriMap, correlationId));
			for (Map.Entry<String, String> mapEntries : uriMap.entrySet()) {
				requestKey = mapEntries.getKey();
				key = endecaPropertiesMap.get(requestKey);
				//Added the following logic for API-351
				if (requestKey.equalsIgnoreCase(RequestAttributeConstant.CATEGORY_ID) 
						&& mapEntries.getValue().equalsIgnoreCase(CommonConstants.ROOT)) {
					requestValue = this.endecaLoader.getEndecaPropertiesMap()
							.get(EndecaConstants.CATALOG_ROOT_DIMENSION_VALUE);	 
				} else {
					requestValue = mapEntries.getValue();
				}
				endecaRequestMap.put(key, requestValue);
			}
		} catch (MissingResourceException e) {
			LOGGER.error(e, correlationId);
			throw new AdapterException(
					String.valueOf(ErrorConstants.ERROR_CODE_11421),
					errorPropertiesMap.get(String.valueOf(ErrorConstants.ERROR_CODE_11421)), requestKey,
					ErrorConstants.HTTP_STATUS_CODE_INVALID_URL_PARAMETERS,
					correlationId);
		}

		LOGGER.debug(" Request Map after URI to EndecaMapper" + endecaRequestMap,
				correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		requestContext.setEndecaRequestMap(endecaRequestMap);
		requestContext.setOptionNodes(optionNodes);
		return requestContext;
	}


	/**
	 * This method will map all the request params
	 * from the request map to a new map in EndecaRequestContext - setUriRequestMap
	 * @param uriMap a map of the uri elements
	 * @param correlationId to track the request
	 * @return a map of the uri elements
	 * @throws AdapterException the exception thrown from this method
	 */
	public final Map<String, String> processUriRequestMap(final Map<String, String> uriMap, 
			final String correlationId) throws AdapterException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		// fetching boost and bury parameter.
		final Map<String, String> elementMap = new HashMap<String, String>(uriMap);
		if (uriMap != null && uriMap.get(RequestAttributeConstant.BOOSTED_RECORDS) != null) {
			uriMap.remove(RequestAttributeConstant.BOOSTED_RECORDS);
		}
		LOGGER.debug("Additional Request Map :" + elementMap, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return elementMap;
	}	

}
