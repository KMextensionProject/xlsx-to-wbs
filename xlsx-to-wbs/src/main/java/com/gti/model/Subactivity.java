package com.gti.model;

import java.util.ArrayList;
import java.util.List;

public class Subactivity extends NamedObject {

	private List<Task> tasks;

	public Subactivity(String subactivityName) {
		super(subactivityName);
		this.tasks = new ArrayList<>();
	}

	public List<Task> getTasks() {
		return tasks;
	}

	@Override
	public String toString() {
		return "Subactivity [name=" + name + ", " 
			+ "tasks=" + tasks + "]";
	}

}
