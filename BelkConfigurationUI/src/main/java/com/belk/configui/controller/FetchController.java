package com.belk.configui.controller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.belk.api.constants.CommonConstants;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.belk.configui.constants.ConfigUIConstants;
import com.belk.configui.utils.ConfigurationUtil;

/**
 * @author Mindtree
 * @date Mar 24, 2014 This is the controller that contains methods to fetch list
 *       of folders, list of files, list of properties
 * 
 */
@Controller
public class FetchController {

	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger(CommonConstants.CONFIGURATION_WEB_APP_LOGGER);
	{
		LoggerUtil.setLogger(LOGGER);
	}

	String configFilePath = ConfigurationUtil.getConfigurationFilePath();

	/*
	 * Commented out as validation framework related changes are not required as
	 * of now
	 */
	/*
	 * String validationConfigFilePath = FilePathFinder.getValidationFilePath();
	 * 
	 * @Autowired(required=true) UpdateValidationRules validationRules;
	 * 
	 * public void setValidationRules(UpdateValidationRules validationRules) {
	 * this.validationRules = validationRules; }
	 */

	/**
	 * This request mapping displays list of Folder names present in a given
	 * location
	 * 
	 * @param principal
	 *            The security object containing the logged in user's
	 *            information.
	 * @param httprequest
	 *            The HTTP request object
	 * @return model
	 */
	@RequestMapping(value = ConfigUIConstants.GET_MODULE_LIST, method = RequestMethod.GET)
	public final ModelAndView displayModules(final Principal principal,
			final HttpServletRequest httprequest) {
		final String correlationId = ConfigUIConstants.BLANK;

		final String userName = principal.getName();

		LOGGER.info("Logged in user: " + userName, correlationId);
		final ModelAndView model = new ModelAndView(
				ConfigUIConstants.MODULE_LIST);
		List<String> listFolder = null;
		List<String> logLevelList = null;
		try {
			listFolder = this.getModuleList(this.configFilePath);
			/*
			 * Commented out as validation framework related changes are not
			 * required as of now
			 */
			// listFolder.add(getDirectoryName(validationConfigFilePath));
		} catch (NullPointerException error) {
			LOGGER.error(error, correlationId);
			model.addObject("error", "Check file path!!");

		}

		model.addObject(ConfigUIConstants.LOGGED_IN_USER_NAME, userName);
		model.addObject(ConfigUIConstants.MODULE_LIST, listFolder);

		logLevelList = this.getLogLevelList();
		model.addObject(ConfigUIConstants.LOG_LEVEL_LIST, logLevelList);
		return model;

	}

	/**
	 * This request mapping displays list of configuration file names present in
	 * a given folder
	 * 
	 * @param principal
	 *            The security object containing the logged in user's
	 *            information.
	 * @param httprequest
	 *            The HTTP request object
	 * @return modelView
	 */
	@RequestMapping(value = ConfigUIConstants.GET_FILE_LIST, method = RequestMethod.GET)
	public final ModelAndView displayFiles(final Principal principal,
			final HttpServletRequest httprequest) {
		final String correlationId = ConfigUIConstants.BLANK;
		String rootLocation = null;
		ModelAndView model = null;
		List<String> fileList = null;
		List<String> logLevelList = null;
		if (null == principal) {
			model = new ModelAndView(ConfigUIConstants.LOGIN);
		} else {
			final String userName = principal.getName();
			final HttpSession session = httprequest.getSession();
			session.setAttribute(ConfigUIConstants.LOGGED_IN_USER_NAME, userName);
			LOGGER.info("Logged in user: " + userName, correlationId);


			/*
			 * Commented out as validation framework related changes are not
			 * required as of now
			 */
			/*
			 * if(moduleName.equalsIgnoreCase(ConfigUIConstants.VALIDATION)) {
			 * rootLocation = validationConfigFilePath; modelView = new
			 * ModelAndView(ConfigUIConstants.VALIDATION_FILE_LIST); } else {
			 */
			rootLocation = this.configFilePath + ConfigUIConstants.SLASH;
			/* + moduleName; */
			model = new ModelAndView(ConfigUIConstants.FILE_LIST);
			// }

			try {
				fileList = this.getFileList(rootLocation);
			} catch (NullPointerException error) {
				LOGGER.error(error, correlationId);
				model.addObject("error", "Check file path!!");

			}

			logLevelList = this.getLogLevelList();
			model.addObject(ConfigUIConstants.LOG_LEVEL_LIST, logLevelList);
			/* model.addObject(ConfigUIConstants.MODULE_NAME, moduleName); */
			model.addObject(ConfigUIConstants.LOGGED_IN_USER_NAME, userName);
			model.addObject(ConfigUIConstants.FILE_LIST, fileList);
			model.addObject(ConfigUIConstants.LOCATION, rootLocation);
		}
		return model;
	}

	/**
	 * @param httprequest
	 *            The HTTP request object.
	 * @param model
	 *            The Model object
	 * @return propDetails - JSP file name
	 * @throws IOException
	 *             The exception thrown from this method if any file read/write
	 *             operation fails.
	 */
	@RequestMapping(value = ConfigUIConstants.GET_PROP, method = RequestMethod.POST)
	public final String displayProperties(final HttpServletRequest httprequest,
			final Model model) throws IOException {
		final String correlationId = ConfigUIConstants.BLANK;
		final String filePath = httprequest
				.getParameter(ConfigUIConstants.LOCATION);
		final Properties properties = new Properties();
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(filePath);
		} catch (NullPointerException error) {
			LOGGER.error(error, correlationId);
			model.addAttribute("error", "Check file path!!");

		}
		properties.load(fileReader);

		final Map<String, String> propertyList = new HashMap<String, String>();
		for (Object string : properties.keySet()) {
			propertyList.put((String) string,
					properties.getProperty((String) string));
		}

		model.addAttribute(ConfigUIConstants.FILE_PATH, filePath);
		model.addAttribute(ConfigUIConstants.PROP_LIST, propertyList);
		return ConfigUIConstants.PROP_DETAILS;
	}

	/*
	 * Commented out as validation framework related changes are not required as
	 * of now
	 */
	/**
	 * @param httprequest
	 * @param model
	 * @return propDetails - JSP file name
	 * @throws IOException
	 */
	/*
	 * @RequestMapping(value = ConfigUIConstants.GET_VALIDATION_PROP, method =
	 * RequestMethod.POST) public String
	 * displayValidationProperties(HttpServletRequest httprequest, Model model)
	 * throws IOException { String filePath =
	 * httprequest.getParameter(ConfigUIConstants.LOCATION); String content =
	 * validationRules.readFile(filePath);
	 * 
	 * model.addAttribute(ConfigUIConstants.FILE_PATH, filePath);
	 * model.addAttribute(ConfigUIConstants.CONTENT, content);
	 * 
	 * return ConfigUIConstants.VALIDATION_PROP_DETAILS; }
	 */

	/**
	 * List all the module sub-directories under a given directory
	 * 
	 * @param rootLocation
	 *            - The root location which has configuration modules
	 * 
	 * @return moduleNames
	 */
	private List<String> getModuleList(final String rootLocation) {
		final String correlationId = ConfigUIConstants.BLANK;
		File directory = null;
		try {
			directory = new File(rootLocation);
		} catch (NullPointerException error) {
			LOGGER.error(error, correlationId);
		}
		final File[] fileList = directory.listFiles();
		final List<String> moduleNames = new ArrayList<String>();

		// Logic to check the folder and add to list
		for (File file : fileList) {
			if (file.isDirectory()) {
				moduleNames.add(file.getName());
			}
		}
		return moduleNames;
	}

	/**
	 * List all the files in a given folder
	 * 
	 * @param moduleName
	 *            - The module name which has configuration files
	 * @return fileName
	 */
	private List<String> getFileList(final String moduleName) {
		final String correlationId = ConfigUIConstants.BLANK;
		File directory = null;
		try {
			directory = new File(moduleName);
		} catch (NullPointerException error) {
			LOGGER.error(error, correlationId);
		}

		final File[] fileList = directory.listFiles();
		final List<String> fileNames = new ArrayList<String>();
		// Logic to check the files and add to list
		for (File file : fileList) {
			if (file.isFile()) {
				fileNames.add(file.getName());
			}
		}
		return fileNames;
	}

	/**
	 * Method to return the list of log level values
	 * 
	 * @return List of log level values
	 */
	private List<String> getLogLevelList() {

		final List<String> logLevelList = new ArrayList<String>();
		logLevelList.add(ConfigUIConstants.LOG_FATAL);
		logLevelList.add(ConfigUIConstants.LOG_DEBUG);
		logLevelList.add(ConfigUIConstants.LOG_WARN);
		logLevelList.add(ConfigUIConstants.LOG_INFO);
		logLevelList.add(ConfigUIConstants.LOG_ERROR);
		return logLevelList;
	}

	/**
	 * Method to return the name of the directory for which the location has
	 * been passed. Update: This has been suppressed as Validation related
	 * changes are not required now.
	 * 
	 * @param rootLocation
	 *            Location of the directory
	 * @return Name of the directory
	 */
	@SuppressWarnings("unused")
	private String getDirectoryName(final String rootLocation) {
		final String correlationId = ConfigUIConstants.BLANK;
		final File directory = new File(rootLocation);
		String directoryName = null;
		if (directory.isDirectory()) {
			directoryName = directory.getName();
		}
		return directoryName;
	}

}
