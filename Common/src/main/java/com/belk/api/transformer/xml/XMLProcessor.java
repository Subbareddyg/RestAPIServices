package com.belk.api.transformer.xml;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.JiBXException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.belk.api.constants.CommonConstants;
import com.belk.api.constants.ErrorConstants;
import com.belk.api.exception.BaseException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.util.ErrorLoader;

/**
 * This class is used by BaseResource to generate the response in xml format.
 * 
 * Updated : Exception handling is done for NullPointerException,
 * Added few comments as part of Phase2, April,14 release
 * @author Mindtree
 * @date sep 23, 2013
 */
@Service
public class XMLProcessor {
	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	/**
	 * creating instance of Error Loader class.
	 */
	@Autowired
	ErrorLoader errorLoader;
	
	/**
	 * Default Constructor.
	 */
	public XMLProcessor() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * setter method for the errorLoader.
	 * 
	 * @param errorLoader the errorLoader to set
	 */
	public final void setErrorLoader(final ErrorLoader errorLoader) {
		this.errorLoader = errorLoader;
	}

	/**
	 * Method to build an XML response from the binding specified for the domain
	 * object in question.
	 * 
	 * @param target
	 *            Final object to be built from the binding
	 * @param className
	 *            class name to be bound
	 * @param correlationId
	 *            to track the request
	 * @param bindingName
	 *            The name of the XML file used for the binding
	 * @return an XML output
	 * @throws BaseException Exception thrown from Common Layer
	 */
	public final ByteArrayOutputStream buildXMLResponse(final Object target,
			final Class<?> className, final String bindingName,
			final String correlationId) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final IBindingFactory factory = this.getBindingFactory(className,
				bindingName, correlationId);
		final ByteArrayOutputStream xmlOutput = new ByteArrayOutputStream();
		final BufferedOutputStream xmlBuffer = new BufferedOutputStream(
				xmlOutput);
		final Map<String, String> errorPropertiesMap = this.errorLoader.getErrorPropertiesMap();
		try {
			final IMarshallingContext context = factory
					.createMarshallingContext();
			context.marshalDocument(target, CommonConstants.UTF_TYPE, null,
					xmlBuffer);
		} catch (JiBXException e) {
			LOGGER.error(e, correlationId);
			throw new BaseException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String.valueOf(ErrorConstants.ERROR_CODE_11523)),
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);
		}  catch (NullPointerException e) {
			LOGGER.error(e, correlationId);
			throw new BaseException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String.valueOf(ErrorConstants.ERROR_CODE_11523)),
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);
		}
		// Removed the feature of printing the xml response in to the logs.
		 LOGGER.info("XML out put generated :" , correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return xmlOutput;
	}

	/**
	 * Method to return the binding factory on the basis of binding name and
	 * class of object to be bound.
	 * 
	 * @param className
	 *            class name to be bound
	 * @param bindingName
	 *            The name of the XML file used for the binding
	 * @param correlationId
	 *            to track the request
	 * @return The binding factory instance
	 */
	private IBindingFactory getBindingFactory(final Class<?> className,
			final String bindingName, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("className: " + className + " ,bindingName : "
				+ bindingName, correlationId);
		IBindingFactory factory = null;
		try {
			factory = BindingDirectory.getFactory(bindingName, className);
		} catch (JiBXException e) {
			LOGGER.error(e, correlationId);
			try {
				factory = BindingDirectory.getFactory(className);
			} catch (JiBXException ex) {
				LOGGER.error(e, correlationId);
			}
		}
		LOGGER.info("IBindingFactory factory : " + factory, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return factory;
	}

}
