package com.belk.api.service.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.belk.api.model.Dimension;
import com.belk.api.model.catalog.Category;

/**
 * 
 * This class is a helper class for unit testing CatalogServiceImpl
 * 
 * This Util class provides the data for other Test classes in
 * CatalogServiceImpl layer
 * 
 * @author Mindtree
 * @date Dec 5, 2013
 */
public class TestCatalogServiceImplUtil {

	/**
	 * Method returns Map<String, String>
	 * 
	 * @return Map<String, String>
	 */
	public static Map<String, String> getRequestMapUri() {
		final Map<String, String> requestMapUri = new HashMap<String, String>();
		requestMapUri.put("showproduct", "1234");
		requestMapUri.put("categoryid", "123");

		return requestMapUri;
	}

	/**
	 * Method returns Map<Long,Dimension>
	 * 
	 * @return Map<Long,Dimension>
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
	 * Method return Category
	 * 
	 * @return Category
	 */
	public static Category getCategory() {
		final Category category = new Category();
		category.setCategoryId(123);
		category.setName("categoryid");
		return category;

	}

	/**
	 * Method returns Map<String, String>
	 * 
	 * @return Map<String, String>
	 */
	public static Map<String, String> getRequestMapUriShowProduct() {
		final Map<String, String> requestMapUri = new HashMap<String, String>();
		requestMapUri.put("showproduct", "false");
		requestMapUri.put("categoryid", "123");

		return requestMapUri;
	}

	/**
	 * Method returns Map<Long,Dimension>
	 * 
	 * @return Map<Long,Dimension>
	 */
	public static Map<Long, Dimension> getResultDimensionListNullDimension() {
		final Map<Long, Dimension> resultDimensionList = new HashMap<Long, Dimension>();
		final Dimension dimension = new Dimension();
		dimension.setDimensionId(123);
		dimension.setName("categoryid");
		dimension.setParentDimensionId(89);
		final List<Dimension> dimensions = new LinkedList<Dimension>();

		dimension.setDimensions(dimensions);
		resultDimensionList.put((long) 123, dimension);
		return resultDimensionList;
	}

	/**
	 * Method returns Map<String, String>
	 * 
	 * @return Map<String, String>
	 */
	public static Map<String, String> getRequestMapShowProductTrue() {
		final Map<String, String> requestMapUri = new HashMap<String, String>();
		requestMapUri.put("showproduct", "true");
		requestMapUri.put("categoryid", "123");

		return requestMapUri;
	}

}
