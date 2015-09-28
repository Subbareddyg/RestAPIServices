/**
 * 
 */
package com.belk.configui.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.belk.api.constants.CommonConstants;
import com.belk.api.constants.ErrorConstants;
import com.belk.api.exception.BaseException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.belk.api.security.SecurityUtil;
import com.belk.api.util.CommonUtil;
import com.belk.configui.constants.ConfigUIConstants;

/**
 * Custom handler to handle operations to be done after a successful
 * authentication.
 * 
 * @author Mindtree
 * @date 6 Jun, 2014
 */
public class LoginAuthenticationSuccessHandler implements
AuthenticationSuccessHandler {
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger(CommonConstants.CONFIGURATION_WEB_APP_LOGGER);
	{
		LoggerUtil.setLogger(LOGGER);
	}

	@Autowired
	SecurityUtil securityUtil;

	final ResourceBundle bundle = ResourceBundle
			.getBundle(CommonConstants.CONFIGURATION_UI_RESOURCE_BUNDLE);
	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();


	/**
	 * @param securityUtil
	 *            the securityUtils to set
	 */
	public final void setSecurityUtils(final SecurityUtil securityUtil) {
		this.securityUtil = securityUtil;
	}

	@Override
	public final void onAuthenticationSuccess(final HttpServletRequest request,
			final HttpServletResponse response,
			final Authentication authentication)
					throws IOException, ServletException {
		final String correlationId = ConfigUIConstants.BLANK;
		String authTokenFormatted = null;
		String authToken = null;
		try {
			authTokenFormatted = this.sendGet(authentication, correlationId);
			if (authTokenFormatted != null) {
				final String encryptedAuthToken = CommonUtil.parseXML(
						authTokenFormatted,
						CommonConstants.AUTH_TOKEN_ELEMENT_STRING);
				authToken = this.securityUtil.decrypt(encryptedAuthToken,
						this.bundle
						.getString(ConfigUIConstants.CONFIG_UI_ENCRYPTION_KEY),
						correlationId);

				final HttpSession session = request.getSession();
				final String loggedInUser = ((User) authentication.getPrincipal())
						.getUsername();
				session.setAttribute(ConfigUIConstants.LOGGED_IN_USER_NAME,
						loggedInUser);
				session.setAttribute(ConfigUIConstants.SESSION_AUTH_TOKEN,
						authToken);
				if (response.isCommitted()) {
					LOGGER.debug(
							"Response has already been committed. Unable to redirect to "
									+ ConfigUIConstants.GET_FILE_LIST,
									correlationId);
					return;
				}

				this.redirectStrategy.sendRedirect(request, response, "/"
						+ ConfigUIConstants.GET_FILE_LIST);
			} else {
				this.redirectStrategy.sendRedirect(request, response, "/"
						+ ConfigUIConstants.LOGIN);
			}
		} catch (BaseException e) {
			LOGGER.error(e, correlationId);
			return;
		}

	}

	/**
	 * This method gets the authentication token from the Security API.
	 * 
	 * @param authentication
	 *            the authentication object obtained during log in to the
	 *            application.
	 * @param correlationId
	 *            ID to track the request.
	 * @return authentication token obtained from the Security API.
	 * @throws BaseException
	 *             Exception thrown from this method if an error happens.
	 */
	private String sendGet(final Authentication authentication,
			final String correlationId)
					throws BaseException {

		/* TODO: need to change this to reading from property file instead */
		final String endpointUrl = this.bundle
				.getString(ConfigUIConstants.SECURITY_API_ENDPOINT);

		final String appId = this.bundle
				.getString(ConfigUIConstants.CONFIG_UI_APP_ID);
		String response = null;
		try {
			final String loggedInUser = ((User) authentication.getPrincipal())
					.getUsername();
			final String currentTimeStamp = String
					.valueOf(System.currentTimeMillis());
			final String encryptedParams = this.securityUtil.encrypt(appId
					+ CommonConstants.FIELD_PAIR_SEPARATOR + loggedInUser
					+ CommonConstants.FIELD_PAIR_SEPARATOR + currentTimeStamp,
					this.bundle
					.getString(ConfigUIConstants.CONFIG_UI_ENCRYPTION_KEY),
					correlationId);

			final URL url = new URL(endpointUrl);
			final HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			// optional default is GET
			connection.setRequestMethod(CommonConstants.GET_REQUEST);

			// add request header
			connection.addRequestProperty(CommonConstants.ENCRYPTED_APP_ID,
					this.bundle
					.getString(ConfigUIConstants.CONFIG_UI_APP_ID_ENCRYPTED));
			connection.addRequestProperty(CommonConstants.ENCRYPTED_KEY_PARAMS,
					encryptedParams);

			final int responseCode = connection.getResponseCode();

			if (responseCode == 200) {
				final BufferedReader incomingBufferedReader = new BufferedReader(
						new InputStreamReader(connection.getInputStream()));
				String inputLine;
				final StringBuffer responseBuffer = new StringBuffer();

				while ((inputLine = incomingBufferedReader.readLine()) != null) {
					responseBuffer.append(inputLine);
				}
				incomingBufferedReader.close();
				response = responseBuffer.toString();
			} else {
				throw new BaseException(
						ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
						correlationId);
			}

		} catch (MalformedURLException error) {
			LOGGER.error(error, correlationId);
		} catch (ProtocolException error) {
			LOGGER.error(error, correlationId);
		} catch (BaseException error) {
			LOGGER.error(error, correlationId);
		} catch (IOException error) {
			LOGGER.error(error, correlationId);
		}

		return response;

	}
}
