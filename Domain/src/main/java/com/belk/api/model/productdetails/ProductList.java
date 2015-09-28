package com.belk.api.model.productdetails;

import java.util.List;

/**
 * Representation class for list of ProductList.
 * 
 * @author Mindtree
 * @date Jul 24, 2013
 */
public class ProductList {

	/**
	 * products is list of ProductDetails objects.
	 */
	private List<ProductDetails> products;

	/**
	 * Default constructor.
	 */
	public ProductList() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return products
	 */
	public final List<ProductDetails> getProducts() {
		return this.products;
	}

	/**
	 * @param products
	 *            to set
	 */
	public final void setProducts(final List<ProductDetails> products) {
		this.products = products;
	}

}
