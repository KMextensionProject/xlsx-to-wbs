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

// to generalize the whole tool...I should use maps instead of pojos since, the hierarchy level can vary
public class ActivityLoader {

	// A -> first column in first sheet
	// 2:C -> third column in the second sheet
	// D:% -> fourth column in the first sheet should be displayed as percentage

	// TODO: keys should be the values of the colum, so I could use them as the labels for task properties
	// TODO: this is the place where I can add positioner
	// TODO: split into smaller methods
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
			List<String> parentColumns = xlsxMeta.getParentColumns();
			Map<String, Object> parent = activities;
			String value = null;
			for (int j = 0; j < parentColumns.size(); j++) {
				String columnCode = parentColumns.get(j);
				// should the mapper's method be generic for name and code? ..it would be useful for user maybe
				value = getCellValue(row.getCell(mapper.getColumnIndex(columnCode))).asString(); // better value resolving.. for testing let it be string

				if (!parent.containsKey(value)) {
					parent.put(value, new LinkedHashMap<>());
				}
				// I need the last reference later, so omit the value extraction in the last iteration
				if (!(j == parentColumns.size() - 1)) {
					parent = (Map<String, Object>) parent.get(value);
				}
			}

			// process properties
			Map<String, Object> taskData = new LinkedHashMap<>();
			for (String columnCode : xlsxMeta.getPropertyColumns()) {
				int columnIndex = mapper.getColumnIndex(columnCode);
				String taskValue = getCellValue(row.getCell(columnIndex)).asString();
				taskData.put(mapper.getColumnName(columnIndex), taskValue);
			}

//			Object parentContent = parent.get(value);
//			if (parentContent instanceof Map) {
//				List<Map<String, Object>> taskList = new ArrayList<>();
//				taskList.add(taskData);
//				parent.put(value, taskList);
//			} else if (parentContent instanceof List) {
//				((List<Map<String, Object>>)parentContent).add(taskData);
//			}
			
			Object parentContent = parent.get(value);
			if (parentContent instanceof Map) {
				Set<Map<String, Object>> taskList = new LinkedHashSet<>();
				taskList.add(taskData);
				parent.put(value, taskList);
			} else if (parentContent instanceof Set) {
				((Set<Map<String, Object>>)parentContent).add(taskData);
			}
		}

		workbook.close();

		return activities;
	}

}
