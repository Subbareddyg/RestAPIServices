package com.belk.api.transformer.json;

import org.springframework.stereotype.Service;

import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.google.gson.Gson;

/**
 * This class is used by BaseResource to generate the response in json format.
 * 
 * @author Mindtree
 * @date sep 23, 2013 *
 */
@Service
public class JSONProcessor {
	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	/**
	 * Default Constructor.
	 */
	public JSONProcessor() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Method to build a JSON response from an XML input.
	 * 
	 * @param target
	 *            The input object to be converted to JSON
	 * @param correlationId
	 *            to track the request
	 * @return Response string in JSON format
	 */
	public final String buildJSONResponse(final Object target,
			final String correlationId) {
		String json = null;
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final Gson gson = new Gson();
		json = gson.toJson(target);
		// Removed the feature of printing the xml response in to the logs.
		LOGGER.info(
				"JSONProcessor.buildJSONResponse() - JSON out put generated: ", correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return json;
	}

}
