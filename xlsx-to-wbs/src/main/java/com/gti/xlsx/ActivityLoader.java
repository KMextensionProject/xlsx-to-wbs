package com.gti.xlsx;

import static com.gti.xlsx.XlsxUtils.getCellValue;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gti.enums.DateFormat;
import com.gti.xlsx.XlsxUtils.CellValue;

public class ActivityLoader {

	// A -> first column in first sheet
	// D% -> fourth column in the first sheet should be displayed as percentage

	/**
	 * Loads the activities data from XLSX file and forms the desired hierarchy as
	 * described by XlsxMetadata object.
	 *
	 * @param xlsxMeta - description object of how to construct hierachy from XLSX
	 *                 columns and how name columns by the title row
	 * @return
	 * @throws IOException - if there is some problem reading XLSX file
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> loadFromXlsx (XlsxMetadata xlsxMeta) throws IOException {
		Workbook workbook = new XSSFWorkbook(new FileInputStream(xlsxMeta.getFile()));
		Sheet sheet = workbook.getSheetAt(xlsxMeta.getDataSheetIndex());
		Row titleRow = sheet.getRow(xlsxMeta.getTitleRowIndex());
		ColumnMapper mapper = new ColumnMapper(titleRow);

		Map<String, Object> activities = new LinkedHashMap<>();
		for (int i = titleRow.getRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);

			// process main hierarchy
			List<ColumnProperty> parentColumns = xlsxMeta.getParentColumnProperties();
			Map<String, Object> parent = activities;
			String value = null;
			for (int j = 0; j < parentColumns.size(); j++) {
				String columnCode = parentColumns.get(j).getLocationCode();
				value = getCellValue(row.getCell(mapper.getColumnIndex(columnCode))).asString(); // parents are always string
				if (!parent.containsKey(value)) {
					parent.put(value, new LinkedHashMap<>());
				}
				// I need the last reference later, so omit the value extraction in the last iteration
				if (j != parentColumns.size() - 1) {
					parent =  (Map<String, Object>) parent.get(value);
				}
			}

			// process properties
			if (!xlsxMeta.getPropertyColumnProperties().isEmpty()) {
				Map<String, Object> taskData = new LinkedHashMap<>();
				for (ColumnProperty columnProperty : xlsxMeta.getPropertyColumnProperties()) {
					String columnCode = columnProperty.getLocationCode();
					int columnIndex = mapper.getColumnIndex(columnCode);
					CellValue cellValue = getCellValue(row.getCell(columnIndex));
					Object taskValue = resolveTaskValue(cellValue, columnProperty);
					taskData.put(mapper.getColumnName(columnIndex), taskValue);
				}

				Object parentContent = parent.get(value);
				if (parentContent instanceof Map) {
					Set<Map<String, Object>> taskList = new LinkedHashSet<>();
					taskList.add(taskData);
					parent.put(value, taskList);
				} else if (parentContent instanceof Set) {
					((Set<Map<String, Object>>) parentContent).add(taskData);
				}
			}
		}

		workbook.close();

		return activities;
	}

	private Object resolveTaskValue(CellValue cellValue, ColumnProperty columnProperty) {
		if (cellValue.isDate()) {
			// TODO: let user define format according to what excel supports
			return cellValue.asLocalDate().format(DateFormat.SLOVAK_DATE_FORMAT);
		} else if (cellValue.isNumeric()) {
			return columnProperty.hasPercentageIndicator() 
					? cellValue.asInt(e -> e * 100) + "" 
					: cellValue.asInt() + "";
		}
		else {
			return cellValue.asString();
		}
	}

}
