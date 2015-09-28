package com.belk.api.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.belk.api.adapter.contract.Adapter;
import com.belk.api.constants.CommonConstants;
import com.belk.api.constants.EndecaConstants;
import com.belk.api.constants.RequestAttributeConstant;
import com.belk.api.constants.ErrorConstants;
import com.belk.api.endeca.EndecaRequestContext;
import com.belk.api.exception.AdapterException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.model.Dimension;
import com.belk.api.util.EndecaLoader;
import com.belk.api.util.EndecaResponseProcessor;
import com.belk.api.util.EndecaUtil;
import com.belk.api.util.ErrorLoader;
import com.belk.api.util.URIToEndecaTransformer;
import com.endeca.content.ContentException;
import com.endeca.content.InitializationException;
import com.endeca.content.ene.ENEContentManager;
import com.endeca.content.ene.ENEContentQuery;
import com.endeca.content.ene.ENEContentResults;
import com.endeca.navigation.ENEQuery;
import com.endeca.navigation.ENEQueryException;
import com.endeca.navigation.ENEQueryResults;
import com.endeca.navigation.HttpENEConnection;
import com.endeca.navigation.UrlENEQueryParseException;

/**
 *  This class acts as interface between endeca and api. This
 * processes the input request parameters to Endeca specific parameters
 * creates the endeca query and invokes the required methods
 *  from the EndecaUtil to get the response.
 * Updated : Added few comments and changed the name of variables as part of Phase2, April,14 release
 * Update: Segregation of Constants as per code review comments.
 * 
 * @author Mindtree
 * @date Sep 10, 2013
 */
@Service
public class EndecaAdapter implements Adapter {

	/**
	 * creating instance of logger.
	 */
	// This will return logger instace
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	/**
	 * creating instance of Endeca Loader class.
	 */
	@Autowired
	EndecaLoader endecaLoader;

	/**
	 * creating instance of Error Loader class.
	 */
	@Autowired
	ErrorLoader errorLoader;

	/**
	 * creating instance of endeca util.
	 */
	@Autowired
	private EndecaUtil endecaUtil;

	/**
	 * creating instance of endeca response processor.
	 */
	@Autowired
	private EndecaResponseProcessor endecaResponseProcessor;

	/**
	 * creating instance of uri keys to endeca keys transformer.
	 */
	@Autowired
	private URIToEndecaTransformer uriToEndecaTransformer;

	/**
	 * Default Constructor.
	 */
	public EndecaAdapter() {
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
	 * setter method for the errorLoader.
	 * 
	 * @param errorLoader the errorLoader to set
	 */
	public final void setErrorLoader(final ErrorLoader errorLoader) {
		this.errorLoader = errorLoader;
	}


	/**
	 * @param uriToEndecaTransformer
	 *            the uriToEndecaTransformer to set
	 */
	public final void setUriToEndecaTransformer(
			final URIToEndecaTransformer uriToEndecaTransformer) {
		this.uriToEndecaTransformer = uriToEndecaTransformer;
	}

	/**
	 * @param endecaUtil
	 *            setter method for autowired
	 */
	public final void setEndecaUtil(final EndecaUtil endecaUtil) {
		this.endecaUtil = endecaUtil;
	}

	/**
	 * @param endecaResponseProcessor
	 *            setter method for autowired
	 */
	public final void setEndecaResponseProcessor(
			final EndecaResponseProcessor endecaResponseProcessor) {
		this.endecaResponseProcessor = endecaResponseProcessor;
	}

	/**
	 * The service method which all adapter implementation classes should
	 * implement. This is the generic method to be called from the service layer
	 * and the implementation class would decide the logic.
	 * 
	 * @param requestMap
	 *            The map containing the search request parameters as key-value
	 *            pairs
	 * @param optionNodes
	 *            the option nodes to set
	 * @param correlationId
	 *            tracking id
	 * @return Results in a list of maps.
	 * @throws AdapterException
	 *             The exception thrown from the Adapter Layer
	 */
	@Override
	public final List<Map<String, String>> service(
			final Map<String, String> requestMap,
			final Map<String, List<String>> optionNodes,
			final String correlationId)
					throws AdapterException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("RequestMap :" + requestMap, correlationId);
		/** converting request identifiers to endeca keys. Stored in EndecaRequestContext **/
		final EndecaRequestContext endecaContext = this.uriToEndecaTransformer
				.queryToURIEndecaContext(requestMap, optionNodes, correlationId);
		// creating endeca query
		final ENEQuery urlENEQuery = this.prepareEndecaQuery(endecaContext,
				correlationId);
		final List<Map<String, String>> endecaResultList = this.queryEndeca(endecaContext, urlENEQuery,
				correlationId);
		LOGGER.debug("Return ResultList :" + endecaResultList, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return endecaResultList;
	}

	/**
	 * The entry method to prepare the Endeca query to fetch the results.
	 * 
	 * @param endecaContext
	 *            The endeca context containing the map containing the request
	 *            parameters
	 * @param correlationId
	 *            tracking id
	 * @return List of maps containing the response from Endeca
	 * @throws AdapterException
	 *             The general exception thrown from Adapter layer
	 */
	private ENEQuery prepareEndecaQuery(
			final EndecaRequestContext endecaContext,
			final String correlationId) throws AdapterException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final Map<String, String> errorPropertiesMap = this.errorLoader.getErrorPropertiesMap();
		LOGGER.debug(" Map is :" + endecaContext.getEndecaRequestMap(), correlationId);
		ENEQuery urlENEQuery = null;
		try {
			//creating endeca query
			urlENEQuery = this.endecaUtil.buildRequest(endecaContext,
					correlationId);
		} catch (UrlENEQueryParseException e) {
			LOGGER.error(e, correlationId);
			throw new AdapterException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String.valueOf(ErrorConstants.ERROR_CODE_11523)),
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);
		}
		LOGGER.debug("Return value is:" + urlENEQuery, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return urlENEQuery;

	}

	/**
	 * The method to execute the Endeca query and fetches the result.
	 * @param endecaContext
	 * 			Context which stores the details of request parameters
	 * @param urlENEQuery
	 *            The query object for Endeca.
	 * @param correlationId
	 *            tracking Id
	 * @return Results from Endeca
	 * @throws AdapterException
	 *             The general exception thrown from Adapter layer.
	 */
	private List<Map<String, String>> queryEndeca(final EndecaRequestContext endecaContext, final ENEQuery urlENEQuery,
			final String correlationId) throws AdapterException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		List<Map<String, String>> endecaResultList = null;
		final Map<String, String> requestMap = endecaContext.getUriRequestMap();
		final Map<String, String> errorPropertiesMap = this.errorLoader.getErrorPropertiesMap();
		if (urlENEQuery == null) {
			throw new AdapterException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String.valueOf(ErrorConstants.ERROR_CODE_11523)),
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);
		}
		ENEQueryResults endecaResults = null;
		final Map<String, String> endecaPropertiesMap = this.endecaLoader.getEndecaPropertiesMap();
		// creates connection instance
		final HttpENEConnection endecaConn = new HttpENEConnection(endecaPropertiesMap.get(EndecaConstants.HOST),
				endecaPropertiesMap.get(EndecaConstants.PORT));
		LOGGER.debug("ENEQuery is : " + urlENEQuery, correlationId);
		try {
			/** If Boost and Bury feature is NOT required, perform ENEQuery else perform ENEContentQuery **/
			if (requestMap != null 
					&& requestMap.get(RequestAttributeConstant.BOOSTED_RECORDS) != null
					&& requestMap.get(RequestAttributeConstant.BOOSTED_RECORDS)
					.equalsIgnoreCase(CommonConstants.TRUE_VALUE)) {
				final ENEContentManager eneContentManager =  new ENEContentManager();
				eneContentManager.setValidating(false);
				final ENEContentQuery contenQuery = (ENEContentQuery) eneContentManager.createQuery();		
				urlENEQuery.setNavAllRefinements(true);

				contenQuery.setENEQuery(urlENEQuery);
				contenQuery.setENEConnection(endecaConn);
				/** Setting the Page Zone. If 'q' exists in parameter list, the zone is SearchPageZone **/
				if (urlENEQuery.getNavERecSearches() != null && !urlENEQuery.getNavERecSearches().isEmpty()) {
					contenQuery.setRuleZone(EndecaConstants.SEARCH_ZONE);
				} else {
					contenQuery.setRuleZone(EndecaConstants.NAVIGATION_ZONE);
				}
				final ENEContentResults eneContentResults = (ENEContentResults) contenQuery.execute();
				/** Calling the processor method to create the result list - map **/
				endecaResultList = this.endecaResponseProcessor
						.contentResponseProcessor(requestMap, eneContentResults, correlationId);
			} else {
				endecaResults = endecaConn.query(urlENEQuery);	
				/** Calling the processor method to create the result list - map **/
				endecaResultList = this.endecaResponseProcessor
						.responseProcessor(requestMap, endecaResults, correlationId);
			} 
		} catch (InitializationException | ContentException | ENEQueryException e) {
			LOGGER.error(e, correlationId);
			throw new AdapterException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String.valueOf(ErrorConstants.ERROR_CODE_11523)),
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);
		} 
		
		LOGGER.debug(" Return value is : " + endecaResults, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return endecaResultList;
	}

	/**
	 * This method to be called from the service layer to perform dimension
	 * query on endeca.
	 * 
	 * @param requestMap
	 *            The map containing the search request parameters as key-value
	 *            pairs
	 * @param correlationId
	 *            tracking Id
	 * @return Results in a list of dimensions.
	 * @throws AdapterException
	 *             The exception thrown from the Adapter Layer
	 */
	public final Map<Long, Dimension> makeDimensionQuery(
			final Map<String, String> requestMap, final String correlationId)
					throws AdapterException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final Map<String, String> errorPropertiesMap = this.errorLoader.getErrorPropertiesMap();
		Map<Long, Dimension> dimensionMap = null;
		try {
			// To load all dimension details from endeca
			dimensionMap = this.endecaUtil.loadDimensionTree(requestMap,
					correlationId);
		} catch (ENEQueryException e) {
			LOGGER.error(e, correlationId);
			throw new AdapterException(
					String.valueOf(ErrorConstants.ERROR_CODE_11521),
					errorPropertiesMap.get(String.valueOf(ErrorConstants.ERROR_CODE_11521)),
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);
		}
		LOGGER.debug("Dimension map loaded", correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return dimensionMap;
	}

}
