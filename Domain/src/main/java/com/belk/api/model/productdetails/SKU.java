package com.belk.api.model.productdetails;

import java.util.List;

/**
 * Representation class for list of Ratings.
 * 
 * @author Mindtree
 * @date Jul 24, 2013
 */

public class SKU {
	/**
	 * skuCode is code of SKU.
	 */
	//skuCode as been set to empty to display the empty tag if its null as its mandatory tag.
	private String skuCode = "";
	/**
	 * upcCode is upc code of SKU.
	 */
	//upcCode as been set to empty to display the empty tag if its null as its mandatory tag.
	private String upcCode = "";
	/**
	 * skuId is ID of SKU.
	 */
	//skuId as been set to empty to display the empty tag if its null as its mandatory tag.
	private String skuId = "";
	/**
	 * orinSku is orin code of SKU.
	 */
	//orinSku as been set to empty to display the empty tag if its null as its mandatory tag.
	private String orinSku = "";
	
	/**
	 * skuShortDesc is the short description for a SKU.
	 */
	//skuShortDesc has been set to empty to display the empty tag if its null as its mandatory tag.
	private String skuShortDesc = "";
	/**
	 * skuMainAttributes is list of SKUMain.
	 */
	private List<SKUMain> skuMainAttributes;
	/**
	 * skuPrice is list of Price.
	 */
	private List<Price> skuPrice;
	/**
	 * productSKUImages is list of ProductSKUImage.
	 */
	private List<ProductSKUImage> productSKUImages;
	/**
	 * skuInventory is inventory of SKU.
	 */
	private SKUInventory skuInventory;
	/**
	 * skuImages is image of SKU.
	 */
	private SKUImageList skuImages;
	/**
	 * extendedAttribute is list of ExtendedAttribute.
	 */
	private List<ExtendedAttribute> extendedAttribute;

	/**
	 * Default constructor.
	 */
	public SKU() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the skuCode
	 */
	public final String getSkuCode() {
		return this.skuCode;
	}

	/**
	 * @param skuCode
	 *            the skuCode to set
	 */
	public final void setSkuCode(final String skuCode) {
		this.skuCode = skuCode;
	}

	/**
	 * @return the upcCode
	 */
	public final String getUpcCode() {
		return this.upcCode;
	}

	/**
	 * @param upcCode
	 *            the upcCode to set
	 */
	public final void setUpcCode(final String upcCode) {
		this.upcCode = upcCode;
	}

	/**
	 * @return the skuId
	 */
	public final String getSkuId() {
		return this.skuId;
	}

	/**
	 * @param skuId the skuId to set
	 */
	public final void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	/**
	 * @return the orinSku
	 */
	public final String getOrinSku() {
		return this.orinSku;
	}

	/**
	 * @param orinSku
	 *            the orinSku to set
	 */
	public final void setOrinSku(final String orinSku) {
		this.orinSku = orinSku;
	}

	/**
	 * @return the skuMainAttributes
	 */
	public final List<SKUMain> getSkuMainAttributes() {
		return this.skuMainAttributes;
	}

	/**
	 * @param skuMainAttributes
	 *            the skuMainAttributes to set
	 */
	public final void setSkuMainAttributes(final List<SKUMain> skuMainAttributes) {
		this.skuMainAttributes = skuMainAttributes;
	}

	/**
	 * @return the skuPrice
	 */
	public final List<Price> getSkuPrice() {
		return this.skuPrice;
	}

	/**
	 * @param skuPrice
	 *            the skuPrice to set
	 */
	public final void setSkuPrice(final List<Price> skuPrice) {
		this.skuPrice = skuPrice;
	}

	/**
	 * @return the productSKUImages
	 */
	public final List<ProductSKUImage> getProductSKUImages() {
		return this.productSKUImages;
	}

	/**
	 * @param productSKUImages
	 *            the productSKUImages to set
	 */
	public final void setProductSKUImages(
			final List<ProductSKUImage> productSKUImages) {
		this.productSKUImages = productSKUImages;
	}

	/**
	 * @return the skuInventory
	 */
	public final SKUInventory getSkuInventory() {
		return this.skuInventory;
	}

	/**
	 * @param skuInventory
	 *            the skuInventory to set
	 */
	public final void setSkuInventory(final SKUInventory skuInventory) {
		this.skuInventory = skuInventory;
	}

	/**
	 * @return the skuImages
	 */
	public final SKUImageList getSkuImages() {
		return this.skuImages;
	}

	/**
	 * @param skuImages
	 *            the skuImages to set
	 */
	public final void setSkuImages(final SKUImageList skuImages) {
		this.skuImages = skuImages;
	}

	/**
	 * @return the extendedAttribute
	 */
	public final List<ExtendedAttribute> getExtendedAttribute() {
		return this.extendedAttribute;
	}

	/**
	 * @param extendedAttribute
	 *            the extendedAttribute to set
	 */
	public final void setExtendedAttribute(
			final List<ExtendedAttribute> extendedAttribute) {
		this.extendedAttribute = extendedAttribute;
	}

	/**
	 * @return the skuShortDesc
	 */
	public final String getSkuShortDesc() {
		return this.skuShortDesc;
	}

	/**
	 * @param skuShortDesc the skuShortDesc to set
	 */
	public final void setSkuShortDesc(final String skuShortDesc) {
		this.skuShortDesc = skuShortDesc;
	}

}
