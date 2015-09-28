/**
 * 
 */
package com.belk.api.constants;

/**
 * The Endeca specific constants which are used in the adapter layer.
 * 
 * @author Mindtree
 * @date Feb 28, 2014
 * 
 */
public final class EndecaConstants {
	/**
	 * PRODUCT_CODE specifies the code of product.
	 */
	public static final String PRODUCT_CODE = "P_product_code";
	/**
	 * BRAND is the product's brand.
	 */
	public static final String BRAND = "P_brand";
	/**
	 * COLOR specifies the color of the product.
	 */
	public static final String COLOR = "P_color";
	/**
	 * CATEGORY_ID is the unique id.
	 */
	public static final String CATEGORY_ID = "N";
	/**
	 * LIMIT is the limit field.
	 */
	public static final String LIMIT = "limit";
	/**
	 * OFFSET is the offset field.
	 */
	public static final String OFFSET = "offset";
	/**
	 * SORT is the sort field.
	 */
	public static final String SORT = "sort";
	/**
	 * DIM is the dim value.
	 */
	public static final String DIM = "dim";
	/**
	 * SALE_PRICE is the sale price of the product.
	 */
	public static final String SALE_PRICE = "SORTKEY_SALEPRICE";
	/**
	 * PRODUCT_SALE_PRICE is the sale price of the product.
	 */
	public static final String PRODUCT_SALE_PRICE = "P_saleprice";
	/**
	 * INVENTORY is the inventory of the product.
	 */
	public static final String INVENTORY = "SORTKEY_INVENTORY_AVAILABLE";
	/**
	 * NAME is the product name.
	 */
	public static final String NAME = "P_product_name";
	/**
	 * LIST_PRICE is the list price of the product.
	 */
	public static final String LIST_PRICE = "SORTKEY_HIGHPRICE";
	/**
	 * NEW_ARRIVAL is to specify whether it is new arrival or not.
	 */
	public static final String NEW_ARRIVAL = "SORTKEY_NEWARRIVAL";
	/**
	 * BEST_SELLER is to specify whether it is best seller or not.
	 */
	public static final String BEST_SELLER = "SORTKEY_BEST_SELLER";
	/**
	 * WISH_LIST_FAVORITES is the wish list field field.
	 */
	public static final String WISH_LIST_FAVORITES = "SORTKEY_MOST_WISHED";
	/**
	 * TOP_RATING is the top rating field.
	 */
	public static final String TOP_RATING = "SORTKEY_PRD_RATING";
	/**
	 * FILTER_PRICE is the filter price of the product.
	 */
	public static final String FILTER_PRICE = "P_skus_sale_price";
	/**
	 * ROLL_UP_KEY is the key value.
	 */
	public static final String ROLL_UP_KEY = "ROLLUPKEY";
	/**
	 * DIM_REFINMENT_COUNT specifies the dim count.
	 */
	public static final String DIM_REFINMENT_COUNT = "DGraph.Bins";
	/**
	 * DEFAULT_NAV_EREC_PER_AGGR_EREC specifies flag used by endeca to fetch
	 * exactly one record.
	 */
	public static final int DEFAULT_NAV_EREC_PER_AGGR_EREC = 1;
	/**
	 * PATH specifies the path.
	 */
	public static final String PATH = "P_path";
	/**
	 * CATEGORY_DIMENSION is the dimension of the category.
	 */
	public static final String CATEGORY_DIMENSION = "category";
	/**
	 * PRODUCT_ID is the unique id of the product.
	 */
	public static final String PRODUCT_ID = "productId";
	/**
	 * WEB_ID is the unique web id.
	 */
	public static final String WEB_ID = "webId";
	/**
	 * DEFAULT_OFFSET specifies the default offset value.
	 */
	public static final String DEFAULT_OFFSET = "ENDECA_DEAFULT_OFFSET";
	/**
	 * DEFAULT_ROOT_DIM specifies the default value of the dim field.
	 */
	public static final String DEFAULT_ROOT_DIM = "ENDECA_DEAFULT_ROOTDIM";
	/**
	 * DEFAULT_LIMIT specifies the default value of the limit field.
	 */
	public static final String DEFAULT_LIMIT = "ENDECA_DEAFULT_LIMIT";
	/**
	 * MULTI_VALUE_ENABLED is to specify whether the multi value enabled or not.
	 */
	public static final String MULTI_VALUE_ENABLED = "MULTIVALUE_ENABLED";

	/**
	 * WILD_CARD_ALL specifies the wild card symbol.
	 */
	public static final String WILD_CARD_ALL = "*";
	/**
	 * CATALOG_ROOT_DIMENSION_VALUE specifies root dimension id.
	 */
	public static final String CATALOG_ROOT_DIMENSION_VALUE = "CATALOG_ROOT_DIMENSION_VALUE";
	/**
	 * CATALOG_HOME_DIMENSION_VALUE specifies home dimension id.
	 */
	public static final String CATALOG_HOME_DIMENSION_VALUE = "CATALOG_HOME_DIMENSION_VALUE";

	/**
	 * ROOT_DIMENSION_KEY specifies the dimension key.
	 */
	public static final String ROOT_DIMENSION_KEY = "rootDimension";

	/**
	 * DIMENSION_QUERY specifies whether the query is dimension query or not.
	 */
	public static final String DIMENSION_QUERY = "dimension";

	/**
	 * NAVERECS_PER_AGGREREC specifies navERecsPerAggrERec field.
	 */
	// Added For NavERecsPerAggrERec
	public static final String NAVERECS_PER_AGGREREC = "navERecsPerAggrERec";

	/**
	 * FLAG_VALUE_NOPRODUCTS specifies flag value 0.
	 */
	public static final int FLAG_VALUE_NOPRODUCTS = 0;
	/**
	 * FLAG_VALUE_ONEPRODUCT specifies the flag value 1.
	 */
	public static final int FLAG_VALUE_ONEPRODUCT = 1;
	/**
	 * FLAG_VALUE_ALLPRODUCTS specifies the flag value 2.
	 */
	public static final int FLAG_VALUE_ALLPRODUCTS = 2;
	/**
	 * BETWEEN specifies range.
	 */
	public static final String BETWEEN = "BTWN";

	/**
	 * ENDECA_FIELDLIST_RESOURCE_BUNDLE specifies field list resource bundle.
	 */
	public static final String ENDECA_FIELDLIST_RESOURCE_BUNDLE = "endecaFieldList.properties";
	/**
	 * REFINEMENTS_REQUIRED specifies whether the refinement is required or not.
	 */
	public static final String REFINEMENTS_REQUIRED = "refinementsRequired";
	/**
	 * REFINEMENTS_REQUIRED_LEVEL specifies the refinements Required Level.
	 */
	public static final String REFINEMENTS_REQUIRED_LEVEL = "refinementsRequiredLevel";
	// key names from endeca property file.
	/**
	 * HOST specifies the host for datasource.
	 */
	public static final String HOST = "HOST";
	/**
	 * PORT specifies the port for datasource.
	 */
	public static final String PORT = "PORT";
	/**
	 * ENDECA_DEAFULT_ROOTDIM specifies default root dimension.
	 */
	public static final String ENDECA_DEAFULT_ROOTDIM = "ENDECA_DEAFULT_ROOTDIM";
	/**
	 * ENDECA_DEAFULT_OFFSET specifies default offset.
	 */
	public static final String ENDECA_DEAFULT_OFFSET = "ENDECA_DEAFULT_OFFSET";
	/**
	 * ENDECA_DEAFULT_LIMIT specifies default limit.
	 */
	public static final String ENDECA_DEAFULT_LIMIT = "ENDECA_DEAFULT_LIMIT";
	/**
	 * MULTIVALUE_ENABLED specifies whether multi selection is enabled for filed
	 * or not.
	 */
	public static final String MULTIVALUE_ENABLED = "MULTIVALUE_ENABLED";
	/**
	 * MATCH_MODE specifies match mode in datasource.
	 */
	public static final String MATCH_MODE = "MATCH_MODE";
	/**
	 * CATALOG_ID specifies catalog id value.
	 */
	public static final String CATALOG_ID = "catalogId";
	/**
	 * NAVIGATION_ZONE specifies NavigationPageZone value.
	 */
	public static final String NAVIGATION_ZONE = "NavigationPageZone";
	/**
	 * SEARCH_ZONE specifies SearchPageZone value.
	 */
	public static final String SEARCH_ZONE = "SearchPageZone";
	/**
	 * CENTER_COLUMN specifies centerColumn value.
	 */
	public static final String CENTER_COLUMN = "centerColumn";
	/**
	 * LEFT_COLUMN specifies leftColumn value.
	 */
	public static final String LEFT_COLUMN = "leftColumn";
	/**
	 * RECORDS_BNB specifies RecordsBNB value.
	 */
	public static final String RECORDS_BNB = "RecordsBNB";
	/**
	 * RECORDS_BNB_NOSORT specifies RecordsBNBNoSort value.
	 */
	public static final String RECORDS_BNB_NOSORT = "RecordsBNBNoSort";
	/**
	 * NAVIGATION_RECORDS specifies navigation_records value.
	 */
	public static final String NAVIGATION_RECORDS = "navigation_records";
	/**
	 * FACETDISPLAY_LABEL specifies facetDisplayLabel value.
	 */
	public static final String FACETDISPLAY_LABEL = "facetDisplayLabel";
	/**
	 * REFINEMENTS specifies refinements value.
	 */
	public static final String REFINEMENTS = "refinements";
	/**
	 * REFINEMENTS specifies refinements value.
	 */
	public static final String NR_FOR_SEARCH_PRIMARY = "AND(Category_Path,NOT(P_IS_PRODUCT_SEARCHABLE:N))";
		
	/**
	 * Default constructor.
	 */
	private EndecaConstants() {

	}

}
