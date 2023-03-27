package com.gti.xlsx;

public class ColumnProperty {

	private String location;
	private boolean hasPercentageIndicator;

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

	public String getLocationCode() {
		return location;
	}

	public boolean hasPercentageIndicator() {
		return hasPercentageIndicator;
	}

}
