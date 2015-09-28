package com.belk.api.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.belk.api.constants.CommonConstants;
import com.belk.api.constants.EndecaConstants;
import com.belk.api.constants.ErrorConstants;
import com.belk.api.constants.RequestAttributeConstant;
import com.belk.api.endeca.DimensionTree;
import com.belk.api.endeca.EndecaRequestContext;
import com.belk.api.exception.AdapterException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.model.Dimension;
import com.endeca.navigation.DimLocation;
import com.endeca.navigation.DimLocationList;
import com.endeca.navigation.DimVal;
import com.endeca.navigation.DimValIdList;
import com.endeca.navigation.DimValList;
import com.endeca.navigation.DimensionSearchResult;
import com.endeca.navigation.DimensionSearchResultGroup;
import com.endeca.navigation.DimensionSearchResultGroupList;
import com.endeca.navigation.ENEQuery;
import com.endeca.navigation.ENEQueryException;
import com.endeca.navigation.ENEQueryResults;
import com.endeca.navigation.ERecSearch;
import com.endeca.navigation.ERecSearchList;
import com.endeca.navigation.FieldList;
import com.endeca.navigation.HttpENEConnection;
import com.endeca.navigation.UrlENEQuery;
import com.endeca.navigation.UrlENEQueryParseException;

/**
 * The common Endeca utility class for building the Endeca query. The class
 * exposes a public method to accept the request parameters and internally
 * builds an Endeca query using them.
 * Updated : Added few comments and changed the name of variables as part of Phase2, April,14 release
 * Update: This class has been modified to meet the requirement of reading the
 * hasSubcategories flag for a dimension not from the configured parameters of
 * Endeca but as per the dimension tree's structure.
 * 
 * Update: Segregation of Constants as per code review comments
 * 
 * @author Mindtree
 * @date Aug 22, 2013
 */
@Service
public class EndecaUtil {

	/**
	 * creating instance of logger.
	 */
	// this will return a logger instance
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	/**
	 * creating instance of Endeca Filed List Loader class.
	 */
	@Autowired
	EndecaFieldListLoader endecaFieldListLoader;

	/**
	 * creating instance of Endeca Request List Loader class.
	 */
	@Autowired
	EndecaRequestListLoader endecaRequestListLoader;
	/**
	 * creating instance of Endeca Loader class.
	 */
	@Autowired
	EndecaLoader endecaLoader;

	/**
	 * creating instance of Error Loader class.
	 */
	@Autowired
	ErrorLoader errorLoader;

	/**
	 * creating instance of dimension tree class.
	 */
	@Autowired
	DimensionTree dimensionTree;

	/**
	 * Default Constructor.
	 */
	public EndecaUtil() {
		// TODO Auto-generated constructor stub
	}



	/**
	 * @param endecaRequestListLoader the endecaRequestListLoader to set
	 */
	public final void setEndecaRequestListLoader(
			final EndecaRequestListLoader endecaRequestListLoader) {
		this.endecaRequestListLoader = endecaRequestListLoader;
	}


	/**
	 * @param dimensionTree
	 *            the dimensionTree to set
	 */
	public final void setDimensionTree(final DimensionTree dimensionTree) {
		this.dimensionTree = dimensionTree;
	}


	/**
	 * setter method for the endecaFieldListLoader.
	 * 
	 * @param endecaFieldListLoader the endecaFieldListLoader to set
	 */
	public final void setEndecaFieldListLoader(final EndecaFieldListLoader endecaFieldListLoader) {
		this.endecaFieldListLoader = endecaFieldListLoader;
	}

	/**
	 * setter method for the endecaLoader.
	 * 
	 * @param endecaLoader the endecaLoader to set
	 */
	public final void setEndecaLoader(final EndecaLoader endecaLoader) {
		this.endecaLoader = endecaLoader;
	}

	/**
	 * setter method for the errorLoader.
	 * 
	 * @param errorLoader the errorLoader to set
	 */
	public final void setErrorLoader(final ErrorLoader errorLoader) {
		this.errorLoader = errorLoader;
	}

	/**
	 * The method to create the Endeca query from the request which contains the
	 * input parameters as key value pairs in a map.
	 * 
	 * @param endecaContext
	 *            the context object containing the request parameters
	 * @param correlationId
	 *            tracking Id
	 * @return the ENEQuery object to be used to query Endeca
	 * @throws AdapterException
	 *             The general exception from Adapter layer if it occurs
	 * @throws UrlENEQueryParseException
	 *             throws if any Parsing errors happen on UrlEnEQuery
	 */
	public final ENEQuery buildRequest(
			final EndecaRequestContext endecaContext,
			final String correlationId) throws AdapterException,
			UrlENEQueryParseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final Map<String, String> endecaRequestMap = endecaContext.getEndecaRequestMap();
		LOGGER.info("Request Parameters are:" + endecaRequestMap, correlationId);
		final Map<String, String> endecaPropertiesMap = this.endecaLoader
				.getEndecaPropertiesMap();
		final Map<String, String> errorPropertiesMap = this.errorLoader
				.getErrorPropertiesMap();
		String rootDimValue = endecaPropertiesMap.get(EndecaConstants.ENDECA_DEAFULT_ROOTDIM);
		long offsetValue = Long.parseLong(endecaPropertiesMap
				.get(EndecaConstants.ENDECA_DEAFULT_OFFSET));
		int limitValue = Integer.parseInt(endecaPropertiesMap
				.get(EndecaConstants.ENDECA_DEAFULT_LIMIT));
		int navErecsPerAggrERec = EndecaConstants.DEFAULT_NAV_EREC_PER_AGGR_EREC;

		try {
			// if N value is passed with the request then assign the value
			if (endecaRequestMap.get(EndecaConstants.CATEGORY_ID) != null) {
				rootDimValue = endecaRequestMap
						.get(EndecaConstants.CATEGORY_ID);
			}
			// if offset value is passed with the request then assign the value
			//else take the default value
			if (endecaRequestMap.get(EndecaConstants.OFFSET) != null) {
				offsetValue = Long.parseLong(endecaRequestMap
						.get(EndecaConstants.OFFSET));
			}
			// if limit value is passed with the request then assign the value
			//else take the default value
			if (endecaRequestMap.get(EndecaConstants.LIMIT) != null) {
				limitValue = Integer.parseInt(endecaRequestMap
						.get(EndecaConstants.LIMIT));
			}
			// if naverecs value is passed with the request then assign the value
			//else take the default value
			if (endecaRequestMap
					.containsKey(EndecaConstants.NAVERECS_PER_AGGREREC)
					&& endecaRequestMap
					.get(EndecaConstants.NAVERECS_PER_AGGREREC) != null) {
				navErecsPerAggrERec = Integer.parseInt(endecaRequestMap
						.get(EndecaConstants.NAVERECS_PER_AGGREREC));
				endecaRequestMap.remove(EndecaConstants.NAVERECS_PER_AGGREREC);
			}
			// Key 'Refinements required' is removed from request object to
			// since not required for endeca query.
			if (endecaRequestMap
					.containsKey(EndecaConstants.REFINEMENTS_REQUIRED)) {
				endecaRequestMap.remove(EndecaConstants.REFINEMENTS_REQUIRED);
			}
			// Key 'Refinements level' is removed from request object to
			// since not required for endeca query.
			if (endecaRequestMap
					.containsKey(EndecaConstants.REFINEMENTS_REQUIRED_LEVEL)) {
				endecaRequestMap
				.remove(EndecaConstants.REFINEMENTS_REQUIRED_LEVEL);
			}
			endecaContext.setEndecaRequestMap(endecaRequestMap);
			//prepare the query for endeca as per request.
			return this.prepareEndecaQuery(endecaContext, correlationId, 
					rootDimValue, offsetValue, limitValue,
					navErecsPerAggrERec);
		} catch (NumberFormatException e) {
			LOGGER.error(e, correlationId);
			throw new AdapterException(
					String.valueOf(ErrorConstants.ERROR_CODE_11422),
					errorPropertiesMap.get(String.valueOf(ErrorConstants.ERROR_CODE_11422)),
					ErrorConstants.HTTP_STATUS_CODE_INVALID_URL_PARAMETERS,
					correlationId);
		}
	}



	/**Method to prepare the Endeca query object from the request parameters.
	 * @param endecaContext The context object containing the request parameters
	 * @param correlationId to track the request
	 * @param rootDimValue the root dimension value
	 * @param offsetValue the offset value
	 * @param limitValue the limit value
	 * @param navErecsPerAggrERec the number of records per aggregated record to be fetched
	 * @return the processed endeca query object
	 * @throws UrlENEQueryParseException the exception thrown from this method if an error occurs while creating the UrlENEQuery object
	 * @throws AdapterException the general exception thrown from this method
	 */
	private ENEQuery prepareEndecaQuery(
			final EndecaRequestContext endecaContext,
			final String correlationId,
			final String rootDimValue,
			final long offsetValue, final int limitValue, final int navErecsPerAggrERec)
			throws UrlENEQueryParseException, AdapterException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final UrlENEQuery endecaQuery = new UrlENEQuery(
				EndecaConstants.CATEGORY_ID
				+ CommonConstants.EQUALS_SIGN
				+ rootDimValue.replace(
						CommonConstants.COMMA_SEPERATOR,
						CommonConstants.EMPTY_VALUE),
						CommonConstants.UTF_TYPE);
		endecaQuery.setNavAggrERecsOffset(offsetValue);
		endecaQuery.setNavNumAggrERecs(limitValue);
		//The method is to populate the search List in the search query based on request parameters.
		
		this.createSearchList(endecaQuery, endecaContext, correlationId);
		endecaQuery.setNavRollupKey(EndecaConstants.ROLL_UP_KEY);
		/*NavERecsPerAggrERec value is set to 1 or 2.
		 When the value is set to 1 then single representative record is returned with each aggregate record.
		 When the value is set to 2 then all records(sku's) are returned with each aggregated record.*/
		endecaQuery.setNavERecsPerAggrERec(navErecsPerAggrERec);
		LOGGER.logMethodExit(startTime, correlationId);
		LOGGER.info("return value of ENEQuery is : " + endecaQuery, correlationId);
		return endecaQuery;
	}

	/**
	 * The method to populate the search List in the search query based on
	 * request parameters.
	 * 
	 * @param query
	 *            The ENEQuery reference passed from buildRequest method
	 * @param endecaContext
	 *            The context object containing the request parameters
	 * @param correlationId
	 *            tracking Id
	 * @throws AdapterException
	 *             The general exception thrown from Adapter layer.
	 * @throws UrlENEQueryParseException
	 *             Throws UrlENEQueryParseException
	 */
	private void createSearchList(final UrlENEQuery query,
			final EndecaRequestContext endecaContext,
			final String correlationId) throws AdapterException,
			UrlENEQueryParseException {
		final Map<String, String> endecaPropertiesMap = this.endecaLoader.getEndecaPropertiesMap();
		final Map<String, String> endecaRequestMap = endecaContext.getEndecaRequestMap();
		final Iterator<Map.Entry<String, String>> endecaRequestIterator = endecaRequestMap
				.entrySet().iterator();
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug(" query :" + query + ", map : " + endecaRequestIterator, correlationId);

		String searchKey;
		String searchTerms;
		//sets the search query for endeca.
		final ERecSearchList searches = new ERecSearchList();
		while (endecaRequestIterator.hasNext()) {
			final Entry<String, String> endecaRequestentry = endecaRequestIterator.next();
			searchKey = endecaRequestentry.getKey();
			searchTerms = endecaRequestentry.getValue();
			if (searchTerms.contains(CommonConstants.COMMA_SEPERATOR)) {
				searchTerms = searchTerms.replace(
						CommonConstants.COMMA_SEPERATOR,
						CommonConstants.EMPTY_VALUE);
			}
			this.processBySearchKey(query, correlationId, endecaPropertiesMap,
					searchKey, searchTerms, searches);
			// If price range exists in the request, set it in the query
			this.checkFilterPriceRange(query, searchKey, searchTerms, correlationId);
		}
		query.setSelection(this.populateFieldList(endecaContext.getOptionNodes(), correlationId));
		if (!searches.isEmpty()) {
			query.setNavERecSearches(searches);
			if (endecaContext.getUriRequestMap() != null 
					&& endecaContext.getUriRequestMap().get(RequestAttributeConstant.BOOSTED_RECORDS) != null 
					&& endecaContext.getUriRequestMap().get(RequestAttributeConstant.BOOSTED_RECORDS)
					.equals(CommonConstants.TRUE_VALUE)) {
				query.setNr(EndecaConstants.NR_FOR_SEARCH_PRIMARY);	
			}
		}
		LOGGER.debug("query is" + query, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}



	/**Method to process the query/search objects based on the search keys from request.
	 * @param query The query object
	 * @param correlationId to track the request
	 * @param endecaPropertiesMap the endeca properties map
	 * @param searchKey the search key 
	 * @param searchTerms the search terms
	 * @param searches the search object
	 */
	private void processBySearchKey(final UrlENEQuery query,
			final String correlationId,
			final Map<String, String> endecaPropertiesMap, final String searchKey,
			final String searchTerms, final ERecSearchList searches) {
		ERecSearch eRecSearch;
		if (EndecaConstants.DIM.equalsIgnoreCase(searchKey)) {
			if (CommonConstants.TRUE_VALUE.equalsIgnoreCase(searchTerms)) {
				query.setNavAllRefinements(true);
			}
		} else if (EndecaConstants.SORT.equalsIgnoreCase(searchKey)) {
			query.setNs(this.getSortKeys(searchTerms, correlationId));
		} else if (!EndecaConstants.CATEGORY_ID.equalsIgnoreCase(searchKey)
				&& !EndecaConstants.OFFSET.equalsIgnoreCase(searchKey)
				&& !EndecaConstants.LIMIT.equalsIgnoreCase(searchKey)
				&& !EndecaConstants.FILTER_PRICE
				.equalsIgnoreCase(searchKey)) {
			eRecSearch = new ERecSearch(searchKey, searchTerms,
					endecaPropertiesMap.get(EndecaConstants.MATCH_MODE));
			searches.add(eRecSearch);
		}
	}

	/**
	 * Method to get a list of all sort parameters.
	 * 
	 * @param sortField
	 *            Pipe-delimited list of sort parameters
	 * @param correlationId
	 *            tracking Id
	 * @return eRecSortKeyList The list of sort parameters for the ENEQuery
	 */
	private String getSortKeys(final String sortField,
			final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("sortField :" + sortField, correlationId);
		String sortKey = null;
		String sortParameter = null;
		final StringBuilder sortedKeyList = new StringBuilder();
		final StringTokenizer stringTokenizer = new StringTokenizer(sortField,
				CommonConstants.PIPE_SEPERATOR);
		while (stringTokenizer.hasMoreTokens()) {
			boolean ascendingFlag = true;
			sortParameter = stringTokenizer.nextToken();
			if (sortParameter.startsWith(CommonConstants.EIPHEN_SEPERATOR)) {
				ascendingFlag = false;
				sortParameter = sortParameter.replace(
						CommonConstants.EIPHEN_SEPERATOR, "");
			}
			//Maps sort keys with endeca keys
			sortKey = this.endecaSortKeyMapper(sortParameter, correlationId);
			sortedKeyList.append(this.checkSortKey(sortKey, ascendingFlag, correlationId));

		}
		LOGGER.logMethodExit(startTime, correlationId);
		LOGGER.debug("Return value of sortedKeyList is :" + sortedKeyList,
				correlationId);
		return sortedKeyList.substring(0, sortedKeyList.length() - 2)
				.toString();

	}

	/**
	 * The method to map the sort key parameters to the equivalent Endeca keys.
	 * 
	 * @param sortParameter
	 *            The parameter to be mapped to the equivalent Endeca parameter
	 * @param correlationId
	 *            tracking Id
	 * @return sortKey The equivalent Endeca key
	 */
	private String endecaSortKeyMapper(final String sortParameter,
			final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info(" sortParameter is :" + sortParameter, correlationId);
		String sortKey = null;
		if (sortParameter
				.equalsIgnoreCase(RequestAttributeConstant.PRODUCT_CODE)) {
			sortKey = EndecaConstants.PRODUCT_CODE;
		} else if (sortParameter
				.equalsIgnoreCase(RequestAttributeConstant.NAME)) {
			sortKey = EndecaConstants.NAME;
		} else if (sortParameter
				.equalsIgnoreCase(RequestAttributeConstant.LIST_PRICE)) {
			sortKey = EndecaConstants.LIST_PRICE;
		} else if (sortParameter
				.equalsIgnoreCase(RequestAttributeConstant.SALE_PRICE)) {
			sortKey = EndecaConstants.SALE_PRICE;
		} else if (sortParameter
				.equalsIgnoreCase(RequestAttributeConstant.INVENTORY)) {
			sortKey = EndecaConstants.INVENTORY;
		} else if (sortParameter
				.equalsIgnoreCase(RequestAttributeConstant.NEW_ARRIVAL)) {
			sortKey = EndecaConstants.NEW_ARRIVAL;
		} else if (sortParameter
				.equalsIgnoreCase(RequestAttributeConstant.BEST_SELLER)) {
			sortKey = EndecaConstants.BEST_SELLER;
		} else if (sortParameter
				.equalsIgnoreCase(RequestAttributeConstant.WISH_LIST_FAVORITES)) {
			sortKey = EndecaConstants.WISH_LIST_FAVORITES;
		} else if (sortParameter
				.equalsIgnoreCase(RequestAttributeConstant.TOP_RATING)) {
			sortKey = EndecaConstants.TOP_RATING;
		}
		LOGGER.debug("sortKey :" + sortKey, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return sortKey;
	}

	/**
	 * The method to append a sort parameter, along with its order, to the list
	 * of sort parameters for Endeca.
	 * 
	 * @param sortKey
	 *            The key to be added to the sort list
	 * @param ascendingFlag
	 *            The flag to check whether sort order is ascending or not
	 * @param correlationId
	 * 				For tracking the request
	 * @return returns sortedKey after formatting as required to create the
	 *         query
	 */
	private String checkSortKey(final String sortKey,
			final boolean ascendingFlag, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final StringBuilder sortedKey = new StringBuilder();
		String sortOrder = CommonConstants.ASC_SORT_ORDER;
		if (sortKey != null) {
			if (ascendingFlag == false) {
				sortOrder = CommonConstants.DESC_SORT_ORDER;
			}
			sortedKey.append(sortKey);
			sortedKey.append(CommonConstants.PIPE_SEPERATOR);
			sortedKey.append(sortOrder);
			sortedKey.append(CommonConstants.DOUBLE_PIPE_SEPERATOR);
		}
		LOGGER.debug("sortedKey :" + sortedKey, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return sortedKey.toString();
	}

	/**Method to convert a parent request object into child nodes
	 * @param nodeValue the parent object containing the child nodes' information
	 * @param endecaFieldListPropertiesMap the endeca field list properties map
	 * @param propertiesList the child properties
	 * @param correlationId to track the request
	 * @return a FieldList object
	 */
	private FieldList convertRequestParentToChildNodes(final String nodeValue, final Map<String, 
			String> endecaFieldListPropertiesMap, final FieldList propertiesList, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final List<String> nodes = Arrays.asList(nodeValue.split("\\s*,\\s*"));
		for (String node : nodes) {
			final String nodeMapping = endecaFieldListPropertiesMap.get(node);
			if (nodeMapping != null) {
				propertiesList.addField(nodeMapping);	
			}
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return propertiesList;
	}

	/**
	 * This method get the list of the all parameters required from the
	 * product details level in endeca.
	 * @param optionalNodes the list of optional nodes
	 * @param correlationId
	 *            tracking id
	 * @return FieldList the list of parameters from endeca.
	 * @throws AdapterException
	 *             The general exception thrown from Adapter layer.
	 */
	private FieldList populateFieldList(final Map<String, List<String>> optionalNodes, final String correlationId)
			throws AdapterException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		FieldList propertiesList = null;
		propertiesList = new FieldList();
		final Map<String, String> endecaFieldListPropertiesMap = this.endecaFieldListLoader
				.getEndecaFieldListpropertiesMap();
		if (optionalNodes != null && !optionalNodes.isEmpty()) {
			final Map<String, String> endecaRequestListPropertiesMap = this.endecaRequestListLoader
					.getEndecaRequestListpropertiesMap();
			propertiesList = this.convertRequestParentToChildNodes(
					endecaRequestListPropertiesMap.get(CommonConstants.ROOT_NODES),
					endecaFieldListPropertiesMap, propertiesList, correlationId);
			final List<String> nodes = optionalNodes.get(CommonConstants.OPTIONS);
			for (String node : nodes) {
				final String nodeValue = endecaRequestListPropertiesMap.get(node);
				if (nodeValue != null) {
					propertiesList = this.convertRequestParentToChildNodes(
							nodeValue, endecaFieldListPropertiesMap, propertiesList, correlationId);
				}
			}
		} else {
			for (Object key : endecaFieldListPropertiesMap.keySet()) {
				final String keyStr = key.toString();
				propertiesList.addField(endecaFieldListPropertiesMap.get(keyStr));
			}
		}

		LOGGER.debug("propertiesList :" + propertiesList, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return propertiesList;
	}

	/**
	 * This method fetches the dimension details from endeca and loads the
	 * dimension tree.
	 * 
	 * @param endecaRequestMap
	 *            the rquest map that would be contain the details for which the
	 *            dimensions are to be fetched.
	 * @param correlationId
	 *            tracking id
	 * @return Map having Long Dimension Id as key and Dimension objects as the
	 *         corresponding value
	 * @throws ENEQueryException
	 *             throws if there are any specific exceptions while querying
	 *             Endeca
	 * @throws AdapterException
	 *             The general exception thrown from Adapter layer.
	 */
	public final Map<Long, Dimension> loadDimensionTree(
			final Map<String, String> endecaRequestMap,
			final String correlationId) throws ENEQueryException,
			AdapterException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.debug("Request Parameters :" + endecaRequestMap, correlationId);
		final ENEQuery query = new ENEQuery();
		final Map<Long, Dimension> idRootDimensionMap = new LinkedHashMap<Long, Dimension>();
		query.setDimSearchDimension(new Long(endecaRequestMap
			.get(EndecaConstants.ROOT_DIMENSION_KEY)));
		final DimValIdList list = new DimValIdList();
		final String showProduct = endecaRequestMap
				.get(RequestAttributeConstant.SHOW_PRODUCT);
		LOGGER.debug("ShowProduct Value " + showProduct, correlationId);
		if (CommonConstants.TRUE_VALUE.equals(showProduct)) {
			this.dimensionTree.setProductsRequired(true);
		} else {
			this.dimensionTree.setProductsRequired(false);
		}
		for (Map.Entry<String, String> mapEntries : endecaRequestMap.entrySet()) {
			if (mapEntries.getKey().equalsIgnoreCase(
					RequestAttributeConstant.CATEGORY_ID)) {
				list.addDimValueId(Long.parseLong(mapEntries.getValue()));
				query.setDimSearchNavDescriptors(list);
			}
		}
		query.setDimSearchTerms(EndecaConstants.WILD_CARD_ALL);
		final ENEQueryResults results = this.queryEndeca(query, correlationId);
		LOGGER.debug("Results obtained by the Endeca query " + results,
				correlationId);
		final DimensionSearchResult dimensionSearchResult = results
				.getDimensionSearch();
		final DimensionSearchResultGroupList dimensionSearchResultGroupList = dimensionSearchResult
				.getResults();
		for (final Object dimensionSearchResultGroupElement : dimensionSearchResultGroupList) {
			this.fetchDimension(correlationId, idRootDimensionMap,
					dimensionSearchResultGroupElement);
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return idRootDimensionMap;
	}

	/**
	 * This method gets iterated for each of the
	 * dimensionSearchResultGroupElement to fetch the DimVal details and call.
	 * the create node method.
	 * 
	 * @param correlationId
	 *            tracking Id
	 * @param idRootDimensionMap
	 *            the map to which the complete Nodes are to be added
	 * @param dimensionSearchResultGroupElement
	 *            the dimensionSearch group is which has the group of elements
	 *            and which has to be iterated to get the dimension data
	 * @throws AdapterException
	 *             The general exception thrown from Adapter layer.
	 */
	private void fetchDimension(final String correlationId,
			final Map<Long, Dimension> idRootDimensionMap,
			final Object dimensionSearchResultGroupElement)
					throws AdapterException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final DimensionSearchResultGroup dimensionSearchResultGroup =
				(DimensionSearchResultGroup) dimensionSearchResultGroupElement;
		final DimValList roots = dimensionSearchResultGroup.getRoots();
		if (!roots.isEmpty()) {
			final DimVal root = (DimVal) roots.get(0);
			for (int i = 0; i < dimensionSearchResultGroup.size(); i++) {
				final DimLocationList dimLocationList = (DimLocationList) dimensionSearchResultGroup
						.get(i);
				for (final Object dimLocationElement : dimLocationList) {
					final DimLocation dimLocation = (DimLocation) dimLocationElement;
					final DimValList ancestors = dimLocation.getAncestors();
					final DimVal dimVal = dimLocation.getDimValue();
					this.dimensionTree.addNodes(root, ancestors, dimVal,
							idRootDimensionMap, correlationId);
				}
			}
		}
		/*
		 * Condition below used to differentiate between calls from Admin and
		 * Catalog APIs
		 * 
		 * The block below gets hold of the leafNodesMap created during the
		 * dimension tree creation phase and calls the nav query to populate the
		 * products for each leaf node, iteratively.
		 */
		if (this.dimensionTree.isProductsRequired()) {
			final Map<Long, Dimension> leafNodeMap = this.dimensionTree
					.getLeafNodesMap();
			final Set<Entry<Long, Dimension>> leafNodeSet = leafNodeMap
					.entrySet();
			final Iterator<Entry<Long, Dimension>> leafNodeIterator = leafNodeSet
					.iterator();
			while (leafNodeIterator.hasNext()) {
				this.dimensionTree.makeNavigationQuery(leafNodeIterator.next()
						.getValue(), correlationId);
			}
		}
		LOGGER.debug("root :" + roots, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
	}

	/**
	 * The method to execute the Endeca query.
	 * 
	 * @param urlENEQuery
	 *            The query object for Endeca.
	 * @param correlationId
	 *            tracking Id
	 * @return Results from Endeca
	 * @throws AdapterException
	 *             The general exception thrown from Adapter layer.
	 */
	private ENEQueryResults queryEndeca(final ENEQuery urlENEQuery,
			final String correlationId) throws AdapterException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final Map<String, String> errorPropertiesMap = this.errorLoader
				.getErrorPropertiesMap();
		if (urlENEQuery == null) {
			throw new AdapterException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String.valueOf(ErrorConstants.ERROR_CODE_11523)),
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);
		}
		ENEQueryResults endecaresults = null;
		try {
			final Map<String, String> endecaPropertiesMap = this.endecaLoader
					.getEndecaPropertiesMap();
			// This creates a endeca connection instance
			final HttpENEConnection endecaConn = new HttpENEConnection(
					endecaPropertiesMap.get(EndecaConstants.HOST),
					endecaPropertiesMap.get(EndecaConstants.PORT));
			LOGGER.debug("EndecaConn value is : " + endecaConn, correlationId);
			endecaresults = endecaConn.query(urlENEQuery);					
		} catch (ENEQueryException e) {
			LOGGER.error(e, correlationId);
			throw new AdapterException(
					String.valueOf(ErrorConstants.ERROR_CODE_11523),
					errorPropertiesMap.get(String.valueOf(ErrorConstants.ERROR_CODE_11523)),
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);
		}
		LOGGER.debug(" Return value is : " + endecaresults, correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return endecaresults;
	}

	
	
	/**
	 * The method to set the range filter based on the values from request.
	 * 
	 * @param query
	 *            Endeca query to be formed from the request params
	 * @param searchKey
	 *            Key to be checked for from the request map
	 * @param searchValue
	 *            Value of the corresponding search key
	 * @param correlationId
	 *            correlationId from request
	 * @throws UrlENEQueryParseException
	 *             Exception thrown from the query
	 */
	private void checkFilterPriceRange(final UrlENEQuery query,
			final String searchKey, final String searchValue,
			final String correlationId) throws UrlENEQueryParseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("Search key for the price range: ", searchKey);
		if (EndecaConstants.FILTER_PRICE.equalsIgnoreCase(searchKey)) {
			// price range is formed as required in the query.
			final String searchFilterTerm = searchValue.replace(
					EndecaConstants.FILTER_PRICE,
					EndecaConstants.FILTER_PRICE
					+ CommonConstants.PIPE_SEPERATOR);
			query.setNf(searchFilterTerm);
		}
		LOGGER.logMethodExit(startTime, correlationId);
	}

}
