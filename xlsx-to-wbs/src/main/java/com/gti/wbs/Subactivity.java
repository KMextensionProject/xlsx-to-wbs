package com.gti.wbs;

import java.util.ArrayList;
import java.util.List;

public class Subactivity extends WbsObject {

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
		return "Subactivity [description=" + description + ", " 
			+ "tasks=" + tasks + "]";
	}

}
