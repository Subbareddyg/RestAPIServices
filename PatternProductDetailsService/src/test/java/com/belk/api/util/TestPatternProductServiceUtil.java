package com.belk.api.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.belk.api.constants.ProductConstant;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.model.productdetails.Price;
import com.belk.api.model.productdetails.ProductDetails;
import com.belk.api.model.productdetails.ProductFlag;
import com.belk.api.model.productdetails.ProductHierarchyAttribute;
import com.belk.api.model.productdetails.ProductList;
import com.belk.api.model.productdetails.ProductSKUImage;
import com.belk.api.model.productdetails.SKU;
import com.belk.api.model.productdetails.SKUImage;
import com.belk.api.model.productdetails.SKUImageList;
import com.belk.api.model.productdetails.SKUInventory;
import com.belk.api.model.productdetails.SKUMain;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * Test methods for pattern product details service util
 * 
 * @author Mindtree
 * @date 25 Oct 2013
 * 
 */
public class TestPatternProductServiceUtil {

	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	Map<String, List<String>> uriMap = new HashMap<String, List<String>>();
	Map<String, String> productDetailsRequestMap;
	Map<String, String> productDetailsResultMap;
	List<Map<String, String>> blueMartiniResultMap;
	ProductList prdList;
	List<ProductDetails> prdDetails;
	ProductDetails product;
	final List<String> list = new ArrayList<String>();

	/**
	 * Test method to createRequestMap which returns requestMap with hardcoded
	 * product code
	 * 
	 * @return Map<String, String> requestMap
	 */

	public static Map<String, String> createRequestMap() {

		final Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("prdCode", "3201634Z31300");
		return requestMap;

	}

	/**
	 * Test method to createURIMap which returns uriMap with hardcoded product
	 * code for testing
	 * 
	 * @return Map<String, List<String>>
	 */
	public static Map<String, List<String>> createPatternProductDetailsURIMap() {

		final Map<String, List<String>> uriMap = new HashMap<String, List<String>>();

		// Creating the Request Map for CategoryProductService Impl
		// getCategoryProducts
		// Consisting of category ids
		final List<String> list1 = new ArrayList<String>();
		list1.add("3201634Z31300");

		uriMap.put("productCode", list1);

		return uriMap;

	}

	/**
	 * This method obtains the Map based on the response of
	 * ProcessRequestAttributeResult.
	 * 
	 * @param list
	 *            has the values which would have been sent from the request
	 * @return Map returns the formatted map with request attribute value mapped
	 *         to the endeca value
	 */
	public final Map<String, String> processRequestAttrResult(
			final List<String> list) {
		for (final String productCode : list) {
			this.productDetailsRequestMap = new HashMap<String, String>();
			this.productDetailsRequestMap.put("P_product_code", productCode);
		}
		return this.productDetailsRequestMap;
	}

	/**
	 * Test method to createSearchResults which returns product list object with
	 * hardcoded results from blue martini
	 * 
	 * @return ProductList
	 * @throws JSONException
	 *             when Json object is failed this exception is thrown
	 * 
	 */

	public final ProductList getPatternProductDetails() throws JSONException {

		final ProductList productList = new ProductList();
		final ProductDetails productDetails = new ProductDetails();
		final List<ProductDetails> productDetailsList = new ArrayList<ProductDetails>();
		List<SKU> skuList = new ArrayList<SKU>();
		List<ProductFlag> productFlagList = new ArrayList<ProductFlag>();

		final JSONObject json = this.blueMartiniResultList();

		productDetails.setBrand(json.getJSONObject("productCode").getString(
				"ATR_brand"));
		productDetails.setBrand("Nautica");
		productDetails.setIsPattern("F");
		productDetails.setLongDescription("");
		productDetails.setName("Big & Tall Pieced Polo");
		productDetails.setProductCode("3201634Z31300");

		productFlagList = this.testPopulatePatternProductFlag();
		productDetails.setProductFlags(productFlagList);

		productDetails.setProductId("");

		// productDetails.setProductPrice(productPrice);
		productDetails.setProductType("Knit Tops");
		productDetails.setShortDescription("Big & Tall Pieced Polo");
		skuList = this.testPopulatePatternSkus();
		productDetails.setSkus(skuList);
		productDetails.setVendorId("3201634");
		productDetails.setWebId("");

		productDetailsList.add(productDetails);

		productList.setProducts(productDetailsList);

		return productList;

	}

	/**
	 * Test Method to populate the Pattern Product Flags
	 * 
	 * @return List<ProductFlag>
	 */
	public final List<ProductFlag> testPopulatePatternProductFlag() {

		final List<ProductFlag> productFlagList = new ArrayList<ProductFlag>();
		ProductFlag productFlag = null;
		productFlag = new ProductFlag();

		productFlag.setKey(ProductConstant.ON_SALE);
		productFlag.setValue("false");
		productFlagList.add(productFlag);

		productFlag = new ProductFlag();
		productFlag.setKey(ProductConstant.AVAILABLE_INSTORE);
		productFlag.setValue(" ");

		productFlag = new ProductFlag();
		productFlag.setKey(ProductConstant.AVAILABLE_ONLINE);
		productFlag.setValue(" ");
		productFlagList.add(productFlag);

		productFlag = new ProductFlag();
		productFlag.setKey(ProductConstant.CLEARANCE);
		productFlag.setValue("false");
		productFlagList.add(productFlag);

		productFlag = new ProductFlag();
		productFlag.setKey(ProductConstant.NEW_ARRIVAL);
		productFlag.setValue("false");
		productFlagList.add(productFlag);

		productFlag = new ProductFlag();
		productFlag.setKey(ProductConstant.IS_BRIDAL);
		productFlag.setValue("false");
		productFlagList.add(productFlag);

		// productDetails.setProductFlags(productFlagList);

		return productFlagList;

	}

	/**
	 * Test method to create BlueMartiniResultList which returns json object
	 * with hardcoded results from blue martini
	 * 
	 * @return JSONObject when Json object is failed this exception is thrown
	 */

	public final JSONObject blueMartiniResultList() {

		final JSONObject jsonObject = new JSONObject();
		try {

			jsonObject.put("productCode", "3201634Z31300");

			final Map<String, String> parentProductMap = this.getParentProductMap();

			jsonObject.put("parentProduct", parentProductMap);

			final Map<String, String> compositeProductDetailsMap = this.getCompositeProductDetailMap();

			jsonObject.put("compositeProductDetails",
					compositeProductDetailsMap);

			final Map<String, String> skuDetailsMap1 = this.getSkuDetailMap1();

			final Map<String, String> skuDetailsMap2 = this.getSkuDetailMap2();

			final Map<String, String> skuDetailsMap3 = new HashMap<String, String>();
			skuDetailsMap3.put("skuCode", "0400675912651");
			skuDetailsMap3.put("upc", "0670113311661");
			skuDetailsMap3.put("ATR_color_desc", "Navy");
			skuDetailsMap3.put("ATR_super_color_2", "Navy");
			skuDetailsMap3.put("AvailableInventory", "50");
			skuDetailsMap3.put("OnHandInventory", "1,000");

			final Map<String, String> skuDetailsMap4 = new HashMap<String, String>();
			skuDetailsMap4.put("skuCode", "0400675912712");
			skuDetailsMap4.put("upc", "0670113311722");
			skuDetailsMap4.put("ATR_color_desc", "Navy");
			skuDetailsMap4.put("ATR_super_color_2", "Navy");
			skuDetailsMap4.put("AvailableInventory", "50");
			skuDetailsMap4.put("OnHandInventory", "100");

			final Object[] array = new Object[] {skuDetailsMap1,
					skuDetailsMap2, skuDetailsMap3, skuDetailsMap4 };

			jsonObject.put("skus", array[3]);

		} catch (final JSONException e) {

			e.printStackTrace();
		}

		return jsonObject;

	}

	/**
	 * Method extracted from blueMartiniResultList to avoid checkstyle
	 * violations returns the map containing String as key and String as
	 * argument
	 * 
	 * @return skuDetailsMap2
	 */
	private Map<String, String> getSkuDetailMap2() {
		final Map<String, String> skuDetailsMap2 = new HashMap<String, String>();
		skuDetailsMap2.put("skuCode", "0400675912668");
		skuDetailsMap2.put("upc", "0670113311678");
		skuDetailsMap2.put("ATR_color_desc", "Navy");
		skuDetailsMap2.put("ATR_super_color_2", "Navy");
		skuDetailsMap2.put("AvailableInventory", " ");
		skuDetailsMap2.put("OnHandInventory", " ");
		return skuDetailsMap2;
	}

	/**
	 * Method extracted from blueMartiniResultList to avoid checkstyle
	 * violations returns the map containing String as key and String as
	 * argument
	 * 
	 * @return skuDetailsMap1
	 */
	private Map<String, String> getSkuDetailMap1() {
		final Map<String, String> skuDetailsMap1 = new HashMap<String, String>();
		skuDetailsMap1.put("skuCode", "0400675912644");
		skuDetailsMap1.put("upc", "0670113311654");
		skuDetailsMap1.put("ATR_color_desc", "Navy");
		skuDetailsMap1.put("ATR_super_color_2", "Navy");
		skuDetailsMap1.put("AvailableInventory", " ");
		skuDetailsMap1.put("OnHandInventory", " ");
		return skuDetailsMap1;
	}

	/**
	 * Method extracted from blueMartiniResultList to avoid checkstyle
	 * violations returns the map containing String as key and String as
	 * argument
	 * 
	 * @return parentProductMap
	 */
	private Map<String, String> getParentProductMap() {
		final Map<String, String> parentProductMap = new HashMap<String, String>();

		parentProductMap.put("ATR_vendor_number", "3201634");
		parentProductMap.put("ATR_bridal_eligible", "F");
		parentProductMap.put("ATR_brand", "Nautica");
		parentProductMap.put("ATR_product_name", "Big & Tall Pieced Polo");
		parentProductMap.put("shortDesc", "Big & Tall Pieced Polo");
		parentProductMap.put("ATR_prod_type", "Knit Tops");
		return parentProductMap;
	}

	/**
	 * Method extracted from blueMartiniResultList to avoid checkstyle
	 * violations returns the map containing String as key and String as
	 * argument
	 * 
	 * @return compositeProductDetailsMap
	 */
	private Map<String, String> getCompositeProductDetailMap() {
		final Map<String, String> compositeProductDetailsMap = new HashMap<String, String>();

		compositeProductDetailsMap.put("displayableListPrice", "65.00");
		compositeProductDetailsMap.put("displayableSalePrice", "65.00");
		compositeProductDetailsMap.put("isOnClearance", "F");
		compositeProductDetailsMap.put("isNewArrival", "F");
		compositeProductDetailsMap.put("hasVaryingListPrice", "F");
		compositeProductDetailsMap.put("hasVaryingSalePrice", "F");
		return compositeProductDetailsMap;
	}

	/**
	 * Method to create multivaluedmap
	 * 
	 * @return MultivaluedMap<String, String>
	 */

	public final MultivaluedMap<String, String> createURIParametersMap() {
		final MultivaluedMap<String, String> uriParametersMap = new MultivaluedMapImpl();

		uriParametersMap.add("prdCode", "3201634Z31300");
		return uriParametersMap;

	}

	/**
	 * Method which creates invalid URI map of data
	 * 
	 * @return Map<String, List<String>>
	 */

	public final Map<String, List<String>> createURIInvalidDataMap() {
		final Map<String, List<String>> uriMap = new HashMap<String, List<String>>();
		final List<String> list1 = new ArrayList<String>();
		list1.add("3201634Z31hjahav");
		uriMap.put("prdCode", list1);

		return uriMap;

	}

	/**
	 * Test method to populate the pattern skus
	 * 
	 * @return List<SKU>
	 */

	public final List<SKU> testPopulatePatternSkus() {

		SKU sku = null;
		final List<SKU> skuList = new ArrayList<SKU>();
		Price price = null;
		SKUMain skuMain = null;

		final List<SKUMain> skuMainList = new ArrayList<SKUMain>();

		final SKUImageList skuImageList = new SKUImageList();
		final List<ProductSKUImage> productSKUImages = new ArrayList<ProductSKUImage>();
		final ProductSKUImage productSKUImage = new ProductSKUImage();
		final List<SKUImage> skuImageAttributes = new ArrayList<SKUImage>();
		final List<Price> skuPrice = new ArrayList<Price>();
		sku = this.getSKU();
		skuMain = this.getSkuMain(skuMainList);
		skuMainList.add(skuMain);
		sku.setSkuMainAttributes(skuMainList);
		final SKUInventory skuInventory = this.getSKUInventory();
		sku.setSkuInventory(skuInventory);
		price = this.getPrice();
		skuPrice.add(price);
		price = this.getNewPrice();
		skuPrice.add(price);
		sku.setSkuPrice(skuPrice);
		final SKUImage skuImage = this.getSKUImage();
		skuImageAttributes.add(skuImage);
		productSKUImage.setSkuImageAttribute(skuImageAttributes);
		productSKUImages.add(productSKUImage);
		skuImageList.setProductSKUImages(productSKUImages);
		sku.setSkuImages(skuImageList);
		skuList.add(sku);

		return skuList;
	}

	/**
	 * Method extracted from blueMartiniResultList to avoid checkstyle
	 * violations
	 * 
	 * 
	 * 
	 * @return sku
	 */
	private SKU getSKU() {
		SKU sku;
		sku = new SKU();
		sku.setSkuCode("0400675912644");
		sku.setUpcCode("0670113311654");
		return sku;
	}

	/**
	 * Method extracted from blueMartiniResultList to avoid checkstyle
	 * violations
	 * 
	 * 
	 * @return skuInventory
	 */
	private SKUInventory getSKUInventory() {
		final SKUInventory skuInventory = new SKUInventory();
		skuInventory.setInventoryAvailable("50");
		skuInventory.setInventoryLevel("1000");
		return skuInventory;
	}

	/**
	 * Method extracted from blueMartiniResultList to avoid checkstyle
	 * violations
	 * 
	 * 
	 * @return skuImage
	 */
	private SKUImage getSKUImage() {
		final SKUImage skuImage = new SKUImage();
		skuImage.setKey(ProductConstant.SKU_MAIN_PRODUCT_IMAGE);
		skuImage.setValue("http://s7d4.scene7.com/is/image/Belk?layer=0&src=3201634_Z31300_A_401_T10L00&layer=comp&");
		return skuImage;
	}

	/**
	 * Method extracted from blueMartiniResultList to avoid checkstyle
	 * violations
	 * 
	 * 
	 * @return price
	 */
	private Price getNewPrice() {
		Price price;
		price = new Price();
		price.setKey(ProductConstant.SKU_SALE_PRICE);
		price.setValue("65.00");
		return price;
	}

	/**
	 * Method extracted from blueMartiniResultList to avoid checkstyle
	 * violations
	 * 
	 * @return price
	 */
	private Price getPrice() {
		Price price;
		price = new Price();
		price.setKey(ProductConstant.SKU_LIST_PRICE);
		price.setValue("65.00");
		return price;
	}

	/**
	 * Method extracted from blueMartiniResultList to avoid checkstyle
	 * violations returns the map containing String as key and String as
	 * argument
	 * 
	 * @param skuMainList
	 *            containing SKUMain
	 * @return skuMain
	 */
	private SKUMain getSkuMain(final List<SKUMain> skuMainList) {
		SKUMain skuMain;
		skuMain = new SKUMain();
		skuMain.setKey(ProductConstant.COLOR);
		skuMain.setValue("Navy");
		skuMainList.add(skuMain);
		skuMain = new SKUMain();
		skuMain.setKey(ProductConstant.COLOR);
		skuMain.setValue("Navy");
		skuMainList.add(skuMain);
		skuMain = new SKUMain();
		skuMain.setKey(ProductConstant.COLOR);
		skuMain.setValue("Navy");
		skuMainList.add(skuMain);
		skuMain = new SKUMain();
		skuMain.setKey(ProductConstant.SIZE);
		skuMain.setValue("2X");
		return skuMain;
	}

	/**
	 * This method forms the input RequestMap.
	 * 
	 * @return map.
	 */
	public final Map<String, List<String>> requestMap() {
		this.list.add("1800303AZJL606");
		this.list.add("21009704159");
		this.list.add("32000482988S");
		this.list.add("3201859RA0375");
		this.uriMap.put("productCode", this.list);
		return this.uriMap;
	}

	/**
	 * It obtains the ResultMap.
	 * 
	 * @return List.
	 */
	public final List<Map<String, String>> blueMartiniResultMap() {
		this.blueMartiniResultMap = new ArrayList<Map<String, String>>();
		this.productDetailsResultMap = new HashMap<String, String>();
		this.productDetailsResultMap.put("P_product_code", "1800303AZJL606");
		this.productDetailsRequestMap.put("P_available_in_Store", "No");
		this.productDetailsRequestMap.put("P_available_online", "Yes");
		this.productDetailsRequestMap.put("P_sku_code", "0400672675542");
		this.productDetailsRequestMap.put("P_vendor_number", "1800303");
		this.productDetailsRequestMap.put("P_product_name",
				"Solid Scoop Neck Tank");
		this.blueMartiniResultMap.add(this.productDetailsRequestMap);
		this.blueMartiniResultMap = new ArrayList<Map<String, String>>();
		this.productDetailsResultMap.put("P_product_code", "21009704159");
		this.productDetailsRequestMap.put("P_available_in_Store", "No");
		this.blueMartiniResultMap.add(this.productDetailsRequestMap);
		this.blueMartiniResultMap = new ArrayList<Map<String, String>>();
		this.productDetailsResultMap.put("P_product_code", "32000482988S");
		this.productDetailsRequestMap.put("P_available_in_Store", "No");
		this.blueMartiniResultMap.add(this.productDetailsRequestMap);
		this.blueMartiniResultMap = new ArrayList<Map<String, String>>();
		this.productDetailsResultMap.put("P_product_code", "3201859RA0375");
		this.productDetailsRequestMap.put("P_available_in_Store", "No");
		this.blueMartiniResultMap.add(this.productDetailsRequestMap);
		return this.blueMartiniResultMap;
	}

	/**
	 * It obtains the ProductList which is required to mock
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
		this.populateProductDetails(prdHiererchyAttributeList);
		return this.prdList;
	}

	/**
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

	/**
	 * This test method returns the Map required , to test the mapper class
	 * 
	 * @return endecaResultMap
	 */
	public static Map<String, String> getEndecaResultMapForMapper() {
		final Map<String, String> endecaResultMap = new HashMap<String, String>();
		endecaResultMap.put("MAX_LIST_PRICE", "abc");
		endecaResultMap.put("P_onSale", "xyz");
		endecaResultMap.put("P_list_price_range", "abc");
		endecaResultMap.put("P_sku_code", "efg");
		return endecaResultMap;
	}

	/**
	 * This test method returns the Map required , to test the mapper class
	 * 
	 * @return endecaResultMap
	 */
	public static Map<String, String> getEndecaResultMapForMapperSkus() {
		final Map<String, String> endecaResultMap = new HashMap<String, String>();
		endecaResultMap.put("P_color3", "abc");
		return endecaResultMap;
	}

	/**
	 * This test method returns the Map required , to test the mapper class
	 * 
	 * @return endecaResultMap
	 */
	public static Map<String, String> getEndecaResultMapForSkus() {
		final Map<String, String> endecaResultMap = new HashMap<String, String>();
		endecaResultMap.put("P_color1", "abc");
		return endecaResultMap;
	}

}
