package com.belk.api.constants;

/**
 * This class contains Common constants which are referred by the entire
 * application.
 * 
 * Updated: some of the unused constants have been removed as part of the
 * phase2, April,14 release
 * 
 * @author Mindtree
 * @date Jul 23, 2013
 */
public final class CommonConstants {
	/**
	 * LOGGER_ENTERING specifies entry to method.
	 */
	public static final String LOGGER_ENTERING = "Entering";
	/**
	 * LOGGER_EXITING specifies exit from method.
	 */
	public static final String LOGGER_EXITING = "Exiting";
	/**
	 * CONTENT_TYPE specifies content type.
	 */
	public static final String CONTENT_TYPE = "Content-Type";
	/**
	 * CONTENT_TYPE_VALUE_XML specifies that the content type is XML.
	 */
	public static final String CONTENT_TYPE_VALUE_XML = "application/xml;charset=UTF-8";
	/**
	 * CONTENT_TYPE_VALUE_JSON specifies that the content type is JSON.
	 */
	public static final String CONTENT_TYPE_VALUE_JSON = "application/json;charset=UTF-8";
	/**
	 * MEDIA_TYPE_JSON specifies whether the media type is JSON or not.
	 */
	public static final String MEDIA_TYPE_JSON = "JSON";
	/**
	 * MEDIA_TYPE_XML specifies the media type.
	 */
	public static final String MEDIA_TYPE_XML = "XML";

	/**
	 * STATUS_OK specifies whethe the status is 200 or not.
	 */
	public static final int STATUS_OK = 200;
	/**
	 * ALLOWED_MEDIA_TYPE specifies JSON,XML are the allowed media types.
	 */
	public static final String ALLOWED_MEDIA_TYPE = "XML,JSON";
	/**
	 * REQUEST_TYPE specifies request type.
	 */
	public static final String REQUEST_TYPE = "format";
	/**
	 * CONFIGURATION_UPDATE_SUCCESS specifies success message after reloading
	 * property map in configuration.
	 */
	public static final String CONFIGURATION_UPDATE_SUCCESS = "The configurations change get updated successfully";
	/**
	 * CONFIGURATIONS_RESOURCE_BUNDLE specifies resource bundle of
	 * configurations.
	 */
	public static final String CONFIGURATIONS_RESOURCE_BUNDLE = "configurations";
	/**
	 * ENDECA_RESOURCE_BUNDLE specifies resource bundle.
	 */
	public static final String ENDECA_RESOURCE_BUNDLE = "endeca.properties";
	/**
	 * DOMAIN_RESOURCE_BUNDLE specifies the resource bundle of domain.
	 */
	public static final String DOMAIN_RESOURCE_BUNDLE = "domain.properties";
	/**
	 * ENDECA_FIELDLIST_RESOURCE_BUNDLE specifies field list resource bundle.
	 */
	public static final String ENDECA_FIELDLIST_RESOURCE_BUNDLE = "endecaFieldList.properties";

	/**
	 * ENDECA_REQUESTLIST_RESOURCE_BUNDLE specifies field list resource bundle.
	 */
	
	public static final String ENDECA_REQUESTLIST_RESOURCE_BUNDLE = "endecaRequestList.properties";
	
	/**
	 * DB_CONNECTION_RESOURCE_BUNDLE specifies field list resource bundle.
	 */
	
	public static final String DB_CONNECTION_RESOURCE_BUNDLE = "dbConnection.properties";
	
	/**
	 * COREMETRICS_RESOURCE_BUNDLE specifies field list resource bundle.
	 */
	
	public static final String PRODUCT_DATA_RESOURCE_BUNDLE = "productData.properties";
	
	/**
	 * ERROR_RESOURCE_BUNDLE specifies the resource bundle of errors.
	 */
	public static final String ERROR_RESOURCE_BUNDLE = "error.properties";
	/**
	 * CACHE_RESOURCE_BUNDLE specifies the resource bundle field memcached.
	 */
	public static final String CACHE_RESOURCE_BUNDLE = "memcached.properties";
	/**
	 * COMMA_SEPERATOR specifies the symbol ",".
	 */
	public static final String COMMA_SEPERATOR = ",";
	/**
	 * TILDE_SEPERATOR specifies the symbol "~".
	 */
	public static final String TILDE_SEPERATOR = "~";
	/**
	 * EIPHEN_SEPERATOR specifies the symbol "-".
	 */
	public static final String EIPHEN_SEPERATOR = "-";
	/**
	 * UNDERSCORE specifies the symbol "_".
	 */
	public static final String UNDERSCORE = "_";
	/**
	 * EMPTY_STRING specifies the empty string.
	 */
	public static final String EMPTY_STRING = "";
	/**
	 * Null string
	 */
	public static final String NULL_STRING = "null";
	/**
	 * EQUALS_SIGN specifies the symbol "=".
	 */
	public static final String EQUALS_SIGN = "=";
	/**
	 * PIPE_SEPERATOR specifies the symbol "|".
	 */
	public static final String PIPE_SEPERATOR = "|";

	/**
	 * DOUBLE_BACK_SLASH specifies the symbol "\\".
	 */
	public static final String DOUBLE_BACK_SLASH = "\\";

	/**
	 * DOUBLE_PIPE_SEPERATOR specifies the symbol "||".
	 */
	public static final String DOUBLE_PIPE_SEPERATOR = "||";
	/**
	 * UTF_TYPE specifies the unique utf type.
	 */
	public static final String UTF_TYPE = "UTF-8";

	/**
	 * FIELD_PAIR_SEPARATOR specifies the symbol ":".
	 */
	public static final String FIELD_PAIR_SEPARATOR = ":";
	/**
	 * FIELD_PAIR_MAPPER_SEPARATOR specifies the symbol "^".
	 */
	public static final String FIELD_PAIR_MAPPER_SEPARATOR = "^";
	/**
	 * COLOR specifies the color.
	 */
	public static final String COLOR = "color";
	/**
	 * SIZE specifies the size.
	 */
	public static final String SIZE = "size";
	/**
	 * BRAND specifies the brand.
	 */
	public static final String BRAND = "brand";
	/**
	 * PRICE specifies the price.
	 */
	public static final String PRICE = "price";
	/**
	 * TRUE_VALUE specifies whether the value is true or not.
	 */
	public static final String TRUE_VALUE = "true";
	/**
	 * FALSE_VALUE specifies whether the value is false or not.
	 */
	public static final String FALSE_VALUE = "false";
	/**
	 * YES_VALUE specifies whether the value is yes or not.
	 */
	public static final String YES_VALUE = "Yes";
	/**
	 * NO_VALUE specifies whether the value is no or not.
	 */
	public static final String NO_VALUE = "No";
	/**
	 * FLAG_YES_VALUE specifies whether the value is true or not.
	 */
	public static final String FLAG_YES_VALUE = "T";
	/**
	 * FLAG_NO_VALUE specifies whether the value is false or not.
	 */
	public static final String FLAG_NO_VALUE = "F";
	/**
	 * COUNT specifies count value.
	 */
	public static final String COUNT = "count";
	/**
	 * DESC_SORT_ORDER specifies the sort order.
	 */
	public static final String DESC_SORT_ORDER = "1";
	/**
	 * ASC_SORT_ORDER specifies the sort order.
	 */
	public static final String ASC_SORT_ORDER = "0";
	// specific to Catalog API for root as category

	/**
	 * TYPE_OF_QUERY_REQUIRED specifies the type of query required.
	 */
	public static final String TYPE_OF_QUERY_REQUIRED = "typeRequired";

	/**
	 * SUB_CATEGORY specifies the sub category.
	 */
	public static final String SUB_CATEGORY = "subcategory";
	/**
	 * CORRELATION_ID specifies the Correlation-Id.
	 */
	public static final String CORRELATION_ID = "Correlation-Id";
	/**
	 * STATIC_CORRELATION_ID specifies the Correlation-Id.
	 */
	public static final String STATIC_CORRELATION_ID = "STATIC_CORRELATION_ID";
	/**
	 * EMPTY_VALUE specifies empty string.
	 */
	public static final String EMPTY_VALUE = " ";
	/**
	 * AND_VALUE specifies the value "AND".
	 */
	public static final String AND_VALUE = "AND";
	/**
	 * OR_VALUE specifies the value "OR".
	 */
	public static final String OR_VALUE = "OR";
	/**
	 * KEY_PATTERN specifies the pattern of the key.
	 */
	// Added for CacheManager class
	public static final String KEY_PATTERN = "cacheObject\\.(.+)\\.className";
	/**
	 * CACHE_OBJECT specifies whether the object is cache object or not.
	 */
	public static final String CACHE_OBJECT = "cacheObject";
	/**
	 * DOT specifies the symbol ".".
	 */
	public static final String DOT = ".";
	/**
	 * PREPEND specifies the prepend field.
	 */
	public static final String PREPEND = "prepend";
	/**
	 * EXPIRATION specifies expiration field.
	 */
	public static final String EXPIRATION = "expiration";
	/**
	 * SERVICES specifies services field.
	 */
	public static final String SERVICES = "Services";
	/**
	 * AVAILABLE specifies the available field.
	 */
	public static final String AVAILABLE = "available";
	/**
	 * PATTERN_PRODUCT_DETAILS_LOGGER specifies the ProductDetails field.
	 */
	public static final String PATTERN_PRODUCT_DETAILS_LOGGER = "PatternProductDetails";
	/**
	 * PATTERN_PRODUCT_DETAILS_LOGGER specifies the ProductDetails field.
	 */
	public static final String PRODUCT_CODE_REQUEST_MAP_APIURL = "productcode";
	/**
	 * PATTERN_PRODUCT_DETAILS_LOGGER specifies the ProductDetails field.
	 */
	public static final String PRODUCT_CODE_REQUEST_MAP_BLUEMARTINI = "prdCode";

	/**
	 * CATALOG_ID specifies the catid field.
	 */
	public static final String CATALOG_ID = "catid";

	/**
	 * PRODUCT_SEARCH_LOGGER specifies the product search logger.
	 */
	public static final String PRODUCT_SEARCH_LOGGER = "ProductSearch";
	/**
	 * PRODUCT_DETAILS_LOGGER specifies the product details logger.
	 */
	public static final String PRODUCT_DETAILS_LOGGER = "ProductDetails";
	/**
	 * CATEGORY_PRODUCT_LOGGER specifies the category product logger.
	 */
	public static final String CATEGORY_PRODUCT_LOGGER = "CategoryProduct";
	/**
	 * CATEGORY_DETAILS_LOGGER specifies the category details logger.
	 */
	public static final String CATEGORY_DETAILS_LOGGER = "CategoryDetails";
	/**
	 * CATALOG_LOGGER specifies the catalog logger.
	 */
	public static final String CATALOG_LOGGER = "Catalog";
	/**
	 * ADMIN_LOGGER specifies the admin logger.
	 */
	public static final String ADMIN_LOGGER = "Admin";
	/**
	 * CONFIGURATION_WEB_APP_LOGGER the configuration UI logger.
	 */
	public static final String CONFIGURATION_WEB_APP_LOGGER = "configuration_update";

	/**
	 * PAYLOAD_MESSAGE_TYPE specifies payloadMessageType field.
	 */
	public static final String PAYLOAD_MESSAGE_TYPE = "payloadMessageType";

	/**
	 * PAYLOAD_MESSAGE_TYPE specifies payloadMessageType field.
	 */
	public static final String PAYLOAD_MESSAGE_TYPE_VALUE = "HTTP";

	/**
	 * PAYLOAD_MESSAGE_DOMAIN specifies payloadMessageDomain field.
	 */
	public static final String PAYLOAD_MESSAGE_DOMAIN = "payloadMessageDomain";

	/**
	 * PAYLOAD_MESSAGE_CORRELATIONID specifies payloadMessageCorrelationId
	 * field.
	 */
	public static final String PAYLOAD_MESSAGE_CORRELATIONID = "payloadMessageCorrelationId";

	/**
	 * PAYLOAD_MESSAGE_CCSID specifies payloadMessageCharacterIdentifier field.
	 */
	public static final String PAYLOAD_MESSAGE_CCSID = "payloadMessageCCSID";

	/**
	 * PAYLOAD_MESSAGE_ENCODING specifies payloadMessageEncoding field.
	 */
	public static final String PAYLOAD_MESSAGE_ENCODING = "payloadMessageEncoding";

	/**
	 * PAYLOAD_MESSAGE_PARENT_CORRELATIONID specifies
	 * payloadMessageParentCorrelationId field.
	 */
	public static final String PAYLOAD_MESSAGE_PARENT_CORRELATIONID = "payloadMessageParentCorrelationId";

	/**
	 * PAYLOAD_MESSAGE_HEADER specifies payloadMessageHeader field.
	 */
	public static final String PAYLOAD_MESSAGE_HEADER = "payloadMessageHeader";

	/**
	 * PAYLOAD_MESSAGE_BODY specifies payloadMessageBody field.
	 */
	public static final String PAYLOAD_MESSAGE_BODY = "payloadMessageBody";
	/**
	 * ENCRYPTION specifies file.encoding field.
	 */
	public static final String ENCRYPTION = "file.encoding";
	/**
	 * UCS_TRANSFORMATION_FORMAT specifies UTF-8 field.
	 */
	public static final String UCS_TRANSFORMATION_FORMAT = "UTF-8";

	/**
	 * TOTAL_PRODUCTS specifies the total number of products.
	 */
	public static final String TOTAL_PRODUCTS = "totalProducts";
	/**
	 * TOTAL_SKUS specifies total skus products.
	 */
	public static final String TOTAL_SKUS = "totalSkus";

	/**
	 * PRODUCT_SEARCH specifies the product search service name.
	 */
	public static final String PRODUCT_SEARCH = "PRODUCT_SEARCH";

	/**
	 * PRODUCT_DETAILS specifies the product details service name.
	 */
	public static final String PRODUCT_DETAILS = "PRODUCT_DETAILS";

	/**
	 * CATALOG specifies the catalog service name.
	 */
	public static final String CATALOG = "CATALOG";

	/**
	 * CATEGORY_DETAILS specifies the category details service name.
	 */
	public static final String CATEGORY_DETAILS = "CATEGORY_DETAILS";

	/**
	 * CATEGORY_PRODUCT specifies the category product service name.
	 */
	public static final String CATEGORY_PRODUCT = "CATEGORY_PRODUCT";

	/**
	 * ZERO_VALUE specifies the value zero.
	 */
	public static final String ZERO_VALUE = "0";

	/**
	 * COMMON_RESOURCE_BUNDLE specifies the resource bundle of common.
	 */
	public static final String COMMON_RESOURCE_BUNDLE = "common";

	/**
	 * ACCESS_CONTROL_ALLOW_ORIGIN specifies access control allow origin header
	 * type.
	 */
	public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

	/**
	 * ALL_VALUES specifies all the values.
	 */
	public static final String ALL_VALUES = "*";

	// these constants are related to configuration framework.
	/**
	 * FILE_NAME specifies the name of file which needs to be updated.
	 */
	public static final String FILE_NAME = "filename";
	/**
	 * PROPERTIES_LIST specifies the list of values that need to be updated.
	 */
	public static final String PROPERTIES_LIST = "propertieslist";
	/**
	 * RELOAD_DEFAULT_VALUES specifies the flag passed in request for
	 * configuration framework.
	 */
	public static final String RELOAD_DEFAULT_VALUES = "reloaddefaultvalues";
	/**
	 * LOG_LEVEL specifies the level which needs to be set for logger.
	 */
	public static final String LOG_LEVEL = "loglevel";
	/**
	 * COMMON specifies the common project name.
	 */
	public static final String COMMON = "common";
	/**
	 * DOMAIN specifies the domain project name.
	 */
	public static final String DOMAIN = "domain";
	/**
	 * ADAPTER specifies the adapter project name.
	 */
	public static final String ADAPTER = "adapter";
	/**
	 * BLUE_MARTINI_ADAPTER specifies the bluemartini adapter project name.
	 */
	public static final String BLUE_MARTINI_ADAPTER = "bluemartiniadapter";

	public static final String RELOAD_VALIDATION_CONFIG = "reloadvalidationconfig";
	/**
	 * HAS_SUBCATEGORIES specifies whether there are sub categories.
	 */
	public static final String HAS_SUBCATEGORIES = "hasSubCategories";
	/**
	 * KEYWORD specifies the presence of keyword.
	 */
	public static final String KEYWORD = "keyword";

	/**
	 * CATEGORY_CACHE_KEY specifies the key to be containing the value of the
	 * category key in the cache.
	 */
	public static final String CATEGORY_CACHE_KEY = "categoryCacheKey";

	public static final String PRIMARY_CACHE_KEY = "APICacheHolder";
	
	public static final String PRIMARY_CACHE_EXPIRATION = "primaryCacheExpiration";
	/**
	 * UNIQUE_CORRELATION_ID specifies the unique correlation id.
	 */
	public static final String UNIQUE_CORRELATION_ID = "0000-1111-2222-3333-AAAA-BBBB";

	// For mail
	public static final String EMAIL_SENDER = "FROM_ID";
	public static final String EMAIL_SMTP_HOST = "SMTP_HOST";
	public static final String EMAIL_SMTP_PORT = "SMTP_PORT";
	public static final String EMAIL_RECEIVER = "TO_ID";
	public static final String EMAIL_SUBJECT = "SUBJECT";
	public static final String CONFIGURATION_UPDATE_FAILURE = "FAILURE_MESSAGE";
	public static final String RELOADING_MESSAGE_BODY = "RELOADING_MESSAGE";
	public static final String MULTI_PROPERTY_CHANGE_MESSAGE_BODY = "MULTI_PROPERTY_CHANGE_MESSAGE";
	public static final String SUCCESS = "success";
	public static final String FAILURE = "failure";
	public static final String MAIL_DATE_FORMAT = "MM/dd/yyyy HH:mm:ss.SSS zzz";
	public static final String NEW_LINE_CHARACTER = "\n";
	public static final String MAIL_RESOURCE_BUNDLE = "mail.properties";
	public static final String MAIL_SMTP_HOST_KEY = "mail.smtp.host";
	public static final String MAIL_SMTP_PORT_KEY = "mail.smtp.port";
	public static final String CONTENT_TYPE_TEXT_HTML = "text/html";
	public static final String MAIL_PRIORITY_KEY = "X-Priority";
	public static final String MAIL_PRIORITY_HIGH = "1";
	public static final String CONFIG_UI_URL = "baseURL.properties";
	public static final String CONFIGURATION_UI_RESOURCE_BUNDLE = "configuration";
	public static final String UPDATED_BY = "updatedby";
	public static final String UPDATED_BY_MESSAGE = "UPDATED_BY";
	public static final String UPDATED_BY_API = "Updated by API";
	public static final String EMAIL_CC_RECEIVER = "CC_ID";
	public static final String SERVER_IP = "SERVER_IP";
	public static final String UPDATED_SERVER = "UPDATED_SERVER";
	public static final String LOG_LEVEL_UPDATE_MESSAGE = "LOG_LEVEL_UPDATE_MESSAGE";
	public static final String UPDATED_SERVICE = "UPDATED_SERVICE";
	public static final String PATTERN_DETAILS_URL = "PATTERN_DETAILS_URL";
	public static final String HTTP_PREPEND_STRING = "http://";
	public static final String AMPERSAND = "&";
	public static final String OPTIONS = "options";
	public static final String ROOT_NODES = "root_nodes";
	public static final String ENCRYPTED_APP_ID = "encryptedAppId";
	public static final String ENCRYPTED_KEY_PARAMS = "encryptedKeyParams";
	public static final String COREMETRICS_BODY = "COREMETRICS_MAIL_BODY";
	public static final String COREMETRICS_SUBJECT = "COREMETRICS_MAIL_SUBJECT";
	

	/* Security API related properties */
	public static final String COMMON_ENCRYPTION_KEY = "CEK";
	public static final String SECURITY_RESOURCE_BUNDLE = "security.properties";
	public static final String SECURITY = "Security";
	public static final String ENCRYPTION_KEY_SUFFIX = "_EK";
	public static final String MAX_TOKEN_GAP_TIME = "MAX_TOKEN_GAP_TIME";
	public static final String TOKEN_EXPIRY_TIME = "TOKEN_EXPIRY_TIME";
	public static final String GET_REQUEST = "GET";
	public static final String SECURITY_LOGGER = "Security";
	/**
	 * SECURITY_BINDING specifies the binding file for security API.
	 */
	public static final String SECURITY_BINDING = "security-binding";
	/**
	 * AUTH_TOKEN_ELEMENT_STRING specifies the name of the xml element as
	 * defined in the security-binding.xml. This needs to be kept in sync
	 * always.
	 */
	public static final String AUTH_TOKEN_ELEMENT_STRING = "authToken";
	public static final String CIPHER_ALGORITHM_DES = "DES";
	public static final String CIPHER_TRANSFORMATION_DES_ECB_PKCS5PADDING = "DES/ECB/PKCS5Padding";
	//Added the following constant to address the root category in API-351
	public static final String ROOT = "root";

	public static final String TARGET_PRODUCT_ID = "TARGET_PRODUCT_ID";
	public static final String RECOMMENDATION = "RECOMMENDATION";
	public static final String PRODUCT = "PRODUCT";
	public static final String COREMETRICS_KEY_SET = "COREMETRICS_KEYS_PDP";
	public static final String PAGE_TYPE = "PAGE_TYPE";
	public static final String PRODUCT_PAGE = "ProductPage";
	public static final String MAX_RECOMMENDATION_FETCH = "MAX_RECOMMENDATION_FETCH";
	
	public static final String BM_DB_DRIVER = "BM_DB_DRIVER";
	public static final String BM_DB_URL = "BM_DB_URL";
	public static final String BM_DB_USER_NAME = "BM_DB_USER_NAME";
	public static final String BM_DB_PASSWORD = "BM_DB_PASSWORD";
	public static final String BM_PASSWORD_KEY = "BM_PASSWORD_KEY";
	
	public static final String AES = "AES";
	public static final String COREMETRICS = "COREMETRICS";
	public static final String CART = "CART";
	public static final String COREMETRICS_LOGGER = "ProductData";
	public static final String ENDECA_RESPONSE_FIELDLIST_RESOURCE_BUNDLE = "endecaResponseFieldList.properties";
}
	