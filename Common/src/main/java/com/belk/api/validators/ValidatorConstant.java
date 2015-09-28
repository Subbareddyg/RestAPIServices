package com.belk.api.validators;

/**
 * This interface contains the constants which is used by the validators.
 * 
 * @author Mindtree
 * 
 */

public interface ValidatorConstant {

	/**
	 * NULL_CHECK_CODE specify the null check code.
	 */
	int NULL_CHECK_CODE = 1;

	/**
	 * NULL_CHECK_MES specify the parameter is null message.
	 */
	String NULL_CHECK_MESSAGE = "Value is null";

	/**
	 * EMPTY_CHECK_CODE specify the empty check code.
	 */
	int EMPTY_CHECK_CODE = 2;

	/**
	 * EMPTY_CHECK_MES specify the empty check message.
	 */
	String EMPTY_CHECK_MESSAGE = "Value is empty";

	/**
	 * NUMBER_CHECK_CODE specify the number check code.
	 */
	int NUMBER_CHECK_CODE = 3;

	/**
	 * NUMBER_CHECK_MES specify the number check message.
	 */
	String NUMBER_CHECK_MESSAGE = "Value is not a number";

	/**
	 * ALPHABET_CHECK_CODE specify the alphabet check code.
	 */
	int ALPHABET_CHECK_CODE = 4;

	/**
	 * ALPHABET_CHECK_MES specify the alphabet check message.
	 */
	String ALPHABET_CHECK_MESSAGE = "Value is not a valid alphabet";

	/**
	 * ALPHANUMERIC_CHECK_CODE specify the alphanumeric check code.
	 */
	int ALPHANUMERIC_CHECK_CODE = 5;

	/**
	 * ALPHANUMERIC_CHECK_MES specify the alphanumeric check message.
	 */
	String ALPHANUMERIC_CHECK_MESSAGE = "Value is not in valid number/alphabet combination";

	/**
	 * MAX_CHECK_CODE specify the maximum length check code.
	 */
	int MAX_CHECK_CODE = 6;

	/**
	 * MAX_CHECK_MES specify the maximum length check message.
	 */
	String MAX_CHECK_MESSAGE = "Invalid Maximum Length";

	/**
	 * EMAIL_CHECK_CODE specify the email check code.
	 */
	int EMAIL_CHECK_CODE = 7;

	/**
	 * EMAIL_CHECK_MES specify the email check message.
	 */
	String EMAIL_CHECK_MESSAGE = "Invalid email address";

	/**
	 * PATTERN_CHECK_CODE specify the pattern check code.
	 */
	int REGEX_CHECK_CODE = 8;

	/**
	 * PATTERN_CHECK_MES specify the pattern check message.
	 */
	String REGEX_CHECK_MESSAGE = "An invalid parameter value has been submitted";

	/**
	 * DATE_CHECK_CODE specify the date check code.
	 */
	int DATE_CHECK_CODE = 9;

	/**
	 * DATE_CHECK_MES specify the date check message.
	 */
	String DATE_CHECK_MESSAGE = "Invalid date";

	/**
	 * PAST_DATE_CHECK_CODE specify the past date check code.
	 */
	int PAST_DATE_CHECK_CODE = 10;

	/**
	 * PAST_DATE_CHECK_MES specify the past date check message.
	 */
	String PAST_DATE_CHECK_MESSAGE = "Invalid Past date";

	/**
	 * FUTURE_DATE_CHECK_CODE specify the future date check code.
	 */
	int FUTURE_DATE_CHECK_CODE = 11;

	/**
	 * FUTURE_DATE_CHECK_MES specify the future date check message.
	 */
	String FUTURE_DATE_CHECK_MESSAGE = "Invalid Future date";

	/**
	 * RANGE_CHECK_CODE specify the range check code.
	 */
	int RANGE_CHECK_CODE = 12;

	/**
	 * RANGE_CHECK_MES specify the range check message.
	 */
	String RANGE_CHECK_MESSAGE = "Invalid Range Check";
	

	/**
	 * MANDATORY_FIELD_CODE specify the mandatory field code.
	 */
	int MANDATORY_FIELD_CODE = 13;
	
	/**
	 * ATTR_PARAM_CHECK_CODE specify the attr param check code.
	 */
	int ATTR_PARAM_CHECK_CODE = 14;
	
	/**
	 * SORT_CHECK_CODE specify the sort check code.
	 */
	int SORT_CHECK_CODE = 15;
	
	/**
	 * SORT_CHECK_CODE specify the sort check code.
	 */
	int OPTIONS_CHECK_CODE = 16;
	
	/**
	 * MANDATORY_FIELD_MES specify the mandatory field message.
	 */
	String MANDATORY_FIELD_MESSAGE = "Mandatory Field is missing";	
	
	/**
	 * PARAMETER_CHECK_MES specify the parameter check message.
	 */
	String PARAMETER_CHECK_MESSAGE = "An invalid parameter name has been submitted";
	
	/**
	 * VALIDATOR_RULES_NOT_AVAIL_MES specify the validator rules not available message.
	 */
	String VALIDATOR_RULES_NOT_AVAIL_MESSAGE = "Validator rules are not available";
	
	/**
	 * VALIDATION_RULES specify the validator rules.
	 */
	String VALIDATION_RULES = "ValidationRules";	

	/**
	 * COMMON_PROPERTIES specify the common properties.
	 */
	String COMMON_PROPERTIES = "common.properties";
	
	/**
	 * VALIDATION_FILE_PATH specify the validation file path.
	 */
	String VALIDATION_FILE_PATH = "validation.filepath";
	
	/**
	 * VALIDATION_CONFIG_FILE_NAME specify the validation config file name.
	 */
	String VALIDATION_CONFIG_FILE_NAME = "validationconfig.filename";
	
	/**
	 * PRODUCT_CODE is the unique code of the product.
	 */
	String PRODUCT_CODE = "productcode";
	/**
	 * NAME is the name of the product.
	 */
	String NAME = "name";
	/**
	 * LIST_PRICE is the list price of the product.
	 */
	String LIST_PRICE = "listprice";
	/**
	 * SALE_PRICE is the sale price of the product.
	 */
	String SALE_PRICE = "saleprice";
	/**
	 * INVENTORY is the inventory of the product.
	 */
	String INVENTORY = "inventory";
	/**
	 * NEW_ARRIVAL is to specify whether it is new arrival or not.
	 */
	String NEW_ARRIVAL = "newarrival";
	/**
	 * BEST_SELLER is to specify whether it is best seller or not.
	 */
	String BEST_SELLER = "bestseller";
	/**
	 * WISH_LIST_FAVORITES is the wish list field field.
	 */
	String WISH_LIST_FAVORITES = "wishlistfavorites";
	/**
	 * TOP_RATING is the top rating field.
	 */
	String TOP_RATING = "toprating";
	
	/**
	 * NUMBER_ZERO specify the number 0.
	 */
	int NUMBER_ZERO = 0;
	
	/**
	 * NUMBER_ONE specify the number 1.
	 */
	int NUMBER_ONE = 1;
	
	/**
	 * NUMBER_TWO specify the number 2.
	 */
	int NUMBER_TWO = 2;
}

