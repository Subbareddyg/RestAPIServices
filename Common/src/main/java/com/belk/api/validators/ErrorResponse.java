package com.belk.api.validators;

/**
 * This class defines the error codes and the error description.
 * 
 * @author Mindtree
 * 
 */
public class ErrorResponse {

	/**
	 * parameterName is the name of the parameter.
	 */
	private String parameterName;

	/**
	 * parameterValue is the value of the parameter.
	 */
	private String parameterValue;

	/**
	 * errorDescription is the description of the error.
	 */
	private String errorDescription;

	/**
	 * errorCode is the code for the error.
	 */
	private int errorCode;

	/**
	 * Method to get the parameter name.
	 * 
	 * @return parameterName - name of the parameter of type String.
	 */
	public final String getParameterName() {
		return this.parameterName;
	}

	/**
	 * Method to set the parameter name.
	 * 
	 * @param parameterName
	 *            - String value containing parameter name.
	 */
	public final void setParameterName(final String parameterName) {
		this.parameterName = parameterName;
	}

	/**
	 * Method to get the parameter value.
	 * 
	 * @return parameterValue - value of the parameter of type String.
	 */
	public final String getParameterValue() {
		return this.parameterValue;
	}

	/**
	 * Method to set the parameter value.
	 * 
	 * @param parameterValue
	 *            - String value containing parameter value.
	 */
	public final void setParameterValue(final String parameterValue) {
		this.parameterValue = parameterValue;
	}

	/**
	 * Method to get the error description.
	 * 
	 * @return errorDescription - description of the error.
	 */
	public final String getErrorDescription() {
		return this.errorDescription;
	}

	/**
	 * Method to set the error description.
	 * 
	 * @param errorDescription
	 *            - String value containing description of the error.
	 */
	public final void setErrorDescription(final String errorDescription) {
		this.errorDescription = errorDescription;
	}

	/**
	 * Method to get the error code.
	 * 
	 * @return errorCode - integer value containing error code.
	 */
	public final int getErrorCode() {
		return this.errorCode;
	}

	/**
	 * Method to set the error code.
	 * 
	 * @param errorCode
	 *            - integer value containing error code.
	 */
	public final void setErrorCode(final int errorCode) {
		this.errorCode = errorCode;
	}

}
