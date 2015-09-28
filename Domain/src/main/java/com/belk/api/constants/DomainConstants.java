package com.belk.api.constants;

/**
 * General constants specifically required for the Domain project.
 * 
 * @author Mindtree
 * @date Jul 24, 2013
 */
public final class DomainConstants {
	/**
	 * PRODUCT_SEARCH_BINDING specifies binding file for product serach api.
	 */
	public static final String PRODUCT_SEARCH_BINDING = "productsearch-binding";
	/**
	 * PRODUCT_DETAIL_BINDING specifies binding file for product details api.
	 */
	public static final String PRODUCT_DETAIL_BINDING = "productdetails-binding";
	/**
	 * CATEGORY_PRODUCT_BINDING specifies binding file for category product api.
	 */
	public static final String CATEGORY_PRODUCT_BINDING = "categoryProduct-binding";
	/**
	 * CATEGORY_DETAILS_BINDING specifies binding file for category details api.
	 */
	public static final String CATEGORY_DETAILS_BINDING = "categorydetails-binding";
	/**
	 * CATALOG_BINDING specifies binding file for catalog api.
	 */
	public static final String CATALOG_BINDING = "catalog-binding";

	// domain specific constants across APIs
	
	
	/**
	 * DOMAIN_RESOURCE_BUNDLE specifies the resource bundle of domain.
	 */
	public static final String DOMAIN_RESOURCE_BUNDLE = "domain";
	/**
	 * CATEGORY_ID specifies the category id.
	 */
	public static final String CATEGORY_ID = "categoryId";
	/**
	 * REFINEMENT_ID specifies the refinement id.
	 */
	public static final String REFINEMENT_ID = "refinementId";
	/**
	 * LIMIT specifies the limit field.
	 */
	public static final String LIMIT = "limit";
	/**
	 * OFFSET specifies the offset field.
	 */
	public static final String OFFSET = "offset";
	/**
	 * REFINEMENT_KEY specifies the refinement key.
	 */
	public static final String REFINEMENT_KEY = "refinementKey";
	/**
	 * DIMENSION_KEY specifies dimension key.
	 */
	public static final String DIMENSION_KEY = "dimensionKey";

	/**
	 * DIMENSION_LABEL specifies unique name.
	 */
	public static final String DIMENSION_LABEL = "label";
	/**
	 * RECORD_COUNT specifies the count of recods.
	 */
	public static final String RECORD_COUNT = "recordCount";
	/**
	 * ATTRIBUTES specifies the attributes field.
	 */
	public static final String ATTRIBUTES = "attributes";

	/**
	 * SORT_FIELDS specifies the sortFields attribute.
	 */
	public static final String SORT_FIELDS = "sortFields";
	/**
	 * REFINEMENTS specifies the refinement field.
	 */
	public static final String REFINEMENTS = "refinements";
	/**
	 * CATEGORY specifies category.
	 */
	public static final String CATEGORY = "category";
	/**
	 * CATEGORY_NAME specifies name of the category.
	 */
	public static final String CATEGORY_NAME = "categoryName";
	/**
	 * PARENT_DIMENSION_KEY specifies the unique dimension key.
	 */
	public static final String PARENT_DIMENSION_KEY = "parentDimentionKey";
	/**
	 * HAS_FURTHER_REFINEMENTS specifies whether there are further refinments.
	 */
	public static final String HAS_FURTHER_REFINEMENTS = "hasFurtherRefinements";
	/**
	 * SUB_CATEGORY specifies the sub category.
	 */
	public static final String SUB_CATEGORY = "subcategory";
	/**
	 * MULTI_SELECT_ENABLED specifies whether the multi select is enabled or
	 * not.
	 */
	public static final String MULTI_SELECT_ENABLED = "multiSelectEnabled";
	/**
	 * MULTI_SELECT_OPERATION specifies the type of the operation.
	 */
	public static final String MULTI_SELECT_OPERATION = "multiSelectOperation";
	
	
	
	
	/**
	 * Default Constructor.
	 */
	private DomainConstants() {
	}
}
