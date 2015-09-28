/**
 * 
 */
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
import com.belk.api.service.ProductDataService;
import com.belk.api.validators.BaseValidator;

/**
 * This is the resource class for the Coremetric API. This serves the requests
 * for getting the recommended products for a particular product whose id is
 * passed in the request.
 * 
 * @author Mindtree
 * @date Oct 29, 2014
 * 
 */

@Path("/v1/products/recommendations")
public class Recommendations extends BaseResource {

	/**
	 * Creating logger instance.
	 */
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger(CommonConstants.COREMETRICS_LOGGER);
	{
		LoggerUtil.setLogger(LOGGER);
	}

	/**
	 * searchMapper is to create instance of SearchMapper.
	 */
	/*
	 * @Autowired private ServiceManager serviceManager;
	 */
	/**
	 * creating instance of baseValidator.
	 */
	@Autowired
	private BaseValidator baseValidator;

	@Autowired
	private ProductDataService productDataService;

	/**
	 * Default constructor
	 */
	public Recommendations() {

	}

	/**
	 * @param serviceManager
	 *            the serviceManager to set
	 */
	/*
	 * public final void setServiceManager(final ServiceManager serviceManager)
	 * { this.serviceManager = serviceManager; }
	 */

	/**
	 * @param baseValidator
	 *            the baseValidator to set
	 */
	public final void setBaseValidator(final BaseValidator baseValidator) {
		this.baseValidator = baseValidator;
	}

	/**
	 * @param productDataService
	 *            the productDataService to set
	 */
	public final void setProductDataService(final ProductDataService productDataService) {
		this.productDataService = productDataService;
	}

	/**
	 * This method gets product details along with the Coremetrics recommended
	 * products list.
	 * 
	 * @param httpHeaders
	 *            - httpheaders
	 * @param uriInfo
	 *            - input query parameters
	 * @return Product details in the requested format, default is xml if not
	 *         specified anything
	 */
	@GET
	@Path("/coremetrics")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public final Response getCoremetricsRecommendedProductList(
			@Context final HttpHeaders httpHeaders,
			@Context final UriInfo uriInfo) {
		final String correlationId = getCorrelationId(httpHeaders);
		final long startTime = LOGGER.logMethodEntry(correlationId);
		Response productDataResponse = null;
		ProductList productData = null;

		final Map<String, List<String>> productDataRequestUriMap = uriInfo
				.getQueryParameters();
		LOGGER.debug("ProductDetailsRequest URI Map is : "
				+ productDataRequestUriMap, correlationId);

		// this returns the requested response format.
		// if no response format is requested then by default its xml.
		final String responseType = getResponseType(productDataRequestUriMap,
				correlationId);
		List<String> target = null;

		try {
			if (productDataRequestUriMap
					.containsKey(CommonConstants.REQUEST_TYPE)) {
				productDataRequestUriMap.remove(CommonConstants.REQUEST_TYPE);
			}

			if (productDataRequestUriMap.containsKey(RequestAttributeConstant.TARGET)) {
				target = productDataRequestUriMap
								.get(RequestAttributeConstant.TARGET);
				productDataRequestUriMap.remove(RequestAttributeConstant.TARGET);
			}
			if (this.baseValidator.isBaseValidationRequired(
					CommonConstants.COREMETRICS, correlationId)) {
				this.baseValidator.uriInfoPath = uriInfo.getPath();
				// This will validate all the input criteria for the given
				// request
				this.baseValidator.validate(productDataRequestUriMap,
						correlationId);
			}

			productDataRequestUriMap.put(RequestAttributeConstant.TARGET, target);
			productData = this.productDataService
					.getCoremetricsRecommendedProductList(
							productDataRequestUriMap, correlationId);
			// This will build the response in the requested format
			productDataResponse = buildResponse(productData, ProductList.class,
					DomainConstants.PRODUCT_DETAIL_BINDING, responseType,
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
		return productDataResponse;

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

				
				updateRequestUriMap.put(CommonConstants.SERVER_IP, serverIp);
				this.productDataService.updatePropertiesMap(correlationId,
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
