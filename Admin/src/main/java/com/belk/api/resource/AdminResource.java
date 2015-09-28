/**
 * 
 */
package com.belk.api.resource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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
import com.belk.api.constants.EndecaConstants;
import com.belk.api.constants.ErrorConstants;
import com.belk.api.constants.RequestAttributeConstant;
import com.belk.api.exception.AdapterException;
import com.belk.api.exception.BaseException;
import com.belk.api.exception.ServiceException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.belk.api.service.AdminService;
import com.belk.api.util.EndecaLoader;

/**
 * This class AdminService is used as a resource for the Admin services. The
 * class can have the resource providers for which admin functionalities are
 * required.
 * Updated : Added few comments and removed the correlation logic and invoked some new methods
 *  as part of Phase2, April,14 release
 * Update: This class has been updated to handle single log file per service, as
 * part of CR: CR_BELK_API_6
 * Update: updated code as part of Phase2 April,14 release
 * @author Mindtree
 * @date Dec 02, 2013
 * 
 */
@Component
@Path("/v1/admin")
public class AdminResource extends BaseResource {
	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger(CommonConstants.ADMIN_LOGGER);
	{
		LoggerUtil.setLogger(LOGGER);
	}


	/**
	 * creating instance of service manager.
	 */
	@Autowired
	private ServiceManager serviceManager;

	/**
	 * creating instance of endeca Loader.
	 */
	@Autowired
	private EndecaLoader endecaLoader;
	
	/**
	 * Default constructor.
	 */
	public AdminResource() {
	}

	/**
	 * @param serviceManager
	 *            serviceManager to set
	 */
	public final void setServiceManager(final ServiceManager serviceManager) {
		this.serviceManager = serviceManager;
	}



	/**
	 * @param endecaLoader the endecaLoader to set
	 */
	public final void setEndecaLoader(final EndecaLoader endecaLoader) {
		this.endecaLoader = endecaLoader;
	}

	/**
	 * This method serves as the resource for admin api. <br>
	 * This method initiates the daemon thread which would populate the products
	 * for the catalog API.
	 * 
	 * @param headers
	 *            - request headers
	 * @return catalog details in the requested format, default is xml if not
	 *         specified anything
	 */
	@Path("/catalog/")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public final Response populateCatalogProductsInCache(
			@Context final HttpHeaders headers) {
		// Getting the correlation id for the incoming request
		final String correlationId = getCorrelationId(headers);
		final long startTime = LOGGER.logMethodEntry(correlationId);
		Response adminResponse = null;
		String responseType = null;
		final Map<String, String> adminRequestUriMap = new LinkedHashMap<String, String>();
		adminRequestUriMap.put(RequestAttributeConstant.SHOW_PRODUCT,
				CommonConstants.TRUE_VALUE);
		final Map<String, String> endecaPropertiesMap = this.endecaLoader.getEndecaPropertiesMap();

		adminRequestUriMap.put(EndecaConstants.ROOT_DIMENSION_KEY,
				endecaPropertiesMap.get(EndecaConstants.CATALOG_ROOT_DIMENSION_VALUE));

		// specifying to HOME_DIMENSION_VALUE with the dimension id of HOME.
		// This needs to be changed to ROOT_DIMENSION_VALUE when the catalog
		// API to be exposed to root the Dimension
		adminRequestUriMap.put(RequestAttributeConstant.CATEGORY_ID,
				endecaPropertiesMap.get(EndecaConstants.CATALOG_HOME_DIMENSION_VALUE));

		LOGGER.debug("Admin Request Uri Map : " + adminRequestUriMap,
				correlationId);
		// this returns the requested response format.
		// if no response format is requested then by default its xml.
		responseType = getResponseTypeForURIParam(adminRequestUriMap,
				correlationId);

		// declaring and assigning again because the daemon thread requires all
		// final parameters.
		final String responseTypeAsync = responseType;
		final Map<String, String> asynchronousUriMap = adminRequestUriMap;
		final AdminService adminService = (AdminService) this.serviceManager
				.getService();
		final String threadCorrelationId = correlationId;

		// Asynchronous method call for the data fetch from backend
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					adminService.populateCategoriesInCache(asynchronousUriMap,
							threadCorrelationId);
				} catch (final ServiceException e) {
					LOGGER.error(e, threadCorrelationId);
					processException(e, responseTypeAsync, threadCorrelationId);
				} catch (final AdapterException e) {
					LOGGER.error(e, threadCorrelationId);
					processException(e, responseTypeAsync, threadCorrelationId);
				} catch (final BaseException e) {
					LOGGER.error(e, threadCorrelationId);
					processException(e, responseTypeAsync, threadCorrelationId);
				}
			}
		}).start();

		// The build object should be with the success status and not
		// with any specific response data
		adminResponse = Response.ok().build();
		LOGGER.logMethodExit(startTime, correlationId);
		return adminResponse;
	}

	/**
	 * This method is to reload the map of property file specified in request.
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
				final AdminService adminService = (AdminService) this.serviceManager
						.getService();
				updateRequestUriMap.put(CommonConstants.SERVER_IP, serverIp);
				adminService.updatePropertiesMap(correlationId,
						updateRequestUriMap);
			}
		} catch (final ServiceException e) {
			// Log the Service Exception into Api Services Logs and ELF
			LOGGER.error(ErrorConstants.ERRORDESC_MESSAGE, e , correlationId);
			processException(e, responseType, correlationId);
		} catch (BaseException e) {
			LOGGER.error(ErrorConstants.ERRORDESC_MESSAGE, e, correlationId);
			processException(e, responseType, correlationId);
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return Response.status(Status.OK).entity(CommonConstants.SUCCESS).build();
	}
	
	/**
	 * This method will trigger the Coremetrics Recommendation load process
	 * @param headers
	 * @return
	 */
	@GET
	@Path("/coremetrics/load")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public final Response loadCormetricsData(@Context final HttpHeaders headers) {
		// Getting the correlation id for the incoming request
		final String correlationId = getCorrelationId(headers);
		final long startTime = LOGGER.logMethodEntry(correlationId);
		Response coremetricsResponse = null;
		String responseType = null;
		// Since this an Admin API and will be executed only by the API layer, supported format is only JSON
		responseType = CommonConstants.MEDIA_TYPE_JSON;
		// declaring and assigning again because the daemon thread requires all
		// final parameters.
		final String responseTypeAsync = responseType;
		final AdminService adminService = (AdminService) this.serviceManager
				.getService();
		final String threadCorrelationId = correlationId;

		// Asynchronous method call for the data fetch from backend
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					adminService.loadCoremetricsData(threadCorrelationId);
				} catch (final ServiceException e) {
					LOGGER.error(e, threadCorrelationId);
					processException(e, responseTypeAsync, threadCorrelationId);
				} catch (final BaseException e) {
					LOGGER.error(e, threadCorrelationId);
					processException(e, responseTypeAsync, threadCorrelationId);
				}
			}
		}).start();

		// The build object should be with the success status and not
		// with any specific response data
		coremetricsResponse = Response.ok().build();
		LOGGER.logMethodExit(startTime, correlationId);
		return coremetricsResponse;
	}
}
