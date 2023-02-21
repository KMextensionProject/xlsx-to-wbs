package com.gti.util;

import java.util.List;

import com.gti.activity.Activity;

public class WbsUtils {

	public static final String LEVEL_MARK = "*";

	private WbsUtils() {
		throw new IllegalStateException("WbsUtils was not designed to be instantiated.");
	}

	public static String getSampleSource() {
		return new StringBuilder()
			.append("@startwbs")
			.append(System.lineSeparator())
			.append("* Business Process Modelling WBS")
			.append(System.lineSeparator())
			.append("** Launch the project")
			.append(System.lineSeparator())
			.append("*** Complete Stakeholder Research")
			.append(System.lineSeparator())
			.append("*** Initial Implementation Plan")
			.append(System.lineSeparator())
			.append("** Design phase")
			.append(System.lineSeparator())
			.append("*** Model of AsIs Processes Completed")
			.append(System.lineSeparator())
			.append("**** Model of AsIs Processes Completed1")
			.append(System.lineSeparator())
			.append("**** Model of AsIs Processes Completed2")
			.append(System.lineSeparator())
			.append("*** Measure AsIs performance metrics")
			.append(System.lineSeparator())
			.append("*** Identify Quick Wins")
			.append(System.lineSeparator())
			.append("** Complete innovate phase")
			.append(System.lineSeparator())
			.append("@endwbs")
			.toString();
	}

	public static String getSource(List<Activity> activities) {
		
		return "";
	}

}
