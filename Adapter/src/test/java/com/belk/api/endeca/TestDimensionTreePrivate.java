/**
 * 
 */
package com.belk.api.endeca;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.belk.api.model.Dimension;
import com.belk.api.util.AdapterTestUtil;
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
 * @date Dec 09, 2013
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ DimensionTree.class, DimVal.class, DimValList.class,
		Dimension.class })
public class TestDimensionTreePrivate {

	private final String correlationId = "12345-67891234567-ASDFXEEETTT";

	private final DimensionTree dimensionTree = new DimensionTree();

	/**
	 * @throws java.lang.Exception
	 *             tests the private method.
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
		PowerMockito.when(dimVal.isLeaf()).thenReturn(false);

		PowerMockito.when(dimensionTreeSpy.isProductsRequired()).thenReturn(
				true);
		final Dimension dimension = PowerMockito.mock(Dimension.class);
		final Map<Long, Dimension> idRootDimensionMap = AdapterTestUtil
				.getIdRootDimensionMap();

		PowerMockito.when(dimensionTreeSpy, "createNode", dimension, dimVal,
				idRootDimensionMap, this.correlationId);
	}
}
