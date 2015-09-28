package com.belk.api.blueMartini;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.belk.api.adapter.constants.BlueMartiniAdapterConstants;
import com.belk.api.constants.CommonConstants;
import com.belk.api.constants.ErrorConstants;
import com.belk.api.exception.AdapterException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.util.ErrorLoader;
import com.belk.api.util.HttpConnectionLoader;

/**
 * The common blue martini utility class for building the blue martini url. The
 * class exposes a public method to accept the request parameters and internally
 * builds an blue martini url using them.
 * 
 * Update: This class has been updated to fix the review comments and to add additional fields, as
 * part of CR: April 2014
 * 
 * Updated : Added few comments and changed the name of variables as part of Phase2, April,14 release
 * @author Mindtree
 * @date Nov 2nd, 2013
 */

@Component
public class HttpClientUtil {
	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	/**
	 * creating instance of Error Loader class.
	 */
	@Autowired
	ErrorLoader errorLoader;

	/**
	 * creating instance of HttpConnection Loader class.
	 */
	@Autowired
	HttpConnectionLoader httpConnectionLoader;

	/**
	 * Default constructor.
	 */
	public HttpClientUtil() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * setter method for the httpConnectionLoader.
	 * 
	 * @param httpConnectionLoader
	 *            the httpConnectionLoader to set
	 */
	public final void setHttpConnectionLoader(final HttpConnectionLoader httpConnectionLoader) {
		this.httpConnectionLoader = httpConnectionLoader;
	}
    
	/**
	 * setter method for the errorLoader.
	 * 
	 * @param errorLoader
	 *            the errorLoader to set
	 */
	public final void setErrorLoader(final ErrorLoader errorLoader) {
		this.errorLoader = errorLoader;
	}

	/**
	 * The method to hit the blue matini using the params and requestor that we
	 * pass and returns the json object.
	 * 
	 * @param requestor
	 *            patternDetails
	 * @param params
	 *            request params
	 * @param correlationId
	 *            correlation id
	 * @return JSONObject Blue martini result
	 * @throws AdapterException
	 *             The exception thrown from the Adapter Layer if it occurs
	 * 
	 * @author Mindtree
	 * @date Nov 2nd, 2013
	 */
	public final JSONObject getHttpContent(final String requestor,
			final Map<String, String> params, final String correlationId)
			throws AdapterException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("Requestor :" + requestor + CommonConstants.COMMA_SEPERATOR + "params in uriMap :" + params, correlationId);
		final Map<String, String> httpConnectionPropertiesMap = this.httpConnectionLoader.getHttpConnectionPropertiesMap();
		final Map<String, String> errorPropertiesMap = this.errorLoader.getErrorPropertiesMap();
		String requestUrl = httpConnectionPropertiesMap.get(BlueMartiniAdapterConstants.PATTERN_DETAILS_URL);

		if (requestUrl == null) {
			throw new AdapterException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String.valueOf(ErrorConstants.ERROR_CODE_11523)),
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);
		}
		final HttpClient client = new DefaultHttpClient();
		//Forming the blueMartini jsp reference url
		requestUrl = this.appendParams(requestUrl, params, correlationId);
		//To get the information regarding the request url
		final HttpGet getRequestURLInfo = new HttpGet(requestUrl);
		JSONObject blueMartiniResponse = null;

		try {
			this.prepareClient(httpConnectionPropertiesMap, client);

			//Calling method execute to get the response for corresponding url
			final HttpResponse response = client.execute(getRequestURLInfo);
			final BufferedReader bufferReader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			//Getting response by hitting the reference jsp
			blueMartiniResponse = new JSONObject(this.replaceComments(bufferReader,
					correlationId));

		} catch (ClientProtocolException e) {
			LOGGER.error(e, correlationId);
			throw new AdapterException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String.valueOf(ErrorConstants.ERROR_CODE_11523)),
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);
		} catch (NullPointerException e) {
			LOGGER.error(e, correlationId);
			throw new AdapterException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String.valueOf(ErrorConstants.ERROR_CODE_11523)),
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);
		} catch (IOException e) {
			LOGGER.error(e, correlationId);
			throw new AdapterException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String.valueOf(ErrorConstants.ERROR_CODE_11523)),
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);
		} catch (JSONException e) {
			LOGGER.error(e, correlationId);
			throw new AdapterException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String.valueOf(ErrorConstants.ERROR_CODE_11523)),
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);
		} catch (IllegalStateException e) {
			LOGGER.error(e, correlationId);
			throw new AdapterException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String.valueOf(ErrorConstants.ERROR_CODE_11523)),
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);
		} finally {
			LOGGER.debug("BlueMartiniResponse :" + blueMartiniResponse, correlationId);
			LOGGER.logMethodExit(startTime, correlationId);
		}
		return blueMartiniResponse;
	}

	/**This method prepares the client for the call
	 * @param httpConnectionPropertiesMap map containing the connection properties
	 * @param client the HTTPClient object
	 * @throws NumberFormatException exception thrown from this method
	 */
	private void prepareClient(
			final Map<String, String> httpConnectionPropertiesMap,
			final HttpClient client) throws NumberFormatException {
		//Setting socket time and connection time out for http client
		client.getParams().setParameter(BlueMartiniAdapterConstants.HTTP_SOCKET_TIMEOUT,
				Integer.parseInt(httpConnectionPropertiesMap.get(BlueMartiniAdapterConstants.SOCKET_TIMEOUT)));
		client.getParams().setParameter(BlueMartiniAdapterConstants.HTTP_CONNECTION_TIMEOUT,
				Integer.parseInt(httpConnectionPropertiesMap.get(BlueMartiniAdapterConstants.CONNECTION_TIMEOUT)));
	}

	/**
	 * The method to replace the comments or String in the results that generated by
	 * blue martini.
	 * 
	 * @param bufferReader
	 *            bufferedReader
	 * @param correlationId
	 *            correlation id
	 * @return String
	 * @throws AdapterException
	 *             The exception thrown from the Adapter Layer if it occurs
	 * 
	 * @author Mindtree
	 * @date Nov 2nd, 2013
	 */
	public final String replaceComments(final BufferedReader bufferReader,
			final String correlationId) throws AdapterException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final StringBuilder stringBuilder = new StringBuilder();
		final Map<String, String> errorPropertiesMap = this.errorLoader.getErrorPropertiesMap();
		String line = null;
		try {
			while ((line = bufferReader.readLine()) != null) {
				stringBuilder.append(line).append("\n");
			}
		} catch (IOException e) {
			LOGGER.error(e, correlationId);
			throw new AdapterException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String.valueOf(ErrorConstants.ERROR_CODE_11523)),
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);
		}
		String sbString = null;
		//To remove the extra string in the blue Martini Response
		if (stringBuilder.toString().startsWith(
				"<!-- Printing response to front end -->")) {
			sbString = stringBuilder.toString().replaceAll(
					"<!-- Printing response to front end -->\n", "");
			LOGGER.debug("SubString  :" + sbString, correlationId);
			LOGGER.logMethodExit(startTime, correlationId);
		}
		return sbString;
	}

	/**
	 * The method to append the query parameters to the request url, inorder to
	 * form the understandable url to BlueMartini.
	 * 
	 * @param requestUrl
	 *            BlueMartini url
	 * @param params
	 *            requested parameters
	 * @param correlationId
	 *            correlation id
	 * @return complete url of blue martini
	 * @throws AdapterException
	 *             The exception thrown from the Adapter Layer if it occurs
	 * 
	 * @author Mindtree
	 * @date Nov 2nd, 2013
	 */
	private String appendParams(final String requestUrl,
			final Map<String, String> params, final String correlationId)
			throws AdapterException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final StringBuilder stringBuilder = new StringBuilder();
		//Append required special characters to the string to form the complete url 
		stringBuilder.append("?");
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (stringBuilder.length() > 1) {
				stringBuilder.append("&");
			}
		//To append the requested parameters to the url
			stringBuilder.append(String.format("%s=%s",
					this.urlEncodeUTF8(entry.getKey().toString(), correlationId),
					this.urlEncodeUTF8(entry.getValue().toString(), correlationId)));
		
			LOGGER.debug("Request url :" + requestUrl.concat(stringBuilder.toString()), correlationId);
			LOGGER.logMethodExit(startTime, correlationId);
		}
		
		return requestUrl.concat(stringBuilder.toString());
	}

	/**
	 * The method to encode a string using the format UCS Transformation
	 * Format8-bit.
	 * 
	 * @param value
	 *            value
	 * @param correlationId
	 *            correlation id
	 * @return encode value
	 * @throws AdapterException
	 *             The exception thrown from the Adapter Layer if it occurs
	 * 
	 * @author Mindtree
	 * @date Nov 2nd, 2013
	 */
	private String urlEncodeUTF8(final String value, final String correlationId)
			throws AdapterException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final Map<String, String> errorPropertiesMap = this.errorLoader.getErrorPropertiesMap();
		try {
			LOGGER.debug("Request url encoder :" + URLEncoder.encode(value, "UTF-8"), correlationId);
			LOGGER.logMethodExit(startTime, correlationId);
			//encode a string using the format UCS Transformation Format8-bit.
			return URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e, correlationId);
			throw new AdapterException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String.valueOf(ErrorConstants.ERROR_CODE_11523)),
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);
		}

	}
}