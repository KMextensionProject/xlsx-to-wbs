package com.gti.util;

public class NoMappingFound extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String columnName;
	private int columnIndex;

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
		StringBuilder message = new StringBuilder("there is no mapping for column ");
		if (!columnName.isEmpty()) {
			message.append("name ")
				   .append(columnName);
		} else if (columnIndex != Integer.MIN_VALUE) {
			message.append("index ")
				   .append(columnIndex);
		} else {
			message.append("<unknown>");
		}
		return message.toString();
	}
}
