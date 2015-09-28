/**
 * 
 */
package com.belk.api.model.productsearch;

/**
 * Representation class for various promotions related to a product.
 * 
 * @author Mindtree
 * @date Oct 21 2013
 */
public class Promotion {

	/**
	 * Promotion key.
	 */
	private String key;
	/**
	 * Promotion value.
	 */
	private String value;

	/**
	 * Default constructor.
	 */
	public Promotion() {
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
	 * @return the value
	 */
	public final String getValue() {
		return this.value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public final void setValue(final String value) {
		this.value = value;
	}

}
