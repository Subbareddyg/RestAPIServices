package com.belk.api.business.service.manager;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.belk.api.business.service.contract.Services;

/**
 * Unit Testing related to ServiceManager class is performed. <br />
 * {@link TestServiceManager} class is written for testing methods in
 * {@link ServiceManager} The unit test cases evaluates the way the methods
 * behave for the inputs given.
 * 
 * <br />
 * The test cases associated with ServiceManager is written to make the code
 * intact by assuring the code test.
 * 
 * @author Mindtree
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ServiceManager.class)
public class TestServiceManager {

	private ServiceManager serviceManager;
	private Services services;

	/**
	 * Initial set up ahead to start unit test case for class
	 */
	@Before
	public final void setup() {
		this.serviceManager = new ServiceManager();
	}

	/**
	 * Test method for
	 * {@link com.belk.api.business.service.manager.ServiceManager#getService()}
	 * . Testing is done to check whether the Service object is obtained
	 * correctly when getService() method is called .
	 */
	@Test
	public final void testGetService() {
		final Services expectedResult = this.serviceManager.getService();
		final Services actualResults = this.services;
		assertEquals(expectedResult, actualResults);
	}

}
