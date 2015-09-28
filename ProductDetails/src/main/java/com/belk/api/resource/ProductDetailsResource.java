package com.belk.api.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import org.springframework.util.StringUtils;

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
import com.belk.api.model.productdetails.ProductList;
import com.belk.api.service.ProductDetailsService;
import com.belk.api.validators.BaseValidator;

/**
 * This is a Resource class for the Product Details related to api service. This
 * class invokes the corresponding service layer and generates the response in
 * requested format which can be xml or json for the given identifier.
 * 
 * Update: This class has been updated to handle single log file per service, as
 * part of CR: CR_BELK_API_6 Updated : Added few comments and removed the
 * correlation logic and invoked some new methods as part of Phase2, April,14
 * release
 * 
 * @author Mindtree
 * @date Oct 10, 2013
 */
@Path("/v1/products")
public class ProductDetailsResource extends BaseResource {
	/**
	 * Creating logget instance.
	 */
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger(CommonConstants.PRODUCT_DETAILS_LOGGER);
	{
		LoggerUtil.setLogger(LOGGER);
	}

	/**
	 * searchMapper is to create instance of SearchMapper.
	 */
	@Autowired
	private ServiceManager serviceManager;
	/**
	 * creating instance of baseValidator.
	 */
	@Autowired
	private BaseValidator baseValidator;

	/**
	 * Default constructor.
	 */
	public ProductDetailsResource() {
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
	 * Get product details for a various identifiers or multiple comma separated
	 * values, It gives the product detail or list of product details for the
	 * given identifier.
	 * 
	 * @param httpHeaders
	 *            - httpheaders
	 * @param uriInfo
	 *            - input query parameters
	 * @return Product details in the requested format, default is xml if not
	 *         specified anything
	 */
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public final Response getProductDetails(
			@Context final HttpHeaders httpHeaders,
			@Context final UriInfo uriInfo) {
		// Getting the correlation id for the incoming request
		final String correlationId = getCorrelationId(httpHeaders);
		final long startTime = LOGGER.logMethodEntry(correlationId);
		Response productDetailsResponse = null;
		ProductList productList = null;

		Map<String, List<String>> productDetailsRequestUriMap = uriInfo
				.getQueryParameters();
		LOGGER.debug("ProductDetailsRequest URI Map is : "
				+ productDetailsRequestUriMap, correlationId);

		// this returns the requested response format.
		// if no response format is requested then by default its xml.
		final String responseType = getResponseType(
				productDetailsRequestUriMap, correlationId);

		try {
			if (productDetailsRequestUriMap
					.containsKey(CommonConstants.REQUEST_TYPE)) {
				productDetailsRequestUriMap
				.remove(CommonConstants.REQUEST_TYPE);
			}
			if (this.baseValidator.isBaseValidationRequired(
					CommonConstants.PRODUCT_DETAILS, correlationId)) {
				this.baseValidator.uriInfoPath = uriInfo.getPath();
				// This will validate all the input criteria for the given
				// request
				this.baseValidator.validate(productDetailsRequestUriMap,
						correlationId);
			}
			// This will splits the vendorPartNum and vendorNum separated with
			// delimiters into individual parameter values
			productDetailsRequestUriMap = this.populateVendorVpnRequest(
					productDetailsRequestUriMap, correlationId);

			// This will return an instance of ProductDetailsService
			final ProductDetailsService productDetailsService = (ProductDetailsService) this.serviceManager
					.getService();
			productList = productDetailsService.getProductDetails(
					productDetailsRequestUriMap, correlationId);
			// This will build the response in the requested format
			productDetailsResponse = buildResponse(productList,
					ProductList.class, DomainConstants.PRODUCT_DETAIL_BINDING,
					responseType, correlationId).build();

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
		return productDetailsResponse;
	}

	/**
	 * This method splits the request parameter values separated with delimiters
	 * into individual parameter values.
	 * 
	 * @param uriMap
	 *            - query parameters
	 * @param correlationId
	 *            to track the request
	 * @return requestUriMap map after the split
	 * @throws ValidationException
	 *             exception thrown from Resource layer
	 */
	private Map<String, List<String>> populateVendorVpnRequest(
			final Map<String, List<String>> uriMap, final String correlationId)
					throws ValidationException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final Map<String, List<String>> requestUriMap = new HashMap<String, List<String>>();
		final List<String> vendorPartNum = new ArrayList<String>();
		final List<String> vendorNum = new ArrayList<String>();
		final Iterator<Map.Entry<String, List<String>>> validateMap = uriMap
				.entrySet().iterator();

		while (validateMap.hasNext()) {
			final Entry<String, List<String>> identifierMap = validateMap
					.next();
			if (RequestAttributeConstant.VENDOR_VPN.equals(identifierMap
					.getKey())) {
				final String[] splitValues = StringUtils
						.collectionToCommaDelimitedString(
								identifierMap.getValue()).split(
										CommonConstants.COMMA_SEPERATOR);
				for (String value : splitValues) {
					final String[] tokens = value
							.split(CommonConstants.DOUBLE_BACK_SLASH
									+ CommonConstants.PIPE_SEPERATOR);
					if (tokens.length == 2
							&& tokens[0] != null
							&& !CommonConstants.EMPTY_STRING.equals(tokens[0]
									.trim())) {
						vendorPartNum.add(tokens[0]);
						vendorNum.add(tokens[1]);
					}
				}
				requestUriMap.put(RequestAttributeConstant.VENDOR_VPN,
						vendorPartNum);
				requestUriMap.put(RequestAttributeConstant.VENDOR_NUMBER,
						vendorNum);
			} else {
				requestUriMap.put(identifierMap.getKey(),
						identifierMap.getValue());
			}
		}

		LOGGER.debug(" Request URI Map " + requestUriMap, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return requestUriMap;
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

				final ProductDetailsService productDetailsService = (ProductDetailsService) this.serviceManager
						.getService();
				updateRequestUriMap.put(CommonConstants.SERVER_IP, serverIp);
				productDetailsService.updatePropertiesMap(correlationId,
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