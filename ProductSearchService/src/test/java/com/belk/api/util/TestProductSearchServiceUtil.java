package com.belk.api.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.belk.api.model.productsearch.Attribute;
import com.belk.api.model.productsearch.Dimension;
import com.belk.api.model.productsearch.ParentCategory;
import com.belk.api.model.productsearch.Price;
import com.belk.api.model.productsearch.ProductSearch;
import com.belk.api.model.productsearch.Refinement;
import com.belk.api.model.productsearch.Search;
import com.belk.api.model.productsearch.SearchAttribute;
import com.belk.api.model.productsearch.SearchReport;
import com.belk.api.model.productsearch.SubCategory;

/**
 * Unit Testing related to ProductSearchServiceUtil class is performed. <br />
 * {@link TestProductSearchServiceUtil} class is written for testing methods in
 * ProductSearchServiceImpl ,Test class behaves as helper class.
 * 
 * @author Mindtree
 * @date 25 Oct 2013
 */
public class TestProductSearchServiceUtil {

	/**
	 * Method to create request map
	 * 
	 * @return requestMap
	 */
	public static Map<String, String> createRequestMap() {

		final Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("q", "handbag");
		requestMap.put("attr",
				"color:blue,red|size:small,large|brand:lee,wrangler|price:3,7");
		requestMap.put("format", "JSON");
		requestMap.put("limit", "10");
		requestMap.put("offset", "100");
		requestMap.put("sort",
				"productcode|-name|-listprice|saleprice|inventory|"
						+ "-newarrival|bestseller|wishlistfavorites");
		requestMap.put("catid", "132323213");
		requestMap.put("isbridal", "true");
		requestMap.put("dim", "true");
		requestMap.put("color", "true");
		requestMap.put("size", "true");
		requestMap.put("brand", "true");
		requestMap.put("price", "P_skus_sale_price");
		return requestMap;
	}

	/**
	 * @return uriMap
	 */
	public static Map<String, List<String>> createURIMapWithColon() {
		final Map<String, List<String>> uriMap = new HashMap<String, List<String>>();
		final List<String> list1 = new ArrayList<String>();
		list1.add("color:blue,red:size:small,large:brand:lee,wrangler:price:3,7");
		uriMap.put("attr", list1);
		return uriMap;
	}

	/**
	 * Method to create result map
	 * 
	 * @return requestMap
	 */
	public static List<Map<String, String>> createResultMap() {
		final List<Map<String, String>> resultMapList = new ArrayList<Map<String, String>>();
		final Map<String, String> resultMap = new HashMap<String, String>();

		resultMap.put("P_product_code", "3203375NA10011");
		resultMap.put("P_available_in_Store", "No");
		resultMap.put("P_available_online", "Yes");
		resultMap.put("P_clearance", "No");
		resultMap.put("P_isPattern", "F");
		resultMap.put("P_listPrice", "64.000000");
		resultMap.put("P_onSale", "No");
		resultMap.put("P_product_name", "Ocean Washed Dress Shirt");
		resultMap.put("P_saleprice", "64.000000");
		resultMap.put("totalProducts", "78");

		resultMapList.add(resultMap);

		return resultMapList;
	}

	/**
	 * Method to create uri map
	 * 
	 * @return uriMap
	 */
	public static Map<String, List<String>> createURIMap() {

		final Map<String, List<String>> uriMap = new HashMap<String, List<String>>();
		final List<String> list1 = new ArrayList<String>();
		list1.add("handbag");
		final List<String> list2 = new ArrayList<String>();
		list2.add("color:blue,red|size:small,large|brand:lee,wrangler|price:3,7");
		final List<String> list3 = new ArrayList<String>();
		list3.add("JSON");
		final List<String> list4 = new ArrayList<String>();
		list4.add("10");
		final List<String> list5 = new ArrayList<String>();
		list5.add("100");
		final List<String> list6 = new ArrayList<String>();
		list6.add("productcode|-name|-listprice|saleprice|inventory|-newarrival|bestseller|wishlistfavorites");
		final List<String> list7 = new ArrayList<String>();
		list7.add("132323213");
		final List<String> list8 = new ArrayList<String>();
		list8.add("true");
		final List<String> list9 = new ArrayList<String>();
		list9.add("false");

		uriMap.put("q", list1);
		uriMap.put("attr", list2);
		uriMap.put("format", list3);
		uriMap.put("limit", list4);
		uriMap.put("offset", list5);
		uriMap.put("sort", list6);
		uriMap.put("catid", list7);
		uriMap.put("isbridal", list8);
		uriMap.put("dim", list9);

		return uriMap;
	}

	/**
	 * Method to create search result object
	 * 
	 * @return search
	 */
	public static Search createSearchResults() {
		final Search search = new Search();
		search.setProducts(getProductSearchList());
		search.setCategories(getCategoriesList());
		search.setDimensions(getDimensionList());
		search.setSearchReport(getSearchReport());
		return search;
	}

	/**
	 * Method to get product search list
	 * 
	 * @return products
	 */
	private static List<ProductSearch> getProductSearchList() {
		final List<ProductSearch> products = new ArrayList<ProductSearch>();
		final ProductSearch productSearch = new ProductSearch();
		productSearch.setProductCode("3203375NA10011");
		productSearch.setName("Ocean Washed Dress Shirt");
		productSearch.setProductPrice(getPriceList());
		products.add(productSearch);
		return products;
	}

	/**
	 * Method to get category list
	 * 
	 * @return categories
	 */
	private static List<ParentCategory> getCategoriesList() {
		final List<ParentCategory> categories = new ArrayList<ParentCategory>();

		final ParentCategory parentCategory = new ParentCategory();

		final List<Attribute> parentCategoryAttributes = new ArrayList<Attribute>();
		final Attribute parentAttribute = new Attribute();
		parentAttribute.setKey("categoryID");
		parentAttribute.setValue("9999");
		parentCategoryAttributes.add(parentAttribute);

		final List<SubCategory> subCategories = new ArrayList<SubCategory>();

		final SubCategory subCategory = new SubCategory();

		final List<Attribute> subCategoryAttributes = new ArrayList<Attribute>();
		final Attribute subAttribute1 = new Attribute();
		subAttribute1.setKey("categoryID");
		subAttribute1.setValue("9999");

		final Attribute subAttribute2 = new Attribute();
		subAttribute2.setKey("categoryName");
		subAttribute2.setValue("Junior");

		subCategoryAttributes.add(subAttribute1);
		subCategoryAttributes.add(subAttribute2);

		subCategory.setCategoryAttributes(subCategoryAttributes);
		subCategories.add(subCategory);
		parentCategory.setCategoryAttributes(parentCategoryAttributes);
		parentCategory.setSubCategories(subCategories);
		categories.add(parentCategory);

		return categories;
	}

	/**
	 * Method to get dimension list of a product
	 * 
	 * @return dimensions
	 */
	private static List<Dimension> getDimensionList() {
		final List<Dimension> dimensions = new ArrayList<Dimension>();

		final Dimension dimension = new Dimension();

		final List<Attribute> dimensionAttributes = new ArrayList<Attribute>();

		final Attribute dimAttribute = new Attribute();
		dimAttribute.setKey("dimensionId");
		dimAttribute.setValue("123");
		dimensionAttributes.add(dimAttribute);

		final List<Refinement> refinements = new ArrayList<Refinement>();
		final Refinement refinement = new Refinement();

		final List<Attribute> refinementAttributes = new ArrayList<Attribute>();

		final Attribute refAttribute1 = new Attribute();
		refAttribute1.setKey("refinementId");
		refAttribute1.setValue("123");

		final Attribute refAttribute2 = new Attribute();
		refAttribute2.setKey("label");
		refAttribute2.setValue("Red");

		refinementAttributes.add(refAttribute1);
		refinementAttributes.add(refAttribute2);

		refinement.setRefinementAttributes(refinementAttributes);

		refinements.add(refinement);

		dimension.setDimensionAttributes(dimensionAttributes);
		dimension.setRefinements(refinements);

		dimensions.add(dimension);

		return dimensions;
	}

	/**
	 * Method to get search report of a product
	 * 
	 * @return searchReport
	 */
	private static SearchReport getSearchReport() {
		final SearchReport searchReport = new SearchReport();
		searchReport.setTotalProducts("140");
		searchReport.setTotalSkus("100");
		searchReport.setKeyword("tops");
		searchReport.setLimit("10");
		searchReport.setOffset("100");
		searchReport.setAttributes(getAttributeList());
		return searchReport;
	}

	/**
	 * Method to get search attribute list of product
	 * 
	 * @return attributesList
	 */
	private static List<SearchAttribute> getAttributeList() {

		final List<SearchAttribute> attributesList = new ArrayList<SearchAttribute>();
		final SearchAttribute attribute1 = new SearchAttribute();
		final List<String> attValueList1 = new ArrayList<String>();
		attValueList1.add("blue");
		attValueList1.add("red");
		attribute1.setKey("color");
		attribute1.setValues(attValueList1);

		final SearchAttribute attribute2 = new SearchAttribute();
		final List<String> attValueList2 = new ArrayList<String>();

		attValueList2.add("small");
		attribute2.setKey("size");
		attribute2.setValues(attValueList2);

		attributesList.add(attribute1);
		attributesList.add(attribute2);

		return attributesList;
	}

	/**
	 * Method to get price list
	 * 
	 * @return price
	 */
	private static List<Price> getPriceList() {

		final List<Price> priceList = new ArrayList<Price>();
		final Price price = new Price();
		price.setKey("listPrice");
		price.setValue("275");
		priceList.add(price);
		return priceList;
	}

	/**
	 * Method which creates URI map of data,which resembles the URInfo object
	 * created at run time from URI Information.
	 * 
	 * @return uriMap
	 */
	public static Map<String, List<String>> createURIInvalidDataMap() {

		final Map<String, List<String>> uriMap = new HashMap<String, List<String>>();

		final List<String> list1 = new ArrayList<String>();
		list1.add("handbag");
		final List<String> list2 = new ArrayList<String>();
		list2.add("color:blue,red|size:small,large|xyz:lee,wrangler|alpha:3,7");
		final List<String> list3 = new ArrayList<String>();
		list3.add("JSON");
		final List<String> list4 = new ArrayList<String>();
		list4.add("10");
		final List<String> list5 = new ArrayList<String>();
		list5.add("100");
		final List<String> list6 = new ArrayList<String>();
		list6.add("productcode|-name|-listprice|saleprice|invention|-newdeparture|bestseller|wishlistfavorites");
		final List<String> list7 = new ArrayList<String>();
		list7.add("132323213");
		final List<String> list8 = new ArrayList<String>();
		list8.add("true");
		final List<String> list9 = new ArrayList<String>();
		list9.add("false");

		uriMap.put("q", list1);
		uriMap.put("attr", list2);
		uriMap.put("format", list3);
		uriMap.put("limit", list4);
		uriMap.put("offset", list5);
		uriMap.put("sort", list6);
		uriMap.put("catid", list7);
		uriMap.put("isbridal", list8);
		uriMap.put("dim", list9);

		return uriMap;
	}

	/**
	 * Test method for
	 * {@link com.belk.api.util.ProductSearchServiceUtil#createRequestMapWithNoPrice()}
	 * . Testing is done to check whether the required data is formatted.
	 * 
	 * @return requestMap
	 * 
	 */
	public static Map<String, String> createRequestMapWithNoPrice() {
		final Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("q", "handbag");
		requestMap.put("attr",
				"color:blue,red|size:small,large|brand:lee,wrangler|price:3,7");
		requestMap.put("format", "JSON");
		requestMap.put("limit", "10");
		requestMap.put("offset", "100");
		requestMap.put("sort",
				"productcode|-name|-listprice|saleprice|inventory|"
						+ "-newarrival|bestseller|wishlistfavorites");
		requestMap.put("catid", "132323213");
		requestMap.put("isbridal", "true");
		requestMap.put("dim", "true");
		requestMap.put("color", "true");
		requestMap.put("size", "true");
		requestMap.put("brand", "true");
		requestMap.put("price", "true");
		return requestMap;
	}

	/**
	 * Test method for
	 * {@link com.belk.api.util.ProductSearchServiceUtil#createRequestMapWithNoPrice()}
	 * .
	 * 
	 * Testing is done to check whether the required data is formatted.
	 * 
	 * @return requestMap
	 */
	public static Map<String, List<String>> createURIMapForPriceLength() {
		final Map<String, List<String>> uriMap = new HashMap<String, List<String>>();
		final List<String> list1 = new ArrayList<String>();
		list1.add("handbag");
		final List<String> list2 = new ArrayList<String>();
		list2.add("color:blue,wrangler|price:3,7,8");
		final List<String> list3 = new ArrayList<String>();
		list3.add("JSON");
		final List<String> list4 = new ArrayList<String>();
		list4.add("10");
		final List<String> list5 = new ArrayList<String>();
		list5.add("100");
		final List<String> list6 = new ArrayList<String>();
		list6.add("productcode|-name|-listprice|saleprice|inventory|-newarrival|bestseller|wishlistfavorites");
		final List<String> list7 = new ArrayList<String>();
		list7.add("132323213");
		final List<String> list8 = new ArrayList<String>();
		list8.add("true");
		final List<String> list9 = new ArrayList<String>();
		list9.add("false");

		uriMap.put("q", list1);
		uriMap.put("attr", list2);
		uriMap.put("format", list3);
		uriMap.put("limit", list4);
		uriMap.put("offset", list5);
		uriMap.put("sort", list6);
		uriMap.put("catid", list7);
		uriMap.put("isbridal", list8);
		uriMap.put("dim", list9);
		return uriMap;

	}

	/**
	 * Method to create request map
	 * 
	 * @return requestMap
	 */
	public static Map<String, List<String>> createURIMapForNumberFormatException() {
		final Map<String, List<String>> uriMap = new HashMap<String, List<String>>();
		final List<String> list1 = new ArrayList<String>();
		list1.add("handbag");
		final List<String> list2 = new ArrayList<String>();
		list2.add("color:blue,red|size:small,large|brand:lee,wrangler|price:u,7");
		final List<String> list3 = new ArrayList<String>();
		list3.add("JSON");
		final List<String> list4 = new ArrayList<String>();
		list4.add("10");
		final List<String> list5 = new ArrayList<String>();
		list5.add("100");
		final List<String> list6 = new ArrayList<String>();
		list6.add("productcode|-name|-listprice|saleprice|inventory|-newarrival|bestseller|wishlistfavorites");
		final List<String> list7 = new ArrayList<String>();
		list7.add("132323213");
		final List<String> list8 = new ArrayList<String>();
		list8.add("true");
		final List<String> list9 = new ArrayList<String>();
		list9.add("true");

		uriMap.put("q", list1);
		uriMap.put("attr", list2);
		uriMap.put("format", list3);
		uriMap.put("limit", list4);
		uriMap.put("offset", list5);
		uriMap.put("sort", list6);
		uriMap.put("catid", list7);
		uriMap.put("isbridal", list8);
		uriMap.put("dim", list9);

		return uriMap;
	}
}
