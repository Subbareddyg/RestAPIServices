package com.belk.api.model.productsearch;

import java.util.List;

import com.belk.api.constants.CommonConstants;

/** Representation class for a search report.
 * @author Mindtree
 * @date Sep 27, 2013
 */

public class SearchReport {

	/**
	 * totalProducts is total products for search keyword.
	 */
	//totalProducts as been set to empty to display the empty tag if its null as its mandatory tag.
	private String totalProducts = CommonConstants.EMPTY_STRING;
	/**
	 * totalSkus is total skus for search keyword.
	 */
	//totalSkus as been set to empty to display the empty tag if its null as its mandatory tag.
	private String totalSkus = CommonConstants.EMPTY_STRING;
	/**
	 * keyword is the search keyword.
	 */
	//keyword as been set to empty to display the empty tag if its null as its mandatory tag.
	private String keyword = CommonConstants.EMPTY_STRING;
	/**
	 * refinementId is id.
	 */
	//refinementId as been set to empty to display the empty tag if its null as its mandatory tag.
	private String refinementId = CommonConstants.EMPTY_STRING;
	/**
	 * limit is the limit of search.
	 */
	private String limit;
	/**
	 * offset is the offset of search.
	 */
	private String offset;
	/**
	 * attributes is list of SearchAttribute.
	 */
	private List<SearchAttribute> attributes;
	/**
	 * sortFields is list of String.
	 */
	private List<String> sortFields;
	/**
	 * Default constructor.
	 */
	public SearchReport() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the totalProducts
	 */
	public final String getTotalProducts() {
		return this.totalProducts;
	}
	/**
	 * @param totalProducts the totalProducts to set
	 */
	public final void setTotalProducts(final String totalProducts) {
		this.totalProducts = totalProducts;
	}
	/**
	 * @return the totalSkus
	 */
	public final String getTotalSkus() {
		return this.totalSkus;
	}
	/**
	 * @param totalSkus the totalSkus to set
	 */
	public final void setTotalSkus(final String totalSkus) {
		this.totalSkus = totalSkus;
	}
	/**
	 * @return the keyword
	 */
	public final String getKeyword() {
		return this.keyword;
	}
	/**
	 * @param keyword the keyword to set
	 */
	public final void setKeyword(final String keyword) {
		this.keyword = keyword;
	}
	/**
	 * @return the refinementId
	 */
	public final String getRefinementId() {
		return this.refinementId;
	}
	/**
	 * @param refinementId the refinementId to set
	 */
	public final void setRefinementId(final String refinementId) {
		this.refinementId = refinementId;
	}
	/**
	 * @return the limit
	 */
	public final String getLimit() {
		return this.limit;
	}
	/**
	 * @param limit the limit to set
	 */
	public final void setLimit(final String limit) {
		this.limit = limit;
	}
	/**
	 * @return the offset
	 */
	public final String getOffset() {
		return this.offset;
	}
	/**
	 * @param offset the offset to set
	 */
	public final void setOffset(final String offset) {
		this.offset = offset;
	}
	/**
	 * @return the attributes
	 */
	public final List<SearchAttribute> getAttributes() {
		return this.attributes;
	}
	/**
	 * @param attributes the attributes to set
	 */
	public final void setAttributes(final List<SearchAttribute> attributes) {
		this.attributes = attributes;
	}
	/**
	 * @return the sortFields
	 */
	public final List<String> getSortFields() {
		return this.sortFields;
	}
	/**
	 * @param sortFields the sortFields to set
	 */
	public final void setSortFields(final List<String> sortFields) {
		this.sortFields = sortFields;
	}
	
}
