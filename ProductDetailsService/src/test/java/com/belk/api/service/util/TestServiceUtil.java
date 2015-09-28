package com.belk.api.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.belk.api.model.productdetails.ProductDetails;
import com.belk.api.model.productdetails.ProductHierarchyAttribute;
import com.belk.api.model.productdetails.ProductList;

/**
 * Unit Testing related to ServiceUtil class is performed. <br />
 * {@link TestServiceUtil} class is written for testing methods in
 * {@link ServiceUtil} The unit test cases evaluates the way the methods behave
 * for the inputs given.
 * 
 * @author Mindtree
 * 
 */
public class TestServiceUtil {
	Map<String, List<String>> uriMap = new HashMap<String, List<String>>();
	Map<String, String> productDetailsRequestMap;
	Map<String, String> productDetailsResultMap;
	List<Map<String, String>> endecaResultMap;
	ProductList prdList;
	List<ProductDetails> prdDetails;
	ProductDetails product;

	final List<String> list = new ArrayList<String>();

	/**
	 * Test method for {@link com.belk.api.service.util#requestMap()}. Testing
	 * is done to check whether the required data is obtained. .
	 * 
	 * @return map.
	 */
	public final Map<String, List<String>> requestMap() {
		this.list.add("1800303AZJL606");
		this.list.add("21009704159");
		this.list.add("32000482988S");
		this.list.add("3201859RA0375");
		this.uriMap.put("styleupc", this.list);
		return this.uriMap;
	}

	public final Map<String, List<String>> requestMapForOptions() {
		this.list.add("3205465FM7130");
		this.uriMap.put("productcode", this.list);
		return this.uriMap;
	}

	/**
	 * Test method for
	 * {@link com.belk.api.service.util#processRequestAttrResult()}. Testing is
	 * done to check whether the required data is obtained. .
	 * 
	 * @param list
	 *            has the values which would have been sent from the request
	 * @return Map returns the formatted map with request attribute value mapped
	 *         to the endeca value
	 * 
	 */
	public final Map<String, String> processRequestAttrResult(
			final List<String> list) {
		for (final String productCode : list) {
			this.productDetailsRequestMap = new HashMap<String, String>();
			this.productDetailsRequestMap.put("productCode", productCode);
		}
		return this.productDetailsRequestMap;
	}

	/**
	 * It Test method for {@link com.belk.api.service.util#endecaResultMap()}.
	 * Testing is done to check whether the required data is obtained. .
	 * 
	 * @return List.
	 */
	public final List<Map<String, String>> endecaResultMap() {
		this.endecaResultMap = new ArrayList<Map<String, String>>();
		this.productDetailsResultMap = new HashMap<String, String>();
		this.productDetailsResultMap.put("productCode", "1800303AZJL606");
		this.productDetailsRequestMap.put("P_available_in_Store", "No");
		this.productDetailsRequestMap.put("P_available_online", "Yes");
		this.productDetailsRequestMap.put("P_sku_code", "0400672675542");
		this.productDetailsRequestMap.put("P_vendor_number", "1800303");
		this.productDetailsRequestMap.put("P_product_name",
				"Solid Scoop Neck Tank");
		this.endecaResultMap.add(this.productDetailsRequestMap);
		this.endecaResultMap = new ArrayList<Map<String, String>>();
		this.productDetailsResultMap.put("productCode", "21009704159");
		this.productDetailsRequestMap.put("P_available_in_Store", "No");
		this.endecaResultMap.add(this.productDetailsRequestMap);
		this.endecaResultMap = new ArrayList<Map<String, String>>();
		this.productDetailsResultMap.put("productCode", "32000482988S");
		this.productDetailsRequestMap.put("P_available_in_Store", "No");
		this.endecaResultMap.add(this.productDetailsRequestMap);
		this.endecaResultMap = new ArrayList<Map<String, String>>();
		this.productDetailsResultMap.put("productCode", "3201859RA0375");
		this.productDetailsRequestMap.put("P_available_in_Store", "No");
		this.endecaResultMap.add(this.productDetailsRequestMap);
		return this.endecaResultMap;
	}

	/**
	 * Test method for {@link com.belk.api.service.util#getProductDetailsList()}
	 * . Testing is done to check whether the required data is obtained. . It
	 * obtains the ProductList which is required to mock
	 * convertToProductDetailsPojo method.
	 * 
	 * @return ProductList
	 */
	public final ProductList getProductDetailsList() {
		this.prdList = new ProductList();
		final List<ProductHierarchyAttribute> prdHiererchyAttributeList = new ArrayList<ProductHierarchyAttribute>();
		this.prdDetails = new ArrayList<ProductDetails>();
		this.product = new ProductDetails();
		this.product.setProductCode("1800303AZJL606");
		this.product.setBrand("Derek Heart");
		this.product.setWebId("1800303AZJL606");
		this.product.setName("Solid Scoop Neck Tank");
		this.product
				.setLongDescription("Perfectly simple, this versatile ribbed tank is a wardrobe");
		final ProductHierarchyAttribute prdHierarchyAttribute = new ProductHierarchyAttribute();
		prdHierarchyAttribute.setKey("1");
		prdHierarchyAttribute.setValue("yes");
		prdHiererchyAttributeList.add(prdHierarchyAttribute);
		populateProductDetails(prdHiererchyAttributeList);
		return this.prdList;
	}

	/**
	 * Test method for
	 * {@link com.belk.api.service.util#populateProductDetails()}. Testing is
	 * done to
	 * 
	 * @param prdHiererchyAttributeList
	 *            list containing the instance of ProductHierarchyAttribute
	 */
	private void populateProductDetails(
			final List<ProductHierarchyAttribute> prdHiererchyAttributeList) {
		this.product.setProductHierarchyAttributes(prdHiererchyAttributeList);
		this.product.setIsPattern("F");
		this.product.setVendorId("AZJL606");
		this.prdDetails.add(this.product);
		this.product = new ProductDetails();
		this.product.setProductCode("21009704159");
		this.product.setBrand("Playtex");
		this.prdDetails.add(this.product);
		this.product = new ProductDetails();
		this.product.setProductCode("32000482988S");
		this.product.setBrand("Gold Toe");
		this.prdDetails.add(this.product);
		this.product = new ProductDetails();
		this.product.setProductCode("3201859RA0375");
		this.product.setBrand("Lacoste");
		this.prdDetails.add(this.product);
		this.prdList.setProducts(this.prdDetails);
	}
}
