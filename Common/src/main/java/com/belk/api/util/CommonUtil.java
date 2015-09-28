package com.belk.api.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.JiBXException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.belk.api.constants.CommonConstants;
import com.belk.api.error.handler.ResponseDetails;
import com.belk.api.exception.BaseException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.transformer.json.JSONProcessor;
import com.belk.api.transformer.xml.XMLProcessor;

/**
 * This is a common utility class used by all api services for various purpose
 * like formatting the decimal and converting to boolean flags.
 * 
 * Updated : Added few comments and loggers as part of Phase2, April,14 release
 * 
 * @author Mindtree
 * @date 22 Oct, 2013
 */
@Service
public class CommonUtil {
	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();
	/**
	 * xmlProcessor is to create instance of XMLProcessor.
	 */
	@Autowired
	private static XMLProcessor xmlProcessor;

	/**
	 * jsonProcessor is to create instance of JSONProcessor.
	 */
	@Autowired
	private static JSONProcessor jsonProcessor;

	/**
	 * Default Constructor.
	 */
	public CommonUtil() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param jsonProcessor
	 *            the jsonProcessor to set
	 */
	public final void setJsonProcessor(final JSONProcessor jsonProcessor) {
		this.jsonProcessor = jsonProcessor;
	}

	/**
	 * @param xmlProcessor
	 *            the xmlProcessor to set
	 */
	public final void setXmlProcessor(final XMLProcessor xmlProcessor) {
		this.xmlProcessor = xmlProcessor;
	}

	/**
	 * This metod formats the decimal numbers to two digits if it has more than
	 * two.
	 * 
	 * @param price
	 *            value to be formatted for 2 decimals
	 * @param correlationId
	 *            to track the request
	 * @return value after the format
	 */
	public final String format(final String price, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		String value = price;
		if (!price.isEmpty()) {
			final DecimalFormat decimalFormat = new DecimalFormat("#.##");
			decimalFormat.setMinimumFractionDigits(2);
			value = decimalFormat.format(Double.parseDouble(price));
			LOGGER.debug("Value after formatting " + value, correlationId);
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return value;

	}

	/**
	 * This metod converts the Flags to T or F from Yes or No more than two.
	 * 
	 * @param value
	 *            this is coming from endeca , it could be Yes or No
	 * @param correlationId
	 *            to track the request
	 * @return flag after converting to T or F
	 */
	public final String convertToFlag(final String value,
			final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		String flag = null;
		if (value.equalsIgnoreCase(CommonConstants.YES_VALUE)) {
			flag = CommonConstants.FLAG_YES_VALUE;
		} else if (value.equalsIgnoreCase(CommonConstants.NO_VALUE)) {
			flag = CommonConstants.FLAG_NO_VALUE;
		} else {
			flag = value;
		}
		LOGGER.debug("Value of the Flag " + value, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return flag;
	}

	/**
	 * This method checks whether incoming value is empty or not.
	 * 
	 * @param value
	 *            this is coming from endeca
	 * @param correlationId
	 *            to track the request
	 * @return true or false based on condition
	 */
	public final boolean isNotEmpty(final String value,
			final String correlationId) {
		if (value != null && !CommonConstants.EMPTY_STRING.equals(value)) {
			return true;
		}
		return false;
	}

	/**
	 * This method is to form absolute path of property file passed.
	 * 
	 * @param fileName
	 *            - property file name.
	 * @param layerName
	 *            - layer name.
	 * @param correlationId
	 *            - to track the request.
	 * @return absolute path to property file passed.
	 */
	public static final String getConfigurationFilePath(final String fileName,
			final String layerName, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final StringBuilder finalFilePath = new StringBuilder();
		final ResourceBundle bundle = ResourceBundle
				.getBundle(CommonConstants.CONFIGURATIONS_RESOURCE_BUNDLE);
		final String propertyFilePath = bundle
				.getString("configurations.files.path");
		finalFilePath.append(propertyFilePath);
		finalFilePath.append("/");
		finalFilePath.append(layerName);
		finalFilePath.append("/");
		finalFilePath.append(fileName);
		LOGGER.debug("Final filepath : " + finalFilePath.toString(),
				"Configuration");
		LOGGER.logMethodExit(startTime, correlationId);
		return finalFilePath.toString();

	}

	/**
	 * This method is to convert the BaseException object (or Service / Adapter
	 * Exception)
	 * 
	 * into a Response Details XML String.
	 * 
	 * @param target
	 *            - BaseException or AdapterException or ServiceException
	 * @param className
	 *            - Class Name of Exception object for Marshalling Purposes
	 * 
	 * 
	 * @param correlationId
	 *            - to track the request.
	 * @return absolute path to property file passed.
	 */

	public static final String generateResponseXMLString(final Object target,
			final Class<?> className, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		byte[] responseByteArray;
		String stringForm = null;
		final BaseException error = (BaseException) target;
		final ResponseDetails responseError = new ResponseDetails(
				error.getErrorCode(), error.getErrorDesc(),
				error.getErrorFieldParameter(), error.getErrorParameterValue());
		try {

			final IBindingFactory factory = BindingDirectory
					.getFactory(responseError.getClass());
			final ByteArrayOutputStream xmlOutput = new ByteArrayOutputStream();
			final BufferedOutputStream xmlBuffer = new BufferedOutputStream(
					xmlOutput);
			final IMarshallingContext context = factory
					.createMarshallingContext();
			context.marshalDocument(responseError, CommonConstants.UTF_TYPE,
					null, xmlBuffer);
			responseByteArray = xmlOutput.toByteArray();
			stringForm = new String(responseByteArray);

		} catch (final JiBXException e) {
			LOGGER.error(e, correlationId);

		}
		LOGGER.debug("Response XML in  stringForm : " + stringForm,
				correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return stringForm;
	}

	/**
	 * Method to return human readable string from camel case.
	 * 
	 * @param input
	 *            input string to be formatted
	 * @return formatted string
	 */
	public static String splitCamelCase(final String input) {
		return input.replaceAll(String.format("%s|%s|%s",
				"(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
				"(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
	}

	/**
	 * Returns a parsed XML string by extracting the value of the node requested
	 * for.
	 * 
	 * @param xmlString
	 *            XML string to be parsed
	 * @param nodeName
	 *            Node for which value is to be extracted
	 * @return value of the desired node
	 */
	public static String parseXML(final String xmlString, final String nodeName) {
		String parsedNodeValue = null;
		try {
			final DocumentBuilder db = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			final InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlString));

			final Document doc = db.parse(is);
			final NodeList nodes = doc.getElementsByTagName(nodeName);
			final Element line = (Element) nodes.item(0);
			parsedNodeValue = getCharacterDataFromElement(line);

		} catch (ParserConfigurationException e) {
			LOGGER.error(e, parsedNodeValue);			
		} catch (SAXException e) {			
			LOGGER.error(e, parsedNodeValue);
		} catch (IOException e) {			
			LOGGER.error(e, parsedNodeValue);
		}
		return parsedNodeValue;
	}

	/**
	 * Method to get the character data out of a parsed element.
	 * 
	 * @param parsedElement
	 *            parsed element containing character data
	 * @return extracted character data out of an Element object.
	 */
	private static String getCharacterDataFromElement(
			final Element parsedElement) {
		final Node child = parsedElement.getFirstChild();
		if (child instanceof CharacterData) {
			final CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return CommonConstants.EMPTY_STRING;
	}

}
