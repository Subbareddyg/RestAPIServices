package com.belk.api.model.productsearch;

import java.util.List;

/** Representation class for an Endeca Dimension.
 * @author Mindtree
 * @date Oct 10, 2013
 */

public class Dimension {
	/**
	 * dimensionAttributes is list of Attribute.
	 */
	private List<Attribute> dimensionAttributes;
	/**
	 * refinements is list of Refinement.
	 */
	private List<Refinement> refinements;
	/**
	 * Default constructor.
	 */
	public Dimension() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the dimensionAttribute
	 */
	public final List<Attribute> getDimensionAttributes() {
		return this.dimensionAttributes;
	}
	/**
	 * @param dimensionAttributes the dimensionAttribute to set 
	 */
	public final void setDimensionAttributes(final List<Attribute> dimensionAttributes) {
		this.dimensionAttributes = dimensionAttributes;
	}
	/**
	 * @return the refinements
	 */
	public final List<Refinement> getRefinements() {
		return this.refinements;
	}
	/**
	 * @param refinements the refinements to set
	 */
	public final void setRefinements(final List<Refinement> refinements) {
		this.refinements = refinements;
	}

		
	
}
