package com.belk.api.validators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Representation class for validation configuration.
 * 
 * @author Mindtree
 * 
 */
@XmlRootElement(name = "resourceValidationConfig")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResourceValidationConfig {

	/**
	 * validateResourceEnabled specify whether the resources are enabled or not.
	 */
	private boolean validateResourceEnabled;

	/**
	 * parameterValidationMap specify the HashMap of <String, ValidationRules>.
	 */
	private Map<String, ValidationRules> parameterValidationMap = new HashMap<String, ValidationRules>();
	
	/**
	 * mandatoryFields specify the list of mandatory fields.
	 */
	private List<String> mandatoryFields;

	/**
	 * 
	 * @return the validateResourceEnabled
	 */
	public final boolean isValidateResourceEnabled() {
		return this.validateResourceEnabled;
	}

	/**
	 * @param validateResourceEnabled
	 *            the validateResourceEnabled to set
	 */
	public final void setValidateResourceEnabled(
			final boolean validateResourceEnabled) {
		this.validateResourceEnabled = validateResourceEnabled;
	}

	/**
	 * @return the parameterValidationMap
	 */
	public final Map<String, ValidationRules> getParameterValidationMap() {
		return this.parameterValidationMap;
	}

	/**
	 * @param parameterValidationMap
	 *            the parameterValidationMap to set
	 */
	public final void setParameterValidationMap(
			final Map<String, ValidationRules> parameterValidationMap) {
		this.parameterValidationMap = parameterValidationMap;
	}

	/**
	 * @return the mandatoryFields
	 */
	public final List<String> getMandatoryFields() {
		return this.mandatoryFields;
	}

	/**
	 * @param mandatoryFields the mandatoryFields to set
	 */
	public final void setMandatoryFields(final List<String> mandatoryFields) {
		this.mandatoryFields = mandatoryFields;
	}
	

}