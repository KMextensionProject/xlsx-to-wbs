package com.gti.wbs;

import static com.gti.enums.DateFormat.SLOVAK_DATE_FORMAT;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.gti.enums.TaskPriority;

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
			result = reader.outputImage(new FileOutputStream(outputFile), new FileFormatOption(fileFormat)).getDescription();
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

		// TODO: separate to different class / style object
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
				config.topLevelNodeName = "PROJEKT";
			}
			config.configuration.append(config.topLevelNodeName)
			  	  .append(System.lineSeparator());
		}

		private void appendData() {
			for (Activity activity : config.data) {
				config.configuration.append("**");
				appendName(activity);
				for (Phase phase : activity.getPhases()) {
					config.configuration.append("***");
					appendName(phase);
					for (Subactivity subactivity : phase.getSubactivities()) {
						config.configuration.append("****");
						appendName(subactivity);
						for (Task task : subactivity.getTasks()) {
							config.configuration.append("*****");
							appendTask(task);
						}
					}
				}
			}
		}

		private void appendName(WbsObject wbsObject) {
			config.configuration.append(boxingTag)
				.append(wbsObject.getPositionNumber())
				.append(" ")
				.append(wbsObject.getDescription())
				.append(System.lineSeparator());
		}

		private void appendTask(Task task) {
			String name = task.getDescription();
			String solver = task.getSolver();
			String status = task.getStatus().getValue();
			int percentage = task.getFinishedInPercent();
			int priority = task.getPriority();

			LocalDate dateFrom = task.getFrom();
			LocalDate dateTo = task.getTo();
			String from = dateFrom == null ? null : dateFrom.format(SLOVAK_DATE_FORMAT);
			String to = dateTo == null ? null : dateTo.format(SLOVAK_DATE_FORMAT);

			// if solver is N/A -> it isn't there ...make that disappear somehow
			// wait for mbartko what he will say about the undefined fields

			config.configuration.append(config.colorless ? "" : task.getStatus().getColorCode())
				.append(boxingTag)
				.append(task.getPositionNumber())
				.append(" ")
				.append(name);

			// if null -> do not appear them ..dates can also be N/A but it translates to null
			if (solver != null) {
				config.configuration.append("\\nRiesitel: ").append(solver);
			}
			if (status != null) {
				config.configuration.append("\\nStav: ")
					.append(status)
					.append(" (")
					.append(percentage)
					.append("%)");
			}
			// should it be displayed even if it is excluded?
			if (priority >= TaskPriority.EXCLUDED) {
				config.configuration.append("\\nPriorita: ").append(priority);
			}
			if (from != null) {
				config.configuration.append("\\nOd: ").append(from);
			}
			if (to != null) {
				config.configuration.append("\\nDo: ").append(to);
			}

			config.configuration.append(System.lineSeparator());
		}
	}
}
