/**
 * This class would handle any modifications that has to be done on 
 * the Coremetrics data such as fetch from DAO
 * 
 * Class not instantiated using annotation due to DAO Concerns. Read Dao comments 
 */
package com.belk.api.util;

import java.io.Serializable;
import java.util.List;


import com.belk.api.cache.CacheManager;
import com.belk.api.constants.CommonConstants;
import com.belk.api.dao.CoremetricsDAO;
import com.belk.api.exception.AdapterException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;

/**
 * @author AFURXG9
 *
 */

public class CoremetricsUtil {

	/**
	 * Creating logger instance.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	/**
	 * instance for coremetrics DAO
	 */

	private CoremetricsDAO coremetricsDao;
	
	/**
	 * instance for CacheManager
	 */

	private CacheManager cacheManager;

	/**
	 * instance for CommonUtil
	 */
	private CommonUtil commonUtil;

	/**
	 * This method queries the DAO Layer and stores the results in Cache
	 * @param correlationId to track the request
	 * @throws AdapterException the exception thrown from this layer
	 */
	public final void getCoremetricsForPdp(final String correlationId) throws AdapterException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		/** Fetch data from DAO layer **/
		this.coremetricsDao.getRecommendationsForPdp(correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * CoremetricsCacheHolder Class is served to cache
	 * @author AFURXG9
	 *
	 */
	public static final class CoremetricsCacheHolder implements Serializable {
		private static final long serialVersionUID = 1L;

		private List<String> recommendedProducts = null;
		/**
		 * @return the recommendedProducts
		 */
		public List<String> getRecommendedProducts() {
			return this.recommendedProducts;
		}
		/**
		 * @param recommendedProducts the recommendedProducts to set
		 */
		public void setRecommendedProducts(final List<String> recommendedProducts) {
			this.recommendedProducts = recommendedProducts;
		}
	}

	/**
	 * CoremetricsCacheKeys Class is served to cache
	 * @author AFURXG9
	 *
	 */
	public static final class CoremetricsCacheKeys implements Serializable {
		private static final long serialVersionUID = 1L;

		private List<String> cacheKeys;

		/**
		 * @return the cacheKeys
		 */
		public List<String> getCacheKeys() {
			return this.cacheKeys;
		}

		/**
		 * @param cacheKeys the cacheKeys to set
		 */
		public void setCacheKeys(final List<String> cacheKeys) {
			this.cacheKeys = cacheKeys;
		}
	}

	/**
	 * @return the coremetricsDao
	 */
	public final CoremetricsDAO getCoremetricsDao() {
		return this.coremetricsDao;
	}

	/**
	 * @param coremetricsDao the coremetricsDao to set
	 */
	public final void setCoremetricsDao(final CoremetricsDAO coremetricsDao) {
		this.coremetricsDao = coremetricsDao;
	}

	/**
	 * This method queries the cache for a particular target product and returns
	 * the list of recommended products for the same.
	 * 
	 * @param parentProductId
	 *            the target product id
	 * @param target
	 *            target type received from request
	 * @param correlationId
	 *            to track the request
	 * @return list of recommended products
	 */
	public final List<String> getRecommendedProductIDList(final String parentProductId,
			final String target, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		CoremetricsCacheHolder cacheHolder = new CoremetricsCacheHolder();
		List<String> recommendedProducts = null;
		String targetString = null;
		if (target.equalsIgnoreCase(CommonConstants.PRODUCT)) {
			targetString = CommonConstants.PRODUCT;
		} else if (target.equalsIgnoreCase(CommonConstants.CART)) {
			targetString = CommonConstants.CART;
		}
		if (this.commonUtil.isNotEmpty(parentProductId, correlationId)) {
			cacheHolder = this.cacheManager.get(parentProductId
					+ CommonConstants.UNDERSCORE + targetString,
					CoremetricsCacheHolder.class, correlationId);
			if (cacheHolder != null) {
				recommendedProducts = cacheHolder.getRecommendedProducts();
				LOGGER.debug(
						"Recommended products size: " + recommendedProducts.size(),
						correlationId);
			}
		}
		
		LOGGER.logMethodExit(startTime, correlationId);
		return recommendedProducts;
	}

	/**
	 * @param cacheManager the cacheManager to set
	 */
	public final void setCacheManager(final CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	/**
	 * @param commonUtil the commonUtil to set
	 */
	public final void setCommonUtil(final CommonUtil commonUtil) {
		this.commonUtil = commonUtil;
	}

}
