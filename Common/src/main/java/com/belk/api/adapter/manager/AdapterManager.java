package com.belk.api.adapter.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.belk.api.adapter.contract.Adapter;

/**
 * The adapter factory to return any adapter reference implemented by the
 * service layer. The type of adapter is managed here.
 * 
 * @author Mindtree
 * @date Sep 12, 2013
 */
@Service
public class AdapterManager {

	/**
	 * creating instance of Adapter.
	 */
	@Autowired
	private Adapter endecaAdapter;

	/**
	 * Default Constructor.
	 */
	public AdapterManager() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Method to return the specific adapter instance as per the requested type.
	 * 
	 * @return specific adapter instance
	 */
	public final Adapter getAdapter() {
		return this.endecaAdapter;
	}

}
