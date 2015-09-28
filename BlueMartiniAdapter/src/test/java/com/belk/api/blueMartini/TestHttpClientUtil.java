package com.belk.api.blueMartini;

import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.belk.api.adapter.constants.BlueMartiniAdapterConstants;
import com.belk.api.exception.AdapterException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.belk.api.util.BlueMartiniTestUtil;
import com.belk.api.util.ErrorLoader;
import com.belk.api.util.HttpConnectionLoader;

/**
 * Unit Testing related to HttpClientUtil class working. <br />
 * {@link TestHttpClientUtil} class is written for testing methods in
 * {@link HttpClientUtil} The unit test cases evaluates the way the methods
 * behave for the inputs given.
 * 
 * The test cases associated with BlueMartiniAdapter is written to make the code
 * intactv by assuring the code test.
 * 
 * @author Mindtree
 * @date Nov 18th 2013
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpClientUtil.class,HttpConnectionLoader.class,ErrorLoader.class})
public class TestHttpClientUtil  {

	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("Junit");
	{
		LoggerUtil.setLogger(LOGGER);
	}
	HttpClientUtil httpClientUtil = new HttpClientUtil();
	String correlationId = "12345-67891234567-ASDFXEEETTT";
	String requestUrl = "http://webqaa.belkinc.com/PatternDetails.jsp?prdCode=1800273064010124";
	String incorrectRequestUrl = "http://webqaa.belkinc.com/PatternDetails.jsp?prdCode=null";
	String nullRequestUrl = null;
	HttpClientUtil blueMartiniutil = new HttpClientUtil();
	HttpClient client;
	HttpGet get;
	StringBuilder stringBuilder;
	JSONObject blueMartiniResponse = null;
	String connectionTimeout = "8000";
	String socketTimeout = "9000";
	String sbString = null;
	String line = null;

//	/**
//	 * Method which executes before each test
//	 */
//	@Override
//	@Before
//	public final void setUp() {
//		this.blueMartiniutil = new HttpClientUtil();
//		
//	}
//
//	/**
//	 * Method which executes after each test
//	 */
//	@Override
//	@After
//	public final void tearDown() {
//		this.blueMartiniutil = null;
//	}

	/**
	 * Test method to test the BlueMartiniUtil getHttpContent method.
	 * 
	 * @throws AdapterException
	 *             exception thrown from endeca.
	 * 
	 */
	@Test(expected = AdapterException.class)
	public final void testGetHttpContent1() throws AdapterException {
		HttpConnectionLoader httpConnectionLoader = PowerMockito.mock(HttpConnectionLoader.class);
		ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		final Map<String, String> httpConnectionPropertiesMap = BlueMartiniTestUtil.requestParams(); 
		httpConnectionPropertiesMap.put("PATTERN_DETAILS_URL","123");
		 this.blueMartiniutil.setHttpConnectionLoader(httpConnectionLoader);
		 this.blueMartiniutil.setErrorLoader(errorLoader);
		PowerMockito.when(httpConnectionLoader.getHttpConnectionPropertiesMap()).thenReturn(httpConnectionPropertiesMap);
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(httpConnectionPropertiesMap);
		this.client = PowerMockito.mock(DefaultHttpClient.class);
		this.get = PowerMockito.mock(HttpGet.class);

		assertNotNull(this.blueMartiniutil.getHttpContent(
				BlueMartiniAdapterConstants.PATTERN_DETAILS,
				BlueMartiniTestUtil.requestParams(), this.correlationId));
	}

	/**
	 * Test method to test the BlueMartiniUtil getHttpContent method for
	 * multiple product codes.
	 * 
	 * @throws AdapterException
	 *             exception thrown from endeca.
	 * 
	 */
	@Test(expected = AdapterException.class)
	public final void testGetHttpContent2() throws AdapterException {
		HttpConnectionLoader httpConnectionLoader = PowerMockito.mock(HttpConnectionLoader.class);
		ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		final Map<String, String> httpConnectionPropertiesMap = BlueMartiniTestUtil.requestParams(); 
		 this.blueMartiniutil.setHttpConnectionLoader(httpConnectionLoader);
		 this.blueMartiniutil.setErrorLoader(errorLoader);
		PowerMockito.when(httpConnectionLoader.getHttpConnectionPropertiesMap()).thenReturn(httpConnectionPropertiesMap);
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(httpConnectionPropertiesMap);
		this.client = PowerMockito.mock(DefaultHttpClient.class);
		this.get = PowerMockito.mock(HttpGet.class);

		final Map input = BlueMartiniTestUtil.requestParams();
		input.put("prdCode", "3201634Z31300");
		assertNotNull(this.blueMartiniutil.getHttpContent(null, input,
				this.correlationId));
	}

	/**
	 * Test method to test the BlueMartiniUtil getHttpContent method for
	 * removing the extra string "<!-- Printing response to front end -->\n"
	 * 
	 * @throws AdapterException
	 *             exception thrown from endeca.
	 * 
	 */

	@Test
	public final void testReplaceComments() throws AdapterException {
		HttpConnectionLoader httpConnectionLoader = PowerMockito.mock(HttpConnectionLoader.class);
		ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		final Map<String, String> httpConnectionPropertiesMap = BlueMartiniTestUtil.requestParams(); 
		 this.blueMartiniutil.setHttpConnectionLoader(httpConnectionLoader);
		 this.blueMartiniutil.setErrorLoader(errorLoader);
		PowerMockito.when(httpConnectionLoader.getHttpConnectionPropertiesMap()).thenReturn(httpConnectionPropertiesMap);
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(httpConnectionPropertiesMap);
		final String sampleOutput = "<!-- Printing response to front end -->\nTest<!-- Printing response to front end -->\n";
		final Reader in = new StringReader(sampleOutput);
		final BufferedReader br = new BufferedReader(in);
		final HttpClientUtil httpClientUtil = new HttpClientUtil();
		final String result = this.blueMartiniutil.replaceComments(br,
				this.correlationId);
		//assertEquals("Test", result);

	}

	/**
	 * Test method to test the BlueMartiniUtil getHttpContent method for string
	 * not starting with "<!-- Printing response to front end -->\n
	 * 
	 * @throws AdapterException
	 *             exception thrown from endeca.
	 * 
	 */
	@Test
	public final void testReplaceComments2() throws AdapterException {
		HttpConnectionLoader httpConnectionLoader = PowerMockito.mock(HttpConnectionLoader.class);
		ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		final Map<String, String> httpConnectionPropertiesMap = BlueMartiniTestUtil.requestParams(); 
		 this.blueMartiniutil.setHttpConnectionLoader(httpConnectionLoader);
		 this.blueMartiniutil.setErrorLoader(errorLoader);
		PowerMockito.when(httpConnectionLoader.getHttpConnectionPropertiesMap()).thenReturn(httpConnectionPropertiesMap);
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(httpConnectionPropertiesMap);
		final String sampleOutput = "Test<!-- Printing response to front end -->\n";
		final Reader in = new StringReader(sampleOutput);
		final BufferedReader br = new BufferedReader(in);
		final HttpClientUtil httpClientUtil = new HttpClientUtil();
		final String result = this.blueMartiniutil.replaceComments(br,
				this.correlationId);
		//assertNotSame("Test", result);

	}

	/**
	 * Test method to test the BlueMartiniUtil getHttpContent method that throw
	 * Adapter Exception.
	 * 
	 * @throws AdapterException
	 *             exception thrown from endeca.
	 * 
	 */
	@Test(expected = AdapterException.class)
	public final void testBuildRequestAdapterException()
			throws AdapterException {
		HttpConnectionLoader httpConnectionLoader = PowerMockito.mock(HttpConnectionLoader.class);
		ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		final Map<String, String> httpConnectionPropertiesMap = BlueMartiniTestUtil.requestParams(); 
		 this.blueMartiniutil.setHttpConnectionLoader(httpConnectionLoader);
		 this.blueMartiniutil.setErrorLoader(errorLoader);
		PowerMockito.when(httpConnectionLoader.getHttpConnectionPropertiesMap()).thenReturn(httpConnectionPropertiesMap);
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(httpConnectionPropertiesMap);
		this.client = PowerMockito.mock(DefaultHttpClient.class);
		this.get = PowerMockito.mock(HttpGet.class);
		this.get = new HttpGet(this.requestUrl);

		this.blueMartiniResponse = PowerMockito.mock(JSONObject.class);
		this.blueMartiniutil.getHttpContent(
				BlueMartiniAdapterConstants.PATTERN_DETAILS,
				BlueMartiniTestUtil.requestParams(), this.correlationId);

	}

	/**
	 * Test method to test the BlueMartiniUtil getHttpContent method that throw
	 * Null pointer Exception.
	 * 
	 * @throws AdapterException
	 *             exception thrown from endeca.
	 * 
	 */
	@Test(expected = AdapterException.class)
	public final void testBuildRequestNullPointerException()
			throws AdapterException {
		HttpConnectionLoader httpConnectionLoader = PowerMockito.mock(HttpConnectionLoader.class);
		ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		final Map<String, String> httpConnectionPropertiesMap = BlueMartiniTestUtil.requestParams(); 
		 this.blueMartiniutil.setHttpConnectionLoader(httpConnectionLoader);
		 this.blueMartiniutil.setErrorLoader(errorLoader);
		PowerMockito.when(httpConnectionLoader.getHttpConnectionPropertiesMap()).thenReturn(httpConnectionPropertiesMap);
		PowerMockito.when(errorLoader.getErrorPropertiesMap()).thenReturn(httpConnectionPropertiesMap);
		this.client = PowerMockito.mock(DefaultHttpClient.class);
		this.get = PowerMockito.mock(HttpGet.class);
		this.get = new HttpGet(this.incorrectRequestUrl);

		this.blueMartiniResponse = PowerMockito.mock(JSONObject.class);
		this.blueMartiniutil.getHttpContent(
				BlueMartiniAdapterConstants.PATTERN_DETAILS,
				BlueMartiniTestUtil.requestParams(), this.correlationId);

	}

}
