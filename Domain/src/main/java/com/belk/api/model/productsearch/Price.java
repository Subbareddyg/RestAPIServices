package com.belk.api.model.productsearch;

/**
 * Representation class for list of all the prices associated with a product.
 * @author Mindtree
 * @date Sep 27, 2013
 */
public class Price {
	/**
	 * Price key.
	 */
	private String key;
	/**
	 * Price value.
	 */
	private String value;
	/**
	 * Default constructor.
	 */
	public Price() {
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
	 * @return the value
	 */
	public final String getValue() {
		return this.value;
	}
	/**
	 * @param value the value to set
	 */
	public final void setValue(final String value) {
		this.value = value;
	}
	
}
