package com.belk.api.model.categorydetails;

import java.util.List;

import com.belk.api.constants.CommonConstants;

/**
 * Representation class for an Endeca category.
 * 
 * Updated : Code modified to initialize mandatory nodes to empty string, as part of
 * Mandatory tags CR
 * 
 * @author Mindtree
 * @date Nov 06, 2013
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
	 * subCategories specify list of sub categories objects.
	 */
	private List<SubCategory> subCategories;

	/**
	 * Default constructor.
	 */
	public Category() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the categoryId
	 */
	public final String getCategoryId() {
		return this.categoryId;
	}

	/**
	 * @param categoryId
	 *            the categoryId to set
	 */
	public final void setCategoryId(final String categoryId) {
		this.categoryId = categoryId;
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
	 * @return the parentCategoryId
	 */
	public final String getParentCategoryId() {
		return this.parentCategoryId;
	}

	/**
	 * @param parentCategoryId
	 *            the parentCategoryId to set
	 */
	public final void setParentCategoryId(final String parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}

	/**
	 * @return the categoryAttributes
	 */
	public final List<Attribute> getCategoryAttributes() {
		return this.categoryAttributes;
	}

	/**
	 * @param categoryAttributes
	 *            the categoryAttributes to set
	 */
	public final void setCategoryAttributes(
			final List<Attribute> categoryAttributes) {
		this.categoryAttributes = categoryAttributes;
	}

	/**
	 * @return the subCategories
	 */
	public final List<SubCategory> getSubCategories() {
		return this.subCategories;
	}

	/**
	 * @param subCategories
	 *            the subCategories to set
	 */
	public final void setSubCategories(final List<SubCategory> subCategories) {
		this.subCategories = subCategories;
	}

}
