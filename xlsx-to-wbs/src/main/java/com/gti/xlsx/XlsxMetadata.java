package com.gti.xlsx;

import java.io.File;

public class XlsxMetadata {

	private File file;
	private int dataSheetIndex;
	private int titleRowIndex;

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
