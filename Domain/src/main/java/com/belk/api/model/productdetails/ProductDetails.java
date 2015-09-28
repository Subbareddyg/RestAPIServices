package com.belk.api.model.productdetails;

import java.util.List;

import com.belk.api.constants.CommonConstants;
import com.belk.api.model.Product;

/**
 * Representation class for list of ProductDetails.
 * Update: Added 'recommendations' property as part of API-63 
 * @author Mindtree
 * @date Oct 3, 2013
 */

public class ProductDetails extends Product {

	/**
	 * productId is the product code of the product.
	 */
	//productId as been set to empty to display the empty tag if its null as its mandatory tag.
	private String productId = CommonConstants.EMPTY_STRING;
	/**
	 * webId is private web id of the product.
	 */
	//webId as been set to empty to display the empty tag if its null as its mandatory tag.
	private String webId = CommonConstants.EMPTY_STRING;
	/**
	 * vendorId is private vendor id of the product.
	 */
	//vendorId as been set to empty to display the empty tag if its null as its mandatory tag.
	private String vendorId = CommonConstants.EMPTY_STRING;
	/**
	 * vendorPartNumber is private vendor part number of the product.
	 */
	//vendorPartNumber as been set to empty to display the empty tag if its null as its mandatory tag.
	private String vendorPartNumber = CommonConstants.EMPTY_STRING;
	/**
	 * name is private product name.
	 */
	//name as been set to empty to display the empty tag if its null as its mandatory tag.
	private String name = CommonConstants.EMPTY_STRING;
	/**
	 * private product brand.
	 */
	//brand as been set to empty to display the empty tag if its null as its mandatory tag.
	private String brand = CommonConstants.EMPTY_STRING;
	/**
	 * shortDescription is the description of the product.
	 */
	//shortDescription as been set to empty to display the empty tag if its null as its mandatory tag.
	private String shortDescription = CommonConstants.EMPTY_STRING;
	/**
	 * longDescription is the description of the product.
	 */
	//longDescription as been set to empty to display the empty tag if its null as its mandatory tag.
	private String longDescription = CommonConstants.EMPTY_STRING;
	/**
	 * productHierarchyAttributes is the product hierarchy attribute.
	 */
	private List<ProductHierarchyAttribute> productHierarchyAttributes;
	/**
	 * productPrice is the price of the product.
	 */
	private List<Price> productPrice;
	/**
	 * ratings is the rating of the product.
	 */
	private Ratings ratings;
	/**
	 * productType is the type of the product.
	 */
	//productType as been set to empty to display the empty tag if its null as its mandatory tag.
	private String productType;
	/**
	 * isPattern specifies the product is pattern type or not.
	 */
	private String isPattern;
	/**
	 * productFlags is the flag of the product.
	 */
	private List<ProductFlag> productFlags;
	/**
	 * private list of marketing attribute object.
	 */
	private List<MarketingAttribute> productMarketingAttributes;
	/**
	 * private list of attribute object.
	 */
	private List<Attribute> productAttributes;
	/**
	 * private childProducts object.
	 */
	private ChildProductList childProducts;
	/**
	 * private list of promotions object.
	 */
	private List<Promotion> promotions;
	/**
	 * private list of extendedAttributes object.
	 */
	private List<ExtendedAttribute> extendedAttributes;
	/**
	 * private list of skus object.
	 */
	private List<SKU> skus;
	
	/**
	 * private list of receommended products.
	 */
	private List<ProductDetails> recommendations;

	/**
	 * Default constructor.
	 */
	public ProductDetails() {
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
	 *            the vendorPartNumber to set.
	 */
	public final void setVendorPartNumber(final String vendorPartNumber) {
		this.vendorPartNumber = vendorPartNumber;
	}

	/**
	 * @return productId.
	 */
	public final String getProductId() {
		return this.productId;
	}

	/**
	 * @param productId
	 *            to set.
	 */
	public final void setProductId(final String productId) {
		this.productId = productId;
	}

	/**
	 * @return webId.
	 */
	public final String getWebId() {
		return this.webId;
	}

	/**
	 * @param webId
	 *            to set.
	 */
	public final void setWebId(final String webId) {
		this.webId = webId;
	}

	/**
	 * @return vendorId.
	 */
	public final String getVendorId() {
		return this.vendorId;
	}

	/**
	 * @param vendorId
	 *            to set.
	 */
	public final void setVendorId(final String vendorId) {
		this.vendorId = vendorId;
	}

	/**
	 * @return longDescription.
	 */
	public final String getLongDescription() {
		return this.longDescription;
	}

	/**
	 * @param longDescription
	 *            to set.
	 */
	public final void setLongDescription(final String longDescription) {
		this.longDescription = longDescription;
	}

	/**
	 * @return the name.
	 */
	public final String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set.
	 */
	public final void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the brand.
	 */
	public final String getBrand() {
		return this.brand;
	}

	/**
	 * @param brand
	 *            the brand to set.
	 */
	public final void setBrand(final String brand) {
		this.brand = brand;
	}

	/**
	 * @return the shortDescription.
	 */
	public final String getShortDescription() {
		return this.shortDescription;
	}

	/**
	 * @param shortDescription
	 *            the shortDescription to set.
	 */
	public final void setShortDescription(final String shortDescription) {
		this.shortDescription = shortDescription;
	}

	/**
	 * @return productHierarchyAttributes.
	 */
	public final List<ProductHierarchyAttribute> getProductHierarchyAttributes() {
		return this.productHierarchyAttributes;
	}

	/**
	 * @param productHierarchyAttributes
	 *            to set.
	 */
	public final void setProductHierarchyAttributes(
			final List<ProductHierarchyAttribute> productHierarchyAttributes) {
		this.productHierarchyAttributes = productHierarchyAttributes;
	}

	/**
	 * @return productPrice.
	 */
	public final List<Price> getProductPrice() {
		return this.productPrice;
	}

	/**
	 * @param productPrice
	 *            to set.
	 */
	public final void setProductPrice(final List<Price> productPrice) {
		this.productPrice = productPrice;
	}

	/**
	 * @return ratings.
	 */
	public final Ratings getRatings() {
		return this.ratings;
	}

	/**
	 * @param ratings
	 *            to set.
	 */
	public final void setRatings(final Ratings ratings) {
		this.ratings = ratings;
	}

	/**
	 * @return productType.
	 */
	public final String getProductType() {
		return this.productType;
	}

	/**
	 * @param productType
	 *            to set.
	 */
	public final void setProductType(final String productType) {
		this.productType = productType;
	}

	/**
	 * @return the isPattern.
	 */
	public final String getIsPattern() {
		return this.isPattern;
	}

	/**
	 * @param isPattern
	 *            the isPattern to set.
	 */
	public final void setIsPattern(final String isPattern) {
		this.isPattern = isPattern;
	}

	/**
	 * @return productFlags.
	 */
	public final List<ProductFlag> getProductFlags() {
		return this.productFlags;
	}

	/**
	 * @param productFlags
	 *            to set.
	 */
	public final void setProductFlags(final List<ProductFlag> productFlags) {
		this.productFlags = productFlags;
	}

	/**
	 * @return productMarketingAttributes.
	 */
	public final List<MarketingAttribute> getProductMarketingAttributes() {
		return this.productMarketingAttributes;
	}

	/**
	 * @param productMarketingAttributes
	 *            to set.
	 */
	public final void setProductMarketingAttributes(
			final List<MarketingAttribute> productMarketingAttributes) {
		this.productMarketingAttributes = productMarketingAttributes;
	}

	/**
	 * @return productAttributes.
	 */
	public final List<Attribute> getProductAttributes() {
		return this.productAttributes;
	}

	/**
	 * @param productAttributes
	 *            to set
	 */
	public final void setProductAttributes(
			final List<Attribute> productAttributes) {
		this.productAttributes = productAttributes;
	}

	/**
	 * @return the childProducts.
	 */
	public final ChildProductList getChildProducts() {
		return this.childProducts;
	}

	/**
	 * @param childProducts
	 *            the childProducts to set.
	 */
	public final void setChildProducts(final ChildProductList childProducts) {
		this.childProducts = childProducts;
	}

	/**
	 * @return promotions.
	 */
	public final List<Promotion> getPromotions() {
		return this.promotions;
	}

	/**
	 * @param promotions
	 *            to set.
	 */
	public final void setPromotions(final List<Promotion> promotions) {
		this.promotions = promotions;
	}

	/**
	 * @return extendedAttributes.
	 */
	public final List<ExtendedAttribute> getExtendedAttributes() {
		return this.extendedAttributes;
	}

	/**
	 * @param extendedAttributes
	 *            to set.
	 */
	public final void setExtendedAttributes(
			final List<ExtendedAttribute> extendedAttributes) {
		this.extendedAttributes = extendedAttributes;
	}

	/**
	 * @return skus.
	 */
	public final List<SKU> getSkus() {
		return this.skus;
	}

	/**
	 * @param skus
	 *            to set.
	 */
	public final void setSkus(final List<SKU> skus) {
		this.skus = skus;
	}


	/**
	 * @return the recommendations
	 */
	public final List<ProductDetails> getRecommendations() {
		return this.recommendations;
	}


	/**
	 * @param recommendations the recommendations to set
	 */
	public final void setRecommendations(final List<ProductDetails> recommendations) {
		this.recommendations = recommendations;
	}

}
