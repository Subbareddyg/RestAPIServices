package com.belk.api.model.productdetails;

import java.util.List;

import com.belk.api.constants.CommonConstants;

/**
 * Representation class for list of ProductSKUImage.
 * 
 * @author Mindtree
 * @date Jul 24, 2013
 */

public class ProductSKUImage {
	/**
	 * skuImageAttribute is list of SKUImage objects.
	 */
	private List<SKUImage> skuImageAttribute;
	/**
	 * defaultImage is private String.
	 */
	//defaultImage as been set to empty to display the empty tag if its null as its mandatory tag.
	private String defaultImage = CommonConstants.EMPTY_STRING;

	/**
	 * Default constructor.
	 */
	public ProductSKUImage() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return skuImageAttribute
	 */
	public final List<SKUImage> getSkuImageAttribute() {
		return this.skuImageAttribute;
	}

	/**
	 * @param skuImageAttribute
	 *            to set
	 */
	public final void setSkuImageAttribute(
			final List<SKUImage> skuImageAttribute) {
		this.skuImageAttribute = skuImageAttribute;
	}

	/**
	 * @return defaultImage
	 */
	public final String getDefaultImage() {
		return this.defaultImage;
	}

	/**
	 * @param defaultImage
	 *            to set
	 */
	public final void setDefaultImage(final String defaultImage) {
		this.defaultImage = defaultImage;
	}

}
