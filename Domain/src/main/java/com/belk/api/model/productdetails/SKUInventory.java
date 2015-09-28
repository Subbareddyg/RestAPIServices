package com.belk.api.model.productdetails;

/**
 * Pojo for SKU Inventory.
 * 
 * @author Mindtree
 * @date Jul 24, 2013
 */
public class SKUInventory {
	/**
	 * inventoryLevel is available inventory level in SKU.
	 */
	private String inventoryLevel = "";
	/**
	 * inventoryAvailable is available inventory in SKU.
	 */
	private String inventoryAvailable = "";

	/**
	 * Default constructor.
	 */
	public SKUInventory() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the inventoryLevel
	 */
	public final String getInventoryLevel() {
		return this.inventoryLevel;
	}

	/**
	 * @param inventoryLevel
	 *            the inventoryLevel to set
	 */
	public final void setInventoryLevel(final String inventoryLevel) {
		this.inventoryLevel = inventoryLevel;
	}

	/**
	 * @return the inventoryAvailable
	 */
	public final String getInventoryAvailable() {
		return this.inventoryAvailable;
	}

	/**
	 * @param inventoryAvailable
	 *            the inventoryAvailable to set
	 */
	public final void setInventoryAvailable(final String inventoryAvailable) {
		this.inventoryAvailable = inventoryAvailable;
	}

}
