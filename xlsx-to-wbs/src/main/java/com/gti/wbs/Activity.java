package com.gti.wbs;

import java.util.ArrayList;
import java.util.List;

public class Activity extends WbsObject {

	private List<Phase> phases;

	public Activity(String activityName) {
		super(activityName);
		phases = new ArrayList<>();
	}

	// must remain mutable for addition
	public List<Phase> getPhases() {
		return phases;
	}

	@Override
	public String toString() {
		return "Activity [description=" + description + ", phases=" + phases + "]";
	}
}
