package com.gti.wbs;

import static com.gti.enums.DateFormat.SLOVAK_DATE_FORMAT;
import static com.gti.util.StringUtils.repeat;
import static java.lang.System.lineSeparator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gti.enums.TaskPriority;
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
			appendData(data, configDef, 2);
			configDef.append(WBS_END_TAG);

			System.out.println(configDef);
			
			config.configuration = configDef.toString();
			return config;
		}

		// TODO: refactor
		// TODO: display N/A values?
		public void appendData(Map<String, Object> activities, StringBuilder configString, int level) {
			int depth = level; 
			for (Map.Entry<String, Object> entry : activities.entrySet()) {
				configString.append(getLevelMark(depth)).append(boxingTag).append(entry.getKey()).append(System.lineSeparator());
				Object value = entry.getValue();
				if (value instanceof Map) {
					Map<String, Object> valueMap = (Map<String, Object>) value;
					appendData(valueMap, configString, depth + 1);
				} else if (value instanceof Set) {
					// line separator after task name must be removed so its properties are going into the same wbs box
					configString.delete(configString.lastIndexOf(System.lineSeparator()), configString.length());
					
					for (Map<String, Object> map : (Set<Map<String, Object>>) value) {
						boolean statusUnresolvedYet = config.statusColoring;

						// prechadzam kazdy task
						for (Map.Entry<String, Object> task : map.entrySet()) {
							
							String propertyName = task.getKey();
							
							// if value is null, skip the whole element?
							Object propertyValue = task.getValue();
							
//							 //if user doesnt want to use coloring
							if (statusUnresolvedYet) {
								// If the property value is one of task status values as described in doc,
								// we can then apply the task coloring -- evaluate this once per task..
								TaskStatus status = TaskStatus.getStatusByValue(String.valueOf(propertyValue));
								if (TaskStatus.UNDEFINED != status) {
									String colorSetting = status.getColorCode();
									int taskStart = configString.lastIndexOf("*") + 1; // will be placed between the last asterix and the boxingTag
									configString.insert(taskStart, colorSetting);

									statusUnresolvedYet = false;
								}
							}
							configString.append("\\n").append(task.getKey()).append(": ");
							configString.append(task.getValue());
						}
					}
					configString.append(System.lineSeparator());
				}
			}
		}

		private String getLevelMark(int level) {
			return repeat("*", level);
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
