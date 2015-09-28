package com.belk.api.model.productsearch;

import java.util.List;

/** Representation class for search attributes.
 * @author Mindtree
 * @date Sep 27, 2013
 */

public class SearchAttribute {

	/**
	 * SearchAttribute key.
	 */
	private String key;
	/**
	 * values is list of values.
	 */
	private List<String> values;
	/**
	 * Default constructor.
	 */
	public SearchAttribute() {
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
	public final List<String> getValues() {
		return this.values;
	}

	/**
	 * @param values the values to set
	 */
	public final void setValues(final List<String> values) {
		this.values = values;
	}

	

}
