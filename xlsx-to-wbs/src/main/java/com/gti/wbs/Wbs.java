package com.gti.wbs;

import static com.gti.util.StringUtils.repeat;
import static java.lang.System.lineSeparator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.gti.enums.TaskStatus;
import com.gti.util.Positioner;

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

	/**
	 * @return {@code true} if the coloring based on the mapping in the TaskStatus
	 *         enum is used with this Wbs diagram, {@code false} otherwise.
	 */
	public boolean hasStatusColoring() {
		return this.statusColoring;
	}

	public String getTopLevelNodeName() {
		return this.topLevelNodeName;
	}

	/**
	 * @return the underlying plantuml script which is used to generate Wbs diagram.
	 *         The script contains styles and data provided during object building.
	 */
	public String getConfigurationString() {
		return this.configuration;
	}

	public NodeStyle getNodeStyle() {
		return this.nodeStyle;
	}

	/**
	 * Generates the Wbs diagram from underlying plantuml script and saves it on
	 * file system in desired format listed in {@linkplain FileFormat} enum.
	 *
	 * @param outputFile
	 * @param fileFormat
	 * @return result from delegated planuml engine or error message if IOException
	 *         occurs
	 */
	public String save(String outputFile, FileFormat fileFormat) {
		SourceStringReader reader = new SourceStringReader(getConfigurationString());
		String result;
		try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
			result = reader.outputImage(outputStream, new FileFormatOption(fileFormat)).getDescription();
		} catch (IOException ioex) {
			result = "ERROR: " + ioex.getMessage();
		}
		return result;
	}

	public static class WbsBuilder {

		private static final String WBS_START_TAG = "@startwbs";
		private static final String WBS_END_TAG = "@endwbs";

		private final Positioner positioner = new Positioner();
		private final Wbs config = new Wbs();

		private String boxingTag;

		/**
		 * Make the node surrounded by borders or display it as plain text.
		 * @param boxed - defaults to {@code true}
		 */
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

		/**
		 * Assign text appearing in top level node.
		 */
		public WbsBuilder withTopLevelNodeName(String topLevelNodeName) {
			config.topLevelNodeName = topLevelNodeName;
			return this;
		}

		/**
		 * Apply node style such as coloring, shadowing, node rounding,
		 * text alignment etc.
		 */
		public WbsBuilder withNodeStyle(NodeStyle nodeStyle) {
			config.nodeStyle = nodeStyle;
			return this;
		}

		/**
		 * Builds the Wbs object and generates plantuml script from provided non-empty map.
		 * @param data
		 * @return
		 */
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
			appendActivities(data, configDef, 1);
			configDef.append(WBS_END_TAG);

			config.configuration = configDef.toString();
			return config;
		}

		// display N/A values? let user specify which values to omit?
		@SuppressWarnings("unchecked")
		private void appendActivities(Map<String, Object> activities, StringBuilder configString, int level) {
			int depth = level;
			for (Map.Entry<String, Object> entry : activities.entrySet()) {
				configString.append(getLevelMark(depth))
							.append(boxingTag)
							.append(positioner.assignLevelNumber(depth))
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
			// add 1 because of the first node which must be marked with one asterisk
			return repeat("*", level + 1);
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
