package com.belk.api.model.productsearch;

import java.util.List;


/** Representation class for an Endeca refinement. 
 * @author Mindtree
 * @date Oct 10, 2013
 */
public class Refinement {

	/**
	 * refinementAttributes is list of Attribute.
	 */
	private List<Attribute> refinementAttributes;
	/**
	 * Default constructor.
	 */
	public Refinement() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the refinementAttributes
	 */
	public final List<Attribute> getRefinementAttributes() {
		return this.refinementAttributes;
	}

	/**
	 * @param refinementAttributes the refinementAttributes to set
	 */
	public final void setRefinementAttributes(final List<Attribute> refinementAttributes) {
		this.refinementAttributes = refinementAttributes;
	}
	
	
	
}
