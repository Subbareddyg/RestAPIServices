package com.belk.api.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.belk.api.constants.CommonConstants;
import com.belk.api.constants.DomainConstants;
import com.belk.api.constants.ErrorConstants;
import com.belk.api.exception.BaseException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.model.categorydetails.Attribute;
import com.belk.api.model.categorydetails.Categories;
import com.belk.api.model.categorydetails.Category;
import com.belk.api.model.categorydetails.SubCategory;
import com.belk.api.util.CommonUtil;

/**
 * Mapper class for the CategoryDetail. Jsoup.parse() has been used to handle
 * the special characters as required. Update: updated code with comments as
 * part of Phase2, April,14 release
 * 
 * Updated : Added few comments and changed the name of variables as part of
 * Phase2, April,14 release
 * 
 * Updated : Code modified to handle empty hasSubcategories flag value, as part
 * of Mandatory tags CR
 * 
 * @author Mindtree
 * @date Oct, 2013
 */
@Service
public class CategoryDetailsMapper {

	/**
	 * creating instance of logger.
	 */
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	@Autowired
	CommonUtil commonUtil;

	/**
	 * Default Constructor.
	 */
	public CategoryDetailsMapper() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param commonUtil
	 *            the commonUtil to set
	 */
	public final void setCommonUtil(final CommonUtil commonUtil) {
		this.commonUtil = commonUtil;
	}

	/**
	 * Method to convert the response map from Adapter into the desired Category
	 * Details POJO.
	 * 
	 * @param responseList
	 *            Response list from adapter
	 * @param correlationId
	 *            to track the request
	 * @return Categories Pojo which is formed from the response for the
	 *         requested criteria
	 * @throws BaseException
	 *             The general exception thrown from Domain Layer.
	 */

	public final Categories convertToCategoryDetailsPojo(
			final List<List<Map<String, String>>> responseList,
			final String correlationId) throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		LOGGER.info("Responselist: " + responseList, correlationId);
		Category category = null;
		final Categories categories = new Categories();
		final List<Category> categoryList = new ArrayList<Category>();
		List<Attribute> categoryAttributeList = null;
		for (List<Map<String, String>> resultList : responseList) {
			for (Map<String, String> responseMap : resultList) {
				// checks if the map contains dimentionKey
				if (responseMap.containsKey(DomainConstants.DIMENSION_KEY)) {
					category = new Category();
					categoryAttributeList = new ArrayList<Attribute>();
					final Attribute attribute = new Attribute();
					attribute.setKey(CommonConstants.HAS_SUBCATEGORIES);

					// checks for Dimension key if it contains it set the
					// category id.
					if (this.commonUtil.isNotEmpty(
							responseMap.get(DomainConstants.DIMENSION_KEY),
							correlationId)) {
						category.setCategoryId(responseMap
								.get(DomainConstants.DIMENSION_KEY));
					}

					// checks for Dimension name if it contains it set the
					// category name.
					if (this.commonUtil.isNotEmpty(
							responseMap.get(DomainConstants.DIMENSION_LABEL),
							correlationId)) {
						category.setName(Jsoup.parse(
								responseMap
										.get(DomainConstants.DIMENSION_LABEL))
								.text());
					}

					// checks for Parent Dimension key if it contains it set the
					// parent category id.
					if (this.commonUtil.isNotEmpty(responseMap
							.get(DomainConstants.PARENT_DIMENSION_KEY),
							correlationId)) {
						category.setParentCategoryId(responseMap
								.get(DomainConstants.PARENT_DIMENSION_KEY));
					}

					// checks for hasFurtherRefinements if contains then
					// sets the value for hassubcategory flag.
					if (this.commonUtil.isNotEmpty(responseMap
							.get(DomainConstants.HAS_FURTHER_REFINEMENTS),
							correlationId)) {
						final String attrValue = Jsoup
								.parse(responseMap
										.get(DomainConstants.HAS_FURTHER_REFINEMENTS))
								.text();
						attribute.setValue(attrValue);
					} else {
						// setting hasSubcategories value as No if empty value
						// returned from Endeca
						attribute.setValue(CommonConstants.NO_VALUE);
					}
					categoryAttributeList.add(attribute);
					category.setCategoryAttributes(categoryAttributeList);
					if (this.commonUtil.isNotEmpty(
							responseMap.get(DomainConstants.REFINEMENTS),
							correlationId)) {
						// refinements are present as tokenized values and
						// by parsing them
						// sub categories are formed.
						category.setSubCategories(this.tokenizeSubCategoryList(
								responseMap.get(DomainConstants.REFINEMENTS),
								correlationId));
					}
					categoryList.add(category);

				}

			}
		}

		categories.setCategories(categoryList);
		LOGGER.info("categories : " + categories.toString(), correlationId);
		LOGGER.logMethodExit(startTime, correlationId);
		return categories;

	}

	/**
	 * Method to create a list of sub categories from a delimited string of
	 * refinement values.
	 * 
	 * @param refinementListValue
	 *            delimited string representing refinement list values
	 * @param correlationId
	 *            to track the request
	 * @return subCategoryList list of sub categories
	 * @throws BaseException
	 *             The base exception thrown from this method
	 */
	private List<SubCategory> tokenizeSubCategoryList(
			final String refinementListValue, final String correlationId)
			throws BaseException {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		final List<SubCategory> subCategoryList = new ArrayList<SubCategory>();

		final StringTokenizer subCategoryListTokens = new StringTokenizer(
				refinementListValue, CommonConstants.PIPE_SEPERATOR);
		try {
			// "|" separates the category.
			while (subCategoryListTokens.hasMoreTokens()) {
				final SubCategory subCategory = new SubCategory();

				final StringTokenizer subCategoryTokens = new StringTokenizer(
						subCategoryListTokens.nextToken(),
						CommonConstants.TILDE_SEPERATOR);
				// "~" separates each variable in category
				while (subCategoryTokens.hasMoreTokens()) {

					final StringTokenizer stringToken = new StringTokenizer(
							subCategoryTokens.nextToken(),
							CommonConstants.FIELD_PAIR_MAPPER_SEPARATOR);

					this.subCategoryDetails(stringToken, subCategory,
							correlationId);

				}
				subCategoryList.add(subCategory);

			}

		} catch (NoSuchElementException e) {
			LOGGER.error(e, correlationId);
			throw new BaseException(
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);
		}
		LOGGER.logMethodExit(startTime, correlationId);
		return subCategoryList;
	}

	/**
	 * Method obtains sub category details from Adapter response.
	 * 
	 * @param stringToken
	 *            response from adapter which contains the sub category details
	 * @param subCategory
	 *            it is the sub category Object
	 * @param correlationId
	 *            it is to track the request
	 */
	private void subCategoryDetails(final StringTokenizer stringToken,
			final SubCategory subCategory, final String correlationId) {
		final long startTime = LOGGER.logMethodEntry(correlationId);
		// "^" separates each variable with corresponding value.
		while (stringToken.hasMoreElements()) {
			final List<Attribute> subCategoryAttrList = new ArrayList<Attribute>();
			final String key = stringToken.nextToken();
			final String value = stringToken.nextToken();
			if (key.equals(DomainConstants.REFINEMENT_KEY)) {
				subCategory.setCategoryId(value);
			} else if (key.equals(DomainConstants.DIMENSION_LABEL)) {
				subCategory.setName(Jsoup.parse(value).text());
			} else if (key.equals(DomainConstants.PARENT_DIMENSION_KEY)) {
				subCategory.setParentCategoryId(value);
			} else if (key.equals(DomainConstants.HAS_FURTHER_REFINEMENTS)) {
				final Attribute attribute = new Attribute();
				attribute.setKey(CommonConstants.HAS_SUBCATEGORIES);
				if (!CommonConstants.EMPTY_STRING.equals(value)) {
					attribute.setValue(Jsoup.parse(value).text());
				} else {
					// setting hasSubcategories value as No if empty value
					// returned from Endeca
					attribute.setValue(CommonConstants.NO_VALUE);
				}
				subCategoryAttrList.add(attribute);

			}
			if (!subCategoryAttrList.isEmpty()) {
				subCategory.setCategoryAttributes(subCategoryAttrList);
			}
		}
		LOGGER.logMethodExit(startTime, correlationId);

	}
}
