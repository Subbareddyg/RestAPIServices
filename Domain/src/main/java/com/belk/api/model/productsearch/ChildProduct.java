package com.belk.api.model.productsearch;

/**
 * Representation class for ChildProduct.
 * 
 * @author Mindtree
 * @date Jul 24, 2013
 */
public class ChildProduct {

	/**
	 * ChildProduct Key.
	 */
	private String key;
	/**
	 * ChildProduct value.
	 */
	private String value;

	/**
	 * Default constructor.
	 */
	public ChildProduct() {
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
