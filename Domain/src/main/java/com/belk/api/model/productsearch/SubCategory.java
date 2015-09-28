package com.belk.api.model.productsearch;

import java.util.List;

/**
 * Representation class for a sub category.
 * @author Mindtree
 * @date Oct 22 2013
 */
public class SubCategory {

	/**
	 * categoryAttributes is list of Attribute.
	 */
	private List<Attribute> categoryAttributes;
	/**
	 * Default constructor.
	 */
	public SubCategory() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the categoryAttributes
	 */
	public final List<Attribute> getCategoryAttributes() {
		return this.categoryAttributes;
	}

	/**
	 * @param categoryAttributes the categoryAttributes to set
	 */
	public final void setCategoryAttributes(final List<Attribute> categoryAttributes) {
		this.categoryAttributes = categoryAttributes;
	}
	
	
}
