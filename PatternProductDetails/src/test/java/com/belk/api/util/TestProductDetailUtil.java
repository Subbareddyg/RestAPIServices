package com.belk.api.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.belk.api.model.productdetails.ProductDetails;
import com.belk.api.model.productdetails.ProductList;

/**
 * 
 * @author Mindtree
 * @date Oct 21, 2013
 */
public class TestProductDetailUtil {
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
		this.list.add("1800303AZJL606");
		this.inputData.put("productcode", this.list);
		return this.inputData;
	}

	/**
	 * this method forms input request(vendor details) map for the
	 * validateRequest method.
	 * 
	 * @param vendorType
	 *            - vendor Type
	 * @return Map.
	 */
	public final Map<String, List<String>> getVendorvpnData(
			final String vendorType) {
		this.inputData = new HashMap<String, List<String>>();
		this.list = new ArrayList<String>();
		if ("vendorvpn".equals(vendorType)) {
			this.list.add("AZJL606|124325");
		} else {
			this.list.add("AZJL606");
		}
		this.inputData.put("vendorvpn", this.list);
		return this.inputData;
	}

	/**
	 * this method obtained the excepted output data for validateRequest with
	 * vendorVpn and vpn request.
	 * 
	 * @return Map
	 */
	public final Map<String, List<String>> getOutputdata() {
		final Map<String, List<String>> outPutData = new HashMap<String, List<String>>();
		this.list = new ArrayList<String>();
		final List<String> vnList = new ArrayList<String>();
		this.list.add("AZJL606");
		vnList.add("124325");
		outPutData.put("vendorvpn", this.list);
		outPutData.put("vendornumber", vnList);
		return outPutData;
	}

	/**
	 * this method returns list of products
	 * @return ProductList
	 */
	public final ProductList getResponseData() {
		final Map<String, List<String>> inputMap = new HashMap<String, List<String>>();
		final List<String> list = new ArrayList<String>();
		list.add("1800303AZJL606");
		inputMap.put("productcode", list);
		final ProductList prdList = new ProductList();
		final List<ProductDetails> prdDetails = new ArrayList<ProductDetails>();
		final ProductDetails product = new ProductDetails();
		product.setProductCode("1800303AZJL606");
		prdDetails.add(product);
		prdList.setProducts(prdDetails);
		return prdList;
	}
}
