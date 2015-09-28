package com.belk.api.model.productsearch;

import java.util.List;

import com.belk.api.constants.CommonConstants;

/**
 * Representation class for product.
 * 
 * @author Mindtree
 * @date Oct 3, 2013
 */
public class ProductSearch {

	/**
	 * productCode is code of product.
	 */
	// productCode as been set to empty to display the empty tag if its null as
	// its mandatory tag.
	private String productCode = CommonConstants.EMPTY_STRING;
	/**
	 * productId is id of product.
	 */
	// productId as been set to empty to display the empty tag if its null as
	// its mandatory tag.
	private String productId = CommonConstants.EMPTY_STRING;;
	/**
	 * webId is webid of product.
	 */
	// webId as been set to empty to display the empty tag if its null as
	// its mandatory tag.
	private String webId = CommonConstants.EMPTY_STRING;
	/**
	 * vendorId is vendorId of product.
	 */
	// vendorId as been set to empty to display the empty tag if its null as its
	// mandatory tag.
	private String vendorId = CommonConstants.EMPTY_STRING;
	/**
	 * vendorPartNumber is vendor part number of product.
	 */
	// vendorPartNumber as been set to empty to display the empty tag if its
	// null as its mandatory tag.
	private String vendorPartNumber = CommonConstants.EMPTY_STRING;
	/**
	 * brand is brand of product.
	 */
	// brand as been set to empty to display the empty tag if its null as its
	// mandatory tag.
	private String brand = CommonConstants.EMPTY_STRING;
	/**
	 * name is name of product.
	 */
	// name as been set to empty to display the empty tag if its null as its
	// mandatory tag.
	private String name = CommonConstants.EMPTY_STRING;
	/**
	 * productPrice is price of product.
	 */
	private List<Price> productPrice;
	/**
	 * ratings is rating of product.
	 */
	private Ratings ratings;
	/**
	 * productType is type of product.
	 */
	// productType as been set to empty to display the empty tag if its null as
	// its mandatory tag.
	private String productType = CommonConstants.EMPTY_STRING;
	/**
	 * productFlags is list of ProductFlag.
	 */
	private List<ProductFlag> productFlags;
	/**
	 * promotions is list of Promotion.
	 */
	private List<Promotion> promotions;
	/**
	 * productAttributes is list of Attribute.
	 */
	private List<Attribute> productAttributes;
	/**
	 * extendedAttributes is list of Attribute.
	 */
	private List<Attribute> extendedAttributes;
	/**
	 * private childProducts object.
	 */
	private ChildProductList childProducts;
	/**
	 * Default constructor.
	 */
	public ProductSearch() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the vendorPartNumber
	 */
	public final String getVendorPartNumber() {
		return this.vendorPartNumber;
	}

	/**
	 * @param vendorPartNumber
	 *            the vendorPartNumber to set
	 */
	public final void setVendorPartNumber(final String vendorPartNumber) {
		this.vendorPartNumber = vendorPartNumber;
	}

	/**
	 * @return the productCode
	 */
	public final String getProductCode() {
		return this.productCode;
	}

	/**
	 * @param productCode
	 *            the productCode to set
	 */
	public final void setProductCode(final String productCode) {
		this.productCode = productCode;
	}

	/**
	 * @return the productId
	 */
	public final String getProductId() {
		return this.productId;
	}

	/**
	 * @param productId
	 *            the productId to set
	 */
	public final void setProductId(final String productId) {
		this.productId = productId;
	}

	/**
	 * @return the webId
	 */
	public final String getWebId() {
		return this.webId;
	}

	/**
	 * @param webId
	 *            the webId to set
	 */
	public final void setWebId(final String webId) {
		this.webId = webId;
	}

	/**
	 * @return the vendorId
	 */
	public final String getVendorId() {
		return this.vendorId;
	}

	/**
	 * @param vendorId
	 *            the vendorId to set
	 */
	public final void setVendorId(final String vendorId) {
		this.vendorId = vendorId;
	}

	/**
	 * @return the brand
	 */
	public final String getBrand() {
		return this.brand;
	}

	/**
	 * @param brand
	 *            the brand to set
	 */
	public final void setBrand(final String brand) {
		this.brand = brand;
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
	 * @return the productPrice
	 */
	public final List<Price> getProductPrice() {
		return this.productPrice;
	}

	/**
	 * @param productPrice
	 *            the productPrice to set
	 */
	public final void setProductPrice(final List<Price> productPrice) {
		this.productPrice = productPrice;
	}

	/**
	 * @return the ratings
	 */
	public final Ratings getRatings() {
		return this.ratings;
	}

	/**
	 * @param ratings
	 *            the ratings to set
	 */
	public final void setRatings(final Ratings ratings) {
		this.ratings = ratings;
	}

	/**
	 * @return the productType
	 */
	public final String getProductType() {
		return this.productType;
	}

	/**
	 * @param productType
	 *            the productType to set
	 */
	public final void setProductType(final String productType) {
		this.productType = productType;
	}

	/**
	 * @return the productFlags
	 */
	public final List<ProductFlag> getProductFlags() {
		return this.productFlags;
	}

	/**
	 * @param productFlags
	 *            the productFlags to set
	 */
	public final void setProductFlags(final List<ProductFlag> productFlags) {
		this.productFlags = productFlags;
	}

	/**
	 * @return the promotions
	 */
	public final List<Promotion> getPromotions() {
		return this.promotions;
	}

	/**
	 * @param promotions
	 *            the promotions to set
	 */
	public final void setPromotions(final List<Promotion> promotions) {
		this.promotions = promotions;
	}

	/**
	 * @return the productAttributes
	 */
	public final List<Attribute> getProductAttributes() {
		return this.productAttributes;
	}

	/**
	 * @param productAttributes
	 *            the productAttributes to set
	 */
	public final void setProductAttributes(
			final List<Attribute> productAttributes) {
		this.productAttributes = productAttributes;
	}

	/**
	 * @return the extendedAttributes
	 */
	public final List<Attribute> getExtendedAttributes() {
		return this.extendedAttributes;
	}

	/**
	 * @param extendedAttributes
	 *            the extendedAttributes to set
	 */
	public final void setExtendedAttributes(
			final List<Attribute> extendedAttributes) {
		this.extendedAttributes = extendedAttributes;
	}

	/**
	 * @return the childProducts
	 */
	public final ChildProductList getChildProducts() {
		return this.childProducts;
	}

	/**
	 * @param childProducts the childProducts to set
	 */
	public final void setChildProducts(final ChildProductList childProducts) {
		this.childProducts = childProducts;
	}

}
