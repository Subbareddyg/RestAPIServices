package com.belk.api.model.catalog;

import java.io.Serializable;
import java.util.List;

/**
 * Representation class for Catalog.
 * 
 * @author Mindtree
 * @date Nov 06, 2013
 */
public class Catalog implements Serializable {

	/**
	 * serialVersionUID specifies default serial version id.
	 */
	private static final long serialVersionUID = 5659477023853290185L;
	/**
	 * catalogId specifies catalog id.
	 */
	private String catalogId;
	/**
	 * catalogName specifies catalog name.
	 */
	private String catalogName;
	/**
	 * categories specifies list of categories.
	 */
	private List<Category> categories;

	/**
	 * Default constructor.
	 */
	public Catalog() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the catalogId
	 */
	public final String getCatalogId() {
		return this.catalogId;
	}

	/**
	 * @param catalogId
	 *            - catalogId to set
	 */
	public final void setCatalogId(final String catalogId) {
		this.catalogId = catalogId;
	}

	/**
	 * @return the catalogName
	 */
	public final String getCatalogName() {
		return this.catalogName;
	}

	/**
	 * @param catalogName
	 *            - catalogName to set
	 */
	public final void setCatalogName(final String catalogName) {
		this.catalogName = catalogName;
	}

	/**
	 * @return the categories
	 */
	public final List<Category> getCategories() {
		return this.categories;
	}

	/**
	 * @param categories
	 *            the categories to set
	 */
	public final void setCategories(final List<Category> categories) {
		this.categories = categories;
	}

}
