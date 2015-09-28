package com.belk.api.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.belk.api.constants.CommonConstants;
import com.belk.api.model.catalog.Attribute;
import com.belk.api.model.catalog.Product;

/**
 * Representation class for Dimension.
 * 
 * Update: This class has been modified to meet the requirement of reading the
 * hasSubcategories flag for a dimension not from the configured parameters of
 * Endeca but as per the dimension tree's structure. isLeaf property got added.
 * 
 * Updated : Code modified to initialize mandatory nodes to empty string, as part of
 * Mandatory tags CR
 * 
 * @author Mindtree
 * @date Nov 11, 2013
 */
public class Dimension implements Serializable {

	/**
	 * serialVersionUID specifies default serial version id.
	 */
	private static final long serialVersionUID = -5331907259798712101L;
	/**
	 * id specifies dimension ID. 
	 * Can not assign empty string to long variable
	 * and assigning default value as zero.
	 */
	private long dimensionId = 0L;
	/**
	 * name specifies dimension name.
	 * added No Value as it is a mandatory tag.
	 */
	private String name = CommonConstants.EMPTY_STRING;
	/**
	 * parentDimensionId specifies parent dimension id.
	 * Can not assign empty string to long variable
	 * and assigning default value as zero.
	 */
	private long parentDimensionId = 0L;
	/**
	 * dimensionAttributes specifies list of dimension attributes. 
	 */
	private List<Attribute> dimensionAttributes;
	/**
	 * dimensions specifies list of dimensions.
	 */
	private List<Dimension> dimensions = new LinkedList<Dimension>();
	/**
	 * products specifies list of products.
	 */
	private List<Product> products;

	/**
	 * Flag specifying whether the dimension is a leaf or not.
	 */
	private boolean isLeaf = true;

	/**
	 * 
	 * @param id
	 *            dimension Id
	 * @param name
	 *            name of the dimension
	 * @param parentDimensionId
	 *            dimension id of the parent
	 * @param dimensionAttributes
	 *            list of attributes
	 * @param dimensions
	 *            list of dimension
	 * @param products
	 *            list of products
	 */
	public Dimension(final long id, final String name,
			final long parentDimensionId,
			final List<Attribute> dimensionAttributes,
			final List<Dimension> dimensions, final List<Product> products) {
		super();
		this.dimensionId = id;
		this.name = name;
		this.parentDimensionId = parentDimensionId;
		this.dimensionAttributes = dimensionAttributes;
		this.dimensions = dimensions;
		this.products = products;
	}

	/**
	 * Default constructor.
	 */
	public Dimension() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * return the dimension id.
	 * 
	 * @param id
	 *            dimension Id
	 */
	public Dimension(final long id) {
		this.dimensionId = id;
	}

	/**
	 * method returns the Id.
	 * 
	 * @return the id
	 */
	public final long getDimensionId() {
		return this.dimensionId;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public final void setDimensionId(final long id) {
		this.dimensionId = id;
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
	 * @return the parentDimensionId
	 */
	public final long getParentDimensionId() {
		return this.parentDimensionId;
	}

	/**
	 * @param parentDimensionId
	 *            the parentDimensionId to set
	 */
	public final void setParentDimensionId(final long parentDimensionId) {
		this.parentDimensionId = parentDimensionId;
	}

	/**
	 * @return the dimensionAttributes
	 */
	public final List<Attribute> getDimensionAttributes() {
		return this.dimensionAttributes;
	}

	/**
	 * @param dimensionAttributes
	 *            the dimensionAttributes to set
	 */
	public final void setDimensionAttributes(
			final List<Attribute> dimensionAttributes) {
		this.dimensionAttributes = dimensionAttributes;
	}

	/**
	 * @return the dimensions
	 */
	public final List<Dimension> getDimensions() {
		return this.dimensions;
	}

	/**
	 * @param dimensions
	 *            the dimensions to set
	 */
	public final void setDimensions(final List<Dimension> dimensions) {
		this.dimensions = dimensions;
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

	/**
	 * method is to add dimensions.
	 * 
	 * @param dimensions
	 *            object dimension
	 */
	public final void addDimensions(final Dimension dimensions) {
		this.dimensions.add(dimensions);
	}

	/**
	 * method to check whether there are further dimensions.
	 * 
	 * @return boolean data
	 */
	public final boolean hasDimensions() {
		return !this.dimensions.isEmpty();
	}

	/**
	 * Method to specify whether this dimension is a leaf or not.
	 * 
	 * @return boolean data
	 */
	public final boolean isLeaf() {
		return this.isLeaf;
	}

	/**
	 * Method to set whether this dimension is leaf or not.
	 * 
	 * @param isLeaf
	 *            Boolean value to be set for a dimension.
	 */
	public final void setLeaf(final boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

}
