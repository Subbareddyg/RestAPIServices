package com.belk.configui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.belk.api.constants.CommonConstants;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;
import com.belk.configui.constants.ConfigUIConstants;

/**
 * @author Mindtree
 * @date Mar 25, 2014
 */
@Controller
public class LoginController {

	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger(CommonConstants.CONFIGURATION_WEB_APP_LOGGER);
	{
		LoggerUtil.setLogger(LOGGER);
	}

	/**
	 * Method to return the login page as the first page.
	 * 
	 * @param model
	 *            The Model object
	 * @return Name of the view to be displayed
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public final String login(final ModelMap model) {
		return ConfigUIConstants.LOGIN;

	}

	/**
	 * Method to return the login page in case the login fails.
	 * 
	 * @param model
	 *            The Model object
	 * @return Name of the view to be displayed
	 */
	@RequestMapping(value = "/loginfailed", method = RequestMethod.GET)
	public final String loginerror(final ModelMap model) {
		model.addAttribute("error", "true");
		return ConfigUIConstants.LOGIN;

	}

	/**
	 * Method to return the login page after user logs out.
	 * 
	 * @param model
	 *            The Model object
	 * @return Name of the view to be displayed
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public final String logout(final ModelMap model) {
		return ConfigUIConstants.LOGIN;

	}

}
