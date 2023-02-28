package com.gti.xlsx;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

public class XlsxUtils {

	public static CellValue getCellValue(Cell cell) {
		Object cellValue;
		int cellType = cell.getCellType();
		if (cellType == Cell.CELL_TYPE_FORMULA) {
			cellType = cell.getCachedFormulaResultType();
		}
		switch (cellType) {
		case Cell.CELL_TYPE_BLANK:
			cellValue = null;
			break;
		case Cell.CELL_TYPE_STRING:
			cellValue = cell.getRichStringCellValue()
				.getString()
				.replace("\n", "")
				.trim();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
                cellValue = cell.getDateCellValue();
            } else {
                cellValue = cell.getNumericCellValue();
            }
			break;
		case Cell.CELL_TYPE_ERROR:
			cellValue = cell.getErrorCellValue();
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			cellValue = cell.getBooleanCellValue();
			break;
		default: 
			cellValue = null;
		}
		return new CellValue(cellValue);
	}

	public static class CellValue {

		private Object cellValue;
		private boolean isUndefined;
		private boolean isNull;

		public CellValue(Object value) {
			if (value == null) {
				this.isNull = true;
			}
			if ("N/A".equals(value)) {
				this.isUndefined = true;
			}
			cellValue = value;
		}

		public boolean isNull() {
			return this.isNull;
		}

		public boolean isUndefined() {
			return this.isUndefined;
		}

		public String asString() {
			return String.valueOf(cellValue);
		}

		public int asInt() {
			if (cellValue instanceof String) {
				return Double.valueOf((String) cellValue).intValue();
			} else if (cellValue instanceof Number) {
				return ((Number) cellValue).intValue();
			}
			throw new UnsupportedOperationException(cellValue + " cannot be converted to int");
		}

		/**
		 * It's enough to provide mapping function to alter original double
		 * value. This method automatically extracts the integer value throwing
		 * away all fractional digits.
		 */
		public int asInt(UnaryOperator<Double> formattingFunction) {
			return formattingFunction.apply(new CellValue(cellValue).asDouble()).intValue();
		}

		public double asDouble() {
			if (cellValue instanceof String) {
				return Double.valueOf((String) cellValue).intValue();
			} else if (cellValue instanceof Number) {
				return ((Number) cellValue).doubleValue();
			}
			throw new UnsupportedOperationException(cellValue + " cannot be converted to double");
		}

		public LocalDate asLocalDate() {
			return asLocalDate(null);
		}

		public LocalDate asLocalDate(DateTimeFormatter format) {
			try {
				if (cellValue instanceof String) {
					String dateValue = (String) cellValue;
					if (format != null) {
						return LocalDate.parse(dateValue, format);
					}
					return LocalDate.parse(dateValue);
				} else if (cellValue instanceof java.util.Date) {
					return new java.sql.Date(((java.util.Date) cellValue).getTime()).toLocalDate();
				}
			} catch (DateTimeParseException parsingError) {
				// parsingError skipped in favor of UnsupportedOperationException
			}
			throw new UnsupportedOperationException(cellValue + " cannot be converted to LocalDate");
		}

		public Object asObject() {
			return this.cellValue;
		}

		public <T> T conditionally(Predicate<T> condition, T trueValue, T falseValue) {
			if (condition == null) {
				throw new IllegalArgumentException("Condition can not be null");
			}
			return condition.test(trueValue) ? trueValue : falseValue;
		}

		public <T, R> R customized(Function<T, R> mappingFunction) { 
			return mappingFunction.apply(null);
		}
	}
}
