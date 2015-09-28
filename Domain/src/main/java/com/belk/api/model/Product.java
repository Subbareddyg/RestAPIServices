package com.belk.api.model;

import com.belk.api.constants.CommonConstants;

/** Base class for Product containing the common attributes a product should have.
 * Specific product representations for APIs should extend this class.
 * @author Mindtree
 * @date Jul 24, 2013
 */

public class Product {
	/**
	 * productCode specifies product code.
	 */
	//productCode as been set to empty to display the empty tag if its null as its mandatory tag.
	private String productCode = CommonConstants.EMPTY_STRING;
	/**
	 * Default constructor.
	 */
	public Product() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the productCode
	 */
	public final String getProductCode() {
		return this.productCode;
	}
	/**
	 * @param productCode the productCode to set
	 */
	public final void setProductCode(final String productCode) {
		this.productCode = productCode;
	}


}

