package com.gti.xlsx;

import java.io.File;
import java.util.List;

public class XlsxMetadata {

	private File file;
	private int dataSheetIndex;
	private int titleRowIndex;
	// these must not be empty and at least one must be present
	List<String> hierarchyColumns; // A, B, C, E
	// these may be empty, but they represent properties of the last column in the hierarchy
	List<String> propertyColumns; // D, F, M, AB, AS

	public XlsxMetadata() {
		// zeroes are default
	}

	public XlsxMetadata(File file) {
		this.file = file;
	}

	public XlsxMetadata (String fileLocation) {
		this.file = new File(fileLocation);
	}

	public void setFile(File file) {
		this.file = file;
	}

	public File getFile() {
		return this.file;
	}

	public void setDataSheetIndex(int dataSheetIndex) {
		this.dataSheetIndex = dataSheetIndex;
	}

	public int getDataSheetIndex() {
		return this.dataSheetIndex;
	}

	public void setTitleRowIndex(int titleRowIndex) {
		this.titleRowIndex = titleRowIndex;
	}

	public int getTitleRowIndex() {
		return this.titleRowIndex;
	}

}
