package com.gti.xlsx;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * this holder describes excel meta data as defined by user..
 */
public class XlsxMetadata {

	private File file;
	private int dataSheetIndex;
	private int titleRowIndex;
	private List<ColumnProperty> parentColumnsProperties;
	private List<ColumnProperty> propertyColumnsProperties;

	public XlsxMetadata() {
		parentColumnsProperties = new ArrayList<>();
		propertyColumnsProperties = new ArrayList<>();
	}

	public XlsxMetadata(File file) {
		this();
		this.file = file;
	}

	public XlsxMetadata (String fileLocation) {
		this();
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

	public List<ColumnProperty> getParentColumnProperties() {
		return new ArrayList<>(parentColumnsProperties);
	}

	public List<ColumnProperty> getPropertyColumnProperties() {
		return new ArrayList<>(propertyColumnsProperties);
	}

	public void setParentColumnsProperties(List<ColumnProperty> parentCols) {
		if (parentColumnsProperties != null) {
			if (this.parentColumnsProperties.isEmpty()) {
				this.parentColumnsProperties.addAll(parentCols);
			} else {
				this.parentColumnsProperties = new ArrayList<>(parentCols);
			}
		}
	}

	public void setPropertyColumnsProperties(List<ColumnProperty> propertyCols) {
		if (propertyCols != null) {
			if (this.propertyColumnsProperties.isEmpty()) {
				this.propertyColumnsProperties.addAll(propertyCols);
			} else {
				this.propertyColumnsProperties = new ArrayList<>(propertyCols);
			}
		}
	}
}
