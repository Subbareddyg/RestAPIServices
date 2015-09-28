package com.belk.api.exception;

import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;

/**
 * This class handles all the exception thrown from the Domain layer.
 * 
 * @author Mindtree @ date Sep 22, 2013
 */
public class BaseException extends Exception {

	/**
	 * declaring serialVersionUID.
	 */
	private static final long serialVersionUID = -6173445095797188369L;

	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();
	/**
	 * the error code to be displayed.
	 */
	private String errorCode;
	/**
	 * The error description to be be displayed.
	 */
	private String errorDesc;

	/**
	 * the error field parameters to be displayed.
	 */
	private String errorFieldParameter;

	/**
	 * the error parameter value to be displayed.
	 */
	private String errorParameterValue;

	/**
	 * the httpStatus.
	 */
	private String httpStatus;

	/**
	 * the correlationId declaration.
	 */
	private String correlationId;

	/* Constructors */
	/**
	 * Default constructor for the Domain Exception.
	 * 
	 */
	public BaseException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param errorCode
	 *            - error code id for specific error
	 * @param errorDesc
	 *            - error description for specific error
	 * @param errorFieldParameter
	 *            - error field parameter
	 * @param errorParameterValue
	 *            - error parameter value
	 * @param httpStatus
	 *            - httpstatus
	 * @param correlationId
	 *            - to track the request
	 */
	public BaseException(final String errorCode, final String errorDesc,
			final String errorFieldParameter, final String errorParameterValue,
			final String httpStatus, final String correlationId) {
		super();
		this.errorCode = errorCode;
		this.errorDesc = errorDesc;
		this.errorFieldParameter = errorFieldParameter;
		this.errorParameterValue = errorParameterValue;
		this.httpStatus = httpStatus;
		this.correlationId = correlationId;
	}

	/**
	 * @param errorCode
	 *            - error code id for specific error
	 * @param errorDesc
	 *            - error description for specific error
	 * @param errorFieldParameter
	 *            - error field parameter
	 * @param httpStatus
	 *            - httpstatus
	 * @param correlationId
	 *            - to track the request
	 */
	public BaseException(final String errorCode, final String errorDesc,
			final String errorFieldParameter, final String httpStatus,
			final String correlationId) {
		super();
		this.errorCode = errorCode;
		this.errorDesc = errorDesc;
		this.errorFieldParameter = errorFieldParameter;
		this.httpStatus = httpStatus;
		this.correlationId = correlationId;
	}

	/**
	 * @param errorCode
	 *            - error code id for specific error
	 * @param errorDesc
	 *            - error description for specific error
	 * @param httpStatus
	 *            - httpstatus
	 * @param correlationId
	 *            - to track the request
	 */
	public BaseException(final String errorCode, final String errorDesc,
			final String httpStatus, final String correlationId) {
		super();
		this.errorCode = errorCode;
		this.errorDesc = errorDesc;
		this.httpStatus = httpStatus;
		this.correlationId = correlationId;
	}

	/**
	 * Parameterized constructor for the Domain Exception.
	 * 
	 * @param errCode
	 *            The error code against which a proper error message is to be
	 *            logged.
	 * @param correlationId
	 *            - to track the request
	 */
	public BaseException(final String errCode, final String correlationId) {
		super(errCode);
		this.errorCode = errCode;
		this.correlationId = correlationId;
	}

	/**
	 * Parameterized constructor for the Domain Exception.
	 * 
	 * @param cause
	 *            The cause that triggered the Adapter Exception.
	 * @param errCode
	 *            The error code against which a proper error message is to be
	 *            logged.
	 * @param correlationId
	 *            - to track the request
	 */
	public BaseException(final Exception cause, final String errCode,
			final String correlationId) {
		super(cause.getMessage(), cause);
		this.correlationId = correlationId;
		this.errorCode = errCode;
	}

	/**
	 * @return the errorFieldParameter
	 */
	public final String getErrorFieldParameter() {
		return this.errorFieldParameter;
	}

	/**
	 * @param errorFieldParameter
	 *            the errorFieldParameter to set
	 */
	public final void setErrorFieldParameter(final String errorFieldParameter) {
		this.errorFieldParameter = errorFieldParameter;
	}

	/**
	 * @return the errorParameterValue
	 */
	public final String getErrorParameterValue() {
		return this.errorParameterValue;
	}

	/**
	 * @param errorParameterValue
	 *            the errorParameterValue to set
	 */
	public final void setErrorParameterValue(final String errorParameterValue) {
		this.errorParameterValue = errorParameterValue;
	}

	/**
	 * @return the httpStatus
	 */
	public final String getHttpStatus() {
		return this.httpStatus;
	}

	/**
	 * @param httpStatus
	 *            the httpStatus to set
	 */
	public final void setHttpStatus(final String httpStatus) {
		this.httpStatus = httpStatus;
	}

	/* General getters and setters */
	/**
	 * Sets the errorCode value.
	 * 
	 * @param errCode
	 *            Error code
	 */
	public final void setErrorCode(final String errCode) {
		this.errorCode = errCode;
	}

	/**
	 * Gets the errorCode value.
	 * 
	 * @return The error code value
	 */
	public final String getErrorCode() {
		return this.errorCode;
	}

	/**
	 * Gets the error description.
	 * 
	 * @return The error description
	 */
	public final String getErrorDesc() {
		return this.errorDesc;
	}

	/**
	 * Sets the error description.
	 * 
	 * @param errDesc
	 *            Error description
	 */
	public final void setErrorDesc(final String errDesc) {
		this.errorDesc = errDesc;
	}

	/**
	 * @return the correlationId
	 */
	public final String getCorrelationId() {
		return this.correlationId;
	}

	/**
	 * @param correlationId
	 *            the correlationId to set
	 */
	public final void setCorrelationId(final String correlationId) {
		this.correlationId = correlationId;
	}
}
