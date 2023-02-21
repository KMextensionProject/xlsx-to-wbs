package com.gti.util;

import org.apache.poi.ss.usermodel.Cell;

public class XlsxUtils {

	// TODO: dat tento bordel do poradku

	public static String getCellValue(Cell cell) {
		return getCellValue(cell, null);
	}

	public static String getCellValue(Cell cell, String ending) {
		String cellValue;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_FORMULA:
			switch (cell.getCachedFormulaResultType()) {
			case Cell.CELL_TYPE_STRING:
				cellValue = cell.getRichStringCellValue().getString();
				if (ending != null) {
					cellValue += ending;
				}
				break;
			case Cell.CELL_TYPE_NUMERIC:
				cellValue = String.valueOf(cell.getNumericCellValue());
				break;
			default:
				cellValue = "";
				break;
			}
			break;
		case Cell.CELL_TYPE_STRING:
			cellValue = cell.getStringCellValue().replace(System.lineSeparator(), "");
			if (ending != null) {
				cellValue += ending;
			}
			break;
		case Cell.CELL_TYPE_NUMERIC:
			cellValue = String.valueOf(cell.getNumericCellValue());
			break;
		case Cell.CELL_TYPE_ERROR:
			cellValue = String.valueOf(cell.getErrorCellValue());
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			cellValue = String.valueOf(cell.getBooleanCellValue());
			break;
		default:
			cellValue = "";
		}
		return cellValue;
	}

}
