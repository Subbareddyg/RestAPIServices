package com.belk.api.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;

import com.belk.api.business.service.manager.ServiceManager;
import com.belk.api.constants.CommonConstants;
import com.belk.api.constants.ErrorConstants;
import com.belk.api.exception.BaseException;
import com.belk.api.exception.ServiceException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.belk.api.security.AuthenticationDetails;
import com.belk.api.service.SecurityService;

/**
 * This is a resource class for the security related services. This class
 * invokes the corresponding service layer and returns the Authentication Token
 * in response.
 * 
 * @author Mindtree
 * @date 29 May, 2014
 */

@Path("/")
public class SecurityResource extends BaseResource {

	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger(CommonConstants.SECURITY_LOGGER);
	{
		LoggerUtil.setLogger(LOGGER);
	}

	/**
	 * searchMapper is to create instance of SearchMapper.
	 */
	@Autowired
	private ServiceManager serviceManager;


	/**
	 * @param serviceManager
	 *            the serviceManager to set
	 */
	public final void setServiceManager(final ServiceManager serviceManager) {
		this.serviceManager = serviceManager;
	}


	/**
	 * This method is called by the calling applications to request for an
	 * authentication token.
	 * 
	 * @param httpHeaders
	 *            the http headers
	 * @param uriInfo
	 *            the uri map
	 * @return response object in json or xml format
	 * @throws BaseException
	 *             exception thrown from this method
	 */
	@GET
	@Path("/v1/security")
	public final Response getAuthToken(@Context final HttpHeaders httpHeaders,
			@Context final UriInfo uriInfo) throws BaseException {
		final String correlationId = getCorrelationId(httpHeaders);
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final MultivaluedMap<String, String> reqHeaders = httpHeaders
				.getRequestHeaders();
		String encryptedAppId = null;
		String encryptedKeyParams = null;
		AuthenticationDetails authToken;
		Response authResponse = null;
		final Map<String, List<String>> securityRequestUriMap = uriInfo
				.getQueryParameters();
		LOGGER.debug("Security request uriMap : " + securityRequestUriMap,
				correlationId);
		final String responseType = getResponseType(securityRequestUriMap,
				correlationId);
		try {
			if (reqHeaders.containsKey(CommonConstants.ENCRYPTED_APP_ID)) {
				encryptedAppId = reqHeaders
						.get(CommonConstants.ENCRYPTED_APP_ID).get(0)
						.toString();
			}

			if (reqHeaders.containsKey(CommonConstants.ENCRYPTED_KEY_PARAMS)) {
				encryptedKeyParams = reqHeaders
						.get(CommonConstants.ENCRYPTED_KEY_PARAMS).get(0)
						.toString();
			}
			final SecurityService securityService = (SecurityService) this.serviceManager
					.getService();
			authToken = securityService.processRequest(encryptedAppId,
					encryptedKeyParams, correlationId);
			authResponse = buildResponse(authToken,
					AuthenticationDetails.class,
					CommonConstants.SECURITY_BINDING, responseType,
					correlationId).build();
		} catch (BaseException e) {
			LOGGER.error(ErrorConstants.ERRORDESC_MESSAGE, e, correlationId);
			processException(e, responseType, correlationId);
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return authResponse;
	}

	/**
	 * This method is to reload map of property file specified in request.
	 * 
	 * @param httpHeaders
	 *            - request headers
	 * @param uriInfo
	 *            - Map of URI parameters
	 * @return status of operation.
	 */
	@GET
	@Path("/v1/security/configuration")
	@Produces(MediaType.TEXT_PLAIN)
	public final Response updatePropertiesMap(
			@Context final HttpHeaders httpHeaders,
			@Context final UriInfo uriInfo) {
		final String correlationId = getCorrelationId(httpHeaders);
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final Map<String, List<String>> updateRequestUriMap = uriInfo
				.getQueryParameters();
		final String responseType = getResponseType(updateRequestUriMap,
				correlationId);

		try {
			// Validate the incoming request first
			if (validateRequest(updateRequestUriMap, correlationId)) {
				/*
				 * Removing the following entries as they are not required for
				 * processing in the service methods
				 */
				updateRequestUriMap.remove(CommonConstants.ENCRYPTED_APP_ID);
				updateRequestUriMap
				.remove(CommonConstants.ENCRYPTED_KEY_PARAMS);

				final List<String> serverIp = new ArrayList<String>();
				serverIp.add(uriInfo.getRequestUri().getHost());

				final SecurityService securityService = (SecurityService) this.serviceManager
						.getService();
				updateRequestUriMap.put(CommonConstants.SERVER_IP, serverIp);
				securityService.updatePropertiesMap(correlationId,
						updateRequestUriMap);

			}
		} catch (final ServiceException e) {
			// Log the Service Exception into Api Services Logs and ELF
			LOGGER.error(ErrorConstants.ERRORDESC_MESSAGE, e, correlationId);
			processException(e, responseType, correlationId);
		} catch (BaseException e) {
			LOGGER.error(ErrorConstants.ERRORDESC_MESSAGE, e, correlationId);
			processException(e, responseType, correlationId);
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return Response.status(Status.OK).entity(CommonConstants.SUCCESS)
				.build();
	}
}
