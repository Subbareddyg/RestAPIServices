package com.belk.api.model.productdetails;

import java.util.List;

/**
 * Representation class for ProductHierarchyAttributeList.
 * 
 * @author Mindtree
 * @date Jul 24, 2013
 */

public class ProductHierarchyAttributeList {
	/**
	 * productHierarchyAttributes is list of ProductHierarchyAttribute object.
	 */
	private List<ProductHierarchyAttribute> productHierarchyAttributes;

	/**
	 * Default constructor.
	 */
	public ProductHierarchyAttributeList() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return productHierarchyAttributes
	 */
	public final List<ProductHierarchyAttribute> getProductHierarchyAttributes() {
		return this.productHierarchyAttributes;
	}

	/**
	 * @param productHierarchyAttributes
	 *            to set
	 */
	public final void setProductHierarchyAttributes(
			final List<ProductHierarchyAttribute> productHierarchyAttributes) {
		this.productHierarchyAttributes = productHierarchyAttributes;
	}

}
