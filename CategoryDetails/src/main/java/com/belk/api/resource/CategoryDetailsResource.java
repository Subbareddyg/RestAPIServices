package com.belk.api.resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
import com.belk.api.constants.RequestAttributeConstant;
import com.belk.api.exception.AdapterException;
import com.belk.api.exception.BaseException;
import com.belk.api.exception.ServiceException;
import com.belk.api.exception.ValidationException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.belk.api.model.categorydetails.Categories;
import com.belk.api.service.CategoryDetailsService;
import com.belk.api.validators.BaseValidator;

/**
 * This is a Resource class for the Category Details related to api service.
 * This class invokes the corresponding service layer and generates the response
 * in requested format which can be xml or json for the given identifier.
 * 
 * Update: This class has been updated to handle single log file per service, as
 * part of CR: CR_BELK_API_6
 *  Updated : Added few comments and removed the correlation logic and invoked some new methods
 *  as part of Phase2, April,14 release
 * @author Mindtree
 * @date Nov 06, 2013
 */
@Path("/v1/categories")
public class CategoryDetailsResource extends BaseResource {
	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger(CommonConstants.CATEGORY_DETAILS_LOGGER);
	{
		LoggerUtil.setLogger(LOGGER);
	}

	/**
	 * creating instance of service manager.
	 */
	@Autowired
	private ServiceManager serviceManager;

	/**
	 * creating instance of baseValidator.
	 */
	@Autowired
	private BaseValidator baseValidator;

	/**
	 * Default Constructor.
	 */
	public CategoryDetailsResource() {
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
	public final void setBaseValidator(final BaseValidator baseValidator) {
		this.baseValidator = baseValidator;
	}

	/**
	 * The method to return the high level Category details associated to one or
	 * more comma separated identifier values.
	 * 
	 * @param httpHeaders
	 *            - input httpheaders
	 * @param categoryId
	 *            - input categoryids
	 * @param uriInfo
	 *            - input query parameters
	 * @return response
	 */
	@GET
	@Path("/{categoryid}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public final Response getCategoryDetails(
			@Context final HttpHeaders httpHeaders,
			@PathParam("categoryid") final String categoryId,
			@Context final UriInfo uriInfo) {
		// Getting the correlation id for the incoming request
		final String correlationId = getCorrelationId(httpHeaders);
		final long startTime = LOGGER.logMethodEntry(correlationId);
		Map<String, List<String>> categoryDetailsRequestUriMap = null;
		String responseType = null;
		Response categoryDetailsResponse = null;
		Categories categories = null;

		categoryDetailsRequestUriMap = uriInfo.getQueryParameters();
		LOGGER.debug("CategoryDetailsRequest Uri Map is: " + categoryDetailsRequestUriMap, correlationId);
		// this returns the requested response format.
		// if no response format is requested then by default its xml.
		responseType = getResponseType(categoryDetailsRequestUriMap, correlationId);

		try {
			if (categoryDetailsRequestUriMap
					.containsKey(CommonConstants.REQUEST_TYPE)) {
				categoryDetailsRequestUriMap
				.remove(CommonConstants.REQUEST_TYPE);
			}
			if (this.baseValidator
					.isBaseValidationRequired(CommonConstants.CATEGORY_DETAILS, correlationId)) {
				this.baseValidator.uriInfoPath = uriInfo.getPath();
				// This will validate all the input criteria for the given
				// request
				this.baseValidator.validate(categoryDetailsRequestUriMap,
						correlationId);
			}
			categoryDetailsRequestUriMap.put(RequestAttributeConstant.CATEGORY_ID,
					Arrays.asList(categoryId));
			if (categoryDetailsRequestUriMap
					.containsKey(RequestAttributeConstant.CATALOG_ID)) {
				categoryDetailsRequestUriMap
				.remove(RequestAttributeConstant.CATALOG_ID);
			}
			// This will return an instance of CategoryDetailsService
			final CategoryDetailsService categoryDetailsService = (CategoryDetailsService) this.serviceManager
					.getService();
			categories = categoryDetailsService.getCategoryDetails(categoryDetailsRequestUriMap,
					correlationId);
			// This will build the response in the requested format
			categoryDetailsResponse = buildResponse(categories, Categories.class,
					DomainConstants.CATEGORY_DETAILS_BINDING, responseType,
					correlationId).build();
		} catch (ValidationException e) {
			// Log the Validation Exception into Api Services Logs and ELF
			LOGGER.error(ErrorConstants.ERRORDESC_MESSAGE, e, correlationId);
			processException(e, responseType, correlationId);
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
		return categoryDetailsResponse;
	}

	/**
	 * The method to return the high level Category details with sub category
	 * details associated to requested one or more comma separated identifier
	 * values.
	 * 
	 * @param httpHeaders
	 *            HTTP header object
	 * @param categoryId
	 *            categoryId from the request
	 * @param uriInfo
	 *            Request parameters
	 * @return response
	 */
	@GET
	@Path("/{categoryId}/subcategories")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public final Response getSubCategoryDetails(
			@Context final HttpHeaders httpHeaders,
			@PathParam("categoryId") final String categoryId,
			@Context final UriInfo uriInfo) {
		final String correlationId = getCorrelationId(httpHeaders);
		final long startTime = LOGGER.logMethodEntry(correlationId);
		Map<String, List<String>> subCategoryDetailsRequesturiMap = null;
		String responseType = null;
		Response subCategoryDetailsResponse = null;
		Categories categories = null;
		subCategoryDetailsRequesturiMap = uriInfo.getQueryParameters();
		LOGGER.debug("Sub Category Details Request Uri Map : " + subCategoryDetailsRequesturiMap, correlationId);
		// this returns the requested response format.
		// if no response format is requested then by default its xml.
		responseType = getResponseType(subCategoryDetailsRequesturiMap, correlationId);

		try {
			if (subCategoryDetailsRequesturiMap
					.containsKey(CommonConstants.REQUEST_TYPE)) {
				subCategoryDetailsRequesturiMap
				.remove(CommonConstants.REQUEST_TYPE);
			}
			if (this.baseValidator
					.isBaseValidationRequired(CommonConstants.CATEGORY_DETAILS, correlationId)) {
				this.baseValidator.uriInfoPath = uriInfo.getPath();
				// This will validate all the input criteria for the given
				// request
				this.baseValidator.validate(subCategoryDetailsRequesturiMap,
						correlationId);
			}
			subCategoryDetailsRequesturiMap.put(RequestAttributeConstant.CATEGORY_ID,
					Arrays.asList(categoryId));
			if (subCategoryDetailsRequesturiMap
					.containsKey(RequestAttributeConstant.CATALOG_ID)) {
				subCategoryDetailsRequesturiMap
				.remove(RequestAttributeConstant.CATALOG_ID);
			}
			subCategoryDetailsRequesturiMap.put(CommonConstants.SUB_CATEGORY, new ArrayList<String>());
			// This will return an instance of CategoryDetailsService
			final CategoryDetailsService categoryDetailsService = (CategoryDetailsService) this.serviceManager
					.getService();
			categories = categoryDetailsService.getCategoryDetails(subCategoryDetailsRequesturiMap,
					correlationId);
			// This will build the response in the requested format
			subCategoryDetailsResponse = buildResponse(categories, Categories.class,
					DomainConstants.CATEGORY_DETAILS_BINDING, responseType,
					correlationId).build();
		} catch (ValidationException e) {
			// Log the Validation Exception into Api Services Logs and ELF
			LOGGER.error(ErrorConstants.ERRORDESC_MESSAGE, e, correlationId);
			processException(e, responseType, correlationId);
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
		return subCategoryDetailsResponse;

	}

	/**
	 * This method is to reload map of property file specified in request.
	 * 
	 * @param httpHeaders
	 *            - request headers
	 * @param uriInfo
	 * 			 - Map of URI parameters
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
		final String responseType = getResponseType(updateRequestUriMap, correlationId);
		try {
			if (validateRequest(updateRequestUriMap, correlationId)) {
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
				final CategoryDetailsService categoryDetailsService = (CategoryDetailsService) this.serviceManager
						.getService();
				updateRequestUriMap.put(CommonConstants.SERVER_IP, serverIp);
				categoryDetailsService.updatePropertiesMap(correlationId,
						updateRequestUriMap);
			}
		} catch (final ServiceException e) {
			// Log the Service Exception into Api Services Logs and ELF
			LOGGER.error(ErrorConstants.ERRORDESC_MESSAGE, e, correlationId);
			processException(e, responseType, correlationId);
		} catch (BaseException baseException) {
			LOGGER.error(ErrorConstants.ERRORDESC_MESSAGE, baseException,
					correlationId);
			processException(baseException, responseType, correlationId);
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return Response.status(Status.OK).entity(CommonConstants.SUCCESS).build();
	}
}