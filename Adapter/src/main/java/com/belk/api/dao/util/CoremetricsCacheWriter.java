/**
 * 
 */
package com.belk.api.dao.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.belk.api.cache.CacheManager;
import com.belk.api.constants.CommonConstants;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.util.CoremetricsUtil;
import com.belk.api.util.CoremetricsUtil.CoremetricsCacheHolder;

/**
 * @author AFURXG9
 *
 * CoremetricsCacheWriter will read the type <T> and load the results to cache.
 */
public class CoremetricsCacheWriter<T> implements ItemWriter<T> {

	/**
	 * Creating logger instance.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();
	/**
	 * Instance of Cache Manager
	 */
	@Autowired
	private CacheManager cacheManager;

	/**
	 * Overridden method to write using Item reader 
	 * This method will read from the modified resultset and insert elements to cache 
	 * @param items
	 * @throws Exception
	 * 
	 */	
	@Override
	public final void write(final List<? extends T> items) throws Exception {
		final long startTime = LOGGER.logMethodEntry("Admin Coremetrics");
		LOGGER.debug("Max Rows Processing in current iteration", String.valueOf(items.size()));
		final List<Map<String, LinkedList<String>>> results =  (List<Map<String, LinkedList<String>>>) items;
		for (Map<String, LinkedList<String>> relation: results) {
			if (relation.size() > 0) {
				final Set<String> keys = relation.keySet();
				final String targetId = keys.iterator().next();
				final CoremetricsUtil.CoremetricsCacheHolder cacheHolder = new CoremetricsCacheHolder();
				cacheHolder.setRecommendedProducts(relation.get(targetId));
				this.cacheManager.set(targetId 
						+ CommonConstants.UNDERSCORE 
						+ CommonConstants.PRODUCT, cacheHolder, "Admin Coremetrics");
			}
		}
		LOGGER.logMethodExit(startTime, "Admin Coremetrics");
	}

	/**
	 * @return the cacheManager
	 */
	public final CacheManager getCacheManager() {
		return this.cacheManager;
	}

	/**
	 * @param cacheManager the cacheManager to set
	 */
	public final void setCacheManager(final CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
}
