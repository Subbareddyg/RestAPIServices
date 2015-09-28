package com.belk.api.exception;

/**
 * This class handles all the exception thrown from the Service layer.
 * 
 * @author Mindtree @ date Sep 22, 2013
 */
public class ServiceException extends BaseException {
	/**
	 * declaring serialVersionUID.
	 */
	private static final long serialVersionUID = -6173445095797188369L;

	/**
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
	public ServiceException(final String errorCode, final String errorDesc,
			final String errorFieldParameter, final String errorParameterValue,
			final String httpStatus, final String correlationId) {
		super(errorCode, errorDesc, errorFieldParameter, errorParameterValue,
				httpStatus, correlationId);

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
	public ServiceException(final String errorCode, final String errorDesc,
			final String errorFieldParameter, final String httpStatus,
			final String correlationId) {
		super(errorCode, errorDesc, errorFieldParameter, httpStatus,
				correlationId);

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
	public ServiceException(final String errorCode, final String errorDesc,
			final String httpStatus, final String correlationId) {
		super(errorCode, errorDesc, httpStatus, correlationId);

	}

}
