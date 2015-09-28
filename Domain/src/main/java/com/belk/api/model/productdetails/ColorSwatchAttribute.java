package com.belk.api.model.productdetails;

/**
 * Representation class for ColorSwatchAttribute. Add: This class has been added
 * as a part of CR: CR_BELK_API_9.
 * 
 * @author Mindtree
 * @date Jul 24, 2013
 */

public class ColorSwatchAttribute {
	/**
	 * ColorSwatchAttribute key.
	 */
	private String key;
	/**
	 * ColorSwatchAttribute value.
	 */
	private String value;

	/**
	 * Default constructor.
	 */
	public ColorSwatchAttribute() {
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
