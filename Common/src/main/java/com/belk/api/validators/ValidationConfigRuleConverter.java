package com.belk.api.validators;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;

/**
 * This class performs required JIBX marshalling and unmarshalling for
 * validation.
 * 
 * @author Mindtree
 * 
 */
public class ValidationConfigRuleConverter {
	
	/**
	 * Creating logger instance.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	/**
	 * Method to get the validation configuration resources.
	 * 
	 * @param xmlFileName
	 *            - name of the xml file which contains configuration for
	 *            validation.
	 * @param correlationId
	 *            - to track the request param.
	 * @return resourceValidationConfig - Instance of ResourceValidationConfig
	 *         class.
	 */
	public final ResourceValidationConfig getResourceValidationConfig(
			final String xmlFileName, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("xmlFileName :" + xmlFileName, correlationId);
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext
					.newInstance(ResourceValidationConfig.class);

			final Unmarshaller jaxbUnmarshaller = jaxbContext
					.createUnmarshaller();

			final ResourceValidationConfig resourceValidationConfig = (ResourceValidationConfig) jaxbUnmarshaller
					.unmarshal(new File(xmlFileName));
			LOGGER.logMethodExit(startTime, correlationId);
			return resourceValidationConfig;
		} catch (JAXBException jaxbException) {
			LOGGER.error(jaxbException, correlationId);
			return null;
		}

	}

	/**
	 * Method to get the configuration for validation path.
	 * 
	 * @param xmlFileName
	 *            - name of the xml file which contains the configuration.
	 * @param correlationId
	 *            - to track the request param.
	 * @return resourceValidationConfig - Instance of ValidationConfig class.
	 */
	public final ValidationConfig getValidationPathConfig(
			final String xmlFileName, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("xmlFileName :" + xmlFileName, correlationId);
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(ValidationConfig.class);

			final Unmarshaller jaxbUnmarshaller = jaxbContext
					.createUnmarshaller();

			final ValidationConfig resourceValidationConfig = (ValidationConfig) jaxbUnmarshaller
					.unmarshal(new File(xmlFileName));
			LOGGER.logMethodExit(startTime, correlationId);
			return resourceValidationConfig;
		} catch (JAXBException jaxbException) {
			LOGGER.error(jaxbException, correlationId);
			return null;
		}

	}

}
