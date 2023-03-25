package com.gti.util;

import java.text.Normalizer;
import java.text.Normalizer.Form;

public class StringUtils {

	public static final String EMPTY_STRING = "";

	public static String stripDiacritics(String input) {
		return Normalizer.normalize(input, Form.NFD)
			.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}

	public static String repeat(String what, int howMany) {
		StringBuilder result = new StringBuilder(what);
		for (int i = 1; i < howMany; i++) {
			result.append(what);
		}
		return result.toString();
	}

}
