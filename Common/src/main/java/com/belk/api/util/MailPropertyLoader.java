/**
 * 
 */
package com.belk.api.util;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.belk.api.constants.CommonConstants;

/**This class to load mail.properties into a map during server start up.
 * @author Mindtree
 * @date Apr 11, 2014
 *
 */
@Service
public class MailPropertyLoader extends PropertyLoader implements InitializingBean {

	private Map<String, String> mailPropertiesMap;

	private String filePath;

	@Override
	public final void afterPropertiesSet() throws Exception {
		final String correlationId = CommonConstants.EMPTY_STRING;
		this.filePath = CommonUtil.getConfigurationFilePath(
				CommonConstants.MAIL_RESOURCE_BUNDLE,
				CommonConstants.COMMON, correlationId);
		this.mailPropertiesMap = loadProperty(this.filePath, correlationId);
	}

	/**
	 * @return the mailPropertiesMap
	 */
	public final Map<String, String> getMailPropertiesMap() {
		return this.mailPropertiesMap;
	}

	/**
	 * @param mailPropertiesMap the mailPropertiesMap to set
	 */
	public final void setMailPropertiesMap(final Map<String, String> mailPropertiesMap) {
		this.mailPropertiesMap = mailPropertiesMap;
	}

}
