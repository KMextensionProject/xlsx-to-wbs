package com.gti.xlsx;

import static com.gti.xlsx.XlsxUtils.getCellValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class ColumnMapper {

	private static final List<String> COLUMN_CODE_MAP = Collections.unmodifiableList(initColumnCodes());

	// having inverse map will provide constant time bidirectional map
	private final Map<String, Integer> nameIndexMap;
	private final Map<Integer, String> indexNameMap;

	/**
	 * Creates a bidirectional mapping for column names and its index positions.
	 * @param titleRow - row that contains column names
	 */
	public ColumnMapper(Row titleRow) {
		this.nameIndexMap = new HashMap<>(30);
		this.indexNameMap = new HashMap<>(30);
		init(titleRow);
	}

	private void init(Row titleRow) {
		for (Iterator<Cell> cellIterator = titleRow.cellIterator(); cellIterator.hasNext();) {
			Cell cell = cellIterator.next();
			String columnName = getCellValue(cell).asString();
			int columnIndex = cell.getColumnIndex();

			nameIndexMap.put(columnName, columnIndex);
			indexNameMap.put(columnIndex, columnName);
		}
	}

	private static List<String> initColumnCodes() {
		List<String> columnCodes = new ArrayList<>(26);
		char start = 'A';
		for (int i = 0; i < 26; i++) {
			columnCodes.add(start + "");
			start++;
		}
		return columnCodes;
	}

	public int getColumnIndex(String columnCodeOrName) {
		int index = COLUMN_CODE_MAP.indexOf(columnCodeOrName);
		if (index == -1) {
			return resolveColumnIndex(columnCodeOrName);
		}
		return index;
	}

	private int resolveColumnIndex(String columnName) {
		Integer index = nameIndexMap.get(columnName);
		if (index == null) {
			throw new NoMappingFound(columnName);
		}
		return index.intValue();	
	}

	public int getColumnIndex(String columnName, int orElse) {
		Integer index = nameIndexMap.get(columnName);
		if (index != null) {
			return index.intValue();
		}
		return orElse;
	}

	public String getColumnName(int columnIndex) {
		String columnName = indexNameMap.get(columnIndex);
		if (columnName == null) {
			throw new NoMappingFound(columnIndex);
		}
		return columnName;
	}

	public String getColumnName(int columnIndex, String orElse) {
		String columnName = indexNameMap.get(columnIndex);
		if (columnName != null) {
			return columnName;
		}
		return orElse;
	}
}
