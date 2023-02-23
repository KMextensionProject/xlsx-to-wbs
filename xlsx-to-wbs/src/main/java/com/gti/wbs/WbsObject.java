package com.gti.wbs;

import java.util.Objects;

public abstract class WbsObject {

	protected String position;
	protected String description;

	public WbsObject(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public void setPositionNumber(String positionNumber) {
		this.position = positionNumber;
	}

	public String getPositionNumber() {
		return this.position;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(description);
	}

	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		} else if (object instanceof WbsObject) {
			return ((WbsObject) object).getDescription().equals(description);
		}
		return false;
	}

	@Override
	public String toString() {
		return "WbsObject [description=" + description + "]";
	}
}
