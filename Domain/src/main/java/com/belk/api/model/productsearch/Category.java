package com.belk.api.model.productsearch;

import java.util.List;

/**
 * Representation class for an Endeca category.
 * 
 * @author Mindtree
 * @date Oct 10, 2013
 */
public class Category {

	/**
	 * Category id.
	 */
	private String categoryId;
	/**
	 * Category name.
	 */
	private String name;
	/**
	 * subCategories is list of Category.
	 */
	private List<Category> subCategories;

	/**
	 * Default constructor.
	 */
	public Category() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id
	 */
	public final String getCategoryId() {
		return this.categoryId;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public final void setCategoryId(final String id) {
		this.categoryId = id;
	}

	/**
	 * @return the name
	 */
	public final String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public final void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the subCategories
	 */
	public final List<Category> getSubCategories() {
		return this.subCategories;
	}

	/**
	 * @param subCategories
	 *            the subCategories to set
	 */
	public final void setSubCategories(final List<Category> subCategories) {
		this.subCategories = subCategories;
	}

}
