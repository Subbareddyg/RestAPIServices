package com.belk.api.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import com.belk.api.constants.CommonConstants;
import com.belk.api.model.productsearch.Price;
import com.belk.api.model.productsearch.ProductSearch;
import com.belk.api.model.productsearch.Search;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * Unit Testing related to ServiceUtil class is performed. <br />
 * {@link TestServiceUtil} class is written for testing methods in
 * {@link ServiceUtil} The unit test cases evaluates the way the methods behave
 * for the inputs given.
 * 
 * @author Mindtree
 * @date 25 Oct 2013
 */
public class TestProductSearchUtil {

	/**
	 * 
	 * Test method for {@link com.belk.api.util#createRequestMap ()}. Testing is
	 * done to check whether the required data is obtained.
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
		requestMap.put("sort",
				"productcode|-name|-listprice|saleprice|inventory|"
						+ "-newarrival|bestseller|wishlistfavorites");
		requestMap.put("catid", "132323213");
		requestMap.put("isbridal", "true");
		requestMap.put("dim", "true");
		return requestMap;
	}

	/**
	 * Test method for {@link com.belk.api.util#createResultMap()}. Testing is
	 * done to check whether the required data is obtained.
	 * 
	 * @return requestMap
	 */
	public static List<Map<String, String>> createResultMap() {
		final List<Map<String, String>> resultMapList = new ArrayList<Map<String, String>>();
		final Map<String, String> resultMap = new HashMap<String, String>();

		resultMap.put("P_product_code", "3203375NA10011");
		resultMap.put("P_available_in_Store", "No");
		resultMap.put("P_available_online", "Yes");
		resultMap.put("P_clearance", "No");
		resultMap.put("P_isPattern", "F");
		resultMap.put("P_listPrice", "64.000000");
		resultMap.put("P_onSale", "No");
		resultMap.put("P_product_name", "Ocean Washed Dress Shirt");
		resultMap.put("P_saleprice", "64.000000");

		resultMapList.add(resultMap);

		return resultMapList;
	}

	/**
	 * Test method for {@link com.belk.api.util#createURIMap()}. Testing is done
	 * to check whether the required data is obtained.
	 * 
	 * @return uriMap of type Map<String, List<String>>
	 */
	public static Map<String, List<String>> createURIMap() {

		final Map<String, List<String>> uriMap = new HashMap<String, List<String>>();
		final List<String> list1 = new ArrayList<String>();
		list1.add("handbag");

		final List<String> list3 = new ArrayList<String>();
		list3.add("JSON");
		final List<String> list4 = new ArrayList<String>();
		list4.add("10");
		final List<String> list5 = new ArrayList<String>();
		list5.add("100");

		final List<String> list7 = new ArrayList<String>();
		list7.add("132323213");
		final List<String> list8 = new ArrayList<String>();
		list8.add("true");
		final List<String> list9 = new ArrayList<String>();
		list9.add("true");

		uriMap.put("q", list1);

		uriMap.put("format", list3);
		uriMap.put("limit", list4);
		uriMap.put("offset", list5);

		uriMap.put("catid", list7);
		uriMap.put("isbridal", list8);
		uriMap.put("dim", list9);

		return uriMap;
	}

	/**
	 * Test method for {@link com.belk.api.util#getProductSearchList()}. Testing
	 * is done to check whether the required data is obtained.
	 * 
	 * @return search
	 */
	public static Search createSearchResults() {
		final Search search = new Search();
		search.setProducts(getProductSearchList());

		return search;
	}

	/**
	 * Test method for {@link com.belk.api.util#getProductSearchList()}. Testing
	 * is done to check whether the required data is obtained.
	 * 
	 * @return products
	 */
	private static List<ProductSearch> getProductSearchList() {
		final List<ProductSearch> products = new ArrayList<ProductSearch>();
		final ProductSearch productSearch = new ProductSearch();
		productSearch.setProductCode("3203375NA10011");
		productSearch.setName("Ocean Washed Dress Shirt");
		productSearch.setProductPrice(getPriceList());
		products.add(productSearch);
		return products;
	}

	/**
	 * Test method for {@link com.belk.api.util#getPriceList()}. Testing is done
	 * to check whether the required data is obtained.
	 * 
	 * @return price
	 */
	private static List<Price> getPriceList() {

		final List<Price> priceList = new ArrayList<Price>();
		final Price price = new Price();
		price.setKey("listPrice");
		price.setValue("275");
		priceList.add(price);
		return priceList;
	}

	/**
	 * Test method for {@link com.belk.api.util#createURIInvalidDataMap()}.
	 * Testing is done to check whether the required data is obtained. Method
	 * which creates URI map of data,which resembles the URInfo object created
	 * at run time from URI Information.
	 * 
	 * @return uriMap
	 */
	public static Map<String, List<String>> createURIInvalidDataMap() {

		final Map<String, List<String>> uriMap = new HashMap<String, List<String>>();

		final List<String> list1 = new ArrayList<String>();
		list1.add("handbag");
		final List<String> list2 = new ArrayList<String>();
		list2.add("color:blue,red|size:small,large|xyz:lee,wrangler|alpha:3,7");
		final List<String> list3 = new ArrayList<String>();
		list3.add("JSON");
		final List<String> list4 = new ArrayList<String>();
		list4.add("10");
		final List<String> list5 = new ArrayList<String>();
		list5.add("100");
		final List<String> list6 = new ArrayList<String>();
		list6.add("productcode|-name|-listprice|saleprice|invention|-newdeparture|bestseller|wishlistfavorites");
		final List<String> list7 = new ArrayList<String>();
		list7.add("132323213");
		final List<String> list8 = new ArrayList<String>();
		list8.add("true");
		final List<String> list9 = new ArrayList<String>();
		list9.add("true");

		uriMap.put("q", list1);
		uriMap.put("attr", list2);
		uriMap.put("format", list3);
		uriMap.put("limit", list4);
		uriMap.put("offset", list5);
		uriMap.put("sort", list6);
		uriMap.put("catid", list7);
		uriMap.put("isbridal", list8);
		uriMap.put("dim", list9);

		return uriMap;
	}

	/**
	 * Test method for {@link com.belk.api.util#createInvalidURIParametersMap()}
	 * . Testing is done to check whether the required data is obtained. .
	 * 
	 * Request Parameters and valid data
	 * 
	 * @return uriParametersMap
	 */
	public static MultivaluedMap<String, String> createInvalidURIParametersMap() {
		final MultivaluedMap<String, String> uriParametersMap = new MultivaluedMapImpl();

		uriParametersMap.add("q", "handbag");
		uriParametersMap.add("attr",
				"color:blue,red|size:small,large|brand:lee,wrangler|sigma:3,7");
		uriParametersMap.add("format", "JSON");
		uriParametersMap.add("limit", "10");
		uriParametersMap.add("offset", "100");
		uriParametersMap.add("sort",
				"productcode|-name|-listprice|saleprice|inventory|"
						+ "-newarrival|bestseller|wishlistfavorites");
		uriParametersMap.add("catid", "132323213");
		uriParametersMap.add("isbridal", "true");
		uriParametersMap.add("dim", "true");

		return uriParametersMap;
	}

	/**
	 * Test method for {@link com.belk.api.util#createRequestHeadersMap()}.
	 * Testing is done to check whether the required data is obtained. .
	 * 
	 * @return reqHeaders
	 */
	public static MultivaluedMap<String, String> createRequestHeadersMap() {
		final MultivaluedMap<String, String> reqHeaders = new MultivaluedMapImpl();
		reqHeaders.add(CommonConstants.CORRELATION_ID, "1234567890");
		return reqHeaders;
	}

	/**
	 * Test method for {@link com.belk.api.util#createURIParametersMap()}.
	 * Testing is done to check whether the required data is obtained. .
	 * 
	 * @return uriParametersMap
	 */
	public static MultivaluedMap<String, String> createURIParametersMap() {
		final MultivaluedMap<String, String> uriParametersMap = new MultivaluedMapImpl();

		uriParametersMap.add("q", "handbag");
		uriParametersMap.add("attr",
				"color:blue,red|size:small,large|brand:lee,wrangler|price:3,7");
		uriParametersMap.add("format", "JSON");
		uriParametersMap.add("limit", "10");
		uriParametersMap.add("offset", "100");
		uriParametersMap.add("sort",
				"productcode|-name|-listprice|saleprice|inventory|"
						+ "-newarrival|bestseller|wishlistfavorites");
		uriParametersMap.add("catid", "132323213");
		uriParametersMap.add("isbridal", "true");
		uriParametersMap.add("dim", "true");

		return uriParametersMap;

	}

	/**
	 * Test method for {@link com.belk.api.util#createURIMapWithColon()}.
	 * Testing is done to check whether the required data is obtained. .
	 * 
	 * @return uriMap
	 */
	public static Map<String, List<String>> createURIMapWithColon() {
		final Map<String, List<String>> uriMap = new HashMap<String, List<String>>();
		final List<String> list1 = new ArrayList<String>();
		list1.add("color:blue,red:size:small,large:brand:lee,wrangler:price:3,7");
		uriMap.put("attr", list1);
		return uriMap;
	}

}
