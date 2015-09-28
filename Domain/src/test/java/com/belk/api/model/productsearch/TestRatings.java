package com.belk.api.model.productsearch;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * 
 * Unit Testing related to Rating class The unit test cases evaluates the way
 * the methods behave for the inputs given.
 * 
 * @author Mindtree
 * @date 29 Oct, 2013
 * 
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Ratings.class })
public class TestRatings extends TestCase {

	/**
	 * Method to test setter and getter for overallRating
	 */
	@Test
	public final void testoverallRating() {
		final Ratings ratings = new Ratings();
		ratings.setOverallRating("Good");
		assertTrue(ratings.getOverallRating() == "Good");
	}

	/**
	 * Method to test setter and getter for reviewCount
	 */
	@Test
	public final void testReviewCount() {
		final Ratings ratings = new Ratings();
		ratings.setReviewCount("10");
		assertTrue(ratings.getReviewCount() == "10");
	}

}
