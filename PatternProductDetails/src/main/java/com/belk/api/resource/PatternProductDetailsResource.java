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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.belk.api.business.service.manager.ServiceManager;
import com.belk.api.constants.CommonConstants;
import com.belk.api.constants.DomainConstants;
import com.belk.api.constants.ErrorConstants;
import com.belk.api.exception.AdapterException;
import com.belk.api.exception.BaseException;
import com.belk.api.exception.ServiceException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.belk.api.model.productdetails.ProductList;
import com.belk.api.service.PatternProductDetailsService;

/**
 * This is a Resource class for the Pattern Product Details api service. This
 * class invokes the corresponding service layer and generates the response in
 * requested format which can be xml or json for the given identifier.
 * 
 * Update: This API has been introduced as part of change request:
 * CR_BELK_API_3.
 * 
 * Update: This class has been updated to handle single log file per service, as
 * part of CR: CR_BELK_API_6
 * 
 * Update: This class has been updated to fix the review comments and to add
 * additional fields, as part of CR: April 2014
 * 
 * @author Mindtree
 * @date Nov 10th, 2013
 */
@Path("/v1/childpattern")
@Component
public class PatternProductDetailsResource extends BaseResource {
	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger(CommonConstants.PATTERN_PRODUCT_DETAILS_LOGGER);
	{
		LoggerUtil.setLogger(LOGGER);
	}
	/**
	 * creating instance of ServiceManager.
	 */
	@Autowired
	private ServiceManager serviceManager;

	/**
	 * Default Constructor.
	 */
	public PatternProductDetailsResource() {
	}

	/**
	 * @param serviceManager
	 *            the serviceManager to set
	 */
	public final void setServiceManager(final ServiceManager serviceManager) {
		this.serviceManager = serviceManager;
	}

	/**
	 * Get pattern product details from blue martini for a various identifiers
	 * or multiple comma separated values. It gives the pattern product details
	 * for the given identifier.
	 * 
	 * @param uriInfo
	 *            - input query parameters
	 * @param httpHeaders
	 *            - headers
	 * @return Product details in the requested format, default is xml if not
	 *         specified anything
	 * 
	 */

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public final Response getPatternProductDetails(
			@Context final HttpHeaders httpHeaders,
			@Context final UriInfo uriInfo) {
		Response patternProductDetailsResponse = null;
		Map<String, List<String>> patternProductRequestURIMap = null;
		ProductList patternProductDetailsList = null;
		// Fetching the correlationId from the httpHeader as set by GateWay
		final String correlationId = getCorrelationId(httpHeaders);
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(CommonConstants.CORRELATION_ID + " : " + correlationId,
				correlationId);

		patternProductRequestURIMap = uriInfo.getQueryParameters();
		LOGGER.debug("patternProductRequest Uri Map : "
				+ patternProductRequestURIMap, correlationId);
		final String responseType = getResponseType(
				uriInfo.getQueryParameters(), correlationId);
		// Changing the 'productCode' from the patternProductRequestURIMap to
		// the 'prdCode' for the
		// understanding of reference JSP
		if (patternProductRequestURIMap
				.containsKey(CommonConstants.PRODUCT_CODE_REQUEST_MAP_APIURL)) {
			patternProductRequestURIMap
			.put(CommonConstants.PRODUCT_CODE_REQUEST_MAP_BLUEMARTINI,
					patternProductRequestURIMap
					.get(CommonConstants.PRODUCT_CODE_REQUEST_MAP_APIURL));
			patternProductRequestURIMap
			.remove(CommonConstants.PRODUCT_CODE_REQUEST_MAP_APIURL);
		}

		try {
			// Calling Service method
			final PatternProductDetailsService patternProductDetailsService = (PatternProductDetailsService) this.serviceManager
					.getService();
			// Calling getPatternProductDetails method to get the pattern
			// product details list
			patternProductDetailsList = patternProductDetailsService
					.getPatternProductDetails(patternProductRequestURIMap,
							correlationId);
			// Building the response
			patternProductDetailsResponse = buildResponse(
					patternProductDetailsList, ProductList.class,
					DomainConstants.PRODUCT_DETAIL_BINDING, responseType,
					correlationId).build();

		} catch (final AdapterException e) {
			// Log the Adapter Exception into Api Services Logs and ELF
			LOGGER.error(ErrorConstants.ERRORDESC_MESSAGE, e, correlationId);
			processException(e, responseType, correlationId);

		} catch (final BaseException e) {
			// Log the Base Exception into Api Services Logs and ELF
			LOGGER.error(ErrorConstants.ERRORDESC_MESSAGE, e, correlationId);
			processException(e, responseType, correlationId);
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return patternProductDetailsResponse;
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
	@Path("/configuration")
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
			if (validateRequest(updateRequestUriMap, correlationId)) {
				updateRequestUriMap.remove(CommonConstants.ENCRYPTED_APP_ID);
				updateRequestUriMap
				.remove(CommonConstants.ENCRYPTED_KEY_PARAMS);
				final List<String> serverIp = new ArrayList<String>();
				serverIp.add(uriInfo.getRequestUri().getHost());
				final PatternProductDetailsService patternProductDetailsService =
						(PatternProductDetailsService) this.serviceManager
						.getService();
				updateRequestUriMap.put(CommonConstants.SERVER_IP, serverIp);
				patternProductDetailsService.updatePropertiesMap(correlationId,
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