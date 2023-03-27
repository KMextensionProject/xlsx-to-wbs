package com.gti.wbs;

import static com.gti.util.StringUtils.repeat;
import static java.lang.System.lineSeparator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.gti.enums.TaskStatus;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

public class Wbs {

	private boolean boxed;
	private boolean statusColoring;
	private String topLevelNodeName;
	private Map<String, Object> data;
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
		private String boxingTag;

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

		public Wbs buildWbs(Map<String, Object> data) {
			config.data = new LinkedHashMap<>(data);
			if (config.data.isEmpty()) {
				throw new IllegalStateException("data must be defined");
			}
			boxingTag = config.boxed ? " " : "_ ";
			StringBuilder configDef = new StringBuilder();
			configDef.append(WBS_START_TAG);
			appendStyleDefinition(configDef);
			appendTopLevelNode(configDef);
			appendActivities(data, configDef, 2);
			configDef.append(WBS_END_TAG);

			System.out.println(configDef);
			config.configuration = configDef.toString();
			return config;
		}

		// TODO: refactor
		// TODO: display N/A values? let user specify which values to omit?
		@SuppressWarnings("unchecked")
		public void appendActivities(Map<String, Object> activities, StringBuilder configString, int level) {
			int depth = level; 
			for (Map.Entry<String, Object> entry : activities.entrySet()) {
				configString.append(getLevelMark(depth))
							.append(boxingTag)
							.append(entry.getKey())
							.append(System.lineSeparator());

				Object value = entry.getValue();
				if (value instanceof Map) {
					Map<String, Object> valueMap = (Map<String, Object>) value;
					appendActivities(valueMap, configString, depth + 1);
				} else if (value instanceof Set) {
					appendTasks(configString, (Set<Map<String, Object>>) value);
				}
			}
		}

		private void appendTasks(StringBuilder configString, Set<Map<String, Object>> tasks) {
			// line separator after task name must be removed so its properties are going into the same wbs box
			configString.delete(configString.lastIndexOf(System.lineSeparator()), configString.length());

			for (Map<String, Object> task : tasks) {
				boolean statusUnresolvedYet = config.statusColoring;
				for (Map.Entry<String, Object> taskEntry : task.entrySet()) {
					Object propertyValue = taskEntry.getValue();

					if (statusUnresolvedYet) {
						// If the property value is one of task status values as described 
						// in the doc, we can then apply the task coloring if it is desired
						TaskStatus status = TaskStatus.getStatusByValue(String.valueOf(propertyValue));

						// move to separate method
						if (TaskStatus.UNDEFINED != status) {
							int taskStart = configString.lastIndexOf("*") + 1; // between the last asterisk and the boxedTag
							if (colorNotPresent(configString, taskStart)) {
								String colorSetting = status.getColorCode();
								configString.insert(taskStart, colorSetting);
							}
							statusUnresolvedYet = false;
						}
					}
					configString.append("\\n")
						.append(taskEntry.getKey())
						.append(": ")
						.append(taskEntry.getValue());
				}
			}
			configString.append(System.lineSeparator());
		}

		private String getLevelMark(int level) {
			return repeat("*", level);
		}

		private boolean colorNotPresent(StringBuilder uml, int fromIndex) {
			return !uml.substring(fromIndex).startsWith("[#");
		}

		private void appendStyleDefinition(StringBuilder configuration) {
			if (config.nodeStyle != null) {
				configuration.append(lineSeparator())
					.append(config.nodeStyle.getStyleDefinition());
			}
		}

		private void appendTopLevelNode(StringBuilder configDef) {
			configDef
				  .append(lineSeparator())
				  .append("*")
				  .append(boxingTag);
			if (config.topLevelNodeName == null) {
				config.topLevelNodeName = "PROJEKT";
			}
			configDef.append(config.topLevelNodeName)
			  	  .append(lineSeparator());
		}
	}
}
