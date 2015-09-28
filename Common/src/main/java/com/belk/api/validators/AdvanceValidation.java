package com.belk.api.validators;

/**
 * Advance Validation POJO Class
 * 
 * @author mindtree
 * @date Feb 21, 2014
 * 
 */
public class AdvanceValidation {

	/**
	 * enable or disable advancevalidation
	 */
	private boolean required;
	/**
	 * regex is to define the regular expression for validation.
	 */
	private Regex regex;

	/**
	 * paramValueValidate is a boolean variable enabled or disabled for
	 * parameter value validation
	 */
	private boolean paramValueValidate;

	/**
	 * sortParamValidate is a boolean variable enabled or disabled for sort
	 * parameter value validation
	 */
	private boolean sortParamValidate;
	
	/**
	 * optionParameterValidate is a boolean variable enabled or disabled for options
	 * parameter value validation
	 */
	private boolean optionsParamValidate;

	/**
	 * Method to get the regular expression of the parameter.
	 * 
	 * @return regex - Instance of Regex class.
	 */
	public final Regex getRegex() {
		return this.regex;
	}

	/**
	 * Method to set the regular expression of the parameter.
	 * 
	 * @param regex
	 *            - Instance of Regex class.
	 */
	public final void setRegex(final Regex regex) {
		this.regex = regex;
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

	/**
	 * @return the paramValueValidate
	 */
	public final boolean isParamValueValidate() {
		return this.paramValueValidate;
	}

	/**
	 * setter method for paramValueValidate.
	 * 
	 * @param paramValueValidate
	 *            the paramValueValidate to set
	 */
	public final void setParamValueValidate(final boolean paramValueValidate) {
		this.paramValueValidate = paramValueValidate;
	}

	/**
	 * @return the sortParamValidate
	 */
	public final boolean isSortParamValidate() {
		return this.sortParamValidate;
	}

	/**
	 * setter method for sortParamValidate.
	 * 
	 * @param sortParamValidate
	 *            the sortParamValidate to set
	 */
	public final void setSortParamValidate(final boolean sortParamValidate) {
		this.sortParamValidate = sortParamValidate;
	}

	/**
	 * @return the optionsParamValidate
	 */
	public final boolean isOptionsParamValidate() {
		return this.optionsParamValidate;
	}

	/**
	 * @param optionsParamValidate the optionsParamValidate to set
	 */
	public final void setOptionsParamValidate(final boolean optionsParamValidate) {
		this.optionsParamValidate = optionsParamValidate;
	}


}
