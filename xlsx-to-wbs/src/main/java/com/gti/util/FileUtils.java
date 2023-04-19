package com.gti.util;

import java.io.File;

import net.sourceforge.plantuml.FileFormat;

public class FileUtils {

	private FileUtils() {
		throw new IllegalStateException("This utility class was not designed to be instantiated");
	}

	public static String appendFileNameWithExtension(String dir, String fileName, FileFormat fileFormat) {
		return new StringBuilder(dir)
			.append(File.separator)
			.append(fileName)
			.append(fileFormat.getFileSuffix())
			.toString();
	}

	public static String overrideFileExtensionIfDifferent(String file, FileFormat fileFormat) {
		int dotSuffixIndex = file.lastIndexOf('.');
		if (dotSuffixIndex == -1) {
			return appendFileExtension(file, fileFormat);
		}
		String suffix = file.substring(dotSuffixIndex);
		if (!suffix.equalsIgnoreCase(fileFormat.getFileSuffix())) {
			return file.replace(suffix, fileFormat.getFileSuffix());
		}
		return file;
	}

	public static String appendFileExtension(String file, FileFormat fileFormat) {
		return new StringBuilder(file)
			.append(fileFormat.getFileSuffix())
			.toString();
	}

}
