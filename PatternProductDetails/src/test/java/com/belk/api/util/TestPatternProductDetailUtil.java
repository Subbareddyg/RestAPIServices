package com.belk.api.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import com.belk.api.constants.CommonConstants;
import com.belk.api.model.productdetails.ProductDetails;
import com.belk.api.model.productdetails.ProductList;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * 
 * @author Mindtree
 * @date Oct 21, 2013
 */
public class TestPatternProductDetailUtil {
	Map<String, List<String>> inputData;
	List<String> list;

	/**
	 * this method forms input request map for the validateRequest method.
	 * 
	 * @return Map
	 */
	public final Map<String, List<String>> getData() {
		this.inputData = new HashMap<String, List<String>>();
		this.list = new ArrayList<String>();
		this.list.add("3201634Z31300");
		this.inputData.put("prdCode", this.list);
		return this.inputData;
	}

	/**
	 * this method forms input request map for the validateRequest method.
	 * 
	 * @return Map
	 */
	public final Map<String, List<String>> getDataForProductCodeParam() {
		this.inputData = new HashMap<String, List<String>>();
		this.list = new ArrayList<String>();
		this.list.add("3201634Z31300");
		this.inputData.put("productcode", this.list);
		return this.inputData;
	}

	/**
	 * this method returns list of products
	 * 
	 * @return ProductList
	 */
	public final ProductList getResponseData() {
		final Map<String, List<String>> inputMap = new HashMap<String, List<String>>();
		final List<String> list = new ArrayList<String>();
		list.add("3201634Z31300");
		inputMap.put("prdCode", list);
		final ProductList prdList = new ProductList();
		final List<ProductDetails> prdDetails = new ArrayList<ProductDetails>();
		final ProductDetails product = new ProductDetails();
		product.setProductCode("3201634Z31300");
		prdDetails.add(product);
		prdList.setProducts(prdDetails);
		return prdList;
	}

	/**
	 * this method returns list of products
	 * 
	 * @return ProductList
	 */
	public final ProductList getResponseDataForProductCodeParam() {
		final Map<String, List<String>> inputMap = new HashMap<String, List<String>>();
		final List<String> list = new ArrayList<String>();
		list.add("3201634Z31300");
		inputMap.put("productcode", list);
		final ProductList prdList = new ProductList();
		final List<ProductDetails> prdDetails = new ArrayList<ProductDetails>();
		final ProductDetails product = new ProductDetails();
		product.setProductCode("3201634Z31300");
		prdDetails.add(product);
		prdList.setProducts(prdDetails);
		return prdList;
	}

	/**
	 * this method returns list of products
	 * 
	 * @return ProductList
	 */
	public final ProductList getResponseDataWithDiffQueryParam() {
		final Map<String, List<String>> inputMap = new HashMap<String, List<String>>();
		final List<String> list = new ArrayList<String>();
		list.add("3201634Z31300");
		inputMap.put("productcode", list);
		final ProductList prdList = new ProductList();
		final List<ProductDetails> prdDetails = new ArrayList<ProductDetails>();
		final ProductDetails product = new ProductDetails();
		product.setProductCode("3201634Z31300");
		prdDetails.add(product);
		prdList.setProducts(prdDetails);
		return prdList;
	}

	/**
	 * Method to create request http headers map
	 * 
	 * @return reqHeaders
	 */
	public final MultivaluedMap<String, String> createRequestHeadersMap() {
		final MultivaluedMap<String, String> reqHeaders = new MultivaluedMapImpl();
		reqHeaders.add(CommonConstants.CORRELATION_ID, "1234567890");
		return reqHeaders;
	}
}
