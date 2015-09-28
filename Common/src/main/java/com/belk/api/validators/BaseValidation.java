package com.belk.api.validators;

/**
 * Base Validation POJO Class
 * 
 * @author mindtree
 * @date Feb 21, 2014
 * 
 */
public class BaseValidation {

	/**
	 * enable or disable basevalidation
	 */
	private boolean required;
	/**
	 * nullCheck is to check whether the parameter is null or not.
	 */
	private boolean nullCheck;

	/**
	 * emptyCheck is to check whether the parameter is empty or not.
	 */
	private boolean emptyCheck;

	/**
	 * range is to validate the parameter range.
	 */
	private Range range;

	/**
	 * emailCheck is to validate the email.
	 */
	private boolean emailCheck;

	/**
	 * date is to define the date for validation.
	 */
	private Date date;
	/**
	 * onlyNumber is to validate whether parameter is only number or not.
	 */
	private boolean onlyNumber;

	/**
	 * onlyAlphabet is to validate whether parameter contains only alphabet or
	 * not.
	 */
	private boolean onlyAlphabet;
	/**
	 * numberAlphabet is to validate the parameter is of combination od number
	 * and alphabet.
	 */
	private boolean numberAlphabet;

	/**
	 * maxLength is to validate the maximum length of parameter.
	 */
	private int maxLength;

	/**
	 * Method to check whether nullCheck variable is set or not.
	 * 
	 * @return nullCheck - boolean value.
	 */
	public final boolean isNullCheck() {
		return this.nullCheck;
	}

	/**
	 * Method to set the nullCheck.
	 * 
	 * @param nullCheck
	 *            - boolean value.
	 */
	public final void setNullCheck(final boolean nullCheck) {
		this.nullCheck = nullCheck;
	}

	/**
	 * Method to check whether the emptyCheck for the parameter is enabled or
	 * not.
	 * 
	 * @return emptyCheck - boolean value.
	 */
	public final boolean isEmptyCheck() {
		return this.emptyCheck;
	}

	/**
	 * Method to set the emptyCheck.
	 * 
	 * @param emptyCheck
	 *            - boolean value.
	 */
	public final void setEmptyCheck(final boolean emptyCheck) {
		this.emptyCheck = emptyCheck;
	}

	/**
	 * Method to get the range of the parameter.
	 * 
	 * @return Range - Instance of Range class.
	 */
	public final Range getRange() {
		return this.range;
	}

	/**
	 * Method to set the range of the parameter.
	 * 
	 * @param range
	 *            - Instance of Range class.
	 */
	public final void setRange(final Range range) {
		this.range = range;
	}

	/**
	 * Method to check whether the emailCheck for the parameter is enabled or
	 * not.
	 * 
	 * @return emailCheck - boolean value.
	 */

	public final boolean isEmailCheck() {
		return this.emailCheck;
	}

	/**
	 * Method to set the emailCheck.
	 * 
	 * @param emailCheck
	 *            - boolean value.
	 */
	public final void setEmailCheck(final boolean emailCheck) {
		this.emailCheck = emailCheck;
	}

	/**
	 * Method to get the date of the parameter.
	 * 
	 * @return date - Instance of Date class.
	 */
	public final Date getDate() {
		return this.date;
	}

	/**
	 * Method to set the date of the parameter.
	 * 
	 * @param date
	 *            - Instance of Date class.
	 */
	public final void setDate(final Date date) {
		this.date = date;
	}

	/**
	 * Method to check whether the onlyNumber for the parameter is enabled or
	 * not.
	 * 
	 * @return onlyNumber - boolean value.
	 */

	public final boolean isOnlyNumber() {
		return this.onlyNumber;
	}

	/**
	 * Method to set the onlyNumber.
	 * 
	 * @param onlyNumber
	 *            - boolean value.
	 */
	public final void setOnlyNumber(final boolean onlyNumber) {
		this.onlyNumber = onlyNumber;
	}

	/**
	 * Method to check whether the onlyAlphabet for the parameter is enabled or
	 * not.
	 * 
	 * @return onlyAlphabet - boolean value.
	 */

	public final boolean isOnlyAlphabet() {
		return this.onlyAlphabet;
	}

	/**
	 * Method to set the onlyAlphabet.
	 * 
	 * @param onlyAlphabet
	 *            - boolean value.
	 */
	public final void setOnlyAlphabet(final boolean onlyAlphabet) {
		this.onlyAlphabet = onlyAlphabet;
	}

	/**
	 * Method to check whether the numberAlphabet for the parameter is enabled
	 * or not.
	 * 
	 * @return numberAlphabet - boolean value.
	 */
	public final boolean isNumberAlphabet() {
		return this.numberAlphabet;
	}

	/**
	 * Method to set the numberAlphabet.
	 * 
	 * @param numberAlphabet
	 *            - boolean value.
	 */
	public final void setNumberAlphabet(final boolean numberAlphabet) {
		this.numberAlphabet = numberAlphabet;
	}

	/**
	 * Method to get the Maximum length of the parameter.
	 * 
	 * @return int - Integer value.
	 */
	public final int getMaxLength() {
		return this.maxLength;
	}

	/**
	 * Method to set the Maximum length of the parameter.
	 * 
	 * @param maxLength
	 *            - Integer value.
	 */
	public final void setMaxLength(final int maxLength) {
		this.maxLength = maxLength;
	}

	/**
	 * Method to check whether validation rules has to be executed or not.
	 * 
	 * @return required - boolean to set enable or disable.
	 */

	public final boolean isRequired() {
		return this.required;
	}

	/**
	 * Method to enable or disable the rules.
	 * 
	 * @param required
	 *            - boolean to set enable or disable.
	 */
	public final void setRequired(final boolean required) {
		this.required = required;
	}
}
