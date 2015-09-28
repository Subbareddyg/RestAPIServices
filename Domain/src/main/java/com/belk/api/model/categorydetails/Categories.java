package com.belk.api.model.categorydetails;

import java.util.List;

/**
 * Representation class for List of category.
 * 
 * @author Mindtree
 * @date Nov 06, 2013
 */

public class Categories {

	/**
	 * categories specifies list of category objects.
	 */
	private List<Category> categories;

	/**
	 * Default constructor.
	 */
	public Categories() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the categories
	 */
	public final List<Category> getCategories() {
		return this.categories;
	}

	/**
	 * @param categories
	 *            the categories to set
	 */
	public final void setCategories(final List<Category> categories) {
		this.categories = categories;
	}

}
