/**
 * 
 */
package com.belk.api.dao.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import com.belk.api.constants.CommonConstants;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.util.ProductDataLoader;


/**
 * @author AFURXG9
 * CoremetricsRowMapper will retrieve the resultset and create a map 
 * that has the target product id mapped to its list of 
 * recommendation product ids.
 * 
 *  This is part of coremetricsJob execcution
 *
 */
public class CoremetricsRowMapper<T> implements RowMapper<T> {
	
	/**
	 * Creating logger instance.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	@Autowired
	private ProductDataLoader productDataLoader;

	@SuppressWarnings("unchecked")
	@Override
	public final T mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		final long startTime = LOGGER.logMethodEntry("Admin Coremetrics");
		final List<String> recommendedList = new LinkedList<String>();
		final Map<String, LinkedList<String>> resultMap = new HashMap<String, LinkedList<String>>();
		final String targetId = rs.getString(CommonConstants.TARGET_PRODUCT_ID);
		final int limit = Integer.parseInt(this.productDataLoader.getProductDataPropertiesMap()
				.get(CommonConstants.MAX_RECOMMENDATION_FETCH));
		if (targetId != null) {
			for (int i = 1; i <= limit; i++) {
				// Check Null and Null String condition
				if (rs.getString(CommonConstants.RECOMMENDATION 
						+ CommonConstants.UNDERSCORE 
						+ i) != null && !rs.getString(CommonConstants.RECOMMENDATION 
								+ CommonConstants.UNDERSCORE 
								+ i).trim().equalsIgnoreCase(CommonConstants.NULL_STRING)) {
					// Insert into recommendation List
					recommendedList.add(rs.getString(CommonConstants.RECOMMENDATION 
							+ CommonConstants.UNDERSCORE 
							+ i).trim());			
				}
			}
			resultMap.put(targetId, (LinkedList<String>) recommendedList);
		}
		LOGGER.logMethodExit(startTime, "Admin Coremetrics");
		return (T) resultMap;
	}
}
