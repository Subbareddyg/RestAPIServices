package com.belk.api.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;

/**
 * PatternValidator class for validating regular expression.
 * 
 * @author Mindtree
 * 
 */
public class PatternValidator {

	/**
	 * STRING_PATTERN is string containing regular expression for validation.
	 */
	private static final String STRING_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	/**
	 * Creating logger instance.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();
	
	/**
	 * pattern instance of Pattern class.
	 */
	private final Pattern pattern;
	
	/**
	 * splitter specify the input param splitter.
	 */
	private String splitter;

	/**
	 * matcher is the instance of Matcher class.
	 */
	private Matcher matcher;

	/**
	 * Constructor.
	 * 
	 */
	public PatternValidator() {
		this.pattern = Pattern.compile(STRING_PATTERN);
	}

	/**
	 * Parameterized Constructor.
	 * 
	 * @param pattern
	 *            - pattern of type String.
	 * @param splitter
	 *            - splitter of the String.
	 */
	public PatternValidator(final String pattern, final String splitter) {
		this.splitter = splitter;
		this.pattern = Pattern.compile(pattern);
	}

	/**
	 * Validate hex with regular expression.
	 * 
	 * @param inputParamValue
	 *            - inputParamValue for validation
	 * @param correlationId
	 *            - to track the request param.
	 * @return true valid hex, false invalid hex
	 */
	public final boolean validate(final String inputParamValue,
			final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" inputParamValue :" + inputParamValue, correlationId);
		if (inputParamValue == null || inputParamValue.isEmpty()) {
			LOGGER.logMethodExit(startTime, correlationId);
			return false;
		}
		if (this.splitter != null) {
			final String[] splitValues = StringUtils
					.delimitedListToStringArray(inputParamValue, this.splitter);
			for (String value : splitValues) {
				this.matcher = this.pattern.matcher(value);
				if (!this.matcher.matches()) {
					LOGGER.logMethodExit(startTime, correlationId);
					return false;
				}
			}
			LOGGER.logMethodExit(startTime, correlationId);
			return true;
		} else {
			this.matcher = this.pattern.matcher(inputParamValue);
			LOGGER.logMethodExit(startTime, correlationId);
			return this.matcher.matches();
		}

	}
}