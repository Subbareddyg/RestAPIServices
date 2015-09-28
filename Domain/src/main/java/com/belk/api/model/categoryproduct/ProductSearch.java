package com.belk.api.model.categoryproduct;

import com.belk.api.constants.CommonConstants;

/**
 * Representation class for Product that has been searched for.
 * 
 * Updated : Code modified to initialize mandatory nodes to empty string, as part of
 * Mandatory tags CR 
 * 
 * @author Mindtree
 * @date Oct 18th, 2013
 */
public class ProductSearch {

	/**
	 * productCode specifies product code.
	 * added No Value as it is a mandatory tag.
	 */
	private String productCode = CommonConstants.EMPTY_STRING;
	/**
	 * productId specifies product id.
	 * added No Value as it is a mandatory tag.
	 */
	private String productId = CommonConstants.EMPTY_STRING;
	/**
	 * webId specifies web id.
	 * added No Value as it is a mandatory tag.
	 */
	private String webId = CommonConstants.EMPTY_STRING;

	/**
	 * Default constructor.
	 */
	public ProductSearch() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return productCode
	 */
	public final String getProductCode() {
		return this.productCode;
	}

	/**
	 * @param productCode
	 *            to set
	 */
	public final void setProductCode(final String productCode) {
		this.productCode = productCode;
	}

	/**
	 * @return productId
	 */
	public final String getProductId() {
		return this.productId;
	}

	/**
	 * @param productId
	 *            to set
	 */
	public final void setProductId(final String productId) {
		this.productId = productId;
	}

	/**
	 * @return webId
	 */
	public final String getWebId() {
		return this.webId;
	}

	/**
	 * @param webId
	 *            to set
	 */
	public final void setWebId(final String webId) {
		this.webId = webId;
	}

}
