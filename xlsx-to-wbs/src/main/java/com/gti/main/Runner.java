package com.gti.main;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gti.util.WbsUtils;
import com.gti.util.XlsxUtils;

import net.sourceforge.plantuml.SourceStringReader;

public class Runner {

	// TODO: normalne, toto je len test
	public static void main(String[] args) throws IOException {
		// load data from excel
		Workbook workbook = new XSSFWorkbook(new FileInputStream("/home/UX/mkrajcovicux/Downloads/_musp_project_plan_v01_20230214.xlsx"));
		Sheet sheet = workbook.getSheetAt(0);
		// show me the title line
		Row titleRow = sheet.getRow(6);

		for (Iterator<Cell> iterator = titleRow.cellIterator(); iterator.hasNext();) {
			Cell cell = iterator.next();
			// if (cell.getColumnIndex() == 0) {
			// continue;
			// }
			System.out.print(XlsxUtils.getCellValue(cell) + ", ");
		}
		System.out.println();
		// load the underlying data
		for (Iterator<Row> rowIterator = sheet.rowIterator(); rowIterator.hasNext();) {
			Row row = rowIterator.next();
			if (row.getRowNum() < 7) {
				continue;
			}
			for (Iterator<Cell> cellIterator = row.cellIterator(); cellIterator.hasNext();) {
				Cell cell = cellIterator.next();
				String cellVal;
				// I don't need the generated ID
				if (cell.getColumnIndex() == 6) {
					double percent = Double.parseDouble(XlsxUtils.getCellValue(cell));
					cellVal = String.format("%.0f%%", percent * 100);
				} else {
					cellVal = XlsxUtils.getCellValue(cell);
				}
				System.out.print(cellVal + ", ");
			}
			System.out.println();
		}
		workbook.close();
		// ID,
		// Aktivita,
		// Podaktivita,
		// Fáza,
		// Nadradené ID,
		// Stav,
		// % Dokončenia,
		// Úloha,
		// Priorita,
		// Výstup,
		// Má akceptovať SO/RO (MIRRI),
		// Riešiteľ,
		// Od pôvodný,
		// Od aktuálny,
		// Do pôvodný,
		// Do aktuálny,
		// Poznámka.

		SourceStringReader reader = new SourceStringReader(WbsUtils.getSampleSource());
		String description = reader.outputImage(new FileOutputStream("/home/UX/mkrajcovicux/Documents/sample.png")).getDescription();
		if ("ERROR".equalsIgnoreCase(description)) {
			System.out.println("WBS to PNG generation failed");
		} else {
			System.out.println("generation successfull");
		}
	}

}
