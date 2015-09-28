package com.belk.api.model.catalog;

import java.io.Serializable;

import com.belk.api.constants.CommonConstants;

/**
 * Representation class for Product.
 * 
 * Updated : Code modified to initialize mandatory nodes to empty string, as part of
 * Mandatory tags CR
 * 
 * @author Mindtree
 * @date Nov 06, 2013
 */
public class Product implements Serializable {

	/**
	 * serialVersionUID specifies default serial version id.
	 */
	private static final long serialVersionUID = -1261891544980201570L;
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

}
