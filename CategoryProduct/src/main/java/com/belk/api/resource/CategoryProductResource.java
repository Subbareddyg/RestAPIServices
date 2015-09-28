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
import org.springframework.stereotype.Component;

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
import com.belk.api.model.categoryproduct.Categories;
import com.belk.api.service.CategoryProductService;
import com.belk.api.validators.BaseValidator;

/**
 * This is a Resource class for the Category Product service. This class invokes
 * the corresponding service layer methods and generates the response in
 * requested format which can be xml or json.
 * 
 * Update: This class has been updated to handle single log file per service, as
 * part of CR: CR_BELK_API_6
 * 
 * Updated : Added few comments and removed the correlation logic and invoked
 * some new methods as part of Phase2, April,14 release
 * 
 * @author Mindtree
 * @date Oct 10, 2013
 */

@Path("/v1/categories")
@Component
public class CategoryProductResource extends BaseResource {

	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger(CommonConstants.CATEGORY_PRODUCT_LOGGER);
	{
		LoggerUtil.setLogger(LOGGER);
	}

	/**
	 * creating instance of service manager.
	 */
	@Autowired
	ServiceManager serviceManager;

	/**
	 * creating instance of baseValidator.
	 */
	@Autowired
	private BaseValidator baseValidator;

	/**
	 * Default Constructor.
	 */
	public CategoryProductResource() {
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
	 * The method to return the high level product details associated to the
	 * requested category. The details returned comprise of category Id,
	 * category name and parent category Id for the given category(s) along with
	 * the products present in the category.
	 * 
	 * @param httpHeaders
	 *            HTTP header object
	 * @param categoryId
	 *            categoryId from the request
	 * @param uriInfo
	 *            Request parameters
	 * @return Category Products in the requested format, default is xml if not
	 *         specified otherwise.
	 */
	@Path("/{categoryid}/products")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public final Response getCategoryProducts(
			@Context final HttpHeaders httpHeaders,
			@PathParam("categoryid") final String categoryId,
			@Context final UriInfo uriInfo) {
		// Getting the correlation id for the incoming request
		final String correlationId = getCorrelationId(httpHeaders);
		final long startTime = LOGGER.logMethodEntry(correlationId);
		Response categoryProductResponse = null;
		Categories categoryProducts = null;
		LOGGER.debug(CommonConstants.CORRELATION_ID, correlationId);
		// To get the query parameters. this should be get query parameters.
		final Map<String, List<String>> categoryProductRequestUriMap = uriInfo
				.getQueryParameters();
		// this returns the requested response format.
		// if no response format is requested then by default its xml.
		final String responseType = getResponseType(
				uriInfo.getQueryParameters(), correlationId);

		try {
			if (categoryProductRequestUriMap
					.containsKey(CommonConstants.REQUEST_TYPE)) {
				categoryProductRequestUriMap
				.remove(CommonConstants.REQUEST_TYPE);
			}
			if (this.baseValidator.isBaseValidationRequired(
					CommonConstants.CATEGORY_PRODUCT, correlationId)) {
				this.baseValidator.uriInfoPath = uriInfo.getPath();
				// This will validate all the input criteria for the given
				// request
				this.baseValidator.validate(categoryProductRequestUriMap,
						correlationId);
			}
			// Placing the requested categoryIds into the map as a value that
			// the Returns a fixed-size
			// list backed by the specified array
			categoryProductRequestUriMap.put(
					RequestAttributeConstant.CATEGORY_ID,
					Arrays.asList(categoryId));
			if (categoryProductRequestUriMap
					.containsKey(RequestAttributeConstant.CATALOG_ID)) {
				categoryProductRequestUriMap
				.remove(RequestAttributeConstant.CATALOG_ID);
			}
			// This will return an instance of CategoryProductService
			final CategoryProductService categoryProductService = (CategoryProductService) this.serviceManager
					.getService();
			// To get the categoryProducts using the requestedMap and
			// correlationId
			categoryProducts = categoryProductService.getCategoryProducts(
					categoryProductRequestUriMap, correlationId);
			// This will build the response in the requested format
			categoryProductResponse = buildResponse(categoryProducts,
					Categories.class, DomainConstants.CATEGORY_PRODUCT_BINDING,
					responseType, correlationId).build();

		} catch (final ValidationException e) {
			// Log the Validation Exception into Api Services Logs and ELF
			LOGGER.error(ErrorConstants.ERRORDESC_MESSAGE, e, correlationId);
			processException(e, responseType, correlationId);
		} catch (final AdapterException e) {
			// Log the Adapter Exception into Api Services Logs and ELF
			LOGGER.error(ErrorConstants.ERRORDESC_MESSAGE, e, correlationId);
			processException(e, responseType, correlationId);
		} catch (final ServiceException e) {
			// Log the Service Exception into Api Services Logs and ELF
			LOGGER.error(ErrorConstants.ERRORDESC_MESSAGE, e, correlationId);
			processException(e, responseType, correlationId);
		} catch (final BaseException e) {
			// Log the Base Exception into Api Services Logs and ELF
			LOGGER.error(ErrorConstants.ERRORDESC_MESSAGE, e, correlationId);
			processException(e, responseType, correlationId);
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return categoryProductResponse;
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
				final boolean isBaseValidatorUpdated = this.baseValidator
						.updateValidationConfigurations(updateRequestUriMap,
								correlationId);
				if (isBaseValidatorUpdated) {
					return Response.status(Status.OK)
							.entity(CommonConstants.SUCCESS).build();
				}
				final CategoryProductService categoryProductService = (CategoryProductService) this.serviceManager
						.getService();
				updateRequestUriMap.put(CommonConstants.SERVER_IP, serverIp);
				categoryProductService.updatePropertiesMap(correlationId,
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
		return Response.status(Status.OK).entity(CommonConstants.SUCCESS)
				.build();
	}

}
