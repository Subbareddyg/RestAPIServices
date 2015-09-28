package com.belk.api.adapter.manager;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import com.belk.api.adapter.contract.Adapter;

/**
 * 
 * Unit Testing related to AdapterManager class is performed. <br />
 * {@link TestAdapterManager} class is written for testing methods in {@link AdapterManager}
 *  The unit test cases evaluates the way the methods behave for the inputs given.
 *  
 *  <br />The test cases associated with AdapterManager is written to make the code intact
 *  by assuring the code test. 
 * @author Mindtree
 *
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(AdapterManager.class)
public class TestAdapterManager {
    
	private AdapterManager adapterManager;
	private Adapter adapter;
	
	/**
	 * Initial set up ahead to start unit test case for class
	 */
	@Before
	public final void setup() {
		 this.adapterManager = new AdapterManager();
	}
	

	/**
	 * Test method for {@link com.belk.api.adapter.manager.AdapterManager#getAdapter()}.
	 * Testing is done to check whether the Adapter object is obtained correctly 
	 * when getAdapter() method is called .
	 */
	@Test
	public final void testGetAdapter() {
		final Adapter expectedResult = this.adapterManager.getAdapter();
		final Adapter actualResults = this.adapter;
		assertEquals(expectedResult, actualResults);
	}
	

}
