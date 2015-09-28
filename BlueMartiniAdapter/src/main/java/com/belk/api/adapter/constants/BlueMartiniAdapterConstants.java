package com.belk.api.adapter.constants;


/**
 * General constants specifically required for the BlueMArtiniAdapter project.
 * Updated: some of the constants removed as part of the phase2, April,14 release
 * @author Mindtree
 * @date Nov 11th, 2013
 */
public final class BlueMartiniAdapterConstants {
	/**
	 * Final String General Constant specifically required for the
	 * BlueMArtiniAdapter.
	 */
	public static final String PATTERN_DETAILS = "patternDetails";

	/**
	 * TRUE_VALUE String containing "T".
	 */
	public static final String TRUE_VALUE = "T";

	/**
	 * FALSE_VALUE String containing "F".
	 */
	public static final String FALSE_VALUE = "F";

	/**
	 * General Constant specifically required for the BlueMArtiniAdapter. Using
	 * this constant value ResourceBundle value is obtained.
	 */
	public static final String HTTP_RESOURCE_BUNDLE = "httpConnection.properties";

	/**
	 * HTTP_SOCKET_TIMEOUT specifies http.socket.timeout to method.
	 */
	public static final String HTTP_SOCKET_TIMEOUT = "http.socket.timeout";
	
	/**
	 * HTTP_SOCKET_TIMEOUT specifies http.socket.timeout to method.
	 */
	public static final String HTTP_CONNECTION_TIMEOUT = "http.connection.timeout";

	//key names from http connection property file.

	/**
	 * CONNECTION_TIMEOUT specifies connection timeout value.
	 */
	public static final String CONNECTION_TIMEOUT = "CONNECTION_TIMEOUT";
	/**
	 * SOCKET_TIMEOUT specifies socket timeout value.
	 */
	public static final String SOCKET_TIMEOUT = "SOCKET_TIMEOUT";
	
	/**
	 * PATTERN_DETAILS_URL specifies connection url to bluemartini.
	 */
	public static final String PATTERN_DETAILS_URL = "PATTERN_DETAILS_URL";
	
	/**
	 * Default constructor.
	 */
	private BlueMartiniAdapterConstants() {

	}

}
