package com.gti.xlsx;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.gti.util.StringUtils;

public class TreeTest {

	public static void main(String[] args) {
		List<List<String>> rows = Arrays.asList(
			Arrays.asList("Aktivita_1", "Podaktivita_1", "Uloha"),
			Arrays.asList("Aktivita_1", "Podaktivita_2", "Uloha"),
			Arrays.asList("Aktivita_1", "Podaktivita_2", "Uloha2"),
			Arrays.asList("Aktivita_2", "Podaktivita_1", "Uloha"));

		Map<String, Object> activities = new LinkedHashMap<>();
		for (List<String> row : rows) {
			Map<String, Object> parent = activities;
			for (String col : row) {
				if (!parent.containsKey(col)) {
					parent.put(col, new LinkedHashMap<>());
				}
				parent = (Map<String, Object>) parent.get(col);
			}
		}
		System.out.println(activities);
		
		StringBuilder config = new StringBuilder();
		printConfig(activities, config, 1);
		System.out.println(config);
	
		
	}
	private static String boxingTag = " ";
	public static void printConfig(Map<String, Object> activities, StringBuilder config, int level) {
		int depth = level; 
		for (Map.Entry<String, Object> entry : activities.entrySet()) {
			config.append(getLevelString(depth)).append(boxingTag).append(entry.getKey()).append(System.lineSeparator());
			Object value = entry.getValue();
			if (value instanceof Map) {
				Map<String, Object> valueMap = (Map<String, Object>) value;
				printConfig(valueMap, config, depth + 1);
			} else if (value instanceof List) {
				List<Map<String, Object>> list = (List<Map<String, Object>>) value;
				for (Map<String, Object> map : list) {
					for (Map.Entry<String, Object> task : map.entrySet()) {
						config.append(getLevelString(depth)).append(boxingTag);
						config.append(task.getKey()).append(": ");
						config.append(task.getValue()).append(System.lineSeparator());
					}
				}
			}
		}
	}

	private static String getLevelString(int level) {
		return StringUtils.repeat("*", level);
	}
}
