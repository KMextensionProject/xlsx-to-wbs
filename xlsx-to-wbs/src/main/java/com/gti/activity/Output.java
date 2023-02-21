package com.gti.activity;

import static com.gti.util.StringUtils.EMPTY_STRING;

public class Output {

	private String name;
	private Boolean value;

	public String getName() {
		return name;
	}

	public Boolean getValue() {
		return value;
	}

	public String getValueAsText() {
		if (value == null) {
			return EMPTY_STRING;
		}
		return value ? "Áno" : "Nie";
	}

	public boolean isEmpty() {
		return name == null;
	}
}
