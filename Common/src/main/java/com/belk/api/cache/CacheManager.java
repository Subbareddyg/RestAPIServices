package com.belk.api.cache;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.DefaultConnectionFactory;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.OperationTimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.belk.api.constants.CommonConstants;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggingHelper;
import com.belk.api.util.MemcachedLoader;

/**
 * The Cache manager provides the implementation for the Memcached. It exposes
 * the methods for setting in and retrieval from the cache.
 * 
 * Updated : Added few comments and changed the name of variables as part of Phase2, April,14 release
 * 
 * @author Mindtree
 * @date Dec 3, 2013
 */
@Component("cacheManager")
public class CacheManager {

	/**
	 * get the logger instance.
	 */
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger(CacheManager.class.getName());

	/**
	 * creating instance of memcached Loader class.
	 */
	@Autowired
	MemcachedLoader memcachedLoader;
	/**
	 * create a Map<String, SubCacheConf> called cacheKeyMap.
	 */
	private Map<String, SubCacheConf> cacheKeyMap;

	/**
	 * create a instance of MemcachedClient.
	 */
	private MemcachedClient memCachedClient;

	/**
	 * Default constructor.
	 */
	public CacheManager() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * setter method for the memcachedLoader.
	 * 
	 * @param memcachedLoader the memcachedLoader to set
	 */
	public final void setMemcachedLoader(final MemcachedLoader memcachedLoader) {
		this.memcachedLoader = memcachedLoader;
	}
	/**
	 * This is a static class used in the CacheManager class to have the
	 * prepending String and expiration time for the generic class.
	 */
	private static final class SubCacheConf {
		/**
		 * declaring prepending string.
		 */
		private String prepend;
		/**
		 * declare expiration for storing expiration time.
		 */
		private int expiration;

		/**
		 * Default constructor.
		 */
		private SubCacheConf() {

		}

	}


	/**
	 * This method checks whether the cache has been configured to be made
	 * available.
	 * 
	 * @return true if the cache is configured to be made available, else false.
	 */
	private boolean isCacheAvailable() {
		final Map<String, String> memcachePropertiesMap = this.memcachedLoader
				.getMemcachePropertiesMap();
		if (CommonConstants.TRUE_VALUE.equals(memcachePropertiesMap
				.get(CommonConstants.AVAILABLE))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method checks whether the class for which an object is to be stored
	 * in the cache is cacheable or not.
	 * 
	 * @param <T>
	 *            The generic class to be used.
	 * @param clazz
	 *            the generic class which is to be checked for cacheable.
	 * @return return true if the class is cacheable, else false.
	 */
	public final <T> boolean isCacheable(final Class<T> clazz) {
		Boolean cachable = false;
		if (this.isCacheAvailable()
				&& this.cacheKeyMap.containsKey(clazz.getName())) {
			cachable = Boolean.TRUE;
		}
		return cachable;
	}

	/**
	 * This method checks whether memcached client is connected.
	 * 
	 * @return true if memcached client is connected, else false.
	 */
	private synchronized boolean isConnected() {
		return this.memCachedClient != null;
	}

	/**
	 * This method is used to set the objects in the cache.
	 * 
	 * @param cacheId
	 *            ID of the object which needs to be set in the cache; this is
	 *            used to form the key for storing in the cache.
	 * @param value
	 *            {@link Serializable} The object to be stored in the cache
	 * @param correlationId
	 *            tracking Id
	 */
	public final void set(final String cacheId, final Serializable value,
			final String correlationId) {
		if (this.isConnected() && this.isCacheable(value.getClass())) {
			try {
				if(cacheId.contains(" ")) {
					return;
				}
				final String key = this.getKeyPrepend(value.getClass())
						+ CommonConstants.UNDERSCORE + cacheId;
				this.memCachedClient.set(key, this.getObjExpiration(value.getClass()
						.getName()), value);
				/* Commented out for future release
				 * setPrimaryCacheKey(key, this.getObjExpiration(value.getClass()
						.getName()), correlationId);*/
			} catch (final OperationTimeoutException timeoutex) {
				LOGGER.error(timeoutex,
						correlationId);
			}
		}
	}

	/**
	 * This method is used to set the objects in the cache alongwith a specified
	 * expiration time.
	 * 
	 * @param cacheId
	 *            ID of the object which needs to be set in the cache; this is
	 *            used to form the key for storing in the cache.
	 * @param value
	 *            {@link Serializable}The object to be stored in the cache
	 * @param expireTime
	 *            The specific expiration time to be set for this object
	 * @param correlationId
	 *            tracking Id
	 */
	public final void set(final String cacheId, final Serializable value,
			final int expireTime, final String correlationId) {
		if (this.isConnected() && this.isCacheable(value.getClass())) {
			try {
				final String key = this.getKeyPrepend(value.getClass())
						+ CommonConstants.UNDERSCORE + cacheId;
				this.memCachedClient.set(key, expireTime, value);
				/*Commented out for future release
				 * setPrimaryCacheKey(key, expireTime, correlationId);*/
			} catch (final OperationTimeoutException timeoutex) {
				LOGGER.error(timeoutex,
						correlationId);
			}
		}
	}

	/**
	 * This method is used to retrieve a particular object from the cache based
	 * on its id.
	 * 
	 * @param <T>
	 *            The generic class to be used.
	 * @param cacheId
	 *            The id for which the cache data is being retrieved.
	 * @param classType
	 *            Class type of the object that is being retrieved
	 * @param correlationId
	 *            tracking id
	 * @return The retrieved object from the cache
	 */

	public final <T> T get(final String cacheId, final Class<T> classType,
			final String correlationId) {
		if (!this.isConnected() || !this.isCacheable(classType)) {
			return null;
		}

		Object obj = null;

		try {
			final String key = this.getKeyPrepend(classType)
					+ CommonConstants.UNDERSCORE + cacheId;
			obj = this.memCachedClient.get(key);

		} catch (final OperationTimeoutException timeoutex) {
			LOGGER.error(timeoutex,
					correlationId);
			return null;
		}

		if (obj != null) {
			try {
				return classType.cast(obj);
			} catch (final ClassCastException exception) {
				LOGGER.error(exception,
						correlationId);
			}
		}
		return null;
	}

	/**
	 * This method gets the expiration time set for a particular class.
	 * 
	 * @param className
	 *            the class for which the Object expiration to be checked
	 * @return the expiration time for the particular class
	 */
	private int getObjExpiration(final String className) {
		final SubCacheConf subCacheConf = this.cacheKeyMap.get(className);
		return subCacheConf.expiration;
	}

	/**
	 * This method retrieves the prepend key for a class.
	 * 
	 * @param clazz
	 *            The class for which the prepend key is being generated
	 * @return formatted prepend key
	 */
	private String getKeyPrepend(final Class<?> clazz) {
		return this.getKeyPrepend(clazz.getName());
	}

	/**
	 * This method retrieves the properly formatted prepend key for a class.
	 * 
	 * @param className
	 *            The class for which the prepend key is being generated
	 * @return formatted prepend key
	 */
	private String getKeyPrepend(final String className) {
		final SubCacheConf subCacheConf = this.cacheKeyMap.get(className);
		final StringBuilder keyPrepend = new StringBuilder(
				CommonConstants.SERVICES);
		keyPrepend.append(CommonConstants.UNDERSCORE);
		keyPrepend.append(subCacheConf.prepend);
		return keyPrepend.toString();
	}

	/**
	 * This method populates the map of the keys obtained from the properties,
	 * to the cache.
	 */
	private void populateCacheKeyMap() {
		this.cacheKeyMap = new HashMap<String, SubCacheConf>();
		final Map<String, String> memcachePropertiesMap = this.memcachedLoader
				.getMemcachePropertiesMap();

		final Set<Entry<String, String>> cacheKeyList = memcachePropertiesMap
				.entrySet();
		final Pattern keyPattern = Pattern.compile(CommonConstants.KEY_PATTERN);
		final Iterator<Entry<String, String>> cacheKeyIterator = cacheKeyList
				.iterator();
		while (cacheKeyIterator.hasNext()) {
			final String key = cacheKeyIterator.next().getKey();
			final Matcher keyMatcher = keyPattern.matcher(key);
			if (keyMatcher.matches()) {
				final String middleString = keyMatcher.group(1);
				final String prepend = CommonConstants.CACHE_OBJECT
						+ CommonConstants.DOT + middleString
						+ CommonConstants.DOT + CommonConstants.PREPEND;
				final String expiration = CommonConstants.CACHE_OBJECT
						+ CommonConstants.DOT + middleString
						+ CommonConstants.DOT + CommonConstants.EXPIRATION;
				final String cacheTerm = memcachePropertiesMap.get(key);

				final SubCacheConf subCache = new SubCacheConf();
				subCache.prepend = memcachePropertiesMap.get(prepend);
				subCache.expiration = Integer.parseInt(memcachePropertiesMap
						.get(expiration));
				this.cacheKeyMap.put(cacheTerm, subCache);
			}
		}
	}

	/**
	 * This method initializes the Memcache, creating a cache client connection
	 * with the default values. This method is called on the server startup.
	 */
	@PostConstruct
	public final void init() {
		final Map<String, String> memcachePropertiesMap = this.memcachedLoader
				.getMemcachePropertiesMap();
		if (this.cacheKeyMap == null) {
			// Populate the memcached key map
			this.populateCacheKeyMap();
		}
		// Connect to memcached
		try {
			this.memCachedClient = new MemcachedClient(
					new DefaultConnectionFactory(),
					AddrUtil.getAddresses(memcachePropertiesMap
							.get("hostnames")));
			if (this.memCachedClient != null) {
				LOGGER.info(
						"Waiting for Memcached to connect (10 attempts max, 500 ms each)",
						"is 1");
				Thread.sleep(500);
				for (int i = 0; (this.memCachedClient.getAvailableServers()
						.isEmpty()) && (i < 10); i++) {
					Thread.sleep(500);
				}
			}
			if (!this.memCachedClient.getAvailableServers().isEmpty()) {
				LOGGER.info("MemcachedClient is connected to hostnames: "
						+ this.memCachedClient.getAvailableServers(), ": 1");
				// Create the main cache key for Cache Admin Screen purpose
				/*Commented out for future release
				 * createPrimaryCacheKey();*/
			} else {
				// memcached server is unavailable, hence shutdown memcached
				this.memCachedClient.shutdown();
				this.memCachedClient = null;
			}
		} catch (final IOException exception) {
			LOGGER.error(exception, "1");

		} catch (final InterruptedException exception) {
			LOGGER.error(exception, "1.");
		}

	}

	/**
	 * This method shuts down the memcache client before the server is shutdown.
	 * 
	 */
	@PreDestroy
	private synchronized void shutDown() {
		if (this.memCachedClient != null) {
			LOGGER.info("Shutting down memcached", ": 1.");
			this.memCachedClient.shutdown();
			this.memCachedClient = null;
		}
	}

	/**
	 * This method create the primary cache key which will hold all the cache keys 
	 * allocated to the all servers in an environment 
	 * 
	 */
	private void createPrimaryCacheKey() {
		try {
			final Map<String, Integer> keyList = new HashMap<String, Integer>();
			final int expirationTime = Integer.parseInt(this.memcachedLoader.getMemcachePropertiesMap().
					get(CommonConstants.PRIMARY_CACHE_EXPIRATION));
			this.memCachedClient.set(CommonConstants.PRIMARY_CACHE_KEY, expirationTime, keyList);
		}  catch (final OperationTimeoutException timeoutex) {
			LOGGER.error(timeoutex,
					":1");
		}
	}

	/** 
	 * This method sets the created cache key to a master list of cache keys.
	 * This is for the purpose of cache monitoring screens
	 *
	 * @param keyName is the Key name to be added to the master key list
	 * @param correlationId - the correlation Id for the request
	 * @param expireTime - expiration Time
	 */
	private void setPrimaryCacheKey(final String keyName, final int expireTime, final String correlationId) {
		try {
			final Map<String, Integer> keyList = (Map<String, Integer>)
					this.memCachedClient.get(CommonConstants.PRIMARY_CACHE_KEY);
			if (keyList != null) {
				keyList.put(keyName, expireTime);
				final int expirationTime = Integer.parseInt(this.memcachedLoader.getMemcachePropertiesMap().
								get(CommonConstants.PRIMARY_CACHE_EXPIRATION));
				this.memCachedClient.set(CommonConstants.PRIMARY_CACHE_KEY, expirationTime, keyList);
			}
		} catch (final OperationTimeoutException timeoutex) {
			LOGGER.error(timeoutex,
					correlationId);
		}
	}
}
