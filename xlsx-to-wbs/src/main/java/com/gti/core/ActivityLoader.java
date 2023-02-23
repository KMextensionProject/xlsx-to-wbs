package com.gti.core;

import static com.gti.enums.TaskStatus.getStatusByValue;
import static com.gti.xlsx.XlsxUtils.getCellValue;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gti.wbs.Activity;
import com.gti.wbs.Output;
import com.gti.wbs.Phase;
import com.gti.wbs.Subactivity;
import com.gti.wbs.Task;
import com.gti.xlsx.ColumnMapper;
import com.gti.xlsx.XlsxMetadata;
import com.gti.xlsx.XlsxUtils.CellValue;

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
			String activityName = getCellValue(row.getCell(mapper.getColumnIndex("aktivita"))).asString();
			String phaseName = getCellValue(row.getCell(mapper.getColumnIndex("faza"))).asString();
			String subactivityName = getCellValue(row.getCell(mapper.getColumnIndex("podaktivita"))).asString();
			String taskName = getCellValue(row.getCell(mapper.getColumnIndex("uloha"))).asString();

			// values
			String outputName = getCellValue(row.getCell(mapper.getColumnIndex("vystup"))).asString();
			String status = getCellValue(row.getCell(mapper.getColumnIndex("stav"))).asString();
			String solver = getCellValue(row.getCell(mapper.getColumnIndex("riesitel"))).asString();
			int priority = getCellValue(row.getCell(mapper.getColumnIndex("priorita"))).asInt();
			int finishedInPercent = getCellValue(row.getCell(mapper.getColumnIndex("% dokoncenia"))).asInt(d -> d * 100);

			// strange feeling that this will explode
			LocalDate from = parseLocalDateOrElseNull("od aktualny", row, mapper);
			LocalDate to = parseLocalDateOrElseNull("do aktualny", row, mapper);

			Activity activity = lookupActivity(activities, activityName);
			if (activity == null) {
				activity = new Activity(activityName);
				//activity.setLevelNumber();
				activities.add(activity);
			}

			Phase phase = lookupPhase(activity, phaseName);
			if (phase == null) {
				phase = new Phase(phaseName);
				// phase.setLevelNumber(fromOther(activity.get);
				activity.getPhases().add(phase);
			}

			Subactivity subactivity = lookupSubactivity(phase, subactivityName);
			if (subactivity == null) {
				subactivity = new Subactivity(subactivityName);
				phase.getSubactivities().add(subactivity);
			}

			Task task = new Task(taskName);
			task.setStatus(getStatusByValue(status));
			task.setSolver(solver);
			task.setPriority(priority);
			task.setFinishedInPercent(finishedInPercent);
			task.setFrom(from);
			task.setTo(to);
			task.setOutput(new Output(outputName));
			subactivity.getTasks().add(task);
		}
		workbook.close();

		// could this be my responsibility? fuck no...
//		activities.sort(Comparator.comparing(Activity::getName));
		return activities;
	}

	private static LocalDate parseLocalDateOrElseNull(String fieldName, Row row, ColumnMapper mapper) {
		CellValue cellValue = getCellValue(row.getCell(mapper.getColumnIndex(fieldName)));
		return cellValue.isNull() || cellValue.isUndefined() ? null : cellValue.asLocalDate();
	}

	// TODO: find a better way to do this with lists -> overriding equals won't help in this case.. God knows why
	private static Activity lookupActivity(List<Activity> activities, String activityName) {
		for (Activity activity : activities) {
			if (activity.getDescription().equals(activityName)) {
				return activity;
			}
		}
		return null;
	}

	public static Phase lookupPhase(Activity activity, String phaseName) {
		for (Phase phase : activity.getPhases()) {
			if (phase.getDescription().equals(phaseName)) {
				return phase;
			}
		}
		return null;
	}

	public static Subactivity lookupSubactivity(Phase phase, String subactivityName) {
		for (Subactivity subactivity : phase.getSubactivities()) {
			if (subactivity.getDescription().equals(subactivityName)) {
				return subactivity;
			}
		}
		return null;
	}

}
