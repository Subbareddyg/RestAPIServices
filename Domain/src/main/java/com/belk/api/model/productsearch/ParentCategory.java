package com.belk.api.model.productsearch;

import java.util.List;

/**
 * Representation class for a category. 
 * @author Mindtree
 * @date Oct 22 2013
 */
public class ParentCategory {

	/**
	 * categoryAttributes is list of Attribute.
	 */
	private List<Attribute> categoryAttributes;
	/**
	 * subCategories is list of SubCategory.
	 */
	private List<SubCategory> subCategories;
	/**
	 * Default constructor.
	 */
	public ParentCategory() {
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
	/**
	 * @return the subCategories
	 */
	public final List<SubCategory> getSubCategories() {
		return this.subCategories;
	}
	/**
	 * @param subCategories the subCategories to set
	 */
	public final void setSubCategories(final List<SubCategory> subCategories) {
		this.subCategories = subCategories;
	}
	
	
}
