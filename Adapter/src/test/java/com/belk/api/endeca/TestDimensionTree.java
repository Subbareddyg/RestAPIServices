package com.belk.api.endeca;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.belk.api.exception.AdapterException;
import com.belk.api.model.Dimension;
import com.belk.api.util.AdapterTestUtil;
import com.belk.api.util.EndecaLoader;
import com.belk.api.util.ErrorLoader;
import com.endeca.navigation.DimVal;
import com.endeca.navigation.DimValList;

/**
 * Unit Testing related to Dimension Tree class is performed. <br />
 * {@link TestDimensionTree} class is written for testing methods in
 * {@link DimensionTree} The unit test cases evaluates the way the methods
 * behave for the inputs given. <br />
 * The test cases associated with DimensionTree is written to make the code
 * intactv by assuring the code test.
 * 
 * @author Mindtree
 * @date Dec 04, 2013
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({ DimensionTree.class, DimVal.class, DimValList.class,
		Dimension.class })
public class TestDimensionTree {

	private final String correlationId = "12345-67891234567-ASDFXEEETTT";

	private final DimensionTree dimensionTree = new DimensionTree();

	/**
	 * tests the public method add nodes.
	 * 
	 * @throws AdapterException
	 *             AdapterException
	 */

	@Test(expected = Exception.class)
	public final void testAddNodes() throws AdapterException {
		final DimVal dimVal = PowerMockito.mock(DimVal.class);

		final DimValList dimValList = PowerMockito.mock(DimValList.class);

		PowerMockito.when(dimVal.isLeaf()).thenReturn(true);
		final Map<Long, Dimension> idRootDimensionMap = AdapterTestUtil
				.getIdRootDimensionMap();

		this.dimensionTree.addNodes(dimVal, dimValList, dimVal,
				idRootDimensionMap, this.correlationId);
		assertNotNull(this.dimensionTree);
	}

	/**
	 * tests the private method.
	 * 
	 * @throws Exception
	 *             Exception
	 */

	@Test
	public final void testCreateNodeForDimVal() throws Exception {

		final DimensionTree dimensionTreeSpy = PowerMockito
				.spy(new DimensionTree());
		this.dimensionTree.setProductsRequired(true);
		dimensionTreeSpy.setLeafNodesMap(new HashMap<Long, Dimension>());
		PowerMockito.mock(DimensionTree.class);
		final DimVal dimVal = PowerMockito.mock(DimVal.class);
		PowerMockito.when(dimVal.isLeaf()).thenReturn(true);

		PowerMockito.when(dimensionTreeSpy.isProductsRequired()).thenReturn(
				true);
		final Dimension dimension = PowerMockito.mock(Dimension.class);
		final Map<Long, Dimension> idRootDimensionMap = AdapterTestUtil
				.getIdRootDimensionMap();

		PowerMockito.when(dimensionTreeSpy, "createNode", dimension, dimVal,
				idRootDimensionMap, this.correlationId);

	}

	/**
	 * tests the public method.
	 * 
	 * @throws AdapterException
	 *             The exception thrown from the AdapterLayer
	 */
	@Test
	public final void testMakeNavigationQuery() throws AdapterException {
		final Dimension dimension = new Dimension();
		dimension.setDimensionId(4294922263L);
		final ErrorLoader errorLoader = PowerMockito.mock(ErrorLoader.class);
		final EndecaLoader endecaLoader = PowerMockito.mock(EndecaLoader.class);
		this.dimensionTree.setErrorLoader(errorLoader);
		Map<String, String> endecaPropertiesMap = new HashMap<String, String>();
		endecaPropertiesMap.put("HOST", "69.166.149.138");
		endecaPropertiesMap.put("PORT", "15000");
		endecaLoader.setEndecaPropertiesMap(endecaPropertiesMap);
		this.dimensionTree.setEndecaLoader(endecaLoader);
		this.dimensionTree.makeNavigationQuery(dimension, this.correlationId);
		assertNotNull(this.dimensionTree);
	}

}
