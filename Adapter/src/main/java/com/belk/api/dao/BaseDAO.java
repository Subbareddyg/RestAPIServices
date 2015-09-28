/**
 * This class would initiate registering with the Driver - 
 * oracle.jdbc.driver.OracleDriver.
 * Also the class would establish the datasource and jdbcTemplate
 * further required for querying through the driver.  
 * 
 */
package com.belk.api.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.util.DBConnectionLoader;
import com.belk.api.util.DataSourceGenerator;


/**
 * Base DAO is extension over NamedParameterJdbcTemplate's methods
 * @author AFURXG9
 *
 */

public class BaseDAO extends NamedParameterJdbcTemplate {

	/**
	 * get the logger instance.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	/**
	 * DB Connection Loader Class to fetch connection properties
	 */
	@Autowired
	DBConnectionLoader connectionLoader;
	
	
	/**
	 * Wiring datasource to constructor, this places a datasource to underlying
	 * NamedParameterJdbcTemplate
	 * 
	 * @param dataSourceGenerator
	 */
	

	public BaseDAO(final DataSourceGenerator dataSourceGenerator) {
		super(dataSourceGenerator.getBmDataSource());
		LOGGER.info(dataSourceGenerator.getBmDataSource().toString(), "On load Wire datasource");
	}

}