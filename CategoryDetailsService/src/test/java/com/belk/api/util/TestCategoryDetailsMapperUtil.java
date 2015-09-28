package com.belk.api.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.belk.api.model.categorydetails.Attribute;
import com.belk.api.model.categorydetails.Categories;
import com.belk.api.model.categorydetails.Category;
import com.belk.api.model.categorydetails.SubCategory;

/**
 * Unit Testing related to CategoryDetailsMapper class is performed. <br />
 * TestCategoryDetailsMapper class is written for testing methods in
 * CategoryDetailsMapper The unit test cases evaluates the way the methods
 * behave for the inputs given.
 * 
 * @author Mindtree
 * @date Nov 14 2013
 */
public class TestCategoryDetailsMapperUtil {

	/**
	 * Method which creates categories list details
	 * 
	 * @return categories
	 */
	public static Categories createCategoriesResult() {
		final Categories categories = new Categories();

		final List<Category> categoryList = new ArrayList<Category>();

		final Category category = new Category();
		category.setCategoryId("4294922263");
		category.setName("Home");
		category.setParentCategoryId("4294967294");

		final List<Attribute> categoryAttributes = new ArrayList<Attribute>();

		final Attribute attribute = new Attribute();
		attribute.setKey("hasFurtherRefinements");
		attribute.setValue("Yes");

		categoryAttributes.add(attribute);

		category.setCategoryAttributes(categoryAttributes);

		final List<SubCategory> subCategories = new ArrayList<SubCategory>();

		final SubCategory subCategory = new SubCategory();
		subCategory.setCategoryId("4294922262");
		subCategory.setName("Home1");
		subCategory.setParentCategoryId("4294922263");

		final List<Attribute> subCategoryAttributes = new ArrayList<Attribute>();

		final Attribute subAttribute1 = new Attribute();
		subAttribute1.setKey("hasFurtherRefinements");
		subAttribute1.setValue("No");
		subCategoryAttributes.add(subAttribute1);

		subCategory.setCategoryAttributes(subCategoryAttributes);

		subCategories.add(subCategory);

		category.setSubCategories(subCategories);

		categoryList.add(category);

		categories.setCategories(categoryList);
		// categories.setCategories(subCategories);

		return categories;
	}

	/**
	 * Method to create endecaResultList
	 * 
	 * @return map1
	 */
	public static List<List<Map<String, String>>> endecaResultList() {

		final Map<String, String> map1 = new HashMap<String, String>();
		map1.put("dimensionKey", "4294922263");
		map1.put("label", "shirts");
		map1.put("name", "Shirts");
		map1.put("parentDimentionKey", "4294967294");
		map1.put("hasFurtherRefinements", "Yes");
		map1.put(
				"refinements",
				"refinementKey^4294920036~label^Shirts~parentDimentionKey^4294920208~hasFurtherRefinements^No");

		final List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		resultList.add(map1);

		final List<List<Map<String, String>>> endecaResultList = new ArrayList<List<Map<String, String>>>();

		endecaResultList.add(resultList);
		return endecaResultList;

	}

	/**
	 * Method to get sub category list
	 * 
	 * @return subCategoryList
	 */
	public static List<SubCategory> getSubCategoriesList() {

		final List<SubCategory> subCategoryList = new ArrayList<SubCategory>();

		final SubCategory subCategory = new SubCategory();
		subCategory.setCategoryId("4294920036");
		subCategory.setName("Home1");
		subCategory.setParentCategoryId("4294920208");

		final List<Attribute> subCategoryAttributes = new ArrayList<Attribute>();

		final Attribute subAttribute1 = new Attribute();
		subAttribute1.setKey("hasFurtherRefinements");
		subAttribute1.setValue("No");
		subCategoryAttributes.add(subAttribute1);

		subCategory.setCategoryAttributes(subCategoryAttributes);

		subCategoryList.add(subCategory);

		return subCategoryList;
	}

	/**
	 * Method to get refinement list string value
	 * 
	 * @return string
	 */
	public static String getRefinementListValue() {
		return "refinementKey^4294920036~label^Shirts~parentDimentionKey^4294920208~hasFurtherRefinements^No";
	}

}
