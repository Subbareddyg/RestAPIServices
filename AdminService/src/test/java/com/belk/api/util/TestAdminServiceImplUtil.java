package com.belk.api.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.belk.api.model.Dimension;
import com.belk.api.model.catalog.Category;

/**
 * This class is a helper class for unit testing AdminServiceImpl
 * 
 * This Util class provides the data for other Test classes in AdminService
 * layer
 * 
 * @author Mindtree
 * @date Dec 5, 2013
 */
public class TestAdminServiceImplUtil {
	/**
	 * Method returns Map<String,String>
	 * 
	 * @return Map<String, String>
	 */
	public static Map<String, String> getRequestMapUri() {
		final Map<String, String> requestMapUri = new HashMap<String, String>();
		requestMapUri.put("showproduct", "1213");
		requestMapUri.put("categoryid", "123");
		requestMapUri.put("format", "1235");
		requestMapUri.put("rootDimension", "234");

		return requestMapUri;
	}

	/**
	 * Method returns Map<Long, Dimension>
	 * 
	 * @return Map<Long, Dimension>
	 */
	public static Map<Long, Dimension> getResultDimensionList() {
		final Map<Long, Dimension> resultDimensionList = new HashMap<Long, Dimension>();
		final Dimension dimension = new Dimension();
		final Dimension d = new Dimension();
		d.setName("categoryid");
		dimension.setDimensionId(123);
		dimension.setName("categoryid");
		dimension.setParentDimensionId(89);
		final List<Dimension> dimensions = new LinkedList<Dimension>();
		dimensions.add(d);
		dimension.setDimensions(dimensions);
		resultDimensionList.put((long) 123, dimension);
		return resultDimensionList;
	}

	/**
	 * Method returns Map<String, Category>
	 * 
	 * @return Map<String, Category>
	 */
	public static Map<String, Category> getLeafCategoryMap() {
		final Map<String, Category> leafCategoryMap = new HashMap<String, Category>();
		final Category category = new Category();
		category.setCategoryId(12);
		category.setName("tshirt");
		category.setParentCategoryId(98);
		leafCategoryMap.put("456", category);
		return leafCategoryMap;
	}

	/**
	 * Method returns Map<String, String>
	 * 
	 * @return Map<String, String>
	 */
	public static Map<String, String> getRequestMapUriForNullDimension() {
		final Map<String, String> requestMapUri = new HashMap<String, String>();
		requestMapUri.put("showproduct", "1213");
		requestMapUri.put("categoryid", "123");
		requestMapUri.put("format", "1235");

		return requestMapUri;
	}

	/**
	 * Method returns Map<Long, Dimension>
	 * 
	 * @return Map<Long, Dimension>
	 */
	public static Map<Long, Dimension> getResultDimensionListForNullDimension() {
		final Map<Long, Dimension> resultDimensionList = new HashMap<Long, Dimension>();
		Dimension dimension = new Dimension();
		dimension = null;

		resultDimensionList.put((long) 123, dimension);
		return resultDimensionList;
	}

	/**
	 * Method returns Dimension
	 * 
	 * @return Dimension
	 */
	public static Dimension getNullDimension() {
		final Dimension dimension = new Dimension();
		final List<Dimension> dimensions = new LinkedList<Dimension>();
		dimension.setDimensions(dimensions);
		dimension.setDimensionId(123);
		dimension.setName("categoryid");
		return dimension;

	}

}
