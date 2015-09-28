/** This is the implementation class for CoremetricsDAO
 * 
 */
package com.belk.api.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.belk.api.constants.DAOConstants;
import com.belk.api.constants.ErrorConstants;
import com.belk.api.constants.QueryConstants;
import com.belk.api.dao.BaseDAO;
import com.belk.api.dao.CoremetricsDAO;
import com.belk.api.exception.AdapterException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.mail.MailClient;
import com.belk.api.util.ErrorLoader;

/**
 * @author AFURXG9
 *
 */

public class CoremetricsDAOImpl implements CoremetricsDAO {

	/**
	 * Creating logger instance.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	/**
	 * instance of Base DAO 
	 */
	private BaseDAO baseDao;

	/**
	 * creating instance of Error Loader class.
	 */
	@Autowired
	private ErrorLoader errorLoader;

	/**
	 * instance of Job Launcher
	 */
	@Autowired
	private JobLauncher jobLauncher;

	/**
	 * instance of Job
	 */
	@Autowired
	private Job coremetricsJob;
	
	/**
	 * instance of Mail Client
	 */
	@Autowired
	private MailClient mailClient;

	/**
	 * @see com.belk.api.dao.CoremetricsDAO#getRecommendationsForPdp(java.lang.String)
	 */
	public final void getRecommendationsForPdp(final String correlationId) throws AdapterException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final Map<String, String> errorPropertiesMap = this.errorLoader.getErrorPropertiesMap();
		try {
			 /** Adding Date as param, so that the Job runs with new tags everytime.
			 Else will create JobInstanceAlreadyCompleteException **/	
			 final JobParameters param =  new JobParametersBuilder()
			 .addLong("Date", System.currentTimeMillis()).toJobParameters();
			 // Calling job execution. Job details defined in admin-batch.xml 
			 final JobExecution execution = (JobExecution) this.jobLauncher.run(this.coremetricsJob, param);
			 if (execution.getStatus().isUnsuccessful()) {
				 this.mailClient.sendCoremetricsMail();
				 LOGGER.debug("Admin API Coremetrics Job Failed", correlationId);
				 throw new AdapterException(
						 String.valueOf(ErrorConstants.ERROR_CODE_11523),
						 errorPropertiesMap.get(String.valueOf(ErrorConstants.ERROR_CODE_11523)),
						 ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
						 correlationId);
			 }
		} catch (JobExecutionAlreadyRunningException exception) {
			this.mailClient.sendCoremetricsMail();
			LOGGER.error(exception, correlationId);
			throw new AdapterException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String.valueOf(ErrorConstants.ERROR_CODE_11523)),
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);
		} catch (JobRestartException exception) {
			this.mailClient.sendCoremetricsMail();
			LOGGER.error(exception, correlationId);
			throw new AdapterException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String.valueOf(ErrorConstants.ERROR_CODE_11523)),
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);
		} catch (JobInstanceAlreadyCompleteException exception) {
			this.mailClient.sendCoremetricsMail();
			LOGGER.error(exception, correlationId);
			throw new AdapterException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String.valueOf(ErrorConstants.ERROR_CODE_11523)),
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);
		} catch (JobParametersInvalidException exception) {
			this.mailClient.sendCoremetricsMail();
			LOGGER.error(exception, correlationId);
			throw new AdapterException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String.valueOf(ErrorConstants.ERROR_CODE_11523)),
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * @see com.belk.api.dao.CoremetricsDAO#getProductRecommendationForPdp(java.lang.String, java.lang.String)
	 */
	public final List<Map<String, Object>> getProductRecommendationForPdp(final String productId, final String correlationId) 
			throws AdapterException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put(DAOConstants.PRODUCT_ID, productId);
		final Map<String, String> errorPropertiesMap = this.errorLoader.getErrorPropertiesMap();
		List<Map<String, Object>> resultList = null;
		try {
			resultList = this.baseDao.queryForList(QueryConstants.GET_RECOMMENDATIONS_FOR_PRODUCT, queryParams);
		} catch (DataAccessException exception) {
			LOGGER.error(exception, correlationId);
			throw new AdapterException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String.valueOf(ErrorConstants.ERROR_CODE_11523)),
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);
		} 
		LOGGER.logMethodExit(startTime, correlationId);
		return resultList;
	}

	/**
	 * @param baseDao the baseDao to set
	 */
	public final void setBaseDao(final BaseDAO baseDao) {
		this.baseDao = baseDao;
	}

}
