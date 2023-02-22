package com.gti.util;

import static com.gti.util.StringUtils.stripDiacritics;
import static com.gti.util.XlsxUtils.getCellValue;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Row;

public class ColumnMapper {

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
			String columnName = stripDiacritics(getCellValue(cell).toLowerCase());
			int columnIndex = cell.getColumnIndex();

			// TODO: delete this, this is unwanted
			// let's skip unwanted columns
			Comment cellComment = cell.getCellComment();
			if (cellComment != null) {
				String comment = cellComment.getString().getString();
				if (comment.startsWith("@nowbs")) {
					continue;
				}
			}

			nameIndexMap.put(columnName, columnIndex);
			indexNameMap.put(columnIndex, columnName);
		}
	}

	public int getColumnIndex(String columnName) {
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
