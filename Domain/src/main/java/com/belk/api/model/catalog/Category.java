/**
 * 
 */
package com.belk.api.model.catalog;

import java.io.Serializable;
import java.util.List;

import com.belk.api.constants.CommonConstants;

/**
 * Category representation class.
 * 
 * Updated : Code modified to initialize mandatory nodes to empty string, as part of
 * Mandatory tags CR
 * 
 * @author Mindtree
 * 
 */
public class Category implements Serializable {
	/**
	 * serialVersionUID specifies default serial version id.
	 */
	private static final long serialVersionUID = 3552422081044624941L;
	/**
	 * categoryId specifies category id. 
	 * Can not assign empty string to long
	 * variable and assigning default value as zero.
	 */
	private long categoryId = 0L;
	/**
	 * name specifies category name.
	 * added No Value as it is a mandatory tag.
	 */
	private String name = CommonConstants.EMPTY_STRING;
	/**
	 * parentCategoryId specifies parent category id.
	 * Can not assign empty string to long
	 * variable and assigning default value as zero.
	 */
	private long parentCategoryId = 0L;
	/**
	 * categoryAttributes specify list of category.
	 */
	private List<Attribute> categoryAttributes;
	/**
	 * subCategories specifies list of sub categories.
	 */
	private List<Category> subCategories;
	/**
	 * products specifies list of products.
	 */
	private List<Product> products;

	/**
	 * Default constructor.
	 */
	public Category() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the categoryId
	 */
	public final long getCategoryId() {
		return this.categoryId;
	}

	/**
	 * @param categoryId
	 *            the categoryId to set
	 */
	public final void setCategoryId(final long categoryId) {
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
	public final long getParentCategoryId() {
		return this.parentCategoryId;
	}

	/**
	 * @param parentCategoryId
	 *            the parentCategoryId to set
	 */
	public final void setParentCategoryId(final long parentCategoryId) {
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
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
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

	/**
	 * @return the products
	 */
	public final List<Product> getProducts() {
		return this.products;
	}

	/**
	 * @param products
	 *            the products to set
	 */
	public final void setProducts(final List<Product> products) {
		this.products = products;
	}

}
