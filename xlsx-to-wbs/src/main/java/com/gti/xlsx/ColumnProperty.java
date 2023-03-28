package com.gti.xlsx;

/**
 * This is an immutable holder for user defined column code mapping.
 *
 * @author mkrajcovicux
 */
public final class ColumnProperty {

	private String location;
	private boolean hasPercentageIndicator;

	/**
	 * @param location
	 *            - not null
	 * @throws IllegalArgumentException
	 *             since location can not be null
	 */
	public ColumnProperty(String location) {
		if (location == null) {
			throw new IllegalArgumentException("location must not be null");
		}
		String handledLocation = location;
		if (location.contains("%")) {
			handledLocation = location.replace("%", "");
			this.hasPercentageIndicator = true;
		}
		this.location = handledLocation.toUpperCase();
	}

	/**
	 * @return the excel column code (location) as defined by user. 
	 * 		   <br>Specifically one of [A-Z].
	 */
	public String getLocationCode() {
		return location;
	}

	/**
	 * @return true if this column is used to hold the information in percent,
	 *         false otherwise.
	 */
	public boolean hasPercentageIndicator() {
		return hasPercentageIndicator;
	}

}
