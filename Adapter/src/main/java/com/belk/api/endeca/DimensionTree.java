package com.belk.api.endeca;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.belk.api.constants.CommonConstants;
import com.belk.api.constants.EndecaConstants;
import com.belk.api.constants.ErrorConstants;
import com.belk.api.exception.AdapterException;
import com.belk.api.logger.GenericLogger;
import com.belk.api.logger.LoggerUtil;
import com.belk.api.model.Dimension;
import com.belk.api.model.catalog.Attribute;
import com.belk.api.model.catalog.Product;
import com.belk.api.util.EndecaLoader;
import com.belk.api.util.ErrorLoader;
import com.endeca.navigation.AggrERec;
import com.endeca.navigation.AggrERecList;
import com.endeca.navigation.DimVal;
import com.endeca.navigation.DimValList;
import com.endeca.navigation.ENEQueryException;
import com.endeca.navigation.ENEQueryResults;
import com.endeca.navigation.ERec;
import com.endeca.navigation.ERecList;
import com.endeca.navigation.FieldList;
import com.endeca.navigation.HttpENEConnection;
import com.endeca.navigation.Navigation;
import com.endeca.navigation.PropertyMap;
import com.endeca.navigation.UrlENEQuery;
import com.endeca.navigation.UrlENEQueryParseException;

/**
 * The class to have the list of Dimension objects hierarchically arranged in a
 * tree structure. As per the this structure, products can only be present at a
 * leaf node and no non-leaf node should have any product.
 * 
 * Updated : Added few comments as part of Phase2, April,14 release Logging
 * methods have not been kept in the methods to avoid memory overflow issues.
 * 
 * Update: This class has been modified to meet the requirement of reading the
 * hasSubcategories flag for a dimension not from the configured parameters of
 * Endeca but as per the dimension tree's structure.
 * 
 * @author Mindtree
 * @date Nov 20, 2013
 */
@Service
public class DimensionTree {

	/**
	 * creating instance of logger.
	 */
	// This will return the logger instance
	private static final GenericLogger LOGGER = LoggerUtil.getLogger();

	/**
	 * creating instance of Endeca Loader class.
	 */
	@Autowired
	EndecaLoader endecaLoader;

	/**
	 * creating instance of Error Loader class.
	 */
	@Autowired
	ErrorLoader errorLoader;

	/**
	 * flag to check whether to fetch product information from endeca or not.
	 */
	private boolean productsRequired = false;

	/**
	 * Temporary map introduced to hold references to the leaf nodes of the
	 * dimension tree
	 */
	private Map<Long, Dimension> leafNodesMap;

	/**
	 * Default Constructor.
	 */
	public DimensionTree() {

	}

	/**
	 * This method is used to check the flag value from the URI whether the
	 * products are required or not.
	 * 
	 * @return true if the products required is set to true else false
	 */
	public final boolean isProductsRequired() {
		return this.productsRequired;
	}

	/**
	 * Method to return the map holding the references to the leaf nodes of the
	 * dimension tree.
	 * 
	 * @return Map containing references to leaf dimensions.
	 */
	public final Map<Long, Dimension> getLeafNodesMap() {
		return this.leafNodesMap;
	}

	/**
	 * Method to set the map holding the references to the leaf nodes of the
	 * dimension tree.
	 * 
	 * @param leafNodesMap
	 *            The map to be set.
	 */
	public final void setLeafNodesMap(final Map<Long, Dimension> leafNodesMap) {
		this.leafNodesMap = leafNodesMap;
	}

	/**
	 * setter method for the productsRequired.
	 * 
	 * @param productsRequired
	 *            the value of the productsRequired checked and set
	 */
	public final void setProductsRequired(final boolean productsRequired) {
		this.productsRequired = productsRequired;
	}

	/**
	 * setter method for the endecaLoader.
	 * 
	 * @param endecaLoader
	 *            the endecaLoader to set
	 */
	public final void setEndecaLoader(final EndecaLoader endecaLoader) {
		this.endecaLoader = endecaLoader;
	}

	/**
	 * setter method for the errorLoader.
	 * 
	 * @param errorLoader
	 *            the errorLoader to set
	 */
	public final void setErrorLoader(final ErrorLoader errorLoader) {
		this.errorLoader = errorLoader;
	}

	/**
	 * This method is used to add the nodes obtained from the dimension group
	 * results from endeca. Each node from the result group is fetched and the
	 * required nodes are created.
	 * 
	 * @param rootNode
	 *            the base node which is the current root node.
	 * @param ancestors
	 *            the current DimValList data
	 * @param leaf
	 *            the leaf node information
	 * @param idRootDimensionMap
	 *            the map to which the complete Nodes are getting added and
	 *            created.
	 * @param correlationId
	 *            the Id passed from header
	 * @throws AdapterException
	 *             throws Exception when an exception has occurred related to
	 *             the Endeca.
	 */
	public final void addNodes(final DimVal rootNode,
			final DimValList ancestors, final DimVal leaf,
			final Map<Long, Dimension> idRootDimensionMap,
			final String correlationId) throws AdapterException {
		Dimension root = idRootDimensionMap.get(rootNode.getId());

		if (null == root) {
			if (null == this.leafNodesMap) {
				this.leafNodesMap = new LinkedHashMap<Long, Dimension>();
			}
			root = new Dimension(rootNode.getId());
			root.setName(rootNode.getName());
			idRootDimensionMap.put(rootNode.getId(), root);
		}
		for (Object dimValElement : ancestors) {
			final DimVal dimVal = (DimVal) dimValElement;
			root = this.createNode(root, dimVal, idRootDimensionMap,
					correlationId);
		}
		this.createNode(root, leaf, idRootDimensionMap, correlationId);
	}

	/**
	 * This method creates the node with the required information extracted from
	 * each DimVal.
	 * 
	 * @param parentDimension
	 *            the parent level information of the dimension
	 * @param dimVal
	 *            the dimval which needs to be fetched
	 * @param idRootDimensionMap
	 *            the map to which the complete Nodes are getting added and
	 *            created.
	 * @param correlationId
	 *            the Id passed from header
	 * @return Dimension which is created with the information thats retrieved
	 *         from the Dimval
	 * @throws AdapterException
	 *             throws Exception when an exception has occurred related to
	 *             the Endeca.
	 */
	private Dimension createNode(final Dimension parentDimension,
			final DimVal dimVal, final Map<Long, Dimension> idRootDimensionMap,
			final String correlationId) throws AdapterException {
		final List<Attribute> attributes = new ArrayList<Attribute>();
		Attribute attribute = null;
		Attribute parentAttribute = null;

		Dimension node = idRootDimensionMap.get(dimVal.getId());
		if (null == node) {

			attribute = new Attribute();
			attribute.setKey(CommonConstants.HAS_SUBCATEGORIES);
			attribute.setValue(CommonConstants.NO_VALUE);

			node = new Dimension(dimVal.getId());
			// checks for null value and set Node name if returned value is not
			// null
			if (dimVal.getName() != null) {
				node.setName(dimVal.getName());
			}
			node.setParentDimensionId(parentDimension.getDimensionId());

			attributes.add(attribute);
			node.setDimensionAttributes(attributes);

			/*
			 * add the new node as a leaf node in the map because a new node
			 * will always be a leaf of the branch upto itself
			 */
			this.leafNodesMap.put(node.getDimensionId(), node);
			// remove the parent node as it is no more a leaf
			if (this.leafNodesMap.containsKey(parentDimension.getDimensionId())) {
				this.leafNodesMap.remove(parentDimension.getDimensionId());
			}
			parentDimension.addDimensions(node);

			/*
			 * The following condition avoids repeated addition from multiple
			 * child nodes
			 */
			if (parentDimension.getDimensionAttributes() != null
					&& parentDimension.isLeaf()) {
				parentDimension.setLeaf(false);
				final Iterator<Attribute> parentDimAttributeIterator = parentDimension
						.getDimensionAttributes().iterator();
				while (parentDimAttributeIterator.hasNext()) {
					parentAttribute = parentDimAttributeIterator.next();
					if (parentAttribute.getKey().equalsIgnoreCase(
							CommonConstants.HAS_SUBCATEGORIES)) {
						parentAttribute.setValue(CommonConstants.YES_VALUE);
					}
				}
			}

			idRootDimensionMap.put(dimVal.getId(), node);
		}
		return node;
	}

	/**
	 * This method is to make the navigation query to endeca to fetch the
	 * navigation object information This method is called when the dimVal is
	 * having the leaf node and the corresponding products for the leaf node to
	 * be fetched. For fetching the product data from endeca, Navigaton query to
	 * be made.
	 * 
	 * Update: This method has been made public as it is required to be called
	 * from EndecaUtil externally after the dimension tree population has been
	 * completed.
	 * 
	 * @param node
	 *            the dimension object for which the product information to be
	 *            fetched
	 * @param correlationId
	 *            the Id passed from header
	 * @throws AdapterException
	 *             throws Exception when an exception has occurred related to
	 *             the Endeca.
	 */
	public final void makeNavigationQuery(final Dimension node,
			final String correlationId) throws AdapterException {
		UrlENEQuery query;
		final Map<String, String> errorPropertiesMap = this.errorLoader
				.getErrorPropertiesMap();
		final Map<String, String> endecaPropertiesMap = this.endecaLoader
				.getEndecaPropertiesMap();
		try {

			query = new UrlENEQuery(EndecaConstants.CATEGORY_ID
					+ CommonConstants.EQUALS_SIGN + node.getDimensionId(),
					CommonConstants.UTF_TYPE);
			query.setNavRollupKey(EndecaConstants.ROLL_UP_KEY);
			query.setNavERecsPerAggrERec(1);
			final FieldList propertiesList = new FieldList();
			final List<Product> products = new ArrayList<Product>();
			propertiesList.addField(EndecaConstants.PRODUCT_CODE);
			query.setSelection(propertiesList);
			final HttpENEConnection endecaConn = new HttpENEConnection(
					endecaPropertiesMap.get(EndecaConstants.HOST),
					endecaPropertiesMap.get(EndecaConstants.PORT));
			final ENEQueryResults results = endecaConn.query(query);
			final Navigation nav = results.getNavigation();

			final AggrERecList aggrList = nav.getAggrERecs();
			for (Object aggr : aggrList) {
				final AggrERec aggERec = (AggrERec) aggr;
				final ERecList eRecList = aggERec.getERecs();
				for (Object eRec : eRecList) {
					this.populateProducts(products, eRec, correlationId);
				}
			}
			node.setProducts(products);
		} catch (UrlENEQueryParseException e) {
			LOGGER.error(e, correlationId);
			throw new AdapterException(
					String.valueOf(ErrorConstants.ERROR_CODE_11521),
					errorPropertiesMap.get(String
							.valueOf(ErrorConstants.ERROR_CODE_11521)),
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);

		} catch (ENEQueryException e) {
			LOGGER.error(e, correlationId);
			throw new AdapterException(
					String.valueOf(ErrorConstants.ERROR_CODE_11521),
					errorPropertiesMap.get(String
							.valueOf(ErrorConstants.ERROR_CODE_11521)),
					ErrorConstants.HTTP_STATUS_CODE_INTERNAL_ERROR,
					correlationId);
		}
	}

	/**
	 * This method is to populate the products in the Product object which is
	 * obtained from the eRec object of endeca.
	 * 
	 * @param products
	 *            List of products which needs to be populated.
	 * @param eRec
	 *            ERec object from the Endeca
	 * @param correlationId
	 *            For tracking the request
	 */
	private void populateProducts(final List<Product> products,
			final Object eRec, final String correlationId) {
		final ERec eRecObj = (ERec) eRec;
		final PropertyMap prosMapERec = eRecObj.getProperties();
		final Product product = new Product();
		if (prosMapERec.get(EndecaConstants.PRODUCT_CODE) != null) {
			product.setProductCode((String) prosMapERec
					.get(EndecaConstants.PRODUCT_CODE));
		}
		if ((String) prosMapERec.get(EndecaConstants.PRODUCT_ID) != null) {
			product.setProductId((String) prosMapERec
					.get(EndecaConstants.PRODUCT_ID));
		}
		if (prosMapERec.get(EndecaConstants.WEB_ID) != null) {
			product.setWebId((String) prosMapERec.get(EndecaConstants.WEB_ID));
		}
		products.add(product);
	}

}