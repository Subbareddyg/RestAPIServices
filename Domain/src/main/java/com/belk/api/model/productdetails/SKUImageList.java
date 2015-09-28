package com.belk.api.model.productdetails;

import java.util.List;

/**
 * Pojo for SKU Image List.
 * 
 * @author Mindtree
 * @date Jul 24, 2013
 */
public class SKUImageList {
	/**
	 * colorSwatchAttributes is list of ColorSwatchAttribute.
	 */
	private List<ColorSwatchAttribute> colorSwatchAttributes;
	/**
	 * productSKUImages is list of ProductSKUImage.
	 */
	private List<ProductSKUImage> productSKUImages;

	/**
	 * Default constructor.
	 */
	public SKUImageList() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the colorSwatchAttributes
	 */
	public final List<ColorSwatchAttribute> getColorSwatchAttributes() {
		return this.colorSwatchAttributes;
	}

	/**
	 * @param colorSwatchAttributes
	 *            the colorSwatchAttributes to set
	 */
	public final void setColorSwatchAttributes(
			final List<ColorSwatchAttribute> colorSwatchAttributes) {
		this.colorSwatchAttributes = colorSwatchAttributes;
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

}
