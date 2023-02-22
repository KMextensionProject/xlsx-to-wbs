package com.gti.wbs;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gti.model.Activity;
import com.gti.model.Phase;
import com.gti.model.Subactivity;
import com.gti.model.Task;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

public class Wbs {

	private boolean boxed;
	private boolean colorless;
	private String topLevelNodeName;
	private List<Activity> data;
	private StringBuilder configuration;
	private int maxLineWidth;

	private Wbs() {
		this.colorless = true;
		this.boxed = true;
		this.configuration = new StringBuilder();
	}

	public boolean isBoxed() {
		return this.boxed;
	}

	public boolean isColorless() {
		return this.colorless;
	}

	public String getTopLevelNodeName() {
		return this.topLevelNodeName;
	}

	public String getConfigurationString() {
		return this.configuration.toString();
	}

	public int getMaxLineWidth() {
		return this.maxLineWidth;
	}

	public String save(String outputFile, FileFormat fileFormat) {
		SourceStringReader reader = new SourceStringReader(getConfigurationString());
		String result;
		try {
			result = reader.outputImage(new FileOutputStream(outputFile), new FileFormatOption(FileFormat.SVG)).getDescription();
		} catch (IOException ioex) {
			result = "ERROR: " + ioex.getMessage();
		}
		return result;
	}

	public static class WbsBuilder {

		private static final String WBS_START_TAG = "@startwbs";
		private static final String WBS_END_TAG = "@endwbs";
		private String boxingTag = " ";

		private final Wbs config = new Wbs();

		public WbsBuilder makeBoxed(boolean boxed) {
			config.boxed = boxed;
			return this;
		}

		public WbsBuilder makeColorless(boolean colorless) {
			config.colorless = colorless;
			return this;
		}

		/**
		 * Defines max line width in pixels.
		 */
		public WbsBuilder withMaxLineWidth(int width) {
			config.maxLineWidth = width;
			return this;
		}

		public WbsBuilder withTopLevelNodeName(String topLevelNodeName) {
			config.topLevelNodeName = topLevelNodeName;
			return this;
		}

		public Wbs buildWbs(List<Activity> data) {
			config.data = new ArrayList<>(data);
			if (config.data.isEmpty()) {
				throw new IllegalStateException("data must be defined");
			}

			// extract to separate method
			boxingTag = config.boxed ? " " : "_ ";
			config.configuration.append(WBS_START_TAG);
			appendStyleDefinition();
			appendTopLevelNode();
			appendData();
			config.configuration.append(WBS_END_TAG);

			return config;
		}

		private void appendStyleDefinition() {
			if (config.maxLineWidth > 0) {
				config.configuration.append(System.lineSeparator());
				StringBuilder styleDef = new StringBuilder("<style>")
					.append(System.lineSeparator())
					.append("node {")
					.append(System.lineSeparator())
					.append("HorizontalAlignment center")
					.append(System.lineSeparator())
					.append("MaximumWidth ")
					.append(config.maxLineWidth)
					.append(System.lineSeparator())
					.append("}")
					.append(System.lineSeparator())
					.append("</style>");

				config.configuration.append(styleDef);
			}
		}

		private void appendTopLevelNode() {
			config.configuration
				  .append(System.lineSeparator())
				  .append("*")
				  .append(boxingTag);
			if (config.topLevelNodeName == null) {
				config.topLevelNodeName = "PROJECT";
			}
			config.configuration.append(config.topLevelNodeName)
			  	  .append(System.lineSeparator());
		}

		private void appendData() {
			for (Activity activity : config.data) {
				config.configuration.append("**")
					.append(boxingTag)
					.append(activity.getName())
					.append(System.lineSeparator());
				for (Phase phase : activity.getPhases()) {
					config.configuration.append("***")
						.append(boxingTag)
						.append(phase.getName())
						.append(System.lineSeparator());
					for (Subactivity subactivity : phase.getSubactivities()) {
						config.configuration.append("****")
							.append(boxingTag)
							.append(subactivity.getName())
							.append(System.lineSeparator());
						for (Task task : subactivity.getTasks()) {
							config.configuration.append("*****")
								.append(config.colorless ? "" : task.getStatus().getColorCode())
								.append(boxingTag)
								.append(task.getName())
								.append(" (")
								// if solver is N/A -> it isn't there ...make that disappear somehow
								.append(task.getSolver())
								.append(", ")
								.append(task.getStatus().getValue())
								.append(")")
								.append(System.lineSeparator());
						}
					}
				}
			}
		}
	}
}
