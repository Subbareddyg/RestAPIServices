package com.belk.api.model.productdetails;

/**
 * Representation class for ExtendedAttribute.
 * 
 * @author Mindtree
 * @date Jul 24, 2013
 */
public class ExtendedAttribute {

	/**
	 * ExtendedAttribute Key.
	 */
	private String key;
	/**
	 * ExtendedAttribute value.
	 */
	private String value;

	/**
	 * Default constructor.
	 */
	public ExtendedAttribute() {
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
