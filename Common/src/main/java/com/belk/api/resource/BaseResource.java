package com.belk.api.resource;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.JiBXException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.belk.api.constants.CommonConstants;
import com.belk.api.constants.ErrorConstants;
import com.belk.api.exception.BaseException;
import com.belk.api.exception.ServiceException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.security.SecurityUtil;
import com.belk.api.transformer.json.JSONProcessor;
import com.belk.api.transformer.xml.XMLProcessor;
import com.belk.api.util.ErrorLoader;

/**
 * This is the Base class for all api services, this generates the response in
 * xml or json format by processing the relevant request. This also generates
 * the error message format if any exceptions are thrown.
 * 
 * Update: correlationId has been added to all the methods as part of CR:
 * CR_BELK_API_1. correlationId is used to track the logs at an enterprise
 * level. Update: Added couple of methods as part of new requirement(404
 * messaging) for Phase2 April,14 release Also added few comments and changed
 * the name of variables.
 * 
 * Update: new method validateRequest has been added as part of security API
 * framework.
 * 
 * Update: Removed Access-Control-Allow-Origin parameter from header in couple
 * of places as the same is handled by CORS: API-338
 * 
 * @author Mindtree
 * @date Jul 27, 2013
 */
public class BaseResource {

	/**
	 * logger is to create instance of GenericLogger.
	 */

	static GenericLogger logger;

	/**
	 * declare allowedTypesList.
	 */

	private static List<String> allowedTypesList;

	/**
	 * instance if UriInfo created below is injected.
	 */
	@Context
	protected UriInfo uriInfo;

	/**
	 * instance of HttpHeaders created bellow is injected.
	 */
	@Context
	protected HttpHeaders headers;

	/**
	 * creating instance of Error Loader class.
	 */
	@Autowired
	ErrorLoader errorLoader;

	/**
	 * xmlProcessor is to create instance of XMLProcessor.
	 */
	@Autowired
	XMLProcessor xmlProcessor;

	/**
	 * jsonProcessor is to create instance of JSONProcessor.
	 */
	@Autowired
	JSONProcessor jsonProcessor;

	@Autowired
	SecurityUtil securityUtils;

	/**
	 * Default Constructor.
	 */
	public BaseResource() {
	}

	/**
	 * @param jsonProcessor
	 *            the jsonProcessor to set
	 */
	public final void setJsonProcessor(final JSONProcessor jsonProcessor) {
		this.jsonProcessor = jsonProcessor;
	}

	/**
	 * @param xmlProcessor
	 *            the xmlProcessor to set
	 */
	public final void setXmlProcessor(final XMLProcessor xmlProcessor) {
		this.xmlProcessor = xmlProcessor;
	}

	/**
	 * @param uriInfo
	 *            the uriInfo to set
	 */
	public final void setUriInfo(final UriInfo uriInfo) {
		this.uriInfo = uriInfo;
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
	 * @param securityUtils
	 *            the securityUtils to set
	 */
	public final void setSecurityUtils(final SecurityUtil securityUtils) {
		this.securityUtils = securityUtils;
	}

	/*
	 * This block tokenizes all the allowed response types and adds to a list
	 */
	static {
		final StringTokenizer tokenizer = new StringTokenizer(
				CommonConstants.ALLOWED_MEDIA_TYPE, ",");
		allowedTypesList = new ArrayList<String>();
		while (tokenizer.hasMoreTokens()) {
			allowedTypesList.add(tokenizer.nextToken());
		}
	}

	/**
	 * Method to build the response object in the requested format after
	 * marshalling the pojo.
	 * 
	 * @param target
	 *            Final object to be built
	 * @param className
	 *            class name to be bound
	 * @param bindingName
	 *            binding file name
	 * @param correlationId
	 *            to track the request
	 * @param responseType
	 *            the response format requested from the URI
	 * @return a response in the requested format
	 * @throws BaseException
	 *             Exception thrown from Common Layer
	 **/

	public final ResponseBuilder buildResponse(final Object target,
			final Class<?> className, final String bindingName,
			final String responseType, final String correlationId)
					throws BaseException {
		logger = LoggerUtil.getLogger();
		final long startTime = logger.logMethodEntry(correlationId);
		logger.info(" target :" + target + ", className : " + className
				+ ", bindingClassName : " + bindingName, correlationId);
		final int statusCode = CommonConstants.STATUS_OK;
		byte[] responseByteArray;
		ByteArrayOutputStream xmlResponseStream = null;
		ResponseBuilder responseBuilder = null;
		final Map<String, String> errorPropertiesMap = this.errorLoader
				.getErrorPropertiesMap();
		try {
			if (target == null) {
				// this method call to generate response if endeca does not
				// return any data
				final ServiceException nonMatchingRespEx = new ServiceException(
						String.valueOf(ErrorConstants.ERROR_CODE_11436),
						errorPropertiesMap.get(String
								.valueOf(ErrorConstants.ERROR_CODE_11436)),
								CommonConstants.EMPTY_VALUE,
								CommonConstants.EMPTY_VALUE,
								ErrorConstants.HTTP_STATUS_CODE_EMPTY_SEARCH_RESULT,
								correlationId);
				this.processException(nonMatchingRespEx, responseType,
						correlationId);

			} else {
				if (responseType
						.equalsIgnoreCase(CommonConstants.MEDIA_TYPE_XML)) {

					xmlResponseStream = this.xmlProcessor.buildXMLResponse(
							target, className, bindingName, correlationId);
					responseByteArray = xmlResponseStream.toByteArray();
					/* By default, the response would be in XML format */
					responseBuilder = Response
							.status(statusCode)
							.entity(responseByteArray)
							.header(CommonConstants.CONTENT_TYPE,
									CommonConstants.CONTENT_TYPE_VALUE_XML)
									.header(CommonConstants.CORRELATION_ID,
											correlationId);
				} else {
					if (responseType
							.equalsIgnoreCase(CommonConstants.MEDIA_TYPE_JSON)) {

						/*
						 * Building the JSON response
						 */
						final String jsonResponse = this.jsonProcessor
								.buildJSONResponse(target, correlationId);
						responseByteArray = jsonResponse
								.getBytes(CommonConstants.UTF_TYPE);
						responseBuilder = Response
								.status(statusCode)
								.entity(responseByteArray)
								.header(CommonConstants.CONTENT_TYPE,
										CommonConstants.CONTENT_TYPE_VALUE_JSON)
										.header(CommonConstants.CORRELATION_ID,
												correlationId);
					}
				}

			}
		} catch (final UnsupportedEncodingException e) {
			logger.error(e, correlationId);

			responseBuilder = Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.entity(ErrorConstants.ERROR_DETAILS
							+ ErrorConstants.ERROR_CODE_11523
							+ ErrorConstants.ERROR_CODE
							+ errorPropertiesMap.get(String
									.valueOf(ErrorConstants.ERROR_CODE_11523))
									+ ErrorConstants.ERROR_DETAILS_END)
									.type(MediaType.APPLICATION_XML)
					.header(CommonConstants.CORRELATION_ID, correlationId);
		}

		logger.logMethodExit(startTime, correlationId);
		return responseBuilder;
	}

	/**
	 * This method to generate process the request if any exception is thrown
	 * with the error details
	 * 
	 * @param exception
	 *            base exception to be sent
	 * @param responseType
	 *            to be sent
	 * @param correlationId
	 *            to track the request
	 */
	public final void processException(final BaseException exception,
			final String responseType, final String correlationId) {
		logger = LoggerUtil.getLogger();
		final String errorCode = exception.getErrorCode();
		final String errorDescription = exception.getErrorDesc();
		String errorFieldParameter = null;
		String errorParametervalue = null;
		logger.info("Error code : " + errorCode + " Error Description : "
				+ errorDescription, correlationId);
		if (exception.getErrorFieldParameter() != null) {
			errorFieldParameter = exception.getErrorFieldParameter();
		}
		if (exception.getErrorParameterValue() != null) {
			errorParametervalue = exception.getErrorParameterValue();
		}
		final com.belk.api.error.handler.ResponseDetails error = new com.belk.api.error.handler.ResponseDetails(
				errorCode, errorDescription, errorFieldParameter,
				errorParametervalue);

		throw new WebApplicationException(this.buildErrorResponse(error,
				error.getClass(), exception.getHttpStatus(), responseType,
				correlationId).build());

	}

	/**
	 * Method to build the response object in the format requested if any
	 * exception is thrown
	 * 
	 * @param target
	 *            Final object to be built from the binding
	 * @param className
	 *            class name to be bound
	 * @param httpStatus
	 *            statusvalue of the http request built
	 * @param responseType
	 *            to be sent
	 * @param correlationId
	 *            to track the request
	 * @return ResponseObject The response object
	 */
	public final ResponseBuilder buildErrorResponse(final Object target,
			final Class<?> className, final String httpStatus,
			final String responseType, final String correlationId) {
		logger = LoggerUtil.getLogger();
		final long startTime = logger.logMethodEntry(correlationId);
		logger.info(" target : " + target + " ,className : " + className,
				correlationId);

		ResponseBuilder responseBuilder = null;
		byte[] responseByteArray;
		final int statusCode = Integer.parseInt(httpStatus);
		final Map<String, String> errorPropertiesMap = this.errorLoader
				.getErrorPropertiesMap();

		try {
			if (responseType.equalsIgnoreCase(CommonConstants.MEDIA_TYPE_XML)) {

				final IBindingFactory factory = BindingDirectory
						.getFactory(className);
				final ByteArrayOutputStream xmlOutput = new ByteArrayOutputStream();
				final BufferedOutputStream xmlBuffer = new BufferedOutputStream(
						xmlOutput);
				final IMarshallingContext context = factory
						.createMarshallingContext();
				context.marshalDocument(target, "UTF-8", null, xmlBuffer);
				responseByteArray = xmlOutput.toByteArray();

				responseBuilder = Response
						.status(statusCode)
						.entity(responseByteArray)
						.header(CommonConstants.CONTENT_TYPE,
								CommonConstants.CONTENT_TYPE_VALUE_XML)
								.header(CommonConstants.CORRELATION_ID, correlationId);
			} else if (responseType
					.equalsIgnoreCase(CommonConstants.MEDIA_TYPE_JSON)) {

				final String jsonResponse = this.jsonProcessor
						.buildJSONResponse(target, correlationId);
				responseByteArray = jsonResponse
						.getBytes(CommonConstants.UTF_TYPE);
				responseBuilder = Response
						.status(statusCode)
						.entity(responseByteArray)
						.header(CommonConstants.CONTENT_TYPE,
								CommonConstants.CONTENT_TYPE_VALUE_JSON)
								.header(CommonConstants.CORRELATION_ID, correlationId);
			}

		} catch (final JiBXException e) {
			logger.error(e, correlationId);
			responseBuilder = Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.entity(ErrorConstants.ERROR_DETAILS
							+ ErrorConstants.ERROR_CODE_11523
							+ ErrorConstants.ERROR_CODE
							+ errorPropertiesMap.get(String
									.valueOf(ErrorConstants.ERROR_CODE_11523))
									+ ErrorConstants.ERROR_DETAILS_END)
									.type(MediaType.APPLICATION_XML)
									.header(CommonConstants.CORRELATION_ID, correlationId);
		} catch (final UnsupportedEncodingException e) {
			logger.error(e, correlationId);
			responseBuilder = Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.entity(ErrorConstants.ERROR_DETAILS
							+ ErrorConstants.ERROR_CODE_11523
							+ ErrorConstants.ERROR_CODE
							+ errorPropertiesMap.get(String
									.valueOf(ErrorConstants.ERROR_CODE_11523))
									+ ErrorConstants.ERROR_DETAILS_END)
									.type(MediaType.APPLICATION_XML)
									.header(CommonConstants.CORRELATION_ID, correlationId);
		}
		logger.logMethodExit(startTime, correlationId);
		return responseBuilder;
	}

	/**
	 * Method to check the response type and give the response type.
	 * 
	 * @param uriMap
	 *            to check the response type is specified or not
	 * @param correlationId
	 *            to track the request
	 * @return String The response type
	 */
	public final String getResponseType(final Map<String, List<String>> uriMap,
			final String correlationId) {
		logger = LoggerUtil.getLogger();
		final long startTime = logger.logMethodEntry(correlationId);
		logger.debug("Input UriMap is:" + uriMap, correlationId);
		String responseType = null;
		if (uriMap.containsKey(CommonConstants.REQUEST_TYPE)) {
			responseType = StringUtils.collectionToCommaDelimitedString(uriMap
					.get(CommonConstants.REQUEST_TYPE));
			if (allowedTypesList.contains(responseType.toUpperCase())) {
				return responseType;
			} else {
				responseType = CommonConstants.MEDIA_TYPE_XML;
				return responseType;
			}
		} else {
			responseType = CommonConstants.MEDIA_TYPE_XML;
		}
		logger.debug("Return Value :" + responseType, correlationId);
		logger.logMethodExit(startTime, correlationId);
		return responseType;
	}

	/**
	 * Method to check the response type and give the response type.
	 * 
	 * @param uriMap
	 *            to check the response type is specified or not
	 * @param correlationId
	 *            to track the request
	 * @return String The response type
	 */
	public final String getResponseTypeForURIParam(
			final Map<String, String> uriMap, final String correlationId) {
		logger = LoggerUtil.getLogger();
		final long startTime = logger.logMethodEntry(correlationId);
		logger.debug("Input uriMap is :" + uriMap, correlationId);
		String responseType = null;
		if (uriMap.containsKey(CommonConstants.REQUEST_TYPE)) {
			responseType = uriMap.get(CommonConstants.REQUEST_TYPE);
			if (allowedTypesList.contains(responseType.toUpperCase())) {
				return responseType;
			} else {
				responseType = CommonConstants.MEDIA_TYPE_XML;
				return responseType;
			}
		} else {
			responseType = CommonConstants.MEDIA_TYPE_XML;
		}
		logger.debug("Return value :" + responseType, correlationId);
		logger.logMethodExit(startTime, correlationId);
		return responseType;
	}

	/**
	 * Method to fetch the correlation Id form the request header.
	 * 
	 * @param httpHeaders
	 *            request header
	 * @return String The correlation Id
	 */
	public final String getCorrelationId(final HttpHeaders httpHeaders) {
		logger = LoggerUtil.getLogger();
		String correlationId = CommonConstants.EMPTY_STRING;
		final MultivaluedMap<String, String> reqHeaders = httpHeaders
				.getRequestHeaders();
		if (reqHeaders.containsKey(CommonConstants.CORRELATION_ID)) {
			correlationId = reqHeaders.get(CommonConstants.CORRELATION_ID)
					.get(0).toString();
		}
		logger.debug("Correlation Id :" + correlationId, correlationId);
		return correlationId;
	}

	/**
	 * Validates the incoming request by authenticating the token from header
	 * 
	 * @param updateRequestUriMap
	 *            the uri map containing the request parameters
	 * @param correlationId
	 *            to track the request
	 * @return authentication status
	 * @throws BaseException
	 *             the exception thrown from this method.
	 */
	public final boolean validateRequest(
			final Map<String, List<String>> updateRequestUriMap,
			final String correlationId)
					throws BaseException {
		logger = LoggerUtil.getLogger();
		final long startTime = logger.logMethodEntry(correlationId);
		String encryptedAppId = null;
		String encryptedKeyParams = null;
		boolean isRequestValid = false;
		final Map<String, String> errorPropertiesMap = this.errorLoader
				.getErrorPropertiesMap();

		if (updateRequestUriMap.containsKey(CommonConstants.ENCRYPTED_APP_ID)) {
			encryptedAppId = updateRequestUriMap
					.get(CommonConstants.ENCRYPTED_APP_ID).get(0).toString();
		}

		if (updateRequestUriMap
				.containsKey(CommonConstants.ENCRYPTED_KEY_PARAMS)) {
			encryptedKeyParams = updateRequestUriMap
					.get(CommonConstants.ENCRYPTED_KEY_PARAMS).get(0)
					.toString();
		}
		if (null != encryptedAppId && null != encryptedKeyParams) {
			isRequestValid = this.securityUtils.validateToken(encryptedAppId,
					encryptedKeyParams, correlationId);
			if (!isRequestValid) {
				logger.info("Expiry exception thrown from BaseResource",
						correlationId);
				throw new BaseException(
						String.valueOf(ErrorConstants.ERROR_CODE_EXPIRED_TOKEN),
						errorPropertiesMap.get(String
								.valueOf(ErrorConstants.ERROR_CODE_EXPIRED_TOKEN)),
								ErrorConstants.HTTP_STATUS_CODE_UNAUTHORIZED,
								correlationId);
			}
		} else {
			throw new BaseException(
					String.valueOf(ErrorConstants.ERROR_CODE_11425),
					errorPropertiesMap.get(String
							.valueOf(ErrorConstants.ERROR_CODE_11425)),
							ErrorConstants.HTTP_STATUS_CODE_INVALID_URL_PARAMETERS,
							correlationId);
		}
		logger.debug("Is request valid :" + isRequestValid, correlationId);
		logger.logMethodExit(startTime, correlationId);
		return isRequestValid;
	}

}
