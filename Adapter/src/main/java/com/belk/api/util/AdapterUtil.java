package com.belk.api.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.belk.api.exception.AdapterException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;

/**
 * This class will be for the utility purpose specific to the Adapter.
 * Note: As part of Standardization of Adapter Response CR this class is written.
 * 
 * @author Mindtree
 * @date Mar 17, 2014
 */
@Service
public class AdapterUtil {
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	/**
	 * Method to populate the map which would have the specific connected
	 * adapter keys and the generic values
	 * 
	 * 
	 * @param endecaFieldListPropertiesMap
	 *            the map which is loaded from the endecaFieldList properties
	 * @param correlationId
	 *            tracking id
	 * @return Map<String, String> which would have the generic fields as key
	 *         and adapter specific as values
	 * @throws AdapterException
	 *             exception thrown if any in this layer
	 */
	public final Map<String, String> populateResponseFields(
			final Map<String, String> endecaFieldListPropertiesMap,
			final String correlationId) throws AdapterException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final Map<String, String> fieldProperties = new HashMap<String, String>();
		// The following steps populates the fieldProperties Map with the generic keys and endeca keys as value
		for (final Object key : endecaFieldListPropertiesMap.keySet()) {
			final String keyString = key.toString();
			fieldProperties.put(endecaFieldListPropertiesMap.get(keyString),
					keyString);
		}

		LOGGER.logMethodExit(startTime, correlationId);
		return fieldProperties;
	}

	/**
	 * Method to map the keys from specific source response map to generic keys
	 * 
	 * @param adapterResponseMap
	 *            the map which has the keys specific to the adapter.
	 * @param responseFields
	 *            the map would contain the key as generic response keys and
	 *            adapter specific keys as values
	 * 
	 * @param correlationId
	 *            tracking id
	 * @return Map which would have the key as generic key and the value as the
	 *         response obtained from the adapter layer
	 * @throws AdapterException
	 *             exception thrown if any in this layer
	 */

	public static final Map<String, String> mapResponseToGenericKeys(
			final Map<String, String> adapterResponseMap,
			final Map<String, String> responseFields, final String correlationId)
					throws AdapterException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final Map<String, String> genericAdapterResponseMap = new LinkedHashMap<String, String>();
		// The following code is to replace the endeca keys with the  generic keys populated in the adapterResponseMap
		for (final Map.Entry<String, String> entry : adapterResponseMap
				.entrySet()) {
			final String key = entry.getKey();
			genericAdapterResponseMap.put(responseFields.get(key),
					entry.getValue());
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return genericAdapterResponseMap;
	}
}
