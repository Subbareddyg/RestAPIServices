package com.belk.api.resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
import com.belk.api.constants.EndecaConstants;
import com.belk.api.constants.ErrorConstants;
import com.belk.api.constants.RequestAttributeConstant;
import com.belk.api.exception.AdapterException;
import com.belk.api.exception.BaseException;
import com.belk.api.exception.ServiceException;
import com.belk.api.exception.ValidationException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.belk.api.model.catalog.Catalog;
import com.belk.api.service.CatalogService;
import com.belk.api.util.EndecaLoader;
import com.belk.api.validators.BaseValidator;

/**
 * This is a Resource class related to Catalog api service. This class invokes
 * the corresponding service layer and generates the response in requested
 * format which can be xml or json for the given identifier.
 * 
 * Update: This class has been updated to handle single log file per service, as
 * part of CR: CR_BELK_API_6 Updated : Added few comments and removed the
 * correlation logic and invoked some new methods Update: This class has been
 * modified as part of performance improvement request to handle the case of
 * complete catalog by directly retrieving the complete catalog xml/json
 * response from the cache.
 * 
 
 * @date Nov 06, 2013
 */
@Path("/v1/catalog")
public class CatalogResource extends BaseResource {
	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Catalog");
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
	 * creating instance of EndecaLoader.
	 */
	@Autowired
	private EndecaLoader endecaLoader;

	/**
	 * Default Constructor.
	 */
	public CatalogResource() {
	}

	/**
	 * @param serviceManager
	 *            serviceManager to set
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
	 * @param endecaLoader
	 *            the endecaLoader to set
	 */
	public final void setEndecaLoader(final EndecaLoader endecaLoader) {
		this.endecaLoader = endecaLoader;
	}

	/**
	 * Get catalog details for the catalog identifier supplied along with the
	 * categories and the corresponding products.
	 * 
	 * @param catalogId
	 *            - catalog identifier supplied
	 * @param format
	 *            - response type
	 * @param headers
	 *            - request headers
	 * @return catalog details in the requested format, default is xml if not
	 *         specified anything
	 */
	@Path("/{catalogId}")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public final Response getCatalog(
			@PathParam("catalogId") final String catalogId,
			@DefaultValue("xml") @QueryParam("format") final String format,
			@Context final HttpHeaders headers) {

		// Getting the correlation id for the incoming request
		final String correlationId = getCorrelationId(headers);
		final long startTime = LOGGER.logMethodEntry(correlationId);
		Response catalogResponse = null;
		Map<String, String> catalogRequestUriMap = null;
		String catalog = null;
		String responseType = null;
		try {

			catalogRequestUriMap = new LinkedHashMap<String, String>();
			catalogRequestUriMap.put(CommonConstants.REQUEST_TYPE, format);
			// this returns the requested response format.
			// if no response format is requested then by default its xml.
			responseType = getResponseTypeForURIParam(catalogRequestUriMap,
					correlationId);

			this.validateCheckCatalogReqUriMap(this.getValidateURIRequestMap(
					uriInfo, catalogId, correlationId), correlationId);

			// method written to avoid the cyclomatic complexity
			this.populateUriMapWithCommonData(catalogId, catalogRequestUriMap,
					responseType);

			// Specified as TRUE Value to indicate that for this variant the
			// categories should be fetched from cache.
			catalogRequestUriMap.put(RequestAttributeConstant.SHOW_PRODUCT,
					CommonConstants.TRUE_VALUE);

			/*
			 * specifying to 'catalogHomeDimensionValue' with the dimension id
			 * of HOME appended with the response type, e.g.: for a
			 * catalogHomeDimensionValue value of 1234, the key that would be
			 * present in the cache is 1234_JSON and 1234_XML This needs to be
			 * changed to 'catalogRootDimensionValue' when the catalog API to be
			 * exposed to root the Dimension
			 */

			LOGGER.debug("Catalog Request Uri Map : " + catalogRequestUriMap,
					correlationId);
			// This will return an instance of CatalogService
			final CatalogService catalogService = (CatalogService) this.serviceManager
					.getService();
			catalog = catalogService.getCatalogAsString(catalogRequestUriMap,
					correlationId);
			String contentType = CommonConstants.CONTENT_TYPE_VALUE_XML;
			if (responseType.equalsIgnoreCase(CommonConstants.MEDIA_TYPE_JSON)) {
				contentType = CommonConstants.CONTENT_TYPE_VALUE_JSON;
			}
			// This will build the response in the requested format
			catalogResponse = Response.status(CommonConstants.STATUS_OK)
					.entity(catalog)
					.header(CommonConstants.CONTENT_TYPE, contentType)
					.header(CommonConstants.CORRELATION_ID, correlationId)
					.build();

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
		return catalogResponse;

	}

	/**
	 * Get catalog details for the catalog identifier supplied excluding
	 * products.
	 * 
	 * @param catalogId
	 *            - catalog identifier supplied
	 * @param format
	 *            - response type
	 * @param headers
	 *            - request headers
	 * @return catalog details in the requested format, default is xml if not
	 *         specified anything
	 */
	@Path("/{catalogId}/categories")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public final Response getCatalogWithAllCategories(
			@PathParam("catalogId") final String catalogId,
			@DefaultValue("xml") @QueryParam("format") final String format,
			@Context final HttpHeaders headers) {
		final String correlationId = getCorrelationId(headers);
		final long startTime = LOGGER.logMethodEntry(correlationId);
		Response response = null;
		Map<String, String> catalogRequestUriMap = null;
		Catalog catalog = null;
		String responseType = null;
		try {

			catalogRequestUriMap = new LinkedHashMap<String, String>();
			catalogRequestUriMap.put(CommonConstants.REQUEST_TYPE, format);
			responseType = getResponseTypeForURIParam(catalogRequestUriMap,
					correlationId);

			// method written to avoid the cyclomatic code complexity
			this.validateCheckCatalogReqUriMap(this.getValidateURIRequestMap(
					uriInfo, catalogId, correlationId), correlationId);
			// method written to avoid the cyclomatic complexity
			this.populateUriMapWithCommonData(catalogId, catalogRequestUriMap,
					responseType);

			/*
			 * Specified as FALSE Value to indicate that for this variant the
			 * categories should NOT be fetched from cache.
			 */
			catalogRequestUriMap.put(RequestAttributeConstant.SHOW_PRODUCT,
					CommonConstants.FALSE_VALUE);

			// specifying to 'catalogHomeDimensionValue' with the dimension id
			// of HOME.
			// This needs to be changed to 'catalogRootDimensionValue' when the
			// catalog
			// API to be exposed to root the Dimension
			catalogRequestUriMap.put(
					RequestAttributeConstant.CATEGORY_ID,
					this.endecaLoader.getEndecaPropertiesMap().get(
							EndecaConstants.CATALOG_HOME_DIMENSION_VALUE));

			LOGGER.debug("Catalog Request Uri Map is: " + catalogRequestUriMap,
					correlationId);

			final CatalogService catalogService = (CatalogService) this.serviceManager
					.getService();
			catalog = catalogService.getCatalog(catalogRequestUriMap,
					correlationId);

			response = buildResponse(catalog, Catalog.class,
					DomainConstants.CATALOG_BINDING, responseType,
					correlationId).build();

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
		return response;
	}

	/**
	 * This Method checks whether the validation switch is turned oFF or ON and <br >
	 * depending on the switch the URI map gets validated.
	 * 
	 * @param catalogValidateReqUriMap
	 *            -map containing the
	 * 
	 * @param correlationId
	 *            - to track the request
	 * 
	 * @throws BaseException
	 *             - returns the {@link BaseException} if any exception occurs
	 */
	private void validateCheckCatalogReqUriMap(
			final Map<String, List<String>> catalogValidateReqUriMap,
			final String correlationId) throws BaseException {
		if (this.baseValidator.isBaseValidationRequired(
				CommonConstants.CATALOG, correlationId)) {
			this.baseValidator.uriInfoPath = this.uriInfo.getPath();
			// This will validate all the input criteria for the given
			// request
			this.baseValidator
			.validate(catalogValidateReqUriMap, correlationId);
		}
	}

	/**
	 * Get catalog details with or without products based on the flag for the
	 * catalog identifier and category identifier supplied.
	 * 
	 * @param catalogId
	 *            - catalog identifier supplied
	 * @param categoryId
	 *            - category identifier supplied
	 * @param format
	 *            - response type
	 * @param showproduct
	 *            - flag
	 * @param headers
	 *            - request headers
	 * @return catalog details in the requested format, default is xml if not
	 *         specified anything
	 */
	@Path("/{catalogId}/category/{categoryId}")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public final Response getCatalogWithSpecificCategory(
			@PathParam("catalogId") final String catalogId,
			@PathParam("categoryId") final String categoryId,
			@QueryParam("showproduct") final String showproduct,
			@DefaultValue("xml") @QueryParam("format") final String format,
			@Context final HttpHeaders headers) {
		final String correlationId = getCorrelationId(headers);
		final long startTime = LOGGER.logMethodEntry(correlationId);
		Response response = null;
		Map<String, String> catalogRequestUriMap = null;
		String responseType = null;
		try {
			catalogRequestUriMap = new LinkedHashMap<String, String>();
			catalogRequestUriMap.put(CommonConstants.REQUEST_TYPE, format);
			responseType = getResponseTypeForURIParam(catalogRequestUriMap,
					correlationId);

			final Map<String, List<String>> catalogValidateReqUriMap = this
					.getValidateURIRequestMap(uriInfo, catalogId, correlationId);
			if (this.baseValidator.isBaseValidationRequired(
					CommonConstants.CATALOG, correlationId)) {
				this.baseValidator.uriInfoPath = uriInfo.getPath();
				// This will validate all the input criteria for the given
				// request
				this.baseValidator.validate(catalogValidateReqUriMap,
						correlationId);
			}
			// method written to avoid the cyclomatic complexity
			this.populateUriMapWithCommonData(catalogId, catalogRequestUriMap,
					responseType);

			/*
			 * Specified as TRUE Value to indicate that for this variant the
			 * categories should be fetched from cache.
			 */
			catalogRequestUriMap.put(RequestAttributeConstant.SHOW_PRODUCT,
					CommonConstants.TRUE_VALUE);
			catalogRequestUriMap.put(RequestAttributeConstant.CATEGORY_ID,
					categoryId);

			// checking for the showproduct flag from the URI
			if (null != showproduct
					&& !CommonConstants.EMPTY_STRING.equals(showproduct)) {
				catalogRequestUriMap.put(RequestAttributeConstant.SHOW_PRODUCT,
						showproduct.toLowerCase());
			}

			LOGGER.debug("Catalog Request Uri Map : " + catalogRequestUriMap,
					correlationId);

			final CatalogService catalogService = (CatalogService) this.serviceManager
					.getService();
			final Catalog catalog = catalogService.getCatalog(
					catalogRequestUriMap, correlationId);
			response = buildResponse(catalog, Catalog.class,
					DomainConstants.CATALOG_BINDING, responseType,
					correlationId).build();

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
		return response;
	}

	/**
	 * Method added to avoid cyclomatic code complexity.
	 * 
	 * @param catalogId
	 *            - catalog identifier supplied
	 * @param uriMap
	 *            the map to which the common request attributes are added
	 * @param responseType
	 *            The response type requested for.
	 */
	private void populateUriMapWithCommonData(final String catalogId,
			final Map<String, String> uriMap, final String responseType) {
		uriMap.put(RequestAttributeConstant.CATALOG_ID, catalogId);
		final Map<String, String> endecaPropertiesMap = this.endecaLoader
				.getEndecaPropertiesMap();
		uriMap.put(EndecaConstants.ROOT_DIMENSION_KEY, endecaPropertiesMap
				.get(EndecaConstants.CATALOG_ROOT_DIMENSION_VALUE));
		uriMap.put(CommonConstants.REQUEST_TYPE, responseType);
	}

	/**
	 * This method is to get URI request map from request parameters.
	 * 
	 * @param uriInfo
	 *            - input query parameters
	 * @param catalogId
	 *            - id for the catalog passed in the URL
	 * @param correlationId
	 *            - to track the request
	 * @return catalogValidateReqUriMap
	 */
	private Map<String, List<String>> getValidateURIRequestMap(
			final UriInfo uriInfo, final String catalogId,
			final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info(" uriInfo : " + uriInfo + " , catalogId : " + catalogId,
				correlationId);
		final Map<String, List<String>> catalogValidateReqUriMap = uriInfo
				.getQueryParameters();
		catalogValidateReqUriMap.put(RequestAttributeConstant.CATALOG_ID,
				Arrays.asList(catalogId));
		if (catalogValidateReqUriMap.containsKey(CommonConstants.REQUEST_TYPE)) {
			catalogValidateReqUriMap.remove(CommonConstants.REQUEST_TYPE);
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return catalogValidateReqUriMap;
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

				final CatalogService catalogService = (CatalogService) this.serviceManager
						.getService();
				updateRequestUriMap.put(CommonConstants.SERVER_IP, serverIp);
				catalogService.updatePropertiesMap(correlationId,
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
