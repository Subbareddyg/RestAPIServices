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
import com.belk.api.model.productsearch.Search;
import com.belk.api.service.ProductSearchService;
import com.belk.api.validators.BaseValidator;

/**
 * This is a Resource class for the Product search related service. This class
 * invokes the corresponding service layer and generates the response in
 * requested format which can be xml or json.
 * 
 * Update: This class has been updated to handle single log file per service, as
 * part of CR: CR_BELK_API_6
 * 
 * Updated : Added few comments and changed the name of variables as part of
 * Phase2, April,14 release
 * 
 * Updated : Changes for Validation Framework,Enterprise Logging Framework and
 * Config Framework
 * 
 * as a part of Phase 2,April,14 Release
 * 
 * @author Mindtree
 * @date Jul 26, 2013
 */

@Path("/v1/products/search")
public class ProductSearchResource extends BaseResource {

	/**
	 * Creating logger instance.
	 */
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger(CommonConstants.PRODUCT_SEARCH_LOGGER);
	{
		LoggerUtil.setLogger(LOGGER);
	}

	/**
	 * searchMapper is to create instance of SearchMapper.
	 */
	@Autowired
	private ServiceManager serviceManager;

	/**
	 * baseValidator is to create instance of BaseValidator.
	 */
	@Autowired
	private BaseValidator baseValidator;

	/**
	 * Default Constructor.
	 */
	public ProductSearchResource() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param serviceManager
	 *            the serviceManager to set
	 */
	public final void setServiceManager(final ServiceManager serviceManager) {
		this.serviceManager = serviceManager;
	}

	/**
	 * @param baseValidator
	 *            the baseValidator to set
	 */
	public final void setBaseValidatorValidator(
			final BaseValidator baseValidator) {
		this.baseValidator = baseValidator;
	}

	/**
	 * Method to return a list of products based on the search criteria for the
	 * given input parameters.
	 * 
	 * @param httpHeaders
	 *            - httpheaders
	 * @param uriInfo
	 *            Map of URI parameters
	 * @return an xml/json representation of a list of products
	 */
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public final Response searchProducts(
			@Context final HttpHeaders httpHeaders,
			@Context final UriInfo uriInfo) {
		// Getting the correlation id for the incoming request
		final String correlationId = getCorrelationId(httpHeaders);
		final long startTime = LOGGER.logMethodEntry(correlationId);
		Response productSearchResponse = null;
		final Map<String, List<String>> productSearchrequestUriMap = uriInfo
				.getQueryParameters();
		LOGGER.debug("Product search request uriMap : "
				+ productSearchrequestUriMap, correlationId);
		// this returns the requested response format.
		// if no response format is requested then by default its xml.
		final String responseType = getResponseType(productSearchrequestUriMap,
				correlationId);
		try {
			if (productSearchrequestUriMap
					.containsKey(CommonConstants.REQUEST_TYPE)) {
				productSearchrequestUriMap.remove(CommonConstants.REQUEST_TYPE);
			}
			if (this.baseValidator.isBaseValidationRequired(
					CommonConstants.PRODUCT_SEARCH, correlationId)) {
				this.baseValidator.uriInfoPath = uriInfo.getPath();
				// This will validate all the input criteria for the given
				// request
				this.baseValidator.validate(productSearchrequestUriMap,
						correlationId);
			}
			// This will return an instance of ProductSearchService
			final ProductSearchService productSearchService = (ProductSearchService) this.serviceManager
					.getService();
			Search searchResult = null;
			searchResult = productSearchService.searchProducts(
					productSearchrequestUriMap, correlationId);
			// This will build the response in the requested format
			productSearchResponse = buildResponse(searchResult, Search.class,
					DomainConstants.PRODUCT_SEARCH_BINDING, responseType,
					correlationId).build();
		} catch (AdapterException e) {
			// Log the Adapter Exception into Api Services Logs and ELF
			LOGGER.error(ErrorConstants.ERRORDESC_MESSAGE, e, correlationId);
			processException(e, responseType, correlationId);
		} catch (ServiceException e) {
			// Log the Service Exception into Api Services Logs and ELF
			LOGGER.error(ErrorConstants.ERRORDESC_MESSAGE, e, correlationId);
			processException(e, responseType, correlationId);
		} catch (BaseException e) {
			// Log the Base Exception into Api Services Logs and ELF
			LOGGER.error(ErrorConstants.ERRORDESC_MESSAGE, e, correlationId);
			processException(e, responseType, correlationId);
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return productSearchResponse;
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

				final boolean isBaseValidatorUpdated = this.baseValidator
						.updateValidationConfigurations(updateRequestUriMap,
								correlationId);
				if (isBaseValidatorUpdated) {
					return Response.status(Status.OK)
							.entity(CommonConstants.SUCCESS).build();
				}

				final ProductSearchService productSearchService = (ProductSearchService) this.serviceManager
						.getService();
				updateRequestUriMap.put(CommonConstants.SERVER_IP, serverIp);
				productSearchService.updatePropertiesMap(correlationId,
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
