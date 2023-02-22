package com.gti.core;

import static com.gti.xlsx.XlsxUtils.getCellValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gti.enums.TaskStatus;
import com.gti.model.Activity;
import com.gti.model.Output;
import com.gti.model.Phase;
import com.gti.model.Subactivity;
import com.gti.model.Task;
import com.gti.xlsx.ColumnMapper;
import com.gti.xlsx.XlsxMetadata;

public class ActivityLoader {

	public static List<Activity> loadFromXlsx(String xlsx) throws IOException {
		return loadFromXlsx(xlsx, new XlsxMetadata());
	}

	public static List<Activity> loadFromXlsx(String xlsx, XlsxMetadata xlsxConfig) throws IOException {
		Workbook workbook = new XSSFWorkbook(ActivityLoader.class.getClassLoader().getResourceAsStream(xlsx));
		Sheet sheet = workbook.getSheetAt(xlsxConfig.getDataSheetIndex());
		Row titleRow = sheet.getRow(xlsxConfig.getTitleRowIndex());
		ColumnMapper mapper = new ColumnMapper(titleRow);

		List<Activity> activities = new ArrayList<>();

		for (int i = titleRow.getRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);

			// names
			String activityName = getCellValue(row.getCell(mapper.getColumnIndex("aktivita")));
			String phaseName = getCellValue(row.getCell(mapper.getColumnIndex("faza")));
			String subactivityName = getCellValue(row.getCell(mapper.getColumnIndex("podaktivita")));
			String taskName = getCellValue(row.getCell(mapper.getColumnIndex("uloha")));

			// values
			String outputNameValue = getCellValue(row.getCell(mapper.getColumnIndex("vystup")));
			String statusValue = getCellValue(row.getCell(mapper.getColumnIndex("stav")));
			String solverValue = getCellValue(row.getCell(mapper.getColumnIndex("riesitel")));
			String priorityValue = getCellValue(row.getCell(mapper.getColumnIndex("priorita"))); // resolve to number
			String finishedInPercentValue = getCellValue(row.getCell(mapper.getColumnIndex("% dokoncenia"))); // resolve to number
			String fromValue = getCellValue(row.getCell(mapper.getColumnIndex("od aktualny"))); // resolve to localDate
			String toValue = getCellValue(row.getCell(mapper.getColumnIndex("do aktualny"))); // resolve to localDate

			Activity activity = lookupActivity(activities, activityName);
			if (activity == null) {
				activity = new Activity(activityName);
				activities.add(activity);
			}

			Phase phase = lookupPhase(activity, phaseName);
			if (phase == null) {
				phase = new Phase(phaseName);
				activity.getPhases().add(phase);
			}

			Subactivity subactivity = lookupSubactivity(phase, subactivityName);
			if (subactivity == null) {
				subactivity = new Subactivity(subactivityName);
				phase.getSubactivities().add(subactivity);
			}

			Task task = new Task(taskName);
			task.setStatus(resolveStatus(statusValue.trim()));
			task.setSolver(solverValue);
//			task.setPriority(Integer.parseInt(priorityValue)); // parse from double
//			task.setFinishedInPercent(Integer.parseInt(finishedInPercentValue)); // parse from double
			task.setOutput(new Output(outputNameValue)); // add resolved value
			subactivity.getTasks().add(task);
		}

		activities.sort(Comparator.comparing(Activity::getName));
		return activities;
	}

	private static TaskStatus resolveStatus(String taskStatusValue) {
		switch (taskStatusValue) {
		case "Dokončené":
			return TaskStatus.COMPLETED;
		case "Zrušené":
			return TaskStatus.CANCELED;
		case "Oneskorené":
			return TaskStatus.DELAYED;
		case "Podľa plánu":
			return TaskStatus.ACCORDING_TO_PLAN;
		default:
			return TaskStatus.FUTURE_TASK;	
		}
	}

	private static Activity lookupActivity(List<Activity> activities, String activityName) {
		for (Activity activity : activities) {
			if (activity.getName().equals(activityName)) {
				return activity;
			}
		}
		return null;
	}

	public static Phase lookupPhase(Activity activity, String phaseName) {
		for (Phase phase : activity.getPhases()) {
			if (phase.getName().equals(phaseName)) {
				return phase;
			}
		}
		return null;
	}

	public static Subactivity lookupSubactivity(Phase phase, String subactivityName) {
		for (Subactivity subactivity : phase.getSubactivities()) {
			if (subactivity.getName().equals(subactivityName)) {
				return subactivity;
			}
		}
		return null;
	}

}
