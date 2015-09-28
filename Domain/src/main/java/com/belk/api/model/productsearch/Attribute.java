package com.belk.api.model.productsearch;


/** Representation class for product attributes.
 * @author Mindtree
 * @date Sep 27, 2013
 */

public class Attribute {
	/**
	 * Attribute key.
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
	 * @return the key
	 */
	public final String getKey() {
		return this.key;
	}

	/**
	 * @param key the key to set
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
	 * @param value the values to set
	 */
	public final void setValue(final String value) {
		this.value = value;
	}

	

}
