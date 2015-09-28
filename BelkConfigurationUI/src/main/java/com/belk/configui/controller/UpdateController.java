package com.belk.configui.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.belk.api.constants.CommonConstants;
import com.belk.api.exception.BaseException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.belk.api.security.SecurityUtil;
import com.belk.configui.constants.ConfigUIConstants;
import com.belk.configui.utils.ConfigurationLoaderUtil;

/**
 * @author Mindtree
 * @date Mar 25, 2014 This is the controller that contains the methods related
 *       to the Update feature in the application.
 */
@Controller
public class UpdateController {

	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger(CommonConstants.CONFIGURATION_WEB_APP_LOGGER);
	{
		LoggerUtil.setLogger(LOGGER);
	}

	@Autowired
	SecurityUtil securityUtils;

	@Autowired
	ConfigurationLoaderUtil configLoaderUtil;

	final ResourceBundle bundle = ResourceBundle
			.getBundle(CommonConstants.CONFIGURATION_UI_RESOURCE_BUNDLE);

	/*
	 * Commented out as validation framework related changes are not required as
	 * of now
	 */
	/*
	 * @Autowired(required=true) UpdateValidationRules validationRules;
	 */

	/**
	 * @param securityUtils
	 *            the securityUtils to set
	 */
	public final void setSecurityUtils(final SecurityUtil securityUtils) {
		this.securityUtils = securityUtils;
	}

	/**
	 * @param configLoaderUtil
	 *            the configLoaderUtil to set
	 */
	public final void setConfigLoaderUtil(
			final ConfigurationLoaderUtil configLoaderUtil) {
		this.configLoaderUtil = configLoaderUtil;
	}

	/**
	 * The method to handle the request for proceeding to update.
	 * 
	 * @param httprequest
	 *            The HTTP Request object
	 * @param model
	 *            The Model object
	 * @return Name of the view to be displayed
	 */
	@RequestMapping(value = ConfigUIConstants.PROCEED, method = RequestMethod.POST)
	public final String proceedToUpdate(final HttpServletRequest httprequest,
			final Model model) {
		String updatedBy = CommonConstants.EMPTY_STRING;
		final HttpSession session = httprequest.getSession();
		if (null != session.getAttribute(ConfigUIConstants.LOGGED_IN_USER_NAME)) {
			updatedBy = (String) session
					.getAttribute(ConfigUIConstants.LOGGED_IN_USER_NAME);
		}

		final Map<String, List<String>> apiList = new HashMap<String, List<String>>();
		String filePath = null;
		String propertyList = null;
		String propertyListString = CommonConstants.EMPTY_STRING;
		// String moduleName = null;
		String fileName = null;
		String[] filePathBits = null;
		String logLevel = null;
		String reloadChoice = null;

		if (null != httprequest.getParameter(ConfigUIConstants.FILE_PATH)) {
			filePath = httprequest.getParameter(ConfigUIConstants.FILE_PATH);
		}
		if (null != httprequest.getParameter(ConfigUIConstants.PROPERTIES)) {
			propertyList = httprequest
					.getParameter(ConfigUIConstants.PROPERTIES);
		}
		if (null != httprequest.getParameter(ConfigUIConstants.LOG_LEVEL)) {
			logLevel = httprequest.getParameter(ConfigUIConstants.LOG_LEVEL);
			fileName = ConfigUIConstants.LOG;
		}
		if (null != httprequest.getParameter(ConfigUIConstants.RELOAD_CHOICE)) {
			reloadChoice = httprequest
					.getParameter(ConfigUIConstants.RELOAD_CHOICE);
		}

		if (null != filePath) {
			filePathBits = filePath.split(ConfigUIConstants.SLASH);
			/*
			 * The following line is to extract the file name coming in the
			 * request by reading the penultimate part of the file path
			 */
			fileName = filePathBits[filePathBits.length - 1];
			// moduleName = filePathBits[filePathBits.length - 2];

			propertyListString = this.populatePropertyListString(propertyList);
		}

		/*
		 * To check the property file belongs to which Module and display APIs
		 * accordingly
		 */
		this.selectAPIsForDisplay(model, apiList, fileName, propertyListString,
				logLevel, updatedBy, reloadChoice);

		model.addAttribute(ConfigUIConstants.FILE_PATH, filePath);
		model.addAttribute(ConfigUIConstants.FILE_NAME, fileName);
		model.addAttribute(ConfigUIConstants.PROP_LIST, propertyListString);
		model.addAttribute(ConfigUIConstants.LOG_LEVEL, logLevel);

		model.addAttribute(CommonConstants.ENCRYPTED_APP_ID, this.bundle
				.getString(ConfigUIConstants.CONFIG_UI_APP_ID_ENCRYPTED));
		model.addAttribute(CommonConstants.ENCRYPTED_KEY_PARAMS,
				this.getEncryptedParamsForHeader(httprequest));
		return ConfigUIConstants.API_UPDATE;
	}

	/**
	 * This method returns the required parameters for the header, encrypted
	 * with the application specific EK.
	 * 
	 * @param httprequest
	 *            the http request
	 * @return an encrypted representation of the parameters for the header
	 */
	private String getEncryptedParamsForHeader(
			final HttpServletRequest httprequest) {
		final String correlationId = ConfigUIConstants.BLANK;
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final HttpSession session = httprequest.getSession();
		final String authToken = (String) session
				.getAttribute(ConfigUIConstants.SESSION_AUTH_TOKEN);
		String encryptedParams = null;

		final String loggedInUser = (String) session
				.getAttribute(ConfigUIConstants.LOGGED_IN_USER_NAME);
		try {
			encryptedParams = this.securityUtils
					.encrypt(
							this.bundle
									.getString(ConfigUIConstants.CONFIG_UI_APP_ID)
									+ CommonConstants.FIELD_PAIR_SEPARATOR
									+ loggedInUser
									+ CommonConstants.FIELD_PAIR_SEPARATOR
									+ System.currentTimeMillis()
									+ CommonConstants.FIELD_PAIR_SEPARATOR
									+ authToken,
							this.bundle
									.getString(ConfigUIConstants.CONFIG_UI_ENCRYPTION_KEY),
							"");
			/*
			 * TODO: to replace above "" with actual correlation ID for config
			 * UI
			 */
		} catch (BaseException e) {
			LOGGER.error(e, correlationId);
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return encryptedParams;
	}

	/**
	 * Method to select modules to be displayed for updation.
	 * 
	 * @param model
	 *            The Model object
	 * @param apiList
	 *            The list of APIs to be updated for
	 * @param fileName
	 *            The file name to be displayed the modules for
	 * @param propertyListString
	 *            The string representation of the properties changed.
	 * @param logLevel
	 *            The log level to be updated as per the request
	 * @param logLevel
	 * @param propertyListString
	 * @param updatedBy
	 *            The username who updates the configuration
	 * @param reloadChoice
	 *            The value that signifies whether a full reload is required or
	 *            not.
	 * 
	 */
	private void selectAPIsForDisplay(final Model model,
			final Map<String, List<String>> apiList, final String fileName,
			final String propertyListString, final String logLevel,
			final String updatedBy, final String reloadChoice) {
		if (fileName.equalsIgnoreCase(ConfigUIConstants.ENDECA_CONF)
				|| fileName
						.equalsIgnoreCase(ConfigUIConstants.ENDECA_FIELD_LIST_CONF)
				|| fileName.equalsIgnoreCase(ConfigUIConstants.LOG)) {
			this.selectURLsForUpdate(apiList,
					ConfigUIConstants.PRODUCT_DETAILS, fileName,
					propertyListString, logLevel, updatedBy, reloadChoice);
			this.selectURLsForUpdate(apiList, ConfigUIConstants.PRODUCT_SEARCH,
					fileName, propertyListString, logLevel, updatedBy,
					reloadChoice);
			this.selectURLsForUpdate(apiList, ConfigUIConstants.CATEGORY_PROD,
					fileName, propertyListString, logLevel, updatedBy,
					reloadChoice);
			this.selectURLsForUpdate(apiList, ConfigUIConstants.CATEGORY_DETL,
					fileName, propertyListString, logLevel, updatedBy,
					reloadChoice);

			if (fileName.equalsIgnoreCase(ConfigUIConstants.LOG)) {
				this.selectURLsForUpdate(apiList,
						ConfigUIConstants.PATTERN_PROD_DETL, fileName,
						propertyListString, logLevel, updatedBy, reloadChoice);
			}
			this.selectURLsForUpdate(apiList, ConfigUIConstants.CATALOG,
					fileName, propertyListString, logLevel, updatedBy,
					reloadChoice);
			this.selectURLsForUpdate(apiList, ConfigUIConstants.ADMIN,
					fileName, propertyListString, logLevel, updatedBy,
					reloadChoice);
			this.selectURLsForUpdate(apiList,
					ConfigUIConstants.PRODUCT_DATA, fileName,
					propertyListString, logLevel, updatedBy, reloadChoice);
			
			model.addAttribute(ConfigUIConstants.API_NAMES, apiList);
		} else if (fileName
				.equalsIgnoreCase(ConfigUIConstants.HTTP_CONNECTION_CONF)) {
			this.selectURLsForUpdate(apiList,
					ConfigUIConstants.PATTERN_PROD_DETL, fileName,
					propertyListString, logLevel, updatedBy, reloadChoice);
			model.addAttribute(ConfigUIConstants.API_NAMES, apiList);
		}
	}

	/**
	 * Method to select the URLs to be called for update.
	 * 
	 * @param apiList
	 *            List of APIs to be populated in the page
	 * @param apiName
	 *            Name of the API for which URLs are being populated
	 * @param fileName
	 *            Name of the file for which update is requested
	 * @param propertyListString
	 *            String representation of the list of properties to be updated
	 * @param logLevel
	 *            The log level to be changed
	 * @param updatedBy
	 *            The username who updates the configuration
	 * @param reloadChoice
	 *            The value that signifies whether a full reload is required or
	 *            not.
	 */
	private void selectURLsForUpdate(final Map<String, List<String>> apiList,
			final String apiName, final String fileName,
			final String propertyListString, final String logLevel,
			final String updatedBy, final String reloadChoice) {
		final Map<String, String> baseUrlMap = this.configLoaderUtil
				.getBaseUrlMap();
		String finalUrl = null;
		String commonUrl = null;
		final List<String> urlsPerApi = new ArrayList<String>();
		if (null != fileName && !fileName.equals(CommonConstants.EMPTY_STRING)
				&& !fileName.equalsIgnoreCase(ConfigUIConstants.LOG)) {
			if (!propertyListString.equals(CommonConstants.EMPTY_STRING)) {
				commonUrl = "/configuration?filename=" + fileName
						+ "&propertieslist=" + propertyListString;
			} else if (null != reloadChoice
					&& reloadChoice
							.equalsIgnoreCase(CommonConstants.TRUE_VALUE)) {
				commonUrl = "/configuration?filename=" + fileName
						+ CommonConstants.AMPERSAND
						+ CommonConstants.RELOAD_DEFAULT_VALUES
						+ CommonConstants.EQUALS_SIGN
						+ CommonConstants.TRUE_VALUE;
			}

		} else if (null != logLevel && !"-1".equals(logLevel)) {
			commonUrl = "/configuration?loglevel=" + logLevel;
		}
		for (Entry urlSet : baseUrlMap.entrySet()) {
			final String environmentName = (String) urlSet.getKey();
			final String baseUrl = (String) urlSet.getValue();

			finalUrl = this.populateURLs(apiName, commonUrl, baseUrl);
			finalUrl = finalUrl + "&updatedby=" + updatedBy;
			urlsPerApi.add(environmentName + "~" + finalUrl);

		}

		apiList.put(apiName, urlsPerApi);
	}

	/**
	 * Private method to avoid cyclomatic complexity while populating URLs.
	 * 
	 * @param apiName
	 *            Name of the API for which URLs are being populated
	 * @param commonUrl
	 *            The common URL to be used
	 * @param baseUrl
	 *            The base URL to be built upon
	 * @return formatted final URL string
	 */
	private String populateURLs(final String apiName, final String commonUrl,
			final String baseUrl) {
		String finalUrl = null;
		if (apiName.equalsIgnoreCase(ConfigUIConstants.PRODUCT_SEARCH)) {
			finalUrl = baseUrl + ConfigUIConstants.PRODUCT_SEARCH_URL
					+ commonUrl;

		} else if (apiName.equalsIgnoreCase(ConfigUIConstants.PRODUCT_DETAILS)) {
			finalUrl = baseUrl + ConfigUIConstants.PRODUCT_DETAILS_URL
					+ commonUrl;

		} else if (apiName.equalsIgnoreCase(ConfigUIConstants.CATEGORY_DETL)) {
			finalUrl = baseUrl + ConfigUIConstants.CATEGORY_DETAILS_URL
					+ commonUrl;

		} else if (apiName.equalsIgnoreCase(ConfigUIConstants.CATEGORY_PROD)) {
			finalUrl = baseUrl + ConfigUIConstants.CATEGORY_PRODUCT_URL
					+ commonUrl;

		} else if (apiName
				.equalsIgnoreCase(ConfigUIConstants.PATTERN_PROD_DETL)) {
			finalUrl = baseUrl + ConfigUIConstants.PATTERN_PRODUCTS_URL
					+ commonUrl;

		} else if (apiName.equalsIgnoreCase(ConfigUIConstants.CATALOG)) {
			finalUrl = baseUrl + ConfigUIConstants.CATALOG_URL + commonUrl;

		} else if (apiName.equalsIgnoreCase(ConfigUIConstants.ADMIN)) {
			finalUrl = baseUrl + ConfigUIConstants.ADMIN_URL + commonUrl;
		} else if (apiName.equalsIgnoreCase(ConfigUIConstants.PRODUCT_DATA)) {
			finalUrl = baseUrl + ConfigUIConstants.PRODUCT_DATA_URL + commonUrl;
		}
		return finalUrl;
	}

	/**
	 * Method to populate the property list string.
	 * 
	 * @param propertyList
	 *            String representation of properties received in request
	 * @return Parsed string representation of properties
	 */
	private String populatePropertyListString(final String propertyList) {
		// To separate the Properties list
		final String[] tempPropertyList = propertyList
				.split(ConfigUIConstants.COMMA);
		String propertyListString = ConfigUIConstants.BLANK;
		// To form the parameter list format key1:value1|key2:value2|
		for (int i = 0; i < tempPropertyList.length; i++) {
			final String[] paramBits = tempPropertyList[i]
					.split(ConfigUIConstants.EQUALS);
			if (paramBits.length == 2) {
				final String key = paramBits[0];
				final String value = paramBits[1];
				propertyListString = propertyListString + key
						+ ConfigUIConstants.COLON + value
						+ ConfigUIConstants.PIPE_LINE;
			}
		}
		return propertyListString;
	}

	/*
	 * Commented out as validation framework related changes are not required as
	 * of now
	 */
	/*
	 * @RequestMapping(value = ConfigUIConstants.PROCEED_VALIDATION, method =
	 * RequestMethod.POST) public String
	 * proceedToUpdateValidation(HttpServletRequest httprequest, Model model) {
	 * String filePath = httprequest.getParameter(ConfigUIConstants.FILE_PATH);
	 * String content = httprequest.getParameter(ConfigUIConstants.CONTENT);
	 * validationRules.writeFile(filePath, content);
	 * 
	 * List<String> apiList = new ArrayList<String>();
	 * apiList.add(ConfigUIConstants.PRODUCT_DETAILS);
	 * apiList.add(ConfigUIConstants.PRODUCT_SEARCH);
	 * apiList.add(ConfigUIConstants.CATEGORY_PROD);
	 * apiList.add(ConfigUIConstants.CATEGORY_DETL);
	 * apiList.add(ConfigUIConstants.PATTERN_PROD_DETL);
	 * model.addAttribute(ConfigUIConstants.API_NAMES, apiList); return
	 * ConfigUIConstants.VALIDATION_API_UPDATE; }
	 */

}
