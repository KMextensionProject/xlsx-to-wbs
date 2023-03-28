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

	/**
	 * Looks for the column index represented by the specified column code (one
	 * of [A-Z]) or one denoted by the column name as specified by the title
	 * row.
	 *
	 * @throws NoMappingFound
	 *             if the column code or name is not found.
	 */
	public int getColumnIndex(String columnCodeOrName) {
		int index = COLUMN_CODE_MAP.indexOf(columnCodeOrName);
		if (index == -1) {
			return getColumnIndexByName(columnCodeOrName);
		}
		return index;
	}

	private int getColumnIndexByName(String columnName) {
		Integer index = nameIndexMap.get(columnName);
		if (index == null) {
			throw new NoMappingFound(columnName);
		}
		return index.intValue();	
	}

	/**
	 * Looks for column name in title row mapping for the specified index.
	 *
	 * @throws NoMappingFound
	 *             if the column index is not mapped to any name from 
	 *             the title row.
	 */
	public String getColumnName(int columnIndex) {
		String columnName = indexNameMap.get(columnIndex);
		if (columnName == null) {
			throw new NoMappingFound(columnIndex);
		}
		return columnName;
	}
}
