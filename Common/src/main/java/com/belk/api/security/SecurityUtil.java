package com.belk.api.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.belk.api.cache.CacheManager;
import com.belk.api.constants.CommonConstants;
import com.belk.api.constants.ErrorConstants;
import com.belk.api.exception.BaseException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.util.CommonUtil;
import com.belk.api.util.ErrorLoader;
import com.belk.api.util.SecurityLoader;

/**
 * This class contains the utility methods required for the security operations.
 * 
 * @author Mindtree
 * @date 29 May, 2014
 * 
 */
@Service
public class SecurityUtil {

	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	@Autowired
	SecurityLoader securityLoader;

	@Autowired
	CacheManager cacheManager;

	@Autowired
	ErrorLoader errorLoader;

	@Autowired
	CommonUtil commonUtil;

	/**
	 * @param securityLoader
	 *            the securityLoader to set
	 */
	public final void setSecurityLoader(final SecurityLoader securityLoader) {
		this.securityLoader = securityLoader;
	}

	/**
	 * @param cacheManager
	 *            the cacheManager to set
	 */
	public final void setCacheManager(final CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	/**
	 * setter method for the errorLoader.
	 * 
	 * @param errorLoader
	 *            the errorLoader to set
	 */
	public final void setErrorLoader(final ErrorLoader errorLoader) {
		this.errorLoader = errorLoader;
	}

	/**
	 * @param commonUtil
	 *            the commonUtil to set
	 */
	public final void setCommonUtil(final CommonUtil commonUtil) {
		this.commonUtil = commonUtil;
	}

	/**
	 * Method to decrypt an encrypted string based on the passed key and
	 * encryption algorithm.
	 * 
	 * @param encryptedString
	 *            String to be decrypted
	 * @param inEncryptionKey
	 *            key to be used for decryption as per request
	 * @param correlationId
	 *            to track the request
	 * @return decrypted string
	 * @throws BaseException
	 *             Exception thrown from this method.
	 */
	public final String decrypt(final String encryptedString,
			final String inEncryptionKey, final String correlationId)
					throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		String decryptedString = null;
		String encryptionKey = null;
		SecretKeyFactory keyFactory = null;
		DESKeySpec desKeySpec = null;
		SecretKey secretKey = null;
		Cipher cipher = null;
		final Map<String, String> errorPropertiesMap = this.errorLoader
				.getErrorPropertiesMap();

		if (!this.commonUtil.isNotEmpty(inEncryptionKey, correlationId)) {
			LOGGER.debug("Common encryption key chosen", correlationId);
			encryptionKey = this.securityLoader.getSecurityPropertiesMap().get(
					CommonConstants.COMMON_ENCRYPTION_KEY);
		} else {
			LOGGER.debug("App specific encryption key chosen", correlationId);
			encryptionKey = inEncryptionKey;
		}
		if (!this.commonUtil.isNotEmpty(encryptedString, correlationId)) {
			throw new BaseException(
					String.valueOf(ErrorConstants.ERROR_CODE_11425),
					errorPropertiesMap.get(String
							.valueOf(ErrorConstants.ERROR_CODE_11425)),
							ErrorConstants.HTTP_STATUS_CODE_INVALID_URL_PARAMETERS,
							correlationId);
		}
		try {
			// Retrieve the key factory for DES
			keyFactory = SecretKeyFactory
					.getInstance(CommonConstants.CIPHER_ALGORITHM_DES);

			// create a key spec from the encryption key obtained above earlier.
			desKeySpec = new DESKeySpec(
					encryptionKey.getBytes(CommonConstants.UTF_TYPE));

			// Generate a SecretKey object from the factory for the encryption
			// key
			secretKey = keyFactory.generateSecret(desKeySpec);
			cipher = Cipher
					.getInstance(CommonConstants.CIPHER_TRANSFORMATION_DES_ECB_PKCS5PADDING);

			// Initialize the cipher for decryption
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			decryptedString = new String(cipher.doFinal(Base64
					.decodeBase64(encryptedString
							.getBytes(CommonConstants.UTF_TYPE))));

		} catch (NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException | InvalidKeySpecException
				| UnsupportedEncodingException e) {
			/*
			 * Return an unauthorized due to invalid token error for any the
			 * above exceptions that have occurred.
			 */
			LOGGER.error(e, correlationId);
			throw new BaseException(
					String.valueOf(ErrorConstants.ERROR_CODE_INVALID_TOKEN),
					errorPropertiesMap.get(String
							.valueOf(ErrorConstants.ERROR_CODE_INVALID_TOKEN)),
							ErrorConstants.HTTP_STATUS_CODE_UNAUTHORIZED, correlationId);
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return decryptedString;
	}

	/**
	 * Method to encrypt a string based on the passed key and encryption
	 * algorithm.
	 * 
	 * @param originalString
	 *            String to be encrypted
	 * @param inEncryptionKey
	 *            key to be used for encryption as per request
	 * @param correlationId
	 *            to track the request
	 * @return encrypted string
	 * @throws BaseException
	 *             exception thrown from this method
	 */
	public final String encrypt(final String originalString,
			final String inEncryptionKey, final String correlationId)
					throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		String encryptionKey = null;
		String encryptedString = null;
		SecretKeyFactory keyFactory = null;
		DESKeySpec desKeySpec = null;
		SecretKey secretKey = null;
		Cipher cipher = null;
		final Map<String, String> errorPropertiesMap = this.errorLoader
				.getErrorPropertiesMap();

		if (!this.commonUtil.isNotEmpty(originalString, correlationId)) {
			throw new BaseException(
					String.valueOf(ErrorConstants.ERROR_CODE_11425),
					errorPropertiesMap.get(String
							.valueOf(ErrorConstants.ERROR_CODE_11425)),
							ErrorConstants.HTTP_STATUS_CODE_INVALID_URL_PARAMETERS,
							correlationId);
		}

		if (inEncryptionKey.equals(CommonConstants.EMPTY_STRING)) {
			LOGGER.debug("Common encryption key chosen", correlationId);
			encryptionKey = this.securityLoader.getSecurityPropertiesMap().get(
					CommonConstants.COMMON_ENCRYPTION_KEY);
		} else {
			LOGGER.debug("App specific encryption key chosen", correlationId);
			encryptionKey = inEncryptionKey;
		}

		try {
			// Retrieve the key factory for DES
			keyFactory = SecretKeyFactory
					.getInstance(CommonConstants.CIPHER_ALGORITHM_DES);

			// create a key spec from the encryption key obtained above earlier.
			desKeySpec = new DESKeySpec(
					encryptionKey.getBytes(CommonConstants.UTF_TYPE));

			/*
			 * Generate a SecretKey object from the factory for the encryption
			 * key
			 */
			secretKey = keyFactory.generateSecret(desKeySpec);
			cipher = Cipher
					.getInstance(CommonConstants.CIPHER_TRANSFORMATION_DES_ECB_PKCS5PADDING);

			// Initialize the cipher for encryption
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			encryptedString = new String(
					Base64.encodeBase64(cipher.doFinal(originalString
							.getBytes(CommonConstants.UTF_TYPE))));

		} catch (NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException | InvalidKeySpecException
				| UnsupportedEncodingException e) {
			throw new BaseException(
					String.valueOf(ErrorConstants.ERROR_CODE_INVALID_TOKEN),
					errorPropertiesMap.get(String
							.valueOf(ErrorConstants.ERROR_CODE_INVALID_TOKEN)),
							ErrorConstants.HTTP_STATUS_CODE_UNAUTHORIZED, correlationId);
		}

		LOGGER.logMethodExit(startTime, correlationId);
		return encryptedString;
	}

	/**
	 * Generates a token by encrypting the incoming token params by the current
	 * system time.
	 * 
	 * @param tokenRequestParamString
	 *            The request string on which the auth token is to be generated.
	 * @param correlationId
	 *            to track the request
	 * @return a token string
	 * @throws BaseException
	 *             The exception thrown from this method.
	 */
	public final String generateAuthToken(final String tokenRequestParamString,
			final String correlationId)
					throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final String authToken = this.encrypt(tokenRequestParamString,
				String.valueOf(System.currentTimeMillis()), correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return authToken;
	}

	/**
	 * Checks whether the passed request is still alive or not.
	 * 
	 * @param timestampToken
	 *            the timestamp for which validity is to be checked
	 * @param correlationId
	 *            to track the request
	 * @return Whether request is alive or not
	 * @throws BaseException
	 *             Exception thrown from this method
	 */
	public final boolean isRequestAlive(final long timestampToken,
			final String correlationId) throws BaseException {
		return System.currentTimeMillis() - timestampToken < this
				.getMaxTokenGapTime(correlationId);
	}

	/**
	 * Returns the application specific encryption key (EK) as present in the
	 * security.properties
	 * 
	 * @param encryptedAppId
	 *            encrypted application id
	 * @param correlationId
	 *            to track the request
	 * @return application specific EK
	 * @throws BaseException
	 *             The exception thrown from this method.
	 */
	public final String getCallingApplicationId(final String encryptedAppId,
			final String correlationId)
					throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final String callingAppId = this.decrypt(encryptedAppId,
				CommonConstants.EMPTY_STRING,
				correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return callingAppId;
	}

	/**
	 * Returns the application specific encryption key as present in
	 * security.properties.
	 * 
	 * @param appId
	 *            Application id for which the specific encryption key is
	 *            requested for
	 * @param correlationId
	 *            to track the request
	 * @return application specific encryption key as present in
	 *         security.properties
	 */
	public final String getAppSpecificEncryptionKey(final String appId,
			final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final String appSpecificEK = this.securityLoader
				.getSecurityPropertiesMap().get(
						appId + CommonConstants.ENCRYPTION_KEY_SUFFIX);
		LOGGER.logMethodExit(startTime, correlationId);
		return appSpecificEK;
	}

	/**
	 * Resets the token TTL if accessed before expiry.
	 * 
	 */
	public void resetTtlForTokenInCache() {

	}

	/**
	 * Verifies whether the token present in the encrypted request is valid or
	 * not. The validity is checked in terms of whether the token is
	 * semantically a valid encrypted string or is still alive and available in
	 * the cache.
	 * 
	 * @param encryptedAppId
	 *            the encrypted application id
	 * @param encryptedKeyParams
	 *            the encrypted application params passed in the request. This
	 *            would be in the form of CA:UID:AT:TS where CA= calling
	 *            application ID UID= user id of the logged in user AT=
	 *            authentication token issued to this user for this application
	 *            TS= timestamp when the incoming request was created
	 * @param correlationId
	 *            to track the request
	 * @return whether the token is valid or not
	 * @throws BaseException
	 *             Exception thrown from this method
	 */
	public final boolean validateToken(final String encryptedAppId,
			final String encryptedKeyParams, final String correlationId)
					throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		String appId;
		String appSpecificEK;
		String appKeyParams;
		String authToken = null;
		String cacheKey;
		String cacheValue;
		boolean isTokenValid = false;
		final Map<String, String> errorPropertiesMap = this.errorLoader
				.getErrorPropertiesMap();
		LOGGER.debug("encyptedAppId:: " + encryptedAppId, correlationId);
		LOGGER.debug("encryptedKeyParams:: " + encryptedKeyParams,
				correlationId);
		appId = this.getCallingApplicationId(encryptedAppId, correlationId);
		appSpecificEK = this.getAppSpecificEncryptionKey(appId, correlationId);
		appKeyParams = this.decrypt(encryptedKeyParams, appSpecificEK,
				correlationId);
		LOGGER.debug("decrypted stuff:: " + appId + "~~" + appSpecificEK + "~~" + appKeyParams, correlationId);
		/* appKeyParams is in the format {app id:user id:timestamp:auth token} */
		cacheKey = appKeyParams.split(CommonConstants.FIELD_PAIR_SEPARATOR)[0]
				+ CommonConstants.FIELD_PAIR_SEPARATOR
				+ appKeyParams.split(CommonConstants.FIELD_PAIR_SEPARATOR)[1];
		authToken = appKeyParams.split(CommonConstants.FIELD_PAIR_SEPARATOR)[3];
		LOGGER.debug("Auth token from request:: " + authToken, correlationId);
		LOGGER.debug("cacheKey:: " + cacheKey, correlationId);
		cacheValue = this.cacheManager.get(cacheKey, String.class,
				correlationId);

		if (null != cacheValue) {
			LOGGER.debug("cacheValue:: " + cacheValue, correlationId);
			if (authToken.equals(cacheValue)) {
				isTokenValid = true;
			}
		} else {
			LOGGER.info("Expiry exception thrown from SecurityUtil",
					correlationId);
			throw new BaseException(
					String.valueOf(ErrorConstants.ERROR_CODE_EXPIRED_TOKEN),
					errorPropertiesMap.get(String
							.valueOf(ErrorConstants.ERROR_CODE_EXPIRED_TOKEN)),
							ErrorConstants.HTTP_STATUS_CODE_UNAUTHORIZED, correlationId);
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return isTokenValid;
	}

	/**
	 * Returns the max time gap allowed for a request to be served with a token
	 * as set in security.properties
	 * 
	 * @param correlationId
	 *            to track the request
	 * @return max time gap for a request
	 * @throws BaseException
	 *             Exception thrown from this method
	 */
	private long getMaxTokenGapTime(final String correlationId)
			throws BaseException {
		long maxTokenGapTime = 0;
		try {
			maxTokenGapTime = Long.parseLong(this.securityLoader
					.getSecurityPropertiesMap().get(
							CommonConstants.MAX_TOKEN_GAP_TIME));
		} catch (NumberFormatException e) {
			LOGGER.error(e, correlationId);
			throw new BaseException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					ErrorConstants.ERRORDESC_INTERNAL_ERROR,
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);
		}
		return maxTokenGapTime;
	}

	/**
	 * Saves the newly generated token in the cache.
	 * 
	 * @param cacheKey
	 *            the key to be used for storing in the cache.
	 * @param authToken
	 *            the value to be stored in the cache
	 * @param correlationId
	 *            to track the request
	 * @throws BaseException
	 *             Exception thrown from this method
	 */
	public final void storeAuthTokenInCache(final String cacheKey,
			final String authToken, final String correlationId)
					throws BaseException {
		final int expireTime = this.getExpireTime(correlationId);
		this.cacheManager.set(cacheKey, authToken, expireTime, correlationId);
	}

	/**
	 * Returns the expiry time(in sec) to be set for a token in the cache.
	 * 
	 * @param correlationId
	 *            to track the request
	 * @return the expiry time as specified in the security.properties
	 * @throws BaseException
	 *             Exception thrown from this method
	 */
	private int getExpireTime(final String correlationId) throws BaseException {
		int expirytime = 0;
		try {
			expirytime = Integer.parseInt(this.securityLoader
					.getSecurityPropertiesMap()
					.get(CommonConstants.TOKEN_EXPIRY_TIME));
		} catch (NumberFormatException e) {
			LOGGER.error(e, correlationId);
			throw new BaseException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					ErrorConstants.ERRORDESC_INTERNAL_ERROR,
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);
		}
		return expirytime;
	}

}
