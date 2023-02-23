package com.gti.util;

import java.io.FileOutputStream;
import java.io.IOException;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

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
			.append("****[#SkyBlue] Model of AsIs Processes Completed1")
			.append(System.lineSeparator())
			.append("****[#salmon] Model of AsIs Processes Completed2")
			.append(System.lineSeparator())
			.append("***[#lightgreen] Measure AsIs performance metrics")
			.append(System.lineSeparator())
			.append("***[#orange] Identify Quick Wins")
			.append(System.lineSeparator())
			.append("** Complete innovate phase()")
			.append(System.lineSeparator())
			.append("@endwbs")
			.toString();
	}

	public static String save(String outputFile, FileFormat fileFormat, String config) {
		SourceStringReader reader = new SourceStringReader(config);
		String result;
		try {
			result = reader.outputImage(new FileOutputStream(outputFile), new FileFormatOption(FileFormat.SVG)).getDescription();
		} catch (IOException ioex) {
			result = "ERROR: " + ioex.getMessage();
		}
		return result;
	}
	
	public static void main(String[] args) {
		save("sampleColor.svg", FileFormat.SVG, getSampleSource());
	}
//
//	// make a class out of this
//	public static String createConfig(List<Activity> activities, String topLevelName) {
//		StringBuilder config = new StringBuilder("@startwbs")
//			.append(System.lineSeparator())
//			.append("* ")
//			.append(topLevelName)
//			.append(System.lineSeparator());
//
//		for (Activity activity : activities) {
//			config.append("** ")
//				.append(activity.getName())
//				.append(System.lineSeparator());
//			for (Phase phase : activity.getPhases()) {
//				config.append("*** ")
//					.append(phase.getName())
//					.append(System.lineSeparator());
//				for (Subactivity subactivity : phase.getSubactivities()) {
//					config.append("**** ")
//						.append(subactivity.getName())
//						.append(System.lineSeparator());
//					for (Task task : subactivity.getTasks()) {
//						// TODO: append all its values + add styles to differentiate between completed tasks and not (hint: take color from xlsx)
//						config.append("***** ")
//							.append(task.getName())
//							.append(System.lineSeparator());
//					}
//				}
//			}
//		}
//		return config.append("@endwbs").toString();
//	}
//
//	public static String generateWbsSvg(String config, String outputSVG) {
//		SourceStringReader reader = new SourceStringReader(config);
//		String result;
//		try {
////			result = reader.outputImage(new FileOutputStream(outputPNG)).getDescription();
//			result = reader.outputImage(new FileOutputStream(outputSVG), new FileFormatOption(FileFormat.SVG)).getDescription();
//		} catch (IOException ioex) {
//			result = "ERROR: " + ioex.getMessage();
//		}
//		return result;
//	}
}
