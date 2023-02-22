package com.gti.core;

import static com.gti.util.XlsxUtils.getCellValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor;

import com.gti.model.Activity;
import com.gti.model.Output;
import com.gti.model.Phase;
import com.gti.model.Subactivity;
import com.gti.model.Task;
import com.gti.util.ColumnMapper;
import com.gti.util.WbsUtils;

public class Runner2 {
// TODO: take color from excel based on each value.. let the user decide whether to use it or not --colorless
// TODO: when pulling something out from the hierarchy... we need not to save such values that are not present in excel -> check the mapper
// TODO: implement style options + hide configuration construction? -> let user know of sheet where to define column mapping
	public static void main(String[] args) throws IOException {

		// load data from excel
		Workbook workbook = new XSSFWorkbook(Runner2.class.getClassLoader().getResourceAsStream("sample.xlsx"));
		Sheet sheet = workbook.getSheetAt(0);
		Row titleRow = sheet.getRow(6);
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

			// code copied from StackOverflow:
//			XSSFCellStyle cellStyle = ((XSSFCellStyle) row.getCell(mapper.getColumnIndex("stav")).getCellStyle());
//			XSSFColor xssfColor = cellStyle.getFillBackgroundXSSFColor();
//			if (xssfColor != null) {
//				CTColor ctFillColor = xssfColor.getCTColor();
//				byte[] argb = ctFillColor.getRgb();
//				String hexColor = String.format("#%02x%02x%02x%02x", argb[0], argb[1], argb[2], argb[3]);
//				System.out.println(hexColor.toUpperCase());
//			}

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
			task.setStatus(statusValue);
			task.setSolver(solverValue);
//			task.setPriority(Integer.parseInt(priorityValue)); // parse from double
//			task.setFinishedInPercent(Integer.parseInt(finishedInPercentValue)); // parse from double
			task.setOutput(new Output(outputNameValue)); // add resolved value
			subactivity.getTasks().add(task);
		}

		activities.sort(Comparator.comparing(Activity::getName));
		String wbsConfig = WbsUtils.createConfig(activities, "MUSP");
//		System.out.println(wbsConfig);
		System.out.println(WbsUtils.generateWbsSvg(wbsConfig, "sample.svg"));
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
