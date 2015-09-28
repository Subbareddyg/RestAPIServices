package com.belk.api.model.productdetails;

import java.util.List;

/**
 * Representation class for ChildProductList.
 * 
 * @author Mindtree
 * @date Jul 24, 2013
 */

public class ChildProductList {
	/**
	 * childProduct is list of ChildProduct objects.
	 */
	private List<ChildProduct> childProduct;
	/**
	 * private collection type.
	 */
	private String collectionType;

	/**
	 * Default constructor.
	 */
	public ChildProductList() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the childProduct
	 */
	public final List<ChildProduct> getChildProduct() {
		return this.childProduct;
	}

	/**
	 * @param childProduct
	 *            to set
	 */
	public final void setChildProduct(final List<ChildProduct> childProduct) {
		this.childProduct = childProduct;
	}

	/**
	 * @return collectionType
	 */
	public final String getCollectionType() {
		return this.collectionType;
	}

	/**
	 * @param collectionType
	 *            to set
	 */
	public final void setCollectionType(final String collectionType) {
		this.collectionType = collectionType;
	}
}
