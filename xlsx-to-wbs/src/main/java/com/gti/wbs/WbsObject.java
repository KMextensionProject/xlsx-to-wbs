package com.gti.wbs;

import java.util.Objects;

public abstract class WbsObject {

	protected String levelNumber;
	protected String description;

	public WbsObject(String name) {
		this.description = name;
	}

	public String getDescription() {
		return this.description;
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
