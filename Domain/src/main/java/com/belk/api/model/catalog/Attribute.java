package com.belk.api.model.catalog;

import java.io.Serializable;

import com.belk.api.constants.CommonConstants;

/**
 * Representation class for Attribute.
 * 
 * Updated : Code modified to initialize mandatory nodes to empty string, as part of
 * Mandatory tags CR
 * 
 * @author Mindtree
 * @date Nov 06, 2013
 */
public class Attribute implements Serializable {

	/**
	 * serialVersionUID specifies default serial version id.
	 */
	private static final long serialVersionUID = 5741735896557318077L;

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
	 * @return key
	 */
	public final String getKey() {
		return this.key;
	}

	/**
	 * @param key
	 *            to set
	 */
	public final void setKey(final String key) {
		this.key = key;
	}

	/**
	 * @return value
	 */
	public final String getValue() {
		return this.value;
	}

	/**
	 * @param value
	 *            to set
	 */
	public final void setValue(final String value) {
		this.value = value;
	}

}
