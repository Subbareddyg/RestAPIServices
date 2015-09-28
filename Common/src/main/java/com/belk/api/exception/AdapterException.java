package com.belk.api.exception;

import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;

/**
 * This class handles all the exception thrown from the adapter layer.
 * 
 * @author Mindtree @ date Sep 22, 2013
 */
public class AdapterException extends BaseException {
	/**
	 * serialVersionUID declaration.
	 */
	private static final long serialVersionUID = -6173445095797188369L;
	/**
	 * creating instance of loggers.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

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
	 *            to track the request
	 */
	public AdapterException(final String errorCode, final String errorDesc,
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
	 *            to track the request
	 */
	public AdapterException(final String errorCode, final String errorDesc,
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
	 *            to track the request
	 */
	public AdapterException(final String errorCode, final String errorDesc,
			final String httpStatus, final String correlationId) {
		super(errorCode, errorDesc, httpStatus, correlationId);

	}

}
