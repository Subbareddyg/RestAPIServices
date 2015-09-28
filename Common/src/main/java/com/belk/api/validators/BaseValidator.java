package com.belk.api.validators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.belk.api.constants.CommonConstants;
import com.belk.api.constants.ErrorConstants;
import com.belk.api.constants.RequestConstants;
import com.belk.api.exception.BaseException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.util.ErrorLoader;

/**
 * This class contains methods for validation.
 * 
 * @author Mindtree
 * 
 */
@Service
public class BaseValidator implements Validator {

	/**
	 * uriInfoPath of each API.
	 * 
	 */
	public static String uriInfoPath;

	/**
	 * Creating logger instance.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	/**
	 * creating instance of Error Loader class.
	 */
	@Autowired
	ErrorLoader errorLoader;
	/**
	 * Service Path Specific to API Service
	 */
	private String servicePath;

	/**
	 * private instance of ResourceValidationConfig class.
	 */
	private ResourceValidationConfig resourceValidationConfig;


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
	 * Method validates the incoming request url.
	 * 
	 * @param uriMap
	 *            - query parameters
	 * @param correlationId
	 *            to track the request
	 * @throws BaseException
	 *             The general exception from Adapter layer
	 */
	@Override
	public final void validate(final Map<String, List<String>> uriMap,
			final String correlationId) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("uriMap : " + uriMap, correlationId);
		final Map<String, String> errorPropertiesMap = this.errorLoader.getErrorPropertiesMap();
		if (this.servicePath == null) {
			this.updateValidatorRules(uriInfoPath, correlationId);
		}
		if (this.isResourceValidationConfigUsable()) {
			return;
		}

		this.executeMandatoryFieldValidation(uriMap, correlationId);

		ErrorResponse errorResponse = null;
		for (Map.Entry<String, List<String>> entry : uriMap.entrySet()) {
			if (this.resourceValidationConfig.getParameterValidationMap()
					.containsKey(entry.getKey())) {

				errorResponse = this.validateParameterValues(entry.getValue(),
						entry.getKey(), correlationId);
				if (errorResponse != null) {

					throw this.getBaseException(
							errorResponse,
							correlationId,
							ErrorConstants.HTTP_STATUS_CODE_INVALID_URL_PARAMETERS);
				}
			} else {
				throw new BaseException(errorPropertiesMap.get(String
						.valueOf(ErrorConstants.ERROR_CODE_1235)),
						ValidatorConstant.PARAMETER_CHECK_MESSAGE, entry.getKey(),
						CommonConstants.EMPTY_STRING,
						ErrorConstants.HTTP_STATUS_CODE_INVALID_URL_PARAMETERS,
						correlationId);
			}
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return;

	}
	/** Checks if ResourceValidationConfig is in a usable condition or not
	 * @return if ResourceValidationConfig is in a usable condition or not 
	 */
	private boolean isResourceValidationConfigUsable() {
		return this.resourceValidationConfig == null
				|| !this.resourceValidationConfig.isValidateResourceEnabled()
				|| this.resourceValidationConfig.getParameterValidationMap() == null
				|| this.resourceValidationConfig.getParameterValidationMap()
				.isEmpty();
	}

	/**
	 * Method which validates the request key and value.
	 * 
	 * @param key
	 *            - String containing key.
	 * @param validationCode
	 *            - validation code
	 * @param value
	 *            - String to be validated
	 * @param correlationId
	 *            - to track the request param.
	 * @param validationRules
	 *            - validation rules for each key
	 * @return ErrorResponse instance
	 * @throws BaseException
	 *             - The general exception from Adapter layer
	 */
	private ErrorResponse executeValidationRules(final String key,
			final int validationCode, final String value,
			final String correlationId, final ValidationRules validationRules)
					throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" key :" + key + ", validationCode : " + validationCode
				+ ", value : " + value + ", validationRules : "
				+ validationRules, correlationId);
		switch (validationCode) {
		case 1: {
			return this.doNullCheck(value, key, correlationId);
		}

		case 2: {
			return this.doEmptyCheck(value, key, correlationId);
		}

		case 3: {
			return this.doNumericCheck(value, key, correlationId);
		}

		case 4: {
			return this.doAlphabetCheck(value, key, correlationId);
		}

		case 5: {
			return this.doAlphaNumericCheck(value, key, correlationId);
		}

		case 6: {
			return this.doMaxCheck(value, key, correlationId, validationRules
					.getBaseValidation().getMaxLength());
		}

		case 7: {
			return this.doEmailCheck(value, key, correlationId);
		}

		case 8: {
			return this.doRegexCheck(value, key, correlationId, validationRules
					.getAdvanceValidation().getRegex());
		}

		case 9: {
			return this.doDateCheck(value, key, correlationId, validationRules
					.getBaseValidation().getDate().getFormat());
		}

		}
		LOGGER.logMethodExit(startTime, correlationId);
		return this.executeValidationRulesExtended(key, validationCode, value,
				correlationId, validationRules);

	}

	/**
	 * Method which validates the request key and value.
	 * This method has been added to avoid Cyclomatic Complexity.
	 * 
	 * @param key
	 *            - String containing key.
	 * @param validationCode
	 *            - validation code
	 * @param value
	 *            - String to be validated
	 * @param correlationId
	 *            - to track the request param.
	 * @param validationRules
	 *            - validation rules for each key
	 * @return ErrorResponse instance
	 * @throws BaseException
	 *             - The general exception from Adapter layer
	 */
	private ErrorResponse executeValidationRulesExtended(final String key,
			final int validationCode, final String value,
			final String correlationId, final ValidationRules validationRules)
					throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" key :" + key + ", validationCode : " + validationCode
				+ ", value : " + value + ", validationRules : "
				+ validationRules, correlationId);
		switch (validationCode) {
		case 10: {
			return this.doPreviousDateCheck(value, key, correlationId,
					validationRules.getBaseValidation().getDate().getFormat());
		}
		case 11: {
			return this.doFutureDateCheck(value, key, correlationId, validationRules
					.getBaseValidation().getDate().getFormat());
		}

		case 12: {
			return this.doRangeCheck(value, key, correlationId, validationRules
					.getBaseValidation().getRange());
		}

		case 14: {
			return this.doAttributeParamCheck(key, value, correlationId);
		}

		case 15: {
			return this.doSortParamCheck(key, value, correlationId);
		}

		case 16: {
			return this.doOptionsParamCheck(key, value, correlationId);
		}


		default:
			return null;
		}
	}

	/**
	 * Method to validate the param value as per configured regular expression.
	 * 
	 * @param value
	 *            - String to be validated
	 * @param key
	 *            - String containing key.
	 * @param correlationId
	 *            - to track the request param.
	 * @param regex
	 *            - the regular expression for validation
	 * @return errorResponse
	 */
	private ErrorResponse doRegexCheck(final String value, final String key,
			final String correlationId, final Regex regex) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" value :" + value + ", key : " + key + ", regex : " + regex, correlationId);
		final Map<String, String> errorPropertiesMap = this.errorLoader.getErrorPropertiesMap();
		if (!new PatternValidator(regex.getRegex(), regex.getSplitter())
		.validate(value, correlationId)) {
			LOGGER.logMethodExit(startTime, correlationId);
			return this.getErrorResponse(key, value,
					Integer.parseInt(errorPropertiesMap.get(String
							.valueOf(ErrorConstants.ERROR_CODE_8))),
							ValidatorConstant.REGEX_CHECK_MESSAGE, correlationId);
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return null;
	}

	/**
	 * Method to validate the param value range.
	 * 
	 * @param value
	 *            - String to be validated
	 * @param key
	 *            - String containing key.
	 * @param correlationId
	 *            - to track the request param.
	 * @param range
	 *            - to validate the parameter range
	 * @return errorResponse
	 */
	private ErrorResponse doRangeCheck(final String value, final String key,
			final String correlationId, final Range range) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" value :" + value + ", key : " + key
				+ ", range : " + range, correlationId);
		if (value == null
				|| !(value.length() <= range.getMaximum() && value.length() >= range
				.getMinimum())) {
			LOGGER.logMethodExit(startTime, correlationId);
			final Map<String, String> errorPropertiesMap = this.errorLoader
					.getErrorPropertiesMap();
			return this.getErrorResponse(key, value, Integer
					.parseInt(errorPropertiesMap.get(String
							.valueOf(ValidatorConstant.RANGE_CHECK_CODE))),
							errorPropertiesMap.get(errorPropertiesMap.get(String
									.valueOf(ValidatorConstant.RANGE_CHECK_CODE))),
									correlationId);
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return null;
	}

	/**
	 * Method to validate the previous date.
	 * 
	 * @param value
	 *            - String to be validated
	 * @param key
	 *            - String containing key.
	 * @param correlationId
	 *            - to track the request param.
	 * @param format
	 *            - date format to validate
	 * @return errorResponse
	 */
	private ErrorResponse doPreviousDateCheck(final String value, final String key,
			final String correlationId, final String format) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" value :" + value + ", key : " + key
				+ ", format : " + format, correlationId);
		if (!new DateValidator().isBeforeDate(value, format, correlationId)) {
			LOGGER.logMethodExit(startTime, correlationId);
			final Map<String, String> errorPropertiesMap = this.errorLoader
					.getErrorPropertiesMap();
			return this.getErrorResponse(key, value, Integer
					.parseInt(errorPropertiesMap.get(String
							.valueOf(ValidatorConstant.PAST_DATE_CHECK_CODE))),
							errorPropertiesMap.get(errorPropertiesMap.get(String
									.valueOf(ValidatorConstant.PAST_DATE_CHECK_CODE))),
									correlationId);		

		}
		LOGGER.logMethodExit(startTime, correlationId);
		return null;
	}

	/**
	 * Method to validate the future date.
	 * 
	 * @param value
	 *            - String to be validated
	 * @param key
	 *            - String containing key.
	 * @param correlationId
	 *            - to track the request param.
	 * @param format
	 *            - date format to validate
	 * @return errorResponse
	 */
	private ErrorResponse doFutureDateCheck(final String value, final String key,
			final String correlationId, final String format) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" value :" + value + ", key : " + key
				+ ", format : " + format, correlationId);
		if (!new DateValidator().isFutureDate(value, format, correlationId)) {
			LOGGER.logMethodExit(startTime, correlationId);
			final Map<String, String> errorPropertiesMap = this.errorLoader
					.getErrorPropertiesMap();
			return this.getErrorResponse(key, value, Integer
					.parseInt(errorPropertiesMap.get(String
							.valueOf(ValidatorConstant.FUTURE_DATE_CHECK_CODE))),
							errorPropertiesMap.get(errorPropertiesMap.get(String
									.valueOf(ValidatorConstant.FUTURE_DATE_CHECK_CODE))),
									correlationId);				
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return null;
	}

	/**
	 * Method to validate the date.
	 * 
	 * @param value
	 *            - String to be validated
	 * @param key
	 *            - String containing key.
	 * @param correlationId
	 *            - to track the request param.
	 * @param refValue
	 *            - refValue
	 * @return errorResponse
	 */
	private ErrorResponse doDateCheck(final String value, final String key,
			final String correlationId, final String refValue) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" value :" + value + ", key : " + key
				+ ", refValue : " + refValue, correlationId);
		if (!new DateValidator().isDateValid(value, refValue, correlationId)) {
			LOGGER.logMethodExit(startTime, correlationId);
			final Map<String, String> errorPropertiesMap = this.errorLoader
					.getErrorPropertiesMap();
			return this.getErrorResponse(key, value, Integer
					.parseInt(errorPropertiesMap.get(String
							.valueOf(ValidatorConstant.DATE_CHECK_CODE))),
							errorPropertiesMap.get(errorPropertiesMap.get(String
									.valueOf(ValidatorConstant.DATE_CHECK_CODE))),
									correlationId);
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return null;

	}

	/**
	 * Method to validate the value of email.
	 * 
	 * @param value
	 *            - String to be validated
	 * @param key
	 *            - String containing key.
	 * @param correlationId
	 *            - to track the request param.
	 * @return errorResponse
	 */
	private ErrorResponse doEmailCheck(final String value, final String key,
			final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" value :" + value + ", key : " + key, correlationId);
		if (!new PatternValidator().validate(value, correlationId)) {
			LOGGER.logMethodExit(startTime, correlationId);
			final Map<String, String> errorPropertiesMap = this.errorLoader
					.getErrorPropertiesMap();
			return this.getErrorResponse(key, value, Integer
					.parseInt(errorPropertiesMap.get(String
							.valueOf(ValidatorConstant.EMAIL_CHECK_CODE))),
							errorPropertiesMap.get(errorPropertiesMap.get(String
									.valueOf(ValidatorConstant.EMAIL_CHECK_CODE))),
									correlationId);			
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return null;
	}

	/**
	 * Method to validate the maximum value for param value.
	 * 
	 * @param value
	 *            - String to be validated
	 * @param key
	 *            - String containing key.
	 * @param correlationId
	 *            - to track the request param.
	 * @param refValue
	 *            - refValue
	 * @return errorResponsel
	 */
	private ErrorResponse doMaxCheck(final String value, final String key,
			final String correlationId, final int refValue) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" value :" + value + ", key : " + key
				+ ", refValue : " + refValue, correlationId);
		if (value != null && value.length() > refValue) {
			LOGGER.logMethodExit(startTime, correlationId);
			final Map<String, String> errorPropertiesMap = this.errorLoader
					.getErrorPropertiesMap();
			return this.getErrorResponse(key, value, Integer
					.parseInt(errorPropertiesMap.get(String
							.valueOf(ValidatorConstant.MAX_CHECK_CODE))),
							errorPropertiesMap.get(errorPropertiesMap.get(String
									.valueOf(ValidatorConstant.MAX_CHECK_CODE))),
									correlationId);			
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return null;
	}

	/**
	 * Method to validate the alpha numeric value for param value.
	 * 
	 * @param value
	 *            - String to be validated
	 * @param key
	 *            - String containing key.
	 * @param correlationId
	 *            - to track the request param.
	 * @return errorResponse
	 */
	private ErrorResponse doAlphaNumericCheck(final String value, final String key,
			final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" value :" + value + ", key : " + key, correlationId);
		if (!StringUtils.isAlphanumeric(value)) {
			LOGGER.logMethodExit(startTime, correlationId);
			final Map<String, String> errorPropertiesMap = this.errorLoader
					.getErrorPropertiesMap();
			return this.getErrorResponse(key, value, Integer
					.parseInt(errorPropertiesMap.get(String
							.valueOf(ValidatorConstant.ALPHANUMERIC_CHECK_CODE))),
							errorPropertiesMap.get(errorPropertiesMap.get(String
									.valueOf(ValidatorConstant.ALPHANUMERIC_CHECK_CODE))),
									correlationId);			
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return null;
	}

	/**
	 * Method to validate the alpha value for param value.
	 * 
	 * @param value
	 *            - String to be validated
	 * @param key
	 *            - String containing key.
	 * @param correlationId
	 *            - to track the request param.
	 * @return errorResponse
	 */
	private ErrorResponse doAlphabetCheck(final String value, final String key,
			final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" value :" + value + ", key : " + key, correlationId);
		if (!StringUtils.isAlpha(value)) {
			LOGGER.logMethodExit(startTime, correlationId);
			final Map<String, String> errorPropertiesMap = this.errorLoader
					.getErrorPropertiesMap();
			return this.getErrorResponse(key, value, Integer
					.parseInt(errorPropertiesMap.get(String
							.valueOf(ValidatorConstant.ALPHABET_CHECK_CODE))),
							errorPropertiesMap.get(errorPropertiesMap.get(String
									.valueOf(ValidatorConstant.ALPHABET_CHECK_CODE))),
									correlationId);				
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return null;
	}

	/**
	 * Method to check the numeric value for param value.
	 * 
	 * @param value
	 *            - String to be validated
	 * @param key
	 *            - String containing key.
	 * @param correlationId
	 *            - to track the request param.
	 * @return errorResponse
	 */
	private ErrorResponse doNumericCheck(final String value, final String key,
			final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" value :" + value + ", key : " + key, correlationId);
		if (!StringUtils.isNumeric(value)) {
			LOGGER.logMethodExit(startTime, correlationId);
			final Map<String, String> errorPropertiesMap = this.errorLoader
					.getErrorPropertiesMap();
			return this.getErrorResponse(key, value, Integer
					.parseInt(errorPropertiesMap.get(String
							.valueOf(ValidatorConstant.NUMBER_CHECK_CODE))),
							errorPropertiesMap.get(errorPropertiesMap.get(String
									.valueOf(ValidatorConstant.NUMBER_CHECK_CODE))),
									correlationId);		
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return null;

	}

	/**
	 * Method to check the empty value for param value.
	 * 
	 * @param value
	 *            - String to be validated
	 * @param key
	 *            - String containing key.
	 * @param correlationId
	 *            - to track the request param.
	 * @return errorResponse
	 */
	private ErrorResponse doEmptyCheck(final String value, final String key,
			final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" value :" + value + ", key : " + key, correlationId);
		if (value == null || value.length() <= 0) {
			LOGGER.logMethodExit(startTime, correlationId);
			final Map<String, String> errorPropertiesMap = this.errorLoader
					.getErrorPropertiesMap();
			return this.getErrorResponse(key, value, Integer
					.parseInt(errorPropertiesMap.get(String
							.valueOf(ValidatorConstant.EMPTY_CHECK_CODE))),
							errorPropertiesMap.get(errorPropertiesMap.get(String
									.valueOf(ValidatorConstant.EMPTY_CHECK_CODE))),
									correlationId);			
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return null;

	}

	/**
	 * Method to null check the param value.
	 * 
	 * @param value
	 *            - String to be validated
	 * @param key
	 *            - String containing key.
	 * @param correlationId
	 *            - to track the request param.
	 * @return errorResponse
	 */
	private ErrorResponse doNullCheck(final String value, final String key,
			final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" value :" + value + ", key : " + key, correlationId);
		if (value == null) {
			LOGGER.logMethodExit(startTime, correlationId);
			final Map<String, String> errorPropertiesMap = this.errorLoader
					.getErrorPropertiesMap();
			return this.getErrorResponse(key, value, Integer
					.parseInt(errorPropertiesMap.get(String
							.valueOf(ValidatorConstant.NULL_CHECK_CODE))),
							errorPropertiesMap.get(errorPropertiesMap.get(String
									.valueOf(ValidatorConstant.NULL_CHECK_CODE))),
									correlationId);
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return null;

	}

	/**
	 * Method to validate mandatory field.
	 * 
	 * @param uriMap
	 *            is a request map
	 * @param correlationId
	 *            for tracking the request
	 * @throws BaseException
	 *             thrown from common layer
	 */
	private void executeMandatoryFieldValidation(
			final Map<String, List<String>> uriMap, final String correlationId)
					throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("uriMap : " + uriMap, correlationId);
		final Map<String, String> errorPropertiesMap = this.errorLoader.getErrorPropertiesMap();
		for (String mandatoryKey : this.resourceValidationConfig
				.getMandatoryFields()) {

			if (!uriMap.containsKey(mandatoryKey)
					|| uriMap.get(mandatoryKey) == null
					|| uriMap.get(mandatoryKey).isEmpty()) {
				final ErrorResponse errResponse = this.getErrorResponse(
						mandatoryKey, CommonConstants.EMPTY_STRING,
						ErrorConstants.ERROR_CODE_11425,
						errorPropertiesMap.get(String
								.valueOf(ErrorConstants.ERROR_CODE_11425)),
								correlationId);

				throw this.getBaseException(errResponse, correlationId,
						ErrorConstants.HTTP_STATUS_CODE_INVALID_URL_PARAMETERS);
			}
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to create base exception.
	 * 
	 * @param errResponse
	 *            - Error Response Object
	 * @param correlationId
	 *            - Correlation id
	 * @param errorCode
	 *            - Code for the error
	 * @return baseException
	 */
	private BaseException getBaseException(final ErrorResponse errResponse,
			final String correlationId, final String errorCode) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" errResponse :" + errResponse + ", errorCode : " + errorCode, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return new BaseException(String.valueOf(errResponse.getErrorCode()),
				errResponse.getErrorDescription(),
				errResponse.getParameterName(),
				errResponse.getParameterValue(), errorCode, correlationId);
	}

	/**
	 * Method to get the error response.
	 * 
	 * @param parameterName
	 *            - Name of the parameter.
	 * @param parameterValue
	 *            - Parameter Value.
	 * @param errorCode
	 *            - code for the given error.
	 * @param errorDescription
	 *            - Description of the error.
	 * @param correlationId
	 *            - to track the request param.
	 * @return errResponse - ErrorResponse object.
	 */
	private ErrorResponse getErrorResponse(final String parameterName,
			final String parameterValue, final int errorCode,
			final String errorDescription, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" parameterName :" + parameterName + ", parameterValue : "
				+ parameterValue + ", errorCode : " + errorCode
				+ ", errorDescription : " + errorDescription, correlationId);
		final ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setParameterName(parameterName);
		errorResponse.setParameterValue(parameterValue);
		errorResponse.setErrorCode(errorCode);
		errorResponse.setErrorDescription(errorDescription);
		LOGGER.logMethodExit(startTime, correlationId);
		return errorResponse;
	}

	/**
	 * Method validates the parameter values of request url.
	 * 
	 * @param param
	 *            - List of String containing parameters
	 * @param key
	 *            - String containing key.
	 * @param correlationId
	 *            - to track the request param.
	 * @return ErrorResponse - Instance of ErrorResponse class.
	 * @throws BaseException
	 *             - The general exception from Adapter layer
	 */
	private ErrorResponse validateParameterValues(final List<String> param,
			final String key, final String correlationId) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" param :" + param + ", key : " + key, correlationId);
		final ValidationRules validationRules = this.resourceValidationConfig
				.getParameterValidationMap().get(key);

		if (!validationRules.isValidateParameterEnabled()) {
			return null;
		}
		ErrorResponse errorResponse = null;
		for (String value : param) {

			for (int validationCode : validationRules.getValidationCodes()) {
				errorResponse = this.executeValidationRules(key, validationCode,
						value, correlationId, validationRules);
				if (errorResponse != null) {
					break;
				}
			}
			if (errorResponse != null) {
				break;
			}

		}
		LOGGER.logMethodExit(startTime, correlationId);
		return errorResponse;

	}

	/**
	 * This method to validate the parameter value.
	 * 
	 * @param key
	 *            - String containing key.
	 * @param param
	 *            - String to be validated
	 * @param correlationId
	 *            - to track the request param.
	 * @return ErrorResponse - Instance of ErrorResponse class.
	 * @throws BaseException
	 *             - The general exception from Adapter layer
	 */
	private ErrorResponse doAttributeParamCheck(final String key,
			final String param, final String correlationId)
					throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" param :" + param, correlationId);
		final Map<String, List<String>> uriMap = new HashMap<String, List<String>>();
		final Map<String, String> errorPropertiesMap = this.errorLoader.getErrorPropertiesMap();
		final StringTokenizer parameterValueString = new StringTokenizer(param,
				CommonConstants.PIPE_SEPERATOR);
		while (parameterValueString.hasMoreElements()) {
			final String[] colonSeparated = parameterValueString.nextToken()
					.split(CommonConstants.FIELD_PAIR_SEPARATOR);
			if (colonSeparated.length > ValidatorConstant.NUMBER_ZERO) {
				if (colonSeparated.length == ValidatorConstant.NUMBER_TWO) {
					if (colonSeparated[ValidatorConstant.NUMBER_ZERO] == null
							|| colonSeparated[ValidatorConstant.NUMBER_ZERO].isEmpty()
							|| CommonConstants.EMPTY_VALUE
							.equals(colonSeparated[ValidatorConstant.NUMBER_ZERO])) {
						return this.getErrorResponse(key, param,
								Integer.parseInt(errorPropertiesMap.get(String
										.valueOf(ErrorConstants.ERROR_CODE_8))),
										ValidatorConstant.REGEX_CHECK_MESSAGE,
										correlationId);
					} else {
						uriMap.put(
								colonSeparated[ValidatorConstant.NUMBER_ZERO],
								Arrays.asList(colonSeparated[ValidatorConstant.NUMBER_ONE]));
					}
				} else {
					uriMap.put(colonSeparated[ValidatorConstant.NUMBER_ZERO],
							Arrays.asList(CommonConstants.EMPTY_VALUE));
				}
			} else {
				return this.getErrorResponse(key, param,
						Integer.parseInt(errorPropertiesMap.get(String
								.valueOf(ErrorConstants.ERROR_CODE_8))),
								ValidatorConstant.REGEX_CHECK_MESSAGE, correlationId);
			}
		}
		this.validate(uriMap, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return null;
	}

	/**
	 * This method is to validate the sort parameter values.
	 * 
	 * @param key
	 *            - String containing key.
	 * @param param
	 *            - String to be validated
	 * @param correlationId
	 *            - to track the request param.
	 * @return ErrorResponse - Instance of ErrorResponse class.
	 */
	private ErrorResponse doSortParamCheck(final String key,
			final String param, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" param :" + param, correlationId);
		final Map<String, String> errorPropertiesMap = this.errorLoader.getErrorPropertiesMap();
		final StringTokenizer pipeSeparated = new StringTokenizer(param,
				CommonConstants.PIPE_SEPERATOR);
		while (pipeSeparated.hasMoreElements()) {
			String inputParamValue = pipeSeparated.nextToken();
			if (inputParamValue.startsWith(CommonConstants.EIPHEN_SEPERATOR)) {
				inputParamValue = inputParamValue.replaceFirst(
						CommonConstants.EIPHEN_SEPERATOR,
						CommonConstants.EMPTY_STRING);
			}
			if (!this.sortParametersList().contains(inputParamValue)) {
				return this.getErrorResponse(key, inputParamValue,
						Integer.parseInt(errorPropertiesMap.get(String
								.valueOf(ErrorConstants.ERROR_CODE_8))),
								ValidatorConstant.REGEX_CHECK_MESSAGE, correlationId);
			}
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return null;
	}

	/**
	 * This method is to validate the options parameter values.
	 * 
	 * @param key
	 *            - String containing key.
	 * @param param
	 *            - String to be validated
	 * @param correlationId
	 *            - to track the request param.
	 * @return ErrorResponse - Instance of ErrorResponse class.
	 */
	private ErrorResponse doOptionsParamCheck(final String key,
			final String param, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" param :" + param, correlationId);
		final Map<String, String> errorPropertiesMap = this.errorLoader.getErrorPropertiesMap();
		final StringTokenizer commaSeparated = new StringTokenizer(param,
				CommonConstants.COMMA_SEPERATOR);
		while (commaSeparated.hasMoreElements()) {
			final String inputParamValue = commaSeparated.nextToken();
			if (!this.optionsParametersList().contains(inputParamValue.toLowerCase())) {
				return this.getErrorResponse(key, inputParamValue,
						Integer.parseInt(errorPropertiesMap.get(String
								.valueOf(ErrorConstants.ERROR_CODE_8))),
								ValidatorConstant.REGEX_CHECK_MESSAGE, correlationId);
			}
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return null;
	}

	/**
	 * getter method for resourceValidationConfig.
	 * 
	 * @return resourceValidationConfig - Instance of ResourceValidationConfig
	 *         class.
	 */

	@Override
	public final ResourceValidationConfig getResourceValidationConfig() {
		return this.resourceValidationConfig;
	}

	/**
	 * setter method for resourceValidationConfig.
	 * 
	 * @param resourceValidationConfig
	 *            - Instance of ResourceValidationConfig class.
	 */

	@Override
	public final void setResourceValidationConfig(
			final ResourceValidationConfig resourceValidationConfig) {
		this.resourceValidationConfig = resourceValidationConfig;
	}

	/**
	 * Method to update the validator rules of service API based on validation configuration.
	 * 
	 * @param uriPath
	 *            path of url
	 * @param correlationId
	 *            - to track the request param.
	 * @throws BaseException
	 *             if Validator Rules are not available
	 */
	public final void updateValidatorRules(final String uriPath, final String correlationId)
			throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("uriPath :" + uriPath, correlationId);
		uriInfoPath = uriPath;

		final ValidationConfig validationConfig = ValidationPathConfigHelper.validationConfig;

		if (this.isValidatorRuleAvailable(correlationId)) {

			final String xmlFileName = ValidationPathConfigHelper.validationFilePath
					+ validationConfig.getValidatorPathConfigMap().get(
							this.servicePath);

			final ResourceValidationConfig resourceValidationConfig = new ValidationConfigRuleConverter()
			.getResourceValidationConfig(xmlFileName, correlationId);

			if (resourceValidationConfig == null) {

				throw new BaseException(String.valueOf(ErrorConstants.ERROR_CODE_1233),
						ValidatorConstant.VALIDATOR_RULES_NOT_AVAIL_MESSAGE,
						ValidatorConstant.VALIDATION_RULES,
						ValidatorConstant.VALIDATOR_RULES_NOT_AVAIL_MESSAGE,
						ErrorConstants.HTTP_STATUS_CODE_INVALID_URL_PARAMETERS,
						correlationId);

			} else {
				this.prepareValidationCodesList(resourceValidationConfig, correlationId);
			}

			this.setResourceValidationConfig(resourceValidationConfig);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method which prepares the validation code list for validating the input request.
	 * 
	 * @param resourceValidationConfig
	 *            - Instance of ResourceValidationConfig class.
	 * @param correlationId
	 *            - to track the request param.
	 */
	private void prepareValidationCodesList(
			final ResourceValidationConfig resourceValidationConfig,
			final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" resourceValidationConfig :" + resourceValidationConfig, correlationId);
		final Map<String, ValidationRules> updatedParameterMap = new HashMap<String, ValidationRules>();

		final List<String> mandatoryFieldsList = new ArrayList<String>();

		for (Map.Entry<String, ValidationRules> entry : resourceValidationConfig
				.getParameterValidationMap().entrySet()) {
			final String key = entry.getKey();
			final ValidationRules validationRules = entry.getValue();

			if (validationRules != null) {
				final List<Integer> validationCodes = new LinkedList<Integer>();

				if (validationRules.isMandatoryField()) {
					mandatoryFieldsList.add(key);
				}

				this.prepareValidationCodesListBaseValidaion(validationRules, validationCodes, correlationId);

				this.prepareValidationCodesListAdvanceValidaion(validationRules, validationCodes, correlationId);

				validationRules.setValidationCodes(validationCodes);
				updatedParameterMap.put(key, validationRules);
			}
		}
		resourceValidationConfig.setMandatoryFields(mandatoryFieldsList);
		resourceValidationConfig.setParameterValidationMap(updatedParameterMap);
		LOGGER.logMethodExit(startTime, correlationId);

	}

	/**
	 * Method which prepares the validation code list for validating the input request.
	 * 
	 * @param validationRules
	 *            - validation rules for each key
	 * @param validationCodes
	 *            - validationCodes
	 * @param correlationId
	 *            - to track the request param.
	 */
	private void prepareValidationCodesListBaseValidaion(
			final ValidationRules validationRules,
			final List<Integer> validationCodes, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" validationRules :" + validationRules
				+ ", validationCodes : " + validationCodes, correlationId);
		if (validationRules.getBaseValidation() != null
				&& validationRules.getBaseValidation().isRequired()) {
			this.prepareValidationCodesListExtended(validationRules, validationCodes, correlationId);
			this.prepareValidationCodesListDate(validationRules, validationCodes, correlationId);
			this.prepareValidationCodesListFutureDate(validationRules, validationCodes, correlationId);
			this.prepareValidationCodesListRange(validationRules, validationCodes, correlationId);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method which prepares the validation code list for validating the input request.
	 * 
	 * @param validationRules
	 *            - validation rules for each key
	 * @param validationCodes
	 *            - validationCodes
	 * @param correlationId
	 *            - to track the request param.
	 */
	private void prepareValidationCodesListExtended(
			final ValidationRules validationRules,
			final List<Integer> validationCodes, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" validationRules :" + validationRules
				+ ", validationCodes : " + validationCodes, correlationId);
		if (validationRules.getBaseValidation().isNullCheck()) {
			validationCodes.add(ValidatorConstant.NULL_CHECK_CODE);
		}

		if (validationRules.getBaseValidation().isEmptyCheck()) {
			validationCodes.add(ValidatorConstant.EMPTY_CHECK_CODE);
		}

		if (validationRules.getBaseValidation().isOnlyNumber()) {
			validationCodes.add(ValidatorConstant.NUMBER_CHECK_CODE);
		}

		if (validationRules.getBaseValidation().isOnlyAlphabet()) {
			validationCodes.add(ValidatorConstant.ALPHABET_CHECK_CODE);
		}

		if (validationRules.getBaseValidation().isNumberAlphabet()) {
			validationCodes.add(ValidatorConstant.ALPHANUMERIC_CHECK_CODE);
		}

		if ((validationRules.getBaseValidation().getRange() == null || !validationRules
				.getBaseValidation().getRange().isRangeCheck())
				&& validationRules.getBaseValidation().getMaxLength() != 0) {
			validationCodes.add(ValidatorConstant.MAX_CHECK_CODE);
		}

		if (validationRules.getBaseValidation().isEmailCheck()) {
			validationCodes.add(ValidatorConstant.EMAIL_CHECK_CODE);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method which prepares the validation code list for validating the input request.
	 * 
	 * @param validationRules
	 *            - validation rules for each key
	 * @param validationCodes
	 *            - validationCodes
	 * @param correlationId
	 *            - to track the request param.
	 */
	private void prepareValidationCodesListDate(
			final ValidationRules validationRules,
			final List<Integer> validationCodes, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" validationRules :" + validationRules
				+ ", validationCodes : " + validationCodes, correlationId);
		if (validationRules.getBaseValidation().getDate() != null
				&& validationRules.getBaseValidation().getDate().isDateCheck()
				&& validationRules.getBaseValidation().getDate().getFormat() != null
				&& !validationRules.getBaseValidation().getDate().getFormat()
				.isEmpty()) {
			validationCodes.add(ValidatorConstant.DATE_CHECK_CODE);
		}

		if (validationRules.getBaseValidation().getDate() != null
				&& validationRules.getBaseValidation().getDate().isPastCheck()
				&& validationRules.getBaseValidation().getDate().getFormat() != null
				&& !validationRules.getBaseValidation().getDate().getFormat()
				.isEmpty()) {
			validationCodes.add(ValidatorConstant.PAST_DATE_CHECK_CODE);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method which prepares the validation code list for validating the input request.
	 * 
	 * @param validationRules
	 *            - validation rules for each key
	 * @param validationCodes
	 *            - validationCodes
	 * @param correlationId
	 *            - to track the request param.
	 */
	private void prepareValidationCodesListFutureDate(
			final ValidationRules validationRules,
			final List<Integer> validationCodes, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" validationRules :" + validationRules
				+ ", validationCodes : " + validationCodes, correlationId);
		if (validationRules.getBaseValidation().getDate() != null
				&& validationRules.getBaseValidation().getDate()
				.isFutureCheck()
				&& validationRules.getBaseValidation().getDate().getFormat() != null
				&& !validationRules.getBaseValidation().getDate().getFormat()
				.isEmpty()) {
			validationCodes.add(ValidatorConstant.FUTURE_DATE_CHECK_CODE);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method which prepares the validation code list for validating the input request.
	 * 
	 * @param validationRules
	 *            - validation rules for each key
	 * @param validationCodes
	 *            - validationCodes
	 * @param correlationId
	 *            - to track the request param.
	 */
	private void prepareValidationCodesListRange(
			final ValidationRules validationRules,
			final List<Integer> validationCodes, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" validationRules :" + validationRules
				+ ", validationCodes : " + validationCodes, correlationId);
		if (validationRules.getBaseValidation().getRange() != null
				&& validationRules.getBaseValidation().getRange()
				.isRangeCheck()
				&& validationRules.getBaseValidation().getRange().getMaximum() != ValidatorConstant.NUMBER_ZERO
				&& validationRules.getBaseValidation().getRange().getMinimum() != ValidatorConstant.NUMBER_ZERO) {
			validationCodes.add(ValidatorConstant.RANGE_CHECK_CODE);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method which prepares the validation code list for validating the input request.
	 * 
	 * @param validationRules
	 *            - validation rules for each key
	 * @param validationCodes
	 *            - validationCodes
	 * @param correlationId
	 *            - to track the request param.
	 */
	private void prepareValidationCodesListAdvanceValidaion(
			final ValidationRules validationRules,
			final List<Integer> validationCodes, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" validationRules :" + validationRules
				+ ", validationCodes : " + validationCodes, correlationId);
		if (validationRules.getAdvanceValidation() != null
				&& validationRules.getAdvanceValidation().isRequired()) {
			if (validationRules.getAdvanceValidation().getRegex() != null
					&& validationRules.getAdvanceValidation().getRegex()
					.isRegexCheck()
					&& validationRules.getAdvanceValidation().getRegex()
					.getRegex() != null
					&& !validationRules.getAdvanceValidation().getRegex()
					.getRegex().isEmpty()) {
				validationCodes.add(ValidatorConstant.REGEX_CHECK_CODE);
			}

			if (validationRules.getAdvanceValidation().isParamValueValidate()) {
				validationCodes.add(ValidatorConstant.ATTR_PARAM_CHECK_CODE);
			}

			if (validationRules.getAdvanceValidation().isSortParamValidate()) {
				validationCodes.add(ValidatorConstant.SORT_CHECK_CODE);
			}

			if (validationRules.getAdvanceValidation().isOptionsParamValidate()) {
				validationCodes.add(ValidatorConstant.OPTIONS_CHECK_CODE);
			}

		}
		LOGGER.logMethodExit(startTime, correlationId);
	}


	/**
	 * This method to check the validation rule available for service.
	 * 
	 * @param correlationId
	 *            - to track the request param.
	 * @return true if the validator rule is available
	 */
	private boolean isValidatorRuleAvailable(final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final ValidationConfig validationConfig = ValidationPathConfigHelper.validationConfig;

		if (validationConfig != null && this.isValidatorConfigPathEntry(validationConfig, correlationId)) {
			LOGGER.logMethodExit(startTime, correlationId);
			return true;
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return false;
	}

	/**
	 * Method to check for validator config path entry is avialable or not.
	 * 
	 * @param validationConfig
	 *            validationConfig
	 * @param correlationId
	 *            - to track the request param.
	 * @return - boolean value
	 */
	private boolean isValidatorConfigPathEntry(
			final ValidationConfig validationConfig, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" validationConfig :" + validationConfig, correlationId);
		if (this.servicePath != null) {
			LOGGER.logMethodExit(startTime, correlationId);
			return true;
		}
		if (validationConfig.getValidatorPathConfigMap() != null
				&& !validationConfig.getValidatorPathConfigMap().isEmpty()) {
			if (validationConfig.getValidatorPathConfigMap().containsKey(
					this.uriInfoPath)) {
				this.servicePath = this.uriInfoPath;
				LOGGER.logMethodExit(startTime, correlationId);
				return true;
			} else {
				for (Entry<String, String> e : validationConfig
						.getValidatorPathConfigMap().entrySet()) {
					if (uriInfoPath.startsWith(e.getKey())) {
						this.servicePath = e.getKey();
						LOGGER.logMethodExit(startTime, correlationId);
						return true;
					}
				}
			}
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return false;
	}

	/**
	 * Method checks for validation required or not for a specific service.
	 * 
	 * @param serviceName
	 *            service name
	 * @param correlationId
	 *            - to track the request param.
	 * @return boolean rue or false
	 * 
	 * @throws BaseException
	 * 			throws BaseException
	 */			
	public final boolean isBaseValidationRequired(final String serviceName,
			final String correlationId) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("serviceName :" + serviceName, correlationId);
		boolean isValidationRequired = false;
		if ((ValidationPathConfigHelper.validationConfig != null)
				&& (ValidationPathConfigHelper.validationConfig
						.getApiServicesConfigMap() != null)
						&& !ValidationPathConfigHelper.validationConfig
						.getApiServicesConfigMap().isEmpty()) {
			LOGGER.logMethodExit(startTime, correlationId);
			isValidationRequired = ValidationPathConfigHelper.validationConfig
					.getApiServicesConfigMap().get(serviceName);
		}
		LOGGER.debug("isValidationRequired :" + isValidationRequired, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return isValidationRequired;
	}

	/**
	 * This method creates the allowed list of sort parameter values.
	 * 
	 * @return list of sort parameters list
	 */
	private List<String> sortParametersList() {
		final List<String> sortParameters = new ArrayList<String>();
		sortParameters.add(ValidatorConstant.PRODUCT_CODE);
		sortParameters.add(ValidatorConstant.NAME);
		sortParameters.add(ValidatorConstant.LIST_PRICE);
		sortParameters.add(ValidatorConstant.SALE_PRICE);
		sortParameters.add(ValidatorConstant.INVENTORY);
		sortParameters.add(ValidatorConstant.NEW_ARRIVAL);
		sortParameters.add(ValidatorConstant.BEST_SELLER);
		sortParameters.add(ValidatorConstant.WISH_LIST_FAVORITES);
		sortParameters.add(ValidatorConstant.TOP_RATING);
		return sortParameters;
	}


	/**
	 * This method creates the allowed list of values for options parameter.
	 *  
	 * @return list of sort parameters list
	 */
	private List<String> optionsParametersList() {
		final List<String> optionsParameters = new ArrayList<String>();
		optionsParameters.add(RequestConstants.SKUS);
		optionsParameters.add(RequestConstants.PRODUCT_TYPE);
		optionsParameters.add(RequestConstants.NAME);
		optionsParameters.add(RequestConstants.BRAND);
		optionsParameters.add(RequestConstants.SHORT_DESC);
		optionsParameters.add(RequestConstants.LONG_DESC);
		optionsParameters.add(RequestConstants.HIERARCHY);
		optionsParameters.add(RequestConstants.PRODUCT_PRICE);
		optionsParameters.add(RequestConstants.RATINGS);
		optionsParameters.add(RequestConstants.PRODUCT_FLAG);
		optionsParameters.add(RequestConstants.PRODUCT_ATTRIBUTES);
		optionsParameters.add(RequestConstants.PROMOTIONS);
		optionsParameters.add(RequestConstants.CHILD_PRODUCTS);
		return optionsParameters;
	}



	/**
	 * This method updates the validation configurations.
	 * 
	 * @param updateRequestUriMap
	 * 			the requiest uri map
	 * @param correlationId
	 * 			correlation Id passed
	 * @return list of sort parameters list
	 * @throws BaseException
	 * 			throws BaseException
	 */

	public final boolean updateValidationConfigurations(
			final Map<String, List<String>> updateRequestUriMap,
			final String correlationId) throws BaseException {

		if (updateRequestUriMap != null
				&& !updateRequestUriMap.isEmpty()
				&& updateRequestUriMap
				.containsKey(CommonConstants.RELOAD_VALIDATION_CONFIG)
				&& uriInfoPath != null) {
			final String isReloadValidationConfig = updateRequestUriMap.get(
					CommonConstants.RELOAD_VALIDATION_CONFIG).get(0);
			if (isReloadValidationConfig != null
					&& isReloadValidationConfig
					.equals(CommonConstants.TRUE_VALUE)) {
				try {
					this.updateValidatorRules(uriInfoPath, correlationId);
					return true;
				} catch (BaseException e) {
					LOGGER.error(e, correlationId);
					throw e;
				}
			}
		}
		return false;
	}

}
