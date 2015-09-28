/**
 * 
 */
package com.belk.api.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.sql.DataSource;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.dbcp.BasicDataSource;

import com.belk.api.constants.CommonConstants;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;


/**
 * This class would map the dbConnection.properties file loaded by 
 * DBConnectionLoader and set the values to Datasource.
 * Also the class would take care of decrypting the DB Password
 * encoded using environment specific keys
 *    
 * @author AFURXG9
 *
 */
public class DataSourceGenerator {

	/**
	 * get the logger instance.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	/**
	 * instance of Data Source
	 */
	private BasicDataSource bmDataSource;
	
	/**
	 * instance of DB Connection Loader
	 */
	private DBConnectionLoader dbConnectionLoader;

	
	/**
	 * Initialization method
	 */
	@PostConstruct
	private void init() {
		final String correlationId = CommonConstants.EMPTY_STRING;
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final Map<String, String> connectionProperties = this.dbConnectionLoader.getDbConnectionPropertiesMap();
		loadBMDatasource(connectionProperties);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * @param connectionProperties
	 */
	private void loadBMDatasource(final Map<String, String> connectionProperties) {
		final String correlationId = CommonConstants.EMPTY_STRING;
		final long startTime = LOGGER.logMethodEntry(correlationId);
		this.bmDataSource.setDriverClassName(
				connectionProperties.get(CommonConstants.BM_DB_DRIVER));
		this.bmDataSource.setUrl(
				connectionProperties.get(CommonConstants.BM_DB_URL));
		this.bmDataSource.setUsername(
				connectionProperties.get(CommonConstants.BM_DB_USER_NAME));
		final String dbPassword = connectionProperties.get(CommonConstants.BM_DB_PASSWORD);
		final String cipherKey = connectionProperties.get(CommonConstants.BM_PASSWORD_KEY);
		final String decryptPwd = DataSourceGenerator.decrypt(dbPassword, cipherKey);
		this.bmDataSource.setPassword(decryptPwd);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * @param encryptedText
	 * @param key
	 * @return
	 */
	private static String decrypt(final String encryptedText, final String key) {
		final String correlationId = CommonConstants.EMPTY_STRING;
		final long startTime = LOGGER.logMethodEntry(correlationId);
		byte[]	decryptedTextBytes = null;	
		try {
			final SecretKeySpec keySpec = new SecretKeySpec(
					key.getBytes(CommonConstants.UTF_TYPE) , 
					CommonConstants.AES);
			// Instantiate the cipher
			final Cipher cipher = Cipher.getInstance(CommonConstants.AES);
			cipher.init(Cipher.DECRYPT_MODE, keySpec);
			final byte[] encryptedTextBytes = Base64.decodeBase64(encryptedText);
			decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
		} catch (NoSuchPaddingException ex) {
			LOGGER.error(ex, "Error on Decrypting Database password");
		} catch (UnsupportedEncodingException ex) {
			LOGGER.error(ex, "Error on Decrypting Database password");
		} catch (NoSuchAlgorithmException ex) {
			LOGGER.error(ex, "Error on Decrypting Database password");
		} catch (InvalidKeyException ex) {
			LOGGER.error(ex, "Error on Decrypting Database password");
		} catch (BadPaddingException ex) {
			LOGGER.error(ex, "Error on Decrypting Database password");
		} catch (IllegalBlockSizeException ex) {
			LOGGER.error(ex, "Error on Decrypting Database password");
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return new String(decryptedTextBytes);
	}
	
	/**
	 * @return the dbConnectionLoader
	 */
	public final DBConnectionLoader getDbConnectionLoader() {
		return this.dbConnectionLoader;
	}

	/**
	 * @param dbConnectionLoader the dbConnectionLoader to set
	 */
	public final void setDbConnectionLoader(final DBConnectionLoader dbConnectionLoader) {
		this.dbConnectionLoader = dbConnectionLoader;
	}

	/**
	 * @return the bmDataSource
	 */
	public final DataSource getBmDataSource() {
		return (DataSource) this.bmDataSource;
	}

	/**
	 * @param bmDataSource the bmDataSource to set
	 */
	public final void setBmDataSource(final DataSource bmDataSource) {
		this.bmDataSource = (BasicDataSource) bmDataSource;
	}
}
