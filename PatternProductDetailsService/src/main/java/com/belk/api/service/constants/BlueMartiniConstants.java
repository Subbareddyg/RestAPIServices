package com.belk.api.service.constants;

/**
 * General constants specifically required for the PatternProductDetailsService
 * project.
 * 
 * Update: This API has been introduced as part of change request:
 * CR_BELK_API_3.
 * 
 * Updated: some of the unused constants removed as part of the phase2, April,14 release
 * 
 * @author Mindtree
 * @date Nov 18th, 2013
 */
public final class BlueMartiniConstants {
	/**
	 * PRODUCT_CODE is unique code for each of the product.
	 */
	public static final String PRODUCT_CODE = "productCode"; 

	/**
	 * PRODUCTS is list of product.
	 */
	public static final String PRODUCTS = "products";

	/**
	 * PARENT_PRODUCT is the parent product code of the product.
	 */
	public static final String PARENT_PRODUCT = "parentProduct";

	/**
	 * DEPARTMENT_NUMBER is department number of product.
	 */
	public static final String DEPARTMENT_NUMBER = "ATR_Dept_Number";

	/**
	 * CLASS_NUMBER is name of the class of product.
	 */
	public static final String CLASS_NUMBER = "ATR_Class_Number";

	/**
	 * SKUS is list of skus product.
	 */
	public static final String SKUS = "skus";

	/**
	 * PATH is the category tagged to the product.
	 */
	public static final String PATH = "path";

	/**
	 * COMPOSITE_PRODUCT_DETAILS is product details of the product.
	 */
	public static final String COMPOSITE_PRODUCT_DETAILS = "compositeProductDetails";

	/**
	 * LIST_PRICE is listing price of the product.
	 */
	public static final String LIST_PRICE = "displayableListPrice";

	/**
	 * SALE_PRICE is sale price of the product.
	 */
	public static final String SALE_PRICE = "displayableSalePrice";

	/**
	 * WEB_STORE_SALE is web store of the product.
	 */
	public static final String WEB_STORE_SALE = "ATR_webstore_sale";

	/**
	 * CLEARANCE is to specify the product is on clearance.
	 */
	public static final String CLEARANCE = "isOnClearance";

	/**
	 * NEW_ARRIVAL is to specify the product is new arrival.
	 */
	public static final String NEW_ARRIVAL = "isNewArrival";

	/**
	 * BRIDAL_ELIGIBLE is to specify the product is bridal eligible.
	 */
	public static final String BRIDAL_ELIGIBLE = "ATR_bridal_eligible";

	/**
	 * SKU_CODE is sku code of the product.
	 */
	public static final String SKU_CODE = "skuCode";

	/**
	 * SKU_UPC is sku upc code of the product.
	 */
	public static final String SKU_UPC = "upc";

	/**
	 * COLOR1 is color of the product.
	 */
	public static final String COLOR1 = "ATR_color_desc";

	/**
	 * COLOR2 is color of the product.
	 */
	public static final String COLOR2 = "ATR_super_color_2";

	/**
	 * COLOR3 is color of the product.
	 */
	public static final String COLOR3 = "ATR_super_color_3";

	/**
	 * SIZE is list of size of the product.
	 */
	public static final String SIZE = "ATR_size_component_1";

	/**
	 * INVENTORY_AVAILABLE is to specify the product is available in inventory.
	 */
	public static final String INVENTORY_AVAILABLE = "AvailableInventory";

	/**
	 * INVENTORY_LEVEL is to specify the product inventory level.
	 */
	public static final String INVENTORY_LEVEL = "OnHandInventory";

	/**
	 * IMAGE_URL is image url of the product.
	 */
	public static final String IMAGE_URL = "ATR_MainImageUrl";

	/**
	 * VENDOR_NUMBER is vendor number of the product.
	 */
	public static final String VENDOR_NUMBER = "ATR_vendor_number";

	/**
	 * BRAND is brand of the product.
	 */
	public static final String BRAND = "ATR_brand";

	/**
	 * PRODUCT_NAME is unique code for each of the product.
	 */
	public static final String PRODUCT_NAME = "ATR_product_name";

	/**
	 * SHORT_DESCRIPTION is description of the product.
	 */
	public static final String SHORT_DESCRIPTION = "shortDesc";

	/**
	 * LONG_DESCRIPTION is description of the product.
	 */
	public static final String LONG_DESCRIPTION = "longDesc";

	/**
	 * PRODUCT_TYPE is type of the product.
	 */
	public static final String PRODUCT_TYPE = "ATR_prod_type";

	/**
	 * IS_PATTERN_PRODUCT is to specify whether the product is pattern or not.
	 */
	public static final String IS_PATTERN_PRODUCT = "isPatternProduct";

	/**
	 * IS_OUT_OF_STOCK_ONLINE is specify whether the product is out of stock or
	 * not.
	 */
	public static final String IS_OUT_OF_STOCK_ONLINE = "isOutOfStockOnline";

	/**
	 * Default private constructor.
	 */
	private BlueMartiniConstants() {

	}
}
