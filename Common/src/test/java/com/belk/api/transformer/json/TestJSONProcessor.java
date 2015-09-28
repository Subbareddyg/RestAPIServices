package com.belk.api.transformer.json;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.belk.api.constants.ErrorConstants;
import com.belk.api.error.handler.ResponseDetails;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;

/**
 * Unit Testing related to JSONProcessor class is performed. <br />
 * {@link TestJSONProcessor} class is written for testing methods in
 * {@link JSONProcessor} The unit test cases evaluates the way the methods
 * behave for the inputs given.
 * 
 * @author Mindtree
 * 
 */
public class TestJSONProcessor {
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger(JSONProcessor.class.getName());
	ResponseDetails errorDescription;
	String correlationId = "1234567891234567";

	/**
	 * Precondition required for Junit test cases for BaseResource class.
	 */
	@Before
	public final void setUp() {
		LoggerUtil.setLogger(LOGGER);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.transformer.json.JSONProcessor#buildJSONResponse()}.
	 * Testing is done to check whether the json response is obtained. when
	 * buildJSONResponse() method is called .
	 * 
	 * @throws IOException
	 *             throws as the OutputStream is being done
	 */
	@Test
	public final void testBuildJSONResponse() throws IOException {
		this.errorDescription = new ResponseDetails(
				String.valueOf(ErrorConstants.HTTP_STATUS_CODE_EMPTY_SEARCH_RESULT),
				ErrorConstants.ERRORDESC_RECORD_NOT_FOUND, "500", "error");
		String json = null;
		json = "{\"code\":\"404\",\"description\":\"Record Not Found.\","
				+ "\"parametername\":\"500\",\"parametervalue\":\"error\"}";
		final String expectedResults = json;
		final JSONProcessor jsonProcessor = new JSONProcessor();
		final String actualResults = jsonProcessor.buildJSONResponse(
				this.errorDescription, this.correlationId);
		assertEquals(expectedResults, actualResults);

	}

}
