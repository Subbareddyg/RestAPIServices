package com.belk.api.model.productdetails;

/**
 * Representation class for list of Ratings.
 * 
 * @author Mindtree
 * @date Jul 24, 2013
 */
public class Ratings {
	/**
	 * overallRating is overall rating.
	 */
	private String overallRating;
	/**
	 * reviewCount is count of ratings.
	 */
	private String reviewCount;

	/**
	 * Default constructor.
	 */
	public Ratings() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return overallRating
	 */
	public final String getOverallRating() {
		return this.overallRating;
	}

	/**
	 * @param overallRating
	 *            to set
	 */
	public final void setOverallRating(final String overallRating) {
		this.overallRating = overallRating;
	}

	/**
	 * @return reviewCount
	 */
	public final String getReviewCount() {
		return this.reviewCount;
	}

	/**
	 * @param reviewCount
	 *            to set
	 */
	public final void setReviewCount(final String reviewCount) {
		this.reviewCount = reviewCount;
	}

}
