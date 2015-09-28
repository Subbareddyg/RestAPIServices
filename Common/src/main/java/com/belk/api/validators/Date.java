package com.belk.api.validators;

/**
 * This class contains date validation methods.
 * 
 * @author Mindtree
 * 
 */
public class Date {

	/**
	 * dateCheck is to check the date.
	 */
	private boolean dateCheck;

	/**
	 * pastCheck is to check the past date.
	 */
	private boolean pastCheck;

	/**
	 * futureCheck is to check the future date.
	 */
	private boolean futureCheck;

	/**
	 * format is to check the format of date.
	 */
	private String format;

	/**
	 * @return the dateCheck
	 */
	public final boolean isDateCheck() {
		return this.dateCheck;
	}

	/**
	 * @param dateCheck
	 *            the dateCheck to set
	 */
	public final void setDateCheck(final boolean dateCheck) {
		this.dateCheck = dateCheck;
	}

	/**
	 * @return the pastCheck
	 */
	public final boolean isPastCheck() {
		return this.pastCheck;
	}

	/**
	 * @param pastCheck
	 *            the pastCheck to set
	 */
	public final void setPastCheck(final boolean pastCheck) {
		this.pastCheck = pastCheck;
	}

	/**
	 * @return the futureCheck
	 */
	public final boolean isFutureCheck() {
		return this.futureCheck;
	}

	/**
	 * @param futureCheck
	 *            the futureCheck to set
	 */
	public final void setFutureCheck(final boolean futureCheck) {
		this.futureCheck = futureCheck;
	}

	/**
	 * @return the format
	 */
	public final String getFormat() {
		return this.format;
	}

	/**
	 * @param format
	 *            the format to set
	 */
	public final void setFormat(final String format) {
		this.format = format;
	}

}
