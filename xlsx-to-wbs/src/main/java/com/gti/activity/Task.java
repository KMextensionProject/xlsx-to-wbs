package com.gti.activity;

import java.time.LocalDate;

public class Task {

	private String status; // one of TaskStatus should those two be static inner classes?
	private String solver;
	private int priority; // one of TaskPriority
	private int finishedInPercent;
	private LocalDate from;
	private LocalDate to;
	private Output output; // may be empty

	// all null checks
	public boolean isEmpty() {
		return false;
	}

}
