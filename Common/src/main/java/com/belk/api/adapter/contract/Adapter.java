package com.belk.api.adapter.contract;

import java.util.List;
import java.util.Map;

import com.belk.api.exception.AdapterException;

/** This interface acts as a contractor which provides a
 *  generic method to implement in the service layer .
 * @author Mindtree
 * @date Sep 12, 2013
 */
public interface Adapter {
	/** The service method which all adapter implementation classes should implement.
	 * This is the generic method to be called  from the service layer and the
	 * implementation class would decide the service logic .
	 * @param request The map containing the search request params as key-value pairs
	 * @param optionNodes the list of option nodes
	 * @param correlationId to track the request  
	 * @return Results in a list of maps.
	 * @throws AdapterException The exception thrown from the Adapter
	 */
	List<Map<String, String>> service(Map<String, String> request, Map<String, List<String>> optionNodes, final String correlationId)
			throws AdapterException;

}
