package com.gti.model;

import java.util.Objects;

public abstract class NamedObject {

	protected String name;

	public NamedObject(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(name);
	}

	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		} else if (object instanceof NamedObject) {
			return ((NamedObject) object).getName().equals(name);
		}
		return false;
	}

	@Override
	public String toString() {
		return "NamedObject [name=" + name + "]";
	}
}
