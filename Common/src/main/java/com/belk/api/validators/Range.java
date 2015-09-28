package com.belk.api.validators;

/**
 * Representation class for range.
 * 
 * @author Mindtree
 * 
 */
public class Range {

	/**
	 * rangeCheck is to check the range.
	 */
	private boolean rangeCheck;
	/**
	 * minimum is to check the minimum range.
	 */
	private int minimum;

	/**
	 * maximum is to check the maximum range.
	 */
	private int maximum;

	/**
	 * @return the rangeCheck
	 */
	public final boolean isRangeCheck() {
		return this.rangeCheck;
	}

	/**
	 * @param rangeCheck
	 *            the rangeCheck to set
	 */
	public final void setRangeCheck(final boolean rangeCheck) {
		this.rangeCheck = rangeCheck;
	}

	/**
	 * @return the minimum
	 */
	public final int getMinimum() {
		return this.minimum;
	}

	/**
	 * @param minimum
	 *            the minimum to set
	 */
	public final void setMinimum(final int minimum) {
		this.minimum = minimum;
	}

	/**
	 * @return the maximum
	 */
	public final int getMaximum() {
		return this.maximum;
	}

	/**
	 * @param maximum
	 *            the maximum to set
	 */
	public final void setMaximum(final int maximum) {
		this.maximum = maximum;
	}

}
