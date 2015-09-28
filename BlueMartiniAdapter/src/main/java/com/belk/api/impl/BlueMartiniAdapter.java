package com.belk.api.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.belk.api.adapter.constants.BlueMartiniAdapterConstants;
import com.belk.api.adapter.constants.KeyMapperConstants;
import com.belk.api.adapter.constants.ValueMapperConstants;
import com.belk.api.adapter.contract.Adapter;
import com.belk.api.blueMartini.HttpClientUtil;
import com.belk.api.constants.CommonConstants;
import com.belk.api.constants.ErrorConstants;
import com.belk.api.exception.AdapterException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.util.ErrorLoader;

/**
 * This is an implementation of the Adapter interface for BlueMartini . This
 * processes the input request parameters to BlueMartini specific parameters and
 * invokes the proper methods from the HttpClientUtil for the response.
 * 
 * Update: This class has been updated to fix the review comments and to add
 * additional field(ATR_product_description, ATR_venstyle_number,Copy Line Text
 * 1,Copy Line Text 2 ,Copy Line Text 3,Copy Line Text 4 and Copy Line Text 5),
 * as part of CR: April 2014
 * 
 * Updated : Added few comments and changed the name of variables as part of Phase2, April,14 release
 * @author Mindtree
 * @date Nov 10th, 2013
 */
@Service
public class BlueMartiniAdapter implements Adapter {

	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	/**
	 * creating instance of HttpClientUtil.
	 */
	@Autowired
	HttpClientUtil httpClientUtil;

	/**
	 * creating instance of Error Loader class.
	 */
	@Autowired
	ErrorLoader errorLoader;
	
	/**
	 * @param httpClientUtil
	 *            objects
	 */
	public final void setHttpClientUtil(final HttpClientUtil httpClientUtil) {
		this.httpClientUtil = httpClientUtil;
	}

	/**
	 * setter method for the errorLoader.
	 * 
	 * @param errorLoader
	 *            the errorLoader to set
	 */
	public final void setErrorLoader(final ErrorLoader errorLoader) {
		this.errorLoader = errorLoader;
	}
	
	/**
	 * The service method that contact blue martini, which all adapter
	 * implementation classes should implement.This is the generic method to be
	 * called from the service layer and the implementation class would decide
	 * the logic.
	 * 
	 * @param request
	 *            The map containing the search request parameters as key-value
	 *            pairs
	 * @param optionNodes the list of option nodes
	 * @param correlationId
	 *            correlationId
	 * @return JSONObject BlueMartini result
	 * @throws AdapterException
	 *             The exception thrown from the Adapter Layer if it occurs
	 */

	@Override
	public final List<Map<String, String>> service(
			final Map<String, String> request, final Map<String, List<String>> optionNodes, final String correlationId)
			throws AdapterException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("RequestMap :" + request, correlationId);

		JSONObject blueMartiniResponse;
		// Calling the httpClientUtil to get the BlueMartini Response
		blueMartiniResponse = this.httpClientUtil.getHttpContent(
				BlueMartiniAdapterConstants.PATTERN_DETAILS, request,
				correlationId);
		LOGGER.info("Blue martini response : " + blueMartiniResponse,
				correlationId);
		List<Map<String, String>> blueMartiniResult;
		// Fetching the selective fields from the BlueMarting and placing them
		// in a Map
		blueMartiniResult = this.blueMartiniResultsTOListofMap(blueMartiniResponse,
				correlationId);
		LOGGER.debug("BlueMartiniResult :" + blueMartiniResult, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return blueMartiniResult;

	}

	/**
	 * The method to iterate through the complete response coming from
	 * BlueMartini and populate the same to a list of maps.
	 * 
	 * @param bluemartiniResponse
	 *            The json object obtained from BlueMartini
	 * @param correlationId
	 *            correlation id
	 * @return blueMartiniResultList The processed list of map of values coming
	 *         from BlueMartini
	 * @throws AdapterException
	 *             The exception thrown from the Adapter Layer if it occurs
	 */

	public final List<Map<String, String>> blueMartiniResultsTOListofMap(
			final JSONObject bluemartiniResponse, final String correlationId)
			throws AdapterException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("BluemartiniResponse :" + bluemartiniResponse,
				correlationId);
		Map<String, String> blueMartiniMap;
		List<Map<String, String>> blueMartiniResultList = null;
		final Map<String, String> errorPropertiesMap = this.errorLoader.getErrorPropertiesMap();
		try {

			if (bluemartiniResponse != null
					&& bluemartiniResponse.getJSONArray(
							ValueMapperConstants.PRODUCTS).length() > 0) {

				// Get Json array from the blue martini result
				JSONArray productsArray;
				productsArray = bluemartiniResponse
						.getJSONArray(ValueMapperConstants.PRODUCTS);

				blueMartiniResultList = new ArrayList<Map<String, String>>();
				for (int i = 0; i < productsArray.length(); i = i + 1) {
					// Get json object for the array
					final JSONObject blueMartiniResponseObject = productsArray
							.getJSONObject(i);
					// Populate the SKU Array details from blue martini response
					// and getting json object and String
					if (blueMartiniResponseObject.getJSONArray(
							ValueMapperConstants.SKUS).length() > 0) {
						for (int index = 0; index < blueMartiniResponseObject
								.getJSONArray(ValueMapperConstants.SKUS)
								.length(); index = index + 1) {
							blueMartiniMap = new HashMap<String, String>();

							// Added this method to avoid code complexity
							this.populateProductDetails(correlationId,
									blueMartiniMap, blueMartiniResultList,
									blueMartiniResponseObject, index);

						}

					}

				}
			}

		} catch (JSONException e) {
			LOGGER.error(e, correlationId);
			throw new AdapterException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String.valueOf(ErrorConstants.ERROR_CODE_11523)),
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);
		}

		LOGGER.debug("BluemartiniResultList :" + blueMartiniResultList,
				correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return blueMartiniResultList;

	}

	/**
	 * Method to populate all the product related details
	 * 
	 * @param correlationId to track the request
	 * @param blueMartiniMap map contains the result
	 * @param blueMartiniResultList list of blue martini result
	 * @param blueMartiniResponseObject is response object from blue martini
	 * @param index for count
	 * @throws JSONException Exception thrown from BlueMartiniAdapter layer
	 */
	private void populateProductDetails(final String correlationId,
			final Map<String, String> blueMartiniMap,
			final List<Map<String, String>> blueMartiniResultList,
			final JSONObject blueMartiniResponseObject, final int index)
			throws JSONException {
		this.departmentDetails(blueMartiniResponseObject, blueMartiniMap,
				correlationId);

		// Parse class number and put in map
		this.classNumberDetails(blueMartiniResponseObject, blueMartiniMap,
				correlationId);

		// Parse list price and put in map
		this.listPrice(blueMartiniResponseObject, blueMartiniMap, correlationId);

		// Parse sale price and put in map
		this.salePrice(blueMartiniResponseObject, blueMartiniMap, correlationId);

		this.productAvailable(blueMartiniResponseObject, blueMartiniMap,
				correlationId);

		// Parse clearance flag and put in map
		this.productClearance(blueMartiniResponseObject, blueMartiniMap,
				correlationId);

		// Parse new arrival flag and put in map
		this.productArrival(blueMartiniResponseObject, blueMartiniMap, correlationId);

		// Parse bridal eligible flag and put in map
		this.bridalStatus(blueMartiniResponseObject, blueMartiniMap, correlationId);

		this.populateSku(blueMartiniResponseObject, blueMartiniMap, index,
				correlationId);

		// Parse inventory available and put in map
		this.populateSkuInventory(blueMartiniResponseObject, blueMartiniMap, index,
				correlationId);

		// Parse list price and put in map
		this.populatePrice(blueMartiniResponseObject, blueMartiniMap, correlationId);

		// Parse image url and put in map
		this.populateImageUrl(blueMartiniResponseObject, blueMartiniMap, index,
				correlationId);

		this.populateParentProduct(blueMartiniResponseObject, blueMartiniMap,
				correlationId);

		// parse short and long description of the parent
		// product
		this.populateDescription(blueMartiniResponseObject, blueMartiniMap,
				correlationId);

		// Parse is pattern product flag and put in map
		this.populatePatternProduct(blueMartiniResponseObject, blueMartiniMap,
				correlationId);
		blueMartiniResultList.add(blueMartiniMap);
	}

	/**
	 * Method will obtain the availability of product in store, online and in
	 * web from the response and place it into map.
	 * 
	 * @param blueMartiniResponseObject
	 *            it is the response object
	 * @param blueMartiniMap
	 *            Department information are placed in a map.
	 * @param correlationId
	 *            it tracks the request
	 * @throws JSONException
	 *             Exception thrown from BlueMartiniAdapter layer
	 */
	private void productAvailable(final JSONObject blueMartiniResponseObject,
			final Map<String, String> blueMartiniMap, final String correlationId)
			throws JSONException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);
		LOGGER.debug("BlueMartiniResponseObject :" + blueMartiniResponseObject,
				correlationId);
		// Parse available in store flag and put in map
		if (blueMartiniResponseObject.getJSONArray(ValueMapperConstants.SKUS)
				.getJSONObject(0).has(ValueMapperConstants.WEB_STORE_SALE)) {
			if (blueMartiniResponseObject
					.getJSONArray(ValueMapperConstants.SKUS).getJSONObject(0)
					.getString(ValueMapperConstants.WEB_STORE_SALE) != null) {
				blueMartiniMap
						.put(KeyMapperConstants.P_AVAILABLE_IN_STORE,
								blueMartiniResponseObject
										.getJSONArray(ValueMapperConstants.SKUS)
										.getJSONObject(0)
										.getString(
												ValueMapperConstants.WEB_STORE_SALE));
			}
		}
		// Parse available online flag and put in map
		if (blueMartiniResponseObject.getJSONObject(
				ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS).has(
				ValueMapperConstants.IS_OUT_OF_STOCK_ONLINE)) {
			if (blueMartiniResponseObject.getJSONObject(
					ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS).getBoolean(
					ValueMapperConstants.IS_OUT_OF_STOCK_ONLINE) == true) {
				blueMartiniMap.put(KeyMapperConstants.P_AVAILABLE_ONLINE,
						BlueMartiniAdapterConstants.TRUE_VALUE);
			} else {
				blueMartiniMap.put(KeyMapperConstants.P_AVAILABLE_ONLINE,
						BlueMartiniAdapterConstants.FALSE_VALUE);
			}
		}
		// Parse web store sale flag and put in map
		if (blueMartiniResponseObject.getJSONObject(
				ValueMapperConstants.PARENT_PRODUCT).has(
				ValueMapperConstants.WEB_STORE_SALE)) {
			if (blueMartiniResponseObject.getJSONObject(
					ValueMapperConstants.PARENT_PRODUCT).getString(
					ValueMapperConstants.WEB_STORE_SALE) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_ON_SALE,
						blueMartiniResponseObject.getJSONObject(
								ValueMapperConstants.PARENT_PRODUCT).getString(
								ValueMapperConstants.WEB_STORE_SALE));
			}
		} else {
			blueMartiniMap.put(KeyMapperConstants.P_ON_SALE,
					BlueMartiniAdapterConstants.FALSE_VALUE);
		}
		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method will obtain the pattern product from the response and place it
	 * into map.
	 * 
	 * @param blueMartiniResponseObject
	 *            it is the response object
	 * @param blueMartiniMap
	 *            Department information are placed in a map.
	 * @param correlationId
	 *            it tracks the input request
	 * @throws JSONException
	 *             Exception thrown from BlueMartiniAdapter layer
	 */
	private void populatePatternProduct(
			final JSONObject blueMartiniResponseObject,
			final Map<String, String> blueMartiniMap, final String correlationId)
			throws JSONException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);
		LOGGER.debug("BlueMartiniResponseObject :" + blueMartiniResponseObject,
				correlationId);
		if (blueMartiniResponseObject.getJSONObject(
				ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS).has(
				ValueMapperConstants.IS_PATTERN_PRODUCT)) {
			if (blueMartiniResponseObject.getJSONObject(
					ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS).getBoolean(
					ValueMapperConstants.IS_PATTERN_PRODUCT) == true) {
				blueMartiniMap.put(KeyMapperConstants.P_IS_PATTERN, CommonConstants.FLAG_YES_VALUE);
			} else {
				blueMartiniMap.put(KeyMapperConstants.P_IS_PATTERN,
						BlueMartiniAdapterConstants.FALSE_VALUE);
			}

		}
		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method will obtain the short description and long description of parent
	 * product from the response and place it into map.
	 * 
	 * @param blueMartiniResponseObject
	 *            it is the response object
	 * @param blueMartiniMap
	 *            Department information are placed in a map.
	 * @param correlationId
	 *            it tracks input request
	 * @throws JSONException
	 *             Exception thrown from BlueMartiniAdapter layer
	 */
	private void populateDescription(
			final JSONObject blueMartiniResponseObject,
			final Map<String, String> blueMartiniMap, final String correlationId)
			throws JSONException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);
		LOGGER.debug("BlueMartiniResponseObject :" + blueMartiniResponseObject,
				correlationId);
		// Parse short description and put in map
		if (blueMartiniResponseObject.getJSONObject(
				ValueMapperConstants.PARENT_PRODUCT).has(
				ValueMapperConstants.SHORT_DESCRIPTION)) {
			if (blueMartiniResponseObject.getJSONObject(
					ValueMapperConstants.PARENT_PRODUCT).getString(
					ValueMapperConstants.SHORT_DESCRIPTION) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_SHORTDESC,
						blueMartiniResponseObject.getJSONObject(
								ValueMapperConstants.PARENT_PRODUCT).getString(
								ValueMapperConstants.SHORT_DESCRIPTION));
			}

		}	
		
		// The change done for April 2014 Release for LongDescription
		// Parse long description and put in map
		if (blueMartiniResponseObject.getJSONObject(
				ValueMapperConstants.PARENT_PRODUCT).has(
				ValueMapperConstants.LONG_DESCRIPTION)) {

			if (blueMartiniResponseObject.getJSONObject(
					ValueMapperConstants.PARENT_PRODUCT).getString(
					ValueMapperConstants.LONG_DESCRIPTION) != null) {

				blueMartiniMap.put(
						KeyMapperConstants.P_LONGDESC,
						blueMartiniResponseObject.getJSONObject(
								ValueMapperConstants.PARENT_PRODUCT).getString(
								ValueMapperConstants.LONG_DESCRIPTION));

			} else {

				blueMartiniMap.put(KeyMapperConstants.P_LONGDESC,
						CommonConstants.EMPTY_STRING);

			}
		}

		// Parse product type and put in map
		if (blueMartiniResponseObject.getJSONObject(
				ValueMapperConstants.PARENT_PRODUCT).has(
				ValueMapperConstants.PRODUCT_TYPE)) {
			if (blueMartiniResponseObject.getJSONObject(
					ValueMapperConstants.PARENT_PRODUCT).getString(
					ValueMapperConstants.PRODUCT_TYPE) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_PRODUCT_TYPE,
						blueMartiniResponseObject.getJSONObject(
								ValueMapperConstants.PARENT_PRODUCT).getString(
								ValueMapperConstants.PRODUCT_TYPE));
			}

		}
		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method will obtain the product code, vendor number,brand and product name
	 * of parent product from the response and place it into map.
	 * 
	 * @param blueMartiniResponseObject
	 *            it is the response object
	 * @param blueMartiniMap
	 *            Department information are placed in a map.
	 * @param correlationId
	 *            it tracks the input request
	 * @throws JSONException
	 *            Exception thrown from BlueMartiniAdapter layer
	 */
	private void populateParentProduct(
			final JSONObject blueMartiniResponseObject,
			final Map<String, String> blueMartiniMap, final String correlationId)
			throws JSONException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);
		LOGGER.debug("BlueMartiniResponseObject :" + blueMartiniResponseObject,
				correlationId);
		// Parse product code and put in map
		if (blueMartiniResponseObject
				.getString(ValueMapperConstants.PRODUCT_CODE) != null) {
			blueMartiniMap.put(KeyMapperConstants.P_PRODUCT_CODE,
					blueMartiniResponseObject
							.getString(ValueMapperConstants.PRODUCT_CODE));

		}
		// Parse vendor number and put in map
		if (blueMartiniResponseObject.getJSONObject(
				ValueMapperConstants.PARENT_PRODUCT).has(
				ValueMapperConstants.VENDOR_NUMBER)) {
			if (blueMartiniResponseObject.getJSONObject(
					ValueMapperConstants.PARENT_PRODUCT).getString(
					ValueMapperConstants.VENDOR_NUMBER) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_VENDOR_NUMBER,
						blueMartiniResponseObject.getJSONObject(
								ValueMapperConstants.PARENT_PRODUCT).getString(
								ValueMapperConstants.VENDOR_NUMBER));
			}

		}
		// Parse vendor part number and put in map
		// change done for April 2014 Release for VendorPart Number
		if (blueMartiniResponseObject.getJSONObject(
				ValueMapperConstants.PARENT_PRODUCT).has(
				ValueMapperConstants.VENDOR_PART_NUMBER)) {
			if (blueMartiniResponseObject.getJSONObject(
					ValueMapperConstants.PARENT_PRODUCT).getString(
					ValueMapperConstants.VENDOR_PART_NUMBER) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_VENDOR_STYLE,
						blueMartiniResponseObject.getJSONObject(
								ValueMapperConstants.PARENT_PRODUCT).getString(
								ValueMapperConstants.VENDOR_PART_NUMBER));
			}

		}
		// Added This Method to avoid cyclomatics code complexity.
		this.populateCopyText(blueMartiniResponseObject, blueMartiniMap,
				correlationId);

		// Added This Method to avoid cyclomatics code complexity.
		this.populateCopyTextExtended(blueMartiniResponseObject, blueMartiniMap,
				correlationId);

		// Parse brand and put in map
		if (blueMartiniResponseObject.getJSONObject(
				ValueMapperConstants.PARENT_PRODUCT).has(
				ValueMapperConstants.BRAND)) {
			if (blueMartiniResponseObject.getJSONObject(
					ValueMapperConstants.PARENT_PRODUCT).getString(
					ValueMapperConstants.BRAND) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_BRAND,
						blueMartiniResponseObject.getJSONObject(
								ValueMapperConstants.PARENT_PRODUCT).getString(
								ValueMapperConstants.BRAND));
			}

		}

		// Parse product name and put in map
		if (blueMartiniResponseObject.getJSONObject(
				ValueMapperConstants.PARENT_PRODUCT).has(
				ValueMapperConstants.PRODUCT_NAME)) {
			if (blueMartiniResponseObject.getJSONObject(
					ValueMapperConstants.PARENT_PRODUCT).getString(
					ValueMapperConstants.PRODUCT_NAME) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_PRODUCT_NAME,
						blueMartiniResponseObject.getJSONObject(
								ValueMapperConstants.PARENT_PRODUCT).getString(
								ValueMapperConstants.PRODUCT_NAME));
			}

		}
		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/** Method to populate Copy text
	 * @param blueMartiniResponseObject
	 *            response from bluemartini
	 * @param blueMartiniMap
	 *            Result
	 * @param correlationId
	 *            to track the request
	 * @throws JSONException
	 *             Exception thrown from BlueMartiniAdapter layer
	 */
	private void populateCopyText(final JSONObject blueMartiniResponseObject,
			final Map<String, String> blueMartiniMap, final String correlationId)
			throws JSONException {
		// Parse copy text1 and put in map
		// change done for April 2014 Release for VendorPart Number
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);
		LOGGER.debug("BlueMartiniResponseObject :" + blueMartiniResponseObject,
				correlationId);
		if (blueMartiniResponseObject.getJSONObject(
				ValueMapperConstants.PARENT_PRODUCT).has(
				ValueMapperConstants.COPY_LINE_TEXT1)) {
			if (blueMartiniResponseObject.getJSONObject(
					ValueMapperConstants.PARENT_PRODUCT).getString(
					ValueMapperConstants.COPY_LINE_TEXT1) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_COPY_TEXT1,
						blueMartiniResponseObject.getJSONObject(
								ValueMapperConstants.PARENT_PRODUCT).getString(
								ValueMapperConstants.COPY_LINE_TEXT1));
			}

		}
		// Parse copy text2 and put in map
		// change done for April 2014 Release for copylinetest2
		if (blueMartiniResponseObject.getJSONObject(
				ValueMapperConstants.PARENT_PRODUCT).has(
				ValueMapperConstants.COPY_LINE_TEXT2)) {
			if (blueMartiniResponseObject.getJSONObject(
					ValueMapperConstants.PARENT_PRODUCT).getString(
					ValueMapperConstants.COPY_LINE_TEXT2) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_COPY_TEXT2,
						blueMartiniResponseObject.getJSONObject(
								ValueMapperConstants.PARENT_PRODUCT).getString(
								ValueMapperConstants.COPY_LINE_TEXT2));
			}

		}

		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**  Method to populate Copy text
	 * @param blueMartiniResponseObject
	 *            response from bluemartini
	 * @param blueMartiniMap
	 *            Result
	 * @param correlationId
	 *            to track the request
	 * @throws JSONException
	 *             Exception thrown from BlueMartiniAdapter layer
	 */
	private void populateCopyTextExtended(final JSONObject blueMartiniResponseObject,
			final Map<String, String> blueMartiniMap, final String correlationId)
			throws JSONException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);
		LOGGER.debug("BlueMartiniResponseObject :" + blueMartiniResponseObject,
				correlationId);
		// Parse copy text3 and put in map
		// change done for April 2014 Release for copylinetest3
		if (blueMartiniResponseObject.getJSONObject(
				ValueMapperConstants.PARENT_PRODUCT).has(
				ValueMapperConstants.COPY_LINE_TEXT3)) {
			if (blueMartiniResponseObject.getJSONObject(
					ValueMapperConstants.PARENT_PRODUCT).getString(
					ValueMapperConstants.COPY_LINE_TEXT3) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_COPY_TEXT3,
						blueMartiniResponseObject.getJSONObject(
								ValueMapperConstants.PARENT_PRODUCT).getString(
								ValueMapperConstants.COPY_LINE_TEXT3));
			}

		}
		// Parse copy text4 and put in map
		// change done for April 2014 Release for copylinetest4
		if (blueMartiniResponseObject.getJSONObject(
				ValueMapperConstants.PARENT_PRODUCT).has(
				ValueMapperConstants.COPY_LINE_TEXT4)) {
			if (blueMartiniResponseObject.getJSONObject(
					ValueMapperConstants.PARENT_PRODUCT).getString(
					ValueMapperConstants.COPY_LINE_TEXT4) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_COPY_TEXT4,
						blueMartiniResponseObject.getJSONObject(
								ValueMapperConstants.PARENT_PRODUCT).getString(
								ValueMapperConstants.COPY_LINE_TEXT4));
			}

		}
		// Parse copy text5 and put in map
		// change done for April 2014 Release for copylinetest5
		if (blueMartiniResponseObject.getJSONObject(
				ValueMapperConstants.PARENT_PRODUCT).has(
				ValueMapperConstants.COPY_LINE_TEXT5)) {
			if (blueMartiniResponseObject.getJSONObject(
					ValueMapperConstants.PARENT_PRODUCT).getString(
					ValueMapperConstants.COPY_LINE_TEXT5) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_COPY_TEXT5,
						blueMartiniResponseObject.getJSONObject(
								ValueMapperConstants.PARENT_PRODUCT).getString(
								ValueMapperConstants.COPY_LINE_TEXT5));
			}

		}
		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method will obtain the Image url from the response and place it into map.
	 * 
	 * @param blueMartiniResponseObject
	 *            it is the response object
	 * @param blueMartiniMap
	 *            Department information are placed in a map.
	 * @param index
	 *            index value of the loop
	 * @param correlationId
	 *            It tracks the input request
	 * @throws JSONException
	 *             Exception thrown from BlueMartiniAdapter layer
	 */
	private void populateImageUrl(final JSONObject blueMartiniResponseObject,
			final Map<String, String> blueMartiniMap, final int index,
			final String correlationId) throws JSONException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);
		LOGGER.debug("BlueMartiniResponseObject :" + blueMartiniResponseObject,
				correlationId);
		if (blueMartiniResponseObject.getJSONArray(ValueMapperConstants.SKUS)
				.getJSONObject(index).has(ValueMapperConstants.IMAGE_URL)) {
			if (blueMartiniResponseObject
					.getJSONArray(ValueMapperConstants.SKUS)
					.getJSONObject(index)
					.getString(ValueMapperConstants.IMAGE_URL) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_PICTURE_URL,
						blueMartiniResponseObject
								.getJSONArray(ValueMapperConstants.SKUS)
								.getJSONObject(index)
								.getString(ValueMapperConstants.IMAGE_URL)
								.replace("$P_THUMB$", ""));
			}

		}

		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method will obtain the list price and sku sale price from the response
	 * and place it into map.
	 * 
	 * @param blueMartiniResponseObject
	 *            it is the response object
	 * @param blueMartiniMap
	 *            Department information are placed in a map.
	 * @param correlationId
	 *            it tracks the input request
	 * @throws JSONException
	 *             Exception thrown from BlueMartiniAdapter layer
	 */
	private void populatePrice(final JSONObject blueMartiniResponseObject,
			final Map<String, String> blueMartiniMap, final String correlationId)
			throws JSONException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);
		LOGGER.debug("BlueMartiniResponseObject :" + blueMartiniResponseObject,
				correlationId);
		if (blueMartiniResponseObject.getJSONObject(
				ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS).has(
				ValueMapperConstants.LIST_PRICE)) {
			if (blueMartiniResponseObject.getJSONObject(
					ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS).getString(
					ValueMapperConstants.LIST_PRICE) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_LISTPRICE,
						blueMartiniResponseObject.getJSONObject(
								ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS)
								.getString(ValueMapperConstants.LIST_PRICE));
			}

		}

		// Parse sale price and put in map
		if (blueMartiniResponseObject.getJSONObject(
				ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS).has(
				ValueMapperConstants.SALE_PRICE)) {
			if (blueMartiniResponseObject.getJSONObject(
					ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS).getString(
					ValueMapperConstants.SALE_PRICE) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_SKUS_SALE_PRICE,
						blueMartiniResponseObject.getJSONObject(
								ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS)
								.getString(ValueMapperConstants.SALE_PRICE));
			}

		}

		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);
		LOGGER.debug("BlueMartiniResponseObject :" + blueMartiniResponseObject,
				correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method will obtain the inventory from the response and place it into map.
	 * 
	 * @param blueMartiniResponseObject
	 *            it is the response object
	 * @param blueMartiniMap
	 *            Department information are placed in a map.
	 * @param index
	 *            index value of the loop
	 * @param correlationId
	 *            it tracks the input request
	 * @throws JSONException
	 *             Exception thrown from BlueMartiniAdapter layer
	 */
	private void populateSkuInventory(
			final JSONObject blueMartiniResponseObject,
			final Map<String, String> blueMartiniMap, final int index,
			final String correlationId) throws JSONException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);
		LOGGER.debug("BlueMartiniResponseObject :" + blueMartiniResponseObject,
				correlationId);
		if (blueMartiniResponseObject.getJSONArray(ValueMapperConstants.SKUS)
				.getJSONObject(index)
				.has(ValueMapperConstants.INVENTORY_AVAILABLE)) {
			if (blueMartiniResponseObject
					.getJSONArray(ValueMapperConstants.SKUS)
					.getJSONObject(index)
					.getString(ValueMapperConstants.INVENTORY_AVAILABLE) != null) {
				blueMartiniMap
						.put(KeyMapperConstants.P_INVENTORY_AVAIL,
								blueMartiniResponseObject
										.getJSONArray(ValueMapperConstants.SKUS)
										.getJSONObject(index)
										.getString(
												ValueMapperConstants.INVENTORY_AVAILABLE));
			}

		}

		// Parse inventory level and put in map
		if (blueMartiniResponseObject.getJSONArray(ValueMapperConstants.SKUS)
				.getJSONObject(index).has(ValueMapperConstants.INVENTORY_LEVEL)) {
			if (blueMartiniResponseObject
					.getJSONArray(ValueMapperConstants.SKUS)
					.getJSONObject(index)
					.getString(ValueMapperConstants.INVENTORY_LEVEL) != null) {
				blueMartiniMap
						.put(KeyMapperConstants.P_INVENTORY_LEVEL,
								blueMartiniResponseObject
										.getJSONArray(ValueMapperConstants.SKUS)
										.getJSONObject(index)
										.getString(
												ValueMapperConstants.INVENTORY_LEVEL));
			}
		}

		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method will obtain the sku code, sku upc ,colors and size from the
	 * response and place it into map.
	 * 
	 * @param blueMartiniResponseObject
	 *            it is the response object
	 * @param blueMartiniMap
	 *            Department information are placed in a map.
	 * @param index
	 *            index value of the loop
	 * @param correlationId
	 *            it tracks the request
	 * @throws JSONException
	 *             Exception thrown from BlueMartiniAdapter layer
	 */
	private void populateSku(final JSONObject blueMartiniResponseObject,
			final Map<String, String> blueMartiniMap, final int index,
			final String correlationId) throws JSONException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);
		LOGGER.debug("BlueMartiniResponseObject :" + blueMartiniResponseObject,
				correlationId);
		this.populateSkuCode(blueMartiniResponseObject, blueMartiniMap, index,
				correlationId);
		// Parse short description for and put in map
		
		if (blueMartiniResponseObject.getJSONArray(ValueMapperConstants.SKUS)
				.getJSONObject(index).has(ValueMapperConstants.SHORT_DESCRIPTION)) {
			if (blueMartiniResponseObject
					.getJSONArray(ValueMapperConstants.SKUS)
					.getJSONObject(index)
					.getString(ValueMapperConstants.SHORT_DESCRIPTION) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_SKU_SHORTDESC,
						blueMartiniResponseObject
								.getJSONArray(ValueMapperConstants.SKUS)
								.getJSONObject(index)
								.getString(ValueMapperConstants.SHORT_DESCRIPTION));
			}
		}
		
		this.populateSkuColor(blueMartiniResponseObject, blueMartiniMap, index);
		// Parse size and put in map
		this.populateSkuSize(blueMartiniResponseObject, blueMartiniMap, index,
				correlationId);

		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);

	}

	/**
	 * Method will obtain the sku color from the response and place it into map.
	 * 
	 * @param blueMartiniResponseObject
	 *            it is the response object
	 * @param blueMartiniMap
	 *            Department information are placed in a map.
	 * @param index
	 *            index value of the loop
	 * @throws JSONException
	 *             Exception thrown from BlueMartiniAdapter layer
	 */
	private void populateSkuColor(final JSONObject blueMartiniResponseObject,
			final Map<String, String> blueMartiniMap, final int index)
			throws JSONException {
		// Parse color and put in map
		if (blueMartiniResponseObject.getJSONArray(ValueMapperConstants.SKUS)
				.getJSONObject(index).has(ValueMapperConstants.COLOR1)) {
			if (blueMartiniResponseObject
					.getJSONArray(ValueMapperConstants.SKUS)
					.getJSONObject(index)
					.getString(ValueMapperConstants.COLOR1) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_COLOR,
						blueMartiniResponseObject
								.getJSONArray(ValueMapperConstants.SKUS)
								.getJSONObject(index)
								.getString(ValueMapperConstants.COLOR1));
			}
		}

		// Parse color and put in map
		if (blueMartiniResponseObject.getJSONArray(ValueMapperConstants.SKUS)
				.getJSONObject(index).has(ValueMapperConstants.COLOR2)) {
			if (blueMartiniResponseObject
					.getJSONArray(ValueMapperConstants.SKUS)
					.getJSONObject(index)
					.getString(ValueMapperConstants.COLOR2) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_COLOR2,
						blueMartiniResponseObject
								.getJSONArray(ValueMapperConstants.SKUS)
								.getJSONObject(index)
								.getString(ValueMapperConstants.COLOR2));
			}

		}

		// Parse color and put in map
		if (blueMartiniResponseObject.getJSONArray(ValueMapperConstants.SKUS)
				.getJSONObject(index).has(ValueMapperConstants.COLOR3)) {
			if (blueMartiniResponseObject
					.getJSONArray(ValueMapperConstants.SKUS)
					.getJSONObject(index)
					.getString(ValueMapperConstants.COLOR3) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_COLOR3,
						blueMartiniResponseObject
								.getJSONArray(ValueMapperConstants.SKUS)
								.getJSONObject(index)
								.getString(ValueMapperConstants.COLOR3));
			}

		}

		// Parse color and put in map
		if (blueMartiniResponseObject.getJSONArray(ValueMapperConstants.SKUS)
				.getJSONObject(index).has(ValueMapperConstants.COLOR4)) {
			if (blueMartiniResponseObject
					.getJSONArray(ValueMapperConstants.SKUS)
					.getJSONObject(index)
					.getString(ValueMapperConstants.COLOR4) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_COLOR1,
						blueMartiniResponseObject
								.getJSONArray(ValueMapperConstants.SKUS)
								.getJSONObject(index)
								.getString(ValueMapperConstants.COLOR4));
			}

		}
	}

	/**
	 * Method will obtain the sku size from the response and place it into map.
	 * 
	 * @param blueMartiniResponseObject
	 *            it is the response object
	 * @param blueMartiniMap
	 *            Department information are placed in a map.
	 * @param index
	 *            index value of the loop
	 * @param correlationId
	 *            it tracks teh request
	 * @throws JSONException
	 *             Exception thrown from BlueMartiniAdapter layer
	 */
	private void populateSkuSize(final JSONObject blueMartiniResponseObject,
			final Map<String, String> blueMartiniMap, final int index,
			final String correlationId) throws JSONException {
		final long startTime = LOGGER.logMethodEntry(correlationId);

		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);
		LOGGER.debug("BlueMartiniResponseObject :" + blueMartiniResponseObject,
				correlationId);
		if (blueMartiniResponseObject.getJSONArray(ValueMapperConstants.SKUS)
				.getJSONObject(index).has(ValueMapperConstants.SIZE)) {
			if (blueMartiniResponseObject
					.getJSONArray(ValueMapperConstants.SKUS)
					.getJSONObject(index).getString(ValueMapperConstants.SIZE) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_SIZE,
						blueMartiniResponseObject
								.getJSONArray(ValueMapperConstants.SKUS)
								.getJSONObject(index)
								.getString(ValueMapperConstants.SIZE));
			}

		}
		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method will obtain the sku code and sku upc from the response and place
	 * it into map.
	 * 
	 * @param blueMartiniResponseObject
	 *            it is the response object
	 * @param blueMartiniMap
	 *            Department information are placed in a map.
	 * @param index
	 *            index value of the loop
	 * @param correlationId
	 *            it tracks the request
	 * @throws JSONException
	 *             Exception thrown from BlueMartiniAdapter layer
	 */
	private void populateSkuCode(final JSONObject blueMartiniResponseObject,
			final Map<String, String> blueMartiniMap, final int index,
			final String correlationId) throws JSONException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);
		LOGGER.debug("BlueMartiniResponseObject :" + blueMartiniResponseObject,
				correlationId);
		// Parse sku code and put in map
		if (blueMartiniResponseObject.getJSONArray(ValueMapperConstants.SKUS)
				.getJSONObject(index).has(ValueMapperConstants.SKU_CODE)) {
			if (blueMartiniResponseObject
					.getJSONArray(ValueMapperConstants.SKUS)
					.getJSONObject(index)
					.getString(ValueMapperConstants.SKU_CODE) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_SKU_NO,
						blueMartiniResponseObject
								.getJSONArray(ValueMapperConstants.SKUS)
								.getJSONObject(index)
								.getString(ValueMapperConstants.SKU_CODE));
			}
		}

		// Parse sku upc and put in map
		if (blueMartiniResponseObject.getJSONArray(ValueMapperConstants.SKUS)
				.getJSONObject(index).has(ValueMapperConstants.SKU_UPC)) {
			if (blueMartiniResponseObject
					.getJSONArray(ValueMapperConstants.SKUS)
					.getJSONObject(index)
					.getString(ValueMapperConstants.SKU_UPC) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_SKU_UPC,
						blueMartiniResponseObject
								.getJSONArray(ValueMapperConstants.SKUS)
								.getJSONObject(index)
								.getString(ValueMapperConstants.SKU_UPC));
			}
		}
		
		// Parse sku id and put in map
				if (blueMartiniResponseObject.getJSONArray(ValueMapperConstants.SKUS)
						.getJSONObject(index).has(ValueMapperConstants.SKU_ID)) {
					if (blueMartiniResponseObject
							.getJSONArray(ValueMapperConstants.SKUS)
							.getJSONObject(index)
							.get(ValueMapperConstants.SKU_ID) != null) {
						blueMartiniMap.put(
								KeyMapperConstants.P_SKU_ID,
								blueMartiniResponseObject
										.getJSONArray(ValueMapperConstants.SKUS)
										.getJSONObject(index)
										.get(ValueMapperConstants.SKU_ID).toString());
					}
				}
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method will obtain the bridal status of the product from the response and
	 * place it into map.
	 * 
	 * @param blueMartiniResponseObject
	 *            it is the response object
	 * @param blueMartiniMap
	 *            Department information are placed in a map.
	 * @param correlationId
	 *            it tracks the request
	 * @throws JSONException
	 *             Exception thrown from BlueMartiniAdapter layer
	 */
	private void bridalStatus(final JSONObject blueMartiniResponseObject,
			final Map<String, String> blueMartiniMap, final String correlationId)
			throws JSONException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		if (blueMartiniResponseObject.getJSONObject(
				ValueMapperConstants.PARENT_PRODUCT).has(
				ValueMapperConstants.BRIDAL_ELIGIBLE)) {
			if (blueMartiniResponseObject.getJSONObject(
					ValueMapperConstants.PARENT_PRODUCT).getString(
					ValueMapperConstants.BRIDAL_ELIGIBLE) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_IS_BRIDAL,
						blueMartiniResponseObject.getJSONObject(
								ValueMapperConstants.PARENT_PRODUCT).getString(
								ValueMapperConstants.BRIDAL_ELIGIBLE));
			}

			LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);
			LOGGER.logMethodExit(startTime, correlationId);
		}

	}

	/**
	 * Method will obtain the new arrival of the product from the response and
	 * place it into map.
	 * 
	 * @param blueMartiniResponseObject
	 *            it is the response object
	 * @param blueMartiniMap
	 *            Department information are placed in a map.
	 * @param correlationId
	 *            it tracks the request
	 * @throws JSONException
	 *            Exception thrown from BlueMartiniAdapter layer
	 */
	private void productArrival(final JSONObject blueMartiniResponseObject,
			final Map<String, String> blueMartiniMap, final String correlationId)
			throws JSONException {
		final String blueMartiniConstants = "BlueMartiniConstants.newArrival";
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);
		LOGGER.debug("BlueMartiniResponseObject :" + blueMartiniResponseObject,
				correlationId);
		if (blueMartiniResponseObject.getJSONObject(
				ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS).has(
				blueMartiniConstants)) {
			if (blueMartiniResponseObject.getJSONObject(
					ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS).getString(
					blueMartiniConstants) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_NEW_ARRIVAL,
						blueMartiniResponseObject.getJSONObject(
								ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS)
								.getString(blueMartiniConstants));
			}
		} else if (blueMartiniResponseObject.getJSONObject(
				ValueMapperConstants.PARENT_PRODUCT).has(
				ValueMapperConstants.NEW_ARRIVAL)) {
			if (blueMartiniResponseObject.getJSONObject(
					ValueMapperConstants.PARENT_PRODUCT).getString(
					ValueMapperConstants.NEW_ARRIVAL) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_NEW_ARRIVAL,
						blueMartiniResponseObject.getJSONObject(
								ValueMapperConstants.PARENT_PRODUCT).getString(
								ValueMapperConstants.NEW_ARRIVAL));
			}
		} else if (blueMartiniResponseObject
				.getJSONArray(ValueMapperConstants.SKUS).getJSONObject(0)
				.has(ValueMapperConstants.NEW_ARRIVAL)) {
			if (blueMartiniResponseObject
					.getJSONArray(ValueMapperConstants.SKUS).getJSONObject(0)
					.getString(ValueMapperConstants.NEW_ARRIVAL) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_NEW_ARRIVAL,
						blueMartiniResponseObject
								.getJSONArray(ValueMapperConstants.SKUS)
								.getJSONObject(0)
								.getString(ValueMapperConstants.NEW_ARRIVAL));
			}
		} else {

			blueMartiniMap.put(KeyMapperConstants.P_NEW_ARRIVAL,
					BlueMartiniAdapterConstants.FALSE_VALUE);
		}

		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);

		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method will obtain the clearance status of the product from the response
	 * and place it into map.
	 * 
	 * @param blueMartiniResponseObject
	 *            it is the response object
	 * @param blueMartiniMap
	 *            Department information are placed in a map.
	 * @param correlationId
	 *            it tracks the request
	 * @throws JSONException
	 *            Exception thrown from BlueMartiniAdapter layer
	 */
	private void productClearance(final JSONObject blueMartiniResponseObject,
			final Map<String, String> blueMartiniMap, final String correlationId)
			throws JSONException {
		final long startTime = LOGGER.logMethodEntry(correlationId);

		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);
		LOGGER.debug("BlueMartiniResponseObject :" + blueMartiniResponseObject,
				correlationId);

		if (blueMartiniResponseObject.getJSONObject(
				ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS).has(
				ValueMapperConstants.CLEARANCE)) {
			if (blueMartiniResponseObject.getJSONObject(
					ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS).getString(
					ValueMapperConstants.CLEARANCE) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_CLEARANCE,
						blueMartiniResponseObject.getJSONObject(
								ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS)
								.getString(ValueMapperConstants.CLEARANCE));
			}
		} else if (blueMartiniResponseObject.getJSONObject(
				ValueMapperConstants.PARENT_PRODUCT).has(
				ValueMapperConstants.CLEARANCE)) {
			if (blueMartiniResponseObject.getJSONObject(
					ValueMapperConstants.PARENT_PRODUCT).getString(
					ValueMapperConstants.CLEARANCE) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_CLEARANCE,
						blueMartiniResponseObject.getJSONObject(
								ValueMapperConstants.PARENT_PRODUCT).getString(
								ValueMapperConstants.CLEARANCE));
			}
		} else if (blueMartiniResponseObject
				.getJSONArray(ValueMapperConstants.SKUS).getJSONObject(0)
				.has(ValueMapperConstants.CLEARANCE)) {
			if (blueMartiniResponseObject
					.getJSONArray(ValueMapperConstants.SKUS).getJSONObject(0)
					.getString(ValueMapperConstants.CLEARANCE) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_CLEARANCE,
						blueMartiniResponseObject
								.getJSONArray(ValueMapperConstants.SKUS)
								.getJSONObject(0)
								.getString(ValueMapperConstants.CLEARANCE));
			}
		} else {

			blueMartiniMap.put(KeyMapperConstants.P_CLEARANCE,
					BlueMartiniAdapterConstants.FALSE_VALUE);
		}

		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);

		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method will obtain the sale price from the response and place it into
	 * map.
	 * 
	 * @param blueMartiniResponseObject
	 *            it is the response object
	 * @param blueMartiniMap
	 *            Department information are placed in a map.
	 * @param correlationId
	 *            it tracks the request
	 * @throws JSONException
	 *             Exception thrown from BlueMartiniAdapter layer
	 */
	private void salePrice(final JSONObject blueMartiniResponseObject,
			final Map<String, String> blueMartiniMap, final String correlationId)
			throws JSONException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);
		LOGGER.debug("BlueMartiniResponseObject :" + blueMartiniResponseObject,
				correlationId);
		// Parse sale price range and put in map
		if (blueMartiniResponseObject.getJSONObject(
				ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS).has(
				ValueMapperConstants.SALE_PRICE)) {
			if (blueMartiniResponseObject.getJSONObject(
					ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS).getString(
					ValueMapperConstants.SALE_PRICE) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_SALES_PRICE_RANGE,
						blueMartiniResponseObject.getJSONObject(
								ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS)
								.getString(ValueMapperConstants.SALE_PRICE));
			}
		}

		if (blueMartiniResponseObject.getJSONObject(
				ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS).has(
				ValueMapperConstants.SALE_PRICE)) {
			if (blueMartiniResponseObject.getJSONObject(
					ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS).getString(
					ValueMapperConstants.SALE_PRICE) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_SALE_PRICE,
						blueMartiniResponseObject.getJSONObject(
								ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS)
								.getString(ValueMapperConstants.SALE_PRICE));
			}
		}
		// Parse max sale price and put in map
		if (blueMartiniResponseObject.getJSONObject(
				ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS).has(
				ValueMapperConstants.SALE_PRICE)) {
			if (blueMartiniResponseObject.getJSONObject(
					ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS).getString(
					ValueMapperConstants.SALE_PRICE) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_MAX_SALES_PRICE,
						blueMartiniResponseObject.getJSONObject(
								ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS)
								.getString(ValueMapperConstants.SALE_PRICE));
			}
		}
		// Parse min sale price and put in map
		if (blueMartiniResponseObject.getJSONObject(
				ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS).has(
				ValueMapperConstants.SALE_PRICE)) {
			if (blueMartiniResponseObject.getJSONObject(
					ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS).getString(
					ValueMapperConstants.SALE_PRICE) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_MIN_SALES_PRICE,
						blueMartiniResponseObject.getJSONObject(
								ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS)
								.getString(ValueMapperConstants.SALE_PRICE));
			}
		}

		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);

		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method will obtain the department information from the response and place
	 * it into map.
	 * 
	 * @param blueMartiniResponseObject
	 *            it is the response object
	 * @param blueMartiniMap
	 *            Department information are placed in a map.
	 * @param correlationId
	 *            it tracks the request
	 * @throws JSONException
	 *             Exception thrown from BlueMartiniAdapter layer
	 */
	private void departmentDetails(final JSONObject blueMartiniResponseObject,
			final Map<String, String> blueMartiniMap, final String correlationId)
			throws JSONException {
		final long startTime = LOGGER.logMethodEntry(correlationId);

		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);
		LOGGER.debug("BlueMartiniResponseObject :" + blueMartiniResponseObject,
				correlationId);

		if (blueMartiniResponseObject.getJSONObject(
				ValueMapperConstants.PARENT_PRODUCT).has(
				ValueMapperConstants.DEPARTMENT_NUMBER)) {
			if (blueMartiniResponseObject.getJSONObject(
					ValueMapperConstants.PARENT_PRODUCT).getString(
					ValueMapperConstants.DEPARTMENT_NUMBER) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_DEPT_ID,
						blueMartiniResponseObject.getJSONObject(
								ValueMapperConstants.PARENT_PRODUCT).getString(
								ValueMapperConstants.DEPARTMENT_NUMBER));
			}
		}

		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);

		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method will obtain the class name from the response and place it into
	 * map.
	 * 
	 * @param blueMartiniResponseObject
	 *            it is the response object
	 * @param blueMartiniMap
	 *            class name information are placed in map
	 * @param correlationId
	 *            it tracks the request
	 * @throws JSONException
	 *             Exception thrown from BlueMartiniAdapter layer
	 */
	private void classNumberDetails(final JSONObject blueMartiniResponseObject,
			final Map<String, String> blueMartiniMap, final String correlationId)
			throws JSONException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);
		LOGGER.debug("BlueMartiniResponseObject :" + blueMartiniResponseObject,
				correlationId);
		if (blueMartiniResponseObject.getJSONObject(
				ValueMapperConstants.PARENT_PRODUCT).has(
				ValueMapperConstants.CLASS_NUMBER)) {
			if (blueMartiniResponseObject.getJSONObject(
					ValueMapperConstants.PARENT_PRODUCT).getString(
					ValueMapperConstants.CLASS_NUMBER) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_CLASS_NAME,
						blueMartiniResponseObject.getJSONObject(
								ValueMapperConstants.PARENT_PRODUCT).getString(
								ValueMapperConstants.CLASS_NUMBER));
			}
		}

		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method will obtain the list price from the response and place it into
	 * map.
	 * 
	 * @param blueMartiniResponseObject
	 *            it is the response object
	 * @param blueMartiniMap
	 *            class name information are placed in map
	 * @param correlationId
	 *            it tracks the request
	 * @throws JSONException
	 *             Exception thrown from BlueMartiniAdapter layer
	 */

	private void listPrice(final JSONObject blueMartiniResponseObject,
			final Map<String, String> blueMartiniMap, final String correlationId)
			throws JSONException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);
		LOGGER.debug("BlueMartiniResponseObject :" + blueMartiniResponseObject,
				correlationId);
		// Parse list price range and put in map
		if (blueMartiniResponseObject.getJSONObject(
				ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS).has(
				ValueMapperConstants.LIST_PRICE)) {
			if (blueMartiniResponseObject.getJSONObject(
					ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS).getString(
					ValueMapperConstants.LIST_PRICE) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_LIST_PRICE_RANGE,
						blueMartiniResponseObject.getJSONObject(
								ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS)
								.getString(ValueMapperConstants.LIST_PRICE));
			}
		}

		if (blueMartiniResponseObject.getJSONObject(
				ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS).has(
				ValueMapperConstants.LIST_PRICE)) {
			if (blueMartiniResponseObject.getJSONObject(
					ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS).getString(
					ValueMapperConstants.LIST_PRICE) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_LISTPRICE,
						blueMartiniResponseObject.getJSONObject(
								ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS)
								.getString(ValueMapperConstants.LIST_PRICE));
			}
		}
		// Parse max list price and put in map
		if (blueMartiniResponseObject.getJSONObject(
				ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS).has(
				ValueMapperConstants.LIST_PRICE)) {
			if (blueMartiniResponseObject.getJSONObject(
					ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS).getString(
					ValueMapperConstants.LIST_PRICE) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_MAX_LIST_PRICE,
						blueMartiniResponseObject.getJSONObject(
								ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS)
								.getString(ValueMapperConstants.LIST_PRICE));
			}
		}
		// Parse min list price and put in map
		if (blueMartiniResponseObject.getJSONObject(
				ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS).has(
				ValueMapperConstants.LIST_PRICE)) {
			if (blueMartiniResponseObject.getJSONObject(
					ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS).getString(
					ValueMapperConstants.LIST_PRICE) != null) {
				blueMartiniMap.put(
						KeyMapperConstants.P_MIN_LIST_PRICE,
						blueMartiniResponseObject.getJSONObject(
								ValueMapperConstants.COMPOSITE_PRODUCT_DETAILS)
								.getString(ValueMapperConstants.LIST_PRICE));
			}
		}

		LOGGER.debug("BlueMartiniMap :" + blueMartiniMap, correlationId);

		LOGGER.logMethodExit(startTime, correlationId);
	}
}
