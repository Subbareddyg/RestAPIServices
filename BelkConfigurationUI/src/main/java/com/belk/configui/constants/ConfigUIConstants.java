package com.belk.configui.constants;

/**
 * @author Mindtree
 * @date Mar 24, 2014 Class Name : ConfigUIConstants Description : This class
 *       includes list of constants used in the Belk Config UI
 */

public class ConfigUIConstants {

	public static final String CONFIGURATION_PROPERTY = "configuration";
	// Symbols used to split or join file name and parameter list
	public static final String SLASH = "/";
	public static final String COLON = ":";
	public static final String PIPE_LINE = "|";
	public static final String EQUALS = "=";
	public static final String COMMA = ",";
	public static final String BLANK = "";

	// Request mapping methods
	public static final String GET_MODULE_LIST = "getModuleList";
	public static final String GET_FILE_LIST = "getFileList";
	public static final String GET_PROP = "getProperty";
	public static final String PROCEED = "proceedToUpdate";
	/*
	 * Commented out as validation framework related changes are not required as
	 * of now
	 */
	/*
	 * public static final String GET_VALIDATION_PROP = "getValidationProperty";
	 * public static final String PROCEED_VALIDATION =
	 * "proceedToUpdateValidation";
	 */

	// JSP file names
	public static final String MODULE_LIST = "moduleList";
	public static final String FILE_LIST = "fileList";
	public static final String PROP_DETAILS = "propDetails";
	public static final String API_UPDATE = "apiUpdate";
	/*
	 * Commented out as validation framework related changes are not required as
	 * of now
	 */
	/*
	 * public static final String VALIDATION_FILE_LIST = "validationFileList";
	 * public static final String VALIDATION_PROP_DETAILS =
	 * "validationPropDetails"; public static final String VALIDATION_API_UPDATE
	 * = "validationApiUpdate";
	 */
	public static final String ERROR_PAGE = "errorPage";
	public static final String LOGIN_PAGE = "loginPage";

	// API names
	public static final String PRODUCT_SEARCH = "ProductSearch";
	public static final String PRODUCT_DETAILS = "ProductDetails";
	public static final String CATEGORY_PROD = "CategoryProduct";
	public static final String CATEGORY_DETL = "CategoryDetails";
	public static final String PATTERN_PROD_DETL = "PatternProductDetails";
	public static final String CATALOG = "Catalog";
	public static final String ADMIN = "Admin";
	public static final String PRODUCT_DATA = "ProductData";

	// Module names
	public static final String ADAPTER = "Adapter";
	public static final String COMMON = "Common";
	public static final String DOMAIN = "Domain";
	public static final String BLUE_MARTINI = "BlueMartini";
	public static final String LOG = "LOG";

	// Property File names present
	public static final String ENDECA_CONF = "endeca_conf.properties";
	public static final String ENDECA_FIELD_LIST_CONF = "endecaFieldList_conf.properties";
	public static final String HTTP_CONNECTION_CONF = "httpConnection_conf.properties";
	/*
	 * Commented out as validation framework related changes are not required as
	 * of now
	 */
	/* public static final String VALIDATION = "Validation"; */

	// String values used
	public static final String MODULE_NAME = "moduleName";
	public static final String LOCATION = "location";
	public static final String FILE_PATH = "filePath";
	public static final String FILE_NAME = "fileName";
	public static final String API_NAMES = "apiNames";
	public static final String PROPERTIES = "properties";
	public static final String PROP_LIST = "propList";
	public static final String UPDATE_LOG_LEVEL = "updateLogLevel";
	public static final String LOG_LEVEL = "logLevel";
	public static final String LOGGED_IN_USER_NAME = "loggedInUserName";
	public static final String RELOAD_CHOICE = "reloadChoice";
	/*
	 * Commented out as validation framework related changes are not required as
	 * of now
	 */
	/* public static final String CONTENT = "content"; */

	// String values for Exception message

	public static final String FILE_NOT_FOUND = "File not found at the location";
	public static final String IO_EXCEPTION = "An IO Exception has occurred";
	public static final String LOGIN = "login";

	// Log levels
	public static final String LOG_FATAL = "fatal";
	public static final String LOG_DEBUG = "debug";
	public static final String LOG_WARN = "warn";
	public static final String LOG_INFO = "info";
	public static final String LOG_ERROR = "error";
	public static final String LOG_LEVEL_LIST = "logLevelList";

	// Module base urls
	public static final String PRODUCT_SEARCH_URL = "/productsearch/v1/products/search";
	public static final String PRODUCT_DETAILS_URL = "/productdetails/v1/products";
	public static final String CATEGORY_PRODUCT_URL = "/categoryproduct/v1/categories";
	public static final String CATEGORY_DETAILS_URL = "/categorydetails/v1/categories";
	public static final String PATTERN_PRODUCTS_URL = "/patternproductdetails/v1/childpattern";
	public static final String CATALOG_URL = "/catalog/v1/catalog";
	public static final String ADMIN_URL = "/admin/v1/admin";
	public static final String PRODUCT_DATA_URL = "/productdata/v1/products/recommendations";

	public static final String CONFIG_UI_APP_ID_ENCRYPTED = "configUIAppIdEncrypted";
	public static final String CONFIG_UI_APP_ID = "configUIAppId";
	public static final String CONFIG_UI_ENCRYPTION_KEY = "configUIAppEncryptionKey";
	public static final String SESSION_AUTH_TOKEN = "sessionAuthToken";
	public static final String SECURITY_API_ENDPOINT = "securityApiEndpointURL";
	

}
