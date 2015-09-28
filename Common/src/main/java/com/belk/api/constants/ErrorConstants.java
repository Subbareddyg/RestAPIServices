package com.belk.api.constants;

/**
 * This class contains the common error constants and messages used in the
 * application.
 * 
 * This class has been updated with consistent error code implementation as part
 * of CR: CR_BELK_API_5
 * 
 * Updated: some of the unused constants have been removed as part of the
 * phase2, April,14 release
 * 
 * @author Mindtree
 * @date Sep 24, 2013
 */
public final class ErrorConstants {
	/**
	 * HTTP_STATUS_CODE_INTERNAL_ERROR refers to the internal error 500.
	 */
	public static final String HTTP_STATUS_CODE_INTERNAL_ERROR = "500";

	/**
	 * HTTP_STATUS_CODE_EMPTY_SEARCH_RESULT refers to the internal error 404.
	 */
	public static final String HTTP_STATUS_CODE_EMPTY_SEARCH_RESULT = "404";
	/**
	 * HTTP_STATUS_CODE_INVALID_URL_PARAMETERS refers to 400 error.
	 */
	public static final String HTTP_STATUS_CODE_INVALID_URL_PARAMETERS = "400";
	/**
	 * ERRORDESC_MESSAGE refers to error message.
	 */
	public static final String ERRORDESC_MESSAGE = "Error Occured";
	/**
	 * ERRORDESC_INTERNAL_ERROR refers to internal error message.
	 */
	public static final String ERRORDESC_INTERNAL_ERROR = "Internal error processing request.";
	/**
	 * ERRORDESC_RECORD_NOT_FOUND refers to the error message.
	 */
	public static final String ERRORDESC_RECORD_NOT_FOUND = "Record Not Found.";
	/**
	 * ERRORDESC_INVALID_URL_PARAMETERS refers to error message.
	 */
	public static final String ERRORDESC_INVALID_URL_PARAMETERS = "Bad Request:Request cannot be fulfilled because of incorrect url.";
	/**
	 * ERROR_DETAILS specifies the error details.
	 */

	public static final String ERROR_DETAILS = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
			+ "<response>\n<code>";
	/**
	 * ERROR_CODE specifies the error code.
	 */
	public static final String ERROR_CODE = "</code>\n<description>";
	/**
	 * ERROR_DETAILS_END refers to error details.
	 */
	public static final String ERROR_DETAILS_END = "</description>\n</response>";
	/**
	 * ERROR_CODE_11421 refers to error code 11421.
	 */
	public static final int ERROR_CODE_11421 = 11421;
	/**
	 * ERROR_CODE_11422 refers to error code 11422.
	 */
	public static final int ERROR_CODE_11422 = 11422;
	/**
	 * ERROR_CODE_11423 refers to error code 11423.
	 */
	public static final int ERROR_CODE_11423 = 11423;
	/**
	 * ERROR_CODE_11424 refers to error code 11424.
	 */
	public static final int ERROR_CODE_11424 = 11424;
	/**
	 * ERROR_CODE_11425 refers to error code 11425.
	 */
	public static final int ERROR_CODE_11425 = 11425;
	/**
	 * ERROR_CODE_11426 refers to error code 11426.
	 */
	public static final int ERROR_CODE_11426 = 11426;
	/**
	 * ERROR_CODE_11427 refers to error code 11427.
	 */
	public static final int ERROR_CODE_11427 = 11427;
	/**
	 * ERROR_CODE_11428 refers to error code 11428.
	 */
	public static final int ERROR_CODE_11428 = 11428;
	/**
	 * ERROR_CODE_11429 refers to error code 11429.
	 */
	public static final int ERROR_CODE_11429 = 11429;
	/**
	 * ERROR_CODE_11430 refers to error code 11430.
	 */
	public static final int ERROR_CODE_11430 = 11430;
	/**
	 * ERROR_CODE_11431 refers to error code 11431.
	 */
	public static final int ERROR_CODE_11431 = 11431;
	/**
	 * ERROR_CODE_11432 refers to error code 11432.
	 */
	public static final int ERROR_CODE_11432 = 11432;
	/**
	 * ERROR_CODE_11433 refers to error code 11433.
	 */
	public static final int ERROR_CODE_11433 = 11433;
	/**
	 * ERROR_CODE_11434 refers to error code 11434.
	 */
	public static final int ERROR_CODE_11434 = 11434;
	/**
	 * ERROR_CODE_11435 refers to error code 11435.
	 */
	public static final int ERROR_CODE_11435 = 11435;
	/**
	 * ERROR_CODE_11521 refers to error code 11521.
	 */
	public static final int ERROR_CODE_11521 = 11521;
	/**
	 * ERROR_CODE_11522 refers to error code 11522.
	 */
	public static final int ERROR_CODE_11522 = 11522;
	/**
	 * ERROR_CODE_11523 refers to error code 11523.
	 */
	public static final int ERROR_CODE_11523 = 11523;
	/**
	 * ERROR_CODE_11436 refers to error code 11436.
	 */
	public static final int ERROR_CODE_11436 = 11436;
	/**
	 * ERROR_CODE_1235 refers to error code 1235.
	 */
	public static final int ERROR_CODE_1235 = 1235;

	/**
	 * ERROR_CODE_1233 refers to error code 1233.
	 */
	public static final int ERROR_CODE_1233 = 1233;

	/**
	 * ERROR_CODE_8 refers to error code 8.
	 */
	public static final int ERROR_CODE_8 = 8;

	/**
	 * ERROR_CODE_INVALID_TOKEN refers to error due to invalid token.
	 */
	public static final int ERROR_CODE_INVALID_TOKEN = 11524;

	/**
	 * ERROR_CODE_EXPIRED_TOKEN refers to error due to expired token.
	 */
	public static final int ERROR_CODE_EXPIRED_TOKEN = 11525;

	/**
	 * HTTP_STATUS_CODE_UNAUTHORIZED refers to error due to unauthorized access.
	 */
	public static final String HTTP_STATUS_CODE_UNAUTHORIZED = "401";

	/**
	 * ERROR_CODE_EXPIRED_REQUEST refers to the error due to expired request.
	 */
	public static final int ERROR_CODE_EXPIRED_REQUEST = 11526;

	/**
	 * Default constructor.
	 */
	private ErrorConstants() {

	}
}
