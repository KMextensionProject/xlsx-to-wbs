package com.gti.xlsx;

import com.gti.util.StringUtils;

public class NoMappingFound extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String columnName;
	private final int columnIndex;

	public NoMappingFound(String forName) {
		this.columnName = forName;
		this.columnIndex = Integer.MIN_VALUE;
	}

	public NoMappingFound(int forIndex) {
		this.columnIndex = forIndex;
		this.columnName = StringUtils.EMPTY_STRING;
	}

	@Override
	public String getMessage() {
		StringBuilder message = new StringBuilder("Nie je možné namapovať stĺpec ");
		if (!columnName.isEmpty()) {
			message.append(columnName);
		} else if (columnIndex != Integer.MIN_VALUE) {
			message.append("index ")
				   .append(columnIndex);
		} else {
			message.append("<unknown>");
		}
		return message.toString();
	}
}
