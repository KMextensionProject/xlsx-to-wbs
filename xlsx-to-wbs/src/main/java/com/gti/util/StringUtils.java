package com.gti.util;

import java.text.Normalizer;
import java.text.Normalizer.Form;

public class StringUtils {

	public static final String EMPTY_STRING = "";

	public static String stripDiacritics(String input) {
		return Normalizer.normalize(input, Form.NFD)
			.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}

}
