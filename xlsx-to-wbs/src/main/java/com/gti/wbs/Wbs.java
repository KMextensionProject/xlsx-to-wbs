package com.gti.wbs;

import static com.gti.enums.DateFormat.SLOVAK_DATE_FORMAT;
import static java.lang.System.lineSeparator;

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
	private boolean statusColoring;
	private String topLevelNodeName;
	private List<Activity> data;
	private NodeStyle nodeStyle;
	private String configuration;

	private Wbs() {
		this.boxed = true;
	}

	public boolean isBoxed() {
		return this.boxed;
	}

	public boolean hasStatusColoring() {
		return this.statusColoring;
	}

	public String getTopLevelNodeName() {
		return this.topLevelNodeName;
	}

	public String getConfigurationString() {
		return this.configuration;
	}

	public NodeStyle getNodeStyle() {
		return this.nodeStyle;
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

		private StringBuilder configuration = new StringBuilder();
		private final Wbs config = new Wbs();

		public WbsBuilder makeBoxed(boolean boxed) {
			config.boxed = boxed;
			return this;
		}

		/**
		 * Coloring based on the mapping in the TaskStatus enum.
		 */
		public WbsBuilder withStatusBasedTaskColoring(boolean coloring) {
			config.statusColoring = coloring;
			return this;
		}

		public WbsBuilder withTopLevelNodeName(String topLevelNodeName) {
			config.topLevelNodeName = topLevelNodeName;
			return this;
		}

		public WbsBuilder withNodeStyle(NodeStyle nodeStyle) {
			config.nodeStyle = nodeStyle;
			return this;
		}

		public Wbs buildWbs(List<Activity> data) {
			config.data = new ArrayList<>(data);
			if (config.data.isEmpty()) {
				throw new IllegalStateException("data must be defined");
			}

			// extract to separate method
			boxingTag = config.boxed ? " " : "_ ";
			configuration.append(WBS_START_TAG);
			appendStyleDefinition();
			appendTopLevelNode();
			appendData();
			configuration.append(WBS_END_TAG);

			config.configuration = configuration.toString();
			return config;
		}

		private void appendStyleDefinition() {
			if (config.nodeStyle != null) {
				configuration.append(lineSeparator())
					.append(config.nodeStyle.getStyleDefinition());
			}
		}

		private void appendTopLevelNode() {
			configuration
				  .append(lineSeparator())
				  .append("*")
				  .append(boxingTag);
			if (config.topLevelNodeName == null) {
				config.topLevelNodeName = "PROJEKT";
			}
			configuration.append(config.topLevelNodeName)
			  	  .append(lineSeparator());
		}

		private void appendData() {
			for (Activity activity : config.data) {
				configuration.append("**");
				appendName(activity);
				for (Phase phase : activity.getPhases()) {
					configuration.append("***");
					appendName(phase);
					for (Subactivity subactivity : phase.getSubactivities()) {
						configuration.append("****");
						appendName(subactivity);
						for (Task task : subactivity.getTasks()) {
							configuration.append("*****");
							appendTask(task);
						}
					}
				}
			}
		}

		private void appendName(WbsObject wbsObject) {
			configuration.append(boxingTag)
				.append(wbsObject.getPositionNumber())
				.append(" ")
				.append(wbsObject.getDescription())
				.append(lineSeparator());
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

			configuration.append(config.statusColoring ? task.getStatus().getColorCode() : "")
				.append(boxingTag)
				.append(task.getPositionNumber())
				.append(" ")
				.append(name);

			// if null -> do not appear them ..dates can also be N/A but it translates to null
			if (solver != null) {
				configuration.append("\\nRiešiteľ: ").append(solver);
			}
			if (status != null) {
				configuration.append("\\nStav: ")
					.append(status);
					if (percentage >= 0) {
						configuration.append(" (")
							.append(percentage)
							.append("%)");
					}
			}
			// should it be displayed even if it is excluded?
			if (priority >= TaskPriority.EXCLUDED) {
				configuration.append("\\nPriorita: ").append(priority);
			}
			if (from != null) {
				configuration.append("\\nOd: ").append(from);
			}
			if (to != null) {
				configuration.append("\\nDo: ").append(to);
			}

			configuration.append(lineSeparator());
		}
	}
}
