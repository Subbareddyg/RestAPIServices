package com.belk.api.model.categoryproduct;

import java.util.List;

import com.belk.api.constants.CommonConstants;

/**
 * Representation class for Category.
 * 
 * Updated : Code modified to initialize mandatory nodes to empty string, as part of
 * Mandatory tags CR
 * 
 * @author Mindtree
 * @date Oct 18th, 2013
 */

public class Category {

	/**
	 * categoryId specifies category id.
	 * added No Value as it is a mandatory tag.
	 */
	private String categoryId = CommonConstants.EMPTY_STRING;
	/**
	 * name specifies category name.
	 * added No Value as it is a mandatory tag.
	 */
	private String name = CommonConstants.EMPTY_STRING;
	/**
	 * parentCategoryId specifies parent category id.
	 * added No Value as it is a mandatory tag.
	 */
	private String parentCategoryId = CommonConstants.EMPTY_STRING;
	/**
	 * categoryAttributes specifies list of category attributes.
	 */
	private List<Attribute> categoryAttributes;
	/**
	 * products specifies list of products.
	 */
	private List<ProductSearch> products;

	/**
	 * Default Constructor.
	 */
	public Category() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return categoryId
	 */

	public final String getCategoryId() {
		return this.categoryId;
	}

	/**
	 * @param categoryId
	 *            to set
	 */
	public final void setCategoryId(final String categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * @return name
	 */
	public final String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            to set
	 */

	public final void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return parentCategoryId
	 */
	public final String getParentCategoryId() {
		return this.parentCategoryId;
	}

	/**
	 * @param parentCategoryId
	 *            to set
	 */

	public final void setParentCategoryId(final String parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}

	/**
	 * @return categoryAttributes
	 */

	public final List<Attribute> getCategoryAttributes() {
		return this.categoryAttributes;
	}

	/**
	 * @param categoryAttributes
	 *            to set
	 */

	public final void setCategoryAttributes(
			final List<Attribute> categoryAttributes) {
		this.categoryAttributes = categoryAttributes;
	}

	/**
	 * @return products
	 */
	public final List<ProductSearch> getProducts() {
		return this.products;
	}

	/**
	 * @param products
	 *            to set
	 */
	public final void setProducts(final List<ProductSearch> products) {
		this.products = products;
	}

}