package com.gti.xlsx;

import static com.gti.enums.TaskStatus.getStatusByValue;
import static com.gti.xlsx.XlsxUtils.getCellValue;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gti.util.PositionManager;
import com.gti.wbs.Activity;
import com.gti.wbs.Output;
import com.gti.wbs.Phase;
import com.gti.wbs.Subactivity;
import com.gti.wbs.Task;
import com.gti.xlsx.XlsxUtils.CellValue;

// to generalize the whole tool...I should use maps instead of pojos since, the hierarchy level can vary
public class ActivityLoader {

	// A -> first column in first sheet
	// 2:C -> third column in the second sheet
	// D:% -> fourth column in the first sheet should be displayed as percentage
	public static void main(String[] args) throws IOException {

		/*
		 * AlgoRytmus:
		 * 
		 * - nacitam metadata o excely :
		 * 		[umiestnenie, 
		 * 		 index titulneho riadku, 
		 * 		 index sheetu, 
		 * 		 hierarchiu parent stlpcov, 
		 * 		 vlastnosti (stlpce) posledneho stlpca v hierarchii
		 * 		]
		 * 
		 * - vytvorim mapper pre stlpce, ktory mi dovoli si pytat toto:
		 * 		[index stlpca na zaklade titulneho nazvu,
		 * 		 index stlpca na zaklade kodu stlpca,
		 * 		 nazov stlpca na zaklade indexu stlpca,
		 * 		 kod stlpca na zaklade indexu stlpca
		 * 		]
		 * 
		 * - zacnem prechadzat kazdy jeden riadok v excely:
		 * 		- pre parentov potrebujem len hodnoty, nazvy stlpcov ma nezaujimaju
		 * 		- ak v parentovy natrafim na duplicitnu hodnotu, vznikne jeho child a tak dalej do poslednej vrstvy
		 * 
		 */

		XlsxMetadata xlsxMeta = new XlsxMetadata("/home/UX/mkrajcovicux/Desktop/local_workspace/xlsx-to-wbs/xlsx-to-wbs/src/main/resources/sample.xlsx");
		xlsxMeta.setDataSheetIndex(0);
		xlsxMeta.setTitleRowIndex(2);
		xlsxMeta.setParentColumns(Arrays.asList("B", "C", "D")); // toto potrebujem lepsie parsovat

		Workbook workbook = new XSSFWorkbook(new FileInputStream(xlsxMeta.getFile()));
		Sheet sheet = workbook.getSheetAt(xlsxMeta.getDataSheetIndex());
		Row titleRow = sheet.getRow(xlsxMeta.getTitleRowIndex());
		ColumnMapper mapper = new ColumnMapper(titleRow);

//		Map<String, Object> hierarchicalData = new LinkedHashMap<>(); // order is important here
//		for (String columnCode : xlsxMeta.getParentColumns()) {
//			int columnIndex = mapper.getColumnIndexByCode(columnCode);
//			System.out.println("index: " + columnIndex + ", code: " + columnCode + ", name: " + mapper.getColumnName(columnIndex, "empty"));
//		}

		// sem si budem ukladat vsetky data
		Map<String, Object> hierarchicalData = new LinkedHashMap<>(); // order is important here

		// prechadzam kazdy jeden riadok v sheete
		for (int i = titleRow.getRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);

			// mapa pre tento riadok
			Map<String, Object> data = new LinkedHashMap<>();

			// teraz prechadzam kazdy stlpec pre parentov
			for (String columnCode : xlsxMeta.getParentColumns()) { // should the mapper's method be generic for name and code? ..it would be useful for user maybe

				// ziskam si hodnotu bunky z hierarchickeho stlpca -> B, C, D
				String value = getCellValue(row.getCell(mapper.getColumnIndexByCode(columnCode))).asString(); // zatial vsetko ako string

				// ak moje data neobsahuju tohto parenta (stlpec B), vytvorim si pre neho miesto a pokracujem dalsim slpcom
				if (!data.containsKey(value)) {
					data.put(value, new LinkedHashMap<>());

					// ak moje data uz obsahuju tohto parenta (stlpec B), vlozim novy udaj do najvacsej hlbky pod tymto klucom
				} else {
					data.get(value);
				}
			}
		}

		workbook.close();
	}

	// xlsx meta should contain column names sorted by hierarchy and the last element should have some indicator
	// that below picuses have
	private List<Map<String, Object>> customLoadFromXlsx(XlsxMetadata xlsxMeta) {
		List<String> parentCols = xlsxMeta.getParentColumns();
		List<String> propertyCols = xlsxMeta.getPropertyColumns();

		return null;
	}

	public List<Activity> loadFromXlsx(XlsxMetadata xlsxConfig) throws IOException {
		Workbook workbook = new XSSFWorkbook(new FileInputStream(xlsxConfig.getFile()));
		Sheet sheet = workbook.getSheetAt(xlsxConfig.getDataSheetIndex());
		Row titleRow = sheet.getRow(xlsxConfig.getTitleRowIndex());
		ColumnMapper mapper = new ColumnMapper(titleRow);

		PositionManager positioner = new PositionManager(0);
		List<Activity> activities = new ArrayList<>();

		for (int i = titleRow.getRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);

			String activityName = getCellValue(row.getCell(mapper.getColumnIndex("aktivita"))).asString();
			String phaseName = getCellValue(row.getCell(mapper.getColumnIndex("podaktivita"))).asString();
			String subactivityName = getCellValue(row.getCell(mapper.getColumnIndex("faza"))).asString();
			String taskName = getCellValue(row.getCell(mapper.getColumnIndex("uloha"))).asString();
			String outputName = getCellValue(row.getCell(mapper.getColumnIndex("vystup"))).asString();

			String status = getCellValue(row.getCell(mapper.getColumnIndex("stav"))).asString();
			String solver = getCellValue(row.getCell(mapper.getColumnIndex("riesitel"))).asString();
			int priority = getCellValue(row.getCell(mapper.getColumnIndex("priorita"))).asInt();
			int finishedInPercent = getCellValue(row.getCell(mapper.getColumnIndex("% dokoncenia"))).asInt(d -> d * 100);

			LocalDate from = parseLocalDateOrElseNull("od aktualny", row, mapper);
			LocalDate to = parseLocalDateOrElseNull("do aktualny", row, mapper);

			Activity activity = lookupActivity(activities, activityName);
			if (activity == null) {
				activity = new Activity(activityName);
				activity.setPositionNumber(positioner.incrementAndGetActivityPosition());
				activities.add(activity);
			}

			Phase phase = lookupPhase(activity, phaseName);
			if (phase == null) {
				phase = new Phase(phaseName);
				phase.setPositionNumber(positioner.incrementAndGetPhasePosition());
				activity.getPhases().add(phase);
			}

			Subactivity subactivity = lookupSubactivity(phase, subactivityName);
			if (subactivity == null) {
				subactivity = new Subactivity(subactivityName);
				subactivity.setPositionNumber(positioner.incrementAndGetSubactivityPosition());
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
			task.setPositionNumber(positioner.incrementAndGetTaskPosition());
			subactivity.getTasks().add(task);
		}

		workbook.close();

		return activities;
	}

	private LocalDate parseLocalDateOrElseNull(String fieldName, Row row, ColumnMapper mapper) {
		CellValue cellValue = getCellValue(row.getCell(mapper.getColumnIndex(fieldName)));
		return cellValue.isNull() || cellValue.isUndefined() ? null : cellValue.asLocalDate();
	}

	private Activity lookupActivity(List<Activity> activities, String activityName) {
		for (Activity activity : activities) {
			if (activity.getDescription().equals(activityName)) {
				return activity;
			}
		}
		return null;
	}

	private Phase lookupPhase(Activity activity, String phaseName) {
		for (Phase phase : activity.getPhases()) {
			if (phase.getDescription().equals(phaseName)) {
				return phase;
			}
		}
		return null;
	}

	private Subactivity lookupSubactivity(Phase phase, String subactivityName) {
		for (Subactivity subactivity : phase.getSubactivities()) {
			if (subactivity.getDescription().equals(subactivityName)) {
				return subactivity;
			}
		}
		return null;
	}

}
