package com.belk.api.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.logger.LoggingHelper;

/**
 * This class is a helper class for populating BlueMartini specific Queries and
 * KeyList <br />
 * This Util class provides the data for other Test classes in BlueMartini layer
 * 
 * @author Mindtree
 * 
 */

public class BlueMartiniTestUtil {

	private static final GenericLogger LOGGER = LoggingHelper
			.getLogger("BlueMartiniTestUtil");
	{
		LoggerUtil.setLogger(LOGGER);
	}

	/**
	 * Test method to create BlueMartiniResultList which returns json object
	 * with hardcoded results from blue martini
	 * 
	 * @return Map<String, String> requested params
	 */

	public static final Map<String, String> requestParams() {

		final Map<String, String> params = new HashMap<String, String>();
		params.put("prdCode", "3201634Z31300");
		params.put("SOCKET_TIMEOUT", "100");
		params.put("CONNECTION_TIMEOUT", "2");

		return params;
	}

	/**
	 * Test method to create BlueMartiniResultList which returns json object
	 * with hardcoded results from blue martini
	 * 
	 * @return Map<String, String> requested params
	 */

	public static final Map<String, String> incorrectRequestParams() {

		final Map<String, String> params = new HashMap<String, String>();
		params.put("prdCode", null);

		return params;
	}

	/**
	 * Test method to create BlueMartiniResultList which returns json object
	 * with hardcoded results from blue martini
	 * 
	 * @return JSONObject
	 */

	public final JSONObject blueMartiniResultList() {

		final JSONObject mainjsonObject = new JSONObject();
		final JSONArray productsArray = new JSONArray();

		try {

			final JSONObject subjsonObject = new JSONObject();
			subjsonObject.put("productCode", "3201634Z31300");

			final Map<String, String> parentProductMap = this.getParentProductMap();
			// parentProductMap.put("isPatternProduct", "F");
			subjsonObject.put("parentProduct", parentProductMap);

			final Map<String, String> compositeProductDetailsMap = this.getCompositeProductDetailsMap();
			// compositeProductDetailsMap.put("isPatternProduct", "F");
			// compositeProductDetailsMap.put("isOutOfStockOnline", "T");

			subjsonObject.put("compositeProductDetails",
					compositeProductDetailsMap);

			final Map<String, String> skuDetailsMap1 = this.getSkuDetailsMap1();
			// skuDetailsMap1.put("isPatternProduct", "F");

			final Map<String, String> skuDetailsMap2 = new HashMap<String, String>();
			this.getSkuMap1(skuDetailsMap1);
			// skuDetailsMap1.put("isPatternProduct", "F");

			final Map<String, String> skuDetailsMap3 = new HashMap<String, String>();
			this.getSkuMap1(skuDetailsMap1);

			final Map<String, String> skuDetailsMap4 = new HashMap<String, String>();
			this.getSkuMap1(skuDetailsMap1);

			// final Object[] array = new Object[]{skuDetailsMap1,
			// skuDetailsMap2, skuDetailsMap3, skuDetailsMap4};

			final JSONArray jsonArray = new JSONArray();
			jsonArray.put(skuDetailsMap1);
			jsonArray.put(skuDetailsMap2);
			jsonArray.put(skuDetailsMap3);
			jsonArray.put(skuDetailsMap4);
			subjsonObject.put("skus", jsonArray);

			// final Object[] arr = new Object[]{jsonObject};
			productsArray.put(subjsonObject);
			mainjsonObject.put("products", productsArray);

		} catch (final JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mainjsonObject;

	}

	/**
	 * Method extracted from blueMartiniArrayList to avoid checkstyle violations
	 * 
	 * @param skuDetailsMap1
	 *            contains map with String as key and String as value
	 * 
	 * 
	 */
	private void getSkuMap1(final Map<String, String> skuDetailsMap1) {
		skuDetailsMap1.put("skuCode", "0400675912644");
		skuDetailsMap1.put("upc", "0670113311654");
		skuDetailsMap1.put("ATR_color_desc", "Navy");
		skuDetailsMap1.put("ATR_super_color_2", "Navy");
		skuDetailsMap1.put("ATR_size_component_1", "2X");
		skuDetailsMap1.put("ATR_super_color_3", "Navy");
		skuDetailsMap1.put("AvailableInventory", "F");
		skuDetailsMap1.put("ATR_webstore_sale", "F");
		// skuDetailsMap1.put("isOutOfStockOnline", "F");
		skuDetailsMap1.put("webStoreSale", "F");
		skuDetailsMap1.put("isOnClearance", "F");
		skuDetailsMap1.put("isNewArrival", "F");
		skuDetailsMap1
				.put("ATR_MainImageUrl",
						"http://s7d4.scene7.com/is/image/Belk?"
								+ "layer=0&src=3201634_Z31300_A_709_T10L00&layer=comp&");
		skuDetailsMap1.put("ATR_super_color_1", "Navy");
	}

	/**
	 * Method extracted from blueMartiniArrayList to avoid checkstyle violations
	 * returns the map containing String as key and String as argument
	 * 
	 * @return skuDetailsMap1
	 */
	private Map<String, String> getSkuDetailsMap1() {
		final Map<String, String> skuDetailsMap1 = new HashMap<String, String>();
		this.getSkuMap1(skuDetailsMap1);
		return skuDetailsMap1;
	}

	/**
	 * Method extracted from blueMartiniArrayList to avoid checkstyle violations
	 * returns the map containing String as key and String as argument
	 * 
	 * @return compositeProductDetailsMap
	 */
	private Map<String, String> getCompositeProductDetailsMap() {
		final Map<String, String> compositeProductDetailsMap = new HashMap<String, String>();

		compositeProductDetailsMap.put("displayableListPrice", "65.00");
		compositeProductDetailsMap.put("displayableSalePrice", "65.00");
		compositeProductDetailsMap.put("isOnClearance", "F");
		compositeProductDetailsMap.put("isNewArrival", "F");
		compositeProductDetailsMap.put("hasVaryingListPrice", "F");
		compositeProductDetailsMap.put("hasVaryingSalePrice", "F");
		compositeProductDetailsMap.put("isNewArrival", "F");
		compositeProductDetailsMap.put("isOnClearance", "F");
		compositeProductDetailsMap.put("ATR_webstore_sale", "F");
		return compositeProductDetailsMap;
	}

	/**
	 * Method extracted from blueMartiniArrayList to avoid checkstyle violations
	 * returns the map containing String as key and String as argument
	 * 
	 * @return parentProductMap
	 */
	private Map<String, String> getParentProductMap() {
		final Map<String, String> parentProductMap = new HashMap<String, String>();

		parentProductMap.put("ATR_vendor_number", "3201634");
		parentProductMap.put("ATR_bridal_eligible", "F");
		parentProductMap.put("ATR_brand", "Nautica");
		parentProductMap.put("ATR_product_name", "Big & Tall Pieced Polo");
		parentProductMap.put("shortDesc", "Big & Tall Pieced Polo");
		parentProductMap.put("ATR_prod_type", "Knit Tops");
		parentProductMap.put("ATR_Dept_Number", "353");
		parentProductMap.put("ATR_Class_Number", "3406");
		parentProductMap.put("isNewArrival", "F");
		parentProductMap.put("isOnClearance", "F");
		parentProductMap.put("ATR_webstore_sale", "F");
		return parentProductMap;
	}

	/**
	 * Test method to convert BlueMartiniResultList to list of maps which
	 * returns list of maps with hardcoded results from blue martini
	 * 
	 * @return List<Map<String, String>>
	 * @throws JSONException
	 *             JSONException
	 */
	public final List<Map<String, String>> testBlueMartiniResultsTOListofMap()
			throws JSONException {

		final Map<String, String> blueMartiniMap = new HashMap<String, String>();
		List<Map<String, String>> blueMartiniResultList = null;
		blueMartiniResultList = new ArrayList<Map<String, String>>();

		blueMartiniResultList();

		blueMartiniMap.put("P_product_code", "3201634Z31300");
		blueMartiniMap.put("P_vendor_number", "3201634");
		blueMartiniMap.put("P_IS_BRIDAL", "F");
		blueMartiniMap.put("P_BRAND", "Nautica");
		blueMartiniMap.put("P_PRODUCT_NAME", "Big & Tall Pieced Polo");
		blueMartiniMap.put("P_SHORTDESC", "Big & Tall Pieced Polo");
		blueMartiniMap.put("P_PRODUCT_TYPE", "Knit Tops");
		blueMartiniMap.put("P_LISTPRICE", "65.00");
		blueMartiniMap.put("P_SALE_PRICE", "65.00");
		blueMartiniMap.put("P_CLEARANCE", "F");
		blueMartiniMap.put("P_NEW_ARRIVAL", "F");
		blueMartiniMap.put("P_sku_code", "0400675912644");
		blueMartiniMap.put("P_SKU_UPC", "0670113311654");
		blueMartiniMap.put("P_COLOR", "Navy");
		blueMartiniMap.put("P_COLOR1", "Navy");
		blueMartiniMap.put("P_INVENTORY_AVAIL", "1000");
		blueMartiniMap.put("P_INVENTORY_LEVEL", "50");

		blueMartiniResultList.add(blueMartiniMap);

		return blueMartiniResultList;
	}

	/**
	 * Test method to create BlueMartiniResultList which returns json object
	 * with hardcoded results from blue martini
	 * 
	 * @return JSONArray
	 */

	public final JSONArray blueMartiniArrayList() {

		new JSONObject();
		final JSONArray jsonArray = new JSONArray();
		final Map<String, String> skuDetailsMap1 = this.getSkuDetailMap1();
		final Map<String, String> skuDetailsMap2 = new HashMap<String, String>();
		skuDetailsMap2.put("skuCode", "0400675912668");
		skuDetailsMap2.put("upc", "0670113311678");
		skuDetailsMap2.put("ATR_color_desc", "Navy");
		skuDetailsMap2.put("ATR_super_color_2", "Navy");
		skuDetailsMap2.put("AvailableInventory", " ");
		skuDetailsMap2.put("OnHandInventory", " ");
		final Map<String, String> skuDetailsMap3 = new HashMap<String, String>();
		skuDetailsMap3.put("skuCode", "0400675912651");
		skuDetailsMap3.put("upc", "0670113311661");
		skuDetailsMap3.put("ATR_color_desc", "Navy");
		skuDetailsMap3.put("ATR_super_color_2", "Navy");
		skuDetailsMap3.put("AvailableInventory", "50");
		skuDetailsMap3.put("OnHandInventory", "1,000");
		final Map<String, String> skuDetailsMap4 = new HashMap<String, String>();
		skuDetailsMap4.put("skuCode", "0400675912712");
		skuDetailsMap4.put("upc", "0670113311722");
		skuDetailsMap4.put("ATR_color_desc", "Navy");
		skuDetailsMap4.put("ATR_super_color_2", "Navy");
		skuDetailsMap4.put("AvailableInventory", "50");
		skuDetailsMap4.put("OnHandInventory", "100");
		final Object[] array = new Object[] {skuDetailsMap1, skuDetailsMap2,
				skuDetailsMap3, skuDetailsMap4 };
		jsonArray.put(array);
		return jsonArray;

	}

	/**
	 * Method extracted from blueMartiniArrayList to avoid checkstyle violations
	 * returns the map containing String as key and String as argument
	 * 
	 * @return skuDetailsMap1
	 */
	private Map<String, String> getSkuDetailMap1() {
		final Map<String, String> skuDetailsMap1 = new HashMap<String, String>();
		skuDetailsMap1.put("skuCode", "0400675912644");
		skuDetailsMap1.put("upc", "0670113311654");
		skuDetailsMap1.put("ATR_color_desc", "Navy");
		skuDetailsMap1.put("ATR_super_color_2", "Navy");
		skuDetailsMap1.put("AvailableInventory", " ");
		skuDetailsMap1.put("OnHandInventory", " ");
		return skuDetailsMap1;
	}

}
