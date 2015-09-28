package com.belk.api.validators;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * ValidationRules class defines the validation rules.
 * 
 * @author Mindtree
 * 
 */
@XmlRootElement(name = "validationRules")
@XmlAccessorType(XmlAccessType.FIELD)
public class ValidationRules {
	
	/**
	 * validateParameterEnabled is a boolean variable enabled or disabled for.
	 * validating parameter.
	 */	
	private boolean validateParameterEnabled;
	
	/**
	 * mandatoryField is a boolean variable enabled or disabled
	 * to check the parameter is mandatory or not.
	 */	
	private boolean mandatoryField;
	
	/**
	 * validationCodes specify the list of validation codes.
	 */
	private List<Integer> validationCodes;

	/**
	 * Base validation rules
	 */
	private BaseValidation baseValidation;
	
	/**
	 * Advance validation rules
	 */
	private AdvanceValidation advanceValidation;
		
	/**
	 * Get Base validation rules
	 * @return baseValidation
	 * 						- Base validation rules
	 * 
	 */
	public final BaseValidation getBaseValidation() {
		return this.baseValidation;
	}

	/**
	 * 
	 * Set the Base validation rules from configuration files
	 * @param baseValidation
	 * 					  - Base validation rules	
	 */
	public final void setBaseValidation(final BaseValidation baseValidation) {
		this.baseValidation = baseValidation;
	}

	/**
	 * get advanced validation rules
	 * @return advanceValidation
	 * 							-advanced validation rules
	 * 
	 */
	public final AdvanceValidation getAdvanceValidation() {
		return this.advanceValidation;
	}

	/**
	 * Set the advance validation rules from configuration files
	 * @param advanceValidation advance validation rules
	 */
	public final void setAdvanceValidation(final AdvanceValidation advanceValidation) {
		this.advanceValidation = advanceValidation;
	}	
	
	/**
	 * method to check whether the validateParameterEnabled is enabled or not.
	 * 
	 * @return validateParameterEnabled 
	 * 								- boolean value of the variable.
	 */
	public final boolean isValidateParameterEnabled() {
		return this.validateParameterEnabled;
	}

	/**
	 * setter method for validateParameterEnabled.
	 * 
	 * @param validateParameterEnabled
	 *            						- boolean data.
	 */
	public final void setValidateParameterEnabled(
			final boolean validateParameterEnabled) {
		this.validateParameterEnabled = validateParameterEnabled;
	}

	/**
	 * @return the mandatoryField
	 */	
	public final boolean isMandatoryField() {
		return this.mandatoryField;
	}

	/**
	 * setter method for mandatoryField.
	 * 
	 * @param mandatoryField the mandatoryField to set
	 */
	public final void setMandatoryField(final boolean mandatoryField) {
		this.mandatoryField = mandatoryField;
	}

	/**
	 * @return the validationCodes
	 */
	public final List<Integer> getValidationCodes() {
		return this.validationCodes;
	}

	/**
	 * @param validationCodes the validationCodes to set
	 */
	public final void setValidationCodes(final List<Integer> validationCodes) {
		this.validationCodes = validationCodes;
	}

}