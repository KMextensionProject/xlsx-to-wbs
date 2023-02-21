package com.gti.activity;

import java.util.ArrayList;
import java.util.List;

public class Activity {

	private String name;
	private List<Phase> phases;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Phase> getPhases() {
		return new ArrayList<>(phases);
	}

	public void setPhases(List<Phase> phases) {
		this.phases = new ArrayList<>(phases);
	}
}
