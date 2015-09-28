package com.belk.api.validators;

/**
 * Representation class for Regular Expression.
 * 
 * @author Mindtree
 * 
 */

public class Regex {
	/**
	 * regexCheck specify the regular expression check.
	 */
	private boolean regexCheck;
	/**
	 * regex specify the regular expression.
	 */
	private String regex;
	
	/**
	 * splitter specify the input param splitter.
	 */
	private String splitter;

	/**
	 * @return the regexCheck
	 */
	public final boolean isRegexCheck() {
		return this.regexCheck;
	}

	/**
	 * @param regexCheck
	 *            the regexCheck to set
	 */
	public final void setRegexCheck(final boolean regexCheck) {
		this.regexCheck = regexCheck;
	}

	/**
	 * @return the regex
	 */
	public final String getRegex() {
		return this.regex;
	}

	/**
	 * @param regex
	 *            the regex to set
	 */
	public final void setRegex(final String regex) {
		this.regex = regex;
	}

	/**
	 * @return the splitter
	 */
	public final String getSplitter() {
		return this.splitter;
	}

	/**
	 * @param splitter the splitter to set
	 */
	public final void setSplitter(final String splitter) {
		this.splitter = splitter;
	}
	

}
