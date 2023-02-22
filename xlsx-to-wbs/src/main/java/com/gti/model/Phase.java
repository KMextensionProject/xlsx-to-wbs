package com.gti.model;

import java.util.ArrayList;
import java.util.List;

public class Phase extends NamedObject {

	private List<Subactivity> subactivities;

	public Phase(String phaseName) {
		super(phaseName);
		this.subactivities = new ArrayList<>();
	}

	public List<Subactivity> getSubactivities() {
		return this.subactivities;
	}

	@Override
	public String toString() {
		return "Phase [name=" + name + ", subactivities=" + subactivities + "]";
	}

}
