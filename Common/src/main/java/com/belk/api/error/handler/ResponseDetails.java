package com.belk.api.error.handler;

/**
 * @author Mindtree
 * @date Nov 21 2013
 */
public class ResponseDetails {

	/**
	 * code specify the code for error.
	 */
	private String code;
	/**
	 * description is the description of the error.
	 */
	private String description;
	/**
	 * parametername is the name of the parameter for error.
	 */
	private String parametername;
	/**
	 * parametervalue is the value of the parameter for error.
	 */
	private String parametervalue;

	/**
	 * httpStatus specify the http status.
	 */
	private String httpStatus;

	/**
	 * Default constructor.
	 */
	public ResponseDetails() {
		super();
	}

	/**
	 * @param errorCode
	 *            - error code id for specific error
	 * @param errorDescription
	 *            - error description for specific error
	 * @param errorFieldParameter
	 *            - error parameter name
	 * @param errorParameterValue
	 *            - error parameter value
	 */
	public ResponseDetails(final String errorCode, final String errorDescription,
			final String errorFieldParameter, final String errorParameterValue) {
		super();
		this.code = errorCode;
		this.description = errorDescription;
		this.parametername = errorFieldParameter;
		this.parametervalue = errorParameterValue;
	}
	

	/**
	 * @return the code
	 */
	public final String getCode() {
		return this.code;
	}

	/**
	 * @param code the code to set
	 */
	public final void setCode(final String code) {
		this.code = code;
	}

	/**
	 * @return the description
	 */
	public final String getDescription() {
		return this.description;
	}

	/**
	 * @param description the description to set
	 */
	public final void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @return the parametername
	 */
	public final String getParametername() {
		return this.parametername;
	}

	/**
	 * @param parametername the parametername to set
	 */
	public final void setParametername(final String parametername) {
		this.parametername = parametername;
	}

	/**
	 * @return the parametervalue
	 */
	public final String getParametervalue() {
		return this.parametervalue;
	}

	/**
	 * @param parametervalue the parametervalue to set
	 */
	public final void setParametervalue(final String parametervalue) {
		this.parametervalue = parametervalue;
	}

	/**
	 * @return the httpStatus
	 */
	public final String getHttpStatus() {
		return this.httpStatus;
	}

	/**
	 * @param httpStatus the httpStatus to set
	 */
	public final void setHttpStatus(final String httpStatus) {
		this.httpStatus = httpStatus;
	}

}