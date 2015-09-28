package com.belk.api.model.productdetails;

/**
 * Representation class for Attribute.
 * 
 * @author Mindtree
 * @date Jul 24, 2013
 */
public class Attribute {

	/**
	 * Attribute Key.
	 */
	private String key;
	/**
	 * Attribute value.
	 */
	private String value;

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
	 * @return VALUE
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
