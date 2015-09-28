package com.belk.api.validators;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Representation class for validation path configuration.
 * 
 * @author Mindtree
 * 
 */
@XmlRootElement(name = "validationConfig")
@XmlAccessorType(XmlAccessType.FIELD)
public class ValidationConfig {

	/**
	 * apiServicesConfigMap specify the configuration map of <String, Boolean>.
	 */
	private Map<String, Boolean> apiServicesConfigMap = new HashMap<String, Boolean>();

	/**
	 * validatorPathConfigMap specify the map of <String,String>.
	 */
	private Map<String, String> validatorPathConfigMap = new HashMap<String, String>();

	/**
	 * @return the validatorPathConfigMap
	 */
	public final Map<String, String> getValidatorPathConfigMap() {
		return this.validatorPathConfigMap;
	}

	/**
	 * @param validatorPathConfigMap
	 *            the validatorPathConfigMap to set
	 */
	public final void setValidatorPathConfigMap(
			final Map<String, String> validatorPathConfigMap) {
		this.validatorPathConfigMap = validatorPathConfigMap;
	}

	/**
	 * @return the apiServicesConfigMap.
	 */
	public final Map<String, Boolean> getApiServicesConfigMap() {
		return this.apiServicesConfigMap;
	}

	/**
	 * @param apiServicesConfigMap
	 *            the apiServicesConfigMap to set.
	 */
	public final void setApiServicesConfigMap(
			final Map<String, Boolean> apiServicesConfigMap) {
		this.apiServicesConfigMap = apiServicesConfigMap;
	}

}
