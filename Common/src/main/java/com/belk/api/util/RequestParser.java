package com.belk.api.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.belk.api.constants.CommonConstants;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;

/**
 * This class will tokenize the request parameter based on the delimiters.
 * 
 * Updated : Added few comments and changed the name of variables as part of
 * Phase2, April,14 release
 * 
 * @author Mindtree
 * @date 25 Sep 2013
 */
@Service
public class RequestParser {
	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	/**
	 * requestMap specify the map of <String,String>.
	 */
	private Map<String, String> requestMap;

	/**
	 * Default Constructor.
	 */
	public RequestParser() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Method to return the map with the delimiters removed.
	 * 
	 * @param inputMap
	 *            Input map of request criteria
	 * @param correlationId
	 *            to track the request
	 * @return a formatted map having key value pairs
	 */
	public final Map<String, String> processRequestAttribute(
			final Map<String, List<String>> inputMap, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final Map<String, String> removeDelimiterMap = new HashMap<String, String>();
		for (Map.Entry<String, List<String>> tempMap : inputMap.entrySet()) {
			for (String attributeString : tempMap.getValue()) {
				LOGGER.debug("attributeString " + attributeString,
						correlationId);
				if (!(attributeString
						.contains(CommonConstants.FIELD_PAIR_SEPARATOR) || attributeString
						.contains(CommonConstants.PIPE_SEPERATOR))) {
					removeDelimiterMap.put(tempMap.getKey(), attributeString);
				} else if (attributeString
						.contains(CommonConstants.PIPE_SEPERATOR)
						&& !attributeString
						.contains(CommonConstants.FIELD_PAIR_SEPARATOR)) {
					attributeString.replaceAll(CommonConstants.PIPE_SEPERATOR,
							CommonConstants.COMMA_SEPERATOR);
					removeDelimiterMap.put(tempMap.getKey(), attributeString);
				} else {
					final StringTokenizer tokenPipe = new StringTokenizer(
							attributeString, CommonConstants.PIPE_SEPERATOR);
					while (tokenPipe.hasMoreTokens()) {
						this.tokenSeperator(removeDelimiterMap, tokenPipe,
								correlationId);
					}
				}
			}
		}
		LOGGER.debug(
				"RequestMap after delimiters remover" + removeDelimiterMap,
				correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return removeDelimiterMap;
	}

	/**
	 * This method separates the values present in the map from the specific
	 * delimiters.
	 * 
	 * @param tokenSeperatorMap
	 *            this contains the key value pairs which has colon as
	 *            delimeters
	 * @param tokenPipe
	 *            is a tokenized string which has pipe delimited
	 * @param correlationId
	 *            is to track the request
	 */
	private void tokenSeperator(final Map<String, String> tokenSeperatorMap,
			final StringTokenizer tokenPipe, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		String[] pipe;
		String[] colon;
		int indexerPipe;
		int indexerColon;
		pipe = new String[10];
		indexerPipe = 0;
		pipe[indexerPipe] = tokenPipe.nextToken();
		indexerPipe += 1;
		for (int i = 0; i < indexerPipe; i++) {
			final StringTokenizer tokenColon = new StringTokenizer(pipe[i],
					CommonConstants.FIELD_PAIR_SEPARATOR);
			colon = new String[10];
			indexerColon = 0;
			while (tokenColon.hasMoreTokens()) {
				colon[indexerColon++] = tokenColon.nextToken();
			}
			for (int j = 0; j < indexerColon; j++) {
				tokenSeperatorMap.put(colon[j], colon[++j]);
			}
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * This method forms endeca request by converting request identifiers to
	 * endeca keys like styleupc(request identifier) to P_product_code (Endeca
	 * key)
	 * 
	 * @param uriMap
	 *            - map of query parameters
	 * @param correlationId
	 *            to track the request
	 * @return requestMap
	 */
	public final Map<String, String> prepareRequest(
			final Map<String, List<String>> uriMap, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		this.requestMap = new LinkedHashMap<String, String>();
		final Iterator<Map.Entry<String, List<String>>> requestMapIterator = uriMap
				.entrySet().iterator();
		while (requestMapIterator.hasNext()) {
			final Entry<String, List<String>> requestEntry = requestMapIterator
					.next();
			this.requestMap.put(requestEntry.getKey(), StringUtils
					.collectionToCommaDelimitedString(requestEntry.getValue()));
		}
		LOGGER.debug("Request Parameters Before sending to Endeca : "
				+ this.requestMap, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return this.requestMap;
	}

	/**
	 * Method to convert request node to a map
	 * 
	 * @param optionValue
	 *            option value to set
	 * @param correlationId
	 *            to track the request
	 * @return a map containing the optional node
	 */
	public final Map<String, List<String>> convertRequestNodetoMapper(
			final String optionValue, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final String optionValueProcessed = optionValue.toLowerCase();
		final Map<String, List<String>> optionalNodeMap = new HashMap<String, List<String>>();
		final String[] optionArray = optionValueProcessed.split(",");
		final List<String> productOptionList = Arrays.asList(optionArray);
		LOGGER.debug("Option Parameter Values : "
				+ productOptionList.toString(), correlationId);
		optionalNodeMap.put("options", productOptionList);
		LOGGER.logMethodExit(startTime, correlationId);
		return optionalNodeMap;
	}

}
