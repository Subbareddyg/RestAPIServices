package com.belk.api.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.belk.api.constants.EndecaConstants;
import com.belk.api.model.Dimension;
import com.endeca.navigation.DimValIdList;
import com.endeca.navigation.ENEQuery;
import com.endeca.navigation.ENEQueryException;
import com.endeca.navigation.ENEQueryResults;
import com.endeca.navigation.ERecSearch;
import com.endeca.navigation.ERecSearchList;
import com.endeca.navigation.ERecSortKey;
import com.endeca.navigation.ERecSortKeyList;
import com.endeca.navigation.HttpENEConnection;

/**
 * This class is a helper class for populating Endeca specific Queries and
 * KeyList <br />
 * This Util class provides the data for other Test classes in Endeca layer
 * 
 * @author Mindtree
 * 
 */
public class AdapterTestUtil {
	public static final String PRODUCT_SEARCH_ALL = "productSearchAll";
	public static final String TSHIRT = "tshirt";
	public static final String DEFAULT_OFFSET = "0";
	public static final String ENDECA_HOST = "69.166.149.171";
	public static final String ENDECA_PORT = "15000";

	/**
	 * 
	 * @return ENEQuery this is the ENEQuery which gets formed to query the
	 *         Endeca for search
	 */
	public static ENEQuery createENEQuery() {
		final ENEQuery eneQuery = new ENEQuery();
		final ERecSearchList searches = new ERecSearchList();
		final ERecSearch eRecSearch = new ERecSearch(PRODUCT_SEARCH_ALL, TSHIRT);
		searches.add(eRecSearch);
		eneQuery.setNavERecSearches(searches);
		eneQuery.setNavRollupKey(EndecaConstants.ROLL_UP_KEY);

		return eneQuery;

	}

	/**
	 * 
	 * @param sortKey
	 *            this parameter will have the list of <br />
	 *            sortable keys as a String with pipe separated.
	 * @return ERecSortKeyList forms an object of {@link ERecSortKeyList}
	 */
	public static ERecSortKeyList createERecSortKeyList(final String sortKey) {
		final ERecSortKeyList eRecSortKeyList = new ERecSortKeyList();
		final ERecSortKey eRecSortKey = new ERecSortKey(sortKey, true);
		eRecSortKeyList.add(eRecSortKey);

		return eRecSortKeyList;
	}

	/**
	 * 
	 * @return ENEQuery for testing
	 */
	public static ENEQuery getEneQuery() {
		final ENEQuery eneQuery = new ENEQuery();
		final ERecSearchList searches = new ERecSearchList();
		final ERecSearch eRecSearch = new ERecSearch("productSearchAll",
				"handbag", "mode matchany");
		searches.add(eRecSearch);
		final DimValIdList dimValIdList = new DimValIdList(
				DEFAULT_OFFSET);
		eneQuery.setNavDescriptors(dimValIdList);
		eneQuery.setNavRollupKey(EndecaConstants.ROLL_UP_KEY);
		eneQuery.setNavERecSearches(searches);
		return eneQuery;
	}

	/**
	 * 
	 * @return EneQuery for the data for Null
	 */
	public static ENEQuery getEneQueryNull() {
		final ENEQuery eneQuery = new ENEQuery();
		final ERecSearchList searches = new ERecSearchList();
		final ERecSearch eRecSearch = new ERecSearch("productSearchAll",
				"mindtree", "mode matchany");
		searches.add(eRecSearch);
		final DimValIdList dimValIdList = new DimValIdList(
				DEFAULT_OFFSET);
		eneQuery.setNavDescriptors(dimValIdList);
		eneQuery.setNavRollupKey(EndecaConstants.ROLL_UP_KEY);
		eneQuery.setNavERecSearches(searches);
		return eneQuery;
	}

	/**
	 * This method is for sample test data of EneQuery which would give null
	 * 
	 * @return {@link ENEQuery} object which is null
	 */

	public final ENEQuery getNullEneuery() {
		return null;
	}

	/**
	 * This method is for sample test data of EndecaMap
	 * 
	 * @return {@link Map} object
	 */
	public final Map<String, String> getMyMap() {
		final Map<String, String> requestMap = new LinkedHashMap<String, String>();
		requestMap.put("productSearchAll", "handbag");
		requestMap.put("color", "yellow");
		requestMap.put("size", "small");
		requestMap.put("brand", "Hollister");
		requestMap.put("price", "200");
		requestMap.put("dim", "true");
		requestMap.put("N", "4294920970");
		requestMap.put("navERecsPerAggrERec", "1");
		return requestMap;
	}

	/**
	 * This method is for sample test data of List of Maps
	 * 
	 * @return List of Maps which is responseMap which we shall have for
	 *         creating the response
	 */
	public final List<Map<String, String>> getMyListOfMap() {
		final List<Map<String, String>> responseList = new ArrayList<Map<String, String>>();
		final Map<String, String> resultMap1 = new HashMap<String, String>();
		resultMap1.put("P_product_code", "3203375NA10011");
		resultMap1.put("P_available_in_Store", "No");
		resultMap1.put("P_available_online", "Yes");
		resultMap1.put("P_clearance", "No");
		resultMap1.put("P_isPattern", "F");
		resultMap1.put("P_listPrice", "64.000000");
		resultMap1.put("P_onSale", "No");
		resultMap1.put("P_brand", "Derek Heart");
		resultMap1.put("P_web_id", " ");
		resultMap1.put("P_product_id", " ");
		resultMap1.put("P_dept_number", "717");
		resultMap1.put("P_color", "Pink");
		resultMap1.put("P_dept_number", "717");
		resultMap1.put("P_color", "Pink");
		resultMap1.put("P_path", "/Assortments/Main/Belk_Primary/Shop_All");
		resultMap1.put("P_class_Number", "7172");
		resultMap1.put("P_bridal_eligible", "F");
		resultMap1.put("P_objectId", "1689949383353220");

		resultMap1.put("totalProducts", "38398");
		resultMap1.put("totalSkus", "152643");
		resultMap1.put("hasFurtherRefinements", "Yes");

		resultMap1.put("dimentionKey", "4294922263");
		resultMap1.put("label", "Home");
		resultMap1.put("parentDimentionKey", "4294967294");

		responseList.add(resultMap1);
		return responseList;

	}

	/**
	 * 
	 * @return {@link ENEQueryResults} object
	 * @throws ENEQueryException
	 *             if there are any exception while fetching results.
	 */
	public final ENEQueryResults getEneQueryResults() throws ENEQueryException {
		final HttpENEConnection connection = new HttpENEConnection(
				ENDECA_HOST, ENDECA_PORT);
		ENEQueryResults eneQueryResults;

		eneQueryResults = connection.query(AdapterTestUtil.getEneQuery());
		return eneQueryResults;
	}

	/**
	 * This method is for sample test data of List of Maps
	 * 
	 * @return Maps which is requestMap.
	 */

	public final Map<String, String> getRequestMap() {
		final Map<String, String> map = new HashMap<String, String>();
		map.put("q", "handbag");
		return map;
	}

	/**
	 * creates list containing Map<String, String>
	 * 
	 * @return List of maps
	 */
	public final List<Map<String, String>> getResponseList() {
		final List<Map<String, String>> responseList = new ArrayList<Map<String, String>>();
		final Map<String, String> resultMap1 = new HashMap<String, String>();
		resultMap1.put("P_product_code", "3203375NA10011");
		resultMap1.put("P_available_in_Store", "No");
		resultMap1.put("P_available_online", "Yes");
		resultMap1.put("P_clearance", "No");
		resultMap1.put("P_isPattern", "F");
		resultMap1.put("P_listPrice", "64.000000");
		resultMap1.put("P_onSale", "No");
		resultMap1.put("P_brand", "Derek Heart");
		resultMap1.put("P_web_id", " ");
		resultMap1.put("P_product_id", " ");
		resultMap1.put("P_dept_number", "717");
		resultMap1.put("P_color", "Pink");
		resultMap1.put("P_dept_number", "717");
		resultMap1.put("P_color", "Pink");
		resultMap1.put("P_path", "/Assortments/Main/Belk_Primary/Shop_All");
		resultMap1.put("P_class_Number", "7172");
		resultMap1.put("P_bridal_eligible", "F");
		resultMap1.put("P_objectId", "1689949383353220");

		resultMap1.put("totalProducts", "38398");
		resultMap1.put("totalSkus", "152643");
		resultMap1.put("hasFurtherRefinements", "Yes");

		resultMap1.put("dimentionKey", "4294922263");
		resultMap1.put("label", "Home");
		resultMap1.put("multiSelectEnabled", "true");
		resultMap1.put("multiSelectOperation", "OR");

		resultMap1.put("parentDimentionKey", "4294967294");

		responseList.add(resultMap1);
		return responseList;
	}

	/**
	 * method to get the endecaMap data as input to the query
	 * 
	 * @return Map for the the input as endecaMap data
	 */
	public final Map<String, String> getServiceRequestMap() {
		final Map<String, String> map = new LinkedHashMap<String, String>();
		// fields come from service layer to Endeca layer
		map.put("productSearchAll", "handbag");
		map.put("P_color", "blue,red");
		map.put("N", "0");
		map.put("offset", "2");
		map.put("sort", "saleprice|listprice|-inventory|newArrival|bestSeller");

		return map;
	}

	/**
	 * method to check for the Ene connection to break when wrong host and port
	 * is provided
	 * 
	 * @return {@link HttpENEConnection} object is returned
	 */
	public final HttpENEConnection getWrongEndecaConnection() {
		final String errorHost = "10.60.212.203";
		final String errorPort = "1234";
		return new HttpENEConnection(errorHost, errorPort);
	}

	/**
	 * Method to create request map
	 * 
	 * @return requestMap
	 */
	public static Map<String, String> createRequestMap() {

		final Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("q", "handbag");
		requestMap.put("attr",
				"color:blue,red|size:small,large|brand:lee,wrangler|price:3,7");
		requestMap.put("format", "JSON");
		requestMap.put("limit", "10");
		requestMap.put("offset", "100");
		requestMap.put("refinementsRequired", "true");
		requestMap.put("refinementsRequiredLevel", "2");

		// the value for the key sort for the requestMap has been split for
		// checkstyle issues
		requestMap.put("sort",
				"productcode|-name|-listprice|saleprice|inventory|"
						+ "-newarrival|bestseller|wishlistfavorites");
		requestMap.put("catid", "132323213");
		requestMap.put("isbridal", "true");
		requestMap.put("color", "yellow");
		requestMap.put("size", "small");
		requestMap.put("brand", "Hollister");
		requestMap.put("price", "200");
		requestMap.put("dim", "true");
		requestMap.put("N", "4294920970");
		requestMap.put("navERecsPerAggrERec", "1");
		requestMap.put("rootDimension", "4294967294");
		requestMap.put("showproduct", "true");
		return requestMap;
	}

	/**
	 * Method to create Endeca ResultMap with actual values
	 * 
	 * @return map
	 */
	public final Map<String, String> getEndecaResultMap() {
		final Map<String, String> map = new HashMap<String, String>();
		map.put("productSearchAll", "handbag");
		map.put("P_color", "blue, red");
		map.put("N", "0");
		map.put("offset", "2");
		map.put("sort", "saleprice|listprice|-inventory|newArrival|bestSeller");

		return map;
	}

	/**
	 * created Map which has endecaInput map
	 * 
	 * @return created Map which has endecaInput map
	 */

	public final Map<String, String> getEndecaInputMap() {
		final Map<String, String> map = new HashMap<String, String>();
		map.put("productSearchAll", "handbag");
		map.put("P_color", "blue, red");
		map.put("N", "0");
		map.put("offset", "2");
		map.put("sort", "saleprice|listprice|-inventory|newArrival|bestSeller");
		map.put("dim", "true");
		map.put("refinementsRequired", "refinementsRequired");

		return map;
	}

	/**
	 * created Map which has dimension values
	 * 
	 * @return created Map which has dimension list
	 */

	public final Map<String, String> getDimensionListMap() {

		final Map<String, String> dimensionResultMap = new HashMap<String, String>();

		dimensionResultMap.put("dimentionKey", "4294922263");
		dimensionResultMap.put("label", "Home");
		dimensionResultMap.put("multiSelectEnabled", "true");
		dimensionResultMap.put("multiSelectOperation", "OR");
		dimensionResultMap.put("parentDimentionKey", "4294967294");
		dimensionResultMap.put("hasFurtherRefinements", "Yes");

		return dimensionResultMap;
	}

	/**
	 * Method gets DimensionMap
	 * 
	 * @return Map<String, String>
	 */
	public static Map<String, String> getRequestDimensionMap() {

		final Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("productSearchAll", "handbag");
		requestMap.put("color", "yellow");
		requestMap.put("size", "small");
		requestMap.put("brand", "Hollister");
		requestMap.put("price", "200");
		requestMap.put("dim", "true");
		requestMap.put("N", "4294920970");
		return requestMap;
	}

	/**
	 * Method gets EndecaMap
	 * 
	 * @return Map<Long, Dimension>
	 */
	public static Map<Long, Dimension> getEndecaMap() {
		final Map<Long, Dimension> idRootDimensionMap = new HashMap<Long, Dimension>();
		final Dimension dimension = new Dimension();

		final Dimension d = new Dimension();
		dimension.setDimensionId(123);
		dimension.setName("catid");
		dimension.setParentDimensionId(89);
		final List<Dimension> dimensions = new LinkedList<Dimension>();
		dimensions.add(d);
		dimension.setDimensions(dimensions);
		idRootDimensionMap.put((long) 123, dimension);
		return idRootDimensionMap;
	}

	/**
	 * Method returns Map<Long, Dimension>
	 * 
	 * @return Map<Long, Dimension>
	 */
	public static Map<Long, Dimension> getIdRootDimensionMap() {
		final Map<Long, Dimension> idRootDimensionMap = new HashMap<Long, Dimension>();
		final Dimension dimension = new Dimension();

		final Dimension d = new Dimension();
		dimension.setDimensionId(123);
		dimension.setName("catid");
		dimension.setParentDimensionId(89);
		final List<Dimension> dimensions = new LinkedList<Dimension>();
		dimensions.add(d);
		dimension.setDimensions(dimensions);
		idRootDimensionMap.put((long) 123, dimension);
		return idRootDimensionMap;
	}

	/**
	 * Method to create request map
	 * 
	 * @return requestMap
	 */
	public static Map<String, String> createRequestDimensionMap() {

		final Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("q", "handbag");
		requestMap.put("attr",
				"color:blue,red|size:small,large|brand:lee,wrangler|price:3,7");
		requestMap.put("format", "JSON");
		requestMap.put("limit", "10");
		requestMap.put("offset", "100");
		// the value for the key sort for the requestMap has been split for
		// checkstyle issues
		requestMap.put("sort",
				"productcode|-name|-listprice|saleprice|inventory|"
						+ "-newarrival|bestseller|wishlistfavorites");
		requestMap.put("catid", "132323213");
		requestMap.put("isbridal", "true");
		requestMap.put("color", "yellow");
		requestMap.put("size", "small");
		requestMap.put("brand", "Hollister");
		requestMap.put("price", "200");
		requestMap.put("dim", "true");
		requestMap.put("N", "4294920970");
		requestMap.put("navERecsPerAggrERec", "1");
		requestMap.put("rootDimension", "4294967294");
		requestMap.put("showproduct", "true");

		return requestMap;
	}

	/**
	 * Method which returns Map<Long, Dimension>
	 * 
	 * @return Map<Long, Dimension>
	 */
	public final Map<Long, Dimension> getLongDimensionMap() {
		final Map<Long, Dimension> map = new HashMap<Long, Dimension>();

		return map;

	}

	/**
	 * creates Map<String, String>
	 * 
	 * @return map
	 */
	public static Map<String, String> getEndecaDimCheckMap() {
		final Map<String, String> map = new HashMap<String, String>();
		map.put("dim", "true");
		map.put("refinementsRequired", "refinementsRequired");
		return map;

	}

	public Map<String, String> getEndecaGenericFieldListPropertiesMap() {
		Map<String, String> endecaFieldListpropertiesMap = new HashMap<String, String>();
		endecaFieldListpropertiesMap.put("P_available_in_Store",
				"availableInStore");
		return endecaFieldListpropertiesMap;

	}
}
