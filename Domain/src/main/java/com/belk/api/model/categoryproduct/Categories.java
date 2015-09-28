package com.belk.api.model.categoryproduct;

import java.util.List;

/**
 * Representation class for List of category.
 * 
 * @author Mindtree
 * @date Oct 18th, 2013
 */

public class Categories {
	/**
	 * categories specifies list of category objects.
	 */
	private List<Category> category;

	/**
	 * Default constructor.
	 */
	public Categories() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the category
	 */

	public final List<Category> getCategory() {
		return this.category;
	}

	/**
	 * @param category to set
	 */

	public final void setCategory(final List<Category> category) {
		this.category = category;
	}

}
