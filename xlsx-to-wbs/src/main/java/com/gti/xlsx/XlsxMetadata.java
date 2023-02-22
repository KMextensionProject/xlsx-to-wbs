package com.gti.xlsx;

public class XlsxMetadata {

	private int dataSheetIndex;
	private int titleRowIndex;

	public XlsxMetadata() {
		// zeroes are default
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
