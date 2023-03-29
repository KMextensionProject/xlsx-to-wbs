package com.gti.wbs;

import static com.gti.util.StringUtils.repeat;
import static java.lang.System.lineSeparator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static java.util.stream.Collectors.joining;

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

	/**
	 * 
	 * @param outputFile
	 * @param fileFormat
	 * @return
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
		// TODO: move to a separate class -> check usage below
		private final List<String> pos = new ArrayList<>(Arrays.asList(""));

		private static final String WBS_START_TAG = "@startwbs";
		private static final String WBS_END_TAG = "@endwbs";
		private String boxingTag;

		private final Wbs config = new Wbs();

		/**
		 *
		 * @param boxed
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
		 *
		 */
		public WbsBuilder withTopLevelNodeName(String topLevelNodeName) {
			config.topLevelNodeName = topLevelNodeName;
			return this;
		}

		/**
		 *
		 */
		public WbsBuilder withNodeStyle(NodeStyle nodeStyle) {
			config.nodeStyle = nodeStyle;
			return this;
		}

		/**
		 *
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

//			System.out.println(configDef);
			config.configuration = configDef.toString();
			return config;
		}

		// TODO: add position labeling here
		// TODO: display N/A values? let user specify which values to omit?
		@SuppressWarnings("unchecked")
		private void appendActivities(Map<String, Object> activities, StringBuilder configString, int level) {
			int depth = level;
			for (Map.Entry<String, Object> entry : activities.entrySet()) {
				configString.append(getLevelMark(depth))
							.append(boxingTag)
							.append(assignLevelNumber(depth))
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

		// TODO: refactor this mess
		private String assignLevelNumber(int depth) {
			if (depth < pos.size()) {
				pos.add(depth, (Integer.parseInt((pos.remove(depth))) + 1) + "");
				for (int i = depth + 1; i < pos.size(); i++) {
					pos.remove(i);
				}
			} else {
				pos.add(1 + "");
			}

			for (int i = depth + 1; i < pos.size(); i++) {
				pos.remove(i);
			}

			return pos.stream().skip(1).collect(joining(".")) + " ";
		}

		// TODO: refactor
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
