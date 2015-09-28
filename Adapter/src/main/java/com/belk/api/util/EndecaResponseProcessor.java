package com.belk.api.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.belk.api.constants.CommonConstants;
import com.belk.api.constants.DomainConstants;
import com.belk.api.constants.EndecaConstants;
import com.belk.api.constants.ErrorConstants;
import com.belk.api.exception.AdapterException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.endeca.content.ContentItem;
import com.endeca.content.ene.ENEContentResults;
import com.endeca.content.ene.NavigationRecords;
import com.endeca.navigation.AggrERec;
import com.endeca.navigation.AggrERecList;
import com.endeca.navigation.DimVal;
import com.endeca.navigation.DimValList;
import com.endeca.navigation.Dimension;
import com.endeca.navigation.DimensionList;
import com.endeca.navigation.ENEQueryResults;
import com.endeca.navigation.ERec;
import com.endeca.navigation.ERecList;
import com.endeca.navigation.Navigation;
import com.endeca.navigation.PropertyMap;

/**
 * The Endeca specific class to process a response obtained from Endeca and
 * added to a map. Updated : Added few comments and changed the name of
 * variables as part of Phase2, April,14 release
 * Update: Changed the logic for reading the generic keys as part of API-352.
 * 
 * @author Mindtree
 * @date Aug 22, 2013
 */
@Service
public class EndecaResponseProcessor {

	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	@Autowired
	AdapterUtil adapterUtil;

	@Autowired
	EndecaFieldListLoader endecaFieldListLoader;
	
	@Autowired
	EndecaResponseFieldListLoader endecaResponseFieldListLoader;

	@Autowired
	ErrorLoader errorLoader;

	/**
	 * Default Constructor.
	 */
	public EndecaResponseProcessor() {
	}

	/**
	 * @param endecaFieldListLoader
	 *            the endecaFieldListLoader to set
	 */
	public final void setEndecaFieldListLoader(
			final EndecaFieldListLoader endecaFieldListLoader) {
		this.endecaFieldListLoader = endecaFieldListLoader;
	}

	/**
	 * @param endecaResponseFieldListLoader the endecaResponseFieldListLoader to set
	 */
	public final void setEndecaResponseFieldListLoader(
			final EndecaResponseFieldListLoader endecaResponseFieldListLoader) {
		this.endecaResponseFieldListLoader = endecaResponseFieldListLoader;
	}

	/**
	 * @param adapterUtil
	 *            the adapterUtil to set
	 */
	public final void setAdapterUtil(final AdapterUtil adapterUtil) {
		this.adapterUtil = adapterUtil;
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
	 * The method to iterate through the complete response coming from Endeca
	 * and populate the same to a list of maps.
	 * 
	 * @param requestMap
	 *            The map containing the request parameters.
	 * @param endecaQueryResults
	 *            The resultset obtained as an Endeca response
	 * @param correlationId
	 *            to track the request
	 * @return endecaList The processed list of map of values coming from Endeca
	 * @throws AdapterException
	 *             The exception thrown from this method
	 */
	public final List<Map<String, String>> responseProcessor(
			final Map<String, String> requestMap,
			final ENEQueryResults endecaQueryResults, final String correlationId)
					throws AdapterException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final Map<String, String> errorPropertiesMap = this.errorLoader
				.getErrorPropertiesMap();
		// The list of result from endeca for the query will be added.
		List<Map<String, String>> endecaResultList = new ArrayList<Map<String, String>>();
		// Map adds total products and total skus to result list.
		final Map<String, String> endecaMap = new LinkedHashMap<String, String>();
		// Stores the navigation results from the endeca.
		final Navigation navigation = endecaQueryResults.getNavigation();
		if (null != navigation) {
			endecaMap.put(CommonConstants.TOTAL_PRODUCTS,
					String.valueOf(navigation.getTotalNumAggrERecs()));
			endecaMap.put(CommonConstants.TOTAL_SKUS,
					String.valueOf(navigation.getTotalNumERecs()));
			endecaResultList.add(endecaMap);
			final AggrERecList aggrList = navigation.getAggrERecs();
			endecaResultList = this.populateProductsList(endecaResultList,
					aggrList, correlationId);
			if (requestMap.containsKey(EndecaConstants.DIM)
					&& CommonConstants.TRUE_VALUE.equalsIgnoreCase(requestMap
							.get(EndecaConstants.DIM))
							/*
							 * Condition below is required only for cases where refinements
							 * are required
							 */
							&& requestMap.containsKey(EndecaConstants.REFINEMENTS_REQUIRED)) {
				endecaResultList = this.populateCompleteDimensionList(
						endecaResultList, navigation, correlationId);
			} else if (requestMap.containsKey(EndecaConstants.DIM)) {
				// dim value exists but only one level of refinement is required.
				endecaResultList = this.populateDescriptorDimensionList(requestMap,
						endecaResultList, navigation, correlationId);
			}
		} else {
			throw new AdapterException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String
							.valueOf(ErrorConstants.ERROR_CODE_11523)),
							ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
							correlationId);
		}
		LOGGER.logMethodExit(startTime, correlationId);
		LOGGER.info("Size of the Endeca Map List is : " + endecaResultList,
				correlationId);
		return endecaResultList;
	}


	/**
	 * The method to iterate through the complete response coming from Endeca
	 * and populate the same to a list of maps.
	 * @param requestMap
	 * 		Request Elements from URI
	 * @param eneContentResults
	 * 		Endeca Content Query results
	 * @param correlationId
	 * 		to track the request
	 * @return The processed list of map of values coming from Endeca
	 * @throws AdapterException The exception thrown from this method
	 */
	public final List<Map<String, String>> contentResponseProcessor(
			final Map<String, String> requestMap,
			final ENEContentResults eneContentResults, final String correlationId)
					throws AdapterException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final Map<String, String> errorPropertiesMap = this.errorLoader
				.getErrorPropertiesMap();
		// The list of result from endeca for the query will be added.
		List<Map<String, String>> endecaResultList = new ArrayList<Map<String, String>>();
		// Map adds total products and total skus to result list.
		final Map<String, String> endecaMap = new LinkedHashMap<String, String>();
		// Stores the navigation results from the endeca.
		AggrERecList aggrList = null;
		final ENEQueryResults endecaResults = eneContentResults
				.getENEQueryResults();
		final ContentItem contentItem = eneContentResults.getContent();

		// Center column needs to be fetched from query results which contain Boost N Bury results
		NavigationRecords navigationRecords = null;
		Navigation navigation = null;
		if (endecaResults.containsNavigation()) {
			navigation = endecaResults.getNavigation();
			navigationRecords = this.getNavigationRecords(contentItem);


			String productCount = null;
			if (null != navigationRecords) {
				productCount = String.valueOf(navigationRecords.getTotalNumAggrERecs());
			} else {
				productCount = String.valueOf(navigation.getTotalNumAggrERecs());
			}

			String skuCount =  null;
			if (null != navigationRecords) {
				skuCount = String.valueOf(navigationRecords.getTotalNumERecs());
			} else {
				skuCount = String.valueOf(navigation.getTotalNumERecs());
			}
			endecaMap.put(CommonConstants.TOTAL_PRODUCTS,
					productCount);
			endecaMap.put(CommonConstants.TOTAL_SKUS,
					skuCount);
			endecaResultList.add(endecaMap);

			/*
			 * The Aggr Records are fetched either from Navigation object(No
			 * BnB) or from NavigationRecords object (if BnB exists)
			 */
			if (navigationRecords != null && !navigationRecords.getAggrERecs().isEmpty()) {
				aggrList = navigationRecords.getAggrERecs();
			} else {
				aggrList = navigation.getAggrERecs();
			}

			endecaResultList = this.populateProductsList(endecaResultList,
					aggrList, correlationId);
			if (requestMap.containsKey(EndecaConstants.DIM)
					&& CommonConstants.TRUE_VALUE.equalsIgnoreCase(requestMap
							.get(EndecaConstants.DIM))
							/*
							 * Condition below is required only for cases where refinements
							 * are required
							 */
							&& requestMap.containsKey(EndecaConstants.REFINEMENTS_REQUIRED)) {
				endecaResultList = this.populateEndecaResultList(endecaResultList,
						correlationId,
						contentItem, navigation);
			}
		} else {
			throw new AdapterException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String
							.valueOf(ErrorConstants.ERROR_CODE_11523)),
							ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
							correlationId);
		}
		LOGGER.logMethodExit(startTime, correlationId);
		LOGGER.info("Size of the Endeca Map List is : " + endecaResultList,
				correlationId);
		return endecaResultList;
	}

	/**
	 * Method to populate the endeca result list either by left nav cartridge or
	 * by complete dimension list.
	 * 
	 * @param endecaResultList
	 *            the endeca result list
	 * @param correlationId
	 *            to track the request
	 * @param contentItem
	 *            the ContentItem object
	 * @param navigation
	 *            the Navigation object
	 * @return a list of maps representing the endeca result list.
	 */
	private List<Map<String, String>> populateEndecaResultList(
			final List<Map<String, String>> endecaResultList,
			final String correlationId,
			final ContentItem contentItem, final Navigation navigation) {
		// Left Navigation results are fetched from the left Column property of content item.
		List<Map<String, String>> endecaResultListLocal = endecaResultList;
		List leftColumnCartridgeList = null;
		if (null != contentItem && null != contentItem.getProperty(EndecaConstants.LEFT_COLUMN)) {
			leftColumnCartridgeList = (List) contentItem.getProperty(EndecaConstants.LEFT_COLUMN).getValue();
			for (int i = 0; i < leftColumnCartridgeList.size(); i++) {
				final ContentItem contItem = (ContentItem) leftColumnCartridgeList.get(i);
				if (contItem != null) {
					endecaResultListLocal = this.processLeftNavCartridge(
							endecaResultList, contItem, correlationId);
				}
			}
		}	else {
			endecaResultListLocal = this.populateCompleteDimensionList(
					endecaResultList, navigation, correlationId);
		}
		return endecaResultListLocal;
	}

	/** Method to return a NavigationRecords object from the ContentItem object.
	 * @param contentItem the content item object
	 * @return a processed NavigationRecords object
	 */
	private NavigationRecords getNavigationRecords(
			final ContentItem contentItem) {
		NavigationRecords navigationRecords = null;
		if (null != contentItem && null != contentItem.getProperty(EndecaConstants.CENTER_COLUMN)) {

			final List centerColumnCartridgeList =
					(List) contentItem.getProperty(EndecaConstants.CENTER_COLUMN).getValue();
			if (null != centerColumnCartridgeList && !centerColumnCartridgeList.isEmpty()) {
				final ContentItem recordsCartridge = (ContentItem) centerColumnCartridgeList.get(0);
				/*if RecordsBNB or RecordsBNBNoSort exists, use Navigation_records
				 * from the records cartridge in center column*/
				if (EndecaConstants.RECORDS_BNB.equals(recordsCartridge.getTemplateId())
						|| EndecaConstants.RECORDS_BNB_NOSORT.equals(recordsCartridge.getTemplateId())) {
					if (null != recordsCartridge.getProperty(EndecaConstants.NAVIGATION_RECORDS)) {
						navigationRecords = (NavigationRecords) recordsCartridge
								.getProperty(EndecaConstants.NAVIGATION_RECORDS).getValue();
					} else {
						navigationRecords = null;
					}

				}
			}
		}
		return navigationRecords;
	}

	/**
	 * This method will create a List of map that contain the structure for
	 * each of the left Nav catrides.
	 * @param endecaResultList
	 * 		Endeca Result List
	 * @param contItem
	 * 		The current Item(cartridge) to be processed.
	 * @param correlationId
	 * 		to track the request
	 * @return a list of maps representing the processed left nav cartridge
	 */
	public final List<Map<String, String>> processLeftNavCartridge(final List<Map<String, String>> endecaResultList,
			final ContentItem contItem, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		DimensionList refinementDimensions = null;
		if (null != contItem.getProperty(EndecaConstants.REFINEMENTS)) {
			refinementDimensions = (DimensionList) contItem.getProperty(EndecaConstants.REFINEMENTS).getValue();
		}
		if (null != refinementDimensions) {
			for (int i = 0; i < refinementDimensions.size(); i++) {
				final Dimension currentDim = (Dimension) refinementDimensions.get(i);
				final Map<String, String> dimensionMap = new HashMap<String, String>();
				String facetName = null;
				if (contItem.getProperty(EndecaConstants.FACETDISPLAY_LABEL) != null 
						&& !contItem.getProperty(EndecaConstants.FACETDISPLAY_LABEL).getValue().
						equals(CommonConstants.EMPTY_STRING)) {
					facetName = (String) contItem.getProperty(EndecaConstants.FACETDISPLAY_LABEL).getValue();
				} else {
					facetName = currentDim.getName();
				}
				dimensionMap.put(DomainConstants.DIMENSION_LABEL,
						facetName);
				final long facetId = currentDim.getId();
				dimensionMap.put(DomainConstants.DIMENSION_KEY,
						String.valueOf(facetId));
				this.populateMultiSelectForDimension(dimensionMap, currentDim, correlationId);
				final DimValList dimensionValList = currentDim.getRefinements();
				final Iterator<DimVal> dimListIterator = dimensionValList.iterator();
				StringBuilder refinementString = new StringBuilder();
				while (dimListIterator.hasNext()) {
					refinementString = this.getRefinementString(refinementString, dimListIterator, correlationId);
				}
				dimensionMap
				.put(DomainConstants.REFINEMENTS,
						refinementString.toString().substring(0,
								refinementString.length() - 1));
				endecaResultList.add(dimensionMap);
			}
		}
		LOGGER.debug("EndecaResultList from processLeftNavCartridge :" + endecaResultList, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return endecaResultList;
	}


	/**
	 * The method to populate the various dimensions alongwith refinements as
	 * obtained from the Endeca response.
	 * 
	 * @param dimensionList
	 *            Reference of the list to which the dimensions are to be added
	 * @param navigation
	 *            Navigation object as obtained from the Endeca response
	 * @param correlationId
	 *            to track the request
	 * @return The list appended with the dimension values
	 */
	public final List<Map<String, String>> populateCompleteDimensionList(
			final List<Map<String, String>> dimensionList,
			final Navigation navigation, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final DimensionList dimensions = navigation.getRefinementDimensions();

		for (Object dim : dimensions) {
			// to store the dimensions in the form of map.
			Map<String, String> dimensionMap = new LinkedHashMap<String, String>();
			// single dimension from the dimension list.
			final Dimension dimension = (Dimension) dim;

			dimensionMap.put(DomainConstants.DIMENSION_KEY,
					String.valueOf(dimension.getId()));
			dimensionMap.put(DomainConstants.DIMENSION_LABEL,
					dimension.getName());
			// populates multi select fields corresponding to all the
			// dimensions.
			this.populateMultiSelectForDimension(dimensionMap, dimension,
					correlationId);
			final DimValList refinementList = dimension.getRefinements();
			StringBuilder refinementString = new StringBuilder();
			if (!refinementList.isEmpty()) {
				final Iterator refinementIterator = refinementList.iterator();

				while (refinementIterator.hasNext()) {
					// Added this method to avoid code complexity
					refinementString = this
							.getRefinementString(refinementString,
									refinementIterator, correlationId);

				}
				dimensionMap
				.put(DomainConstants.REFINEMENTS,
						refinementString.toString().substring(0,
								refinementString.length() - 1));

			}
			dimensionList.add(dimensionMap);
			dimensionMap = null;
		}
		LOGGER.debug("dimensionList " + dimensionList, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return dimensionList;
	}

	/**
	 * Method is written to avoid the checkstyle issue.
	 * 
	 * @param refinementString
	 *            refinementString
	 * @param refinementIterator
	 *            iterator
	 * @param correlationId
	 *            For tracking the request
	 * @return refinedString
	 * 
	 */

	private StringBuilder getRefinementString(
			final StringBuilder refinementString,
			final Iterator refinementIterator, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final DimVal refinement = (DimVal) refinementIterator.next();
		final StringBuilder refinedString;
		int refinementCount = 0;
		refinementString.append(DomainConstants.REFINEMENT_KEY);

		refinementString.append(CommonConstants.FIELD_PAIR_MAPPER_SEPARATOR);

		refinementString.append(refinement.getId());

		refinementString.append(CommonConstants.TILDE_SEPERATOR);

		refinementString.append(DomainConstants.DIMENSION_LABEL);
		refinementString.append(CommonConstants.FIELD_PAIR_MAPPER_SEPARATOR);
		refinementString.append(refinement.getName());
		refinementString.append(CommonConstants.TILDE_SEPERATOR);

		if (refinement.getProperties().get(EndecaConstants.DIM_REFINMENT_COUNT) != null) {
			refinementCount = Integer.parseInt((String) refinement
					.getProperties().get(EndecaConstants.DIM_REFINMENT_COUNT));
		}
		refinementString.append(DomainConstants.RECORD_COUNT);
		refinementString.append(CommonConstants.FIELD_PAIR_MAPPER_SEPARATOR);
		refinementString.append(refinementCount);
		refinedString = refinementString.append(CommonConstants.PIPE_SEPERATOR);
		//Logger is removed as this method is running in a loop and was printing all the refinement data to the logs.
		LOGGER.logMethodExit(startTime, correlationId);
		return refinedString;
	}

	/**
	 * This method populates multi select fields correponding to all the
	 * dimensions.
	 * 
	 * @param dimensionMap
	 *            - The Map with the dimension values
	 * @param dimension
	 *            - dimension object from endeca
	 * @param correlationId
	 *            For tracking the request
	 */
	private void populateMultiSelectForDimension(
			final Map<String, String> dimensionMap, final Dimension dimension,
			final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		String multiSelectOperation = null;
		String multiSelectEnabled = CommonConstants.FALSE_VALUE;
		final boolean multiSelectAnd = dimension.getRefinementParent()
				.isMultiSelectAnd();
		final boolean multiSelectOr = dimension.getRefinementParent()
				.isMultiSelectOr();
		if (multiSelectAnd || multiSelectOr) {
			multiSelectEnabled = CommonConstants.TRUE_VALUE;
		}
		if (multiSelectAnd) {
			multiSelectOperation = CommonConstants.AND_VALUE;
		} else if (multiSelectOr) {
			multiSelectOperation = CommonConstants.OR_VALUE;
		}
		dimensionMap.put(DomainConstants.MULTI_SELECT_ENABLED,
				multiSelectEnabled);
		if (CommonConstants.TRUE_VALUE.equals(multiSelectEnabled)) {
			dimensionMap.put(DomainConstants.MULTI_SELECT_OPERATION,
					multiSelectOperation);
		}
		LOGGER.debug("dimensionMap is" + dimensionMap, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * The method to populate the various dimensions without refinements as
	 * obtained from the Endeca response.
	 * 
	 * @param requestMap
	 *            map
	 * @param dimensionList
	 *            Reference of the list to which the dimensions are to be added
	 * @param nav
	 *            Navigation object as obtained from the Endeca response
	 * @param correlationId
	 *            to track the request
	 * @return The list appended with the dimension values
	 * @throws AdapterException
	 *             The exception thrown from this method
	 */
	public final List<Map<String, String>> populateDescriptorDimensionList(
			final Map<String, String> requestMap,
			final List<Map<String, String>> dimensionList,
			final Navigation nav, final String correlationId)
					throws AdapterException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final DimensionList dimensions = nav.getDescriptorDimensions();
		final List<Map<String, String>> endecaDimensionList = new ArrayList<Map<String, String>>();
		if (!dimensions.isEmpty()) {
			for (Object dim : dimensions) {
				Map<String, String> dimensionMap = new LinkedHashMap<String, String>();
				final Dimension dimension = (Dimension) dim;
				final DimVal dimensionDescriptor = dimension.getDescriptor();
				// dimension key is set into Map.
				dimensionMap.put(DomainConstants.DIMENSION_KEY,
						String.valueOf(dimensionDescriptor.getId()));
				// dimension value is set into Map.
				dimensionMap.put(DomainConstants.DIMENSION_LABEL,
						dimensionDescriptor.getName());

				final int dimSize = dimension.getAncestors().size();
				if (dimSize > 0) {
					dimensionMap.put(
							DomainConstants.PARENT_DIMENSION_KEY,
							String.valueOf(dimension.getAncestors()
									.getDimValue(dimSize - 1).getId()));
				} else {
					final String dimensionName = String.valueOf(dimension
							.getName());
					// Validating the dimension name
					if (dimensionName
							.equals(EndecaConstants.CATEGORY_DIMENSION)) {
						dimensionMap.put(DomainConstants.PARENT_DIMENSION_KEY,
								String.valueOf(dimension.getId()));
					} else {
						return endecaDimensionList;
					}
				}
				/*
				 * Refinements are checked and value is assigned yes or no based
				 * on number of refinements present.
				 */
				if (!dimension.getRefinements().isEmpty()) {
					dimensionMap.put(DomainConstants.HAS_FURTHER_REFINEMENTS,
							CommonConstants.YES_VALUE);
					LOGGER.info("CommonConstants.HAS_FURTHER_REFINEMENTS"
							+ CommonConstants.YES_VALUE, correlationId);
				} else {
					dimensionMap.put(DomainConstants.HAS_FURTHER_REFINEMENTS,
							CommonConstants.NO_VALUE);
					LOGGER.info("CommonConstants.HAS_FURTHER_REFINEMENTS"
							+ CommonConstants.NO_VALUE, correlationId);
				}
				// To obtain the category details dim value and refinements is
				// checked.
				if (CommonConstants.TRUE_VALUE.equalsIgnoreCase(requestMap
						.get(EndecaConstants.DIM))
						&& requestMap
						.containsKey(EndecaConstants.REFINEMENTS_REQUIRED_LEVEL)) {
					this.populateRefinementList(dimension, dimensionMap,
							correlationId);
				}
				dimensionList.add(dimensionMap);
				dimensionMap = null;
			}
		} else {
			return endecaDimensionList;
		}
		LOGGER.debug("dimensionList is" + dimensionList, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return dimensionList;
	}

	/**
	 * The method to populate the various dimension refinements as obtained from
	 * the Endeca response.
	 * 
	 * @param dimension
	 *            Reference to the endeca dimension
	 * @param dimensionMap
	 *            The Map with the dimension values
	 * @param correlationId
	 *            to track the request
	 */
	private void populateRefinementList(final Dimension dimension,
			final Map<String, String> dimensionMap, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" dimensionMap : " + dimensionMap, correlationId);
		final DimValList refinementList = dimension.getRefinements();
		final StringBuilder refinementString = new StringBuilder();
		// If refinement list size is greater than zero, sub category details
		// are obtained.
		if (!refinementList.isEmpty()) {
			final Iterator refinementIterator = refinementList.iterator();

			while (refinementIterator.hasNext()) {
				this.getRefinementForCompleteDimension(dimension,
						refinementString, refinementIterator, correlationId);

			}
			// Each refinement is placed into map.
			dimensionMap.put(DomainConstants.REFINEMENTS, refinementString
					.toString().substring(0, refinementString.length() - 1));

		}
		LOGGER.debug("dimensionMap " + dimensionMap, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method is extracted to avoid cyclomatic code complexity.
	 * 
	 * @param dimension
	 *            dimension
	 * @param refinementString
	 *            refinementString
	 * @param refinementIterator
	 *            refinementIterator
	 * @param correlationId
	 *            For tracking the request
	 */
	private void getRefinementForCompleteDimension(final Dimension dimension,
			final StringBuilder refinementString,
			final Iterator refinementIterator, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final DimVal refinement = (DimVal) refinementIterator.next();
		int refinementCount = 0;
		// String is formed with tilde separator of fields.
		refinementString.append(DomainConstants.REFINEMENT_KEY);
		refinementString.append(CommonConstants.FIELD_PAIR_MAPPER_SEPARATOR);
		refinementString.append(refinement.getId());
		refinementString.append(CommonConstants.TILDE_SEPERATOR);

		refinementString.append(DomainConstants.DIMENSION_LABEL);
		refinementString.append(CommonConstants.FIELD_PAIR_MAPPER_SEPARATOR);
		refinementString.append(refinement.getName());
		refinementString.append(CommonConstants.TILDE_SEPERATOR);

		if (refinement.getProperties().get(EndecaConstants.DIM_REFINMENT_COUNT) != null) {
			refinementCount = Integer.parseInt((String) refinement
					.getProperties().get(EndecaConstants.DIM_REFINMENT_COUNT));
		}

		refinementString.append(DomainConstants.PARENT_DIMENSION_KEY);
		refinementString.append(CommonConstants.FIELD_PAIR_MAPPER_SEPARATOR);
		refinementString.append(String.valueOf(dimension.getDescriptor()
				.getId()));
		refinementString.append(CommonConstants.TILDE_SEPERATOR);

		if (refinement.isLeaf()) {
			refinementString.append(DomainConstants.HAS_FURTHER_REFINEMENTS);
			refinementString
			.append(CommonConstants.FIELD_PAIR_MAPPER_SEPARATOR);
			refinementString.append(CommonConstants.NO_VALUE);
			refinementString.append(CommonConstants.TILDE_SEPERATOR);

		} else {
			refinementString.append(DomainConstants.HAS_FURTHER_REFINEMENTS);
			refinementString
			.append(CommonConstants.FIELD_PAIR_MAPPER_SEPARATOR);
			refinementString.append(CommonConstants.YES_VALUE);
			refinementString.append(CommonConstants.TILDE_SEPERATOR);
		}

		refinementString.append(DomainConstants.RECORD_COUNT);
		refinementString.append(CommonConstants.FIELD_PAIR_MAPPER_SEPARATOR);
		refinementString.append(refinementCount);
		refinementString.append(CommonConstants.PIPE_SEPERATOR);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * Method to read the individual record's properties and populate them as a
	 * Map per product in the final product list.
	 * 
	 * @param productList
	 *            The final list of the product maps
	 * @param aggrList
	 *            Aggregate List object as obtained from the Endeca response
	 * @param correlationId
	 *            to track the request
	 * 
	 * @throws AdapterException
	 *             The exception thrown from this method
	 * @return productList List of products
	 */
	private List<Map<String, String>> populateProductsList(
			final List<Map<String, String>> productList, final AggrERecList aggrList,
			final String correlationId) throws AdapterException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		// Map is to store all aggregate and properties of product.
		Map<String, String> endecaMap;
		// This contains list of aggregate values.
		for (Object aggr : aggrList) {
			final AggrERec aggERec = (AggrERec) aggr;
			final ERecList eRecList = aggERec.getERecs();
			for (Object eRec : eRecList) {
				final ERec eRecObj = (ERec) eRec;

				final PropertyMap prosMapERec = eRecObj.getProperties();
				endecaMap = new LinkedHashMap<String, String>();
				endecaMap.putAll(prosMapERec);
				// Method to get multiple value for one endeca key
				this.getMultipleEndecaValues(prosMapERec, endecaMap,
						correlationId);
				endecaMap.putAll(aggERec.getProperties());
				//The following code maps the response as per the generic keys.
				endecaMap = AdapterUtil.mapResponseToGenericKeys(endecaMap,
						this.endecaResponseFieldListLoader.getEndecaResponseFieldListPropertiesMap(),
						correlationId);
				productList.add(endecaMap);
			}
		}
		LOGGER.debug("ProductList from endeca " + productList, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return productList;
	}

	/**
	 * Method to get multiple product paths,product colors,product saleprice and
	 * sku saleprice for a product.
	 * 
	 * @param propertiesMapERec
	 *            is endeca response
	 * @param endecaMap
	 *            this is a map consists same endeca response
	 * @param correlationId
	 *            id to track this request
	 */
	private void getMultipleEndecaValues(final PropertyMap propertiesMapERec,
			final Map<String, String> endecaMap, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		// pathList stores the paths of the product.
		final List<String> pathList = new ArrayList<String>();
		// colorList stores the list of colors available for particular product.
		final List<String> colorList = new ArrayList<String>();
		// salePriceList is list of sale price values for product.
		final List<String> salePriceList = new ArrayList<String>();
		// skuSalepriceList is the list of sale price for skus.
		final List<String> skuSalepriceList = new ArrayList<String>();
		// propertiesMapReciterator is Iterator for properties.
		final Iterator propertiesMapRecordIterator = propertiesMapERec
				.entrySet().iterator();
		// iterate over the properties and store the properties of product into
		// list.
		while (propertiesMapRecordIterator.hasNext()) {

			final Map.Entry propertiesRecMapEntry = (Map.Entry) propertiesMapRecordIterator
					.next();
			if (propertiesRecMapEntry.getKey().equals(EndecaConstants.PATH)) {
				pathList.add((String) propertiesRecMapEntry.getValue());
			}
			if (propertiesRecMapEntry.getKey().equals(EndecaConstants.COLOR)) {
				colorList.add((String) propertiesRecMapEntry.getValue());
			}
			if (propertiesRecMapEntry.getKey().equals(
					EndecaConstants.PRODUCT_SALE_PRICE)) {
				salePriceList.add((String) propertiesRecMapEntry.getValue());
			}
			if (propertiesRecMapEntry.getKey().equals(
					EndecaConstants.FILTER_PRICE)) {
				skuSalepriceList.add((String) propertiesRecMapEntry.getValue());
			}
		}
		LOGGER.debug("endecaMap after iterating through multiple P_paths: "
				+ endecaMap, correlationId);
		endecaMap.put(EndecaConstants.PATH,
				StringUtils.collectionToCommaDelimitedString(pathList));
		endecaMap.put(EndecaConstants.COLOR,
				StringUtils.collectionToCommaDelimitedString(colorList));
		endecaMap.put(EndecaConstants.PRODUCT_SALE_PRICE,
				StringUtils.collectionToCommaDelimitedString(salePriceList));
		endecaMap.put(EndecaConstants.FILTER_PRICE,
				StringUtils.collectionToCommaDelimitedString(skuSalepriceList));
		LOGGER.debug("endecaMap from endeca " + endecaMap, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}
}
