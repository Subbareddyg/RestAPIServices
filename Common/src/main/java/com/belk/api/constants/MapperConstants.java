package com.belk.api.constants;

/**
 * General constants specifically required for the Domain project.
 * 
 * Update: swatch image url has been added as a part of CR: CR_BELK_API_9.
 * Update: the value of the constants has been changed to generic keys as
 * part of Standardization of Adapter Response CR.
 * 
 * @author Mindtree
 * @date Oct 04, 2013
 */
public final class MapperConstants {

	/**
	 * AVAILABLE_IN_STORE is to specify whether product is available in store or
	 * not.
	 */
	public static final String AVAILABLE_IN_STORE = "availableInStore";

	/**
	 * AVAILABLE_ONLINE is to specify whether product is available in online or
	 * not.
	 */
	public static final String AVAILABLE_ONLINE = "availableOnline";

	/**
	 * NEW_ARRIVAL is to specify whether product is newly arrival or not.
	 */
	public static final String NEW_ARRIVAL = "p_newArrival";

	/**
	 * PRODUCT_NAME is the name of the product.
	 */
	public static final String PRODUCT_NAME = "productName";

	/**
	 * PRODUCT_CODE is the unique code of the product.
	 */
	public static final String PRODUCT_CODE = "productcode";

	/**
	 * LIST_PRICE is the listing price of the product.
	 */
	public static final String LIST_PRICE = "p_listPrice";

	/**
	 * LIST_PRICE_RANGE is range of the listing price of the product.
	 */
	public static final String LIST_PRICE_RANGE = "listPriceRange";

	/**
	 * MIN_LIST_PRICE is minimum price list of the product.
	 */
	public static final String MIN_LIST_PRICE = "minListPrice";
	/**
	 * MAX_LIST_PRICE is maximum price list of the product.
	 */
	public static final String MAX_LIST_PRICE = "maxListPrice";

	/**
	 * SALE_PRICE is sale price of the product.
	 */
	public static final String SALE_PRICE = "p_salePrice";
	/**
	 * SALE_PRICE_RANGE is sale price list of the product.
	 */
	public static final String SALE_PRICE_RANGE = "salePriceRange";
	/**
	 * MIN_SALE_PRICE is minimum sale price of the product.
	 */
	public static final String MIN_SALE_PRICE = "minSalePrice";
	/**
	 * MAX_SALE_PRICE is maximum sale price of the product.
	 */
	public static final String MAX_SALE_PRICE = "maxSalePrice";
	/**
	 * INVENTORY_LEVEL specifies the inventory level of the product.
	 */
	public static final String INVENTORY_LEVEL = "inventoryLevel";
	/**
	 * INVENTORY_AVAILABLE specifies whether the product is present in inventory
	 * or not.
	 */
	public static final String INVENTORY_AVAILABLE = "inventoryAvail";
	/**
	 * PRODUCT_MAIN_IMAGE_URL is image url of the product.
	 */
	public static final String PRODUCT_MAIN_IMAGE_URL = "productUrl";

	/**
	 * PRODUCT_SWATCH_URL is image url of the product swatch.
	 */
	public static final String PRODUCT_SWATCH_URL = "productSwatchUrl";

	/**
	 * PRODUCT_IMAGE_COUNT is image count.
	 */

	public static final String PRODUCT_IMAGE_COUNT = "productImageCount";

	/**
	 * IS_PATTERN specifies whether the product is pattern type or not.
	 */
	public static final String IS_PATTERN = "isPattern";

	/**
	 * ON_SALE is to specify whether the product is on sale or not.
	 */
	public static final String ON_SALE = "onSale";

	/**
	 * VENDOR_STYLE is vendor style number.
	 */
	public static final String VENDOR_STYLE = "vendorstyle";
	/**
	 * VENDOR_NUMBER is vendor number.
	 */
	public static final String VENDOR_ID = "vendorid";
	/**
	 * BRAND is the brand of the product.
	 */
	public static final String BRAND = "brand";
	/**
	 * CLEARANCE is to specify whether the product is available for clearance
	 * sale or not.
	 */
	public static final String CLEARANCE = "clearance";
	/**
	 * PRODUCT_SHORT_DESCRIPTION is the short description of the product.
	 */
	public static final String PRODUCT_SHORT_DESCRIPTION = "productshortdesc";

	/**
	 * PRODUCT_LONG_DESCRIPTION is the long description of the product.
	 */
	public static final String PRODUCT_LONG_DESCRIPTION = "productLongDesc";
	/**
	 * PATH is the product path.
	 */
	public static final String PATH = "path";

	/**
	 * BRIDAL_ELIGIBLE is to specify whether the product is bridal or not.
	 */
	public static final String BRIDAL_ELIGIBLE = "bridalEligible";
	/**
	 * DEPARTMENT_NUMBER is the department id.
	 */
	public static final String DEPARTMENT_NUMBER = "departmentNumber";
	/**
	 * CLASS_NUMBER is the class name.
	 */
	public static final String CLASS_NUMBER = "classNumber";
	/**
	 * SKU_CODE is the sku code of the product.
	 */
	public static final String SKU_CODE = "skuCode";
	/**
	 * SKU_UPC is the sku upc code of the product.
	 */
	public static final String SKU_UPC = "skuupc";
	/**
	 * SKU_ID is the sku id of the product.
	 */
	public static final String SKU_ID = "skuid";
	/**
	 * COLOR is the color of the product.
	 */
	public static final String COLOR = "color";
	/**
	 * COMP_SIZE is size of the product.
	 */
	public static final String COMP_SIZE = "compSize";
	/**
	 * PATTERN_CODE is pattern code of the product.
	 */
	public static final String PATTERN_CODE = "patternCode";
	/**
	 * SKU_SALE_PRICE is skus sale price of the product.
	 */
	public static final String SKU_SALE_PRICE = "skusSalePrice";
	/**
	 * PRODUCT_ID is unique code of the product.
	 */
	public static final String PRODUCT_ID = "productId"; // TODO: This is a
	// placeholder
	// item
	// currently
	/**
	 * WEB_ID is the web id.
	 */
	public static final String WEB_ID = "webId"; // TODO: This is a
	// placeholder item
	// currently
	/**
	 * PROMOTIONS is the promotion description of the product.
	 */
	public static final String PROMOTIONS = "promotions";
	/**
	 * IS_DROP_SHIP is the promotion description of the product.
	 */
	public static final String IS_DROP_SHIP = "is_drop_ship";
	/**
	 * EXTENDED_ATTRIBUTES is the extended attribute.
	 */
	public static final String EXTENDED_ATTRIBUTES = "extendedAttributes";
	/**
	 * PRODUCT_COPY_TEXT_1 is the text about product.
	 */
	public static final String PRODUCT_COPY_TEXT_1 = "productCopyText1";
	/**
	 * PRODUCT_COPY_TEXT_2 is the text about product.
	 */
	public static final String PRODUCT_COPY_TEXT_2 = "productCopyText2";
	/**
	 * PRODUCT_COPY_TEXT_3 is the text about product.
	 */
	public static final String PRODUCT_COPY_TEXT_3 = "productCopyText3";
	/**
	 * PRODUCT_COPY_TEXT_4 is the text of the product.
	 */
	public static final String PRODUCT_COPY_TEXT_4 = "productCopyText4";
	/**
	 * OVERALL_RATING is the text for the rating.
	 */
	public static final Object OVERALL_RATING = "rating"; // this is a place
	// holder. Once the
	// rating
	// implementation
	// has completed the
	// actual value to
	// be replaced
	/**
	 * SIZE_CODE is the text of the product.
	 */
	public static final String SIZE_CODE = "size_code";
	/**
	 * COLOR_CODE is the text of the product.
	 */
	public static final String COLOR_CODE = "color_code";
	/**
	 * DEFAULT_SKU is the text of the product.
	 */
	public static final String DEFAULT_SKU = "default_sku";
	/**
	 * SHOW_COLOR is the text of the product.
	 */
	public static final String SHOW_COLOR = "show_color";
	/**
	 * SHOW_SIZE is the text of the product.
	 */
	public static final String SHOW_SIZE = "show_size";
	
	/* HAS_DEAL_PROMO is to specify whether this product has a deal promo associated with it*/
	public static final String HAS_DEAL_PROMO = "hasDealPromo";

	/**
	 * MIN_COLOR_DESC and MAX_COLOR_DESC are used together to find whether a product has multiple colors
	 */
	public static final String MIN_COLOR_DESC = "minColorDesc";

	/**
	 * MIN_COLOR_DESC and MAX_COLOR_DESC are used together to find whether a product has multiple colors
	 */
	public static final String MAX_COLOR_DESC = "maxColorDesc";
}