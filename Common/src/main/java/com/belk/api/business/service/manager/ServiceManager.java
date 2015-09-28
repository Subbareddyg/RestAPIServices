package com.belk.api.business.service.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.belk.api.business.service.contract.Services;

/**
 * The Service factory to return the reference of any Service implementation
 * class.
 * 
 * @author Mindtree
 * @date Oct 08, 2013
 */
@Service
public class ServiceManager {
	/**
	 * creating instance of Services.
	 */
	@Autowired
	private Services services;

	/**
	 * Default Constructor.
	 */
	public ServiceManager() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Method to return the specific service implementation instance.
	 * 
	 * @return specific service instance
	 */
	public final Services getService() {
		return this.services;
	}

}
