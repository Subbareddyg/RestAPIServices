package com.belk.api.model.categoryproduct;

import com.belk.api.constants.CommonConstants;

/**
 * Representation class for Attribute.
 * 
 * Updated : Code modified to initialize mandatory nodes to empty string, as part of
 * Mandatory tags CR
 * 
 * @author Mindtree
 * @date Oct 25, 2013
 */

public class Attribute {

	/**
	 * key specifies attribute key.
	 * added No Value as it is a mandatory tag.
	 */
	private String key = CommonConstants.EMPTY_STRING;
	/**
	 * value specifies attribute value.
	 * added No Value as it is a mandatory tag.
	 */
	private String value = CommonConstants.EMPTY_STRING;

	/**
	 * Default constructor.
	 */
	public Attribute() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the key
	 */
	public final String getKey() {
		return this.key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public final void setKey(final String key) {
		this.key = key;
	}

	/**
	 * @return the values
	 */
	public final String getValue() {
		return this.value;
	}

	/**
	 * @param value
	 *            the values to set
	 */
	public final void setValue(final String value) {
		this.value = value;
	}

}
