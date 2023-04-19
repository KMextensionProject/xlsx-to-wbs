package com.gti.enums;

import java.time.format.DateTimeFormatter;

public class DateFormat {

	private DateFormat() {
		throw new IllegalStateException("This utility class was not designed to be instantiated");
	}

	public static final DateTimeFormatter SLOVAK_DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

}
