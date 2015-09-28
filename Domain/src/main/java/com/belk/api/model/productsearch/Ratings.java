/**
 * 
 */
package com.belk.api.model.productsearch;

/** 
 * Representation class for product ratings.
 * @author Mindtree
 * @date Oct 22 2013
 */
public class Ratings {

	/**
	 * overallRating is overall rating of product.
	 */
	private String overallRating;
	/**
	 * reviewCount is review count of ratings.
	 */
	private String reviewCount;
	/**
	 * Default constructor.
	 */
	public Ratings() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the overallRating
	 */
	public final String getOverallRating() {
		return this.overallRating;
	}
	/**
	 * @param overallRating the overallRating to set
	 */
	public final void setOverallRating(final String overallRating) {
		this.overallRating = overallRating;
	}
	/**
	 * @return the reviewCount
	 */
	public final String getReviewCount() {
		return this.reviewCount;
	}
	/**
	 * @param reviewCount the reviewCount to set
	 */
	public final void setReviewCount(final String reviewCount) {
		this.reviewCount = reviewCount;
	}
	
	
	
}
