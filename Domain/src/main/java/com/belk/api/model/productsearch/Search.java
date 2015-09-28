package com.belk.api.model.productsearch;

import java.util.List;


/** Representation class for search response.
 * @author Mindtree
 * @date Sep 27, 2013
 */

public class Search {
	/**
	 * products is list of ProductSearch.
	 */
	private List<ProductSearch> products;
	/**
	 * categories is list of ParentCategory.
	 */
	private List<ParentCategory> categories;
	/**
	 * dimensions is list of Dimension.
	 */
	private List<Dimension> dimensions;
	/**
	 * searchReport is search report.
	 */
	private SearchReport searchReport;
	/**
	 * Default constructor.
	 */
	public Search() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the products
	 */
	public final List<ProductSearch> getProducts() {
		return this.products;
	}
	/**
	 * @param products the products to set
	 */
	public final void setProducts(final List<ProductSearch> products) {
		this.products = products;
	}
	
	
	/**
	 * @return the categories
	 */
	public final List<ParentCategory> getCategories() {
		return this.categories;
	}
	/**
	 * @param categories the categories to set
	 */
	public final void setCategories(final List<ParentCategory> categories) {
		this.categories = categories;
	}
	/**
	 * @return the dimensions
	 */
	public final List<Dimension> getDimensions() {
		return this.dimensions;
	}
	/**
	 * @param dimensions the dimensions to set
	 */
	public final void setDimensions(final List<Dimension> dimensions) {
		this.dimensions = dimensions;
	}
	/**
	 * @return the searchReport
	 */
	public final SearchReport getSearchReport() {
		return this.searchReport;
	}
	/**
	 * @param searchReport the searchReport to set
	 */
	public final void setSearchReport(final SearchReport searchReport) {
		this.searchReport = searchReport;
	}
	
	
}
