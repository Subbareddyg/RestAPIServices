package com.belk.api.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;

/**
 * This class validate the date.
 * 
 * @author Mindtree 
 * 
 */
public class DateValidator {

	/**
	 * Creating logger instance.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();
	
	/**
	 * Method to check whether the given date is future date or not.
	 * 
	 * @param dateToValidate
	 *            - date to be validate.
	 * @param correlationId
	 *            - to track the request param.
	 * @param dateFormat
	 *            - format of the date.
	 * @return true/false.
	 */
	public final boolean isFutureDate(final String dateToValidate,
			final String dateFormat, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" dateToValidate :" + dateToValidate + " ,dateFormat :"
				+ dateFormat, correlationId);
		final Date date = this.getValidDate(dateToValidate, dateFormat, correlationId);
		if (date != null) {

			// create calendar objects.
			final Calendar calendar = Calendar.getInstance();
			final Calendar future = Calendar.getInstance();

			// change year in future calendar
			future.setTime(date);

			if (future.after(calendar)) {
				LOGGER.logMethodExit(startTime, correlationId);
				return true;
			}

		}
		LOGGER.logMethodExit(startTime, correlationId);
		return false;
	}

	/**
	 * Method to check whether the given date is past date or not.
	 * 
	 * @param dateToValidate
	 *            - date to be validated.
	 * @param dateFormat
	 *            - format of date.
	 * @param correlationId
	 *            - to track the request param.
	 * @return true/false.
	 */
	public final boolean isBeforeDate(final String dateToValidate,
			final String dateFormat, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" dateToValidate :" + dateToValidate + " ,dateFormat :"
				+ dateFormat, correlationId);
		final Date date = this.getValidDate(dateToValidate, dateFormat, correlationId);
		if (date != null) {
			final Calendar calendar = Calendar.getInstance();
			final Calendar future = Calendar.getInstance();

			// change year in future calendar
			future.setTime(date);

			if (future.before(calendar)) {
				LOGGER.logMethodExit(startTime, correlationId);
				return true;
			}
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return false;

	}

	/**
	 * Method to get the valid date.
	 * 
	 * @param dateToValidate
	 *            - date to be validated.
	 * @param dateFormat
	 *            - date format
	 * @param correlationId
	 *            - to track the request param.
	 * @return date - Instance of Date class.
	 */
	private Date getValidDate(final String dateToValidate,
			final String dateFormat, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" dateToValidate :" + dateToValidate + " ,dateFormat :"
				+ dateFormat, correlationId);
		if (dateToValidate == null) {
			return null;
		}

		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		simpleDateFormat.setLenient(false);

		try {

			// if not valid, it will throw ParseException
			final Date date = simpleDateFormat.parse(dateToValidate);
			LOGGER.logMethodExit(startTime, correlationId);
			return date;

		} catch (ParseException e) {
			LOGGER.error(e, correlationId);
			return null;
		}		
	}

	/**
	 * Method to validate the given date.
	 * 
	 * @param dateToValidate
	 *            - date to be validated.
	 * @param dateFormat
	 *            - date format.
	 * @param correlationId
	 *            - to track the request param.
	 * @return true/false.
	 */
	public final boolean isDateValid(final String dateToValidate,
			final String dateFormat, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" dateToValidate :" + dateToValidate + " ,dateFormat :"
				+ dateFormat, correlationId);
		if (this.getValidDate(dateToValidate, dateFormat, correlationId) != null) {
			LOGGER.logMethodExit(startTime, correlationId);
			return true;
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return false;

	}
}