package com.belk.api.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.belk.api.constants.CommonConstants;
import com.belk.api.constants.ErrorConstants;
import com.belk.api.exception.BaseException;
import com.belk.api.exception.ServiceException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.mail.MailClient;
import com.belk.api.security.AuthenticationDetails;
import com.belk.api.security.SecurityUtil;
import com.belk.api.service.SecurityService;
import com.belk.api.util.ErrorLoader;
import com.belk.api.util.PropertyReloader;

/**
 * The implementation class for the security services.
 * 
 * @author Mindtree
 * @date 29 May, 2014
 */
@Service
public class SecurityServiceImpl implements SecurityService {

	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	@Autowired
	SecurityUtil securityUtil;

	/**
	 * creating instance of Error Loader class.
	 */
	@Autowired
	ErrorLoader errorLoader;

	/**
	 * creating instance of Property Reloader class.
	 */
	@Autowired
	PropertyReloader propertyReloader;

	/**
	 * mailClient is the utility class for sending mails.
	 */
	@Autowired
	MailClient mailClient;

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
		this.securityUtil = securityUtils;
	}

	/**
	 * @param propertyReloader
	 *            the propertyReloader to set
	 */
	public final void setPropertyReloader(
			final PropertyReloader propertyReloader) {
		this.propertyReloader = propertyReloader;
	}

	/**
	 * @param mailClient
	 *            the mailClient to set
	 */
	public final void setMailClient(final MailClient mailClient) {
		this.mailClient = mailClient;
	}

	@Override
	public final AuthenticationDetails processRequest(
			final String encryptedAppId, final String encryptedKeyParams,
			final String correlationId)
					throws BaseException, ServiceException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		String appId = null;
		String appSpecificEK = null;
		String appKeyParams = null;
		String authToken = null;
		String cacheKey = null;
		String cacheValue = null;
		AuthenticationDetails authenticationDetails = null;
		final Map<String, String> errorPropertiesMap = this.errorLoader
				.getErrorPropertiesMap();

		try {

			/*
			 * The request can be served only if both the encrypted app ID and
			 * the key params are present, otherwise an exception has to be
			 * thrown
			 */
			if (null != encryptedAppId && null != encryptedKeyParams) {

				appId = this.securityUtil.getCallingApplicationId(
						encryptedAppId, correlationId);
				LOGGER.debug("Calling app id: " + appId, correlationId);
				appSpecificEK = this.securityUtil.getAppSpecificEncryptionKey(
						appId, correlationId);
				appKeyParams = this.securityUtil.decrypt(encryptedKeyParams,
						appSpecificEK, correlationId);
				LOGGER.debug("Request key params: " + appKeyParams,
						correlationId);

				/*
				 * The following lines of code get the timestamp part out of the
				 * appKeyParams string which is in the format CA:UID:TS where CA -
				 * calling application id, UID - logged in user id, TS - timestamp
				 * at which the request was generated
				 */
				final String[] appKeyParamsArray = appKeyParams
						.split(CommonConstants.FIELD_PAIR_SEPARATOR);
				final long requestTimestamp = Long
						.parseLong(appKeyParamsArray[appKeyParamsArray.length - 1]);

				/*
				 * The following code checks whether the incoming request has been
				 * made within the acceptable time interval. If yes, it requests for
				 * a token to be generated and saves this token in the memcached
				 * with the key as CA:UID, the abbreviations that have been
				 * explained above
				 */
				if (this.securityUtil.isRequestAlive(requestTimestamp,
						correlationId)) {
					authToken = this.securityUtil.generateAuthToken(
							appKeyParams, correlationId);
					cacheKey = appKeyParams.split(CommonConstants.FIELD_PAIR_SEPARATOR)[0]
							+ CommonConstants.FIELD_PAIR_SEPARATOR
							+ appKeyParams.split(CommonConstants.FIELD_PAIR_SEPARATOR)[1];
					cacheValue = authToken;
					this.securityUtil.storeAuthTokenInCache(cacheKey,
							cacheValue, correlationId);
				} else {
					/*
					 * The service exception is thrown if the request is made beyond
					 * the acceptable interval
					 */
					throw new ServiceException(
							String.valueOf(ErrorConstants.ERROR_CODE_EXPIRED_REQUEST),
							errorPropertiesMap.get(String
									.valueOf(ErrorConstants.ERROR_CODE_EXPIRED_REQUEST)),
									ErrorConstants.HTTP_STATUS_CODE_UNAUTHORIZED,
									correlationId);
				}
			} else {
				/*
				 * The service exception is thrown if the request doesn't have
				 * both the encrypted app id and the encrypted key params
				 */
				throw new ServiceException(
						String.valueOf(ErrorConstants.ERROR_CODE_11425),
						errorPropertiesMap.get(String
								.valueOf(ErrorConstants.ERROR_CODE_11425)),
								ErrorConstants.HTTP_STATUS_CODE_INVALID_URL_PARAMETERS,
								correlationId);
			}

		} catch (NumberFormatException | NullPointerException e) {
			/*
			 * Thrown if the timestamp value in the incoming string is incorrect
			 * or if a null pointer exception occurs
			 */
			LOGGER.error(e, correlationId);
			throw new ServiceException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String
							.valueOf(ErrorConstants.ERROR_CODE_11523)),
							ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
							correlationId);
		}
		/*
		 * The auth token is returned encrypted with the app specific EK to
		 * ensure that no eavesdropper can make out even what the actual auth
		 * token is
		 */
		authenticationDetails = new AuthenticationDetails(
				this.securityUtil.encrypt(authToken, appSpecificEK,
						correlationId));
		LOGGER.logMethodExit(startTime, correlationId);
		return authenticationDetails;
	}

	@Override
	public final void updatePropertiesMap(final String correlationId,
			final Map<String, List<String>> updateRequestUriMap)
					throws ServiceException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final Map<String, String> errorPropertiesMap = this.errorLoader
				.getErrorPropertiesMap();
		try {
			this.propertyReloader.updatePropertyMap(updateRequestUriMap,
					correlationId);
			this.mailClient.sendEmail(updateRequestUriMap, correlationId,
					CommonConstants.SUCCESS);
		} catch (BaseException e) {
			this.mailClient.sendEmail(updateRequestUriMap, correlationId,
					CommonConstants.FAILURE);
			throw new ServiceException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String
							.valueOf(ErrorConstants.ERROR_CODE_11523)),
							ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
							correlationId);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

}
