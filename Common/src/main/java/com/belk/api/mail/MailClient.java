package com.belk.api.mail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.belk.api.constants.CommonConstants;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggingHelper;
import com.belk.api.util.CommonUtil;
import com.belk.api.util.MailPropertyLoader;

/**
 * This is the utility class for sending mails from the service layer.
 * 
 * @author Mindtree
 * @date Apr 10, 2014
 * 
 */
@Service
public class MailClient {
	/**
	 * Creating logger instance.
	 */

	/*As MailClient is used only by the Config UI, hence the mail events
	 *  are written to the Config UI's logger. */
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger(CommonConstants.CONFIGURATION_WEB_APP_LOGGER);


	@Autowired
	MailPropertyLoader mailPropertyLoader;

	/**
	 * @return the mailPropertyLoader
	 */
	public final MailPropertyLoader getMailPropertyLoader() {
		return this.mailPropertyLoader;
	}

	/**
	 * @param mailPropertyLoader
	 *            the mailPropertyLoader to set
	 */
	public final void setMailPropertyLoader(
			final MailPropertyLoader mailPropertyLoader) {
		this.mailPropertyLoader = mailPropertyLoader;
	}

	/**
	 * Method to send email
	 * 
	 * @param updateRequestUriMap
	 *            request map containing properties to be updated
	 * @param correlationId
	 *            tracking id
	 * @param status
	 *            status of the configuration operation
	 */
	public final void sendEmail(
			final Map<String, List<String>> updateRequestUriMap,
			final String correlationId, final String status) {
		final Date date = new Date();
		final Map<String, String> mailPropertyMap = this.mailPropertyLoader
				.getMailPropertiesMap();
		final String body = this.processMailSubject(updateRequestUriMap,
				correlationId, status);
		final SimpleDateFormat dateFormatter = new SimpleDateFormat(
				CommonConstants.MAIL_DATE_FORMAT);
		final String dateFormat = dateFormatter.format(date);
		final String from = mailPropertyMap.get(CommonConstants.EMAIL_SENDER);
		final String smtpHost = mailPropertyMap
				.get(CommonConstants.EMAIL_SMTP_HOST);
		final String smtpPort = mailPropertyMap
				.get(CommonConstants.EMAIL_SMTP_PORT);
		final String receiver = mailPropertyMap
				.get(CommonConstants.EMAIL_RECEIVER);
		final String subject = mailPropertyMap
				.get(CommonConstants.EMAIL_SUBJECT);
		final String ccRecipients = mailPropertyMap.get(CommonConstants.EMAIL_CC_RECEIVER);

		final String[] to = receiver.split(CommonConstants.COMMA_SEPERATOR);
		final String[] ccTo = ccRecipients.split(CommonConstants.COMMA_SEPERATOR);
		// Setup mail server
		final Properties prop = new Properties();
		prop.put(CommonConstants.MAIL_SMTP_HOST_KEY, smtpHost);
		prop.put(CommonConstants.MAIL_SMTP_PORT_KEY, smtpPort);

		// Get the default Session object
		final Session session = Session.getDefaultInstance(prop, null);
		final MimeMessage message = new MimeMessage(session);

		try {
			this.setMessageParameters(body, dateFormat, from, subject, to, ccTo,
					message);
			Transport.send(message);
			LOGGER.info("Message Sent Successfully", correlationId);
		} catch (MessagingException exception) {
			LOGGER.error(exception, correlationId);
		}
	}

	/**Method to set the message params like to,cc,subject,etc.
	 * @param body Body of the message
	 * @param dateFormat date format to be used in the message
	 * @param from The sender of the message
	 * @param subject Subject of the message
	 * @param message The message to be sent
	 * @param to The list of the addresses to be kept in the To section of the message
	 * @param ccTo The list of the addresses to be kept in the CC section of the message
	 * @throws MessagingException Exception thrown from this method
	 * @throws AddressException Exception thrown if the input mail IDs are syntactically incorrect
	 */
	private void setMessageParameters(final String body,
			final String dateFormat, final String from, final String subject,
			final String[] to, final String[] ccTo, final MimeMessage message)
					throws MessagingException, AddressException {
		message.setFrom(new InternetAddress(from));

		// This for loop is for sending mails to multiple people
		final InternetAddress[] addressTo = new InternetAddress[to.length];
		for (int i = 0; i < to.length; i++) {
			addressTo[i] = new InternetAddress(to[i]);
		}

		final InternetAddress[] addressCCTo = new InternetAddress[to.length];
		for (int i = 0; i < addressCCTo.length; i++) {
			addressCCTo[i] = new InternetAddress(ccTo[i]);
		}

		message.setRecipients(Message.RecipientType.TO, addressTo);
		message.setRecipients(Message.RecipientType.CC, addressCCTo);
		message.setHeader(CommonConstants.MAIL_PRIORITY_KEY,
				CommonConstants.MAIL_PRIORITY_HIGH);
		message.setSubject(subject + " " + dateFormat);
		message.setContent(body, CommonConstants.CONTENT_TYPE_TEXT_HTML);
	}

	/**
	 * Method to populate the mail subject as per the properties changed
	 * 
	 * @param updateRequestUriMap
	 *            Map containing properties changed
	 * @param correlationId
	 *            to track the request
	 * @param status
	 *            status of the update operation
	 * @return mail body containing the details of changes made
	 */
	private String processMailSubject(
			final Map<String, List<String>> updateRequestUriMap,
			final String correlationId, final String status) {
		final StringBuilder mailBodyBuffer = new StringBuilder();
		final Map<String, String> mailPropertyMap = this.mailPropertyLoader
				.getMailPropertiesMap();
		String updatedBy = null;
		/*
		 * The following block sets the updated by part of the mail body to
		 * "updated by api" if the request doesn't come from the UI, otherwise
		 * sets it to the user who updated from the UI
		 */
		if (null != updateRequestUriMap.get(CommonConstants.UPDATED_BY)) {
			updatedBy = StringUtils
					.collectionToCommaDelimitedString(updateRequestUriMap
							.get(CommonConstants.UPDATED_BY));
		} else {
			updatedBy = CommonConstants.UPDATED_BY_API;
		}
		List<String> propertiesList;
		String fileName;
		if (status.equalsIgnoreCase(CommonConstants.SUCCESS)) {
			if (updateRequestUriMap.get(CommonConstants.FILE_NAME) != null) {
				fileName = StringUtils
						.collectionToCommaDelimitedString(updateRequestUriMap
								.get(CommonConstants.FILE_NAME));
				if (updateRequestUriMap
						.containsKey(CommonConstants.PROPERTIES_LIST)) {
					propertiesList = updateRequestUriMap
							.get(CommonConstants.PROPERTIES_LIST);
					mailBodyBuffer
					.append(mailPropertyMap
							.get(CommonConstants.MULTI_PROPERTY_CHANGE_MESSAGE_BODY))
							.append(propertiesList);
				} else if (updateRequestUriMap
						.containsKey(CommonConstants.RELOAD_DEFAULT_VALUES)
						&& CommonConstants.TRUE_VALUE
						.equals(StringUtils
								.collectionToCommaDelimitedString(updateRequestUriMap
										.get(CommonConstants.RELOAD_DEFAULT_VALUES)))) {
					mailBodyBuffer.append(mailPropertyMap
							.get(CommonConstants.RELOADING_MESSAGE_BODY)
							+ fileName);
				} else {
					mailBodyBuffer.append(mailPropertyMap
							.get(CommonConstants.CONFIGURATION_UPDATE_FAILURE));
				}

			} else if (updateRequestUriMap.containsKey(CommonConstants.LOG_LEVEL)) {
				final String logLevel = StringUtils
						.collectionToCommaDelimitedString(updateRequestUriMap
								.get(CommonConstants.LOG_LEVEL));
				mailBodyBuffer
				.append(mailPropertyMap
						.get(CommonConstants.LOG_LEVEL_UPDATE_MESSAGE))
						.append(logLevel);
			}
		} else if (status.equalsIgnoreCase(CommonConstants.FAILURE)) {
			mailBodyBuffer.append(mailPropertyMap
					.get(CommonConstants.CONFIGURATION_UPDATE_FAILURE));
		}
		mailBodyBuffer.append(CommonConstants.NEW_LINE_CHARACTER
				+ mailPropertyMap.get(CommonConstants.UPDATED_SERVER))
				.append(updateRequestUriMap.get(CommonConstants.SERVER_IP));

		mailBodyBuffer.append(CommonConstants.NEW_LINE_CHARACTER
				+ mailPropertyMap.get(CommonConstants.UPDATED_BY_MESSAGE))
				.append(updatedBy);
		mailBodyBuffer.append(CommonConstants.NEW_LINE_CHARACTER
				+ mailPropertyMap.get(CommonConstants.UPDATED_SERVICE))
				.append(getCallerServiceName());
		LOGGER.debug("Mail subject: " + mailBodyBuffer.toString(),
				correlationId);
		return mailBodyBuffer.toString();
	}

	/**Method to return the name of the service for which the mail has to be triggered.
	 * @return Calling service's name
	 */
	private static String getCallerServiceName() {
		String callerServiceName = null;
		final StackTraceElement[] stackElements = Thread.currentThread().getStackTrace();
		for (int i = 1; i < stackElements.length; i++) {
			final StackTraceElement stackElement = stackElements[i];
			if (!stackElement.getClassName().equals(MailClient.class.getName())
					&& stackElement.getClassName().indexOf("java.lang.Thread") != 0) {
				callerServiceName =  stackElement.getClassName();
				break;
			}
		}

		/*TODO: to decide later whether the updating service should send it's own name
		or it needs to be discovered by this class itself. Accordingly the hardcodings below are to be removed.*/
		return CommonUtil.splitCamelCase(callerServiceName.replaceAll("com.belk.api.service.impl.", "")
				.replace("Impl", ""));
	}
	
	public final void sendCoremetricsMail() {
		final Date date = new Date();
		final Map<String, String> mailPropertyMap = this.mailPropertyLoader
				.getMailPropertiesMap();
		final String body = this.mailPropertyLoader
				.getMailPropertiesMap().get(CommonConstants.COREMETRICS_BODY);
		final SimpleDateFormat dateFormatter = new SimpleDateFormat(
				CommonConstants.MAIL_DATE_FORMAT);
		final String dateFormat = dateFormatter.format(date);
		final String from = mailPropertyMap.get(CommonConstants.EMAIL_SENDER);
		final String smtpHost = mailPropertyMap
				.get(CommonConstants.EMAIL_SMTP_HOST);
		final String smtpPort = mailPropertyMap
				.get(CommonConstants.EMAIL_SMTP_PORT);
		final String receiver = mailPropertyMap
				.get(CommonConstants.EMAIL_RECEIVER);
		final String subject = mailPropertyMap
				.get(CommonConstants.COREMETRICS_SUBJECT);
		final String ccRecipients = mailPropertyMap.get(CommonConstants.EMAIL_CC_RECEIVER);

		final String[] to = receiver.split(CommonConstants.COMMA_SEPERATOR);
		final String[] ccTo = ccRecipients.split(CommonConstants.COMMA_SEPERATOR);
		// Setup mail server
		final Properties prop = new Properties();
		prop.put(CommonConstants.MAIL_SMTP_HOST_KEY, smtpHost);
		prop.put(CommonConstants.MAIL_SMTP_PORT_KEY, smtpPort);

		// Get the default Session object
		final Session session = Session.getDefaultInstance(prop, null);
		final MimeMessage message = new MimeMessage(session);
		try {
			this.setMessageParameters(body, dateFormat, from, subject, to, ccTo,
					message);
			
			Transport.send(message);
			LOGGER.info("Message Sent Successfully", "Admin Coremetrics API");
		} catch (MessagingException exception) {
			LOGGER.error(exception, "Admin Coremetrics API");
		}
	}
}
