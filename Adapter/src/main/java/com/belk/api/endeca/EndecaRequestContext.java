package com.belk.api.endeca;

import java.util.List;
import java.util.Map;

/**This is the context class for the Endeca request.
 * @author Rahul Gopinath
 *
 */
public class EndecaRequestContext {
	
	/** Map used for constructing the endeca query **/
	private Map<String, String> endecaRequestMap;
	
	/** Map used to restrict the endeca results **/
	private Map<String, List<String>> optionNodes;
	
	/** Map used for storing params that come as part of uri request, but not all to be passed to endeca**/
	private Map<String, String> uriRequestMap; 
	
	/**
	 * @return the endeca request map
	 */
	public final Map<String, String> getEndecaRequestMap() {
		return this.endecaRequestMap;
	}
	/**
	 * @param endecaRequestMap to set
	 */
	public final void setEndecaRequestMap(final Map<String, String> endecaRequestMap) {
		this.endecaRequestMap = endecaRequestMap;
	}
	/**
	 * @return a list of option nodes
	 */
	public final Map<String, List<String>> getOptionNodes() {
		return this.optionNodes;
	}
	/**
	 * @param optionNodes to set
	 */
	public final void setOptionNodes(final Map<String, List<String>> optionNodes) {
		this.optionNodes = optionNodes;
	}
	/**
	 * @return the uriRequestMap
	 */
	public final Map<String, String> getUriRequestMap() {
		return this.uriRequestMap;
	}
	/**
	 * @param uriRequestMap the uriRequestMap to set
	 */
	public final void setUriRequestMap(final Map<String, String> uriRequestMap) {
		this.uriRequestMap = uriRequestMap;
	}

	
}
