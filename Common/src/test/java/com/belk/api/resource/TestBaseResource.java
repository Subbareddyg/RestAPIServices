package com.belk.api.resource;

import static org.mockito.Mockito.stub;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.RuntimeDelegate;

import junit.framework.TestCase;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.JiBXException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.belk.api.constants.CommonConstants;
import com.belk.api.error.handler.ResponseDetails;
import com.belk.api.exception.BaseException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.belk.api.transformer.json.JSONProcessor;
import com.belk.api.transformer.xml.XMLProcessor;
import com.belk.api.util.ErrorLoader;
import com.belk.api.util.TestBaseResourceUtil;

/**
 * Unit Testing related to BaseResource class is performed. <br />
 * {@link TestBaseResource} class is written for testing methods in
 * {@link BaseResource} The unit test cases evaluates the way the methods behave
 * for the inputs given.
 * 
 * @author Mindtree
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ BaseResource.class, XMLProcessor.class, JSONProcessor.class,
		ResponseDetails.class, BaseException.class, BindingDirectory.class })
public class TestBaseResource extends TestCase {

	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger(BaseResource.class.getName());

	URI uri;
	BaseResource baseResource;
	ResponseDetails targetMethodObject;
	Class<?> className;
	String errorBinding;
	Map<String, List<String>> uriMap;
	List<String> list;
	ByteArrayOutputStream xmlResponseStream;
	String xmlResposne;
	byte[] bytes;
	UriBuilder uriBuilder;
	IBindingFactory factory;
	BufferedOutputStream xmlBuffer;
	IMarshallingContext context;
	String expectedResults;
	String actualResults;
	ResponseBuilder expectedResponseBuilder;
	ResponseBuilder actualResponseBuilder;
	String correlationId;

	@Mock
	ResponseBuilder responseBuilder;

	@Mock
	private RuntimeDelegate runtimeDelegate;

	/**
	 * Precondition required for Junit test cases for BaseResource class.
	 */
	@Override
	@Before
	public final void setUp() {
		this.baseResource = new BaseResource();
		this.uriMap = new HashMap<String, List<String>>();
		this.list = new ArrayList<String>();
		RuntimeDelegate.setInstance(this.runtimeDelegate);
		this.targetMethodObject = new ResponseDetails();
		this.className = ResponseDetails.class;
		this.errorBinding = "error-binding";
		this.uri = URI.create("http://localhost:8080/product");
		this.xmlResponseStream = new ByteArrayOutputStream();
		this.xmlResposne = "<?xml version='1.0' encoding='UTF-8'?><ErrorDetails/>";
		this.responseBuilder = PowerMockito.mock(ResponseBuilder.class);
		this.uriBuilder = PowerMockito.mock(UriBuilder.class);
		PowerMockito.mockStatic(XMLProcessor.class);
		PowerMockito.mockStatic(JSONProcessor.class);
		// Mocked the runtime Delegate to obtain the Response Builder.
		PowerMockito.when((this.runtimeDelegate).createResponseBuilder())
				.thenReturn(this.responseBuilder);
		// Mocked the runtime Delegate to obtain the uri Builder.
		PowerMockito.when((this.runtimeDelegate).createUriBuilder())
				.thenReturn(this.uriBuilder);
		PowerMockito.when((this.responseBuilder).location(this.uri))
				.thenReturn(this.responseBuilder);
		PowerMockito.when((this.responseBuilder).contentLocation(this.uri))
				.thenReturn(this.responseBuilder);
		this.correlationId = "1234567891234567";
		LoggerUtil.setLogger(LOGGER);
		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		Map<String, String> errorPropertiesMap = new HashMap<String, String>();
		errorPropertiesMap.put("11523", "internal server error");
		errorLoader.setErrorPropertiesMap(errorPropertiesMap);
		this.baseResource.setErrorLoader(errorLoader);
	}

	/**
	 * Tests the method getResourceTypeForUriParameter
	 * {@link com.belk.api.resource.BaseResource#getResponseTypeForURIParam()}
	 * to check the response type is specified or not
	 */
	@Ignore
	public final void testGetResponseTypeForURIParam() {
		final Map<String, String> uriMapForBaseResource = TestBaseResourceUtil
				.getUriMap();
		final String correlationId = "123445";
		final BaseResource baseResource = PowerMockito.mock(BaseResource.class);
		final String responseType;
		responseType = baseResource.getResponseTypeForURIParam(
				uriMapForBaseResource, correlationId);
		assertNotNull(responseType);
	}

	/**
	 * Tests the method getResourceTypeUriParameter
	 * {@link com.belk.api.resource.BaseResource#getResponseTypeForURIParam()}
	 * to check the response type is specified or not
	 */
	@Ignore
	public final void testGetResponseTypeForURIParamForNoRequestType() {
		final Map<String, String> uriMapForBaseResource = TestBaseResourceUtil
				.getUriMapForNoRequestType();
		final String correlationId = "123445";
		final BaseResource baseResource = PowerMockito.mock(BaseResource.class);
		final String responseType;
		responseType = baseResource.getResponseTypeForURIParam(
				uriMapForBaseResource, correlationId);
		assertNotNull(responseType);

	}

	/**
	 * Test method for
	 * {@link com.belk.api.resource.BaseResource#getResponseType()}. Testing is
	 * done to check whether correct response type is obtained (XML or JSon).
	 * 
	 */
	@Test
	public final void testGetResponseTypeForXML() {
		this.list.add("xml");
		this.uriMap.put(CommonConstants.REQUEST_TYPE, this.list);
		this.expectedResults = "xml";
		this.actualResults = this.baseResource.getResponseType(this.uriMap,
				this.correlationId);
		assertEquals(this.expectedResults, this.actualResults);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.resource.BaseResource#getResponseType()}. Testing is
	 * done to check whether correct response type is obtained (XML or JSon).
	 * 
	 */
	@Test
	public final void testGetResponseTypeForJSON() {
		this.list.add("json");
		this.uriMap.put(CommonConstants.REQUEST_TYPE, this.list);
		this.expectedResults = "json";
		this.actualResults = this.baseResource.getResponseType(this.uriMap,
				this.correlationId);
		assertEquals(this.expectedResults, this.actualResults);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.resource.BaseResource#BuildResponse()}. Testing is
	 * done to check whether correct XML response is obtained.
	 * 
	 * @throws IOException
	 *             - ioexception
	 * @throws BaseException 
	 *             Exception thrown from Common Layer
	 */
	@Test
	public final void testBuildResponseForXML() throws IOException, BaseException {
		this.bytes = this.xmlResposne.getBytes();
		this.xmlResponseStream.write(this.bytes);
		// Stubbed to assign data to responseBuilder.
		stub(this.responseBuilder.toString()).toReturn(this.xmlResposne);

		final XMLProcessor xmlProcessor = PowerMockito.mock(XMLProcessor.class);
		this.baseResource.setXmlProcessor(xmlProcessor);
		// Mocked the XmlProcessor to obtain ByteArrayOutputStream when
		// method BuildXMLResponse is called.
		PowerMockito.when(
				xmlProcessor.buildXMLResponse(this.targetMethodObject,
						this.className, this.errorBinding, this.correlationId))
				.thenReturn(this.xmlResponseStream);

		// Mocked the header of the response builder.
		PowerMockito.when(
				(this.responseBuilder).header(CommonConstants.CONTENT_TYPE,
						CommonConstants.CONTENT_TYPE_VALUE_XML)).thenReturn(
				this.responseBuilder);

		PowerMockito.when(
				(this.responseBuilder).header(CommonConstants.CORRELATION_ID,
						this.correlationId)).thenReturn(this.responseBuilder);

		// Mocked the entity of the response builder.
		PowerMockito.when(
				(this.responseBuilder).entity(this.xmlResponseStream
						.toByteArray())).thenReturn(this.responseBuilder);
		// Mocked the status of the response builder.
		PowerMockito.when((this.responseBuilder).status(200)).thenReturn(
				this.responseBuilder);
		this.expectedResponseBuilder = this.responseBuilder;
		this.actualResponseBuilder = this.baseResource.buildResponse(
				this.targetMethodObject, this.className, this.errorBinding,
				CommonConstants.MEDIA_TYPE_XML, this.correlationId);
		assertEquals(this.expectedResponseBuilder, this.actualResponseBuilder);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.resource.BaseResource#BuildResponse()}. Testing is
	 * done to check whether correct JSON response is obtained.
	 * 
	 * @throws IOException
	 *             - ioexception
	 * @throws BaseException 
	 *             Exception thrown from Common Layer
	 */
	@Test
	public final void testBuildResponseForJSON() throws IOException, BaseException {
		final String requestJson = "{product:1354236}";
		this.bytes = requestJson.getBytes();
		this.xmlResponseStream.write(this.bytes);
		// Stub to assign data to responseBuilder.
		stub(this.responseBuilder.toString()).toReturn(requestJson);
		final JSONProcessor jsonProcessor = PowerMockito
				.mock(JSONProcessor.class);
		this.baseResource.setJsonProcessor(jsonProcessor);
		// Mocked the XmlProcessor to class to obtain ByteArrayOutputStream when
		// the
		// method BuildXMLResponse is called.
		PowerMockito.when(
				jsonProcessor.buildJSONResponse(this.targetMethodObject,
						this.correlationId)).thenReturn(requestJson);

		// Mocked the header of the response builder.
		PowerMockito.when(
				(this.responseBuilder).header(CommonConstants.CONTENT_TYPE,
						CommonConstants.CONTENT_TYPE_VALUE_JSON)).thenReturn(
				this.responseBuilder);

		PowerMockito.when(
				(this.responseBuilder).header(CommonConstants.CORRELATION_ID,
						this.correlationId)).thenReturn(this.responseBuilder);

		PowerMockito.when(
				(this.responseBuilder).entity(requestJson
						.getBytes(CommonConstants.UTF_TYPE))).thenReturn(
				this.responseBuilder);
		// Mocked the status of the response builder.
		PowerMockito.when((this.responseBuilder).status(200)).thenReturn(
				this.responseBuilder);
		this.expectedResults = requestJson;
		this.actualResults = (this.baseResource.buildResponse(
				this.targetMethodObject, this.className, this.errorBinding,
				CommonConstants.MEDIA_TYPE_JSON, this.correlationId))
				.toString();
		assertEquals(this.expectedResults, this.actualResults);

	}

	/**
	 * Test method for
	 * {@link com.belk.api.resource.BaseResource#BuildErrorResponse()}. Testing
	 * is done to check whether correct XML response is obtained.
	 * 
	 * @throws IOException
	 *             - ioexception
	 * @throws JiBXException
	 *             - jibx exception
	 */
	@Ignore
	public final void testBuildErrorResponseForXML() throws IOException,
			JiBXException {
		// Stub to assign data to responseBuilder.
		stub(this.responseBuilder.toString()).toReturn(this.xmlResposne);
		this.targetMethodObject.setCode("404");
		// Mocked the header of the response builder.
		PowerMockito.when(
				(this.responseBuilder).header(CommonConstants.CONTENT_TYPE,
						CommonConstants.CONTENT_TYPE_VALUE_XML)).thenReturn(
				this.responseBuilder);
		PowerMockito.when(
				(this.responseBuilder).header(CommonConstants.CORRELATION_ID,
						this.correlationId)).thenReturn(this.responseBuilder);
		final IBindingFactory iBindingFactory = PowerMockito
				.mock(IBindingFactory.class);
		PowerMockito.mockStatic(BindingDirectory.class);
		PowerMockito.when(BindingDirectory.getFactory(this.className))
				.thenReturn(iBindingFactory);

		final IMarshallingContext context = PowerMockito
				.mock(IMarshallingContext.class);
		this.xmlBuffer = new BufferedOutputStream(this.xmlResponseStream);
		PowerMockito.when(iBindingFactory.createMarshallingContext())
				.thenReturn(context);

		context.marshalDocument(this.targetMethodObject, "UTF-8", null,
				this.xmlBuffer);
		// Mocked the entity of the response builder.
		PowerMockito.when(
				(this.responseBuilder).entity(this.xmlResponseStream
						.toByteArray())).thenReturn(this.responseBuilder);
		// Mocked the status of the response builder.
		PowerMockito.when((this.responseBuilder).status(404)).thenReturn(
				this.responseBuilder);
		this.expectedResponseBuilder = this.responseBuilder;
		this.actualResponseBuilder = this.baseResource.buildErrorResponse(
				this.targetMethodObject, this.className, "404",
				CommonConstants.MEDIA_TYPE_XML, this.correlationId);
		assertEquals(this.expectedResponseBuilder, this.actualResponseBuilder);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.resource.BaseResource#BuildErrorResponse()}. Testing
	 * is done to check whether correct JSON response is obtained.
	 * 
	 * @throws IOException
	 *             - ioexception
	 * 
	 */
	@Ignore
	public final void testBuildErrorResponseForJSON() throws IOException {
		final String requestJson = "{error:404}";
		this.bytes = requestJson.getBytes();

		this.xmlResponseStream.write(this.bytes);
		// Stubbed to assign data to responseBuilder.
		stub(this.responseBuilder.toString()).toReturn(requestJson);
		this.targetMethodObject.setCode("404");

		final JSONProcessor jsonProcessor = PowerMockito
				.mock(JSONProcessor.class);
		this.baseResource.setJsonProcessor(jsonProcessor);
		PowerMockito.mockStatic(JSONProcessor.class);
		PowerMockito.when(
				jsonProcessor.buildJSONResponse(this.targetMethodObject,
						this.correlationId)).thenReturn(requestJson);
		// Mocked the header of the response builder.
		PowerMockito.when(
				(this.responseBuilder).header(CommonConstants.CONTENT_TYPE,
						CommonConstants.CONTENT_TYPE_VALUE_JSON)).thenReturn(
				this.responseBuilder);

		PowerMockito.when(
				(this.responseBuilder).header(CommonConstants.CORRELATION_ID,
						this.correlationId)).thenReturn(this.responseBuilder);

		// Mocked the entity of the response builder.
		PowerMockito.when(
				(this.responseBuilder).entity(requestJson
						.getBytes(CommonConstants.UTF_TYPE))).thenReturn(
				this.responseBuilder);
		// Mocked the status of the response builder.
		PowerMockito.when((this.responseBuilder).status(404)).thenReturn(
				this.responseBuilder);
		this.expectedResults = requestJson;
		this.actualResults = (this.baseResource.buildErrorResponse(
				this.targetMethodObject, this.className, "404",
				CommonConstants.MEDIA_TYPE_JSON, this.correlationId))
				.toString();
		assertEquals(this.expectedResults, this.actualResults);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.resource.BaseResource#getResponseType()}. Testing is
	 * done to check whether correct response type is obtained (XML or JSon).
	 * 
	 */
	@Test
	public final void testGetResponseTypeForEfault() {
		this.expectedResults = "XML";
		this.actualResults = this.baseResource.getResponseType(this.uriMap,
				this.correlationId);
		assertEquals(this.expectedResults, this.actualResults);
	}

	/**
	 * Junit testing is done to test ProcessException method present in Base
	 * Resource.
	 */
	@Test(expected = Exception.class)
	public final void testProcessException() {
		final BaseException baseException = PowerMockito
				.mock(BaseException.class);
		this.baseResource.processException(baseException, "null",
				this.correlationId);
	}

	/**
	 * Junit testing is done to test ProcessException method present in Base
	 * Resource.
	 */
	@Test(expected = Exception.class)
	public final void testProcessExceptionForErrorField() {
		final BaseException baseException = PowerMockito
				.mock(BaseException.class);
		final String errorFieldParameter = "123ABC";
		final String errorParameterValue = "ABC123";
		baseException.getErrorFieldParameter();
		baseException.getErrorParameterValue();
		PowerMockito.when(baseException.getErrorFieldParameter()).thenReturn(
				errorFieldParameter);
		PowerMockito.when(baseException.getErrorParameterValue()).thenReturn(
				errorParameterValue);
		new ResponseDetails("504", "Service unavailable",
				baseException.getErrorFieldParameter(),
				baseException.getErrorParameterValue());
		this.baseResource.processException(baseException, "null",
				this.correlationId);
	}

	/**
	 * Junit testing is done to test ProcessException method present in Base
	 * Resource.
	 */
	@Test(expected = Exception.class)
	public final void testProcessExceptionForErrorFieldParameter() {
		final BaseException baseException = PowerMockito
				.mock(BaseException.class);

		baseException.getErrorFieldParameter();
		baseException.getErrorParameterValue();
		new ResponseDetails("504", "Service unavailable", "123ABC", "ABC123");
		this.baseResource.processException(baseException, "null",
				this.correlationId);
	}

	/**
	 * Test method for
	 * {@link com.belk.api.resource.BaseResource#BuildResponse()}. Testing is
	 * done to check whether correct XML response is obtained.
	 * @throws BaseException  
	 *             Exception thrown from Common Layer
	 * @throws IOException .
	 */
	
	@Ignore
	public final void testBuildResponseForTargetNull() throws IOException, BaseException {
		this.bytes = this.xmlResposne.getBytes();
		this.xmlResponseStream.write(this.bytes);
		// Stubbed to assign data to responseBuilder.
		stub(this.responseBuilder.toString()).toReturn(this.xmlResposne);

		final XMLProcessor xmlProcessor = PowerMockito.mock(XMLProcessor.class);
		this.baseResource.setXmlProcessor(xmlProcessor);
		// Mocked the XmlProcessor to obtain ByteArrayOutputStream when
		// method BuildXMLResponse is called.
		PowerMockito.when(
				xmlProcessor.buildXMLResponse(this.targetMethodObject,
						this.className, this.errorBinding, this.correlationId))
				.thenReturn(this.xmlResponseStream);

		// Mocked the header of the response builder.
		PowerMockito.when(
				(this.responseBuilder).header(CommonConstants.CONTENT_TYPE,
						CommonConstants.CONTENT_TYPE_VALUE_XML)).thenReturn(
				this.responseBuilder);

		PowerMockito.when(
				(this.responseBuilder).header(CommonConstants.CORRELATION_ID,
						this.correlationId)).thenReturn(this.responseBuilder);

		// Mocked the entity of the response builder.
		PowerMockito.when(
				(this.responseBuilder).entity(this.xmlResponseStream
						.toByteArray())).thenReturn(this.responseBuilder);
		// Mocked the status of the response builder.
		PowerMockito.when((this.responseBuilder).status(200)).thenReturn(
				this.responseBuilder);
		this.expectedResponseBuilder = this.responseBuilder;
		this.actualResponseBuilder = this.baseResource.buildResponse(null,
				this.className, this.errorBinding,
				CommonConstants.MEDIA_TYPE_XML, this.correlationId);
		assertEquals(this.expectedResponseBuilder, this.actualResponseBuilder);
	}

	/**
	 * Tests the method getResourceTypeUriParameter
	 * {@link com.belk.api.resource.BaseResource#getResponseTypeForURIParam()}
	 * to check the response type is specified or not
	 */
	@Test
	public final void testGetResponseType() {
		this.list.add("format");
		this.uriMap.put(CommonConstants.REQUEST_TYPE, this.list);
		this.expectedResults = "XML";
		this.actualResults = this.baseResource.getResponseType(this.uriMap,
				this.correlationId);
		assertEquals(this.expectedResults, this.actualResults);

	}

	/**
	 * Tests the method getResourceTypeUriParameter
	 * {@link com.belk.api.resource.BaseResource#getResponseTypeForURIParam()}
	 * to check the response type is specified or not
	 */
	@Ignore
	public final void testGetResponseTypeForURIParamWithXML() {
		final Map<String, String> uriMapForBaseResource = TestBaseResourceUtil
				.getUriMapWithXML();
		final String responseType;
		final String correlationId = "123445";
		final BaseResource baseResource = PowerMockito.mock(BaseResource.class);
		responseType = baseResource.getResponseTypeForURIParam(
				uriMapForBaseResource, correlationId);
		assertNotNull(responseType);

	}


}
