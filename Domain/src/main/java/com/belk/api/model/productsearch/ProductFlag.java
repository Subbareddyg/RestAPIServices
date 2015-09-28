package com.belk.api.model.productsearch;

/**
 * Representation class for various product flags.
 * @author Mindtree
 * @date Oct 21 2013
 */
public class ProductFlag {

	/**
	 * ProductFlag key.
	 */
	private String key;
	/**
	 * ProductFlag value.
	 */
	private String value;
	/**
	 * Default constructor.
	 */
	public ProductFlag() {
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
