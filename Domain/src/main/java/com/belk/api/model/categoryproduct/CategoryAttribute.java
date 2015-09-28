package com.belk.api.model.categoryproduct;

/**
 * Representation class for CategoryAttribute.
 * 
 * @author Mindtree
 * @date Oct 25, 2013
 */
public class CategoryAttribute {
	/**
	 * hasSubCategories specifies whether given category has sub categories or
	 * not.
	 */
	private String hasSubCategories;
	/**
	 * totalProducts specifies number of products for given category.
	 */
	private String totalProducts;
	/**
	 * totalSkus specifies number of skus for given category.
	 */
	private String totalSkus;

	/**
	 * Default Constructor.
	 */
	public CategoryAttribute() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return hasSubCategories
	 */

	public final String getHasSubCategories() {
		return this.hasSubCategories;
	}

	/**
	 * @param hasSubCategories
	 *            to set
	 */
	public final void setHasSubCategories(final String hasSubCategories) {
		this.hasSubCategories = hasSubCategories;
	}

	/**
	 * @return totalProducts
	 */
	public final String getTotalProducts() {
		return this.totalProducts;
	}

	/**
	 * @param totalProducts
	 *            to set
	 */
	public final void setTotalProducts(final String totalProducts) {
		this.totalProducts = totalProducts;
	}

	/**
	 * @return totalSkus
	 */
	public final String getTotalSkus() {
		return this.totalSkus;
	}

	/**
	 * @param totalSkus
	 *            to set
	 */
	public final void setTotalSkus(final String totalSkus) {
		this.totalSkus = totalSkus;
	}

}
