package com.gti.xlsx;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
	}
}
