/**
 * 
 */
package com.belk.api.security;

/**
 * This class is used to map to the jibx binding for authentication token.
 * 
 * @author Mindtree
 * @date 10 Jun, 2014
 */
public class AuthenticationDetails {

	private String authToken;

	/**
	 * Default constructor
	 */
	public AuthenticationDetails() {
		super();
	}

	/**
	 * @param authToken
	 *            Authentication token
	 */
	public AuthenticationDetails(final String authToken) {
		super();
		this.authToken = authToken;
	}

	/**
	 * @return the authToken
	 */
	public final String getAuthToken() {
		return this.authToken;
	}

	/**
	 * @param authToken
	 *            the authToken to set
	 */
	public final void setAuthToken(final String authToken) {
		this.authToken = authToken;
	}

}
