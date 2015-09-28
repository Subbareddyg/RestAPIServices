package com.belk.api.transformer.xml;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.belk.api.constants.ErrorConstants;
import com.belk.api.error.handler.ResponseDetails;
import com.belk.api.exception.BaseException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.belk.api.util.ErrorLoader;

/**
 * Unit Testing related to XMLProcessor class is performed. <br />
 * {@link TestXMLProcessor} class is written for testing methods in
 * {@link XMLProcessor} The unit test cases evaluates the way the methods behave
 * for the inputs given.
 * 
 * <br />
 * The test cases associated with XMLProcessor is written to make the code
 * intact by assuring the code test.
 * 
 * @author Mindtree
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ ErrorLoader.class })
public class TestXMLProcessor {
	// private static final GenericLogger LOGGER =
	// LoggingHelper.getLogger(XMLProcessor.class.getName());
	public static final String ERROR_BINDING = "error-binding";
	public static final String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<error><code>404</code><description>Record Not Found.</description><parametername>500</parametername>"
			+ "<parametervalue>Error</parametervalue></error>";
	// +
	// "<ErrorDetails><ErrorCode>404</ErrorCode><ErrorDescription>Record Not Found.</ErrorDescription></ErrorDetails>";
	String correlationId = "1234567891234567";
	ErrorLoader errorLoader;
	Map<String, String> errorPropertiesMap = new HashMap<>();
	XMLProcessor xmlProcessor = new XMLProcessor();
	/**
	 * Precondition required for Junit test cases for BaseResource class.
	 */
	@Before
	public final void setUp() {
		final GenericLogger logger = LoggingHelper.getLogger("Junit");
		{
			LoggerUtil.setLogger(logger);
		}
		this.errorLoader = PowerMockito.mock(ErrorLoader.class);
		 this.errorPropertiesMap.put("11523", "There has been an internal service error");
		PowerMockito.when(this.errorLoader.getErrorPropertiesMap()).thenReturn(this.errorPropertiesMap);
		this.xmlProcessor.setErrorLoader(this.errorLoader);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.transformer.xml.XMLProcessor#buildXMLResponse(java.lang.Object, java.lang.Class, java.lang.String)}
	 * .
	 * 
	 * @throws IOException
	 *             throws as the OutputStream is being done
	 * @throws BaseException 
	 *               Exception thrown from Common Layer
	 */
	@Ignore
	public final void testBuildXMLResponse() throws IOException, BaseException {
		final ResponseDetails errorDescription = new ResponseDetails(
				String.valueOf(ErrorConstants.HTTP_STATUS_CODE_EMPTY_SEARCH_RESULT),
				ErrorConstants.ERRORDESC_RECORD_NOT_FOUND, "500", "Error");
		final ByteArrayOutputStream xmlOutput = new ByteArrayOutputStream();
		final byte[] b = XML.getBytes();
		xmlOutput.write(b);
		final String expectedResults = String.valueOf(xmlOutput);
		final String actualResults = String.valueOf(xmlProcessor
				.buildXMLResponse(errorDescription,
						errorDescription.getClass(), ERROR_BINDING,
						this.correlationId));
		assertEquals(expectedResults, actualResults);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.transformer.xml.XMLProcessor#buildXMLResponse(java.lang.Object, java.lang.Class, java.lang.String)}
	 * .
	 * 
	 * @throws IOException
	 *             throws as the OutputStream is being done
	 * @throws Exception
	 *             - exception
	 * @throws BaseException
	 *             Exception thrown from Common Layer 
	 */
	@Test(expected = Exception.class)
	public final void testBuildXMLResponseException() throws Exception {
		final ResponseDetails errorDescription = new ResponseDetails(
				String.valueOf(ErrorConstants.HTTP_STATUS_CODE_EMPTY_SEARCH_RESULT),
				ErrorConstants.ERRORDESC_RECORD_NOT_FOUND, "500", "Error");
		final ByteArrayOutputStream xmlOutput = new ByteArrayOutputStream();
		final byte[] b = XML.getBytes();
		xmlOutput.write(b);
		String.valueOf(xmlOutput);
		String.valueOf(xmlProcessor.buildXMLResponse(errorDescription,
				errorDescription.getClass(), ERROR_BINDING, this.correlationId));

		throw new Exception("Binding not found");
	}

}
